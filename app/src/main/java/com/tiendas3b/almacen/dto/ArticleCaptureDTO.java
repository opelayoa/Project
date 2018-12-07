package com.tiendas3b.almacen.dto;

/**
 * Created by dfa on 21/06/2016.
 */
public class ArticleCaptureDTO {

    private Integer iclave;
    private Integer amount;
    private Long type;
    private Long obs;
    private Long obsLog;

    public ArticleCaptureDTO() {
        super();
    }

    public Long getObsLog() {
        return obsLog;
    }

    public void setObsLog(Long obsLog) {
        this.obsLog = obsLog;
    }

    public Integer getIclave() {
        return iclave;
    }

    public void setIclave(Integer iclave) {
        this.iclave = iclave;
    }

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

}
