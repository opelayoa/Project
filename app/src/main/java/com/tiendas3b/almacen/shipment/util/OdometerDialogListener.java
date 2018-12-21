package com.tiendas3b.almacen.shipment.util;

public interface OdometerDialogListener {

    void capture(Double odometer, Integer event);

    void showError(String message);
}
