package com.tiendas3b.almacen.shipment.presenters;

public interface PalletLoadingPresenter {
    void validateStore(long tripDetailId, long tripId);

    void getTrips(long tripId);

    void validateTotalStoresLoaded(long tripId);
}
