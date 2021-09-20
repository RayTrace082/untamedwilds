package untamedwilds.item.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class HighlighterItem extends Item {

    public HighlighterItem(Properties properties) {
        super(properties);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (playerIn.isSneaking() && worldIn.isRemote) {
            Minecraft.getInstance().getRenderManager().setDebugBoundingBox(!Minecraft.getInstance().getRenderManager().isDebugBoundingBox());
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (target.getEntityWorld().isRemote) return ActionResultType.PASS;
        target.addPotionEffect(new EffectInstance(Effects.GLOWING, 999999, 0, true, false));
        return ActionResultType.SUCCESS;
    }
}