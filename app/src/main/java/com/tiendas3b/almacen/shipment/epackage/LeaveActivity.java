package com.tiendas3b.almacen.shipment.epackage;

import android.os.Bundle;

import com.tiendas3b.almacen.db.dao.EPackage;
import com.tiendas3b.almacen.shipment.epackage.EpackageActivity;

import mbanje.kurt.fabbutton.FabButton;

public class LeaveActivity extends EpackageLeaveActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        status = EpackageActivity.AUDIT_STOWAGE;
        targetStatus = EpackageActivity.SHIPMENT_RECEIVED;
        super.onCreate(savedInstanceState);
    }

//
//    @Override
//    protected void setItems() {
//        items = db.listEPackages(status);
//    }
}
