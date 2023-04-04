package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;
import untamedwilds.config.ConfigFeatureControl;
import untamedwilds.config.ConfigMobControl;
import untamedwilds.init.ModBlock;

import java.util.Random;

public class FeatureFloatingPlants extends Feature<NoneFeatureConfiguration> {

    public FeatureFloatingPlants(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext context) {
        boolean flag = false;
        RandomSource rand = context.level().getRandom();
        WorldGenLevel world = context.level();
        BlockPos pos = world.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR, context.origin());
        if (ConfigFeatureControl.dimensionFeatureBlacklist.get().contains(world.getLevel().dimension().location().toString()))
            return false;

        for(int i = 0; i < 64; ++i) {
            BlockPos blockpos = pos.offset(rand.nextInt(6) - rand.nextInt(6), rand.nextInt(2) - rand.nextInt(2), rand.nextInt(6) - rand.nextInt(6));
            if(world.getFluidState(blockpos.below()).getType() == Fluids.WATER && world.isEmptyBlock(blockpos)) {
                world.setBlock(blockpos, ModBlock.WATER_HYACINTH.get().defaultBlockState(), 2);
                flag = true;
            }
        }

        return flag;
    }
}