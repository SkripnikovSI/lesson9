package ru.ifmo.ctddev.skripnikov.Weather;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public abstract class WeatherView extends RelativeLayout {

    public WeatherView(Context context) {
        super(context);
    }

    public WeatherView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    protected int getIconId(String id) {
        return getResources().
                getIdentifier("i" + id, "drawable", getContext().getPackageName());
    }
}
