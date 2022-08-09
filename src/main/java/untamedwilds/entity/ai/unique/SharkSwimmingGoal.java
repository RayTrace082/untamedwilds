package untamedwilds.entity.ai.unique;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import untamedwilds.entity.fish.EntityShark;

import javax.annotation.Nullable;

public class SharkSwimmingGoal extends RandomSwimmingGoal {
    private EntityShark taskOwner;

    public SharkSwimmingGoal(EntityShark entity) {
        super(entity, 1.0D, 20);
        this.taskOwner = entity;
    }

    @Nullable
    protected Vec3 getPosition() {
        Vec3 vector3d = BehaviorUtils.getRandomSwimmablePos(this.taskOwner, 10, 7);

        for(int i = 0; vector3d != null && !this.taskOwner.level.getBlockState(new BlockPos(vector3d)).isPathfindable(this.taskOwner.level, new BlockPos(vector3d), PathComputationType.WATER) && i++ < 10; vector3d = BehaviorUtils.getRandomSwimmablePos(this.taskOwner, 10, 7)) {
        }

        if (this.taskOwner.isBottomDweller() && vector3d != null && this.taskOwner.level.canSeeSky(this.taskOwner.blockPosition())) {
            int offset = 5 + this.taskOwner.getRandom().nextInt(7) - 4;
            return new Vec3(vector3d.x(), this.taskOwner.level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int)vector3d.x(), (int)vector3d.z()) + offset, vector3d.z());
        }
        return vector3d;
    }
}
