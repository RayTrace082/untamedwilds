package untamedwilds.entity.fish;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAquatic;
import untamedwilds.entity.INewSkins;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.SmartMeleeAttackGoal;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntityTriggerfish extends ComplexMobAquatic implements ISpecies, INewSkins {

    public EntityTriggerfish(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0D)
                .add(Attributes.MOVEMENT_SPEED, 0.6D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0D)
                .add(Attributes.ARMOR, 2D);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SmartMeleeAttackGoal(this, 1.8D, false, 2));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new TriggerFishBlowGoal(this, 600));
        this.goalSelector.addGoal(4, new SwimGoal(this, 6));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
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
        }
    }

    /* Breeding conditions for the Triggerfish are:
     * A nearby Triggerfish of different gender */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
            List<EntityTriggerfish> list = this.level.getEntitiesOfClass(EntityTriggerfish.class, this.getBoundingBox().inflate(12.0D, 8.0D, 12.0D));
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
        EntityUtils.dropEggs(this, "egg_triggerfish", this.getOffspring());
        return null;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.GUARDIAN_FLOP;
    }

    public class TriggerFishBlowGoal extends Goal {

        private final ComplexMobAquatic taskOwner;
        private final int chance;
        private BlockPos targetPos;
        private boolean taskComplete;
        private int counter;

        public TriggerFishBlowGoal(ComplexMobAquatic entityIn, int chance) {
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
            int X = 4;
            int Y = 6;
            //List<BlockPos> inventories = new ArrayList<>();
            for (BlockPos blockpos : BlockPos.betweenClosed(roomCenter.offset(-X, -Y, -X), roomCenter.offset(X, 0, X))) {

                if (this.taskOwner.level.getBlockState(blockpos).getBlock() == Blocks.SAND && this.taskOwner.level.getBlockState(blockpos.above()).isAir() && random.nextInt(2) == 0) {

                    return blockpos;
                }
            }
            return null;
        }

        public boolean canContinueToUse() {
            return !this.taskComplete && !this.taskOwner.getNavigation().isDone() && this.counter >= 0;
        }

        public void start() {
            this.taskOwner.getNavigation().moveTo(this.targetPos.getX() + 0.5, this.targetPos.above(3).getY(), this.targetPos.getZ() + 0.5, 1);
        }

        public void tick() {
            if (this.counter == 0 && this.taskOwner.distanceToSqr(this.targetPos.getX() + 0.5, this.targetPos.above().getY(), this.targetPos.getZ() + 0.5) < 1) {
                //this.taskOwner.getNavigation().stop();
                this.counter = this.taskOwner.getRandom().nextInt(20) + 30;
                //this.taskOwner.addEffect(new EffectInstance(Effects.GLOWING, 80, 0));
            }
            if (this.counter > 0) {
                this.counter--;
                if (this.counter == 0) {
                    Direction direction = this.taskOwner.getMotionDirection();
                    this.taskOwner.setDeltaMovement(this.taskOwner.getDeltaMovement().add((double)direction.getStepX() * -0.2D, 0.2D, (double)direction.getStepZ() * -0.2D));
                    this.taskOwner.getNavigation().stop();
                    Level worldIn = this.taskOwner.getLevel();
                    ((ServerLevel)worldIn).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, worldIn.getBlockState(this.targetPos)), this.targetPos.getX() + 0.5, this.targetPos.above().getY(), this.targetPos.getZ() + 0.5, 50, 0.0D, 0.0D, 0.0D, 0.15F);
                    this.taskComplete = true;
                }
            }
        }
    }
}
