package untamedwilds.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMobAmphibious;
import untamedwilds.entity.ComplexMobAquatic;
import untamedwilds.init.ModAdvancementTriggers;
import untamedwilds.init.ModParticles;

import java.util.ArrayList;
import java.util.List;

public class ChumItem extends Item {
    public ChumItem(Properties builder) {
        super(builder);
    }

    public int getUseDuration(ItemStack stack) { return 40; }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        BlockRayTraceResult blockraytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
        BlockRayTraceResult blockraytraceresult1 = blockraytraceresult.withPosition(blockraytraceresult.getPos().up());
        if (worldIn.getFluidState(blockraytraceresult.getPos()).isTagged(FluidTags.WATER)) {
            ItemStack itemstack = playerIn.getHeldItem(handIn);
            playerIn.getCooldownTracker().setCooldown(this, 30);
            worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, SoundCategory.NEUTRAL, 1F, 1F);
            if (!worldIn.isRemote) {
                Vector3d pos = blockraytraceresult.getHitVec();
                for (int i = 0; i < 6;  i++) {
                    double d2 = random.nextGaussian() * 0.03D;
                    double d3 = random.nextGaussian() * 0.03D;
                    double d4 = random.nextGaussian() * 0.03D;
                    ((ServerWorld)worldIn).spawnParticle(ModParticles.CHUM_DISPERSE, pos.getX(), pos.getY() - 0.1F, pos.getZ(), 1, d2, d3, d4, 0.02D);
                }
                ModAdvancementTriggers.BAIT_BASIC.trigger((ServerPlayerEntity) playerIn);
                playerIn.addStat(Stats.ITEM_USED.get(this));
                if (!playerIn.abilities.isCreativeMode) {
                    itemstack.shrink(1);
                }

                // Currently, using Chum will lure all nearby water mobs into the used location through a single tryMoveToXYZ
                float dist = 80;
                List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, (new AxisAlignedBB(playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), playerIn.getPosX() + 1.0D, playerIn.getPosY() + 1.0D, playerIn.getPosZ() + 1.0D)).grow(dist));
                List<MobEntity> waterMobs = new ArrayList<>();
                for (Entity entity : list) {
                    if ((entity instanceof WaterMobEntity || entity instanceof ComplexMobAquatic || entity instanceof ComplexMobAmphibious) && entity.isInWater())
                        waterMobs.add((MobEntity) entity);
                }
                if (waterMobs.size() >= 100) {
                    ModAdvancementTriggers.MASTER_BAIT.trigger((ServerPlayerEntity) playerIn);
                }
                for (MobEntity waterMob : waterMobs) {
                    if (UntamedWilds.DEBUG)
                        waterMob.addPotionEffect(new EffectInstance(Effects.GLOWING, 60, 0, true, false));
                    waterMob.getNavigator().clearPath();
                    waterMob.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1);
                }
            }
        }
        // TODO: Maybe make Sharks in an AoE aggressive and/or spawn new mobs from the Large Ocean pool?
        ActionResultType actionresulttype = super.onItemUse(new ItemUseContext(playerIn, handIn, blockraytraceresult1));
        return new ActionResult<>(actionresulttype, playerIn.getHeldItem(handIn));
    }
}