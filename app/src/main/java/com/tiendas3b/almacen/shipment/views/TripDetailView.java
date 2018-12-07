package com.tiendas3b.almacen.shipment.views;

import com.tiendas3b.almacen.db.dao.TripDetail;

import java.util.List;

public interface TripDetailView {

    void setTripDetailSuccess(List<TripDetail> list);

    void startTrip();
}
