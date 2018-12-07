package com.tiendas3b.almacen.receipt.report;

import java.util.List;

/**
 * Created by danflo on 27/04/2017 madafaka.
 */

public class ReceiptSheetPrintDTO {

    private long regionId;
    private long providerId;
    private String receiptDate;
    private int folio;
    private int odc;
    private String facturaRef;
    private float subtotal;
    private float iva;
    private float ieps;
    private float total;
    private float totalBill;
    private String caption;
    private List<ReceiptSheetDetPrintDTO> details;

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(final long regionId) {
        this.regionId = regionId;
    }

    public long getProviderId() {
        return providerId;
    }

    public void setProviderId(final long providerId) {
        this.providerId = providerId;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(final String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(final int folio) {
        this.folio = folio;
    }

    public int getOdc() {
        return odc;
    }

    public void setOdc(final int odc) {
        this.odc = odc;
    }

    public String getFacturaRef() {
        return facturaRef;
    }

    public void setFacturaRef(final String facturaRef) {
        this.facturaRef = facturaRef;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(final float subtotal) {
        this.subtotal = subtotal;
    }

    public float getIva() {
        return iva;
    }

    public void setIva(final float iva) {
        this.iva = iva;
    }

    public float getIeps() {
        return ieps;
    }

    public void setIeps(final float ieps) {
        this.ieps = ieps;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(final float total) {
        this.total = total;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(final String caption) {
        this.caption = caption;
    }

    public List<ReceiptSheetDetPrintDTO> getDetails() {
        return details;
    }

    public void setDetails(final List<ReceiptSheetDetPrintDTO> details) {
        this.details = details;
    }

    public float getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(float totalBill) {
        this.totalBill = totalBill;
    }
}
