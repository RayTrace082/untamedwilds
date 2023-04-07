package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigMobControl;
import untamedwilds.world.FaunaHandler;
import untamedwilds.world.FaunaSpawn;

import java.util.Optional;
import java.util.Random;

public class FeatureHerbivores extends Feature<NoneFeatureConfiguration> {

    public FeatureHerbivores(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext context) {
        RandomSource rand = context.level().getRandom();
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        if (ConfigMobControl.dimensionBlacklist.get().contains(world.getLevel().dimension().location().toString()))
            return false;

        for (int i = 0; i < 3; i++) {
            Optional<FaunaHandler.SpawnListEntry> entry = WeightedRandom.getRandomItem(rand, FaunaHandler.getSpawnableList(FaunaHandler.animalType.LARGE_HERB));
            if (entry.isPresent()) {
                EntityType<?> type = entry.get().entityType;
                if (FaunaSpawn.performWorldGenSpawning(type, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.WORLD_SURFACE_WG, world, pos, rand, entry.get().getGroupCount())) {
                    return true;
                }
            }
        }
        return false;
    }
}