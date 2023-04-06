package untamedwilds.entity.reptile;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import oshi.util.tuples.Pair;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.control.look.SmartSwimmerLookControl;
import untamedwilds.entity.ai.control.movement.SmartSwimmingMoveControl;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModTags;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntityMonitor extends ComplexMobAmphibious implements ISpecies, INeedsPostUpdate, INewSkins, INestingMob {

    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(EntityMonitor.class, EntityDataSerializers.BOOLEAN);

    public static Animation IDLE_TONGUE;
    public static Animation ATTACK_THRASH;
    public int swimProgress;
    public float offset;
    public Pair<Float, Float> head_movement;
    private float neck_val = 0;
    private float head_val = 0;

    public EntityMonitor(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
        this.moveControl = new SmartSwimmingMoveControl(this, 40, 5, 0.25F, 0.3F, true);
        this.lookControl = new SmartSwimmerLookControl(this, 20);
        IDLE_TONGUE = Animation.create(20);
        ATTACK_THRASH = Animation.create(30);
        this.head_movement = new Pair<>(0F, 0F);
        this.ticksToSit = 20;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HAS_EGG, false);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1D)
                .add(Attributes.MOVEMENT_SPEED, 0.7D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2D);
    }

    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 1.4D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 0.7D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, (float)this.getAttributeValue(Attributes.FOLLOW_RANGE), 1D, 1.1D,  input -> getEcoLevel(input) > getEcoLevel(this)));
        this.goalSelector.addGoal(3, new SmartWanderGoal(this, 1.0D, true));
        this.goalSelector.addGoal(3, new LayEggsOnNestGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, false, input ->  getEcoLevel(input) < getEcoLevel(this)));
    }

    public float getWalkTargetValue(BlockPos p_149140_, LevelReader p_149141_) {
        return 0.0F;
    }

    public boolean wantsToBeOnLand() { return true; }

    public boolean wantsToBeInWater() { return false; }

    public boolean isPushedByFluid() {
        return false;
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

            if (this.level.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
            if (this.getAnimation() == NO_ANIMATION && this.getTarget() == null && !this.isSleeping()) {
                int i = this.random.nextInt(3000);
                if (i <= 10 && !this.isInWater()) {
                    this.getNavigation().stop();
                    this.setSitting(true);
                }
                else if ((i == 11 || this.isInWater()) && this.isSitting()) {
                    this.setSitting(false);
                }
                else if (i > 2900) {
                    if (this.isVehicle() && this.getFirstPassenger() != null) {
                        this.setAnimation(ATTACK_THRASH);
                        doHurtTarget(this.getFirstPassenger());
                    }
                    else {
                        this.setAnimation(IDLE_TONGUE);
                    }
                }
            }
        }
        else {
            if (Math.abs(this.getYRot() - this.yRotO) > 0.005) {
                this.offset = Mth.rotLerp(0.05F, this.offset, (this.getYRot() - this.yRotO));
            }
            if (this.isInWater() && !this.isNotMoving()) {
                if (this.swimProgress < 20)
                    this.swimProgress++;
            } else if ((!this.isInWater() || this.isNotMoving()) && this.swimProgress > 0) {
                this.swimProgress--;
            }
            if (this.tickCount % 120 < 11) {
                if (this.tickCount % 120 == 1) {
                    neck_val = (float) (0.8F - this.random.nextDouble() * 1.6F);
                    head_val = (float) (0.4F - this.random.nextDouble() * 0.8F);
                }
                this.head_movement = new Pair<>(Mth.lerp(0.1F, head_movement.getA(), neck_val), Mth.lerp(0.1F, head_movement.getB(), head_val));
            }
        }
    }

    /* Breeding conditions for the giant salamander are:
     * A nearby giant salamander of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
                List<EntityMonitor> list = this.level.getEntitiesOfClass(EntityMonitor.class, this.getBoundingBox().inflate(6.0D, 4.0D, 6.0D));
                list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
                if (list.size() >= 1) {
                    this.setAge(this.getPregnancyTime());
                    list.get(0).setAge(this.getPregnancyTime());
                    return true;
                }
            }
        }
        return false;
    }

    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag && this.getAnimation() == NO_ANIMATION && !this.isBaby()) {
            this.setAnimation(ATTACK_THRASH);
            this.setAnimationTick(0);
            if (entityIn instanceof LivingEntity) {
                ((LivingEntity)entityIn).addEffect(new MobEffectInstance(MobEffects.POISON, 40, 0));
                if (!this.isVehicle() && entityIn.getBbWidth() * entityIn.getBbHeight() < 0.4F && !(entityIn instanceof TamableAnimal && ((TamableAnimal) entityIn).isTame())) {
                    entityIn.startRiding(this);
                    ((LivingEntity) entityIn).setHealth(10);
                }
            }
        }
        return flag;
    }

    public double getPassengersRidingOffset() {
        return 0D;
    }

    @Override
    public void positionRider(Entity p_20312_) {
        //super.positionRider(p_20312_);
        this.positionPreyInJaw(p_20312_, Entity::setPos);
    }

    private void positionPreyInJaw(Entity p_19957_, Entity.MoveFunction p_19958_) {
        if (this.hasPassenger(p_19957_)) {
            int rev = this.getAnimationTick() % 12 >= 6 ? -1 : 1;
            int factor = rev * (this.getAnimationTick() % 12);
            double d0 = this.getY() + this.getPassengersRidingOffset() + p_19957_.getMyRidingOffset();
            p_19958_.accept(p_19957_, this.getX() + Math.cos(Math.toRadians(this.getYRot() + 90 + (factor * 4))) * 1, d0, this.getZ() + Math.sin(Math.toRadians(this.getYRot() + 90 + (factor * 4))) * 1);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (itemstack.isEmpty() && this.isAlive() && hand.equals(InteractionHand.MAIN_HAND)) {
            this.setAnimation(ATTACK_THRASH);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        EntityUtils.dropEggs(this, "egg_monitor", this.getOffspring());
        return null;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        SoundEvent soundevent = this.isBaby() ? SoundEvents.TURTLE_SHAMBLE_BABY : SoundEvents.TURTLE_SHAMBLE;
        this.playSound(soundevent, 0.15F, 1.0F);
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        // Retaliate I: Mob will strike back when attacked by its current target
        performRetaliation(damageSource, this.getHealth(), amount, true);
        return super.hurt(damageSource, amount);
    }

    public Animation[] getAnimations() { return new Animation[]{NO_ANIMATION, IDLE_TONGUE, ATTACK_THRASH}; }

    @Override
    public void updateAttributes() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getAttack());
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getHealth());
        this.setHealth(this.getMaxHealth());
    }

    @Override
    public boolean wantsToLayEggs() {
        return this.entityData.get(HAS_EGG);
    }

    @Override
    public void setEggStatus(boolean status) {
        this.entityData.set(HAS_EGG, status);
    }

    @Override
    public Block getNestType() {
        return ModBlock.NEST_REPTILE.get();
    }

    @Override
    public boolean isValidNestBlock(BlockPos pos) {
        return this.level.isEmptyBlock(pos) && this.level.getBlockState(pos.below()).is(ModTags.ModBlockTags.VALID_REPTILE_NEST) && this.getNestType().defaultBlockState().canSurvive(this.level, pos);
    }

    public void addAdditionalSaveData(CompoundTag compound){
        super.addAdditionalSaveData(compound);
        compound.putBoolean("has_egg", this.wantsToLayEggs());
    }

    public void readAdditionalSaveData(CompoundTag compound){
        super.readAdditionalSaveData(compound);
        this.setEggStatus(compound.getBoolean("has_egg"));
    }
}
