package com.tiendas3b.almacen.shipment.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Trip;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.shipment.base.GPSBaseActivity;
import com.tiendas3b.almacen.shipment.presenters.PalletCounterPresenter;
import com.tiendas3b.almacen.shipment.presenters.PalletCounterPresenterImpl;
import com.tiendas3b.almacen.shipment.views.PalletCounterView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;

public class PalletCounterActivity extends GPSBaseActivity implements PalletCounterView {

    @BindView(R.id.btnCounter)
    Button btnCounter;

    @BindView(R.id.palletCounter)
    TextView palletCounter;

    @BindView(R.id.palletTotal)
    TextView palletTotal;

    @BindView(R.id.storeDescription)
    TextView storeDescription;

    PalletCounterPresenter palletCounterPresenter;
    long tripDetailId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_counter);
        ButterKnife.bind(this);
        configureToolbar();

        Long data = getIntent().getExtras().getLong("tripDetailId");
        if (data == null) {
            onBackPressed();
        } else {
            this.tripDetailId = data;
        }

        palletCounterPresenter = new PalletCounterPresenterImpl(getApplicationContext(), this);
        palletCounterPresenter.getInfo(this.tripDetailId);
    }

    @Override
    public void onBackPressed() {
    }


    protected void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Contador de Tarimas");
        setSupportActionBar(toolbar);
    }

    @OnLongClick(R.id.btnCounter)
    public boolean pressCounter() {
        palletCounterPresenter.countPallet(tripDetailId, getLocation());
        return true;
    }

    @Override
    public void updateCounter(TripDetail tripDetail) {
        btnCounter.setText(String.valueOf(tripDetail.getUploadedPalletCounter()));
        palletCounter.setText(String.valueOf(tripDetail.getUploadedPalletCounter()));
    }

    @Override
    public void finishCount(TripDetail tripDetail) {
        btnCounter.setText(String.valueOf(tripDetail.getUploadedPalletCounter()));
        palletCounter.setText(String.valueOf(tripDetail.getUploadedPalletCounter()));
        finish();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setDetail(TripDetail tripDetail) {
        palletTotal.setText(tripDetail.getPalletsNumber().toString());
        storeDescription.setText(tripDetail.getStoreDescription());
    }
}
