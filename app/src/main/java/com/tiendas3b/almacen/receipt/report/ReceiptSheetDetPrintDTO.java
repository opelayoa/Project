package com.tiendas3b.almacen.receipt.report;

import java.io.Serializable;

/**
 * Created by dfa on 24/04/2017.
 */
public class ReceiptSheetDetPrintDTO implements Serializable {

    private long iclave;
    private String barcode;
    private String description;
    private Integer packing;
    private Integer pieces;
    private Integer boxes;
    private Float unitCost;
    private Float totalCost;

    public long getIclave() {
        return iclave;
    }

    public void setIclave(long iclave) {
        this.iclave = iclave;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPacking() {
        return packing;
    }

    public void setPacking(Integer packing) {
        this.packing = packing;
    }

    public Integer getPieces() {
        return pieces;
    }

    public void setPieces(Integer pieces) {
        this.pieces = pieces;
    }

    public Integer getBoxes() {
        return boxes;
    }

    public void setBoxes(Integer boxes) {
        this.boxes = boxes;
    }

    public Float getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Float unitCost) {
        this.unitCost = unitCost;
    }

    public Float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Float totalCost) {
        this.totalCost = totalCost;
    }
}
