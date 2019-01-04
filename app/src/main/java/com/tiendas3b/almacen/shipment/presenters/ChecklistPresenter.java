package com.tiendas3b.almacen.shipment.presenters;

import android.location.Location;

public interface ChecklistPresenter {

    void getForm();

    void generateLog(long tripId, String message, Double odometer, Location location);

}
