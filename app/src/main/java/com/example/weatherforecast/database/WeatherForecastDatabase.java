package com.example.weatherforecast.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.weatherforecast.WeatherData;

/**
 * Created by martijn on 28/06/16.
 */
public class WeatherForecastDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "forecast.sqlite";
    private static final int DB_VERSION = 1;

    public WeatherForecastDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + WeatherForecastContract.Forecast.TABLE_NAME + "(" +
                   BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   WeatherForecastContract.Forecast.COLUMN_NAME_DATE_EPOCH_MILLIS + " INTEGER, " +
                   WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_LOW + " INTEGER, " +
                   WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_HIGH + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void onDelete(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + WeatherForecastContract.Forecast.TABLE_NAME);
    }

    /**
     * Inserts the weather data into the database
     * @param db writable SQLiteDatabase
     * @param weatherData
     * @return Id of inserted row or -1 if an error occurred
     */
    public long insertForecast(SQLiteDatabase db, WeatherData weatherData) {
        ContentValues values = new ContentValues();
        values.put(WeatherForecastContract.Forecast.COLUMN_NAME_DATE_EPOCH_MILLIS, weatherData.date.toDateTimeAtStartOfDay().getMillis());
        values.put(WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_LOW, weatherData.temperatureLow);
        values.put(WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_HIGH, weatherData.temperatureHigh);

        return db.insert(WeatherForecastContract.Forecast.TABLE_NAME,
                  null,
                  values);
    }

    public Cursor getForecast(SQLiteDatabase db) {

        String[] projection = {
                WeatherForecastContract.Forecast.COLUMN_NAME_DATE_EPOCH_MILLIS,
                WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_HIGH,
                WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_LOW,
        };

        String sortOrder =
                WeatherForecastContract.Forecast.COLUMN_NAME_DATE_EPOCH_MILLIS + " DESC";

        String selection = WeatherForecastContract.Forecast.COLUMN_NAME_DATE_EPOCH_MILLIS + " > ?";
        String[] selectionArgs = new String[] { WeatherForecastContract.Forecast.COLUMN_NAME_DATE_EPOCH_MILLIS};

//        Cursor c = db.query(WeatherForecastContract.Forecast.TABLE_NAME, projection, selection,
//                        selectionArgs, null, null, sortOrder);
        Cursor c = db.query(WeatherForecastContract.Forecast.TABLE_NAME, projection, null,
                            null, null, null, null);
        return c;
    }
}
