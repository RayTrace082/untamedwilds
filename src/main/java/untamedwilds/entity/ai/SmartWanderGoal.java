package untamedwilds.entity.ai;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;
import untamedwilds.entity.ComplexMobTerrestrial;

import java.util.EnumSet;

public class SmartWanderGoal extends Goal {
    private final ComplexMobTerrestrial creature;
    protected double x;
    protected double y;
    protected double z;
    private final int runChance;
    private final int executionChance;
    private final boolean avoidWater;
    private final double speed;

    public SmartWanderGoal(ComplexMobTerrestrial creatureIn, double speedIn) {
        this(creatureIn, speedIn, 120, 0,false);
    }

    public SmartWanderGoal(ComplexMobTerrestrial creatureIn, double speedIn, boolean avoidWater) {
        this(creatureIn, speedIn, 120, 0, avoidWater);
    }

    public SmartWanderGoal(ComplexMobTerrestrial creatureIn, double speedIn, int runChance, boolean avoidWater) {
        this(creatureIn, speedIn, 120, runChance, avoidWater);
    }

    public SmartWanderGoal(ComplexMobTerrestrial creatureIn, double speedIn, int chance, int runChance, boolean avoidWater) {
        this.creature = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.runChance = runChance;
        this.avoidWater = avoidWater;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.creature.isActive() || this.creature.isBeingRidden() || !this.creature.canMove() || !this.creature.getNavigator().noPath() || this.creature.getCommandInt() != 0) {
            return false;
        } else {
            if (this.creature.getRNG().nextInt(this.executionChance) != 0) { return false; }

            Vector3d vec3d;
            vec3d = this.getPosition();
            if (vec3d == null) {
                return false;
            } else {
                this.x = vec3d.x;
                this.y = vec3d.y;
                this.z = vec3d.z;
                return true;
            }
        }
    }

    private Vector3d getPosition() {
        if (this.avoidWater) {
            Vector3d vec3d = RandomPositionGenerator.getLandPos(this.creature, 15, 7);
            return vec3d == null ? RandomPositionGenerator.findRandomTarget(this.creature, 10, 4) : vec3d;
        }
        else {
            return RandomPositionGenerator.findRandomTarget(this.creature, 10, 4);
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.creature.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, this.speed * (this.creature.getRNG().nextInt(100) < this.runChance ? 1.8f : 1f));
    }
}
