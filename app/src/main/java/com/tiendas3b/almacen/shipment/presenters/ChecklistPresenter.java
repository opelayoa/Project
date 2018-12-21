package com.tiendas3b.almacen.shipment.presenters;

import com.google.android.gms.location.LocationCallback;

public interface ChecklistPresenter {

    void getForm();

    void generateLog(long tripId, String message, Double odometer);
}
