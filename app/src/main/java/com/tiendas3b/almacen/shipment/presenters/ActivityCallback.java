package com.tiendas3b.almacen.shipment.presenters;

import com.tiendas3b.almacen.db.dao.SheetTrip;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.shipment.http.ShipmentService;
import com.tiendas3b.almacen.shipment.util.ShipmentConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCallback {

    private ActivityCallbackListener listener;
    private ShipmentService service;
    private IDatabaseManager databaseManager;

    public ActivityCallback(ActivityCallbackListener listener, ShipmentService service, IDatabaseManager databaseManager) {
        this.listener = listener;
        this.service = service;
        this.databaseManager = databaseManager;

    }

    public void sendActivity(SheetTrip sheetTrip) {
        service.insertActivity(sheetTrip).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    sheetTrip.setStatus(ShipmentConstants.STATUS_ACTIVITY_REGISTERED);
                    databaseManager.update(sheetTrip);
                    listener.onPostActivitySent();
                } else {
                    sheetTrip.setStatus(ShipmentConstants.STATUS_ACTIVITY_ERROR);
                    databaseManager.update(sheetTrip);
                    listener.onPostActivitySent();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                sheetTrip.setStatus(ShipmentConstants.STATUS_ACTIVITY_ERROR);
                databaseManager.update(sheetTrip);
                listener.onPostActivitySent();
            }
        });
    }

}
