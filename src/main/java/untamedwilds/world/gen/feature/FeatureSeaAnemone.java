package untamedwilds.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.CountConfig;
import untamedwilds.init.ModBlock;

import java.util.Random;
import java.util.function.Function;

public class FeatureSeaAnemone extends Feature<CountConfig> {
    public FeatureSeaAnemone(Function<Dynamic<?>, ? extends CountConfig> p_i51442_1_) {
        super(p_i51442_1_);
    }

    public boolean place(IWorld worldIn, ChunkGenerator<?> generator, Random rand, BlockPos pos, CountConfig config) {
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
        for(int j = 0; j < config.count; ++j) {
            int k = rand.nextInt(12) - rand.nextInt(12);
            int l = rand.nextInt(12) - rand.nextInt(12);
            int i1 = worldIn.getHeight(Heightmap.Type.OCEAN_FLOOR, pos.getX() + k, pos.getZ() + l);
            BlockPos blockpos = new BlockPos(pos.getX() + k, i1, pos.getZ() + l);
            BlockState blockstate = type.getDefaultState();
            if (worldIn.getBlockState(blockpos).getBlock() == Blocks.WATER && blockstate.isValidPosition(worldIn, blockpos)) {
                worldIn.setBlockState(blockpos, blockstate, 2);
                ++i;
            }
        }

        return i > 0;
    }
}