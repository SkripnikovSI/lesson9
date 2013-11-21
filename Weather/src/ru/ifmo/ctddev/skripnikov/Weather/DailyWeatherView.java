package ru.ifmo.ctddev.skripnikov.Weather;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyWeatherView extends WeatherView {

    private ImageView image;
    private TextView time;
    private TextView temp_night;
    private TextView temp_day;

    public DailyWeatherView(Context context) {
        super(context);
        initialization(context);
    }

    public DailyWeatherView(Context context, AttributeSet attr) {
        super(context, attr);
        initialization(context);
    }

    private void initialization(Context context) {
        ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.daily_weather_view, this);
        time = (TextView) findViewById(R.id.text_time);
        image = (ImageView) findViewById(R.id.image_weather);
        temp_day = (TextView) findViewById(R.id.text_temp_day);
        temp_night = (TextView) findViewById(R.id.text_temp_night);
    }

    public void update(DailyWeather dw) {
        time.setText(new SimpleDateFormat("dd.MM").format(new Date(dw.time * 1000)));
        if (dw.dayTemp > 0) {
            temp_day.setText("+" + Float.toString(dw.dayTemp));
            temp_day.setTextColor(Color.RED);
        } else if (dw.dayTemp < 0) {
            temp_day.setText(Float.toString(dw.dayTemp));
            temp_day.setTextColor(Color.BLUE);
        } else {
            temp_day.setText(Float.toString(dw.dayTemp));
        }
        if (dw.nightTemp > 0) {
            temp_night.setText("+" + Float.toString(dw.nightTemp));
            temp_night.setTextColor(Color.RED);
        } else if (dw.nightTemp < 0) {
            temp_night.setText(Float.toString(dw.nightTemp));
            temp_night.setTextColor(Color.BLUE);
        } else {
            temp_night.setText(Float.toString(dw.nightTemp));
        }
        image.setImageResource(getIconId(dw.icon));
    }
}