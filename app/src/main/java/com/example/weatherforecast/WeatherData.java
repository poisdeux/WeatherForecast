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

import org.joda.time.LocalDate;

/**
 * Created by martijn on 27/06/16.
 */
public class WeatherData {
    public LocalDate date;
    public Integer temperatureLow;
    public Integer temperatureHigh;

    public WeatherData(LocalDate date, int temperatureLow, int temperatureHigh) {
        this.date = date;
        this.temperatureHigh = temperatureHigh;
        this.temperatureLow = temperatureLow;
    }

    @Override
    public boolean equals(Object o) {
        if ( ! (o instanceof WeatherData) ) {
            return false;
        }

        WeatherData w = (WeatherData) o;

        return date.equals(w.date)
               && temperatureHigh.equals(w.temperatureHigh)
               && temperatureLow.equals(w.temperatureLow);
    }

    @Override
    public String toString() {
        return date.toString() + " : low=" + temperatureLow + " : high="+temperatureHigh;
    }
}
