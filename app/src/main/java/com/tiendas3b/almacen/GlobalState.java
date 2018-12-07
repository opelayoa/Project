package com.tiendas3b.almacen;

import android.app.Application;

import com.tiendas3b.almacen.http.ServiceGenerator;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.shipment.http.ShipmentService;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.Preferences;

import butterknife.ButterKnife;

/**
 * Created by dfa on 17/02/2016.
 */
public class GlobalState extends Application {

    public static final String TAG = "Almacen";

    @Override
    public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(BuildConfig.DEBUG);
    }

    public Tiendas3bClient getHttpService() {
        String url = Constants.URL_VRP;
        return ServiceGenerator.createService(Tiendas3bClient.class, url);
    }

    public Tiendas3bClient getHttpServiceWithAuth() {
        Preferences p = new Preferences(this);
        String login = p.getSharedStringSafe(Preferences.KEY_LOGIN, "NO LOGIN");
        String password = p.getSharedStringSafe(Preferences.KEY_PASS, "NO PASS");
        String url = p.getSharedStringSafe(Preferences.KEY_URL, Constants.URL_LOCAL);
        return ServiceGenerator.createService(Tiendas3bClient.class, login, password, url);
    }

    public Tiendas3bClient getHttpServiceWithAuthTime() {
        Preferences p = new Preferences(this);
        String login = p.getSharedStringSafe(Preferences.KEY_LOGIN, "NO LOGIN");
        String password = p.getSharedStringSafe(Preferences.KEY_PASS, "NO PASS");
        String url = p.getSharedStringSafe(Preferences.KEY_URL, Constants.URL_LOCAL);
        return ServiceGenerator.createServiceTime(Tiendas3bClient.class, login, password, url);
    }

    public Tiendas3bClient getHttpServiceWithAuthTimeVpr() {
        return ServiceGenerator.createServiceTime(Tiendas3bClient.class, null, null, Constants.URL_VRP);
    }

    public Tiendas3bClient getHttpServiceWithAuthTimeGsonTime() {
        Preferences p = new Preferences(this);
        String login = p.getSharedStringSafe(Preferences.KEY_LOGIN, "NO LOGIN");
        String password = p.getSharedStringSafe(Preferences.KEY_PASS, "NO PASS");
        String url = p.getSharedStringSafe(Preferences.KEY_URL, Constants.URL_LOCAL);
        return ServiceGenerator.createServiceTimeGsonTime(Tiendas3bClient.class, login, password, url);
    }

    public ShipmentService getShipmentHttpService() {
        return ServiceGenerator.createShipmentService(ShipmentService.class, Constants.URL_SHIPMENT);
    }

    public long getRegion() {
        Preferences p = new Preferences(this);
        String region = p.getSharedStringSafe(Preferences.KEY_REGION, "0");
        return Long.valueOf(region);
    }

    public String getName() {
        Preferences p = new Preferences(this);
        return p.getSharedStringSafe(Preferences.KEY_NAME, "N/A");
    }

    public long getUserId() {
        Preferences p = new Preferences(this);
        String userId = p.getSharedStringSafe(Preferences.KEY_USER_ID, "0");
        return Long.valueOf(userId);
    }
}
