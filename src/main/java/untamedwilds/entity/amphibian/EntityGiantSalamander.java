package untamedwilds.entity.amphibian;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntityGiantSalamander extends ComplexMobAmphibious implements ISpecies, INeedsPostUpdate, INewSkins {

    public static Animation ATTACK_SWALLOW;
    public int swimProgress;

    public EntityGiantSalamander(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new ComplexMobAquatic.MoveHelperController(this, 0.6F);
        ATTACK_SWALLOW = Animation.create(15);
        this.experienceValue = 1;
        this.swimSpeedMult = 3;
        this.buoyancy = 0.992F;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.16D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 8.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 10.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0D);
    }

    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 1.4D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 0.7D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, (float)this.getAttributeValue(Attributes.FOLLOW_RANGE), 1D, 1.1D, input -> getEcoLevel(input) > 6));
        this.goalSelector.addGoal(3, new AmphibiousTransition(this, 1D));
        this.goalSelector.addGoal(4, new AmphibiousRandomSwimGoal(this, 0.7, 600));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, false, input -> getEcoLevel(input) < 6));
    }

    public boolean wantsToLeaveWater() { return this.world.isRainingAt(this.getPosition()); }

    public boolean wantsToEnterWater() { return true; }

    public boolean isPushedByWater() {
        return false;
    }

    public void livingTick() {
        super.livingTick();

        if (!this.world.isRemote) {
            if (this.ticksExisted % 1000 == 0) {
                if (this.wantsToBreed() && !this.isMale()) {
                    this.breed();
                }
            }
            if (this.world.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
        }
        else {
            if (this.isInWater() && !this.isOnGround() && this.swimProgress < 20) {
                this.swimProgress++;
            } else if ((!this.isInWater() || this.isOnGround()) && this.swimProgress > 0) {
                this.swimProgress--;
            }
        }
    }

    /* Breeding conditions for the giant salamander are:
     * A nearby giant salamander of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this)) {
                List<EntityGiantSalamander> list = this.world.getEntitiesWithinAABB(EntityGiantSalamander.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
                list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
                if (list.size() >= 1) {
                    this.setGrowingAge(this.getGrowingAge());
                    list.get(0).setGrowingAge(this.getGrowingAge());
                    return true;
                }
            }
        }
        return false;
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        float f = (float)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
        if (flag) {
            if (entityIn instanceof LivingEntity && entityIn.getWidth() * entityIn.getHeight() < 0.4F && (entityIn instanceof TameableEntity && !((TameableEntity) entityIn).isTamed())) {
                this.setAnimation(ATTACK_SWALLOW);
                this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.BLOCK_BEEHIVE_ENTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
                EntityUtils.spawnParticlesOnEntity(this.world, (LivingEntity)entityIn, ParticleTypes.POOF, 6, 2);
                this.setMotion(new Vector3d(entityIn.getPosX() - this.getPosX(), entityIn.getPosY() - this.getPosY(), entityIn.getPosZ() - this.getPosZ()).scale(0.15F));
                this.peacefulTicks = 12000;
                entityIn.remove();
            }
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        EntityUtils.dropEggs(this, "egg_giant_salamander", this.getOffspring());
        return null;
    }

    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);
        if (hand == Hand.MAIN_HAND) {
            if (itemstack.getItem().equals(Items.WATER_BUCKET) && this.isAlive()) {
                EntityUtils.mutateEntityIntoItem(this, player, hand, "bucket_giant_salamander", itemstack);
                return ActionResultType.func_233537_a_(this.world.isRemote);
            }
        }
        return super.func_230254_b_(player, hand);
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        SoundEvent soundevent = this.isChild() ? SoundEvents.ENTITY_TURTLE_SHAMBLE_BABY : SoundEvents.ENTITY_TURTLE_SHAMBLE;
        this.playSound(soundevent, 0.15F, 1.0F);
    }

    public Animation[] getAnimations() { return new Animation[]{NO_ANIMATION, ATTACK_SWALLOW}; }

    @Override
    public void updateAttributes() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getAttack());
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getHealth());
        this.setHealth(this.getMaxHealth());
    }
}
