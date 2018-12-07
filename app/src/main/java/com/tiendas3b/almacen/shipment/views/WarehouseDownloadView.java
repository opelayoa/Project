package com.tiendas3b.almacen.shipment.views;

public interface WarehouseDownloadView {

    void updateCounter(int items);

    void setDetail(int totalItems);

    void finishCount();
}
