package untamedwilds.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.block.blockentity.CritterBurrowBlockEntity;
import untamedwilds.compat.CompatBridge;
import untamedwilds.compat.CompatSereneSeasons;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.config.ConfigMobControl;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModItems;
import untamedwilds.util.EntityDataHolder;
import untamedwilds.util.EntityUtils;
import untamedwilds.util.ResourceListenerEvent;
import untamedwilds.util.SpeciesDataHolder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public abstract class ComplexMob extends TameableEntity {

    private static final DataParameter<BlockPos> HOME_POS = EntityDataManager.createKey(ComplexMob.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(ComplexMob.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> SKIN = EntityDataManager.createKey(ComplexMob.class, DataSerializers.VARINT);
    public static HashMap<String, HashMap<Integer, ArrayList<ResourceLocation>>> TEXTURES_COMMON = new HashMap<>();
    public static HashMap<String, HashMap<Integer, ArrayList<ResourceLocation>>> TEXTURES_RARE = new HashMap<>();
    private static final DataParameter<Float> SIZE = EntityDataManager.createKey(ComplexMob.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> GENDER = EntityDataManager.createKey(ComplexMob.class, DataSerializers.VARINT); // 0 - Male, 1 - Female
    private static final DataParameter<Boolean> IS_ANGRY = EntityDataManager.createKey(ComplexMob.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COMMAND = EntityDataManager.createKey(ComplexMob.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SLEEPING = EntityDataManager.createKey(ComplexMobTerrestrial.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(ComplexMobTerrestrial.class, DataSerializers.BOOLEAN);
    public HerdEntity herd = null;
    public float turn_speed = 1F;
    public int peacefulTicks;
    public static HashMap<EntityType<?>, EntityDataHolder> ENTITY_DATA_HASH = new HashMap<>();
    public static HashMap<EntityType<?>, HashMap<Integer, String>> CLIENT_DATA_HASH = new HashMap<>();

    public ComplexMob(EntityType<? extends ComplexMob> type, World worldIn){
        super(type, worldIn);
    }

    @Override
    protected void registerData(){
        super.registerData();
        this.dataManager.register(HOME_POS, BlockPos.ZERO);
        this.dataManager.register(VARIANT, 0);
        this.dataManager.register(SKIN, 0);
        this.dataManager.register(SIZE, 1F);
        this.dataManager.register(GENDER, 0);
        this.dataManager.register(IS_ANGRY, false);
        this.dataManager.register(COMMAND, 0);
        this.dataManager.register(SLEEPING, false);
        this.dataManager.register(SITTING, false);
    }

    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote && this.peacefulTicks > 0) {
            this.peacefulTicks--;
        }
    }

    /**
     * Wrapper method to access EntityDataHolder objects, contains safeguards against accessing data before its initialization
     * @param typeIn The EntityType to access in ENTITY_DATA_HASH, or initialize it if needed</br>
     */
    public static EntityDataHolder getEntityData(EntityType<?> typeIn) {
        if (!ENTITY_DATA_HASH.containsKey(typeIn)) {
            ResourceListenerEvent.registerEntityData(typeIn);
        }
        return ENTITY_DATA_HASH.get(typeIn);
    }

    protected SoundEvent getAmbientSound() {
        try {
            return getEntityData(this.getType()).getSounds(this.getVariant(), "ambient");
        } catch (NullPointerException e) {
            return null;
        }
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        try {
            return getEntityData(this.getType()).getSounds(this.getVariant(), "hurt");
        } catch (NullPointerException e) {
            return SoundEvents.ENTITY_GENERIC_HURT;
        }
    }

    protected SoundEvent getDeathSound() {
        try {
            return getEntityData(this.getType()).getSounds(this.getVariant(), "death");
        } catch (NullPointerException e) {
            return SoundEvents.ENTITY_GENERIC_DEATH;
        }
    }

    protected SoundEvent getThreatSound() {
        try {
            return getEntityData(this.getType()).getSounds(this.getVariant(), "threat");
        } catch (NullPointerException e) {
            return null;
        }
    }

    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
        return true;
    }

    // Why is a method called 'isNotColliding()' also checking for Water?
    public boolean isNotColliding(IWorldReader worldIn) {
        return worldIn.checkNoEntityCollision(this);
    }

    public boolean canBeLeashedTo(PlayerEntity player) {
        if (player.isCreative()) {
            return !this.getLeashed();
        }
        return (!this.getLeashed() && this.isTamed());
    }

    public boolean preventDespawn() {
        return true;
    }

    public void setSleeping(boolean sleeping){ this.dataManager.set(SLEEPING, sleeping); }
    public boolean isSleeping(){ return (this.dataManager.get(SLEEPING)); }

    public void setSitting(boolean sitting){ this.dataManager.set(SITTING, sitting); }
    public boolean isSitting(){ return (this.dataManager.get(SITTING)); }
    public boolean isNotMoving(){
        return this.getMotion().x == 0 && this.getMotion().z == 0;
    }
    public boolean canBeTargeted() { return true; }
    public double getCurrentSpeed() { return Math.sqrt(this.getMotion().x * this.getMotion().x + this.getMotion().z * this.getMotion().z); }

    public int getTalkInterval() {
        //return Integer.MAX_VALUE;
        return 300;
    }

    public static void processData(EntityDataHolder dataIn, EntityType<?> typeIn) {
        ENTITY_DATA_HASH.put(typeIn, dataIn);
        processSkins(dataIn, typeIn.getRegistryName().getPath());
        // TODO: Wuh?
        for (SpeciesDataHolder speciesData : ENTITY_DATA_HASH.get(typeIn).getSpeciesData()) {
            if (!ComplexMob.CLIENT_DATA_HASH.containsKey(typeIn)) {
                ComplexMob.CLIENT_DATA_HASH.put(typeIn, new HashMap<>());
            }
            ComplexMob.CLIENT_DATA_HASH.get(typeIn).put(speciesData.getVariant(), speciesData.getName());
        }
    }

    public static void processSkins(EntityDataHolder dataIn, String nameIn) {
        for (SpeciesDataHolder speciesDatum : dataIn.getSpeciesData()) {
            EntityUtils.buildSkinArrays(nameIn, speciesDatum.getName().toLowerCase(), dataIn, speciesDatum.getVariant(), TEXTURES_COMMON, TEXTURES_RARE);
        }
    }

    public int getVariant(){ return (this.dataManager.get(VARIANT)); }
    public void setVariant(int variant){ this.dataManager.set(VARIANT, variant); }
    public int getSkin(){ return (this.dataManager.get(SKIN)); }
    public void setSkin(int skin){ this.dataManager.set(SKIN, skin); }
    public <T extends ComplexMob> int chooseSkinForSpecies(T entityIn, boolean allowRares) {
        if (entityIn.getType().getRegistryName() != null && this instanceof INewSkins && !this.world.isRemote) {
            String name = entityIn.getType().getRegistryName().getPath();
            if (!TEXTURES_COMMON.get(name).isEmpty()) {
                boolean isRare = allowRares && TEXTURES_RARE.get(name).containsKey(this.getVariant()) && this.rand.nextFloat() < ConfigGamerules.rareSkinChance.get();
                int skin = this.rand.nextInt(isRare ? TEXTURES_RARE.get(name).get(this.getVariant()).size() : TEXTURES_COMMON.get(name).get(this.getVariant()).size()) + (isRare ? 100 : 0);
                this.setSkin(skin);
                return skin;
            }
        }
        return 0;
    }

    public float getModelScale() { return getEntityData(this.getType()).getScale(this.getVariant()); }
    public float getMobSize(){ return (this.dataManager.get(SIZE)); }
    public void setMobSize(float size){ this.dataManager.set(SIZE, size); }
    public void setRandomMobSize(){ this.dataManager.set(SIZE, this.getModelScale() + ((float)this.rand.nextGaussian() * 0.1F)); }

    public void setGender(int gender){ this.dataManager.set(GENDER, gender); }
    public int getGender(){	return (this.dataManager.get(GENDER)); }
    public boolean isMale() { return this.getGender() == 0; }
    public String getGenderString() {
        return this.isMale() ? "male" : "female";
    }

    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get()) {
            if (CompatBridge.SereneSeasons) {
                return CompatSereneSeasons.isCurrentSeason(this.world, this.getBreedingSeason());
            }
            return true;
            //return this.isInLove();
        }
        return false;
    }

    @SuppressWarnings("unchecked") // Don't use this outside ComplexMobs
    public <T extends ComplexMob> void breed() {
        int bound = 1 + (this.getOffspring() > 0 ? this.rand.nextInt(this.getOffspring() + 1) : 0);
        for (int i = 0; i < bound; i++) {
            T child = (T) this.func_241840_a((ServerWorld) this.world, this);
            if (child != null) {
                child.setVariant(this.getVariant());
                child.setGrowingAge(this.getAdulthoodTime() * -1);
                child.setGender(this.rand.nextInt(2));
                child.setRandomMobSize();
                child.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F, 0.0F);
                if (this.getOwner() != null) {
                    child.setTamedBy((PlayerEntity) this.getOwner());
                }
                if (this instanceof INeedsPostUpdate) {
                    ((INeedsPostUpdate) child).updateAttributes();
                }
                if (TEXTURES_COMMON.containsKey(child.getType().getRegistryName().getPath())) {
                    chooseSkinForSpecies(child, true);
                }
                this.world.addEntity(child);
            }
        }
    }

    protected <T extends ComplexMob> T create_offspring(T entity) {
        entity.setGender(this.rand.nextInt(2));
        entity.setRandomMobSize();
        entity.setVariant(this.getVariant());
        if (this instanceof INeedsPostUpdate) {
            ((INeedsPostUpdate) this).updateAttributes();
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

    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem().equals(getEntityData(this.getType()).getFavouriteFood(this.getVariant()).getItem());
    }

    protected int getOffspring() {
        return getEntityData(this.getType()).getOffspring(this.getVariant());
    }

    public boolean isFavouriteFood(ItemStack stack) {
        return stack.getItem() == ModItems.DEBUG_LOVE_POTION.get();
    }
    public boolean canEquipItem(ItemStack stack) {
        return false;
    }

    private boolean isBlinking() {
        return this.ticksExisted % 60 > 53;
    }

    public boolean shouldRenderEyes() { return !this.isSleeping() && !this.dead && !this.isBlinking() && this.hurtTime == 0; }

    public boolean canMove() { return !this.isSitting() && !this.isSleeping(); }

    public void setHome(BlockPos position) {
        this.dataManager.set(HOME_POS, position);
    }
    public BlockPos getHome() { return this.dataManager.get(HOME_POS); }
    public Vector3d getHomeAsVec() {
        BlockPos home = this.getHome();
        return new Vector3d(home.getX(), home.getY(), home.getZ());
    }

    public void setGrowingAge(int age) {
        int i = this.growingAge;
        super.setGrowingAge(age);
        this.growingAge = age;
        if (!this.isMale() && !ConfigGamerules.easyBreeding.get()) {
            if (i > 0 && age <= 0) {
                this.breed();
            }
        }
    }

    public ResourceLocation getTexture() {
        return EntityUtils.getSkinFromEntity(this);
    }

    // Returns the ecological level of an entity. Values are data-driven, defaulting to 4 if no key is found.
    public static int getEcoLevel(LivingEntity entity) {
        ResourceLocation name = entity.getType().getRegistryName();
        if (name != null && ModEntity.eco_levels.containsKey(name.toString())) {
            return ModEntity.eco_levels.get(name.toString());
        }
        return entity instanceof MonsterEntity ? 7 : 4;
    }

    protected void setAngry(boolean isAngry) { this.dataManager.set(IS_ANGRY, isAngry); }
    public boolean isAngry() { return (this.dataManager.get(IS_ANGRY)); }

    // Commands:
    // 0 - Wander: The mob wanders around naturally
    // 1 - Follow: The mob will follow it's owner, occasionally teleporting
    // 2 - Sit: The mob will sit in place
    // 3 - Guard: The mob will sit in place and attack nearby mobs (NIY)
    // Commands can be implicitly used to check if a mob is tamed or not
    public void setCommandInt(int command) { this.dataManager.set(COMMAND, command % 3); }
    public int getCommandInt() { return (this.dataManager.get(COMMAND)); }

    public boolean shouldDespawn() { return this instanceof ISpecies && this.getHome() != BlockPos.ZERO; }
    @Override
    public void checkDespawn() {
        super.checkDespawn();
        if (this.shouldDespawn()) {
            if (!this.world.isPlayerWithin(this.getPosX(), this.getPosY(), this.getPosZ(), ConfigMobControl.critterSpawnRange.get())) {
                if (this instanceof ISpecies && this.getHome() != BlockPos.ZERO) {
                    TileEntity burrow = this.world.getTileEntity(this.getHome());
                    if (burrow instanceof CritterBurrowBlockEntity) {
                        ((CritterBurrowBlockEntity)burrow).tryEnterBurrow(this);
                        burrow.markDirty();
                    }
                }
            }
        }
    }

    public void writeAdditional(CompoundNBT compound){
        super.writeAdditional(compound);
        if (this.getHome() != BlockPos.ZERO) {
            compound.putInt("HomePosX", this.getHome().getX());
            compound.putInt("HomePosY", this.getHome().getY());
            compound.putInt("HomePosZ", this.getHome().getZ());
        }
        if (this.isTamed()) {
            compound.putInt("Command", this.getCommandInt());
        }
        compound.putInt("Variant", this.getVariant());
        compound.putInt("Skin", this.getSkin());
        compound.putFloat("Size", this.getMobSize());
        compound.putInt("Gender", this.getGender());
        compound.putBoolean("isAngry", this.isAngry());
        compound.putInt("PeacefulTicks", this.peacefulTicks);
    }

    public void readAdditional(CompoundNBT compound){
        super.readAdditional(compound);
        if (compound.contains("HomePosX")) {
            int i = compound.getInt("HomePosX");
            int j = compound.getInt("HomePosY");
            int k = compound.getInt("HomePosZ");
            this.setHome(new BlockPos(i, j, k));
        }
        if (compound.contains("OwnerUUID")) {
            this.setCommandInt(compound.getInt("Command"));
        }
        this.setVariant(compound.getInt("Variant"));
        this.setSkin(compound.getInt("Skin"));
        this.setMobSize(compound.getFloat("Size"));
        this.setGender(compound.getInt("Gender"));
        this.setAngry(compound.getBoolean("isAngry"));
        this.peacefulTicks = compound.getInt("PeacefulTicks");
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        if (reason != SpawnReason.DISPENSER && reason != SpawnReason.BUCKET) {
            if (this instanceof ISpecies) {
                Optional<RegistryKey<Biome>> optional = worldIn.func_242406_i(this.getPosition());
                if (optional.isPresent()) {
                    int i = ((ISpecies)this).setSpeciesByBiome(optional.get(), worldIn.getBiome(this.getPosition()), reason);
                    this.setVariant(i);
                    if (i == 99) {
                        this.remove();
                        return null;
                    }
                }
            }
            this.setGender(this.rand.nextInt(2));
            this.setRandomMobSize();
            if (TEXTURES_COMMON.containsKey(this.getType().getRegistryName().getPath())) {
                chooseSkinForSpecies(this, ConfigGamerules.wildRareSkins.get());
            }
            if (this instanceof INeedsPostUpdate) {
                ((INeedsPostUpdate) this).updateAttributes();
            }

            this.setGrowingAge(0);
        }
        if (this instanceof IPackEntity) {
            IPackEntity.initPack(this);
        }
        return spawnDataIn;
    }

    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        if (hand == Hand.MAIN_HAND && !this.world.isRemote()) {
            ItemStack itemstack = player.getHeldItem(hand);

            // Highlight mobs in the same pack if Player is in Creative mode
            if (player.isCreative() && itemstack.isEmpty() && this instanceof IPackEntity) {
                for (int i = 0; i < this.herd.creatureList.size(); ++i) {
                    ComplexMob creature = this.herd.creatureList.get(i);
                    creature.addPotionEffect(new EffectInstance(Effects.GLOWING, 80, 0));
                }
            }

            // Command handler for tamed mobs, includes Food/Potion consumption
            if (this.isTamed() && this.getOwner() == player) {
                if (itemstack.isEmpty()) {
                    this.setCommandInt(this.getCommandInt() + 1);
                    player.sendMessage(new TranslationTextComponent("entity.untamedwilds.command." + this.getCommandInt()), Util.DUMMY_UUID);
                    if (this.getCommandInt() > 1) {
                        this.getNavigator().clearPath();
                        this.setSitting(true);
                    } else if (this.getCommandInt() <= 1 && this.isSitting()) {
                        this.setSitting(false);
                    }
                }
                else {
                    EntityUtils.consumeItemStack(this, itemstack);
                }
            }
            return super.func_230254_b_(player, hand);
        }
        return ActionResultType.PASS;
    }
}
