package untamedwilds.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigFeatureControl {
    public static ForgeConfigSpec.BooleanValue addAnemones;
    public static ForgeConfigSpec.BooleanValue addReeds;

    public static void init(ForgeConfigSpec.Builder server, ForgeConfigSpec.Builder client) {
        server.comment("World Gen Control");
        addAnemones = server.comment("Controls whether to add Anemones and their associated items to oceans.").define("gencontrol.anemone", true);
        addReeds = server.comment("Controls whether to add Big Cats and their associated items.").define("gencontrol.reeds", true);
    }
}
