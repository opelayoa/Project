package com.tiendas3b.almacen.services;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.MainActivity;
import com.tiendas3b.almacen.geofencing.GeofenceErrorMessages;
import com.tiendas3b.almacen.util.FileUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("MissingPermission")
public class GeofenceTransitionsIntentService extends IntentService /*implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener*/ {

    //    public static final String FUSED_PRIVIDER = "fused";
    protected static final String TAG = "GeofenceTransitionsIS";
//    public static final int MILLISECONDS_PER_SECOND = 1000;
//    public static final int UPDATE_INTERVAL_IN_SECONDS = 3;
//    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
//    public static final int FAST_CEILING_IN_SECONDS = 1;
//    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;
//    private static final float DISPLACEMAENT = 2.0f;

//    private Context mContext;
//    private IDatabaseManager db;
//    protected GoogleApiClient mGoogleApiClient;
////    private LocationManager mLocationManager;
//    private LocationRequest mLocationRequest;
//    private boolean bfusedProvider = false;
//    private LocationListener fusedListener;
//    private Location mLastLocation;
//    private Location mLocation;
//    private boolean sendNow = false;

//    private static GeofenceTransitionsIntentService instance = null;
//
//    public static GeofenceTransitionsIntentService getInstance() {
//        return instance;
//    }

//    public synchronized void setSendNow(boolean sendNow) {
//        this.sendNow = sendNow;
//    }

    public GeofenceTransitionsIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        mContext = getApplicationContext();
//        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

//        Location l = new Location("");
//        mLastLocation = db.

//        buildGoogleApiClient();
//        mGoogleApiClient.connect();
//        fusedListener = new com.google.android.gms.location.LocationListener() {
//            @Override
//            public synchronized void onLocationChanged(Location location) {
//                Log.w("FusedLocationListener", "onLocationChanged" + location.getTime());
//                FileUtil.writeFile(TAG + ": onLocationChanged");
//                if (LocationUtil.isBetterLocation(location, mLocation)) {
//                    mLocation = location;
//                    if (sendNow) {
//                        LocationService.startActionBestLocation(mContext, mLocation);
//                    }
//                }
//            }
//        };
    }

    private static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss:SSS", Locale.getDefault());

    /**
     * Handles incoming intents.
     *
     * @param intent sent by Location Services. This Intent is provided to Location
     *               Services (inside a PendingIntent) when addGeofences() is called.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        FileUtil.writeFile(TAG + ": onHandleIntent");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this, geofencingEvent.getErrorCode());
            FileUtil.writeFile(TAG + ": " + errorMessage);
            //Log.e(TAG, errorMessage);
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            String date = df.format(new Date());
            FileUtil.writeFile(TAG + " : Exit " + date);
//            sendNotification("Exit");
//            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
//            String geofenceTransitionDetails = getGeofenceTransitionDetails(this, geofenceTransition, triggeringGeofences);


            findBestLocation();


//            sendNotification(geofenceTransitionDetails);
//            Log.i(TAG, geofenceTransitionDetails);
        } else {
            FileUtil.writeFile(TAG + ": NO Exit");
            //Log.e(TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }
        FileUtil.writeFile(TAG + ": End");
    }

    private void findBestLocation() {
        if (isMyServiceRunning(LocationService.class)) {
            FileUtil.writeFile("isMyServiceRunning");
        } else {
            FileUtil.writeFile("!isMyServiceRunning");
            Intent i = new Intent(this, LocationService.class);
            startService(i);
//        requestLocationFused(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, DISPLACEMAENT);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

//    private boolean servicesConnected() {
//        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
//        int result = googleAPI.isGooglePlayServicesAvailable(this);
//        if (result != ConnectionResult.SUCCESS) {
////            if(googleAPI.isUserResolvableError(result)) {
////                googleAPI.getErrorDialog(this, result,
////                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
////            }
//            FileUtil.writeFile(TAG + ": result != ConnectionResult.SUCCESS");
//            return false;
//        }
//
//        return true;
//    }
//
//    public synchronized void requestLocationFused(int priority, float displacement) {
//        FileUtil.writeFile(TAG + ": requestLocationFused");
//        if (mGoogleApiClient == null) {
//            FileUtil.writeFile(TAG + ": mGoogleApiClient == null");
//            buildGoogleApiClient();
//            mGoogleApiClient.connect();
//        } else if (mGoogleApiClient.isConnected()) {
//            FileUtil.writeFile(TAG + ": mGoogleApiClient.isConnected");
//            if (!bfusedProvider) {
//                FileUtil.writeFile(TAG + ": !bfusedProvider");
//                startUpdates(priority, displacement);
//            }
//        } else if (mGoogleApiClient.isConnecting()) {
//            Log.d(TAG, "mLocationClient.isConnecting");
//        } else {
//            mGoogleApiClient.connect();
//        }
//    }
//
//    public void startUpdates(int priority, float displacement) {
//        FileUtil.writeFile(TAG + ": startUpdates");
//        if (servicesConnected()) {
////            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
//            mLocationRequest = LocationRequest.create();
//            mLocationRequest.setPriority(priority);
//            mLocationRequest.setSmallestDisplacement(displacement);
//            mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
//            mLocationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
////            if(checkLocationPermission()){
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, fusedListener);
//            bfusedProvider = true;
////                writeFile("requestFusedUpdates!");
////            }
//
//        }
//    }
//
//    public void stopUpdates() {
//        FileUtil.writeFile(TAG + ": stopUpdates");
//        if (servicesConnected()) {
//            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
////                writeFile("\n" + sdf.format(new Date()) + "removeFusedUpdates!");
//                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, fusedListener);
//            }
//            bfusedProvider = false;
//        }
//    }
//
//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
//    }
//
//    @Override
//    public void onDestroy() {
//        FileUtil.writeFile(TAG + ": onDestroy");
//        super.onDestroy();
//        stopUpdates();
//        mGoogleApiClient.disconnect();
//        mGoogleApiClient = null;
//    }
//
//    @Override
//    public void onConnected(Bundle connectionHint) {
//        FileUtil.writeFile(TAG + ": onConnected");
//        Log.i(TAG, "Connected to GoogleApiClient");
//        startUpdates(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, DISPLACEMAENT);
////        if (PermissionUtil.permissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION) /*|| PermissionUtil.permissionGranted(this, Manifest.permission.ACCESS_COARSE_LOCATION)*/) {
////            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
////        } else {
////            PermissionUtil.showDialog(activity, PermissionUtil.REQUEST_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
////        }
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
//        // onConnectionFailed.
//        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
//    }
//
//    @Override
//    public void onConnectionSuspended(int cause) {
//        // The connection to Google Play services was lost for some reason.
//        Log.i(TAG, "Connection suspended");
//
//        // onConnected() will be called again automatically when the service reconnects
//    }

//    /**
//     * Gets transition details and returns them as a formatted string.
//     *
//     * @param context             The app context.
//     * @param geofenceTransition  The ID of the geofence transition.
//     * @param triggeringGeofences The geofence(s) triggered.
//     * @return The transition details formatted as String.
//     */
//    private String getGeofenceTransitionDetails(Context context, int geofenceTransition, List<Geofence> triggeringGeofences) {
//
//        String geofenceTransitionString = getTransitionString(geofenceTransition);
//
//        // Get the Ids of each geofence that was triggered.
//        ArrayList triggeringGeofencesIdsList = new ArrayList();
//        for (Geofence geofence : triggeringGeofences) {
//            triggeringGeofencesIdsList.add(geofence.getRequestId());
//        }
//        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);
//
//        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
//    }

    /**
     * Posts a notification in the notification bar when a transition is detected.
     * If the user clicks the notification, control goes to the MainActivity.
     */
    private void sendNotification(String notificationDetails) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        builder.setSmallIcon(R.drawable.ic_date_24dp)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_date_24dp)).setColor(Color.RED).setContentTitle(notificationDetails).setContentText(getString(R.string.geofence_transition_notification_text)).setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }

//    /**
//     * Maps geofence transition types to their human-readable equivalents.
//     *
//     * @param transitionType A transition type constant defined in Geofence
//     * @return A String indicating the type of transition
//     */
//    private String getTransitionString(int transitionType) {
//        switch (transitionType) {
//            case Geofence.GEOFENCE_TRANSITION_ENTER:
//                return getString(R.string.geofence_transition_entered);
//            case Geofence.GEOFENCE_TRANSITION_EXIT:
//                return getString(R.string.geofence_transition_exited);
//            default:
//                return getString(R.string.unknown_geofence_transition);
//        }
//    }
}