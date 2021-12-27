package untamedwilds.entity.ai.target;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.EntityPredicates;
import untamedwilds.entity.ComplexMob;
import untamedwilds.util.EntityUtils;

import java.util.List;
import java.util.function.Predicate;

public class HuntWeakerTarget<T extends LivingEntity> extends HuntMobTarget<T> {

    private final int executionChance;

    public HuntWeakerTarget(ComplexMob creature, Class<T> classTarget, boolean checkSight) {
        this(creature, classTarget, checkSight, false);
    }

    public HuntWeakerTarget(ComplexMob creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby) {
        this(creature, classTarget, 300, checkSight, onlyNearby, input -> !EntityUtils.hasFullHealth(input));
    }

    public HuntWeakerTarget(ComplexMob creature, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, final Predicate <? super LivingEntity> targetSelector) {
        super(creature, classTarget, checkSight,200, false, EntityPredicates.NOT_SPECTATING);
        this.executionChance = chance;
        this.targetEntitySelector = (Predicate<T>) entity -> {
            if (targetSelector != null && !targetSelector.test(entity)) {
                return false;
            }
            if (entity instanceof ComplexMob) {
                ComplexMob ctarget = (ComplexMob)entity;
                return (this.goalOwner.getClass() == entity.getClass() && ((ComplexMob)this.goalOwner).getVariant() == ctarget.getVariant()) || !ctarget.canBeTargeted();
            }
            return this.isSuitableTarget(entity, EntityPredicate.DEFAULT);
        };
    }

    @Override
    public boolean shouldExecute() {
        if (this.goalOwner.isChild() || this.goalOwner.getRNG().nextInt(this.executionChance) != 0) {
            return false;
        }
        double perception = this.goalOwner.getAttribute(Attributes.FOLLOW_RANGE).getValue();
        List<T> list = this.goalOwner.world.getEntitiesWithinAABB(this.targetClass, this.goalOwner.getBoundingBox().grow(perception, 8.0D, perception), this.targetEntitySelector);
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

    @Override
    public boolean shouldRemoveTarget(LivingEntity entity) {
        if (entity instanceof CreeperEntity || entity == this.goalOwner || entity.getRidingEntity() != null)  // TODO: Bigger bugs
            return true; // Hardcoded Creepers out because they will absolutely destroy wildlife if targeted
        if (entity instanceof ComplexMob && entity.getHealth() / entity.getMaxHealth() > 0.8) {
            ComplexMob ctarget = (ComplexMob)entity;
            return (this.goalOwner.getClass() == entity.getClass() && ((ComplexMob)this.goalOwner).getVariant() == ctarget.getVariant()) || !ctarget.canBeTargeted();
        }
        return false;
    }
}