package untamedwilds.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigMobControl {
    public static ForgeConfigSpec.IntValue critterSpawnRange;
    public static ForgeConfigSpec.BooleanValue masterSpawner;
    public static ForgeConfigSpec.BooleanValue addBear;
    public static ForgeConfigSpec.BooleanValue addBigCat;
    public static ForgeConfigSpec.BooleanValue addHippo;
    public static ForgeConfigSpec.BooleanValue addRhino;
    public static ForgeConfigSpec.BooleanValue addAardvark;
    public static ForgeConfigSpec.BooleanValue addSnake;
    public static ForgeConfigSpec.BooleanValue addSoftshellTurtle;
    public static ForgeConfigSpec.BooleanValue addTortoise;
    public static ForgeConfigSpec.BooleanValue addSunfish;
    public static ForgeConfigSpec.BooleanValue addTrevally;
    public static ForgeConfigSpec.BooleanValue addArowana;
    public static ForgeConfigSpec.BooleanValue addShark;
    public static ForgeConfigSpec.BooleanValue addFootballFish;
    public static ForgeConfigSpec.BooleanValue addTarantula;
    public static ForgeConfigSpec.BooleanValue addGiantClam;
    public static ForgeConfigSpec.BooleanValue addGiantSalamander;

    ConfigMobControl(final ForgeConfigSpec.Builder builder) {
        builder.comment("Pest Control");
        masterSpawner = builder.comment("This toggle can used to fully disable the spawning of all UntamedWilds entities, giving flexibility if somebody chooses to use alternative mob spawning methods.").define("mobcontrol.masterspawner", true);

        addBear = builder.comment("Controls whether to add Bears and their associated items.").define("mobcontrol.bear", true);
        addBigCat = builder.comment("Controls whether to add Big Cats and their associated items.").define("mobcontrol.bigcat", true);
        addHippo = builder.comment("Controls whether to add Hippos and their associated items.").define("mobcontrol.hippo", true);
        addAardvark = builder.comment("Controls whether to add Aardvarks and their associated items.").define("mobcontrol.aardvark", true);
        addRhino = builder.comment("Controls whether to add Rhinoceros and their associated items.").define("mobcontrol.rhino", true);

        addSnake = builder.comment("Controls whether to add Snakes and their associated items.").define("mobcontrol.snake", true);
        addSoftshellTurtle = builder.comment("Controls whether to add Softshell Turtles and their associated items.").define("mobcontrol.softshell_turtle", true);
        addTortoise = builder.comment("Controls whether to add Tortoises and their associated items.").define("mobcontrol.tortoise", true);
        addSunfish = builder.comment("Controls whether to add Sunfish and their associated items.").define("mobcontrol.sunfish", true);
        addTrevally = builder.comment("Controls whether to add Trevally and their associated items.").define("mobcontrol.trevally", true);
        addArowana = builder.comment("Controls whether to add Arowana and their associated items.").define("mobcontrol.arowana", true);
        addShark = builder.comment("Controls whether to add Sharks and their associated items.").define("mobcontrol.shark", true);
        addFootballFish = builder.comment("Controls whether to add Football Fish and their associated items.").define("mobcontrol.football_fish", true);
        addTarantula = builder.comment("Controls whether to add Tarantulas and their associated items.").define("mobcontrol.tarantula", true);
        addGiantClam = builder.comment("Controls whether to add Giant Clams and their associated items.").define("mobcontrol.giant_clam", true);
        addGiantSalamander = builder.comment("Controls whether to add Giant Salamanders and their associated items.").define("mobcontrol.giant_salamander", true);

        critterSpawnRange = builder.comment("Critters further than this value from any Player will despawn into their Burrow (only if they have a Burrow assigned).").defineInRange("mobcontrol.critterSpawnRange", 40, 0, Integer.MAX_VALUE);

    }
}
