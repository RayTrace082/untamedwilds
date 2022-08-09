package untamedwilds.entity.ai.unique;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.Path;
import untamedwilds.entity.mammal.EntityBear;

import java.util.EnumSet;

public class PandaBreakBamboo extends Goal {
    private BlockPos targetPos;
    private final EntityBear taskOwner;
    private final int executionChance;
    private Path path;
    private int searchCooldown;
    private boolean continueTask;

    public PandaBreakBamboo(EntityBear entityIn, int chance) {
        this.taskOwner = entityIn;
        this.executionChance = chance;
        this.searchCooldown = 100;
        this.continueTask = true;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        if (!this.taskOwner.isOnGround() || this.taskOwner.getHunger() > 40) {
            return false;
        }
        if (this.taskOwner.getTarget() != null) {
            return false;
        }
        if (this.taskOwner.getRandom().nextInt(this.executionChance) != 0) {
            return false;
        }
        BlockPos pos = this.taskOwner.blockPosition();

        this.targetPos = findNearbyBamboo(pos);
        if (this.targetPos != null) {
            this.path = this.taskOwner.getNavigation().createPath(this.targetPos, 0);
            return this.path != null;
        }
        return false;
    }

    public void start() {
        this.taskOwner.getNavigation().moveTo(this.path, 1);
        super.start();
    }

    public void stop() { }

    public void tick() {
        // For some fucking reason, Panda Bears stop long before reaching their target block
        this.taskOwner.getLookControl().setLookAt(this.targetPos.getX(), this.targetPos.getY() + 1.5F, this.targetPos.getZ(), 10f, (float)this.taskOwner.getMaxHeadXRot());
        if (!this.taskOwner.isSitting()) {
            this.taskOwner.getMoveControl().strafe(1, 0);
        }
        if (this.targetPos != null && this.taskOwner.distanceToSqr(targetPos.getX(), targetPos.getY(), targetPos.getZ()) < 5) {
            this.taskOwner.getLookControl().setLookAt(this.targetPos.getX(), this.targetPos.getY() + 1.5F, this.targetPos.getZ(), 10f, (float)this.taskOwner.getMaxHeadXRot());
            this.taskOwner.getNavigation().stop();
            this.taskOwner.setSitting(true);
            this.searchCooldown--;
            if (this.searchCooldown == 0) {
                this.searchCooldown = 100;
                this.taskOwner.level.destroyBlock(this.targetPos.above(), false);
                this.taskOwner.setAnimation(EntityBear.ATTACK_SWIPE);
                // TODO: Make the Panda hold the Bamboo and chew it
                this.taskOwner.addHunger(8);
                this.continueTask = false;
            }
        }
        super.tick();
    }

    public boolean canContinueToUse() {
        if (!this.continueTask) {
            this.taskOwner.setSitting(false);
            return false;
        }
        return true;
    }

    public BlockPos findNearbyBamboo(BlockPos blockpos) {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 8; ++j) {
                for(int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                    for(int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                        blockpos$mutable.set(blockpos).move(k, i, l);
                        if (this.taskOwner.level.getBlockState(blockpos$mutable).getBlock() == Blocks.BAMBOO && this.taskOwner.level.getBlockState(blockpos$mutable.above()).getBlock() == Blocks.BAMBOO) {
                            return blockpos$mutable;
                        }
                    }
                }
            }
        }

        return null;
    }
}