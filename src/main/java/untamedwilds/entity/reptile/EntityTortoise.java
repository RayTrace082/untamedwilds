package untamedwilds.entity.reptile;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobTerrestrial;
import untamedwilds.entity.INewSkins;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.SmartMeleeAttackGoal;
import untamedwilds.entity.ai.SmartSwimGoal_Land;
import untamedwilds.entity.ai.SmartWanderGoal;
import untamedwilds.entity.ai.unique.TortoiseHideInShellGoal;
import untamedwilds.init.ModItems;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntityTortoise extends ComplexMobTerrestrial implements ISpecies, INewSkins {

    public EntityTortoise(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
        this.ticksToSit = 20;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2D)
                .add(Attributes.ARMOR, 10D);
    }

    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SmartSwimGoal_Land(this));
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 1D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 0.7D));
        this.goalSelector.addGoal(2, new TortoiseHideInShellGoal<>(this, LivingEntity.class, 7, input -> getEcoLevel(input) > getEcoLevel(this)));
        this.goalSelector.addGoal(3, new SmartWanderGoal(this, 1.0D, 400, 0,true));
        //this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public void die(DamageSource cause) {
        if (cause == DamageSource.ANVIL && !this.isBaby()) {
            // Advancement Trigger: "Unethical Soup"
            ItemEntity entityitem = this.spawnAtLocation(new ItemStack(ModItems.FOOD_TURTLE_SOUP.get()), 0.2F);
            if (entityitem != null) {
                entityitem.getItem().setCount(1);
            }
        }
        super.die(cause);
    }

    public void aiStep() {
        super.aiStep();

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
    }

    /* Breeding conditions for the tortoise are:
     * A nearby tortoise of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
                List<EntityTortoise> list = this.level.getEntitiesOfClass(EntityTortoise.class, this.getBoundingBox().inflate(6.0D, 4.0D, 6.0D));
                list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
                if (list.size() >= 1) {
                    this.setAge(this.getPregnancyTime());
                    list.get(0).setAge(this.getPregnancyTime());
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        EntityUtils.dropEggs(this, "egg_tortoise", this.getOffspring());
        return null;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (itemstack.isEmpty() && this.isAlive()) {
            EntityUtils.turnEntityIntoItem(this, "spawn_tortoise");
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    public boolean hurt(DamageSource source, float amount) {
        if (source != DamageSource.FALL && this.sitProgress > 0) {
            amount = amount * 0.2F;
        }
        return super.hurt(source, amount);
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        SoundEvent soundevent = this.isBaby() ? SoundEvents.TURTLE_SHAMBLE_BABY : SoundEvents.TURTLE_SHAMBLE;
        this.playSound(soundevent, 0.15F, 1.0F);
    }
}
