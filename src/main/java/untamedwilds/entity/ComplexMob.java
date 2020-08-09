package untamedwilds.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.compat.CompatBridge;
import untamedwilds.compat.CompatSereneSeasons;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.init.ModItems;

import javax.annotation.Nullable;

public abstract class ComplexMob extends TameableEntity {

    private static final DataParameter<BlockPos> HOME_POS = EntityDataManager.createKey(ComplexMob.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Integer> SPECIES = EntityDataManager.createKey(ComplexMob.class, DataSerializers.VARINT);
    private static final DataParameter<Float> SIZE = EntityDataManager.createKey(ComplexMob.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> GENDER = EntityDataManager.createKey(ComplexMob.class, DataSerializers.VARINT); // 0 - Male, 1 - Female
    private static final DataParameter<Boolean> IS_ANGRY = EntityDataManager.createKey(ComplexMob.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COMMAND = EntityDataManager.createKey(ComplexMob.class, DataSerializers.VARINT);
    public int ecoLevel;

    public ComplexMob(EntityType<? extends ComplexMob> type, World worldIn){
        super(type, worldIn);
    }

    @Override
    protected void registerData(){
        super.registerData();
        this.dataManager.register(HOME_POS, BlockPos.ZERO);
        this.dataManager.register(SPECIES, 0);
        this.dataManager.register(SIZE, 0F);
        this.dataManager.register(GENDER, 0);
        this.dataManager.register(IS_ANGRY, false);
        this.dataManager.register(COMMAND, 0);
        this.ecoLevel = 0;
    }

    /*public static boolean canSpawnInPosition(EntityType<? extends AnimalEntity> entity, IWorld worldIn, SpawnReason reason, BlockPos pos, Random p_223316_4_) {
        return (worldIn.getBlockState(pos.down()).getBlock() == Blocks.GRASS_BLOCK || worldIn.getBlockState(pos.down()).getBlock() == Blocks.ICE);
    }*/

    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
        return true;
        //return this.getBlockPathWeight(new BlockPos(this.getPosX(), this.getBoundingBox().minY, this.getPosZ()), worldIn) >= 0.0F;
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

    //protected BodyController createBodyController() {
    //    return new SmartBodyHelper(this);
    //}

    public boolean isNotMoving(){
        return this.getMotion().x == 0 && this.getMotion().z == 0;
    }
    public boolean canBeTargeted() { return true; }
    public double getSpeed() { return Math.sqrt(this.getMotion().x * this.getMotion().x + this.getMotion().z * this.getMotion().z); }

    public int getSpecies(){ return (this.dataManager.get(SPECIES)); }
    public void setSpecies(int species){ this.dataManager.set(SPECIES, species); }
    protected int setSpeciesByBiome(Biome biome, SpawnReason reason) { return 0; }

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
            return this.isInLove();
        }
        return false;
    }
    protected String getBreedingSeason() {
        return "ALL";
    }
    public boolean isFavouriteFood(ItemStack stack) {
        return stack.getItem() == ModItems.DEBUG_LOVE_POTION.get();
    }

    public void setHome(BlockPos position) {
        this.dataManager.set(HOME_POS, position);
    }
    public BlockPos getHome() { return this.dataManager.get(HOME_POS); }
    public Vec3d getHomeAsVec() {
        BlockPos home = this.getHome();
        return new Vec3d(home.getX(), home.getY(), home.getZ());
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

    public void breed() {
        this.createChild(this);
    }

    public ResourceLocation getTexture() {
        return null;
    }

    // Returns the ecological level of an entity. Since only UntamedWilds mobs have such parameter, it is defaulted to 4, with the exception of players, who are 7 (TODO: Make this data driven)
    protected int getEcoLevel(LivingEntity entity) {
        if (entity instanceof ComplexMob) {
            return ((ComplexMob) entity).ecoLevel;
        }
        if (entity instanceof PlayerEntity) {
            return 7;
        }
        return 4;
    }

    protected void setAngry(boolean isAngry) { this.dataManager.set(IS_ANGRY, isAngry); }
    public boolean isAngry() { return (this.dataManager.get(IS_ANGRY)); }

    // Commands:
    // 0 - Wander: The mob wanders around naturally
    // 1 - Follow: The mob will follow it's owner, occasionally teleporting
    // 2 - Sit: The mob will sit in place
    // 3 - Guard: The mob will sit in place and attack nearby mobs (NIY)
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
        compound.putInt("Species", this.getSpecies());
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
        this.setSpecies(compound.getInt("Species"));
        this.setMobSize(compound.getFloat("Size"));
        this.setGender(compound.getInt("Gender"));
        this.setAngry(compound.getBoolean("isAngry"));
    }

    // This method writes this entity into a CompoundNBT Tag
    public CompoundNBT writeEntityToNBT(LivingEntity entity) {
        CompoundNBT baseTag = new CompoundNBT();
        CompoundNBT entityTag = new CompoundNBT();
        entity.writeUnlessRemoved(entityTag); // Write the entity into NBT
        baseTag.put("EntityTag", entityTag); // Put the entity in the Tag
        return baseTag;
    }

    // This function makes the entity drop some Eggs of the given item_name, and with random stacksize between 1 and number
    protected void dropEggs(String item_name, int number) {
        ItemEntity entityitem = this.entityDropItem(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(UntamedWilds.MOD_ID + ":" + item_name.toLowerCase()))), 0.2F);
        if (entityitem != null) {
            entityitem.getItem().setCount(1 + this.rand.nextInt(number - 1));
        }
    }

    // This function turns the entity into an item with item_name registry name, and removes the entity from the world
    protected void turnEntityIntoItem(String item_name) {
        ItemEntity entityitem = this.entityDropItem(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(UntamedWilds.MOD_ID + ":" + item_name.toLowerCase()))), 0.2F);
        if (entityitem != null) {
            entityitem.setMotion((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F, this.rand.nextFloat() * 0.05F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
            entityitem.getItem().setTag(this.writeEntityToNBT(this));
            if (this.hasCustomName()) {
                entityitem.getItem().setDisplayName(this.getCustomName());
            }
            this.remove();
        }
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason reason, ILivingEntityData livingdata, CompoundNBT NBTTag) {
        if (reason != SpawnReason.DISPENSER) {
            this.setRandomMobSize();
            this.setGender(this.rand.nextInt(2));
            if (this instanceof ISpecies) {
                this.setSpecies(this.setSpeciesByBiome(world.getBiome(this.getPosition()), reason));
                if (this.getSpecies() == 99) {
                    this.remove();
                    return null;
                }
                if (UntamedWilds.DEBUG && reason == SpawnReason.CHUNK_GENERATION) {
                    UntamedWilds.LOGGER.info("Spawned: " + this.getGenderString() + " " + ((ISpecies) this).getSpeciesName());
                }
            } else if (UntamedWilds.DEBUG && reason == SpawnReason.CHUNK_GENERATION) {
                UntamedWilds.LOGGER.info("Spawned: " + this.getGenderString() + " " + this.getName().getString());
            }
            this.setGrowingAge(0);
            return livingdata;
        }
        return livingdata;
    }
}