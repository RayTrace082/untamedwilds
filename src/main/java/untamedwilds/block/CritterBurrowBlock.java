package untamedwilds.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import untamedwilds.block.blockentity.CritterBurrowBlockEntity;
import untamedwilds.init.ModItems;

import java.util.Random;

public class CritterBurrowBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public CritterBurrowBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.FALSE));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = this.defaultBlockState();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return blockstate.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collision) {
        return SHAPE;
    }

    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos);
    }

    protected boolean isValidGround(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return !state.getCollisionShape(worldIn, pos).getFaceShape(Direction.UP).isEmpty();
    }

    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
        if (!canSurvive(stateIn, worldIn, currentPos)) {
            worldIn.destroyBlock(currentPos, false);
        }

        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CritterBurrowBlockEntity(pos, state);
    }

    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.level.LevelReader world, BlockPos pos, int fortune, int silktouch) {
        return 10 + RANDOM.nextInt(10);
    }

    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
        if (worldIn.getBlockEntity(pos) instanceof CritterBurrowBlockEntity burrow) {
            burrow.releaseOrCreateMob(worldIn);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit) {
        if (worldIn.isClientSide || hand.equals(InteractionHand.OFF_HAND)) {
            return InteractionResult.FAIL;
        }
        else {
            CritterBurrowBlockEntity te = (CritterBurrowBlockEntity) worldIn.getBlockEntity(pos);
            if (playerIn.isCreative() && te != null) {

                if (playerIn.isSteppingCarefully())
                    te.releaseOrCreateMob((ServerLevel) worldIn);
                else {
                    playerIn.sendMessage(new TranslatableComponent("This burrow contains " + te.getEntityType().getDescriptionId()).withStyle(ChatFormatting.ITALIC), playerIn.getUUID());
                    playerIn.sendMessage(new TranslatableComponent("The variant is " + te.getVariant()).withStyle(ChatFormatting.ITALIC), playerIn.getUUID());
                    playerIn.sendMessage(new TranslatableComponent("There are " + (te.getInhabitants().size() + te.getCount()) + " mobs inside the burrow (" + te.getInhabitants().size() + " stored, and " + te.getCount() + " to be spawned)").withStyle(ChatFormatting.ITALIC), playerIn.getUUID());
                }
            }
            else {
                playerIn.sendMessage(new TranslatableComponent("block.burrow.state", te.getEntityType().getDescription().getString()), playerIn.getUUID());
            }
            return InteractionResult.SUCCESS;
        }
    }
}