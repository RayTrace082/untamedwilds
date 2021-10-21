package untamedwilds.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EntityDataHolder {

    public static final Codec<EntityDataHolder> CODEC = RecordCodecBuilder.create((p_237051_0_) -> p_237051_0_.group(
            Codec.STRING.fieldOf("name").orElse("").forGetter((p_237056_0_) -> p_237056_0_.name),
            Codec.FLOAT.fieldOf("scale").orElse(1F).forGetter((p_237055_0_) -> p_237055_0_.modelScale),
            Codec.INT.fieldOf("rarity").orElse(0).forGetter((p_237054_0_) -> p_237054_0_.rarity),
            Codec.FLOAT.fieldOf("attack").orElse(-1F).forGetter((p_237054_0_) -> p_237054_0_.attack),
            Codec.FLOAT.fieldOf("health").orElse(-1F).forGetter((p_237054_0_) -> p_237054_0_.health),
            Codec.STRING.fieldOf("favouriteFood").orElse("").forGetter((p_237052_0_) -> p_237052_0_.favouriteFood),
            Codec.INT.fieldOf("growing_time").orElse(1).forGetter((p_237054_0_) -> p_237054_0_.growing_time),
            Codec.INT.fieldOf("offspring").orElse(1).forGetter((p_237054_0_) -> p_237054_0_.offspring),
            Codec.STRING.fieldOf("breeding_season").orElse("ANY").forGetter((p_237054_0_) -> p_237054_0_.breeding_season),
            Codec.unboundedMap(Codec.STRING, SoundEvent.CODEC).fieldOf("sounds").orElse(Collections.emptyMap()).forGetter((p_237052_0_) -> p_237052_0_.sounds),
            Codec.INT.listOf().fieldOf("flags").orElse(new ArrayList<>()).forGetter((p_237054_0_) -> p_237054_0_.flags),
            SpeciesDataHolder.CODEC.listOf().fieldOf("species").orElse(new ArrayList<>()).forGetter((p_237052_0_) -> p_237052_0_.speciesData))
            .apply(p_237051_0_, EntityDataHolder::new));
    private final String name;
    private final float modelScale;
    private final int rarity;
    private final float attack;
    private final float health;
    private final String favouriteFood;
    private final int growing_time;
    private final int offspring;
    private final String breeding_season;
    public final Map<String, SoundEvent> sounds;
    private final List<Integer> flags;
    private final List<SpeciesDataHolder> speciesData;

    public EntityDataHolder(String p_i232114_1_, float p_i232114_2_, int p_i232114_3_, float attack, float health, String favouriteFood, int growing_time, int offspring, String breeding, Map<String, SoundEvent> sounds, List<Integer> flags, List<SpeciesDataHolder> speciesData) {
        this.name = p_i232114_1_;
        this.modelScale = p_i232114_2_;
        this.rarity = p_i232114_3_;

        this.attack = attack;
        this.health = health;

        this.favouriteFood = favouriteFood;
        this.growing_time = growing_time;
        this.offspring = offspring;
        this.breeding_season = breeding;

        // ambient: Ambient sound, hurt: Hurt sound, death: Death sound, threat: Threat sound
        this.sounds = sounds;

        // Additional data I can't be arsed to properly define, due to it being specific for each class
        this.flags = flags;
        UntamedWilds.LOGGER.info(speciesData.toString());
        this.speciesData = speciesData;
    }

    public String getString() {
        return this.name + ": Scale: " + this.modelScale + " Rarity: " + this.rarity + " Attack: " + this.attack + " Health: " + this.health +  " Ambient Sound: " + this.sounds;
    }

    public void printSpeciesData() {
        for (SpeciesDataHolder speciesDatum : this.speciesData) {
            UntamedWilds.LOGGER.info(speciesDatum.getString());
        }
    }

    public float getScale(int i) {
        if (this.speciesData.get(i).getModelScale() < 0) {
            return this.modelScale;
        }
        return this.speciesData.get(i).getModelScale();
    }

    public int getRarity(int i) {
        if (this.speciesData.get(i).getRarity() < 0) {
            return this.rarity;
        }
        return this.speciesData.get(i).getRarity();
    }

    public float getAttack(int i) {
        if (this.speciesData.get(i).getAttack() < 0) {
            return this.modelScale;
        }
        return this.speciesData.get(i).getAttack();
    }

    public float getHealth(int i) {
        if (this.speciesData.get(i).getHealth() < 0) {
            return this.modelScale;
        }
        return this.speciesData.get(i).getHealth();
    }

    // TODO: May be possible to define ItemStack during resource reload, rather than calculate it every time
    public ItemStack getFavouriteFood(int i) {
        if (this.speciesData.get(i).getFavouriteFood() == null) {
            return new ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryCreate(this.favouriteFood)));
        }
        return this.speciesData.get(i).getFavouriteFood();
    }

    public float getGrowingTime(int i) {
        if (this.speciesData.get(i).getGrowingTime() < 0) {
            return this.growing_time;
        }
        return this.speciesData.get(i).getGrowingTime();
    }

    public float getOffspring(int i) {
        if (this.speciesData.get(i).getOffspring() < 0) {
            return this.offspring;
        }
        return this.speciesData.get(i).getOffspring();
    }

    public String getBreedingSeason(int i) {
        if (this.speciesData.get(i).getBreedingSeason().equals("NONE")) {
            return this.breeding_season;
        }
        return this.speciesData.get(i).getBreedingSeason();
    }

    @Nullable
    public SoundEvent getSounds(int i, String sound_id) {
        if (!this.speciesData.get(i).getSounds().isEmpty() && this.speciesData.get(i).getSounds().get(sound_id) != null) {
            this.speciesData.get(i).getSounds().get(sound_id);
        }
        return this.sounds.get(sound_id);
    }

    public List<SpeciesDataHolder> getSpeciesData() {
        return this.speciesData;
    }
}