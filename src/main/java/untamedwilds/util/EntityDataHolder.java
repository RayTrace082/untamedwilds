package untamedwilds.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMobTerrestrial;

import javax.annotation.Nullable;
import java.util.*;

public class EntityDataHolder {

    public static final Codec<EntityDataHolder> CODEC = RecordCodecBuilder.create((p_237051_0_) -> p_237051_0_.group(
            Codec.STRING.fieldOf("name").orElse("").forGetter((p_237056_0_) -> p_237056_0_.name),
            Codec.FLOAT.fieldOf("scale").orElse(1F).forGetter((p_237055_0_) -> p_237055_0_.modelScale),
            Codec.INT.fieldOf("rarity").orElse(0).forGetter((p_237054_0_) -> p_237054_0_.rarity),
            Codec.FLOAT.fieldOf("attack").orElse(-1F).forGetter((p_237054_0_) -> p_237054_0_.attack),
            Codec.FLOAT.fieldOf("health").orElse(-1F).forGetter((p_237054_0_) -> p_237054_0_.health),
            ComplexMobTerrestrial.ActivityType.CODEC.fieldOf("activityType").orElse(ComplexMobTerrestrial.ActivityType.INSOMNIAC).forGetter((p_237052_0_) -> p_237052_0_.activityType),
            Codec.STRING.fieldOf("favourite_food").orElse("").forGetter((p_237052_0_) -> p_237052_0_.favouriteFood_input),
            Codec.INT.fieldOf("growing_time").orElse(1).forGetter((p_237054_0_) -> p_237054_0_.growing_time),
            Codec.INT.fieldOf("offspring").orElse(1).forGetter((p_237054_0_) -> p_237054_0_.offspring),
            Codec.STRING.fieldOf("breeding_season").orElse("ANY").forGetter((p_237054_0_) -> p_237054_0_.breeding_season),
            Codec.unboundedMap(Codec.STRING, SoundEvent.CODEC).fieldOf("sounds").orElse(Collections.emptyMap()).forGetter((p_237052_0_) -> p_237052_0_.sounds),
            Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("flags").orElse(Collections.emptyMap()).forGetter((p_237054_0_) -> p_237054_0_.flags),
            SpeciesDataHolder.CODEC.listOf().fieldOf("species").orElse(new ArrayList<>()).forGetter((p_237052_0_) -> p_237052_0_.speciesData))
            .apply(p_237051_0_, EntityDataHolder::new));
    private final String name;
    private final float modelScale;
    private final int rarity;
    private final float attack;
    private final float health;
    private final ComplexMobTerrestrial.ActivityType activityType;
    private final String favouriteFood_input;
    private final ItemStack favouriteFood;
    private final int growing_time;
    private final int offspring;
    private final String breeding_season;
    public final Map<String, SoundEvent> sounds;
    private final Map<String, Integer> flags;
    private final List<SpeciesDataHolder> speciesData;

    public EntityDataHolder(String p_i232114_1_, float p_i232114_2_, int p_i232114_3_, float attack, float health, ComplexMobTerrestrial.ActivityType activityType, String favouriteFood, int growing_time, int offspring, String breeding, Map<String, SoundEvent> sounds, Map<String, Integer> flags, List<SpeciesDataHolder> speciesData) {
        this.name = p_i232114_1_;
        this.modelScale = p_i232114_2_;
        this.rarity = p_i232114_3_;

        this.attack = attack;
        this.health = health;
        this.activityType = activityType;

        this.favouriteFood_input = favouriteFood;
        this.favouriteFood = new ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryCreate(this.favouriteFood_input)));
        this.growing_time = growing_time;
        this.offspring = offspring;
        this.breeding_season = breeding;

        // ambient: Ambient sound, hurt: Hurt sound, death: Death sound, threat: Threat sound
        this.sounds = sounds;

        // Additional data I can't be arsed to properly define, due to it being specific for each class
        this.flags = flags;
        this.speciesData = speciesData;
    }

    /*public EntityDataHolder(CompoundNBT nbtData) {
        this.name = nbtData.getString("name");
        this.modelScale = nbtData.getFloat("scale");
        this.rarity = nbtData.getInt("rarity");
        this.attack = nbtData.getFloat("attack");
        this.health = nbtData.getFloat("health");
        this.activityType = ComplexMobTerrestrial.ActivityType.valueOf(nbtData.getString("activityType"));
        this.favouriteFood = nbtData.getString("favourite_food");
        this.growing_time = nbtData.getInt("growing_time");
        this.offspring = nbtData.getInt("offspring");
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
        List<SpeciesDataHolder> species = new ArrayList<>();
        for (String nbt : nbtData.getCompound("species").keySet()) {
            species.add(new SpeciesDataHolder(nbtData.getCompound("species").getCompound(nbt)));
        };
        this.speciesData = species;
    }*/

    public String getString() {
        return this.name + ": Scale: " + this.modelScale + " Rarity: " + this.rarity + " Attack: " + this.attack + " Health: " + this.health +  " Ambient Sound: " + this.sounds;
    }

    public void printSpeciesData() {
        for (SpeciesDataHolder speciesDatum : this.speciesData) {
            UntamedWilds.LOGGER.info(speciesDatum.getString());
        }
    }

    public String getName(int i) {
        if (Objects.equals(this.speciesData.get(i).getName(), "")) {
            return this.name;
        }
        return this.speciesData.get(i).getName();
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

    public ComplexMobTerrestrial.ActivityType getActivityType(int i) {
        if (this.speciesData.get(i).getActivityType() == ComplexMobTerrestrial.ActivityType.INSOMNIAC) {
            return this.activityType;
        }
        return this.speciesData.get(i).getActivityType();
    }

    public ItemStack getFavouriteFood(int i) {
        if (this.speciesData.get(i).getFavouriteFood().equals("")) {
            return this.favouriteFood;
        }
        return this.speciesData.get(i).getFavouriteFood();
    }

    public int getGrowingTime(int i) {
        if (this.speciesData.get(i).getGrowingTime() < 0) {
            return this.growing_time;
        }
        return this.speciesData.get(i).getGrowingTime();
    }

    public int getOffspring(int i) {
        if (this.speciesData.get(i).getOffspring() < 0) {
            return this.offspring;
        }
        return this.speciesData.get(i).getOffspring();
    }

    public Integer getSkins(int i) {
        return this.speciesData.get(i).getSkins();
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
            return this.speciesData.get(i).getSounds().get(sound_id);
        }
        return this.sounds.get(sound_id);
    }

    public Integer getGroupCount(int i) {
        if (!this.speciesData.get(i).getFlags().isEmpty() && this.speciesData.get(i).getFlags().get("groupCount") != null) {
            return this.speciesData.get(i).getFlags().get("groupCount");
        }
        return 1;
    }

    @Nullable
    public int getFlags(int i, String flag) {
        if (!this.speciesData.get(i).getFlags().isEmpty() && this.speciesData.get(i).getFlags().get(flag) != null) {
            return this.speciesData.get(i).getFlags().get(flag);
        }
        if (!this.flags.containsKey(flag)) {
            UntamedWilds.LOGGER.error("Couldn't find " + flag + " flag in ENTITY_DATA");
            return 0;
        }
        return this.flags.get(flag);
    }

    public List<SpeciesDataHolder> getSpeciesData() {
        return this.speciesData;
    }

    /*public CompoundNBT writeEntityDataToNBT() {
        CompoundNBT result = new CompoundNBT();
        result.putString("name", this.name);
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
            if (sound_data.getValue().getRegistryName() != null) {
                sounds.putString(sound_data.getKey(), sound_data.getValue().getRegistryName().toString());
            }
        }
        result.put("sounds", sounds);
        CompoundNBT flags = new CompoundNBT();
        for (Map.Entry<String, Integer> flags_data : this.flags.entrySet()) {
            flags.putInt(flags_data.getKey(), flags_data.getValue());
        }
        result.put("flags", flags);
        CompoundNBT species = new CompoundNBT();
        for (SpeciesDataHolder species_data : this.speciesData) {
            species.put(species_data.getName(), species_data.writeEntityDataToNBT());
        }
        result.put("species", species);
        UntamedWilds.LOGGER.info(result);
        return result;
    }*/
}