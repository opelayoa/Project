package com.tiendas3b.almacen.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class RealService extends Service {
    private static final String TAG = "RealService";

    public RealService() {
    }

    public static RealService instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (startService(new Intent(this, FakeService.class)) == null)
            throw new RuntimeException("Couldn't find " + FakeService.class.getSimpleName());
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy!!");
        super.onDestroy();
        instance = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind!!");
        return null;
    }
}
