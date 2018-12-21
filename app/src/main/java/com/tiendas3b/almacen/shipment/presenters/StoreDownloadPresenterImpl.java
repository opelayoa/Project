package com.tiendas3b.almacen.shipment.presenters;

import android.content.Context;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.db.dao.ShipmentControl;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.shipment.util.DataBaseUtil;
import com.tiendas3b.almacen.shipment.util.ShipmentConstants;
import com.tiendas3b.almacen.shipment.views.StoreDownloadView;

import org.joda.time.LocalDate;

public class StoreDownloadPresenterImpl implements StoreDownloadPresenter {


    StoreDownloadView view;
    private Context context;
    private IDatabaseManager databaseManager;
    private GlobalState mContext;
    private long regionId;

    public StoreDownloadPresenterImpl(Context context, StoreDownloadView view) {
        this.view = view;
        this.mContext = (GlobalState) context;
        this.regionId = mContext.getRegion();
        this.databaseManager = new DatabaseManager(mContext);
    }

    @Override
    public void countPallet(long tripDetailId) {
        TripDetail tripDetail = databaseManager.findTripDetailById(tripDetailId);
        int palletCounter = tripDetail.getDeliveredPalletCounter();
        palletCounter++;

        tripDetail.setDeliveredPalletCounter(palletCounter);


        ShipmentControl shipmentControl = databaseManager.findShipmentControlByDate(LocalDate.now().toDate());
        DataBaseUtil.insertLog(databaseManager, "Tarima descargada en tienda: " + palletCounter, tripDetail.getTripId(), this.mContext.getRegion(), this.mContext.getUserId(), tripDetail.getStoreId(), shipmentControl.getTruckId(), ShipmentConstants.ACTIVITY_DOWNLOAD, null);

        if (tripDetail.getPalletsNumber() > tripDetail.getDeliveredPalletCounter()) {
            databaseManager.insertOrUpdate(tripDetail);
            view.updateCounter(tripDetail);
        } else if (tripDetail.getPalletsNumber() == tripDetail.getDeliveredPalletCounter()) {
            tripDetail.setStatus(ShipmentConstants.TRIP_DETAIL_STATUS_DELIVERED);
            databaseManager.insertOrUpdate(tripDetail);
            view.finishCount(tripDetail);
        }
    }

    @Override
    public void getInfo(long tripDetailId) {
        TripDetail tripDetail = databaseManager.findTripDetailById(tripDetailId);
        view.setDetail(tripDetail);
    }
}
