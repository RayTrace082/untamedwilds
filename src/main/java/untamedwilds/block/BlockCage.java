package untamedwilds.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.block.tileentity.BlockEntityCage;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.init.ModBlock;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

// BUG: It's possible to catch ghost entities if a mob touches multiple boxes in the same tick (eg. 2x2 mob falling into a pit full of Cages)
public class BlockCage extends Block implements IWaterLoggable {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape CAGE_COLLISION_AABB_EMPTY = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D);
    private static final VoxelShape CAGE_COLLISION_AABB_FULL = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);

    public BlockCage(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(OPEN, Boolean.FALSE).with(WATERLOGGED, Boolean.FALSE));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(OPEN, WATERLOGGED);
    }

    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isRemote) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof BlockEntityCage) {

                BlockEntityCage te = (BlockEntityCage)tileentity;

                ItemStack itemstack = new ItemStack(ModBlock.TRAP_CAGE.get());
                CompoundNBT compound = new CompoundNBT();
                if (te.hasTagCompound() && te.hasCagedEntity()) {
                    compound.putBoolean("closed", te.hasCagedEntity());
                    compound.put("EntityTag", te.getTagCompound());
                    itemstack.setTag(compound);
                }

                spawnAsEntity(worldIn, pos, itemstack);
                worldIn.updateComparatorOutputLevel(pos, state.getBlock());
            }
            else { super.onBlockHarvested(worldIn, pos, state, player); }
        }
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return state.get(OPEN) ? CAGE_COLLISION_AABB_EMPTY : CAGE_COLLISION_AABB_FULL;
    }

    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return state.get(OPEN);
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState blockstate = this.getDefaultState();
        FluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        return blockstate.with(OPEN, Boolean.TRUE).with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (stack.hasTag()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (stack.getTag() != null && te != null) {
                te.read(state, stack.getTag());
                BlockEntityCage tileentity = (BlockEntityCage)worldIn.getTileEntity(pos);
                if (tileentity.hasCagedEntity() && tileentity.hasTagCompound()) {
                    worldIn.setBlockState(pos, state.with(OPEN, Boolean.FALSE));
                }
            }
        }
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote) {
            if (worldIn.isBlockPowered(pos)) {
                spawnEntity(state, (ServerWorld)worldIn, pos);
            }
        }
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {

        if (playerIn.isSneaking() || worldIn.isRemote || state.get(OPEN)) {
            return ActionResultType.FAIL;
        }
        else {
            spawnEntity(state, (ServerWorld)worldIn, pos);
            return ActionResultType.SUCCESS;
        }
    }

    private void spawnEntity(BlockState state, ServerWorld worldIn, BlockPos pos) {
        BlockEntityCage te = (BlockEntityCage)worldIn.getTileEntity(pos);
        BlockPos check = pos.down();
        spawnParticles(worldIn, pos);
        worldIn.playSound(null, pos, SoundEvents.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.8F);
        worldIn.setBlockState(pos, state.with(OPEN, Boolean.TRUE));
        if (worldIn.isAirBlock(check)) {
            if (te != null) {
                te.spawnCagedCreature(worldIn, pos, true);
            }
        }
        else {
            if (te != null) {
                BlockPos spawnpos = new BlockPos(pos.getX(), pos.getY() + 1F, pos.getZ());
                te.spawnCagedCreature(worldIn, spawnpos, false);
            }
        }
    }

    private void spawnParticles(World worldIn, BlockPos pos) {
        Random random = worldIn.rand;
        double d3 = random.nextGaussian() * 0.02D;
        double d1 = random.nextGaussian() * 0.02D;
        double d2 = random.nextGaussian() * 0.02D;
        ((ServerWorld)worldIn).spawnParticle(ParticleTypes.POOF, (double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, 12, d3, d1, d2, 0.15F);
    }


    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        BlockEntityCage te = (BlockEntityCage)world.getTileEntity(pos);
        if (!world.isRemote && !(entity instanceof PlayerEntity) && entity.isNonBoss() && entity instanceof LivingEntity) {
            if (te != null && !te.hasCagedEntity()) {
                if (te.cageEntity(entity)) {
                    world.playSound(null, pos, SoundEvents.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.8F);
                    world.setBlockState(pos, state.with(OPEN, Boolean.FALSE));
                    spawnParticles(world, pos);
                }
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getTag() != null) {
            CompoundNBT compound_1 = stack.getChildTag("EntityTag");
            if (compound_1 != null) {
                CompoundNBT compound = compound_1.getCompound("EntityTag");
                String entityID = compound.getString("id").replace(":", ".");
                tooltip.add(new TranslationTextComponent("entity." + entityID).mergeStyle(TextFormatting.GRAY));
                if (compound.contains("Gender")) {
                    if (compound.getInt("Gender") == 0) {
                        tooltip.add((new TranslationTextComponent("mobspawn.tooltip.male")).mergeStyle(TextFormatting.GRAY));
                    }
                    else { tooltip.add((new TranslationTextComponent("mobspawn.tooltip.female")).mergeStyle(TextFormatting.GRAY)); }
                }
                else { tooltip.add((new TranslationTextComponent("mobspawn.tooltip.unknown")).mergeStyle(TextFormatting.GRAY)); }

                if (ConfigGamerules.scientificNames.get() && !compound.hasUniqueId("Species")) {
                    TextFormatting[] sciText = new TextFormatting[]{TextFormatting.ITALIC, TextFormatting.GRAY};
                    if (!(new TranslationTextComponent("entity." + entityID + ".sciname").getUnformattedComponentText()).equals("entity." + entityID + ".sciname")) {
                        tooltip.add(new TranslationTextComponent("entity." + entityID + ".sciname").mergeStyle(sciText));
                    }
                }
            }
        }
        else {
            tooltip.add((new TranslationTextComponent("block.trap_cage.state_empty")).mergeStyle(TextFormatting.GRAY));
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) {
        return new BlockEntityCage();
    }

    public boolean canEntitySpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type) {
        return false;
    }

    public static class DispenserBehaviorTrapCage extends DefaultDispenseItemBehavior implements IDispenseItemBehavior {

        public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
            Item item = stack.getItem();
            if (item instanceof BlockItem) {
                Direction direction = source.getBlockState().get(DispenserBlock.FACING);
                BlockPos blockpos = source.getBlockPos().offset(direction);
                Direction direction1 = source.getWorld().isAirBlock(blockpos.down()) ? direction : Direction.UP;
                boolean successful = ((BlockItem)item).tryPlace(new DirectionalPlaceContext(source.getWorld(), blockpos, direction, stack, direction1)) == ActionResultType.SUCCESS;
            }
            return stack;
        }

        protected void playDispenseSound(IBlockSource source) {
            source.getWorld().playEvent(1000, source.getBlockPos(), 0);
        }
    }
}