package untamedwilds.item.debug;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobTerrestrial;
import untamedwilds.entity.ISpecies;
import untamedwilds.util.TimeUtils;

public class AnalyzerItem extends Item {

    public AnalyzerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        Level world = target.getLevel();
        if (world.isClientSide) return InteractionResult.PASS;
        if (target instanceof ComplexMob) {
            ComplexMob entity = (ComplexMob)target;
            String entityName = entity instanceof ISpecies ? ((ISpecies) entity).getSpeciesName() : entity.getName().getString();
            playerIn.sendMessage(new TextComponent("Diagnose: " + (ConfigGamerules.genderedBreeding.get() ? entity.getGenderString() + " " : "") + entityName + " (Skin: " + entity.getSkin() + ") (Eco Level: " + ComplexMob.getEcoLevel(entity) + ") " + entity.getHealth() + "/" + entity.getMaxHealth() + " HP"), playerIn.getUUID());

            if (ConfigGamerules.scientificNames.get()) {
                String useVarName = entity instanceof ISpecies ? "_" + ((ISpecies) entity).getRawSpeciesName(entity.getVariant()) : "";
                playerIn.sendMessage(new TranslatableComponent(entity.getType().getDescriptionId() + useVarName + ".sciname").withStyle(ChatFormatting.ITALIC), playerIn.getUUID());
            }
            if (target instanceof ComplexMobTerrestrial) {
                playerIn.sendMessage(new TextComponent("Hunger: " + ((ComplexMobTerrestrial)entity).getHunger() + "/100 Hunger"), playerIn.getUUID());
            }
            if (!entity.isMale() && entity.getAge() > 0 && !ConfigGamerules.easyBreeding.get()) {
                playerIn.sendMessage(new TextComponent("This female will give birth in " + TimeUtils.convertTicksToDays(world, entity.getAge()) + " (" + entity.getAge() + " ticks)"), playerIn.getUUID());
            }
            if (entity.wantsToBreed()) {
                playerIn.sendMessage(new TextComponent("This mob is looking for a suitable mate"), playerIn.getUUID());
            }
            if (entity.isBaby()) {
                playerIn.sendMessage(new TextComponent("This mob will grow up in " + TimeUtils.convertTicksToDays(world, entity.getAge() * -1) + " (" + entity.getAge() * -1 + " ticks)"), playerIn.getUUID());
            }
            //playerIn.sendMessage(new StringTextComponent("This mob will naturally despawn: " + !entity.preventDespawn()));
            return InteractionResult.SUCCESS;
        }
        else {
            playerIn.sendMessage(new TextComponent("Diagnose: " + target.getName().getString() + " " + target.getHealth() + "/" + target.getMaxHealth() + " HP (Eco Level: " + ComplexMob.getEcoLevel(target) + ")"), playerIn.getUUID());
            if (target.isBaby() && target instanceof AgeableMob) {
                AgeableMob entity = (AgeableMob) target;
                playerIn.sendMessage(new TextComponent("This mob will grow up in " + TimeUtils.convertTicksToDays(world, entity.getAge() * -1) + " (" + entity.getAge() * -1 + " ticks)"), playerIn.getUUID());
            }
            return InteractionResult.SUCCESS;
        }
    }
}