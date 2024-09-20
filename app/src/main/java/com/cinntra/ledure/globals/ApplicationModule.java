package com.cinntra.ledure.globals;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.location.LocationManager;

import com.cinntra.ledure.services.AndroidLocationProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class ApplicationModule {

    private static Application application; // Assuming you will set this during app initialization

    // Set the application context (to be called once during app initialization)
    public static void setApplication(Application app) {
        application = app;
    }

    public static LocationManager provideLocationManager() {
        return (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
    }

    public static FusedLocationProviderClient provideFusedLocationProviderClient() {
        return LocationServices.getFusedLocationProviderClient(application);
    }

    public static AndroidLocationProvider provideAndroidLocationProvider() {
        return new AndroidLocationProvider(
                application,
                provideLocationManager(),
                provideFusedLocationProviderClient()
        );
    }

    public static NotificationManager provideNotificationManager() {
        return (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /*public static LocationNotification provideLocationNotification() {
        return new LocationNotification(
                application,
                provideNotificationManager()
        );
    }*/
}
