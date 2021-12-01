package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModTags.BlockTags;

import java.util.Random;

public class FeatureFloatingPlants extends Feature<NoFeatureConfig> {

    public FeatureFloatingPlants(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        boolean flag = false;

        for(int i = 0; i < 64; ++i) {
            BlockPos blockpos = pos.add(rand.nextInt(6) - rand.nextInt(6), rand.nextInt(2) - rand.nextInt(2), rand.nextInt(6) - rand.nextInt(6));
            if(world.getBlockState(blockpos.down()).getBlock().isIn(BlockTags.REEDS_PLANTABLE_ON)) {
                if(world.isAirBlock(blockpos) && (world.getBlockState(blockpos.down()).getBlock() == Blocks.WATER)) {
                    world.setBlockState(blockpos, ModBlock.WATER_HYACINTH.get().getDefaultState(), 2);
                    flag = true;
                }
            }
        }

        return flag;
    }
}
