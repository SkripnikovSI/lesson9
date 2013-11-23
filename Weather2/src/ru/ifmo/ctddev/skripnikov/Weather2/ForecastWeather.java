package ru.ifmo.ctddev.skripnikov.Weather2;

import android.graphics.drawable.Drawable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ForecastWeather {
    public final String weatherDesc;
    public final long date;
    public final int weatherCode;
    public final int tempMaxC;
    public final int tempMinC;
    public final int windspeedKmph;
    public final int winddirDegree;
    public final int precipMM;
    public final Drawable icon;

    public ForecastWeather(JSONObject jsonObject) throws JSONException, IOException {
        String url = jsonObject.getJSONArray("weatherIconUrl").getJSONObject(0).getString("value");
        icon = Drawable.createFromStream(
                (InputStream) new URL(url).getContent(), "src");
        weatherDesc = jsonObject.getJSONArray("weatherDesc").getJSONObject(0).getString("value");
        long time;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            time = format.parse(jsonObject.getString("date")).getTime();
        } catch (ParseException e) {
            time = System.currentTimeMillis();
        }
        date = time;
        weatherCode = jsonObject.getInt("weatherCode");
        tempMaxC = jsonObject.getInt("tempMaxC");
        tempMinC = jsonObject.getInt("tempMinC");
        windspeedKmph = jsonObject.getInt("windspeedKmph");
        winddirDegree = jsonObject.getInt("winddirDegree");
        precipMM = jsonObject.getInt("precipMM");
    }
}
