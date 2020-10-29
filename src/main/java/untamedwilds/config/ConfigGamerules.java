package untamedwilds.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigGamerules {
    public static ForgeConfigSpec.BooleanValue naturalBreeding;
    public static ForgeConfigSpec.BooleanValue hardcoreBreeding;
    public static ForgeConfigSpec.BooleanValue easyBreeding;
    public static ForgeConfigSpec.BooleanValue hardcoreDeath;
    public static ForgeConfigSpec.BooleanValue playerBreeding;
    public static ForgeConfigSpec.BooleanValue randomSpecies;
    public static ForgeConfigSpec.BooleanValue scientificNames;
    public static ForgeConfigSpec.BooleanValue extinctMobs;
    public static ForgeConfigSpec.BooleanValue fantasyMobs;
    public static ForgeConfigSpec.BooleanValue grazerGriefing;
    public static ForgeConfigSpec.IntValue cycleLength;

    public static void init(ForgeConfigSpec.Builder server, ForgeConfigSpec.Builder client)
    {
        server.comment("Options pertaining to global Gamerules");

        naturalBreeding = server.comment("Defines whether animals should breed without Player intervention.").define("gamerules.natural_breeding", true);
        hardcoreBreeding = server.comment("Adds additional restrictions to mob breeding, including Biome/Temperature requirements and Overcrowding.").define("gamerules.hardcore_breeding", false);
        easyBreeding = server.comment("Pregnancy time is only used as a cooldown, babies pop out instantly like in Vanilla.").define("gamerules.easy_breeding", false);
        hardcoreDeath = server.comment("Disable this option to have tamed mobs respawn in their home with half a Heart if they were to 'die' (IMPORTANT: This gamerule is NOT fully functional and using it as a free get-out-of-jail card is bound to be disappointing, use at your own risk).").define("gamerules.hardcore_death", true);
        playerBreeding = server.comment("Defines whether players can trigger breeding by feeding a creature's favourite item, like in vanilla.").define("gamerules.player_breeding", false);
        randomSpecies = server.comment("Allows mobs to spawn as fully random species, ignoring Biomes and Rarity.").define("gamerules.random_species", false);
        scientificNames = server.comment("Features scientific names in various descriptions (eg. for mobs inside Cage Traps).").define("gamerules.scientific_names", true);
        extinctMobs = server.comment("Should extinct mobs spawn naturally in the world?.").define("gamerules.extinct_mobs", true);
        fantasyMobs = server.comment("Should fantasy mobs spawn naturally in the world?.").define("gamerules.fantasy_mobs", true);
        grazerGriefing = server.comment("Should 'Grazing' mobs destroy Tall Grass and/or turn Grass into dirt blocks (like Vanilla Sheep do).").define("gamerules.grazer_griefing", true);

        cycleLength = server.comment("Defines how long a cycle should last, cycles are used to scale the gestation and breeding periods",
                "Example values: 24000 - Day, 168000 - Week, 720000 - Month, 8760000 - Year").defineInRange("gamerules.cycle_length", 24000, 0, 8760000);
    }
}
