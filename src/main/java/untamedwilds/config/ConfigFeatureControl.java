package untamedwilds.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigFeatureControl {
    public static ForgeConfigSpec.BooleanValue addAnemones;
    public static ForgeConfigSpec.BooleanValue addReeds;
    public static ForgeConfigSpec.BooleanValue loadsOfReeds;
    public static ForgeConfigSpec.BooleanValue addTreeOrchids;

    public static void init(ForgeConfigSpec.Builder server, ForgeConfigSpec.Builder client) {
        server.comment("World Gen Control");
        addAnemones = server.comment("Controls whether to add Anemones and their associated items to oceans.").define("gencontrol.anemone", true);
        addReeds = server.comment("Controls whether to add Reeds and their associated items.").define("gencontrol.reeds", true);
        loadsOfReeds = server.comment("Enabling this option will cause a lot more Reeds to spawn, leading to a more natural, albeit less convenient distribution").define("gencontrol.extra_reeds", false);
        addTreeOrchids = server.comment("Controls whether to add Tree Orchids and their associated items. (NYI)").define("gencontrol.tree_orchid", true);
    }
}
