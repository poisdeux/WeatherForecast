package com.example.weatherforecast.utils;

import android.database.sqlite.SQLiteDatabase;

import com.example.weatherforecast.TestData;
import com.example.weatherforecast.WeatherData;
import com.example.weatherforecast.WeatherForecastActivity;
import com.example.weatherforecast.database.WeatherForecastDatabase;

import org.robolectric.RuntimeEnvironment;

import java.io.File;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

/**
 * Created by martijn on 01/08/16.
 */
public class DatabaseUtils {

    /**
     * Creates a new database for testing purposes
     * @return
     * @throws URISyntaxException
     */
    public static SQLiteDatabase createDatabase() throws URISyntaxException {
        String filePath = WeatherForecastActivity.class.getResource("/test.sqlite3").toURI().getPath();

        SQLiteDatabase database = SQLiteDatabase.openDatabase(
                (new File(filePath)).getAbsolutePath(),
                null,
                SQLiteDatabase.OPEN_READWRITE);

        return database;
    }

    public static WeatherForecastDatabase fillDatabase(SQLiteDatabase database) {
        WeatherForecastDatabase weatherForecastDatabase = new WeatherForecastDatabase(RuntimeEnvironment.application);
        weatherForecastDatabase.onCreate(database);

        for(WeatherData weatherData : TestData.getExpectedForecast().values()) {
            weatherForecastDatabase.insertForecast(database, weatherData);
        }

        return weatherForecastDatabase;
    }
}
