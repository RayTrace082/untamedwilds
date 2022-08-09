package untamedwilds.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.phys.Vec3;
import untamedwilds.entity.ComplexMobAmphibious;

import javax.annotation.Nullable;

public class AmphibiousRandomSwimGoal extends RandomSwimmingGoal {
    public static final int[][] XY_DISTANCE_TIERS = new int[][]{{1, 1}, {3, 3}, {5, 5}, {6, 5}, {7, 7}, {10, 7}};
    private final ComplexMobAmphibious fish;

    public AmphibiousRandomSwimGoal(ComplexMobAmphibious entityIn, double speedIn, int chance) {
        super(entityIn, speedIn, chance);
        this.fish = entityIn;
    }

    public boolean canUse() {
        if (!this.fish.isInWater() && this.fish.wantsToBeOnLand()) {
            return false;
        }
        return super.canUse();
    }

    @Nullable
    protected Vec3 getPosition() {
        Vec3 vec3 = null;
        Vec3 vec31 = null;

        for(int[] aint : XY_DISTANCE_TIERS) {
            if (vec3 == null) {
                vec31 = BehaviorUtils.getRandomSwimmablePos(this.fish, aint[0], aint[1]);
            } else {
                vec31 = this.fish.position().add(this.fish.position().vectorTo(vec3).normalize().multiply(aint[0], aint[1], aint[0]));
            }

            if (vec31 == null || this.fish.level.getFluidState(new BlockPos(vec31)).isEmpty()) {
                return vec3;
            }

            vec3 = vec31;
        }

        return vec31;
    }
}
