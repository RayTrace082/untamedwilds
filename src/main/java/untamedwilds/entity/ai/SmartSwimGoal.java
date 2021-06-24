package untamedwilds.entity.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import untamedwilds.entity.ComplexMobTerrestrial;
import untamedwilds.util.EntityUtils;

import java.util.EnumSet;

public class SmartSwimGoal extends Goal {

    private final MobEntity entity;
    private final float speed;

    // TODO: Make use of getNavigator().getCanSwim() to check whether a mob should be able to swim or not
    public SmartSwimGoal(MobEntity entityIn) {
        this(entityIn, 0.7f);
    }

    public SmartSwimGoal(MobEntity entityIn, float speedIn) {
        this.entity = entityIn;
        this.speed = speedIn;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
        entityIn.getNavigator().setCanSwim(true);
    }

    @Override
    public boolean shouldExecute() {
        if (this.entity.getAttackTarget() == null) {
            double eyeHeight = (double) this.entity.getEyeHeight() - 0.18F; // Tiny offset because otherwise the Mob drowns
            return this.entity.func_233571_b_(FluidTags.WATER) > eyeHeight || this.entity.isInLava();
        }
        return false;
    }

    @Override
    public void startExecuting() {
        if (this.entity instanceof ComplexMobTerrestrial && !((ComplexMobTerrestrial) this.entity).canMove()) {
            ComplexMobTerrestrial entityIn = (ComplexMobTerrestrial)this.entity;
            entityIn.setSleeping(false);
            entityIn.setSitting(false);
            if (entityIn.getCommandInt() == 2) {
                entityIn.setCommandInt(0);
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.entity.isOnGround() && this.entity.isInWater() && this.entity.getAttackTarget() == null;
    }

    @Override
    public void tick() {
        if (this.entity.getNavigator().noPath()) {
            this.entity.getMoveHelper().strafe(this.speed, 0);
        }
        if (this.entity.areEyesInFluid(FluidTags.WATER) || this.entity.collidedHorizontally) {
            this.entity.getJumpController().setJumping();
        }
        if (this.entity.ticksExisted % 6 == 0) {
            EntityUtils.spawnParticlesOnEntity(this.entity.world, this.entity, ParticleTypes.SPLASH, 4, 2);
        }
    }
}