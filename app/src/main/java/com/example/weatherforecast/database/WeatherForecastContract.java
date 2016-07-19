package com.example.weatherforecast.database;

import android.provider.BaseColumns;

/**
 * Created by martijn on 28/06/16.
 */
public class WeatherForecastContract {

    private WeatherForecastContract() {}

    public static abstract class Forecast implements BaseColumns {
        public static final String TABLE_NAME = "forecast";
        public static final String COLUMN_NAME_DATE_EPOCH = "epoch";
        public static final String COLUMN_NAME_TEMP_LOW = "temp_low";
        public static final String COLUMN_NAME_TEMP_HIGH = "temp_max";
    }
}