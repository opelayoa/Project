package com.tiendas3b.almacen.shipment.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.shipment.filters.DecimalDigitsFilter;
import com.tiendas3b.almacen.shipment.filters.DoubleFilter;
import com.tiendas3b.almacen.shipment.presenters.TrackSelectionPresenter;
import com.tiendas3b.almacen.shipment.presenters.TrackSelectionPresenterImpl;
import com.tiendas3b.almacen.shipment.util.DialogFactory;
import com.tiendas3b.almacen.shipment.views.TrackSelectionView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrackSelectionActivity extends AppCompatActivity implements TrackSelectionView {

    @BindView(R.id.toGas)
    Button toGas;

    @BindView(R.id.toStore)
    Button toStore;

    @BindView(R.id.returnWarehouse)
    Button returnWarehouse;

    @BindView(R.id.layoutToStore)
    LinearLayout layoutToStore;

    @BindView(R.id.layoutToWareHouse)
    LinearLayout layoutToWarehouse;

    private EditText price;
    private EditText liters;

    AlertDialog errorDialog;
    AlertDialog successDialog;

    TrackSelectionPresenter trackSelectionPresenter;

    private long tripId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_tracking_selection);
        ButterKnife.bind(this);
        configureToolbar();

        Long data = getIntent().getExtras().getLong("tripId");
        if (data == null) {
            onBackPressed();
        } else {
            this.tripId = data;
        }

        this.trackSelectionPresenter = new TrackSelectionPresenterImpl(getApplicationContext(), this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        this.trackSelectionPresenter.validateTrip(tripId);
    }

    @Override
    public void onBackPressed() {
    }

    protected void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ruta a tienda");
        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.toGas)
    public void toGas() {
        getDialog().show();
    }

    private AlertDialog getDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.content_shipment_gas_capture, null);
        price = view.findViewById(R.id.price);
        liters = view.findViewById(R.id.liters);


        price.setFilters(new InputFilter[]{new DoubleFilter("1", "30.00"), new DecimalDigitsFilter(3, 2)});

        liters.setFilters(new InputFilter[]{new DoubleFilter("1", "380"), new DecimalDigitsFilter(4, 2)});

        AlertDialog.Builder builder = new AlertDialog.Builder(TrackSelectionActivity.this, R.style.AlertDialogTheme)
                .setView(view)
                .setTitle("Registro de combustible")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                })
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            double longPrice = Double.valueOf(price.getText().toString());
                            double longLiters = Double.valueOf(liters.getText().toString());
                            trackSelectionPresenter.saveFuel(tripId, longPrice, longLiters);
                        } catch(Exception e) {
                            showError("Error al guardar, intenta nuevamente por favor.");
                        }
                    }
                });

        return builder.create();
    }

    @OnClick(R.id.toStore)
    public void toStore() {
        this.trackSelectionPresenter.deliverPallets(tripId);
    }

    @OnClick(R.id.returnWarehouse)
    public void downloadWarehouse() {
        Bundle bundle = new Bundle();
        bundle.putLong("tripId", tripId);
        Intent intent = new Intent(this, WarehouseDownloadActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void showError(String error) {
        errorDialog = DialogFactory.getErrorDialog(this, error);
        errorDialog.show();
    }

    @Override
    public void showMessage(String message) {
        successDialog = DialogFactory.getSuccessDialog(this, message);
        successDialog.show();
    }

    @Override
    public void deliverPallets(long tripDetailId) {
        Bundle bundle = new Bundle();
        bundle.putLong("tripDetailId", tripDetailId);
        Intent intent = new Intent(this, StoreDownloadActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void showToWarehouse() {
        this.layoutToStore.setVisibility(LinearLayout.GONE);
        this.layoutToWarehouse.setVisibility(LinearLayout.VISIBLE);
    }

    @Override
    public void resetValues() {
        price.setText("");
        liters.setText("");
    }
}
