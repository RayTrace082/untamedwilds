package untamedwilds.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.util.EntityUtils;

public abstract class ComplexMobTerrestrial extends ComplexMob implements IAnimatedEntity {

    public int sitProgress; // A counter which defines the progress towards the Sitting Poses
    public int ticksToSit;
    public int sleepProgress; // A counter which defines the progress towards the Sleeping Poses
    public int forceSleep;
    protected int tiredCounter = 0;
    protected int buoyancy = 1;
    private static final DataParameter<Integer> HUNGER = EntityDataManager.createKey(ComplexMobTerrestrial.class, DataSerializers.VARINT);
    private int animationTick;
    private Animation currentAnimation;
    public float turn_speed = 0.2F;
    protected float swimSpeedMult = 1.0F;

    public ComplexMobTerrestrial(EntityType<? extends ComplexMob> type, World worldIn){
        super(type, worldIn);
        this.moveController = new ComplexMobTerrestrial.MoveHelperController(this);
        this.ticksToSit = 40;
    }

    @Override
    protected void registerData(){
        super.registerData();
        this.dataManager.register(HUNGER, 79); // One point less than the breeding threshold
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

            if (this.getAir() < 40 && this.ticksExisted % 10 == 0) { // TODO: There's probably a better place to dump this (mobs about to drown will go to the surface for air), but it refuses to work everywhere else
                this.jump();
            }

            if (!this.isSleeping() && this.forceSleep > 0) {
                this.setSleeping(true);
            }
            if (this.ticksExisted % 200 == 0) {
                if (!this.isActive() && this.getNavigator().noPath()) {
                    this.tiredCounter++;
                    if (this.getDistanceSq(this.getHomeAsVec()) <= 6) {
                        this.setSleeping(true);
                        this.tiredCounter = 0;
                    }
                    else if (tiredCounter >= 3) {
                        this.setHome(BlockPos.ZERO);
                        this.tiredCounter = 0;
                    }
                    this.moveController.setMoveTo(this.getHome().getX(), this.getHome().getY(), this.getHome().getZ(), 1f);
                }
            }
        }
        if (this.isSitting() && this.sitProgress < this.ticksToSit) {
            this.sitProgress++;
        } else if (!this.isSitting() && this.sitProgress > 0) {
            this.sitProgress--;
        }
        if (this.isSleeping() && this.sleepProgress < 40) {
            this.sleepProgress++;
        } else if (!this.isSleeping() && this.sleepProgress > 0) {
            this.sleepProgress--;
        }
        super.livingTick();
    }

    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (!this.isChild() && this.isFavouriteFood(itemstack) && !this.dead) {
            if (!this.world.isRemote && !player.isCreative()) {
                itemstack.shrink(1);
            }
            if (ConfigGamerules.playerBreeding.get() && this.growingAge == 0) {
                this.setInLove(player);
                EntityUtils.spawnParticlesOnEntity(this.world, this, ParticleTypes.HEART, 7, 1);
            }
            this.setAnimation(this.getAnimationEat());
            this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_GENERIC_EAT, this.getSoundCategory(), 1F, 1, true);
            return ActionResultType.CONSUME;
        }

        return super.func_230254_b_(player, hand);
    }

    protected activityType getActivityType() {
        return activityType.INSOMNIAC;
    }

    public boolean isActive() {
        activityType type = this.getActivityType();
        Pair<Integer, Integer> times = type.getTimes();
        if ((this.isTamed() && this.getCommandInt() != 0) || !ConfigGamerules.sleepBehaviour.get()) {
            return true;
        }
        if (type == activityType.CATHEMERAL) {
            return this.ticksExisted % 17000 < 3000;
        }
        long time = this.world.getDayTime();
        if (!times.getFirst().equals(times.getSecond())) {
            if (times.getFirst() > times.getSecond()) {
                return time > times.getFirst() || time < times.getSecond();
            }
            else {
                return time > times.getFirst() && time < times.getSecond();
            }
        }
        return this.forceSleep >= 0;
    }

    protected enum activityType implements IStringSerializable {
        DIURNAL         (1000, 16000), // From 7 AM to 10 PM
        NOCTURNAL       (13000, 4000), // From 7 PM to 10 AM
        CREPUSCULAR     (8000, 23000), // From 14 PM to 5 AM ; 4000 - 19000 ???
        CATHEMERAL      (-1, -1),      // Random naps throughout the day
        INSOMNIAC       (-1, -1);      // No sleep, redundant, should just not add the GoToSleepGoal

        public int wakeUp;
        public int sleep;

        activityType(int wakeUp, int sleep) {
            this.wakeUp = wakeUp;
            this.sleep = sleep;
        }

        public Pair<Integer, Integer> getTimes() {
            return new Pair<>(this.wakeUp, this.sleep);
        }

        public String getString() { return null; }
    }

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
        if (!this.world.isRemote && !ConfigGamerules.hardcoreDeath.get() && this.getHome() != BlockPos.ZERO && this.isTamed() && this.getHunger() != 0) {
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
    public void travel(Vector3d destination) {
        if (this.isServerWorld() || this.canPassengerSteer()) {
            ModifiableAttributeInstance gravity = this.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
            boolean flag = this.getMotion().y <= 0.0D;
            /*if (flag && this.isPotionActive(Effects.SLOW_FALLING)) {
                if (!gravity.hasModifier(SLOW_FALLING)) gravity.applyNonPersistentModifier(SLOW_FALLING);
                this.fallDistance = 0.0F;
            } else if (gravity.hasModifier(SLOW_FALLING)) {
                gravity.removeModifier(SLOW_FALLING);
            }*/
            double d0 = gravity.getValue();

            FluidState fluidstate = this.world.getFluidState(this.getPosition());
            if (this.isInWater() && this.func_241208_cS_() && !this.func_230285_a_(fluidstate.getFluid())) {
                double d8 = this.getPosY();
                float f5 = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
                float f6 = 0.045F; // EDITED: Was 0.02
                float f7 = (float)EnchantmentHelper.getDepthStriderModifier(this);

                if (!this.onGround) {
                    f7 *= 0.7F;
                }

                if (f7 > 0.0F) {
                    if (f7 > 3.0F) {
                        f7 = 3.0F;
                    }
                    f5 += (0.54600006F - f5) * f7 / 3.0F;
                    f6 += (this.getAIMoveSpeed() - f6) * f7 / 3.0F;
                }

                if (this.isPotionActive(Effects.DOLPHINS_GRACE)) {
                    f5 = 0.96F;
                }

                f6 *= (float)this.getAttribute(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()).getValue();
                this.moveRelative(f6, destination);
                this.move(MoverType.SELF, this.getMotion());
                Vector3d vector3d6 = this.getMotion();
                if (this.collidedHorizontally && this.isOnLadder()) {
                    vector3d6 = new Vector3d(vector3d6.x, 0.2D, vector3d6.z);
                }

                this.setMotion(vector3d6.mul(f5, 0.8F, f5));
                Vector3d vector3d2 = this.func_233626_a_(d0, flag, this.getMotion());
                this.setMotion(vector3d2);
                if (this.collidedHorizontally && this.isOffsetPositionInLiquid(vector3d2.x, vector3d2.y + (double)0.6F - this.getPosY() + d8, vector3d2.z)) {
                    this.setMotion(vector3d2.x, 0.3F, vector3d2.z);
                }
            } else if (this.isInLava() && this.func_241208_cS_() && !this.func_230285_a_(fluidstate.getFluid())) {
                double d7 = this.getPosY();
                this.moveRelative(0.02F, destination);
                this.move(MoverType.SELF, this.getMotion());
                if (this.func_233571_b_(FluidTags.LAVA) <= this.func_233579_cu_()) {
                    this.setMotion(this.getMotion().mul(0.5D, 0.8F, 0.5D));
                    this.setMotion(this.func_233626_a_(d0, flag, this.getMotion()));
                } else {
                    this.setMotion(this.getMotion().scale(0.5D));
                }

                if (!this.hasNoGravity()) {
                    this.setMotion(this.getMotion().add(0.0D, -d0 / 4.0D, 0.0D));
                }

                Vector3d vector3d4 = this.getMotion();
                if (this.collidedHorizontally && this.isOffsetPositionInLiquid(vector3d4.x, vector3d4.y + (double)0.6F - this.getPosY() + d7, vector3d4.z)) {
                    this.setMotion(vector3d4.x, 0.3F, vector3d4.z);
                }
            } else {
                BlockPos blockpos = this.getPositionUnderneath();
                float f3 = this.world.getBlockState(this.getPositionUnderneath()).getSlipperiness(world, this.getPositionUnderneath(), this);
                float f4 = this.onGround ? f3 * 0.91F : 0.91F;
                Vector3d vector3d5 = this.func_233633_a_(destination, f3);
                double d2 = vector3d5.y;
                if (this.isPotionActive(Effects.LEVITATION)) {
                    d2 += (0.05D * (double)(this.getActivePotionEffect(Effects.LEVITATION).getAmplifier() + 1) - vector3d5.y) * 0.2D;
                    this.fallDistance = 0.0F;
                } else if (this.world.isRemote && !this.world.isBlockLoaded(blockpos)) {
                    if (this.getPosY() > 0.0D) {
                        d2 = -0.1D;
                    } else {
                        d2 = 0.0D;
                    }
                } else if (!this.hasNoGravity()) {
                    d2 -= d0;
                }

                this.setMotion(vector3d5.x * (double)f4, d2 * (double)0.98F, vector3d5.z * (double)f4);
            }
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d5 = this.getPosX() - this.prevPosX;
        double d6 = this.getPosZ() - this.prevPosZ;
        double d8 = this instanceof IFlyingAnimal ? this.getPosY() - this.prevPosY : 0.0D;
        float f8 = MathHelper.sqrt(d5 * d5 + d8 * d8 + d6 * d6) * 4.0F;
        if (f8 > 1.0F) {
            f8 = 1.0F;
        }

        this.limbSwingAmount += (f8 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
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
    }

    public void readAdditional(CompoundNBT compound){
        super.readAdditional(compound);
        this.setSleeping(compound.getBoolean("Sleeping"));
        this.forceSleep = compound.getInt("SleepingTicks");
        this.setSitting(compound.getBoolean("Sitting"));
        this.setHunger(compound.getInt("Hunger"));
    }

    static class MoveHelperController extends MovementController {
        private final ComplexMobTerrestrial entity;

        MoveHelperController(ComplexMobTerrestrial turtleIn) {
            super(turtleIn);
            this.entity = turtleIn;
        }

        @Override
        public void tick() {
            if (this.action == MovementController.Action.STRAFE) {
                float f = (float)this.entity.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
                float f1 = (float)this.speed * f;
                float f2 = this.moveForward;
                float f3 = this.moveStrafe;
                float f4 = MathHelper.sqrt(f2 * f2 + f3 * f3);
                if (f4 < 1.0F) {
                    f4 = 1.0F;
                }

                f4 = f1 / f4;
                f2 = f2 * f4;
                f3 = f3 * f4;
                float f5 = MathHelper.sin(this.entity.rotationYaw * ((float)Math.PI / 180F));
                float f6 = MathHelper.cos(this.entity.rotationYaw * ((float)Math.PI / 180F));
                float f7 = f2 * f6 - f3 * f5;
                float f8 = f3 * f6 + f2 * f5;
                PathNavigator pathnavigator = this.entity.getNavigator();
                NodeProcessor nodeprocessor = pathnavigator.getNodeProcessor();
                if (nodeprocessor.getPathNodeType(this.entity.world, MathHelper.floor(this.entity.getPosX() + (double) f7), MathHelper.floor(this.entity.getPosY()), MathHelper.floor(this.entity.getPosZ() + (double) f8)) != PathNodeType.WALKABLE) {
                    this.moveForward = (float) this.getSpeed(); // EDITED: was hardcoded to 1.0F
                    this.moveStrafe = 0.0F;
                    f1 = f;
                }

                this.entity.setAIMoveSpeed(f1);
                this.entity.setMoveForward(this.moveForward);
                this.entity.setMoveStrafing(this.moveStrafe);
                this.action = MovementController.Action.WAIT;

            } else if (this.action == MovementController.Action.MOVE_TO) {
                this.action = MovementController.Action.WAIT;
                /*if (this.entity.getAttackTarget() != null) {
                    if (this.entity.canEntityBeSeen(this.entity.getAttackTarget())) {
                        this.posX = this.entity.getAttackTarget().getPosX();
                        this.posY = this.entity.getAttackTarget().getPosY();
                        this.posZ = this.entity.getAttackTarget().getPosZ();
                    }
                }*/
                double d0 = this.posX - this.entity.getPosX();
                double d1 = this.posZ - this.entity.getPosZ();
                double d2 = this.posY - this.entity.getPosY();
                double d3 = d0 * d0 + d2 * d2 + d1 * d1;
                if (d3 < (double)2.5000003E-7F) {
                    this.entity.setMoveForward(0.0F);
                    return;
                }

                float f = (float)(MathHelper.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f, this.entity.turn_speed * 100);
                BlockPos blockpos = this.entity.getPosition();
                BlockState blockstate = this.entity.world.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                VoxelShape voxelshape = blockstate.getCollisionShape(this.entity.world, blockpos);

                if (this.entity.isInWater()) {
                    if (this.entity.collidedHorizontally && this.entity.ticksExisted % 10 == 0) {
                        this.entity.jump();
                    }
                    //this.entity.setMotion(this.entity.getMotion().scale(1.1));
                    float f2 = -((float)(MathHelper.atan2(d1, MathHelper.sqrt(d0 * d0 + d2 * d2)) * (double)(180F / (float)Math.PI)));
                    f2 = MathHelper.clamp(MathHelper.wrapDegrees(f2), -85.0F, 85.0F);
                    this.entity.rotationPitch = this.limitAngle(this.entity.rotationPitch, f2, 1.0F);
                    float f4 = MathHelper.sin(this.entity.rotationPitch * ((float)Math.PI / 180F));
                    this.entity.moveVertical = -f4 *  (float)this.entity.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
                    if (this.entity.getAttackTarget() == null) {
                        this.entity.setMotion(this.entity.getMotion().add(0.0D, this.entity.buoyancy - 1, 0.0D));
                    }
                    //this.entity.rotationPitch = this.limitAngle(this.entity.rotationPitch, (float) (this.entity.getMotion().getY() * -40), 0.1F);
                }

                //((ServerWorld)this.entity.world).spawnParticle(ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0.15F);
                float f1 = (float)(this.getSpeed() * this.entity.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
                this.entity.setAIMoveSpeed(MathHelper.lerp(0.125F, this.entity.getAIMoveSpeed(), f1));

                if (d2 > (double)this.entity.stepHeight && d0 * d0 + d1 * d1 < (double)Math.max(1.0F, this.entity.getWidth()) || !voxelshape.isEmpty() && this.entity.getPosY() < voxelshape.getEnd(Direction.Axis.Y) + (double)blockpos.getY() && !block.isIn(BlockTags.DOORS) && !block.isIn(BlockTags.FENCES)) {
                    this.entity.getJumpController().setJumping();
                    this.action = MovementController.Action.JUMPING;
                }
            } else if (this.action == MovementController.Action.JUMPING) {
                this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));
                if (this.entity.onGround) {
                    this.action = MovementController.Action.WAIT;
                }
            } else {
                if (this.entity.isInWater() && this.entity.getAttackTarget() == null && !this.entity.collidedHorizontally) {
                    this.entity.setMotion(this.entity.getMotion().add(0.0D, this.entity.buoyancy - 1, 0.0D));
                }
                this.entity.setMoveForward(0.0F);
            }
        }
    }
}
