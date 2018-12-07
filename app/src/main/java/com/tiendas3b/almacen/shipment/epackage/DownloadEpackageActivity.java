package com.tiendas3b.almacen.shipment.epackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.satsuware.usefulviews.LabelledSpinner;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.GenericSpinnerAdapter;
import com.tiendas3b.almacen.db.dao.EPackage;
import com.tiendas3b.almacen.db.dao.EpackageTruck;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.dao.TruckPackage;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.NetworkUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import mbanje.kurt.fabbutton.FabButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadEpackageActivity extends EpackageDownloadActivity {

    private long truckIdSelected;
    public static final String EXTRA_STORE = "EXTRA_STORE";
    private Store store;
    private EPackage epackageEncontrado;

    private String packageBarCode = null;
    private String packageBarCodeCam = null;
    List<EPackage> listaEpackage = new ArrayList<>();
    EPackage ePackageUpdt = new EPackage();
    private EpackageTruck epackageTruck = null;
    private long idEpackage = 0;
    private EpackageRvAdapter adapter;

    private MenuItem btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        status = EpackageActivity.SHIPMENT_RECEIVED;
        targetStatus = EpackageActivity.SHIPMENT_TO_STORE;
        store = (Store) getIntent().getSerializableExtra(EXTRA_STORE);
        //
        mContext = (GlobalState) getApplicationContext();
        //ButterKnife.bind(this);
        db = new DatabaseManager(mContext);
        if(!NetworkUtil.isConnected(mContext)) {
            items = db.listEPackages(status, store.getId());
        }
        super.onCreate(savedInstanceState);

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_send:
                //downloadStatusCageIn();
                validate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void validate() {
        System.out.println("****EXTRA_DATE********** " + EXTRA_DATE);
        System.out.println("****store********** " + store);
        if (items != null) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DownloadEpackageActivity.this);
            alertDialogBuilder.setMessage("Falta por descargar " + items.size() + " paquete(s)");
            alertDialogBuilder.setPositiveButton(getString(android.R.string.ok),
//                  (dialog, which) -> finish());
                    (dialog, which) -> {
                        //insertCancel();
                        System.out.println("****acepta**********");
                        Snackbar.make(viewSwitcher, "No pueden quedar paquetes pendientes por entregar", BaseTransientBottomBar.LENGTH_LONG).show();
                    }
            );
            alertDialogBuilder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                        //insertCancel();
                        System.out.println("****cancela**********");
                    }
            );

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }else{
            Intent intent = getIntent();
            startActivity(new Intent(mContext, DownloadResumeEpackageActivity.class).putExtra(DownloadEpackageActivity.EXTRA_STORE, (Serializable) store)
                    .putExtra(EXTRA_DATE, EXTRA_DATE));
        }
    }


    @Override
    protected void setItems() {
        items = db.listEPackages(status, store.getId());
        //updateItems();
    }

    public void requestFocusScan() {
        txtBarcode.requestFocus();
    }
}
