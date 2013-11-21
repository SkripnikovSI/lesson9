package ru.ifmo.ctddev.skripnikov.Weather;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MainActivity extends FindLocationActivity {
    private RelativeLayout pb;
    private SharedPreferences sp;
    public static final String PREFERENCE_POSITION = "position";
    private CurrentWeatherView cwv;
    private City[] cities;
    private WeatherFetcher wf;
    private ListView fw;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        cwv = (CurrentWeatherView) findViewById(R.id.current_weather);
        fw = (ListView) findViewById(R.id.forecast_weather);
        pb = (RelativeLayout) findViewById(R.id.progressBar);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        DBStorage dbs = new DBStorage(this);
        cities = dbs.getCities();
        findLocation();
        if (cities.length == 0) {
            Intent intent = new Intent(this, AddCityActivity.class);
            startActivity(intent);
        } else {
            initActionBar();
            getWeathers();
        }
        dbs.destroy();
    }

    public void onRestart() {
        super.onRestart();
        DBStorage dbs = new DBStorage(this);
        cities = dbs.getCities();
        if (cities.length == 0) {
            finish();
        } else {
            initActionBar();
            getWeathers();
        }
        dbs.destroy();
    }
    private void initActionBar() {
        if (locationIsFound && cities != null && cities.length > 0)
            for (City city : cities) city.setDistance(lat, lon);
        CitiesListAdapter adapter = new CitiesListAdapter(getBaseContext(), cities);
        getActionBar().setListNavigationCallbacks(adapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                getWeathers();
                return false;
            }
        });
        getActionBar().setSelectedNavigationItem(sp.getInt(PREFERENCE_POSITION, 0));
    }

    private void getWeathers() {
        if (wf != null)
            wf.cancel(true);
        wf = new WeatherFetcher(cities[getActionBar().getSelectedNavigationIndex()], null);
        wf.execute();
        pb.setVisibility(View.VISIBLE);
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

    private class WeatherFetcher extends AsyncTask<Void, Void, Void> {

        private City city;
        private WeatherUpdater weatherUpdater;

        WeatherFetcher(City city, WeatherUpdater weatherUpdater) {
            this.city = city;
            this.weatherUpdater = weatherUpdater;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(isCancelled())
                return null;
            if (weatherUpdater == WeatherUpdater.CURRENT_WEATHER) {
                WeatherAPI.updateCurrentWeather(city, this);
            } else if (weatherUpdater == WeatherUpdater.FORECAST_WEATHER) {
                WeatherAPI.updateForecastWeather(city, this);
            } else if (weatherUpdater == WeatherUpdater.DAILY_WEATHER) {
                WeatherAPI.updateDailyWeather(city, this);
            } else {
                WeatherAPI.updateCurrentWeather(city, this);
                WeatherAPI.updateForecastWeather(city, this);
                WeatherAPI.updateDailyWeather(city, this);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            super.onPostExecute(param);
            if(isCancelled())
                return;
            if (weatherUpdater == WeatherUpdater.CURRENT_WEATHER) {
                cwv.update(city);
            } else if (weatherUpdater == WeatherUpdater.FORECAST_WEATHER) {
                ForecastWeatherListAdapter adapter = new ForecastWeatherListAdapter(getBaseContext(), city.getForecastWeather());
                fw.setAdapter(adapter);
            } else if (weatherUpdater == WeatherUpdater.DAILY_WEATHER) {

            } else {
                cwv.update(city);
                ForecastWeatherListAdapter adapter = new ForecastWeatherListAdapter(getBaseContext(), city.getForecastWeather());
                fw.setAdapter(adapter);
            }
            pb.setVisibility(View.INVISIBLE);
        }
    }

    private enum WeatherUpdater {
        CURRENT_WEATHER,
        FORECAST_WEATHER,
        DAILY_WEATHER,
    }

}
