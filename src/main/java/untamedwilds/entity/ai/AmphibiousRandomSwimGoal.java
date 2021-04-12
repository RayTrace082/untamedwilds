package untamedwilds.entity.ai;

import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import untamedwilds.entity.ComplexMobAmphibious;

public class AmphibiousRandomSwimGoal extends RandomSwimmingGoal {
    private final ComplexMobAmphibious fish;

    public AmphibiousRandomSwimGoal(ComplexMobAmphibious entityIn, double speedIn, int chance) {
        super(entityIn, speedIn, chance);
        this.fish = entityIn;
    }

    public boolean shouldExecute() {
        if (!this.fish.isInWater() && this.fish.wantsToLeaveWater()) {
            return false;
        }
        return super.shouldExecute();
    }
}
