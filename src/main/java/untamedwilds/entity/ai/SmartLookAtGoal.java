package untamedwilds.entity.ai;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import untamedwilds.entity.ComplexMob;

import java.util.EnumSet;

public class SmartLookAtGoal extends Goal {
    protected final ComplexMob taskOwner;
    protected Entity closestEntity;
    protected final float maxDistance;
    private int lookTime;
    private final int chance;
    private double lookX;
    private double lookZ;
    protected final Class<? extends LivingEntity> watchedClass;
    protected final TargetingConditions SHOULD_LOOK;

    public SmartLookAtGoal(ComplexMob entityIn, Class<? extends LivingEntity> targetClass, float maxDistance) {
        this(entityIn, targetClass, maxDistance, 60);
    }

    public SmartLookAtGoal(ComplexMob entityIn, Class<? extends LivingEntity> targetClass, float maxDistance, int chanceIn) {
        this.taskOwner = entityIn;
        this.watchedClass = targetClass;
        this.maxDistance = maxDistance;
        this.chance = chanceIn;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        if (targetClass == Player.class) {
            this.SHOULD_LOOK = TargetingConditions.forNonCombat().range(maxDistance).selector((p_25531_) -> EntitySelector.notRiding(entityIn).test(p_25531_));
        } else {
            this.SHOULD_LOOK = TargetingConditions.forNonCombat().range(maxDistance);
        }
    }

    @Override
    public boolean canUse() {
        if (this.taskOwner.isSleeping()) {
            return false;
        }
        if (this.taskOwner.getRandom().nextInt(this.chance) != 0) {
            return false;
        } else {
            if (this.taskOwner.getTarget() != null) {
                this.closestEntity = this.taskOwner.getTarget();
            }

            if (this.watchedClass == Player.class) {
                this.closestEntity = this.taskOwner.level.getNearestPlayer(this.SHOULD_LOOK, this.taskOwner, this.taskOwner.getX(), this.taskOwner.getY() + (double)this.taskOwner.getEyeHeight(), this.taskOwner.getZ());
            } else {
                this.closestEntity = this.taskOwner.level.getNearestEntity(this.watchedClass, this.SHOULD_LOOK, this.taskOwner, this.taskOwner.getX(), this.taskOwner.getY() + (double)this.taskOwner.getEyeHeight(), this.taskOwner.getZ(), this.taskOwner.getBoundingBox().inflate(this.maxDistance, 3.0D, this.maxDistance));
            }

            return this.closestEntity != null || this.taskOwner.getRandom().nextInt(20) != 0;
        }
    }

    @Override
    public boolean canContinueToUse() {
        /*if (!this.closestEntity.isAlive() || this.taskOwner.distanceToSqr(this.closestEntity) > (double)(this.maxDistance * this.maxDistance)) {
            return false;
        }*/
        return this.lookTime > 0;
    }

    @Override
    public void start() {
        if (this.closestEntity == null) {
            double d0 = (Math.PI * 2D) * this.taskOwner.getRandom().nextDouble();
            this.lookX = Math.cos(d0);
            this.lookZ = Math.sin(d0);
            this.lookTime = 20 + this.taskOwner.getRandom().nextInt(20);
        }
        this.lookTime = 40 + this.taskOwner.getRandom().nextInt(40);
    }

    @Override
    public void stop() {
        this.closestEntity = null;
    }

    @Override
    public void tick() {
        if (this.closestEntity == null) {
            this.taskOwner.getLookControl().setLookAt(this.taskOwner.getX() + this.lookX, this.taskOwner.getY() + (double)this.taskOwner.getEyeHeight(), this.taskOwner.getZ());
        }
        else {
            this.taskOwner.getLookControl().setLookAt(this.closestEntity.getX(), this.closestEntity.getY() + (double)this.closestEntity.getEyeHeight(), this.closestEntity.getZ(), 20, this.taskOwner.getHeadRotSpeed());
        }
        --this.lookTime;
    }
}
