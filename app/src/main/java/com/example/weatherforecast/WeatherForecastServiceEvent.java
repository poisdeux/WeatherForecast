package com.example.weatherforecast;

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
