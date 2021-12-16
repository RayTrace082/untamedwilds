package untamedwilds.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
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
            Codec.STRING.fieldOf("favourite_food").orElse("").forGetter((p_237052_0_) -> p_237052_0_.favouriteFood),
            Codec.INT.fieldOf("growing_time").orElse(-1).forGetter((p_237054_0_) -> p_237054_0_.growing_time),
            Codec.INT.fieldOf("offspring").orElse(-1).forGetter((p_237054_0_) -> p_237054_0_.offspring),
                    Codec.INT.fieldOf("skins").orElse(10).forGetter((p_237054_0_) -> p_237054_0_.skins),
                    //Codec.pair(Codec.INT, Codec.INT).fieldOf("skins").orElse(new Pair<>(1, 0)).forGetter((p_237054_0_) -> p_237054_0_.skins),
            Codec.STRING.fieldOf("breeding_season").orElse("NONE").forGetter((p_237054_0_) -> p_237054_0_.breeding_season),
            Codec.unboundedMap(Codec.STRING, SoundEvent.CODEC).fieldOf("sounds").orElse(Collections.emptyMap()).forGetter((p_237052_0_) -> p_237052_0_.sounds),
            Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("flags").orElse(Collections.emptyMap()).forGetter((p_237054_0_) -> p_237054_0_.flags),
            // TODO: Should be possible to replace this with a custom Object, with custom Codec and deeper functionality
            Codec.STRING.listOf().listOf().fieldOf("spawnBiomes").orElse(new ArrayList<>()).forGetter((p_237052_0_) -> p_237052_0_.spawnBiomes))
            .apply(p_237051_0_, SpeciesDataHolder::new));

    private final String name;
    private final int variant;
    private final Float modelScale;
    private final int rarity;
    private final float attack;
    private final float health;
    private final ComplexMobTerrestrial.ActivityType activityType;
    private final String favouriteFood;
    private final int growing_time;
    private final int offspring;
    private final int skins;
    private final String breeding_season;
    private final Map<String, SoundEvent> sounds;
    private final Map<String, Integer> flags;
    private final List<List<String>> spawnBiomes;
    private final List<List<BiomeDataHolder.BiomeTestHolder>> spawnBiomeData;

    public SpeciesDataHolder(String p_i232114_1_, int variant, float p_i232114_2_, int p_i232114_3_, float attack, float health, ComplexMobTerrestrial.ActivityType activityType, String favourite_food, int growing_time, int offspring, int skins, String breeding_season, Map<String, SoundEvent> sounds, Map<String, Integer> flags, List<List<String>> p_i232114_4_) {
        this.name = p_i232114_1_;
        this.variant = variant;
        this.modelScale = p_i232114_2_;
        this.rarity = p_i232114_3_;
        this.attack = attack;
        this.health = health;
        this.activityType = activityType;
        this.favouriteFood = favourite_food;
        this.growing_time = growing_time;
        this.offspring = offspring;
        this.skins = skins;
        this.breeding_season = breeding_season;
        this.sounds = sounds;
        this.flags = flags;
        this.spawnBiomes = p_i232114_4_;
        this.spawnBiomeData = new ArrayList<>();
        for (List<String> sublist : this.spawnBiomes) {
            List<BiomeDataHolder.BiomeTestHolder> subsublist = new ArrayList<>();
            for (String condition : sublist) {
                String key = condition;
                if (condition.contains("|"))
                    key = condition.split("\\|")[1];
                BiomeDataHolder.ConditionTypes type = BiomeDataHolder.getTypeOfCondition(condition);
                BiomeDataHolder.ConditionModifiers modifier = BiomeDataHolder.getModifierFromString(condition);
                BiomeDataHolder.BiomeTestHolder testHolder = new BiomeDataHolder.BiomeTestHolder(key, type, modifier);
                UntamedWilds.LOGGER.info("Registering new BiomeTestHolder from " + key + " with type " + type.getString());
                subsublist.add(testHolder);
            }
            this.spawnBiomeData.add(subsublist);
        }
    }

    /*public SpeciesDataHolder(CompoundNBT nbtData) {
        this.name = nbtData.getString("name");
        this.variant = nbtData.getInt("variant");
        this.modelScale = nbtData.getFloat("scale");
        this.rarity = nbtData.getInt("rarity");
        this.attack = nbtData.getFloat("attack");
        this.health = nbtData.getFloat("health");
        this.activityType = ComplexMobTerrestrial.ActivityType.valueOf(nbtData.getString("activityType"));
        this.favouriteFood = nbtData.getString("favourite_food");
        this.growing_time = nbtData.getInt("growing_time");
        this.offspring = nbtData.getInt("offspring");
        this.skins = nbtData.getInt("skins");
        this.breeding_season = nbtData.getString("breeding_season");
        Map<String, SoundEvent> sounds = new HashMap<>();
        for (String nbt : nbtData.getCompound("sounds").keySet()) {
            sounds.put(nbt, new SoundEvent(new ResourceLocation(nbtData.getCompound("sounds").getString(nbt))));
        }
        this.sounds = sounds;
        Map<String, Integer> flags = new HashMap<>();
        for (String nbt : nbtData.getCompound("flags").keySet()) {
            flags.put(nbt, nbtData.getCompound("flags").getInt(nbt));
        }
        this.flags = flags;
        this.spawnBiomes = new ArrayList<>(nbtData.getCompound("spawnBiomes").keySet());
    }*/

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
    public String getFavouriteFood() {
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

    public List<List<BiomeDataHolder.BiomeTestHolder>> getBiomeCategories() {
        return this.spawnBiomeData;
    }

    public boolean canSpawnInBiome(RegistryKey<Biome> biomekey, Biome biome) {
        for (List<BiomeDataHolder.BiomeTestHolder> testList : this.spawnBiomeData) {
            for (BiomeDataHolder.BiomeTestHolder test : testList) {
                if (!test.isValidBiome(biomekey, biome)) {
                    break;
                }
            }
            return true;
        }
        return false;
    }

    /*public CompoundNBT writeEntityDataToNBT() {
        CompoundNBT result = new CompoundNBT();
        result.putString("name", this.name);
        result.putInt("variant", this.variant);
        result.putFloat("scale", this.modelScale);
        result.putInt("rarity", this.rarity);
        result.putFloat("attack", this.attack);
        result.putFloat("health", this.health);
        result.putString("activityType", this.activityType.toString());
        result.putString("favourite_food", this.favouriteFood);
        result.putInt("growing_time", this.growing_time);
        result.putInt("offspring", this.offspring);
        result.putString("breeding_season", this.breeding_season);
        CompoundNBT sounds = new CompoundNBT();
        for (Map.Entry<String, SoundEvent> sound_data : this.sounds.entrySet()) {
            sounds.putString(sound_data.getKey(), sound_data.getValue().getRegistryName().toString());
        }
        result.put("sounds", sounds);
        CompoundNBT flags = new CompoundNBT();
        for (Map.Entry<String, Integer> flags_data : this.flags.entrySet()) {
            flags.putInt(flags_data.getKey(), flags_data.getValue());
        }
        result.put("flags", flags);
        CompoundNBT spawnBiomes = new CompoundNBT();
        for (String spawn_biomes : this.spawnBiomes) {
            spawnBiomes.putString(spawn_biomes, spawn_biomes);
        }
        result.put("spawnBiomes", spawnBiomes);
        return result;
    }*/
}