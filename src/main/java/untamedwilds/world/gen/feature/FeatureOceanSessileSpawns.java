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

public class FeatureOceanSessileSpawns extends Feature<NoFeatureConfig> {

    public FeatureOceanSessileSpawns(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        //pos = new BlockPos(pos.getX(), world.getHeight(Heightmap.Type.OCEAN_FLOOR, pos.getX(), pos.getZ()), pos.getZ()); // So Sessile creatures do not spawn floating
        pos = world.getHeight(Heightmap.Type.WORLD_SURFACE, pos);
        //world.setBlockState(pos, Blocks.TORCH.getDefaultState(), 2);

        for (int i = 0; i < 5; i++) {
            FaunaHandler.SpawnListEntry entry = WeightedRandom.getRandomItem(rand, FaunaHandler.getSpawnableList(FaunaHandler.animalType.SESSILE));
            if (FaunaSpawn.performWorldGenSpawning(entry.entityType, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, world, pos, rand, entry.groupCount)) {
                return true;
            }
        }
        return false;
    }
}
