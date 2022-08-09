package untamedwilds.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CarpetBlock extends Block {

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public CarpetBlock(Properties p_i48290_2_) {
        super(p_i48290_2_);
    }

    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return SHAPE;
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState p_196271_3_, LevelAccessor p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        return !state.canSurvive(p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }

    public boolean canSurvive(BlockState p_196260_1_, LevelReader p_196260_2_, BlockPos p_196260_3_) {
        return !p_196260_2_.isEmptyBlock(p_196260_3_.below());
    }
}