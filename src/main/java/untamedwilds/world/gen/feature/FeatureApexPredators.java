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
import untamedwilds.entity.mammal.EntityHippo;
import untamedwilds.entity.mammal.bear.AbstractBear;
import untamedwilds.entity.mammal.bigcat.AbstractBigCat;
import untamedwilds.init.ModEntity;
import untamedwilds.world.FaunaSpawn;

import java.util.Random;

public class FeatureApexPredators extends Feature<NoFeatureConfig> {

    public FeatureApexPredators(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        pos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos);
        Biome biome = world.getBiome(pos);
        EntityType<? extends ComplexMob> type;
        int groupSize = 1;
        int rng_type = rand.nextInt(5);
        switch (rng_type) {
            default:
            case 0:
                type = AbstractBear.SpeciesBear.getSpeciesByBiome(world, pos);
                break;
            case 1:
                type = AbstractBigCat.SpeciesBigCat.getSpeciesByBiome(biome);
                if (type == ModEntity.LION || type == ModEntity.CAVE_LION) {
                    groupSize += 1 + rand.nextInt(2);
                }
                break;
            case 2:
                type = EntityHippo.SpeciesHippo.getSpeciesByBiome(biome);
                groupSize += 1 + rand.nextInt(4);
                break;
            case 3:
                type = ModEntity.RHINO;
                break;
            case 4:
                type = ModEntity.HYENA;
                groupSize += 1 + rand.nextInt(4);
                break;
        }

        int diff = Math.abs(pos.getY() - world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos).getY());
        if (diff >= 10) {
            FaunaSpawn.performWorldGenSpawning(type, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, world, world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos).up(4), rand, groupSize);
            return true;
        }
        FaunaSpawn.performWorldGenSpawning(type, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, world, pos, rand, groupSize);
        return true;
    }
}
