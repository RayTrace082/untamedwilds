package untamedwilds.entity.amphibian;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.control.look.SmartSwimmerLookControl;
import untamedwilds.entity.ai.control.movement.SmartSwimmingMoveControl;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntityGiantSalamander extends ComplexMobAmphibious implements ISpecies, INeedsPostUpdate, INewSkins {

    public static Animation ATTACK_SWALLOW;
    public int swimProgress;
    public float offset;

    public EntityGiantSalamander(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
        this.moveControl = new SmartSwimmingMoveControl(this, 40, 5, 0.25F, 0.3F, true);
        this.lookControl = new SmartSwimmerLookControl(this, 20);
        ATTACK_SWALLOW = Animation.create(15);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1D)
                .add(Attributes.MOVEMENT_SPEED, 0.8D)
                .add(Attributes.FOLLOW_RANGE, 8.0D)
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0D);
    }

    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 1.4D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 0.7D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, (float)this.getAttributeValue(Attributes.FOLLOW_RANGE), 1D, 1.1D,  input -> getEcoLevel(input) > getEcoLevel(this)));
        //this.goalSelector.addGoal(3, new AmphibiousTransition(this, 1D));
        this.goalSelector.addGoal(4, new AmphibiousRandomSwimGoal(this, 0.7, 400));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, false, input ->  getEcoLevel(input) < getEcoLevel(this) && input.isInWater()));
    }

    public float getWalkTargetValue(BlockPos p_149140_, LevelReader p_149141_) {
        return 0.0F;
    }

    public boolean wantsToBeOnLand() { return this.level.isRainingAt(this.blockPosition()); }

    public boolean wantsToBeInWater() { return true; }

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

            if (this.tickCount % 1000 == 0) {
                if (this.wantsToBreed() && !this.isMale()) {
                    this.breed();
                }
            }
            if (this.level.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
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
        }
    }

    /* Breeding conditions for the giant salamander are:
     * A nearby giant salamander of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
                List<EntityGiantSalamander> list = this.level.getEntitiesOfClass(EntityGiantSalamander.class, this.getBoundingBox().inflate(6.0D, 4.0D, 6.0D));
                list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
                if (list.size() >= 1) {
                    this.setAge(this.getAge());
                    list.get(0).setAge(this.getAge());
                    return true;
                }
            }
        }
        return false;
    }

    public boolean doHurtTarget(Entity entityIn) {
        float f = (float)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        boolean flag = entityIn.hurt(DamageSource.mobAttack(this), f);
        if (flag) {
            if (entityIn instanceof LivingEntity && entityIn.getBbWidth() * entityIn.getBbHeight() < 0.4F && !(entityIn instanceof TamableAnimal && ((TamableAnimal) entityIn).isTame())) {
                this.setAnimation(ATTACK_SWALLOW);
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F);
                EntityUtils.spawnParticlesOnEntity(this.level, (LivingEntity)entityIn, ParticleTypes.POOF, 6, 2);
                this.setDeltaMovement(new Vec3(entityIn.getX() - this.getX(), entityIn.getY() - this.getY(), entityIn.getZ() - this.getZ()).scale(0.15F));
                this.huntingCooldown = 12000;
                entityIn.remove(RemovalReason.KILLED);
            }
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        EntityUtils.dropEggs(this, "egg_giant_salamander", this.getOffspring());
        return null;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (hand == InteractionHand.MAIN_HAND) {
            if (itemstack.getItem().equals(Items.WATER_BUCKET) && this.isAlive()) {
                EntityUtils.mutateEntityIntoItem(this, player, hand, "bucket_giant_salamander", itemstack);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        }
        return super.mobInteract(player, hand);
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        SoundEvent soundevent = this.isBaby() ? SoundEvents.TURTLE_SHAMBLE_BABY : SoundEvents.TURTLE_SHAMBLE;
        this.playSound(soundevent, 0.15F, 1.0F);
    }

    public Animation[] getAnimations() { return new Animation[]{NO_ANIMATION, ATTACK_SWALLOW}; }

    @Override
    public void updateAttributes() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getAttack());
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getHealth());
        this.setHealth(this.getMaxHealth());
    }
}
