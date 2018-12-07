package com.tiendas3b.almacen.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by dfa on 27/06/2016.
 */
public class PermissionUtil {

    public static final int REQUEST_WRITE_STORAGE = 1;
    public static final int REQUEST_FINE_LOCATION = 2;

//    public static String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    public static boolean permissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void showDialog(Activity activity, int request, String... permission) {
        ActivityCompat.requestPermissions(activity, permission, request);
    }

    public static void shouldShowDialog(Activity activity, int request, String permission) {
        if (!permissionGranted(activity, permission)) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, request);
        }
    }
}
