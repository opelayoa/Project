package com.tiendas3b.almacen.shipment.views;

public interface ShipmentView {

    void initProgressDialog(String title);

    void stopProgressDialog();

    void showError(String error);

    void showInfo(String info);

}
