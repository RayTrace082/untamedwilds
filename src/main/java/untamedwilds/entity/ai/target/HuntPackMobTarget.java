package untamedwilds.entity.ai.target;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.EntityPredicates;
import untamedwilds.entity.ComplexMob;

import java.util.function.Predicate;

public class HuntPackMobTarget<T extends LivingEntity> extends HuntMobTarget<T> {

    public HuntPackMobTarget(ComplexMob creature, Class<T> classTarget, boolean checkSight, int hungerThreshold, boolean onlyNearby, final Predicate<? super T> targetSelector) {
        super(creature, classTarget, checkSight, hungerThreshold, onlyNearby, false, EntityPredicates.NOT_SPECTATING);
        this.targetEntitySelector = (Predicate<T>) entity -> {
            if (targetSelector != null && !targetSelector.test(entity)) {
                return false;
            }
            else {
                return this.isSuitableTarget(entity, EntityPredicate.DEFAULT);
            }
        };
    }

    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.targetEntity);
        if (this.goalOwner instanceof ComplexMob) {
            ComplexMob taskOwner = (ComplexMob)this.goalOwner;
            if (taskOwner.herd != null) {
                for (ComplexMob creature : taskOwner.herd.creatureList) {
                    creature.setAttackTarget(this.targetEntity);
                }
            }
        }
        super.startExecuting();
    }
}
