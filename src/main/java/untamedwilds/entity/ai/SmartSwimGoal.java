package untamedwilds.entity.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import untamedwilds.util.EntityUtils;

import java.util.EnumSet;

public class SmartSwimGoal extends Goal {

    private final MobEntity entity;
    private final float speed;

    public SmartSwimGoal(MobEntity entityIn) {
        this(entityIn, 0.7f);
    }

    public SmartSwimGoal(MobEntity entityIn, float speedIn) {
        this.entity = entityIn;
        this.speed = speedIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP));
        entityIn.getNavigator().setCanSwim(true);
    }

    public boolean shouldExecute() {
        if (this.entity.getAttackTarget() == null) {
            double eyeHeight = (double) this.entity.getEyeHeight() - 0.18F; // Tiny offset because otherwise the Mob is prone to drowning
            return this.entity.getSubmergedHeight() > eyeHeight || this.entity.isInLava();
        }
        return false;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.onGround && !this.entity.isInWater();
    }

    public void tick() {
        //if (!this.entity.collidedVertically) {
            this.entity.getMoveHelper().strafe(this.speed, 0);
        //}
        if (this.entity.areEyesInFluid(FluidTags.WATER) || this.entity.collidedHorizontally) {
            //this.entity.setMotion(this.entity.getMotion().getX(), this.entity.getMotion().getY() + 0.2F, this.entity.getMotion().getZ());
            this.entity.getJumpController().setJumping();
        }
        if (this.entity.ticksExisted % 6 == 0) {
            EntityUtils.spawnParticlesOnEntity(this.entity.world, this.entity, ParticleTypes.SPLASH, 4, 1);
        }
        /*if (this.entity.areEyesInFluid(FluidTags.WATER)) {
            this.entity.getJumpController().setJumping();
        }
        if (this.entity.collidedHorizontally) {
            this.entity.setMotion(this.entity.getMotion().getX(), this.entity.getMotion().getY() + 0.2F, this.entity.getMotion().getZ());
        }*/
    }
}