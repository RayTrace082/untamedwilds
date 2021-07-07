package untamedwilds.entity.ai.unique;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import untamedwilds.entity.mammal.bear.AbstractBear;

import java.util.EnumSet;

public class PandaBreakBamboo extends Goal {
    private BlockPos targetPos;
    private final AbstractBear taskOwner;
    private final int executionChance;
    private Path path;
    private int searchCooldown;
    private boolean continueTask;

    public PandaBreakBamboo(AbstractBear entityIn, int chance) {
        this.taskOwner = entityIn;
        this.executionChance = chance;
        this.searchCooldown = 100;
        this.continueTask = true;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean shouldExecute() {
        if (!this.taskOwner.isOnGround() || this.taskOwner.getHunger() > 40) {
            return false;
        }
        if (this.taskOwner.getAttackTarget() != null) {
            return false;
        }
        if (this.taskOwner.getRNG().nextInt(this.executionChance) != 0) {
            return false;
        }
        BlockPos pos = this.taskOwner.getPosition();

        this.targetPos = findNearbyBamboo(pos);
        if (this.targetPos != null) {
            this.path = this.taskOwner.getNavigator().getPathToPos(this.targetPos, 0);
            return this.path != null;
        }
        return false;
    }

    public void startExecuting() {
        this.taskOwner.getNavigator().setPath(this.path, 1);
        super.startExecuting();
    }

    public void resetTask() {
        super.resetTask();
    }

    public void tick() {
        // For some fucking reason, Panda Bears stop long before reaching their target block
        this.taskOwner.getLookController().setLookPosition(this.targetPos.getX(), this.targetPos.getY() + 1.5F, this.targetPos.getZ(), 10f, (float)this.taskOwner.getVerticalFaceSpeed());
        if (!this.taskOwner.isSitting()) {
            this.taskOwner.getMoveHelper().strafe(1, 0);
        }
        if (this.targetPos != null && this.taskOwner.getDistanceSq(targetPos.getX(), targetPos.getY(), targetPos.getZ()) < 5) {
            this.taskOwner.getLookController().setLookPosition(this.targetPos.getX(), this.targetPos.getY() + 1.5F, this.targetPos.getZ(), 10f, (float)this.taskOwner.getVerticalFaceSpeed());
            this.taskOwner.getNavigator().clearPath();
            this.taskOwner.setSitting(true);
            this.searchCooldown--;
            if (this.searchCooldown == 0) {
                this.searchCooldown = 100;
                this.taskOwner.world.destroyBlock(this.targetPos.up(), false);
                this.taskOwner.setAnimation(AbstractBear.ATTACK_SWIPE);
                // TODO: Make the Panda hold the Bamboo and chew it
                this.taskOwner.addHunger(8);
                this.continueTask = false;
            }
        }
        super.tick();
    }

    public boolean shouldContinueExecuting() {
        if (!this.continueTask) {
            this.taskOwner.setSitting(false);
            return false;
        }
        return true;
    }

    public BlockPos findNearbyBamboo(BlockPos blockpos) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 8; ++j) {
                for(int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                    for(int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                        blockpos$mutable.setPos(blockpos).move(k, i, l);
                        if (this.taskOwner.world.getBlockState(blockpos$mutable).getBlock() == Blocks.BAMBOO && this.taskOwner.world.getBlockState(blockpos$mutable.up()).getBlock() == Blocks.BAMBOO) {
                            return blockpos$mutable;
                        }
                    }
                }
            }
        }

        return null;
    }
}