package untamedwilds.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.vector.Vector3d;
import untamedwilds.util.EntityUtils;

import java.util.EnumSet;
import java.util.Objects;

public class SmartMeleeAttackGoal extends Goal {
    protected final CreatureEntity attacker;
    protected int attackTick;
    private final double speedTowardsTarget;
    private final boolean longMemory;
    private Path path;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    private float extraReach;
    private long field_220720_k;
    private int failedPathFindingPenalty = 0;
    private boolean canPenalize = false;
    private final boolean doSkirmish;

    public SmartMeleeAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory) {
        this(creature, speedIn, useLongMemory, 0, false);
    }

    public SmartMeleeAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory, float reach) {
        this(creature, speedIn, useLongMemory, reach, false);
    }

    public SmartMeleeAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory, float reach, boolean doSkirmish) {
        this.attacker = creature;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.extraReach = reach;
        this.doSkirmish = doSkirmish;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        if (this.attacker.isChild() || (this.doSkirmish && !EntityUtils.hasFullHealth(this.attacker))) {
            return false;
        }
        long i = this.attacker.world.getGameTime();
        if (i - this.field_220720_k < 20L) {
            return false;
        } else {
            this.field_220720_k = i;
            LivingEntity livingentity = this.attacker.getAttackTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                if (canPenalize) {
                    if (--this.delayCounter <= 0) {
                        this.path = this.attacker.getNavigator().getPathToEntity(livingentity, 0);
                        this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
                        return this.path != null;
                    } else {
                        return true;
                    }
                }
                this.path = this.attacker.getNavigator().getPathToEntity(livingentity, 0);
                return this.path != null;
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        // TODO: DEBUG: Skirmish option, where a mob low on health will retreat if targeted, disabled
        if (false && this.doSkirmish && !EntityUtils.hasFullHealth(this.attacker)) {
            if (this.attacker.getAttackTarget() instanceof CreatureEntity) {
                CreatureEntity targetEntity = (CreatureEntity) this.attacker.getAttackTarget();
                if (Objects.equals(targetEntity.getAttackTarget(), this.attacker)) {
                    Vector3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(targetEntity, 12, 4, new Vector3d(targetEntity.getPosX(), targetEntity.getPosY(), targetEntity.getPosZ()));
                    if (vec3d != null) {
                        this.attacker.setAttackTarget(null);
                        this.attacker.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, this.speedTowardsTarget);
                        return false;
                    }
                }
            }
        }
        LivingEntity livingentity = this.attacker.getAttackTarget();
        if (livingentity == null || (this.attacker.getAir() < 40 && !this.attacker.canBreatheUnderwater()) || !livingentity.isAlive()) {
            return false;
        } else if (!this.longMemory) {
            return !this.attacker.getNavigator().noPath();
        } else if (!this.attacker.isWithinHomeDistanceFromPosition(livingentity.getPosition())) {
            return false;
        }
        else {
            return !(livingentity instanceof PlayerEntity) || !livingentity.isSpectator() && !((PlayerEntity)livingentity).isCreative();
        }
    }

    public void startExecuting() {
        this.attacker.getNavigator().setPath(this.path, this.speedTowardsTarget);
        this.attacker.setAggroed(true);
        this.delayCounter = 0;
    }

    public void resetTask() {
        LivingEntity livingentity = this.attacker.getAttackTarget();
        if (!EntityPredicates.CAN_AI_TARGET.test(livingentity)) {
            this.attacker.setAttackTarget(null);
        }
        this.attacker.setAggroed(false);
        this.attacker.getNavigator().clearPath();
    }

    public void tick() {
        LivingEntity livingentity = this.attacker.getAttackTarget();
        this.attacker.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);
        double d0 = this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getBoundingBox().minY, livingentity.getPosZ());
        --this.delayCounter;

        // This piece of code fixes mobs in water being unable to chase upwards
        if (this.attacker.isInWater() && this.attacker.ticksExisted % 12 == 0) {
            if ((livingentity.getBoundingBox().minY + 1) > this.attacker.getPosY()) {
                this.attacker.getJumpController().setJumping();
            }
        }

        if ((this.longMemory || this.attacker.getEntitySenses().canSee(livingentity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
            this.targetX = livingentity.getPosX();
            this.targetY = livingentity.getBoundingBox().minY;
            this.targetZ = livingentity.getPosZ();
            this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
            if (this.canPenalize) {
                this.delayCounter += failedPathFindingPenalty;
                if (this.attacker.getNavigator().getPath() != null) {
                    net.minecraft.pathfinding.PathPoint finalPathPoint = this.attacker.getNavigator().getPath().getFinalPathPoint();
                    if (finalPathPoint != null && livingentity.getDistanceSq(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
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

            if (!this.attacker.getNavigator().tryMoveToEntityLiving(livingentity, this.speedTowardsTarget)) {
                this.delayCounter += 15;
            }
        }

        this.attackTick = Math.max(this.attackTick - 1, 0);
        this.checkAndPerformAttack(livingentity, d0);
    }

    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double d0 = this.getAttackReachSqr(enemy);
        if (distToEnemySqr <= d0 && this.attackTick <= 0) {
            this.attackTick = 20;
            this.attacker.attackEntityAsMob(enemy);
        }

    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return (this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + attackTarget.getWidth() + this.extraReach);
    }
}