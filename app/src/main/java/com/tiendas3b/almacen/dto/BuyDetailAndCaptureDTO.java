package com.tiendas3b.almacen.dto;

import com.tiendas3b.almacen.db.dao.ReceiptSheetDetailCapture;

/**
 * Created by danflo on 02/12/2016 madafaka.
 */
public class BuyDetailAndCaptureDTO {

    private BuyDetailDTO buyDetail;
    private ReceiptSheetDetailCapture capture;

    public BuyDetailDTO getBuyDetail() {
        return buyDetail;
    }

    public void setBuyDetail(BuyDetailDTO buyDetail) {
        this.buyDetail = buyDetail;
    }

    public ReceiptSheetDetailCapture getCapture() {
        return capture;
    }

    public void setCapture(ReceiptSheetDetailCapture capture) {
        this.capture = capture;
    }
}
