package com.example.weatherforecast;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by martijn on 27/06/16.
 */
public class YahooWeatherApiResponseTest {

    @Test
    public void parseJsonTest() {
        HashMap<String, WeatherData> forecastExpected = TestData.getExpectedForecast();

        ArrayList<WeatherData> forecast = new ArrayList<>();
        YahooWeatherApiResponse response = new YahooWeatherApiResponse();
        try {
            forecast = response.parseJson(TestData.getJsonResponse());
        } catch (YahooWeatherApiException e) {
            fail(e.toString());
        }

        for(WeatherData weather : forecast) {
            WeatherData expectedWeather = forecastExpected.get(weather.date.toString());
            assertTrue("Weather on " + weather.date.toString(), weather.equals(expectedWeather));
        }
    }
}
