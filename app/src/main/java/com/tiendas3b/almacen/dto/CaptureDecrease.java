package com.tiendas3b.almacen.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dfa on 15/04/2016.
 */
public class CaptureDecrease implements Serializable{

//    private static final long serialVersionUID = 8798000861629523939L;
    private List<ArticleCaptureDTO> toSend;
    private long warehouse;

    public CaptureDecrease(){
    }

    public List<ArticleCaptureDTO> getToSend() {
        return toSend;
    }

    public void setToSend(List<ArticleCaptureDTO> toSend) {
        this.toSend = toSend;
    }

    public long getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(long warehouse) {
        this.warehouse = warehouse;
    }
}
