package com.barclayadunn.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: barclayadunn
 * Date: 8/22/14
 * Time: 12:08 PM
 */
public class DateUtil {
    private static final DateFormat shortMysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String getShortMySqlFormattedDate(long date) {
        return getShortMySqlFormattedDate(new Date(date));
    }

    public static String getShortMySqlFormattedDate(Date date) {
        return shortMysqlDateFormat.format(date);
    }

    public static Date parseShortMysqlFormattedDateString(String dateString) throws ParseException {
        return shortMysqlDateFormat.parse(dateString);
    }
}
