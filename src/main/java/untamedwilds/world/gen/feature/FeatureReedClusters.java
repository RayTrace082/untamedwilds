package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import untamedwilds.block.FloraReeds;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModTags.UTBlockTags;

import java.util.Random;

public class FeatureReedClusters extends Feature<NoFeatureConfig> {

    public FeatureReedClusters(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        boolean flag = false;

        for(int i = 0; i < 64; ++i) {
            BlockPos blockpos = pos.add(rand.nextInt(6) - rand.nextInt(6), rand.nextInt(2) - rand.nextInt(2), rand.nextInt(6) - rand.nextInt(6));
            if(world.getBlockState(blockpos.down()).getBlock().isIn(UTBlockTags.REEDS_PLANTABLE_ON)) {
                if(world.isAirBlock(blockpos) || (world.getBlockState(blockpos).getBlock() == Blocks.WATER && world.isAirBlock(blockpos.up()))) {
                    int height = rand.nextInt(4);
                    for (int j = 0; j <= height; ++j) {
                        int fluidstate = world.getFluidState(blockpos.up(j)).isEmpty() ? 1 : 2;
                        BlockState blockstate = ((FloraReeds) ModBlock.COMMON_REED.get()).getStateForWorldgen(world, blockpos.up(j));
                        if (blockstate != null) {
                            world.setBlockState(blockpos.up(j), blockstate.with(FloraReeds.PROPERTY_AGE, j == height ? 0 : fluidstate), 2);
                        }
                    }
                    flag = true;
                }
            }
        }

        return flag;
    }
}
