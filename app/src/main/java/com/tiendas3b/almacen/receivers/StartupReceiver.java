package com.tiendas3b.almacen.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tiendas3b.almacen.util.FileUtil;

public class StartupReceiver extends BroadcastReceiver {
    public StartupReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        FileUtil.writeFile("StartupReceiver!!!");
    }
}
