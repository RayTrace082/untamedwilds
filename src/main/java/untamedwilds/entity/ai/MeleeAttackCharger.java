package untamedwilds.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import untamedwilds.entity.mammal.EntityRhino;
import untamedwilds.util.EntityUtils;

import java.util.EnumSet;
import java.util.List;

// TODO: Detach this class to work with anything, and not just Rhinos
public class MeleeAttackCharger extends Goal {

    private final int executionChance;
    private final float speed;
    private final EntityRhino taskOwner;
    private double chargeX;
    private double chargeY;
    private double chargeZ;
    private int charge;

    public MeleeAttackCharger(EntityRhino entityIn, float speedIn, int chance) {
        this.taskOwner = entityIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.charge = 0;
        this.setMutexFlags(EnumSet.of(Flag.TARGET, Flag.MOVE, Flag.LOOK));
    }

    public boolean shouldExecute() {
        LivingEntity chargeTarget = this.taskOwner.getAttackTarget();

        if (this.taskOwner.isChild() || chargeTarget == null || !this.taskOwner.isOnGround() || this.taskOwner.getRNG().nextInt(this.executionChance) != 0) {
            return false;
        } else {
            double distance = this.taskOwner.getDistance(chargeTarget);
            if (distance < 2 || distance > 24 || !this.taskOwner.isOnGround()) {
                return false;
            } else {
                Vector3d chargePos = EntityUtils.getOvershootPath(taskOwner, chargeTarget, 10);
                boolean canSeeTargetFromDest = taskOwner.getEntitySenses().canSee(chargeTarget);
                if (canSeeTargetFromDest) {
                    chargeX = chargePos.getX();
                    chargeY = chargePos.getY();
                    chargeZ = chargePos.getZ();

                    return true;
                }
                return false;
            }
        }
    }

    public void startExecuting() {
        this.charge = 50;
        this.taskOwner.setAnimation(EntityRhino.ATTACK_THREATEN);
    }

    public boolean shouldContinueExecuting() {
        return charge > 0 || !this.taskOwner.getNavigator().noPath();
    }

    public void tick() {
        this.taskOwner.getLookController().setLookPosition(chargeX, chargeY - 1, chargeZ);
        if (charge > 0) {
            if (--charge == 0) {
                this.taskOwner.getNavigator().tryMoveToXYZ(chargeX, chargeY, chargeZ, this.speed * 1.2F);
            } else {
                this.taskOwner.setCharging(true);
                this.taskOwner.setSprinting(true);
            }
        } else if (this.taskOwner.isCharging()) { // AABB checking
            AxisAlignedBB offset_box = this.taskOwner.getBoundingBox().offset(Math.cos(Math.toRadians(this.taskOwner.rotationYaw + 90)) * 1.2, 0, Math.sin(Math.toRadians(this.taskOwner.rotationYaw + 90)) * 1.2);
            //AxisAlignedBB offset_box = this.taskOwner.getBoundingBox().offset(Math.cos(this.taskOwner.rotationYaw * ((float)Math.PI / 180F)), 0, Math.sin(this.taskOwner.rotationYaw * ((float)Math.PI / 180F)));
            /*for (int i = 0; i < 4;  i++) {
                ((ServerWorld)this.taskOwner.world).spawnParticle(ParticleTypes.SOUL_FIRE_FLAME, offset_box.minX, offset_box.minY, offset_box.minZ, 1, 0, 0, 0, 0.05D);
                ((ServerWorld)this.taskOwner.world).spawnParticle(ParticleTypes.SOUL_FIRE_FLAME, offset_box.maxX, offset_box.maxY, offset_box.maxZ, 1, 0, 0, 0, 0.05D);
            }*/
            List<LivingEntity> entitiesHit = this.taskOwner.getEntityWorld().getEntitiesWithinAABB(LivingEntity.class, offset_box, EntityPredicates.CAN_AI_TARGET);
            for (LivingEntity entityHit : entitiesHit) {
                if (!(entityHit instanceof EntityRhino)) {
                    this.taskOwner.attackEntityAsMob(entityHit);
                }
            }
        }
    }

    public void resetTask() {
        this.charge = 0;
        this.taskOwner.setCharging(false);
        this.taskOwner.setSprinting(false);
    }
}
