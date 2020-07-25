package untamedwilds.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import untamedwilds.world.FaunaHandler;
import untamedwilds.world.FaunaSpawn;

import java.util.Random;
import java.util.function.Function;

public class FeatureOceanSwimming extends Feature<NoFeatureConfig> {

    public FeatureOceanSwimming(Function<Dynamic<?>, ? extends NoFeatureConfig> deserializer) {
        super(NoFeatureConfig::deserialize);
    }

    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        if (rand.nextFloat() > 0.95) {
            Biome biome = world.getBiome(pos);
            FaunaSpawn.performWorldGenSpawning(FaunaHandler.getSpawnableList(FaunaHandler.animalType.LARGE_OCEAN), EntitySpawnPlacementRegistry.PlacementType.IN_WATER, world, biome, pos, rand);
            return true;
        }
        return false;
    }
}
