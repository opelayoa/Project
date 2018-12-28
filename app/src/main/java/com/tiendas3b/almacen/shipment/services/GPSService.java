package com.tiendas3b.almacen.shipment.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.tiendas3b.almacen.R;

public class GPSService extends Service {

    private Handler handler;
    private Runnable runnable;

    private BroadcastReceiver broadcastReceiver;

    GeolocationHelper geolocationHelper;
    private final static int INTERVAL = 5000;
    public static final String LOCATION_UPDATE = "location_update";
    public static final String REQUEST_LOCATION = "request_location";

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        geolocationHelper = new GeolocationHelper(getApplicationContext());

        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    broadCastLocation(geolocationHelper.getLastLocation());
                }
            };
        }

        registerReceiver(broadcastReceiver, new IntentFilter(GPSService.REQUEST_LOCATION));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        runnable = new TimerRunnable();
        initLocation();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, INTERVAL);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        mNotificationManager.cancelAll();
        super.onDestroy();
    }

    private class TimerRunnable implements Runnable {

        public TimerRunnable() {

        }

        @Override
        public void run() {

            Location location = geolocationHelper.getLastLocation();
            sendLocation(location);

            handler.postDelayed(this, INTERVAL);
        }
    }

    private void broadCastLocation(Location location) {
        Intent intent = new Intent(LOCATION_UPDATE);
        intent.putExtra("location", location);
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initLocation() {
        geolocationHelper.addLocationListener(new CallbackResponse<Location>() {
            @Override
            public void sendValue(Location response) {

            }

            @Override
            public void error(Exception e) {

            }
        });
    }

    private void sendLocation(Location location) {
        broadCastLocation(location);
        sendNotification();
    }

    private NotificationManager mNotificationManager;

    public void sendNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");

        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Tiendas 3B")
                .setContentText("Viaje iniciado...")
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .setSound(null);

        this.getApplicationContext();
        mNotificationManager =
                (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "T3Bgps";
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Canal de gps",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }
}
