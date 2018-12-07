package com.tiendas3b.almacen.shipment.presenters;

public interface TripDetailPresenter {
    void getTrips(long tripId);

    void startTrip(long tripId);
}
