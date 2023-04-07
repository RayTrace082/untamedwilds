package untamedwilds.world.gen.treedecorator;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.material.Material;
import untamedwilds.block.EpyphitePlantBlock;
import untamedwilds.init.ModBlock;
import untamedwilds.world.UntamedWildsGenerator;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class TreeOrchidDecorator extends TreeDecorator {
    public static final Codec<TreeOrchidDecorator> CODEC;
    public static final TreeOrchidDecorator field_236871_b_ = new TreeOrchidDecorator();

    protected TreeDecoratorType<?> func_230380_a_() {
        return UntamedWildsGenerator.TREE_ORCHID.get();
    }

    static {
        CODEC = Codec.unit(() -> field_236871_b_);
    }

    public void type(LevelSimulatedReader worldIn, BiConsumer<BlockPos, BlockState> p_161746_, Random rand, List<BlockPos> posList, List<BlockPos> blockPosSet) {
        if (!(rand.nextFloat() >= 0.95)) {
            int i = posList.get(0).getY();
            posList.stream().filter((p_236867_1_) -> {
                return p_236867_1_.getY() - i <= 2;
            }).forEach((p_242865_5_) -> {
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    if (rand.nextFloat() <= 0.25F) {
                        Direction direction1 = direction.getOpposite();
                        BlockPos blockpos = p_242865_5_.offset(direction1.getStepX(), 0, direction1.getStepZ());
                        if (worldIn.isStateAtPosition(blockpos, (state) -> state.getMaterial() == Material.AIR)) {
                            BlockState blockstate = ModBlock.ORCHID_RED.get().defaultBlockState().setValue(EpyphitePlantBlock.FACING, direction);
                            this.type(worldIn, p_161746_, rand, posList, blockPosSet);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return null;
    }

    @Override
    public void place(Context p_226044_) {

    }
}