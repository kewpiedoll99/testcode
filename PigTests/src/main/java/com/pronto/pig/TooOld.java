package com.pronto.pig;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.pig.FilterFunc;
import org.apache.pig.PigWarning;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.Tuple;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: barclaydunn
 * Date: 6/5/13
 * Time: 1:30 PM
 */
public class TooOld extends FilterFunc {

    public Boolean exec(Tuple input) throws IOException {
        // expect 1 keyvalue
        KeyValue keyValue = (KeyValue) input.get(0);

        // get timestamp from keyvalue
        Long timestamp = keyValue.getTimestamp();

        SimpleTimeZone mdt = new SimpleTimeZone(-7 * 60 * 60 * 1000, TimeZone.getAvailableIDs(-7 * 60 * 60 * 1000)[0]); // mtn time
        Calendar expirationCalendar = new GregorianCalendar(mdt);
        expirationCalendar.setTime(new Date());
        // Unlike set(), add() forces an immediate recomputation of the Calendar's milliseconds and all fields
        expirationCalendar.add(Calendar.YEAR, -1);
        Long expirationTimestamp = expirationCalendar.getTimeInMillis();

        return timestamp < expirationTimestamp;
    }
}

/*
        String monthString = ((DataByteArray) input.get(0)).toString();
        String dayString = ((DataByteArray) input.get(1)).toString();
        String timeOrYear = ((DataByteArray) input.get(2)).toString();
        int year = 2013;
        int day = 1;

        if (timeOrYear.contains(":")) {
            return true;
        } else {
            try {
                year = Integer.parseInt(timeOrYear);
                day = Integer.parseInt(dayString);
            } catch (NumberFormatException nfe) {
                // Give a warning, but do not throw an exception.
                warn("Number parse error!", PigWarning.ACCESSING_NON_EXISTENT_FIELD);
                // Returning null will indicate to Pig that we failed but we want to continue execution
//                return null;
            }
        }

        String DATE_FORMAT = "MMM dd yyyy";
        String comprisedDateString = monthString + " " + day + " " + year;
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        Date comprisedDate;

        try {
            comprisedDate = dateFormatter.parse(comprisedDateString);

            Calendar recordCalendar = new GregorianCalendar(edt);
            //        recordCalendar.setTime(steal.getLiveDate());
            recordCalendar.setTime(comprisedDate);
        } catch (ParseException e) {
            // Give a warning, but do not throw an exception.
            warn("Date parse error (must be something wrong with month) for " + comprisedDateString + "!", PigWarning.ACCESSING_NON_EXISTENT_FIELD);
            return false;
        }
*/
