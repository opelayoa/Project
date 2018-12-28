package com.tiendas3b.almacen.shipment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Trip;
import com.tiendas3b.almacen.shipment.adapters.TripAdapter;
import com.tiendas3b.almacen.shipment.base.GPSBaseActivity;
import com.tiendas3b.almacen.shipment.presenters.TripPresenter;
import com.tiendas3b.almacen.shipment.presenters.TripPresenterImpl;
import com.tiendas3b.almacen.shipment.services.GPSService;
import com.tiendas3b.almacen.shipment.util.AlertDialogListener;
import com.tiendas3b.almacen.shipment.util.DialogFactory;
import com.tiendas3b.almacen.shipment.views.TripView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListTripActivity extends AppCompatActivity implements TripView, AlertDialogListener, AdapterView.OnItemClickListener {

    private TripPresenter tripPresenter;
    private List<Trip> trips;
    private ListView listView;

    @BindView(R.id.startTrip)
    Button button;

    private AlertDialog errorDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_trips);
        ButterKnife.bind(this);

        configureToolbar();

        this.listView = findViewById(R.id.trips);
        this.listView.setOnItemClickListener(this);

        tripPresenter = new TripPresenterImpl(getApplicationContext(), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tripPresenter.getTrips();

        if (GPSBaseActivity.isServiceRunning(this, GPSService.class)) {
            Intent gpsService = new Intent(this, GPSService.class);
            stopService(gpsService);
        }
    }

    @Override
    public void setTripSuccess(List<Trip> list) {
        this.trips = list;
        TripAdapter tripAdapter = new TripAdapter(R.layout.row_shipment_trip, trips, this);
        listView.setAdapter(tripAdapter);
    }

    @Override
    public void showError(String message) {
        errorDialog = DialogFactory.getErrorDialogListener(this, message, this);
        errorDialog.show();
    }

    @Override
    public void showDetails(long tripId) {
        Bundle bundle = new Bundle();
        bundle.putLong("tripId", tripId);

        Intent service = new Intent(ListTripActivity.this, GPSService.class);
        startService(service);

        Intent intent = new Intent(this, ListTripDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent, bundle);
    }

    @Override
    public void showButton() {
        button.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        tripPresenter.validateSelectedTrip(trips.get(position).getSequence(), trips.get(position).getStatus());
    }

    protected void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Viajes");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @OnClick(R.id.startTrip)
    void sendInfo() {
        this.tripPresenter.showInfo();
    }

    @Override
    public void execute() {
        finish();
    }
}
