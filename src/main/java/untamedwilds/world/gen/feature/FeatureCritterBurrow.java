package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.entity.Entity;
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

import java.util.Optional;
import java.util.Random;

public class FeatureCritterBurrow extends Feature<NoFeatureConfig> {

    public FeatureCritterBurrow(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        int i = rand.nextInt(8) - rand.nextInt(8);
        int j = rand.nextInt(8) - rand.nextInt(8);
        int k = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos.getX() + i, pos.getZ() + j);
        pos = new BlockPos(pos.getX() + i, k, pos.getZ() + j);
        FaunaHandler.SpawnListEntry entry = WeightedRandom.getRandomItem(rand, FaunaHandler.getSpawnableList(FaunaHandler.animalType.CRITTER));

        Entity entity = entry.entityType.create(world.getWorld());
        if (entity != null && world.getBlockState(pos).canBeReplacedByLeaves(world, pos) && world.getBlockState(pos.down()).canSpawnMobs(world, pos, entity)) {
            //((MobEntity)entity).onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.CHUNK_GENERATION, null, null);
            if (entity instanceof ISpecies) {
                Optional<RegistryKey<Biome>> optional = world.func_242406_i(pos);
                int variant = ((ISpecies)entity).setSpeciesByBiome(optional.get(), world.getBiome(pos), SpawnReason.CHUNK_GENERATION);
                if (variant != 99) {
                    world.setBlockState(pos, ModBlock.BURROW.get().getDefaultState(), 1);
                    CritterBurrowBlockEntity te = (CritterBurrowBlockEntity)world.getTileEntity(pos);
                    te.setCount(Math.max(3, entry.groupCount * (rand.nextInt(2) + 1)));
                    te.setEntityType(entry.entityType);
                    te.setVariant(variant);
                    entity.remove();
                }
            }
            else {
                world.setBlockState(pos, ModBlock.BURROW.get().getDefaultState(), 1);
                CritterBurrowBlockEntity te = (CritterBurrowBlockEntity)world.getTileEntity(pos);
                te.setCount(entry.groupCount * rand.nextInt(3) + 4);
                te.setEntityType(entry.entityType);
                entity.remove();
            }
            return true;
        }
        return false;
    }
}
