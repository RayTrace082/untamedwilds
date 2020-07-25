package untamedwilds.item.debug;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import untamedwilds.entity.ComplexMobTerrestrial;

public class ItemLovePotion extends Item {

    public ItemLovePotion(Properties properties) {
        super(properties);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (target.getEntityWorld().isRemote) return false;
        if (target instanceof PlayerEntity || !target.isNonBoss()) return false;
        if (target instanceof ComplexMobTerrestrial) {
            ComplexMobTerrestrial entity = (ComplexMobTerrestrial)target;
            entity.setInLove(playerIn);
            entity.setGrowingAge(60);
            return true;
        }
        if (target instanceof AnimalEntity) {
            ((AnimalEntity) target).setInLove(playerIn);
            return true;
        }
        return false;
    }
}