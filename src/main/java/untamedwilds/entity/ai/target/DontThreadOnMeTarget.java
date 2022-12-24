package untamedwilds.entity.ai.target;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Creeper;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class DontThreadOnMeTarget<T extends LivingEntity> extends TargetGoal {
    protected final Class<T> targetClass;
    protected Predicate<T> targetEntitySelector;
    private int runningTicks;

    public DontThreadOnMeTarget(Mob entityIn, Class<T> targetClassIn, boolean checkSight) {
        this(entityIn, targetClassIn, checkSight, false);
    }

    public DontThreadOnMeTarget(Mob entityIn, Class<T> targetClassIn, boolean checkSight, boolean nearbyOnlyIn) {
        super(entityIn, checkSight, nearbyOnlyIn);
        this.targetClass = targetClassIn;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        this.targetEntitySelector = entity -> isValidTarget(entity, null);
    }

    protected boolean isValidTarget(LivingEntity entity, @Nullable Predicate<LivingEntity> predicate) {
        if (entity instanceof Creeper || entity.equals(this.mob) || (!ConfigGamerules.attackUndead.get() && entity.getMobType() == MobType.UNDEAD) || (predicate != null && !predicate.test(entity))) {
            return false;
        }
        if (this.mob.getClass() == entity.getClass() && this.mob instanceof ComplexMob attacker && entity instanceof ComplexMob defender) {
            if (attacker.getVariant() == defender.getVariant()) {
                return false;
            }
        }
        return entity.getBoundingBox().intersects(this.mob.getBoundingBox()) && canAttack(entity, TargetingConditions.forCombat().range(getFollowDistance()));
    }

    public boolean canUse() {
        if (!ConfigGamerules.contactAgression.get()) {
            return false;
        } else {
            List<T> list = this.mob.level.getEntitiesOfClass(this.targetClass, this.mob.getBoundingBox().inflate(1F), this.targetEntitySelector);
            if (list.isEmpty()) {
                return false;
            }
            else {
                this.targetMob = list.get(0);
                return true;
            }
        }
    }

    public void start() {
        this.mob.setTarget(this.targetMob);
        this.runningTicks = 60;
        super.start();
    }

    public boolean canContinueToUse() {
        this.runningTicks--;
        if (this.runningTicks < 1) {
            this.mob.setTarget(null);
            return false;
        }
        return super.canContinueToUse();
    }
}