package untamedwilds.entity.mammal;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.target.AngrySleeperTarget;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModLootTables;
import untamedwilds.init.ModSounds;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class EntityBoar extends ComplexMobTerrestrial implements ISpecies, INewSkins, INeedsPostUpdate {

    private static final EntityDataAccessor<Boolean> WARTHOG = SynchedEntityData.defineId(EntityBoar.class, EntityDataSerializers.BOOLEAN);

    private BlockPos lastDugPos = null;

    public static Animation WORK_DIG;
    public static Animation ATTACK;
    public static Animation TALK;

    public EntityBoar(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
        this.entityData.define(WARTHOG, false);
        this.turn_speed = 0.6F;
        WORK_DIG = Animation.create(48);
        ATTACK = Animation.create(18);
        TALK = Animation.create(20);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal_Land(this));
        this.goalSelector.addGoal(2, new GoHogWildGoal(this, 2D));
        this.goalSelector.addGoal(2, new FindItemsGoal(this, 12, 100, false, true));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 2D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> getEcoLevel(input) > getEcoLevel(this)));
        this.goalSelector.addGoal(3, new GotoSleepGoal(this, 1));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, 120, 20, true));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
        this.targetSelector.addGoal(4, new AngrySleeperTarget<>(this, LivingEntity.class, true));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.8D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D)
                .add(Attributes.ARMOR, 2D);
    }

    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            return !this.isSleeping() && this.getAge() == 0 && EntityUtils.hasFullHealth(this) && this.getHunger() >= 80;
        }
        return false;
    }

    @Override
    public void aiStep() {
        if (!this.level.isClientSide) {
            this.setAngry(this.getTarget() != null);
            if (this.level.getGameTime() % 1000 == 0) {
                this.addHunger(-10);
                if (!this.isStarving()) {
                    this.heal(1.0F);
                }
            }
            // Random idle animations
            if (this.getAnimation() == NO_ANIMATION && this.getTarget() == null && !this.isSleeping() && this.getCommandInt() == 0) {
                int i = this.random.nextInt(3000);
                if (i == 13 && !this.isInWater() && this.isNotMoving() && this.canMove()) {
                    this.setSitting(true);
                }
                if (i == 14 && this.isSitting()) {
                    this.setSitting(false);
                }
                if (i > 2960 && i < 2979 && !this.isInWater() && !this.isBaby()) {
                    this.setAnimation(TALK);
                }
                if (i > 2980 && !this.isInWater() && this.getHunger() < 60 && this.canMove() && this.getAnimation() == NO_ANIMATION) {
                    if ((this.lastDugPos == null || this.distanceToSqr(this.lastDugPos.getX(), this.getY(), this.lastDugPos.getZ()) > 50) && this.level.getBlockState(this.blockPosition().below()).is(BlockTags.MINEABLE_WITH_SHOVEL)) {
                        this.setAnimation(WORK_DIG);
                        this.addHunger(20);
                        this.lastDugPos = this.blockPosition();
                    }
                }
            }
            if (this.getAnimation() == WORK_DIG && this.getAnimationTick() % 8 == 0) {
                ((ServerLevel)this.level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.level.getBlockState(this.blockPosition().below())), this.getX(), this.getY(), this.getZ(), 20, 0.0D, 0.0D, 0.0D, 0.15F);
                this.playSound(SoundEvents.SHOVEL_FLATTEN, 0.8F, 0.6F);
                if (this.getAnimationTick() == 64 && this.random.nextInt(5) == 0) {
                    List<ItemStack> result = EntityUtils.getItemFromLootTable(ModLootTables.LOOT_DIGGING, this.level);
                    for (ItemStack itemstack : result)
                        this.spawnAtLocation(itemstack);
                }
            }
            if (this.getAnimation() == TALK && this.getAnimationTick() == 1 && this.getAmbientSound() != null) {
                this.playSound(this.getAmbientSound(), this.getSoundVolume(), this.getVoicePitch());
            }
            if (this.getTarget() != null && this.tickCount % 120 == 0) {
                this.playSound(this.getThreatSound(), this.getSoundVolume(), this.getVoicePitch());
            }
            if (this.getAnimation() != NO_ANIMATION) {
                if (this.getAnimation() == ATTACK && this.getAnimationTick() == 8 && this.random.nextInt(3) == 0) {
                    this.playSound(ModSounds.ENTITY_BOAR_SQUEAL, this.getSoundVolume(), this.getVoicePitch());
                }
            }
        }
        super.aiStep();
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (hand == InteractionHand.MAIN_HAND && !this.level.isClientSide()) {
            if (!this.isTame() && this.isBaby() && EntityUtils.hasFullHealth(this) && this.isFood(itemstack)) {
                this.playSound(SoundEvents.HORSE_EAT, 1.5F, 0.8F);
                if (this.getRandom().nextInt(3) == 0) {
                    this.tame(player);
                    EntityUtils.spawnParticlesOnEntity(this.level, this, ParticleTypes.HEART, 3, 6);
                } else {
                    EntityUtils.spawnParticlesOnEntity(this.level, this, ParticleTypes.SMOKE, 3, 3);
                }
            }
            if (!this.isTame() && !this.isBaby() && itemstack.isEmpty()) {
                this.setSitting(false);
                this.setSleeping(false);
                if (!this.level.isClientSide) {
                    player.setYRot(this.getYRot());
                    player.setXRot(this.getXRot());
                    player.startRiding(this);
                }
            }
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag && this.getAnimation() == NO_ANIMATION && !this.isBaby()) {
            this.setAnimation(ATTACK);
            this.setAnimationTick(0);
        }
        return flag;
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        // Retaliate I: Mob will strike back when attacked by its current target
        performRetaliation(damageSource, this.getHealth(), amount, true);
        return super.hurt(damageSource, amount);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, WORK_DIG, ATTACK, TALK};
    }

    public Animation getAnimationEat() { return WORK_DIG; }

    @Nullable
    public EntityBoar getBreedOffspring(ServerLevel serverWorld, AgeableMob ageable) {
        return create_offspring(new EntityBoar(ModEntity.BOAR.get(), this.level));
    }

    @Override
    public void updateAttributes() {
        this.setWarthog(getEntityData(this.getType()).getFlags(this.getVariant(), "isWarthog") == 1);
    }

    public boolean isWarthog(){ return (this.entityData.get(WARTHOG)); }
    private void setWarthog(boolean warthog){ this.entityData.set(WARTHOG, warthog); }

    public void addAdditionalSaveData(CompoundTag compound){
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isWarthog", this.isWarthog());
        if (this.lastDugPos != null) {
            compound.putInt("DugPosX", this.lastDugPos.getX());
            compound.putInt("DugPosZ", this.lastDugPos.getZ());
        }
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setWarthog(compound.getBoolean("isWarthog"));
        if (compound.contains("LastDugPos")) {
            this.lastDugPos = new BlockPos(compound.getInt("DugPosX"), 0, compound.getInt("DugPosZ"));
        }
    }

    public static class GoHogWildGoal extends Goal {
        private final EntityBoar boar;
        private final double speedModifier;
        private double posX;
        private double posY;
        private double posZ;

        public GoHogWildGoal(EntityBoar p_25890_, double p_25891_) {
            this.boar = p_25890_;
            this.speedModifier = p_25891_;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (!this.boar.isTame() && this.boar.isVehicle()) {
                Vec3 vec3 = DefaultRandomPos.getPos(this.boar, 5, 4);
                if (vec3 == null) {
                    return false;
                } else {
                    this.posX = vec3.x;
                    this.posY = vec3.y;
                    this.posZ = vec3.z;
                    return true;
                }
            } else {
                return false;
            }
        }

        public void start() {
            this.boar.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
        }

        public boolean canContinueToUse() {
            return !this.boar.isTame() && !this.boar.getNavigation().isDone() && this.boar.isVehicle();
        }

        public void tick() {
            if (this.boar.getRandom().nextInt(this.adjustedTickDelay(50)) < 3) {
                this.boar.setAnimation(ATTACK);
                this.boar.playSound(ModSounds.ENTITY_BOAR_SQUEAL, this.boar.getSoundVolume(), this.boar.getVoicePitch());
            }
            if (!this.boar.isTame() && this.boar.getRandom().nextInt(this.adjustedTickDelay(50)) == 0) {
                Entity entity = this.boar.getPassengers().get(0);
                if (entity == null) {
                    return;
                }

                this.boar.ejectPassengers();
                this.boar.setTarget((LivingEntity) entity);
                this.boar.level.broadcastEntityEvent(this.boar, (byte)6);
            }
        }
    }
}
