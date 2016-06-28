package com.example.weatherforecast.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

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
                   WeatherForecastContract.Forecast.COLUMN_NAME_DATE_EPOCH + " INTEGER, " +
                   WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_LOW + " INTEGER, " +
                   WeatherForecastContract.Forecast.COLUMN_NAME_TEMP_HIGH + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
