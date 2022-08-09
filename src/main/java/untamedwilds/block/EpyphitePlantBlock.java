package untamedwilds.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;

public class EpyphitePlantBlock extends HorizontalDirectionalBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, Block.box(1D, 1D, 15D, 15D, 15D, 16D),
            Direction.SOUTH, Block.box(1D, 1D, 0.0D, 15D, 15D, 1D),
            Direction.WEST, Block.box(15D, 1D, 1D, 16D, 15D, 15D),
            Direction.EAST, Block.box(0.0D, 1D, 1D, 1D, 15D, 15D)));

    public EpyphitePlantBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing.getOpposite() == stateIn.getValue(FACING) && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : stateIn;
    }

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockpos = pos.relative(direction.getOpposite());
        BlockState blockstate = worldIn.getBlockState(blockpos);
        return blockstate.isFaceSturdy(worldIn, blockpos, direction);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = super.getStateForPlacement(context);
        LevelReader iworldreader = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Direction[] adirection = context.getNearestLookingDirections();

        for(Direction direction : adirection) {
            if (direction.getAxis().isHorizontal()) {
                blockstate = blockstate.setValue(FACING, direction.getOpposite());
                if (blockstate.canSurvive(iworldreader, blockpos)) {
                    return blockstate;
                }
            }
        }

        return null;
    }
}