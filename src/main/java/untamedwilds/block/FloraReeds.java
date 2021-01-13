package untamedwilds.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.SwordItem;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.entity.ComplexMobAquatic;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModTags.UTBlockTags;

import javax.annotation.Nullable;
import java.util.Random;

public class FloraReeds extends Block implements IGrowable, IWaterLoggable {
   protected static final VoxelShape SHAPE_NORMAL = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
   protected static final VoxelShape SHAPE_COLLISION = Block.makeCuboidShape(0D, 0.0D, 0D, 0D, 0D, 0D);
   public static final IntegerProperty PROPERTY_AGE = BlockStateProperties.AGE_0_2;
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   public static final IntegerProperty PROPERTY_STAGE = BlockStateProperties.STAGE_0_1;

   public FloraReeds(Properties properties) {
      super(properties);
      this.setDefaultState(this.stateContainer.getBaseState().with(PROPERTY_AGE, 0).with(PROPERTY_STAGE, 0).with(WATERLOGGED, Boolean.FALSE));
   }

   protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
      builder.add(PROPERTY_AGE, PROPERTY_STAGE, WATERLOGGED);
   }

   public OffsetType getOffsetType() {
      return OffsetType.XZ;
   }

   public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
      return true;
   }

   public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
      Vector3d vector3d = state.getOffset(worldIn, pos);
      return SHAPE_NORMAL.withOffset(vector3d.x, vector3d.y, vector3d.z);
   }

   public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
      return true;
   }

   public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
      Vector3d vector3d = state.getOffset(worldIn, pos);
      return SHAPE_COLLISION.withOffset(vector3d.x, vector3d.y, vector3d.z);
   }

   @Nullable
   public BlockState getStateForWorldgen(ISeedReader world, BlockPos pos) {
      boolean isWaterLogged = !world.getFluidState(pos).isEmpty();
      BlockState blockstate = world.getBlockState(pos.down());
      if (blockstate.getBlock() == ModBlock.COMMON_REED.get()) {
         if (world.getFluidState(pos.down()).isEmpty()) {
            world.setBlockState(pos.down(), blockstate.getBlockState().with(PROPERTY_AGE, 1), 1);
         }
         BlockState blockstate1 = world.getBlockState(pos.up());
         return blockstate1.getBlock() != ModBlock.COMMON_REED.get() ? ModBlock.COMMON_REED.get().getDefaultState().with(PROPERTY_AGE, 0).with(WATERLOGGED, isWaterLogged) : this.getDefaultState().with(PROPERTY_AGE, 1).with(WATERLOGGED, isWaterLogged);
      }
      if (blockstate.isIn(UTBlockTags.REEDS_PLANTABLE_ON)) {
         return this.getDefaultState().with(PROPERTY_AGE, isWaterLogged ? 2 : 0).with(WATERLOGGED, isWaterLogged);
      }
      else {
         return null;
      }
   }

   @Nullable
   public BlockState getStateForPlacement(BlockItemUseContext context) {
      boolean isWaterLogged = !context.getWorld().getFluidState(context.getPos()).isEmpty();
      BlockState blockstate = context.getWorld().getBlockState(context.getPos().down());
      if (blockstate.getBlock() == ModBlock.COMMON_REED.get()) {
         if (context.getWorld().getFluidState(context.getPos().down()).isEmpty()) {
            context.getWorld().setBlockState(context.getPos().down(), blockstate.getBlockState().with(PROPERTY_AGE, 1));
         }
         BlockState blockstate1 = context.getWorld().getBlockState(context.getPos().up());
         return blockstate1.getBlock() != ModBlock.COMMON_REED.get() ? ModBlock.COMMON_REED.get().getDefaultState().with(PROPERTY_AGE, 0).with(WATERLOGGED, isWaterLogged) : this.getDefaultState().with(PROPERTY_AGE, 1).with(WATERLOGGED, isWaterLogged);
      }
      if (blockstate.isIn(UTBlockTags.REEDS_PLANTABLE_ON)) {
         return this.getDefaultState().with(PROPERTY_AGE, isWaterLogged ? 2 : 0).with(WATERLOGGED, isWaterLogged);
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

   /**
    * Returns whether or not this block is of a type that needs random ticking. Called for ref-counting purposes by
    * ExtendedBlockStorage in order to broadly cull a chunk from the random chunk update list for efficiency's sake.
    */
   public boolean ticksRandomly(BlockState state) {
      return state.get(PROPERTY_STAGE) == 0;
   }

   /**
    * Performs a random tick on a block.
    */
   public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
      if (state.get(PROPERTY_STAGE) == 0 && random.nextInt(8) == 0) {
         if (worldIn.isAirBlock(pos.up()) && worldIn.getLightSubtracted(pos.up(), 0) >= 9) {
            int i = this.getNumReedBlocksBelow(worldIn, pos) + 1;
            if (i < 4 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt(3) == 0)) {
               this.grow(state, worldIn, pos, random, i);
               net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
            }
         }
      }
   }

   public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
      return worldIn.getBlockState(pos.down()).isIn(UTBlockTags.REEDS_PLANTABLE_ON) || worldIn.getBlockState(pos.down()).getBlock() == ModBlock.COMMON_REED.get();
   }

   /**
    * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
    * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
    * returns its solidified counterpart.
    * Note that this method should ideally consider only the specific face passed in.
    */
   public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
      if (stateIn.get(WATERLOGGED)) {
         worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
      }

      if (!stateIn.isValidPosition(worldIn, currentPos)) {
         worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
      }

      return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
   }

   /**
    * Whether this IGrowable can grow
    */
   public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
      int i = this.getNumReedBlocksAbove(worldIn, pos);
      int j = this.getNumReedBlocksBelow(worldIn, pos);
      return i + j + 1 < 4 && worldIn.getBlockState(pos.up(i)).get(PROPERTY_STAGE) != 1;
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
         if (k >= 4 || blockstate.get(PROPERTY_STAGE) == 1 || !worldIn.isAirBlock(blockpos.up())) {
            return;
         }
         this.grow(blockstate, worldIn, blockpos, rand, k);
         ++i;
         ++k;
      }

   }

   /**
    * Get the hardness of this Block relative to the ability of the given player
    * @deprecated call via stuff whenever
    * possible. Implementing/overriding is fine.
    */
   public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
      return player.getHeldItemMainhand().getItem() instanceof SwordItem ? 1.0F : super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
   }

   protected void grow(BlockState blockStateIn, World worldIn, BlockPos posIn, Random rand, int p_220258_5_) {
      BlockState blockstate = worldIn.getBlockState(posIn.down());
      int i = blockStateIn.get(PROPERTY_AGE) != 1 && blockstate.getBlock() != ModBlock.COMMON_REED.get() ? 0 : 1;
      int j = (p_220258_5_ < 1 || !(rand.nextFloat() < 0.4F)) && p_220258_5_ != 4 ? 0 : 1;
      if (blockStateIn.get(PROPERTY_AGE) != 2) {
         worldIn.setBlockState(posIn, this.getDefaultState().with(PROPERTY_AGE, 1).with(PROPERTY_STAGE, j), 3);
      }
      worldIn.setBlockState(posIn.up(), this.getDefaultState().with(PROPERTY_AGE, 0).with(PROPERTY_STAGE, j), 3);
   }

   public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
      if (entityIn instanceof LivingEntity && !(entityIn instanceof WaterMobEntity) && !(entityIn instanceof ComplexMobAquatic) && !entityIn.isInWater() && !entityIn.isSneaking()) {
         entityIn.setMotionMultiplier(state, new Vector3d(0.95F, 1D, 0.95F));
         /*if (worldIn.getRandom().nextInt(10) == 0) {
            worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CROP_BREAK, SoundCategory.AMBIENT, 1, 1, true);
         }*/
      }
   }

   /**
    * Returns the number of continuous bamboo blocks above the position passed in, up to 16.
    */
   protected int getNumReedBlocksAbove(IBlockReader worldIn, BlockPos pos) {
      int i;
      for(i = 0; i < 4 && worldIn.getBlockState(pos.up(i + 1)).getBlock() == ModBlock.COMMON_REED.get(); ++i) {
      }
      return i;
   }

   /**
    * Returns the number of continuous bamboo blocks below the position passed in, up to 16.
    */
   protected int getNumReedBlocksBelow(IBlockReader worldIn, BlockPos pos) {
      int i;
      for(i = 0; i < 4 && worldIn.getBlockState(pos.down(i + 1)).getBlock() == ModBlock.COMMON_REED.get(); ++i) {
      }
      return i;
   }

   public FluidState getFluidState(BlockState state) {
      return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
   }
}