package untamedwilds.entity.mollusk;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.INewSkins;
import untamedwilds.entity.ISpecies;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.List;

public class EntityGiantClam extends ComplexMob implements ISpecies, INewSkins {

    private static final EntityDataAccessor<Boolean> CLAM_OPEN = SynchedEntityData.defineId(EntityGiantClam.class, EntityDataSerializers.BOOLEAN);
    public int closeProgress;

    public EntityGiantClam(EntityType<? extends ComplexMob> type, Level worldIn) {
        super(type, worldIn);
        this.entityData.define(CLAM_OPEN, false);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 0D)
                .add(Attributes.MOVEMENT_SPEED, 0D)
                .add(Attributes.FOLLOW_RANGE, 1D)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1D)
                .add(Attributes.ARMOR, 12D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        //this.entityData.define(CLAM_OPEN, true);
        //this.entityCollisionReduction = 1F;
    }

    @Override
    public void baseTick() {
        int i = this.getAirSupply();
        super.baseTick();
        if (this.isAlive() && !this.isInWaterOrBubble()) {
            --i;
            this.setAirSupply(i);

            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(DamageSource.DRY_OUT, 2.0F);
            }
        }
        else {
            this.setAirSupply(300);
        }
    }

    public void aiStep() {
        super.aiStep();
        // The following locks the X and Z position to zero, preventing the entity from being pushed
        this.setDeltaMovement(0, this.getDeltaMovement().get(Direction.Axis.Y), 0);
        if (!this.level.isClientSide) {
            if (this.isInWater() && this.isOpen() && this.getRandom().nextFloat() > 0.99) {
                ((ServerLevel) this.level).sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, this.getPosition(0).x, this.getPosition(0).y + 0.2, this.getPosition(0).z, 1, random.nextFloat() * 0.2, random.nextFloat() * 0.2, random.nextFloat() * 0.2, 0);
            }
            if (this.tickCount % 1000 == 0) {
                if (this.wantsToBreed()) {
                    this.breed();
                }
            }
            this.setOpen(this.level.isDay());
        }
        if (this.level.isClientSide) {
            if (!this.isOpen() && this.closeProgress < 200) {
                this.closeProgress++;
            } else if (this.isOpen() && this.closeProgress > 0) {
                this.closeProgress--;
            }
        }
    }

    /* Breeding conditions for the Giant Clam are:
     * A nearby Giant Clam of the same species, being hermaphrodites, they do not take Gender into account */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.getAge() == 0 && EntityUtils.hasFullHealth(this)) {
            List<EntityGiantClam> list = this.level.getEntitiesOfClass(EntityGiantClam.class, this.getBoundingBox().inflate(12.0D, 6.0D, 12.0D));
            list.removeIf(input -> input == this || input.getAge() != 0 || input.getVariant() != this.getVariant());
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
        EntityUtils.dropEggs(this, "egg_giant_clam", this.getOffspring());
        return null;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (itemstack.getItem() instanceof ShovelItem && this.isAlive() && hand == InteractionHand.MAIN_HAND) {
            if (this.random.nextInt(4) == 0) {
                this.level.playSound(null, this.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.BLOCKS, 1.0F, 0.8F);
                EntityUtils.turnEntityIntoItem(this, "spawn_giant_clam");
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
            else {
                this.level.playSound(null, this.blockPosition(), SoundEvents.SHULKER_HURT_CLOSED, SoundSource.BLOCKS, 1.0F, 0.8F);
                EntityUtils.spawnParticlesOnEntity(this.level, this, ParticleTypes.SMOKE, 3, 1);
            }
        }
        return super.mobInteract(player, hand);
    }

    public boolean canBeTargeted() { return false; }
    private boolean isOpen(){ return (this.entityData.get(CLAM_OPEN)); }
    private void setOpen(boolean open){ this.entityData.set(CLAM_OPEN, open); }

    public void addAdditionalSaveData(CompoundTag compound){ // Write NBT Tags
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isOpen", this.isOpen());
    }

    public void readAdditionalSaveData(CompoundTag compound){ // Read NBT Tags
        super.readAdditionalSaveData(compound);
        this.setOpen(compound.getBoolean("isOpen"));
    }
}