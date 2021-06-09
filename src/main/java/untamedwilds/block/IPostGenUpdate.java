package untamedwilds.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;

/**
 * Interface reserved for flora which need to have different BlockState setup than those from naturally generated
 */

public interface IPostGenUpdate {

    void updatePostGen(ISeedReader worldIn, BlockPos pos);
}
