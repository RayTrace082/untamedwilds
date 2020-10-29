package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import untamedwilds.entity.ComplexMob;
import untamedwilds.world.FaunaHandler;
import untamedwilds.world.FaunaSpawn;

import java.util.Random;

public class FeatureOceanSwimming extends Feature<NoFeatureConfig> {

    public FeatureOceanSwimming(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public boolean func_241855_a(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        if (rand.nextFloat() > 0.96) {
            pos = world.getHeight(Heightmap.Type.OCEAN_FLOOR, pos.add(8, 8, 8));
            Biome biome = world.getBiome(pos);
            EntityType<? extends ComplexMob> type;
            int groupSize = 1;
            type = (EntityType<? extends ComplexMob>) WeightedRandom.getRandomItem(rand, FaunaHandler.getSpawnableList(FaunaHandler.animalType.LARGE_OCEAN)).entityType;
            FaunaSpawn.performWorldGenSpawning(type, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, world, biome, pos, rand, groupSize);
            return true;
        }
        return false;
    }
}
