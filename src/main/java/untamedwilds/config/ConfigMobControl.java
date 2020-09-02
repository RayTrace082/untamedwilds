package untamedwilds.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigMobControl {
    public static ForgeConfigSpec.BooleanValue masterSpawner;
    public static ForgeConfigSpec.BooleanValue addBear;
    public static ForgeConfigSpec.BooleanValue addBigCat;
    public static ForgeConfigSpec.BooleanValue addHippo;
    public static ForgeConfigSpec.BooleanValue addSnake;
    public static ForgeConfigSpec.BooleanValue addSoftshellTurtle;
    public static ForgeConfigSpec.BooleanValue addSunfish;
    public static ForgeConfigSpec.BooleanValue addTarantula;
    public static ForgeConfigSpec.BooleanValue addGiantClam;

    public static void init(ForgeConfigSpec.Builder server, ForgeConfigSpec.Builder client)
    {
        server.comment("Pest Control");
        masterSpawner = server.comment("This toggle can used to fully disable the spawning of all UntamedWilds entities, giving flexibility if somebody chooses to use alternative mob spawning methods. [default: false]").define("mobcontrol.masterspawner", true);

        addBear = server.comment("Controls whether to add Bears and their associated items.").define("mobcontrol.bear", true);
        addBigCat = server.comment("Controls whether to add Big Cats and their associated items.").define("mobcontrol.bigcat", true);
        addHippo = server.comment("Controls whether to add Hippos and their associated items.").define("mobcontrol.hippo", true);

        addSnake = server.comment("Controls whether to add Snakes and their associated items.").define("mobcontrol.snake", true);
        addSoftshellTurtle = server.comment("Controls whether to add Softshell Turtles and their associated items.").define("mobcontrol.softshell_turtle", true);
        addSunfish = server.comment("Controls whether to add Sunfish and their associated items.").define("mobcontrol.sunfish", true);
        addTarantula = server.comment("Controls whether to add Tarantulas and their associated items.").define("mobcontrol.tarantula", true);
        addGiantClam = server.comment("Controls whether to add Giant Clams and their associated items.").define("mobcontrol.giant_clam", true);
    }
}
