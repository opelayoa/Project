package com.tiendas3b.almacen.shipment.views;

import com.tiendas3b.almacen.db.dao.TripDetail;

public interface StoreUploadView {

    void updateCounter(TripDetail tripDetail);

    void setDetail(TripDetail tripDetail);
}
