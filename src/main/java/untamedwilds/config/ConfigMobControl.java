package untamedwilds.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ConfigMobControl {

    public static ForgeConfigSpec.IntValue critterSpawnRange;
    public static ForgeConfigSpec.IntValue burrowRepopulationChance;
    public static ForgeConfigSpec.IntValue treeSpawnBias;
    public static ForgeConfigSpec.BooleanValue masterSpawner;
    public static ForgeConfigSpec.BooleanValue tickingNests;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> dimensionBlacklist;

    ConfigMobControl(final ForgeConfigSpec.Builder builder) {
        builder.comment("Pest Control");
        masterSpawner = builder.comment("This toggle can used to fully disable the spawning of all UntamedWilds entities, giving flexibility if somebody chooses to use alternative mob spawning methods.").define("mobcontrol.masterspawner", true);

        critterSpawnRange = builder.comment("Critters further than this value from any Player will despawn into their Burrow (only if they have a Burrow assigned).").defineInRange("mobcontrol.critterSpawnRange", 40, 0, Integer.MAX_VALUE);
        burrowRepopulationChance = builder.comment("Chance, defined as once every N tries, where N is this value, for a new mob to be generated when a burrow spawns a new mob.").defineInRange("mobcontrol.burrowRepopulationChance", 10, 0, 100);
        tickingNests = builder.comment("Defines whether baby mobs occasionally spawn from nests without player intervention.").define("mobcontrol.tickingNests", true);

        treeSpawnBias = builder.comment("If a mob were to be unable to spawn on the ground (due to terrain collisions), check again N blocks above the floor, where N is this value. Set to 0 to disable, higher values will mean mobs spawning on top of taller trees").defineInRange("mobcontrol.treeSpawnBias", 4, 0, 256);
        dimensionBlacklist = builder.comment("Prevent mobs and burrows from spawning in the defined dimensions.").defineList("mobcontrol.dimensionBlacklist", Lists.newArrayList(), string -> string instanceof String);

    }
}
