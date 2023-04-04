package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import untamedwilds.config.ConfigMobControl;
import untamedwilds.init.ModEntity;
import untamedwilds.world.FaunaSpawn;

import java.util.Random;

public class FeatureUndergroundFaunaLarge extends Feature<NoneFeatureConfiguration> {

    public FeatureUndergroundFaunaLarge(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext context) {
        RandomSource rand = context.level().getRandom();
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        if (ConfigMobControl.dimensionBlacklist.get().contains(world.getLevel().dimension().location().toString()))
            return false;

        if (pos.getY() < 52 && rand.nextInt(9) == 0) {
            for (int i = 0; i < 5; i++) {
                /*FaunaHandler.SpawnListEntry entry = WeightedRandom.getRandomItem(rand, FaunaHandler.getSpawnableList(FaunaHandler.animalType.LARGE_UNDERGROUND));
                if (FaunaSpawn.performWorldGenSpawning(entry.get().entityType, SpawnPlacements.Type.ON_GROUND, null, world, pos, rand, entry.get().groupCount)) {
                    return true;
                }*/
                //world.setBlock(pos, Blocks.SEA_LANTERN.defaultBlockState(), 2);
                if (FaunaSpawn.performWorldGenSpawning(ModEntity.BEAR.get(), SpawnPlacements.Type.ON_GROUND, null, world, pos, rand, 1)) {
                    return true;
                }
            }
        }
        return false;
    }
}