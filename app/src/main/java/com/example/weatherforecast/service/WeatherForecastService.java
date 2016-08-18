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

package com.example.weatherforecast.service;

import android.app.Service;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import com.example.weatherforecast.WeatherData;
import com.example.weatherforecast.database.WeatherForecastDatabase;
import com.example.weatherforecast.utils.EventBusUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by martijn on 05/08/16.
 */
public class WeatherForecastService extends Service {
    public static String BUNDLE_KEY_FORECAST_URL = "forecastUrl";

    private HandlerThread handlerThread;
    private Handler callbackHandler;
    private IBinder iBinder = new LocalBinder();
    private boolean isSyncing;

    private HttpConnection httpConnection;

    public class LocalBinder extends Binder {
        public WeatherForecastService getService() {
            return WeatherForecastService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        httpConnection = new HttpConnection();

        handlerThread = new HandlerThread("WeatherForecastService", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();

        callbackHandler = new Handler(handlerThread.getLooper());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(isSyncing)
            return START_NOT_STICKY;

        Bundle extras = intent.getExtras();
        if ( extras == null ) {
            Log.e("onStartCommand", "Intent does not contain a map of extended data (getExtras())");
            return START_NOT_STICKY;
        }

        String url = extras.getString(BUNDLE_KEY_FORECAST_URL);
        if( url == null ) {
            Log.e("onStartCommand", "Key "+BUNDLE_KEY_FORECAST_URL+" not found in extended data from intent");
            return START_NOT_STICKY;
        }

        try {
            URL forecastUrl = new URL(url);
            httpConnection.execute(forecastUrl, httpConnectionCallback, callbackHandler);
            isSyncing = true;
        } catch (MalformedURLException e) {
            EventBusUtils.sendHttpErrorEvent(e.getMessage());
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        isSyncing = false;
    }

    public boolean isSyncing() {
        return isSyncing;
    }

    private HttpConnection.HttpConnectionCallback httpConnectionCallback = new HttpConnection.HttpConnectionCallback() {
        @Override
        public void onSuccess(String response) {
            ArrayList<WeatherData> forecast = new ArrayList<>();
            YahooWeatherApiResponse yahooWeatherApiResponse = new YahooWeatherApiResponse();
            try {
                forecast = yahooWeatherApiResponse.parseJson(response);
            } catch (YahooWeatherApiException e) {
                EventBusUtils.sendSqlErrorEvent(e.getMessage());
            }

            WeatherForecastDatabase weatherForecastDatabase =
                    new WeatherForecastDatabase(getApplicationContext());
            SQLiteDatabase db = weatherForecastDatabase.getWritableDatabase();

            try {
                //Clear previous forecast
                weatherForecastDatabase.onClear(db);

                for (WeatherData weather : forecast) {
                    if ( weatherForecastDatabase.insertForecast(db, weather) == -1 ) {
                        throw new SQLException("Failed to save forecast to database");
                    }
                }
            } catch ( SQLException e ) {
                EventBusUtils.sendSqlErrorEvent(e.getMessage());
            }
            db.close();

            isSyncing = false;
            EventBusUtils.sendSyncCompleteEvent();

            stopSelf();
        }

        @Override
        public void onError(HttpConnection.ErrorCode errorCode, String message) {
            isSyncing = false;
            EventBusUtils.sendHttpErrorEvent(message);

            stopSelf();
        }
    };
}
