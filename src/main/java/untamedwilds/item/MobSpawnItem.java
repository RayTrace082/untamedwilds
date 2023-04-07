package untamedwilds.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.INeedsPostUpdate;
import untamedwilds.util.EntityUtils;
import untamedwilds.util.ModCreativeModeTab;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class MobSpawnItem extends Item {
    private final Supplier<? extends EntityType<?>> entity;

    public MobSpawnItem(Supplier<? extends EntityType<?>> typeIn, Properties properties) {
        super(properties);
        this.entity = typeIn;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        EntityUtils.buildTooltipData(stack, tooltip, this.entity.get(), EntityUtils.getVariantName(this.entity.get(), this.getSpecies(stack)));
    }

    public Component getName(ItemStack stack) {
        return MutableComponent.create(new TranslatableContents("entity.untamedwilds." + this.entity.get().builtInRegistryHolder().key().location().getPath() + "_" + EntityUtils.getVariantName(this.entity.get(), this.getSpecies(stack))));
        //return new TranslatableComponent("entity.untamedwilds." + this.entity.getRegistryName().getPath() + "_" + ComplexMob.getEntityData(this.entity).getSpeciesData().get(this.getSpecies(stack)).getName()).getString();
    }

    @Override
    public InteractionResult useOn(UseOnContext useContext) {

        Level worldIn = useContext.getLevel();
        if (!(worldIn instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemStack = useContext.getItemInHand();
            BlockPos pos = useContext.getClickedPos();
            Direction facing = useContext.getClickedFace();
            BlockState blockState = worldIn.getBlockState(pos);
            BlockPos spawnPos = blockState.getCollisionShape(worldIn, pos).isEmpty() ? pos : pos.relative(facing);

            EntityType<?> entity = EntityUtils.getEntityTypeFromTag(itemStack.getTag(), this.entity.get());
            boolean doVerticalOffset = !Objects.equals(pos, spawnPos) && facing == Direction.UP;
            EntityUtils.createMobFromItem((ServerLevel) worldIn, itemStack, entity, this.getSpecies(itemStack), spawnPos, useContext.getPlayer(), doVerticalOffset);

            if (useContext.getPlayer() != null) {
                if (!useContext.getPlayer().isCreative()) {
                    itemStack.shrink(1);
                }
            }
        }
        return InteractionResult.CONSUME;
    }

    private int getSpecies(ItemStack itemIn) {
        if (itemIn.getTag() != null && itemIn.getTag().contains("CustomModelData")) {
            return itemIn.getTag().getInt("CustomModelData");
        }
        UntamedWilds.LOGGER.error("No variant found in this itemstack NBT data");
        return 0;
    }

    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (group == ModCreativeModeTab.untamedwilds_items) {
            for(int i = 0; i < EntityUtils.getNumberOfSpecies(this.entity.get()); i++) {
                CompoundTag baseTag = new CompoundTag();
                ItemStack item = new ItemStack(this);
                baseTag.putInt("variant", i);
                baseTag.putInt("CustomModelData", i);
                item.setTag(baseTag);
                items.add(item);
            }
        }
    }
}