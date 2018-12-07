package com.tiendas3b.almacen.shipment.util;

public interface ShipmentConstants {

    int TRIP_STATUS_NOT_YET_STARTED = 0;
    int TRIP_STATUS__STARTED = 1;
    int TRIP_STATUS_COMPLETED = 2;

    int TRIP_DETAIL_STATUS_NOT_YET_STARTED = 0;
    int TRIP_DETAIL_STATUS_STARTED = 1;
    int TRIP_DETAIL_STATUS_LOADED = 2;
    int TRIP_DETAIL_STATUS_DELIVERED = 3;
    int TRIP_DETAIL_STATUS_COLLECTED = 4;

    int SHIPMENT_CONTROL_UPGRADABLE = 0;
    int SHIPMENT_CONTROL_NOT_UPGRADABLE = 1;


    long ACTIVITY_LOAD = 1;
    long ACTIVITY_DOWNLOAD = 2;
    long ACTIVITY_FUEL = 3;
    long ACTIVITY_RETURN = 4;
    long ACTIVITY_ROUTE = 5;
    long ACTIVITY_REVIEW = 6;
    long ACTIVITY_OTHERS = 7;

}
