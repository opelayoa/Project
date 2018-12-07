package com.tiendas3b.almacen.receipt.express;

/**
 * Created by danflo on 05/10/2017 madafaka.
 */

public class ExpressDetailDTO {

    private int folio;//... mejor de una vez a bd... mejor no jajaja
    private int folioOdc;
    private short scaffoldNum;
    private int providerId;
    private int iclave;
    //    private int articleDesc;
    private int cantidad;//esta en _enc, se usa para a granel
    private String date;
    private String user;
    //    private int regionId;
    private String brandType;
    private String barcode;
    //    private String providerName;
    private String unitType;
    private int folioReceipt;
    private String dateUpdate;
    private String lote;
    private String expirationDate;
    private String unit;
    private String facturaRef;
    private String serie;
    private float iva;
    private float ieps;
    private float subtotal;
    private float total;
    private boolean received;

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public int getFolioOdc() {
        return folioOdc;
    }

    public void setFolioOdc(int folioOdc) {
        this.folioOdc = folioOdc;
    }

    public short getScaffoldNum() {
        return scaffoldNum;
    }

    public void setScaffoldNum(short scaffoldNum) {
        this.scaffoldNum = scaffoldNum;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public int getIclave() {
        return iclave;
    }

    public void setIclave(int iclave) {
        this.iclave = iclave;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBrandType() {
        return brandType;
    }

    public void setBrandType(String brandType) {
        this.brandType = brandType;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public int getFolioReceipt() {
        return folioReceipt;
    }

    public void setFolioReceipt(int folioReceipt) {
        this.folioReceipt = folioReceipt;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }
}
