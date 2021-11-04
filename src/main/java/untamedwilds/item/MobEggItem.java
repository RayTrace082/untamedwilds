package untamedwilds.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.INeedsPostUpdate;
import untamedwilds.util.EntityUtils;
import untamedwilds.util.ItemGroupUT;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class MobEggItem extends Item {
    private final EntityType<? extends ComplexMob> entity;
    public int species;

    // TODO: Patchwork to allow multiple loading types in fillItemGroup
    public MobEggItem(EntityType<? extends ComplexMob> typeIn, Properties properties) {
        super(properties);
        this.entity = typeIn;
        this.species = -1;
    }

    public MobEggItem(EntityType<? extends ComplexMob> typeIn, int species, Properties properties) {
        super(properties);
        this.entity = typeIn;
        this.species = species;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("mobspawn.tooltip.unknown").mergeStyle(TextFormatting.GRAY));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext useContext) {
        World worldIn = useContext.getWorld();
        if (!(worldIn instanceof ServerWorld)) {
            return ActionResultType.SUCCESS;
        } else {
            ItemStack itemStack = useContext.getItem();
            BlockPos pos = useContext.getPos();
            Direction facing = useContext.getFace();
            BlockState blockState = worldIn.getBlockState(pos);

            BlockPos spawnPos = blockState.getCollisionShape(worldIn, pos).isEmpty() ? pos : pos.offset(facing);

            EntityType<?> entity = EntityUtils.getEntityTypeFromTag(itemStack.getTag(), this.entity);
            Entity spawn = entity.create((ServerWorld) worldIn, itemStack.getTag(), null, useContext.getPlayer(), spawnPos, SpawnReason.BUCKET, true, !Objects.equals(pos, spawnPos) && facing == Direction.UP);
            if (spawn instanceof ComplexMob) {
                ComplexMob entitySpawn = (ComplexMob) spawn;
                entitySpawn.setVariant(this.getSpecies(itemStack, entitySpawn));
                entitySpawn.chooseSkinForSpecies(entitySpawn, true);
                entitySpawn.setRandomMobSize();
                UntamedWilds.LOGGER.info(entitySpawn.getMobSize());
                entitySpawn.setGender(entitySpawn.getRNG().nextInt(2));
                entitySpawn.setGrowingAge(entitySpawn.getAdulthoodTime() * -1);

                if (spawn instanceof INeedsPostUpdate) {
                    ((INeedsPostUpdate) spawn).updateAttributes();
                }
            }
            if (spawn != null) {
                worldIn.addEntity(spawn);
            }
            itemStack.shrink(1);
        }
        return ActionResultType.CONSUME;
    }

    public String getTranslationKey() {
        return new TranslationTextComponent("item.untamedwilds.egg_" + this.entity.getRegistryName().getPath()).getString();
    }

    private int getSpecies(ItemStack itemIn, ComplexMob entityIn) {
        if (itemIn.getTag() != null && itemIn.getTag().contains("variant")) {
            return itemIn.getTag().getInt("variant");
        }
        return entityIn.getVariant();
    }

    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (group == ItemGroupUT.untamedwilds_items) {
            UntamedWilds.LOGGER.info(entity.getName());
            int j = ComplexMob.getEntityData(entity).getSpeciesData().size();
            for(int i = 0; i < j; i++) {
                CompoundNBT baseTag = new CompoundNBT();
                ItemStack item = new ItemStack(this);
                baseTag.putInt("variant", i);
                baseTag.putInt("custom_model_data", i);
                item.setTag(baseTag);
                items.add(item);
            }
        }
    }

    // TODO: Have dropped eggs eventually hatch into baby mobs
    /*@Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {

        ItemStack itemstack = entityItem.getItem();
        World worldIn = entityItem.world;

        if (entityItem.world.isRemote || entityItem.ticksExisted < this.getHatchingTime(itemstack)) {
            return super.onEntityItemUpdate(entityItem);
        }

        Entity entity = EntityList.createEntityByIDFromName(this.entity, worldIn);;

        if (entity instanceof ComplexMob) {
            ComplexMob entitySpawn = (ComplexMob) entity;
            entitySpawn.setSpecies(this.getSpecies(itemstack));
            entitySpawn.setPosition(entityItem.getPosition().getX() + 0.5, entityItem.getPosition().getY() + 1, entityItem.getPosition().getZ() + 0.5);
            entitySpawn.setRandomMobSize();
            entitySpawn.setGender(worldIn.rand.nextInt(2));

            itemstack.shrink(1);
            if (itemstack.isEmpty()) {
                entityItem.setDead();
            }

            worldIn.spawnEntity(entitySpawn);
            entitySpawn.playLivingSound();
        }

        return super.onEntityItemUpdate(entityItem);
    }*/
}
