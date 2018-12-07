package com.tiendas3b.almacen.dto;

import java.io.Serializable;

/**
 * Created by danflo on 28/11/2016 madafaka.
 */
public class CaptureDocCompras implements Serializable {

    private BuyDTO buy;
    private ReceiptSheetCaptureDTO receiptSheet;

    public BuyDTO getBuy() {
        return buy;
    }

    public void setBuy(BuyDTO buy) {
        this.buy = buy;
    }

    public ReceiptSheetCaptureDTO getReceiptSheet() {
        return receiptSheet;
    }

    public void setReceiptSheet(ReceiptSheetCaptureDTO receiptSheet) {
        this.receiptSheet = receiptSheet;
    }
}
