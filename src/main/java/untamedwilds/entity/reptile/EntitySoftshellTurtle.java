package untamedwilds.entity.reptile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAmphibious;
import untamedwilds.entity.INewSkins;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.control.look.SmartSwimmerLookControl;
import untamedwilds.entity.ai.control.movement.SmartSwimmingMoveControl;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.init.ModItems;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntitySoftshellTurtle extends ComplexMobAmphibious implements ISpecies, INewSkins {

    public int baskProgress;

    public EntitySoftshellTurtle(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
        this.moveControl = new SmartSwimmingMoveControl(this, 60, 10, 0.4F, 0.25F, true);
        this.lookControl = new SmartSwimmerLookControl(this, 20);
        this.swimSpeedMult = 3;
        this.buoyancy = 0.998F;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0D)
                .add(Attributes.MOVEMENT_SPEED, 0.7D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0D)
                .add(Attributes.ARMOR, 2D);
    }

    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 1D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 0.7D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1D, 1.1D, input -> getEcoLevel(input) > getEcoLevel(this)));
        this.goalSelector.addGoal(3, new AmphibiousTransition(this, 1D));
        this.goalSelector.addGoal(4, new AmphibiousRandomSwimGoal(this, 0.7, 80));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false, input -> getEcoLevel(input) < getEcoLevel(this)));
    }

    public boolean wantsToBeOnLand() { return this.level.getDayTime() > 5000 && this.level.getDayTime() < 7000; }

    public boolean wantsToBeInWater() { return !(this.level.getDayTime() > 5000 && this.level.getDayTime() < 7000); }

    public boolean isPushedByFluid() {
        return false;
    }

    public void die(DamageSource cause) {
        if (cause == DamageSource.ANVIL && !this.isBaby()) {
            // Advancement Trigger: "Unethical Soup"
            ItemEntity entityitem = this.spawnAtLocation(new ItemStack(ModItems.FOOD_TURTLE_SOUP.get()), 0.2F);
            if (entityitem != null) {
                entityitem.getItem().setCount(1);
            }
        }
        super.die(cause);
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide && this.isInWater() && this.getDeltaMovement().lengthSqr() > 0.03D) {
            Vec3 vec3 = this.getViewVector(0.0F);
            float f = Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * 0.3F;
            float f1 = Mth.sin(this.getYRot() * ((float)Math.PI / 180F)) * 0.3F;
            float f2 = 1.2F - this.random.nextFloat() * 0.7F;

            for(int i = 0; i < 2; ++i) {
                this.level.addParticle(ParticleTypes.DOLPHIN, this.getX() - vec3.x * (double)f2 + (double)f, this.getY() - vec3.y, this.getZ() - vec3.z * (double)f2 + (double)f1, 0.0D, 0.0D, 0.0D);
                this.level.addParticle(ParticleTypes.DOLPHIN, this.getX() - vec3.x * (double)f2 - (double)f, this.getY() - vec3.y, this.getZ() - vec3.z * (double)f2 - (double)f1, 0.0D, 0.0D, 0.0D);
            }
        }
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
            if (this.isInWater()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.003D, 0.0D));
            }
        }
        else {
            if (!this.isInWater() && this.baskProgress < 100) {
                this.baskProgress++;
            } else if (this.isInWater() && this.baskProgress > 0) {
                this.baskProgress--;
            }
        }
    }

    /* Breeding conditions for the softshell_turtle are:
     * A nearby softshell_turtle of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
                List<EntitySoftshellTurtle> list = this.level.getEntitiesOfClass(EntitySoftshellTurtle.class, this.getBoundingBox().inflate(6.0D, 4.0D, 6.0D));
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

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        EntityUtils.dropEggs(this, "egg_softshell_turtle", this.getOffspring());
        return null;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (itemstack.isEmpty() && this.isAlive()) {
            EntityUtils.turnEntityIntoItem(this, "spawn_softshell_turtle");
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        SoundEvent soundevent = this.isBaby() ? SoundEvents.TURTLE_SHAMBLE_BABY : SoundEvents.TURTLE_SHAMBLE;
        this.playSound(soundevent, 0.15F, 1.0F);
    }
}
