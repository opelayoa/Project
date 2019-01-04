package com.tiendas3b.almacen.shipment.util;

import android.location.Location;

import com.tiendas3b.almacen.db.dao.SheetTrip;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;

import java.util.Date;

public class DataBaseUtil {

    public static SheetTrip insertLog(IDatabaseManager databaseManager, String description, Long tripId, Long regionId, Long userId, Long storeId, Long truckId, Long activityId, Double odometer, Location location) {
        SheetTrip sheetTrip = new SheetTrip();
        sheetTrip.setDate(new Date());
        sheetTrip.setDescription(description);
        sheetTrip.setOdometer(odometer);
        sheetTrip.setTripId(tripId);
        sheetTrip.setStoreId(storeId);
        sheetTrip.setTruckId(truckId);
        sheetTrip.setRegionId(regionId);
        sheetTrip.setUserId(userId);
        sheetTrip.setActivityId(activityId);

        sheetTrip.setLatitude(location.getLatitude());
        sheetTrip.setLongitude(location.getLongitude());
        sheetTrip.setStatus(ShipmentConstants.STATUS_ACTIVITY_CREATED);
        try {
            databaseManager.insert(sheetTrip);
        } catch (Exception e) {
            return null;
        }
        return sheetTrip;
    }
}
