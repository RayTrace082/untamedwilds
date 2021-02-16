package untamedwilds.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemOwnershipDeed extends Item {
    
    public ItemOwnershipDeed(Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTag()) {
            CompoundNBT nbt = stack.getTag();
            tooltip.add(new TranslationTextComponent("item.untamedwilds.ownership_deed_desc_4", nbt.getString("entityname")).mergeStyle(TextFormatting.GRAY));
            tooltip.add(new TranslationTextComponent("item.untamedwilds.ownership_deed_desc_5").mergeStyle(TextFormatting.GRAY));
            tooltip.add(new TranslationTextComponent("item.untamedwilds.ownership_deed_desc_6",  nbt.getString("ownername")).mergeStyle(TextFormatting.ITALIC).mergeStyle(TextFormatting.GRAY));
        }
        else {
            tooltip.add(new TranslationTextComponent("item.untamedwilds.ownership_deed_desc_1").mergeStyle(TextFormatting.GRAY));
        }
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (itemstack.hasTag() ) {
            CompoundNBT nbt = itemstack.getTag();
            if (!nbt.getString("entityid").isEmpty()) {
                List<LivingEntity> list = worldIn.getEntitiesWithinAABB(LivingEntity.class, playerIn.getBoundingBox().grow(8.0D));
                for(LivingEntity entity : list) {
                    if (entity.getUniqueID().equals(UUID.fromString(nbt.getString("entityid")))) {
                        entity.addPotionEffect(new EffectInstance(Effects.GLOWING, 80, 0, false, false));
                    }
                }
            }
        }
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }


    /*@Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        if (target instanceof TameableEntity) {
            TameableEntity entity_target = (TameableEntity) target;

            if (entity_target.isTamed()) {
                if (entity_target.getOwnerId().equals(playerIn.getUniqueID()) && !itemstack.hasTag()) {
                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("ownername", playerIn.getName().getString());
                    nbt.putString("entityname", entity_target.getName().getString());
                    nbt.putString("ownerid", playerIn.getUniqueID().toString());
                    nbt.putString("entityid", entity_target.getUniqueID().toString());
                    itemstack.setTag(nbt);
                    if (UntamedWilds.DEBUG) {
                        UntamedWilds.LOGGER.log(Level.INFO, "Pet owner signed a deed for a " + entity_target.getName().getString());
                    }
                    return ActionResultType.SUCCESS;
                }

                else if (itemstack.hasTag()) {
                    if (entity_target.getOwnerId().toString().equals(itemstack.getTag().getString("ownerid")) && entity_target.getUniqueID().toString().equals(itemstack.getTag().getString("entityid"))) {
                        entity_target.setOwnerId(playerIn.getUniqueID());
                        if (!playerIn.isCreative()) {
                            itemstack.shrink(1);
                        }
                        // playerIn.addStat(Stats.getObjectUseStats(this));
                        if (UntamedWilds.DEBUG) {
                            UntamedWilds.LOGGER.log(Level.INFO, "Pet ownership transferred to " + playerIn.getName().getString());
                        }
                        return ActionResultType.CONSUME;
                    }
                }
                return ActionResultType.FAIL;
            }
        }
        return ActionResultType.FAIL;
    }*/
}