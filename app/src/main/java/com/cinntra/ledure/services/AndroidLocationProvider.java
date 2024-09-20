package com.cinntra.ledure.services;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.cinntra.ledure.services.LocationProvider;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AndroidLocationProvider implements LocationProvider {

    private final Context context;
    private final LocationManager manager;
    private final FusedLocationProviderClient client;
    private final List<LocationDetails> dataArray = new ArrayList<>();

    public AndroidLocationProvider(Context context, LocationManager manager, FusedLocationProviderClient client) {
        this.context = context;
        this.manager = manager;
        this.client = client;
    }

    @Override
    public void getLocation(long interval, final LocationCallback callback) {
        if (!checkLocationPermissions()) {
            throw new LocationException("Missing location permissions");
        }
        if (locationDisabled()) {
            throw new LocationException("Location is disabled");
        }

        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(interval)
                .setFastestInterval(interval)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        com.google.android.gms.location.LocationCallback locationCallback = new com.google.android.gms.location.LocationCallback() {
            @RequiresApi(Build.VERSION_CODES.O)
            @Override
            public void onLocationResult(LocationResult result) {
                super.onLocationResult(result);
                if (result == null || result.getLocations().isEmpty()) {
                    return;
                }

                Location location = result.getLocations().get(result.getLocations().size() - 1);
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                int second = c.get(Calendar.SECOND);
                String time = hour + ":" + minute + ":" + second;

                Log.e("LocListSize-InTime", time);
                Log.e("LocListSize-InTime", "" + location.getLatitude());
                Log.e("LocListSize-InTime", "" + location.getLongitude());
                Log.e("LocListSize-InTime", "" + time);

                LocationDetails obj = new LocationDetails(time, result.getLocations().toString(), location.getLatitude(), location.getLongitude());
                dataArray.add(obj);
                Gson gson = new Gson();
                String json = gson.toJson(dataArray);
                // Save JSON string to shared preferences or wherever needed
                // Prefs.putString("LocList", json);

                if (callback != null) {
                    callback.onLocationResult(location);
                }
            }
        };

        try {
            client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } catch (SecurityException e) {
            // Handle the exception if permission is not granted
            Toast.makeText(context, "Location permission is not granted", Toast.LENGTH_SHORT).show();
        }

       // client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        // Handle cleanup
        // This can be done in a separate method or lifecycle event like onDestroy for a Service
        // client.removeLocationUpdates(locationCallback);
    }

    private boolean checkLocationPermissions() {
        // Implement permission check logic here
        // This typically involves checking if the user has granted location permissions
        return true;
    }

    private boolean locationDisabled() {
        return !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static class LocationException extends RuntimeException {
        public LocationException(String message) {
            super(message);
        }
    }

    // Example class for LocationDetails, should be replaced with your actual implementation
    public static class LocationDetails {
        private final String time;
        private final String locations;
        private final double latitude;
        private final double longitude;

        public LocationDetails(String time, String locations, double latitude, double longitude) {
            this.time = time;
            this.locations = locations;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        // Getters and setters
    }
}