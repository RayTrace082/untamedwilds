package untamedwilds.item.debug;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import untamedwilds.entity.ComplexMob;

public class ItemGrowthTonic extends Item {

    public ItemGrowthTonic(Properties properties) {
        super(properties);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (target.getEntityWorld().isRemote) return false;
        if (target instanceof PlayerEntity || !target.isNonBoss()) return false;
        if (target instanceof ComplexMob && !target.isChild()) {
            float prevSize = ((ComplexMob) target).getMobSize();
            ((ComplexMob) target).setMobSize(prevSize + 0.1F);
            return true;
        }
        if (target instanceof AgeableEntity && target.isChild()) {
            ((AgeableEntity) target).setGrowingAge(0);
            return true;
        }
        return false;
    }
}