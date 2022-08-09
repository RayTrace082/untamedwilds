package untamedwilds.entity.ai.target;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobTerrestrial;
import untamedwilds.entity.ISpecies;
import untamedwilds.util.EntityUtils;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class AngrySleeperTarget<T extends LivingEntity> extends TargetGoal {
    protected final Class<T> targetClass;
    protected final int targetChance;
    protected LivingEntity target;
    protected ComplexMobTerrestrial taskOwner;
    protected Predicate<T> targetEntitySelector;
    private int runningTicks;

    public AngrySleeperTarget(ComplexMobTerrestrial entityIn, Class<T> targetClassIn, boolean checkSight) {
        this(entityIn, targetClassIn, checkSight, false);
    }

    public AngrySleeperTarget(ComplexMobTerrestrial entityIn, Class<T> targetClassIn, boolean checkSight, boolean nearbyOnlyIn) {
        this(entityIn, targetClassIn, 4, checkSight, nearbyOnlyIn);
    }

    public AngrySleeperTarget(ComplexMobTerrestrial entityIn, Class<T> targetClassIn, int targetChanceIn, boolean checkSight, boolean nearbyOnlyIn) {
        super(entityIn, checkSight, nearbyOnlyIn);
        this.targetClass = targetClassIn;
        this.targetChance = targetChanceIn;
        this.setFlags(EnumSet.of(Flag.TARGET));
        this.runningTicks = 1000;
        this.taskOwner = entityIn;
        this.targetEntitySelector = entity -> {
            if (entity instanceof Creeper || ComplexMob.getEcoLevel(this.taskOwner) > ComplexMob.getEcoLevel(entity) * 2) {
                return false;
            }
            if (this.taskOwner.getClass() == entity.getClass()) {
                if (this.taskOwner instanceof ISpecies && entity instanceof ISpecies) {
                    ComplexMob attacker = this.taskOwner;
                    ComplexMob defender = ((ComplexMob)entity);
                    if (attacker.getVariant() == defender.getVariant()) {
                        return false;
                    }
                }
            }
            if (entity instanceof Player player) {
                if (player.isSteppingCarefully() || player.isCreative() || player.isSpectator())
                    return false;
            }
            return TargetingConditions.forCombat().test(this.taskOwner, entity) && this.canAttack(entity, TargetingConditions.DEFAULT);
        };
    }

    public boolean canUse() {
        if (!ConfigGamerules.angrySleepers.get() || !this.taskOwner.isSleeping() || this.taskOwner.isTame() || this.taskOwner.forceSleep != 0) {
            return false;
        }
        List<LivingEntity> list = this.mob.level.getEntitiesOfClass(LivingEntity.class, this.mob.getBoundingBox().inflate(6.0D, 4.0D, 6.0D), (input) -> this.targetEntitySelector.test((T) input));
        if (!list.isEmpty()) {
            LivingEntity player = list.get(0);
            this.taskOwner.setSleeping(false);
            this.target = player;
        }
        return true;
    }

    public void start() {
        this.taskOwner.setTarget(this.target);
        this.taskOwner.forceSleep = -300;
        super.start();
    }

    public boolean canContinueToUse() {
        this.runningTicks--;
        if (this.runningTicks < 1)
            return false;
        return super.canContinueToUse();
    }
}