package com.tiendas3b.almacen.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dfa on 15/04/2016.
 */
public class CaptureReturnVendor implements Serializable{

    private List<ReturnVendorCaptureDTO> toSend;
    private long warehouse;
    private int folio;
    private String cause;

    public CaptureReturnVendor(){
    }

    public List<ReturnVendorCaptureDTO> getToSend() {
        return toSend;
    }

    public void setToSend(List<ReturnVendorCaptureDTO> toSend) {
        this.toSend = toSend;
    }

    public long getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(long warehouse) {
        this.warehouse = warehouse;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }
}
