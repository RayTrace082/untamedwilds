package untamedwilds.entity.relict;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.target.ProtectChildrenTarget;
import untamedwilds.entity.ai.target.SmartHurtByTargetGoal;
import untamedwilds.entity.ai.target.SmartOwnerHurtTargetGoal;
import untamedwilds.entity.ai.unique.SpitterAttackGoal;
import untamedwilds.entity.ai.unique.SpitterTerritorialityGoal;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModTags;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntitySpitter extends ComplexMobTerrestrial implements ISpecies, INewSkins, INestingMob, INeedsPostUpdate {

    private static final EntityDataAccessor<Boolean> CAN_GROW = SynchedEntityData.defineId(EntitySpitter.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(EntitySpitter.class, EntityDataSerializers.BOOLEAN);

    public static Animation ATTACK_MAUL_RIGHT;
    public static Animation ATTACK_MAUL_LEFT;
    public static Animation ATTACK_SPIT;
    public static Animation ANIMATION_EAT;
    public static Animation IDLE_WATCH;
    public static Animation IDLE_TALK;
    public int aggroProgress;

    public EntitySpitter(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
        ATTACK_MAUL_RIGHT = Animation.create(22);
        ATTACK_MAUL_LEFT = Animation.create(22);
        ATTACK_SPIT = Animation.create(20);
        IDLE_TALK = Animation.create(40);
        IDLE_WATCH = Animation.create(80);
        ANIMATION_EAT = Animation.create(80);
        this.turn_speed = 0.3F;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CAN_GROW, false);
        this.entityData.define(HAS_EGG, false);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal_Land(this));
        this.goalSelector.addGoal(2, new FindItemsGoal(this, 12, true));
        this.goalSelector.addGoal(2, new SpitterAttackGoal(this, 2.0D, false, 1));
        this.goalSelector.addGoal(3, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> getEcoLevel(input) > getEcoLevel(this)));
        this.goalSelector.addGoal(4, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(4, new GrazeGoal(this, 300) {
            public boolean isGrazeable() {
                return (this.entityWorld.getBlockState(this.testpos).is(Blocks.SMALL_DRIPLEAF) || this.entityWorld.getBlockState(this.testpos.below()).getBlock() == Blocks.CLAY);
            }
        });
        this.goalSelector.addGoal(4, new GotoSleepGoal(this, 1D));
        //this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, true));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, new SmartHurtByTargetGoal(this).setAlertOthers(EntitySpitter.class));
        this.targetSelector.addGoal(3, new ProtectChildrenTarget<>(this, LivingEntity.class, true, input -> !(input instanceof EntitySpitter)));
        this.targetSelector.addGoal(4, new SpitterTerritorialityGoal<>(this, EntitySpitter.class, true));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.8D)
                .add(Attributes.MOVEMENT_SPEED, 0.16D)
                .add(Attributes.FOLLOW_RANGE, 32D)
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
                .add(Attributes.ARMOR, 0D);
    }

    /* Breeding conditions for the Spitter are:
     * Less than 6 other Spitters (larva or adult) entities nearby */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
                List<EntitySpitter> list = this.level.getEntitiesOfClass(EntitySpitter.class, this.getBoundingBox().inflate(32.0D, 12.0D, 32.0D));
                return list.size() < 6;
            }
        }
        return false;
    }

    @Override
    public float getStepHeight() {
        return 1;
    }

    public void setAge(int age) {
        int i = this.age;
        if (this.isBaby() && i < 100 && !this.getWantsToGrow())
            super.setAge(Math.max(-12000, ConfigGamerules.cycleLength.get() / -2));
        else {
            super.setAge(age);
        }
    }

    public boolean wantsToGrow() {
        if (this.isBaby()) {
            List<EntitySpitter> list = this.level.getEntitiesOfClass(EntitySpitter.class, this.getBoundingBox().inflate(32.0D, 16.0D, 32.0D));
            for (EntitySpitter spitter : list) {
                if (spitter.getGender() == this.getGender() && (!spitter.isBaby() || spitter.getWantsToGrow()))
                    return false;
            }
            return true;
        }
        return false;
    }

    @Nullable
    public EntitySpitter getBreedOffspring(ServerLevel serverWorld, AgeableMob ageable) {
        return create_offspring(new EntitySpitter(ModEntity.SPITTER.get(), this.level));
    }

    public boolean isPushedByFluid() {
        return false;
    }

    public Animation getAnimationEat() {
        return ANIMATION_EAT;
    }

    public void aiStep() {
        if (!this.level.isClientSide) {
            if (this.tickCount % 600 == 0) {
                if (!this.getWantsToGrow())
                    this.setWantsToGrow(this.wantsToGrow());
                if (this.wantsToBreed()) {
                    this.setInLove(null);
                }
                if (this.wantsToLayEggs() && !this.isMale()) {
                    for(int k = 0; k < 3; ++k) {
                        BlockState state = ModBlock.EGG_SPITTER.get().defaultBlockState();
                        BlockPos blockpos = this.blockPosition().offset(this.random.nextInt(3) - 1, 1 - this.random.nextInt(3), this.random.nextInt(3) - 1);
                        if (this.getLevel().isInWorldBounds(blockpos) && this.getLevel().getBlockState(blockpos).is(Blocks.WATER) && state.canSurvive(this.getLevel(), blockpos)) {
                            this.getLevel().setBlock(blockpos, state, 2);
                            this.setEggStatus(false);
                        }
                    }
                }
            }

            // Boosted Regeneration
            if (this.level.getGameTime() % 500 == 0) {
                this.addHunger(-3);
                if (!this.isStarving()) {
                    this.heal(2.0F);
                }
            }

            // Random idle animations
            if (this.getAnimation() == NO_ANIMATION && this.getTarget() == null && !this.isSleeping()) {
                if (this.getCommandInt() == 0) {
                    int i = this.random.nextInt(3000);
                    if (i == 0 && !this.isInWater() && this.isNotMoving() && this.canMove() && this.isActive()) {
                        this.getNavigation().stop();
                        this.setSitting(true);
                    }
                    else if ((i == 1 || this.isInWater()) && this.isSitting() && this.getCommandInt() < 2) {
                        this.setSitting(false);
                    }
                    else if (i < 25 && !this.isInWater() && !this.isBaby()) {
                        this.setAnimation(IDLE_TALK);
                    }
                    else if (i > 2980 && !this.isInWater() && !this.isBaby() && this.canMove() && this.isNotMoving()) {
                        this.setAnimation(IDLE_WATCH);
                    }
                }
            }
            if (this.getTarget() != null && (this.getAnimation() == ATTACK_SPIT || this.isBaby())) {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.6F));
                if (this.getAnimationTick() == 14)
                    performRangedAttack(this.getTarget(), 0.5F);
            }
            this.setAngry(this.getTarget() != null);
        }
        if (this.getAnimation() == ANIMATION_EAT && (this.getAnimationTick() == 10 || this.getAnimationTick() == 20 || this.getAnimationTick() == 30)) {
            this.playSound(SoundEvents.HORSE_EAT,1.5F, 0.8F);
        }
        if (this.getAnimation() == ATTACK_MAUL_RIGHT && this.getAnimationTick() == 10) {
            this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP,1.5F, 0.8F);
        }
        if (this.getAnimation() == IDLE_TALK && this.getAnimationTick() == 1 && this.getAmbientSound() != null) {
            this.playSound(this.getAmbientSound(), this.getSoundVolume(), this.getVoicePitch());
        }
        if (this.level.isClientSide && this.isAngry() && this.aggroProgress < 40) {
            this.aggroProgress++;
        } else if (this.level.isClientSide && !this.isAngry() && this.aggroProgress > 0) {
            this.aggroProgress--;
        }
        super.aiStep();
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.HOGLIN_STEP, 0.15F, 1.0F);
    }

    @Override
    protected void reassessTameGoals() {
        if (this.isTame()) {
            if (UntamedWilds.DEBUG) {
                UntamedWilds.LOGGER.info("Updating AI tasks for tamed mob");
            }
            this.goalSelector.addGoal(3, new SmartFollowOwnerGoal(this, 2.3D, 12.0F, 3.0F));
            this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
            this.targetSelector.addGoal(2, new SmartOwnerHurtTargetGoal(this));
        }
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
        }

        return super.mobInteract(player, hand);
    }

    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag && this.getAnimation() == NO_ANIMATION && !this.isBaby()) {
            Animation anim = chooseAttackAnimation();
            this.setAnimation(anim);
            this.setAnimationTick(0);
        }
        return flag;
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        // Retaliate I: Mob will strike back when attacked by its current target
        float f = this.getHealth();
        if (!this.level.isClientSide && !this.isNoAi() && this.getTarget() == damageSource.getEntity() && amount < f && (damageSource.getEntity() != null || damageSource.getDirectEntity() != null) && damageSource.getEntity() instanceof LivingEntity && (damageSource.getEntity() instanceof TamableAnimal tamable && tamable.getOwner() != null)) {
            if (this.hasLineOfSight(damageSource.getEntity()) && !damageSource.getEntity().isInvulnerable() && this.getAnimation() == NO_ANIMATION) {
                this.doHurtTarget(damageSource.getEntity());
            }
        }
        return super.hurt(damageSource, amount);
    }

    private Animation chooseAttackAnimation() {
        return switch (this.random.nextInt(2)) {
            case 0 -> ATTACK_MAUL_LEFT;
            case 1 -> ATTACK_MAUL_RIGHT;
            default -> ATTACK_MAUL_RIGHT;
        };
    }

    //public Animation getAnimationEat() { return ANIMATION_EAT; }
    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ATTACK_MAUL_RIGHT, ATTACK_MAUL_LEFT, ATTACK_SPIT, IDLE_WATCH, IDLE_TALK, ANIMATION_EAT};
    }

    public boolean canBeAffected(MobEffectInstance potionEffectIn) {
        return potionEffectIn.getEffect() != MobEffects.POISON && super.canBeAffected(potionEffectIn);
    }

    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * 0.9F;
    }

    public boolean getWantsToGrow(){ return (this.entityData.get(CAN_GROW)); }
    private void setWantsToGrow(boolean can_grow){ this.entityData.set(CAN_GROW, can_grow); }

    @Override
    public boolean wantsToLayEggs() {
        return this.entityData.get(HAS_EGG);
    }
    @Override
    public void setEggStatus(boolean status) {
        this.entityData.set(HAS_EGG, status);
    }

    public void addAdditionalSaveData(CompoundTag compound){
        super.addAdditionalSaveData(compound);
        compound.putBoolean("canGrow", this.getWantsToGrow());
        compound.putBoolean("has_egg", this.wantsToLayEggs());
    }

    public void readAdditionalSaveData(CompoundTag compound){
        super.readAdditionalSaveData(compound);
        this.setWantsToGrow(compound.getBoolean("canGrow"));
        this.setEggStatus(compound.getBoolean("has_egg"));
    }

    public void performRangedAttack(LivingEntity entityIn, float p_33318_) {
        ProjectileSpit camel_spit = new ProjectileSpit(this.level, this, new MobEffectInstance(MobEffects.POISON, 100, 0));
        double d0 = entityIn.getX() - this.getX();
        double d1 = entityIn.getY(0.3333333333333333D) - camel_spit.getY();
        double d2 = entityIn.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2) * (double)0.2F;
        camel_spit.shoot(d0, d1 + d3, d2, 1.5F, 6.0F);
        this.getLookControl().setLookAt(entityIn);
        if (!this.isSilent()) {
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LLAMA_SPIT, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
        }
        this.level.addFreshEntity(camel_spit);
    }

    @Override
    public Block getNestType() {
        return ModBlock.EGG_SPITTER.get();
    }

    @Override
    public boolean isValidNestBlock(BlockPos pos) {
        return this.level.isEmptyBlock(pos) && this.level.getBlockState(pos.below()).is(ModTags.ModBlockTags.VALID_REPTILE_NEST) && this.getNestType().defaultBlockState().canSurvive(this.level, pos);
    }

    @Override
    public void updateAttributes() {
        int rand = this.getRandom().nextInt(5);
        if (rand > 3 && !this.isMale()) {
            this.breed();
        }
    }
}
