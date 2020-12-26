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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;

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
        if (stack.getTag() != null) {
            CompoundNBT compound = stack.getChildTag("EntityTag");
            if (compound.contains("Gender")) {
                if (compound.getInt("Gender") == 0) {
                    tooltip.add((new TranslationTextComponent("mobspawn.tooltip.male")).mergeStyle(TextFormatting.GRAY));
                }
                else { tooltip.add((new TranslationTextComponent("mobspawn.tooltip.female")).mergeStyle(TextFormatting.GRAY)); }
            }
            else { tooltip.add((new TranslationTextComponent("mobspawn.tooltip.unknown")).mergeStyle(TextFormatting.GRAY)); }
        }
        if (ConfigGamerules.scientificNames.get()) {
            tooltip.add(new TranslationTextComponent(entity.getTranslationKey() + "_" + this.sciname + ".sciname").mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY));
        }
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

            EntityType<?> entity = this.getType(itemStack.getTag());
            Entity spawn;
            if (itemStack.hasTag()) {
                if (itemStack.getTag().contains("EntityTag")) {
                    spawn = entity.create((ServerWorld) worldIn, itemStack.getTag(), null, useContext.getPlayer(), spawnPos, SpawnReason.BUCKET, true, !Objects.equals(pos, spawnPos) && facing == Direction.UP);
                    if (itemStack.hasDisplayName()) {
                        spawn.setCustomName(itemStack.getDisplayName());
                    }
                    spawn.setLocationAndAngles(spawnPos.getX() + 0.5F, spawnPos.getY(), spawnPos.getZ() + 0.5F, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
                    if (((ServerWorld) worldIn).getEntityByUuid(spawn.getUniqueID()) != null) {
                        UntamedWilds.LOGGER.info("Randomizing repeated UUID");
                        spawn.setUniqueId(MathHelper.getRandomUUID(worldIn.rand));
                        worldIn.addEntity(spawn); // Commented out because it throws an exception on the chunk tracker
                    }
                    /*if (!((ServerWorld) worldIn).addEntityIfNotDuplicate(spawn)) {
                        spawn.setUniqueId(MathHelper.getRandomUUID(worldIn.rand));
                        //worldIn.addEntity(spawn); // Commented out because it throws an exception on the chunk tracker
                        //UntamedWilds.LOGGER.info("Randomizing repeated UUID");
                    }*/
                }
            }
            else {
                // If no NBT data is assigned to the entity (eg. Item taken from the Creative menu), create a new, random mob
                spawn = entity.create((ServerWorld) worldIn, itemStack.getTag(), null, useContext.getPlayer(), spawnPos, SpawnReason.BUCKET, true, !Objects.equals(pos, spawnPos) && facing == Direction.UP);
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

    private EntityType<?> getType(@Nullable CompoundNBT nbt) {
        if (nbt != null && nbt.contains("EntityTag", 10)) {
            CompoundNBT entityNBT = nbt.getCompound("EntityTag");
            if (entityNBT.contains("id", 8)) {
                return EntityType.byKey(entityNBT.getString("id")).orElse(this.entity);
            }
        }

        return this.entity;
    }
}
