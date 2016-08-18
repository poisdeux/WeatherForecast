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

import android.database.Cursor;

import com.example.weatherforecast.WeatherData;
import com.example.weatherforecast.database.WeatherForecastContract;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by martijn on 28/07/16.
 */
public class TestUtils {
    public static void forecastCursorTest(Cursor cursor) {
        int columnIndexEpoch = cursor.getColumnIndex(WeatherForecastContract.Forecast.COLUMN_NAME_DATE_EPOCH_MILLIS);
        int columnIndexTempLow = cursor.getColumnIndex(WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_LOW);
        int columnIndexTempHigh = cursor.getColumnIndex(WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_HIGH);
        do {
            long epoch = cursor.getLong(columnIndexEpoch);
            int tempLow = cursor.getInt(columnIndexTempLow);
            int tempHigh = cursor.getInt(columnIndexTempHigh);

            WeatherData weatherData = TestData.getForecast(epoch);
            assertNotNull(weatherData);
            assertTrue(weatherData.temperatureLow == tempLow);
            assertTrue(weatherData.temperatureHigh == tempHigh);
        } while(cursor.moveToNext());

    }
}
