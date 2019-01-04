package com.tiendas3b.almacen.shipment.presenters;

import android.content.Context;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.db.dao.SheetTrip;
import com.tiendas3b.almacen.db.dao.ShipmentControl;
import com.tiendas3b.almacen.db.dao.Trip;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.shipment.http.ShipmentService;
import com.tiendas3b.almacen.shipment.util.ShipmentConstants;
import com.tiendas3b.almacen.shipment.views.TripView;

import org.joda.time.LocalDate;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripPresenterImpl implements TripPresenter {

    private TripView view;
    private Context context;
    private IDatabaseManager databaseManager;
    private GlobalState mContext;
    private long regionId;
    private ShipmentService shipmentService;

    public TripPresenterImpl(Context context, TripView view) {
        this.view = view;
        this.mContext = (GlobalState) context;
        this.regionId = mContext.getRegion();
        this.databaseManager = new DatabaseManager(mContext);
        this.shipmentService = mContext.getShipmentHttpService();
    }

    @Override
    public void getTrips() {
        ShipmentControl shipmentControl = this.databaseManager.findShipmentControlByDate(LocalDate.now().toDate());
        if (shipmentControl == null) {
            this.view.showError("Se requiere realizar sincronización");
            return;
        }

        if (shipmentControl.getTruckId() == null) {
            this.view.showError("Debes seleccionar el camión en la opción \"configuración\"");
            return;
        }

        List<Trip> trips = databaseManager.findTrips(LocalDate.now().toDate(), shipmentControl.getTruckId(), regionId);
        view.setTripSuccess(trips);

    }

    @Override
    public void validateSelectedTrip(long sequence, int status) {
        ShipmentControl shipmentControl = this.databaseManager.findShipmentControlByDate(LocalDate.now().toDate());
        Trip trip = databaseManager.findNextTrip(shipmentControl.getTruckId());
        if (status != ShipmentConstants.TRIP_STATUS_COMPLETED) {
            if (sequence == trip.getSequence()) {
                view.showDetails(trip.getId());
            } else if (sequence > trip.getSequence()) {
                view.showError("Debe completar los viajes anteriores, para ver este viaje.");
            }
        }
    }

    @Override
    public void showInfo() {
        List<SheetTrip> logs = databaseManager.listAll(SheetTrip.class);
        /*shipmentService.sendLongs(logs).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("Exito");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });*/
    }

}
