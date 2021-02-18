package untamedwilds.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.LightType;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMobTerrestrial;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class GotoSleepGoal extends Goal {

    private final ComplexMobTerrestrial creature;
    protected Vector3f target;
    private final int executionChance;
    private final double speed;

    public GotoSleepGoal(ComplexMobTerrestrial entityIn, double speedIn) {
        this(entityIn, speedIn, 120);
    }

    public GotoSleepGoal(ComplexMobTerrestrial entityIn, double speedIn, int chance) {
        this.creature = entityIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean shouldExecute() {
        if (this.creature.isTamed() && this.creature.getCommandInt() != 0) {
            return false;
        }
        if (this.creature.isActive() || this.creature.isBeingRidden() || !ConfigGamerules.sleepBehaviour.get() || !this.creature.canMove() || !this.creature.getNavigator().noPath()) {
            return false;
        }
        /*if (this.creature.getIdleTime() >= 100) {
                return false;
        }*/
        if (this.creature.getRNG().nextInt(this.executionChance) != 0) { return false; }
        UntamedWilds.LOGGER.info("Trying to Sleep");
        if (this.creature.getHome() == BlockPos.ZERO || !canEasilyReach(this.creature.getHome()) || this.creature.getDistanceSq(this.creature.getHomeAsVec()) > 100000) {
            this.creature.setHome(BlockPos.ZERO);
            BlockPos pos = this.checkForNewHome();
            if (pos == null) {
                return false;
            } else {
                this.creature.setHome(pos);
            }
        }
        this.target = new Vector3f(this.creature.getHomeAsVec());
        return true;
    }

    private boolean canEasilyReach(BlockPos target) {
        //this.targetSearchDelay = 10 + this.creature.getRNG().nextInt(5);
        Path path = this.creature.getNavigator().getPathToPos(target, 0);
        if (path == null) {
            return false;
        } else {
            PathPoint pathpoint = path.getFinalPathPoint();
            if (pathpoint == null) {
                return false;
            } else {
                int i = pathpoint.x - MathHelper.floor(target.getX());
                int j = pathpoint.z - MathHelper.floor(target.getZ());
                return (double)(i * i + j * j) <= 2.25D;
            }
        }
    }

    @Override
    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.target.getX(), this.target.getY(), this.target.getZ(), this.speed);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.creature.getNavigator().noPath();
    }

    @Nullable
    public BlockPos checkForNewHome() {
        /*Random random = this.creature.getRNG();
        BlockPos blockpos = new BlockPos(this.creature.getPosition());
        UntamedWilds.LOGGER.info("Trying to find new home");
        for(int i = 0; i < 10; ++i) {
            int perception = (int) this.creature.getAttribute(Attributes.FOLLOW_RANGE).getValue() / 2;
            int offsetX = random.nextInt(perception * 2) - perception;
            int offsetY = random.nextInt(4) - 2;
            int offsetZ = random.nextInt(perception * 2) - perception;

            BlockPos blockpos1 = blockpos.add(offsetX, offsetY, offsetZ);
            if (!this.creature.world.getBlockState(blockpos1).isSolid() && this.creature.world.getBlockState(blockpos1.down()).isSolid()) {
                this.creature.world.setBlockState(blockpos1, Blocks.TORCH.getDefaultState(), 11);
                if (this.isValidShelter(blockpos1) && this.creature.getBlockPathWeight(blockpos1) < 0.0F) {
                    this.creature.world.setBlockState(blockpos1, Blocks.LANTERN.getDefaultState(), 11);
                    // This comment shall become a reminder about how fucking trash this piece of code has consistently been
                    UntamedWilds.LOGGER.info("Found new home");
                    return new BlockPos(Vector3d.copyCenteredHorizontally(blockpos1));
                }
            }
        }
        return null;*/
        Random random = this.creature.getRNG();
        BlockPos blockpos = this.creature.getPosition();

        for(int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.add(random.nextInt(12) - 6, random.nextInt(4) - 2, random.nextInt(12) - 6);
            if (isValidShelter(blockpos1) && this.creature.getBlockPathWeight(blockpos1) < 0.0F) {
                return blockpos1;
            }
        }

        return null;
    }

    private boolean isValidShelter(BlockPos blockPos) {
        // We consider a valid shelter a dark location, with Sky Light Level less than 13 (mostly, to prevent mobs sleeping under broad daylight)
        //return this.creature.world.canBlockSeeSky(blockPos);
        return this.creature.world.getLightFor(LightType.SKY, blockPos) <= 12; // Was 12
    }
}
