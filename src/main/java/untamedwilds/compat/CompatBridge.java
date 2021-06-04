package untamedwilds.compat;

import net.minecraftforge.fml.ModList;
import org.apache.logging.log4j.Level;
import untamedwilds.UntamedWilds;

public class CompatBridge {

    private static final String SERENSEASONS_MODID = "sereneseasons";
    private static final String BETTER_CAVES_MODID = "bettercaves";

    public static boolean SereneSeasons = false;
    public static boolean betterCaves = false;

    public static void RegisterCompat() {
        if (ModList.get().isLoaded(SERENSEASONS_MODID)) {
            SereneSeasons = true;
            UntamedWilds.LOGGER.log(Level.INFO, "Loading compatibility module with SereneSeasons");
        }
        if (ModList.get().isLoaded(BETTER_CAVES_MODID)) {
            betterCaves = true;
            UntamedWilds.LOGGER.log(Level.INFO, "Loading compatibility module with YUNG's Better Caves");
        }
    }
}
