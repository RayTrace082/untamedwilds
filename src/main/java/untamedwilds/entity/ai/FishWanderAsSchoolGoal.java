package untamedwilds.entity.ai;

import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAquatic;
import untamedwilds.entity.IPackEntity;

public class FishWanderAsSchoolGoal extends RandomSwimmingGoal {

    private final ComplexMob taskOwner;
    private final int maxDist;

    public FishWanderAsSchoolGoal(ComplexMobAquatic entityIn) {
        this(entityIn, 1.0D, 20, 5);
    }

    public FishWanderAsSchoolGoal(ComplexMobAquatic entityIn, double speedIn, int chance, int maxDist) {
        super(entityIn, speedIn, chance);
        this.maxDist = maxDist;
        this.taskOwner = entityIn;
    }

    @Override
    public boolean canUse() {
        if (!(this.mob instanceof IPackEntity)) {
            return false;
        }
        if (this.taskOwner.herd == null || this.taskOwner.herd.getLeader() != this.taskOwner) {
            return false;
        }
        return super.canUse();
    }

    @Override
    protected Vec3 getPosition() {
        return DefaultRandomPos.getPos(this.mob, 20, 7);
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
        for (ComplexMob herd_member : this.taskOwner.herd.creatureList) {
            if (this.taskOwner.distanceTo(this.taskOwner.herd.getLeader()) < this.maxDist) {
                double posX = this.wantedX + (herd_member.getX() - this.taskOwner.getX());
                double posY = this.wantedY + (herd_member.getY() - this.taskOwner.getY());
                double posZ = this.wantedZ + (herd_member.getZ() - this.taskOwner.getZ());
                herd_member.getNavigation().moveTo(posX, posY, posZ, this.speedModifier);
            }
        }
    }
}
