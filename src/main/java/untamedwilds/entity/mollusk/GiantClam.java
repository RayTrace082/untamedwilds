package untamedwilds.entity.mollusk;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
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
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ISpecies;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GiantClam extends ComplexMob implements ISpecies {

    private static final int GROWING = 6 * ConfigGamerules.cycleLength.get();
    private static final String BREEDING = "LATE_SUMMER";
    private static final DataParameter<Boolean> CLAM_OPEN = EntityDataManager.createKey(GiantClam.class, DataSerializers.BOOLEAN);
    public int closeProgress;

    public GiantClam(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.ecoLevel = 2;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 1D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1D)
                .createMutableAttribute(Attributes.ARMOR, 12D);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.entityCollisionReduction = 1F;
        // this.dataManager.register(CLAM_OPEN, true);
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
        // The following locks the X and Z position to zero, preventing the entity from being pushed
        this.setMotion(0, this.getMotion().getY(), 0);
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
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        dropEggs("egg_giant_clam_" + this.getRawSpeciesName().toLowerCase(), 4);
        return null;
    }

    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);

        if (!this.world.isRemote && !this.isBreedingItem(itemstack) && itemstack.getItem() instanceof ShovelItem && this.isAlive() && hand == Hand.MAIN_HAND) {
            if (this.rand.nextInt(4) == 0) {
                // TODO: "Clam Digger" Advancement trigger here
                world.playSound(null, this.getPosition(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.BLOCKS, 1.0F, 0.8F);
                turnEntityIntoItem("giant_clam_" + this.getRawSpeciesName().toLowerCase());
                return ActionResultType.SUCCESS;
            }
            else {
                world.playSound(null, this.getPosition(), SoundEvents.ENTITY_SHULKER_HURT_CLOSED, SoundCategory.BLOCKS, 1.0F, 0.8F);
                EntityUtils.spawnParticlesOnEntity(world, this, ParticleTypes.SMOKE, 3, 1);
            }
        }
        return super.func_230254_b_(player, hand);
    }

    @Override
    public int setSpeciesByBiome(RegistryKey<Biome> biomekey, Biome biome, SpawnReason reason) {
        if (biomekey.equals(Biomes.WARM_OCEAN) || biomekey.equals(Biomes.LUKEWARM_OCEAN)) {
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
    private boolean isOpen(){ return (this.dataManager.get(CLAM_OPEN)); }
    private void setOpen(boolean open){ this.dataManager.set(CLAM_OPEN, open); }

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

        DERASA			(0, "Tridacna derasa", 		1.0F, 1, Biome.Category.OCEAN),
        GIGAS			(1, "Tridacna gigas", 		1.2F, 3, Biome.Category.OCEAN), // ??
        MAXIMA          (2, "Tridacna maxima",       0.7F, 2, Biome.Category.OCEAN),
        SQUAMOSA	    (3, "Tridacna squamosa", 	0.8F, 2, Biome.Category.OCEAN);

        public String sciname;
        public Float scale;

        public int species;
        public int rolls;
        public Biome.Category[] spawnBiomes;

        SpeciesGiantClam(int species, String sname, Float scale, int rolls, Biome.Category... biomes) {
            this.species = species;
            this.sciname = sname;
            this.scale = scale;
            this.rolls = rolls;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        public String getString() {
            return I18n.format("entity.giantclam." + this.name().toLowerCase());
        }

        public static int getSpeciesByBiome(Biome biome) {
            List<SpeciesGiantClam> types = new ArrayList<>();
            for (SpeciesGiantClam type : values()) {
                for(Biome.Category biomeTypes : type.spawnBiomes) {
                    if(biome.getCategory() == biomeTypes){
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