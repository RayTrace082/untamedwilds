package untamedwilds.entity.ai;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.util.math.vector.Vector3d;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAquatic;
import untamedwilds.entity.IPackEntity;

public class FishReturnToSchoolGoal extends RandomSwimmingGoal {

    private final ComplexMob taskOwner;
    private final int maxDist;

    public FishReturnToSchoolGoal(ComplexMobAquatic entity) {
        this(entity, 1.0D, 20, 5);
    }

    public FishReturnToSchoolGoal(ComplexMobAquatic entity, double speedIn, int chance, int maxDist) {
        super(entity, speedIn, chance);
        this.maxDist = maxDist;
        this.taskOwner = entity;
    }

    @Override
    public boolean shouldExecute() {
        if (!(this.creature instanceof IPackEntity)) {
            return false;
        }
        if (this.taskOwner.herd == null || this.taskOwner.herd.getLeader() == this.taskOwner || this.taskOwner.getDistance(this.taskOwner.herd.getLeader()) < this.maxDist) {
            return false;
        }
        return super.shouldExecute();
    }

    @Override
    protected Vector3d getPosition() {
        Vector3d vec3d = RandomPositionGenerator.findRandomTarget(this.taskOwner.herd.getLeader(), 4, 3);
        return vec3d == null ? RandomPositionGenerator.findRandomTarget(this.taskOwner.herd.getLeader(), 3, 2) : vec3d;
    }
}
