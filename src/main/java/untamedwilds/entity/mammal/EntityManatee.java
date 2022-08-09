package untamedwilds.entity.mammal;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.level.Level;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.control.look.SmartSwimmerLookControl;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModTags;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntityManatee extends ComplexMobAquatic implements ISpecies, INewSkins, INeedsPostUpdate {

    private static final EntityDataAccessor<Boolean> IS_EATING = SynchedEntityData.defineId(EntityManatee.class, EntityDataSerializers.BOOLEAN);

    public int ringBufferIndex = -1;
    public final double[][] ringBuffer = new double[64][3];

    public EntityManatee(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
        this.entityData.define(IS_EATING, false);
        this.moveControl = new SmoothSwimmingMoveControl(this, 30, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmartSwimmerLookControl(this, 30);
        this.turn_speed = 0.1F;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0D)
                .add(Attributes.MOVEMENT_SPEED, 0.45D)
                .add(Attributes.FOLLOW_RANGE, 8.0D)
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1D)
                .add(Attributes.ARMOR, 2D);
    }

    protected void registerGoals() {
        //super.registerGoals();
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(3, new ManateeFeedGoal(this, 120));
        this.goalSelector.addGoal(4, new SwimGoal(this));
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            if (this.isEating()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0, -0.01F, 0));
            }
            if (this.level.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
        }

        if (!this.isNoAi() && !this.isBaby()) {
            if (this.ringBufferIndex < 0) {
                for (int i = 0; i < this.ringBuffer.length; ++i) {
                    this.ringBuffer[i][0] = this.getYRot();
                    this.ringBuffer[i][1] = this.getY();
                }
            }
            this.ringBufferIndex++;
            if (this.ringBufferIndex == this.ringBuffer.length) {
                this.ringBufferIndex = 0;
            }
            this.ringBuffer[this.ringBufferIndex][0] = this.yRotO + 0.5F * Mth.wrapDegrees(this.getYRot() - this.yRotO);
            this.ringBuffer[ringBufferIndex][1] = this.getY();
        }
    }

    public double[] getMovementOffsets(int offset, float partialTicks) {
        if (this.isDeadOrDying()) {
            partialTicks = 0.0F;
        }
        partialTicks = 1.0F - partialTicks;
        int i = this.ringBufferIndex - offset & 63;
        int j = this.ringBufferIndex - offset - 1 & 63;
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

    /* Breeding conditions for the Manatee are:
     * A nearby Manatee */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
            List<EntityManatee> list = this.level.getEntitiesOfClass(EntityManatee.class, this.getBoundingBox().inflate(12.0D, 8.0D, 12.0D));
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
        return create_offspring(new EntityManatee(ModEntity.MANATEE.get(), this.level));
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.GUARDIAN_FLOP;
    }

    @Override
    public void updateAttributes() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getAttack());
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(getEntityData(this.getType()).getSpeciesData().get(this.getVariant()).getHealth());
        this.setHealth(this.getMaxHealth());
    }

    public boolean isEating(){ return (this.entityData.get(IS_EATING)); }
    private void setIsEating(boolean eating){ this.entityData.set(IS_EATING, eating); }

    public class ManateeFeedGoal extends Goal {

        private final EntityManatee taskOwner;
        private final int chance;
        private BlockPos targetPos;
        private boolean taskComplete;
        private int counter;
        private int eatingCounter;

        public ManateeFeedGoal(EntityManatee entityIn, int chance) {
            this.taskOwner = entityIn;
            this.chance = chance;
        }

        public boolean canUse() {
            if (this.taskOwner.getRandom().nextInt(this.chance) == 0) {
                this.taskComplete = false;
                this.targetPos = getNearbySandBlock(this.taskOwner.blockPosition());
                return this.targetPos != null;
            }
            return false;
        }

        private BlockPos getNearbySandBlock(BlockPos roomCenter) {
            int X = 5;
            int Y = 8;
            //List<BlockPos> inventories = new ArrayList<>();
            for (BlockPos blockpos : BlockPos.MutableBlockPos.betweenClosed(roomCenter.offset(-X, -Y, -X), roomCenter.offset(X, 0, X))) {
                if (this.taskOwner.level.getBlockState(blockpos).is(ModTags.ModBlockTags.GRAZEABLE_ALGAE) && random.nextInt(2) == 0) {
                    return blockpos;
                }
            }
            return null;
        }

        public boolean canContinueToUse() {
            return !this.taskComplete && !this.taskOwner.getNavigation().isDone() && this.eatingCounter != 0;
        }

        public void start() {
            this.eatingCounter = -1;
            this.taskOwner.getNavigation().moveTo(this.targetPos.getX() + 0.5, this.targetPos.above().getY(), this.targetPos.getZ() + 0.5, 1.5);
        }

        public void stop() {
            if (this.taskOwner.isEating())
                this.taskOwner.setIsEating(false);
            super.stop();
        }

        public void tick() {
            if (this.eatingCounter > 0)
                this.eatingCounter--;
            if (this.counter == 0 && this.taskOwner.distanceToSqr(this.targetPos.getX() + 0.5, this.targetPos.above().getY(), this.targetPos.getZ() + 0.5) < 1) {
                this.taskOwner.getNavigation().stop();
                this.counter = this.taskOwner.getRandom().nextInt(20) + 30;
            }
            if (this.counter > 0) {
                this.counter--;
                if (this.counter == 0) {
                    this.taskOwner.setIsEating(true);
                    this.eatingCounter = this.taskOwner.getRandom().nextInt(40) + 20;
                    this.taskOwner.getNavigation().stop();
                    Level worldIn = this.taskOwner.getLevel();
                    this.taskOwner.getLookControl().setLookAt(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
                    if (worldIn.getBlockState(this.targetPos).is(ModTags.ModBlockTags.GRAZEABLE_ALGAE)) {
                        if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(worldIn, this.taskOwner)) {
                            //worldIn.globalLevelEvent(2001, this.targetPos, Block.getId(Blocks.SEAGRASS.defaultBlockState()));
                            if (ConfigGamerules.grazerGriefing.get()) {
                                worldIn.destroyBlock(this.targetPos, false);
                            }
                        }
                    }
                    this.taskOwner.ate();
                    this.taskOwner.playSound(SoundEvents.GENERIC_EAT, 1F, 1);
                    this.taskComplete = true;
                }
            }
        }
    }
}
