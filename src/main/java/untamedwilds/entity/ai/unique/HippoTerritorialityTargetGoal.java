package untamedwilds.entity.ai.unique;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import untamedwilds.entity.ComplexMobAquatic;
import untamedwilds.entity.ComplexMobTerrestrial;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class HippoTerritorialityTargetGoal<T extends LivingEntity> extends TargetGoal {

    private final Class<T> targetClass;
    private final Sorter sorter;
    private final Predicate<? super T> targetEntitySelector;
    private T targetEntity;

    public HippoTerritorialityTargetGoal(MobEntity creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby, final Predicate<? super T > targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.targetClass = classTarget;
        this.sorter = new Sorter(creature);
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.targetEntitySelector = (Predicate<T>) entity -> {
            if (entity == null || creature instanceof ComplexMobTerrestrial) {
                if (entity instanceof CreeperEntity) {
                    return false;
                }
                return EntityPredicates.NOT_SPECTATING.test(entity) && targetSelector.test(entity) && this.isSuitableTarget(entity, EntityPredicate.DEFAULT);
            }
            else if (targetSelector != null && !targetSelector.test(entity)) {
                return false;
            }
            else {
                return EntityPredicates.NOT_SPECTATING.test(entity) && this.isSuitableTarget(entity, EntityPredicate.DEFAULT);
            }
        };
    }

    public boolean shouldExecute() {
        if (goalOwner.isChild() || !goalOwner.isInWater()) {
            return false;
        }
        if (goalOwner == targetEntity) {
            return false;
        }
        List<T> list = this.goalOwner.world.getEntitiesWithinAABB(this.targetClass, this.getTargetableArea(6F), this.targetEntitySelector::test);
        list.removeIf((LivingEntity entity) -> (goalOwner.getClass() == entity.getClass() || entity instanceof ComplexMobAquatic));
        if (list.isEmpty()) {
            return false;
        }
        else {
            list.sort(this.sorter);
            this.targetEntity = list.get(0);
            return true;
        }
    }

    private AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.goalOwner.getBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity entity;

        private Sorter(Entity entityIn)
        {
            this.entity = entityIn;
        }

        public int compare(Entity entity_1, Entity entity_2) {
            double dist_1 = this.entity.getDistanceSq(entity_1);
            double dist_2 = this.entity.getDistanceSq(entity_2);

            if (dist_1 < dist_2) {
                return -1;
            }
            else {
                return dist_1 > dist_2 ? 1 : 0;
            }
        }
    }
}
