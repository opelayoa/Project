package com.tiendas3b.almacen.receipt.express;

/**
 * Created by danflo on 02/10/2017 madafaka.
 */

public class HeaderExpressDTO {

    private int folio;
//    private int odc;
//    private int providerId;
//    private int regionId;
    private int articlesQuantity;
    private int scaffoldQuantity;
    private String status;
    private boolean received;
    private String facturaRef;
    private String serie;
    private float iva;
    private float ieps;
    private String facturaSubtotal;
    private String facturaTotal;

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public int getArticlesQuantity() {
        return articlesQuantity;
    }

    public void setArticlesQuantity(int articlesQuantity) {
        this.articlesQuantity = articlesQuantity;
    }

    public int getScaffoldQuantity() {
        return scaffoldQuantity;
    }

    public void setScaffoldQuantity(int scaffoldQuantity) {
        this.scaffoldQuantity = scaffoldQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public String getFacturaRef() {
        return facturaRef;
    }

    public void setFacturaRef(String facturaRef) {
        this.facturaRef = facturaRef;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public float getIva() {
        return iva;
    }

    public void setIva(float iva) {
        this.iva = iva;
    }

    public float getIeps() {
        return ieps;
    }

    public void setIeps(float ieps) {
        this.ieps = ieps;
    }

    public String getFacturaSubtotal() {
        return facturaSubtotal;
    }

    public void setFacturaSubtotal(String facturaSubtotal) {
        this.facturaSubtotal = facturaSubtotal;
    }

    public String getFacturaTotal() {
        return facturaTotal;
    }

    public void setFacturaTotal(String facturaTotal) {
        this.facturaTotal = facturaTotal;
    }
}
