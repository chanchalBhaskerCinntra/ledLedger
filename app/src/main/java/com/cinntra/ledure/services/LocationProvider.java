package com.cinntra.ledure.services;

import android.location.Location;
import java.util.List;

public interface LocationProvider {

    // Define a method to get location updates
    void getLocation(long interval, LocationCallback callback);

    // Define a callback interface to handle location updates
    interface LocationCallback {
        void onLocationResult(Location location);
    }

    // Custom exception class
    class LocationException extends Exception {
        public LocationException(String message) {
            super(message);
        }
    }
}
