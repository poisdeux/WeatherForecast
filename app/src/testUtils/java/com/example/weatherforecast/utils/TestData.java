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

import com.example.weatherforecast.WeatherData;

import org.joda.time.LocalDate;

import java.util.HashMap;

/**
 * Created by martijn on 28/06/16.
 */
public class TestData {
    private static String jsonResponse = "{\"query\":{\"count\":1,\"created\":\"2016-06-27T18:46:41Z\",\"lang\":\"nl\",\"results\":{\"channel\":{\"units\":{\"distance\":\"mi\",\"pressure\":\"in\",\"speed\":\"mph\",\"temperature\":\"F\"},\"title\":\"Yahoo! Weather - Amsterdam, NH, NL\",\"link\":\"http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-727232/\",\"description\":\"Yahoo! Weather for Amsterdam, NH, NL\",\"language\":\"en-us\",\"lastBuildDate\":\"Mon, 27 Jun 2016 08:46 PM CEST\",\"ttl\":\"60\",\"location\":{\"city\":\"Amsterdam\",\"country\":\"Netherlands\",\"region\":\" NH\"},\"wind\":{\"chill\":\"64\",\"direction\":\"295\",\"speed\":\"11\"},\"atmosphere\":{\"humidity\":\"55\",\"pressure\":\"1018.0\",\"rising\":\"0\",\"visibility\":\"16.1\"},\"astronomy\":{\"sunrise\":\"5:21 am\",\"sunset\":\"10:6 pm\"},\"image\":{\"title\":\"Yahoo! Weather\",\"width\":\"142\",\"height\":\"18\",\"link\":\"http://weather.yahoo.com\",\"url\":\"http://l.yimg.com/a/i/brand/purplelogo//uh/us/news-wea.gif\"},\"item\":{\"title\":\"Conditions for Amsterdam, NH, NL at 07:00 PM CEST\",\"lat\":\"52.373119\",\"long\":\"4.89319\",\"link\":\"http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-727232/\",\"pubDate\":\"Mon, 27 Jun 2016 07:00 PM CEST\",\"condition\":{\"code\":\"34\",\"date\":\"Mon, 27 Jun 2016 07:00 PM CEST\",\"temp\":\"65\",\"text\":\"Mostly Sunny\"},\"forecast\":[{\"code\":\"12\",\"date\":\"27 Jun 2016\",\"day\":\"Mon\",\"high\":\"62\",\"low\":\"58\",\"text\":\"Rain\"},{\"code\":\"39\",\"date\":\"28 Jun 2016\",\"day\":\"Tue\",\"high\":\"66\",\"low\":\"57\",\"text\":\"Scattered Showers\"},{\"code\":\"12\",\"date\":\"29 Jun 2016\",\"day\":\"Wed\",\"high\":\"64\",\"low\":\"56\",\"text\":\"Rain\"},{\"code\":\"39\",\"date\":\"30 Jun 2016\",\"day\":\"Thu\",\"high\":\"65\",\"low\":\"58\",\"text\":\"Scattered Showers\"},{\"code\":\"28\",\"date\":\"01 Jul 2016\",\"day\":\"Fri\",\"high\":\"66\",\"low\":\"58\",\"text\":\"Mostly Cloudy\"},{\"code\":\"28\",\"date\":\"02 Jul 2016\",\"day\":\"Sat\",\"high\":\"65\",\"low\":\"58\",\"text\":\"Mostly Cloudy\"},{\"code\":\"12\",\"date\":\"03 Jul 2016\",\"day\":\"Sun\",\"high\":\"64\",\"low\":\"56\",\"text\":\"Rain\"},{\"code\":\"30\",\"date\":\"04 Jul 2016\",\"day\":\"Mon\",\"high\":\"66\",\"low\":\"58\",\"text\":\"Partly Cloudy\"},{\"code\":\"30\",\"date\":\"05 Jul 2016\",\"day\":\"Tue\",\"high\":\"67\",\"low\":\"58\",\"text\":\"Partly Cloudy\"},{\"code\":\"30\",\"date\":\"06 Jul 2016\",\"day\":\"Wed\",\"high\":\"67\",\"low\":\"57\",\"text\":\"Partly Cloudy\"}],\"description\":\"<![CDATA[<img src=\\\"http://l.yimg.com/a/i/us/we/52/34.gif\\\"/>\\n<BR />\\n<b>Current Conditions:</b>\\n<BR />Mostly Sunny\\n<BR />\\n<BR />\\n<b>Forecast:</b>\\n<BR /> Mon - Rain. High: 62Low: 58\\n<BR /> Tue - Scattered Showers. High: 66Low: 57\\n<BR /> Wed - Rain. High: 64Low: 56\\n<BR /> Thu - Scattered Showers. High: 65Low: 58\\n<BR /> Fri - Mostly Cloudy. High: 66Low: 58\\n<BR />\\n<BR />\\n<a href=\\\"http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-727232/\\\">Full Forecast at Yahoo! Weather</a>\\n<BR />\\n<BR />\\n(provided by <a href=\\\"http://www.weather.com\\\" >The Weather Channel</a>)\\n<BR />\\n]]>\",\"guid\":{\"isPermaLink\":\"false\"}}}}}}";
    private static HashMap<Long, WeatherData> forecastExpected;

    public static String getJsonResponse() {
        return jsonResponse;
    }
    public static LocalDate earliestForecastDate = new LocalDate(2016, 06, 27);

    public static HashMap<Long, WeatherData> getExpectedForecast() {
        if (forecastExpected != null)
            return forecastExpected;

        forecastExpected = new HashMap<>();

        addForecast(new WeatherData(new LocalDate(2016, 06, 27), 58, 62));
        addForecast(new WeatherData(new LocalDate(2016, 06, 28), 57, 66));
        addForecast(new WeatherData(new LocalDate(2016, 06, 29), 56, 64));
        addForecast(new WeatherData(new LocalDate(2016, 06, 30), 58, 65));
        addForecast(new WeatherData(new LocalDate(2016, 07, 01), 58, 66));
        addForecast(new WeatherData(new LocalDate(2016, 07, 02), 58, 65));
        addForecast(new WeatherData(new LocalDate(2016, 07, 03), 56, 64));
        addForecast(new WeatherData(new LocalDate(2016, 07, 04), 58, 66));
        addForecast(new WeatherData(new LocalDate(2016, 07, 05), 58, 67));
        addForecast(new WeatherData(new LocalDate(2016, 07, 06), 57, 67));

        return forecastExpected;
    }

    /**
     * @param epochMilliseconds
     * @return the WeatherData object or null if not found
     */
    public static WeatherData getForecast(long epochMilliseconds) {
        HashMap<Long, WeatherData> forecast = getExpectedForecast();
        return forecast.get(epochMilliseconds);
    }

    private static void addForecast(WeatherData weatherData) {
        long epoch = weatherData.date.toDateTimeAtStartOfDay().getMillis();
        forecastExpected.put(epoch, weatherData);
    }
}
