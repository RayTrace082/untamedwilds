package untamedwilds.entity;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.biome.Biome;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.util.SpeciesDataHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Interface reserved for mobs which implement species, defined as an attached Enum
 * Species are visual variants of the original Entity, and should not have unique behaviors
 * Unlike ISkins, ISpecies need to match their species to breed, and may have unique Eggs and/or Spawn Items
 * Each Species defined as the attached Enum needs to have it's own .sciname defined
 */

public interface ISpecies {

    default int setSpeciesByBiome(Holder<Biome> biome, MobSpawnType reason) {
        if (ConfigGamerules.randomSpecies.get() || isArtificialMobSpawnType(reason)) {
            return ((Mob)this).getRandom().nextInt(ComplexMob.getEntityData(((Mob)this).getType()).getSpeciesData().size());
        }
        List<Integer> validTypes = new ArrayList<>();
        for (SpeciesDataHolder speciesDatum : ComplexMob.getEntityData(((Mob)this).getType()).getSpeciesData()) {
            for (List<SpeciesDataHolder.BiomeTestHolder> testList : speciesDatum.getBiomeCategories()) {
                List<Boolean> results = new ArrayList<>();
                for (SpeciesDataHolder.BiomeTestHolder test : testList) {
                    //UntamedWilds.LOGGER.info("Trying to spawn " + ((MobEntity)this).getType().getRegistryName().getPath() + " in " + biome.getRegistryName() + " returns " + test.isValidBiome(biomekey, biome));
                    if (!test.isValidBiome(biome, biome.value())) {
                        break;
                    }
                    results.add(true);
                }
                if (results.size() == testList.size()) {
                    for (int i=0; i < speciesDatum.getRarity(); i++) {
                        validTypes.add(speciesDatum.getVariant());
                    }
                }
            }
        }
        if (validTypes.isEmpty()) {
            return 99;
        } else {
            return validTypes.get(new Random().nextInt(validTypes.size()));
        }
    }

    default String getRawSpeciesName(int i) {
        return ComplexMob.getEntityData(((ComplexMob)this).getType()).getSpeciesData().get(i).getName().toLowerCase();
    }

    default String getSpeciesName(int i) {
        return new TranslatableComponent("entity.untamedwilds." + ((ComplexMob)this).getType().getRegistryName().getPath() + "_" + getRawSpeciesName(i)).getString();
    }

    default String getSpeciesName() {
        return this instanceof ComplexMob ? getSpeciesName(((ComplexMob)this).getVariant()) : "";
    }

    default boolean isArtificialMobSpawnType(MobSpawnType reason) {
        return reason == MobSpawnType.SPAWN_EGG || reason == MobSpawnType.BUCKET || reason == MobSpawnType.MOB_SUMMONED || reason == MobSpawnType.COMMAND || reason == MobSpawnType.SPAWNER;
    }
}
