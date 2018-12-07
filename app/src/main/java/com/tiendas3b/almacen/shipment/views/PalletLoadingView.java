package com.tiendas3b.almacen.shipment.views;

import com.tiendas3b.almacen.db.dao.TripDetail;

import java.util.List;

public interface PalletLoadingView {

    void setTripDetailSuccess(List<TripDetail> list);

    void showError(String error);

    void openCounter(long tripDetailId);

    void showNext();
}
