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
        if (id.equals("01d")) {
            return R.drawable.i01d;
        } else if (id.equals("01n")) {
            return R.drawable.i01n;
        } else if (id.equals("02d")) {
            return R.drawable.i02d;
        } else if (id.equals("02n")) {
            return R.drawable.i02n;
        } else if (id.equals("03d")) {
            return R.drawable.i03d;
        } else if (id.equals("03n")) {
            return R.drawable.i03n;
        } else if (id.equals("04d")) {
            return R.drawable.i04d;
        } else if (id.equals("04n")) {
            return R.drawable.i04n;
        } else if (id.equals("09d")) {
            return R.drawable.i09d;
        } else if (id.equals("09n")) {
            return R.drawable.i09n;
        } else if (id.equals("10d")) {
            return R.drawable.i10d;
        } else if (id.equals("10n")) {
            return R.drawable.i10n;
        } else if (id.equals("11d")) {
            return R.drawable.i11d;
        } else if (id.equals("11n")) {
            return R.drawable.i11n;
        } else if (id.equals("13d")) {
            return R.drawable.i13d;
        } else if (id.equals("13n")) {
            return R.drawable.i13n;
        } else if (id.equals("50d")) {
            return R.drawable.i50d;
        } else {
            return R.drawable.i50n;
        }
    }
}
