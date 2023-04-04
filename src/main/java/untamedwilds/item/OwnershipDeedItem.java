package untamedwilds.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class OwnershipDeedItem extends Item {
    
    public OwnershipDeedItem(Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.hasTag()) {
            CompoundTag nbt = stack.getTag();
            tooltip.add(MutableComponent.create(new TranslatableContents("item.untamedwilds.ownership_deed_desc_4", nbt.getString("entityname"))).withStyle(ChatFormatting.GRAY));
            tooltip.add(MutableComponent.create(new TranslatableContents("item.untamedwilds.ownership_deed_desc_5")).withStyle(ChatFormatting.GRAY));
            tooltip.add(MutableComponent.create(new TranslatableContents("item.untamedwilds.ownership_deed_desc_6",  nbt.getString("ownername"))).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        }
        else {
            tooltip.add(MutableComponent.create(new TranslatableContents("item.untamedwilds.ownership_deed_desc_1")).withStyle(ChatFormatting.GRAY));
        }
    }

    public boolean isFoil(ItemStack stack) {
        return stack.hasTag();
    }

    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);

        if (itemstack.hasTag()) {
            CompoundTag nbt = itemstack.getTag();
            if (!nbt.getString("entityid").isEmpty()) {
                List<LivingEntity> list = worldIn.getEntitiesOfClass(LivingEntity.class, playerIn.getBoundingBox().inflate(8.0D));
                for(LivingEntity entity : list) {
                    if (entity.getUUID().equals(UUID.fromString(nbt.getString("entityid")))) {
                        entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 80, 0, false, false));
                    }
                }
            }
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
    }


    /*@Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, Hand hand) {
        UntamedWilds.LOGGER.log(Level.INFO, "Trying to get entity");

        ItemStack itemstack = playerIn.getItemInHand(hand);
        if (target instanceof TameableEntity) {
            TameableEntity entity_target = (TameableEntity) target;

            if (entity_target.isTame()) {
                if (entity_target.getOwnerId().equals(playerIn.getUUID()) && !itemstack.hasTag()) {
                    CompoundTag nbt = new CompoundTag();
                    nbt.putString("ownername", playerIn.getName().getString());
                    nbt.putString("entityname", entity_target.getName().getString());
                    nbt.putString("ownerid", playerIn.getUUID().toString());
                    nbt.putString("entityid", entity_target.getUUID().toString());
                    itemstack.setTag(nbt);
                    if (UntamedWilds.DEBUG) {
                        UntamedWilds.LOGGER.log(Level.INFO, "Pet owner signed a deed for a " + entity_target.getName().getString());
                    }
                    return InteractionResult.SUCCESS;
                }

                else if (itemstack.hasTag()) {
                    if (entity_target.getOwnerId().toString().equals(itemstack.getTag().getString("ownerid")) && entity_target.getUUID().toString().equals(itemstack.getTag().getString("entityid"))) {
                        entity_target.setOwnerId(playerIn.getUUID());
                        if (!playerIn.isCreative()) {
                            itemstack.shrink(1);
                        }
                        // playerIn.addStat(Stats.getObjectUseStats(this));
                        if (UntamedWilds.DEBUG) {
                            UntamedWilds.LOGGER.log(Level.INFO, "Pet ownership transferred to " + playerIn.getName().getString());
                        }
                        return InteractionResult.CONSUME;
                    }
                }
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.FAIL;
    }*/
}