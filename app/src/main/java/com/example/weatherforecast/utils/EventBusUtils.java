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

package com.example.weatherforecast.utils;

import com.example.weatherforecast.service.WeatherForecastServiceEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by martijn on 05/08/16.
 */
public class EventBusUtils {

    public static void sendSqlErrorEvent(String message) {
        WeatherForecastServiceEvent event =
                new WeatherForecastServiceEvent(WeatherForecastServiceEvent.Type.SQL_ERROR);
        event.setMessage(message);
        EventBus.getDefault().post(event);
    }

    public static void sendSyncCompleteEvent() {
        EventBus.getDefault().post(
                new WeatherForecastServiceEvent(WeatherForecastServiceEvent.Type.SYNC_COMPLETE));
    }

    public static void sendHttpErrorEvent(String message) {
        WeatherForecastServiceEvent event =
                new WeatherForecastServiceEvent(WeatherForecastServiceEvent.Type.HTTP_ERROR);
        event.setMessage(message);
        EventBus.getDefault().post(event);
    }
}
