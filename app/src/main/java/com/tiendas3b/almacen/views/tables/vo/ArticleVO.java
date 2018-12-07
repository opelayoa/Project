package com.tiendas3b.almacen.views.tables.vo;

import java.io.Serializable;

/**
 * Created by dfa on 24/06/2016.
 */
public class ArticleVO implements Serializable {

    private long iclave;
    private String description;
//    private String unity;
    private Integer amount;
    private Long type;
    private Long obs;
    private Long obsLog;
    private Float cost;
    private String obsStr;

    public Long getObsLog() {
        return obsLog;
    }

    public void setObsLog(Long obsLog) {
        this.obsLog = obsLog;
    }

    public long getIclave() {
        return iclave;
    }

    public void setIclave(long iclave) {
        this.iclave = iclave;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public String getUnity() {
//        return unity;
//    }
//
//    public void setUnity(String unity) {
//        this.unity = unity;
//    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getObs() {
        return obs;
    }

    public void setObs(Long obs) {
        this.obs = obs;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public String getObsStr() {
        return obsStr;
    }

    public void setObsStr(String obsStr) {
        this.obsStr = obsStr;
    }
}
