package com.tiendas3b.almacen.shipment.presenters;

import android.location.Location;

public interface StoreDownloadPresenter {

    void countPallet(long tripDetailId, Location location);

    void getInfo(long tripDetailId);
}
