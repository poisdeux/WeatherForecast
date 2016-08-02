package com.example.weatherforecast.utils;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by martijn on 02/08/16.
 */
public class DateUtils {
    private static DateTimeFormatter epochToDateTimeFormatter = DateTimeFormat.forPattern("d-M-y");

    public static String toDateString(LocalDate localDate) {
        return toDateString(toEpochMillis(localDate));
    }

    public static String toDateString(long epoch) {
        return epochToDateTimeFormatter.print(epoch);
    }

    public static long toEpochMillis(LocalDate date) {
        return date.toDateTimeAtStartOfDay().getMillis();
    }

    public static long toEpochMillisPreviousDay(LocalDate date) {
        return date.minusDays(1).toDateTimeAtStartOfDay().getMillis();
    }
}
