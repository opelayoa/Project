package com.tiendas3b.almacen.shipment.views;

import com.tiendas3b.almacen.db.dao.TripDetail;

public interface PalletCounterView {

    void updateCounter(TripDetail tripDetail);

    void finishCount(TripDetail tripDetail);

    void setDetail(TripDetail tripDetail);
}
