package untamedwilds.entity.ai;

import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import untamedwilds.entity.ComplexMobAmphibious;

public class AmphibiousRandomSwimGoal extends RandomSwimmingGoal {
    private final ComplexMobAmphibious fish;

    public AmphibiousRandomSwimGoal(ComplexMobAmphibious entity, double speed, int chance) {
        super(entity, speed, chance);
        this.fish = entity;
    }

    public boolean shouldExecute() {
        if (!this.fish.isInWater() && this.fish.wantsToLeaveWater()) {
            return false;
        }
        return super.shouldExecute();
    }
}
