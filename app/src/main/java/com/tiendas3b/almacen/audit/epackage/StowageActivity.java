package com.tiendas3b.almacen.audit.epackage;

import android.os.Bundle;

import com.tiendas3b.almacen.picking.epackage.EpackageActivity;

public class StowageActivity extends EpackageActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        status = EpackageActivity.PICKING_CAGE_OUT;
        targetStatus = EpackageActivity.AUDIT_STOWAGE;
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_stowage);
    }

//    @Override
//    protected void action(EPackage ePackage) {
//        action(ePackage, status, targetStatus);
//    }
//
//    @Override
//    protected void setItems() {
//        items = db.listEPackages(status);
////        updateItems();
//    }

}
