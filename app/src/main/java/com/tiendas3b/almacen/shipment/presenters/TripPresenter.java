package com.tiendas3b.almacen.shipment.presenters;

public interface TripPresenter {
    void getTrips();

    void validateSelectedTrip(long sequence, int status);

    void showInfo();
}
