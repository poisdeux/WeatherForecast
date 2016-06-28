package com.example.weatherforecast;

import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by martijn on 27/06/16.
 */
public class YahooWeatherApiResponseTest {

    @Test
    public void jsonParserTest() {
        String jsonResponse = "{\"query\":{\"count\":1,\"created\":\"2016-06-27T18:46:41Z\",\"lang\":\"nl\",\"results\":{\"channel\":{\"units\":{\"distance\":\"mi\",\"pressure\":\"in\",\"speed\":\"mph\",\"temperature\":\"F\"},\"title\":\"Yahoo! Weather - Amsterdam, NH, NL\",\"link\":\"http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-727232/\",\"description\":\"Yahoo! Weather for Amsterdam, NH, NL\",\"language\":\"en-us\",\"lastBuildDate\":\"Mon, 27 Jun 2016 08:46 PM CEST\",\"ttl\":\"60\",\"location\":{\"city\":\"Amsterdam\",\"country\":\"Netherlands\",\"region\":\" NH\"},\"wind\":{\"chill\":\"64\",\"direction\":\"295\",\"speed\":\"11\"},\"atmosphere\":{\"humidity\":\"55\",\"pressure\":\"1018.0\",\"rising\":\"0\",\"visibility\":\"16.1\"},\"astronomy\":{\"sunrise\":\"5:21 am\",\"sunset\":\"10:6 pm\"},\"image\":{\"title\":\"Yahoo! Weather\",\"width\":\"142\",\"height\":\"18\",\"link\":\"http://weather.yahoo.com\",\"url\":\"http://l.yimg.com/a/i/brand/purplelogo//uh/us/news-wea.gif\"},\"item\":{\"title\":\"Conditions for Amsterdam, NH, NL at 07:00 PM CEST\",\"lat\":\"52.373119\",\"long\":\"4.89319\",\"link\":\"http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-727232/\",\"pubDate\":\"Mon, 27 Jun 2016 07:00 PM CEST\",\"condition\":{\"code\":\"34\",\"date\":\"Mon, 27 Jun 2016 07:00 PM CEST\",\"temp\":\"65\",\"text\":\"Mostly Sunny\"},\"forecast\":[{\"code\":\"12\",\"date\":\"27 Jun 2016\",\"day\":\"Mon\",\"high\":\"62\",\"low\":\"58\",\"text\":\"Rain\"},{\"code\":\"39\",\"date\":\"28 Jun 2016\",\"day\":\"Tue\",\"high\":\"66\",\"low\":\"57\",\"text\":\"Scattered Showers\"},{\"code\":\"12\",\"date\":\"29 Jun 2016\",\"day\":\"Wed\",\"high\":\"64\",\"low\":\"56\",\"text\":\"Rain\"},{\"code\":\"39\",\"date\":\"30 Jun 2016\",\"day\":\"Thu\",\"high\":\"65\",\"low\":\"58\",\"text\":\"Scattered Showers\"},{\"code\":\"28\",\"date\":\"01 Jul 2016\",\"day\":\"Fri\",\"high\":\"66\",\"low\":\"58\",\"text\":\"Mostly Cloudy\"},{\"code\":\"28\",\"date\":\"02 Jul 2016\",\"day\":\"Sat\",\"high\":\"65\",\"low\":\"58\",\"text\":\"Mostly Cloudy\"},{\"code\":\"12\",\"date\":\"03 Jul 2016\",\"day\":\"Sun\",\"high\":\"64\",\"low\":\"56\",\"text\":\"Rain\"},{\"code\":\"30\",\"date\":\"04 Jul 2016\",\"day\":\"Mon\",\"high\":\"66\",\"low\":\"58\",\"text\":\"Partly Cloudy\"},{\"code\":\"30\",\"date\":\"05 Jul 2016\",\"day\":\"Tue\",\"high\":\"67\",\"low\":\"58\",\"text\":\"Partly Cloudy\"},{\"code\":\"30\",\"date\":\"06 Jul 2016\",\"day\":\"Wed\",\"high\":\"67\",\"low\":\"57\",\"text\":\"Partly Cloudy\"}],\"description\":\"<![CDATA[<img src=\\\"http://l.yimg.com/a/i/us/we/52/34.gif\\\"/>\\n<BR />\\n<b>Current Conditions:</b>\\n<BR />Mostly Sunny\\n<BR />\\n<BR />\\n<b>Forecast:</b>\\n<BR /> Mon - Rain. High: 62Low: 58\\n<BR /> Tue - Scattered Showers. High: 66Low: 57\\n<BR /> Wed - Rain. High: 64Low: 56\\n<BR /> Thu - Scattered Showers. High: 65Low: 58\\n<BR /> Fri - Mostly Cloudy. High: 66Low: 58\\n<BR />\\n<BR />\\n<a href=\\\"http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-727232/\\\">Full Forecast at Yahoo! Weather</a>\\n<BR />\\n<BR />\\n(provided by <a href=\\\"http://www.weather.com\\\" >The Weather Channel</a>)\\n<BR />\\n]]>\",\"guid\":{\"isPermaLink\":\"false\"}}}}}}";
        HashMap<String, WeatherData> forecastExpected = getExpectedForecast();

        YahooWeatherApiResponse response = new YahooWeatherApiResponse();
        try {
            response.parseJson(jsonResponse);
        } catch (YahooWeatherApiResponse.YahooWeatherApiException e) {
            fail(e.toString());
        }

        for(WeatherData weather : response.getForecast()) {
            WeatherData expectedWeather = forecastExpected.get(weather.date.toString());
            assertTrue("Weather on " + weather.date.toString(), weather.equals(expectedWeather));
        }
    }

    private HashMap<String, WeatherData> getExpectedForecast() {
        HashMap<String, WeatherData> forecastExpected = new HashMap<>();

        WeatherData weatherData = new WeatherData(new LocalDate(2016, 06, 27), 58, 62);
        forecastExpected.put(weatherData.date.toString(), weatherData);
        weatherData = new WeatherData(new LocalDate(2016, 06, 28), 57, 66);
        forecastExpected.put(weatherData.date.toString(), weatherData);
        weatherData = new WeatherData(new LocalDate(2016, 06, 29), 56, 64);
        forecastExpected.put(weatherData.date.toString(), weatherData);
        weatherData = new WeatherData(new LocalDate(2016, 06, 30), 58, 65);
        forecastExpected.put(weatherData.date.toString(), weatherData);
        weatherData = new WeatherData(new LocalDate(2016, 07, 01), 58, 66);
        forecastExpected.put(weatherData.date.toString(), weatherData);
        weatherData = new WeatherData(new LocalDate(2016, 07, 02), 58, 65);
        forecastExpected.put(weatherData.date.toString(), weatherData);
        weatherData = new WeatherData(new LocalDate(2016, 07, 03), 56, 64);
        forecastExpected.put(weatherData.date.toString(), weatherData);
        weatherData = new WeatherData(new LocalDate(2016, 07, 04), 58, 66);
        forecastExpected.put(weatherData.date.toString(), weatherData);
        weatherData = new WeatherData(new LocalDate(2016, 07, 05), 58, 67);
        forecastExpected.put(weatherData.date.toString(), weatherData);
        weatherData = new WeatherData(new LocalDate(2016, 07, 06), 57, 67);
        forecastExpected.put(weatherData.date.toString(), weatherData);

        return forecastExpected;
    }
}