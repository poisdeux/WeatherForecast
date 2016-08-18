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

import com.example.weatherforecast.service.YahooWeatherApiException;
import com.example.weatherforecast.service.YahooWeatherApiResponse;
import com.example.weatherforecast.utils.TestData;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by martijn on 27/06/16.
 */
public class YahooWeatherApiResponseTest {

    @Test
    public void parseJsonTest() {
        ArrayList<WeatherData> forecast = new ArrayList<>();
        YahooWeatherApiResponse response = new YahooWeatherApiResponse();
        try {
            forecast = response.parseJson(TestData.getJsonResponse());
        } catch (YahooWeatherApiException e) {
            fail(e.toString());
        }

        for(WeatherData weather : forecast) {
            WeatherData expectedWeather = TestData.getForecast(weather.date.toDateTimeAtStartOfDay().getMillis());
            assertTrue("Weather on " + weather.date.toString(), weather.equals(expectedWeather));
        }
    }
}
