package untamedwilds.entity.ai;

import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAquatic;
import untamedwilds.entity.IPackEntity;

public class FishReturnToSchoolGoal extends RandomSwimmingGoal {

    private final ComplexMob taskOwner;
    private final int maxDist;

    public FishReturnToSchoolGoal(ComplexMobAquatic entityIn) {
        this(entityIn, 1.0D, 20, 5);
    }

    public FishReturnToSchoolGoal(ComplexMobAquatic entityIn, double speedIn, int chance, int maxDist) {
        super(entityIn, speedIn, chance);
        this.maxDist = maxDist;
        this.taskOwner = entityIn;
    }

    @Override
    public boolean canUse() {
        if (!(this.mob instanceof IPackEntity)) {
            return false;
        }
        if (this.taskOwner.herd == null || this.taskOwner.herd.getLeader() == this.taskOwner || this.taskOwner.distanceTo(this.taskOwner.herd.getLeader()) < this.maxDist) {
            return false;
        }
        return super.canUse();
    }

    @Override
    protected Vec3 getPosition() {
        Vec3 vec3d = DefaultRandomPos.getPos(this.taskOwner.herd.getLeader(), 4, 3);
        return vec3d == null ? DefaultRandomPos.getPos(this.taskOwner.herd.getLeader(), 3, 2) : vec3d;
    }
}
