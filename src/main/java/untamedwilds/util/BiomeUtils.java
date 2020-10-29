package untamedwilds.util;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

// Function taken from the Ice & Fire mod
public class BiomeUtils {
    public static String getBiomeName(Biome biome) {
        ResourceLocation loc = ForgeRegistries.BIOMES.getKey(biome);
        return loc == null ? "" : loc.toString();
    }
}
