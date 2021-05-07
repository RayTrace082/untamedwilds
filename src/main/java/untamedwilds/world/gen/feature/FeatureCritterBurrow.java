package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import untamedwilds.block.blockentity.CritterBurrowBlockEntity;
import untamedwilds.entity.ISpecies;
import untamedwilds.init.ModBlock;
import untamedwilds.world.FaunaHandler;
import untamedwilds.world.FaunaSpawn;

import java.util.Optional;
import java.util.Random;

public class FeatureCritterBurrow extends Feature<NoFeatureConfig> {

    public FeatureCritterBurrow(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        pos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos);
        FaunaHandler.SpawnListEntry entry = WeightedRandom.getRandomItem(rand, FaunaHandler.getSpawnableList(FaunaHandler.animalType.CRITTER));
        boolean offsetY = Math.abs(pos.getY() - world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos).getY()) >= 10;

        if (FaunaSpawn.performWorldGenSpawning(entry.entityType, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, world, offsetY ? world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos).up(4) : pos, rand, entry.groupCount)) {
            Entity entity = entry.entityType.create(world.getWorld());
            ((MobEntity)entity).onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.CHUNK_GENERATION, null, null);
            if (world.getBlockState(pos.down()).canSpawnMobs(world, pos, entity)) {
                if (entity instanceof ISpecies) {
                    Optional<RegistryKey<Biome>> optional = world.func_242406_i(pos);
                    int variant = ((ISpecies)entity).setSpeciesByBiome(optional.get(), world.getBiome(pos), SpawnReason.CHUNK_GENERATION);
                    if (variant != 99) {
                        world.setBlockState(pos, ModBlock.BURROW.get().getDefaultState(), 1);
                        CritterBurrowBlockEntity te = (CritterBurrowBlockEntity)world.getTileEntity(pos);
                        te.setCount(entry.groupCount * (rand.nextInt(3) + 1));
                        te.setEntityType(entry.entityType);
                        te.setVariant(variant);
                        entity.remove();
                        return true;
                    }
                }
                else {
                    world.setBlockState(pos, ModBlock.BURROW.get().getDefaultState(), 1);
                    CritterBurrowBlockEntity te = (CritterBurrowBlockEntity)world.getTileEntity(pos);
                    te.setCount(entry.groupCount * rand.nextInt(3) + 1);
                    te.setEntityType(entry.entityType);
                    entity.remove();
                    return true;
                }
            }
            return true;
        }


        return false;
    }
}
