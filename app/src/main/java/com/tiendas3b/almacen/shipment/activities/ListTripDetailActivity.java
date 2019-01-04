package com.tiendas3b.almacen.shipment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ListView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.shipment.adapters.TripDetailAdapter;
import com.tiendas3b.almacen.shipment.base.GPSBaseActivity;
import com.tiendas3b.almacen.shipment.presenters.TripDetailPresenter;
import com.tiendas3b.almacen.shipment.presenters.TripDetailPresenterImpl;
import com.tiendas3b.almacen.shipment.util.DialogFactory;
import com.tiendas3b.almacen.shipment.views.ActivitySenderListener;
import com.tiendas3b.almacen.shipment.views.TripDetailView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListTripDetailActivity extends GPSBaseActivity implements TripDetailView, ActivitySenderListener {

    private TripDetailPresenter tripDetailPresenter;
    private List<TripDetail> tripDetails;
    private ListView listView;


    private AlertDialog progressDialog;

    @BindView(R.id.startTrip)
    Button startTrip;

    private long tripId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_trips);
        ButterKnife.bind(this);

        Long data = getIntent().getExtras().getLong("tripId");
        if (data == null) {
            onBackPressed();
        } else {
            this.tripId = data;
        }

        configureToolbar();

        progressDialog = DialogFactory.getProgressDialog(this, "Enviando actividad");

        this.listView = findViewById(R.id.trips);
        // TODO: Obtención del ID del camión seleccionado
        tripDetailPresenter = new TripDetailPresenterImpl(getApplicationContext(), this);
        tripDetailPresenter.getTrips(this.tripId);

        startTrip.setVisibility(Button.VISIBLE);
        startTrip.setOnClickListener(view -> {
            tripDetailPresenter.startTrip(tripId, getLocation());
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    protected void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Viaje " + tripId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void setTripDetailSuccess(List<TripDetail> list) {
        this.tripDetails = list;
        TripDetailAdapter tripDetailAdapter = new TripDetailAdapter(R.layout.row_shipment_trip_detail, tripDetails, this);
        listView.setAdapter(tripDetailAdapter);
    }

    @Override
    public void startTrip() {
        Bundle bundle = new Bundle();
        bundle.putLong("tripId", tripId);
        Intent intent = new Intent(ListTripDetailActivity.this, CheckListActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void startProgressActivityUpload(String title) {
        progressDialog = DialogFactory.getProgressDialog(this, title);
        progressDialog.show();
    }

    @Override
    public void stopProgressActivityUpload() {
        progressDialog.dismiss();
    }

}
