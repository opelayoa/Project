package com.tiendas3b.almacen.shipment.presenters;

import android.content.Context;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.db.dao.ShipmentControl;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.shipment.util.DataBaseUtil;
import com.tiendas3b.almacen.shipment.util.ShipmentConstants;
import com.tiendas3b.almacen.shipment.views.PalletLoadingView;

import org.joda.time.LocalDate;

import java.util.List;

public class PalletLoadingPresenterImpl implements PalletLoadingPresenter {

    PalletLoadingView view;
    private Context context;
    private IDatabaseManager databaseManager;
    private GlobalState mContext;
    private long regionId;

    public PalletLoadingPresenterImpl(Context context, PalletLoadingView view) {
        this.view = view;
        this.mContext = (GlobalState) context;
        this.regionId = mContext.getRegion();
        this.databaseManager = new DatabaseManager(mContext);
    }

    @Override
    public void validateStore(long tripDetailId, long tripId) {
        TripDetail tripDetailNext = databaseManager.findNextTripDetail(tripId, ShipmentConstants.TRIP_DETAIL_STATUS_NOT_YET_STARTED, ShipmentConstants.TRIP_DETAIL_STATUS_STARTED);
        TripDetail tripDetail = databaseManager.findTripDetailById(tripDetailId);
        if (tripDetailNext != null) {
            if (!tripDetail.getId().equals(tripDetailNext.getId())) {
                if (tripDetail.getStatus() != ShipmentConstants.TRIP_STATUS_COMPLETED) {
                    view.showError("La tienda seleccionada no es la siguiente, por favor seleccione la correcta.");
                }
            } else {
                ShipmentControl shipmentControl = databaseManager.findShipmentControlByDate(LocalDate.now().toDate());
                DataBaseUtil.insertLog(databaseManager, "Inicio carga de tarimas del almacen.", tripId, this.mContext.getRegion(), this.mContext.getUserId(), tripDetail.getStoreId(), shipmentControl.getTruckId(), ShipmentConstants.ACTIVITY_LOAD);
                view.openCounter(tripDetail.getId());
            }
        }
    }

    @Override
    public void getTrips(long tripId) {
        List<TripDetail> tripDetails = this.databaseManager.listTripDetails(tripId);

        int counter = 0;
        for (TripDetail tripDetail : tripDetails) {
            counter += tripDetail.getPalletsNumber();
        }

        this.view.setTripDetailSuccess(tripDetails);
    }

    @Override
    public void validateTotalStoresLoaded(long tripId) {
        TripDetail tripDetailNext = databaseManager.findNextTripDetail(tripId, ShipmentConstants.TRIP_DETAIL_STATUS_NOT_YET_STARTED, ShipmentConstants.TRIP_DETAIL_STATUS_STARTED);
        if (tripDetailNext == null) {
            view.showNext();
        }
    }


}
