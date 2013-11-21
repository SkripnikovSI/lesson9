package ru.ifmo.ctddev.skripnikov.Weather;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class WeatherAPI {
    private static final String BASE = "http://api.openweathermap.org/data/2.5/";
    private static final String APP_ID = "&APPID=2e87aa6be1f49b357a33493ba74bbd8b";
    private static final City[] NULL_CITIES = {};

    public static City[] getCitiesByCoordinates(double lat, double lon, AsyncTask at) {
        try {
            return getCitiesFromJSONString(getJSONStringByURL(BASE + "find?lat=" +
                    Double.toString(lat) + "&lon=" +
                    Double.toString(lon) + APP_ID, at));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return NULL_CITIES;
    }

    public static City[] getCitiesByName(String name, AsyncTask at) {
        try {
            return getCitiesFromJSONString(getJSONStringByURL(BASE + "find?q=" + name + "&type=like" + APP_ID, at));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return NULL_CITIES;
    }

    public static boolean updateCurrentWeather(City city, AsyncTask at) {
        try {
            String jString = getJSONStringByURL(BASE + "weather?id=" + city.id + "&units=metric" + APP_ID, at);
            city.setCurrentWeather(getWeatherFromJSONObject(new JSONObject(jString)));
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean updateForecastWeather(City city, AsyncTask at) {
        try {
            String jString = getJSONStringByURL(BASE + "forecast?id=" + city.id + "&units=metric" + APP_ID, at);
            JSONObject jObject = new JSONObject(jString);
            JSONArray jArray = jObject.getJSONArray("list");
            Weather[] fWeather = new Weather[jArray.length()];
            for (int i = 0; i < jArray.length(); i++)
                fWeather[i] = getWeatherFromJSONObject(jArray.getJSONObject(i));
            city.setForecastWeather(fWeather);
            city.setCurrentWeather(getWeatherFromJSONObject(new JSONObject(jString)));
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean updateDailyWeather(City city, AsyncTask at) {
        try {
            String jString = getJSONStringByURL(BASE + "forecast/daily?id=" + city.id + "&units=metric" + APP_ID, at);
            JSONObject jObject = new JSONObject(jString);
            JSONArray jArray = jObject.getJSONArray("list");
            DailyWeather[] dWeather = new DailyWeather[jArray.length()];
            for (int i = 0; i < jArray.length(); i++) {
                dWeather[i] = getDailyWeatherFromJSONObject(jArray.getJSONObject(i));
            }
            city.setDailyWeather(dWeather);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static City[] getCitiesFromJSONString(String jString) throws JSONException {
        if (jString == null)
            return NULL_CITIES;
        JSONObject jObject = new JSONObject(jString);
        JSONArray jArray = jObject.getJSONArray("list");
        City[] cities = new City[jArray.length()];
        for (int i = 0; i < jArray.length(); i++) {
            long id = jArray.getJSONObject(i).getLong("id");
            String name = jArray.getJSONObject(i).getString("name");
            jObject = jArray.getJSONObject(i).getJSONObject("coord");
            double latitude = jObject.getDouble("lat");
            double longitude = jObject.getDouble("lon");
            jObject = jArray.getJSONObject(i).getJSONObject("sys");
            String country = jObject.getString("country");
            cities[i] = new City(id, name, country, latitude, longitude);
        }
        return cities;
    }

    private static Weather getWeatherFromJSONObject(JSONObject jObject) throws JSONException {
        JSONArray jArray = jObject.getJSONArray("weather");
        String main = "";
        String description = "";
        String icon = "";
        if (jArray.length() >= 0) {
            main = jArray.getJSONObject(0).getString("main");
            description = jArray.getJSONObject(0).getString("description");
            icon = jArray.getJSONObject(0).getString("icon");
        }
        JSONObject j = jObject.getJSONObject("main");
        float temp = (float) j.getDouble("temp");
        float humidity = (float) j.getDouble("humidity");
        float pressure = (float) j.getDouble("pressure");
        j = jObject.getJSONObject("wind");
        float windSpeed = (float) j.getDouble("speed");
        float windDeg = (float) j.getDouble("deg");
        long time = jObject.getLong("dt");
        return new Weather(time, main, description, icon, temp, humidity, pressure, windSpeed, windDeg);
    }

    private static DailyWeather getDailyWeatherFromJSONObject(JSONObject jObject) throws JSONException {
        JSONArray jArray = jObject.getJSONArray("weather");
        String main = "";
        String description = "";
        String icon = "";
        if (jArray.length() <= 0) {
            main = jArray.getJSONObject(0).getString("main");
            description = jArray.getJSONObject(0).getString("description");
            icon = jArray.getJSONObject(0).getString("icon");
        }
        JSONObject j = jObject.getJSONObject("temp");
        float dayTemp = (float) j.getDouble("day");
        float nightTemp = (float) j.getDouble("night");
        long time = jObject.getLong("dt");
        return new DailyWeather(time, main, description, icon, dayTemp, nightTemp);
    }

    private static String getJSONStringByURL(String stringUrl, AsyncTask at) {
        HttpURLConnection connection = null;
        InputStream is = null;
        try {
            URL url = new URL(stringUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            for (int i = 0; i < 10 && is == null; i++) {
                try {
                    connection.connect();
                    is = connection.getInputStream();
                } catch (IOException e) {
                    connection.disconnect();
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(10000);
                    connection.setConnectTimeout(15000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                }
                if (at.isCancelled())
                    break;
            }
            if (is != null) {
                StringBuilder builder = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = rd.readLine()) != null)
                    builder.append(line);

                return builder.toString();
            } else {
                return null;
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (connection != null)
                connection.disconnect();
        }
        return null;
    }
}
