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
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.SmartMeleeAttackGoal;
import untamedwilds.entity.ai.SmartWanderGoal;
import untamedwilds.entity.ai.control.look.SmartSwimmerLookControl;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModSounds;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class EntityOpossum extends ComplexMobTerrestrial implements ISpecies, INewSkins {

    private static final EntityDataAccessor<Boolean> PLAYING_DEAD = SynchedEntityData.defineId(EntityOpossum.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> JOEYS = SynchedEntityData.defineId(EntityOpossum.class, EntityDataSerializers.INT);

    public static Animation IDLE_SCRATCH;
    public static Animation THREAT_BACK_OFF;

    // TODO: Some kind of chest raiding
    // TODO: Ain't coding climbing AI
    public EntityOpossum(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
        //this.moveControl = new SmartSwimmingMoveControl(this, 40, 5, 0.25F, 0.3F, true);
        this.lookControl = new SmartSwimmerLookControl(this, 20);
        IDLE_SCRATCH = Animation.create(80);
        THREAT_BACK_OFF = Animation.create(30);
        this.ticksToSit = 20;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PLAYING_DEAD, false);
        this.entityData.define(JOEYS, 0);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.14D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.0D);
    }

    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 2.0D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(2, new OpossumBackOffGoal<>(this, LivingEntity.class, 4, 1D, 1.1D, input -> getEcoLevel(input) > getEcoLevel(this)));
        this.goalSelector.addGoal(3, new SmartWanderGoal(this, 1D, true));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false, input -> getEcoLevel(input) <= getEcoLevel(this)));
    }

    public void aiStep() {
        super.aiStep();

        if (!this.level.isClientSide) {
            if (this.isInWater()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.003D, 0.0D));
                if (!this.isNotMoving() && this.random.nextInt(5) == 0 && this.getDeltaMovement().horizontalDistance() > 0.08) {
                    Vec3 testpos = this.position().add(Math.cos(Math.toRadians(this.getYRot()+ 90)) * -0.8, 0, Math.sin(Math.toRadians(this.getYRot() + 90)) * -0.8);
                    BlockPos testblockpos = new BlockPos(testpos);
                    if (level.getBlockState(new BlockPos(testblockpos.below())).is(BlockTags.MINEABLE_WITH_SHOVEL))
                        ((ServerLevel)this.level).sendParticles(new BlockParticleOption(ParticleTypes.FALLING_DUST, this.level.getBlockState(testblockpos.below())), testpos.x, testpos.y + 0.2, testpos.z, 2, random.nextFloat() * 0.2, random.nextFloat() * 0.2, random.nextFloat() * 0.2, 0);
                    else
                        ((ServerLevel)this.level).sendParticles(ParticleTypes.UNDERWATER, testpos.x, testpos.y + 0.2, testpos.z, 2, random.nextFloat() * 0.2, random.nextFloat() * 0.2, random.nextFloat() * 0.2, 0);
                }
            }
            if (this.isPlayingDead() && this.forceSleep == 0) {
                this.setPlayingDead(false);
            }

            if (this.level.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
                if (this.getJoeys() > 0 && this.random.nextInt(2) == 0) {
                    EntityOpossum child = this.getBreedOffspring((ServerLevel) this.level, this);
                    if (child != null) {
                        child.setVariant(this.getVariant());
                        child.setAge(this.getAdulthoodTime() * -1);
                        child.setGender(this.random.nextInt(2));
                        child.setRandomMobSize();
                        child.setBaby(true);
                        child.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
                        if (this.getOwner() != null)
                            child.tame((Player) this.getOwner());
                        if (this instanceof INeedsPostUpdate) {
                            ((INeedsPostUpdate) child).updateAttributes();
                        }
                        if (TEXTURES_COMMON.containsKey(child.getType().builtInRegistryHolder().key().location().getPath())) {
                            chooseSkinForSpecies(child, true);
                        }
                        //((ServerLevel)this.level).addFreshEntityWithPassengers(child);
                        this.level.addFreshEntity(child);
                        this.level.broadcastEntityEvent(this, (byte)18);
                        this.setJoeys(this.getJoeys() - 1);
                    }
                }
            }
            if (this.getAnimation() == NO_ANIMATION && this.getTarget() == null && !this.isSleeping()) {
                int i = this.random.nextInt(3000);
                if (i <= 8 && !this.isInWater()) {
                    this.getNavigation().stop();
                    this.setSitting(true);
                }
                else if ((i <= 12 || this.isInWater()) && this.isSitting()) {
                    this.setSitting(false);
                }
                else if (i > 2980) {
                    this.setAnimation(IDLE_SCRATCH);
                }
            }

            if (this.getAnimation() == THREAT_BACK_OFF) {
                this.getMoveControl().strafe(-0.5F, 0);
            }
        }
    }

    /* Breeding conditions for the giant salamander are:
     * A nearby Opossum of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getJoeys() == 0 && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
                List<EntityOpossum> list = this.level.getEntitiesOfClass(EntityOpossum.class, this.getBoundingBox().inflate(6.0D, 4.0D, 6.0D));
                list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
                return list.size() >= 1;
            }
        }
        return false;
    }

    public <T extends ComplexMob> void breed() {
        this.setJoeys(this.random.nextInt(3) + 1);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (itemstack.isEmpty() && this.isAlive() && hand.equals(InteractionHand.MAIN_HAND)) {
            this.setAnimation(THREAT_BACK_OFF);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Nullable
    public EntityOpossum getBreedOffspring(ServerLevel serverWorld, AgeableMob ageable) {
        return create_offspring(new EntityOpossum(ModEntity.OPOSSUM.get(), this.level));
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        if (!this.isPlayingDead()) {
            if (damageSource.getEntity() instanceof Mob aggressor) {
                // TODO: null target doesn't work
                aggressor.setTarget(null);
                this.makeAreaOfEffectCloud(this.level, this.blockPosition());
                this.setPlayingDead(true);
                this.forceSleep = 600;
            }
        }
        return super.hurt(damageSource, amount);
    }

    public boolean canBeAffected(MobEffectInstance potionEffectIn) {
        return potionEffectIn.getEffect() != MobEffects.POISON && super.canBeAffected(potionEffectIn);
    }

    private void makeAreaOfEffectCloud(Level worldIn, BlockPos pos) {
        AreaEffectCloud linger_cloud = new AreaEffectCloud(worldIn, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);

        linger_cloud.setRadius(3.0F);
        linger_cloud.setRadiusOnUse(-0.2F);
        linger_cloud.setWaitTime(10);
        linger_cloud.setRadiusPerTick(-linger_cloud.getRadius() / ((float)linger_cloud.getDuration() * 0.2F));
        linger_cloud.setFixedColor(5599028);
        linger_cloud.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0, true, false));

        worldIn.addFreshEntity(linger_cloud);
    }

    public Animation[] getAnimations() { return new Animation[]{NO_ANIMATION, IDLE_SCRATCH, THREAT_BACK_OFF}; }

    public boolean canBeTargeted() { return !this.isPlayingDead(); }
    public boolean isPlayingDead(){ return (this.entityData.get(PLAYING_DEAD)); }
    private void setPlayingDead(boolean short_snout){ this.entityData.set(PLAYING_DEAD, short_snout); }
    public int getJoeys(){ return (this.entityData.get(JOEYS)); }
    private void setJoeys(int joeys){ this.entityData.set(JOEYS, joeys); }

    public void addAdditionalSaveData(CompoundTag compound){
        super.addAdditionalSaveData(compound);
        compound.putBoolean("playing_dead", this.isPlayingDead());
        compound.putInt("joeys", this.getJoeys());
    }

    public void readAdditionalSaveData(CompoundTag compound){
        super.readAdditionalSaveData(compound);
        this.setPlayingDead(compound.getBoolean("playing_dead"));
        this.setJoeys(compound.getInt("joeys"));
    }

    public static class OpossumBackOffGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {

        protected EntityOpossum taskOwner;
        protected final float avoidDistance;
        private final TargetingConditions builtTargetSelector;

        public OpossumBackOffGoal(EntityOpossum entityIn, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn, final Predicate<LivingEntity> targetSelector) {
            super(entityIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
            this.taskOwner = entityIn;
            this.avoidDistance = avoidDistanceIn;
            this.builtTargetSelector = TargetingConditions.forCombat().range(avoidDistanceIn).selector(targetSelector);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.taskOwner.tickCount % 4 != 0) {
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
                return this.toAvoid != null;
            }
        }

        public boolean canContinueToUse() {
            return this.taskOwner.getAnimation() == NO_ANIMATION;
        }

        @Override
        public void start() {
            if (!this.taskOwner.getNavigation().isDone())
                this.taskOwner.getNavigation().stop();
            if (this.taskOwner.isSitting())
                this.taskOwner.setSitting(false);
            this.taskOwner.playSound(ModSounds.ENTITY_OPOSSUM_HISS, 1.0F, this.taskOwner.getVoicePitch());
            this.taskOwner.getLookControl().setLookAt(this.toAvoid);
            this.taskOwner.setAnimation(THREAT_BACK_OFF);
        }

        public void stop() {
            this.toAvoid = null;
        }

        public void tick() {
            if (this.taskOwner.getAnimation() == NO_ANIMATION)
                this.taskOwner.setAnimation(THREAT_BACK_OFF);
            this.taskOwner.lookAt(this.toAvoid, 30.0F, 30.0F);;
        }
    }
}