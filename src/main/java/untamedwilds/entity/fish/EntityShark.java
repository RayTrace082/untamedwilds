package untamedwilds.entity.fish;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.MeleeAttackCircle;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.target.HuntWeakerTarget;
import untamedwilds.entity.ai.target.SmartHurtByTargetGoal;
import untamedwilds.entity.ai.unique.SharkSwimmingGoal;
import untamedwilds.init.ModEntity;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntityShark extends ComplexMobAquatic implements ISpecies, IAnimatedEntity, INeedsPostUpdate, INewSkins {

    private static final EntityDataAccessor<Boolean> SHORT_FINS = SynchedEntityData.defineId(EntityShark.class, EntityDataSerializers.BOOLEAN);

    public static Animation ATTACK_THRASH;
    private int animationTick;
    private Animation currentAnimation;
    public int ringBufferIndex = -1;
    public final double[][] ringBuffer = new double[64][3];

    public EntityShark(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
        ATTACK_THRASH = Animation.create(15);
        this.entityData.define(SHORT_FINS, false);
        this.turn_speed = 0.3F;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.4D)
                .add(Attributes.MOVEMENT_SPEED, 0.8D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1D)
                .add(Attributes.MAX_HEALTH, 50.0D);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new MeleeAttackCircle(this, 2.3D, false, 2));
        this.goalSelector.addGoal(3, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(4, new SharkSwimmingGoal(this));
        this.targetSelector.addGoal(1, new SmartHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntWeakerTarget<>(this, LivingEntity.class, true));
    }

    public void aiStep() {
        super.aiStep();
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (!this.level.isClientSide) {
            this.setAngry(this.getTarget() != null);
            if (this.tickCount % 1000 == 0) {
                if (this.wantsToBreed() && !this.isMale()) {
                    this.setAge(this.getPregnancyTime());
                }
            }
            if (this.level.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
        }

        if (!this.isNoAi() && !this.isBaby()) {
            if (this.ringBufferIndex < 0) {
                for (int i = 0; i < this.ringBuffer.length; ++i) {
                    this.ringBuffer[i][0] = this.getYRot();
                    this.ringBuffer[i][1] = this.getY();
                }
            }
            this.ringBufferIndex++;
            if (this.ringBufferIndex == this.ringBuffer.length) {
                this.ringBufferIndex = 0;
            }
            this.ringBuffer[this.ringBufferIndex][0] = this.yRotO + 0.5F * Mth.wrapDegrees(this.getYRot() - this.yRotO);
            this.ringBuffer[ringBufferIndex][1] = this.getY();
        }
    }

    public double[] getMovementOffsets(int offset, float partialTicks) {
        if (this.isDeadOrDying()) {
            partialTicks = 0.0F;
        }
        partialTicks = 1.0F - partialTicks;
        int i = this.ringBufferIndex - offset & 63;
        int j = this.ringBufferIndex - offset - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.ringBuffer[i][0];
        double d1 = this.ringBuffer[j][0] - d0;
        adouble[0] = d0 + d1 * (double) partialTicks;
        d0 = this.ringBuffer[i][1];
        d1 = this.ringBuffer[j][1] - d0;
        adouble[1] = d0 + d1 * (double) partialTicks;
        adouble[2] = Mth.lerp(partialTicks, this.ringBuffer[i][2], this.ringBuffer[j][2]);
        return adouble;
    }

    /* Breeding conditions for the Shark are:
     * A nearby Shark of different gender */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
            List<EntityShark> list = this.level.getEntitiesOfClass(EntityShark.class, this.getBoundingBox().inflate(12.0D, 8.0D, 12.0D));
            list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
            if (list.size() >= 1) {
                this.setAge(this.getAge());
                list.get(0).setAge(this.getAge());
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return create_offspring(new EntityShark(ModEntity.SHARK.get(), this.level));
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }

    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag && this.getAnimation() == NO_ANIMATION && !this.isBaby()) {
            this.setAnimation(ATTACK_THRASH);
        }
        return flag;
    }

    public int getAnimationTick() { return animationTick; }
    public void setAnimationTick(int tick) { animationTick = tick; }
    public Animation getAnimation() { return currentAnimation; }
    public void setAnimation(Animation animation) { currentAnimation = animation; }
    public Animation[] getAnimations() { return new Animation[]{NO_ANIMATION, ATTACK_THRASH}; }

    // Flags Parameters
    public boolean isBottomDweller() { return getEntityData(this.getType()).getFlags(this.getVariant(), "bottomDweller") == 1; }

    @Override
    public void updateAttributes() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getAttack());
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getHealth());
        this.setHealth(this.getMaxHealth());
        this.setShortFins(getEntityData(this.getType()).getFlags(this.getVariant(), "shortFins") == 1);
    }

    public boolean hasShortFins(){ return (this.entityData.get(SHORT_FINS)); }
    private void setShortFins(boolean short_fins){ this.entityData.set(SHORT_FINS, short_fins); }

    public void addAdditionalSaveData(CompoundTag compound){
        super.addAdditionalSaveData(compound);
        compound.putBoolean("hasShortFins", this.hasShortFins());
    }

    public void readAdditionalSaveData(CompoundTag compound){
        super.readAdditionalSaveData(compound);
        this.setShortFins(compound.getBoolean("hasShortFins"));
    }
}
