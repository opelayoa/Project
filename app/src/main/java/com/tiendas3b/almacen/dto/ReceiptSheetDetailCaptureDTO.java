package com.tiendas3b.almacen.dto;

import java.io.Serializable;

/**
 * Created by danflo on 01/12/2016 madafaka.
 */
public class ReceiptSheetDetailCaptureDTO implements Serializable {

    private int odcAmount;
    private int billAmount;
    private int receivedAmount;
//    private int balanceAmount;
    private int paletizado;
    private int paletizadoCount;
    private long checkId;
    private int sample;
    private int packing;
    private long rejectionReasonId;
    private String rejectionReasonStr;
    private String expiryDate;
    private String lote;
    private String comments;
    private long buyDetailId;
    private String receiptDate;
    private long providerId;
    private long iclave;
    private long regionId;
    private long deviceId;
    private int odc;

    public long getRejectionReasonId() {
        return rejectionReasonId;
    }

    public void setRejectionReasonId(long rejectionReasonId) {
        this.rejectionReasonId = rejectionReasonId;
    }

    public int getOdcAmount() {
        return odcAmount;
    }

    public void setOdcAmount(int odcAmount) {
        this.odcAmount = odcAmount;
    }

    public int getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(int billAmount) {
        this.billAmount = billAmount;
    }

    public int getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(int receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

//    public int getBalanceAmount() {
//        return balanceAmount;
//    }
//
//    public void setBalanceAmount(int balanceAmount) {
//        this.balanceAmount = balanceAmount;
//    }

    public int getPaletizado() {
        return paletizado;
    }

    public void setPaletizado(int paletizado) {
        this.paletizado = paletizado;
    }

    public int getPaletizadoCount() {
        return paletizadoCount;
    }

    public void setPaletizadoCount(int paletizadoCount) {
        this.paletizadoCount = paletizadoCount;
    }

    public long getCheckId() {
        return checkId;
    }

    public void setCheckId(long checkId) {
        this.checkId = checkId;
    }

    public int getSample() {
        return sample;
    }

    public void setSample(int sample) {
        this.sample = sample;
    }

    public int getPacking() {
        return packing;
    }

    public void setPacking(int packing) {
        this.packing = packing;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public long getBuyDetailId() {
        return buyDetailId;
    }

    public void setBuyDetailId(long buyDetailId) {
        this.buyDetailId = buyDetailId;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public long getProviderId() {
        return providerId;
    }

    public void setProviderId(long providerId) {
        this.providerId = providerId;
    }

    public long getIclave() {
        return iclave;
    }

    public void setIclave(long iclave) {
        this.iclave = iclave;
    }

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }

    public int getOdc() {
        return odc;
    }

    public void setOdc(int odc) {
        this.odc = odc;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getRejectionReasonStr() {
        return rejectionReasonStr;
    }

    public void setRejectionReasonStr(String rejectionReasonStr) {
        this.rejectionReasonStr = rejectionReasonStr;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }
}
