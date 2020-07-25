package untamedwilds.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;

import java.util.EnumSet;
import java.util.List;

public class SmartMateGoal extends Goal {
    private final ComplexMob taskOwner;
    private World world;
    private final Class<? extends ComplexMob> mateClass;
    private ComplexMob targetMate;
    private int spawnBabyDelay;
    private double moveSpeed;

    public SmartMateGoal(ComplexMob taskOwner, double speedIn) {
        this(taskOwner, speedIn, taskOwner.getClass());
    }

    private SmartMateGoal(ComplexMob taskOwner, double speedIn, Class<? extends ComplexMob> mateClass) {
        this.taskOwner = taskOwner;
        this.world = taskOwner.world;
        this.mateClass = mateClass;
        this.moveSpeed = speedIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean shouldExecute() {
        if (!this.taskOwner.isInLove() || this.taskOwner.getGrowingAge() != 0) {
            return false;
        } else {
            this.targetMate = this.getNearbyMate();
            return this.targetMate != null;
        }
    }

    public boolean shouldContinueExecuting() {
        return this.targetMate.isAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 200;
    }

    public void resetTask() {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }

    public void tick() {
        this.taskOwner.getLookController().setLookPositionWithEntity(this.targetMate, 10.0F, (float) this.taskOwner.getVerticalFaceSpeed());
        this.taskOwner.getNavigator().tryMoveToXYZ(this.targetMate.getPosX(), this.targetMate.getPosY(), this.targetMate.getPosZ(), this.moveSpeed);
        ++this.spawnBabyDelay;

        if (this.spawnBabyDelay >= 100 && this.taskOwner.getDistanceSq(this.targetMate) < 9.0D) {
            this.taskOwner.resetInLove();
            this.targetMate.resetInLove();
            if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                this.world.addEntity(new ExperienceOrbEntity(this.world, this.taskOwner.getPosX(), this.taskOwner.getPosY(), this.taskOwner.getPosZ(), this.taskOwner.getRNG().nextInt(7) + 1));
            }
            // Positive Growing Age is used as pregnancy counter (only works for females)
            this.taskOwner.setGrowingAge(this.taskOwner.getPregnancyTime());
            this.targetMate.setGrowingAge(this.taskOwner.getPregnancyTime());
            if (!ConfigGamerules.easyBreeding.get()) {
                if (!this.taskOwner.isMale()) {
                    this.taskOwner.breed();
                } else {
                    this.targetMate.breed();
                }
            }
        }
    }

    private ComplexMob getNearbyMate() {
        List<ComplexMob> list = this.world.getEntitiesWithinAABB(mateClass, this.taskOwner.getBoundingBox().grow(8.0D));
        list.remove(this.taskOwner);
        double d0 = Double.MAX_VALUE;
        ComplexMob entityanimal = null;
        for (ComplexMob potentialMates : list) {
            if (canMateWith(this.taskOwner, potentialMates) && this.taskOwner.getDistanceSq(potentialMates) < d0) {
                entityanimal = potentialMates;
                d0 = this.taskOwner.getDistanceSq(potentialMates);
            }
        }
        return entityanimal;
    }

    private boolean canMateWith(ComplexMob father, ComplexMob mother) {
        if (father.getGender() == mother.getGender()) {
            return false;
        }
        else if (father.getSpecies() != mother.getSpecies()) {
            return false;
        }
        else {
            return father.wantsToBreed() && mother.wantsToBreed();
        }
    }
}