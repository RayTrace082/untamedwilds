package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
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
        if (rand.nextFloat() > 0.24) {
            Biome biome = world.getBiome(pos);
            BlockPos blockPos = new BlockPos(pos.getX(), world.getHeight(Heightmap.Type.OCEAN_FLOOR, pos.getX(), pos.getZ()), pos.getZ()); // So Sessile creatures do not spawn floating
            FaunaSpawn.performWorldGenSpawning(FaunaHandler.getSpawnableList(FaunaHandler.animalType.SESSILE), EntitySpawnPlacementRegistry.PlacementType.IN_WATER, world, biome, blockPos, rand);
            return true;
        }
        return false;
    }
}
