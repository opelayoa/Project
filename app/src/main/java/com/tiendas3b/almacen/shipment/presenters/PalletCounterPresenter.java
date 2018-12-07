package com.tiendas3b.almacen.shipment.presenters;

public interface PalletCounterPresenter {

    void countPallet(long tripDetailId);

    void getInfo(long tripDetailId);
}
