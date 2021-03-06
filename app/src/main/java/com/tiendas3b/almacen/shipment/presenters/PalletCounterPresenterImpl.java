package com.tiendas3b.almacen.shipment.presenters;

import android.content.Context;
import android.location.Location;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.db.dao.SheetTrip;
import com.tiendas3b.almacen.db.dao.ShipmentControl;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.shipment.util.DataBaseUtil;
import com.tiendas3b.almacen.shipment.util.ShipmentConstants;
import com.tiendas3b.almacen.shipment.views.ActivitySenderListener;
import com.tiendas3b.almacen.shipment.views.PalletCounterView;

import org.joda.time.LocalDate;

public class PalletCounterPresenterImpl implements PalletCounterPresenter, ActivityCallbackListener {

    PalletCounterView view;
    private Context context;
    private IDatabaseManager databaseManager;
    private GlobalState mContext;
    private long regionId;
    private ActivitySenderListener activitySenderListener;

    public PalletCounterPresenterImpl(Context context, PalletCounterView view, ActivitySenderListener activitySenderListener) {
        this.view = view;
        this.mContext = (GlobalState) context;
        this.regionId = mContext.getRegion();
        this.databaseManager = new DatabaseManager(mContext);
        this.activitySenderListener = activitySenderListener;
    }

    @Override
    public void countPallet(long tripDetailId, Location location) {
        TripDetail tripDetail = databaseManager.findTripDetailById(tripDetailId);
        int palletCounter = tripDetail.getUploadedPalletCounter();
        palletCounter++;

        ShipmentControl shipmentControl = databaseManager.findShipmentControlByDate(LocalDate.now().toDate());

        tripDetail.setUploadedPalletCounter(palletCounter);

        if (tripDetail.getPalletsNumber() > tripDetail.getUploadedPalletCounter()) {
            tripDetail.setStatus(ShipmentConstants.TRIP_DETAIL_STATUS_STARTED);
            databaseManager.insertOrUpdate(tripDetail);
            view.updateCounter(tripDetail);
        } else if (tripDetail.getPalletsNumber() == tripDetail.getUploadedPalletCounter()) {
            tripDetail.setStatus(ShipmentConstants.TRIP_DETAIL_STATUS_LOADED);
            databaseManager.insertOrUpdate(tripDetail);
            view.finishCount(tripDetail);
        }

        SheetTrip sheetTrip = DataBaseUtil.insertLog(databaseManager, "Tarima cargada: " + palletCounter, tripDetail.getTripId(), this.mContext.getRegion(), this.mContext.getUserId(), tripDetail.getStoreId(), shipmentControl.getTruckId(), ShipmentConstants.ACTIVITY_LOAD, null, location);

    }

    @Override
    public void getInfo(long tripDetailId) {
        TripDetail tripDetail = databaseManager.findTripDetailById(tripDetailId);
        view.setDetail(tripDetail);
    }

    @Override
    public void onPostActivitySent() {

    }
}