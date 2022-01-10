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
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.block.blockentity.CritterBurrowBlockEntity;

import javax.annotation.Nullable;

public class CritterBurrowBlock extends Block implements IWaterLoggable {

    protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
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

    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return hasSolidSideOnTop(worldIn, pos.down());
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        if (!isValidPosition(stateIn, worldIn, currentPos)) {
            worldIn.destroyBlock(currentPos, false);
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
        if (worldIn.isRemote || hand.equals(Hand.OFF_HAND)) {
            return ActionResultType.FAIL;
        }
        else {
            CritterBurrowBlockEntity te = (CritterBurrowBlockEntity) worldIn.getTileEntity(pos);
            if (playerIn.isCreative() && te != null) {

                if (playerIn.isSneaking())
                    te.cooldown = 1;
                else {
                    playerIn.sendMessage(new TranslationTextComponent("This burrow contains " + te.getEntityType().getTranslationKey()).mergeStyle(TextFormatting.ITALIC), playerIn.getUniqueID());
                    playerIn.sendMessage(new TranslationTextComponent("The variant is " + te.getVariant()).mergeStyle(TextFormatting.ITALIC), playerIn.getUniqueID());
                    playerIn.sendMessage(new TranslationTextComponent("There are " + (te.getInhabitants().size() + te.getCount()) + " mobs inside the burrow (" + te.getInhabitants().size() + " stored, and " + te.getCount() + " to be spawned)").mergeStyle(TextFormatting.ITALIC), playerIn.getUniqueID());
                }
            }
            else {
                playerIn.sendStatusMessage(new TranslationTextComponent("block.burrow.state", te.getEntityType().getName().getString()), true);
            }
            return ActionResultType.SUCCESS;
        }
    }
}