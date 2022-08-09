package untamedwilds.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import untamedwilds.entity.ComplexMobAquatic;

import java.util.Random;

public class FloatingPlantBlock extends BushBlock implements BonemealableBlock {
    protected static final VoxelShape SHAPE_NORMAL = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 12D, 13.0D);

    public FloatingPlantBlock(BlockBehaviour.Properties builder) {
        super(builder);
    }

    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof LivingEntity && !(entityIn instanceof WaterAnimal) && !(entityIn instanceof ComplexMobAquatic) && entityIn.isInWater() && !entityIn.isSteppingCarefully()) {
            entityIn.makeStuckInBlock(state, new Vec3(0.98F, 1D, 0.98F));
            if (worldIn.getRandom().nextInt(20) == 0) {
                worldIn.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.WET_GRASS_STEP, SoundSource.AMBIENT, 1, 1, true);
            }
        }
    }

    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Vec3 vector3d = state.getOffset(worldIn, pos);
        return SHAPE_NORMAL.move(vector3d.x, vector3d.y, vector3d.z);
    }

    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return true;
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    protected boolean isValidGround(BlockState state, BlockGetter worldIn, BlockPos pos) {
        FluidState fluidstate = worldIn.getFluidState(pos);
        FluidState fluidstate1 = worldIn.getFluidState(pos.above());
        return (fluidstate.getType() == Fluids.WATER || state.getMaterial() == Material.ICE) && fluidstate1.getType() == Fluids.EMPTY;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos);
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
            if (worldIn.isInWorldBounds(blockpos) && worldIn.isEmptyBlock(blockpos) && worldIn.getBlockState(blockpos).is(Blocks.WATER) && blockstate.canSurvive(worldIn, blockpos)) {
                worldIn.setBlock(blockpos, blockstate, 2);
            }
        }
    }
}