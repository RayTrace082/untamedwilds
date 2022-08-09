package untamedwilds.entity.ai.unique;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.mammal.EntityBaleenWhale;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BaleenWhaleFeedGoal extends Goal {

    private final EntityBaleenWhale taskOwner;
    private final int chance;
    private BlockPos targetPos;
    private int eatingCounter;

    public BaleenWhaleFeedGoal(EntityBaleenWhale entityIn, int chance) {
        this.taskOwner = entityIn;
        this.chance = chance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        if (this.taskOwner.getRandom().nextInt(this.chance) == 0) {
            this.targetPos = getPosition();
            return this.targetPos != null;
        }
        return false;
    }

    @Nullable
    protected BlockPos getPosition() {
        Vec3 vector3d = BehaviorUtils.getRandomSwimmablePos(this.taskOwner, 20, 7);

        for(int i = 0; vector3d != null && !this.taskOwner.level.getBlockState(new BlockPos(vector3d)).isPathfindable(this.taskOwner.level, new BlockPos(vector3d), PathComputationType.WATER) && i++ < 10; vector3d = BehaviorUtils.getRandomSwimmablePos(this.taskOwner, 20, 7)) {
        }

        if (vector3d != null && this.taskOwner.level.canSeeSky(this.taskOwner.blockPosition())) {
            int offset = 5 + this.taskOwner.getRandom().nextInt(7) - 4;
            return new BlockPos(vector3d.x(), this.taskOwner.level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int)vector3d.x(), (int)vector3d.z()) + offset, vector3d.z());
        }
        if (vector3d != null) {
            return new BlockPos(vector3d);
        }
        return null;
    }

    public boolean canContinueToUse() {
        //UntamedWilds.LOGGER.info((!this.taskOwner.getNavigation().isDone()) + " " + (this.eatingCounter != 0));
        return !this.taskOwner.getNavigation().isDone() && this.eatingCounter != 0;
    }

    public void start() {
        this.eatingCounter = 200;
        this.taskOwner.setFeeding(true);
        this.taskOwner.getNavigation().moveTo(this.targetPos.getX() + 0.5, this.targetPos.above().getY(), this.targetPos.getZ() + 0.5, 1.5);
    }

    public void stop() {
        //UntamedWilds.LOGGER.info("STOPPING");
        if (this.taskOwner.isFeeding())
            this.taskOwner.setFeeding(false);
        super.stop();
    }

    public void tick() {
        //UntamedWilds.LOGGER.info(this.sinkingCounter);
        if (this.eatingCounter > 0)
            this.eatingCounter--;
        //this.eatingCounter = this.taskOwner.getRandom().nextInt(200) + 200;
    }

    public boolean isInterruptable() {
        return false;
    }
}