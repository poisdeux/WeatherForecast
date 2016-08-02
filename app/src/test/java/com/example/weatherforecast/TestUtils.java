package com.example.weatherforecast;

import android.database.Cursor;

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
