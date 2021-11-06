package untamedwilds.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.INeedsPostUpdate;
import untamedwilds.util.EntityUtils;
import untamedwilds.util.ItemGroupUT;

import javax.annotation.Nullable;
import java.util.List;

public class MobBucketedItem extends BucketItem {
    private final EntityType<? extends ComplexMob> entity;

    public MobBucketedItem(EntityType<? extends ComplexMob> typeIn, Fluid fluid, Item.Properties builder) {
        super(() -> fluid, builder);
        this.entity = typeIn;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (ComplexMob.ENTITY_DATA_HASH.containsKey(this.entity)) {
            EntityUtils.buildTooltipData(stack, tooltip, this.entity, ComplexMob.getEntityData(this.entity).getSpeciesData().get(this.getSpecies(stack)).getName());
        }
    }

    public String getTranslationKey(ItemStack stack) {
        if (ComplexMob.ENTITY_DATA_HASH.containsKey(this.entity)) {
            return new TranslationTextComponent("item.untamedwilds.bucket_" + this.entity.getRegistryName().getPath()).getString() + "_" + ComplexMob.getEntityData(this.entity).getSpeciesData().get(this.getSpecies(stack)).getName();
        }
        return super.getTranslationKey(stack);
    }

    public void onLiquidPlaced(World worldIn, ItemStack itemStack, BlockPos pos) {
        if (worldIn instanceof ServerWorld) {
            EntityType<?> entity = EntityUtils.getEntityTypeFromTag(itemStack.getTag(), this.entity);
            if (itemStack.getTag() != null && itemStack.getTag().contains("EntityTag")) {
                EntityUtils.createMobFromItem((ServerWorld) worldIn, itemStack, entity, 0, pos, null, false);
            }
            else {
                Entity spawn = entity.create((ServerWorld) worldIn, itemStack.getTag(), null, null, pos, SpawnReason.BUCKET, true, false);
                if (spawn instanceof ComplexMob) {
                    ComplexMob entitySpawn = (ComplexMob) spawn;
                    entitySpawn.setVariant(this.getSpecies(itemStack));
                    entitySpawn.chooseSkinForSpecies(entitySpawn, true);
                    entitySpawn.setGrowingAge(entitySpawn.getAdulthoodTime() * -1);
                    if (spawn instanceof INeedsPostUpdate) {
                        ((INeedsPostUpdate) spawn).updateAttributes();
                    }
                    worldIn.addEntity(spawn);
                }
            }
        }
    }

    protected void playEmptySound(@Nullable PlayerEntity player, IWorld worldIn, BlockPos pos) {
        worldIn.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY_FISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }


    private int getSpecies(ItemStack itemIn) {
        if (itemIn.getTag() != null && itemIn.getTag().contains("CustomModelData")) {
            return itemIn.getTag().getInt("CustomModelData");
        }
        UntamedWilds.LOGGER.error("No variant found in this itemstack NBT data");
        return 0;
    }

    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (group == ItemGroupUT.untamedwilds_items) {
            int j = ComplexMob.getEntityData(entity).getSpeciesData().size();
            for(int i = 0; i < j; i++) {
                CompoundNBT baseTag = new CompoundNBT();
                ItemStack item = new ItemStack(this);
                baseTag.putInt("variant", i);
                baseTag.putInt("custom_model_data", i);
                item.setTag(baseTag);
                items.add(item);
            }
        }
    }
}
