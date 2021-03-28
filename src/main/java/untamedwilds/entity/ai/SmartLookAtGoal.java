package untamedwilds.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;
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
    protected final EntityPredicate SHOULD_LOOK;

    public SmartLookAtGoal(ComplexMob entityIn, Class<? extends LivingEntity> targetClass, float maxDistance) {
        this(entityIn, targetClass, maxDistance, 60);
    }

    public SmartLookAtGoal(ComplexMob entityIn, Class<? extends LivingEntity> targetClass, float maxDistance, int chanceIn) {
        this.taskOwner = entityIn;
        this.watchedClass = targetClass;
        this.maxDistance = maxDistance;
        this.chance = chanceIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
        if (targetClass == PlayerEntity.class) {
            this.SHOULD_LOOK = (new EntityPredicate()).setDistance(maxDistance).allowFriendlyFire().allowInvulnerable().setSkipAttackChecks().setCustomPredicate((p_220715_1_) -> EntityPredicates.notRiding(entityIn).test(p_220715_1_));
        } else {
            this.SHOULD_LOOK = (new EntityPredicate()).setDistance(maxDistance).allowFriendlyFire().allowInvulnerable().setSkipAttackChecks();
        }

    }

    @Override
    public boolean shouldExecute() {
        if (this.taskOwner.isSleeping()) {
            return false;
        }
        if (this.taskOwner.getRNG().nextInt(this.chance) != 0) {
            return false;
        } else {
            if (this.taskOwner.getAttackTarget() != null) {
                this.closestEntity = this.taskOwner.getAttackTarget();
            }

            if (this.watchedClass == PlayerEntity.class) {
                this.closestEntity = this.taskOwner.world.getClosestPlayer(this.SHOULD_LOOK, this.taskOwner, this.taskOwner.getPosX(), this.taskOwner.getPosY() + (double)this.taskOwner.getEyeHeight(), this.taskOwner.getPosZ());
            } else {
                this.closestEntity = this.taskOwner.world.func_225318_b(this.watchedClass, this.SHOULD_LOOK, this.taskOwner, this.taskOwner.getPosX(), this.taskOwner.getPosY() + (double)this.taskOwner.getEyeHeight(), this.taskOwner.getPosZ(), this.taskOwner.getBoundingBox().grow(this.maxDistance, 3.0D, this.maxDistance));
            }

            return this.closestEntity != null || this.taskOwner.getRNG().nextInt(20) != 0;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        /*if (!this.closestEntity.isAlive() || this.taskOwner.getDistanceSq(this.closestEntity) > (double)(this.maxDistance * this.maxDistance)) {
            return false;
        }*/
        return this.lookTime > 0;
    }

    @Override
    public void startExecuting() {
        if (this.closestEntity == null) {
            double d0 = (Math.PI * 2D) * this.taskOwner.getRNG().nextDouble();
            this.lookX = Math.cos(d0);
            this.lookZ = Math.sin(d0);
            this.lookTime = 20 + this.taskOwner.getRNG().nextInt(20);
        }
        this.lookTime = 40 + this.taskOwner.getRNG().nextInt(40);
    }

    @Override
    public void resetTask() {
        this.closestEntity = null;
    }

    @Override
    public void tick() {
        if (this.closestEntity == null) {
            this.taskOwner.getLookController().setLookPosition(this.taskOwner.getPosX() + this.lookX, this.taskOwner.getPosY() + (double)this.taskOwner.getEyeHeight(), this.taskOwner.getPosZ());
        }
        else {
            this.taskOwner.getLookController().setLookPosition(this.closestEntity.getPosX(), this.closestEntity.getPosY() + (double)this.closestEntity.getEyeHeight(), this.closestEntity.getPosZ(), 20, this.taskOwner.getVerticalFaceSpeed());
        }
        --this.lookTime;
    }
}
