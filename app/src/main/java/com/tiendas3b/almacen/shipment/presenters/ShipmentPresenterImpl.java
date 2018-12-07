package com.tiendas3b.almacen.shipment.presenters;

import android.content.Context;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.db.dao.SheetTrip;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.shipment.http.ShipmentService;
import com.tiendas3b.almacen.shipment.views.ShipmentView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShipmentPresenterImpl implements ShipmentPresenter {

    private Context context;
    private IDatabaseManager databaseManager;
    private GlobalState mContext;
    private long regionId;
    private ShipmentService shipmentService;
    private ShipmentView view;

    public ShipmentPresenterImpl(Context context, ShipmentView view) {
        this.mContext = (GlobalState) context;
        this.regionId = mContext.getRegion();
        this.databaseManager = new DatabaseManager(mContext);
        this.shipmentService = mContext.getShipmentHttpService();
        this.view = view;
    }

    @Override
    public void uploadInfoTrips() {
        List<SheetTrip> list = databaseManager.listAll(SheetTrip.class);

        view.initProgressDialog("Enviando información...");

        shipmentService.uploadInfoTrips(list).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    view.stopProgressDialog();
                    view.showInfo("Envió de información exitosa.");
                } else {
                    view.showError("Existió un error, intente más tarde.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                view.stopProgressDialog();
                view.showError("Existió un error, intente más tarde.");

            }
        });
    }
}
