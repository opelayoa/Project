package com.tiendas3b.almacen.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dfa on 15/04/2016.
 */
public class CaptureInputTransference implements Serializable{

    private List<InputTransferenceDetailDTO> toSend;
    private InputTransferenceVO inputTransference;
    private String cause;

    public CaptureInputTransference(){
    }

    public List<InputTransferenceDetailDTO> getToSend() {
        return toSend;
    }

    public void setToSend(List<InputTransferenceDetailDTO> toSend) {
        this.toSend = toSend;
    }

    public InputTransferenceVO getInputTransference() {
        return inputTransference;
    }

    public void setInputTransference(InputTransferenceVO inputTransference) {
        this.inputTransference = inputTransference;
    }
//    public long getWarehouseId() {
//        return warehouseId;
//    }
//
//    public void setWarehouseId(long warehouseId) {
//        this.warehouseId = warehouseId;
//    }
//
//    public long getStoreId() {
//        return storeId;
//    }
//
//    public void setStoreId(long storeId) {
//        this.storeId = storeId;
//    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
