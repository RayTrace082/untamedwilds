package untamedwilds.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ISpecies;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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

    // Spawn particles throughout the entity
    public static <T extends IParticleData> void spawnParticlesOnEntity(World worldIn, LivingEntity entityIn, T particle, int count, int iter) {
        if (worldIn.isRemote) return;
        for (int i = 0; i < iter;  i++) {
            ((ServerWorld)worldIn).spawnParticle(particle, entityIn.getPosX(), entityIn.getPosY() + (double)entityIn.getHeight() / 1.5D, entityIn.getPosZ(), count, entityIn.getWidth() / 4.0F, entityIn.getHeight() / 4.0F, entityIn.getWidth() / 4.0F, 0.05D);
        }
    }

    // Self explanatory
    public static EntityType<?> getEntityTypeFromTag(CompoundNBT nbt, @Nullable EntityType<?> alt) {
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

                spawn = entity.spawn(worldIn, itemstack, player, spawnPos, SpawnReason.BUCKET, true, offset);
                if (spawn != null) {
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
                    entitySpawn.setVariant(species);
                    if (itemstack.hasDisplayName()) {
                        entitySpawn.setCustomName(itemstack.getDisplayName());
                    }
                }
                worldIn.addEntity(spawn);
            }
        }
    }

    // This function makes the entity drop some Eggs of the given item_name, and with random stacksize between 1 and number
    public static void dropEggs(LivingEntity entity, String item_name, int number) {
        ItemEntity entityitem = entity.entityDropItem(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(UntamedWilds.MOD_ID + ":" + item_name.toLowerCase()))), 0.2F);
        if (entityitem != null) {
            entityitem.getItem().setCount(1 + entity.getRNG().nextInt(number - 1));
        }
    }

    // This function turns the entity into an item with item_name registry name, and removes the entity from the world
    public static void turnEntityIntoItem(LivingEntity entity, String item_name) {
        if (ConfigGamerules.easyMobCapturing.get() || ((MobEntity)entity).getAttackTarget() == null) {
            ItemEntity entityitem = entity.entityDropItem(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(UntamedWilds.MOD_ID + ":" + item_name.toLowerCase()))), 0.2F);
            Random rand = entity.getRNG();
            if (entityitem != null) {
                entityitem.setMotion((rand.nextFloat() - rand.nextFloat()) * 0.1F, rand.nextFloat() * 0.05F, (rand.nextFloat() - rand.nextFloat()) * 0.1F);
                entityitem.getItem().setTag(writeEntityToNBT(entity));
                if (entity.hasCustomName()) {
                    entityitem.getItem().setDisplayName(entity.getCustomName());
                }
                entity.remove();
            }
        }
    }

    // This function replaces a given ItemStack with a new item with item_name registry name, and removes the entity from the world
    public static void mutateEntityIntoItem(LivingEntity entity, PlayerEntity player, Hand hand, String item_name, ItemStack itemstack) {
        if (ConfigGamerules.easyMobCapturing.get() || ((MobEntity)entity).getAttackTarget() == null) {
            entity.playSound(SoundEvents.ITEM_BUCKET_FILL_FISH, 1.0F, 1.0F);
            itemstack.shrink(1);
            ItemStack newitem = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(UntamedWilds.MOD_ID + ":" + item_name.toLowerCase())));
            newitem.setTag(writeEntityToNBT(entity));
            if (entity.hasCustomName()) {
                newitem.setDisplayName(entity.getCustomName());
            }
            if (!entity.world.isRemote) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity) player, newitem);
            }
            if (itemstack.isEmpty()) {
                player.setHeldItem(hand, newitem);
            } else if (!player.inventory.addItemStackToInventory(newitem)) {
                player.dropItem(newitem, false);
            }
            entity.remove();
        }
    }

    // This method writes this entity into a CompoundNBT Tag
    public static CompoundNBT writeEntityToNBT(LivingEntity entity) {
        CompoundNBT baseTag = new CompoundNBT();
        CompoundNBT entityTag = new CompoundNBT();
        entity.writeUnlessRemoved(entityTag);
        entityTag.remove("Pos"); // Remove the Position from the NBT data, as it would fuck things up later on
        entityTag.remove("Motion");
        if (entity instanceof ISpecies) {
            entityTag.remove("HomePosX");
            entityTag.remove("HomePosY");
            entityTag.remove("HomePosZ");
        }
        baseTag.put("EntityTag", entityTag); // Put the entity in the Tag
        return baseTag;
    }

    // Returns whether an entity has full HP or not (takes into account overloaded HP)
    public static boolean hasFullHealth(LivingEntity entityIn) {
        return entityIn.getHealth() >= entityIn.getMaxHealth();
    }

    // Pulls all resources with the given name from the provided ResourceLocation
    public static Pair<Integer, Integer> buildSkinArrays(String name, String species, int variant, HashMap<String, HashMap<Integer, ArrayList<ResourceLocation>>> common_list, HashMap<String, HashMap<Integer, ArrayList<ResourceLocation>>> rare_list) {
        String path = "textures/entity/" + name + "/" + species;

        if (!common_list.containsKey(name)) {
            common_list.put(name, new HashMap<>());
        }
        if (!rare_list.containsKey(name)) {
            rare_list.put(name, new HashMap<>());
        }
        Pair<Integer, Integer> result = new Pair<>(populateSkinArray(path, "_%d.png", variant, common_list.get(name), true), populateSkinArray(path, "_%dr.png", variant, rare_list.get(name), false));
        if (UntamedWilds.DEBUG) {
            UntamedWilds.LOGGER.info("Number of textures for " + species + " " + name + ": " + result);
        }
        return result;
    }

    // Populates the provided array with the data located in the specified path
    public static int populateSkinArray(String path, String suffix, int variant, HashMap<Integer, ArrayList<ResourceLocation>> list, boolean addDefault) {
        list.put(variant, new ArrayList<>());
        for (int i = 0; i < 99; i++) {
            int k = i;
            try {
                final String full_path = String.format(path + suffix, i + 1);
                Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(UntamedWilds.MOD_ID, full_path));
                list.get(variant).add(new ResourceLocation(UntamedWilds.MOD_ID, full_path));
            } catch (Exception e) {
                if (k == 0 && addDefault) {
                    //UntamedWilds.LOGGER.info("Using the default path instead");
                    list.get(variant).add(new ResourceLocation(UntamedWilds.MOD_ID, path + ".png"));
                    k++;
                }
                //UntamedWilds.LOGGER.info(k + " " + variant + " " + path + " " + list);
                if (list.get(variant).isEmpty()) {
                    list.remove(variant);
                }
                return k;
            }
        }
        return 0;
    }

    // Takes the skin from the TEXTURES_COMMON or TEXTURES_RARE array
    public static ResourceLocation getSkinFromEntity(ComplexMob entityIn) {
        String name = entityIn.getType().getRegistryName().getPath();
        if (entityIn.getSkin() > 99) {
            return ComplexMob.TEXTURES_RARE.get(name).get(entityIn.getVariant()).get(Math.min(entityIn.getSkin() - 100, ComplexMob.TEXTURES_RARE.get(name).get(entityIn.getVariant()).size() - 1));
        }
        return ComplexMob.TEXTURES_COMMON.get(name).get(entityIn.getVariant()).get(Math.min(entityIn.getSkin(), ComplexMob.TEXTURES_COMMON.get(name).get(entityIn.getVariant()).size() - 1));
    }
}
