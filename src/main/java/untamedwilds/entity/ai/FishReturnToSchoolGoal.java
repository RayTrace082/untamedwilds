package untamedwilds.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.phys.Vec3;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAquatic;
import untamedwilds.entity.IPackEntity;

public class FishReturnToSchoolGoal extends RandomSwimmingGoal {

    private final ComplexMob taskOwner;
    private final int maxDist;
    private boolean hasReachedLeader;

    public FishReturnToSchoolGoal(ComplexMobAquatic entityIn) {
        this(entityIn, 2D, 10, 5);
    }

    public FishReturnToSchoolGoal(ComplexMobAquatic entityIn, double speedIn, int chance, int maxDist) {
        super(entityIn, speedIn, chance);
        this.maxDist = maxDist;
        this.taskOwner = entityIn;
    }

    @Override
    public boolean canUse() {
        if (!(this.mob instanceof IPackEntity) || this.taskOwner.herd == null || this.taskOwner.herd.getLeader() == this.taskOwner || this.taskOwner.distanceTo(this.taskOwner.herd.getLeader()) < this.maxDist) {
            return false;
        }
        return super.canUse();
    }

    public boolean canContinueToUse() {
        return super.canContinueToUse() && !this.hasReachedLeader;
    }

    public void tick() {
        super.tick();
        if (this.taskOwner.distanceTo(this.taskOwner.herd.getLeader()) < this.maxDist) {
            this.hasReachedLeader = true;
            this.taskOwner.getNavigation().stop();
            BlockPos dest = this.taskOwner.herd.getLeader().getNavigation().getTargetPos();
            // TODO: Evaluate if this even triggers?
            if (dest != null)
                this.taskOwner.getNavigation().moveTo(dest.getX(), dest.getY(), dest.getZ(), this.speedModifier / 2);
        }
    }

    @Override
    protected Vec3 getPosition() {
        Vec3 vec3d = BehaviorUtils.getRandomSwimmablePos(this.taskOwner.herd.getLeader(), 2, 1);
        return vec3d == null ? BehaviorUtils.getRandomSwimmablePos(this.taskOwner.herd.getLeader(), 2, 1) : vec3d;
    }
}
