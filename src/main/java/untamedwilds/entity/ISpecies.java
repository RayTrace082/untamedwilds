package untamedwilds.entity;

import net.minecraft.entity.SpawnReason;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;

/**
 * Interface reserved for mobs which implement species, defined as an attached Enum
 * Species are visual variants of the original Entity, and should not have unique behaviors
 * Unlike ISkins, ISpecies need to match their species to breed, and may have unique Eggs and/or Spawn Items
 * Each Species defined as the attached Enum needs to have it's own .sciname defined
 */

public interface ISpecies {

    int setSpeciesByBiome(RegistryKey<Biome> biomekey, Biome biome, SpawnReason reason);

    String getRawSpeciesName(int i);

    String getSpeciesName(int i);

    default String getSpeciesName() {
        return this instanceof ComplexMob ? getSpeciesName(((ComplexMob)this).getVariant()) : "";
    }

    default boolean isArtificialSpawnReason(SpawnReason reason) {
        return reason == SpawnReason.SPAWN_EGG || reason == SpawnReason.BUCKET || reason == SpawnReason.MOB_SUMMONED || reason == SpawnReason.COMMAND || reason == SpawnReason.SPAWNER;
    }
}
