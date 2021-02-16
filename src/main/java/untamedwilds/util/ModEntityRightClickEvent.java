package untamedwilds.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;
import untamedwilds.UntamedWilds;
import untamedwilds.init.ModItems;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID)
public class ModEntityRightClickEvent {

    @SubscribeEvent
    public static void modEntityRightClickEvent(PlayerInteractEvent.EntityInteract event) {
        PlayerEntity playerIn = event.getPlayer();
        Entity target = event.getTarget();
        Hand hand = event.getHand();
        if (hand.equals(Hand.MAIN_HAND) && event.getItemStack().getItem() == ModItems.OWNERSHIP_DEED.get()) {
            ItemStack itemstack = playerIn.getHeldItem(hand);
            if (target instanceof TameableEntity) {
                TameableEntity entity_target = (TameableEntity) target;

                if (entity_target.isTamed()) {
                    if (Objects.equals(entity_target.getOwnerId(), playerIn.getUniqueID()) && !itemstack.hasTag()) {
                        CompoundNBT nbt = new CompoundNBT();
                        nbt.putString("ownername", playerIn.getName().getString());
                        nbt.putString("entityname", entity_target.getName().getString());
                        nbt.putString("ownerid", playerIn.getUniqueID().toString());
                        nbt.putString("entityid", entity_target.getUniqueID().toString());
                        itemstack.setTag(nbt);
                        if (UntamedWilds.DEBUG) {
                            UntamedWilds.LOGGER.log(Level.INFO, "Pet owner signed a deed for a " + entity_target.getName().getString());
                        }
                        event.setCanceled(true);
                    }

                    else if (itemstack.getTag() != null) {
                        if (entity_target.getOwnerId().toString().equals(itemstack.getTag().getString("ownerid")) && entity_target.getUniqueID().toString().equals(itemstack.getTag().getString("entityid"))) {
                            entity_target.setOwnerId(playerIn.getUniqueID());
                            if (!playerIn.isCreative()) {
                                itemstack.shrink(1);
                            }
                            if (UntamedWilds.DEBUG) {
                                UntamedWilds.LOGGER.log(Level.INFO, "Pet ownership transferred to " + playerIn.getName().getString());
                            }
                        }
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}