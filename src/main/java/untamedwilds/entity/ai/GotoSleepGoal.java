package untamedwilds.entity.ai;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import untamedwilds.entity.ComplexMobTerrestrial;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class GotoSleepGoal extends Goal {
    private final ComplexMobTerrestrial creature;
    protected double x;
    protected double y;
    protected double z;
    private int executionChance;
    private final double speed;

    public GotoSleepGoal(ComplexMobTerrestrial entityIn, double speedIn) {
        this(entityIn, speedIn, 120, false);
    }

    public GotoSleepGoal(ComplexMobTerrestrial entityIn, double speedIn, boolean avoidWater) {
        this(entityIn, speedIn, 100, avoidWater);
    }

    public GotoSleepGoal(ComplexMobTerrestrial entityIn, double speedIn, int chance, boolean avoidWater) {
        this.creature = entityIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean shouldExecute() {
        if (this.creature.isTamed() && this.creature.getCommandInt() != 0) {
            return false;
        }
        if (this.creature.isActive() || this.creature.isBeingRidden() || this.creature.isSleeping() || !this.creature.canMove() || !this.creature.getNavigator().noPath()) {
            return false;
        } else {
            /*if (this.creature.getIdleTime() >= 100) {
                return false;
            }*/
            //if (this.creature.getRNG().nextInt(this.executionChance) != 0) { return false; }

            if (this.creature.getHome() == BlockPos.ZERO || this.creature.getDistanceSq(this.creature.getHomeAsVec()) > 100000) {
                this.creature.setHome(BlockPos.ZERO);
                BlockPos pos = this.checkForNewHome();
                if (pos == null) {
                    return false;
                } else {
                    this.creature.setHome(pos);
                    this.x = this.creature.getHome().getX(); // TODO: Replace x, y, z with vec3d, somehow
                    this.y = this.creature.getHome().getY();
                    this.z = this.creature.getHome().getZ();
                    return true;
                }
            }
            this.x = this.creature.getHome().getX();
            this.y = this.creature.getHome().getY();
            this.z = this.creature.getHome().getZ();
            return true;
        }
    }

    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, this.speed);
    }

    public boolean shouldContinueExecuting() {
        return !this.creature.getNavigator().noPath();
    }

    @Nullable
    public BlockPos checkForNewHome() {
        Random random = this.creature.getRNG();
        BlockPos blockpos = new BlockPos(this.creature.getPosition());

        for(int i = 0; i < 10; ++i) {
            int perception = (int) this.creature.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getValue();
            int offsetX = random.nextInt(perception * 2) - perception;
            int offsetY = random.nextInt(6) - 3;
            int offsetZ = random.nextInt(perception * 2) - perception;

            BlockPos blockpos1 = blockpos.add(offsetX, offsetY, offsetZ);
            if (!this.creature.world.getBlockState(blockpos1).isSolid() && this.creature.world.getBlockState(blockpos1.down()).isSolid()) {
                if (this.isValidShelter(blockpos1)/* && this.creature.getBlockPathWeight(blockpos1) < 0.0F*/) {
                    //this.creature.world.setBlockState(blockpos1, Blocks.TORCH.getDefaultState(), 11);
                    // This comment shall become a reminder about how fucking trash this piece of code has consistently been
                    return blockpos1;
                }
            }
        }
        return null;
    }

    private boolean isValidShelter(BlockPos blockPos) {
        // We consider a valid shelter a dark location, with Sky Light Level less than 13 (mostly, to prevent mobs sleeping under broad daylight)
        return this.creature.world.getLightFor(LightType.SKY, blockPos) <= 12;
    }
}
