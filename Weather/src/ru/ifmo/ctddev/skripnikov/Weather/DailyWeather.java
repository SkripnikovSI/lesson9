package ru.ifmo.ctddev.skripnikov.Weather;

public class DailyWeather {
    public final long time;
    public final String title;
    public final String description;
    public final String icon;
    public final float dayTemp;
    public final float nightTemp;

    public DailyWeather(long time,
                        String title,
                        String description,
                        String icon,
                        float dayTemp,
                        float nightTemp) {
        this.time = time;
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.dayTemp = dayTemp;
        this.nightTemp = nightTemp;
    }
}
