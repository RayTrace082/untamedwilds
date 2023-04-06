package untamedwilds.entity.ai.target;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Creeper;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class ProtectChildrenTarget<T extends LivingEntity> extends HuntMobTarget<T> {

    private Mob protectTarget;

    public ProtectChildrenTarget(ComplexMob creature, Class<T> classTarget, boolean checkSight, final Predicate<LivingEntity> targetSelector) {
        super(creature, classTarget, checkSight,200, false, targetSelector);
    }

    protected boolean isValidTarget(LivingEntity entity, @Nullable Predicate<LivingEntity> predicate) {
        if (entity instanceof Creeper || entity.equals(this.mob) || (!ConfigGamerules.attackUndead.get() && entity.getMobType() == MobType.UNDEAD) || (predicate != null && !predicate.test(entity))) {
            return false;
        }
        if (ComplexMob.getEcoLevel(entity) < ComplexMob.getEcoLevel(this.mob) && this.mob.getClass() == entity.getClass() && this.mob instanceof ComplexMob attacker && entity instanceof ComplexMob defender) {
            if (attacker.getVariant() == defender.getVariant()) {
                return false;
            }
        }
        return canAttack(entity, TargetingConditions.forCombat().range(getFollowDistance()));
    }

    @Override
    public boolean canUse() {
        if (this.mob.isBaby() || (this.mob instanceof TamableAnimal tamable && tamable.isTame()))
            return false;

        if (this.mob instanceof ComplexMob temp) {

            for (Mob child : this.mob.level.getEntitiesOfClass(this.mob.getClass(), mob.getBoundingBox().inflate(8.0D, 4.0D, 8.0D))) {
                if (child.isBaby() && ((ComplexMob)child).getVariant() == temp.getVariant()) {
                    this.protectTarget = child;
                    List<T> list = this.mob.level.getEntitiesOfClass(this.targetClass, this.getTargettableArea(this.getFollowDistance()), this.targetEntitySelector);

                    if (list.isEmpty()) {
                        return false;
                    }

                    list.sort(this.sorter);
                    this.targetMob = list.get(0);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canContinueToUse() {
        if (this.protectTarget.distanceTo(this.mob) > 12) {
            this.mob.setTarget(null);
            this.targetMob = null;
            this.mob.getNavigation().moveTo(this.protectTarget, 1);
            return false;
        }
        return super.canContinueToUse();
    }

    @Override
    protected double getFollowDistance() {
        return super.getFollowDistance() * 0.5D;
    }
}