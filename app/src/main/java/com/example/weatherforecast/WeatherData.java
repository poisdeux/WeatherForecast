package com.example.weatherforecast;

import org.joda.time.LocalDate;

/**
 * Created by martijn on 27/06/16.
 */
public class WeatherData {
    public LocalDate date;
    public Integer temperatureMin;
    public Integer temperatureMax;

    public WeatherData(LocalDate date, int temperatureMin, int temperatureMax) {
        this.date = date;
        this.temperatureMax = temperatureMax;
        this.temperatureMin = temperatureMin;
    }

    @Override
    public boolean equals(Object o) {
        WeatherData w;
        try {
            w = (WeatherData) o;
        } catch (Exception e) {
            return false;
        }
        return date.equals(w.date)
               && temperatureMax.equals(w.temperatureMax)
               && temperatureMin.equals(w.temperatureMin);
    }
}
