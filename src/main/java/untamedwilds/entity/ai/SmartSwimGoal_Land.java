package untamedwilds.entity.ai;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.mammal.EntityBear;
import untamedwilds.util.EntityUtils;

import java.util.EnumSet;
import java.util.List;

public class SmartSwimGoal_Land extends Goal {

    private final ComplexMob entity;
    private final float speed;

    public SmartSwimGoal_Land(ComplexMob entityIn) {
        this(entityIn, 0.7f);
    }

    public SmartSwimGoal_Land(ComplexMob entityIn, float speedIn) {
        this.entity = entityIn;
        this.speed = speedIn;
        this.setFlags(EnumSet.of(Flag.JUMP));
        entityIn.getNavigation().setCanFloat(true);
    }

    @Override
    public boolean canUse() {
        if (this.entity.isInWater() && this.entity.getTarget() == null && this.entity.getNavigation().canFloat()) {
            double eyeHeight = (double) this.entity.getEyeHeight() - (this.entity.isBaby() ? -0.8 : 0.18F); // Tiny offset because otherwise the Mob drowns
            return this.entity.getFluidHeight(FluidTags.WATER) > eyeHeight || this.entity.isInLava();
        }
        return false;
    }

    @Override
    public void start() {
        if (!this.entity.canMove()) {
            this.entity.setSleeping(false);
            this.entity.setSitting(false);
            if (this.entity.getCommandInt() == 2) {
                this.entity.setCommandInt(0);
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.entity.isOnGround() && this.entity.isInWater() && this.entity.getTarget() == null;
    }

    @Override
    public void tick() {
        if (this.entity.getNavigation().isDone()) {
            this.entity.getMoveControl().strafe(this.speed, 0);
        }
        boolean colliding = this.entity.level.collidesWithSuffocatingBlock(this.entity, this.entity.getBoundingBox().expandTowards(this.entity.getLookAngle()));
        if (this.entity.isEyeInFluid(FluidTags.WATER) || colliding) {
            this.entity.getJumpControl().jump();
        }
        if (this.entity.tickCount % 6 == 0) {
            EntityUtils.spawnParticlesOnEntity(this.entity.level, this.entity, ParticleTypes.SPLASH, 4, 2);
        }
    }
    /*public SmartSwimGoal_Land(ComplexMob entityIn) {
        this(entityIn, 0.7f);
    }

    public SmartSwimGoal_Land(ComplexMob entityIn, float speedIn) {
        this.entity = entityIn;
        this.speed = speedIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
        entityIn.getNavigation().setCanFloat(true);
    }

    public boolean canUse() {
        return this.entity.isInWater() && this.entity.getFluidHeight(FluidTags.WATER) > this.entity.getFluidJumpThreshold() || this.entity.isInLava();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        if (this.entity.getRandom().nextFloat() < 0.8F) {
            this.entity.getJumpControl().jump();
        }
    }*/
}
    /*public SmartSwimGoal_Land(ComplexMob entityIn) {
        this(entityIn, 0.7f);
    }

    public SmartSwimGoal_Land(ComplexMob entityIn, float speedIn) {
        this.entity = entityIn;
        this.speed = speedIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
        entityIn.getNavigation().setCanFloat(true);
    }

    @Override
    public boolean canUse() {
        if (this.entity.isInWater() && this.entity.getTarget() == null && this.entity.getNavigation().canFloat()) {
            double eyeHeight = (double) this.entity.getEyeHeight() - (this.entity.isBaby() ? -0.8 : 0.18F); // Tiny offset because otherwise the Mob drowns
            return true;
            //return this.entity.getFluidHeight(FluidTags.WATER) > eyeHeight || this.entity.isInLava();
        }
        return false;
    }

    @Override
    public void start() {
        if (!this.entity.canMove()) {
            this.entity.setSleeping(false);
            this.entity.setSitting(false);
            if (this.entity.getCommandInt() == 2) {
                this.entity.setCommandInt(0);
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.entity.isOnGround() && this.entity.isInWater() && this.entity.getTarget() == null;
    }

    @Override
    public void tick() {
        this.entity.getMoveControl().strafe(this.speed, 0);

        if (this.entity.getNavigation().isDone()) {
            this.entity.getMoveControl().strafe(this.speed, 0);
            //this.entity.getMoveControl().strafe(this.speed, 0);
        }
        if (this.entity.isEyeInFluid(FluidTags.WATER) || this.entity.horizontalCollision) {
            this.entity.getJumpControl().jump();
        }
        if (this.entity.tickCount % 6 == 0) {
            EntityUtils.spawnParticlesOnEntity(this.entity.level, this.entity, ParticleTypes.SPLASH, 4, 2);
        }
    }
}*/