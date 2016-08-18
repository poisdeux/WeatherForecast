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

package com.example.weatherforecast;

import android.app.Service;
import android.content.Intent;

import com.example.weatherforecast.service.WeatherForecastService;
import com.example.weatherforecast.utils.TestData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by martijn on 08/08/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class WeatherForecastServiceTest {
    private WeatherForecastService weatherForecastService;
    private MockWebServer server;

    @Before
    public void setup() throws Exception {
        weatherForecastService = new WeatherForecastService();
        weatherForecastService.onCreate();

        server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(TestData.getJsonResponse()));
        server.start();
    }

    @After
    public void cleanup() throws Exception {
        weatherForecastService.onDestroy();
        server.shutdown();
    }

    @Test
    public void onStartCommandTest() throws Exception {
        assertFalse("WeatherForecastService says its syncing while it shouldn't",
                    weatherForecastService.isSyncing());
        Intent intent = new Intent(RuntimeEnvironment.application, WeatherForecastService.class);
        intent.putExtra(WeatherForecastService.BUNDLE_KEY_FORECAST_URL, server.url("/didwedoarequest").toString());
        int continuation_mask = weatherForecastService.onStartCommand(intent, 0, 0);

        assertTrue("Service should be started START_NOT_STICKY",
                   continuation_mask == Service.START_NOT_STICKY);
        assertTrue("WeatherForecastService should be syncing but it is not",
                   weatherForecastService.isSyncing());

        RecordedRequest request = server.takeRequest();
        assertEquals("" + request.getPath(), "/didwedoarequest", request.getPath());
    }

    @Test
    public void onBindTest() {
        WeatherForecastService.LocalBinder localBinder =
                (WeatherForecastService.LocalBinder) weatherForecastService.onBind(null);
        assertNotNull(localBinder);
        WeatherForecastService weatherForecastService = localBinder.getService();
        assertNotNull(weatherForecastService);
        assertFalse("WeatherForecastService says its syncing while it shouldn't",
                    weatherForecastService.isSyncing());

        Intent intent = new Intent(RuntimeEnvironment.application, WeatherForecastService.class);
        intent.putExtra(WeatherForecastService.BUNDLE_KEY_FORECAST_URL, server.url("").toString());
        weatherForecastService.onStartCommand(intent, 0, 0);

        assertTrue("WeatherForecastService should be syncing but it is not",
                   weatherForecastService.isSyncing());

        weatherForecastService.onTaskRemoved(intent);
        assertFalse("WeatherForecastService says its syncing while it shouldn't",
                    weatherForecastService.isSyncing());
    }
}
