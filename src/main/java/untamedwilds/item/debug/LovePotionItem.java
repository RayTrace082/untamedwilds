package untamedwilds.item.debug;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import untamedwilds.entity.ComplexMob;

public class LovePotionItem extends Item {

    public LovePotionItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        if (target.getLevel().isClientSide) return InteractionResult.PASS;
        if (target instanceof Player/* || !target.isNonBoss()*/) return InteractionResult.FAIL;
        if (target instanceof ComplexMob) {
            if (((ComplexMob) target).getAge() > 0) {
                ((ComplexMob) target).setAge(1);
            }
            else {
                ComplexMob entity = (ComplexMob)target;
                entity.setInLove(playerIn);
                //entity.breed();
                //entity.setAge(60);
                return InteractionResult.SUCCESS;
            }
        }
        if (target instanceof Animal) {
            ((Animal) target).setInLove(playerIn);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}