package com.tiendas3b.almacen.dto;

import java.io.Serializable;

/**
 * Created by danflo on 17/11/2016 madafaka.
 */

public class BuyDetailDTO implements Serializable{

    private Long id;
    private Integer articleAmount;
    private Integer iclave;
    private String barcode;
    private int balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getArticleAmount() {
        return articleAmount;
    }

    public void setArticleAmount(Integer articleAmount) {
        this.articleAmount = articleAmount;
    }

    public Integer getIclave() {
        return iclave;
    }

    public void setIclave(Integer iclave) {
        this.iclave = iclave;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
