package com.tiendas3b.almacen.shipment.presenters;

import android.content.Context;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.db.dao.ShipmentControl;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.shipment.http.ShipmentService;
import com.tiendas3b.almacen.shipment.util.DataBaseUtil;
import com.tiendas3b.almacen.shipment.util.ShipmentConstants;
import com.tiendas3b.almacen.shipment.views.TrackSelectionView;

import org.joda.time.LocalDate;

public class TrackSelectionPresenterImpl implements TrackSelectionPresenter {


    private final TrackSelectionView view;
    private ShipmentService shipmentService;
    private IDatabaseManager databaseManager;
    private GlobalState mContext;
    private long regionId;

    public TrackSelectionPresenterImpl(Context context, TrackSelectionView view) {
        this.view = view;
        this.mContext = (GlobalState) context;
        this.regionId = mContext.getRegion();
        this.databaseManager = new DatabaseManager(mContext);
        this.shipmentService = mContext.getShipmentHttpService();
    }

    @Override
    public void saveFuel(long tripId, double price, double liters) {
        ShipmentControl shipmentControl = databaseManager.findShipmentControlByDate(LocalDate.now().toDate());
        DataBaseUtil.insertLog(databaseManager, "{\"price\" : " + price + ", \"liters\" : " + liters + "}", tripId, this.mContext.getRegion(), this.mContext.getUserId(), null, shipmentControl.getTruckId(), ShipmentConstants.ACTIVITY_FUEL);
        view.resetValues();
        view.showMessage("Conbustible registrado correctamente.");
    }

    @Override
    public void deliverPallets(long tripId) {
        TripDetail tripDetail = databaseManager.findNextTripDetail(tripId, ShipmentConstants.TRIP_DETAIL_STATUS_LOADED);
        if (tripDetail == null) {
            view.showError("Los viajes se han completado.");
        } else {
            view.deliverPallets(tripDetail.getId());
        }

    }

    @Override
    public void validateTrip(long tripId) {
        TripDetail tripDetail = databaseManager.findNextTripDetail(tripId, ShipmentConstants.TRIP_DETAIL_STATUS_LOADED);
        if (tripDetail == null) {
            view.showToWarehouse();
        }
    }
}
