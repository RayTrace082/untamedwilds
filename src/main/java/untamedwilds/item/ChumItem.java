package untamedwilds.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ChumItem extends Item {
    public ChumItem(Properties builder) {
        super(builder);
    }

    public int getUseDuration(ItemStack stack) {
        return 40;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        BlockRayTraceResult blockraytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
        BlockRayTraceResult blockraytraceresult1 = blockraytraceresult.withPosition(blockraytraceresult.getPos().up());
        if (worldIn.getFluidState(blockraytraceresult.getPos()).isTagged(FluidTags.WATER) && !worldIn.isRemote) {
            for (int i = 0; i < 6;  i++) {
                ((ServerWorld)worldIn).spawnParticle(ParticleTypes.SQUID_INK, blockraytraceresult.getPos().getX(), blockraytraceresult.getPos().getY(), blockraytraceresult.getPos().getZ(), 3, 0, 0, 0, 0.05D);
            }
        }
        // TODO: Make Sharks in an AoE aggressive and/or spawn new mobs from the Large Ocean pool
        ActionResultType actionresulttype = super.onItemUse(new ItemUseContext(playerIn, handIn, blockraytraceresult1));
        return new ActionResult<>(actionresulttype, playerIn.getHeldItem(handIn));
    }
}