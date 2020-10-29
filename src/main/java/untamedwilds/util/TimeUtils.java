package untamedwilds.util;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

// A set of functions relating Time conversion between Ticks, Days and Months
// Handles compatibility with SereneSeasons, using their functions if the Mod and the option are enabled
public abstract class TimeUtils {

    public static String convertTicksToDays(World world, int ticks) {
        /*if (CompatBridge.SereneSeasons) {
            int d = ticks/CompatSereneSeasons.getDayLength(world);
            if (d > CompatSereneSeasons.getDaysInMonth(world)) {
                int m = d/CompatSereneSeasons.getDaysInMonth(world);
                d -= m * CompatSereneSeasons.getDaysInMonth(world);
                return m + " months and " + d + " days";
            }
            return d + " days";
        }*/
        int d = ticks/24000;
        if (d > 7) {
            int m = d/7;
            d -= m * 7;
            return new TranslationTextComponent("untamedwilds.timeutils.weeks", m, d).getString();
        }
        return new TranslationTextComponent("untamedwilds.timeutils.days", d).getString();
    }

    public static int getTicksInMonth(World world) {
        /*if (CompatBridge.SereneSeasons) {
            return CompatSereneSeasons.getTicksInMonth(world);
        }*/
        return 168000;
    }
}
