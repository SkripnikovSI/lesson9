package ru.ifmo.ctddev.skripnikov.Weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class CitiesListAdapter extends ArrayAdapter<City> implements SpinnerAdapter {
    private final Context context;
    private final City[] cities;

    public CitiesListAdapter(Context context, City[] cities) {
        super(context, android.R.layout.simple_list_item_2, cities);
        this.context = context;
        this.cities = cities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item;
        if (convertView == null) {
            item = ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
        } else {
            item = convertView;
        }
        ((TextView) item.findViewById(android.R.id.text1))
                .setText(cities[position].name + ", " + cities[position].country);
        ((TextView) item.findViewById(android.R.id.text2))
                .setText(Float.toString((float) cities[position].latitude) + ", "
                        + Float.toString((float) cities[position].longitude) + ", " + cities[position].getDistance());
        return item;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
