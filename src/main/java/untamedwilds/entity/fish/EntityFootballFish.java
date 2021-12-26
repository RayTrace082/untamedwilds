package untamedwilds.entity.fish;

import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.SmartMeleeAttackGoal;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;

public class EntityFootballFish extends ComplexMobAquatic implements ISpecies, INewSkins, INeedsPostUpdate {

    private static final DataParameter<Boolean> HAS_MALE = EntityDataManager.createKey(EntityFootballFish.class, DataSerializers.BOOLEAN);

    public EntityFootballFish(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        //this.dataManager.register(HAS_MALE, false);
        this.experienceValue = 2;
    }

    protected void registerData() {
        super.registerData();
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.42D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 8.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 8.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0D)
                .createMutableAttribute(Attributes.ARMOR, 2D);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SmartMeleeAttackGoal(this, 1.8D, false, 2));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new SwimGoal(this, 4));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new HuntMobTarget<>(this, LivingEntity.class, true, false, input -> getEcoLevel(input) < getEcoLevel(this)));
    }

    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote) {
            if (this.ticksExisted % 1000 == 0) {
                if (this.wantsToBreed() && !this.isMale()) {
                    this.breed();
                }
                if (!this.hasAttachedMale() && this.rand.nextInt(40) == 0 && this.getPosY() < 42) {
                    this.setAttachedMale(true);
                }
            }
            if (this.world.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
            // TODO: In 1.17, feature glow squid particles
            //EntityUtils.spawnParticlesOnEntity(this.world, this, ParticleTypes.CRIT, 1, 1);
        }
    }

    /* Breeding conditions for the Football Fish are:
     * Be really deep in the ocean (16 blocks at least), and that's it */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.hasAttachedMale() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this)) {
            BlockPos.Mutable blockPos = new BlockPos.Mutable();
            for (int i = 0; i <= 16; i++) {
                BlockState state = world.getBlockState(blockPos.setPos(this.getPosX(), this.getPosY() + i, this.getPosZ()));
                if (!state.getFluidState().isTagged(FluidTags.WATER)) {
                    return false;
                }
            }
            this.setGrowingAge(this.getGrowingAge());
            return true;
        }
        return false;
    }

    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);
        if (hand == Hand.MAIN_HAND && !this.world.isRemote()) {

            if (this.hasAttachedMale() && itemstack.getItem() == Items.SHEARS) {
                this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.5F, 0.8F);
                this.setAttachedMale(false);
                this.attackEntityFrom(DamageSource.causeMobDamage(player), 1);
            }
        }

        return super.func_230254_b_(player, hand);
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        if (!this.hasAttachedMale())
            this.setAttachedMale(true);
        EntityUtils.dropEggs(this, "egg_football_fish", this.getOffspring());
        return null;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_GUARDIAN_FLOP;
    }

    @Override
    public void updateAttributes() {
        // All Football Fish are female, for the purpose of the mod, males do not exist
        this.setGender(1);
        if (this.rand.nextInt(3) == 0) {
            this.setAttachedMale(true);
        }
    }

    public boolean hasAttachedMale(){ return (this.dataManager.get(HAS_MALE)); }
    private void setAttachedMale(boolean attachedMale){ this.dataManager.set(HAS_MALE, attachedMale); }

    public void writeAdditional(CompoundNBT compound){
        super.writeAdditional(compound);
        compound.putBoolean("hasMale", this.hasAttachedMale());
    }

    public void readAdditional(CompoundNBT compound){
        super.readAdditional(compound);
        this.setAttachedMale(compound.getBoolean("hasMale"));
    }
}
