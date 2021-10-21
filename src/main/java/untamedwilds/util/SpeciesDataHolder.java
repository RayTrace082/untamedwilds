package untamedwilds.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SpeciesDataHolder {

    public EntityDataHolder masterData;

    // Negative numbers are used to signify when to use the EntityDataHolder value instead
    public static final Codec<SpeciesDataHolder> CODEC = RecordCodecBuilder.create((p_237051_0_) -> p_237051_0_.group(
            Codec.STRING.fieldOf("name").orElse("").forGetter((p_237056_0_) -> p_237056_0_.name),
            Codec.INT.fieldOf("variant").orElse(0).forGetter((p_237054_0_) -> p_237054_0_.variant),
            Codec.FLOAT.fieldOf("scale").orElse(-1F).forGetter((p_237055_0_) -> p_237055_0_.modelScale),
            Codec.INT.fieldOf("rarity").orElse(-1).forGetter((p_237054_0_) -> p_237054_0_.rarity),
            Codec.FLOAT.fieldOf("attack").orElse(-1F).forGetter((p_237055_0_) -> p_237055_0_.attack),
            Codec.FLOAT.fieldOf("health").orElse(-1F).forGetter((p_237055_0_) -> p_237055_0_.health),
            Codec.unboundedMap(Codec.STRING, SoundEvent.CODEC).fieldOf("sounds").orElse(Collections.emptyMap()).forGetter((p_237052_0_) -> p_237052_0_.sounds),
            // TODO: Should be possible to replace this with a custom Object, with custom Codec and deeper functionality
            Codec.STRING.listOf().fieldOf("spawnBiomes").orElse(new ArrayList<>()).forGetter((p_237052_0_) -> p_237052_0_.spawnBiomes))
            .apply(p_237051_0_, SpeciesDataHolder::new));

    private final String name;
    private final int variant;
    private final Float modelScale;
    private final int rarity;
    private final float attack;
    private final float health;
    private final Map<String, SoundEvent> sounds;
    private final List<String> spawnBiomes;

    public SpeciesDataHolder(String p_i232114_1_, int variant, float p_i232114_2_, int p_i232114_3_, float attack, float health, Map<String, SoundEvent> sounds, List<String> p_i232114_4_) {
        this.name = p_i232114_1_;
        this.variant = variant;
        this.modelScale = p_i232114_2_;
        this.rarity = p_i232114_3_;
        this.attack = attack;
        this.health = health;
        this.sounds = sounds;
        this.spawnBiomes = p_i232114_4_;
    }

    public void assignMasterData(EntityDataHolder input) {
        this.masterData = input;
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