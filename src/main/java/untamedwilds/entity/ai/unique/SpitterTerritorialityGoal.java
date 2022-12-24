package untamedwilds.entity.ai.unique;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.entity.relict.EntitySpitter;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class SpitterTerritorialityGoal<T extends LivingEntity> extends HuntMobTarget<T> {

    private final int executionChance;
    private final float threshold;

    public SpitterTerritorialityGoal(ComplexMob creature, Class<T> classTarget, boolean checkSight) {
        this(creature, classTarget, 300, checkSight, 0, null);
    }

    public SpitterTerritorialityGoal(ComplexMob creature, Class<T> classTarget, int chance, boolean checkSight, float threshold, final Predicate<LivingEntity> targetSelector) {
        super(creature, classTarget, checkSight,200, false, targetSelector);
        this.executionChance = chance;
        this.threshold = threshold;
    }

    protected boolean isValidTarget(LivingEntity entity, @Nullable Predicate<LivingEntity> predicate) {
        if (!(entity instanceof EntitySpitter) || ((EntitySpitter) entity).getGender() != ((EntitySpitter) this.mob).getGender() || entity.isBaby() || entity.equals(this.mob) || ((EntitySpitter) this.mob).isTame() || (predicate != null && !predicate.test(entity)) || entity.getHealth() / entity.getMaxHealth() < threshold) {
            return false;
        }
        return canAttack(entity, TargetingConditions.forCombat().range(getFollowDistance()));
    }

    @Override
    public boolean canUse() {
        if (this.mob.isBaby() || this.mob.getRandom().nextInt(this.executionChance) != 0) {
            return false;
        }
        List<T> list = this.mob.level.getEntitiesOfClass(this.targetClass, this.mob.getBoundingBox().inflate(this.getFollowDistance(), 12.0D, this.getFollowDistance()), this.targetEntitySelector);
        if (list.isEmpty())
            return false;

        list.sort(this.sorter);
        this.targetMob = list.get(0);
        return true;
    }
}