package untamedwilds.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.util.EntityUtils;
import untamedwilds.util.ModCreativeModeTab;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class MobBucketedItem extends BucketItem {
    private final Supplier<? extends EntityType<?>> entity;

    public MobBucketedItem(Supplier<? extends EntityType<?>> typeIn, Fluid fluid, Item.Properties builder) {
        super(() -> fluid, builder);
        this.entity = typeIn;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (ComplexMob.ENTITY_DATA_HASH.containsKey(this.entity.get())) {
            EntityUtils.buildTooltipData(stack, tooltip, this.entity.get(), EntityUtils.getVariantName(this.entity.get(), this.getSpecies(stack)));
        }
    }

    public Component getName(ItemStack stack) {
        if (ComplexMob.ENTITY_DATA_HASH.containsKey(this.entity.get())) {
            return MutableComponent.create(new TranslatableContents("item.untamedwilds.bucket_" + this.entity.get().builtInRegistryHolder().key().location().getPath() + "_" + EntityUtils.getVariantName(this.entity.get(), this.getSpecies(stack))));
        }
        return super.getName(stack);
    }

    public void checkExtraContent(@Nullable Player playerIn, Level worldIn, ItemStack itemStackIn, BlockPos posIn) {
        if (worldIn instanceof ServerLevel) {
            this.spawn(worldIn, itemStackIn, posIn);
            worldIn.gameEvent(playerIn, GameEvent.ENTITY_PLACE, posIn);
        }
    }

    public void spawn(Level worldIn, ItemStack itemStack, BlockPos pos) {
        if (worldIn instanceof ServerLevel) {
            EntityType<?> entity = EntityUtils.getEntityTypeFromTag(itemStack.getTag(), this.entity.get());
            EntityUtils.createMobFromItem((ServerLevel) worldIn, itemStack, entity, this.getSpecies(itemStack), pos, null, false);
        }
    }

    protected void playEmptySound(@Nullable Player player, LevelAccessor worldIn, BlockPos pos) {
        worldIn.playSound(player, pos, SoundEvents.BUCKET_EMPTY_FISH, SoundSource.NEUTRAL, 1.0F, 1.0F);
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