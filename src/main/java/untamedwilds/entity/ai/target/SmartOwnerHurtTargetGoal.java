package untamedwilds.entity.ai.target;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import untamedwilds.entity.ComplexMob;

import java.util.EnumSet;

public class SmartOwnerHurtTargetGoal extends TargetGoal {
    private final ComplexMob tameable;
    private LivingEntity attacker;
    private int timestamp;

    public SmartOwnerHurtTargetGoal(ComplexMob entityIn) {
        super(entityIn, false);
        this.tameable = entityIn;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
        if (this.tameable.isTame() && !this.tameable.isOrderedToSit()) {
            LivingEntity livingentity = this.tameable.getOwner();
            if (livingentity == null) {
                return false;
            } else {
                this.attacker = livingentity.getLastHurtMob();
                int i = livingentity.getLastHurtMobTimestamp();
                return i != this.timestamp && this.canAttack(this.attacker, TargetingConditions.DEFAULT) && this.shouldAttackEntity(this.attacker, livingentity);
            }
        } else {
            return false;
        }
    }

    public void start() {
        this.mob.setTarget(this.attacker);
        LivingEntity livingentity = this.tameable.getOwner();
        if (livingentity != null) {
            this.timestamp = livingentity.getLastHurtMobTimestamp();
        }

        super.start();
    }

    // Taken from WolfEntity
    private boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
        if (!(target instanceof Creeper) && !(target instanceof Ghast)) {
            if (target instanceof TamableAnimal tamableTarget) {
                if (tamableTarget.isTame() && tamableTarget.getOwner() == owner) {
                    return false;
                }
            }

            if (target instanceof Player && owner instanceof Player && !owner.canAttack(owner)) {
                return false;
            } else return !(target instanceof Horse) || !((Horse) target).isTamed();
        }
        return false;
    }
}