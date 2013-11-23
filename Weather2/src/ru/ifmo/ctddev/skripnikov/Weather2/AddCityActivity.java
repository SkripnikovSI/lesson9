package ru.ifmo.ctddev.skripnikov.Weather2;

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

import java.util.concurrent.TimeUnit;

public class AddCityActivity extends FindLocationActivity {
    private ListView citiesList;
    private ProgressBarView progressBar;
    private String name = "";
    private CitiesFetcher cf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city_activity);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = (ProgressBarView) findViewById(R.id.progress_bar);
        progressBar.setText(getResources().getString(R.string.fetching_cities));
        citiesList = (ListView) findViewById(R.id.list_view_cities);
        citiesList.setEmptyView(findViewById(R.id.text_view_message));
        EditText cityName = (EditText) findViewById(R.id.edit_text_city);
        cityName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                name = s.toString();
                fetchCities();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        findLocation();
        fetchCities();
    }

    private void fetchCities() {
        cf = new CitiesFetcher();
        cf.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    private class CitiesFetcher extends AsyncTask<Void, Void, Void> {
        private City[] cities;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (this != cf)
                return null;
            name = name.trim();
            if (name.length() > 0) {
                name = name.replace(' ', '+');
                cities = WorldWeatherOnlineAPI.getCitiesByName(name);
            } else if (locationIsFound) {
                cities = WorldWeatherOnlineAPI.getCitiesByCoordinates(lat, lon);
            } else {
                cities = WorldWeatherOnlineAPI.NULL_CITIES;
            }
            if (locationIsFound)
                for (City city : cities) city.setDistance(lat, lon);
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            super.onPostExecute(param);
            if (cf != this)
                return;
            progressBar.setVisibility(View.INVISIBLE);
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
