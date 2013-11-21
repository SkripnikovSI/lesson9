package ru.ifmo.ctddev.skripnikov.Weather;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ForecastWeatherView extends WeatherView {

    private ImageView image;
    private TextView time;
    private TextView temp;
    private TextView humidity;
    private TextView wind;

    public ForecastWeatherView(Context context) {
        super(context);
        initialization(context);
    }

    public ForecastWeatherView(Context context, AttributeSet attr) {
        super(context, attr);
        initialization(context);
    }

    private void initialization(Context context) {
        ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.forecast_weather_view, this);
        time = (TextView) findViewById(R.id.text_time);
        image = (ImageView) findViewById(R.id.image_weather);
        temp = (TextView) findViewById(R.id.text_temp);
        humidity = (TextView) findViewById(R.id.text_humidity);
        wind = (TextView) findViewById(R.id.text_wind);
    }

    public void update(Weather weather) {
        time.setText(new SimpleDateFormat("dd.MM   HH:mm").format(new Date(weather.time * 1000)));
        if (weather.temp > 0) {
            temp.setText("+" + Float.toString(weather.temp) + "°");
            temp.setTextColor(Color.RED);
        } else if (weather.temp < 0) {
            temp.setText(Float.toString(weather.temp) + "°");
            temp.setTextColor(Color.BLUE);
        } else {
            temp.setText(Float.toString(weather.temp) + "°");
        }
        humidity.setText(getResources().getString(R.string.humidity) + " " + Float.toString(weather.humidity) + "%");
        wind.setText(getResources().getString(R.string.wind) + " " +
                Float.toString(weather.windSpeed) + " " + getResources().getString(R.string.m_s));
        image.setImageResource(getIconId(weather.icon));
    }

}