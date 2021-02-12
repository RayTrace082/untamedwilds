package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import untamedwilds.world.FaunaHandler;
import untamedwilds.world.FaunaSpawn;

import java.util.Random;

public class FeatureOceanSwimming extends Feature<NoFeatureConfig> {

    public FeatureOceanSwimming(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        pos = world.getHeight(Heightmap.Type.OCEAN_FLOOR, pos.add(8, 8, 8));
        for (int i = 0; i < 5; i++) {
            FaunaHandler.SpawnListEntry entry = WeightedRandom.getRandomItem(rand, FaunaHandler.getSpawnableList(FaunaHandler.animalType.LARGE_OCEAN));
            if (FaunaSpawn.performWorldGenSpawning(entry.entityType, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, world, pos, rand, entry.groupCount)) {
                return true;
            }
        }
        return false;
    }
}
