package untamedwilds.entity.amphibian;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.control.look.SmartSwimmerLookControl;
import untamedwilds.entity.ai.control.movement.SmartSwimmingMoveControl;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntityNewt extends ComplexMobAmphibious implements ISpecies, INewSkins {

    public int swimProgress;
    public float offset;

    public EntityNewt(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
        this.moveControl = new SmartSwimmingMoveControl(this, 40, 5, 0.25F, 0.3F, true);
        this.lookControl = new SmartSwimmerLookControl(this, 20);
        //this.setPathfindingMalus(BlockPathTypes.WATER, this.isAquatic() ? 0.0F : -1);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0D)
                .add(Attributes.MOVEMENT_SPEED, 0.7D)
                .add(Attributes.FOLLOW_RANGE, 8.0D)
                .add(Attributes.MAX_HEALTH, 2.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0D);
    }

    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 1.4D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 0.7D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, (float)this.getAttributeValue(Attributes.FOLLOW_RANGE), 1D, 1.3D,  input -> getEcoLevel(input) > getEcoLevel(this)));
        this.goalSelector.addGoal(3, new AmphibiousTransition(this, 1D));
        //this.goalSelector.addGoal(3, new RandomSwimmingGoal(this, 1D, 60));
        //this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, 120, 0, false));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public float getWalkTargetValue(BlockPos p_149140_, LevelReader p_149141_) {
        return 0.0F;
    }

    private boolean isAquatic() {
        return getEntityData(this.getType()).getFlags(this.getVariant(), "isAquatic") == 1;
    }

    public boolean wantsToBeOnLand() { return !this.isAquatic(); }

    public boolean wantsToBeInWater() { return this.isAquatic(); }

    public boolean isPushedByFluid() {
        return false;
    }

    public void aiStep() {
        super.aiStep();

        if (!this.level.isClientSide) {
            if (this.isInWater()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.003D, 0.0D));
            }
            if (this.tickCount % 1000 == 0) {
                if (this.wantsToBreed() && !this.isMale()) {
                    this.breed();
                }
            }
            if (this.level.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
        }
        else {
            if (Math.abs(this.getYRot() - this.yRotO) > 0.005) {
                this.offset = Mth.rotLerp(0.05F, this.offset, (this.getYRot() - this.yRotO));
            }
            if (this.isInWater() && !this.isOnGround() && this.swimProgress < 20) {
                this.swimProgress++;
            } else if ((!this.isInWater() || this.isOnGround()) && this.swimProgress > 0) {
                this.swimProgress--;
            }
        }
    }

    /* Breeding conditions for the giant salamander are:
     * A nearby giant salamander of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
                List<EntityNewt> list = this.level.getEntitiesOfClass(EntityNewt.class, this.getBoundingBox().inflate(6.0D, 4.0D, 6.0D));
                list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
                if (list.size() >= 1) {
                    this.setAge(this.getAge());
                    list.get(0).setAge(this.getAge());
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        EntityUtils.dropEggs(this, "egg_newt", this.getOffspring());
        return null;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (hand == InteractionHand.MAIN_HAND) {
            if (itemstack.getItem().equals(Items.WATER_BUCKET) && this.isAlive()) {
                EntityUtils.mutateEntityIntoItem(this, player, hand, "bucket_newt", itemstack);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        }
        return super.mobInteract(player, hand);
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        SoundEvent soundevent = SoundEvents.TURTLE_SHAMBLE_BABY;
        this.playSound(soundevent, 0.15F, 1.0F);
    }

    public float getModelScale() { return 0.8F; }
}
