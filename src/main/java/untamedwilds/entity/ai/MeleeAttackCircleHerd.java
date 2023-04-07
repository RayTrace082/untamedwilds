package untamedwilds.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import untamedwilds.entity.ComplexMob;
import untamedwilds.util.EntityUtils;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Random;

public class MeleeAttackCircleHerd extends Goal {
    protected final ComplexMob attacker;
    protected int attackTick;
    private final double speedTowardsTarget;
    private final boolean longMemory;
    private Path path;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    private final float extraReach;
    private long field_220720_k;
    private int failedPathFindingPenalty = 0;
    private final boolean canPenalize = false;
    private float offset = 0;
    private final float maxJumpVelocity;
    private final boolean isJumper;

    public MeleeAttackCircleHerd(ComplexMob entityIn, double speedIn, boolean useLongMemory) {
        this(entityIn, speedIn, useLongMemory, 0);
    }

    public MeleeAttackCircleHerd(ComplexMob entityIn, double speedIn, boolean useLongMemory, float reach) {
        this(entityIn, speedIn, useLongMemory, reach, false);
    }

    public MeleeAttackCircleHerd(ComplexMob entityIn, double speedIn, boolean useLongMemory, float reach, boolean isJumper) {
        this.attacker = entityIn;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.extraReach = reach;
        this.isJumper = isJumper;
        this.maxJumpVelocity = 1;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.attacker.isBaby()) {
            return false;
        }
        long i = this.attacker.level.getGameTime();
        if (i - this.field_220720_k < 20L) {
            return false;
        } else {
            this.field_220720_k = i;
            LivingEntity livingentity = this.attacker.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                if (canPenalize) {
                    if (--this.delayCounter <= 0) {
                        this.path = this.attacker.getNavigation().createPath(livingentity, 0);
                        this.delayCounter = 4 + this.attacker.getRandom().nextInt(7);
                        return this.path != null;
                    } else {
                        return true;
                    }
                }
                this.path = this.attacker.getNavigation().createPath(livingentity, 0);
                return this.path != null;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity livingentity = this.attacker.getTarget();
        if (livingentity == null || (this.attacker.getAirSupply() < 40 && !this.attacker.canBreatheUnderwater()) || !livingentity.isAlive()) {
            return false;
        } else if (!this.longMemory) {
            return !this.attacker.getNavigation().isDone();
        } else if (!this.attacker.isWithinRestriction(livingentity.blockPosition())) {
            return false;
        }
        else {
            return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
        }
    }

    public void start() {
        this.attacker.getNavigation().moveTo(this.path, this.speedTowardsTarget);
        this.attacker.setAggressive(true);
        this.delayCounter = 0;
    }

    public void stop() {
        LivingEntity livingentity = this.attacker.getTarget();
        if (!TargetingConditions.forCombat().test(this.attacker, livingentity)) {
            this.attacker.setTarget(null);
        }
        this.attacker.setAggressive(false);
        this.attacker.getNavigation().stop();
    }

    public void tick() {
        LivingEntity livingentity = this.attacker.getTarget();
        if (this.attacker.tickCount % 200 == 0) {
            this.offset = this.attacker.getRandom().nextInt(10);
        }
        if (this.attacker.herd.creatureList.size() == 1 || (this.attacker.tickCount % 200 < 61 && this.attacker.getTarget().getLastHurtByMob() != this.attacker)) {
            this.attacker.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            double d0 = this.attacker.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ());
            --this.delayCounter;

            if ((this.longMemory || this.attacker.getSensing().hasLineOfSight(livingentity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRandom().nextFloat() < 0.05F)) {
                this.targetX = livingentity.getX();
                this.targetY = livingentity.getBoundingBox().minY;
                this.targetZ = livingentity.getZ();
                this.delayCounter = 4 + this.attacker.getRandom().nextInt(7);
                if (this.canPenalize) {
                    this.delayCounter += failedPathFindingPenalty;
                    if (this.attacker.getNavigation().getPath() != null) {
                        Node finalPathPoint = this.attacker.getNavigation().getPath().getEndNode();
                        if (finalPathPoint != null && livingentity.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                            failedPathFindingPenalty = 0;
                        else
                            failedPathFindingPenalty += 10;
                    } else {
                        failedPathFindingPenalty += 10;
                    }
                }
                if (d0 > 1024.0D) {
                    this.delayCounter += 10;
                } else if (d0 > 256.0D) {
                    this.delayCounter += 5;
                }

                if (!this.attacker.getNavigation().moveTo(livingentity, this.speedTowardsTarget)) {
                    this.delayCounter += 15;
                }
            }
        }
        else if (this.attacker.herd != null) {
            if (this.attacker.getTarget() != null) {
                double x = this.attacker.getTarget().getX() + Math.cos(this.offset + this.attacker.tickCount / 40F) * 6;
                double z = this.attacker.getTarget().getZ() + Math.sin(this.offset + this.attacker.tickCount / 40F) * 6;
                this.attacker.getNavigation().moveTo(x, this.attacker.getTarget().getY(), z, 1.2F);
            }
        }

        BlockPos forwardNearPos = EntityUtils.getRelativeBlockPos(this.attacker, 1.2F, 0);
        if (this.isJumper && this.attacker.isOnGround() && this.attacker.getLevel().getBlockState(forwardNearPos.below()).isAir() && this.attacker.getLevel().getBlockState(forwardNearPos.below(2)).isAir() && this.attacker.getSensing().hasLineOfSight(livingentity)) {
            BlockPos forwardFarPos = EntityUtils.getRelativeBlockPos(this.attacker, 5F, 0);
            if (new Vec3(forwardFarPos.getX(), forwardFarPos.getY(), forwardFarPos.getZ()).distanceTo(livingentity.getPosition(0)) < this.attacker.getPosition(0).distanceTo(livingentity.getPosition(0))) {
                //this.attacker.getEntityWorld().setBlockState(targetpos, Blocks.TORCH.defaultBlockState());
                RandomSource rand = this.attacker.getRandom();
                for (int i = 0; i < 4; i++) {
                    forwardFarPos.offset(rand.nextInt(2) - 1, rand.nextInt(2) - 1, rand.nextInt(2) - 1);
                    if (this.attacker.getNavigation().isStableDestination(forwardFarPos)) {
                        Optional<Vec3> jump_vec = this.calculateOptimalJumpVector(this.attacker, Vec3.atCenterOf(forwardFarPos));
                        if (jump_vec.isPresent()) {
                            double d1 = jump_vec.get().length();
                            double d2 = 1 + d1 + (this.attacker.hasEffect(MobEffects.JUMP) ? (double)(0.1F * (float)(this.attacker.getEffect(MobEffects.JUMP).getAmplifier() + 1)) : 0.0D);
                            this.attacker.setDeltaMovement(jump_vec.get().x * d2 / d1, jump_vec.get().y, jump_vec.get().z * d2 / d1);
                            this.attacker.getNavigation().stop();
                            break;
                        }
                    }
                }
            }
        }

        this.attackTick = Math.max(this.attackTick - 1, 0);
        this.checkAndPerformAttack(livingentity, this.attacker.distanceToSqr(this.targetX, this.targetY, this.targetZ));
    }

    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double d0 = this.getAttackReachSqr(enemy);
        if (this.attacker.hasLineOfSight(enemy) && distToEnemySqr <= d0 && this.attackTick <= 0) {
            this.attackTick = 20;
            this.attacker.doHurtTarget(enemy);
        }
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return (this.attacker.getBbWidth() * 2.0F * this.attacker.getBbWidth() * 2.0F + attackTarget.getBbWidth() + this.extraReach);
    }

    // Start of the code gore
    private Optional<Vec3> calculateOptimalJumpVector(PathfinderMob entityIn, Vec3 targetIn) {
        return calculateOptimalJumpVector(entityIn, targetIn, 20, 55); // 65, 85 are original Goat params
    }

    private Optional<Vec3> calculateOptimalJumpVector(PathfinderMob entityIn, Vec3 targetIn, int minAngleIn, int maxAngleIn) {
        Optional<Vec3> optional = Optional.empty();

        for(int i = minAngleIn; i < maxAngleIn; i += 5) {
            Optional<Vec3> optional1 = this.calculateJumpVectorForAngle(entityIn, targetIn, i);
            if (!optional.isPresent() || optional1.isPresent() && optional1.get().lengthSqr() < optional.get().lengthSqr())
                optional = optional1;
        }

        return optional;
    }

    private Optional<Vec3> calculateJumpVectorForAngle(PathfinderMob entityIn, Vec3 targetIn, int angleIn) {
        Vec3 entityPos = entityIn.position();
        Vec3 vec31 = (new Vec3(targetIn.x - entityPos.x, 0.0D, targetIn.z - entityPos.z)).normalize().scale(0.5D);
        targetIn = targetIn.subtract(vec31);
        Vec3 vec32 = targetIn.subtract(entityPos);
        float angleRad = (float)angleIn * (float)Math.PI / 180.0F;
        double d1 = vec32.subtract(0.0D, vec32.y, 0.0D).lengthSqr();
        double d2 = Math.sqrt(d1);
        double d3 = vec32.y;
        double d4 = Math.sin((2.0F * angleRad));
        double d6 = Math.pow(Math.cos(angleRad), 2.0D);
        double d11 = d1 * 0.08D / (d2 * d4 - 2.0D * d3 * d6);
        if (d11 < 0.0D)
            return Optional.empty();
        else {
            double d7 = Math.sin(angleRad);
            double d8 = Math.cos(angleRad);

            double d0 = Math.atan2(vec32.z, vec32.x);
            double d9 = Math.sin(d0);
            double d10 = Math.cos(d0);

            double d12 = Math.sqrt(d11);
            if (d12 > (double)this.maxJumpVelocity)
                return Optional.empty();
            else {
                double d13 = d12 * d8;
                double d14 = d12 * d7;
                int i = Mth.ceil(d2 / d13) * 2;
                double d15 = 0.0D;
                Vec3 vec33 = null;

                for(int j = 0; j < i - 1; ++j) {
                    d15 += d2 / (double)i;
                    double d16 = d7 / d8 * d15 - Math.pow(d15, 2.0D) * 0.08D / (2.0D * d11 * Math.pow(d8, 2.0D));
                    double d17 = d15 * d10;
                    double d18 = d15 * d9;
                    Vec3 vec34 = new Vec3(entityPos.x + d17, entityPos.y + d16, entityPos.z + d18);
                    if (vec33 != null && !this.isClearTransition(entityIn, vec33, vec34)) {
                        return Optional.empty();
                    }

                    vec33 = vec34;
                }

                return Optional.of((new Vec3(d13 * d10, d14, d13 * d9)).scale(0.95F));
            }
        }
    }

    private boolean isClearTransition(Mob entityIn, Vec3 p_147665_, Vec3 p_147666_) {
        EntityDimensions entitydimensions = entityIn.getDimensions(Pose.LONG_JUMPING);
        Vec3 vec3 = p_147666_.subtract(p_147665_);
        double d0 = Math.min(entitydimensions.width, entitydimensions.height);
        int i = Mth.ceil(vec3.length() / d0);
        Vec3 vec31 = vec3.normalize();
        Vec3 vec32 = p_147665_;

        for(int j = 0; j < i; ++j) {
            vec32 = j == i - 1 ? p_147666_ : vec32.add(vec31.scale(d0 * (double)0.9F));
            AABB aabb = entitydimensions.makeBoundingBox(vec32);
            if (!entityIn.level.noCollision(entityIn, aabb)) {
                return false;
            }
        }

        return true;
    }
}