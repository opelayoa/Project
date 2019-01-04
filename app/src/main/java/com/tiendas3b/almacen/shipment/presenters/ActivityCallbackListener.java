package com.tiendas3b.almacen.shipment.presenters;

import com.tiendas3b.almacen.db.dao.SheetTrip;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public interface ActivityCallbackListener {

    void onPostActivitySent();
}
