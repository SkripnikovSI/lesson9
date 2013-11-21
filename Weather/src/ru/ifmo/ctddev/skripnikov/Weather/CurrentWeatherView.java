package ru.ifmo.ctddev.skripnikov.Weather;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

public class CurrentWeatherView extends WeatherView {
    private ImageView image;
    private TextView description;
    private TextView temp;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;

    public CurrentWeatherView(Context context) {
        super(context);
        initialization(context);
    }

    public CurrentWeatherView(Context context, AttributeSet attr) {
        super(context, attr);
        initialization(context);
    }

    private void initialization(Context context) {
        ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.current_weather_view, this);
        description = (TextView) findViewById(R.id.text_description);
        image = (ImageView) findViewById(R.id.image_current_weather);
        temp = (TextView) findViewById(R.id.text_temp);
        humidity = (TextView) findViewById(R.id.text_humidity);
        pressure = (TextView) findViewById(R.id.text_pressure);
        wind = (TextView) findViewById(R.id.text_wind);
    }

    public void update(City city) {
        Weather weather = city.getCurrentWeather();
        description.setText(weather.description);
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
        pressure.setText(getResources().getString(R.string.pressure) + " " +
                Float.toString(weather.pressure) + " " + getResources().getString(R.string.hpa));
        wind.setText(getResources().getString(R.string.wind) + " " +
                Float.toString(weather.windSpeed) + " " + getResources().getString(R.string.m_s));
        image.setImageResource(getIconId(weather.icon));
    }
}