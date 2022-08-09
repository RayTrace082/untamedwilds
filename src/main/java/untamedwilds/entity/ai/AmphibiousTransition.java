package untamedwilds.entity.ai;

import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import untamedwilds.entity.ComplexMobAmphibious;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class AmphibiousTransition extends RandomStrollGoal {
    private final ComplexMobAmphibious taskOwner;

    public AmphibiousTransition(ComplexMobAmphibious entityIn, double speedIn) {
        this(entityIn, speedIn, 120);
    }

    public AmphibiousTransition(ComplexMobAmphibious entityIn, double speedIn, int chance) {
        super(entityIn, speedIn, chance);
        this.taskOwner = entityIn;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (!taskOwner.canMove() || taskOwner.getTarget() != null /* && !taskOwner.getTarget().isInWater()*/) {
            return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.taskOwner.getNavigation().isDone();
    }

    @Nullable
    protected Vec3 getPosition() {
        Vec3 vec3d = null;
        int rand = this.taskOwner.getRandom().nextInt(2);
        switch (rand) {
            case 0 -> {
                if (this.taskOwner.wantsToBeInWater())
                    vec3d = BehaviorUtils.getRandomSwimmablePos(this.taskOwner, 10, 7);
            }
            case 1 -> {
                if (this.taskOwner.wantsToBeOnLand())
                    vec3d = LandRandomPos.getPos(this.taskOwner, 10, 7);
            }
        }
        return vec3d;
    }
}
