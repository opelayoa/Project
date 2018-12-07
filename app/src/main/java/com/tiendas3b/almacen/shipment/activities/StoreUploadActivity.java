package com.tiendas3b.almacen.shipment.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.shipment.presenters.StoreUploadPresenter;
import com.tiendas3b.almacen.shipment.presenters.StoreUploadPresenterImpl;
import com.tiendas3b.almacen.shipment.views.StoreUploadView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class StoreUploadActivity extends AppCompatActivity implements StoreUploadView {

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

    private int counter;
    private int totalPallets;
    private AlertDialog alertDialog;

    private StoreUploadPresenter storeUploadPresenter;
    private long tripDetailId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_counter);
        ButterKnife.bind(this);
        configureToolbar();
        findViewById(R.id.palletLabel).setVisibility(View.GONE);
        alertDialog = getDialog();
        next.setVisibility(View.VISIBLE);

        Long data = getIntent().getExtras().getLong("tripDetailId");
        if (data == null) {
            onBackPressed();
        } else {
            this.tripDetailId = data;
        }

        storeUploadPresenter = new StoreUploadPresenterImpl(getApplicationContext(), this);
        storeUploadPresenter.getInfo(tripDetailId);
    }

    protected void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Carga de Tienda");
        setSupportActionBar(toolbar);
    }

    @OnLongClick(R.id.btnCounter)
    public boolean pressCounter() {
        alertDialog.show();
        return true;
    }

    @OnClick(R.id.next)
    protected void next() {
        // Intent intent = new Intent(StoreUploadActivity.this, WarehouseDownloadActivity.class);
        // startActivity(intent);
        finish();
    }

    private AlertDialog getDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.content_shipment_select_type, null);

        List<String> tipos = new ArrayList() {{
            add("Retornable");
            add("Reciclaje");
            add("Producto general");
        }};

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.item_form_spinner_item, tipos);
        dataAdapter.setDropDownViewResource(R.layout.item_form_spinner_item);

        Spinner spinner = view.findViewById(R.id.spTypes);
        spinner.setAdapter(dataAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(StoreUploadActivity.this, R.style.AlertDialogTheme)
                .setView(view)
                .setTitle("Seleccione tipo de producto")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        storeUploadPresenter.countPallet(tripDetailId);
                    }
                });

        return builder.create();
    }

    @Override
    public void updateCounter(TripDetail tripDetail) {
        btnCounter.setText(String.valueOf(tripDetail.getCollectedPalletTotal()));
        palletCounter.setText(String.valueOf(tripDetail.getCollectedPalletTotal()));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setDetail(TripDetail tripDetail) {
        palletTotal.setText(tripDetail.getPalletsNumber().toString());
        storeDescription.setText(tripDetail.getStoreDescription());
    }
}
