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
