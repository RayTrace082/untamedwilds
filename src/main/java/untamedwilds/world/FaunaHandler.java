package untamedwilds.world;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityType;
import net.minecraft.util.WeightedRandom;

import java.util.List;

public abstract class FaunaHandler {

    private final static List<SpawnListEntry> spawnCritter = Lists.newArrayList();
    private final static List<SpawnListEntry> spawnUndergroundLarge = Lists.newArrayList();
    private final static List<SpawnListEntry> spawnLargePrey = Lists.newArrayList();
    // This list is meant to populate rivers and water bodies with fish, and has higher density to compensate for the scarcity of water
    private final static List<SpawnListEntry> spawnDenseWater = Lists.newArrayList();
    private final static List<SpawnListEntry> spawnOcean = Lists.newArrayList();
    private final static List<SpawnListEntry> spawnSessile = Lists.newArrayList();
    private final static List<SpawnListEntry> spawnSmallPred = Lists.newArrayList();
    private final static List<SpawnListEntry> spawnApexPred = Lists.newArrayList();

    public FaunaHandler() {
        spawnLargePrey.add(new FaunaHandler.SpawnListEntry(EntityType.SHEEP, 12, 4));
    }

    public static List<SpawnListEntry> getSpawnableList(animalType type) {
        switch (type) {
            default:
            case CRITTER:
                return spawnCritter;
            case SESSILE:
                return spawnSessile;
            case DENSE_WATER:
                return spawnDenseWater;
            case LARGE_OCEAN:
                return spawnOcean;
            case LARGE_HERB:
                return spawnLargePrey;
            case SMALL_PRED:
                return spawnSmallPred;
            case APEX_PRED:
                return spawnApexPred;
            case LARGE_UNDERGROUND:
                return spawnUndergroundLarge;
        }
    }

    public static class SpawnListEntry extends WeightedRandom.Item {
        public EntityType<?> entityType;
        public int itemWeight;
        public int groupCount;

        public SpawnListEntry(EntityType<?> entityTypeIn, int weight, int groupCount) {
            super(weight);
            this.entityType = entityTypeIn;
            this.itemWeight = weight;
            this.groupCount = groupCount;
        }

        public String toString() {
            return EntityType.getKey(this.entityType) + "*(1-" + this.groupCount + "):" + this.itemWeight;
        }
    }

    public enum animalType {
        CRITTER,
        SESSILE,
        DENSE_WATER,
        LARGE_OCEAN,
        LARGE_HERB,
        SMALL_PRED,
        LARGE_UNDERGROUND,
        APEX_PRED
    }
}
