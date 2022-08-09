package untamedwilds.entity.ai.unique;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ai.target.HuntMobTarget;

import java.util.function.Predicate;

public class HippoTerritoryTargetGoal<T extends LivingEntity> extends HuntMobTarget<T>  {

    public HippoTerritoryTargetGoal(ComplexMob creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby, final Predicate<? super T > targetSelector) {
        super(creature, classTarget, checkSight,200, false, ((Predicate<LivingEntity>)null));
        this.targetEntitySelector = (Predicate<T>) entity -> {
            if (targetSelector != null && !targetSelector.test(entity)) {
                return false;
            }
            else {
                return TargetingConditions.forCombat().test(creature, entity) && this.canAttack(entity, TargetingConditions.DEFAULT);
            }
        };
    }

    public boolean canUse() {
        if (mob.isBaby() || !mob.isInWater()) {
            return false;
        }
        return super.canUse();
    }
}
