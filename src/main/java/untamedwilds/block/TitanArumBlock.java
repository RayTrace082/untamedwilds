package untamedwilds.block;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModItems;
import untamedwilds.init.ModTags.ModBlockTags;

import javax.annotation.Nullable;
import java.util.Random;

public class TitanArumBlock extends Block implements BonemealableBlock, IPostGenUpdate {
   protected static final VoxelShape SHAPE_NORMAL = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
   protected static final VoxelShape SHAPE_SPATHE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D);
   protected static final VoxelShape SHAPE_CORM = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 1.0D, 14.0D);
   public static final IntegerProperty PROPERTY_AGE = BlockStateProperties.AGE_5; // AGE 5 is not used
   public static final IntegerProperty PROPERTY_STAGE = BlockStateProperties.STAGE;

   public TitanArumBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(this.stateDefinition.any().setValue(PROPERTY_AGE, 0).setValue(PROPERTY_STAGE, 0));
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(PROPERTY_AGE, PROPERTY_STAGE);
   }

   public OffsetType getOffsetType() {
      return OffsetType.XZ;
   }

   public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      if (state.getValue(PROPERTY_AGE) == 0) {
         return SHAPE_CORM;
      }
      Vec3 vector3d = state.getOffset(worldIn, pos);
      VoxelShape shape = state.getValue(PROPERTY_AGE) == 1 && state.getValue(PROPERTY_STAGE) == 1 ? SHAPE_NORMAL : SHAPE_SPATHE;
      return shape.move(vector3d.x, vector3d.y, vector3d.z);
   }

   public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
      return true;
   }

   public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      return this.getShape(state, worldIn, pos, context);
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
               this.grow(state, worldIn, pos, random, i);
               net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
            }
         }
      }
      // TODO: Re-enable mob luring in the future
      /* if (state.get(PROPERTY_STAGE) == 1 && state.get(PROPERTY_AGE) > 1) {
         List<MobEntity> list = worldIn.getEntitiesOfClass(MobEntity.class, VoxelShapes.fullCube().getBoundingBox().inflate(12, 4, 12));
         list.removeIf(input -> !(input.getCreatureAttribute() == CreatureAttribute.ARTHROPOD || input.getCreatureAttribute() == CreatureAttribute.UNDEAD || input.getTarget() != null));
         if (!list.isEmpty()) {
            MobEntity lured_entity = list.get(random.nextInt(list.size()));
            EntityUtils.spawnParticlesOnEntity(worldIn, lured_entity, ParticleTypes.END_ROD, 8, 1);
            lured_entity.getNavigation().tryMoveToXYZ(pos.getX() + random.nextInt(6) - 3, pos.getY(), pos.getZ() + random.nextInt(6) - 3, 1);
         }
         // Attract Undead and Arthropods, spawn new ones 10% of the time?
      } */
   }

   public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
      return worldIn.getBlockState(pos.below()).is(ModBlockTags.REEDS_PLANTABLE_ON) || worldIn.getBlockState(pos.below()).getBlock() == ModBlock.TITAN_ARUM.get();
   }

   public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
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

   public boolean isBonemealSuccess(Level worldIn, Random rand, BlockPos pos, BlockState state) {
      return true;
   }

   public void performBonemeal(ServerLevel worldIn, Random rand, BlockPos pos, BlockState state) {
      int i = this.getNumReedBlocksAbove(worldIn, pos);
      int j = this.getNumReedBlocksBelow(worldIn, pos);
      int k = i + j + 1;

      if (j < 3) {
         BlockPos blockpos = pos.above(i);
         BlockState blockstate = worldIn.getBlockState(blockpos);
         if (worldIn.getBlockState(pos.below()).getBlock() == ModBlock.TITAN_ARUM.get() || blockstate.getValue(PROPERTY_STAGE) == 1 || !worldIn.isEmptyBlock(blockpos.above())) {
            return;
         }
         if (k >= 3) {
            this.makeAreaOfEffectCloud(worldIn, pos);
            for(i = 0; i < 3 && worldIn.getBlockState(pos.above(i)).getBlock() == ModBlock.TITAN_ARUM.get(); ++i) {
               worldIn.setBlock(pos.above(i), this.defaultBlockState().setValue(PROPERTY_AGE, worldIn.getBlockState(pos.above(i)).getValue(PROPERTY_AGE)).setValue(PROPERTY_STAGE, 1), 3);
            }
            for(int l = 0; l < 3; ++l) {
               BlockPos seed_pos = pos.offset(rand.nextInt(3) - 1, 1 - rand.nextInt(3), rand.nextInt(3) - 1);
               if (worldIn.isInWorldBounds(seed_pos) && worldIn.getBlockState(seed_pos).is(Blocks.WATER) && blockstate.canSurvive(worldIn, seed_pos)) {
                  worldIn.setBlock(seed_pos, ModBlock.TITAN_ARUM.get().defaultBlockState().setValue(PROPERTY_STAGE, 1), 2);
               }
            }
            return;
         }
         this.grow(blockstate, worldIn, blockpos, rand, k);
      }
   }

   protected void grow(BlockState blockStateIn, Level worldIn, BlockPos posIn, Random rand, int p_220258_5_) {
      int l = blockStateIn.getValue(PROPERTY_AGE);
      if (l == 0) {
         worldIn.setBlockAndUpdate(posIn, this.defaultBlockState().setValue(PROPERTY_AGE, 1));
      }
      else {
         worldIn.setBlock(posIn.above(), this.defaultBlockState().setValue(PROPERTY_AGE, Math.min(3, l + 1)).setValue(PROPERTY_STAGE, 0), 3);
      }
   }

   public float getDestroyProgress(BlockState state, Player player, BlockGetter worldIn, BlockPos pos) {
      return player.getMainHandItem().canPerformAction(net.minecraftforge.common.ToolActions.SWORD_DIG) ? 1.0F : super.getDestroyProgress(state, player, worldIn, pos);
   }

   protected int getNumReedBlocksAbove(BlockGetter worldIn, BlockPos pos) {
      int i;
      for(i = 0; i < 4 && worldIn.getBlockState(pos.above(i + 1)).getBlock() == ModBlock.TITAN_ARUM.get(); ++i) {
      }
      return i;
   }

   protected int getNumReedBlocksBelow(BlockGetter worldIn, BlockPos pos) {
      int i;
      for(i = 0; i < 4 && worldIn.getBlockState(pos.below(i + 1)).getBlock() == ModBlock.TITAN_ARUM.get(); ++i) {
      }
      return i;
   }

   private void makeAreaOfEffectCloud(Level worldIn, BlockPos pos) {
      AreaEffectCloud areaeffectcloudentity = new AreaEffectCloud(worldIn, pos.getX() + 0.5, pos.getY() - worldIn.getBlockState(pos).getValue(PROPERTY_AGE), pos.getZ() + 0.5);

      areaeffectcloudentity.setRadius(6.0F);
      areaeffectcloudentity.setRadiusOnUse(-0.2F);
      areaeffectcloudentity.setWaitTime(10);
      areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / ((float)areaeffectcloudentity.getDuration() * 0.5F));
      areaeffectcloudentity.setFixedColor(5599028);
      areaeffectcloudentity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0, true, false));

      worldIn.addFreshEntity(areaeffectcloudentity);
   }

   public ItemStack getCloneItemStack(BlockGetter p_60261_, BlockPos p_60262_, BlockState p_60263_) {
      return new ItemStack(ModItems.SEED_TITAN_ARUM.get());
   }

   public void updatePostGen(LevelAccessor worldIn, BlockPos pos) {
      for (int i = 0; i < 3 && (worldIn.getBlockState(pos.above(i)).isAir() || worldIn.getBlockState(pos.above(i)).getBlock() == ModBlock.TITAN_ARUM.get()); i++) {
         worldIn.setBlock(pos.above(i), this.defaultBlockState().setValue(PROPERTY_AGE, i + 1).setValue(PROPERTY_STAGE, 1), 3);
      }
   }
}