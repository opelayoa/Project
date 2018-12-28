package com.tiendas3b.almacen.shipment.presenters;

import android.location.Location;

public interface TripDetailPresenter {
    void getTrips(long tripId);

    void startTrip(long tripId, Location location);
}
