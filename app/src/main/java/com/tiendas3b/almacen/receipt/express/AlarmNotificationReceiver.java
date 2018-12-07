package com.tiendas3b.almacen.receipt.express;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.tiendas3b.almacen.R;

public class AlarmNotificationReceiver extends BroadcastReceiver {

    public static final String EXTRA_ODC = "EXTRA_ODC";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "unChanelXD");

        int odc = intent.getIntExtra(EXTRA_ODC, -1);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Recibo pendiente")
                .setContentText("ODC:" + odc)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
//                .setContentInfo("Info")
        ;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(odc, builder.build());
    }
}
