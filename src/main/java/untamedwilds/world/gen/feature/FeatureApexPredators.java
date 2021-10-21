package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import untamedwilds.entity.mammal.bear.AbstractBear;
import untamedwilds.entity.mammal.bear.EntitySunBear;
import untamedwilds.entity.mammal.bigcat.AbstractBigCat;
import untamedwilds.entity.mammal.bigcat.EntityCaveLion;
import untamedwilds.entity.mammal.bigcat.EntityLion;
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
            // TODO: Hardcoded Bear and Big Cats until they get integrated into the Species system
            int groupCount = entry.groupCount;
            EntityType<?> type = entry.entityType;
            if (type.create(world.getWorld()) instanceof AbstractBear) {
                type = AbstractBear.SpeciesBear.getSpeciesByBiome(world, pos);
                if (type != null) {
                    Entity entity = type.create(world.getWorld());
                    if (entity instanceof EntitySunBear) {
                        groupCount = 3;
                    }
                }
            }
            else if (type.create(world.getWorld()) instanceof AbstractBigCat) {
                type = AbstractBigCat.SpeciesBigCat.getSpeciesByBiome(world.getBiome(pos));
                if (type != null) {
                    Entity entity = type.create(world.getWorld());
                    if (entity instanceof EntityLion || entity instanceof EntityCaveLion) {
                        groupCount = 3;
                    }
                }
            }

            if (type != null) {
                boolean offsetY = Math.abs(pos.getY() - world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos).getY()) >= 10;
                if (FaunaSpawn.performWorldGenSpawning(type, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.WORLD_SURFACE, world, offsetY ? world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos).up(4) : pos, rand, groupCount)) { // TODO: change last parameter for entry.groupCount
                    return true;
                }
            }
        }
        return false;
        /*Biome biome = world.getBiome(pos);
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
        return true;*/
    }
}
