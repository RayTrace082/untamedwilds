package untamedwilds.entity.fish;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
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
import oshi.util.tuples.Pair;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAquatic;
import untamedwilds.entity.INewSkins;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.unique.CatfishGarbageBinGoal;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntityCatfish extends ComplexMobAquatic implements ISpecies, INewSkins {

    public Pair<Float, Float> whisker_offset;
    private float whisker_y = 0;
    private float whisker_z = 0;

    public EntityCatfish(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
        this.whisker_offset = new Pair<>(0F, 0F);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.3D)
                .add(Attributes.MOVEMENT_SPEED, 0.65D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MAX_HEALTH, 8.0D);
    }

    protected void registerGoals() {
        //super.registerGoals();
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new CatfishGarbageBinGoal(this, 6, 100));
        this.goalSelector.addGoal(4, new SwimGoal(this, 3));
    }

    public void aiStep() {
        if (!this.level.isClientSide) {
            if (this.isInWater()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.003D, 0.0D));
            }

            if (this.tickCount % 1000 == 0) {
                if (this.wantsToBreed() && !this.isMale()) {
                    this.breed();
                }
            }
            if (this.level.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
        }
        if (this.tickCount % 120 < 11) {
            if (this.tickCount % 120 == 1) {
                whisker_y = 0.8F - this.random.nextFloat() * 1.6F;
                whisker_z = 0.4F - this.random.nextFloat() * 0.8F;
            }
            this.whisker_offset = new Pair<>(Mth.lerp(0.1F, whisker_offset.getA(), whisker_y), Mth.lerp(0.1F, whisker_offset.getB(), whisker_z));
        }
        super.aiStep();
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (hand == InteractionHand.MAIN_HAND) {
            if (itemstack.getItem().equals(Items.WATER_BUCKET) && this.isAlive()) {
                EntityUtils.mutateEntityIntoItem(this, player, hand, "bucket_catfish", itemstack);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        }
        return super.mobInteract(player, hand);
    }

    /* Breeding conditions for the Trevally are:
     * A nearby Trevally of different gender */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
            List<EntityCatfish> list = this.level.getEntitiesOfClass(EntityCatfish.class, this.getBoundingBox().inflate(12.0D, 8.0D, 12.0D));
            list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
            if (list.size() >= 1) {
                this.setAge(this.getPregnancyTime());
                list.get(0).setAge(this.getPregnancyTime());
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        EntityUtils.dropEggs(this, "egg_catfish", this.getOffspring());
        return null;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }
}
