package untamedwilds.world;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMobTerrestrial;
import untamedwilds.util.SpawnDataHolder;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class FaunaHandler {

    private final static List<SpawnListEntry> spawnCritter = Lists.newArrayList();
    private final static List<SpawnListEntry> spawnUndergroundLarge = Lists.newArrayList();
    private final static List<SpawnListEntry> spawnLargePrey = Lists.newArrayList();
    // This list is meant to populate rivers and water bodies with fish, and has higher density to compensate for the scarcity of water
    private final static List<SpawnListEntry> spawnDenseWater = Lists.newArrayList();
    private final static List<SpawnListEntry> spawnOcean = Lists.newArrayList();
    private final static List<SpawnListEntry> spawnSessile = Lists.newArrayList();
    private final static List<SpawnListEntry> spawnApexPred = Lists.newArrayList();

    public FaunaHandler() {
    }

    public static List<SpawnListEntry> getSpawnableList(animalType type) {
        return switch (type) {
            case CRITTER -> spawnCritter;
            case BENTHOS -> spawnSessile;
            case DENSE_WATER -> spawnDenseWater;
            case LARGE_OCEAN -> spawnOcean;
            case LARGE_HERB -> spawnLargePrey;
            case APEX_PRED -> spawnApexPred;
            case LARGE_UNDERGROUND -> spawnUndergroundLarge;
        };
    }

    public static List<SpawnListEntry> getSpawnableList(String type) {
        switch (type) {
            case "critter" -> {
                return spawnCritter;
            }
            case "benthos" ->  {
                return spawnSessile;
            }
            case "water_river" ->  {
                return spawnDenseWater;
            }
            case "water_ocean" ->  {
                return spawnOcean;
            }
            case "herbivores" ->  {
                return spawnLargePrey;
            }
            case "predators" ->  {
                return spawnApexPred;
            }
            case "underground" ->  {
                return spawnUndergroundLarge;
            }
        }
        return null;
    }

    public static class SpawnListEntry extends WeightedEntry.IntrusiveBase {
        public static final Codec<FaunaHandler.SpawnListEntry> CODEC = RecordCodecBuilder.create((p_237051_0_) -> p_237051_0_.group(
                        Codec.STRING.fieldOf("type").orElse("").forGetter((p_237056_0_) -> p_237056_0_.entityName),
                        Codec.INT.fieldOf("weight").orElse(0).forGetter((p_237054_0_) -> p_237054_0_.itemWeight),
                        Codec.INT.fieldOf("size_min").orElse(0).forGetter((p_237055_0_) -> p_237055_0_.minGroupCount),
                        Codec.INT.fieldOf("size_max").orElse(0).forGetter((p_237054_0_) -> p_237054_0_.maxGroupCount))
                .apply(p_237051_0_, FaunaHandler.SpawnListEntry::new));

        public String entityName;
        public EntityType<?> entityType;
        public int itemWeight;
        public int minGroupCount;
        public int maxGroupCount;

        public SpawnListEntry(String entityName, Integer weight, Integer minGroupCount, Integer maxGroupCount) {
            super(weight);
            this.entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityName));
            this.itemWeight = weight;
            this.minGroupCount = minGroupCount;
            this.maxGroupCount = maxGroupCount;
        }

        public SpawnListEntry(EntityType<?> entityTypeIn, int weight, int minGroupCount, int maxGroupCount) {
            super(weight);
            this.entityType = entityTypeIn;
            this.itemWeight = weight;
            this.minGroupCount = minGroupCount;
            this.maxGroupCount = maxGroupCount;
        }

        public String toString() {
            return EntityType.getKey(this.entityType) + "*(" + this.minGroupCount + "-" + this.maxGroupCount + "):" + this.itemWeight;
        }

        public Integer getGroupCount() {
            if (this.minGroupCount >= this.maxGroupCount)
                return this.minGroupCount;
            return new Random().nextInt(this.minGroupCount, this.maxGroupCount);
        }
    }

    public enum animalType {
        CRITTER ("critter"),
        BENTHOS("benthos"),
        DENSE_WATER ("water_river"),
        LARGE_OCEAN ("water_ocean"),
        LARGE_HERB ("herbivores"),
        LARGE_UNDERGROUND ("underground"),
        APEX_PRED ("predators");

        public String name;

        animalType(String name) {
            this.name = name;
        }
    }
}
