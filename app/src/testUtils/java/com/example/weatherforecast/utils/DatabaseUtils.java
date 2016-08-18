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

package com.example.weatherforecast.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.weatherforecast.WeatherData;
import com.example.weatherforecast.WeatherForecastActivity;
import com.example.weatherforecast.database.WeatherForecastDatabase;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collection;

/**
 * Created by martijn on 01/08/16.
 */
public class DatabaseUtils {

    /**
     * Creates a new database for testing purposes
     * @return
     * @throws URISyntaxException
     */
    public static SQLiteDatabase createDatabase() throws URISyntaxException {
        String filePath = WeatherForecastActivity.class.getResource("/test.sqlite3").toURI().getPath();

        SQLiteDatabase database = SQLiteDatabase.openDatabase(
                (new File(filePath)).getAbsolutePath(),
                null,
                SQLiteDatabase.OPEN_READWRITE);

        return database;
    }

    /**
     * fills the database with the given weatherdata elements
     * @param context
     * @param database database to use, if null a writable database is retrieved using {@link WeatherForecastDatabase#getWritableDatabase()}
     * @param weatherData
     */
    public static void fillDatabase(Context context,
                                                       SQLiteDatabase database,
                                                       Collection<WeatherData> weatherData) {
        WeatherForecastDatabase weatherForecastDatabase = new WeatherForecastDatabase(context);

        if( database == null )
            database = weatherForecastDatabase.getWritableDatabase();

        for(WeatherData item : weatherData) {
            weatherForecastDatabase.insertForecast(database, item);
        }
    }
}
