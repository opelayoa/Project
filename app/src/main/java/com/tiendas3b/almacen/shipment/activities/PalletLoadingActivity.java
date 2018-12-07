package com.tiendas3b.almacen.shipment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.shipment.adapters.TripDetailAdapter;
import com.tiendas3b.almacen.shipment.presenters.PalletCounterPresenter;
import com.tiendas3b.almacen.shipment.presenters.PalletLoadingPresenter;
import com.tiendas3b.almacen.shipment.presenters.PalletLoadingPresenterImpl;
import com.tiendas3b.almacen.shipment.presenters.TripDetailPresenter;
import com.tiendas3b.almacen.shipment.presenters.TripDetailPresenterImpl;
import com.tiendas3b.almacen.shipment.util.DialogFactory;
import com.tiendas3b.almacen.shipment.views.PalletLoadingView;
import com.tiendas3b.almacen.shipment.views.TripDetailView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PalletLoadingActivity extends AppCompatActivity implements PalletLoadingView, AdapterView.OnItemClickListener {

    private PalletLoadingPresenter palletLoadingPresenter;
    private List<TripDetail> tripDetails;
    private ListView listView;

    private long tripId;

    int counter = 0;

    @BindView(R.id.startTrip)
    Button next;

    private AlertDialog errorDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_trips);
        ButterKnife.bind(this);
        configureToolbar();

        Long data = getIntent().getExtras().getLong("tripId");
        if (data == null) {
            onBackPressed();
        } else {
            this.tripId = data;
        }

        this.listView = findViewById(R.id.trips);
        this.listView.setOnItemClickListener(this);
        // TODO: Obtención del ID del camión seleccionado
        palletLoadingPresenter = new PalletLoadingPresenterImpl(getApplicationContext(), this);
        palletLoadingPresenter.getTrips(tripId);

    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        palletLoadingPresenter.getTrips(tripId);
        palletLoadingPresenter.validateTotalStoresLoaded(tripId);
    }

    protected void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Carga de Tarimas");
        setSupportActionBar(toolbar);
    }

    @Override
    public void setTripDetailSuccess(List<TripDetail> list) {
        Collections.reverse(list);
        this.tripDetails = list;
        TripDetailAdapter tripDetailAdapter = new TripDetailAdapter(R.layout.row_shipment_trip_detail, this.tripDetails, this);
        listView.setAdapter(tripDetailAdapter);
    }

    @Override
    public void showError(String error) {
        errorDialog = DialogFactory.getErrorDialog(this, error);
        errorDialog.show();
    }

    @Override
    public void openCounter(long tripDetailId) {
        Bundle bundle = new Bundle();
        bundle.putLong("tripDetailId", tripDetailId);
        Intent intent = new Intent(PalletLoadingActivity.this, PalletCounterActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void showNext() {
        next.setText("Siguiente");
        next.setVisibility(Button.VISIBLE);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        palletLoadingPresenter.validateStore(id, tripId);
    }

    @OnClick(R.id.startTrip)
    public void next() {

        Bundle bundle = new Bundle();
        bundle.putLong("tripId", tripId);
        Intent intent = new Intent(PalletLoadingActivity.this, TrackSelectionActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
