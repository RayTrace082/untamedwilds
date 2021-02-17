package untamedwilds.entity.ai.target;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.world.GameRules;
import untamedwilds.entity.ComplexMob;

import java.util.EnumSet;

public class HurtPackByTargetGoal extends TargetGoal {
    private static final EntityPredicate field_220795_a = (EntityPredicate.DEFAULT).setLineOfSiteRequired().setUseInvisibilityCheck();
    private boolean entityCallsForHelp;
    private int revengeTimerOld;
    private final Class<?>[] excludedReinforcementTypes;
    private Class<?>[] reinforcementTypes;

    public HurtPackByTargetGoal(CreatureEntity creatureIn, Class<?>... excludeReinforcementTypes) {
        super(creatureIn, true);
        this.excludedReinforcementTypes = excludeReinforcementTypes;
        this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    public boolean shouldExecute() {
        int i = this.goalOwner.getRevengeTimer();
        LivingEntity livingentity = this.goalOwner.getRevengeTarget();
        if (i != this.revengeTimerOld && livingentity != null) {
            if (livingentity.getType() == EntityType.PLAYER && this.goalOwner.world.getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER)) {
                return false;
            } else {
                for(Class<?> oclass : this.excludedReinforcementTypes) {
                    if (oclass.isAssignableFrom(livingentity.getClass())) {
                        return false;
                    }
                }

                return this.isSuitableTarget(livingentity, field_220795_a);
            }
        } else {
            return false;
        }
    }

    public HurtPackByTargetGoal setCallsForHelp(Class<?>... reinforcementTypes) {
        this.entityCallsForHelp = true;
        this.reinforcementTypes = reinforcementTypes;
        return this;
    }

    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.goalOwner.getRevengeTarget());
        this.target = this.goalOwner.getAttackTarget();
        this.revengeTimerOld = this.goalOwner.getRevengeTimer();
        this.unseenMemoryTicks = 300;
        if (this.entityCallsForHelp) {
            this.alertOthers();
        }

        super.startExecuting();
    }

    protected void alertOthers() {
        if (this.goalOwner instanceof ComplexMob) {
            ComplexMob taskOwner = (ComplexMob)this.goalOwner;
            if (taskOwner.herd != null) {
                for (ComplexMob creature : taskOwner.herd.creatureList) {
                    creature.setAttackTarget(this.goalOwner.getRevengeTarget());
                }
            }
        }
    }
}

