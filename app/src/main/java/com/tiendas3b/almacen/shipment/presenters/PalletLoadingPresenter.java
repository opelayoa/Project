package com.tiendas3b.almacen.shipment.presenters;

import android.location.Location;

public interface PalletLoadingPresenter {
    void validateStore(long tripDetailId, long tripId, Location location);

    void getTrips(long tripId);

    void validateTotalStoresLoaded(long tripId);
}
