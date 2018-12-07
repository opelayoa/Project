package com.tiendas3b.almacen.shipment.presenters;

import com.tiendas3b.almacen.db.dao.Truck;

public interface ShipmentConfigPresenter {

    void syncShipment();

    void getTrucks();

    void saveCurrentTruck(long tripId);
}
