package untamedwilds.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigGamerules {
    public static ForgeConfigSpec.BooleanValue naturalBreeding;
    public static ForgeConfigSpec.BooleanValue hardcoreBreeding;
    public static ForgeConfigSpec.BooleanValue easyBreeding;
    public static ForgeConfigSpec.BooleanValue genderedBreeding;
    public static ForgeConfigSpec.BooleanValue hardcoreDeath;
    public static ForgeConfigSpec.BooleanValue playerBreeding;
    public static ForgeConfigSpec.BooleanValue randomSpecies;
    public static ForgeConfigSpec.BooleanValue scientificNames;
    public static ForgeConfigSpec.BooleanValue extinctMobs;
    public static ForgeConfigSpec.BooleanValue fantasyMobs;
    public static ForgeConfigSpec.BooleanValue grazerGriefing;
    public static ForgeConfigSpec.BooleanValue mobGriefing;

    public static ForgeConfigSpec.BooleanValue sleepBehaviour;
    public static ForgeConfigSpec.BooleanValue easyMobCapturing;
    public static ForgeConfigSpec.IntValue cycleLength;
    public static ForgeConfigSpec.DoubleValue rareSkinChance;
    public static ForgeConfigSpec.BooleanValue wildRareSkins;

    ConfigGamerules(final ForgeConfigSpec.Builder builder) {
        builder.comment("Options pertaining to global Gamerules");

        naturalBreeding = builder.comment("Defines whether animals should breed without Player intervention.").define("gamerules.natural_breeding", true);
        hardcoreBreeding = builder.comment("Adds additional restrictions to mob breeding, including Biome/Temperature requirements and Overcrowding.").define("gamerules.hardcore_breeding", false);
        easyBreeding = builder.comment("Pregnancy time is only used as a cooldown, babies pop out instantly like in Vanilla.").define("gamerules.easy_breeding", false);
        genderedBreeding = builder.comment("Whether breeding requires a Male and a Female to produce offspring/eggs. (Warning: may lead to uncontrolled spawns of eggs)").define("gamerules.gendered_breeding", true);
        hardcoreDeath = builder.comment("Disable this option to have tamed mobs respawn in their home with half a Heart if they were to 'die' (IMPORTANT: This gamerule is NOT fully functional and using it as a free get-out-of-jail card is bound to be disappointing, use at your own risk).").define("gamerules.hardcore_death", true);
        playerBreeding = builder.comment("Defines whether players can trigger breeding by feeding a creature's favourite item, like in vanilla.").define("gamerules.player_breeding", false);
        randomSpecies = builder.comment("Allows mobs to spawn as fully random species, ignoring Biomes and Rarity.").define("gamerules.random_species", false);
        scientificNames = builder.comment("Features scientific names in various descriptions (eg. for mobs inside Cage Traps).").define("gamerules.scientific_names", true);
        extinctMobs = builder.comment("Should extinct mobs spawn naturally in the world?.").define("gamerules.extinct_mobs", true);
        fantasyMobs = builder.comment("Should fantasy mobs spawn naturally in the world?.").define("gamerules.fantasy_mobs", true);
        grazerGriefing = builder.comment("Should 'Grazing' mobs destroy Tall Grass and/or turn Grass into dirt blocks (like Vanilla Sheep do).").define("gamerules.grazer_griefing", true);
        mobGriefing = builder.comment("Should mobs potentially destroy the terrain? Keep in mind 'mobGriefing' is still required").define("gamerules.mob_griefing", false);

        sleepBehaviour = builder.comment("Should the 'Sleeping' behaviour run? Disabling this option also disables the activity").define("gamerules.mob_sleeping", true);
        easyMobCapturing = builder.comment("If set to false, makes mobs a lot harder to catch by preventing the capture of hostile mobs").define("gamerules.easy_mob_capture", true);

        cycleLength = builder.comment("Defines how long a cycle should last, cycles are used to scale the gestation and breeding periods",
                "Example values: 24000 - Day, 168000 - Week, 720000 - Month, 8760000 - Year").defineInRange("gamerules.cycle_length", 24000, 0, 8760000);
        rareSkinChance = builder.comment("Chance for a mob, out of 1, to have it's Skin replaced by a Rare skin (if any are defined through assets)").defineInRange("gamerules.rare_skin_chance", 0.05, 0, 1);
        wildRareSkins = builder.comment("Should mobs with Rare skins generate in the wild (if defined through assets)").define("gamerules.wild_rare_skins", true);

    }
}
