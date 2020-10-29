package untamedwilds.entity.ai;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import untamedwilds.entity.ComplexMob;

import java.util.EnumSet;

public class SmartFollowOwnerGoal extends Goal {
    protected final ComplexMob taskOwner;
    private LivingEntity owner;
    protected final IWorldReader world;
    private final double followSpeed;
    private final PathNavigator navigator;
    private int timeToRecalcPath;
    private final float maxDist;
    private final float minDist;
    private float oldWaterCost;

    public SmartFollowOwnerGoal(ComplexMob taskowner, double followspeed, float minDistIn, float maxDistIn) {
        this.taskOwner = taskowner;
        this.world = taskOwner.world;
        this.followSpeed = followspeed;
        this.navigator = taskowner.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        if (!(taskowner.getNavigator() instanceof GroundPathNavigator) && !(taskowner.getNavigator() instanceof FlyingPathNavigator)) {
            throw new IllegalArgumentException("Unsupported mob type for SmartFollowOwnerGoal");
        }
    }

    public boolean shouldExecute() {
        LivingEntity owner = this.taskOwner.getOwner();
        if (owner == null) {
            return false;
        } else if (owner.isSpectator()) {
            return false;
        } else if (this.taskOwner.isSitting()) {
            return false;
        } else if (this.taskOwner.getDistanceSq(owner) < (double)(this.minDist * this.minDist)) {
            return false;
        } else {
            this.owner = owner;
            return this.taskOwner.getCommandInt() == 1;
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.navigator.noPath() && this.taskOwner.getDistanceSq(this.owner) > (double)(this.maxDist * this.maxDist) && !this.taskOwner.isSitting();
    }

    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.taskOwner.getPathPriority(PathNodeType.WATER);
        this.taskOwner.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    public void resetTask() {
        this.owner = null;
        this.navigator.clearPath();
        this.taskOwner.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    public void tick() {
        this.taskOwner.getLookController().setLookPositionWithEntity(this.owner, 10.0F, (float)this.taskOwner.getVerticalFaceSpeed());
        if (!this.taskOwner.isSitting() && this.taskOwner.getCommandInt() == 1) {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (!this.navigator.tryMoveToEntityLiving(this.owner, this.followSpeed)) {
                    if (!this.taskOwner.getLeashed() && !this.taskOwner.isPassenger()) {
                        if (this.taskOwner.getDistanceSq(this.owner) >= 144.0D) {
                            int lvt_1_1_ = MathHelper.floor(this.owner.getPosX()) - 2;
                            int lvt_2_1_ = MathHelper.floor(this.owner.getPosZ()) - 2;
                            int lvt_3_1_ = MathHelper.floor(this.owner.getBoundingBox().minY);

                            for(int i = 0; i <= 4; ++i) {
                                for(int j = 0; j <= 4; ++j) {
                                    if ((i < 1 || j < 1 || i > 3 || j > 3) && this.canTeleportToBlock(new BlockPos(lvt_1_1_ + i, lvt_3_1_ - 1, lvt_2_1_ + j))) {
                                        this.taskOwner.setLocationAndAngles(((float)(lvt_1_1_ + i) + 0.5F), lvt_3_1_, ((float)(lvt_2_1_ + j) + 0.5F), this.taskOwner.rotationYaw, this.taskOwner.rotationPitch);
                                        this.navigator.clearPath();
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected boolean canTeleportToBlock(BlockPos pos) {
        BlockState state = this.world.getBlockState(pos);
        return state.canEntitySpawn(this.world, pos, this.taskOwner.getType()) && this.world.isAirBlock(pos.up()) && this.world.isAirBlock(pos.up(2));
    }
}
