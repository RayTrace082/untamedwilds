package untamedwilds.entity.ai.target;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.phys.AABB;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobTerrestrial;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class HuntMobTarget<T extends LivingEntity> extends TargetGoal {
    protected final Class<T> targetClass;
    protected final Sorter sorter;
    protected Predicate<? super T> targetEntitySelector;
    private final int threshold;
    private final boolean isCannibal;

    public HuntMobTarget(ComplexMob creature, Class<T> classTarget, boolean checkSight, boolean isCannibal, final Predicate<LivingEntity> targetSelector) {
        this(creature, classTarget, checkSight, 200, isCannibal, targetSelector);
    }

    public HuntMobTarget(ComplexMob creature, Class<T> classTarget, boolean checkSight, int hungerThreshold, boolean isCannibal, final Predicate<LivingEntity> targetSelector) {
        super(creature, checkSight, true);
        this.targetClass = classTarget;
        this.sorter = new Sorter(creature);
        this.setFlags(EnumSet.of(Flag.TARGET));
        this.threshold = hungerThreshold;
        this.isCannibal = isCannibal;
        this.targetEntitySelector = entity -> isValidTarget(entity, targetSelector);
    }

    protected boolean isValidTarget(LivingEntity entity, @Nullable Predicate<LivingEntity> predicate) {
        if (entity instanceof Creeper || entity.equals(this.mob) || (!ConfigGamerules.attackUndead.get() && entity.getMobType() == MobType.UNDEAD) || (entity instanceof ComplexMob cmob && !cmob.canBeTargeted()) || (predicate != null && !predicate.test(entity))) {
            return false;
        }
        if (!this.isCannibal && this.mob.getClass() == entity.getClass() && this.mob instanceof ComplexMob attacker && entity instanceof ComplexMob defender) {
            if (attacker.getVariant() == defender.getVariant()) {
                return false;
            }
        }
        return canAttack(entity, TargetingConditions.forCombat().range(getFollowDistance()));
    }

    public boolean canUse() {
        if (this.mob.isBaby() || this.mob.getHealth() < this.mob.getMaxHealth() / 3) {
            return false;
        }
        if (this.mob instanceof ComplexMob) {
            if (((ComplexMob)this.mob).huntingCooldown != 0)
                return false;
            if (this.mob instanceof ComplexMobTerrestrial tamed) {
                if (tamed.isTame() || tamed.getHunger() > this.threshold)
                    return false;
            }
        }

        List<T> list = this.mob.level.getEntitiesOfClass(this.targetClass, this.getTargettableArea(this.getFollowDistance()), this.targetEntitySelector);
        if (list.isEmpty())
            return false;

        list.sort(this.sorter);
        this.targetMob = list.get(0);
        return true;
    }

    AABB getTargettableArea(double targetDistance) {
        return this.mob.getBoundingBox().inflate(targetDistance, 4.0D, targetDistance);
    }

    public void start() {
        if (!this.mob.getNavigation().isDone())
            this.mob.getNavigation().stop();
        if (this.mob instanceof ComplexMob)
            ((ComplexMob)this.mob).huntingCooldown = 6000;
        this.mob.setTarget(this.targetMob);
        super.start();
    }

    public boolean canContinueToUse() {
        return super.canContinueToUse();
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity entity;

        private Sorter(Entity entityIn) {
            this.entity = entityIn;
        }

        public int compare(Entity entity_1, Entity entity_2) {
            double dist_1 = this.entity.distanceToSqr(entity_1);
            double dist_2 = this.entity.distanceToSqr(entity_2);

            if (dist_1 < dist_2) {
                return -1;
            }
            else {
                return dist_1 > dist_2 ? 1 : 0;
            }
        }
    }
}
