package untamedwilds.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.pathfinding.PathType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
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

public class TitanArumBlock extends Block implements IGrowable, IPostGenUpdate {
   protected static final VoxelShape SHAPE_NORMAL = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
   protected static final VoxelShape SHAPE_POKE = Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D);
   protected static final VoxelShape SHAPE_CORM = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 1.0D, 14.0D);
   public static final IntegerProperty PROPERTY_AGE = BlockStateProperties.AGE_0_5; // AGE 5 is not used
   public static final IntegerProperty PROPERTY_STAGE = BlockStateProperties.STAGE_0_1;

   public TitanArumBlock(Properties properties) {
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
      if (state.get(PROPERTY_AGE) == 0) {
         return SHAPE_CORM;
      }
      Vector3d vector3d = state.getOffset(worldIn, pos);
      VoxelShape shape = state.get(PROPERTY_AGE) == 1 ? SHAPE_NORMAL : SHAPE_POKE;
      return shape.withOffset(vector3d.x, vector3d.y, vector3d.z);
   }

   public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
      return false;
   }

   public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
      return this.getShape(state, worldIn, pos, context);
   }

   @Nullable
   public BlockState getStateForWorldgen(ISeedReader world, BlockPos pos) {
      BlockState blockstate = world.getBlockState(pos.down());
      if (blockstate.getBlock() == ModBlock.TITAN_ARUM.get()) {
         return this.getDefaultState().with(PROPERTY_AGE, Math.min(3, world.getBlockState(pos.down()).get(PROPERTY_AGE) + 1)).with(PROPERTY_STAGE, 1);
      }
      else if (blockstate.isIn(BlockTags.REEDS_PLANTABLE_ON)) {
         return this.getDefaultState().with(PROPERTY_AGE, 1).with(PROPERTY_STAGE, 1);
      }
      return null;
   }

   @Nullable
   public BlockState getStateForPlacement(BlockItemUseContext context) {
      BlockState blockstate = context.getWorld().getBlockState(context.getPos().down());
      if (blockstate.getBlock() == ModBlock.TITAN_ARUM.get()) {
         int i = blockstate.get(PROPERTY_AGE);
         return this.getDefaultState().with(PROPERTY_AGE, Math.min(2, i + 1));
      }
      else if (blockstate.isIn(BlockTags.REEDS_PLANTABLE_ON)) {
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
      return worldIn.getBlockState(pos.down()).isIn(BlockTags.REEDS_PLANTABLE_ON) || worldIn.getBlockState(pos.down()).getBlock() == ModBlock.TITAN_ARUM.get();
   }

   public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
      if (!stateIn.isValidPosition(worldIn, currentPos)) {
         worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
      }

      return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
   }

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

      if (j < 3) {
         BlockPos blockpos = pos.up(i);
         BlockState blockstate = worldIn.getBlockState(blockpos);
         if (worldIn.getBlockState(pos.down()).getBlock() == ModBlock.TITAN_ARUM.get() || blockstate.get(PROPERTY_STAGE) == 1 || !worldIn.isAirBlock(blockpos.up())) {
            return;
         }
         if (k >= 3) {
            this.makeAreaOfEffectCloud(worldIn, pos);
            for(i = 0; i < 3 && worldIn.getBlockState(pos.up(i)).getBlock() == ModBlock.TITAN_ARUM.get(); ++i) {
               worldIn.setBlockState(pos.up(i), this.getDefaultState().with(PROPERTY_AGE, worldIn.getBlockState(pos.up(i)).get(PROPERTY_AGE)).with(PROPERTY_STAGE, 1), 3);
            }
            i = pos.getY();
            if (i >= 1 && i + 1 < 256) { // TODO: Needs to be changed by 1.17
               for(int n = 0; n < 3; ++n) {
                  pos = pos.add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1);
                  if (worldIn.getBlockState(pos).canBeReplacedByLeaves(worldIn, pos) && blockstate.isValidPosition(worldIn, pos)) {
                     worldIn.setBlockState(pos, ModBlock.TITAN_ARUM.get().getDefaultState().with(PROPERTY_STAGE, 1), 2);
                  }
               }
            }
            return;
         }
         this.grow(blockstate, worldIn, blockpos, rand, k);
      }
   }

   public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
      return player.getHeldItemMainhand().getItem() instanceof SwordItem ? 1.0F : super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
   }

   protected void grow(BlockState blockStateIn, World worldIn, BlockPos posIn, Random rand, int p_220258_5_) {
      int l = blockStateIn.get(PROPERTY_AGE);
      if (l == 0) {
         worldIn.setBlockState(posIn, this.getDefaultState().with(PROPERTY_AGE, 1));
      }
      else {
         worldIn.setBlockState(posIn.up(), this.getDefaultState().with(PROPERTY_AGE, Math.min(3, l + 1)).with(PROPERTY_STAGE, 0), 3);
      }
   }

   protected int getNumReedBlocksAbove(IBlockReader worldIn, BlockPos pos) {
      int i;
      for(i = 0; i < 4 && worldIn.getBlockState(pos.up(i + 1)).getBlock() == ModBlock.TITAN_ARUM.get(); ++i) {
      }
      return i;
   }

   protected int getNumReedBlocksBelow(IBlockReader worldIn, BlockPos pos) {
      int i;
      for(i = 0; i < 4 && worldIn.getBlockState(pos.down(i + 1)).getBlock() == ModBlock.TITAN_ARUM.get(); ++i) {
      }
      return i;
   }

   private void makeAreaOfEffectCloud(World worldIn, BlockPos pos) {
      AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(worldIn, pos.getX() + 0.5, pos.getY() - worldIn.getBlockState(pos).get(PROPERTY_AGE), pos.getZ() + 0.5);

      areaeffectcloudentity.setRadius(6.0F);
      areaeffectcloudentity.setRadiusOnUse(-0.2F);
      areaeffectcloudentity.setWaitTime(10);
      areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / ((float)areaeffectcloudentity.getDuration() * 0.5F));
      areaeffectcloudentity.setColor(5599028);
      areaeffectcloudentity.addEffect(new EffectInstance(Effects.NAUSEA, 80, 0, true, false));

      worldIn.addEntity(areaeffectcloudentity);
   }

   protected IItemProvider getSeedsItem() {
      return ModItems.SEED_TITAN_ARUM.get();
   }

   public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
      return new ItemStack(this.getSeedsItem());
   }

   public void updatePostGen(ISeedReader worldIn, BlockPos pos) {
      for (int i = 0; i < 3 && (worldIn.getBlockState(pos.up(i)).canBeReplacedByLeaves(worldIn, pos.up(i)) || worldIn.getBlockState(pos.up(i)).getBlock() == ModBlock.TITAN_ARUM.get()); i++) {
         worldIn.setBlockState(pos.up(i), this.getDefaultState().with(PROPERTY_AGE, i + 1).with(PROPERTY_STAGE, 1), 3);
      }
   }
}