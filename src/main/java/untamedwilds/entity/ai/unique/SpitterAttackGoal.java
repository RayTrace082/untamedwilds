package untamedwilds.entity.ai.unique;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import untamedwilds.entity.relict.EntitySpitter;

import java.util.EnumSet;
import java.util.List;

public class SpitterAttackGoal extends Goal {
    protected final EntitySpitter attacker;
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

    public SpitterAttackGoal(EntitySpitter entityIn, double speedIn, boolean useLongMemory, float reach) {
        this.attacker = entityIn;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.extraReach = reach;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
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
        if (!this.attacker.isBaby())
            this.attacker.getNavigation().moveTo(this.path, this.speedTowardsTarget);
        this.attacker.setAggressive(true);
        this.delayCounter = 0;

        List<? extends EntitySpitter> list = this.attacker.level.getEntitiesOfClass(EntitySpitter.class, this.attacker.getBoundingBox().inflate(12.0D, 8.0D, 12.0D));

        for (EntitySpitter entityanimal1 : list) {
            if (entityanimal1.getVariant() == this.attacker.getVariant()) {
                entityanimal1.setTarget(this.attacker.getTarget());
            }
        }
    }

    public void stop() {
        /*LivingEntity livingentity = this.attacker.getTarget();
        if (!TargetingConditions.forCombat().test(this.attacker, livingentity)) {
            this.attacker.setTarget(null);
        }*/
        this.attacker.setTarget(null); // <- Edited
        this.attacker.setAggressive(false);
        this.attacker.getNavigation().stop();
    }

    public void tick() {
        LivingEntity livingentity = this.attacker.getTarget();
        this.attacker.getLookControl().setLookAt(Vec3.atCenterOf(livingentity.blockPosition()));
        //this.attacker.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
        double d0 = this.attacker.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ());
        --this.delayCounter;

        // This piece of code fixes mobs in water being unable to chase upwards
        if (this.attacker.isInWater() && this.attacker.tickCount % 12 == 0) {
            if ((livingentity.getBoundingBox().minY - 2) > this.attacker.getY()) {
                this.attacker.getJumpControl().jump();
            }
        }

        if (this.attacker.getAnimation() == EntitySpitter.NO_ANIMATION && this.attacker.getSensing().hasLineOfSight(livingentity) && this.attacker.getRandom().nextInt(this.attacker.isBaby() ? 80 : 200) == 0 && d0 > 12) { // TODO: random chance = 200
            this.attacker.getLookControl().setLookAt(livingentity);
            this.attacker.setAnimation(EntitySpitter.ATTACK_SPIT);
        }
        else if (!this.attacker.isBaby() && (this.longMemory || this.attacker.getSensing().hasLineOfSight(livingentity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRandom().nextFloat() < 0.05F)) {
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

        this.attackTick = Math.max(this.attackTick - 1, 0);
        this.checkAndPerformAttack(livingentity, d0);
    }

    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double d0 = this.getAttackReachSqr(enemy);
        if (this.attacker.hasLineOfSight(enemy) && distToEnemySqr <= d0 && (this.attackTick <= 0 || (this.attackTick <= 10 && this.attacker.getAnimation() == IAnimatedEntity.NO_ANIMATION))) {
            this.attackTick = 20;
            this.attacker.doHurtTarget(enemy);
        }

    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return (this.attacker.getBbWidth() * 2.0F * this.attacker.getBbWidth() * 2.0F + attackTarget.getBbWidth() + this.extraReach);
    }
}