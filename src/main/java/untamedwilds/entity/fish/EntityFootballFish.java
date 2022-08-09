package untamedwilds.entity.fish;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.SmartMeleeAttackGoal;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;

public class EntityFootballFish extends ComplexMobAquatic implements ISpecies, INewSkins, INeedsPostUpdate {

    private static final EntityDataAccessor<Boolean> HAS_MALE = SynchedEntityData.defineId(EntityFootballFish.class, EntityDataSerializers.BOOLEAN);

    public EntityFootballFish(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HAS_MALE, false);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0D)
                .add(Attributes.MOVEMENT_SPEED, 0.42D)
                .add(Attributes.FOLLOW_RANGE, 8.0D)
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0D)
                .add(Attributes.ARMOR, 2D);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SmartMeleeAttackGoal(this, 1.8D, false, 2));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new SwimGoal(this, 4));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new HuntMobTarget<>(this, LivingEntity.class, true, false, input -> getEcoLevel(input) < 5));
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            if (this.tickCount % 1000 == 0) {
                if (this.wantsToBreed() && !this.isMale()) {
                    this.breed();
                }
                if (!this.hasAttachedMale() && this.random.nextInt(40) == 0 && this.getY() < 42) {
                    this.setAttachedMale(true);
                }
            }
            if (this.level.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
            if (this.random.nextInt(18) == 0)
                ((ServerLevel)this.level).sendParticles(ParticleTypes.GLOW, this.getX(), this.getY() + 0.4, this.getZ(), 1, 0F, 0F, 0F, 0D);
        }
    }

    /* Breeding conditions for the Football Fish are:
     * Be really deep in the ocean (16 blocks at least), and that's it */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.hasAttachedMale() && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
            BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
            for (int i = 0; i <= 16; i++) {
                BlockState state = level.getBlockState(blockPos.set(this.getX(), this.getY() + i, this.getZ()));
                if (!state.getFluidState().is(FluidTags.WATER)) {
                    return false;
                }
            }
            this.setAge(this.getAge());
            return true;
        }
        return false;
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (hand == InteractionHand.MAIN_HAND && !this.level.isClientSide()) {

            if (this.hasAttachedMale() && itemstack.getItem() == Items.SHEARS) {
                this.playSound(SoundEvents.SHEEP_SHEAR, 1.5F, 0.8F);
                this.setAttachedMale(false);
                this.hurt(DamageSource.mobAttack(player), 1);
            }
        }

        return super.mobInteract(player, hand);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        if (!this.hasAttachedMale())
            this.setAttachedMale(true);
        EntityUtils.dropEggs(this, "egg_football_fish", this.getOffspring());
        return null;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.GUARDIAN_FLOP;
    }

    @Override
    public void updateAttributes() {
        // All Football Fish are female, for the purpose of the mod, males do not exist
        this.setGender(1);
        if (this.random.nextInt(3) == 0) {
            this.setAttachedMale(true);
        }
    }

    public boolean hasAttachedMale(){ return (this.entityData.get(HAS_MALE)); }
    private void setAttachedMale(boolean attachedMale){ this.entityData.set(HAS_MALE, attachedMale); }

    public void addAdditionalSaveData(CompoundTag compound){
        super.addAdditionalSaveData(compound);
        compound.putBoolean("hasMale", this.hasAttachedMale());
    }

    public void readAdditionalSaveData(CompoundTag compound){
        super.readAdditionalSaveData(compound);
        this.setAttachedMale(compound.getBoolean("hasMale"));
    }
}
