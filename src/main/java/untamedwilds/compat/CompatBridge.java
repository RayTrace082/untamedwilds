package untamedwilds.compat;

import net.minecraftforge.fml.ModList;
import org.apache.logging.log4j.Level;
import untamedwilds.UntamedWilds;

public class CompatBridge {

    private static final String SS_MOD_ID = "sereneseasons";

    public static boolean SereneSeasons = false;

    public static void RegisterCompat() {
        if (ModList.get().isLoaded(SS_MOD_ID)) {
            SereneSeasons = true;
            UntamedWilds.LOGGER.log(Level.INFO, "Loading compatibility module with SereneSeasons");
        }
    }
}
