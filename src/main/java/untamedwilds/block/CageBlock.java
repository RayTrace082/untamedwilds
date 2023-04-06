package untamedwilds.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.block.blockentity.CageBlockEntity;
import untamedwilds.init.ModBlock;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class CageBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {
    
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape CAGE_COLLISION_AABB_EMPTY = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D);
    private static final VoxelShape CAGE_COLLISION_AABB_FULL = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);

    public CageBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPEN, WATERLOGGED);
    }

    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        this.spawnDestroyParticles(worldIn, player, pos, state);
        if (!worldIn.isClientSide) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof CageBlockEntity te) {

                ItemStack itemstack = new ItemStack(ModBlock.TRAP_CAGE.get());
                CompoundTag compound = new CompoundTag();
                if (te.hasTagCompound() && te.isLocked()) {
                    compound.putBoolean("closed", te.isLocked());
                    compound.put("EntityTag", te.getTagCompound().getCompound("EntityTag"));
                    itemstack.setTag(compound);
                }

                popResource(worldIn, pos, itemstack);
                worldIn.updateNeighbourForOutputSignal(pos, state.getBlock());
            }
            else { super.playerWillDestroy(worldIn, pos, state, player); }
        }
    }

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return state.getValue(OPEN) ? CAGE_COLLISION_AABB_EMPTY : CAGE_COLLISION_AABB_FULL;
    }

    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return state.getValue(OPEN);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = this.defaultBlockState();
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return blockstate.setValue(OPEN, Boolean.TRUE).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }


    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (stack.hasTag()) {
            BlockEntity te = worldIn.getBlockEntity(pos);
            if (stack.getTag() != null && te instanceof CageBlockEntity blockEntity) {
                te.load(stack.getTag());
                if (blockEntity.isLocked() && blockEntity.hasTagCompound()) {
                    worldIn.setBlockAndUpdate(pos, state.setValue(OPEN, Boolean.FALSE));
                }
            }
        }
    }

    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            if (worldIn.hasNeighborSignal(pos)) {
                trySpawningEntity(state, (ServerLevel) worldIn, pos);
            }
        }
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }

        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit) {

        if (playerIn.isSteppingCarefully() || worldIn.isClientSide || state.getValue(OPEN)) {
            return InteractionResult.FAIL;
        }
        else {
            boolean success = trySpawningEntity(state, (ServerLevel) worldIn, pos);
            return success ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        }
    }

    private boolean trySpawningEntity(BlockState state, ServerLevel worldIn, BlockPos pos) {
        CageBlockEntity te = (CageBlockEntity)worldIn.getBlockEntity(pos);
        BlockPos check = pos.below();
        if (te != null) {
            BlockPos spawnpos = /*!worldIn.getBlockState(check).isSolid() ? pos :*/ new BlockPos(pos.getX(), pos.getY() + 1F, pos.getZ());
            if (te.spawnCagedCreature(worldIn, spawnpos, worldIn.isEmptyBlock(check))) {
                spawnParticles(worldIn, pos, ParticleTypes.POOF);
                worldIn.playSound(null, pos, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundSource.BLOCKS, 0.3F, 0.8F);
                worldIn.setBlockAndUpdate(pos, state.setValue(OPEN, Boolean.TRUE));
                return true;
            }
            else {
                spawnParticles(worldIn, pos, ParticleTypes.SMOKE);
                worldIn.playSound(null, pos, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundSource.BLOCKS, 0.3F, 0.6F);
            }
        }
        return false;
    }

    private <T extends ParticleOptions> void spawnParticles(Level worldIn, BlockPos pos, T particle) {
        RandomSource random = worldIn.getRandom();
        float d3 = random.nextFloat() * 0.02F;
        float d1 = random.nextFloat() * 0.02F;
        float d2 = random.nextFloat() * 0.02F;
        ((ServerLevel)worldIn).sendParticles(particle, pos.getX() + random.nextFloat(), pos.getY(), pos.getZ() + random.nextFloat(), 15, d3, d1, d2, 0.12F);
    }


    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        CageBlockEntity te = (CageBlockEntity)world.getBlockEntity(pos);
        if (!world.isClientSide && !(entity instanceof Player) && entity.isAlive() && entity instanceof LivingEntity) {
            if (te != null && !te.isLocked() && entity instanceof Mob) {
                if (te.cageEntity((Mob) entity)) {
                    world.playSound(null, pos, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundSource.BLOCKS, 0.3F, 0.8F);
                    world.setBlockAndUpdate(pos, state.setValue(OPEN, Boolean.FALSE));
                    spawnParticles(world, pos, ParticleTypes.POOF);
                }
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.getTag() != null) {
            EntityType<?> type = EntityUtils.getEntityTypeFromTag(stack.getTag(), null);
            if (type != null) {
                EntityUtils.buildTooltipData(stack, tooltip, type, EntityUtils.getVariantName(type, stack.getTag().getCompound("EntityTag").getInt("Variant")));
            }
        }
        else {
            tooltip.add( MutableComponent.create(new TranslatableContents("block.trap_cage.state_empty")).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CageBlockEntity(pos, state);
    }

    public static class DispenserBehaviorTrapCage implements DispenseItemBehavior {

        public ItemStack dispense(BlockSource source, ItemStack stack) {
            Item item = stack.getItem();
            if (item instanceof BlockItem) {
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos blockpos = source.getPos().relative(direction);
                Direction direction1 = source.getLevel().isEmptyBlock(blockpos.below()) ? direction : Direction.UP;
                boolean successful = ((BlockItem)item).place(new DirectionalPlaceContext(source.getLevel(), blockpos, direction, stack, direction1)) == InteractionResult.SUCCESS;
            }
            return stack;
        }

        protected void playDispenseSound(BlockSource source) {
            source.getLevel().globalLevelEvent(1000, source.getPos(), 0);
        }
    }
}