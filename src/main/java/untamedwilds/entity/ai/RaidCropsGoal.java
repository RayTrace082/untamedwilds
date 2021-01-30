package untamedwilds.entity.ai;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.entity.ComplexMobTerrestrial;

import java.util.EnumSet;
import java.util.List;

public class RaidCropsGoal extends Goal {
    private BlockPos targetPos;
    private final ComplexMobTerrestrial taskOwner;
    private final boolean continueTask;

    public RaidCropsGoal(ComplexMobTerrestrial entityIn) {
        this.taskOwner = entityIn;
        this.continueTask = true;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean shouldExecute() {
        if (this.taskOwner.isTamed() || !net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.taskOwner.world, this.taskOwner)) {
            return false;
        }
        if (!this.taskOwner.isOnGround() || this.taskOwner.getHunger() > 80) {
            return false;
        }
        if (this.taskOwner.getAttackTarget() != null) {
            return false;
        }
        if (this.taskOwner.getRNG().nextInt(120) != 0) {
            return false;
        }
        BlockPos pos = this.taskOwner.getPosition();

        this.targetPos = getNearbyInventories(pos);
        return this.targetPos != null;
    }

    public void startExecuting() {
        this.taskOwner.getNavigator().tryMoveToXYZ((double)this.targetPos.getX() + 0.5D, this.targetPos.getY() + 1, (double)this.targetPos.getZ() + 0.5D, 1f);
        super.startExecuting();
    }

    public void resetTask() {
        super.resetTask();
    }

    public void tick() {
        super.tick();
        if (this.targetPos != null && this.taskOwner.getDistanceSq(targetPos.getX(), targetPos.getY(), targetPos.getZ()) < 4) {
            BlockState block = this.taskOwner.world.getBlockState(this.targetPos);
            if (block.getBlock() instanceof CropsBlock) {
                LootContext.Builder loot = new LootContext.Builder((ServerWorld)taskOwner.world).withParameter(LootParameters.TOOL, ItemStack.EMPTY).withParameter(LootParameters.field_237457_g_, taskOwner.getPositionVec()).withRandom(this.taskOwner.getRNG()).withLuck(1.0F);
                List<ItemStack> drops = block.getBlock().getDrops(block, loot);
                if (!drops.isEmpty()) {
                    int count = 0;
                    for(ItemStack stack : drops){
                        if(count == 0){
                            ItemStack duplicate = stack.copy();
                            duplicate.setCount(1);
                                /*if (!this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty() && !this.entity.world.isRemote) {
                                    this.entity.entityDropItem(this.entity.getHeldItem(Hand.MAIN_HAND), 0.0F);
                                }
                                this.entity.setHeldItem(Hand.MAIN_HAND, duplicate);*/
                        }
                        count++;
                    }
                    this.taskOwner.addHunger(count * 10);
                    this.taskOwner.world.destroyBlock(this.targetPos, false);
                    this.taskOwner.getNavigator().clearPath();
                }
            }
        }
    }

    public boolean shouldContinueExecuting() {
        if (this.taskOwner.getHunger() > 80 || this.taskOwner.world.isAirBlock(this.targetPos)) {
            return false;
        }
        return this.continueTask;
    }


    private BlockPos getNearbyInventories(BlockPos roomCenter) {
        int X = 15;
        int Y = 3;
        //List<BlockPos> inventories = new ArrayList<>();
        for (BlockPos blockpos : BlockPos.getAllInBoxMutable(roomCenter.add(-X, -Y, -X), roomCenter.add(X, Y, X))) {
            if (this.taskOwner.world.getBlockState(blockpos).getBlock() instanceof FarmlandBlock) {
                if (this.shouldMoveTo(this.taskOwner.world, blockpos)) {
                    return blockpos.up();
                }
            }
        }
        return null;
    }

    private boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
        BlockState block = worldIn.getBlockState(pos.up());
        if (block.getBlock() instanceof CropsBlock) {
            return ((CropsBlock)block.getBlock()).isMaxAge(worldIn.getBlockState(pos.up()));
        }
        return false;
    }
}
