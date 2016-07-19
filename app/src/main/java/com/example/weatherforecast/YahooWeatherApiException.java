package com.example.weatherforecast;

/**
 * Created by martijn on 19/07/16.
 */
public class YahooWeatherApiException extends Exception {
    public static final int INVALID_RESPONSE_FROM_HOST = 1;

    private int errorCode;

    public YahooWeatherApiException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "Error: " + errorCode + ", " + getMessage();
    }
}
