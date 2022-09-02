package untamedwilds.entity.arthropod;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAmphibious;
import untamedwilds.entity.INewSkins;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.SmartAvoidGoal;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.SmartWanderGoal;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntityKingCrab extends ComplexMobAmphibious implements ISpecies, INewSkins, IAnimatedEntity {

    private int animationTick;
    private Animation currentAnimation;
    public static Animation EAT_LEFT;
    public static Animation EAT_RIGHT;
    public static Animation EAT_BOTH;

    public EntityKingCrab(EntityType<? extends EntityKingCrab> type, Level worldIn) {
        super(type, worldIn);
        //this.moveControl = new SmartSwimmingMoveControl(this, 40, 5, 1F, 0.6F, true);
        EAT_LEFT = Animation.create(56);
        EAT_RIGHT = Animation.create(56);
        EAT_BOTH = Animation.create(80);
        this.maxUpStep = 1;
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0D)
                .add(Attributes.ARMOR, 4D);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.3D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D,  input -> getEcoLevel(input) > getEcoLevel(this)));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D, 240));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    public float getWalkTargetValue(BlockPos p_149140_, LevelReader p_149141_) {
        return 0.0F;
    }

    public boolean wantsToBeOnLand() { return this.level.isRainingAt(this.blockPosition()); }

    public boolean wantsToBeInWater() { return true; }

    public boolean isPushedByFluid() {
        return false;
    }

    //public float getWaterSlowDown() { }

    public void aiStep() {
        super.aiStep();
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (!this.level.isClientSide) {
            if (this.tickCount % 1000 == 0) {
                if (this.wantsToBreed() && !this.isMale()) {
                    this.breed();
                }
            }
            if (this.isInWater()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
            if (this.level.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
            if (this.isInWater() && this.getAnimation() == NO_ANIMATION && this.getTarget() == null && this.level.getBlockState(this.blockPosition().below()).is(BlockTags.MINEABLE_WITH_SHOVEL)) {
                if (this.getCommandInt() == 0) {
                    int i = this.random.nextInt(3000);
                    if (i > 2940 && i < 2960)
                        this.setAnimation(EAT_RIGHT);
                    if (i > 2960 && i < 2980)
                        this.setAnimation(EAT_LEFT);
                    if (i > 2980)
                        this.setAnimation(EAT_BOTH);
                }
            }
            if (this.getAnimation() != NO_ANIMATION) {
                if (((this.getAnimation() == EAT_LEFT || this.getAnimation() == EAT_RIGHT) && (this.getAnimationTick() == 20)) || (this.getAnimation() == EAT_BOTH && (this.getAnimationTick() == 20 || this.getAnimationTick() == 44))) {
                    ((ServerLevel)this.level).sendParticles(new BlockParticleOption(ParticleTypes.FALLING_DUST, this.level.getBlockState(this.blockPosition().below())), this.getX(), this.getY(), this.getZ(), 3, 0.0D, 0.0D, 0.0D, 0.15F);
                    this.playSound(SoundEvents.SHOVEL_FLATTEN, 0.2F, 0.7F);
                }
            }
        }
        if (this.level.isClientSide()) {

        }
    }

    /* Breeding conditions for the Tarantula are:
     * A nearby Tarantula of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.isInWater() && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
            List<EntityKingCrab> list = this.level.getEntitiesOfClass(EntityKingCrab.class, this.getBoundingBox().inflate(6.0D, 4.0D, 6.0D));
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
        EntityUtils.dropEggs(this, "egg_king_crab", this.getOffspring());
        return null;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (hand == InteractionHand.MAIN_HAND) {
            if (itemstack.getItem().equals(Items.WATER_BUCKET) && this.isAlive()) {
                EntityUtils.mutateEntityIntoItem(this, player, hand, "bucket_king_crab", itemstack);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        }
        return super.mobInteract(player, hand);
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    public int getAnimationTick() { return animationTick; }
    public void setAnimationTick(int tick) { animationTick = tick; }
    public Animation getAnimation() { return currentAnimation; }
    public void setAnimation(Animation animation) { currentAnimation = animation; }
    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, EAT_RIGHT, EAT_LEFT, EAT_BOTH};
    }
}
