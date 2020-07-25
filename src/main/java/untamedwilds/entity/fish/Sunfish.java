package untamedwilds.entity.fish;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAquatic;
import untamedwilds.entity.ISpecies;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sunfish extends ComplexMobAquatic implements ISpecies {

    private static final String BREEDING = "EARLY_AUTUMN";
    private static final int GROWING = 6 * ConfigGamerules.cycleLength.get();

    public int baskProgress;

    public Sunfish(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.experienceValue = 5;
        this.ecoLevel = 7;
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45F);
        this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new SwimGoal(this));
    }

    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote) {
            if (this.ticksExisted % 1000 == 0) {
                if (this.wantsToBreed() && !this.isMale()) {
                    this.breed();
                }
            }
            if (this.world.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
        }
        if (this.world.isRemote && !world.hasWater(this.getPosition().up().up()) && this.baskProgress < 100) {
            this.baskProgress++;
        } else if (this.world.isRemote && world.hasWater(this.getPosition().up().up()) && this.baskProgress > 0) {
            this.baskProgress--;
        }
    }

    /* Breeding conditions for the Sunfish are:
     * A nearby Sunfish */
    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.getGrowingAge() == 0 && this.getHealth() == this.getMaxHealth()) {
            List<Sunfish> list = this.world.getEntitiesWithinAABB(Sunfish.class, this.getBoundingBox().grow(12.0D, 8.0D, 12.0D));
            list.removeIf(input -> (input.getGender() == this.getGender()) || input.getGrowingAge() != 0);
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
        ItemEntity entityItem = this.entityDropItem(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(UntamedWilds.MOD_ID + ":egg_sunfish_" + this.getRawSpeciesName().toLowerCase()))), 0.2F);
        entityItem.getItem().setCount(2 + this.rand.nextInt(3));
        return null;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_GUARDIAN_FLOP;
    }
    public int getAdulthoodTime() { return GROWING; }
    public String getBreedingSeason() { return BREEDING; }

    public boolean isPotionApplicable(EffectInstance potionEffectIn) {
        return potionEffectIn.getPotion() != Effects.POISON && super.isPotionApplicable(potionEffectIn);
    }

    @Override
    public int setSpeciesByBiome(Biome biome, SpawnReason reason) {
        if (biome == Biomes.DEEP_LUKEWARM_OCEAN || biome == Biomes.DEEP_OCEAN || biome == Biomes.DEEP_COLD_OCEAN) {
            if (ConfigGamerules.randomSpecies.get()) {
                return this.rand.nextInt(Sunfish.SpeciesSunfish.values().length);
            }
            return Sunfish.SpeciesSunfish.getSpeciesByBiome(biome);
        }
        if (reason == SpawnReason.SPAWN_EGG || reason == SpawnReason.BUCKET) {
            return this.rand.nextInt(Sunfish.SpeciesSunfish.values().length);
        }
        return 99;
    }

    public String getSpeciesName() { return new TranslationTextComponent("entity.untamedwilds.sunfish_" + this.getRawSpeciesName()).getUnformattedComponentText(); }
    public String getRawSpeciesName() { return Sunfish.SpeciesSunfish.values()[this.getSpecies()].name().toLowerCase(); }

    public enum SpeciesSunfish implements IStringSerializable {

        SUNFISH			(0, 1F, 2, BiomeDictionary.Type.OCEAN),
        SOUTHERN    	(1, 1.1F, 1, BiomeDictionary.Type.OCEAN);

        public Float sizeMult;
        public int species;
        public int rolls;
        public BiomeDictionary.Type[] spawnBiomes;

        SpeciesSunfish(int species, Float smult, int rolls, BiomeDictionary.Type... biomes) {
            this.species = species;
            this.sizeMult = smult;
            this.rolls = rolls;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        public String getName() {
            return I18n.format("entity.sunfish." + this.name().toLowerCase());
        }

        public static int getSpeciesByBiome(Biome biome) {
            List<Sunfish.SpeciesSunfish> types = new ArrayList<>();

            for (Sunfish.SpeciesSunfish type : values()) {
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
