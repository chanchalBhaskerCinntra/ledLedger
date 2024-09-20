package com.cinntra.ledure.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.cinntra.ledure.R;

public class LocationNotification {
    public static final String CHANNEL_ID = "location_channel_id";
    public static final int ID = 1;

    private final Context context;
    NotificationManager manager;

    public LocationNotification(Context context) {
        this.context = context;
        manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void updateContentText(double latitude, double longitude) {
         manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = createNotification(latitude, longitude);
        manager.notify(ID, notification);
    }

    private Notification createNotification(double latitude, double longitude) {
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Location Service")
                .setContentText("Lat: " + latitude + ", Lon: " + longitude)
                .setSmallIcon(R.drawable.ic_map_icon)  // Replace with your own icon
                .build();
    }


    public void createChannel() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                  "Location sharing",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("location description");
            manager.createNotificationChannel(channel);
        }
    }
}
