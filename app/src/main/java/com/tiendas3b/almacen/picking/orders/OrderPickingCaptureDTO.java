package com.tiendas3b.almacen.picking.orders;

import java.io.Serializable;
import java.util.List;

/**
 * Created by danflo on 14/03/2017 madafaka.
 */

public class OrderPickingCaptureDTO implements Serializable {

//    private long id;
    private long regionId;
    private long storeId;
    private ScanTimeDTO scanTimeDTO;
    private List<OrderCaptureDTO> orders;//no cambiar tipo de dato

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public List<OrderCaptureDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderCaptureDTO> orders) {
        this.orders = orders;
    }

    public ScanTimeDTO getScanTimeDTO() {
        return scanTimeDTO;
    }

    public void setScanTimeDTO(ScanTimeDTO scanTimeDTO) {
        this.scanTimeDTO = scanTimeDTO;
    }
}
