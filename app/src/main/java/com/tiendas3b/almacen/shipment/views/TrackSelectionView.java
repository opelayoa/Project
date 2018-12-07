package com.tiendas3b.almacen.shipment.views;

import com.tiendas3b.almacen.db.dao.TripDetail;

public interface TrackSelectionView {

    void showError(String error);

    void showMessage(String message);

    void deliverPallets(long tripId);

    void showToWarehouse();

    void resetValues();
}
