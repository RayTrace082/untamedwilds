package untamedwilds.util;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import untamedwilds.init.ModItems;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModCreativeModeTab extends CreativeModeTab {

    public static final ModCreativeModeTab untamedwilds_items = new ModCreativeModeTab(ModCreativeModeTab.TABS.length, "untamedwilds_items");

    private ModCreativeModeTab(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ModItems.LOGO.get());
    }
}
