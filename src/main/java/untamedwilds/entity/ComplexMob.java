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
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.UntamedWilds;
import untamedwilds.compat.CompatBridge;
import untamedwilds.compat.CompatSereneSeasons;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModItems;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class ComplexMob extends TameableEntity {

    private static final DataParameter<BlockPos> HOME_POS = EntityDataManager.createKey(ComplexMob.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(ComplexMob.class, DataSerializers.VARINT);
    private static final DataParameter<Float> SIZE = EntityDataManager.createKey(ComplexMob.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> GENDER = EntityDataManager.createKey(ComplexMob.class, DataSerializers.VARINT); // 0 - Male, 1 - Female
    private static final DataParameter<Boolean> IS_ANGRY = EntityDataManager.createKey(ComplexMob.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COMMAND = EntityDataManager.createKey(ComplexMob.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SLEEPING = EntityDataManager.createKey(ComplexMobTerrestrial.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(ComplexMobTerrestrial.class, DataSerializers.BOOLEAN);
    public HerdEntity herd = null;

    public ComplexMob(EntityType<? extends ComplexMob> type, World worldIn){
        super(type, worldIn);
    }

    @Override
    protected void registerData(){
        super.registerData();
        this.dataManager.register(HOME_POS, BlockPos.ZERO);
        this.dataManager.register(VARIANT, 0);
        this.dataManager.register(SIZE, 0F);
        this.dataManager.register(GENDER, 0);
        this.dataManager.register(IS_ANGRY, false);
        this.dataManager.register(COMMAND, 0);
        this.dataManager.register(SLEEPING, false);
        this.dataManager.register(SITTING, false);
    }

    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
        return true;
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
    public double getSpeed() { return Math.sqrt(this.getMotion().x * this.getMotion().x + this.getMotion().z * this.getMotion().z); }

    public int getVariant(){ return (this.dataManager.get(VARIANT)); }
    public void setVariant(int variant){ this.dataManager.set(VARIANT, variant); }

    public float getModelScale() { return 1f; }
    public float getMobSize(){ return (this.dataManager.get(SIZE)); }
    public void setMobSize(float size){ this.dataManager.set(SIZE, size); }
    public void setRandomMobSize(){ this.dataManager.set(SIZE, Math.abs((float)this.rand.nextGaussian())/2); }

    public void setGender(int gender){ this.dataManager.set(GENDER, gender); }
    public int getGender(){	return (this.dataManager.get(GENDER)); }
    public boolean isMale() { return this.getGender() == 0; }
    public String getGenderString() {
        return this.isMale() ? "male" : "female";
    }

    public int getPregnancyTime() { return 24000; }
    public int getAdulthoodTime() { return 24000; }
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
                child.setGrowingAge(this.getGrowingAge() * -1);
                child.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F, 0.0F);
                if (this instanceof ISkins) {
                    child.setVariant(this.getVariant());
                }
                if (this.getOwner() != null) {
                    child.setTamedBy((PlayerEntity) this.getOwner());
                }
                this.world.addEntity(child);
            }
        }
    }
    protected <T extends ComplexMob> T create_offspring(T entity) {
        entity.setGender(this.rand.nextInt(2));
        entity.setMobSize(this.rand.nextFloat());
        entity.setGrowingAge(entity.getAdulthoodTime() * -2);
        if (entity instanceof ISkins) {
            entity.setVariant(this.getVariant());
        }
        return entity;
    }

    protected int getOffspring() {
        return 0;
    }
    protected String getBreedingSeason() {
        return "ALL";
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
        return null;
    }

    // Returns the ecological level of an entity. Values are data-driven, defaulting to 4 if no key is found.
    protected int getEcoLevel(LivingEntity entity) {
        if (ModEntity.eco_levels.containsKey(entity.getEntityString())) {
            return ModEntity.eco_levels.get(entity.getEntityString());
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

    public void writeAdditional(CompoundNBT compound){ // Write NBT Tags
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
        compound.putFloat("Size", this.getMobSize());
        compound.putInt("Gender", this.getGender());
        compound.putBoolean("isAngry", this.isAngry());
    }

    public void readAdditional(CompoundNBT compound){ // Read NBT Tags
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
        this.setMobSize(compound.getFloat("Size"));
        this.setGender(compound.getInt("Gender"));
        this.setAngry(compound.getBoolean("isAngry"));
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        if (reason != SpawnReason.DISPENSER) {
            this.setRandomMobSize();
            this.setGender(this.rand.nextInt(2));
            if (this instanceof ISpecies) {
                Optional<RegistryKey<Biome>> optional = worldIn.func_242406_i(this.getPosition());
                if (optional.isPresent()) {
                    int i = ((ISpecies)this).setSpeciesByBiome(optional.get(), worldIn.getBiome(this.getPosition()), reason);
                    this.setVariant(i);
                    if (i == 99) {
                        this.remove();
                        return null;
                    }
                    if (UntamedWilds.DEBUG && reason == SpawnReason.CHUNK_GENERATION) {
                        UntamedWilds.LOGGER.info("Spawned: " + this.getGenderString() + " " + ((ISpecies) this).getSpeciesName());
                    }
                }
            } else if (UntamedWilds.DEBUG && reason == SpawnReason.CHUNK_GENERATION) {
                UntamedWilds.LOGGER.info("Spawned: " + this.getGenderString() + " " + this.getName().getString());
            }
            if (this instanceof ISkins) {
                this.setVariant(this.rand.nextInt(((ISkins)this).getSkinNumber()));
            }
            if (this instanceof IPackEntity) {
                IPackEntity.initPack(this);
            }
            this.setGrowingAge(0);
        }
        return spawnDataIn;
    }
}