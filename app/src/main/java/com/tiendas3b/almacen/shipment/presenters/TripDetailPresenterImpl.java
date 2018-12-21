package com.tiendas3b.almacen.shipment.presenters;

import android.content.Context;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.db.dao.ShipmentControl;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.shipment.util.DataBaseUtil;
import com.tiendas3b.almacen.shipment.util.ShipmentConstants;
import com.tiendas3b.almacen.shipment.views.TripDetailView;

import org.joda.time.LocalDate;

import java.util.List;

public class TripDetailPresenterImpl implements TripDetailPresenter {

    private TripDetailView view;
    private Context context;
    private IDatabaseManager databaseManager;
    private GlobalState mContext;
    private long regionId;


    public TripDetailPresenterImpl(Context context, TripDetailView view) {
        this.view = view;
        this.mContext = (GlobalState) context;
        this.regionId = mContext.getRegion();
        this.databaseManager = new DatabaseManager(mContext);
    }


    @Override
    public void getTrips(long tripId) {
        List<TripDetail> tripDetails = this.databaseManager.listTripDetails(tripId);
        this.view.setTripDetailSuccess(tripDetails);
    }

    @Override
    public void startTrip(long tripId) {
        ShipmentControl shipmentControl = databaseManager.findShipmentControlByDate(LocalDate.now().toDate());
        shipmentControl.setStatus(ShipmentConstants.SHIPMENT_CONTROL_NOT_UPGRADABLE);
        databaseManager.update(shipmentControl);
        DataBaseUtil.insertLog(databaseManager, "Inicio de viaje", tripId, this.mContext.getRegion(), this.mContext.getUserId(), null, shipmentControl.getTruckId(), ShipmentConstants.ACTIVITY_ROUTE, null);
        view.startTrip();
    }
}
