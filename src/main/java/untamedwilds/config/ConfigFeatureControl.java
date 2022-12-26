package untamedwilds.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class ConfigFeatureControl {

    public static ForgeConfigSpec.BooleanValue addAnemones;
    public static ForgeConfigSpec.BooleanValue addReeds;
    public static ForgeConfigSpec.BooleanValue addFlora;
    public static ForgeConfigSpec.BooleanValue addAlgae;
    public static ForgeConfigSpec.BooleanValue addTreeOrchids;
    public static ForgeConfigSpec.BooleanValue addBurrows;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> reedBlacklist;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> floraBlacklist;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> algaeBlacklist;

    public static ForgeConfigSpec.IntValue freqReeds;
    public static ForgeConfigSpec.IntValue freqFlora;
    public static ForgeConfigSpec.IntValue freqAlgae;

    public static ForgeConfigSpec.IntValue freqCritter;
    public static ForgeConfigSpec.IntValue freqWater;
    public static ForgeConfigSpec.IntValue freqSessile;
    public static ForgeConfigSpec.IntValue freqOcean;
    public static ForgeConfigSpec.IntValue freqApex;
    public static ForgeConfigSpec.IntValue freqHerbivores;
    public static ForgeConfigSpec.IntValue probUnderground;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> dimensionFeatureBlacklist;

    ConfigFeatureControl(final ForgeConfigSpec.Builder builder) {
        //builder.push("feature_control");
        builder.comment("Options pertaining to blocks and their world generation");
        addAnemones = builder.comment("Controls whether to add Anemones and their associated items to oceans.").define("gencontrol.anemone", true);
        addReeds = builder.comment("Controls whether to spawn Reeds in River/Swamp biomes").define("gencontrol.reeds", true);
        addFlora = builder.comment("Controls whether to spawn random Flora in the world").define("gencontrol.bush", true);
        addTreeOrchids = builder.comment("Controls whether to add Tree Orchids (NOT YET IMPLEMENTED)").define("gencontrol.tree_orchid", true);
        addAlgae = builder.comment("Controls whether to spawn Amazon Sword in Swamp/Jungle biomes").define("gencontrol.algae", true);
        addBurrows = builder.comment("Controls whether to use Burrows to spawn Critters, if disabled, Critters will spawn in the world like normal mobs.").define("gencontrol.burrows", true);

        reedBlacklist = builder.comment("Prevent spawns of Reeds in these biomes").defineList("gencontrol.reed_blacklist", Lists.newArrayList(), string -> string instanceof String);
        freqReeds = builder.comment("Frequency of Reeds, 1 in N chunks will generate Reeds (0 to disable)").defineInRange("gencontrol.freqreeds", 4, 0, Integer.MAX_VALUE);
        floraBlacklist = builder.comment("Prevent spawns of Flora in these biomes").defineList("gencontrol.flora_blacklist", Lists.newArrayList(), string -> string instanceof String);
        freqFlora = builder.comment("Frequency of Flora, 1 in N chunks will generate random Flora (0 to disable)").defineInRange("gencontrol.freqflora", 4, 0, Integer.MAX_VALUE);
        algaeBlacklist = builder.comment("Prevent spawns of Algae in these biomes").defineList("gencontrol.algae_blacklist", Arrays.asList("minecraft:frozen_ocean","minecraft:deep_frozen_ocean"), string -> string instanceof String);
        freqAlgae = builder.comment("Frequency of Algae, abstract value (0 to disable)").defineInRange("gencontrol.freqalgae", 1, 0, Integer.MAX_VALUE);

        freqCritter = builder.comment("Frequency of Critters, 1 in N chunks will generate with Critters (0 to disable)").defineInRange("gencontrol.freqcritter", 6, 0, Integer.MAX_VALUE);
        freqSessile = builder.comment("Frequency of Sessile Ocean Mobs, 1 in N chunks will generate with Sessile Mobs (0 to disable)").defineInRange("gencontrol.freqsessile", 8, 0, Integer.MAX_VALUE);
        freqOcean = builder.comment("Frequency of Ocean Mobs, 1 in N chunks will generate with Ocean Mobs (0 to disable)").defineInRange("gencontrol.freqocean", 16, 0, Integer.MAX_VALUE);
        freqApex = builder.comment("Frequency of Apex Predators, 1 in N chunks will generate with an Apex Predator (0 to disable)").defineInRange("gencontrol.freqapex", 64, 0, Integer.MAX_VALUE);
        freqHerbivores = builder.comment("Frequency of Herbivores, 1 in N chunks will generate with an Apex Predator (0 to disable)").defineInRange("gencontrol.freqherbivore", 48, 0, Integer.MAX_VALUE);
        freqWater = builder.comment("Frequency of Freshwater Mobs, 1 in N chunks will generate with Freshwater Mobs (0 to disable)").defineInRange("gencontrol.freqwater", 8, 0, Integer.MAX_VALUE);
        probUnderground = builder.comment("Frequency of Underground mobs, N attempts to spawn a mob will be made on each chunk (0 to disable)").defineInRange("gencontrol.probunderground", 1, 0, Integer.MAX_VALUE);

        dimensionFeatureBlacklist = builder.comment("Prevent flora and other blocks (besides Burrows) from generating in the defined dimensions.").defineList("gencontrol.dimensionFeatureBlacklist", Lists.newArrayList(), string -> string instanceof String);
        //builder.pop();
    }
}
