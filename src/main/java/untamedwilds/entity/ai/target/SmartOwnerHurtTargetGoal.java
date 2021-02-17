package untamedwilds.entity.ai.target;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import untamedwilds.entity.ComplexMob;

import java.util.EnumSet;

public class SmartOwnerHurtTargetGoal extends TargetGoal {
    private final ComplexMob tameable;
    private LivingEntity attacker;
    private int timestamp;

    public SmartOwnerHurtTargetGoal(ComplexMob entityIn) {
        super(entityIn, false);
        this.tameable = entityIn;
        this.setMutexFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean shouldExecute() {
        if (this.tameable.isTamed() && !this.tameable.isSleeping() && !this.tameable.isChild()) {
            LivingEntity owner = this.tameable.getOwner();
            if (owner == null) {
                return false;
            } else {
                this.attacker = owner.getLastAttackedEntity();
                int lvt_2_1_ = owner.getLastAttackedEntityTime();
                return lvt_2_1_ != this.timestamp && this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT) && shouldAttackEntity(this.attacker, owner);
            }
        } else {
            return false;
        }
    }

    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.attacker);
        LivingEntity lvt_1_1_ = this.tameable.getOwner();
        if (lvt_1_1_ != null) {
            this.timestamp = lvt_1_1_.getLastAttackedEntityTime();
        }

        super.startExecuting();
    }

    // Taken from WolfEntity
    private boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
        if (!(target instanceof CreeperEntity) && !(target instanceof GhastEntity)) {
            if (target instanceof TameableEntity) {
                TameableEntity tameableTarget = (TameableEntity)target;
                if (tameableTarget.isTamed() && tameableTarget.getOwner() == owner) {
                    return false;
                }
            }

            if (target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity)owner).canAttackPlayer((PlayerEntity)owner)) {
                return false;
            } else if (target instanceof AbstractHorseEntity && ((AbstractHorseEntity)target).isTame()) {
                return false;
            }
        }
        return false;
    }
}