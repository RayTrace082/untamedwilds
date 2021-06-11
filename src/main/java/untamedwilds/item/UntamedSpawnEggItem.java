package untamedwilds.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.UntamedWilds;

import javax.annotation.Nullable;
import java.util.List;

public class UntamedSpawnEggItem extends SpawnEggItem {

   private int currentSpecies;
   private final int speciesNumber;

   // TODO: The currently selected species doesn't display on new loads, despite the NBT data existing
   public UntamedSpawnEggItem(EntityType<?> typeIn, int species, int primaryColorIn, int secondaryColorIn, Properties builder) {
      super(typeIn, primaryColorIn, secondaryColorIn, builder);
      this.speciesNumber = species + 1;
      this.currentSpecies = 0;
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
      tooltip.add(new TranslationTextComponent("item.untamedwilds.spawn_egg_info").mergeStyle(TextFormatting.GRAY));
      tooltip.add(new TranslationTextComponent("item.untamedwilds.spawn_egg_current",  this.getCurrentSpeciesNumber()).mergeStyle(TextFormatting.ITALIC).mergeStyle(TextFormatting.GRAY));
   }

   private String getCurrentSpeciesNumber() {
      int i = this.currentSpecies - 1;
      return i >= 0 ? String.valueOf(i) : "Random";
   }

   public void increaseSpeciesNumber(int intIn) {
      UntamedWilds.LOGGER.info(intIn % this.speciesNumber);
      this.currentSpecies = (intIn % this.speciesNumber);
   }

   public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
      ItemStack itemstack = playerIn.getHeldItem(handIn);
      if (!worldIn.isRemote && playerIn.isSneaking()) {
         UntamedWilds.LOGGER.info(this.currentSpecies);
         this.increaseSpeciesNumber(this.currentSpecies + 1);
         playerIn.sendStatusMessage(new TranslationTextComponent("item.untamedwilds.spawn_egg_change",  this.getCurrentSpeciesNumber()), true);

         itemstack.setTag(new CompoundNBT());
         if (this.currentSpecies != 0) {
            CompoundNBT entityNBT = new CompoundNBT();
            entityNBT.putInt("Variant", this.currentSpecies - 1);
            itemstack.getTag().put("EntityTag", entityNBT);
         }
         return ActionResult.resultPass(itemstack);
      }
      return super.onItemRightClick(worldIn, playerIn, handIn);
   }
}
