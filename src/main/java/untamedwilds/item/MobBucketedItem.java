package untamedwilds.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.entity.ComplexMob;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class MobBucketedItem extends BucketItem {
    private final EntityType<? extends ComplexMob> entity;
    private final int species;
    private final String sciname;

    public MobBucketedItem(EntityType<? extends ComplexMob> typeIn, Fluid fluid, Item.Properties builder, int species, String sciname) {
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
            EntityUtils.createMobFromItem((ServerWorld) worldIn, itemStack, entity, this.species, pos, null, false);
        }
    }

    protected void playEmptySound(@Nullable PlayerEntity player, IWorld worldIn, BlockPos pos) {
        worldIn.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY_FISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }
}
