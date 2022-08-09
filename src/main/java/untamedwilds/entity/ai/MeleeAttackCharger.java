package untamedwilds.entity.ai;

import com.mojang.math.Vector3d;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import untamedwilds.entity.ComplexMobTerrestrial;
import untamedwilds.entity.mammal.EntityBison;
import untamedwilds.entity.mammal.EntityRhino;
import untamedwilds.util.EntityUtils;

import java.util.EnumSet;
import java.util.List;

public class MeleeAttackCharger extends Goal {

    private final int executionChance;
    private final float speed;
    private final ComplexMobTerrestrial taskOwner;
    private double chargeX;
    private double chargeY;
    private double chargeZ;
    private int charge;

    public MeleeAttackCharger(ComplexMobTerrestrial entityIn, float speedIn, int chance) {
        this.taskOwner = entityIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.charge = 0;
        this.setFlags(EnumSet.of(Flag.TARGET, Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        LivingEntity chargeTarget = this.taskOwner.getTarget();

        if (this.taskOwner.isBaby() || chargeTarget == null || !this.taskOwner.isOnGround() || this.taskOwner.getRandom().nextInt(this.executionChance) != 0) {
            return false;
        } else {
            double distance = this.taskOwner.distanceTo(chargeTarget);
            if (distance < 2 || distance > 24 || !this.taskOwner.isOnGround()) {
                return false;
            } else {
                Vec3 chargePos = EntityUtils.getOvershootPath(taskOwner, chargeTarget, 10);
                boolean canSeeTargetFromDest = taskOwner.getSensing().hasLineOfSight(chargeTarget);
                if (canSeeTargetFromDest) {
                    chargeX = chargePos.x;
                    chargeY = chargePos.y;
                    chargeZ = chargePos.z;

                    return true;
                }
                return false;
            }
        }
    }

    public void start() {
        this.charge = 50;
        if (this.taskOwner instanceof EntityBison)
            this.taskOwner.setAnimation(EntityBison.ATTACK_THREATEN);
        if (this.taskOwner instanceof EntityRhino)
            this.taskOwner.setAnimation(EntityRhino.ATTACK_THREATEN);
    }

    public boolean canContinueToUse() {
        return charge > 0 || !this.taskOwner.getNavigation().isDone();
    }

    public void tick() {
        this.taskOwner.getLookControl().setLookAt(chargeX, chargeY - 1, chargeZ);
        if (charge > 0) {
            if (--charge == 0) {
                this.taskOwner.getNavigation().moveTo(chargeX, chargeY, chargeZ, this.speed * 1.2F);
            } else {
                this.taskOwner.setSprinting(true);
            }
        } else { // AABB checking
            AABB offset_box = this.taskOwner.getBoundingBox().move(Math.cos(Math.toRadians(this.taskOwner.getYRot() + 90)) * 1.2, 0, Math.sin(Math.toRadians(this.taskOwner.getYRot() + 90)) * 1.2);
            //AxisAlignedBB offset_box = this.taskOwner.getBoundingBox().offset(Math.cos(this.taskOwner.getYRot() * ((float)Math.PI / 180F)), 0, Math.sin(this.taskOwner.getYRot() * ((float)Math.PI / 180F)));
            /*for (int i = 0; i < 4;  i++) {
                ((ServerWorld)this.taskOwner.world).spawnParticle(ParticleTypes.SOUL_FIRE_FLAME, offset_box.minX, offset_box.minY, offset_box.minZ, 1, 0, 0, 0, 0.05D);
                ((ServerWorld)this.taskOwner.world).spawnParticle(ParticleTypes.SOUL_FIRE_FLAME, offset_box.maxX, offset_box.maxY, offset_box.maxZ, 1, 0, 0, 0, 0.05D);
            }*/
            List<LivingEntity> entitiesHit = this.taskOwner.getLevel().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), this.taskOwner, offset_box);
            for (LivingEntity entityHit : entitiesHit) {
                if (!(entityHit instanceof EntityRhino) && !entityHit.equals(this.taskOwner) && this.taskOwner.hasLineOfSight(entityHit)) {
                    this.taskOwner.doHurtTarget(entityHit);
                }
            }
        }
    }

    public void stop() {
        this.charge = 0;
        this.taskOwner.setSprinting(false);
    }
}
