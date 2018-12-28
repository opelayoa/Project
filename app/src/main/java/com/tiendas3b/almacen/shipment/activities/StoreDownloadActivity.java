package com.tiendas3b.almacen.shipment.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.shipment.base.GPSBaseActivity;
import com.tiendas3b.almacen.shipment.presenters.PalletCounterPresenterImpl;
import com.tiendas3b.almacen.shipment.presenters.StoreDownloadPresenter;
import com.tiendas3b.almacen.shipment.presenters.StoreDownloadPresenterImpl;
import com.tiendas3b.almacen.shipment.views.StoreDownloadView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class StoreDownloadActivity extends GPSBaseActivity implements StoreDownloadView {

    @BindView(R.id.btnCounter)
    Button btnCounter;

    @BindView(R.id.palletCounter)
    TextView palletCounter;

    @BindView(R.id.palletTotal)
    TextView palletTotal;

    @BindView(R.id.storeDescription)
    TextView storeDescription;

    @BindView(R.id.next)
    Button next;



    private long tripDetailId;
    private StoreDownloadPresenter storeDownloadPresenter;

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

        storeDownloadPresenter = new StoreDownloadPresenterImpl(getApplicationContext(), this);
        storeDownloadPresenter.getInfo(this.tripDetailId);
    }

    @Override
    public void onBackPressed() {
    }

    protected void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Descargar Tienda");
        setSupportActionBar(toolbar);
    }

    @OnLongClick(R.id.btnCounter)
    public boolean pressCounter() {
        storeDownloadPresenter.countPallet(tripDetailId, getLocation());
        return true;
    }

    @OnClick(R.id.next)
    public void next() {
        Bundle bundle = new Bundle();
        bundle.putLong("tripDetailId", tripDetailId);
        Intent intent = new Intent(this, StoreUploadActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


    @Override
    public void updateCounter(TripDetail tripDetail) {
        btnCounter.setText(String.valueOf(tripDetail.getDeliveredPalletCounter()));
        palletCounter.setText(String.valueOf(tripDetail.getDeliveredPalletCounter()));

    }

    @Override
    public void finishCount(TripDetail tripDetail) {
        btnCounter.setText(String.valueOf(tripDetail.getDeliveredPalletCounter()));
        palletCounter.setText(String.valueOf(tripDetail.getDeliveredPalletCounter()));
        btnCounter.setEnabled(false);
        next.setVisibility(Button.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setDetail(TripDetail tripDetail) {
        palletTotal.setText(tripDetail.getPalletsNumber().toString());
        storeDescription.setText(tripDetail.getStoreDescription());
    }
}
