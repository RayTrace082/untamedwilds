package untamedwilds.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import untamedwilds.entity.ComplexMobAquatic;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModTags.ModBlockTags;

import javax.annotation.Nullable;
import java.util.Random;

public class ReedBlock extends Block implements BonemealableBlock, SimpleWaterloggedBlock {
   protected static final VoxelShape SHAPE_NORMAL = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
   public static final IntegerProperty PROPERTY_AGE = BlockStateProperties.AGE_2;
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   public static final IntegerProperty PROPERTY_STAGE = BlockStateProperties.STAGE;

   public ReedBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(this.stateDefinition.any().setValue(PROPERTY_AGE, 0).setValue(PROPERTY_STAGE, 0).setValue(WATERLOGGED, Boolean.FALSE));
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(PROPERTY_AGE, PROPERTY_STAGE, WATERLOGGED);
   }

   public OffsetType getOffsetType() {
      return OffsetType.XZ;
   }

   public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
      return true;
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

   @Nullable
   public BlockState getStateForWorldgen(LevelAccessor world, BlockPos pos) {
      boolean isWaterLogged = !world.getFluidState(pos).isEmpty();
      BlockState blockstate = world.getBlockState(pos.below());
      if (blockstate.getBlock() == ModBlock.COMMON_REED.get()) {
         if (world.getFluidState(pos.below()).isEmpty() || world.getBlockState(pos.below(2)).getBlock() == ModBlock.COMMON_REED.get()) {
            world.setBlock(pos.below(), blockstate.setValue(PROPERTY_AGE, 1), 1);
         }
         BlockState blockstate1 = world.getBlockState(pos.above());
         return blockstate1.getBlock() != ModBlock.COMMON_REED.get() ? ModBlock.COMMON_REED.get().defaultBlockState().setValue(PROPERTY_AGE, 0).setValue(WATERLOGGED, isWaterLogged) : this.defaultBlockState().setValue(PROPERTY_AGE, 1).setValue(WATERLOGGED, isWaterLogged);
      }
      if (blockstate.is(ModBlockTags.REEDS_PLANTABLE_ON)) {
         return this.defaultBlockState().setValue(PROPERTY_AGE, isWaterLogged ? 2 : 0).setValue(WATERLOGGED, isWaterLogged);
      }
      else {
         return null;
      }
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      boolean isWaterLogged = !context.getLevel().getFluidState(context.getClickedPos()).isEmpty();
      BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().below());
      if (blockstate.getBlock() == ModBlock.COMMON_REED.get()) {
         if (context.getLevel().getFluidState(context.getClickedPos().below()).isEmpty() || context.getLevel().getBlockState(context.getClickedPos().below(2)).getBlock() == ModBlock.COMMON_REED.get()) {
            context.getLevel().setBlockAndUpdate(context.getClickedPos().below(), blockstate.setValue(PROPERTY_AGE, 1));
         }
         BlockState blockstate1 = context.getLevel().getBlockState(context.getClickedPos().above());
         return blockstate1.getBlock() != ModBlock.COMMON_REED.get() ? ModBlock.COMMON_REED.get().defaultBlockState().setValue(PROPERTY_AGE, 0).setValue(WATERLOGGED, isWaterLogged) : this.defaultBlockState().setValue(PROPERTY_AGE, 1).setValue(WATERLOGGED, isWaterLogged);
      }
      if (blockstate.is(ModBlockTags.REEDS_PLANTABLE_ON)) {
         return this.defaultBlockState().setValue(PROPERTY_AGE, isWaterLogged ? 2 : 0).setValue(WATERLOGGED, isWaterLogged);
      }
      else {
         return null;
      }
   }

   public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
      if (!state.canSurvive(worldIn, pos)) {
         worldIn.destroyBlock(pos, true);
      }
   }

   public boolean isRandomlyTicking(BlockState state) {
      return state.getValue(PROPERTY_STAGE) == 0;
   }

   public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
      if (state.getValue(PROPERTY_STAGE) == 0 && random.nextInt(8) == 0) {
         if (worldIn.isEmptyBlock(pos.above()) && worldIn.getLightEmission(pos.above()) >= 9) {
            int i = this.getNumReedBlocksBelow(worldIn, pos) + 1;
            if (i < 4 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt(3) == 0)) {
               this.grow(state, worldIn, pos, random, i);
               net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
            }
         }
      }
   }

   public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
      return worldIn.getBlockState(pos.below()).is(ModBlockTags.REEDS_PLANTABLE_ON) || worldIn.getBlockState(pos.below()).getBlock() == ModBlock.COMMON_REED.get();
   }

   public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
      if (stateIn.getValue(WATERLOGGED)) {
         worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
      }

      if (!stateIn.canSurvive(worldIn, currentPos)) {
         worldIn.scheduleTick(currentPos, this, 1);
      }

      return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
   }

   public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
      int i = this.getNumReedBlocksAbove(worldIn, pos);
      int j = this.getNumReedBlocksBelow(worldIn, pos);
      return i + j + 1 < 4 && worldIn.getBlockState(pos.above(i)).getValue(PROPERTY_STAGE) != 1;
   }

   public boolean isBonemealSuccess(Level worldIn, RandomSource rand, BlockPos pos, BlockState state) {
      return true;
   }

   public void performBonemeal(ServerLevel worldIn, RandomSource rand, BlockPos pos, BlockState state) {
      int i = this.getNumReedBlocksAbove(worldIn, pos);
      int j = this.getNumReedBlocksBelow(worldIn, pos);
      int k = i + j + 1;
      int l = 1 + rand.nextInt(2);

      for(int i1 = 0; i1 < l; ++i1) {
         BlockPos blockpos = pos.above(i);
         BlockState blockstate = worldIn.getBlockState(blockpos);
         if (k >= 4 || blockstate.getValue(PROPERTY_STAGE) == 1 || !worldIn.isEmptyBlock(blockpos.above())) {
            return;
         }
         this.grow(blockstate, worldIn, blockpos, rand, k);
         ++i;
         ++k;
      }
   }

   protected void grow(BlockState blockStateIn, Level worldIn, BlockPos posIn, RandomSource rand, int p_220258_5_) {
      BlockState blockstate = worldIn.getBlockState(posIn.below());
      int i = blockStateIn.getValue(PROPERTY_AGE) != 1 && blockstate.getBlock() != ModBlock.COMMON_REED.get() ? 0 : 1;
      int j = (p_220258_5_ < 1 || !(rand.nextFloat() < 0.4F)) && p_220258_5_ != 4 ? 0 : 1;
      if (blockStateIn.getValue(PROPERTY_AGE) != 2) {
         worldIn.setBlock(posIn, this.defaultBlockState().setValue(PROPERTY_AGE, 1).setValue(PROPERTY_STAGE, j), 3);
      }
      worldIn.setBlock(posIn.above(), this.defaultBlockState().setValue(PROPERTY_AGE, 0).setValue(PROPERTY_STAGE, j), 3);
   }

   public float getDestroyProgress(BlockState state, Player player, BlockGetter worldIn, BlockPos pos) {
      return player.getMainHandItem().canPerformAction(net.minecraftforge.common.ToolActions.SWORD_DIG) ? 1.0F : super.getDestroyProgress(state, player, worldIn, pos);
   }

   public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
      if (entityIn instanceof LivingEntity && !(entityIn instanceof WaterAnimal) && !(entityIn instanceof ComplexMobAquatic) && !entityIn.isInWater() && !entityIn.isSteppingCarefully()) {
         entityIn.makeStuckInBlock(state, new Vec3(0.95F, 1D, 0.95F));
         if (worldIn.getRandom().nextInt(20) == 0) {
            worldIn.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.GRASS_STEP, SoundSource.AMBIENT, 1, 1, true);
         }
      }
   }

   protected int getNumReedBlocksAbove(BlockGetter worldIn, BlockPos pos) {
      int i;
      for(i = 0; i < 4 && worldIn.getBlockState(pos.above(i + 1)).getBlock() == ModBlock.COMMON_REED.get(); ++i) {
      }
      return i;
   }

   protected int getNumReedBlocksBelow(BlockGetter worldIn, BlockPos pos) {
      int i;
      for(i = 0; i < 4 && worldIn.getBlockState(pos.below(i + 1)).getBlock() == ModBlock.COMMON_REED.get(); ++i) {
      }
      return i;
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }
}