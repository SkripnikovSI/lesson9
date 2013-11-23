package ru.ifmo.ctddev.skripnikov.Weather2;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MainActivity extends FindLocationActivity {

    public static final String PREFERENCE_POSITION = "position";

    private SharedPreferences sp;
    private RelativeLayout pb;
    private CurrentWeatherView cwv;
    private City[] cities;
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
        if (locationIsFound)
            for (City city : cities) city.setDistance(lat, lon);
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
        dbs.destroy();
    }

    private void fetchWeather() {
        WeatherFetcher wf = new WeatherFetcher(cities[getActionBar().getSelectedNavigationIndex()]);
        wf.execute();
        pb.setVisibility(View.VISIBLE);
    }

    public void onRestart() {
        super.onRestart();
        DBStorage dbs = new DBStorage(this);
        cities = dbs.getCities();
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
        dbs.destroy();
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

        WeatherFetcher(City city) {
            this.city = city;
        }

        @Override
        protected Void doInBackground(Void... params) {
            WorldWeatherOnlineAPI.updateWeather(city);
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            super.onPostExecute(param);
            cwv.update(city);
            ForecastWeatherListAdapter adapter = new ForecastWeatherListAdapter(getBaseContext(), city.getForecastWeather());
            fw.setAdapter(adapter);
            pb.setVisibility(View.INVISIBLE);
        }
    }
}
