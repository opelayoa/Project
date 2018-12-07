package com.tiendas3b.almacen.dto;

import java.io.Serializable;

/**
 * Created by dfa on 12/05/2016.
 */
public class InputTransferenceDetailDTO extends ArticleCaptureDTO implements Serializable{

//    private Long id;
//    private String barcode;
//    private String description;
//    private String unity;
//    private Integer packing;
    private Integer amountSmn;
    private Integer amountInv;
    private Integer amountMer;
    private Integer amountReg;
//    private Long type;
//    private Long obs;
    private Float cost;

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getBarcode() {
//        return barcode;
//    }
//
//    public void setBarcode(String barcode) {
//        this.barcode = barcode;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getUnity() {
//        return unity;
//    }
//
//    public void setUnity(String unity) {
//        this.unity = unity;
//    }
//
//    public Integer getPacking() {
//        return packing;
//    }
//
//    public void setPacking(Integer packing) {
//        this.packing = packing;
//    }

    public Integer getAmountSmn() {
        return amountSmn;
    }

    public void setAmountSmn(Integer amountSmn) {
        this.amountSmn = amountSmn;
    }

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

//    public Long getType() {
//        return type;
//    }
//
//    public void setType(Long type) {
//        this.type = type;
//    }
//
//    public Long getObs() {
//        return obs;
//    }
//
//    public void setObs(Long obs) {
//        this.obs = obs;
//    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }
}
