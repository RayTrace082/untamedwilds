package untamedwilds.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import untamedwilds.entity.ComplexMob;

import java.util.EnumSet;

public class SmartFollowOwnerGoal extends Goal {
    protected final ComplexMob taskOwner;
    private LivingEntity owner;
    protected final Level level;
    private final double followSpeed;
    private final PathNavigation navigator;
    private int timeToRecalcPath;
    private final float maxDist;
    private final float minDist;
    private float oldWaterCost;

    public SmartFollowOwnerGoal(ComplexMob entityIn, double speedIn, float minDistIn, float maxDistIn) {
        this.taskOwner = entityIn;
        this.level = taskOwner.level;
        this.followSpeed = speedIn;
        this.navigator = entityIn.getNavigation();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        if (!(entityIn.getNavigation() instanceof GroundPathNavigation) && !(entityIn.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for SmartFollowOwnerGoal");
        }
    }

    @Override
    public boolean canUse() {
        LivingEntity owner = this.taskOwner.getOwner();
        if (owner == null || owner.isSpectator() || !this.taskOwner.canMove() || this.taskOwner.distanceToSqr(owner) < (double)(this.minDist * this.minDist)) {
            return false;
        }
        this.owner = owner;
        return this.taskOwner.getCommandInt() == 1;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.navigator.isDone() && this.taskOwner.distanceToSqr(this.owner) > (double)(this.maxDist * this.maxDist) && !this.taskOwner.isSitting();
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.taskOwner.getPathfindingMalus(BlockPathTypes.WATER);
        this.taskOwner.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.navigator.stop();
        this.taskOwner.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.taskOwner.getLookControl().setLookAt(this.owner, 10.0F, (float)this.taskOwner.getHeadRotSpeed());
        if (this.taskOwner.canMove() && this.taskOwner.getCommandInt() == 1) {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (!this.taskOwner.isLeashed() && !this.taskOwner.isPassenger()) {
                    if (!this.navigator.moveTo(this.owner, this.followSpeed)) {
                        if (this.taskOwner.distanceToSqr(this.owner) >= 225.0D && this.taskOwner.getTarget() == null) {
                            this.teleportToOwner();
                        }
                    }
                }
            }
        }
    }

    private void teleportToOwner() {
        BlockPos blockpos = this.owner.blockPosition();

        for(int i = 0; i < 10; ++i) {
            int j = this.randomIntInclusive(-3, 3);
            int k = this.randomIntInclusive(-1, 1);
            int l = this.randomIntInclusive(-3, 3);
            boolean flag = this.maybeTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
            if (flag) {
                return;
            }
        }

    }

    private boolean maybeTeleportTo(int p_25304_, int p_25305_, int p_25306_) {
        if (Math.abs((double)p_25304_ - this.owner.getX()) < 2.0D && Math.abs((double)p_25306_ - this.owner.getZ()) < 2.0D) {
            return false;
        } else if (!this.canTeleportTo(new BlockPos(p_25304_, p_25305_, p_25306_))) {
            return false;
        } else {
            this.taskOwner.moveTo((double)p_25304_ + 0.5D, p_25305_, (double)p_25306_ + 0.5D, this.taskOwner.getYRot(), this.taskOwner.getXRot());
            this.taskOwner.getNavigation().stop();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos p_25308_) {
        BlockPathTypes blockpathtypes = WalkNodeEvaluator.getBlockPathTypeStatic(this.level, p_25308_.mutable());
        if (blockpathtypes != BlockPathTypes.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = this.level.getBlockState(p_25308_.below());
            if (/*!this.canFly && */blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = p_25308_.subtract(this.taskOwner.blockPosition());
                return this.level.noCollision(this.taskOwner, this.taskOwner.getBoundingBox().inflate(1, 0, 1).move(blockpos));
            }
        }
    }

    private int randomIntInclusive(int p_25301_, int p_25302_) {
        return this.taskOwner.getRandom().nextInt(p_25302_ - p_25301_ + 1) + p_25301_;
    }
}
