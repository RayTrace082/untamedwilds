package untamedwilds.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.block.blockentity.ReptileNestBlockEntity;
import untamedwilds.config.ConfigMobControl;

import java.util.Random;

public class NestReptileBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {

    protected static final VoxelShape SHAPE = Block.box(1D, 0.0D, 1D, 15D, 3.5D, 15D);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public NestReptileBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.FALSE));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = this.defaultBlockState();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return blockstate.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collision) {
        return SHAPE;
    }

    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos);
    }

    protected boolean isValidGround(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return !state.getCollisionShape(worldIn, pos).getFaceShape(Direction.UP).isEmpty();
    }

    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
        if (!canSurvive(stateIn, worldIn, currentPos)) {
            worldIn.destroyBlock(currentPos, false);
        }

        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ReptileNestBlockEntity(pos, state);
    }

    public void fallOn(Level levelIn, BlockState stateIn, BlockPos posIn, Entity entityIn, float p_154849_) {
        ReptileNestBlockEntity te = (ReptileNestBlockEntity) levelIn.getBlockEntity(posIn);
        if (te != null && !entityIn.getType().equals(te.getEntityType())) {
            te.destroyEgg(levelIn, posIn, stateIn);
        }

        super.fallOn(levelIn, stateIn, posIn, entityIn, p_154849_);
    }

    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
        if (ConfigMobControl.tickingNests.get() && worldIn.getBlockEntity(pos) instanceof ReptileNestBlockEntity burrow) {
            burrow.createMobs(worldIn);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit) {
        if (worldIn.isClientSide || hand.equals(InteractionHand.OFF_HAND)) {
            return InteractionResult.FAIL;
        }
        else {
            ReptileNestBlockEntity te = (ReptileNestBlockEntity) worldIn.getBlockEntity(pos);
            if (te != null) {
                if (playerIn.isCreative() && playerIn.isSteppingCarefully()) {
                    UntamedWilds.LOGGER.info(te.getEggCount()); // TODO: DEBUG
                }
                else {
                    te.removeEggs(worldIn, 1);
                    CompoundTag baseTag = new CompoundTag();
                    ItemStack item = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(UntamedWilds.MOD_ID + ":egg_" + te.getEntityType().getRegistryName().getPath())));
                    baseTag.putInt("variant", te.getVariant());
                    baseTag.putInt("custom_model_data", te.getVariant());
                    item.setTag(baseTag);
                    playerIn.getInventory().add(item);
                }
            }
            return InteractionResult.SUCCESS;
        }
    }
}