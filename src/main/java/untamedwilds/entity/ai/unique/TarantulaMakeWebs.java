package untamedwilds.entity.ai.unique;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.entity.arthropod.Tarantula;

import java.util.EnumSet;

public class TarantulaMakeWebs extends Goal {
    private BlockPos targetPos;
    private BlockPos movementPos;
    private final Tarantula taskOwner;
    private BlockPos foundWebs;
    private final int executionChance;
    private int searchCooldown;
    private boolean continueTask;

    public TarantulaMakeWebs(Tarantula entityIn, int chance) {
        this.taskOwner = entityIn;
        this.executionChance = chance;
        this.searchCooldown = 100;
        this.continueTask = true;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean shouldExecute() {

        if (!this.taskOwner.onGround || this.taskOwner.getAttackTarget() != null/* || this.taskOwner.webProgress > 0*/) {
            return false;
        }
        if (this.taskOwner.getRNG().nextInt(this.executionChance) != 0) {
            return false;
        }
        BlockPos pos = this.taskOwner.getPosition();

        this.foundWebs = findNearbyWebs(pos);
        if (this.foundWebs == null) {
            this.targetPos = findNearbyWebbableSpot(pos);
            return this.targetPos != null;
        }
        return false;
    }

    public void startExecuting() {
        this.taskOwner.getNavigator().tryMoveToXYZ((double)((float)this.movementPos.getX()) + 0.5D, (double)(this.movementPos.getY() + 1), (double)((float)this.movementPos.getZ()) + 0.5D, 1f);
        super.startExecuting();
    }

    public void resetTask() {
        super.resetTask();
    }

    public void tick() {
        if (this.targetPos != null && this.taskOwner.getNavigator().tryMoveToXYZ(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1)) {
            this.taskOwner.world.setBlockState(this.targetPos, Blocks.COBWEB.getDefaultState());
            //this.taskOwner.webProgress = this.taskOwner.getAdulthoodTime();
            this.taskOwner.getNavigator().clearPath();
            this.searchCooldown--;
            this.continueTask = false;
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

    public BlockPos findNearbyWebs(BlockPos blockpos) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 8; ++j) {
                for(int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                    for(int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                        blockpos$mutable.setPos(blockpos).move(k, i, l);
                        if (this.taskOwner.world.getBlockState(blockpos$mutable).getBlock() == Blocks.COBWEB) {
                            return blockpos$mutable;
                        }
                    }
                }
            }
        }

        return null;
    }

    // TODO: Broken, causes NPE. Temporarly disabled
    public BlockPos findNearbyWebbableSpot(BlockPos blockpos) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        int score;
        BlockPos testpos;
        if (blockpos.getY() > 4) {
            for(int i = 0; i < 3; ++i) {
                for(int j = 0; j < 8; ++j) {
                    for(int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                        for(int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                            blockpos$mutable.setPos(blockpos).move(k, i, l);
                            score = 0;
                            if (this.taskOwner.world.getBlockState(blockpos$mutable).isAir() && this.taskOwner.world.getLight(blockpos$mutable) < 14) {
                                for (Direction dir : Direction.values()) {
                                    testpos = this.targetPos.offset(dir);
                                    if (!this.taskOwner.world.isRemote()) {
                                        ((ServerWorld)this.taskOwner.world).spawnParticle(ParticleTypes.FLAME, testpos.getX(), testpos.getY(), testpos.getZ(), 1, 0, 0, 0, 1);
                                    }
                                    if (this.taskOwner.world.getBlockState(testpos).isSolid()) {
                                        score++;
                                    }
                                    if (score >= 4) {
                                        return blockpos$mutable;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}