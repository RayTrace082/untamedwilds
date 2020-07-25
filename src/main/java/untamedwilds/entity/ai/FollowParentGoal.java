package untamedwilds.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobTerrestrial;

import java.util.EnumSet;
import java.util.List;

public class FollowParentGoal extends Goal {
    private ComplexMob taskOwner;
    private ComplexMob parentAnimal;
    private double moveSpeed;
    private int delayCounter;

    public FollowParentGoal(ComplexMob entityIn, double speedIn) {
        this.taskOwner = entityIn;
        this.moveSpeed = speedIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean shouldExecute() {

       if (!this.taskOwner.isChild()) {
            return false;
        }
        if (this.taskOwner instanceof ComplexMobTerrestrial) {
            if (this.taskOwner.isSleeping() || this.taskOwner.isSitting()) {
                return false;
            }
        }
        List<ComplexMob> list = this.taskOwner.world.getEntitiesWithinAABB(this.taskOwner.getClass(), this.taskOwner.getBoundingBox().grow(8.0D, 4.0D, 8.0D));
        ComplexMob entityanimal = null;
        double d0 = Double.MAX_VALUE;

        for (ComplexMob entityanimal1 : list) {
            if (!entityanimal1.isChild() && (entityanimal1.getSpecies() == this.taskOwner.getSpecies())) {
                double d1 = this.taskOwner.getDistanceSq(entityanimal1);

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

    public boolean shouldContinueExecuting() {
        if (this.taskOwner.getGrowingAge() >= 0) {
            return false;
        }
        else if (!this.parentAnimal.isAlive()) {
            return false;
        }
        else {
            double d0 = this.taskOwner.getDistanceSq(this.parentAnimal);
            return d0 >= 9.0D && d0 <= 256.0D;
        }
    }

    public void startExecuting()
    {
        this.delayCounter = 0;
    }

    public void resetTask()
    {
        this.parentAnimal = null;
    }

    public void tick() {
        if (--this.delayCounter <= 0) {
            this.delayCounter = 10;
            this.taskOwner.getNavigator().tryMoveToEntityLiving(this.parentAnimal, this.moveSpeed);
        }
    }
}
