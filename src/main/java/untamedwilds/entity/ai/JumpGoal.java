package untamedwilds.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
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

    public boolean canUse() {
        if (this.taskOwner.getRandom().nextInt(this.chance) != 0) {
            return false;
        } else {
            Direction direction = this.taskOwner.getMotionDirection();
            int i = direction.getStepX();
            int j = direction.getStepZ();
            BlockPos blockpos = this.taskOwner.blockPosition();

            for(int k : JUMP_DISTANCES) {
                if (!this.isAirAbove(blockpos, i, j, k) || (this.safeJumping && !this.canJumpTo(blockpos, i, j, k))) {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean canJumpTo(BlockPos pos, int dx, int dz, int scale) {
        BlockPos blockpos = pos.offset(dx * scale, 0, dz * scale);
        return this.taskOwner.level.getFluidState(blockpos).is(FluidTags.WATER) && !this.taskOwner.level.getBlockState(blockpos).getMaterial().blocksMotion();
    }

    private boolean isAirAbove(BlockPos pos, int dx, int dz, int scale) {
        return this.taskOwner.level.getBlockState(pos.offset(dx * scale, 1, dz * scale)).isAir() && this.taskOwner.level.getBlockState(pos.offset(dx * scale, 2, dz * scale)).isAir();
    }

    public boolean canContinueToUse() {
        double d0 = this.taskOwner.getDeltaMovement().y;
        return (!(d0 * d0 < (double)0.03F) || this.taskOwner.getXRot() == 0.0F || !(Math.abs(this.taskOwner.getXRot()) < 10.0F) || !this.taskOwner.isInWater()) && !this.taskOwner.isOnGround();
    }

    public boolean isInterruptable() {
        return false;
    }

    public void start() {
        Direction direction = this.taskOwner.getMotionDirection();
        this.taskOwner.setDeltaMovement(this.taskOwner.getDeltaMovement().add((double)direction.getStepX() * 0.6D, 0.6D, (double)direction.getStepZ() * 0.6D));
        this.taskOwner.getNavigation().stop();
    }

    public void stop() {
        this.taskOwner.setXRot(0.0F);
    }

    public void tick() {
        boolean flag = this.inWater;
        if (!flag) {
            FluidState fluidstate = this.taskOwner.level.getFluidState(this.taskOwner.blockPosition());
            this.inWater = fluidstate.is(FluidTags.WATER);
        }

        if (this.inWater && !flag) {
            this.taskOwner.playSound(SoundEvents.GOAT_LONG_JUMP, 1.0F, 1.0F);
        }

        Vec3 vec3 = this.taskOwner.getDeltaMovement();
        if (vec3.y * vec3.y < (double)0.03F && this.taskOwner.getXRot() != 0.0F) {
            this.taskOwner.setXRot(Mth.rotlerp(this.taskOwner.getXRot(), 0.0F, 0.2F));
        } else if (vec3.length() > (double)1.0E-5F) {
            double d0 = vec3.horizontalDistance();
            double d1 = Math.atan2(-vec3.y, d0) * (double)(180F / (float)Math.PI);
            this.taskOwner.setXRot((float)d1);
        }
    }
}