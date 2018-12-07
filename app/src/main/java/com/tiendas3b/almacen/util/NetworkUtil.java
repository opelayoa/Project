package com.tiendas3b.almacen.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.tiendas3b.almacen.R;

/**
 * Created by dfa on 22/02/2016.
 */
public class NetworkUtil {

    private static NetworkInfo getActiveNetworkInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    public static boolean isConnectedWithToast(final Context context) {
        final boolean isConnected = isConnected(context);
        if (!isConnected) {
            Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
        return isConnected;
    }

    public static boolean isConnected(final Context context) {
        final NetworkInfo network = getActiveNetworkInfo(context);
        return network != null && network.isConnected();
    }

    public static boolean isConnectedToWifi(final Context context) {
        NetworkInfo activeNetwork = getActiveNetworkInfo(context);
        return activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnected();
    }

}
