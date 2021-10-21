package untamedwilds.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

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
            Codec.STRING.fieldOf("favouriteFood").orElse("").forGetter((p_237052_0_) -> p_237052_0_.favouriteFood),
            Codec.INT.fieldOf("growing_time").orElse(-1).forGetter((p_237054_0_) -> p_237054_0_.growing_time),
            Codec.INT.fieldOf("offspring").orElse(-1).forGetter((p_237054_0_) -> p_237054_0_.offspring),
            Codec.STRING.fieldOf("breeding_season").orElse("NONE").forGetter((p_237054_0_) -> p_237054_0_.breeding_season),
            Codec.unboundedMap(Codec.STRING, SoundEvent.CODEC).fieldOf("sounds").orElse(Collections.emptyMap()).forGetter((p_237052_0_) -> p_237052_0_.sounds),
            Codec.INT.listOf().fieldOf("flags").orElse(new ArrayList<>()).forGetter((p_237054_0_) -> p_237054_0_.flags),
            // TODO: Should be possible to replace this with a custom Object, with custom Codec and deeper functionality
            Codec.STRING.listOf().fieldOf("spawnBiomes").orElse(new ArrayList<>()).forGetter((p_237052_0_) -> p_237052_0_.spawnBiomes))
            .apply(p_237051_0_, SpeciesDataHolder::new));

    private final String name;
    private final int variant;
    private final Float modelScale;
    private final int rarity;
    private final float attack;
    private final float health;
    private final String favouriteFood;
    private final int growing_time;
    private final int offspring;
    private final String breeding_season;
    private final Map<String, SoundEvent> sounds;
    private final List<Integer> flags;
    private final List<String> spawnBiomes;

    public SpeciesDataHolder(String p_i232114_1_, int variant, float p_i232114_2_, int p_i232114_3_, float attack, float health, String favourite_food, int growing_time, int offspring, String breeding_season, Map<String, SoundEvent> sounds, List<Integer> flags, List<String> p_i232114_4_) {
        this.name = p_i232114_1_;
        this.variant = variant;
        this.modelScale = p_i232114_2_;
        this.rarity = p_i232114_3_;
        this.attack = attack;
        this.health = health;
        this.favouriteFood = favourite_food;
        this.growing_time = growing_time;
        this.offspring = offspring;
        this.breeding_season = breeding_season;
        this.sounds = sounds;
        this.flags = flags;
        this.spawnBiomes = p_i232114_4_;
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

    @Nullable
    public ItemStack getFavouriteFood() {
        return new ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryCreate(this.favouriteFood)));
    }

    public Integer getGrowingTime() {
        return this.growing_time;
    }

    public Integer getOffspring() {
        return this.offspring;
    }

    public String getBreedingSeason() {
        return this.breeding_season;
    }

    public Map<String, SoundEvent> getSounds() {
        return this.sounds;
    }

    public List<Biome.Category> getBiomeCategories() {
        List<Biome.Category> result = new ArrayList<>();
        for (String biomeCategories : this.spawnBiomes) {
            result.add(Biome.Category.byName(biomeCategories));
        }
        return result;
    }
}