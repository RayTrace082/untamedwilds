package untamedwilds.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import untamedwilds.entity.ComplexMob;

import java.util.EnumSet;
import java.util.List;

public class FollowParentGoal extends Goal {
    private final ComplexMob taskOwner;
    private ComplexMob parentAnimal;
    private final double moveSpeed;
    private int delayCounter;

    public FollowParentGoal(ComplexMob entityIn, double speedIn) {
        this.taskOwner = entityIn;
        this.moveSpeed = speedIn;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
       if (!this.taskOwner.isBaby() || !this.taskOwner.canMove()) {
            return false;
        }
        List<? extends ComplexMob> list = this.taskOwner.level.getEntitiesOfClass(this.taskOwner.getClass(), this.taskOwner.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
        ComplexMob entityanimal = null;
        double d0 = Double.MAX_VALUE;

        for (ComplexMob entityanimal1 : list) {
            if (!entityanimal1.isBaby() && (entityanimal1.getVariant() == this.taskOwner.getVariant())) {
                double d1 = this.taskOwner.distanceToSqr(entityanimal1);

                if (d1 <= d0) {
                    d0 = d1;
                    entityanimal = entityanimal1;
                }
            }
        }

        if (entityanimal == null) {
            return false;
        }
        else if (d0 < 9.0D) {
            return false;
        }
        else {
            this.parentAnimal = entityanimal;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.taskOwner.getAge() >= 0) {
            return false;
        }
        else if (!this.parentAnimal.isAlive()) {
            return false;
        }
        else {
            double d0 = this.taskOwner.distanceToSqr(this.parentAnimal);
            return d0 >= 9.0D && d0 <= 256.0D;
        }
    }

    @Override
    public void start()
    {
        this.delayCounter = 0;
    }

    @Override
    public void stop()
    {
        this.parentAnimal = null;
    }

    @Override
    public void tick() {
        if (--this.delayCounter <= 0) {
            this.delayCounter = 10;
            this.taskOwner.getNavigation().moveTo(this.parentAnimal, this.moveSpeed);
        }
    }
}
