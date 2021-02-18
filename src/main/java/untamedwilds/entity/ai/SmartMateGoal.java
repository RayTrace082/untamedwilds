package untamedwilds.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ISkins;

import java.util.EnumSet;
import java.util.List;

public class SmartMateGoal extends Goal {
    private final ComplexMob taskOwner;
    private final World world;
    private final int executionChance;
    private final Class<? extends ComplexMob> mateClass;
    private ComplexMob targetMate;
    private int spawnBabyDelay;
    private final double moveSpeed;

    public SmartMateGoal(ComplexMob taskOwner, double speedIn) {
        this(taskOwner, speedIn, 120, taskOwner.getClass());
    }

    private SmartMateGoal(ComplexMob taskOwner, double speedIn, int executionChance, Class<? extends ComplexMob> mateClass) {
        this.taskOwner = taskOwner;
        this.world = taskOwner.world;
        this.mateClass = mateClass;
        this.executionChance = executionChance;
        this.moveSpeed = speedIn;
        this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.taskOwner.isInLove() || this.taskOwner.getGrowingAge() != 0) {
            return false;
        }
        if (this.taskOwner.getRNG().nextInt(this.executionChance) != 0) {
            return false;
        }
        else {
            this.targetMate = this.getNearbyMate();
            return this.targetMate != null;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.targetMate.isAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 200;
    }

    @Override
    public void resetTask() {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }

    @Override
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
            // Positive Growing Age is used as pregnancy counter (handled in ComplexMob)
            this.taskOwner.setGrowingAge(this.taskOwner.getPregnancyTime());
            this.targetMate.setGrowingAge(this.taskOwner.getPregnancyTime());
            if (ConfigGamerules.easyBreeding.get()) {
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
        else if (!(father instanceof ISkins) && father.getVariant() != mother.getVariant()) {
            return false;
        }
        else {
            if (ConfigGamerules.playerBreeding.get()) { // Bypass breeding restrictions in the event of a player triggered breeding
                return true;
            }
            return father.wantsToBreed() && mother.wantsToBreed();
        }
    }
}