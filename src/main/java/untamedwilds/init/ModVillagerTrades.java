package untamedwilds.init;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import untamedwilds.UntamedWilds;

import java.util.Random;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID)
public class ModVillagerTrades {

    @SubscribeEvent
    public static void onVillagerTradesEvent(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.FISHERMAN) {
            event.getTrades().get(3).add(new setupOffer(Items.EMERALD, 1, ModItems.MATERIAL_PEARL.get(), 1, 6, 8, 0.2F));
            event.getTrades().get(3).add(new setupOffer(ModItems.MATERIAL_PEARL.get(), 2, Items.EMERALD, 1, 6, 8, 0.2F));
            event.getTrades().get(4).add(new setupOffer(ModItems.RARE_GIANT_PEARL.get(), 1, Items.EMERALD, 6, 3, 5, 0.2F));
        }
        if (event.getType() == VillagerProfession.BUTCHER) {
            event.getTrades().get(3).add(new setupOffer(ModItems.MATERIAL_BLUBBER.get(), 6, Items.EMERALD, 1, 12, 5, 0.05F));
            event.getTrades().get(3).add(new setupOffer(Items.EMERALD, 1, ModItems.MATERIAL_FAT.get(), 4, 12, 5, 0.05F));
        }
    }

    public static class setupOffer implements VillagerTrades.ItemListing {
        private final Item itemstackIn;
        private final int stackSizeIn;
        private final Item itemstackOut;
        private final int stackSizeOut;
        private final int maxUses;
        private final int givenExp;
        private final float priceMultiplier;

        setupOffer(Item stackOut, int sizeOut, Item stackIn, int sizeIn, int maxUses, int givenExp, float priceMultiplier) {
            this.itemstackIn = stackIn;
            this.stackSizeIn = sizeIn;
            this.itemstackOut = stackOut;
            this.stackSizeOut = sizeOut;
            this.maxUses = maxUses;
            this.givenExp = givenExp;
            this.priceMultiplier = priceMultiplier;
        }

        public MerchantOffer getOffer(Entity entityIn, Random rand) {
            return new MerchantOffer(new ItemStack(this.itemstackOut, this.stackSizeOut), new ItemStack(this.itemstackIn, this.stackSizeIn), this.maxUses, this.givenExp, this.priceMultiplier);
        }
    }
}
