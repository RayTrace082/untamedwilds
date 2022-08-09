package untamedwilds.entity.ai.target;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import untamedwilds.entity.ComplexMob;

import java.util.EnumSet;

public class HurtPackByTargetGoal extends TargetGoal {
    private static final TargetingConditions field_220795_a = (TargetingConditions.DEFAULT);
    private boolean entityCallsForHelp;
    private int revengeTimerOld;
    private final Class<?>[] excludedReinforcementTypes;
    private Class<?>[] reinforcementTypes;

    public HurtPackByTargetGoal(PathfinderMob creatureIn, Class<?>... excludeReinforcementTypes) {
        super(creatureIn, true);
        this.excludedReinforcementTypes = excludeReinforcementTypes;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    public boolean canUse() {
        int i = this.mob.getLastHurtByMobTimestamp();
        LivingEntity livingentity = this.mob.getLastHurtByMob();
        if (i != this.revengeTimerOld && livingentity != null) {
            for(Class<?> oclass : this.excludedReinforcementTypes) {
                if (oclass.isAssignableFrom(livingentity.getClass())) {
                    return false;
                }
            }
            return this.canAttack(livingentity, field_220795_a);
        } else {
            return false;
        }
    }

    public HurtPackByTargetGoal setAlertOthers(Class<?>... reinforcementTypes) {
        this.entityCallsForHelp = true;
        this.reinforcementTypes = reinforcementTypes;
        return this;
    }

    public void start() {
        this.mob.setTarget(this.mob.getLastHurtByMob());
        this.targetMob = this.mob.getTarget();
        this.revengeTimerOld = this.mob.getLastHurtByMobTimestamp();
        this.unseenMemoryTicks = 300;
        if (this.entityCallsForHelp) {
            this.alertOthers();
        }

        super.start();
    }

    protected void alertOthers() {
        if (this.mob instanceof ComplexMob) {
            ComplexMob mob = (ComplexMob)this.mob;
            if (mob.herd != null) {
                for (ComplexMob creature : mob.herd.creatureList) {
                    creature.setTarget(this.mob.getLastHurtByMob());
                }
            }
        }
    }
}

