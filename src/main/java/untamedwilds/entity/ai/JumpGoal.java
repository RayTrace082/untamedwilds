package untamedwilds.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.fluid.FluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAquatic;

public class JumpGoal extends Goal {
    private static final int[] JUMP_DISTANCES = new int[]{0, 1, 3, 4}; // Vanilla implementation {0, 1, 4, 5, 6, 7}
    private final ComplexMob taskOwner;
    private final int chance;
    private boolean inWater;
    private final boolean safeJumping;

    public JumpGoal(ComplexMobAquatic entityIn, int chance) {
        this(entityIn, chance, true);
    }

    public JumpGoal(ComplexMobAquatic entityIn, int chance, boolean safeJumping) {
        this.taskOwner = entityIn;
        this.chance = chance;
        this.safeJumping = safeJumping;
    }

    public boolean shouldExecute() {
        if (this.taskOwner.getRNG().nextInt(this.chance) != 0) {
            return false;
        } else {
            Direction direction = this.taskOwner.getAdjustedHorizontalFacing();
            int i = direction.getXOffset();
            int j = direction.getZOffset();
            BlockPos blockpos = this.taskOwner.getPosition();

            for(int k : JUMP_DISTANCES) {
                if (!this.isAirAbove(blockpos, i, j, k) || (this.safeJumping && !this.canJumpTo(blockpos, i, j, k))) {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean canJumpTo(BlockPos pos, int dx, int dz, int scale) {
        BlockPos blockpos = pos.add(dx * scale, 0, dz * scale);
        return this.taskOwner.world.getFluidState(blockpos).isTagged(FluidTags.WATER) && !this.taskOwner.world.getBlockState(blockpos).getMaterial().blocksMovement();
    }

    private boolean isAirAbove(BlockPos pos, int dx, int dz, int scale) {
        return this.taskOwner.world.getBlockState(pos.add(dx * scale, 1, dz * scale)).isAir() && this.taskOwner.world.getBlockState(pos.add(dx * scale, 2, dz * scale)).isAir();
    }

    public boolean shouldContinueExecuting() {
        double d0 = this.taskOwner.getMotion().y;
        return (!(d0 * d0 < (double)0.03F) || this.taskOwner.rotationPitch == 0.0F || !(Math.abs(this.taskOwner.rotationPitch) < 10.0F) || !this.taskOwner.isInWater()) && !this.taskOwner.isOnGround();
    }

    public boolean isPreemptible() {
        return false;
    }

    public void startExecuting() {
        Direction direction = this.taskOwner.getAdjustedHorizontalFacing();
        this.taskOwner.setMotion(this.taskOwner.getMotion().add((double)direction.getXOffset() * 0.6D, 0.6D, (double)direction.getZOffset() * 0.6D));
        this.taskOwner.getNavigator().clearPath();
    }

    public void resetTask() {
        this.taskOwner.rotationPitch = 0.0F;
    }

    public void tick() {
        boolean flag = this.inWater;
        if (!flag) {
            FluidState fluidstate = this.taskOwner.world.getFluidState(this.taskOwner.getPosition());
            this.inWater = fluidstate.isTagged(FluidTags.WATER);
        }

        if (this.inWater && !flag) {
            this.taskOwner.playSound(SoundEvents.ENTITY_DOLPHIN_JUMP, 1.0F, 1.0F);
        }

        Vector3d vector3d = this.taskOwner.getMotion();
        if (vector3d.y * vector3d.y < (double)0.03F && this.taskOwner.rotationPitch != 0.0F) {
            this.taskOwner.rotationPitch = MathHelper.rotLerp(this.taskOwner.rotationPitch, 0.0F, 0.2F);
        } else {
            double d0 = Math.sqrt(Entity.horizontalMag(vector3d));
            double d1 = Math.signum(-vector3d.y) * Math.acos(d0 / vector3d.length()) * (double)(180F / (float)Math.PI);
            this.taskOwner.rotationPitch = (float)d1;
        }
    }
}