package com.tiendas3b.almacen.shipment.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Truck;
import com.tiendas3b.almacen.shipment.adapters.SpinnerTruckAdapter;
import com.tiendas3b.almacen.shipment.presenters.ShipmentConfigPresenter;
import com.tiendas3b.almacen.shipment.presenters.ShipmentConfigPresenterImpl;
import com.tiendas3b.almacen.shipment.util.DialogFactory;
import com.tiendas3b.almacen.shipment.views.ShipmentConfigView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShipmentConfigActivity extends AppCompatActivity implements ShipmentConfigView {

    @BindView(R.id.toGas)
    public Button firstButton;

    @BindView(R.id.toStore)
    public Button secondButton;

    @BindView(R.id.firstDescription)
    public TextView firstDescription;

    @BindView(R.id.secondDescription)
    public TextView secondDescription;

    private AlertDialog selectTruckDialog;
    private AlertDialog progressDialog;
    private AlertDialog successDialog;
    private AlertDialog errorDialog;
    private ShipmentConfigPresenter presenter;

    private long selectedTripId = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_tracking_selection);
        ButterKnife.bind(this);
        configureToolbar();
        firstButton.setBackground(getResources().getDrawable(R.drawable.ic_button_sync));
        secondButton.setBackground(getResources().getDrawable(R.drawable.ic_button_truck));
        firstDescription.setText("Elegir camión");
        secondDescription.setText("Sincronizar");

        presenter = new ShipmentConfigPresenterImpl(this, getApplicationContext());
        progressDialog = getProgressDialog("Sincronizando...");

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    protected void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cofiguración");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @OnClick(R.id.toGas)
    public void toGas() {
        presenter.syncShipment();
    }

    private AlertDialog getSelectTruckDialog(String title, List<Truck> trucks) {

        Truck defaultTruck = new Truck();
        defaultTruck.setDescription("Selecciona camión...");
        defaultTruck.setTruckId(0);

        trucks.add(0, defaultTruck);

        View view = LayoutInflater.from(this).inflate(R.layout.content_shipment_select_type, null);

        SpinnerTruckAdapter spinnerTruckAdapter = new SpinnerTruckAdapter(trucks, R.layout.item_form_spinner_item, this);

        Spinner spinner = view.findViewById(R.id.spTypes);
        spinner.setAdapter(spinnerTruckAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTripId = id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(ShipmentConfigActivity.this, R.style.AlertDialogTheme)
                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                })
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShipmentConfigActivity.this.presenter.saveCurrentTruck(selectedTripId);
                    }
                });

        return builder.create();
    }

    private AlertDialog getProgressDialog(String title) {

        View view = LayoutInflater.from(this).inflate(R.layout.content_progress, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ShipmentConfigActivity.this, R.style.AlertDialogTheme)
                .setView(view)
                .setTitle(title);

        return builder.create();
    }

    @OnClick(R.id.toStore)
    public void toStore() {
        presenter.getTrucks();
    }

    @Override
    public void startSyncProgress() {
        progressDialog.show();
    }

    @Override
    public void stopSyncProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String message) {
        errorDialog = DialogFactory.getErrorDialog(this, message);
        errorDialog.show();
    }

    @Override
    public void showMessage(String message) {
        successDialog = DialogFactory.getSuccessDialog(this, message);
        successDialog.show();
    }

    @Override
    public void showSelectTruck(List<Truck> trucks) {
        this.selectTruckDialog = getSelectTruckDialog("Selecciona camión", trucks);
        this.selectTruckDialog.show();
    }
}
