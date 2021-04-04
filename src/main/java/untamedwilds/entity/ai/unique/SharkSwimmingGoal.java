package untamedwilds.entity.ai.unique;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.gen.Heightmap;
import untamedwilds.entity.fish.EntityShark;

import javax.annotation.Nullable;

public class SharkSwimmingGoal extends RandomSwimmingGoal {
    private EntityShark taskOwner;

    public SharkSwimmingGoal(EntityShark entity) {
        super(entity, 1.0D, 20);
        this.taskOwner = entity;
    }

    @Nullable
    protected Vector3d getPosition() {
        Vector3d vector3d = RandomPositionGenerator.findRandomTarget(this.creature, 10, 7);

        for(int i = 0; vector3d != null && !this.creature.world.getBlockState(new BlockPos(vector3d)).allowsMovement(this.creature.world, new BlockPos(vector3d), PathType.WATER) && i++ < 10; vector3d = RandomPositionGenerator.findRandomTarget(this.creature, 10, 7)) {
        }

        if (this.taskOwner.isBottomDweller() && vector3d != null && this.taskOwner.world.canBlockSeeSky(this.taskOwner.getPosition())) {
            int offset = 5 + this.creature.getRNG().nextInt(7) - 4;
            return new Vector3d(vector3d.getX(), this.creature.world.getHeight(Heightmap.Type.OCEAN_FLOOR, (int)vector3d.getX(), (int)vector3d.getZ()) + offset, vector3d.getZ());
        }
        return vector3d;
    }
}
