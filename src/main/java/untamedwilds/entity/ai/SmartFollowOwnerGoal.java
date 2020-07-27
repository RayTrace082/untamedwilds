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

// TODO: Abstract the process so mobs can follow other mobs
// TODO: Extend the class to cover Water mobs
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

                            for(int lvt_4_1_ = 0; lvt_4_1_ <= 4; ++lvt_4_1_) {
                                for(int lvt_5_1_ = 0; lvt_5_1_ <= 4; ++lvt_5_1_) {
                                    if ((lvt_4_1_ < 1 || lvt_5_1_ < 1 || lvt_4_1_ > 3 || lvt_5_1_ > 3) && this.canTeleportToBlock(new BlockPos(lvt_1_1_ + lvt_4_1_, lvt_3_1_ - 1, lvt_2_1_ + lvt_5_1_))) {
                                        this.taskOwner.setLocationAndAngles((double)((float)(lvt_1_1_ + lvt_4_1_) + 0.5F), (double)lvt_3_1_, (double)((float)(lvt_2_1_ + lvt_5_1_) + 0.5F), this.taskOwner.rotationYaw, this.taskOwner.rotationPitch);
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

    protected boolean canTeleportToBlock(BlockPos p_220707_1_) {
        BlockState lvt_2_1_ = this.world.getBlockState(p_220707_1_);
        return lvt_2_1_.canEntitySpawn(this.world, p_220707_1_, this.taskOwner.getType()) && this.world.isAirBlock(p_220707_1_.up()) && this.world.isAirBlock(p_220707_1_.up(2));
    }
}
