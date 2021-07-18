package untamedwilds.entity.ai.unique;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import untamedwilds.entity.ComplexMob;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class TortoiseHideInShellGoal<T extends LivingEntity> extends Goal {

    protected final Class<T> classToAvoid;
    protected T avoidTarget;
    protected ComplexMob taskOwner;
    protected final float avoidDistance;
    private final EntityPredicate builtTargetSelector;

    public TortoiseHideInShellGoal(ComplexMob entityIn, Class<T> classToAvoidIn, float avoidDistanceIn, final Predicate<LivingEntity> targetSelector) {
        this.taskOwner = entityIn;
        this.classToAvoid = classToAvoidIn;
        this.avoidDistance = avoidDistanceIn;
        this.builtTargetSelector = (new EntityPredicate()).setDistance(avoidDistanceIn).setCustomPredicate(targetSelector);
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (this.taskOwner.ticksExisted % 40 != 0) {
            return false;
        }
        if (this.taskOwner.getAttackTarget() != null || this.taskOwner.getCommandInt() != 0 || this.taskOwner.isTamed()) {
            return false;
        }

        List<T> list = this.taskOwner.world.getTargettableEntitiesWithinAABB(classToAvoid, this.builtTargetSelector, this.taskOwner, this.taskOwner.getBoundingBox().grow(avoidDistance, 4f, avoidDistance));
        if (list.isEmpty()) {
            this.taskOwner.setSitting(false);
            return false;
        }
        this.avoidTarget = list.get(0);
        return true;
    }

    public void startExecuting() {
        super.startExecuting();
        this.taskOwner.getNavigator().clearPath();
        this.taskOwner.setSitting(true);
    }

    public void resetTask() {
        super.resetTask();
    }

    public void tick() {
        if (this.taskOwner.getRNG().nextInt(40) == 0) {
            List<T> list = this.taskOwner.world.getTargettableEntitiesWithinAABB(classToAvoid, this.builtTargetSelector, this.taskOwner, this.taskOwner.getBoundingBox().grow(avoidDistance, 4f, avoidDistance));
            if (list.isEmpty()) {
                this.taskOwner.setSitting(false);
            }
        }
        if (this.taskOwner.getDistance(this.avoidTarget) > 10) {
            this.taskOwner.setSitting(false);
        }
        super.tick();
    }

    public boolean shouldContinueExecuting() {
        return !this.taskOwner.isSitting();
    }
}