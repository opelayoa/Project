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
import com.tiendas3b.almacen.shipment.views.StoreUploadView;

import org.joda.time.LocalDate;

public class StoreUploadPresenterImpl implements StoreUploadPresenter {


    StoreUploadView view;
    private Context context;
    private IDatabaseManager databaseManager;
    private GlobalState mContext;
    private long regionId;

    public StoreUploadPresenterImpl(Context context, StoreUploadView view) {
        this.view = view;
        this.mContext = (GlobalState) context;
        this.regionId = mContext.getRegion();
        this.databaseManager = new DatabaseManager(mContext);
    }

    @Override
    public void countPallet(long tripDetailId) {
        TripDetail tripDetail = databaseManager.findTripDetailById(tripDetailId);
        int palletCounter = tripDetail.getCollectedPalletTotal();
        palletCounter++;


        ShipmentControl shipmentControl = databaseManager.findShipmentControlByDate(LocalDate.now().toDate());
        DataBaseUtil.insertLog(databaseManager, "Tarima cargada en tienda: " + palletCounter, tripDetail.getTripId(), this.mContext.getRegion(), this.mContext.getUserId(), tripDetail.getStoreId(), shipmentControl.getTruckId(), ShipmentConstants.ACTIVITY_LOAD);
        tripDetail.setCollectedPalletTotal(palletCounter);

        tripDetail.setStatus(ShipmentConstants.TRIP_DETAIL_STATUS_COLLECTED);
        databaseManager.insertOrUpdate(tripDetail);

        view.updateCounter(tripDetail);
    }

    @Override
    public void getInfo(long tripDetailId) {
        TripDetail tripDetail = databaseManager.findTripDetailById(tripDetailId);
        tripDetail.setStatus(ShipmentConstants.TRIP_DETAIL_STATUS_DELIVERED);
        databaseManager.insertOrUpdate(tripDetail);
        view.setDetail(tripDetail);
    }
}
