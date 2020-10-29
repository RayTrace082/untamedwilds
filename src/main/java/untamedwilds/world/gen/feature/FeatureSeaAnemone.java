package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import untamedwilds.init.ModBlock;

import java.util.Random;

public class FeatureSeaAnemone extends Feature<FeatureSpreadConfig> {

    public FeatureSeaAnemone(Codec<FeatureSpreadConfig> p_i231987_1_) {
        super(p_i231987_1_);
    }

    public boolean func_241855_a(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, FeatureSpreadConfig config) {
        int i = 0;
        int m = rand.nextInt(3);
        Block type;
        switch(m) {
            case 1: type = ModBlock.ANEMONE_SAND.get();
                break;
            case 2: type = ModBlock.ANEMONE_SEBAE.get();
                break;
            default: type = ModBlock.ANEMONE_ROSE_BULB.get();
        }
        for(int j = 0; j < config.func_242799_a().func_242259_a(rand); ++j) {
            int k = rand.nextInt(12) - rand.nextInt(12);
            int l = rand.nextInt(12) - rand.nextInt(12);
            int i1 = world.getHeight(Heightmap.Type.OCEAN_FLOOR, pos.getX() + k, pos.getZ() + l);
            BlockPos blockpos = new BlockPos(pos.getX() + k, i1, pos.getZ() + l);
            BlockState blockstate = type.getDefaultState();
            if (world.getBlockState(blockpos).getBlock() == Blocks.WATER && blockstate.isValidPosition(world, blockpos)) {
                world.setBlockState(blockpos, blockstate, 2);
                ++i;
            }
        }

        return i > 0;
    }
}