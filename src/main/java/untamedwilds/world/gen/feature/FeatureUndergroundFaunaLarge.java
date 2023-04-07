package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import untamedwilds.config.ConfigFeatureControl;
import untamedwilds.config.ConfigMobControl;
import untamedwilds.world.FaunaHandler;
import untamedwilds.world.FaunaSpawn;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class FeatureUndergroundFaunaLarge extends Feature<NoneFeatureConfiguration> {

    public FeatureUndergroundFaunaLarge(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public static List<PlacementModifier> placed() {
        return Arrays.asList(CountPlacement.of(1),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                RandomOffsetPlacement.vertical(ConstantInt.of(ConfigFeatureControl.probUnderground.get())), BiomeFilter.biome());
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> config) {
        WorldGenLevel world = config.level();
        BlockPos blockpos = config.origin();
        RandomSource rng = config.random();
        Optional<FaunaHandler.SpawnListEntry> entry = Optional.empty();

        BlockPos.MutableBlockPos setPos = new BlockPos.MutableBlockPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        final int horiz = 2;
        final int vert = 2;

        if (ConfigMobControl.dimensionBlacklist.get().contains(world.getLevel().dimension().location().toString()))
            return false;

        for(int i = -horiz; i < horiz + 1; i++)
            for(int j = -horiz; j < horiz + 1; j++)
                for(int k = -vert; k < vert + 1; k++) {
                    setPos.set(blockpos.getX() + i, blockpos.getY() + k, blockpos.getZ() + j);

                    if (world.isStateAtPosition(setPos, BlockState::isAir)) {
                        for (int l = 0; l < 5; l++) {
                            if (entry.isEmpty())
                                entry = WeightedRandom.getRandomItem(rng, FaunaHandler.getSpawnableList(FaunaHandler.animalType.LARGE_UNDERGROUND));
                            if (entry.isPresent()) {
                                EntityType<?> type = entry.get().entityType;
                                if (type != null) {
                                    if (FaunaSpawn.performWorldGenSpawning(type, SpawnPlacements.Type.NO_RESTRICTIONS, null, world, blockpos, rng, entry.get().getGroupCount())) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }

        return true;
    }
}