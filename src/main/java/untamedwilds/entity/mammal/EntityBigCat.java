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
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.entity.ai.target.ProtectChildrenTarget;
import untamedwilds.entity.ai.target.SmartOwnerHurtTargetGoal;
import untamedwilds.init.ModEntity;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntityBigCat extends ComplexMobTerrestrial implements ISpecies, INewSkins, INeedsPostUpdate, IPackEntity {

    private static final DataParameter<Boolean> DIMORPHISM = EntityDataManager.createKey(EntityBigCat.class, DataSerializers.BOOLEAN);

    public static Animation ATTACK_BITE;
    public static Animation ATTACK_MAUL;
    public static Animation ATTACK_POUNCE;
    public static Animation ANIMATION_ROAR;
    public static Animation ANIMATION_EAT;
    public static Animation IDLE_TALK;
    public static Animation IDLE_STRETCH;
    public int aggroProgress;

    public EntityBigCat(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.dataManager.register(DIMORPHISM, false);
        ATTACK_POUNCE = Animation.create(42);
        ATTACK_MAUL = Animation.create(22);
        IDLE_TALK = Animation.create(20);
        IDLE_STRETCH = Animation.create(110);
        this.stepHeight = 1;
        this.experienceValue = 10;
        this.turn_speed = 0.1F;
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal(this));
        this.goalSelector.addGoal(2, new FindItemsGoal(this, 12, true));
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 2.3D, false, 1, false, true));
        this.goalSelector.addGoal(3, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> getEcoLevel(input) > 9));
        this.goalSelector.addGoal(4, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(4, new GotoSleepGoal(this, 1D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, true));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ProtectChildrenTarget<>(this, LivingEntity.class, 0, true, true, input -> !(input instanceof EntityBigCat)));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false, input -> getEcoLevel(input) < 8));
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 8.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.16D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 40.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
                .createMutableAttribute(Attributes.ARMOR, 0D);
    }

    /* Breeding conditions for the Snow Leopard are:
     * Cold Biome (T between -1.0 and 0.4)
     * No other entities nearby */
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
    public EntityBigCat func_241840_a(ServerWorld serverWorld, AgeableEntity ageable) {
        return create_offspring(new EntityBigCat(ModEntity.BIG_CAT, this.world));
    }

    public boolean isPushedByWater() {
        return false;
    }

    protected int calculateFallDamage(float distance, float damageMultiplier) {
        return MathHelper.ceil((distance * 0.5F - 3.0F) * damageMultiplier);
    }

    public void livingTick() {
        if (!this.world.isRemote) {
            if (this.herd == null && EntityUtils.getPackSize(this.getType(), this.getVariant()) > 1) {
                IPackEntity.initPack(this);
            }
            else if (EntityUtils.getPackSize(this.getType(), this.getVariant()) > 1) {
                this.herd.tick();
            }
            if (this.ticksExisted % 600 == 0) {
                if (this.wantsToBreed()) {
                    this.setInLove(null);
                }
            }
            if (this.world.getGameTime() % 1000 == 0) {
                this.addHunger(-3);
                if (!this.isStarving()) {
                    this.heal(2.0F);
                }
            }
            // Angry Sleepers
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
            if (this.ticksExisted % 200 == 0) {
                if (!this.isActive() && this.getNavigator().noPath()) {
                    this.tiredCounter++;
                    if (this.getDistanceSq(this.getHomeAsVec()) <= 6) {
                        this.setSleeping(true);
                        this.tiredCounter = 0;
                    }
                    else if (tiredCounter >= 3) {
                        this.setHome(BlockPos.ZERO);
                        this.tiredCounter = 0;
                    }
                }
            }
            // Random idle animations
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
                        this.getNavigator().clearPath();
                        this.setAnimation(IDLE_STRETCH);
                    }
                    if (i > 2980 && !this.isInWater() && !this.isChild()) {
                        this.setAnimation(IDLE_TALK);
                    }
                }
            }
            if (this.ticksExisted % 80 == 2 && this.getAttackTarget() != null && this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(ANIMATION_ROAR);
            }
            if (this.getAnimation() == ATTACK_POUNCE && this.getAnimationTick() == 10) {
                this.getMoveHelper().strafe(2F, 0);
                this.getJumpController().setJumping();
            }
            this.setAngry(this.getAttackTarget() != null);
        }
        if (this.getAnimation() == ANIMATION_EAT && (this.getAnimationTick() == 10 || this.getAnimationTick() == 20 || this.getAnimationTick() == 30)) {
            this.playSound(SoundEvents.ENTITY_HORSE_EAT,1.5F, 0.8F);
        }
        if (this.getAnimation() == ATTACK_MAUL && this.getAnimationTick() == 10) {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,1.5F, 0.8F);
        }
        if (this.getAnimation() == IDLE_TALK && this.getAnimationTick() == 1) {
            this.playSound(this.getAmbientSound(), this.getSoundVolume(), this.getSoundPitch());
        }
        if (this.world.isRemote && this.isAngry() && this.aggroProgress < 40) {
            this.aggroProgress++;
        } else if (this.world.isRemote && !this.isAngry() && this.aggroProgress > 0) {
            this.aggroProgress--;
        }
        super.livingTick();
    }

    public double getMountedYOffset() { return this.getModelScale() + 0.5f * this.getMobSize(); }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
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

    protected SoundEvent getAmbientSound() {
        return this.isChild() ? SoundEvents.ENTITY_OCELOT_AMBIENT : super.getAmbientSound();
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return this.isChild() ?  SoundEvents.ENTITY_OCELOT_HURT : super.getHurtSound(source);
    }

    protected SoundEvent getDeathSound() {
        return this.isChild() ? SoundEvents.ENTITY_OCELOT_DEATH : super.getDeathSound();
    }

    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);
        if (hand == Hand.MAIN_HAND && !this.world.isRemote()) {

            if (!this.isTamed() && this.isChild() && EntityUtils.hasFullHealth(this) && this.isFavouriteFood(itemstack)) {
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

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (flag && this.getAnimation() == NO_ANIMATION && !this.isChild()) {
            Animation anim = chooseAttackAnimation(entityIn);
            this.setAnimation(anim);
            this.setAnimationTick(0);
        }
        return flag;
    }

    private Animation chooseAttackAnimation(Entity target) {
        if (target.getHeight() < this.getHeight()) {
            return ATTACK_MAUL;
        }
        return ATTACK_POUNCE;
    }

    //public Animation getAnimationEat() { return ANIMATION_EAT; }
    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ATTACK_POUNCE, ATTACK_MAUL, IDLE_TALK, IDLE_STRETCH};
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.9F;
    }

    @Override
    public void updateAttributes() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getAttack());
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getHealth());
        this.setHealth(this.getMaxHealth());
        this.setDimorphism(getEntityData(this.getType()).getFlags(this.getVariant(), "dimorphism") == 1);
    }

    public boolean hasDimorphism(){ return (this.dataManager.get(DIMORPHISM)); }
    private void setDimorphism(boolean dimorphism){
        UntamedWilds.LOGGER.info("Child Dimorphism? " + getEntityData(this.getType()).getFlags(this.getVariant(), "dimorphism"));
        this.dataManager.set(DIMORPHISM, dimorphism);
    }

    public void writeAdditional(CompoundNBT compound){
        super.writeAdditional(compound);
        compound.putBoolean("hasDimorphism", this.hasDimorphism());
    }

    public void readAdditional(CompoundNBT compound){
        super.readAdditional(compound);
        this.setDimorphism(compound.getBoolean("hasDimorphism"));
    }

    public ResourceLocation getTexture() {
        ResourceLocation texture_path = EntityUtils.getSkinFromEntity(this);
        if (this.hasDimorphism()) {
            String trimmed_path = texture_path.getPath().substring(0, texture_path.getPath().lastIndexOf('.'));
            return new ResourceLocation(UntamedWilds.MOD_ID, trimmed_path + "_" + this.getGenderString() + ".png");
        }
        return texture_path;
    }
}
