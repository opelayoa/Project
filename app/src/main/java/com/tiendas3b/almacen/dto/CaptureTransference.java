package com.tiendas3b.almacen.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dfa on 15/04/2016.
 */
public class CaptureTransference implements Serializable{

//    private static final long serialVersionUID = 8798000861629523939L;
    private List<ArticleCaptureDTO> toSend;
    private long warehouseId;
    private long storeId;
    private String cause;

    public CaptureTransference(){
    }

    public List<ArticleCaptureDTO> getToSend() {
        return toSend;
    }

    public void setToSend(List<ArticleCaptureDTO> toSend) {
        this.toSend = toSend;
    }

    public long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
