package untamedwilds.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigModCompat {

    public static ForgeConfigSpec.BooleanValue sereneSeasonsCompat;

    ConfigModCompat(final ForgeConfigSpec.Builder builder) {
        builder.comment("Inter-mod compatibility");

        sereneSeasonsCompat = builder.comment("Controls whether to check for Serene Seasons for compatibility (Mobs will only breed during specific seasons).").define("modcompat.serene_seasons", true);

    }
}
