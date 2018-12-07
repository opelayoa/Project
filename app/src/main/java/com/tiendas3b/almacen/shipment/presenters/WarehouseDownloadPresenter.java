package com.tiendas3b.almacen.shipment.presenters;

public interface WarehouseDownloadPresenter {

    void getInfo(long tripId);

    void notifyFinish(long tripId);

    void generateLog(long tripId, String message);
}
