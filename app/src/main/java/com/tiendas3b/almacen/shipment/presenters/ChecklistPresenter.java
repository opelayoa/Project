package com.tiendas3b.almacen.shipment.presenters;

public interface ChecklistPresenter {

    void getForm();

    void generateLog(long tripId, String message);
}
