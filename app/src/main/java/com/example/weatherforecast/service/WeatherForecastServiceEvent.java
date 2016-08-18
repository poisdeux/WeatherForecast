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

/**
 * Created by martijn on 05/08/16.
 */
public class WeatherForecastServiceEvent {
    public enum Type {
        SYNC_COMPLETE,
        SQL_ERROR,
        HTTP_ERROR
    }

    private Type event;
    private String message;

    public WeatherForecastServiceEvent(Type event) {
        this.event = event;
    }

    public Type getEvent() {
        return event;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
