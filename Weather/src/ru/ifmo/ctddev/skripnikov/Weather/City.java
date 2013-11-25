package ru.ifmo.ctddev.skripnikov.Weather;

import java.util.ArrayList;

public class City {
    public final String name;
    public final long id;
    public final String country;
    public final double latitude;
    public final double longitude;
    private double distance = 0;
    private Weather cWeather;
    private Weather[] fWeather;
    private DailyWeather[] dWeather;

    City(long id, String name,
         String country,
         double latitude,
         double longitude) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setDistance(double lat, double lon) {
        double R = 6371;
        // перевести координаты в радианы
        double lat1 = lat * Math.PI / 180;
        double lat2 = latitude * Math.PI / 180;
        double long1 = lon * Math.PI / 180;
        double long2 = longitude * Math.PI / 180;
        // косинусы и синусы широт и разницы долгот
        double cl1 = Math.cos(lat1);
        double cl2 = Math.cos(lat2);
        double sl1 = Math.sin(lat1);
        double sl2 = Math.sin(lat2);
        double delta = long2 - long1;
        double cdelta = Math.cos(delta);
        double sdelta = Math.sin(delta);
        // вычисления длины большого круга
        double y = Math.sqrt(Math.pow(cl2 * sdelta, 2) + Math.pow(cl1 * sl2 - sl1 * cl2 * cdelta, 2));
        double x = sl1 * sl2 + cl1 * cl2 * cdelta;
        distance = Math.atan2(y, x) * R;
    }

    public String getDistance() {
        if (distance == 0)
            return "";
        return Long.toString(Math.round(distance)) + "km";
    }

    public void setCurrentWeather(Weather cWeather) {
        this.cWeather = cWeather;
    }

    public Weather getCurrentWeather() {
        return cWeather;
    }

    public void setForecastWeather(Weather[] fWeather) {
        this.fWeather = fWeather;
    }

    public Weather[] getForecastWeather() {
        if(fWeather == null)
            fWeather = new Weather[0];
        ArrayList<Weather> al = new ArrayList<Weather>();
        for (Weather aFWeather : fWeather)
            if (aFWeather.time > cWeather.time)
                al.add(aFWeather);
        Weather[] result = new Weather[al.size()];
        al.toArray(result);
        return result;
    }

    public void setDailyWeather(DailyWeather[] dWeather) {
        this.dWeather = dWeather;
    }

    public DailyWeather[] getDailyWeather() {
        return dWeather;
    }
}
