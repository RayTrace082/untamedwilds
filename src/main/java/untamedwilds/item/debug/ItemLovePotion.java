package untamedwilds.item.debug;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import untamedwilds.entity.ComplexMob;

public class ItemLovePotion extends Item {

    public ItemLovePotion(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (target.getEntityWorld().isRemote) return ActionResultType.PASS;
        if (target instanceof PlayerEntity || !target.isNonBoss()) return ActionResultType.FAIL;
        if (target instanceof ComplexMob) {
            ComplexMob entity = (ComplexMob)target;
            entity.setInLove(playerIn);
            entity.breed();
            //entity.setGrowingAge(60);
            return ActionResultType.SUCCESS;
        }
        if (target instanceof AnimalEntity) {
            ((AnimalEntity) target).setInLove(playerIn);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }
}