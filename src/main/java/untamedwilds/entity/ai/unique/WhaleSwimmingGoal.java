package untamedwilds.entity.ai.unique;

import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import untamedwilds.entity.mammal.EntityBaleenWhale;

import javax.annotation.Nullable;

public class WhaleSwimmingGoal extends RandomSwimmingGoal {
    private final EntityBaleenWhale taskOwner;

    public WhaleSwimmingGoal(EntityBaleenWhale entity) {
        super(entity, 1.0D, 20);
        this.taskOwner = entity;
    }

    @Nullable
    protected Vec3 getPosition() {
        Vec3 vector3d = BehaviorUtils.getRandomSwimmablePos(this.taskOwner, 10, 7);
        int offset = 5 + this.taskOwner.getRandom().nextInt(7) - 4;

        //for(int i = 0; vector3d != null && !this.taskOwner.level.getBlockState(new BlockPos(vector3d)).isPathfindable(this.taskOwner.level, new BlockPos(vector3d), PathComputationType.WATER) && i++ < 10; vector3d = BehaviorUtils.getRandomSwimmablePos(this.taskOwner, 10, 7)) {
        //}
        if (vector3d != null) {
            if (this.taskOwner.level.canSeeSky(this.taskOwner.blockPosition())) {
                //this.taskOwner.level.setBlock(new BlockPos(vector3d.x(), this.taskOwner.level.getHeight(Heightmap.Types.WORLD_SURFACE, (int)vector3d.x(), (int)vector3d.z()) - offset, vector3d.z()), Blocks.SEAGRASS.defaultBlockState(), 1);
                return new Vec3(vector3d.x(), this.taskOwner.level.getHeight(Heightmap.Types.WORLD_SURFACE, (int)vector3d.x(), (int)vector3d.z()) - offset, vector3d.z());
            }
            //this.taskOwner.level.setBlock(new BlockPos(vector3d.x(), this.taskOwner.level.getHeight(Heightmap.Types.WORLD_SURFACE, (int)vector3d.x(), (int)vector3d.z()) - offset, vector3d.z()), Blocks.SEAGRASS.defaultBlockState(), 1);
            return vector3d.add(0, -offset, 0);
        }
        return null;
    }
}
