package untamedwilds.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModTags;

import javax.annotation.Nullable;
import java.util.Random;

public class UndergrowthPoisonousBlock extends UndergrowthBlock implements IGrowable, IPostGenUpdate, net.minecraftforge.common.IForgeShearable {

    public static final IntegerProperty PROPERTY_AGE = BlockStateProperties.AGE_0_2;

    public UndergrowthPoisonousBlock(Properties properties, OffsetType type) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(PROPERTY_AGE, 0));
        this.offset = type;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(PROPERTY_AGE);
    }

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof LivingEntity && !entityIn.isSneaking() && entityIn.getHeight() > 1) {
            ((LivingEntity)entityIn).addPotionEffect(new EffectInstance(Effects.POISON, 100, 1));
            entityIn.setMotionMultiplier(state, new Vector3d(0.95F, 1D, 0.95F));
            if (worldIn.getRandom().nextInt(20) == 0) {
                worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_GRASS_STEP, SoundCategory.AMBIENT, 1, 1, true);
            }
        }
    }

    public OffsetType getOffsetType() {
        return this.offset;
    }

    @Nullable
    public BlockState getStateForWorldgen(ISeedReader world, BlockPos pos) {
        BlockState blockstate = world.getBlockState(pos.down());
        if (blockstate.getBlock() == ModBlock.HEMLOCK.get()) {
            if (world.getFluidState(pos.down()).isEmpty() || world.getBlockState(pos.down(2)).getBlock() == ModBlock.HEMLOCK.get()) {
                world.setBlockState(pos.down(), blockstate.getBlockState().with(PROPERTY_AGE, 1), 1);
            }
            BlockState blockstate1 = world.getBlockState(pos.up());
            return blockstate1.getBlock() != ModBlock.HEMLOCK.get() ? ModBlock.HEMLOCK.get().getDefaultState().with(PROPERTY_AGE, 0) : this.getDefaultState().with(PROPERTY_AGE, 1);
        }
        if (blockstate.isIn(ModTags.BlockTags.REEDS_PLANTABLE_ON)) {
            return this.getDefaultState().with(PROPERTY_AGE, 0);
        }
        else {
            return null;
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState blockstate = context.getWorld().getBlockState(context.getPos().down());
        if (blockstate.getBlock() == ModBlock.HEMLOCK.get()) {
            if (context.getWorld().getFluidState(context.getPos().down()).isEmpty() || context.getWorld().getBlockState(context.getPos().down(2)).getBlock() == ModBlock.HEMLOCK.get()) {
                context.getWorld().setBlockState(context.getPos().down(), blockstate.getBlockState().with(PROPERTY_AGE, 1));
            }
            BlockState blockstate1 = context.getWorld().getBlockState(context.getPos().up());
            return blockstate1.getBlock() != ModBlock.HEMLOCK.get() ? ModBlock.HEMLOCK.get().getDefaultState().with(PROPERTY_AGE, 0) : this.getDefaultState().with(PROPERTY_AGE, 1);
        }
        if (blockstate.isIn(ModTags.BlockTags.REEDS_PLANTABLE_ON)) {
            return this.getDefaultState().with(PROPERTY_AGE, 0);
        }
        else {
            return null;
        }
    }

    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!state.isValidPosition(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).isIn(ModTags.BlockTags.REEDS_PLANTABLE_ON) || worldIn.getBlockState(pos.down()).getBlock() == ModBlock.HEMLOCK.get();
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (!stateIn.isValidPosition(worldIn, currentPos)) {
            worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
        }
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        int i = this.getNumReedBlocksAbove(worldIn, pos);
        int j = this.getNumReedBlocksBelow(worldIn, pos);
        return i + j + 1 < 3;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
        int i = this.getNumReedBlocksAbove(worldIn, pos);
        int j = this.getNumReedBlocksBelow(worldIn, pos);
        int k = i + j + 1;
        int l = 1 + rand.nextInt(2);

        for(int i1 = 0; i1 < l; ++i1) {
            BlockPos blockpos = pos.up(i);
            BlockState blockstate = worldIn.getBlockState(blockpos);
            if (k >= 4 || !worldIn.isAirBlock(blockpos.up())) {
                return;
            }
            if (blockstate.get(PROPERTY_AGE) != 2) {
                worldIn.setBlockState(blockpos, this.getDefaultState().with(PROPERTY_AGE, 1), 3);
            }
            worldIn.setBlockState(blockpos.up(), this.getDefaultState().with(PROPERTY_AGE, 0), 3);
            ++i;
            ++k;
        }

    }

    public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
        return player.getHeldItemMainhand().getItem() instanceof SwordItem ? 1.0F : super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
    }

    protected int getNumReedBlocksAbove(IBlockReader worldIn, BlockPos pos) {
        int i;
        for(i = 0; i < 4 && worldIn.getBlockState(pos.up(i + 1)).getBlock() == ModBlock.HEMLOCK.get(); ++i) {
        }
        return i;
    }

    protected int getNumReedBlocksBelow(IBlockReader worldIn, BlockPos pos) {
        int i;
        for(i = 0; i < 4 && worldIn.getBlockState(pos.down(i + 1)).getBlock() == ModBlock.HEMLOCK.get(); ++i) {
        }
        return i;
    }

    public void updatePostGen(ISeedReader worldIn, BlockPos pos) {
        if (worldIn.getRandom().nextBoolean()) {
            worldIn.setBlockState(pos, this.getDefaultState().with(PROPERTY_AGE, 1), 3);
            worldIn.setBlockState(pos.up(), this.getDefaultState().with(PROPERTY_AGE, 0), 3);
        }
    }
}

