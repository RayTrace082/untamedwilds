package untamedwilds.util;

import net.minecraft.entity.*;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;

import javax.annotation.Nullable;
import java.util.List;

// A series of functions that are not meant to be limited to ComplexMobs
public abstract class EntityUtils {

    // Destroy the boat of the selected entity (if it exists)
    public static void destroyBoat(World worldIn, LivingEntity entityIn) {
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
        }
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

    // This function builds a full tooltip containing Custom Name, EntityType, Gender and Scientific name (if available)
    public static void buildTooltipData(ItemStack stack, List<ITextComponent> tooltip, EntityType<?> entity, String path) {
        if (stack.getTag() != null) {
            CompoundNBT compound = stack.getChildTag("EntityTag");
            if (compound != null) {
                //String component = "mobspawn.tooltip." + (compound.contains("Gender") ? (compound.getInt("Gender") == 0 ? "male" : "female") : "unknown");
                //tooltip.add(new TranslationTextComponent(component).mergeStyle(TextFormatting.GRAY));
                String gender = compound.contains("Gender") ? new TranslationTextComponent("mobspawn.tooltip." + (compound.getInt("Gender") == 0 ? "male" : "female")).getString() + " " : "";
                String type = new TranslationTextComponent(entity.getTranslationKey()).getString();
                if (stack.getTag().getCompound("EntityTag").contains("CustomName")) {
                    String customName = stack.getTag().getCompound("EntityTag").getString("CustomName");
                    // Entity uses ITextComponent.Serializer.getComponentFromJson(s) instead of substrings
                    tooltip.add(new StringTextComponent(customName.substring(9, customName.length() - 2) + " (" + gender + type + ")").mergeStyle(TextFormatting.GRAY));
                }
                else {
                    tooltip.add(new StringTextComponent(gender + type).mergeStyle(TextFormatting.GRAY));
                }
            }
        }
        if (ConfigGamerules.scientificNames.get()) {
            String scipath = path.isEmpty() ? "" : "_" + path;
            TranslationTextComponent tooltipText = new TranslationTextComponent(entity.getTranslationKey() + scipath + ".sciname");
            if (!tooltipText.getString().contains(".")) {
                tooltip.add(tooltipText.mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY));
            }
        }
    }

    // This function creates a new mob from the NBT Tag stored in an ItemStack. Uses default EntityType and Species data to allow creating new entities from zero
    public static void createMobFromItem(ServerWorld worldIn, ItemStack itemstack, EntityType<?> entity, @Nullable int species, BlockPos spawnPos, @Nullable PlayerEntity player, boolean offset) {
        Entity spawn;
        if (itemstack.getTag() != null) {
            if (itemstack.getTag().contains("EntityTag")) {
                if (itemstack.getChildTag("EntityTag").contains("UUID")) {
                    if (worldIn.getEntityByUuid(itemstack.getChildTag("EntityTag").getUniqueId("UUID")) != null) {
                        itemstack.getChildTag("EntityTag").putUniqueId("UUID", MathHelper.getRandomUUID(worldIn.rand));
                    }
                }
                itemstack.getChildTag("EntityTag").remove("Pos"); // TODO: Temporary solution to prevent loss of mobs with Pos tag
                
                spawn = entity.spawn(worldIn, itemstack, player, spawnPos, SpawnReason.BUCKET, true, offset);
                if (spawn != null) {
                    //spawn.setLocationAndAngles(spawnPos.getX() + 0.5F, spawnPos.getY(), spawnPos.getZ() + 0.5F, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
                    if (itemstack.hasDisplayName()) {
                        spawn.setCustomName(itemstack.getDisplayName());
                    }
                    worldIn.func_242417_l(spawn);
                }
            }
        }
        else {
            // If no NBT data is assigned to the entity (eg. Item taken from the Creative menu), create a new, random mob
            spawn = entity.create(worldIn, null, null, player, spawnPos, SpawnReason.SPAWN_EGG, true, offset);
            if (spawn != null) {
                if (spawn instanceof MobEntity) {
                    ((MobEntity)spawn).onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(spawnPos), SpawnReason.SPAWN_EGG, null, null);
                }
                if (spawn instanceof ComplexMob) {
                    ComplexMob entitySpawn = (ComplexMob) spawn;
                    entitySpawn.setSpecies(species);
                    if (itemstack.hasDisplayName()) {
                        entitySpawn.setCustomName(itemstack.getDisplayName());
                    }
                }
                worldIn.addEntity(spawn);
            }
        }
    }
}
