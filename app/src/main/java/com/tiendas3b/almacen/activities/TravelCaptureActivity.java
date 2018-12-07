package com.tiendas3b.almacen.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.satsuware.usefulviews.LabelledSpinner;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.AutoCompleteAdapter;
import com.tiendas3b.almacen.adapters.GenericSpinnerAdapter;
import com.tiendas3b.almacen.db.dao.Driver;
import com.tiendas3b.almacen.db.dao.Roadmap;
import com.tiendas3b.almacen.db.dao.Truck;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.DialogUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TravelCaptureActivity extends SaveMenuActivity {

//    private static final String TAG = "TravelCaptureActivity";
    @BindView(R.id.spnTrucks)
    LabelledSpinner spnTrucks;
    @BindView(R.id.spnTravel)
    LabelledSpinner spnTravel;
    @BindView(R.id.txtDriver)
    AutoCompleteTextView txtDriver;
    @BindView(R.id.txtRegion)
    EditText txtRegion;
    @BindView(R.id.txtDate)
    EditText txtDate;

    private long truckId;
    private int travelNum;
    private Long driverId;
    private long regionId;
    private String dateStr;
    private int lastTravel;

    @Override
    protected void saveData() {
        if(isValid()){
            Roadmap roadmap = db.findRoadmap(regionId, driverId, truckId, travelNum, dateStr);
            if(roadmap == null) {
                save();
                setResultOk();
            } else {
                showWarningDialog();
            }
        }
    }

    private void save() {
        Roadmap roadmap = new Roadmap();
        roadmap.setRegionId(regionId);
        roadmap.setDate(dateStr);
        roadmap.setTruckId(truckId);
        roadmap.setTravel(travelNum);
        roadmap.setDriverId(driverId);
        roadmap.setSync(false);
        db.insertOrReplaceInTx(roadmap);
    }

//    @Override
    protected boolean isValid() {
        String driverDesc = txtDriver.getText().toString();
        if("".equals(driverDesc)){
            Toast.makeText(this, "Operador vacío", Toast.LENGTH_SHORT).show();
            return false;
        }
        Driver driver = db.getByDescription(driverDesc, Driver.class);
        if(driver == null){
            Toast.makeText(this, "Operador no válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        driverId = driver.getId();
        travelNum = Integer.parseInt((String)spnTravel.getSpinner().getSelectedItem());
        return true;
    }

//    @Override
    protected void showWarningDialog() {
        DialogUtil.showAlertDialog(TravelCaptureActivity.this, R.string.travel_warning_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                save();
                setResultOk();
            }
        });
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_capture);
        mContext = (GlobalState) getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    protected void init() {
        ButterKnife.bind(TravelCaptureActivity.this);
        db = new DatabaseManager(mContext);

        regionId = mContext.getRegion();
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        setLastTravel();

        Truck truck = db.getById(truckId, Truck.class);
        List<Truck> l = new ArrayList<>();
        l.add(truck);
        GenericSpinnerAdapter<Truck> truckAdapter = new GenericSpinnerAdapter<>(this, R.layout.spinner_item_table, l);
        spnTrucks.setCustomAdapter(truckAdapter);
        spnTrucks.getSpinner().setEnabled(false);

        ArrayAdapter<CharSequence> numTravelAdapter = ArrayAdapter.createFromResource(this,
                R.array.num_travel_array, R.layout.spinner_item_table);
        numTravelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTravel.setCustomAdapter(numTravelAdapter);
        spnTravel.setSelection(lastTravel);
        spnTravel.getSpinner().setEnabled(false);

        List<Driver> stores = db.listActiveDrivers(regionId);
        AutoCompleteAdapter<Driver> originAdapter = new AutoCompleteAdapter<>(this, R.layout.row_item_autocomplete, stores);
        txtDriver.setAdapter(originAdapter);
        txtDriver.setThreshold(2);

        txtRegion.setText(String.valueOf(regionId));
        dateStr = DateUtil.getDateStr(new Date());
        txtDate.setText(dateStr);
    }

    private void setLastTravel() {
        Intent i= getIntent();
        lastTravel = i.getIntExtra(RoadmapActivity.EXTRA_LAST_TRAVEL, 0);
        truckId = i.getLongExtra(RoadmapActivity.EXTRA_TRUCK_ID, -1L);
    }

    @Override
    public void onBackPressed() {
        showExitWithoutSaveDialog(TravelCaptureActivity.this);
    }

}
