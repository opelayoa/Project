package com.tiendas3b.almacen.picking.orders;

/**
 * Created by danflo on 14/03/2017 madafaka.
 */

public class OrderDetailTableDTO extends OrderDetailCaptureDTO {

    private int iclave;
    private int quantity;
    private Integer fatherIclave;
    private String type;
    private int packing;
    private boolean child;

    public Integer getFatherIclave() {
        return fatherIclave;
    }

    public void setFatherIclave(final Integer fatherIclave) {
        this.fatherIclave = fatherIclave;
    }

    public boolean isChild() {
        return child;
    }

    public void setChild(final boolean child) {
        this.child = child;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getIclave() {
        return iclave;
    }

    public void setIclave(int iclave) {
        this.iclave = iclave;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPacking() {
        return packing;
    }

    public void setPacking(int packing) {
        this.packing = packing;
    }
}
