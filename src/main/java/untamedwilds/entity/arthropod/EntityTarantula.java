package untamedwilds.entity.arthropod;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.INewSkins;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.SmartAvoidGoal;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.SmartSwimGoal_Land;
import untamedwilds.entity.ai.target.DontThreadOnMeTarget;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

// TODO: The whole climbing is terrible, refactor it
public class EntityTarantula extends ComplexMob implements ISpecies, INewSkins {

    private static final EntityDataAccessor<Boolean> CLIMBING = SynchedEntityData.defineId(EntityTarantula.class, EntityDataSerializers.BOOLEAN);

    public int aggroProgress;
    public int climbProgress;
    public boolean invertClimbing = false;
    //public int webProgress;

    public EntityTarantula(EntityType<? extends EntityTarantula> type, Level worldIn) {
        super(type, worldIn);
        this.entityData.define(CLIMBING, false);
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MAX_HEALTH, 2.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0D)
                .add(Attributes.ARMOR, 0D);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal_Land(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.3D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D,  input -> getEcoLevel(input) > getEcoLevel(this)));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false,  input -> getEcoLevel(input) < getEcoLevel(this)));
        this.targetSelector.addGoal(3, new DontThreadOnMeTarget<>(this, LivingEntity.class, true));
    }

    protected PathNavigation createNavigation(Level p_33802_) {
        return new WallClimberNavigation(this, p_33802_);
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            if (this.tickCount % 1000 == 0) {
                if (this.wantsToBreed() && !this.isMale()) {
                    this.breed();
                }
            }
            if (this.level.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
            this.setAngry(this.getTarget() != null);
            this.setClimbing(this.horizontalCollision && nextToClimbableBlock(this));
            if (!this.isOnGround() && !this.isClimbing() && this.getDeltaMovement().y() < 0 && nextToClimbableBlock(this)){
                this.setDeltaMovement(this.getDeltaMovement().multiply(0, 0.5, 0));
                this.setClimbing(true);
            }
        }
        if (this.level.isClientSide()) {
            if (this.isAngry() && this.aggroProgress < 40)
                this.aggroProgress++;
            else if (!this.isAngry() && this.aggroProgress > 0)
                this.aggroProgress--;

            if (this.isClimbing() && this.climbProgress < 20)
                this.climbProgress++;
            else if (!this.isClimbing() && this.climbProgress > 0)
                this.climbProgress--;

            if (this.climbProgress % 20 != 0 && this.invertClimbing != (this.getDeltaMovement().y() < 0))
                this.invertClimbing = !this.isOnGround() && this.getDeltaMovement().y() < 0;
        }
    }

    public boolean onClimbable() {
        return this.isClimbing();
    }

    /* Breeding conditions for the Tarantula are:
     * A nearby Tarantula of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && !this.isSleeping() && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
            List<EntityTarantula> list = this.level.getEntitiesOfClass(EntityTarantula.class, this.getBoundingBox().inflate(6.0D, 4.0D, 6.0D));
            list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
            if (list.size() >= 1) {
                this.setAge(this.getPregnancyTime());
                list.get(0).setAge(this.getPregnancyTime());
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        EntityUtils.dropEggs(this, "egg_tarantula", this.getOffspring());
        return null;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (itemstack.getItem() == Items.GLASS_BOTTLE && this.isAlive()) {
            EntityUtils.turnEntityIntoItem(this, "bottle_tarantula");
            itemstack.shrink(1);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    public boolean causeFallDamage(float p_148859_, float p_148860_, DamageSource p_148861_) {
        return false;
    }

    public boolean doHurtTarget(Entity entityIn) {
        float f = (float)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        boolean flag = entityIn.hurt(DamageSource.mobAttack(this), f);
        if (flag) {
            if (entityIn instanceof LivingEntity) {
                ((LivingEntity)entityIn).addEffect(new MobEffectInstance(MobEffects.POISON, 80, 0));
            }
            return true;
        }
        return false;
    }

    public boolean canBeAffected(MobEffectInstance potionEffectIn) {
        return potionEffectIn.getEffect() != MobEffects.POISON && super.canBeAffected(potionEffectIn);
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    public boolean isClimbing() {
        return this.entityData.get(CLIMBING) && nextToClimbableBlock(this);
    }

    public static boolean nextToClimbableBlock(EntityTarantula entityIn) {
        Level world = entityIn.getLevel();
        BlockPos pos_1 = entityIn.blockPosition().offset(Math.cos(Math.toRadians(entityIn.getYRot() + 90)) * 1.2, 0, Math.sin(Math.toRadians(entityIn.getYRot() + 90)) * 1.2);
        BlockPos pos_2 = entityIn.blockPosition().offset(Math.cos(Math.toRadians(entityIn.getYRot() + 90)) * -1.2, 0, Math.sin(Math.toRadians(entityIn.getYRot() + 90)) * -1.2);
        BlockState block_1 = world.getBlockState(pos_1);
        BlockState block_2 = world.getBlockState(pos_2);
        return block_1.isCollisionShapeFullBlock(world, pos_1) || block_2.isCollisionShapeFullBlock(world, pos_2);
    }

    public void setClimbing(boolean p_33820_) {
        this.entityData.set(CLIMBING, p_33820_);
    }
}
