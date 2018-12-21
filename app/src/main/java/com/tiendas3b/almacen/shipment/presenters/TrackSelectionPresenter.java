package com.tiendas3b.almacen.shipment.presenters;

public interface TrackSelectionPresenter {

    void saveFuel(long tripId, double price, double liters);

    void deliverPallets(long tripId);

    void validateTrip(long tripId);

    void dispatch(Double odometro, Integer event, long tripId);
}
