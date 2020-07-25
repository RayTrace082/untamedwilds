package untamedwilds.entity.ai.target;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import untamedwilds.entity.ComplexMob;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class TouchedTarget<T extends LivingEntity> extends TargetGoal {

    private final ComplexMob mother;
    private final Sorter sorter;
    private final Class<T> targetClass;
    private final int targetChance;
    private final Predicate<? super T> targetEntitySelector;
    private T targetEntity;

    public TouchedTarget(ComplexMob creature, Class<T> classTarget, boolean checkSight) {
        this(creature, classTarget, checkSight, false);
    }

    public TouchedTarget(ComplexMob creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby) {
        this(creature, classTarget, 10, checkSight, onlyNearby, null);
    }

    public TouchedTarget(ComplexMob creature, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, final Predicate <? super LivingEntity> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.mother = creature;
        this.targetClass = classTarget;
        this.targetChance = chance;
        this.sorter = new Sorter(creature);
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
        this.targetEntitySelector = (Predicate<LivingEntity>) entity -> {
            if (entity == null) {
                return false;
            }
            else if (targetSelector != null && !targetSelector.test(entity)) {
                return false;
            }
            else if (entity instanceof CreeperEntity || entity.getClass() == this.mother.getClass()) {
                // Hardcoded Creepers out because they will absolutely destroy wildlife if targeted
                return false;
            }
            else {
                return EntityPredicates.NOT_SPECTATING.test(this.mother) && this.isSuitableTarget(entity, EntityPredicate.DEFAULT);
            }
        };
    }

    public boolean shouldExecute() {
        if (mother.isChild() || mother.isTamed()) {
            return false;
        }
        if (this.targetChance > 0 && this.goalOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        }
        else {
            List<T> list = this.goalOwner.world.getEntitiesWithinAABB(this.targetClass, this.getTargetableArea(this.mother, 1), this.targetEntitySelector);
            if (list.isEmpty()) {
                return false;
            }
            else {
                list.sort(this.sorter);
                this.targetEntity = list.get(0);
                return true;
            }
        }
    }

    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }

        private AxisAlignedBB getTargetableArea(ComplexMob target, double targetDistance) {
        return target.getBoundingBox().grow(targetDistance, 1.0D, targetDistance);
    }

    protected double getTargetDistance()
        {
            return super.getTargetDistance() * 0.5D;
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