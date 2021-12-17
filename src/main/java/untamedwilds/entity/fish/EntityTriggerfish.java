package untamedwilds.entity.fish;

import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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

    public EntityTriggerfish(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.experienceValue = 2;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.6D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 16.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 6.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0D)
                .createMutableAttribute(Attributes.ARMOR, 2D);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SmartMeleeAttackGoal(this, 1.8D, false, 2));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new TriggerFishBlowGoal(this, 600));
        this.goalSelector.addGoal(4, new SwimGoal(this, 6));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote) {
            if (this.ticksExisted % 1000 == 0) {
                if (this.wantsToBreed() && !this.isMale()) {
                    this.breed();
                }
            }
            if (this.world.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
            this.setAngry(this.getAttackTarget() != null);
        }
    }

    /* Breeding conditions for the Triggerfish are:
     * A nearby Triggerfish of different gender */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this)) {
            List<EntityTriggerfish> list = this.world.getEntitiesWithinAABB(EntityTriggerfish.class, this.getBoundingBox().grow(12.0D, 8.0D, 12.0D));
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
        EntityUtils.dropEggs(this, "egg_triggerfish", this.getOffspring());
        return null;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_GUARDIAN_FLOP;
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

        public boolean shouldExecute() {
            if (this.taskOwner.getRNG().nextInt(this.chance) == 0) {
                this.taskComplete = false;
                this.targetPos = getNearbySandBlock(this.taskOwner.getPosition());
                return this.targetPos != null;
            }
            return false;
        }

        private BlockPos getNearbySandBlock(BlockPos roomCenter) {
            int X = 4;
            int Y = 6;
            //List<BlockPos> inventories = new ArrayList<>();
            for (BlockPos blockpos : BlockPos.getAllInBoxMutable(roomCenter.add(-X, -Y, -X), roomCenter.add(X, 0, X))) {

                if (this.taskOwner.world.getBlockState(blockpos).getBlock() == Blocks.SAND && !this.taskOwner.world.getBlockState(blockpos.up()).isSolid() && rand.nextInt(2) == 0) {

                    return blockpos;
                }
            }
            return null;
        }

        public boolean shouldContinueExecuting() {
            return !this.taskComplete && !this.taskOwner.getNavigator().noPath() && this.counter >= 0;
        }

        public void startExecuting() {
            this.taskOwner.getNavigator().tryMoveToXYZ(this.targetPos.getX() + 0.5, this.targetPos.up(3).getY(), this.targetPos.getZ() + 0.5, 1);
        }

        public void tick() {
            if (this.counter == 0 && this.taskOwner.getDistanceSq(this.targetPos.getX() + 0.5, this.targetPos.up().getY(), this.targetPos.getZ() + 0.5) < 1) {
                //this.taskOwner.getNavigator().clearPath();
                this.counter = this.taskOwner.getRNG().nextInt(20) + 30;
                //this.taskOwner.addPotionEffect(new EffectInstance(Effects.GLOWING, 80, 0));
            }
            if (this.counter > 0) {
                this.counter--;
                if (this.counter == 0) {
                    Direction direction = this.taskOwner.getAdjustedHorizontalFacing();
                    this.taskOwner.setMotion(this.taskOwner.getMotion().add((double)direction.getXOffset() * -0.2D, 0.2D, (double)direction.getZOffset() * -0.2D));
                    this.taskOwner.getNavigator().clearPath();
                    World worldIn = this.taskOwner.getEntityWorld();
                    ((ServerWorld)worldIn).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, worldIn.getBlockState(this.targetPos)), this.targetPos.getX() + 0.5, this.targetPos.up().getY(), this.targetPos.getZ() + 0.5, 50, 0.0D, 0.0D, 0.0D, 0.15F);
                    this.taskComplete = true;
                }
            }
        }
    }
}
