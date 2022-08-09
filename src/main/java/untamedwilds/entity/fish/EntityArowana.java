package untamedwilds.entity.fish;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAquatic;
import untamedwilds.entity.INewSkins;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.FishBreachGoal;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntityArowana extends ComplexMobAquatic implements ISpecies, INewSkins {

    public EntityArowana(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0D)
                .add(Attributes.MOVEMENT_SPEED, 0.8D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MAX_HEALTH, 6.0D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new SwimGoal(this));
        this.goalSelector.addGoal(4, new FishBreachGoal(this, 400, true));
    }

    public void aiStep() {
        if (!this.level.isClientSide) {
            if (this.tickCount % 1000 == 0) {
                if (this.wantsToBreed() && !this.isMale()) {
                    this.breed();
                }
            }
            if (this.level.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
        }

        // Coerces the Arowana to stay at the surface
        if (this.getTarget() == null && this.isInWater() && this.tickCount % 10 == 0) {
            if (level.isWaterAt(this.blockPosition().above(2))) {
                this.setDeltaMovement(this.getDeltaMovement().add(0, 0.1F, 0));
            }
        }
        super.aiStep();
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (hand == InteractionHand.MAIN_HAND) {
            if (itemstack.getItem().equals(Items.WATER_BUCKET) && this.isAlive()) {
                EntityUtils.mutateEntityIntoItem(this, player, hand, "bucket_arowana", itemstack);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        }
        return super.mobInteract(player, hand);
    }

    /* Breeding conditions for the Arowana are:
     * A nearby Arowana of different gender */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
            List<EntityArowana> list = this.level.getEntitiesOfClass(EntityArowana.class, this.getBoundingBox().inflate(12.0D, 8.0D, 12.0D));
            list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
            if (list.size() >= 1) {
                this.setAge(this.getAge());
                list.get(0).setAge(this.getAge());
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        EntityUtils.dropEggs(this, "egg_arowana", this.getOffspring());
        return null;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }
}
