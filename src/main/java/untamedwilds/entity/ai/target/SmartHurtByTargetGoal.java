package untamedwilds.entity.ai.target;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

import java.util.EnumSet;

// Only difference between SmartHurtByTargetGoal and HurtByTargetGoal is that this class
// automatically alerts others when the hurt mod is a baby
public class SmartHurtByTargetGoal extends HurtByTargetGoal {

    public SmartHurtByTargetGoal(PathfinderMob p_26039_, Class<?>... p_26040_) {
        super(p_26039_, p_26040_);
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    public void start() {
        if (this.mob.isBaby())
            this.alertOthers();
        super.start();
    }
}
