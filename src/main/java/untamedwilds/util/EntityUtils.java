package untamedwilds.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;

import javax.annotation.Nullable;
import java.util.List;

// A series of functions that are not meant to be limited to ComplexMobs
public abstract class EntityUtils {

    // Destroy the boat of the selected entity (if it exists)
    public static boolean destroyBoat(World worldIn, LivingEntity entityIn) {
        if (entityIn.getRidingEntity() != null && entityIn.getRidingEntity() instanceof BoatEntity) {
            BoatEntity boat = (BoatEntity) entityIn.getRidingEntity();
            boat.remove();
            if (worldIn.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                for(int j = 0; j < 3; ++j) {
                    boat.entityDropItem(boat.getBoatType().asPlank());
                }
                for(int k = 0; k < 2; ++k) {
                    boat.entityDropItem(Items.STICK);
                }
            }
            return true;
        }
        return false;
    }

    // Spawn particles throughout the entity. Features safe casting of worldIn
    public static <T extends IParticleData> void spawnParticlesOnEntity(World worldIn, LivingEntity entityIn, T particle, int count, int iter) {
        if (worldIn.isRemote) {
            return;
        }
        for (int i = 0; i < iter;  i++) {
            ((ServerWorld)worldIn).spawnParticle(particle, entityIn.getPosX(), entityIn.getPosY() + (double)entityIn.getHeight() / 1.5D, entityIn.getPosZ(), count, entityIn.getWidth() / 4.0F, entityIn.getHeight() / 4.0F, entityIn.getWidth() / 4.0F, 0.05D);
        }
    }

    // Self explanatory
    public static EntityType<?> getEntityTypeFromTag(@Nullable CompoundNBT nbt, EntityType<?> alt) {
        if (nbt != null && nbt.contains("EntityTag", 10)) {
            CompoundNBT entityNBT = nbt.getCompound("EntityTag");
            if (entityNBT.contains("id", 8)) {
                return EntityType.byKey(entityNBT.getString("id")).orElse(alt);
            }
        }
        return alt;
    }

    public static void buildTooltipData(ItemStack stack, List<ITextComponent> tooltip, EntityType<?> entity, String path) {
        if (stack.getTag() != null) {
            CompoundNBT compound = stack.getChildTag("EntityTag");
            if (compound != null) {
                String component = "mobspawn.tooltip." + (compound.contains("Gender") ? (compound.getInt("Gender") == 0 ? "male" : "female") : "unknown");
                tooltip.add(new TranslationTextComponent(component).mergeStyle(TextFormatting.GRAY));
            }
        }
        if (ConfigGamerules.scientificNames.get()) {
            tooltip.add(new TranslationTextComponent(entity.getTranslationKey() + "_" + path + ".sciname").mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY));
        }
    }

    public static void createMobFromItem(ServerWorld worldIn, ItemStack itemstack, EntityType<?> entity, @Nullable int species, BlockPos spawnPos, @Nullable PlayerEntity player, boolean offset) {
        Entity spawn;
        if (itemstack.getTag() != null) {
            if (itemstack.getTag().contains("EntityTag")) {
                spawn = entity.spawn(worldIn, itemstack, player, spawnPos, SpawnReason.BUCKET, true, offset);
                if (spawn != null) {
                    spawn.setLocationAndAngles(spawnPos.getX() + 0.5F, spawnPos.getY(), spawnPos.getZ() + 0.5F, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
                    if (itemstack.hasDisplayName()) {
                        spawn.setCustomName(itemstack.getDisplayName());
                    }
                    if (worldIn.getEntityByUuid(spawn.getUniqueID()) != null) {
                        UntamedWilds.LOGGER.info("Randomizing repeated UUID");
                        spawn.setUniqueId(MathHelper.getRandomUUID(worldIn.rand));
                        worldIn.func_242417_l(spawn);
                    }
                }
            }
        }
        else {
            // If no NBT data is assigned to the entity (eg. Item taken from the Creative menu), create a new, random mob
            spawn = entity.create(worldIn, null, null, player, spawnPos, SpawnReason.BUCKET, true, offset);
            if (spawn instanceof ComplexMob) {
                // Instead of using onInitialSpawn, data is replicated to prevent RandomSpecies from acting, not an ideal solution
                ComplexMob entitySpawn = (ComplexMob) spawn;
                entitySpawn.setRandomMobSize();
                entitySpawn.setGender(worldIn.rand.nextInt(2));
                entitySpawn.setSpecies(species);
                entitySpawn.setGrowingAge(0);
            }
            if (spawn != null) {
                spawn.setUniqueId(MathHelper.getRandomUUID(worldIn.rand));
                if (itemstack.hasDisplayName()) {
                    spawn.setCustomName(itemstack.getDisplayName());
                }
                worldIn.addEntity(spawn);
            }
        }
    }
}
