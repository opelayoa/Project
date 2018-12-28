package com.tiendas3b.almacen.shipment.presenters;

import android.location.Location;

public interface TrackSelectionPresenter {

    void saveFuel(long tripId, double price, double liters, Location location);

    void deliverPallets(long tripId);

    void validateTrip(long tripId);

    void dispatch(Double odometro, Integer event, long tripId, Location location);
}
