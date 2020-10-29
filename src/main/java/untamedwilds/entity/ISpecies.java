package untamedwilds.entity;

import net.minecraft.entity.SpawnReason;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;

/**
 * Interface reserved for mobs which implement species, defined via an Enum
 * Species are visual variants of the original Entity, Species should not have unique behaviors
 */

public interface ISpecies {
    int setSpeciesByBiome(RegistryKey<Biome> biomekey, Biome biome, SpawnReason reason);
    public String getRawSpeciesName();
    public String getSpeciesName();
}
