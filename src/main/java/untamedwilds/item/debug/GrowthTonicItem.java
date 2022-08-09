package untamedwilds.item.debug;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import untamedwilds.entity.ComplexMob;

public class GrowthTonicItem extends Item {

    public GrowthTonicItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        if (target.getLevel().isClientSide) return InteractionResult.PASS;
        if (target instanceof Player/* || !target.isNonBoss()*/) return InteractionResult.FAIL;
        if (target instanceof ComplexMob && !target.isBaby()) {
            float prevSize = ((ComplexMob) target).getMobSize();
            ((ComplexMob) target).setMobSize(prevSize + 0.1F);
            return InteractionResult.SUCCESS;
        }
        if (target instanceof AgeableMob && target.isBaby()) {
            ((AgeableMob) target).setAge(0);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}