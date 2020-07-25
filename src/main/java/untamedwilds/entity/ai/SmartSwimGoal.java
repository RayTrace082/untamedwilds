package untamedwilds.entity.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tags.FluidTags;

import java.util.EnumSet;

public class SmartSwimGoal extends Goal {

    private final MobEntity entity;
    private float speed;

    public SmartSwimGoal(MobEntity entityIn) {
        this(entityIn, 1.0f);
    }

    public SmartSwimGoal(MobEntity entityIn, float speedIn) {
        this.entity = entityIn;
        this.speed = speedIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP));
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        entityIn.getNavigator().setCanSwim(true);
    }

    public boolean shouldExecute() {
        double eyeHeight = (double) this.entity.getEyeHeight() - 0.18F; // Tiny offset because otherwise the Mob is prone to drowning
        return this.entity.onGround && (this.entity.isInWater() || this.entity.isInLava());
    }

    public void tick() {
        if (!this.entity.collidedVertically) {
            this.entity.getMoveHelper().strafe(this.speed, 0);
        }
        if (this.entity.areEyesInFluid(FluidTags.WATER) || this.entity.collidedHorizontally) {
            this.entity.getJumpController().setJumping();
        }
    }
}