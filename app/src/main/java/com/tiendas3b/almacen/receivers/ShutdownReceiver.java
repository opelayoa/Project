package com.tiendas3b.almacen.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tiendas3b.almacen.util.FileUtil;

public class ShutdownReceiver extends BroadcastReceiver {
    public ShutdownReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        FileUtil.writeFile("ShutdownReceiver!!!");
    }
}
