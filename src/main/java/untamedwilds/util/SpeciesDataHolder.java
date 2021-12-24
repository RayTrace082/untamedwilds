package untamedwilds.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMobTerrestrial;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SpeciesDataHolder {

    // Negative numbers are used to signify when to use the EntityDataHolder value instead
    public static final Codec<SpeciesDataHolder> CODEC = RecordCodecBuilder.create((p_237051_0_) -> p_237051_0_.group(
            Codec.STRING.fieldOf("name").orElse("").forGetter((p_237056_0_) -> p_237056_0_.name),
            Codec.INT.fieldOf("variant").orElse(0).forGetter((p_237054_0_) -> p_237054_0_.variant),
            Codec.FLOAT.fieldOf("scale").orElse(-1F).forGetter((p_237055_0_) -> p_237055_0_.modelScale),
            Codec.INT.fieldOf("rarity").orElse(-1).forGetter((p_237054_0_) -> p_237054_0_.rarity),
            Codec.FLOAT.fieldOf("attack").orElse(-1F).forGetter((p_237055_0_) -> p_237055_0_.attack),
            Codec.FLOAT.fieldOf("health").orElse(-1F).forGetter((p_237055_0_) -> p_237055_0_.health),
            ComplexMobTerrestrial.ActivityType.CODEC.fieldOf("activityType").orElse(ComplexMobTerrestrial.ActivityType.INSOMNIAC).forGetter((p_237052_0_) -> p_237052_0_.activityType),
            Codec.STRING.fieldOf("favourite_food").orElse("").forGetter((p_237052_0_) -> p_237052_0_.favouriteFood_input),
            Codec.INT.fieldOf("growing_time").orElse(-1).forGetter((p_237054_0_) -> p_237054_0_.growing_time),
            Codec.INT.fieldOf("offspring").orElse(-1).forGetter((p_237054_0_) -> p_237054_0_.offspring),
                    Codec.INT.fieldOf("skins").orElse(10).forGetter((p_237054_0_) -> p_237054_0_.skins),
                    //Codec.pair(Codec.INT, Codec.INT).fieldOf("skins").orElse(new Pair<>(1, 0)).forGetter((p_237054_0_) -> p_237054_0_.skins),
            Codec.STRING.fieldOf("breeding_season").orElse("NONE").forGetter((p_237054_0_) -> p_237054_0_.breeding_season),
            Codec.unboundedMap(Codec.STRING, SoundEvent.CODEC).fieldOf("sounds").orElse(Collections.emptyMap()).forGetter((p_237052_0_) -> p_237052_0_.sounds),
            Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("flags").orElse(Collections.emptyMap()).forGetter((p_237054_0_) -> p_237054_0_.flags),
            Codec.STRING.listOf().listOf().fieldOf("spawnBiomes").orElse(new ArrayList<>()).forGetter((p_237052_0_) -> p_237052_0_.spawnBiomes))
            .apply(p_237051_0_, SpeciesDataHolder::new));

    private final String name;
    private final int variant;
    private final Float modelScale;
    private final int rarity;
    private final float attack;
    private final float health;
    private final ComplexMobTerrestrial.ActivityType activityType;
    private final String favouriteFood_input;
    private final ItemStack favouriteFood;
    private final int growing_time;
    private final int offspring;
    private final int skins;
    private final String breeding_season;
    private final Map<String, SoundEvent> sounds;
    private final Map<String, Integer> flags;
    private final List<List<String>> spawnBiomes;
    private final List<List<BiomeTestHolder>> spawnBiomeData;

    public SpeciesDataHolder(String p_i232114_1_, int variant, float p_i232114_2_, int p_i232114_3_, float attack, float health, ComplexMobTerrestrial.ActivityType activityType, String favourite_food, int growing_time, int offspring, int skins, String breeding_season, Map<String, SoundEvent> sounds, Map<String, Integer> flags, List<List<String>> spawn_biomes) {
        this.name = p_i232114_1_;
        this.variant = variant;
        this.modelScale = p_i232114_2_;
        this.rarity = p_i232114_3_;
        this.attack = attack;
        this.health = health;
        this.activityType = activityType;
        this.favouriteFood_input = favourite_food;
        this.favouriteFood = new ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryCreate(this.favouriteFood_input)));
        this.growing_time = growing_time;
        this.offspring = offspring;
        this.skins = skins;
        this.breeding_season = breeding_season;
        this.sounds = sounds;
        this.flags = flags;
        this.spawnBiomes = spawn_biomes;
        this.spawnBiomeData = new ArrayList<>();
        for (List<String> sublist : this.spawnBiomes) {
            List<BiomeTestHolder> subsublist = new ArrayList<>();
            for (String condition : sublist) {
                String key = condition;
                if (condition.contains("|"))
                    key = condition.split("\\|")[1];
                ConditionTypes type = getTypeOfCondition(condition);
                ConditionModifiers modifier = getModifierFromString(condition);
                BiomeTestHolder testHolder = new BiomeTestHolder(key, type, modifier);
                if (UntamedWilds.DEBUG) {
                    UntamedWilds.LOGGER.info("Registering new" + modifier.getString() + "BiomeTestHolder from " + key + " with type " + type.getString());
                }
                subsublist.add(testHolder);
            }
            this.spawnBiomeData.add(subsublist);
        }
    }

    public String getString() {
        return this.name + ": Scale: " + this.modelScale + " Rarity: " + this.rarity + " Spawn Biomes: " + this.spawnBiomes;
    }

    public String getName() {
        return this.name;
    }

    public int getVariant() {
        return this.variant;
    }

    public Float getModelScale() {
        return this.modelScale;
    }

    public Integer getRarity() {
        return this.rarity;
    }

    public Float getAttack() {
        return this.attack;
    }

    public Float getHealth() {
        return this.health;
    }

    public ComplexMobTerrestrial.ActivityType getActivityType() {
        return this.activityType;
    }

    @Nullable
    public ItemStack getFavouriteFood() {
        return this.favouriteFood;
    }

    public Integer getGrowingTime() {
        return this.growing_time;
    }

    public Integer getOffspring() {
        return this.offspring;
    }

    public Integer getSkins() {
        return this.skins;
    }

    public String getBreedingSeason() {
        return this.breeding_season;
    }

    public Map<String, SoundEvent> getSounds() {
        return this.sounds;
    }

    public Map<String, Integer> getFlags() {
        return this.flags;
    }

    public List<List<BiomeTestHolder>> getBiomeCategories() {
        return this.spawnBiomeData;
    }

    public static ConditionModifiers getModifierFromString(String strIn) {
        if (strIn.contains("!"))
            return ConditionModifiers.INVERTED;
        else if (strIn.contains("#"))
            return ConditionModifiers.PRIORITY;
        return ConditionModifiers.NONE;
    }

    public static ConditionTypes getTypeOfCondition(String strIn) {
        String clean = strIn.replaceAll("[!#]","");
        if (clean.contains("|")) {
            String str = clean.split("\\|")[0];
            switch (str) {
                case "category":
                    return ConditionTypes.BIOME_CATEGORY;
                case "dictionary":
                    return ConditionTypes.FORGE_DICTIONARY;
                case "resource":
                    return ConditionTypes.REGISTRY_NAME;
            }
        }
        return ConditionTypes.BIOME_CATEGORY;
    }

    public enum ConditionTypes implements IStringSerializable {
        BIOME_CATEGORY ("Category"),
        FORGE_DICTIONARY ("Dictionary"),
        REGISTRY_NAME ("Resource Location");

        public String type;

        ConditionTypes(String type) {
            this.type = type;
        }

        @Override
        public String getString() {
            return this.type;
        }
    }

    public enum ConditionModifiers implements IStringSerializable {
        NONE (" "),
        INVERTED (" Inverted "),
        PRIORITY (" Priority ");

        public String type;

        ConditionModifiers(String type) {
            this.type = type;
        }

        @Override
        public String getString() {
            return this.type;
        }
    }

    public static class BiomeTestHolder {

        private final String key;
        private final ConditionTypes type;
        private final ConditionModifiers modifier;

        public BiomeTestHolder(String key, ConditionTypes typeIn, ConditionModifiers modifierIn) {
            this.key = key;
            this.type = typeIn;
            this.modifier = modifierIn;
        }

        public boolean isValidBiome(RegistryKey<Biome> biomekey, Biome biome) {
            boolean result = false;
            switch (this.type) {
                case BIOME_CATEGORY:
                    result = biome.getCategory() == Biome.Category.byName(this.key);
                    break;
                case FORGE_DICTIONARY:
                    result = BiomeDictionary.hasType(biomekey, BiomeDictionary.Type.getType(this.key));
                    break;
                case REGISTRY_NAME:
                    result = biome.getRegistryName().equals(new ResourceLocation(this.key));
                    break;
            }
            if (this.modifier == ConditionModifiers.INVERTED) {
                return !result;
            }
            return result;
        }
    }
}