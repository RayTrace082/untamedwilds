package untamedwilds.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMobBucketed  extends BucketItem {
    private final EntityType<? extends ComplexMob> entity;
    private final int species;
    private final String sciname;

    public ItemMobBucketed(EntityType<? extends ComplexMob> typeIn, Fluid fluid, Item.Properties builder, int species, String sciname) {
        super(fluid, builder);
        this.entity = typeIn;
        this.species = species;
        this.sciname = sciname;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        EntityUtils.buildTooltipData(stack, tooltip, this.entity, this.sciname);
    }

    public void onLiquidPlaced(World worldIn, ItemStack itemStack, BlockPos pos) {
        if (worldIn instanceof ServerWorld) {
            EntityType<?> entity = EntityUtils.getEntityTypeFromTag(itemStack.getTag(), this.entity);
            Entity spawn;
            if (itemStack.hasTag()) {
                if (itemStack.getTag().contains("EntityTag")) {
                    spawn = entity.spawn((ServerWorld) worldIn, itemStack, null, pos, SpawnReason.BUCKET, true, false);
                    if (itemStack.hasDisplayName()) {
                        spawn.setCustomName(itemStack.getDisplayName());
                    }
                    spawn.setLocationAndAngles(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
                    if (((ServerWorld) worldIn).getEntityByUuid(spawn.getUniqueID()) != null) {
                        UntamedWilds.LOGGER.info("Randomizing repeated UUID");
                        spawn.setUniqueId(MathHelper.getRandomUUID(worldIn.rand));
                        ((ServerWorld) worldIn).func_242417_l(spawn);
                    }
                }
            }
            else {
                // If no NBT data is assigned to the entity (eg. Item taken from the Creative menu), create a new, random mob
                spawn = entity.create((ServerWorld) worldIn, itemStack.getTag(), null, null, pos, SpawnReason.BUCKET, true, false);
                if (spawn instanceof ComplexMob) {
                    // Instead of using onInitialSpawn, data is replicated to prevent RandomSpecies from acting, not an ideal solution
                    ComplexMob entitySpawn = (ComplexMob) spawn;
                    entitySpawn.setRandomMobSize();
                    entitySpawn.setGender(worldIn.rand.nextInt(2));
                    entitySpawn.setSpecies(this.species);
                    entitySpawn.setGrowingAge(0);
                }
                if (spawn != null) {
                    spawn.setUniqueId(MathHelper.getRandomUUID(worldIn.rand));
                    if (itemStack.hasDisplayName()) {
                        spawn.setCustomName(itemStack.getDisplayName());
                    }
                    worldIn.addEntity(spawn);
                }
            }
        }
    }

    protected void playEmptySound(@Nullable PlayerEntity player, IWorld worldIn, BlockPos pos) {
        worldIn.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY_FISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }
}
