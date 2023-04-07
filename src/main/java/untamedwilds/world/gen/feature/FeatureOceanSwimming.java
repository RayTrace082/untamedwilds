package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import untamedwilds.config.ConfigMobControl;
import untamedwilds.world.FaunaHandler;
import untamedwilds.world.FaunaSpawn;

import java.util.Optional;
import java.util.Random;

public class FeatureOceanSwimming extends Feature<NoneFeatureConfiguration> {

    public FeatureOceanSwimming(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext context) {
        RandomSource rand = context.level().getRandom();
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        if (ConfigMobControl.dimensionBlacklist.get().contains(world.getLevel().dimension().location().toString()))
            return false;

        for (int i = 0; i < 5; i++) {
            Optional<FaunaHandler.SpawnListEntry> entry = WeightedRandom.getRandomItem(rand, FaunaHandler.getSpawnableList(FaunaHandler.animalType.LARGE_OCEAN));
            if (entry.isPresent()) {
                if (FaunaSpawn.performWorldGenSpawning(entry.get().entityType, SpawnPlacements.Type.IN_WATER, Heightmap.Types.OCEAN_FLOOR, world, pos, rand, entry.get().getGroupCount())) {
                    return true;
                }
            }
        }
        return false;
    }
}