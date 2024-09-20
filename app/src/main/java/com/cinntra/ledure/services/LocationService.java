package com.cinntra.ledure.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.cinntra.ledure.R;


import android.content.Context;


public class LocationService extends Service {

    public static final String ACTION_START = "actionStart";
    public static final String ACTION_STOP = "actionStop";
    public static final long INTERVAL = 120_000L; // 2 minutes in milliseconds

    private LocationNotification notification;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Handler handler;
    private Runnable locationRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize your notification and locationManager here
        notification = new LocationNotification(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        handler = new Handler();
        // Define a LocationListener to handle location updates
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                notification.updateContentText(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                start();
            } else if (ACTION_STOP.equals(action)) {
                stop();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void start() {
        startForegroundService();
        startLocationUpdates();
    }

    private void stop() {
        stopLocationUpdates();
        stopForeground(STOP_FOREGROUND_REMOVE);
        stopSelf();
    }

    private void startLocationUpdates() {
        // Request location updates from the LocationManager
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL, 0, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void stopLocationUpdates() {
        // Remove location updates
        locationManager.removeUpdates(locationListener);
    }

    private void startForegroundService() {
        Notification notification = createNotification();
        startForeground(LocationNotification.ID, notification);
    }

    private Notification createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    LocationNotification.CHANNEL_ID,
                    "Location Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

        return new NotificationCompat.Builder(this, LocationNotification.CHANNEL_ID)
                .setContentTitle("Location Service")
                .setContentText("Tracking location...")
                .setSmallIcon(R.drawable.ic_map_icon)  // Replace with your own icon
                .build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

