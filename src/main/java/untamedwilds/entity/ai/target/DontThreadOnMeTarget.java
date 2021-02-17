package untamedwilds.entity.ai.target;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.EntityPredicates;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ICritter;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class DontThreadOnMeTarget<T extends LivingEntity> extends TargetGoal {
    protected final Class<T> targetClass;
    protected final int targetChance;
    protected LivingEntity nearestTarget;
    protected Predicate<T> targetEntitySelector;

    public DontThreadOnMeTarget(MobEntity goalOwnerIn, Class<T> targetClassIn, boolean checkSight) {
        this(goalOwnerIn, targetClassIn, checkSight, false);
    }

    public DontThreadOnMeTarget(MobEntity goalOwnerIn, Class<T> targetClassIn, boolean checkSight, boolean nearbyOnlyIn) {
        this(goalOwnerIn, targetClassIn, 6, checkSight, nearbyOnlyIn);
    }

    public DontThreadOnMeTarget(MobEntity goalOwnerIn, Class<T> targetClassIn, int targetChanceIn, boolean checkSight, boolean nearbyOnlyIn) {
        super(goalOwnerIn, checkSight, nearbyOnlyIn);
        this.targetClass = targetClassIn;
        this.targetChance = targetChanceIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
        this.targetEntitySelector = entity -> {
            if (entity instanceof CreeperEntity) {
                return false;
            }
            if (this.goalOwner.getClass() == entity.getClass()) {
                if (this.goalOwner instanceof ICritter && entity instanceof ICritter) {
                    ComplexMob attacker = ((ComplexMob)this.goalOwner);
                    ComplexMob defender = ((ComplexMob)entity);
                    if (attacker.getSpecies() == defender.getSpecies()) {
                        return false;
                    }
                }
                return false;
            }
            return EntityPredicates.NOT_SPECTATING.test(entity) && this.isSuitableTarget(entity, EntityPredicate.DEFAULT);
        };}

    public boolean shouldExecute() {
        if (this.goalOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        } else {
            List<T> list = this.goalOwner.world.getEntitiesWithinAABB(this.targetClass, this.goalOwner.getBoundingBox(), this.targetEntitySelector);
            if (list.isEmpty()) {
                return false;
            }
            else {
                this.nearestTarget = list.get(0);
                return true;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.nearestTarget);
        super.startExecuting();
    }
}