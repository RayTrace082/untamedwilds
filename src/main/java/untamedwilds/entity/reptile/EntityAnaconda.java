package untamedwilds.entity.reptile;

import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.AmphibiousRandomSwimGoal;
import untamedwilds.entity.ai.AmphibiousTransition;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.SmartWanderGoal;
import untamedwilds.entity.ai.control.movement.SmartSwimmingMoveControl;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModItems;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntityAnaconda extends ComplexMobAmphibious implements ISpecies, INeedsPostUpdate, INewSkins {

    public final int length;
    public final EntityAnacondaPart[] anacondaParts;
    public final float[] buffer = new float[3];
    public int ringBufferIndex = -1;
    public final double[][] ringBuffer = new double[64][3];

    public EntityAnaconda(EntityType<? extends ComplexMobTerrestrial> type, Level worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.moveControl = new SmartSwimmingMoveControl(this, 20, 5, 0.3F, 0.4F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.ticksToSit = 40;
        this.length = getMultiparts();
        this.anacondaParts = new EntityAnacondaPart[this.length];
        for (int i = 0; i < this.length; i++) {
            this.anacondaParts[i] = new EntityAnacondaPart(this, this.getBbWidth(), this.getBbHeight());
        }
    }

    private void setPartPosition(EntityAnacondaPart part, double offsetX, double offsetY, double offsetZ) {
        part.setPos(this.getX() + offsetX * part.scale, this.getY() + offsetY * part.scale, this.getZ() + offsetZ * part.scale);
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public net.minecraftforge.entity.PartEntity<?>[] getParts() {
        return this.anacondaParts;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.6D)
                .add(Attributes.MOVEMENT_SPEED, 0.6D)
                .add(Attributes.FOLLOW_RANGE, 12.0D)
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
                .add(Attributes.ARMOR, 4D);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.3D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(3, new AmphibiousTransition(this, 1D));
        this.goalSelector.addGoal(4, new AmphibiousRandomSwimGoal(this, 0.7, 400));
        this.goalSelector.addGoal(4, new SmartWanderGoal(this, 0.7, false) {
            public boolean canUse() {
                if (this.creature.huntingCooldown != 0)
                    return false;
                return super.canUse();
            }
        });
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false, input -> getEcoLevel(input) < getEcoLevel(this)));
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            if (this.tickCount % 1000 == 0) {
                if (this.random.nextInt(40) == 0) {
                    this.spawnAtLocation(new ItemStack(ModItems.MATERIAL_SNAKE_SKIN.get()), 0.2F);
                }
            }
            if (this.level.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
            if (this.getAnimation() == NO_ANIMATION && this.getTarget() == null && !this.isSleeping()) {
                int i = this.random.nextInt(3000);
                if (i <= 10 && !this.isInWater() && this.isNotMoving() && this.canMove()) {
                    this.getNavigation().stop();
                    this.setSitting(true);
                }
                if ((i == 11 || this.isInWater() || this.isActive()) && this.isSitting()) {
                    this.setSitting(false);
                }
            }
            if (this.isInWater()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.006D, 0.0D));
            }
            this.setAngry(this.getTarget() != null);
        }

        if (!this.isNoAi() && !this.isBaby()) {
            if (this.canMove()) {
                if (this.ringBufferIndex < 0) {
                    for (int i = 0; i < this.ringBuffer.length; ++i) {
                        this.ringBuffer[i][0] = this.getYRot() ;
                        this.ringBuffer[i][1] = this.getY();
                    }
                }
                this.ringBufferIndex++;
                if (this.ringBufferIndex == this.ringBuffer.length) {
                    this.ringBufferIndex = 0;
                }
                this.ringBuffer[this.ringBufferIndex][0] = this.getYRot() ;
                this.ringBuffer[ringBufferIndex][1] = this.getY();
                Vector3d[] avector3d = new Vector3d[this.anacondaParts.length];

                for (int j = 0; j < this.anacondaParts.length; ++j) {
                    this.anacondaParts[j].collideWithNearbyEntities();
                    avector3d[j] = new Vector3d(this.anacondaParts[j].getX(), this.anacondaParts[j].getY(), this.anacondaParts[j].getZ());
                }
                float f15 = (float) (this.getMovementOffsets(5, 1.0F)[1] - this.getMovementOffsets(10, 1.0F)[1]) * 10.0F * ((float) Math.PI / 180F);
                float f16 = Mth.cos(f15);
                float yaw = this.getYRot()  * ((float) Math.PI / 180F);
                float pitch = this.getXRot() * ((float) Math.PI / 180F);
                float f3 = Mth.sin(yaw) * (1 - Math.abs(this.getXRot() / 90F));
                float f18 = Mth.cos(yaw) * (1 - Math.abs(this.getXRot() / 90F));

                double[] adouble = this.getMovementOffsets(5, 1.0F);

                for (int k = 0; k < getMultiparts(); ++k) {
                    EntityAnacondaPart anaconda_part = this.anacondaParts[k];

                    double[] adouble1 = this.getMovementOffsets(5 + k * 2, 1.0F);
                    float f7 = yaw + (float) Mth.wrapDegrees(adouble1[0] - adouble[0]) * ((float) Math.PI / 180F);
                    float f20 = Mth.sin(f7) * (1 - Math.abs(this.getXRot() / 90F));
                    float f21 = Mth.cos(f7) * (1 - Math.abs(this.getXRot() / 90F));
                    float f23 = k == 0 ? (float) (k + 1) : (k + 1) * -1;
                    float value = pitch * (k);
                    this.setPartPosition(anaconda_part, -(f3 * 0.5F + f20 * f23) * f16, value, (f18 * 0.5F + f21 * f23) * f16);

                    this.anacondaParts[k].xo = avector3d[k].x;
                    this.anacondaParts[k].yo = avector3d[k].y;
                    this.anacondaParts[k].zo = avector3d[k].z;
                    this.anacondaParts[k].xOld = avector3d[k].x;
                    this.anacondaParts[k].yOld = avector3d[k].y;
                    this.anacondaParts[k].zOld = avector3d[k].z;
                }
            }
            else {
                for (int k = 0; k < getMultiparts(); ++k) {
                    EntityAnacondaPart anaconda_part = this.anacondaParts[k];
                    this.setPartPosition(anaconda_part, 0, 0, 0);
                    this.anacondaParts[k].xo = this.getX();
                    this.anacondaParts[k].yo = this.getY();
                    this.anacondaParts[k].zo = this.getZ();
                    this.anacondaParts[k].xOld = this.getX();
                    this.anacondaParts[k].yOld = this.getY();
                    this.anacondaParts[k].zOld = this.getZ();
                }
            }
        }
    }

    public double[] getMovementOffsets(int p_70974_1_, float partialTicks) {
        if (this.isDeadOrDying()) {
            partialTicks = 0.0F;
        }
        partialTicks = 1.0F - partialTicks;
        int i = this.ringBufferIndex - p_70974_1_ & 63;
        int j = this.ringBufferIndex - p_70974_1_ - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.ringBuffer[i][0];
        double d1 = this.ringBuffer[j][0] - d0;
        adouble[0] = d0 + d1 * (double) partialTicks;
        d0 = this.ringBuffer[i][1];
        d1 = this.ringBuffer[j][1] - d0;
        adouble[1] = d0 + d1 * (double) partialTicks;
        adouble[2] = Mth.lerp(partialTicks, this.ringBuffer[i][2], this.ringBuffer[j][2]);
        return adouble;
    }

    /* Breeding conditions for the Snake are:
     * A nearby Snake of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
                List<EntityAnaconda> list = this.level.getEntitiesOfClass(EntityAnaconda.class, this.getBoundingBox().inflate(6.0D, 4.0D, 6.0D));
                list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
                if (list.size() >= 1) {
                    this.setAge(this.getPregnancyTime());
                    list.get(0).setAge(this.getPregnancyTime());
                    return true;
                }
            }
        }
        return false;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    protected SoundEvent getAmbientSound() {
        if (this.isAngry()) {
            return super.getAmbientSound();
        }
        return null;
    }

    @Nullable
    @Override
    public EntityAnaconda getBreedOffspring(ServerLevel serverWorld, AgeableMob ageable) {
        if (this.isEggLayer()) {
            EntityUtils.dropEggs(this, "egg_large_snake_" + getRawSpeciesName(this.getVariant()).toLowerCase(), 4);
            return null;
        }
        return create_offspring(new EntityAnaconda(ModEntity.ANACONDA.get(), this.level));
    }

    public boolean doHurtTarget(Entity entityIn) {
        float f = (float)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        boolean flag = entityIn.hurt(DamageSource.mobAttack(this), f);
        if (flag) {
            if (this.huntingCooldown == 0 && entityIn instanceof LivingEntity && !(entityIn instanceof Player) && entityIn.getBbWidth() * entityIn.getBbHeight() < 1.2F && (entityIn instanceof TamableAnimal && !((TamableAnimal) entityIn).isTame())) {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F);
                EntityUtils.spawnParticlesOnEntity(this.level, (LivingEntity)entityIn, ParticleTypes.POOF, 6, 2);
                this.setDeltaMovement(new Vec3(entityIn.getX() - this.getX(), entityIn.getY() - this.getY(), entityIn.getZ() - this.getZ()).scale(0.15F));
                this.huntingCooldown = 144000; // Large Snakes will spend the next 6 days idling after eating prey
                entityIn.remove(RemovalReason.KILLED);
            }
            return true;
        }
        return flag;
    }

    public boolean attackEntityPartFrom(EntityAnacondaPart anaconda_part, DamageSource source, float amount) {
        return this.hurt(source, amount);
    }

    // Flags Parameters
    public boolean isEggLayer() {
        return getEntityData(this.getType()).getFlags(this.getVariant(), "eggLayer") == 1;
    }
    public int getMultiparts() {
        return 3; // TODO: Unused parameter, as all Large Snakes have the same length. Need to consider how to handle multiparts
        //return getEntityData(this.getType()).getFlags(this.getVariant(), "parts");
    }

    @Override
    public void updateAttributes() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getAttack());
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getHealth());
        this.setHealth(this.getMaxHealth());
    }

    public static class EntityAnacondaPart extends PartEntity<EntityAnaconda> {

        private final EntityDimensions size;
        public float scale = 1;

        public EntityAnacondaPart(EntityAnaconda parent, float sizeX, float sizeY) {
            super(parent);
            this.size = EntityDimensions.scalable(sizeX, sizeY);
            this.refreshDimensions();
        }

        protected void collideWithNearbyEntities() {
            List<Entity> entities = this.level.getEntities(this, this.getBoundingBox().inflate(0.20000000298023224D, 0.0D, 0.20000000298023224D));
            Entity parent = this.getParent();
            if (parent != null) {
                entities.stream().filter(entity -> entity != parent && !(entity instanceof EntityAnacondaPart && ((EntityAnacondaPart) entity).getParent() == parent) && entity.isPushable()).forEach(entity -> entity.push(parent));
            }
        }

        public InteractionResult mobInteract(Player player, InteractionHand hand) {
            return this.getParent() == null ? InteractionResult.PASS : this.getParent().mobInteract(player, hand);
        }

        public void push(Entity entityIn) {
            entityIn.push(this);
        }

        public boolean canBeCollidedWith() {
            return true;
        }

        public boolean hurt(DamageSource source, float amount) {
            return !this.isInvulnerableTo(source) && this.getParent().attackEntityPartFrom(this, source, amount);
        }

        @Override
        protected void defineSynchedData() { }

        @Override
        protected void readAdditionalSaveData(CompoundTag compound) { }

        @Override
        protected void addAdditionalSaveData(CompoundTag compound) { }

        public boolean is(Entity entityIn) {
            return this == entityIn || this.getParent() == entityIn;
        }

        public Packet<?> getAddEntityPacket() {
            throw new UnsupportedOperationException();
        }

        public EntityDimensions getSize(Pose poseIn) {
            return this.size.scale(scale);
        }
    }
}