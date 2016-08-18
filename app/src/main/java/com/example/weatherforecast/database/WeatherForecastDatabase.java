/*
 * Copyright 2016 Martijn Brekhof. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.weatherforecast.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.weatherforecast.WeatherData;

/**
 * Created by martijn on 28/06/16.
 */
public class WeatherForecastDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "forecast.sqlite";
    private static final int DB_VERSION = 1;

    public WeatherForecastDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + WeatherForecastContract.Forecast.TABLE_NAME + "(" +
                   BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   WeatherForecastContract.Forecast.COLUMN_NAME_DATE_EPOCH_MILLIS + " INTEGER, " +
                   WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_LOW + " INTEGER, " +
                   WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_HIGH + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Deletes all tables from database
     * @param db
     */
    public void onDelete(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + WeatherForecastContract.Forecast.TABLE_NAME);
    }

    /**
     * Deletes all rows from all tables in the database
     */
    public void onClear(SQLiteDatabase db) {
        db.delete(WeatherForecastContract.Forecast.TABLE_NAME, null, null);
    }

    /**
     * Inserts the weather data into the database
     * @param db writable SQLiteDatabase
     * @param weatherData
     * @return Id of inserted row or -1 if an error occurred
     */
    public long insertForecast(SQLiteDatabase db, WeatherData weatherData) {
        ContentValues values = new ContentValues();
        values.put(WeatherForecastContract.Forecast.COLUMN_NAME_DATE_EPOCH_MILLIS, weatherData.date.toDateTimeAtStartOfDay().getMillis());
        values.put(WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_LOW, weatherData.temperatureLow);
        values.put(WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_HIGH, weatherData.temperatureHigh);

        return db.insert(WeatherForecastContract.Forecast.TABLE_NAME,
                         null,
                         values);
    }

    /**
     * Returns the weather forecast after the given date
     * @param db
     * @param epochMillis milliseconds since 01-01-1970
     * @return Cursor holding forecast after epochMillis
     */
    public Cursor getForecast(SQLiteDatabase db, long epochMillis) {
        String sortOrder =
                WeatherForecastContract.Forecast.COLUMN_NAME_DATE_EPOCH_MILLIS + " DESC";

        String selection = WeatherForecastContract.Forecast.COLUMN_NAME_DATE_EPOCH_MILLIS + " > ?";
        String[] selectionArgs = new String[] { String.valueOf(epochMillis) };

        return db.query(WeatherForecastContract.Forecast.TABLE_NAME,
                            WeatherForecastContract.Forecast.ALL_COLUMNS, selection,
                            selectionArgs, null, null, sortOrder);
    }

    /**
     * Returns the epoch from given cursor if it exists
     * @param c
     * @return epoch or -1 if it does not exist
     */
    public static long getEpoch(Cursor c) {
        if(c == null)
            return -1;

        int index = c.getColumnIndex(WeatherForecastContract.Forecast.COLUMN_NAME_DATE_EPOCH_MILLIS);
        if(index == -1)
            return -1;

        return c.getLong(index);
    }

    /**
     * Returns the low temperature from the given cursor if it exists
     * @param c
     * @return
     */
    public static int getTemperatureLow(Cursor c) {
        if(c == null)
            return -1;

        int index = c.getColumnIndex(WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_LOW);
        if(index == -1)
            return -1;

        return c.getInt(index);
    }

    /**
     * Returns the high temperature from the given cursor if it exists
     * @param c
     * @return
     */
    public static int getTemperatureHigh(Cursor c) {
        if(c == null)
            return -1;

        int index = c.getColumnIndex(WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_HIGH);
        if(index == -1)
            return -1;

        return c.getInt(index);
    }
}
