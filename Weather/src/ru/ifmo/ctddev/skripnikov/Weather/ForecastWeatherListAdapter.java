package ru.ifmo.ctddev.skripnikov.Weather;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ForecastWeatherListAdapter extends ArrayAdapter<Weather>{
    private final Context context;
    private final Weather[] weathers;

    public ForecastWeatherListAdapter(Context context, Weather[] weathers) {
        super(context, R.layout.forecast_weather_view, weathers);
        this.context = context;
        this.weathers = weathers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item;
        if (convertView == null) {
            item = new ForecastWeatherView(context);
        } else {
            item = convertView;
        }
        ((ForecastWeatherView)item).update(weathers[position]);
        return item;
    }
}
