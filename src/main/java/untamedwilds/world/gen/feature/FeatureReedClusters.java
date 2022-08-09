package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import untamedwilds.block.ReedBlock;
import untamedwilds.config.ConfigFeatureControl;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModTags.ModBlockTags;

import java.util.Random;

public class FeatureReedClusters extends Feature<NoneFeatureConfiguration> {

    public FeatureReedClusters(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext context) {
        boolean flag = false;
        Random rand = context.level().getRandom();
        WorldGenLevel world = context.level();
        if (ConfigFeatureControl.dimensionFeatureBlacklist.get().contains(world.getLevel().dimension().location().toString()))
            return false;

        BlockPos pos = world.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR, context.origin());
        int attempts = world.getBiome(pos).value().biomeCategory == Biome.BiomeCategory.RIVER ? 3 : 1;
        for(int k = 0; k < attempts; ++k) {
            pos = pos.offset(rand.nextInt(8) - rand.nextInt(8), 0, rand.nextInt(8) - rand.nextInt(8));
            for(int i = 0; i < 16; ++i) {
                BlockPos blockpos = pos.offset(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(3) - rand.nextInt(3), rand.nextInt(8) - rand.nextInt(8));
                Biome biome = world.getBiome(pos).value();
                if(world.getFluidState(blockpos.above(3)).isEmpty() && world.getBlockState(blockpos.below()).is(ModBlockTags.REEDS_PLANTABLE_ON) && (biome.biomeCategory == Biome.BiomeCategory.RIVER || biome.biomeCategory == Biome.BiomeCategory.JUNGLE || biome.biomeCategory == Biome.BiomeCategory.SWAMP)) {
                    if(!world.getBlockState(blockpos).isFaceSturdy(context.level(), blockpos, Direction.UP) || (world.getBlockState(blockpos).getBlock() == Blocks.WATER && world.isEmptyBlock(blockpos.above()))) {
                        int height = rand.nextInt(4);
                        for (int j = 0; j <= height; ++j) {
                            if (world.getBlockState(pos).getBlock() == Blocks.SNOW && world.getBlockState(pos.below()).getBlock() == Blocks.GRASS_BLOCK)
                                world.setBlock(pos.below(), Blocks.GRASS_BLOCK.defaultBlockState().setValue(GrassBlock.SNOWY, false), 2); // TODO: Shitty hack to fix grey grass in snowy biomes
                            int fluidstate = world.getFluidState(blockpos.above(j)).isEmpty() ? 1 : 2;
                            BlockState blockstate = ((ReedBlock) ModBlock.COMMON_REED.get()).getStateForWorldgen(world, blockpos.above(j));
                            if (blockstate != null) {
                                world.setBlock(blockpos.above(j), blockstate.setValue(ReedBlock.PROPERTY_AGE, j == height ? 0 : fluidstate), 2);
                            }
                        }
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }
}
