package com.tiendas3b.almacen.shipment.presenters;

import android.content.Context;
import android.location.Location;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.db.dao.ShipmentControl;
import com.tiendas3b.almacen.db.dao.Trip;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.shipment.util.DataBaseUtil;
import com.tiendas3b.almacen.shipment.util.ShipmentConstants;
import com.tiendas3b.almacen.shipment.views.StoreDownloadView;
import com.tiendas3b.almacen.shipment.views.WarehouseDownloadView;

import org.joda.time.LocalDate;

import java.util.List;

public class WarehouseDownloadPresenterImpl implements WarehouseDownloadPresenter {


    private WarehouseDownloadView view;
    private Context context;
    private IDatabaseManager databaseManager;
    private GlobalState mContext;
    private long regionId;

    public WarehouseDownloadPresenterImpl(Context context, WarehouseDownloadView view) {
        this.view = view;
        this.mContext = (GlobalState) context;
        this.regionId = mContext.getRegion();
        this.databaseManager = new DatabaseManager(mContext);
    }

    @Override
    public void getInfo(long tripId) {
        List<TripDetail> list = databaseManager.listTripDetails(tripId);

        int totalItems = 0;
        for (TripDetail tripDetail : list) {
            totalItems += tripDetail.getCollectedPalletTotal();
        }

        view.setDetail(totalItems);
    }

    @Override
    public void notifyFinish(long tripId) {
        Trip trip = databaseManager.findTripById(tripId);
        trip.setStatus(ShipmentConstants.TRIP_STATUS_COMPLETED);
        databaseManager.update(trip);
        view.finishCount();

    }

    @Override
    public void generateLog(long tripId, String message, Location location) {
        ShipmentControl shipmentControl = databaseManager.findShipmentControlByDate(LocalDate.now().toDate());
        DataBaseUtil.insertLog(databaseManager, message, tripId, this.mContext.getRegion(), this.mContext.getUserId(), null, shipmentControl.getTruckId(), ShipmentConstants.ACTIVITY_DOWNLOAD, null, location);
    }
}
