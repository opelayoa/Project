package com.tiendas3b.almacen.miscellaneous;

import android.os.Bundle;

import com.tiendas3b.almacen.picking.epackage.EpackageActivity;

public class StoreReceiptEpackageActivity extends EpackageActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        status = EpackageActivity.SHIPMENT_TO_STORE;
        targetStatus = EpackageActivity.STORE_RECEIVED;
        super.onCreate(savedInstanceState);
    }

//    @Override
//    protected void action(EPackage ePackage) {
//        action(ePackage, status, targetStatus);
//    }
//
//    @Override
//    protected void setItems() {
//        items = db.listEPackages(status);
//    }

}
