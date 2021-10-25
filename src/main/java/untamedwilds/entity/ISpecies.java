package untamedwilds.entity;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.biome.Biome;
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

    default int setSpeciesByBiome(RegistryKey<Biome> biomekey, Biome biome, SpawnReason reason) {
        if (ConfigGamerules.randomSpecies.get() || isArtificialSpawnReason(reason)) {
            return ((MobEntity)this).getRNG().nextInt(ComplexMob.ENTITY_DATA_HASH.get(((MobEntity)this).getType()).getSpeciesData().size());
        }
        List<Integer> validTypes = new ArrayList<>();
        for (SpeciesDataHolder speciesDatum : ComplexMob.ENTITY_DATA_HASH.get(((MobEntity)this).getType()).getSpeciesData()) {
            for(Biome.Category biomeTypes : speciesDatum.getBiomeCategories()) {
                if(biome.getCategory() == biomeTypes){
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
        return ComplexMob.ENTITY_DATA_HASH.get(((ComplexMob)this).getType()).getSpeciesData().get(i).getName().toLowerCase();
    }

    default float getModelScale(int i) {
        return ComplexMob.ENTITY_DATA_HASH.get(((ComplexMob)this).getType()).getScale(i);
    }

    default String getSpeciesName(int i) {
        return new TranslationTextComponent("entity.untamedwilds." + ((ComplexMob)this).getName().getString().toLowerCase() + "_" + getRawSpeciesName(i)).getString();
    }

    default String getSpeciesName() {
        return this instanceof ComplexMob ? getSpeciesName(((ComplexMob)this).getVariant()) : "";
    }

    default boolean isArtificialSpawnReason(SpawnReason reason) {
        return reason == SpawnReason.SPAWN_EGG || reason == SpawnReason.BUCKET || reason == SpawnReason.MOB_SUMMONED || reason == SpawnReason.COMMAND || reason == SpawnReason.SPAWNER;
    }
}
