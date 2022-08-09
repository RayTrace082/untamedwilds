package untamedwilds.entity.ai.target;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.phys.AABB;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobTerrestrial;
import untamedwilds.entity.mammal.EntityCamel;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class BeAnAssTarget<T extends LivingEntity> extends TargetGoal {
    protected final Class<T> targetClass;
    protected final Sorter sorter;
    protected Predicate<? super T> targetEntitySelector;
    protected T targetEntity;

    public BeAnAssTarget(ComplexMob creature, Class<T> classTarget, boolean checkSight, final Predicate<? super T> targetSelector) {
        super(creature, checkSight, true);
        this.targetClass = classTarget;
        this.sorter = new Sorter(creature);
        this.setFlags(EnumSet.of(Flag.TARGET));
        this.targetEntitySelector = (Predicate<T>) entity -> {
            if (targetSelector != null && !targetSelector.test(entity)) {
                return false;
            }
            return this.canAttack(entity, TargetingConditions.DEFAULT);
        };
    }

    public boolean canUse() {
        if (!(this.mob instanceof EntityCamel)) {
            UntamedWilds.LOGGER.warn("Trying to run BeAnAssTarget Goal on a mob without a ranged attack");
            return false;
        }
        if (this.mob.getRandom().nextInt(200) != 0 || this.mob.isBaby() || this.mob.getHealth() < this.mob.getMaxHealth() / 2) {
            return false;
        }
        if (this.mob instanceof ComplexMob complexMob) {
            if (complexMob.isTame()) {
                return false;
            }
        }
        List<T> list = this.mob.level.getEntitiesOfClass(this.targetClass, this.getTargetableArea(this.getFollowDistance()), this.targetEntitySelector);
        list.removeIf((Predicate<LivingEntity>) this::shouldRemoveTarget);

        if (list.isEmpty()) {
            return false;
        }
        else {
            list.sort(this.sorter);
            this.targetEntity = list.get(0);
            if (this.mob instanceof ComplexMob) {
                ((ComplexMob)this.mob).huntingCooldown = 6000;
            }
            return true;
        }
    }

    AABB getTargetableArea(double targetDistance) {
        return this.mob.getBoundingBox().inflate(targetDistance, 4.0D, targetDistance);
    }

    public void start() {
        this.mob.getLookControl().setLookAt(this.targetEntity);
        if (this.mob instanceof EntityCamel camel) {
            camel.performRangedAttack(this.targetEntity, 0.5F);
        }
        //this.mob.setTarget(this.targetEntity);
        super.start();
    }

    public boolean shouldRemoveTarget(LivingEntity entity) {
        if (entity instanceof Creeper) {
            return false; // Hardcoded Creepers out because they will absolutely destroy wildlife if targeted
        }
        if (entity instanceof ComplexMob ctarget) {
            return (mob.getClass() == entity.getClass() && ((ComplexMob)mob).getVariant() == ctarget.getVariant()) || !ctarget.canBeTargeted();
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
