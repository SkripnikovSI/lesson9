package ru.ifmo.ctddev.skripnikov.Weather;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class AddCityActivity extends FindLocationActivity {
    private ListView citiesList;
    private LinearLayout pb;
    private String name = "";
    private CitiesFetcher cf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city_activity);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        pb = (LinearLayout) findViewById(R.id.progressBar);
        citiesList = (ListView) findViewById(R.id.list_view_cities);
        citiesList.setEmptyView(findViewById(R.id.text_view_message));
        EditText cityName = (EditText) findViewById(R.id.edit_text_city);
        cityName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                name = s.toString();
                cf.cancel(true);
                cf = new CitiesFetcher();
                cf.execute();
                pb.setVisibility(View.VISIBLE);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        findLocation();
        cf = new CitiesFetcher();
        cf.execute();
        pb.setVisibility(View.VISIBLE);
    }

    private class CitiesFetcher extends AsyncTask<Void, Void, Void> {

        private City[] cities;
        private final City[] NULL_CITIES = {};

        @Override
        protected Void doInBackground(Void... params) {
            if(isCancelled())
                return null;
            if (name.length() > 0) {
                cities = WeatherAPI.getCitiesByName(name, this);
            } else if (locationIsFound) {
                cities = WeatherAPI.getCitiesByCoordinates(lat, lon, this);
            } else {
                cities = NULL_CITIES;
            }
            if (locationIsFound)
                for (City city : cities) city.setDistance(lat, lon);
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            super.onPostExecute(param);
            if(isCancelled())
                return;
            pb.setVisibility(View.INVISIBLE);
            CitiesListAdapter adapter = new CitiesListAdapter(getBaseContext(), cities);
            citiesList.setAdapter(adapter);
            citiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                        DBStorage dbs = new DBStorage(getBaseContext());
                        dbs.addCity(cities[position]);
                        dbs.destroy();
                        finish();
                    }
                });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
