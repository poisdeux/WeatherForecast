package com.example.weatherforecast;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.weatherforecast.database.WeatherForecastContract;
import com.example.weatherforecast.database.WeatherForecastDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by martijn on 19/07/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class WeatherForecastDatabaseTest {

    private SQLiteDatabase database;

    @Before
    public void setup() throws URISyntaxException {
        String filePath = getClass().getResource("/test.sqlite3").toURI().getPath();

        this.database = SQLiteDatabase.openDatabase(
                (new File(filePath)).getAbsolutePath(),
                null,
                SQLiteDatabase.OPEN_READWRITE);

        WeatherForecastDatabase weatherForecastDatabase =
                new WeatherForecastDatabase(RuntimeEnvironment.application);

        weatherForecastDatabase.onCreate(this.database);
    }

    @After
    public void cleanup() {
        WeatherForecastDatabase weatherForecastDatabase =
                new WeatherForecastDatabase(RuntimeEnvironment.application);

        weatherForecastDatabase.onDelete(this.database);
    }

    @Test
    public void insertGetForecastTest() {
        ArrayList<WeatherData> forecastInserted =
                insertData(TestData.getExpectedForecast().values().toArray(new WeatherData[0]));


        WeatherForecastDatabase weatherForecastDatabase =
                new WeatherForecastDatabase(RuntimeEnvironment.application);
        Cursor cursor = weatherForecastDatabase.getForecast(this.database);


        assertTrue("Cursor is empty", cursor.moveToFirst());

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

            assertTrue("Forecast: " + weatherData + " is in database but was not inserted.",
                       forecastInserted.remove(weatherData));

        } while(cursor.moveToNext());

        for(WeatherData weatherData : forecastInserted) {
            fail("Expected forecast: " + weatherData + " is not in the database");
        }
    }

    /**
     * Inserts all items in weatherData into the database
     * Throws {@link AssertionError} if insertion fails
     * @return items inserted
     */
    private ArrayList<WeatherData> insertData(WeatherData[] weatherData) {
        ArrayList<WeatherData> forecastInserted = new ArrayList<>();
        WeatherForecastDatabase weatherForecastDatabase =
                new WeatherForecastDatabase(RuntimeEnvironment.application);

        for(WeatherData weatherDataItem : weatherData) {
            assertTrue("Failed to insert " + weatherData,
                       weatherForecastDatabase.insertForecast(this.database, weatherDataItem) != -1);
            forecastInserted.add(weatherDataItem);
        }
        return forecastInserted;
    }
}
