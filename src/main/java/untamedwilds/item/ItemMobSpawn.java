package untamedwilds.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.entity.ComplexMob;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemMobSpawn extends Item {
    private final EntityType<? extends ComplexMob> entity;
    private final int species;
    private final String sciname;

    public ItemMobSpawn(EntityType<? extends ComplexMob> typeIn, int species, String sciname, Properties properties) {
        super(properties);
        this.entity = typeIn;
        this.species = species;
        this.sciname = sciname;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        EntityUtils.buildTooltipData(stack, tooltip, this.entity, this.sciname);
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
            boolean doVerticalOffset = !Objects.equals(pos, spawnPos) && facing == Direction.UP;

            EntityUtils.createMobFromItem((ServerWorld) worldIn, itemStack, entity, this.species, spawnPos, useContext.getPlayer(), doVerticalOffset);

            itemStack.shrink(1);
        }
        return ActionResultType.CONSUME;
    }

    /*public boolean tick() {
        super(tick);
        ItemStack itemstack = entityItem.getItem();
        World worldIn = entityItem.world;

        if (entityItem.world.isRemote || entityItem.ticksExisted < 180 || this.isVerminItemDead(itemstack)) {
            return super.onEntityItemUpdate(entityItem);
        }

        Entity entity;

        if (itemstack.hasTag()) {
            entity = EntityList.createEntityFromNBT(itemstack.getTag(), worldIn);
            LivingEntity entityliving = (LivingEntity)entity;
            entityliving.enablePersistence();
            entityliving.playLivingSound();
        }
        else {
            entity = EntityList.createEntityByIDFromName(this.entity, worldIn);
            if (entity instanceof ComplexMob) {
                ComplexMob entitySpawn = (ComplexMob)entity;
                entitySpawn.setRandomMobSize();
                entitySpawn.setGender(worldIn.rand.nextInt(2));
                entitySpawn.setSpecies(this.species);
                entitySpawn.setGrowingAge(0);
                entitySpawn.enablePersistence();
                entitySpawn.playLivingSound();
                entitySpawn.updateAttributes();
            }
        }
        itemstack.shrink(1);
        if (itemstack.isEmpty()) {
            entityItem.setDead();
        }
        entity.setUniqueId(MathHelper.getRandomUUID(worldIn.rand));
        entity.setPosition(entityItem.getPosition().getX() + 0.5, entityItem.getPosition().getY() + 1, entityItem.getPosition().getZ() + 0.5);
        worldIn.spawnEntity(entity);
        return super.onEntityItemUpdate(entityItem);
    }*/
}
