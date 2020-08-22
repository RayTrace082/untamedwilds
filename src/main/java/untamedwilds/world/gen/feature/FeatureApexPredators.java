package untamedwilds.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.mammal.EntityHippo;
import untamedwilds.entity.mammal.bear.AbstractBear;
import untamedwilds.entity.mammal.bigcat.AbstractBigCat;
import untamedwilds.init.ModEntity;
import untamedwilds.world.FaunaSpawn;

import java.util.Random;
import java.util.function.Function;

public class FeatureApexPredators extends Feature<NoFeatureConfig> {

    public FeatureApexPredators(Function<Dynamic<?>, ? extends NoFeatureConfig> deserializer) {
        super(NoFeatureConfig::deserialize);
    }

    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        if (rand.nextFloat() > 0.7) {
            Biome biome = world.getBiome(pos);
            EntityType<? extends ComplexMob> type;
            int groupSize = 1;
            int rng_type = rand.nextInt(3);
            switch (rng_type) {
                default:
                case 0:
                    type = AbstractBear.SpeciesBear.getSpeciesByBiome(biome);
                    break;
                case 1:
                    type = AbstractBigCat.SpeciesBigCat.getSpeciesByBiome(biome);
                    if (type == ModEntity.LION || type == ModEntity.CAVE_LION) {
                        groupSize += 1 + rand.nextInt(4);
                    }
                    break;
                case 2:
                    type = EntityHippo.SpeciesHippo.getSpeciesByBiome(biome);
                    groupSize += 1 + rand.nextInt(6);
                    break;
            }

            int diff = Math.abs(pos.getY() - world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos).getY());
            if (diff >= 10) {
                FaunaSpawn.performWorldGenSpawning(type, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, world, biome, world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos).up(4), rand, groupSize);
                return false;
            }
            FaunaSpawn.performWorldGenSpawning(type, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, world, biome, pos, rand, groupSize);
            return true;
        }
        return false;
    }
}
