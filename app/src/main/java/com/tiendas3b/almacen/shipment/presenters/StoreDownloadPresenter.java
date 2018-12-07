package com.tiendas3b.almacen.shipment.presenters;

public interface StoreDownloadPresenter {

    void countPallet(long tripDetailId);

    void getInfo(long tripDetailId);
}
