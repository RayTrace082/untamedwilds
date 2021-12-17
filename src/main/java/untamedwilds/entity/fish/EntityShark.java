package untamedwilds.entity.fish;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.MeleeAttackCircle;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.target.HuntWeakerTarget;
import untamedwilds.entity.ai.unique.SharkSwimmingGoal;
import untamedwilds.init.ModEntity;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntityShark extends ComplexMobAquatic implements ISpecies, IAnimatedEntity, INeedsPostUpdate, INewSkins {

    private static final DataParameter<Boolean> SHORT_FINS = EntityDataManager.createKey(EntityShark.class, DataSerializers.BOOLEAN);

    public static Animation ATTACK_THRASH;
    private int animationTick;
    private Animation currentAnimation;

    public EntityShark(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        ATTACK_THRASH = Animation.create(15);
        this.experienceValue = 10;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 12.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.8D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 50.0D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackCircle(this, 2.3D, false, 2));
        this.goalSelector.addGoal(3, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(4, new SharkSwimmingGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntWeakerTarget<>(this, LivingEntity.class, true));
    }

    public void livingTick() {
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (!this.world.isRemote) {
            this.setAngry(this.getAttackTarget() != null);
            if (this.ticksExisted % 1000 == 0) {
                if (this.wantsToBreed() && !this.isMale()) {
                    this.setGrowingAge(this.getPregnancyTime());
                }
            }
            if (this.world.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
        }
        super.livingTick();
    }

    /* Breeding conditions for the Shark are:
     * A nearby Shark of different gender */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this)) {
            List<EntityShark> list = this.world.getEntitiesWithinAABB(EntityShark.class, this.getBoundingBox().grow(12.0D, 8.0D, 12.0D));
            list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
            if (list.size() >= 1) {
                this.setGrowingAge(this.getGrowingAge());
                list.get(0).setGrowingAge(this.getGrowingAge());
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        return create_offspring(new EntityShark(ModEntity.SHARK, this.world));
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_COD_FLOP;
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (flag && this.getAnimation() == NO_ANIMATION && !this.isChild()) {
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

    public boolean hasShortFins(){ return (this.dataManager.get(SHORT_FINS)); }
    private void setShortFins(boolean short_fins){ this.dataManager.set(SHORT_FINS, short_fins); }

    public void writeAdditional(CompoundNBT compound){
        super.writeAdditional(compound);
        compound.putBoolean("hasShortFins", this.hasShortFins());
    }

    public void readAdditional(CompoundNBT compound){
        super.readAdditional(compound);
        this.setShortFins(compound.getBoolean("hasShortFins"));
    }
}
