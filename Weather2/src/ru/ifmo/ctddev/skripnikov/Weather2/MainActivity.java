package ru.ifmo.ctddev.skripnikov.Weather2;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends FindLocationActivity {

    public static final String PREFERENCE_POSITION = "position";

    private SharedPreferences sp;
    private ProgressBarView progressBar;
    private CurrentWeatherView cwv;
    private City[] cities;
    private ListView fw;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        cwv = (CurrentWeatherView) findViewById(R.id.current_weather);
        fw = (ListView) findViewById(R.id.forecast_weather);
        progressBar = (ProgressBarView) findViewById(R.id.progress_bar);
        progressBar.setText(getResources().getString(R.string.fetching_weather));
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        fetchCities();
        if (cities.length == 0) {
            Intent intent = new Intent(this, AddCityActivity.class);
            startActivity(intent);
        } else {
            CitiesListAdapter adapter = new CitiesListAdapter(getBaseContext(), cities);
            getActionBar().setListNavigationCallbacks(adapter, new ActionBar.OnNavigationListener() {
                @Override
                public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                    fetchWeather();
                    return false;
                }
            });
            getActionBar().setSelectedNavigationItem(sp.getInt(PREFERENCE_POSITION, 0));
            fetchWeather();
        }
    }

    private void fetchWeather() {
        City city = cities[getActionBar().getSelectedNavigationIndex()];
        WeatherFetcher wf = new WeatherFetcher(city);
        wf.execute(city.latitude, city.longitude);
    }

    private void fetchCities() {
        DBStorage dbs = new DBStorage(this);
        cities = dbs.getCities();
        dbs.destroy();
        findLocation();
        if (locationIsFound)
            for (City city : cities) city.setDistance(lat, lon);
    }

    public void onRestart() {
        super.onRestart();
        fetchCities();
        if (cities.length == 0) {
            finish();
        } else {
            CitiesListAdapter adapter = new CitiesListAdapter(getBaseContext(), cities);
            getActionBar().setListNavigationCallbacks(adapter, new ActionBar.OnNavigationListener() {
                @Override
                public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                    fetchWeather();
                    return false;
                }
            });
            getActionBar().setSelectedNavigationItem(sp.getInt(PREFERENCE_POSITION, 0));
        }
    }

    public void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(PREFERENCE_POSITION, getActionBar().getSelectedNavigationIndex());
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_city:
                Intent intent = new Intent(this, AddCityActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class WeatherFetcher extends AsyncTask<Float, Void, Weather> {
        private City city;

        WeatherFetcher(City city) {
            this.city = city;
        }

        @Override
        protected void onPreExecute() {
            progressBar.show();
        }

        @Override
        protected Weather doInBackground(Float... params) {
            return WorldWeatherOnlineAPI.getWeather(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Weather param) {
            super.onPostExecute(param);
            city.setWeather(param);
            cwv.update(city);
            ForecastWeatherListAdapter adapter = new ForecastWeatherListAdapter(getBaseContext(), city.getForecastWeather());
            fw.setAdapter(adapter);
            progressBar.hide();
        }
    }
}
