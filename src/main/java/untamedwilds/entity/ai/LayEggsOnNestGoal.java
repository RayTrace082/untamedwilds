package untamedwilds.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import untamedwilds.block.blockentity.ReptileNestBlockEntity;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.INestingMob;
import untamedwilds.init.ModBlock;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class LayEggsOnNestGoal extends MoveToBlockGoal {
    private final ComplexMob taskOwner;
    private final Level world;
    private boolean hasReachedDestination;
    private boolean needsToBuildNest = false;
    private int nestBuildingTicks;

    public LayEggsOnNestGoal(ComplexMob entityIn) {
        super(entityIn, 1, 16, 4);
        this.taskOwner = entityIn;
        this.world = entityIn.level;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (!(this.taskOwner instanceof INestingMob) || !((INestingMob)taskOwner).wantsToLayEggs())
            return false;
        if (this.nextStartTick > 0) {
            --this.nextStartTick;
            return false;
        } else {
            needsToBuildNest = false;
            this.nextStartTick = this.nextStartTick(this.mob);
            if (this.findNearestBlock())
                return true;
            needsToBuildNest = true;
            return this.checkForNewNest();
        }
    }

    public boolean canContinueToUse() {
        return ((INestingMob)this.taskOwner).wantsToLayEggs() && super.canContinueToUse();
    }

    @Override
    public double acceptedDistance() {
        return 1.0D;
    }

    public void tick() {
        super.tick();
        BlockPos blockpos = this.getMoveToTarget();
        if (!isWithinXZDist(blockpos, this.mob.position(), this.acceptedDistance())) {
            this.hasReachedDestination = false;
            ++this.tryTicks;
            if (this.shouldRecalculatePath()) {
                this.mob.getNavigation().moveTo((double) ((float) blockpos.getX()) + 0.5D, blockpos.getY(), (double) ((float) blockpos.getZ()) + 0.5D, this.speedModifier);
            }
        } else {
            this.hasReachedDestination = true;
            --this.tryTicks;
        }
        //((ServerLevel)this.taskOwner.level).sendParticles(ParticleTypes.ANGRY_VILLAGER, blockpos.getX(), blockpos.getY(), blockpos.getZ(), 20, 0.0D, 0.0D, 0.0D, 0.15F);

        if (this.isReachedTarget()) {
            if (this.needsToBuildNest) {
                --this.nestBuildingTicks;
                if (this.nestBuildingTicks % 30 == 0) {
                    ((ServerLevel)this.taskOwner.level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.taskOwner.level.getBlockState(this.blockPos.below())), this.taskOwner.getX(), this.taskOwner.getY(), this.taskOwner.getZ(), 20, 0.0D, 0.0D, 0.0D, 0.15F);
                    this.taskOwner.playSound(SoundEvents.SHOVEL_FLATTEN, 0.8F, 0.6F);
                }
                if (this.nestBuildingTicks <= 0) {
                    this.world.setBlock(this.blockPos, ModBlock.NEST_REPTILE.get().defaultBlockState(), 2);
                    ReptileNestBlockEntity te = (ReptileNestBlockEntity) world.getBlockEntity(this.blockPos);
                    if (te != null) {
                        te.setEntityType(this.taskOwner.getType());
                        te.setVariant(this.taskOwner.getVariant());
                        te.setEggCount(0);
                    }
                    this.needsToBuildNest = false;
                }
            }
            else {
                this.addEggsToNest();
                stop();
            }
        }
    }

    @Nullable
    public boolean checkForNewNest() {
        RandomSource random = this.taskOwner.getRandom();
        BlockPos blockpos = this.taskOwner.blockPosition();
        for(int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.offset(random.nextInt(8) - 4, random.nextInt(4) - 2, random.nextInt(8) - 4);
            if (((INestingMob)this.taskOwner).isValidNestBlock(blockpos1) && this.isValidTarget(this.mob.level, blockpos1)) {
                this.nestBuildingTicks = 400 + this.taskOwner.getRandom().nextInt(300);
                this.blockPos = blockpos1;
                return true;
            }
        }

        return false;
    }

    private void addEggsToNest() {
        BlockState blockstate = taskOwner.level.getBlockState(this.blockPos);
        if (blockstate.is(ModBlock.NEST_REPTILE.get())) {
            if (taskOwner.level.getBlockEntity(this.blockPos) instanceof ReptileNestBlockEntity nest) {
                //UntamedWilds.LOGGER.info("Adding eggs to existing nest");
                if (((INestingMob)this.taskOwner).wantsToLayEggs())
                    nest.setEggCount(nest.getEggCount() + this.taskOwner.getOffspring());
                taskOwner.level.updateNeighbourForOutputSignal(this.blockPos, blockstate.getBlock());
                ((INestingMob)taskOwner).setEggStatus(false);
            }
        }
    }

    private boolean isWithinXZDist(BlockPos blockpos, Vec3 positionVec, double distance) {
        return blockpos.distSqr(new BlockPos(positionVec.x(), blockpos.getY(), positionVec.z())) < distance * distance;
    }

    protected boolean isReachedTarget() {
        return this.hasReachedDestination;
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        if (this.needsToBuildNest) {
            //((ServerLevel)this.taskOwner.level).sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 20, 0.0D, 0.0D, 0.0D, 0.15F);
            return ((INestingMob)this.taskOwner).isValidNestBlock(pos);
        }
        return worldIn.getBlockState(pos).is(ModBlock.NEST_REPTILE.get()) && worldIn.getBlockEntity(pos) instanceof ReptileNestBlockEntity && ((ReptileNestBlockEntity) worldIn.getBlockEntity(pos)).getVariant() == this.taskOwner.getVariant();
    }
}