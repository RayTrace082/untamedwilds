package untamedwilds.util;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
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
        Player playerIn = event.getEntity();
        Entity target = event.getTarget();
        InteractionHand hand = event.getHand();
        if (/*!event.getWorld().isClientSide && hand == InteractionHand.MAIN_HAND &&*/ playerIn.getItemInHand(InteractionHand.MAIN_HAND).getItem() == ModItems.OWNERSHIP_DEED.get()) {
            ItemStack itemstack = playerIn.getItemInHand(hand);
            if (target instanceof TamableAnimal) {
                TamableAnimal entity_target = (TamableAnimal) target;
                if (entity_target.isTame()) {
                    if (Objects.equals(entity_target.getOwnerUUID(), playerIn.getUUID()) && !itemstack.hasTag()) {
                        CompoundTag nbt = new CompoundTag();
                        nbt.putString("ownername", playerIn.getName().getString());
                        nbt.putString("entityname", entity_target.getName().getString());
                        nbt.putString("ownerid", playerIn.getUUID().toString());
                        nbt.putString("entityid", entity_target.getUUID().toString());
                        itemstack.setTag(nbt);
                        if (UntamedWilds.DEBUG) {
                            UntamedWilds.LOGGER.log(Level.INFO, "Pet owner signed a deed for a " + entity_target.getName().getString());
                        }
                        event.setCanceled(true);
                        event.setCancellationResult(InteractionResult.SUCCESS);
                    }

                    else {
                        if (itemstack.getTag() != null) {
                            if (entity_target.getOwnerUUID().toString().equals(itemstack.getTag().getString("ownerid")) && entity_target.getUUID().toString().equals(itemstack.getTag().getString("entityid"))) {
                                entity_target.setOwnerUUID(playerIn.getUUID());
                                if (!playerIn.isCreative()) {
                                    itemstack.shrink(1);
                                }
                                if (UntamedWilds.DEBUG) {
                                    UntamedWilds.LOGGER.log(Level.INFO, "Pet ownership transferred to " + playerIn.getName().getString());
                                }
                            }
                            event.setCanceled(true);
                            event.setCancellationResult(InteractionResult.SUCCESS);
                        }
                    }
                }
            }
            event.setCancellationResult(InteractionResult.PASS);
        }
    }
}