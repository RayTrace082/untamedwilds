package untamedwilds.entity.ai;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.util.math.vector.Vector3d;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAquatic;
import untamedwilds.entity.IPackEntity;

public class FishReturnToSchoolGoal extends RandomSwimmingGoal {

    private ComplexMob taskOwner;
    private int maxDist = 6;

    public FishReturnToSchoolGoal(ComplexMobAquatic entity) {
        super(entity, 1.0D, 20);
    }

    public boolean shouldExecute() {
        if (!(this.creature instanceof IPackEntity)) {
            return false;
        }
        this.taskOwner = (ComplexMob) this.creature;
        if (this.taskOwner.herd == null) {
            return false;
        }
        if (this.taskOwner.herd.getLeader() == this.taskOwner) {
            return false;
        }
        if (this.taskOwner.getDistance(this.taskOwner.herd.getLeader()) < this.maxDist) {
            return false;
        }
        return super.shouldExecute();
    }

    protected Vector3d getPosition() {
        Vector3d vec3d = RandomPositionGenerator.findRandomTarget(this.taskOwner.herd.getLeader(), 4, 3);
        return vec3d == null ? RandomPositionGenerator.findRandomTarget(this.taskOwner.herd.getLeader(), 3, 2) : vec3d;
    }
}
