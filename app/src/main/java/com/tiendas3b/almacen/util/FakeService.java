package com.tiendas3b.almacen.util;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FakeService extends Service {

    private static final int NOTIFICATION_ID = 1987;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (RealService.instance == null)
            throw new RuntimeException(RealService.class.getSimpleName() + " not running");

        //Set both services to foreground using the same notification id, resulting in just one notification
        startForeground(RealService.instance);
        startForeground(this);

        //Cancel this service's notification, resulting in zero notifications
        stopForeground(true);

        //Stop this service so we don't waste RAM
        stopSelf();

        return START_NOT_STICKY;
    }

    private static void startForeground(Service service) {
        Notification notification = new Notification.Builder(service).build();
        service.startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
