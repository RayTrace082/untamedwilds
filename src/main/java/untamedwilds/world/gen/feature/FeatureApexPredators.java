package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.arthropod.Tarantula;
import untamedwilds.entity.mammal.EntityHippo;
import untamedwilds.entity.mammal.bear.AbstractBear;
import untamedwilds.entity.mammal.bigcat.AbstractBigCat;
import untamedwilds.init.ModEntity;
import untamedwilds.world.FaunaHandler;
import untamedwilds.world.FaunaSpawn;

import java.util.Random;

public class FeatureApexPredators extends Feature<NoFeatureConfig> {

    public FeatureApexPredators(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        if (rand.nextFloat() > 0.94) {
            pos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos.add(8, 0, 8));
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
                        groupSize += 1 + rand.nextInt(2);
                    }
                    break;
                case 2:
                    //type = ModEntity.SOFTSHELL_TURTLE;
                    type = EntityHippo.SpeciesHippo.getSpeciesByBiome(biome);
                    groupSize += 1 + rand.nextInt(4);
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
