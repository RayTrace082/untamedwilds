package untamedwilds.entity.mammal;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.entity.ai.target.ProtectChildrenTarget;
import untamedwilds.entity.ai.target.SmartOwnerHurtTargetGoal;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModSounds;
import untamedwilds.util.EntityUtils;
import untamedwilds.util.SpeciesDataHolder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityBear extends ComplexMobTerrestrial implements ISpecies, INewSkins, INeedsPostUpdate {

    private static final DataParameter<Boolean> SHORT_SNOUT = EntityDataManager.createKey(EntityBear.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BACK_HUMP = EntityDataManager.createKey(EntityBear.class, DataSerializers.BOOLEAN);

    public static Animation ATTACK_BITE;
    public static Animation ATTACK_MAUL;
    public static Animation ATTACK_SWIPE;
    public static Animation ATTACK_POUND;
    public static Animation ANIMATION_ROAR;
    public static Animation IDLE_STAND;
    public static Animation IDLE_TALK;
    public static Animation ANIMATION_EAT;

    public EntityBear(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.dataManager.register(SHORT_SNOUT, false);
        this.dataManager.register(BACK_HUMP, false);
        ANIMATION_ROAR = Animation.create(50);
        IDLE_TALK = Animation.create(20);
        IDLE_STAND = Animation.create(148);
        ANIMATION_EAT = Animation.create(104);
        ATTACK_BITE = Animation.create(18);
        ATTACK_MAUL = Animation.create(76);
        ATTACK_SWIPE = Animation.create(26);
        ATTACK_POUND = Animation.create(28);
        this.stepHeight = 1;
        this.turn_speed = 0.3F;
        this.experienceValue = 10;
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal(this));
        this.goalSelector.addGoal(2, new FindItemsGoal(this, 12));
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 2.3D, false, 1));
        this.goalSelector.addGoal(3, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> getEcoLevel(input) > 6));
        this.goalSelector.addGoal(4, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(4, new GotoSleepGoal(this, 1D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        //this.goalSelector.addGoal(5, new BearRaidChestsGoal(this, 120));
        this.goalSelector.addGoal(6, new SmartWanderGoal(this, 1D, true));
        this.goalSelector.addGoal(7, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ProtectChildrenTarget<>(this, LivingEntity.class, 0, true, true, input -> !(input instanceof EntityBear)));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false, input -> getEcoLevel(input) <= 5));
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.15D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 24D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 30.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1D)
                .createMutableAttribute(Attributes.ARMOR, 4D);
    }

    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this) && this.getHunger() >= 80) {
                if (ConfigGamerules.hardcoreBreeding.get()) {
                    List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
                    return list.size() < 3;
                }
                return true;
            }
        }
        return false;
    }

    @Nullable
    public EntityBear func_241840_a(ServerWorld serverWorld, AgeableEntity ageable) {
        return create_offspring(new EntityBear(ModEntity.BEAR, this.world));
    }

    public boolean isPushedByWater() {
        return false;
    }

    public void livingTick() {
        if (!this.world.isRemote) {
            if (this.ticksExisted % 600 == 0) {
                if (this.wantsToBreed()) {
                    this.setInLove(null);
                }
            }
            if (this.ticksExisted % 1000 == 0) {
                this.addHunger(-2);
                if (!this.isStarving()) {
                    this.heal(2.0F);
                }
            }
            // Bearserk
            if (this.getHealth() < this.getMaxHealth() / 2) {
                this.addPotionEffect(new EffectInstance(Effects.STRENGTH, 1200, 0, true, true));
                this.forceSleep = -1200;
            }
            // Angry sleepers
            if (ConfigGamerules.angrySleepers.get() && !this.isTamed() && this.isSleeping() && this.forceSleep == 0) {
                List<PlayerEntity> list = this.world.getEntitiesWithinAABB(PlayerEntity.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
                if (!list.isEmpty()) {
                    PlayerEntity player = list.get(0);
                    if (!player.isSneaking() && !player.isCreative()) {
                        this.setSleeping(false);
                        this.setAttackTarget(player);
                        this.forceSleep = -300;
                    }
                }
            }
            this.setAngry(this.getAttackTarget() != null);
            if (this.getAnimation() == NO_ANIMATION && this.getAttackTarget() == null && !this.isSleeping()) {
                if (this.getCommandInt() == 0) {
                    int i = this.rand.nextInt(3000);
                    if (i == 0 && !this.isInWater() && this.isNotMoving() && this.canMove() && this.isActive()) {
                        this.getNavigator().clearPath();
                        this.setSitting(true);
                    }
                    if ((i == 1 || this.isInWater()) && this.isSitting() && this.getCommandInt() < 2) {
                        this.setSitting(false);
                    }
                    if (i == 2 && this.canMove() && !this.isInWater() && !this.isChild()) {
                        if (!this.isSitting()) {
                            this.setAnimation(IDLE_STAND);
                        }
                    }
                    if (i > 2980 && !this.isInWater() && !this.isChild()) {
                        this.setAnimation(IDLE_TALK);
                    }
                }
            }
        }
        if (this.ticksExisted % 100 == 0 && this.getAttackTarget() != null && this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_ROAR);
        }
        if (this.getAnimation() != NO_ANIMATION) {
            if (this.getAnimation() == ATTACK_BITE && this.getAnimationTick() == 1) {
                this.playSound(ModSounds.ENTITY_ATTACK_BITE, 1.5F, 0.8F);
            }
            if (this.getAnimation() == ATTACK_SWIPE && this.getAnimationTick() == 8) {
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.5F, 0.8F);
            }
            if (this.getAnimation() == ATTACK_POUND && this.getAnimationTick() == 10) {
                this.getMoveHelper().strafe(2F, 0);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.5F, 0.8F);
            }
            if (this.getAnimation() == ATTACK_MAUL) {
                if (this.getAnimationTick() == 10) {
                    this.playSound(this.getThreatSound(), 1.5F, 1);
                }
                if (this.getAnimationTick() == 20) {
                    this.playSound(SoundEvents.ENTITY_PLAYER_SMALL_FALL, 1.5F, 0.8F);
                    if (!this.world.isRemote()) {
                        BlockPos pos = this.getPosition().down();
                        BlockState state = this.world.getBlockState(pos);
                        if (!state.addLandingEffects((ServerWorld)this.world, pos, state, this, 40))
                            ((ServerWorld)this.world).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, state), this.getPosX(), this.getPosY(), this.getPosZ(), 40, 0.0D, 0.0D, 0.0D, 99.3F);
                    }
                }
            }
            if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() == 7) {
                this.playSound(this.getThreatSound(), 1.5F, 1);
            }
            if (this.getAnimation() == IDLE_TALK && this.getAnimationTick() == 1) {
                this.playSound(this.getAmbientSound(), 1.5F, 1);
            }
            if (this.getAnimation() == ANIMATION_EAT && (this.getAnimationTick() == 10 || this.getAnimationTick() == 20 || this.getAnimationTick() == 30)) {
                this.playSound(SoundEvents.ENTITY_HORSE_EAT, 1.5F, 0.8F);
            }
        }
        super.livingTick();
    }

    @Override
    protected void setupTamedAI() {
        if (this.isTamed()) {
            if (UntamedWilds.DEBUG) {
                UntamedWilds.LOGGER.info("Updating AI tasks for tamed mob");
            }
            this.goalSelector.addGoal(3, new SmartFollowOwnerGoal(this, 2.3D, 12.0F, 3.0F));
            this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
            this.targetSelector.addGoal(2, new SmartOwnerHurtTargetGoal(this));
        }
    }

    public double getMountedYOffset() { return getModelScale() + 0.5f * this.getMobSize(); }

    protected SoundEvent getAmbientSound() {
        return !this.isChild() ? null : ModSounds.ENTITY_BEAR_BABY_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return !this.isChild() ? ModSounds.ENTITY_BEAR_HURT : ModSounds.ENTITY_BEAR_BABY_CRY;
    }

    protected SoundEvent getDeathSound() { return ModSounds.ENTITY_BEAR_DEATH; }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_POLAR_BEAR_STEP, 0.15F, 1.0F);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (flag && this.getAnimation() == NO_ANIMATION && !this.isChild()) {
            Animation anim = chooseAttackAnimation();
            this.setAnimation(anim);
            this.setAnimationTick(0);
        }
        return flag;
    }

    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || source == DamageSource.SWEET_BERRY_BUSH;
    }

    private Animation chooseAttackAnimation() {
        switch (this.rand.nextInt(4)) {
            case 0: return ATTACK_SWIPE;
            case 1: return ATTACK_BITE;
            case 2: return ATTACK_MAUL;
            default: return ATTACK_POUND;
        }
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.85F;
    }

    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);
        if (hand == Hand.MAIN_HAND && !this.world.isRemote()) {

            if (!this.isTamed() && this.isChild() && EntityUtils.hasFullHealth(this) && this.isBreedingItem(itemstack)) {
                this.playSound(SoundEvents.ENTITY_HORSE_EAT, 1.5F, 0.8F);
                if (this.getRNG().nextInt(3) == 0) {
                    this.setTamedBy(player);
                    EntityUtils.spawnParticlesOnEntity(this.world, this, ParticleTypes.HEART, 3, 6);
                } else {
                    EntityUtils.spawnParticlesOnEntity(this.world, this, ParticleTypes.SMOKE, 3, 3);
                }
            }
        }

        return super.func_230254_b_(player, hand);
    }

    public Animation getAnimationEat() { return ANIMATION_EAT; }
    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ANIMATION_ROAR, IDLE_STAND, IDLE_TALK, ANIMATION_EAT, ATTACK_MAUL, ATTACK_BITE, ATTACK_SWIPE, ATTACK_POUND};
    }

    // Flags Parameters
    public boolean isPanda() {
        return getEntityData(this.getType()).getFlags(this.getVariant(), "isPanda") == 1;
    }

    @Override
    public void updateAttributes() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getAttack());
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getHealth());
        this.setHealth(this.getMaxHealth());
        this.setShortSnout(getEntityData(this.getType()).getFlags(this.getVariant(), "hasShortSnout") == 1);
        this.setHump(getEntityData(this.getType()).getFlags(this.getVariant(), "hasHump") == 1);
    }

    public boolean hasShortSnout(){ return (this.dataManager.get(SHORT_SNOUT)); }
    private void setShortSnout(boolean short_snout){ this.dataManager.set(SHORT_SNOUT, short_snout); }
    public boolean hasHump(){ return (this.dataManager.get(BACK_HUMP)); }
    private void setHump(boolean hump){ this.dataManager.set(BACK_HUMP, hump); }

    // TODO: Very shitty hardcoded solution for Blind Cave Bears to keep spawning
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        ILivingEntityData data = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        if (this.getPosY() < 53) {
            this.setVariant(1);
        }
        return data;
    }

    public void writeAdditional(CompoundNBT compound){
        super.writeAdditional(compound);
        compound.putBoolean("hasShortSnout", this.hasShortSnout());
        compound.putBoolean("hasHump", this.hasHump());
    }

    public void readAdditional(CompoundNBT compound){
        super.readAdditional(compound);
        this.setShortSnout(compound.getBoolean("hasShortSnout"));
        this.setHump(compound.getBoolean("hasHump"));
    }

    public int setSpeciesByBiome(RegistryKey<Biome> biomekey, Biome biome, SpawnReason reason) {
        if (ConfigGamerules.randomSpecies.get() || isArtificialSpawnReason(reason)) {
            return this.getRNG().nextInt(ComplexMob.getEntityData(this.getType()).getSpeciesData().size());
        }
        // TODO: Need a better way to lock Polar and Panda bears
        if (biomekey.equals(Biomes.FROZEN_OCEAN) || biomekey.equals(Biomes.DEEP_FROZEN_OCEAN)) {
            return 5;
        }
        if (biomekey.equals(Biomes.BAMBOO_JUNGLE) || biomekey.equals(Biomes.BAMBOO_JUNGLE_HILLS)) {
            return 4;
        }
        List<Integer> validTypes = new ArrayList<>();
        for (SpeciesDataHolder speciesDatum : getEntityData(this.getType()).getSpeciesData()) {
            for(Biome.Category biomeTypes : speciesDatum.getBiomeCategories()) {
                if(biome.getCategory() == biomeTypes){
                    for (int i=0; i < speciesDatum.getRarity(); i++) {
                        validTypes.add(speciesDatum.getVariant());
                    }
                }
            }
        }
        if (validTypes.isEmpty()) {
            return 99;
        } else {
            return validTypes.get(new Random().nextInt(validTypes.size()));
        }
    }
}
