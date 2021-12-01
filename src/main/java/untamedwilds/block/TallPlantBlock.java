package untamedwilds.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModItems;
import untamedwilds.init.ModTags.BlockTags;

import javax.annotation.Nullable;
import java.util.Random;

public class TallPlantBlock extends Block implements IGrowable, IPostGenUpdate {
   protected static final VoxelShape SHAPE_TRUNK = Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D);
   protected static final VoxelShape SHAPE_STEM = Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
   protected static final VoxelShape SHAPE_FLOWERING = Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D);
   public static final IntegerProperty PROPERTY_AGE = BlockStateProperties.AGE_0_5;
   public static final IntegerProperty PROPERTY_STAGE = BlockStateProperties.STAGE_0_1;

   public TallPlantBlock(Properties properties) {
      super(properties);
      this.setDefaultState(this.stateContainer.getBaseState().with(PROPERTY_AGE, 0).with(PROPERTY_STAGE, 0));
   }

   protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
      builder.add(PROPERTY_AGE, PROPERTY_STAGE);
   }

   public OffsetType getOffsetType() {
      return OffsetType.XZ;
   }

   public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
      return true;
   }

   public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
      if (state.get(PROPERTY_STAGE) == 1) {
         return SHAPE_FLOWERING;
      }
      Vector3d vector3d = state.getOffset(worldIn, pos);
      VoxelShape shape = state.get(PROPERTY_AGE) == 1 && state.get(PROPERTY_STAGE) == 1 ? SHAPE_STEM : SHAPE_TRUNK;
      return shape.withOffset(vector3d.x, vector3d.y, vector3d.z);
   }

   public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
      return false;
   }

   public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
      return this.getShape(state, worldIn, pos, context);
   }

   @Nullable
   public BlockState getStateForPlacement(BlockItemUseContext context) {
      BlockState blockstate = context.getWorld().getBlockState(context.getPos().down());
      if (blockstate.isIn(BlockTags.REEDS_PLANTABLE_ON)) {
         return this.getDefaultState().with(PROPERTY_AGE, 0);
      }
      return null;
   }

   public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
      if (!state.isValidPosition(worldIn, pos)) {
         worldIn.destroyBlock(pos, true);
      }
   }

   public boolean ticksRandomly(BlockState state) {
      return state.get(PROPERTY_STAGE) == 0;
   }

   public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
      if (state.get(PROPERTY_STAGE) == 0 && rand.nextInt(8) == 0) {
         if (worldIn.isAirBlock(pos.up()) && worldIn.getLightSubtracted(pos.up(), 0) >= 9) {
            int i = this.getNumReedBlocksBelow(worldIn, pos) + 1;
            if (i < 4 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt(3) == 0)) {
               this.grow(worldIn, pos, rand, i);
               net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
            }
         }
      }
   }

   public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
      return worldIn.getBlockState(pos.down()).isIn(BlockTags.REEDS_PLANTABLE_ON) || worldIn.getBlockState(pos.down()).getBlock() == ModBlock.ZIMBABWE_ALOE.get();
   }

   public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
      if (!stateIn.isValidPosition(worldIn, currentPos)) {
         worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
      }

      return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
   }

   public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
      int i = this.getNumReedBlocksAbove(worldIn, pos);
      return worldIn.getBlockState(pos.up(i)).get(PROPERTY_STAGE) != 1;
   }

   public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
      return true;
   }

   public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
      int i = this.getNumReedBlocksAbove(worldIn, pos);
      int j = this.getNumReedBlocksBelow(worldIn, pos);
      int k = i + j + 1;

      if (worldIn.getBlockState(pos).get(PROPERTY_AGE) == 0) {
         worldIn.setBlockState(pos, this.getDefaultState().with(PROPERTY_AGE, 5));
      }
      else {
         BlockPos blockpos = pos.up(i);
         BlockState blockstate = worldIn.getBlockState(blockpos);
         if (blockstate.get(PROPERTY_STAGE) == 1 || !worldIn.isAirBlock(blockpos.up())) {
            return;
         }
         this.grow(worldIn, blockpos, rand, k);
      }
   }

   public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
      return player.getHeldItemMainhand().getItem() instanceof SwordItem ? 1.0F : super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
   }

   protected void grow(World worldIn, BlockPos posIn, Random rand, int p_220258_5_) {
      if ((p_220258_5_ > 2 && rand.nextInt(3) == 0) || p_220258_5_ > 4) {
         worldIn.setBlockState(posIn.up(), this.getDefaultState().with(PROPERTY_AGE, 5).with(PROPERTY_STAGE, 1));
      }
      else {
         worldIn.setBlockState(posIn.up(), this.getDefaultState().with(PROPERTY_AGE, 5));
         worldIn.setBlockState(posIn, this.getDefaultState().with(PROPERTY_AGE, 4));
         int j = 1;
         for(int i = 3; i >= 0 && worldIn.getBlockState(posIn.down(j)).getBlock() == ModBlock.ZIMBABWE_ALOE.get(); --i) {
            worldIn.setBlockState(posIn.down(j), ModBlock.ZIMBABWE_ALOE.get().getDefaultState().with(PROPERTY_AGE, Math.max(i, 1)));
            j++;
         }
         worldIn.setBlockState(posIn.up(), this.getDefaultState().with(PROPERTY_AGE, 5));
      }
   }

   protected int getNumReedBlocksAbove(IBlockReader worldIn, BlockPos pos) {
      int i;
      for(i = 0; i < 4 && worldIn.getBlockState(pos.up(i + 1)).getBlock() == ModBlock.ZIMBABWE_ALOE.get(); ++i) {
      }
      return i;
   }

   protected int getNumReedBlocksBelow(IBlockReader worldIn, BlockPos pos) {
      int i;
      for(i = 0; i < 4 && worldIn.getBlockState(pos.down(i + 1)).getBlock() == ModBlock.ZIMBABWE_ALOE.get(); ++i) {
      }
      return i;
   }

   protected IItemProvider getSeedsItem() {
      return ModItems.SEED_ZIMBABWE_ALOE.get();
   }

   public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
      return new ItemStack(this.getSeedsItem());
   }

   public void updatePostGen(ISeedReader worldIn, BlockPos pos) {
      int i;
      for (i = 0; i < 3 && (worldIn.getBlockState(pos.up(i)).canBeReplacedByLeaves(worldIn, pos.up(i)) || worldIn.getBlockState(pos.up(i)).getBlock() == ModBlock.ZIMBABWE_ALOE.get()); i++) {
         worldIn.setBlockState(pos.up(i), this.getDefaultState().with(PROPERTY_AGE, Math.max(1, i + 3)), 3);
      }
   }
}