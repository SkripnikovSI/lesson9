package ru.ifmo.ctddev.skripnikov.Weather;

public class Weather {
    public final long time;
    public final String title;
    public final String description;
    public final String icon;
    public final float temp;
    public final float humidity;
    public final float pressure;
    public final float windSpeed;
    public final float windDeg;

    public Weather(long time,
                   String title,
                   String description,
                   String icon,
                   float temp,
                   float humidity,
                   float pressure,
                   float windSpeed,
                   float windDeg) {
        this.time = time;
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.windDeg = windDeg;
    }
}
