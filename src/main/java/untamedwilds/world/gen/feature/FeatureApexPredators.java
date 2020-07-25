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
import untamedwilds.UntamedWilds;
import untamedwilds.world.FaunaHandler;
import untamedwilds.world.FaunaSpawn;

import java.util.Random;
import java.util.function.Function;

public class FeatureApexPredators extends Feature<NoFeatureConfig> {

    public FeatureApexPredators(Function<Dynamic<?>, ? extends NoFeatureConfig> deserializer) {
        super(NoFeatureConfig::deserialize);
    }

    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        if (rand.nextFloat() > 0.8) { // 0.85 as default
            Biome biome = world.getBiome(pos);
            UntamedWilds.LOGGER.info("Trying to spawn apex predator of type: " + FaunaHandler.getSpawnableList(FaunaHandler.animalType.APEX_PRED));
            FaunaSpawn.performWorldGenSpawning(FaunaHandler.getSpawnableList(FaunaHandler.animalType.APEX_PRED), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, world, biome, pos, rand);
            return true;
        }
        return false;
    }
}
