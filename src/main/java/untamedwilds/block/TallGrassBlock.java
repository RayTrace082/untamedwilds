package untamedwilds.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModItems;
import untamedwilds.init.ModTags.ModBlockTags;

import javax.annotation.Nullable;
import java.util.Random;

public class TallGrassBlock extends Block implements BonemealableBlock, IPostGenUpdate {
   protected static final VoxelShape SHAPE_TRUNK = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D);
   protected static final VoxelShape SHAPE_STEM = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
   protected static final VoxelShape SHAPE_FLOWERING = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D);
   public static final IntegerProperty PROPERTY_AGE = BlockStateProperties.AGE_5;
   public static final IntegerProperty PROPERTY_STAGE = BlockStateProperties.STAGE;

   public TallGrassBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(this.stateDefinition.any().setValue(PROPERTY_AGE, 0).setValue(PROPERTY_STAGE, 0));
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(PROPERTY_AGE, PROPERTY_STAGE);
   }

   public OffsetType getOffsetType() {
      return OffsetType.XZ;
   }

   public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
      return true;
   }

   public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      if (state.getValue(PROPERTY_STAGE) == 1) {
         return SHAPE_FLOWERING;
      }
      Vec3 vector3d = state.getOffset(worldIn, pos);
      VoxelShape shape = state.getValue(PROPERTY_AGE) == 1 && state.getValue(PROPERTY_STAGE) == 1 ? SHAPE_STEM : SHAPE_TRUNK;
      return shape.move(vector3d.x, vector3d.y, vector3d.z);
   }

   public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
      return true;
   }

   public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      return state.getValue(PROPERTY_STAGE) == 1 ? Shapes.empty() : this.getShape(state, worldIn, pos, context);
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().below());
      if (blockstate.is(ModBlockTags.REEDS_PLANTABLE_ON)) {
         return this.defaultBlockState().setValue(PROPERTY_AGE, 0);
      }
      return null;
   }

   public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
      if (!state.canSurvive(worldIn, pos)) {
         worldIn.destroyBlock(pos, true);
      }
   }

   public boolean isRandomlyTicking(BlockState state) {
      return state.getValue(PROPERTY_STAGE) == 0;
   }

   public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
      if (state.getValue(PROPERTY_STAGE) == 0 && random.nextInt(8) == 0) {
         if (worldIn.isEmptyBlock(pos.above()) && worldIn.getLightEmission(pos.above()) >= 9) {
            int i = this.getNumReedBlocksBelow(worldIn, pos) + 1;
            if (i < 4 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt(3) == 0)) {
               this.grow(worldIn, pos, random, i);
               net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
            }
         }
      }
   }

   public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
      return worldIn.getBlockState(pos.below()).is(ModBlockTags.ALOE_PLANTABLE_ON) || worldIn.getBlockState(pos.below()).getBlock() == ModBlock.PAMPAS_GRASS.get();
   }

   public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
      if (!stateIn.canSurvive(worldIn, currentPos)) {
         worldIn.scheduleTick(currentPos, this, 1);
      }

      return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
   }

   public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
      int i = this.getNumReedBlocksAbove(worldIn, pos);
      return worldIn.getBlockState(pos.above(i)).getValue(PROPERTY_STAGE) != 1;
   }

   public boolean isBonemealSuccess(Level worldIn, Random rand, BlockPos pos, BlockState state) {
      return true;
   }

   public void performBonemeal(ServerLevel worldIn, Random rand, BlockPos pos, BlockState state) {
      int i = this.getNumReedBlocksAbove(worldIn, pos);
      int j = this.getNumReedBlocksBelow(worldIn, pos);
      int k = i + j + 1;

      if (worldIn.getBlockState(pos).getValue(PROPERTY_AGE) == 0) {
         worldIn.setBlockAndUpdate(pos, this.defaultBlockState().setValue(PROPERTY_AGE, 5));
      }
      else {
         BlockPos blockpos = pos.above(i);
         BlockState blockstate = worldIn.getBlockState(blockpos);
         if (blockstate.getValue(PROPERTY_STAGE) == 1 || !worldIn.isEmptyBlock(blockpos.above())) {
            return;
         }
         this.grow(worldIn, blockpos, rand, k);
      }
   }

   protected void grow(Level worldIn, BlockPos posIn, Random rand, int p_220258_5_) {
      worldIn.setBlockAndUpdate(posIn.above(), this.defaultBlockState().setValue(PROPERTY_AGE, 5));
      worldIn.setBlockAndUpdate(posIn, this.defaultBlockState().setValue(PROPERTY_AGE, 4));
      int j = 1;
      for(int i = 3; i >= 0 && worldIn.getBlockState(posIn.below(j)).getBlock() == ModBlock.PAMPAS_GRASS.get(); --i) {
         worldIn.setBlockAndUpdate(posIn.below(j), ModBlock.PAMPAS_GRASS.get().defaultBlockState().setValue(PROPERTY_AGE, Math.max(i, 1)));
         j++;
      }
      worldIn.setBlockAndUpdate(posIn.above(), this.defaultBlockState().setValue(PROPERTY_AGE, 5));
      if (p_220258_5_ == 2) {
         for(int i = 0; i < 3 && worldIn.getBlockState(posIn.above(i)).getBlock() == ModBlock.PAMPAS_GRASS.get(); ++i) {
            worldIn.setBlock(posIn.above(i), this.defaultBlockState().setValue(PROPERTY_AGE, worldIn.getBlockState(posIn.above(i)).getValue(PROPERTY_AGE)).setValue(PROPERTY_STAGE, 1), 3);
         }
      }
   }

   public float getDestroyProgress(BlockState state, Player player, BlockGetter worldIn, BlockPos pos) {
      return player.getMainHandItem().canPerformAction(net.minecraftforge.common.ToolActions.SWORD_DIG) ? 1.0F : super.getDestroyProgress(state, player, worldIn, pos);
   }

   protected int getNumReedBlocksAbove(BlockGetter worldIn, BlockPos pos) {
      int i;
      for(i = 0; i < 4 && worldIn.getBlockState(pos.above(i + 1)).getBlock() == ModBlock.PAMPAS_GRASS.get(); ++i) {
      }
      return i;
   }

   protected int getNumReedBlocksBelow(BlockGetter worldIn, BlockPos pos) {
      int i;
      for(i = 0; i < 4 && worldIn.getBlockState(pos.below(i + 1)).getBlock() == ModBlock.PAMPAS_GRASS.get(); ++i) {
      }
      return i;
   }

   protected ItemLike getSeedsItem() {
      return ModItems.SEED_ZIMBABWE_ALOE.get();
   }

   public ItemStack getCloneItemStack(BlockGetter p_60261_, BlockPos p_60262_, BlockState p_60263_) {
      return new ItemStack(this.getSeedsItem());
   }

   public void updatePostGen(LevelAccessor worldIn, BlockPos pos) {
      int i;
      for (i = 0; i < 3 && (worldIn.getBlockState(pos.above(i)).isAir() || worldIn.getBlockState(pos.above(i)).getBlock() == ModBlock.PAMPAS_GRASS.get()); i++) {
         worldIn.setBlock(pos.above(i), this.defaultBlockState().setValue(PROPERTY_AGE, Math.max(1, i + 3)), 3);
      }
   }
}