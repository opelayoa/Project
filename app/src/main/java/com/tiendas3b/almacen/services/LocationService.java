package com.tiendas3b.almacen.services;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.db.dao.Message;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.FileUtil;
import com.tiendas3b.almacen.util.LocationUtil;
import com.tiendas3b.almacen.util.PermissionUtil;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
@SuppressWarnings("MissingPermission")
public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LocationService";
//    private static final String BEST_LOCATION = "ACTION_BEST_LOCATION";
//    private static final String EXTRA_LOCATION = "EXTRA_LOCATION";

    public static final String FUSED_PRIVIDER = "fused";
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    private static final long WAIT_TIME_INTERVAL = UPDATE_INTERVAL_IN_SECONDS * 2;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    public static final int FAST_CEILING_IN_SECONDS = 1;
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;
    private static final float DISPLACEMAENT = 2.0f;
    private static final long TIMEOUT_IN_MS = 60 * MILLISECONDS_PER_SECOND;

    private Tiendas3bClient httpService;
    //    private CountDownLatch doneSignal;
//    private static final int TOTAL = 1;
    private GlobalState mContext;

    protected GoogleApiClient mGoogleApiClient;
    //    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private boolean bfusedProvider = false;
    private LocationListener fusedListener;
    private Location mLocation;
    private boolean sendNow = false;

    private Location lastLocation;

//    public LocationService() {
//        super("LocationService");
//    }

    private static LocationService instance = null;
    private Handler handler;
    private Runnable callback;

    public static LocationService getInstance() {
        return instance;
    }

    public synchronized void setSendNow(boolean sendNow) {
        this.sendNow = sendNow;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        FileUtil.writeFile(TAG + ": onBind!!!");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = (GlobalState) getApplicationContext();
        fusedListener = new LocationListener() {
            @Override
            public synchronized void onLocationChanged(Location location) {
//                //Log.w("FusedLocationListener", "onLocationChanged" + location.getTime());
//                FileUtil.writeFile(TAG + ": " +mLocationRequest.);
                if (LocationUtil.isBetterLocation(location, mLocation)) {
                    ////Log.w("FusedLocationListener", "isBetterLocation");
                    mLocation = location;
                    if (sendNow) {
                        handler.removeCallbacks(callback);
                        sendNow();
                    }
                }
                ////Log.w("FusedLocationListener", "end");
            }
        };

        callback = new Runnable() {
            public void run() {
                FileUtil.writeFile("TIME!!!");
                sendNow();
            }
        };

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(DISPLACEMAENT);
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
        mLocationRequest.setMaxWaitTime(WAIT_TIME_INTERVAL);
    }

    private void sendNow() {
        sendNow = false;
        ////Log.w("FusedLocationListener", "sendNow");
        lastLocation = mLocation;
        LocationUtil.setLastLocation(lastLocation);
        FileUtil.writeFile(lastLocation.getLatitude() + "," + lastLocation.getLongitude() + "|", "roads.txt");
        handleActionBestLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestLocationFused();
        return START_STICKY;
    }

    private void handleActionBestLocation() {
//        bestLocation = getBestLocation();

//        FileUtil.writeFile(TAG + ": handleActionBestLocation");
        save();

        if (lastLocation.getSpeed() < 1.1F) {//TODO ver que pedo XD 4.2 es 15km/h
            FileUtil.writeFile(TAG + ": create geofence!!!");
            stopUpdates();
            createGeofencing();
            send();
            stopSelf();
        } else {
            FileUtil.writeFile(TAG + ": continue ->" + lastLocation.getSpeed() * 3.6F);
            send();
            setTimer();
        }
    }

    private void send() {
//        doneSignal = new CountDownLatch(TOTAL);
//        send();
//        try {
//            doneSignal.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    private void save() {
        ////Log.w(TAG, "save");
        Message m = new Message();
        m.setAccuracy(lastLocation.getAccuracy());
        m.setEmissionDateMillis(lastLocation.getTime());
        m.setLat(lastLocation.getLatitude());
        m.setLng(lastLocation.getLongitude());
        m.setProvider(lastLocation.getProvider());
        m.setSpeed(lastLocation.getSpeed() * 3.6f);
        m.setBearing(lastLocation.getBearing());
        IDatabaseManager db = new DatabaseManager(mContext);
        db.insertOrReplaceInTx(m);
        db.closeDbConnections();
    }

    private void createGeofencing() {
//        FileUtil.writeFile(TAG + ": createGeofencing");
        TrackingService ts = ((TrackingService) TrackingService.instance);
        ts.setmGeofence(lastLocation.getLatitude(), lastLocation.getLongitude());
//        ts.removeAddGeofence(true);
        ts.addGeofence();
    }

    private boolean servicesConnected() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
//            if(googleAPI.isUserResolvableError(result)) {
//                googleAPI.getErrorDialog(this, result,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            }
            FileUtil.writeFile(TAG + ": result != ConnectionResult.SUCCESS");
            return false;
        }

        return true;
    }

    public synchronized void requestLocationFused() {
        FileUtil.writeFile(TAG + ": requestLocationFused");
        if (mGoogleApiClient == null) {
            FileUtil.writeFile(TAG + ": mGoogleApiClient == null");
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        } else if (mGoogleApiClient.isConnected()) {
            FileUtil.writeFile(TAG + ": mGoogleApiClient.isConnected");
            if (!bfusedProvider) {
                FileUtil.writeFile(TAG + ": !bfusedProvider");
                startUpdates();
            }
        } else if (mGoogleApiClient.isConnecting()) {
            //Log.d(TAG, "mLocationClient.isConnecting");
        } else {
            mGoogleApiClient.connect();
        }
    }

    public void startUpdates() {
        if (PermissionUtil.permissionGranted(mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (servicesConnected()) {
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
//            if(checkLocationPermission()){
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, fusedListener);
                bfusedProvider = true;

                setTimer();
//                writeFile("requestFusedUpdates!");
//            }
            }
        } else {
//            PermissionUtil.showDialog((Activity) loginView, PermissionUtil.REQUEST_WRITE_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            startInstalledAppDetailsActivity();
        }
        FileUtil.writeFile(TAG + ": startUpdates");
    }

    private void setTimer() {
        handler = new Handler();
        handler.postDelayed(callback, TIMEOUT_IN_MS);
    }

    public void startInstalledAppDetailsActivity() {
        if (mContext == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + mContext.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        mContext.startActivity(i);
    }

    public void stopUpdates() {
        FileUtil.writeFile(TAG + ": stopUpdates");
        if (servicesConnected()) {
//            FileUtil.writeFile(TAG + ": servicesConnected");
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                FileUtil.writeFile(TAG + ": mGoogleApiClient != null && mGoogleApiClient.isConnected");
//                writeFile("\n" + sdf.format(new Date()) + "removeFusedUpdates!");
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, fusedListener);
            }
            bfusedProvider = false;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    @Override
    public void onDestroy() {
        FileUtil.writeFile(TAG + ": onDestroy");
        super.onDestroy();
//        stopUpdates();
        mGoogleApiClient.disconnect();
        mGoogleApiClient = null;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        FileUtil.writeFile(TAG + ": onConnected");
        startUpdates();
//        if (PermissionUtil.permissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION) /*|| PermissionUtil.permissionGranted(this, Manifest.permission.ACCESS_COARSE_LOCATION)*/) {
//            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        } else {
//            PermissionUtil.showDialog(activity, PermissionUtil.REQUEST_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
//        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        FileUtil.writeFile(TAG + ": onConnectionFailed!");
        //Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        ////Log.i(TAG, "Connection suspended");
        FileUtil.writeFile(TAG + ": onConnectionSuspended!");
        // onConnected() will be called again automatically when the service reconnects
    }

//    private void send() {
//        doneSignal.countDown();
//    }
}
