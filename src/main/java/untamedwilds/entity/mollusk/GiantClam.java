package untamedwilds.entity.mollusk;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ISpecies;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GiantClam extends ComplexMob implements ISpecies {

    private static final int GROWING = 6 * ConfigGamerules.cycleLength.get();
    private static final String BREEDING = "LATE_SUMMER";

    private static final DataParameter<Boolean> IS_OPEN = EntityDataManager.createKey(ComplexMob.class, DataSerializers.BOOLEAN);
    public int closeProgress;

    public GiantClam(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.ecoLevel = 2;
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(12D);
        this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.entityCollisionReduction = 1F;
        this.dataManager.register(IS_OPEN, true);
    }

    public void applyEntityCollision(Entity entityIn) {
    }

    public float getCollisionBorderSize() {
        return 0.0F;
    }

    @Override
    public void baseTick() {
        int i = this.getAir();
        super.baseTick();
        if (this.isAlive() && !this.isInWater()) {
            --i;
            this.setAir(i);

            if (this.getAir() == -20) {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.DROWN, 2.0F);
            }
        }
        else {
            this.setAir(300);
        }
    }

    public void livingTick() {
        super.livingTick();
        double speedY = this.getMotion().getY();
        this.setMotion(0, speedY, 0);
        if (!this.world.isRemote) {
            if (this.ticksExisted % 1000 == 0) {
                if (this.wantsToBreed()) {
                    this.breed();
                }
            }
            this.setOpen(this.world.isDaytime());
        }
        if (this.world.isRemote) {
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
        if (ConfigGamerules.naturalBreeding.get() && this.getGrowingAge() == 0 && this.getHealth() == this.getMaxHealth()) {
            List<GiantClam> list = this.world.getEntitiesWithinAABB(GiantClam.class, this.getBoundingBox().grow(12.0D, 6.0D, 12.0D));
            list.removeIf(input -> input.getSpecies() != this.getSpecies());
            list.removeIf(input -> input == this || input.getGrowingAge() != 0);
            if (list.size() >= 1) {
                this.setGrowingAge(GROWING);
                list.get(0).setGrowingAge(GROWING);
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageableEntity) {
        dropEggs("egg_giant_clam_" + this.getRawSpeciesName().toLowerCase(), 4);
        return null;
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);

        if (!this.world.isRemote && !this.isBreedingItem(itemstack) && itemstack.getItem() instanceof ShovelItem && this.isAlive() && hand == Hand.MAIN_HAND) {
            if (this.rand.nextInt(4) == 0) {
                // TODO: "Clam Digger" Advancement trigger here
                world.playSound(null, this.getPosition(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.BLOCKS, 1.0F, 0.8F);
                turnEntityIntoItem("giant_clam_" + this.getRawSpeciesName().toLowerCase());
                return true;
            }
            else {
                world.playSound(null, this.getPosition(), SoundEvents.ENTITY_SHULKER_HURT_CLOSED, SoundCategory.BLOCKS, 1.0F, 0.8F);
                ((ServerWorld)this.world).spawnParticle(ParticleTypes.SMOKE, this.getPosX(), this.getPosY() + (double)this.getHeight() / 1.5D, this.getPosZ(), 3, this.getWidth() / 4.0F, this.getHeight() / 4.0F, this.getWidth() / 4.0F, 0.05D);
            }
        }
        return super.processInteract(player, hand);
    }

    @Override
    public int setSpeciesByBiome(Biome biome, SpawnReason reason) {
        if (biome == Biomes.WARM_OCEAN || biome == Biomes.LUKEWARM_OCEAN) {
            if (ConfigGamerules.randomSpecies.get()) {
                return this.rand.nextInt(SpeciesGiantClam.values().length);
            }
            return SpeciesGiantClam.getSpeciesByBiome(biome);
        }
        if (reason == SpawnReason.SPAWN_EGG || reason == SpawnReason.BUCKET) {
            return this.rand.nextInt(SpeciesGiantClam.values().length);
        }
        return 99;
    }
    public String getSpeciesName() { return new TranslationTextComponent("item.untamedwilds.giant_clam_" + this.getRawSpeciesName()).getUnformattedComponentText(); }
    public String getRawSpeciesName() { return SpeciesGiantClam.values()[this.getSpecies()].name().toLowerCase(); }

    public boolean canBeTargeted() { return false; }
    private boolean isOpen(){ return (this.dataManager.get(IS_OPEN)); }
    private void setOpen(boolean open){ this.dataManager.set(IS_OPEN, open); }

    public String getBreedingSeason() {
        return BREEDING;
    }
    public int getAdulthoodTime() { return GROWING; }

    public void writeAdditional(CompoundNBT compound){ // Write NBT Tags
        super.writeAdditional(compound);
        compound.putBoolean("isOpen", this.isOpen());
    }

    public void readAdditional(CompoundNBT compound){ // Read NBT Tags
        super.readAdditional(compound);
        this.setOpen(compound.getBoolean("isOpen"));
    }

    public enum SpeciesGiantClam implements IStringSerializable {

        DERASA			(0, "Tridacna derasa", 		1.0F, 1, BiomeDictionary.Type.OCEAN),
        GIGAS			(1, "Tridacna gigas", 		1.2F, 3, BiomeDictionary.Type.OCEAN), // ??
        MAXIMA          (2, "Tridacna maxima",       0.7F, 2, BiomeDictionary.Type.OCEAN),
        SQUAMOSA	    (3, "Tridacna squamosa", 	0.8F, 2, BiomeDictionary.Type.OCEAN);

        public String sciname;
        public Float scale;

        public int species;
        public int rolls;
        public BiomeDictionary.Type[] spawnBiomes;

        SpeciesGiantClam(int species, String sname, Float scale, int rolls, BiomeDictionary.Type... biomes) {
            this.species = species;
            this.sciname = sname;
            this.scale = scale;
            this.rolls = rolls;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        public String getName() {
            return I18n.format("entity.giantclam." + this.name().toLowerCase());
        }

        public static int getSpeciesByBiome(Biome biome) {
            List<GiantClam.SpeciesGiantClam> types = new ArrayList<>();
            for (GiantClam.SpeciesGiantClam type : values()) {
                for(BiomeDictionary.Type biomeTypes : type.spawnBiomes) {
                    if(BiomeDictionary.hasType(biome, biomeTypes)){
                        for (int i=0; i < type.rolls; i++) {
                            types.add(type);
                        }
                    }
                }
            }
            if (types.isEmpty()) {
                return 99;
            } else {
                return types.get(new Random().nextInt(types.size())).getSpecies();
            }
        }
    }
}