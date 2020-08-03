package untamedwilds.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import untamedwilds.config.ConfigGamerules;

public abstract class ComplexMobTerrestrial extends ComplexMob implements IAnimatedEntity {

    private static final DataParameter<Boolean> SLEEPING = EntityDataManager.createKey(ComplexMobTerrestrial.class, DataSerializers.BOOLEAN);
    public int sitProgress; // A counter which defines the progress towards the Sitting Poses
    public int sleepProgress; // A counter which defines the progress towards the Sleeping Poses
    protected int forceSleep;
    protected int tiredCounter = 0;
    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(ComplexMobTerrestrial.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> HUNGER = EntityDataManager.createKey(ComplexMobTerrestrial.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> IS_RUNNING = EntityDataManager.createKey(ComplexMobTerrestrial.class, DataSerializers.BOOLEAN); // TODO: Deprecated, may be redundant if we use Speed to determine the running state
    private int animationTick;
    private Animation currentAnimation;
    public float dexterity = 0.2F;

    public ComplexMobTerrestrial(EntityType<? extends ComplexMob> type, World worldIn){
        super(type, worldIn);
        this.moveController = new ComplexMobTerrestrial.MoveHelperController(this);
    }

    @Override
    protected void registerData(){
        super.registerData();
        this.dataManager.register(SLEEPING, false);
        this.dataManager.register(SITTING, false);
        this.dataManager.register(HUNGER, 79); // Why 79? because it's one point less than the breeding threshold
        this.dataManager.register(IS_RUNNING, false);
    }

    public void livingTick() {
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (!world.isRemote) {
            if (this.forceSleep > 0) {
                this.forceSleep--;
            }
            else if (this.forceSleep < 0) {
                this.forceSleep++;
            }
            if (!this.getNavigator().noPath() && (this.isSitting() || this.isSleeping())) {
                this.setSitting(false);
                this.setSleeping(false);
            }
            if (this.isSleeping() && this.forceSleep <= 0 && this.isActive()) {
                this.setSleeping(false);
            }
            if (!this.isSleeping() && this.forceSleep > 0) {
                this.setSleeping(true);
            }
        }
        if (this.isSitting() && this.sitProgress < 40) {
            this.sitProgress++;
        } else if (!this.isSitting() && this.sitProgress > 0) {
            this.sitProgress--;
        }
        if (this.isSleeping() && this.sleepProgress < 40) {
            this.sleepProgress++;
        } else if (!this.isSleeping() && this.sleepProgress > 0) {
            this.sleepProgress--;
        }
        if (this.isRunning() && !world.isRemote && this.isNotMoving()) {
            this.setRunning(false);
        }
        super.livingTick();
    }

    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (!this.isChild() && this.isFavouriteFood(itemstack) && !this.dead) {
            if (!this.world.isRemote && !player.isCreative()) {
                itemstack.shrink(1);
            }
            if (ConfigGamerules.playerBreeding.get() && this.growingAge == 0) {
                this.setInLove(player);
                for (int i = 0; i < 7; ++i) {
                    double d0 = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    this.world.addParticle(ParticleTypes.HEART, this.getPosX() + (double)(this.rand.nextFloat() * this.getWidth() * 2.0F) - (double)this.getWidth(), this.getPosY() + 0.5D + (double)(this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double)(this.rand.nextFloat() * this.getWidth() * 2.0F) - (double)this.getWidth(), d0, d1, d2);
                }
            }
            this.setAnimation(this.getAnimationEat());
            this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_GENERIC_EAT, this.getSoundCategory(), 1F, 1, true);
            return true;
        }
        return super.processInteract(player, hand);
    }

    private boolean isBlinking() {
        return this.ticksExisted % 60 > 53;
    }

    public boolean shouldRenderEyes() { return !this.isSleeping() && !this.dead && !this.isBlinking() && this.hurtTime == 0; }

    public void setSleeping(boolean sleeping){ this.dataManager.set(SLEEPING, sleeping); }
    public boolean isSleeping(){ return (this.dataManager.get(SLEEPING)); }
    public boolean isActive() {
        if (this.isTamed()) {
            return true;
        }
        return this.forceSleep >= 0;
    }

    public void setSitting(boolean sitting){ this.dataManager.set(SITTING, sitting); }
    public boolean isSitting(){ return (this.dataManager.get(SITTING)); }

    public void setRunning(boolean running){ this.dataManager.set(IS_RUNNING, running); }
    public boolean isRunning(){ return (this.dataManager.get(IS_RUNNING)); }
    public boolean canMove() { return !this.isSitting() && !this.isSleeping(); }

    private void setHunger(int hunger){
        this.dataManager.set(HUNGER, hunger);
    }
    public int getHunger(){
        return (this.dataManager.get(HUNGER));
    }
    public boolean isStarving() { return this.getHunger() <= 0; }
    public void addHunger(int change) {
        int i = this.getHunger() + change;
        this.setHunger((i > 200) ? 200 : (Math.max(i, 0)));
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isSitting()) {
            this.setSitting(false);
        }
        if (this.isSleeping()) {
            this.setSleeping(false);
            this.forceSleep = -4000;
        }
        return super.attackEntityFrom(source, amount);
    }

    public void onDeath(DamageSource p_70645_1_) {
        if (!this.world.isRemote && ConfigGamerules.hardcoreDeath.get() && this.getHome() != BlockPos.ZERO && this.isTamed() && this.getHunger() != 0) {
            // TODO: Wiggle the mob a bit, to avoid teleporting inside a wall
            this.addPotionEffect(new EffectInstance(Effects.GLOWING, 800, 0));
            this.setHealth(0.5F);
            this.setHunger(0);
            if (!this.attemptTeleport(this.getHome().getX(), this.getHome().getY(), this.getHome().getZ(), true)){
                super.onDeath(p_70645_1_);
            }
        }
        else {
            super.onDeath(p_70645_1_);
        }
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }
    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }
    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }
    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }
    @Override
    public Animation[] getAnimations() {
        return new Animation[]{};
    }

    public Animation getAnimationEat() { return this.NO_ANIMATION; }

    public void writeAdditional(CompoundNBT compound){
        super.writeAdditional(compound);
        compound.putBoolean("Sleeping", this.isSleeping());
        compound.putInt("SleepingTicks", this.forceSleep);
        compound.putBoolean("Sitting", this.isSitting());
        compound.putInt("Hunger", this.getHunger());
        compound.putBoolean("isRunning", this.isRunning());
    }

    public void readAdditional(CompoundNBT compound){
        super.readAdditional(compound);
        this.setSleeping(compound.getBoolean("Sleeping"));
        this.forceSleep = compound.getInt("SleepingTicks");
        this.setSitting(compound.getBoolean("Sitting"));
        this.setHunger(compound.getInt("Hunger"));
        this.setRunning(compound.getBoolean("isRunning"));
    }

    static class MoveHelperController extends MovementController {
        private final ComplexMobTerrestrial turtle;

        MoveHelperController(ComplexMobTerrestrial turtleIn) {
            super(turtleIn);
            this.turtle = turtleIn;
        }

        private void updateSpeed() {
            float speed = (float)this.turtle.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
            speed *= this.getSpeed();
            //UntamedWilds.LOGGER.info(speed + " " + this.getSpeed() + " " + this.turtle.getAIMoveSpeed());
            if (this.turtle.isInWater()) {
                if (this.turtle.isChild()) {
                    this.turtle.setAIMoveSpeed((float) Math.max(speed / 2, this.getSpeed() / 2));
                }
                else {
                    this.turtle.setAIMoveSpeed(Math.max(speed, this.turtle.getAttackTarget() != null ? 2F : 0.8F));
                }
            } else if (this.turtle.onGround) {
                this.turtle.setAIMoveSpeed(speed);
            }
        }

        public void tick() {
            this.updateSpeed();
            if (this.turtle.isInWater() || (this.action == MovementController.Action.MOVE_TO && !this.turtle.getNavigator().noPath())) { // Yeah, fuck this shit, it's staying this way
                double d0 = this.posX - this.turtle.getPosX();
                //double d1 = this.posY - this.turtle.getPosY();
                double d2 = this.posZ - this.turtle.getPosZ();
                //double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                //d1 = d1 / d3;
                float f = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.turtle.rotationYaw = this.limitAngle(this.turtle.rotationYaw, f, this.turtle.dexterity * 100);
                this.turtle.renderYawOffset = this.turtle.rotationYaw;
                float f1 = (float)(this.speed * this.turtle.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
                // added
                /*if (!this.turtle.inWater && this.turtle.onGround) {
                    this.turtle.setAIMoveSpeed(Math.min(this.turtle.getAIMoveSpeed(), 0.15F));
                }*/
                this.turtle.setAIMoveSpeed(MathHelper.lerp(0.125F, this.turtle.getAIMoveSpeed(), f1));
                if (this.turtle.areEyesInFluid(FluidTags.WATER) || this.turtle.collidedHorizontally) {
                    this.turtle.getJumpController().setJumping();
                }
                /*else if (!this.turtle.areEyesInFluid(FluidTags.WATER)) {
                    this.turtle.setMotion(this.turtle.getMotion().add(0.0D, 0.001D, 0.0D));
                }*/
            } else {
                this.turtle.setAIMoveSpeed(0.0F);
            }
        }
    }
}
