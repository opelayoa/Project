package com.tiendas3b.almacen.shipment.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Form;
import com.tiendas3b.almacen.db.dao.Question;
import com.tiendas3b.almacen.shipment.base.GPSBaseActivity;
import com.tiendas3b.almacen.shipment.presenters.CheckListPresenterImpl;
import com.tiendas3b.almacen.shipment.presenters.ChecklistPresenter;
import com.tiendas3b.almacen.shipment.util.DialogFactory;
import com.tiendas3b.almacen.shipment.util.OdometerDialogListener;
import com.tiendas3b.almacen.shipment.views.ChecklistView;

import java.util.HashMap;
import java.util.Map;

public class CheckListActivity extends GPSBaseActivity implements ChecklistView, OdometerDialogListener {

    private ChecklistPresenter checklistPresenter;
    private Map<Integer, CheckBox> map;
    private Button button;
    private AlertDialog alertDialog;
    private long tripId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_checklist);

        configureToolbar();

        Long data = getIntent().getExtras().getLong("tripId");
        if (data == null) {
            onBackPressed();
        } else {
            this.tripId = data;
        }

        this.checklistPresenter = new CheckListPresenterImpl(getApplicationContext(), this);
        alertDialog = getDialog();

        this.button = findViewById(R.id.checklistNext);
        this.button.setOnClickListener(view -> {
            alertDialog = DialogFactory.getOdometerDialog(this, this, null);
            alertDialog.show();
        });

        checklistPresenter.getForm();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void showCheckList(Form form) {
        displayForm(form);
    }

    private void displayForm(Form form) {
        LinearLayout linearLayout = findViewById(R.id.formContainer);
        map = new HashMap();
        int counter = 0;
        for (final Question question : form.getQuestionList()) {
            counter++;
            View view = getLayoutInflater().inflate(R.layout.item_form_checklist, null);
            CheckBox checkBox = view.findViewById(R.id.checkbox);
            if (map.size() != 0) {
                checkBox.setEnabled(false);
            }
            map.put(counter, checkBox);
            checkBox.setText(question.getDescription());
            int finalCounter = counter;
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> onChangeItem(question.getId(), isChecked ? 2 : 1, finalCounter));
            linearLayout.addView(view);
        }
    }

    private void onChangeItem(long questionId, int answerId, int counter) {
        CheckBox checkBox = map.get(counter);
        checkBox.setEnabled(false);

        // Toast.makeText(this, "Latitude: " + this.location.getLatitude() + " Longitude: " + this.location.getLongitude(), Toast.LENGTH_SHORT).show();

        if (map.containsKey(counter + 1)) {
            checkBox = map.get(counter + 1);
            checkBox.setEnabled(true);
            checklistPresenter.generateLog(tripId, "Check: " + checkBox.getText(), null, getLocation());
        } else {
            button.setVisibility(Button.VISIBLE);
        }
    }

    private AlertDialog getDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckListActivity.this);
        builder.setTitle("Alerta");
        builder.setMessage("Prueba");
        return builder.create();
    }

    protected void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Check de la unidad");
        setSupportActionBar(toolbar);
    }

    @Override
    public void capture(Double odometer, Integer event) {
        alertDialog.dismiss();
        checklistPresenter.generateLog(tripId, "Inicio de viaje: " + tripId, odometer, getLocation());
        Bundle bundle = new Bundle();
        bundle.putLong("tripId", tripId);
        Intent intent = new Intent(CheckListActivity.this, PalletLoadingActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void showError(String message) {
        AlertDialog alertDialog = DialogFactory.getErrorDialog(this, message);
        alertDialog.show();
    }


}
