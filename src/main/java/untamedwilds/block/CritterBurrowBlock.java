package untamedwilds.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.block.blockentity.CritterBurrowBlockEntity;

import javax.annotation.Nullable;

public class CritterBurrowBlock extends Block implements IWaterLoggable {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public CritterBurrowBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, Boolean.FALSE));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) {
        return new CritterBurrowBlockEntity();
    }

    public void spawnAdditionalDrops(BlockState state, ServerWorld worldIn, BlockPos pos, ItemStack stack) {
        super.spawnAdditionalDrops(state, worldIn, pos, stack);
    }

    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader world, BlockPos pos, int fortune, int silktouch) {
        return 10 + RANDOM.nextInt(10);
    }

    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {

        if (!playerIn.isCreative() || worldIn.isRemote) {
            return ActionResultType.FAIL;
        }
        else {
            CritterBurrowBlockEntity te = (CritterBurrowBlockEntity) worldIn.getTileEntity(pos);
            playerIn.sendMessage(new TranslationTextComponent("This burrow contains " + te.getMobToSpawn().getTranslationKey()).mergeStyle(TextFormatting.ITALIC), playerIn.getUniqueID());
            playerIn.sendMessage(new TranslationTextComponent("The variant is " + te.getVariant()).mergeStyle(TextFormatting.ITALIC), playerIn.getUniqueID());
            return ActionResultType.SUCCESS;
        }
    }
}