package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import untamedwilds.block.CritterBurrowBlock;
import untamedwilds.block.blockentity.CritterBurrowBlockEntity;
import untamedwilds.config.ConfigMobControl;
import untamedwilds.entity.ComplexMobAmphibious;
import untamedwilds.entity.ISpecies;
import untamedwilds.init.ModBlock;
import untamedwilds.world.FaunaHandler;

import java.util.Optional;
import java.util.Random;

public class FeatureCritterBurrow extends Feature<NoneFeatureConfiguration> {

    public FeatureCritterBurrow(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext context) {
        RandomSource rand = context.level().getRandom();
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        if (ConfigMobControl.dimensionBlacklist.get().contains(world.getLevel().dimension().location().toString()))
            return false;

        int i = rand.nextInt(8) - rand.nextInt(8);
        int j = rand.nextInt(8) - rand.nextInt(8);
        int k = world.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX() + i, pos.getZ() + j);
        pos = new BlockPos(pos.getX() + i, k, pos.getZ() + j);
        Optional<FaunaHandler.SpawnListEntry> entry = WeightedRandom.getRandomItem(rand, FaunaHandler.getSpawnableList(FaunaHandler.animalType.CRITTER));
        if (entry.isPresent()) {
            Entity entity = entry.get().entityType.create(world.getLevel());
            int variant = -1;
            if (entity != null && isReplaceablePlant(world, pos)) {
                if (!world.getFluidState(pos).isEmpty() && !(entity instanceof ComplexMobAmphibious))
                    return false;
                if (entity instanceof ISpecies) {
                    variant = ((ISpecies) entity).setSpeciesByBiome(world.getBiome(pos), MobSpawnType.CHUNK_GENERATION);
                    if (variant == 99) {
                        entity.discard();
                        return false;
                    }
                }
                if (world.getBlockState(pos).getBlock() == Blocks.SNOW && world.getBlockState(pos.below()).getBlock() == Blocks.GRASS_BLOCK)
                    world.setBlock(pos.below(), Blocks.GRASS_BLOCK.defaultBlockState().setValue(GrassBlock.SNOWY, false), 2); // TODO: Shitty hack to fix grey grass in snowy biomes
                if (world.getBlockEntity(pos) == null) {
                    world.setBlock(pos, ModBlock.BURROW.get().defaultBlockState().setValue(CritterBurrowBlock.WATERLOGGED, !world.getFluidState(pos).isEmpty()), 2);
                    if (world.getBlockState(pos).getBlock() == ModBlock.BURROW.get()) {
                        CritterBurrowBlockEntity te = (CritterBurrowBlockEntity) world.getBlockEntity(pos);
                        if (te != null) {
                            te.setCount(Math.max(4, entry.get().getGroupCount() * (rand.nextInt(3) + 1)));
                            te.setEntityType(entry.get().entityType);
                            if (variant >= 0)
                                te.setVariant(variant);
                        }
                        entity.discard();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isReplaceablePlant(LevelSimulatedReader p_67289_, BlockPos p_67290_) {
        return p_67289_.isStateAtPosition(p_67290_, (p_160551_) -> {
            Material material = p_160551_.getMaterial();
            return material == Material.REPLACEABLE_PLANT || material == Material.AIR;
        });
    }
}