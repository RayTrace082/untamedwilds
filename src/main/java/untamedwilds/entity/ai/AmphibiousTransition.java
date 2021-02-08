package untamedwilds.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import untamedwilds.entity.ComplexMobAmphibious;

import java.util.EnumSet;
import java.util.Random;

public class AmphibiousTransition extends Goal {
    private final ComplexMobAmphibious taskOwner;
    private final double movementSpeed;
    private final int executionChance;
    private final World world;
    private Vector3d shelter;

    public AmphibiousTransition(ComplexMobAmphibious taskOwner, double movementSpeedIn) {
        this(taskOwner, movementSpeedIn, 120);
    }

    public AmphibiousTransition(ComplexMobAmphibious taskOwner, double movementSpeedIn, int chance) {
        this.taskOwner = taskOwner;
        this.movementSpeed = movementSpeedIn;
        this.executionChance = chance;
        this.world = taskOwner.world;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    @Override
    public boolean shouldExecute() {
        if (taskOwner.isBeingRidden() || !taskOwner.canMove() || taskOwner.getAttackTarget() != null /* && !taskOwner.getAttackTarget().isInWater()*/) {
            return false;
        }
        if (this.taskOwner.getRNG().nextInt(this.executionChance) != 0) { return false; }

        Vector3d vec3d = null;
        if (taskOwner.wantsToEnterWater() && !taskOwner.isInWater()) {
            vec3d = this.findWaterPos();
        }
        else if (taskOwner.wantsToLeaveWater() && taskOwner.isInWater()) {
            vec3d = this.findLandPos();
        }

        this.shelter = vec3d;
        return vec3d != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.taskOwner.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        this.taskOwner.getNavigator().tryMoveToXYZ(this.shelter.getX(), this.shelter.getY(), this.shelter.getZ(), this.movementSpeed);
    }

    protected Vector3d findWaterPos() {
        Random random = this.taskOwner.getRNG();
        BlockPos blockpos = new BlockPos(this.taskOwner.getPosX(), this.taskOwner.getBoundingBox().minY, this.taskOwner.getPosZ());

        for (int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);

            if (this.world.getFluidState(blockpos1).isTagged(FluidTags.WATER)) {
                return new Vector3d(blockpos1.getX() + 0.5D, blockpos1.getY(), blockpos1.getZ() + 0.5D);
            }
        }

        return null;
    }

    private Vector3d findLandPos() {
        Random random = this.taskOwner.getRNG();
        BlockPos blockpos = new BlockPos(this.taskOwner.getPosX(), this.taskOwner.getBoundingBox().minY, this.taskOwner.getPosZ());

        for (int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6), random.nextInt(20) - 10);

            if (this.world.getFluidState(blockpos1).isEmpty() && this.world.getBlockState(blockpos1).allowsMovement(this.world, blockpos1, PathType.LAND) && this.world.getBlockState(blockpos1.down()).isSolid()) {
                return new Vector3d(blockpos1.getX() + 0.5D, blockpos1.getY(), blockpos1.getZ() + 0.5D);
            }
        }

        return null;
    }
}
