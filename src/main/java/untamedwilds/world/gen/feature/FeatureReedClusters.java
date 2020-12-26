package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
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
            BlockPos blockpos = pos.add(rand.nextInt(4) - rand.nextInt(4), rand.nextInt(2) - rand.nextInt(2), rand.nextInt(4) - rand.nextInt(4));
            if(blockpos.getY() < world.getWorld().getHeight() - 2 && world.getBlockState(blockpos.down()).getBlock().isIn(UTBlockTags.REEDS_PLANTABLE_ON)) {
                if(world.isAirBlock(blockpos) || (world.getBlockState(blockpos).getBlock() == Blocks.WATER && world.isAirBlock(blockpos.up()))) {
                    // TODO: Code gore, to fix later
                    world.setBlockState(blockpos, ((FloraReeds)ModBlock.COMMON_REED.get()).getStateForWorldgen(world, blockpos).with(FloraReeds.PROPERTY_AGE, world.getFluidState(blockpos).isEmpty() ? 1 : 2), 2);
                    world.setBlockState(blockpos.up(), ((FloraReeds)ModBlock.COMMON_REED.get()).getStateForWorldgen(world, blockpos.up()).with(FloraReeds.PROPERTY_AGE, 1), 2);
                    world.setBlockState(blockpos.up(2), ((FloraReeds)ModBlock.COMMON_REED.get()).getStateForWorldgen(world, blockpos.up()).with(FloraReeds.PROPERTY_AGE, 0), 2);
                    flag = true;
                }
            }
        }

        return flag;
    }
}
