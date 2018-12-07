package com.tiendas3b.almacen.shipment.views;

import com.tiendas3b.almacen.db.dao.Truck;

import java.util.List;

public interface ShipmentConfigView {

    void startSyncProgress();

    void stopSyncProgress();

    void showError(String message);

    void showMessage(String message);

    void showSelectTruck(List<Truck> trucks);
}
