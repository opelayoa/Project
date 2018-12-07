package com.tiendas3b.almacen.shipment.epackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.EPackage;
import com.tiendas3b.almacen.db.dao.EpackageDownload;
import com.tiendas3b.almacen.db.dao.EpackageTruck;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.util.NetworkUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DownloadResumeEpackageActivity extends EpackageLeaveActivity {

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
        status = EpackageActivity.SHIPMENT_TO_STORE;
        targetStatus = EpackageActivity.STORE_RECEIVED;
        store = (Store) getIntent().getSerializableExtra(EXTRA_STORE);
        System.out.println("EXTRA_DATE: " + EXTRA_DATE);
        //
        mContext = (GlobalState) getApplicationContext();
        //ButterKnife.bind(this);
        db = new DatabaseManager(mContext);
        if(!NetworkUtil.isConnected(mContext)) {
            items = db.listEPackagesDownload(status, store.getId());
        }
        super.onCreate(savedInstanceState);
    }



}
