package untamedwilds.entity.ai;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.util.math.vector.Vector3d;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAquatic;
import untamedwilds.entity.IPackEntity;

public class FishWanderAsSchoolGoal extends RandomSwimmingGoal {

    private ComplexMob taskOwner;
    private int maxDist = 6;

    public FishWanderAsSchoolGoal(ComplexMobAquatic entity) {
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
        if (this.taskOwner.herd.getLeader() != this.taskOwner) {
            return false;
        }
        return super.shouldExecute();
    }

    protected Vector3d getPosition() {
        return RandomPositionGenerator.findRandomTarget(this.creature, 20, 7);
    }

    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, this.speed);
        for (ComplexMob herd_member : this.taskOwner.herd.creatureList) {
            if (this.taskOwner.getDistance(this.taskOwner.herd.getLeader()) < this.maxDist) {
                herd_member.getNavigator().tryMoveToXYZ(this.x + (herd_member.getPosX() - this.taskOwner.getPosX()), this.y + (herd_member.getPosY() - this.taskOwner.getPosY()), this.z + (herd_member.getPosZ() - this.taskOwner.getPosZ()), this.speed);
            }
        }
    }
}
