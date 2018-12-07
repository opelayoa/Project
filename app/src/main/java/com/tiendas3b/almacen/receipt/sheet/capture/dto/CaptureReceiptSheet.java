package com.tiendas3b.almacen.receipt.sheet.capture.dto;

import com.tiendas3b.almacen.db.dao.ReceiptSheetCapture;
import com.tiendas3b.almacen.dto.BuyDTO;

import java.io.Serializable;

/**
 * Created by danflo on 08/12/2016 madafaka.
 */
public class CaptureReceiptSheet implements Serializable {

    private ReceiptSheetCapture capture;
    private BuyDTO buy;

    public ReceiptSheetCapture getCapture() {
        return capture;
    }

    public void setCapture(ReceiptSheetCapture capture) {
        this.capture = capture;
    }

    public BuyDTO getBuy() {
        return buy;
    }

    public void setBuy(BuyDTO buy) {
        this.buy = buy;
    }
}
