package com.tiendas3b.almacen.shipment.util;

import com.tiendas3b.almacen.db.dao.SheetTrip;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;

import java.util.Date;

public class DataBaseUtil {

    public static boolean insertLog(IDatabaseManager databaseManager, String description, Long tripId, Long regionId, Long userId, Long storeId, Long truckId, Long activityId, Double odometer) {
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
        try {
            databaseManager.insert(sheetTrip);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
