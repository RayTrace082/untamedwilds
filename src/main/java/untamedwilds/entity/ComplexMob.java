package untamedwilds.entity;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import untamedwilds.block.blockentity.CritterBurrowBlockEntity;
import untamedwilds.compat.CompatBridge;
import untamedwilds.compat.CompatSereneSeasons;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.config.ConfigMobControl;
import untamedwilds.init.ModAdvancementTriggers;
import untamedwilds.util.*;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class ComplexMob extends TamableAnimal {

    public static HashMap<String, HashMap<Integer, ArrayList<ResourceLocation>>> TEXTURES_COMMON = new HashMap<>();
    public static HashMap<String, HashMap<Integer, ArrayList<ResourceLocation>>> TEXTURES_RARE = new HashMap<>();

    private static final EntityDataAccessor<BlockPos> HOME_POS = SynchedEntityData.defineId(ComplexMob.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(ComplexMob.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SKIN = SynchedEntityData.defineId(ComplexMob.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(ComplexMob.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> GENDER = SynchedEntityData.defineId(ComplexMob.class, EntityDataSerializers.INT); // 0 - Male, 1 - Female
    private static final EntityDataAccessor<Boolean> IS_ANGRY = SynchedEntityData.defineId(ComplexMob.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(ComplexMob.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(ComplexMob.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(ComplexMob.class, EntityDataSerializers.BOOLEAN);
    public HerdEntity herd = null;
    public float turn_speed = 1F;
    public int huntingCooldown;
    public static HashMap<EntityType<?>, EntityDataHolder> ENTITY_DATA_HASH = new HashMap<>();
    public static HashMap<EntityType<?>, EntityDataHolderClient> CLIENT_DATA_HASH = new HashMap<>();

    public ComplexMob(EntityType<? extends ComplexMob> type, Level worldIn){
        super(type, worldIn);
        this.moveControl = new MoveControl(this);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HOME_POS, BlockPos.ZERO);
        this.entityData.define(VARIANT, 0);
        this.entityData.define(SKIN, 0);
        this.entityData.define(SIZE, 1F);
        this.entityData.define(GENDER, 0);
        this.entityData.define(IS_ANGRY, false);
        this.entityData.define(COMMAND, 0);
        this.entityData.define(SLEEPING, false);
        this.entityData.define(SITTING, false);
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide && this.huntingCooldown > 0) {
            this.huntingCooldown--;
        }
    }

    /**
     * Wrapper method to access EntityDataHolder objects, contains safeguards against accessing data before its initialization
     * @param typeIn The EntityType to access in ENTITY_DATA_HASH, or initialize it if needed
     */
    public static EntityDataHolder getEntityData(EntityType<?> typeIn) {
        if (!ENTITY_DATA_HASH.containsKey(typeIn)) {
            EntityDataListenerEvent.registerEntityData(typeIn);
        }
        return ENTITY_DATA_HASH.get(typeIn);
    }

    protected SoundEvent getAmbientSound() {
        return EntityUtils.getSound(this.getType(), this.getVariant(), "ambient");
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return EntityUtils.getSound(this.getType(), this.getVariant(), "hurt", SoundEvents.GENERIC_HURT);
    }

    protected SoundEvent getDeathSound() {
        return EntityUtils.getSound(this.getType(), this.getVariant(), "death", SoundEvents.GENERIC_DEATH);
    }

    protected SoundEvent getThreatSound() {
        return EntityUtils.getSound(this.getType(), this.getVariant(), "threat");
    }

    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return true;
    }

    // Why is a method called 'checkSpawnObstruction()' also checking for Water?
    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        //return !p_21433_.containsAnyLiquid(this.getBoundingBox()) && p_21433_.isUnobstructed(this);
        return worldIn.isUnobstructed(this);
    }

    @Override
    public boolean canBeLeashed(Player player) {
        if (player.isCreative()) {
            return !this.isLeashed();
        }
        return (!this.isLeashed() && this.isTame());
    }

    @Override
    public boolean removeWhenFarAway(double p_21542_) {
        return false;
    }

    public void setSleeping(boolean sleeping){ this.entityData.set(SLEEPING, sleeping); }
    public boolean isSleeping(){ return (this.entityData.get(SLEEPING)); }

    public void setSitting(boolean sitting){ this.entityData.set(SITTING, sitting); }
    public boolean isSitting(){ return (this.entityData.get(SITTING)); }
    public boolean isNotMoving(){
        return this.getDeltaMovement().x == 0 && this.getDeltaMovement().z == 0;
    }
    public boolean canBeTargeted() { return true; }
    public double getCurrentSpeed() { return Math.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z); }

    public int getAmbientSoundInterval() {
        //return Integer.MAX_VALUE;
        return 300;
    }

    protected int getExperienceReward(Player p_27590_) {
        int xp = Math.max(getEcoLevel(this) / 2, 1);
        return xp + this.level.random.nextInt(xp);
    }

    /**
     * Method that links an EntityType with an EntityDataHolder object, and uses the EntityDataHolder to build a
     * hash with only Variant data, to be synced and accessed by the client
     * @param dataIn The EntityDataHolder to introduce in ENTITY_DATA_HASH
     * @param typeIn The EntityType to be associated with the dataIn object
     */
    public static void processData(EntityDataHolder dataIn, EntityType<?> typeIn) {
        ENTITY_DATA_HASH.put(typeIn, dataIn);
        processSkins(dataIn, typeIn.getRegistryName().getPath());
        for (SpeciesDataHolder speciesData : ENTITY_DATA_HASH.get(typeIn).getSpeciesData()) {
            if (!ComplexMob.CLIENT_DATA_HASH.containsKey(typeIn)) {
                ComplexMob.CLIENT_DATA_HASH.put(typeIn, new EntityDataHolderClient(new HashMap<>(), new HashMap<>()));
            }
            ComplexMob.CLIENT_DATA_HASH.get(typeIn).species_data.put(speciesData.getVariant(), speciesData.getName());
        }
    }

    public static void processSkins(EntityDataHolder dataIn, String nameIn) {
        for (SpeciesDataHolder speciesDatum : dataIn.getSpeciesData()) {
            EntityUtils.buildSkinArrays(nameIn, speciesDatum.getName().toLowerCase(), dataIn, speciesDatum.getVariant(), TEXTURES_COMMON, TEXTURES_RARE);
        }
    }

    public int getVariant(){ return (this.entityData.get(VARIANT)); }
    public void setVariant(int variant){ this.entityData.set(VARIANT, variant); }
    public int getSkin(){ return (this.entityData.get(SKIN)); }
    public void setSkin(int skin){ this.entityData.set(SKIN, skin); }
    public <T extends ComplexMob> void chooseSkinForSpecies(T entityIn, boolean allowRares) {
        if (entityIn.getType().getRegistryName() != null && this instanceof INewSkins && !this.level.isClientSide) {
            String name = entityIn.getType().getRegistryName().getPath();
            if (!TEXTURES_COMMON.get(name).isEmpty()) {
                boolean isRare = allowRares && TEXTURES_RARE.get(name).containsKey(this.getVariant()) && this.random.nextFloat() < ConfigGamerules.rareSkinChance.get();
                int skin = this.random.nextInt(isRare ? TEXTURES_RARE.get(name).get(this.getVariant()).size() : TEXTURES_COMMON.get(name).get(this.getVariant()).size()) + (isRare ? 100 : 0);
                this.setSkin(skin);
            }
        }
    }

    public float getModelScale() { return getEntityData(this.getType()).getScale(this.getVariant()); }
    public float getMobSize(){ return (this.entityData.get(SIZE)); }
    public void setMobSize(float size){ this.entityData.set(SIZE, size); }
    public void setRandomMobSize(){ this.entityData.set(SIZE, this.getModelScale() + ((float)this.random.nextGaussian() * 0.1F)); }

    public void setGender(int gender){ this.entityData.set(GENDER, gender); }
    public int getGender(){	return (this.entityData.get(GENDER)); }
    public boolean isMale() { return this.getGender() == 0; }
    public String getGenderString() {
        return this.isMale() ? "male" : "female";
    }

    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get()) {
            if (CompatBridge.SereneSeasons) {
                return CompatSereneSeasons.isCurrentSeason(this.level, this.getBreedingSeason());
            }
            return true;
            //return this.isInLove();
        }
        return false;
    }

    @SuppressWarnings("unchecked") // Don't use this outside ComplexMobs
    public <T extends ComplexMob> void breed() {
        int bound = 1 + (this.getOffspring() > 0 ? this.random.nextInt(this.getOffspring() + 1) : 0);
        for (int i = 0; i < bound; i++) {
            T child = (T) this.getBreedOffspring((ServerLevel) this.level, this);
            if (child != null) {
                child.setVariant(this.getVariant());
                child.setAge(this.getAdulthoodTime() * -1);
                child.setGender(this.random.nextInt(2));
                child.setRandomMobSize();
                child.setBaby(true);
                child.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
                if (this.getOwner() != null)
                    child.tame((Player) this.getOwner());
                if (this instanceof INeedsPostUpdate) {
                    ((INeedsPostUpdate) child).updateAttributes();
                }
                if (TEXTURES_COMMON.containsKey(child.getType().getRegistryName().getPath())) {
                    chooseSkinForSpecies(child, true);
                }
                //((ServerLevel)this.level).addFreshEntityWithPassengers(child);
                this.level.addFreshEntity(child);
                this.level.broadcastEntityEvent(this, (byte)18);
            }
        }
    }

    protected <T extends ComplexMob> T create_offspring(T entity) {
        entity.setGender(this.random.nextInt(2));
        entity.setRandomMobSize();
        entity.setVariant(this.getVariant());
        if (entity instanceof INeedsPostUpdate) {
            ((INeedsPostUpdate) entity).updateAttributes();
        }
        return entity;
    }

    public String getBreedingSeason() {
        return getEntityData(this.getType()).getBreedingSeason(this.getVariant());
    }

    // Adulthood time is twice the Growing time, and only used for mobs that do not lay eggs
    public int getAdulthoodTime() {
        return getEntityData(this.getType()).getGrowingTime(this.getVariant()) * ConfigGamerules.cycleLength.get() * 2;
    }

    public int getPregnancyTime() {
        return getEntityData(this.getType()).getGrowingTime(this.getVariant()) * ConfigGamerules.cycleLength.get();
    }

    public int getOffspring() {
        return getEntityData(this.getType()).getOffspring(this.getVariant());
    }

    public boolean isFood(ItemStack stack) {
        if (getEntityData(this.getType()).getFavouriteFood(this.getVariant()).getItem().equals(Blocks.AIR.asItem()))
            return false;
        return stack.getItem().equals(getEntityData(this.getType()).getFavouriteFood(this.getVariant()).getItem());
    }
    public boolean canTakeItem(ItemStack stack) {
        return false;
    }

    private boolean isBlinking() {
        return this.tickCount % 60 > 53;
    }

    public boolean shouldRenderEyes() { return !this.isSleeping() && !this.dead && !this.isBlinking() && this.hurtTime == 0; }

    public boolean canMove() { return !this.isSitting() && !this.isSleeping() && !this.isVehicle(); }

    public void setHome(BlockPos position) {
        this.entityData.set(HOME_POS, position);
    }
    public BlockPos getHome() { return this.entityData.get(HOME_POS); }
    public Vec3 getHomeAsVec() {
        BlockPos home = this.getHome();
        return new Vec3(home.getX(), home.getY(), home.getZ());
    }

    public void setAge(int age) {
        int i = this.age;
        super.setAge(age);
        this.age = age;
        if (!this.isMale() && !(this instanceof INestingMob nestingMob && nestingMob.isEggLayer()) && !ConfigGamerules.easyBreeding.get()) {
            if (i > 0 && age <= 0) {
                this.breed();
            }
        }
    }

    public ResourceLocation getTexture() {
        return EntityUtils.getSkinFromEntity(this);
    }

    // Returns the ecological level of an entity. Values are dynamically calculated based on current HP, Attack and Herd size (if any)
    public static int getEcoLevel(LivingEntity entity) {
        if (entity instanceof Player) {
            return (int) (4 + (entity.getHealth() / 6));
        }
        int attack = (int) Math.max(entity.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 1, 4);
        if (entity instanceof ComplexMob && ((ComplexMob) entity).herd != null) {
            return (int) (Math.sqrt(entity.getHealth() * attack) / 2.5F) + ((ComplexMob) entity).herd.creatureList.size();
        }
        return (int) (Math.sqrt(entity.getHealth() * attack) / 2.5F);
    }

    protected void performRetaliation(DamageSource damageSource, float health, float damage, boolean needsActiveTarget) {
        if (needsActiveTarget && this.getTarget() != damageSource.getEntity())
            return;
        if (!this.level.isClientSide && !this.isNoAi() && this.getTarget() != null && damage < health && (damageSource.getEntity() != null || damageSource.getDirectEntity() != null) && damageSource.getEntity() instanceof LivingEntity && !(damageSource.getEntity() instanceof TamableAnimal tamable && tamable.getOwner() != null)) {
            if (this.hasLineOfSight(damageSource.getEntity()) && !damageSource.getEntity().isInvulnerable()) {
                this.doHurtTarget(damageSource.getEntity());
            }
        }
    }

    protected void setAngry(boolean isAngry) { this.entityData.set(IS_ANGRY, isAngry); }
    public boolean isAngry() { return (this.entityData.get(IS_ANGRY)); }

    // Commands:
    // 0 - Wander: The mob wanders around naturally
    // 1 - Follow: The mob will follow its owner, occasionally teleporting
    // 2 - Sit: The mob will sit in place
    // 3 - Guard: The mob will sit in place and attack nearby mobs (NIY)
    public void setCommandInt(int command) { this.entityData.set(COMMAND, command % 3); }
    public int getCommandInt() { return (this.entityData.get(COMMAND)); }

    public boolean shouldDespawn() { return this instanceof ISpecies && this.getHome() != BlockPos.ZERO; }
    @Override
    public void checkDespawn() {
        super.checkDespawn();
        if (this.shouldDespawn()) {
            if (!this.level.hasNearbyAlivePlayer(this.getX(), this.getY(), this.getZ(), ConfigMobControl.critterSpawnRange.get())) {
                if (this instanceof ISpecies && this.getHome() != BlockPos.ZERO) {
                    BlockEntity burrow = this.level.getBlockEntity(this.getHome());
                    if (burrow instanceof CritterBurrowBlockEntity) {
                        ((CritterBurrowBlockEntity)burrow).tryEnterBurrow(this);
                        burrow.setChanged();
                    }
                }
            }
        }
    }

    public void addAdditionalSaveData(CompoundTag compound){
        super.addAdditionalSaveData(compound);
        if (this.getHome() != BlockPos.ZERO) {
            compound.putInt("HomePosX", this.getHome().getX());
            compound.putInt("HomePosY", this.getHome().getY());
            compound.putInt("HomePosZ", this.getHome().getZ());
        }
        if (this.isTame()) {
            compound.putInt("Command", this.getCommandInt());
        }
        compound.putInt("Variant", this.getVariant());
        compound.putInt("Skin", this.getSkin());
        compound.putFloat("Size", this.getMobSize());
        compound.putInt("Gender", this.getGender());
        compound.putBoolean("isAngry", this.isAngry());
        compound.putInt("PeacefulTicks", this.huntingCooldown);
    }

    public void readAdditionalSaveData(CompoundTag compound){
        super.readAdditionalSaveData(compound);
        if (compound.contains("HomePosX")) {
            int i = compound.getInt("HomePosX");
            int j = compound.getInt("HomePosY");
            int k = compound.getInt("HomePosZ");
            this.setHome(new BlockPos(i, j, k));
        }
        if (compound.contains("OwnerUUID")) {
            this.setCommandInt(compound.getInt("Command"));
        }
        this.setVariant(EntityUtils.getClampedNumberOfSpecies(compound.getInt("Variant"), this.getType()));
        //this.setVariant(compound.getInt("Variant"));
        this.setSkin(compound.getInt("Skin"));
        this.setMobSize(compound.getFloat("Size"));
        this.setGender(compound.getInt("Gender"));
        this.setAngry(compound.getBoolean("isAngry"));
        this.huntingCooldown = compound.getInt("PeacefulTicks");
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (reason != MobSpawnType.DISPENSER && reason != MobSpawnType.BUCKET && reason != MobSpawnType.BREEDING) {
            if (this instanceof ISpecies) {
                Holder<Biome> optional = worldIn.getBiome(new BlockPos(this.position()));
                int i = ((ISpecies)this).setSpeciesByBiome(optional, reason);
                this.setVariant(i);
                if (i == 99) {
                    this.remove(RemovalReason.DISCARDED);
                    return null;
                }
            }
            this.setGender(this.random.nextInt(2));
            this.setRandomMobSize();
            if (TEXTURES_COMMON.containsKey(this.getType().getRegistryName().getPath())) {
                chooseSkinForSpecies(this, ConfigGamerules.wildRareSkins.get());
            }
            if (this instanceof INeedsPostUpdate) {
                ((INeedsPostUpdate) this).updateAttributes();
            }

            this.setAge(0);
        }
        if (this instanceof IPackEntity) {
            IPackEntity.initPack(this);
        }
        return spawnDataIn;
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND && !this.level.isClientSide()) {
            if (!CompatBridge.Patchouli) {
                ModAdvancementTriggers.NO_PATCHOULI_LOADED.trigger((ServerPlayer) player);
            }
            ItemStack itemstack = player.getItemInHand(hand);

            // Highlight mobs in the same pack if Player is in Creative mode
            if (player.isCreative() && itemstack.isEmpty() && this instanceof IPackEntity && this.herd != null) {
                for (int i = 0; i < this.herd.creatureList.size(); ++i) {
                    ComplexMob creature = this.herd.creatureList.get(i);
                    creature.addEffect(new MobEffectInstance(MobEffects.GLOWING, 80, 0));
                }
            }

            // Command handler for tamed mobs, includes Food/Potion consumption
            if (this.isTame() && this.getOwner() == player) {
                if (itemstack.isEmpty()) {
                    this.setCommandInt(this.getCommandInt() + 1);
                    player.sendMessage(new TranslatableComponent("entity.untamedwilds.command." + this.getCommandInt()), Util.NIL_UUID);
                    if (this.getCommandInt() > 1) {
                        this.getNavigation().stop();
                        this.setSitting(true);
                    } else if (this.getCommandInt() <= 1 && this.isSitting()) {
                        this.setSitting(false);
                    }
                }
                else {
                    EntityUtils.consumeItemStack(this, itemstack);
                }
            }
            return super.mobInteract(player, hand);
        }
        return InteractionResult.PASS;
    }
}
