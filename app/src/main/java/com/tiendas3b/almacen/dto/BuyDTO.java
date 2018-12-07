package com.tiendas3b.almacen.dto;

import java.io.Serializable;

/**
 * Created by danflo on 28/11/2016 madafaka.
 */
public class BuyDTO implements Serializable{

    private long id;
    private Double total;
    private String programedDate;
    private Integer platform;
    private String time;
    private String deliveryTime;
    private Integer checked;
    private long regionId;
    private long providerId;
    private int folio;
    private String mov;
    private int forced;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getProgramedDate() {
        return programedDate;
    }

    public void setProgramedDate(String programedDate) {
        this.programedDate = programedDate;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }

    /**
     *
     * @return pclave
     */
    public long getProviderId() {
        return providerId;
    }

    /**
     *
     * @param providerId es la pclave
     */
    public void setProviderId(long providerId) {
        this.providerId = providerId;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public String getMov() {
        return mov;
    }

    public void setMov(String mov) {
        this.mov = mov;
    }

    public int getForced() {
        return forced;
    }

    public void setForced(int forced) {
        this.forced = forced;
    }
}
