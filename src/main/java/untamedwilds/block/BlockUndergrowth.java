package untamedwilds.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockUndergrowth extends BushBlock implements IGrowable, net.minecraftforge.common.IForgeShearable {

    public BlockUndergrowth(Properties properties) {
        super(properties);
    }

    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.isSolidSide(worldIn, pos, Direction.UP) && !state.isIn(Blocks.MAGMA_BLOCK);
    }

    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return true;
    }
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof PlayerEntity && !entityIn.isSneaking()) {
            entityIn.setMotionMultiplier(state, new Vector3d(0.95F, 1D, 0.95F));
            if (worldIn.getRandom().nextInt(20) == 0) {
                worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_GRASS_STEP, SoundCategory.AMBIENT, 1, 1, true);
            }
        }
    }

    public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
        BlockState blockstate = worldIn.getBlockState(pos);
        int i = pos.getY();
        if (i >= 1 && i + 1 < 256) { // TODO: Needs to be changed by 1.17
            for(int k = 0; k < 3; ++k) {
                BlockPos blockpos = pos.add(rand.nextInt(3) - 1, 1 - rand.nextInt(3), rand.nextInt(3) - 1);
                if (worldIn.getBlockState(blockpos).canBeReplacedByLeaves(worldIn, blockpos) && blockstate.isValidPosition(worldIn, blockpos)) {
                    worldIn.setBlockState(blockpos, blockstate, 2);
                }
            }
        }
    }

    //public OffsetType getOffsetType() {
    //    return OffsetType.XZ;
    //}
}

