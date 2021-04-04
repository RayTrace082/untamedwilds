package untamedwilds.item.debug;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import untamedwilds.entity.ComplexMobTerrestrial;

public class IpecacItem extends Item {

    public IpecacItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        context.getPlayer().sendMessage(new StringTextComponent("Pos: " + context.getPos()), context.getPlayer().getUniqueID());
        return ActionResultType.PASS;
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (target.getEntityWorld().isRemote) return ActionResultType.PASS;
        if (target instanceof PlayerEntity || !target.isNonBoss()) return ActionResultType.FAIL;
        if (target instanceof ComplexMobTerrestrial) {
            ComplexMobTerrestrial entity = (ComplexMobTerrestrial)target;
            entity.addHunger(-100);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }
}