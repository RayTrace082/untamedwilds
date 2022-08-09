package untamedwilds.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigModCompat {
    public static ForgeConfigSpec.BooleanValue sereneSeasonsCompat;
    public static ForgeConfigSpec.BooleanValue betterCavesCompat;
    public static ForgeConfigSpec.BooleanValue cavesAndCliffsCompat;
    public static ForgeConfigSpec.BooleanValue patchouliCompat;

    ConfigModCompat(final ForgeConfigSpec.Builder builder) {
        builder.comment("Inter-mod compatibility");

        sereneSeasonsCompat = builder.comment("Controls whether to check for Serene Seasons for compatibility (Mobs will only breed during specific seasons).").define("modcompat.serene_seasons", true);
        betterCavesCompat = builder.comment("Controls whether to check for YUNG's Better Caves (Reduces the rate at which Underground mobs are placed).").define("modcompat.better_caves", true);
        cavesAndCliffsCompat = builder.comment("Controls whether to check for Patchouli compatibility (For the purpose of granting the Root advancement).").define("modcompat.patchouli", true);
    }
}
