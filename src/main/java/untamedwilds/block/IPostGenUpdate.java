package untamedwilds.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

/**
 * Interface reserved for flora which need to have different BlockState setup than those from naturally generated
 */

public interface IPostGenUpdate {

    void updatePostGen(LevelAccessor worldIn, BlockPos pos);
}
