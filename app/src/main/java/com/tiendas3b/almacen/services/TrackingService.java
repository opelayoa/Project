package com.tiendas3b.almacen.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.tiendas3b.almacen.util.FileUtil;
import com.tiendas3b.almacen.util.RealService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TrackingService extends RealService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener/*, ResultCallback<Status>*/ {
    private static final String TAG = "TrackingService";
//    public static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";
//    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";
//    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    //    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 1;
//    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    private static final String GEOFENCE_NAME = "lastLocation";
    public static final float GEOFENCE_RADIUS_IN_METERS = 85;

    protected GoogleApiClient mGoogleApiClient;
    //    protected ArrayList<Geofence> mGeofenceList;
    private Geofence mGeofence;
    //    private boolean mGeofencesAdded;
    private PendingIntent mGeofencePendingIntent;
//    private List<String> list;
//    private SharedPreferences mSharedPreferences;

    public TrackingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        FileUtil.writeFile(TAG + ": onBind!!");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void verifyProviders() {
        int locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE, 0);
        if (locationMode == Settings.Secure.LOCATION_MODE_OFF) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss:SSS", Locale.getDefault());

    public void addGeofence() {
        if (!mGoogleApiClient.isConnected()) {
            FileUtil.writeFile(TAG + " add: !mGoogleApiClient.isConnected");
            return;
        }
//        verifyProviders();
        String date = df.format(new Date());
        FileUtil.writeFile(TAG + ": addGeofence " + date);
        try {
            LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, getGeofencingRequest(), getGeofencePendingIntent()).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {
                        FileUtil.writeFile(TAG + ": onResult-addGeofence");
                    } else {
                        FileUtil.writeFile(TAG + ": onResult Fail addGeofence");
                    }
                }
            });
        } catch (SecurityException securityException) {
            logSecurityException(securityException);
        }
    }

//    /**
//     * Removes geofences, which stops further notifications when the device enters or exits
//     * previously registered geofences.
//     */
//    public void removeAddGeofence(final boolean add) {
//        if (!mGoogleApiClient.isConnected()) {
//            FileUtil.writeFile(TAG + " rm: !mGoogleApiClient.isConnected");
//            return;
//        }
//        try {
//            FileUtil.writeFile(TAG + ": removeGeofence");
//            LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient, list).setResultCallback(new ResultCallback<Status>() {
//                @Override
//                public void onResult(@NonNull Status status) {
//                    if (add) addGeofence();
//                    if (status.isSuccess()) {
//                        FileUtil.writeFile(TAG + ": onResult-removeGeofence");
//                    } else {
//                        FileUtil.writeFile(TAG + ": onResult Fail removeGeofence");
//                    }
//                }
//            });
//        } catch (SecurityException securityException) {
//            logSecurityException(securityException);
//        }
//    }

    private void logSecurityException(SecurityException securityException) {
        //Log.e(TAG, "Invalid location permission. " + "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.addGeofence(mGeofence);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent == null) {
            Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
            // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
            // addGeofences() and removeGeofences().
            mGeofencePendingIntent = PendingIntent.getService(this, 2212, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return mGeofencePendingIntent;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        mGeofenceList = new ArrayList<>();
        mGeofencePendingIntent = null;
//        list = new ArrayList<>();
//        list.add(GEOFENCE_NAME);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        verifyProviders();
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        FileUtil.writeFile(TAG + ": no pinches mames!!!");
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        //Log.i(TAG, "Connected to GoogleApiClient");
        FileUtil.writeFile(TAG + ": onConnected");
        populateGeofenceList();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        FileUtil.writeFile(TAG + ": onConnectionFailed");
        //Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        //Log.i(TAG, "Connection suspended");
        FileUtil.writeFile(TAG + ": onConnectionSuspended!");
        // onConnected() will be called again automatically when the service reconnects
    }

//    /**
//     * Runs when the result of calling addGeofences() and removeGeofences() becomes available.
//     * Either method can complete successfully or with an error.
//     * <p/>
//     * Since this activity implements the {@link ResultCallback} interface, we are required to
//     * define this method.
//     *
//     * @param status The Status returned through a PendingIntent when addGeofences() or
//     *               removeGeofences() get called.
//     */
//    public void onResult(Status status) {
//        if (status.isSuccess()) {
//            FileUtil.writeFile(TAG + ": onResult-Success");
//            // Update state and save in shared preferences.
////            mGeofencesAdded = !mGeofencesAdded;
////            SharedPreferences.Editor editor = mSharedPreferences.edit();
////            editor.putBoolean(GEOFENCES_ADDED_KEY, mGeofencesAdded);
////            editor.apply();
//
//            // Update the UI. Adding geofences enables the Remove Geofences button, and removing
//            // geofences enables the Add Geofences button.
////            setButtonsEnabledState();
//
////            Toast.makeText(this, getString(mGeofencesAdded ? R.string.geofences_added : R.string.geofences_removed), Toast.LENGTH_SHORT).show();
//        } else {
//            FileUtil.writeFile(TAG + ": onResult Fail");
//            // Get the status code for the error and log it using a user-friendly message.
//            String errorMessage = GeofenceErrorMessages.getErrorString(this, status.getStatusCode());
//            Log.e(TAG, errorMessage);
//        }
//    }

    /**
     * This sample hard codes geofence data. A real app might dynamically create geofences based on
     * the user's location.
     */
    @SuppressWarnings("MissingPermission")
    public void populateGeofenceList() {
//        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (l == null) {
//            FileUtil.writeFile("!isMyServiceRunningTS");
            Intent i = new Intent(this, LocationService.class);
            startService(i);
//        } else {
//            //Log.i(TAG, "l != null");
//            FileUtil.writeFile(TAG + ": lat:" + l.getLatitude() + " lon: " + l.getLongitude());
//            setmGeofence(l.getLatitude(), l.getLongitude());
//            LocationUtil.setLastLocation(l);
//            addGeofence();
//        }
    }

//    public Geofence getmGeofence() {
//        return mGeofence;
//    }
//
//    public void setmGeofence(Geofence mGeofence) {
//        this.mGeofence = mGeofence;
//    }

    public void setmGeofence(double latitude, double longitude) {
        mGeofence = new Geofence.Builder().setRequestId(GEOFENCE_NAME).setCircularRegion(latitude, longitude, GEOFENCE_RADIUS_IN_METERS).setExpirationDuration(Geofence.NEVER_EXPIRE).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT).build();
    }
}
