package com.tiendas3b.almacen.shipment.views;

import com.tiendas3b.almacen.db.dao.Trip;

import java.util.List;

public interface TripView {

    void setTripSuccess(List<Trip> list);

    void showError(String message);

    void showDetails(long tripId);

    void showButton();
}
