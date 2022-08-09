package untamedwilds.item.debug;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;

public class EraserItem extends Item {

    public EraserItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        if (target.getLevel().isClientSide) return InteractionResult.PASS;
        target.remove(Entity.RemovalReason.DISCARDED);
        return InteractionResult.SUCCESS;
    }
}