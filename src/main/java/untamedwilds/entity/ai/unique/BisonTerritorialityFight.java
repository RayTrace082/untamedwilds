package untamedwilds.entity.ai.unique;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;
import untamedwilds.entity.mammal.EntityBison;
import untamedwilds.util.EntityUtils;

import java.util.EnumSet;
import java.util.List;

public class BisonTerritorialityFight extends Goal {
    private final EntityBison taskOwner;
    private EntityBison targetAnimal;
    private final double moveSpeed;
    private int charge;
    private boolean isDone = false;
    private Goal slaveGoal;

    public BisonTerritorialityFight(EntityBison entityIn, double speedIn) {
        this.taskOwner = entityIn;
        this.moveSpeed = speedIn;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!EntityUtils.hasFullHealth(this.taskOwner) || this.taskOwner.isBaby() || !this.taskOwner.canMove() || !this.taskOwner.isMale()) {
            return false;
        }
        if (this.taskOwner.getRandom().nextInt(400) != 0)
            return false;
        List<? extends EntityBison> list = this.taskOwner.level.getEntitiesOfClass(this.taskOwner.getClass(), this.taskOwner.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
        EntityBison target_animal = null;
        double d0 = Double.MAX_VALUE;

        for (EntityBison potential_targets : list) {
            if (!potential_targets.equals(this.taskOwner) && !potential_targets.isBaby() && (potential_targets.getVariant() == this.taskOwner.getVariant()) && potential_targets.isMale() && potential_targets.canMove()) {
                double d1 = this.taskOwner.distanceToSqr(potential_targets);
                if (d1 <= d0) {
                    d0 = d1;
                    target_animal = potential_targets;
                }
            }
        }

        if (target_animal == null) {
            return false;
        } else {
            this.targetAnimal = target_animal;
            this.isDone = false;
            this.slaveGoal = new TerritorialFightingGoal_slave(this.targetAnimal, 1.4F, this.taskOwner);
            return true;
        }
    }

    @Override
    public void start() {
        EntityUtils.spawnParticlesOnEntity(this.taskOwner.level, this.taskOwner, ParticleTypes.ANGRY_VILLAGER, 10, 1);
        EntityUtils.spawnParticlesOnEntity(this.targetAnimal.level, this.targetAnimal, ParticleTypes.ANGRY_VILLAGER, 10, 1);
        this.charge = 40;
        this.targetAnimal.goalSelector.addGoal(1, this.slaveGoal);
        this.taskOwner.getLookControl().setLookAt(this.targetAnimal);
        this.taskOwner.setAnimation(EntityBison.ATTACK_THREATEN);
        this.taskOwner.setSprinting(true);
        if (!this.targetAnimal.canMove()) {
            this.targetAnimal.setSitting(false);
            this.targetAnimal.setSleeping(false);
        }
    }

    public boolean canContinueToUse() {
        return !this.isDone && EntityUtils.hasFullHealth(this.taskOwner);
    }

    public void tick() {
        this.taskOwner.getLookControl().setLookAt(this.targetAnimal);
        if (charge > 0) {
            if (--charge == 0) {
                this.taskOwner.getNavigation().moveTo(this.targetAnimal, this.moveSpeed * 1.2F);
            }
        } else { // AABB checking
            AABB offset_box = this.taskOwner.getBoundingBox().move(Math.cos(Math.toRadians(this.taskOwner.getYRot() + 90)) * 1.2, 0, Math.sin(Math.toRadians(this.taskOwner.getYRot() + 90)) * 1.2);
            List<LivingEntity> entitiesHit = this.taskOwner.getLevel().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), this.taskOwner, offset_box);
            for (LivingEntity entityHit : entitiesHit) {
                if (!entityHit.equals(this.taskOwner) && this.taskOwner.hasLineOfSight(entityHit)) {
                    if (entityHit.equals(this.targetAnimal)) {
                        RandomSource rand = this.taskOwner.getRandom();
                        ServerLevel world = (ServerLevel) this.taskOwner.getLevel();
                        world.sendParticles(ParticleTypes.SMOKE,
                            rand.nextDouble() * (offset_box.maxX - offset_box.minX) + offset_box.minX,
                            rand.nextDouble() * (offset_box.maxY - offset_box.minY) + offset_box.minX,
                            rand.nextDouble() * (offset_box.maxZ - offset_box.minZ) + offset_box.minZ, 10, 0, 0, 0, 0.05);
                        this.targetAnimal.hurt(DamageSource.GENERIC, 2);
                        this.isDone = true;
                    }
                }
            }
        }
    }

    public void stop() {
        this.targetAnimal.goalSelector.removeGoal(this.slaveGoal);
        this.charge = 0;
        this.taskOwner.setSprinting(false);
        this.taskOwner.level.playSound(null, this.taskOwner.getX(), this.taskOwner.getY(), this.taskOwner.getZ(), SoundEvents.GOAT_RAM_IMPACT, SoundSource.NEUTRAL, 1.0F, 1.0F);
        this.taskOwner.setDeltaMovement(this.taskOwner.getDeltaMovement().scale(-0.15F));
    }

    public static class TerritorialFightingGoal_slave extends Goal {
        private final EntityBison taskOwner;
        private final EntityBison targetAnimal;
        private final double moveSpeed;
        private int charge;
        private boolean isDone = false;

        public TerritorialFightingGoal_slave(EntityBison entityIn, double speedIn, EntityBison targetAnimal) {
            this.taskOwner = entityIn;
            this.moveSpeed = speedIn;
            this.targetAnimal = targetAnimal;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return !this.taskOwner.isBaby() && this.taskOwner.canMove() && this.taskOwner.isMale();
        }

        @Override
        public void start() {
            this.charge = 40;
            this.isDone = false;
            this.taskOwner.getLookControl().setLookAt(this.targetAnimal);
            this.taskOwner.setAnimation(EntityBison.ATTACK_THREATEN);
            this.taskOwner.setSprinting(true);
            if (!this.targetAnimal.canMove()) {
                this.targetAnimal.setSitting(false);
                this.targetAnimal.setSleeping(false);
            }
        }

        public boolean canContinueToUse() {
            return !this.isDone;
        }

        public void tick() {
            this.taskOwner.getLookControl().setLookAt(this.targetAnimal);
            if (charge > 0) {
                if (--charge == 0) {
                    this.taskOwner.getNavigation().moveTo(this.targetAnimal, this.moveSpeed * 1.2F);
                }
            } else { // AABB checking
                AABB offset_box = this.taskOwner.getBoundingBox().move(Math.cos(Math.toRadians(this.taskOwner.getYRot() + 90)) * 1.2, 0, Math.sin(Math.toRadians(this.taskOwner.getYRot() + 90)) * 1.2);
                List<LivingEntity> entitiesHit = this.taskOwner.getLevel().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), this.taskOwner, offset_box);
                for (LivingEntity entityHit : entitiesHit) {
                    if (!entityHit.equals(this.taskOwner) && this.taskOwner.hasLineOfSight(entityHit)) {
                        if (entityHit.equals(this.targetAnimal)) {
                            RandomSource rand = this.taskOwner.getRandom();
                            ServerLevel world = (ServerLevel) this.taskOwner.getLevel();
                            world.sendParticles(ParticleTypes.SMOKE,
                                rand.nextDouble() * (offset_box.maxX - offset_box.minX) + offset_box.minX,
                                rand.nextDouble() * (offset_box.maxY - offset_box.minY) + offset_box.minX,
                                rand.nextDouble() * (offset_box.maxZ - offset_box.minZ) + offset_box.minZ,
                                10, 0, 0, 0, 0.05);
                            this.targetAnimal.hurt(DamageSource.mobAttack(null), 2);
                            this.isDone = true;
                        }
                    }
                }
            }
        }

        public void stop() {
            this.charge = 0;
            this.taskOwner.setSprinting(false);
            this.taskOwner.setDeltaMovement(this.taskOwner.getDeltaMovement().scale(-0.15F));
        }
    }
}