package org.lammsecure.lammsecureamass.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Max on 11/3/17.
 *
 * Useful functions
 */

public class ApplicationUtilities {

    public static String convertTimeStampToDate(String timestamp) {
        DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date activationDate = (new Date(Long.parseLong(timestamp) * 1000L));
        return simpleDateFormat.format(activationDate);
    }
}
