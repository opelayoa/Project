package com.tiendas3b.almacen.views.tables.vo;

import java.io.Serializable;

/**
 * Created by dfa on 28/06/2016.
 */
public class InputTransferenceArticleVO extends ArticleVO implements Serializable{

    private Integer amountInv;
    private Integer amountMer;
    private Integer amountReg;
    private Integer amountSmn;

    public Integer getAmountInv() {
        return amountInv;
    }

    public void setAmountInv(Integer amountInv) {
        this.amountInv = amountInv;
    }

    public Integer getAmountMer() {
        return amountMer;
    }

    public void setAmountMer(Integer amountMer) {
        this.amountMer = amountMer;
    }

    public Integer getAmountReg() {
        return amountReg;
    }

    public void setAmountReg(Integer amountReg) {
        this.amountReg = amountReg;
    }

    public Integer getAmountSmn() {
        return amountSmn;
    }

    public void setAmountSmn(Integer amountSmn) {
        this.amountSmn = amountSmn;
    }
}
