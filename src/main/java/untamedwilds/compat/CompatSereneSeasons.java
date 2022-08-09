package untamedwilds.compat;

import net.minecraft.world.level.Level;

public class CompatSereneSeasons {
    public static boolean isCurrentSeason(Level world, String string) {
        return true;
    }
        /*ISeasonState data = SeasonHelper.getSeasonState(world);
        String season = data.getSeason().toString();
        if (string.equals("ALL")) {
            return true;
        }
        if (season.equals(string)) {
            return true;
        }
        String subseason = data.getSubSeason().toString();
        if (subseason.equals(string)) {
            return true;
        }
        String tropicalseason = data.getTropicalSeason().toString();
        return tropicalseason.equals(string);
    }

    public static int getDayLength(World world) {
        return SeasonHelper.getSeasonState(world).getDayDuration();
    }

    public static int getDaysInMonth(World world) {
        return SeasonHelper.getSeasonState(world).getSubSeasonDuration() / getDayLength(world);
    }

    public static int getTicksInMonth(World world) {
        return SeasonHelper.getSeasonState(world).getSubSeasonDuration();
    }*/
}