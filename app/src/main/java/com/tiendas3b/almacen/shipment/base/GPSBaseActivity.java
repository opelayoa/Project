package com.tiendas3b.almacen.shipment.base;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tiendas3b.almacen.shipment.util.AlertDialogListener;
import com.tiendas3b.almacen.shipment.util.DialogFactory;

public class GPSBaseActivity extends AppCompatActivity implements LocationListener, AlertDialogListener {

    private LocationManager locationManager = null;
    private HandlerThread locationHandlerThread = null;
    private Looper locationHandlerLooper = null;

    private static final int TIME = 500;

    protected Location location;

    private AlertDialog progressDialog;
    private AlertDialog errorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();

        validateGPS();


        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME, 0, this, locationHandlerLooper);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME, 0, this, locationHandlerLooper);
        } catch (SecurityException e) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            // The app doesn't have the correct permissions
        }

        locationHandlerLooper = null;
        locationHandlerThread.quitSafely();
        locationHandlerThread = null;


        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
        progressDialog.dismiss();

        this.location = location;

        if (isBetterLocation(location, this.location)) {
            this.location = location;
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            return true;
        }

        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TIME;
        boolean isSignificantlyOlder = timeDelta < -TIME;
        boolean isNewer = timeDelta > 0;

        if (isSignificantlyNewer) {
            return true;
        } else if (isSignificantlyOlder) {
            return false;
        }

        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void execute() {
        try {
            validateGPS();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME, 0, this, locationHandlerLooper);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME, 0, this, locationHandlerLooper);
        } catch (SecurityException e) {

        }
    }


    private void validateGPS() {



        if (locationManager == null || locationHandlerThread == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationHandlerThread = new HandlerThread("locationHandlerThread");

            locationHandlerThread.start();
            locationHandlerLooper = locationHandlerThread.getLooper();
        }




        try {
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (progressDialog == null) {
                progressDialog = DialogFactory.getProgressDialog(this, "Sincronizando GPS...");
                progressDialog.show();
            } else {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }



            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                progressDialog.dismiss();
                errorDialog = DialogFactory.getErrorDialogListener(this, "Active GPS de su dispositivo", this);
                errorDialog.show();
                return;
            }

        } catch (SecurityException e) {

        }
    }
}
