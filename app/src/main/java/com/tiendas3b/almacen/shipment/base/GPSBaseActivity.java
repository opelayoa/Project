package com.tiendas3b.almacen.shipment.base;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tiendas3b.almacen.shipment.services.GPSService;
import com.tiendas3b.almacen.shipment.util.DialogFactory;

import java.util.List;

public class GPSBaseActivity extends AppCompatActivity {


    private Location location;

    private BroadcastReceiver broadcastReceiver;


    private AlertDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress = DialogFactory.getProgressDialog(this, "Sincronizando GPS");
        progress.show();

        runtimePermissions();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!isServiceRunning(this, GPSService.class)) {
            Intent intent = new Intent(GPSBaseActivity.this, GPSService.class);
            startService(intent);
        }

        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getExtras().get("location") != null) {
                        setLocation((Location) intent.getExtras().get("location"));
                    }
                }
            };
        }

        registerReceiver(broadcastReceiver, new IntentFilter(GPSService.LOCATION_UPDATE));

        broadCastLocation();
    }

    private void setLocation(Location location) {
        progress.dismiss();
        this.location = location;
    }

    protected Location getLocation() {
        return this.location;
    }

    private void broadCastLocation() {
        Intent intent = new Intent(GPSService.REQUEST_LOCATION);
        sendBroadcast(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            } else {
                runtimePermissions();
            }
        }
    }

    private boolean runtimePermissions() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        return false;
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            Log.d("T3st", String.format("Service:%s", runningServiceInfo.service.getClassName()));
            if (runningServiceInfo.service.getClassName().equals(serviceClass.getName())) {
                return true;
            }
        }
        return false;
    }

}
