package untamedwilds.entity;

import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import untamedwilds.UntamedWilds;

import javax.annotation.Nullable;

public abstract class ComplexMobAquatic extends ComplexMob {

    public ComplexMobAquatic(EntityType<? extends ComplexMob> entity, Level worldIn) {
        super(entity, worldIn);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    @Override
    public void onInsideBubbleColumn(boolean downwards) {}

    public boolean canBreatheUnderwater() {
        return true;
    }

    public MobType getMobType() {
        return MobType.WATER;
    }

    protected float getStandingEyeHeight(Pose p_213348_1_, EntityDimensions p_213348_2_) {
        return p_213348_2_.height * 0.2F;
    }

    public void baseTick() {
        int i = this.getAirSupply();
        super.baseTick();
        this.updateAir(i);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
    }

    protected PathNavigation createNavigation(Level worldIn) {
        return new WaterBoundPathNavigation(this, worldIn);
    }

    public void aiStep() {
        if (!this.isInWater() && this.isOnGround() && this.verticalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().add(((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F), 0.4000000059604645D, ((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F)));
            this.setOnGround(false);
            this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
        }

        super.aiStep();
    }

    protected void updateAir(int air) {
        if (this.isAlive() && !this.isInWaterOrBubble()) {
            this.setAirSupply(air - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(DamageSource.DROWN, 2.0F);
            }
        } else {
            this.setAirSupply(300);
        }
    }

    public boolean isPushedByFluid() {
        return false;
    }

    protected abstract SoundEvent getFlopSound();

    protected SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    public void travel(Vec3 movement) {
        if (!this.level.isClientSide() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), movement);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(movement);
        }
    }

    /*public static class MoveHelperController extends MoveControl {
        private final ComplexMob entity;
        private final float landMoveFactor;

        public MoveHelperController(ComplexMob entity) {
            super(entity);
            this.entity = entity;
            this.landMoveFactor = 0.1F;
        }

        public MoveHelperController(ComplexMob entity, float landMoveFactor) {
            super(entity);
            this.entity = entity;
            this.landMoveFactor = landMoveFactor;
        }

        public void tick() {
            if (this.entity.isInWater()) {
                this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(0.0D, 0.0045D, 0.0D));
            }

            if (this.action == MovementController.Action.MOVE_TO && !this.entity.getNavigation().isDone()) {
                double d0 = this.getX() - this.entity.getX();
                double d1 = this.getY() - this.entity.getY();
                double d2 = this.getZ() - this.entity.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (d3 < (double)2.5000003E-7F) {
                    this.mob.setMoveForward(0.0F);
                } else {
                    float f = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                    this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f, this.entity.turn_speed * 10);
                    this.entity.renderYawOffset = this.entity.rotationYaw;
                    this.entity.rotationYawHead = this.entity.rotationYaw;
                    float f1 = (float)(this.speed * this.entity.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
                    if (this.entity.isInWater()) {
                        this.entity.setAIMoveSpeed(f1 * 0.02F);
                        float f2 = -((float)(Mth.atan2(d1, Mth.sqrt(d0 * d0 + d2 * d2)) * (double)(180F / (float)Math.PI)));
                        f2 = Mth.clamp(Mth.wrapDegrees(f2), -85.0F, 85.0F);
                        this.entity.getXRot() = this.limitAngle(this.entity.getXRot(), f2, 5.0F);
                        float f3 = Mth.cos(this.entity.getXRot() * ((float)Math.PI / 180F));
                        float f4 = Mth.sin(this.entity.getXRot() * ((float)Math.PI / 180F));
                        this.entity.moveForward = f3 * f1;
                        this.entity.moveVertical = -f4 * f1;
                    } else {
                        this.entity.setAIMoveSpeed(f1 * landMoveFactor);
                    }
                }
            } else {
                this.entity.setAIMoveSpeed(0.0F);
                this.entity.setMoveStrafing(0.0F);
                this.entity.setMoveVertical(0.0F);
                this.entity.setMoveForward(0.0F);
            }
        }
    }*/

    protected static class SwimGoal extends RandomSwimmingGoal {

        public int heightFromBottom;

        public SwimGoal(ComplexMobAquatic entity) {
            this(entity, -1);
        }

        public SwimGoal(ComplexMobAquatic entity, int offset) {
            super(entity, 1.0D, 20);
            this.heightFromBottom = offset;
        }

        public boolean canUse() {
            return super.canUse();
        }

        @Nullable
        protected Vec3 getPosition() {
            Vec3 vector3d = BehaviorUtils.getRandomSwimmablePos(this.mob, 10, 7);

            //UntamedWilds.LOGGER.info((vector3d != null) + " " + (this.heightFromBottom > 0) + " " + this.mob.level.canSeeSkyFromBelowWater(this.mob.blockPosition()));
            if (vector3d != null && this.heightFromBottom > 0 && this.mob.level.canSeeSkyFromBelowWater(this.mob.blockPosition())) {
                int offset = this.heightFromBottom + this.mob.level.getRandom().nextInt(7) - 4;
                //UntamedWilds.LOGGER.info(vector3d);
                //((ServerLevel)this.mob.level).sendParticles(ParticleTypes.GLOW, vector3d.x(), this.mob.level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int)vector3d.x(), (int)vector3d.z()) + offset, vector3d.z() + 0.5, 50, 0.0D, 0.0D, 0.0D, 0.15F);
                return new Vec3(vector3d.x(), this.mob.level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int)vector3d.x(), (int)vector3d.z()) + offset, vector3d.z());
            }
            return vector3d;
        }
    }
}
