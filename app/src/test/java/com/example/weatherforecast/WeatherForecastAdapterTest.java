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

package com.example.weatherforecast;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.example.weatherforecast.database.WeatherForecastDatabase;
import com.example.weatherforecast.utils.DatabaseUtils;
import com.example.weatherforecast.utils.DateUtils;
import com.example.weatherforecast.utils.TestData;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by martijn on 01/08/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class WeatherForecastAdapterTest {
    private WeatherForecastAdapter weatherForecastAdapter;
    private SQLiteDatabase database;

    @Before
    public void setup() throws Exception {
        database = DatabaseUtils.createDatabase();

        WeatherForecastDatabase weatherForecastDatabase =
                new WeatherForecastDatabase(RuntimeEnvironment.application);

        weatherForecastDatabase.onCreate(database);

        DatabaseUtils.fillDatabase(RuntimeEnvironment.application, database,
                                           TestData.getExpectedForecast().values());

        Cursor c =
                weatherForecastDatabase.getForecast(database,
                                                    DateUtils.toEpochMillisPreviousDay(TestData.earliestForecastDate));

        weatherForecastAdapter =
                new WeatherForecastAdapter(RuntimeEnvironment.application, c);
    }

    @After
    public void cleanup() {
        WeatherForecastDatabase weatherForecastDatabase =
                new WeatherForecastDatabase(RuntimeEnvironment.application);

        weatherForecastDatabase.onDelete(database);
    }

    @Test
    public void newViewBindViewTest() {
        Cursor c = weatherForecastAdapter.getCursor();
        View view = weatherForecastAdapter.newView(RuntimeEnvironment.application, c, null);
        assertNotNull(view);
        assertTrue(c.moveToFirst());
        weatherForecastAdapter.bindView(view, RuntimeEnvironment.application, c);
        testView(view, c);
    }


    private void testView(View view, Cursor c) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("d-M-y");

        WeatherForecastAdapter.ViewHolder viewHolder = (WeatherForecastAdapter.ViewHolder) view.getTag();

        assertNotNull(viewHolder.date);
        assertTrue(viewHolder.date.getText().toString() + " != " +
                   dateTimeFormatter.print(WeatherForecastDatabase.getEpoch(c)),
                   viewHolder.date.getText().toString().contentEquals(
                           dateTimeFormatter.print(WeatherForecastDatabase.getEpoch(c))));

        assertNotNull(viewHolder.temperatureLow);
        assertTrue(Integer.parseInt(viewHolder.temperatureLow.getText().toString())  + " != " +
                   WeatherForecastDatabase.getTemperatureLow(c),
                   Integer.parseInt(viewHolder.temperatureLow.getText().toString()) ==
                   WeatherForecastDatabase.getTemperatureLow(c));

        assertNotNull(viewHolder.temperatureHigh);
        assertTrue(Integer.parseInt(viewHolder.temperatureHigh.getText().toString()) + " == " +
                   WeatherForecastDatabase.getTemperatureHigh(c),
                   Integer.parseInt(viewHolder.temperatureHigh.getText().toString()) ==
                   WeatherForecastDatabase.getTemperatureHigh(c));
    }
}
