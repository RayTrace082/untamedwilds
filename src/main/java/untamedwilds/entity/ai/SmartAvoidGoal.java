package untamedwilds.entity.ai;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class SmartAvoidGoal <T extends LivingEntity> extends AvoidEntityGoal<T> {

    protected ComplexMob taskOwner;
    protected final float avoidDistance;
    private final TargetingConditions builtTargetSelector;

    public SmartAvoidGoal(ComplexMob entityIn, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn, final Predicate<LivingEntity> targetSelector) {
        super(entityIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
        this.taskOwner = entityIn;
        this.avoidDistance = avoidDistanceIn;
        this.builtTargetSelector = TargetingConditions.forCombat().range(avoidDistanceIn).selector(targetSelector);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.taskOwner.tickCount % 40 != 0) {
            return false;
        }
        if (this.taskOwner.getTarget() != null || this.taskOwner.isSleeping() || this.taskOwner.getCommandInt() != 0 || this.taskOwner.isTame()) {
            return false;
        }

        List<T> list = this.taskOwner.level.getNearbyEntities(avoidClass, this.builtTargetSelector, this.taskOwner, this.taskOwner.getBoundingBox().inflate(avoidDistance, 4f, avoidDistance));
        if (list.isEmpty()) {
            return false;
        } else {
            this.toAvoid = list.get(0);
            Vec3 vec3d = DefaultRandomPos.getPosAway(this.taskOwner, 16, 7, new Vec3(this.toAvoid.getX(), this.toAvoid.getY(), this.toAvoid.getZ()));
            if (vec3d == null) {
                return false;
            } else if (this.toAvoid.distanceToSqr(vec3d.x, vec3d.y, vec3d.z) < this.toAvoid.distanceToSqr(this.mob)) {
                return false;
            } else {
                this.path = this.pathNav.createPath(vec3d.x, vec3d.y, vec3d.z, 0);
                return this.path != null;
            }
        }
    }
}