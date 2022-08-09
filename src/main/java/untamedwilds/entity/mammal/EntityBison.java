package untamedwilds.entity.mammal;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.target.ProtectChildrenTarget;
import untamedwilds.entity.ai.target.SmartOwnerHurtTargetGoal;
import untamedwilds.entity.ai.unique.BisonTerritorialityFight;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModSounds;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class EntityBison extends ComplexMobTerrestrial implements INewSkins, ISpecies, IPackEntity {

   private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(EntityBison.class, EntityDataSerializers.BOOLEAN);

    public static Animation ATTACK_THREATEN;
    public static Animation ATTACK_GORE;

    public EntityBison(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
        ATTACK_THREATEN = Animation.create(50);
        ATTACK_GORE = Animation.create(14);
        this.maxUpStep = 1F;
        this.turn_speed = 0.2F;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CHARGING, false);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal_Land(this));
        this.goalSelector.addGoal(2, new RodeoGoal(this, 1.8F));
        this.goalSelector.addGoal(2, new MeleeAttackCharger(this, 1.4F, 3));
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 1.6D, false));
        this.goalSelector.addGoal(3, new SmartMateGoal(this, 0.8D));
        this.goalSelector.addGoal(3, new GrazeGoal(this, 10));
        this.goalSelector.addGoal(4, new GotoSleepGoal(this, 1D));
        this.goalSelector.addGoal(5, new BisonTerritorialityFight(this, 1.4F));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, 120, 0, true));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ProtectChildrenTarget<>(this, LivingEntity.class, true, input -> !(input instanceof EntityBison) && getEcoLevel(input) > getEcoLevel(this)));
    }

    @Override
    protected void reassessTameGoals() {
        if (this.isTame()) {
            if (UntamedWilds.DEBUG) {
                UntamedWilds.LOGGER.info("Updating AI tasks for tamed mob");
            }
            this.goalSelector.addGoal(3, new SmartFollowOwnerGoal(this, 1.3D, 12.0F, 3.0F));
            this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
            this.targetSelector.addGoal(2, new SmartOwnerHurtTargetGoal(this));
        }
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 12.0D)
                .add(Attributes.MAX_HEALTH, 35.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1D)
                .add(Attributes.ARMOR, 2D);
    }

    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.age == 0) {
            return this.getHunger() >= 80;
        }
        return false;
    }

    @Override
    public void aiStep() {
        if (!this.level.isClientSide) {
            if (this.herd == null) {
                IPackEntity.initPack(this);
            }
            else {
                this.herd.tick();
            }
            if (this.level.getGameTime() % 1000 == 0) {
                this.addHunger(-10);
                if (!this.isStarving()) {
                    this.heal(1.0F);
                }
            }
            int i = this.random.nextInt(3000);
            if (i == 13 && !this.isInWater() && this.getTarget() == null && this.isNotMoving() && this.canMove() && this.getAnimation() == NO_ANIMATION) {
                this.setSitting(true);
            }
            if (i == 14 && this.isSitting()) {
                this.setSitting(false);
            }
            this.setAngry(this.getTarget() != null);
        }
        else {
            if (this.getAnimation() == ATTACK_THREATEN) {
                this.setSprinting(this.getAnimationTick() % 18 < 6);
            }
        }
        super.aiStep();
    }

    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag && this.getAnimation() == NO_ANIMATION && !this.isBaby()) {
            Animation anim = chooseAttackAnimation();
            this.setAnimation(anim);
            if (!this.isCharging()) {
                this.playSound(SoundEvents.ZOGLIN_ATTACK, 1.0F, this.getVoicePitch());
                HoglinBase.hurtAndThrowTarget(this, (LivingEntity)entityIn);
            }
        }
        return flag;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.RAVAGER_STEP, 0.15F, 1.0F);
    }

    private Animation chooseAttackAnimation() {
        return ATTACK_GORE;
    }

    @Nullable
    public EntityBison getBreedOffspring(ServerLevel serverWorld, AgeableMob ageable) {
        return create_offspring(new EntityBison(ModEntity.BISON.get(), this.level));
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (hand == InteractionHand.MAIN_HAND && !this.level.isClientSide()) {
            if (!this.isTame() && this.isBaby() && EntityUtils.hasFullHealth(this) && this.isFood(itemstack)) {
                this.playSound(SoundEvents.HORSE_EAT, 1.5F, 0.8F);
                if (this.getRandom().nextInt(3) == 0) {
                    this.tame(player);
                    EntityUtils.spawnParticlesOnEntity(this.level, this, ParticleTypes.HEART, 3, 6);
                } else {
                    EntityUtils.spawnParticlesOnEntity(this.level, this, ParticleTypes.SMOKE, 3, 3);
                }
            }
            if (!this.isTame() && !this.isBaby() && itemstack.isEmpty()) {
                this.setSitting(false);
                this.setSleeping(false);
                if (!this.level.isClientSide) {
                    player.setYRot(this.getYRot());
                    player.setXRot(this.getXRot());
                    player.startRiding(this);
                }
            }
        }

        return super.mobInteract(player, hand);
    }

    public boolean isCharging() {
        return entityData.get(CHARGING);
    }

    public void setCharging(boolean bool) { entityData.set(CHARGING, bool); }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ATTACK_THREATEN, ATTACK_GORE};
    }

    public Animation getAnimationEat() { return NO_ANIMATION; }

    public static class RodeoGoal extends Goal {
        private final EntityBison bison;
        private final double speedModifier;
        private double posX;
        private double posY;
        private double posZ;

        public RodeoGoal(EntityBison p_25890_, double p_25891_) {
            this.bison = p_25890_;
            this.speedModifier = p_25891_;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (!this.bison.isTame() && this.bison.isVehicle()) {
                Vec3 vec3 = DefaultRandomPos.getPos(this.bison, 5, 4);
                if (vec3 == null) {
                    return false;
                } else {
                    this.posX = vec3.x;
                    this.posY = vec3.y;
                    this.posZ = vec3.z;
                    return true;
                }
            } else {
                return false;
            }
        }

        public void start() {
            this.bison.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
        }

        public boolean canContinueToUse() {
            return !this.bison.isTame() && !this.bison.getNavigation().isDone() && this.bison.isVehicle();
        }

        public void tick() {
            if (this.bison.getRandom().nextInt(this.adjustedTickDelay(50)) < 3) {
                this.bison.setAnimation(ATTACK_GORE);
                this.bison.playSound(ModSounds.ENTITY_BISON_AMBIENT, this.bison.getSoundVolume(), this.bison.getVoicePitch());
            }
            if (!this.bison.isTame() && this.bison.getRandom().nextInt(this.adjustedTickDelay(50)) == 0) {
                Entity entity = this.bison.getPassengers().get(0);
                if (entity == null) {
                    return;
                }

                this.bison.ejectPassengers();
                this.bison.setTarget((LivingEntity) entity);
                this.bison.level.broadcastEntityEvent(this.bison, (byte)6);
            }
        }
    }
}
