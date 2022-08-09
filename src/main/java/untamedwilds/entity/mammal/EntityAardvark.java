package untamedwilds.entity.mammal;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobTerrestrial;
import untamedwilds.entity.INewSkins;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.*;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModItems;
import untamedwilds.init.ModLootTables;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntityAardvark extends ComplexMobTerrestrial implements ISpecies, INewSkins {

    private BlockPos lastDugPos = null;

    public static Animation WORK_DIG;
    public static Animation ATTACK;

    public EntityAardvark(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
        this.turn_speed = 0.8F;
        WORK_DIG = Animation.create(76);
        ATTACK = Animation.create(18);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal_Land(this));
        this.goalSelector.addGoal(2, new FindItemsGoal(this, 12, 100, false, true));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.6D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> getEcoLevel(input) > getEcoLevel(this)));
        this.goalSelector.addGoal(3, new GotoSleepGoal(this, 1));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, 120, 0, false));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1D)
                .add(Attributes.MOVEMENT_SPEED, 0.18D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0D)
                .add(Attributes.ARMOR, 0D);
    }

    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            return !this.isSleeping() && this.getAge() == 0 && EntityUtils.hasFullHealth(this) && this.getHunger() >= 80;
        }
        return false;
    }

    @Override
    public void aiStep() {
        if (!this.level.isClientSide) {
            this.setAngry(this.getTarget() != null);
            if (this.tickCount % 600 == 0) {
                if (this.wantsToBreed()) {
                    this.setInLove(null);
                }
            }
            if (this.level.getGameTime() % 1000 == 0) {
                this.addHunger(-10);
                if (!this.isStarving()) {
                    this.heal(1.0F);
                }
            }
            // Random idle animations
            if (this.getAnimation() == NO_ANIMATION && this.getTarget() == null && !this.isSleeping() && this.getCommandInt() == 0) {
                int i = this.random.nextInt(3000);
                if (i == 13 && !this.isInWater() && this.isNotMoving() && this.canMove()) {
                    this.setSitting(true);
                }
                if (i == 14 && this.isSitting()) {
                    this.setSitting(false);
                }
                if (i > 2980 && !this.isInWater() && this.getHunger() < 60 && this.canMove() && this.getAnimation() == NO_ANIMATION) {
                    if ((this.lastDugPos == null || this.distanceToSqr(this.lastDugPos.getX(), this.getY(), this.lastDugPos.getZ()) > 50) && this.level.getBlockState(this.blockPosition().below()).is(BlockTags.MINEABLE_WITH_SHOVEL)) {
                        this.setAnimation(WORK_DIG);
                        this.lastDugPos = this.blockPosition();
                    }
                }
            }

            if (this.getAnimation() == WORK_DIG && this.getAnimationTick() % 8 == 0) {
                ((ServerLevel)this.level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.level.getBlockState(this.blockPosition().below())), this.getX(), this.getY(), this.getZ(), 20, 0.0D, 0.0D, 0.0D, 0.15F);
                this.playSound(SoundEvents.SHOVEL_FLATTEN, 0.8F, 0.6F);
                if (this.getAnimationTick() == 64) {
                    int rand = this.random.nextInt(6);
                    if (rand == 0) {
                        List<ItemStack> result = EntityUtils.getItemFromLootTable(ModLootTables.LOOT_DIGGING, this.level);
                        for (ItemStack itemstack : result)
                            this.spawnAtLocation(itemstack);
                    }
                    else if (rand == 1) {
                        this.spawnAtLocation(new ItemStack(ModItems.VEGETABLE_AARDVARK_CUCUMBER.get()));
                    }
                }
            }
        }
        if (this.getAnimation() != NO_ANIMATION) {
            if (this.getAnimation() == ATTACK && (this.getAnimationTick() == 8 || this.getAnimationTick() == 12)) {
                this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0F, 0.7F);
            }
        }
        super.aiStep();
    }

    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * 0.85F;
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag && this.getAnimation() == NO_ANIMATION && !this.isBaby()) {
            this.setAnimation(ATTACK);
            this.setAnimationTick(0);
        }
        return flag;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, WORK_DIG, ATTACK};
    }

    @Nullable
    public EntityAardvark getBreedOffspring(ServerLevel serverWorld, AgeableMob ageable) {
        return create_offspring(new EntityAardvark(ModEntity.AARDVARK.get(), this.level));
    }

    public void addAdditionalSaveData(CompoundTag compound){
        super.addAdditionalSaveData(compound);
        if (this.lastDugPos != null) {
            compound.putInt("DugPosX", this.lastDugPos.getX());
            compound.putInt("DugPosZ", this.lastDugPos.getZ());
        }
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("LastDugPos")) {
            this.lastDugPos = new BlockPos(compound.getInt("DugPosX"), 0, compound.getInt("DugPosZ"));
        }
    }
}
