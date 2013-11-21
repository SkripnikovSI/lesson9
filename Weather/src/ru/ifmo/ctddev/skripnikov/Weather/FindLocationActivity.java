package ru.ifmo.ctddev.skripnikov.Weather;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public abstract class FindLocationActivity extends Activity {

    protected boolean locationIsFound = false;
    protected double lat;
    protected double lon;

    protected void findLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null)
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null)
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if (location == null) {
            locationIsFound = false;
            return;
        }
        lat = location.getLatitude();
        lon = location.getLongitude();
        locationIsFound = true;
    }
}
