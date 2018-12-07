package com.tiendas3b.almacen.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.satsuware.usefulviews.LabelledSpinner;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.GenericSpinnerAdapter;
import com.tiendas3b.almacen.db.dao.Truck;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.CaptureDevice;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.Preferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingActivity extends AppCompatActivity {

    @BindView(R.id.txtName)
    EditText txtName;
    @BindView(R.id.txtMsisdn)
    EditText txtMsisdn;
    @BindView(R.id.txtImei)
    EditText txtImei;
    @BindView(R.id.txtBrand)
    EditText txtBrand;
    @BindView(R.id.txtModel)
    EditText txtModel;
    @BindView(R.id.spnTrucks)
    LabelledSpinner spnTrucks;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    MenuItem btnSave;

    protected GlobalState mContext;
    protected IDatabaseManager db;
    private long truckId;
    private String name;
    private String imei;
    private String msisdn;
    private String brand;
    private String model;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, TrackingActivity.class);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tracking, menu);
        btnSave = menu.findItem(R.id.action_send);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_send:
                sendInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendInfo() {
        btnSave.setVisible(false);
        progressBar.setVisibility(View.VISIBLE);
        if (isValid()) {
            savePreferences();
            Toast.makeText(mContext, "Guardado", Toast.LENGTH_SHORT).show();//quitar cuando ya exista el ws
//            CaptureDevice captureRoadmap = createToSend();
//            sendRequest(captureRoadmap);
////            save();
////            handleTrackingservice();
            setResultOk();
            finish();
        } else {
            progressBar.setVisibility(View.GONE);
            btnSave.setVisible(true);
        }
    }

    protected void setResultOk() {
//        Intent data = new Intent();
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, null);
        } else {
            getParent().setResult(Activity.RESULT_OK, null);
        }
    }

    private void savePreferences() {
        Preferences p = new Preferences(this);
        p.setSharedStringSafe(Preferences.KEY_TRUCK_ID, String.valueOf(truckId));
    }

    private void sendRequest(CaptureDevice captureRoadmap) {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.insertDevice(captureRoadmap).enqueue(new Callback<GeneralResponseDTO>() {
            @Override
            public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                GeneralResponseDTO gr = response.body();
                if (response.isSuccessful() && gr != null && gr.getCode() == 1L) {
//                    updateSync();
//                    startActivityForResult(new Intent(mContext, DecreaseReportActivity.class).putExtra("t", Constants.DP)
//                            .putExtra("f", gr.getDescription()).putExtra("d", (Serializable) toSend), -1);
                    finish();
                } else {
//                    Log.e(TAG, "send error gr!");
                }
                progressBar.setVisibility(View.INVISIBLE);
                btnSave.setVisible(true);
            }

            @Override
            public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                btnSave.setVisible(true);
//                showEmptyView();
//                Log.e(TAG, "send error!");
            }
        });
    }

    private boolean isValid() {
        truckId = ((Truck) spnTrucks.getSpinner().getSelectedItem()).getId();

//        name = txtName.getText().toString();
//        if ("".equals(name)) {
//            Toast.makeText(this, "Nombre vacio", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        msisdn = txtMsisdn.getText().toString();
//        if ("".equals(msisdn)) {
//            Toast.makeText(this, "Teléfono vacio", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (msisdn.length() < 10) {
//            Toast.makeText(this, "Teléfono no válido", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        imei = txtImei.getText().toString();
//        brand = txtBrand.getText().toString();
//        model = txtModel.getText().toString();
////        if ("".equals(imei)) {
////            Toast.makeText(this, "IMEI vacio", Toast.LENGTH_SHORT).show();
////            return false;
////        }

        return true;
    }

    private CaptureDevice createToSend() {
        CaptureDevice cr = new CaptureDevice();
        cr.setRegionId(mContext.getRegion());
        cr.setName(name);
        cr.setMsisdn(msisdn);
        cr.setImei(imei);
        cr.setTruckId(truckId);
        cr.setBrand(brand);
        cr.setModel(model);
        return cr;
    }

    @Override
    protected void onRestart() {
        db = new DatabaseManager(this);
        super.onRestart();
    }

    @Override
    protected void onStop() {
        //no se todavia si poner los items = null...
        if (db != null) db.closeDbConnections();
        super.onStop();
    }

    @Override
    protected void onResume() {
        db = DatabaseManager.getInstance(this);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = (GlobalState) getApplicationContext();
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        db = DatabaseManager.getInstance(this);

        txtBrand.setText(Build.MANUFACTURER);
        txtModel.setText(Build.MODEL);

        List<Truck> trucks = db.listActiveTrucks(mContext.getRegion());
        GenericSpinnerAdapter<Truck> truckAdapter = new GenericSpinnerAdapter<>(this, R.layout.spinner_item_table, trucks);
        spnTrucks.setCustomAdapter(truckAdapter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String imei = mngr.getDeviceId();
            txtImei.setText(imei);
            txtName.setText(String.format("%s_%s", Build.MODEL, imei));

//        String imsi = mngr.getSubscriberId();//numero serie sim
            String msisdn = mngr.getLine1Number();
            txtMsisdn.setText(msisdn == null ? "" : msisdn);
        } else {

        }

    }

}
