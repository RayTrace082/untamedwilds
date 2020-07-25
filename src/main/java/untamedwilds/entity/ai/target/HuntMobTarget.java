package untamedwilds.entity.ai.target;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobTerrestrial;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class HuntMobTarget<T extends LivingEntity, target, attacked> extends TargetGoal {
    private final Class<T> targetClass;
    private final Sorter sorter;
    private final Predicate<? super T> targetEntitySelector;
    private T targetEntity;
    private boolean isCannibal;

    public HuntMobTarget(ComplexMob creature, Class<T> classTarget, boolean checkSight, int hungerThreshold, boolean onlyNearby, boolean isCannibal, final Predicate<? super T > targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.targetClass = classTarget;
        this.sorter = new Sorter(creature);
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        this.isCannibal = isCannibal;
        this.targetEntitySelector = (Predicate<T>) entity -> {
            if (entity == null || creature instanceof ComplexMobTerrestrial) {
                if (((ComplexMobTerrestrial)creature).getHunger() >= hungerThreshold || creature.isTamed()) {
                    return false;
                }
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
        if (this.goalOwner.isChild() || this.goalOwner.getHealth() < this.goalOwner.getMaxHealth() / 3) {
            return false;
        }
        //if (this.goalOwner == targetEntity) {
        //  return false;
        //}
        List<T> list = this.goalOwner.world.getEntitiesWithinAABB(this.targetClass, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
        list.removeIf((Predicate<LivingEntity>) this::shouldRemoveTarget);

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

    private boolean shouldRemoveTarget(LivingEntity entity) {
        if (entity instanceof CreeperEntity) {
            return false; // Hardcoded Creepers out because they will absolutely destroy wildlife if targeted
            // TODO: Revalidate the Creeper hardcode since mobs won't hunt below 1/3 Health
        }
        if (!this.isCannibal) {
            if (entity instanceof ComplexMob) {
                ComplexMob ctarget = (ComplexMob)entity;
                return (goalOwner.getClass() == entity.getClass() && ((ComplexMob)goalOwner).getSpecies() == ctarget.getSpecies()) || !ctarget.canBeTargeted();
            }
        }
        return false;
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
