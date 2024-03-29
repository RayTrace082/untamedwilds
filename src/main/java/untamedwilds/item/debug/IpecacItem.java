package untamedwilds.item.debug;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import untamedwilds.entity.ComplexMobTerrestrial;

public class IpecacItem extends Item {

    public IpecacItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        context.getPlayer().sendMessage(new TextComponent("Pos: " + context.getClickedPos()), context.getPlayer().getUUID());
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        if (target.getLevel().isClientSide) return InteractionResult.PASS;
        if (target instanceof Player/* || !target.isNonBoss()*/) return InteractionResult.FAIL;
        if (target instanceof ComplexMobTerrestrial) {
            ComplexMobTerrestrial entity = (ComplexMobTerrestrial)target;
            entity.addHunger(-100);
            entity.huntingCooldown = 0;
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}