package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import untamedwilds.world.FaunaHandler;
import untamedwilds.world.FaunaSpawn;

import java.util.Random;

public class FeatureUndergroundFaunaLarge extends Feature<NoFeatureConfig> {

    public FeatureUndergroundFaunaLarge(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        if (pos.getY() < 52) {
            if (rand.nextFloat() > 0.96) {
                Biome biome = world.getBiome(pos);
                FaunaSpawn.performWorldGenSpawning(FaunaHandler.getSpawnableList(FaunaHandler.animalType.LARGE_UNDERGROUND), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, world, pos, rand);
                return true;
            }
        }
        return false;
    }
}
