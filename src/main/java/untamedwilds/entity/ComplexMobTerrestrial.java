package untamedwilds.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import untamedwilds.config.ConfigGamerules;

public abstract class ComplexMobTerrestrial extends ComplexMob implements IAnimatedEntity {

    private static final DataParameter<Boolean> SLEEPING = EntityDataManager.createKey(ComplexMobTerrestrial.class, DataSerializers.BOOLEAN);
    public int sitProgress; // A counter which defines the progress towards the Sitting Poses
    public int sleepProgress; // A counter which defines the progress towards the Sleeping Poses
    protected int forceSleep;
    protected int tiredCounter = 0;
    protected int buoyancy = 1;
    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(ComplexMobTerrestrial.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> HUNGER = EntityDataManager.createKey(ComplexMobTerrestrial.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> IS_RUNNING = EntityDataManager.createKey(ComplexMobTerrestrial.class, DataSerializers.BOOLEAN); // TODO: Deprecated, may be redundant if we use Speed to determine the running state
    private int animationTick;
    private Animation currentAnimation;
    public float dexterity = 0.2F;
    protected float swimSpeedMult = 1.0F;

    public ComplexMobTerrestrial(EntityType<? extends ComplexMob> type, World worldIn){
        super(type, worldIn);
        this.moveController = new ComplexMobTerrestrial.MoveHelperController(this);
    }

    @Override
    protected void registerData(){
        super.registerData();
        this.dataManager.register(SLEEPING, false);
        this.dataManager.register(SITTING, false);
        this.dataManager.register(HUNGER, 79); // One point less than the breeding threshold
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
    public void travel(Vec3d destination) {
        if (this.isServerWorld() || this.canPassengerSteer()) {
            IAttributeInstance gravity = this.getAttribute(ENTITY_GRAVITY);
            boolean flag = this.getMotion().y <= 0.0D;
            /*if (flag && this.isPotionActive(Effects.SLOW_FALLING)) { // Private REEEE
                if (!gravity.hasModifier(SLOW_FALLING)) gravity.applyModifier(SLOW_FALLING);
                this.fallDistance = 0.0F;
            } else if (gravity.hasModifier(SLOW_FALLING)) {
                gravity.removeModifier(SLOW_FALLING);
            }*/
            double d0 = gravity.getValue();

            if (!this.isInWater()) {
                if (!this.isInLava()) {
                    /*if (this.isElytraFlying()) {
                        Vec3d vec3d3 = this.getMotion();
                        if (vec3d3.y > -0.5D) {
                            this.fallDistance = 1.0F;
                        }

                        Vec3d vec3d = this.getLookVec();
                        float f6 = this.rotationPitch * ((float)Math.PI / 180F);
                        double d9 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
                        double d11 = Math.sqrt(horizontalMag(vec3d3));
                        double d12 = vec3d.length();
                        float f3 = MathHelper.cos(f6);
                        f3 = (float)((double)f3 * (double)f3 * Math.min(1.0D, d12 / 0.4D));
                        vec3d3 = this.getMotion().add(0.0D, d0 * (-1.0D + (double)f3 * 0.75D), 0.0D);
                        if (vec3d3.y < 0.0D && d9 > 0.0D) {
                            double d3 = vec3d3.y * -0.1D * (double)f3;
                            vec3d3 = vec3d3.add(vec3d.x * d3 / d9, d3, vec3d.z * d3 / d9);
                        }

                        if (f6 < 0.0F && d9 > 0.0D) {
                            double d13 = d11 * (double)(-MathHelper.sin(f6)) * 0.04D;
                            vec3d3 = vec3d3.add(-vec3d.x * d13 / d9, d13 * 3.2D, -vec3d.z * d13 / d9);
                        }

                        if (d9 > 0.0D) {
                            vec3d3 = vec3d3.add((vec3d.x / d9 * d11 - vec3d3.x) * 0.1D, 0.0D, (vec3d.z / d9 * d11 - vec3d3.z) * 0.1D);
                        }

                        this.setMotion(vec3d3.mul((double)0.99F, (double)0.98F, (double)0.99F));
                        this.move(MoverType.SELF, this.getMotion());
                        if (this.collidedHorizontally && !this.world.isRemote) {
                            double d14 = Math.sqrt(horizontalMag(this.getMotion()));
                            double d4 = d11 - d14;
                            float f4 = (float)(d4 * 10.0D - 3.0D);
                            if (f4 > 0.0F) {
                                this.playSound(this.getFallSound((int)f4), 1.0F, 1.0F);
                                this.attackEntityFrom(DamageSource.FLY_INTO_WALL, f4);
                            }
                        }

                        if (this.onGround && !this.world.isRemote) {
                            this.setFlag(7, false);
                        }
                    }
                    else {*/
                    BlockPos blockpos = this.getPositionUnderneath();
                    float f5 = this.world.getBlockState(blockpos).getSlipperiness(world, blockpos, this);
                    float f7 = this.onGround ? f5 * 0.91F : 0.91F;
                    this.moveRelative(this.onGround ? this.getAIMoveSpeed() * (0.21600002F / (f5 * f5 * f5)) : this.jumpMovementFactor, destination);
                    //this.setMotion(this.func_213362_f(this.getMotion())); // Private REEEE
                    this.setMotion(this.getMotion());
                    this.move(MoverType.SELF, this.getMotion());
                    Vec3d vec3d5 = this.getMotion();
                    if ((this.collidedHorizontally || this.isJumping) && this.isOnLadder()) {
                        vec3d5 = new Vec3d(vec3d5.x, 0.2D, vec3d5.z);
                    }

                    double d10 = vec3d5.y;
                    if (this.isPotionActive(Effects.LEVITATION)) {
                        d10 += (0.05D * (double)(this.getActivePotionEffect(Effects.LEVITATION).getAmplifier() + 1) - vec3d5.y) * 0.2D;
                        this.fallDistance = 0.0F;
                    } else if (this.world.isRemote && !this.world.isBlockLoaded(blockpos)) {
                        if (this.getPosY() > 0.0D) {
                            d10 = -0.1D;
                        } else {
                            d10 = 0.0D;
                        }
                    } else if (!this.hasNoGravity()) {
                        d10 -= d0;
                    }

                    this.setMotion(vec3d5.x * (double)f7, d10 * (double)0.98F, vec3d5.z * (double)f7);

                } else {
                    double d7 = this.getPosY();
                    this.moveRelative(0.02F, destination);
                    this.move(MoverType.SELF, this.getMotion());
                    this.setMotion(this.getMotion().scale(0.5D));
                    if (!this.hasNoGravity()) {
                        this.setMotion(this.getMotion().add(0.0D, -d0 / 4.0D, 0.0D));
                    }

                    Vec3d vec3d4 = this.getMotion();
                    if (this.collidedHorizontally && this.isOffsetPositionInLiquid(vec3d4.x, vec3d4.y + (double)0.6F - this.getPosY() + d7, vec3d4.z)) {
                        this.setMotion(vec3d4.x, 0.3F, vec3d4.z);
                    }
                }
            } else {
                double d1 = this.getPosY();
                float f = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
                float f1 = 0.04F; // EDITED: Was 0.02
                float f2 = (float) EnchantmentHelper.getDepthStriderModifier(this);
                if (!this.onGround) {
                    f2 *= 0.5F; // EDITED: Disabled speed halving if the mob is not on solid ground, unnecessary
                }
                /*else {
                    f *= 1.1f; // EDITED: Mobs on the ground get slightly faster
                }*/
                if (f2 > 0.0F) {
                    if (f2 > 3.0F) {
                        f2 = 3.0F;
                    }
                    f += (0.54600006F - f) * f2 / 3.0F;
                    f1 += (this.getAIMoveSpeed() - f1) * f2 / 3.0F;
                }
                // EDITED: Removed Dolphin's Grace effect

                f1 *= (float)this.getAttribute(SWIM_SPEED).getValue();
                f1 *= this.swimSpeedMult;
                this.moveRelative(f1, destination);
                this.move(MoverType.SELF, this.getMotion());
                Vec3d vec3d1 = this.getMotion();
                if (this.collidedHorizontally && this.isOnLadder()) {
                    vec3d1 = new Vec3d(vec3d1.x, 0.2D, vec3d1.z);
                }

                this.setMotion(vec3d1.mul(f, 0.8F, f));
                if (!this.hasNoGravity()) { // EDITED: Removed "&& !this.isSprinting()" from the check, does not cover ComplexMobs
                    Vec3d vec3d2 = this.getMotion();
                    double d2;
                    if (flag && Math.abs(vec3d2.y - 0.005D) >= 0.003D && Math.abs(vec3d2.y - d0 / 16.0D) < 0.003D) {
                        d2 = -0.003D;
                    } else {
                        d2 = vec3d2.y - d0 / 16.0D;
                    }

                    this.setMotion(vec3d2.x, d2, vec3d2.z);
                }

                Vec3d vec3d6 = this.getMotion();
                if (this.collidedHorizontally && this.isOffsetPositionInLiquid(vec3d6.x, vec3d6.y + (double)0.6F - this.getPosY() + d1, vec3d6.z)) {
                    this.setMotion(vec3d6.x, (double)0.3F, vec3d6.z);
                }
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
        private final ComplexMobTerrestrial entity;

        MoveHelperController(ComplexMobTerrestrial turtleIn) {
            super(turtleIn);
            this.entity = turtleIn;
        }

        @Override
        public void tick() {
            //this.updateSpeed();
            if (this.action == MovementController.Action.STRAFE) {
                float f = (float)this.entity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
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
                if (this.entity.getAttackTarget() != null) {
                    if (this.entity.canEntityBeSeen(this.entity.getAttackTarget())) {
                        this.posX = this.entity.getAttackTarget().getPosX();
                        this.posY = this.entity.getAttackTarget().getPosY();
                        this.posZ = this.entity.getAttackTarget().getPosZ();
                    }
                }
                double d0 = this.posX - this.entity.getPosX();
                double d1 = this.posZ - this.entity.getPosZ();
                double d2 = this.posY - this.entity.getPosY();
                double d3 = d0 * d0 + d2 * d2 + d1 * d1;
                if (d3 < (double)2.5000003E-7F) {
                    this.entity.setMoveForward(0.0F);
                    return;
                }

                float f = (float)(MathHelper.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f, this.entity.dexterity * 100);
                BlockPos blockpos = new BlockPos(this.entity);
                BlockState blockstate = this.entity.world.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                VoxelShape voxelshape = blockstate.getCollisionShape(this.entity.world, blockpos);

                if (this.entity.isInWater()) {
                    //this.entity.setMotion(this.entity.getMotion().scale(1.1));
                    float f2 = -((float)(MathHelper.atan2(d1, MathHelper.sqrt(d0 * d0 + d2 * d2)) * (double)(180F / (float)Math.PI)));
                    f2 = MathHelper.clamp(MathHelper.wrapDegrees(f2), -85.0F, 85.0F);
                    this.entity.rotationPitch = this.limitAngle(this.entity.rotationPitch, f2, 5.0F);
                    float f4 = MathHelper.sin(this.entity.rotationPitch * ((float)Math.PI / 180F));
                    this.entity.moveVertical = -f4 *  (float)this.entity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
                    if (this.entity.getAttackTarget() == null) {
                        this.entity.setMotion(this.entity.getMotion().add(0.0D, this.entity.buoyancy - 1, 0.0D));
                    }
                    //this.entity.rotationPitch = this.limitAngle(this.entity.rotationPitch, (float) (this.entity.getMotion().getY() * -40), 5.0F);
                }

                //((ServerWorld)this.entity.world).spawnParticle(ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0.15F);
                float f1 = (float)(this.getSpeed() * this.entity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
                this.entity.setAIMoveSpeed(MathHelper.lerp(0.125F, this.entity.getAIMoveSpeed(), f1));


                // 0.033 seems to be the max for Bear underwater, 0.035 on land
                // 0.035 seems to be the max value for Big Cats underwater, 0.040 on land
                // UntamedWilds.LOGGER.info(this.entity.getMotion());

                if (d2 > (double)this.entity.stepHeight && d0 * d0 + d1 * d1 < (double)Math.max(1.0F, this.entity.getWidth()) || !voxelshape.isEmpty() && this.entity.getPosY() < voxelshape.getEnd(Direction.Axis.Y) + (double)blockpos.getY() && !block.isIn(BlockTags.DOORS) && !block.isIn(BlockTags.FENCES)) {
                    this.entity.getJumpController().setJumping();
                    this.action = MovementController.Action.JUMPING;
                }
            } else if (this.action == MovementController.Action.JUMPING) {
                this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));
                if (this.entity.onGround) {
                    this.action = MovementController.Action.WAIT;
                }
            } else {
                if (this.entity.isInWater() && this.entity.getAttackTarget() == null) {
                    this.entity.setMotion(this.entity.getMotion().add(0.0D, this.entity.buoyancy - 1, 0.0D));
                }
                this.entity.setMoveForward(0.0F);
            }
        }
    }
}
