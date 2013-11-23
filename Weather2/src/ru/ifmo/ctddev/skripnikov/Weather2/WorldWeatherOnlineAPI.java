package ru.ifmo.ctddev.skripnikov.Weather2;

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

public class WorldWeatherOnlineAPI {
    private static final String BASE = "http://api.worldweatheronline.com/free/v1/";
    private static final String APP_ID = "&format=json&key=s2pufaxvwh4fwzfx6rrnpthg";
    public static final City[] NULL_CITIES = {};

    public static City[] getCitiesByCoordinates(double lat, double lon) {
        try {
            return getCitiesFromJSONString(getJSONStringByURL(BASE + "search.ashx?q=" +
                    Double.toString(lat) + "%2C" +
                    Double.toString(lon) + APP_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return NULL_CITIES;
    }

    public static City[] getCitiesByName(String name) {
        try {
            return getCitiesFromJSONString(getJSONStringByURL(BASE + "search.ashx?q=" + name + APP_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return NULL_CITIES;
    }

    public static boolean updateWeather(City city) {
        String jString = getJSONStringByURL(BASE + "weather.ashx?q=" + city.latitude + "%2C" + city.longitude + "&num_of_days=5" + APP_ID);
        if (jString == null)
            return false;
        try {
            JSONObject jObject = new JSONObject(jString);
            jObject = jObject.getJSONObject("data");
            city.setCurrentWeather(new CurrentWeather(jObject.getJSONArray("current_condition").getJSONObject(0)));
            JSONArray jsonArray = jObject.getJSONArray("weather");
            ForecastWeather[] forecastWeathers = new ForecastWeather[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++)
                forecastWeathers[i] = new ForecastWeather(jsonArray.getJSONObject(i));
            city.setForecastWeather(forecastWeathers);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static City[] getCitiesFromJSONString(String jString) throws JSONException {
        if (jString == null)
            return NULL_CITIES;
        JSONObject jObject = new JSONObject(jString);
        jObject = jObject.getJSONObject("search_api");
        JSONArray jArray = jObject.getJSONArray("result");
        City[] cities = new City[jArray.length()];
        for (int i = 0; i < jArray.length(); i++) {
            String name = jArray.getJSONObject(i).getJSONArray("areaName").getJSONObject(0).getString("value");
            String region = jArray.getJSONObject(i).getJSONArray("region").getJSONObject(0).getString("value");
            String country = jArray.getJSONObject(i).getJSONArray("country").getJSONObject(0).getString("value");
            float latitude = (float) jArray.getJSONObject(i).getDouble("latitude");
            float longitude = (float) jArray.getJSONObject(i).getDouble("longitude");
            cities[i] = new City(name, region, country, latitude, longitude);
        }
        return cities;
    }

    private static String getJSONStringByURL(String stringUrl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        try {
            URL url = new URL(stringUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();
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
            return null;
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