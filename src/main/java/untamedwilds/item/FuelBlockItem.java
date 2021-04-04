package untamedwilds.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;

public class FuelBlockItem extends BlockItem implements IForgeItem {

    private final int burnTime;

    public FuelBlockItem(Block blockIn, int burnTime, Properties builder) {
        super(blockIn, builder);
        this.burnTime = burnTime;
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        return this.burnTime;
    }
}