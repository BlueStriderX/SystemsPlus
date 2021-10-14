package thederpgamer.systemsplus.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Contains misc date and time utilities.
 *
 * @author TheDerpGamer
 * @version 1.0 - [10/12/2021]
 */
public class DateUtils {

    public static float getAgeDays(Date date) {
        Date current = new Date(System.currentTimeMillis());
        long difference = Math.abs(current.getTime() - date.getTime());
        return ((float) (difference / (1000 * 60 * 60 * 24)));
    }

    public static float getAgeDays(long time) {
        return getAgeDays(new Date(time));
    }

    public static String getTimeFormatted(String format) {
        return (new SimpleDateFormat(format)).format(new Date());
    }

    public static String getTimeFormatted() {
        return getTimeFormatted("MM/dd/yyyy '-' hh:mm:ss z");
    }
}
