package untamedwilds.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import untamedwilds.entity.ComplexMob;

import java.util.EnumSet;

public class SmartLookRandomlyGoal extends Goal {
    private final ComplexMob field_75258_a;
    private double lookX;
    private double lookZ;
    private int idleTime;

    public SmartLookRandomlyGoal(ComplexMob entityIn) {
        this.field_75258_a = entityIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.field_75258_a.isSleeping()) {
            return this.field_75258_a.getRNG().nextFloat() < 0.02F;
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.idleTime >= 0;
    }

    @Override
    public void startExecuting() {
        double d0 = (Math.PI * 2D) * this.field_75258_a.getRNG().nextDouble();
        this.lookX = Math.cos(d0);
        this.lookZ = Math.sin(d0);
        this.idleTime = 20 + this.field_75258_a.getRNG().nextInt(20);
    }

    @Override
    public void tick() {
        --this.idleTime;
        this.field_75258_a.getLookController().setLookPosition(this.field_75258_a.getPosX() + this.lookX, this.field_75258_a.getPosY() + (double)this.field_75258_a.getEyeHeight(), this.field_75258_a.getPosZ() + this.lookZ);
    }
}
