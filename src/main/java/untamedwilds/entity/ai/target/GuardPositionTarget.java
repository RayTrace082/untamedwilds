package untamedwilds.entity.ai.target;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.phys.AABB;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobTerrestrial;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class GuardPositionTarget<T extends LivingEntity> extends TargetGoal {
    protected BlockPos guardPos;
    protected final Class<T> targetClass;
    protected final Sorter sorter;
    protected Predicate<? super T> targetEntitySelector;

    public GuardPositionTarget(ComplexMob creature, Class<T> classTarget, boolean checkSight, boolean isCannibal, final Predicate<LivingEntity> targetSelector) {
        super(creature, checkSight, true);
        this.targetClass = classTarget;
        this.sorter = new Sorter(creature);
        this.setFlags(EnumSet.of(Flag.TARGET, Flag.MOVE, Flag.JUMP));
        this.targetEntitySelector = entity -> isValidTarget(entity, targetSelector);
    }

    protected boolean isValidTarget(LivingEntity entity, @Nullable Predicate<LivingEntity> predicate) {
        if (entity instanceof Creeper || entity.equals(this.mob) || (predicate != null && !predicate.test(entity))) {
            return false;
        }
        if (entity instanceof TamableAnimal tamable) {
            if (tamable.isTame()) {
                // TODO: Logic to decide which mobs belong to an enemy "team"
                return false;
            }
        }
        return canAttack(entity, TargetingConditions.forCombat().range(getFollowDistance()));
    }

    public boolean canUse() {
        if (this.mob.isBaby() || this.mob.getHealth() < this.mob.getMaxHealth() / 3) {
            return false;
        }
        if (this.mob instanceof ComplexMob cmob) {
            if (!cmob.isTame() || cmob.getCommandInt() != 3)
                return false;
        }

        if (this.guardPos == null)
            this.guardPos = this.mob.blockPosition();

        List<T> list = this.mob.level.getEntitiesOfClass(this.targetClass, this.getTargettableArea(this.getFollowDistance()), this.targetEntitySelector);
        if (list.isEmpty() && this.guardPos != null) {
            this.mob.getNavigation().moveTo(this.guardPos.getX(), this.guardPos.getY(), this.guardPos.getZ(), 1);
            if (this.mob.distanceToSqr(this.guardPos.getX(), this.guardPos.getY(), this.guardPos.getZ()) < 2) {
                this.mob.getNavigation().stop();
                if (this.mob instanceof ComplexMob cmob)
                    cmob.setSitting(true);
            }
            // TODO: Code to go back to position and sit there
            return false;
        }

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
