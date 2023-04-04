package untamedwilds.item.debug;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
            playerIn.sendSystemMessage(MutableComponent.create(new LiteralContents("Diagnose: " + (ConfigGamerules.genderedBreeding.get() ? entity.getGenderString() + " " : "") + entityName + " (Skin: " + entity.getSkin() + ") (Eco Level: " + ComplexMob.getEcoLevel(entity) + ") " + entity.getHealth() + "/" + entity.getMaxHealth() + " HP")));

            if (ConfigGamerules.scientificNames.get()) {
                String useVarName = entity instanceof ISpecies ? "_" + ((ISpecies) entity).getRawSpeciesName(entity.getVariant()) : "";
                playerIn.sendSystemMessage(MutableComponent.create(new LiteralContents(entity.getType().getDescriptionId() + useVarName + ".sciname")).withStyle(ChatFormatting.ITALIC));
            }
            if (target instanceof ComplexMobTerrestrial) {
                playerIn.sendSystemMessage(MutableComponent.create(new LiteralContents("Hunger: " + ((ComplexMobTerrestrial)entity).getHunger() + "/100 Hunger")));
            }
            if (!entity.isMale() && entity.getAge() > 0 && !ConfigGamerules.easyBreeding.get()) {
                playerIn.sendSystemMessage(MutableComponent.create(new LiteralContents("This female will give birth in " + TimeUtils.convertTicksToDays(world, entity.getAge()) + " (" + entity.getAge() + " ticks)")));
            }
            if (entity.wantsToBreed()) {
                playerIn.sendSystemMessage(MutableComponent.create(new LiteralContents("This mob is looking for a suitable mate")));
            }
            if (entity.isBaby()) {
                playerIn.sendSystemMessage(MutableComponent.create(new LiteralContents("This mob will grow up in " + TimeUtils.convertTicksToDays(world, entity.getAge() * -1) + " (" + entity.getAge() * -1 + " ticks)")));
            }
            //playerIn.sendMessage(new StringTextComponent("This mob will naturally despawn: " + !entity.preventDespawn()));
            return InteractionResult.SUCCESS;
        }
        else {
            playerIn.sendSystemMessage(MutableComponent.create(new LiteralContents("Diagnose: " + target.getName().getString() + " " + target.getHealth() + "/" + target.getMaxHealth() + " HP (Eco Level: " + ComplexMob.getEcoLevel(target) + ")")));
            if (target.isBaby() && target instanceof AgeableMob) {
                AgeableMob entity = (AgeableMob) target;
                playerIn.sendSystemMessage(MutableComponent.create(new LiteralContents("This mob will grow up in " + TimeUtils.convertTicksToDays(world, entity.getAge() * -1) + " (" + entity.getAge() * -1 + " ticks)")));
            }
            return InteractionResult.SUCCESS;
        }
    }
}