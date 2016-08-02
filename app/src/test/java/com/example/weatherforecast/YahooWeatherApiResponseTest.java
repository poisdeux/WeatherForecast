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
