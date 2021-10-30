package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
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

public class FeatureApexPredators extends Feature<NoFeatureConfig> {

    public FeatureApexPredators(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        for (int i = 0; i < 3; i++) {
            FaunaHandler.SpawnListEntry entry = WeightedRandom.getRandomItem(rand, FaunaHandler.getSpawnableList(FaunaHandler.animalType.APEX_PRED));
            int groupCount = entry.groupCount;
            EntityType<?> type = entry.entityType;
            if (type != null) {
                boolean offsetY = Math.abs(pos.getY() - world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos).getY()) >= 10;
                if (FaunaSpawn.performWorldGenSpawning(type, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.WORLD_SURFACE, world, offsetY ? world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos).up(4) : pos, rand, groupCount)) { // TODO: change last parameter for entry.groupCount
                    return true;
                }
            }
        }
        return false;
    }
}
