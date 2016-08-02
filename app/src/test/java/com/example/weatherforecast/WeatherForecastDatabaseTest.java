package com.example.weatherforecast;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.weatherforecast.database.WeatherForecastContract;
import com.example.weatherforecast.database.WeatherForecastDatabase;
import com.example.weatherforecast.utils.DatabaseUtils;
import com.example.weatherforecast.utils.DateUtils;
import com.example.weatherforecast.utils.TestData;
import com.example.weatherforecast.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

/**
 * Created by martijn on 19/07/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class WeatherForecastDatabaseTest {

    private SQLiteDatabase database;

    @Before
    public void setup() throws URISyntaxException {
        this.database = DatabaseUtils.createDatabase();

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
    public void weatherForecastColumnsTest() {
        WeatherForecastDatabase weatherForecastDatabase =
                new WeatherForecastDatabase(RuntimeEnvironment.application);
        Cursor cursor = weatherForecastDatabase.getForecast(this.database,
                                                            DateUtils.toEpochMillisPreviousDay(TestData.earliestForecastDate));
        assertTrue(cursor.getColumnIndex(WeatherForecastContract.Forecast._ID) != -1);
        assertTrue(cursor.getColumnIndex(WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_HIGH) != -1);
        assertTrue(cursor.getColumnIndex(WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_LOW) != -1);
        assertTrue(cursor.getColumnIndex(WeatherForecastContract.Forecast.COLUMN_NAME_DATE_EPOCH_MILLIS) != -1);
    }

    @Test
    public void insertGetForecastTest() {
        Collection<WeatherData> expectedForecast = TestData.getExpectedForecast().values();
        ArrayList<WeatherData> forecastInserted =
                insertData(expectedForecast.toArray(new WeatherData[0]));

        assertTrue(forecastInserted.size() == expectedForecast.size());

        WeatherForecastDatabase weatherForecastDatabase =
                new WeatherForecastDatabase(RuntimeEnvironment.application);
        Cursor cursor = weatherForecastDatabase.getForecast(this.database,
                                                            DateUtils.toEpochMillisPreviousDay(TestData.earliestForecastDate));

        assertTrue("Retrieved amount of items: " + cursor.getCount() +
                   " does not equal expected amount of items " + forecastInserted.size(),
                   cursor.getCount() == forecastInserted.size());

        assertTrue("Cursor is empty", cursor.moveToFirst());
        TestUtils.forecastCursorTest(cursor);
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
