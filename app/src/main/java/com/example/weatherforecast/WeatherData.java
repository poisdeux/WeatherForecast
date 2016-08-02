package com.example.weatherforecast;

import org.joda.time.LocalDate;

/**
 * Created by martijn on 27/06/16.
 */
public class WeatherData {
    public LocalDate date;
    public Integer temperatureLow;
    public Integer temperatureHigh;

    public WeatherData(LocalDate date, int temperatureLow, int temperatureHigh) {
        this.date = date;
        this.temperatureHigh = temperatureHigh;
        this.temperatureLow = temperatureLow;
    }

    @Override
    public boolean equals(Object o) {
        if ( ! (o instanceof WeatherData) ) {
            return false;
        }

        WeatherData w = (WeatherData) o;

        return date.equals(w.date)
               && temperatureHigh.equals(w.temperatureHigh)
               && temperatureLow.equals(w.temperatureLow);
    }

    @Override
    public String toString() {
        return date.toString() + " : low=" + temperatureLow + " : high="+temperatureHigh;
    }
}
