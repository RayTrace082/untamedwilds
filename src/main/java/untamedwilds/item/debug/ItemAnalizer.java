package untamedwilds.item.debug;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobTerrestrial;
import untamedwilds.entity.ISpecies;
import untamedwilds.util.TimeUtils;

public class ItemAnalizer extends Item {

    public ItemAnalizer(Properties properties) {
        super(properties);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        World world = target.getEntityWorld();
        if (world.isRemote) return false;
        if (target instanceof ComplexMob) {
            ComplexMob entity = (ComplexMob)target;
            if (entity instanceof ISpecies) {
                playerIn.sendMessage(new StringTextComponent("Diagnose: " + entity.getGenderString() + " " + ((ISpecies) entity).getSpeciesName() + " " + entity.getHealth() + "/" + entity.getMaxHealth() + " HP"));
            }
            else {
                playerIn.sendMessage(new StringTextComponent("Diagnose: " + entity.getGenderString() + " " + entity.getName().getString() + " " + entity.getHealth() + "/" + entity.getMaxHealth() + " HP"));
            }

            if (ConfigGamerules.scientificNames.get()) {
                if (entity instanceof ISpecies) {
                    playerIn.sendMessage(new TranslationTextComponent(entity.getType().getTranslationKey() + "_" + ((ISpecies) entity).getRawSpeciesName() + ".sciname").applyTextStyle(TextFormatting.ITALIC));
                }
                else {
                    playerIn.sendMessage(new TranslationTextComponent(entity.getType().getTranslationKey() + ".sciname").applyTextStyle(TextFormatting.ITALIC));
                }
            }
            if (target instanceof ComplexMobTerrestrial) {
                playerIn.sendMessage(new StringTextComponent("Hunger: " + ((ComplexMobTerrestrial)entity).getHunger() + "/100 Hunger"));
                if (!entity.isMale() && entity.getGrowingAge() > 0) {
                    playerIn.sendMessage(new StringTextComponent("This female will give birth in " + TimeUtils.convertTicksToDays(world, entity.getGrowingAge()) + " (" + entity.getGrowingAge() + " ticks)"));
                }
            }
            if (entity.wantsToBreed()) {
                playerIn.sendMessage(new StringTextComponent("This mob is looking for a suitable mate"));
            }
            if (entity.isChild()) {
                playerIn.sendMessage(new StringTextComponent("This mob will grow up in " + TimeUtils.convertTicksToDays(world, entity.getGrowingAge() * -1) + " (" + entity.getGrowingAge() * -1 + " ticks)"));
            }
            //playerIn.sendMessage(new StringTextComponent("This mob will naturally despawn: " + !entity.preventDespawn()));
            return true;
        }
        else {
            playerIn.sendMessage(new StringTextComponent("Diagnose: " + target.getName().getString() + " " + target.getHealth() + "/" + target.getMaxHealth() + " HP"));
            if (target.isChild() && target instanceof AgeableEntity) {
                AgeableEntity entity = (AgeableEntity) target;
                playerIn.sendMessage(new StringTextComponent("This mob will grow up in " + TimeUtils.convertTicksToDays(world, entity.getGrowingAge() * -1) + " (" + entity.getGrowingAge() * -1 + " ticks)"));
            }
            return true;
        }
    }
}