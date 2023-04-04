package untamedwilds.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeSpawnEggItem;
import untamedwilds.entity.ComplexMob;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class UntamedSpawnEggItem extends ForgeSpawnEggItem {

    private int currentSpecies;
    private boolean isCached;
    private final Supplier<? extends EntityType<? extends Mob>> entityType;

    public UntamedSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> typeIn, int primaryColorIn, int secondaryColorIn, Properties builder) {
        super(typeIn, primaryColorIn, secondaryColorIn, builder);
        this.currentSpecies = 0;
        this.entityType = typeIn;
        this.isCached = false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(MutableComponent.create(new TranslatableContents("item.untamedwilds.spawn_egg_info")).withStyle(ChatFormatting.GRAY));
        tooltip.add(MutableComponent.create(new TranslatableContents("item.untamedwilds.spawn_egg_current", this.getCurrentSpeciesName(stack))).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
    }

    private String getCurrentSpeciesName(ItemStack stack) {
        if (!this.isCached && stack.hasTag()) {
            if (stack.getTag().contains("EntityTag")) {
                this.currentSpecies = stack.getTag().getCompound("EntityTag").getInt("Variant");
                this.isCached = true;
            }
        }
        int i = this.currentSpecies - 1;
        return i >= 0 ? EntityUtils.getVariantName(this.entityType.get(), i) + " (" + i + ")" : "Random";
    }

    public void increaseSpeciesNumber(int intIn) {
        this.currentSpecies = (intIn % (ComplexMob.getEntityData(this.entityType.get()).getSpeciesData().size() + 1));
        this.isCached = true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (!(worldIn instanceof ServerLevel)) {
            return InteractionResultHolder.success(itemstack);
        }
        if (playerIn.isSteppingCarefully()) {
            this.increaseSpeciesNumber(this.currentSpecies + 1);
            playerIn.displayClientMessage(MutableComponent.create(new TranslatableContents("item.untamedwilds.spawn_egg_change", this.getCurrentSpeciesName(itemstack))), true);

            itemstack.setTag(new CompoundTag());
            if (this.currentSpecies != 0) {
                CompoundTag entityNBT = new CompoundTag();
                entityNBT.putInt("Variant", this.currentSpecies - 1);
                itemstack.getTag().put("EntityTag", entityNBT);
            }
            return InteractionResultHolder.pass(itemstack);
        }

        BlockHitResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.SOURCE_ONLY);
        if (raytraceresult.getType() != BlockHitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            BlockPos blockpos = raytraceresult.getBlockPos();
            if (!(worldIn.getBlockState(blockpos).getBlock() instanceof LiquidBlock)) {
                return InteractionResultHolder.pass(itemstack);
            } else if (worldIn.mayInteract(playerIn, blockpos) && playerIn.mayUseItemAt(blockpos, raytraceresult.getDirection(), itemstack)) {
                Integer species = this.currentSpecies - 1 < 0 ? null : this.currentSpecies - 1;
                EntityUtils.createMobFromItem((ServerLevel) worldIn, itemstack, this.entityType.get(), species, blockpos, playerIn, false, true);

                playerIn.awardStat(Stats.ITEM_USED.get(this));
                worldIn.gameEvent(playerIn, GameEvent.ENTITY_PLACE, blockpos);
                return InteractionResultHolder.consume(itemstack);
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext useContext) {
        Level world = useContext.getLevel();
        if (!(world instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemstack = useContext.getItemInHand();
            BlockPos blockpos = useContext.getClickedPos();
            Direction direction = useContext.getClickedFace();
            BlockState blockstate = world.getBlockState(blockpos);

            if (blockstate.is(Blocks.SPAWNER)) {
                BlockEntity tileentity = world.getBlockEntity(blockpos);
                if (tileentity instanceof SpawnerBlockEntity) {
                    BaseSpawner abstractspawner = ((SpawnerBlockEntity) tileentity).getSpawner();
                    EntityType<?> entitytype1 = this.getType(itemstack.getTag());
                    abstractspawner.setEntityId(entitytype1);

                    CompoundTag entityNBT_2 = new CompoundTag();
                    entityNBT_2.putString("id", entitytype1.builtInRegistryHolder().key().location().toString());
                    if (this.currentSpecies - 1 >= 0) {
                        entityNBT_2.putInt("Variant", this.currentSpecies - 1);
                        entityNBT_2.putFloat("Size", ComplexMob.getEntityData(entitytype1).getScale(this.currentSpecies - 1));
                    } else {
                        entityNBT_2.putFloat("Size", 1);
                    }

                    abstractspawner.setNextSpawnData(world, blockpos, new SpawnData(entityNBT_2, Optional.empty()));

                    tileentity.setChanged();
                    world.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
                    itemstack.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }

            BlockPos blockpos1;
            if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            } else {
                blockpos1 = blockpos.relative(direction);
            }

            if (!itemstack.hasTag())
                itemstack.setTag(new CompoundTag());
            Entity spawn = this.entityType.get().create((ServerLevel) world, itemstack.getTag(), null, useContext.getPlayer(), blockpos, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);
            if (spawn == null) {
                return InteractionResult.PASS;
            }
            //BlockPos spawnPos = blockstate.getCollisionShape(world, blockpos).isEmpty() ? blockpos : blockpos.relative(direction);

            Integer species = this.currentSpecies - 1 < 0 ? null : this.currentSpecies - 1;
            EntityUtils.createMobFromItem((ServerLevel) world, itemstack, this.entityType.get(), species, blockpos1, useContext.getPlayer(), false, true);

            if (useContext.getPlayer() != null) {
                itemstack.shrink(1);
                world.gameEvent(useContext.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);
            }
            return InteractionResult.CONSUME;
        }
    }
}