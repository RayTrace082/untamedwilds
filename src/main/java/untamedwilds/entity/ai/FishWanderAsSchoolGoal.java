package untamedwilds.entity.ai;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.util.math.vector.Vector3d;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAquatic;
import untamedwilds.entity.IPackEntity;

public class FishWanderAsSchoolGoal extends RandomSwimmingGoal {

    private final ComplexMob taskOwner;
    private final int maxDist;

    public FishWanderAsSchoolGoal(ComplexMobAquatic entity) {
        this(entity, 1.0D, 20, 5);
    }

    public FishWanderAsSchoolGoal(ComplexMobAquatic entity, double speedIn, int chance, int maxDist) {
        super(entity, speedIn, chance);
        this.maxDist = maxDist;
        this.taskOwner = entity;
    }

    @Override
    public boolean shouldExecute() {
        if (!(this.creature instanceof IPackEntity)) {
            return false;
        }
        if (this.taskOwner.herd == null || this.taskOwner.herd.getLeader() != this.taskOwner) {
            return false;
        }
        return super.shouldExecute();
    }

    @Override
    protected Vector3d getPosition() {
        return RandomPositionGenerator.findRandomTarget(this.creature, 20, 7);
    }

    @Override
    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, this.speed);
        for (ComplexMob herd_member : this.taskOwner.herd.creatureList) {
            if (this.taskOwner.getDistance(this.taskOwner.herd.getLeader()) < this.maxDist) {
                double posX = this.x + (herd_member.getPosX() - this.taskOwner.getPosX());
                double posY = this.y + (herd_member.getPosY() - this.taskOwner.getPosY());
                double posZ = this.z + (herd_member.getPosZ() - this.taskOwner.getPosZ());
                herd_member.getNavigator().tryMoveToXYZ(posX, posY, posZ, this.speed);
            }
        }
    }
}
