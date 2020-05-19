package com.barclayadunn.date;

import java.text.ParseException;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: barclaydunn
 * Date: Jun 25, 2010
 * Time: 12:55:18 PM
 */
public class DateStuff {
    public static void main(String[] args) {

        // this stuff was an attempt to rewrite StealModule.setExpirationOnSteals() - saving here so as not to lose it
        Date expirationDate;
        // create a GregorianCalendar with the Eastern Daylight time zone
        // with the steal date and time = noon
        SimpleTimeZone edt = new SimpleTimeZone(-5 * 60 * 60 * 1000, TimeZone.getAvailableIDs(-5 * 60 * 60 * 1000)[0]);
        Calendar calendar = new GregorianCalendar(edt);
//        calendar.setTime(steal.getLiveDate());
        calendar.set(Calendar.HOUR, 12);         // Set the time - hour
        calendar.set(Calendar.MINUTE, 0);        // Set the time - minute
        // A calendar's milliseconds is not recomputed after set() until the next call to get() - http://java.sun.com/j2se/1.4.2/docs/api/java/util/Calendar.html
        calendar.get(Calendar.DAY_OF_MONTH);

        // Unlike set(), add() forces an immediate recomputation of the calendar's milliseconds and all fields
        calendar.add(Calendar.HOUR, 24);

        expirationDate = calendar.getTime();
//        steal.setExpirationDate(expirationDate);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
//        log.debug("setExpirationOnSteals(): Expiration date: " + df.format(expirationDate));

        String DATE_FORMAT = "MM/dd/yyyy";
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        dateFormatter.setLenient(false);
        Date newDate = null;
        try {
            newDate = dateFormatter.parse("01/12/2013");
        } catch (ParseException e) {
            e.printStackTrace();

        }
        System.out.println(newDate);
    }

    /**
     * Also attempted to rewrite these two methods in StealDO
     */
    public boolean isExpired() {
        Calendar calendar = getCalendar(new Date());

//        return this.expiredBefore(calendar.getTime());
        return true;
    }
    /**
     * create a GregorianCalendar with the Eastern Daylight time zone
     * and the supplied date\
     * @param date
     * @return
     */
    private Calendar getCalendar(Date date) {
        SimpleTimeZone edt = new SimpleTimeZone(-5 * 60 * 60 * 1000, TimeZone.getAvailableIDs(-5 * 60 * 60 * 1000)[0]);
        Calendar calendar = new GregorianCalendar(edt);
        calendar.setTime(date);
        return calendar;
    }
    
}
