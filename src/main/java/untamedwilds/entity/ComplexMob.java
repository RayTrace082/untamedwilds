package untamedwilds.entity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
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
    private static final DataParameter<Integer> SPECIES = EntityDataManager.createKey(ComplexMob.class, DataSerializers.VARINT);
    private static final DataParameter<Float> SIZE = EntityDataManager.createKey(ComplexMob.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> GENDER = EntityDataManager.createKey(ComplexMob.class, DataSerializers.VARINT); // 0 - Male, 1 - Female
    private static final DataParameter<Boolean> IS_ANGRY = EntityDataManager.createKey(ComplexMob.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COMMAND = EntityDataManager.createKey(ComplexMob.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SLEEPING = EntityDataManager.createKey(ComplexMobTerrestrial.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(ComplexMobTerrestrial.class, DataSerializers.BOOLEAN);
    public HerdEntity herd = null;
    int maxSchoolSize = 8;
    protected LivingEntity followEntity;
    protected Vector3d targetVec;

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

    public int getSpecies(){ return (this.dataManager.get(SPECIES)); }
    public void setSpecies(int species){ this.dataManager.set(SPECIES, species); }
    protected int setSpeciesByBiome(RegistryKey<Biome> biomekey, Biome biome, SpawnReason reason) { return 0; }

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
    @SuppressWarnings("unchecked") // Don't use this outside ComplexMobs
    public <T extends ComplexMob> void breed() {
        int bound = 1 + (this.getOffspring() > 0 ? this.rand.nextInt(this.getOffspring() + 1) : 0);
        for (int i = 0; i < bound; i++) {
            T child = (T) this.func_241840_a((ServerWorld) this.world, this);
            if (child != null) {
                child.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F, 0.0F);
                if (this instanceof ISkins) {
                    child.setSpecies(this.getSpecies());
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
        if (entity.getSkinNumber() != 0) {
            entity.setSpecies(this.getSpecies());
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
        // TODO: Too many constant calls to getEcoLevel, don't do that
        // UntamedWilds.LOGGER.info(entity.getEntityString());
        if (ModEntity.eco_levels.containsKey(entity.getEntityString())) {
            // UntamedWilds.LOGGER.info(ModEntity.eco_levels.get(entity.getEntityString()));
            return ModEntity.eco_levels.get(entity.getEntityString());
        }
        else if (entity instanceof MonsterEntity || entity instanceof PlayerEntity) {
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

    // This function replaces a given ItemStack with a new item with item_name registry name, and removes the entity from the world
    protected void mutateEntityIntoItem(PlayerEntity player, Hand hand, String item_name, ItemStack itemstack) {
        this.playSound(SoundEvents.ITEM_BUCKET_FILL_FISH, 1.0F, 1.0F);
        itemstack.shrink(1);
        ItemStack newitem = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(UntamedWilds.MOD_ID + ":" + item_name.toLowerCase())));
        newitem.setTag(this.writeEntityToNBT(this));
        if (this.hasCustomName()) {
            newitem.setDisplayName(this.getCustomName());
        }
        if (!this.world.isRemote) {
            CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity)player, newitem);
        }
        if (itemstack.isEmpty()) {
            player.setHeldItem(hand, newitem);
        } else if (!player.inventory.addItemStackToInventory(newitem)) {
            player.dropItem(newitem, false);
        }
        this.remove();
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        if (reason != SpawnReason.DISPENSER) {
            this.setRandomMobSize();
            this.setGender(this.rand.nextInt(2));
            if (this instanceof ISpecies) {
                Optional<RegistryKey<Biome>> optional = worldIn.func_242406_i(this.getPosition());
                int i = this.setSpeciesByBiome(optional.get(), worldIn.getBiome(this.getPosition()), reason);
                this.setSpecies(i);
                if (i == 99) {
                    this.remove();
                    return null;
                }
                if (UntamedWilds.DEBUG && reason == SpawnReason.CHUNK_GENERATION) {
                    UntamedWilds.LOGGER.info("Spawned: " + this.getGenderString() + " " + ((ISpecies) this).getSpeciesName());
                }
            } else if (UntamedWilds.DEBUG && reason == SpawnReason.CHUNK_GENERATION) {
                UntamedWilds.LOGGER.info("Spawned: " + this.getGenderString() + " " + this.getName().getString());
            }
            if (this instanceof ISkins) {
                this.setSpecies(this.rand.nextInt(getSkinNumber()));
            }
            if (this instanceof IPackEntity) {
                this.initPack();
            }
            //worldIn.func_242417_l(this);
            this.setGrowingAge(0);
        }
        return spawnDataIn;
    }

    public void initPack() {
        this.herd = new HerdEntity(this);
    }

    public boolean shouldLeavePack() {
        return false;
        //return this.rand.nextInt(1800) == 0;
    }

    public boolean canCombineWith(HerdEntity otherPack) {
        return true;
    }

    // Method exclusive for classes inheriting ISkins
    public int getSkinNumber() {
        return 0;
    }
}