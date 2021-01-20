package untamedwilds.world.gen.treedecorator;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import untamedwilds.block.BlockPlantEpyphite;
import untamedwilds.init.ModBlock;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class TreeOrchidDecorator extends TreeDecorator {
    public static final Codec<TreeOrchidDecorator> field_236866_a_ = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(TreeOrchidDecorator::new, (p_236868_0_) -> {
        return p_236868_0_.chance;
    }).codec();
    public final float chance;

    public TreeOrchidDecorator(float chance) {
        this.chance = chance;
    }

    protected TreeDecoratorType<?> func_230380_a_() {
        return TreeDecoratorType.COCOA;
    }

    public void func_225576_a_(ISeedReader worldIn, Random rand, List<BlockPos> posList, List<BlockPos> p_225576_4_, Set<BlockPos> blockPosSet, MutableBoundingBox boundingBox) {
      if (!(rand.nextFloat() >= this.chance)) {
         int i = posList.get(0).getY();
         posList.stream().filter((p_236867_1_) -> {
            return p_236867_1_.getY() - i <= 2;
         }).forEach((p_242865_5_) -> {
            for(Direction direction : Direction.Plane.HORIZONTAL) {
               if (rand.nextFloat() <= 0.25F) {
                  Direction direction1 = direction.getOpposite();
                  BlockPos blockpos = p_242865_5_.add(direction1.getXOffset(), 0, direction1.getZOffset());
                  if (Feature.isAirAt(worldIn, blockpos)) {
                     BlockState blockstate = ModBlock.ORCHID_RED.get().getDefaultState().with(BlockPlantEpyphite.HORIZONTAL_FACING, direction);
                     this.func_227423_a_(worldIn, blockpos, blockstate, blockPosSet, boundingBox);
                  }
               }
            }
         });
      }
   }
}