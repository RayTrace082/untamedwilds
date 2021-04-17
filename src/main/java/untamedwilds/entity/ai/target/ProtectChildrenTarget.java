package untamedwilds.entity.ai.target;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.EntityPredicates;
import untamedwilds.entity.ComplexMob;

import java.util.List;
import java.util.function.Predicate;

public class ProtectChildrenTarget<T extends LivingEntity> extends HuntMobTarget<T> {

    private final int executionChance;

    public ProtectChildrenTarget(ComplexMob creature, Class<T> classTarget, boolean checkSight) {
        this(creature, classTarget, checkSight, false);
    }

    public ProtectChildrenTarget(ComplexMob creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby) {
        this(creature, classTarget, 10, checkSight, onlyNearby, null);
    }

    public ProtectChildrenTarget(ComplexMob creature, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, final Predicate <? super LivingEntity> targetSelector) {
        super(creature, classTarget, checkSight,200, onlyNearby, false, EntityPredicates.NOT_SPECTATING);
        this.executionChance = chance;
        this.targetEntitySelector = (Predicate<T>) entity -> {
            if (targetSelector != null && !targetSelector.test(entity)) {
                return false;
            }
            else {
                return this.isSuitableTarget(entity, EntityPredicate.DEFAULT);
            }
        };
    }

    @Override
    public boolean shouldExecute() {
        if (this.goalOwner.isChild()) {
            return false;
        }

        if (this.goalOwner instanceof ComplexMob) {
            ComplexMob temp = (ComplexMob) this.goalOwner;
            if (temp.isTamed()) {
                return false;
            }

            for (MobEntity child : this.goalOwner.world.getEntitiesWithinAABB(this.goalOwner.getClass(), goalOwner.getBoundingBox().grow(8.0D, 4.0D, 8.0D))) {
                if (child.isChild() && ((ComplexMob)child).getVariant() == temp.getVariant()) {

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
            }
        }
        return false;
    }

    @Override
    protected double getTargetDistance()
        {
            return super.getTargetDistance() * 0.5D;
        }

}