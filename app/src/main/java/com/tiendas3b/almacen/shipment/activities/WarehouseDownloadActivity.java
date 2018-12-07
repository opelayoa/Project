package com.tiendas3b.almacen.shipment.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.shipment.presenters.WarehouseDownloadPresenter;
import com.tiendas3b.almacen.shipment.presenters.WarehouseDownloadPresenterImpl;
import com.tiendas3b.almacen.shipment.views.WarehouseDownloadView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class WarehouseDownloadActivity extends AppCompatActivity implements WarehouseDownloadView {

    @BindView(R.id.btnCounter)
    Button btnCounter;

    @BindView(R.id.palletCounter)
    TextView palletCounter;

    @BindView(R.id.palletTotal)
    TextView totalItem;

    @BindView(R.id.itemLabel)
    TextView itemLabel;

    @BindView(R.id.storeLayout)
    LinearLayout storeLayout;

    @BindView(R.id.next)
    Button next;


    private WarehouseDownloadPresenter warehouseDownloadPresenter;

    private int counter;
    private int totalItems;
    private long tripId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_counter);
        ButterKnife.bind(this);
        configureToolbar();

        itemLabel.setText("Items");
        storeLayout.setVisibility(View.GONE);

        Long data = getIntent().getExtras().getLong("tripId");
        if (data == null) {
            onBackPressed();
        } else {
            this.tripId = data;
        }

        counter = 0;

        this.warehouseDownloadPresenter = new WarehouseDownloadPresenterImpl(getApplicationContext(), this);
        this.warehouseDownloadPresenter.getInfo(tripId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (totalItems == 0) {
            warehouseDownloadPresenter.notifyFinish(tripId);
        }
    }

    @Override
    public void onBackPressed() {}

    protected void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Descargar Almacen");
        setSupportActionBar(toolbar);
    }

    @OnLongClick(R.id.btnCounter)
    public boolean pressCounter() {
        counter++;
        palletCounter.setText(String.valueOf(counter));
        btnCounter.setText(String.valueOf(counter));

        this.warehouseDownloadPresenter.generateLog(tripId, "Decarga de Item en Almacen: " + counter);

        if (counter == totalItems) {
            warehouseDownloadPresenter.notifyFinish(tripId);
        }
        return true;
    }

    @OnClick(R.id.next)
    public void next() {
        finish();
    }

    @Override
    public void updateCounter(int items) {
        counter++;

        btnCounter.setText(String.valueOf(counter));


        if (counter == totalItems) {
            warehouseDownloadPresenter.notifyFinish(tripId);
        }
    }

    @Override
    public void setDetail(int totalItems) {
        this.totalItems = totalItems;
        totalItem.setText(String.valueOf(totalItems));
    }

    @Override
    public void finishCount() {
        btnCounter.setEnabled(false);
        next.setText("Finalizar");
        next.setVisibility(Button.VISIBLE);
    }
}
