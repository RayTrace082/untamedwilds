package untamedwilds.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import untamedwilds.entity.ComplexMobAmphibious;

import java.util.EnumSet;
import java.util.Random;

public class AmphibiousTransition extends Goal {
    protected final ComplexMobAmphibious taskOwner;
    private final double movementSpeed;
    private final World world;
    private double shelterX;
    private double shelterY;
    private double shelterZ;

    public AmphibiousTransition(ComplexMobAmphibious taskOwner, double movementSpeedIn) {
        this.taskOwner = taskOwner;
        this.movementSpeed = movementSpeedIn;
        this.world = taskOwner.world;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (taskOwner.isBeingRidden() || !taskOwner.canMove() || taskOwner.getAttackTarget() != null /* && !taskOwner.getAttackTarget().isInWater()*/) {
            return false;
        }

        Vec3d vec3d = null;
        if (taskOwner.wantsToEnterWater() && !taskOwner.isInWater()) {
            vec3d = this.findWaterShelter();
        }
        else if (taskOwner.wantsToLeaveWater() && taskOwner.isInWater()) {
            vec3d = this.findLandShelter();
        }

        if (vec3d == null) {
            return false;
        } else {
            this.shelterX = vec3d.x;
            this.shelterY = vec3d.y;
            this.shelterZ = vec3d.z;
            return true;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.taskOwner.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        this.taskOwner.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }

    protected Vec3d findWaterShelter() {
        Random random = this.taskOwner.getRNG();
        BlockPos blockpos = new BlockPos(this.taskOwner.getPosX(), this.taskOwner.getBoundingBox().minY, this.taskOwner.getPosZ());

        for (int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);

            if (this.world.getFluidState(blockpos1).isTagged(FluidTags.WATER)) {
                return new Vec3d((double) blockpos1.getX() + 0.5D, blockpos1.getY(), (double) blockpos1.getZ() + 0.5D);
            }
        }

        return null;
    }

    private Vec3d findLandShelter() {
        Random random = this.taskOwner.getRNG();
        BlockPos blockpos = new BlockPos(this.taskOwner.getPosX(), this.taskOwner.getBoundingBox().minY, this.taskOwner.getPosZ());

        for (int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6), random.nextInt(20) - 10);
            if (this.world.getFluidState(blockpos1).isEmpty() && this.world.getBlockState(blockpos1).allowsMovement(this.world, blockpos1, PathType.LAND) && this.world.getBlockState(blockpos1.down()).isSolid() && this.world.getFluidState(blockpos1.down()).isEmpty()) {
                //this.world.setBlockState(blockpos1, Blocks.TORCH.getDefaultState());
                return new Vec3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
            }
        }

        return null;
    }
}
