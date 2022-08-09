package untamedwilds.item.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;

public class HighlighterItem extends Item {

    public HighlighterItem(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (playerIn.isSteppingCarefully() && worldIn.isClientSide) {
            Minecraft.getInstance().getEntityRenderDispatcher().setRenderHitBoxes(!Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes());
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        if (target.getLevel().isClientSide) return InteractionResult.PASS;
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 999999, 0, true, false));
        return InteractionResult.SUCCESS;
    }
}