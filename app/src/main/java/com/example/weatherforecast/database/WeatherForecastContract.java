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

import android.provider.BaseColumns;

/**
 * Created by martijn on 28/06/16.
 */
public class WeatherForecastContract {

    private WeatherForecastContract() {}

    public static abstract class Forecast implements BaseColumns {
        public static final String TABLE_NAME = "forecast";
        public static final String COLUMN_NAME_DATE_EPOCH_MILLIS = "epoch";
        public static final String COLUMN_NAME_TEMP_LOW = "temp_low";
        public static final String COLUMN_NAME_TEMP_HIGH = "temp_max";

        public static String[] ALL_COLUMNS = {
                _ID,
                COLUMN_NAME_DATE_EPOCH_MILLIS,
                COLUMN_NAME_TEMP_HIGH,
                COLUMN_NAME_TEMP_LOW
        };
    }
}
