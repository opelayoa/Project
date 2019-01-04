package com.tiendas3b.almacen.shipment.presenters;

import android.content.Context;
import android.location.Location;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.db.dao.SheetTrip;
import com.tiendas3b.almacen.db.dao.ShipmentControl;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.shipment.http.ShipmentService;
import com.tiendas3b.almacen.shipment.util.DataBaseUtil;
import com.tiendas3b.almacen.shipment.util.ShipmentConstants;
import com.tiendas3b.almacen.shipment.views.ActivitySenderListener;
import com.tiendas3b.almacen.shipment.views.TripDetailView;

import org.joda.time.LocalDate;

import java.util.List;

public class TripDetailPresenterImpl implements TripDetailPresenter, ActivityCallbackListener {

    private TripDetailView view;
    private IDatabaseManager databaseManager;
    private GlobalState mContext;
    private long regionId;
    private ShipmentService shipmentService;
    private ActivitySenderListener activitySenderListener;


    public TripDetailPresenterImpl(Context context, TripDetailView view, ActivitySenderListener activitySenderListener) {
        this.view = view;
        this.mContext = (GlobalState) context;
        this.regionId = mContext.getRegion();
        this.databaseManager = new DatabaseManager(mContext);
        this.shipmentService = mContext.getShipmentHttpService();
        this.activitySenderListener = activitySenderListener;
    }


    @Override
    public void getTrips(long tripId) {
        List<TripDetail> tripDetails = this.databaseManager.listTripDetails(tripId);
        this.view.setTripDetailSuccess(tripDetails);
    }

    @Override
    public void startTrip(long tripId, Location location) {
        ShipmentControl shipmentControl = databaseManager.findShipmentControlByDate(LocalDate.now().toDate());
        shipmentControl.setStatus(ShipmentConstants.SHIPMENT_CONTROL_NOT_UPGRADABLE);
        databaseManager.update(shipmentControl);
        SheetTrip sheetTrip = DataBaseUtil.insertLog(databaseManager, "Inicio de viaje", tripId, this.mContext.getRegion(), this.mContext.getUserId(), null, shipmentControl.getTruckId(), ShipmentConstants.ACTIVITY_ROUTE, null, location);


        activitySenderListener.startProgressActivityUpload("Enviando información");
        if (sheetTrip != null) {
            ActivityCallback activityCallback = new ActivityCallback(this, shipmentService, databaseManager);
            activityCallback.sendActivity(sheetTrip);
        } else {
            view.startTrip();
        }

    }


    @Override
    public void onPostActivitySent() {
        activitySenderListener.stopProgressActivityUpload();
        view.startTrip();
    }
}
