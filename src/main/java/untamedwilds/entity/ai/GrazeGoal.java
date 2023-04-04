package untamedwilds.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMobTerrestrial;
import untamedwilds.init.ModTags;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class GrazeGoal extends Goal {

    private final ComplexMobTerrestrial taskOwner;
    private final Level entityWorld;
    private BlockPos testpos;
    private int eatingGrassTimer;
    private final int executionChance;

    public GrazeGoal(ComplexMobTerrestrial entityIn, int chance) {
        this.taskOwner = entityIn;
        this.entityWorld = entityIn.level;
        this.executionChance = chance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (!this.taskOwner.canMove() || this.taskOwner.isBaby() || this.taskOwner.getHunger() > 100 || this.taskOwner.getTarget() != null || this.taskOwner.getRandom().nextInt(executionChance) != 0) {
            return false;
        }
        this.testpos = this.taskOwner.blockPosition().offset(Math.cos(Math.toRadians(this.taskOwner.getYRot()+ 90)) * 1.2, 0, Math.sin(Math.toRadians(this.taskOwner.getYRot() + 90)) * 1.2);
        //this.testpos = new BlockPos(this.taskOwner.getPosition());
        if (this.entityWorld.getBlockState(this.testpos).is(ModTags.ModBlockTags.GRAZEABLE_BLOCKS) || this.entityWorld.getBlockState(this.testpos.below()).getBlock() == Blocks.GRASS_BLOCK) {
            return true;
        }
        if (this.taskOwner.getHunger() < 40) {
            BlockPos pos = this.locateGrazeables();
            if (pos != null) {
                this.taskOwner.getNavigation().moveTo(pos.getX(), pos.getY(),pos.getZ(), 1);
            }
        }
        return false;
    }

    @Override
    public void start() {
        this.eatingGrassTimer = 40;
        this.entityWorld.broadcastEntityEvent(this.taskOwner, (byte)10);
        this.taskOwner.getNavigation().stop();
        this.taskOwner.setAnimation(this.taskOwner.getAnimationEat());
    }

    @Override
    public void stop()
    {
        this.eatingGrassTimer = 0;
    }

    @Override
    public boolean canContinueToUse()
    {
        return this.eatingGrassTimer > 0;
    }

    @Override
    public void tick() {
        this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);
        if (this.eatingGrassTimer == 4) {
            if (this.entityWorld.getBlockState(this.testpos).is(ModTags.ModBlockTags.GRAZEABLE_BLOCKS)) {
                if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.entityWorld, this.taskOwner) && ConfigGamerules.grazerGriefing.get()) {
                    this.entityWorld.destroyBlock(this.testpos, false);
                }
                this.taskOwner.addHunger(16);
                this.taskOwner.ate();
            } else {
                BlockPos blockpos1 = this.testpos.below();
                if (this.entityWorld.getBlockState(blockpos1).getBlock() == Blocks.GRASS_BLOCK) {
                    if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.entityWorld, this.taskOwner)) {
                        this.entityWorld.globalLevelEvent(2001, blockpos1, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
                        if (ConfigGamerules.grazerGriefing.get()) {
                            this.entityWorld.setBlock(blockpos1, Blocks.DIRT.defaultBlockState(), 2);
                        }
                    }
                    this.taskOwner.addHunger(16);
                    this.taskOwner.ate();
                }
            }
        }
    }

    @Nullable
    private BlockPos locateGrazeables() {
        RandomSource random = this.taskOwner.getRandom();
        BlockPos blockpos = this.taskOwner.blockPosition();

        for(int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.offset(random.nextInt(12) - 6, random.nextInt(4) - 2, random.nextInt(12) - 6);
            if ((this.entityWorld.getBlockState(this.testpos).is(ModTags.ModBlockTags.GRAZEABLE_BLOCKS) || this.entityWorld.getBlockState(this.testpos.below()).getBlock() == Blocks.GRASS_BLOCK) && this.taskOwner.getWalkTargetValue(blockpos1) < 0.0F) {
                return blockpos1;
            }
        }

        return null;
    }
}