package untamedwilds.util;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import untamedwilds.init.ModItems;

public class ItemGroupUT extends ItemGroup {

    public static final ItemGroupUT untamedwilds_items = new ItemGroupUT(ItemGroupUT.GROUPS.length, "untamedwilds_items");

    private ItemGroupUT(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.LOGO.get());
    }
}
