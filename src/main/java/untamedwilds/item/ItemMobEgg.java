package untamedwilds.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.entity.ComplexMob;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemMobEgg extends Item {
    private EntityType<? extends ComplexMob> entity;
    public int species;

    public ItemMobEgg(EntityType<? extends ComplexMob> typeIn, int species, Properties properties) {
        super(properties);
        this.entity = typeIn;
        this.species = species;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("mobspawn.tooltip.unknown").applyTextStyle(TextFormatting.GRAY));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext useContext) {
        World worldIn = useContext.getWorld();
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            ItemStack itemStack = useContext.getItem();
            BlockPos pos = useContext.getPos();
            Direction facing = useContext.getFace();
            BlockState blockState = worldIn.getBlockState(pos);

            BlockPos spawnPos;
            if (blockState.getCollisionShape(worldIn, pos).isEmpty()) {
                spawnPos = pos;
            } else {
                spawnPos = pos.offset(facing);
            }

            if (!useContext.getPlayer().abilities.isCreativeMode) {
                itemStack.shrink(1);
            }
            EntityType<?> entity = this.getType(itemStack.getTag());
            Entity spawn = entity.create(worldIn, itemStack.getTag(), null, useContext.getPlayer(), spawnPos, SpawnReason.BUCKET, true, !Objects.equals(pos, spawnPos) && facing == Direction.UP);
            if (!itemStack.hasTag()) {
                if (spawn instanceof ComplexMob) {
                    ComplexMob entitySpawn = (ComplexMob)spawn;
                    entitySpawn.setRandomMobSize();
                    entitySpawn.setGender(worldIn.rand.nextInt(2));
                    entitySpawn.setSpecies(this.species);
                    entitySpawn.setGrowingAge(entitySpawn.getAdulthoodTime() * -1);
                    entitySpawn.setPlayerSpawned(true);
                }
            }
            worldIn.addEntity(spawn);
            return ActionResultType.SUCCESS;
        }
    }

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

    public EntityType<?> getType(@Nullable CompoundNBT nbt) {
        if (nbt != null && nbt.contains("EntityTag", 10)) {
            CompoundNBT entityNBT = nbt.getCompound("EntityTag");
            if (entityNBT.contains("id", 8)) {
                return EntityType.byKey(entityNBT.getString("id")).orElse(this.entity);
            }
        }

        return this.entity;
    }
}
