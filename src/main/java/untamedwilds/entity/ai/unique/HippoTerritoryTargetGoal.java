package untamedwilds.entity.ai.unique;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.EntityPredicates;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ai.target.HuntMobTarget;

import java.util.function.Predicate;

public class HippoTerritoryTargetGoal<T extends LivingEntity> extends HuntMobTarget<T>  {

    public HippoTerritoryTargetGoal(ComplexMob creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby, final Predicate<? super T > targetSelector) {
        super(creature, classTarget, checkSight,200, onlyNearby, false, EntityPredicates.NOT_SPECTATING);
        this.targetEntitySelector = (Predicate<T>) entity -> {
            if (targetSelector != null && !targetSelector.test(entity)) {
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
        return super.shouldExecute();
    }
}
