package ru.ifmo.ctddev.skripnikov.Weather2;

public class Weather {
    public final CurrentWeather cWeather;
    public final ForecastWeather[] fWeather;

    public Weather(CurrentWeather cWeather, ForecastWeather[] fWeather) {
        this.cWeather = cWeather;
        this.fWeather = fWeather;
    }
}
