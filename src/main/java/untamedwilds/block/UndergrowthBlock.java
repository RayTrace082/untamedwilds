package untamedwilds.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class UndergrowthBlock extends BushBlock implements BonemealableBlock, net.minecraftforge.common.IForgeShearable {

    protected OffsetType offset;

    public UndergrowthBlock(Properties properties) {
        super(properties);
        this.offset = OffsetType.NONE;
    }

    public UndergrowthBlock(Properties properties, OffsetType type) {
        super(properties);
        this.offset = type;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return state.isFaceSturdy(worldIn, pos, Direction.UP) && !state.is(Blocks.MAGMA_BLOCK);
    }

    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return true;
    }

    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof Player && !entityIn.isSteppingCarefully()) {
            entityIn.makeStuckInBlock(state, new Vec3(0.95F, 1D, 0.95F));
            if (worldIn.getRandom().nextInt(20) == 0) {
                worldIn.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.GRASS_STEP, SoundSource.AMBIENT, 1, 1, true);
            }
        }
    }

    public OffsetType getOffsetType() {
        return this.offset;
    }

    public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    public boolean isBonemealSuccess(Level worldIn, Random rand, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel worldIn, Random rand, BlockPos pos, BlockState state) {
        BlockState blockstate = worldIn.getBlockState(pos);
        for(int k = 0; k < 3; ++k) {
            BlockPos blockpos = pos.offset(rand.nextInt(3) - 1, 1 - rand.nextInt(3), rand.nextInt(3) - 1);
            if (worldIn.isInWorldBounds(blockpos) && worldIn.isEmptyBlock(blockpos) && blockstate.canSurvive(worldIn, blockpos)) {
                worldIn.setBlock(blockpos, blockstate, 2);
            }
        }
    }
}

