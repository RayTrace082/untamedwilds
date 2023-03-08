package untamedwilds.entity.ai;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import untamedwilds.entity.ComplexMobAmphibious;
import untamedwilds.entity.ComplexMobTerrestrial;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class GotoSleepGoal extends Goal {

    private final ComplexMobTerrestrial creature;
    protected Vector3f target;
    private final int executionChance;
    private final double speed;
    private final boolean usesHome;

    public GotoSleepGoal(ComplexMobTerrestrial entityIn, double speedIn) {
        this(entityIn, speedIn, 200, true);
    }

    public GotoSleepGoal(ComplexMobTerrestrial entityIn, double speedIn, int chance) {
        this(entityIn, speedIn, chance, true);
    }

    public GotoSleepGoal(ComplexMobTerrestrial entityIn, double speedIn, int chance, boolean usesHome) {
        this.creature = entityIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.usesHome = usesHome;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        // Common checks
        if (this.creature.getRandom().nextInt(this.executionChance) != 0 || this.creature.forceSleep < 0 || this.creature.getTarget() != null || (this.creature.isTame() && this.creature.getCommandInt() != 0)) {
            return false;
        }
        // Wake up checks
        if (this.creature.isSleeping() && (this.creature.forceSleep <= 0 || (!(this.creature instanceof ComplexMobAmphibious) && this.creature.isInWater()))) {
            this.creature.setSleeping(false);
            return false;
        }
        // Valid sleep checks
        if (this.creature.getCommandInt() != 0 || this.creature.isActive() || !this.creature.canMove() || (this.creature.isInWater() && !(this.creature instanceof ComplexMobAmphibious))) {
            return false;
        }
        if (isValidShelter(this.creature.blockPosition()) || !this.usesHome) {
            this.creature.setHome(this.creature.blockPosition());
            this.creature.setSleeping(true);
            return false;
        }
        if (this.creature.getHome() == BlockPos.ZERO || !canEasilyReach(this.creature.getHome()) || this.creature.distanceToSqr(this.creature.getHomeAsVec()) > 100000) {
            this.creature.setHome(BlockPos.ZERO);
            BlockPos pos = this.checkForNewHome();
            if (pos == null) {
                return false;
            } else {
                this.creature.setHome(pos);
            }
        }
        this.target = new Vector3f(this.creature.getHomeAsVec());
        return true;
    }

    private boolean canEasilyReach(BlockPos target) {
        //this.targetSearchDelay = 10 + this.creature.getRandom().nextInt(5);
        Path path = this.creature.getNavigation().createPath(target, 0);
        if (path == null) {
            return false;
        } else {
            Node pathpoint = path.getEndNode();
            if (pathpoint == null) {
                return false;
            } else {
                int i = pathpoint.x - Mth.floor(target.getX());
                int j = pathpoint.z - Mth.floor(target.getZ());
                return (double)(i * i + j * j) <= 2.25D;
            }
        }
    }

    @Override
    public void start() {
        this.creature.getNavigation().moveTo(this.target.x(), this.target.y(), this.target.z(), this.speed);
    }

    @Override
    public boolean canContinueToUse() {
        return !this.creature.getNavigation().isDone();
    }

    @Nullable
    public BlockPos checkForNewHome() {
        Random random = this.creature.getRandom();
        BlockPos blockpos = this.creature.blockPosition();

        for(int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.offset(random.nextInt(12) - 6, random.nextInt(4) - 2, random.nextInt(12) - 6);
            if (isValidShelter(blockpos1) && this.creature.getWalkTargetValue(blockpos1) < 0.0F) {
                return blockpos1;
            }
        }

        return null;
    }

    private boolean isValidShelter(BlockPos blockPos) {
        // We consider a valid shelter a dark location, with Sky Light Level less than 14 (mostly, to prevent mobs sleeping under broad daylight)
        return !this.creature.level.canSeeSky(blockPos);
        //return this.creature.level.getLightFor(LightType.SKY, blockPos) <= 14; // Was 12
    }
}
