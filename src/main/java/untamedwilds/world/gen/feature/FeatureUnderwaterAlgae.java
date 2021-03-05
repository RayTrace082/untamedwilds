package untamedwilds.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import untamedwilds.init.ModBlock;

import java.util.Random;

// TODO: Hardcoded this class to use Amazon Swords, but should be abstracted later
public class FeatureUnderwaterAlgae extends Feature<NoFeatureConfig> {
   public FeatureUnderwaterAlgae(Codec<NoFeatureConfig> p_i231988_1_) {
      super(p_i231988_1_);
   }

   public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
      boolean flag = false;
      int i = rand.nextInt(8) - rand.nextInt(8);
      int j = rand.nextInt(8) - rand.nextInt(8);
      int k = reader.getHeight(Heightmap.Type.OCEAN_FLOOR, pos.getX() + i, pos.getZ() + j);
      BlockPos blockpos = new BlockPos(pos.getX() + i, k, pos.getZ() + j);
      if (reader.getBlockState(blockpos).isIn(Blocks.WATER)) {
         BlockState blockstate = ModBlock.AMAZON_SWORD.get().getDefaultState();
         if (blockstate.isValidPosition(reader, blockpos)) {
            reader.setBlockState(blockpos, blockstate, 2);
            flag = true;
         }
      }

      return flag;
   }
}
