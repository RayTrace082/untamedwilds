package untamedwilds.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMobTerrestrial;

import java.util.EnumSet;
import java.util.function.Predicate;

public class GrazeGoal extends Goal {

    private static final Predicate<BlockState> IS_GRASS = BlockStateMatcher.forBlock(Blocks.GRASS);
    private final ComplexMobTerrestrial taskOwner;
    private final World entityWorld;
    private BlockPos testpos;
    private int eatingGrassTimer;
    private final int executionChance;

    public GrazeGoal(ComplexMobTerrestrial taskOwner, int executionChance) {
        this.taskOwner = taskOwner;
        this.entityWorld = taskOwner.world;
        this.executionChance = executionChance;
        this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.taskOwner.canMove() || this.taskOwner.isChild() || this.taskOwner.getHunger() > 100) {
            return false;
        }
        if (this.taskOwner.getAttackTarget() != null) {
            return false;
        }
        if (this.taskOwner.getRNG().nextInt(executionChance) != 0) {
            return false;
        }
        else {
            this.testpos = new BlockPos(this.taskOwner.getPosition());
            if (IS_GRASS.test(this.entityWorld.getBlockState(this.testpos))) {
                return true;
            } else {
                return this.entityWorld.getBlockState(this.testpos.down()).getBlock() == Blocks.GRASS_BLOCK;
            }
        }
    }

    @Override
    public void startExecuting() {
        this.eatingGrassTimer = 40;
        this.entityWorld.setEntityState(this.taskOwner, (byte)10);
        this.taskOwner.getNavigator().clearPath();
        this.taskOwner.setAnimation(this.taskOwner.getAnimationEat());
    }

    @Override
    public void resetTask()
    {
        this.eatingGrassTimer = 0;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return this.eatingGrassTimer > 0;
    }

    @Override
    public void tick() {
        this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);
        if (this.eatingGrassTimer == 4) {
            if (IS_GRASS.test(this.entityWorld.getBlockState(this.testpos))) { // TODO: Expand to SeaGrass and other vegetables
                if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.entityWorld, this.taskOwner) && ConfigGamerules.grazerGriefing.get()) {
                    this.entityWorld.destroyBlock(this.testpos, false);
                }
                this.taskOwner.addHunger(16);
                this.taskOwner.eatGrassBonus();
            } else {
                BlockPos blockpos1 = this.testpos.down();
                if (this.entityWorld.getBlockState(blockpos1).getBlock() == Blocks.GRASS_BLOCK) {
                    if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.entityWorld, this.taskOwner)) {
                        this.entityWorld.playEvent(2001, blockpos1, Block.getStateId(Blocks.GRASS_BLOCK.getDefaultState()));
                        if (ConfigGamerules.grazerGriefing.get()) {
                            this.entityWorld.setBlockState(blockpos1, Blocks.DIRT.getDefaultState(), 2);
                        }
                    }
                    this.taskOwner.addHunger(16);
                    this.taskOwner.eatGrassBonus();
                }
            }
        }
    }
}