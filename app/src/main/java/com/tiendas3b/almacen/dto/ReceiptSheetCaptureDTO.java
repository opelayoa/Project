package com.tiendas3b.almacen.dto;

import java.io.Serializable;

/**
 * Created by danflo on 28/11/2016 madafaka.
 */
public class ReceiptSheetCaptureDTO implements Serializable {

    private Long id;
    private String dateTime;
    private String arriveTime;
    private String deliveryTime;
    private String departureTime;
    private Integer arrive;
    private Integer numReceivers;
    private Integer paletizado;
    private Float amountFact;
    private Float amountEm;
    private Float iva;
    private Float ieps;
    private Integer platform;
    private Long levelId;
    private Long dateTypeId;
    private long buyId;
    private String receiptDate;
    private int odc;
    private String facturaRef;
    private String deliveryman;
    private String receiver;

    public String getDeliveryman() {
        return deliveryman;
    }

    public void setDeliveryman(String deliveryman) {
        this.deliveryman = deliveryman;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getArrive() {
        return arrive;
    }

    public void setArrive(Integer arrive) {
        this.arrive = arrive;
    }

    public Integer getNumReceivers() {
        return numReceivers;
    }

    public void setNumReceivers(Integer numReceivers) {
        this.numReceivers = numReceivers;
    }

    public Integer getPaletizado() {
        return paletizado;
    }

    public void setPaletizado(Integer paletizado) {
        this.paletizado = paletizado;
    }

    public Float getAmountFact() {
        return amountFact;
    }

    public void setAmountFact(Float amountFact) {
        this.amountFact = amountFact;
    }

    public Float getAmountEm() {
        return amountEm;
    }

    public void setAmountEm(Float amountEm) {
        this.amountEm = amountEm;
    }

    public Float getIva() {
        return iva;
    }

    public void setIva(Float iva) {
        this.iva = iva;
    }

    public Float getIeps() {
        return ieps;
    }

    public void setIeps(Float ieps) {
        this.ieps = ieps;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Long getDateTypeId() {
        return dateTypeId;
    }

    public void setDateTypeId(Long dateTypeId) {
        this.dateTypeId = dateTypeId;
    }

    public long getBuyId() {
        return buyId;
    }

    public void setBuyId(long buyId) {
        this.buyId = buyId;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public int getOdc() {
        return odc;
    }

    public void setOdc(int odc) {
        this.odc = odc;
    }

    public String getFacturaRef() {
        return facturaRef;
    }

    public void setFacturaRef(String facturaRef) {
        this.facturaRef = facturaRef;
    }
}
