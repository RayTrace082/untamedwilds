package untamedwilds.item.debug;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import untamedwilds.entity.ComplexMob;

public class ItemGrowthTonic extends Item {

    public ItemGrowthTonic(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (target.getEntityWorld().isRemote) return ActionResultType.PASS;
        if (target instanceof PlayerEntity || !target.isNonBoss()) return ActionResultType.FAIL;
        if (target instanceof ComplexMob && !target.isChild()) {
            float prevSize = ((ComplexMob) target).getMobSize();
            ((ComplexMob) target).setMobSize(prevSize + 0.1F);
            return ActionResultType.SUCCESS;
        }
        if (target instanceof AgeableEntity && target.isChild()) {
            ((AgeableEntity) target).setGrowingAge(0);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }
}