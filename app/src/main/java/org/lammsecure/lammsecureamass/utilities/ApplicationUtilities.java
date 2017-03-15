package org.lammsecure.lammsecureamass.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Max on 11/3/17.
 *
 * Useful functions
 */

public class ApplicationUtilities {

    public static String convertTimeStampToDate(String timestamp) {
        Calendar calendar = Calendar.getInstance();
        TimeZone timezone = calendar.getTimeZone();
        DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        simpleDateFormat.setTimeZone(timezone);
        Date activationDate = (new Date(Long.parseLong(timestamp) * 1000L));
        return simpleDateFormat.format(activationDate);
    }
}
