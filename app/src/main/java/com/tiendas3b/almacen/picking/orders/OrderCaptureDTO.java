package com.tiendas3b.almacen.picking.orders;

import java.io.Serializable;
import java.util.List;

/**
 * Created by danflo on 14/03/2017 madafaka.
 */

public class OrderCaptureDTO implements Serializable{

    private int paybill;
    private List<OrderDetailCaptureDTO> details;

    public int getPaybill() {
        return paybill;
    }

    public void setPaybill(int paybill) {
        this.paybill = paybill;
    }

    public List<OrderDetailCaptureDTO> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetailCaptureDTO> details) {
        this.details = details;
    }
}
