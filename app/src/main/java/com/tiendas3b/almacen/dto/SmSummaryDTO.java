package com.tiendas3b.almacen.dto;

import java.io.Serializable;

/**
 * Created by dfa on 26/07/2016.
 */
public class SmSummaryDTO implements Serializable {

    private int regionId;
    private String date;
    private int atmrefCount;
    private int icalveCount;
    private int atmcantCount;
    private float atmcostCount;
    private float atmsaleCount;

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAtmrefCount() {
        return atmrefCount;
    }

    public void setAtmrefCount(int atmrefCount) {
        this.atmrefCount = atmrefCount;
    }

    public int getIcalveCount() {
        return icalveCount;
    }

    public void setIcalveCount(int icalveCount) {
        this.icalveCount = icalveCount;
    }

    public int getAtmcantCount() {
        return atmcantCount;
    }

    public void setAtmcantCount(int atmcantCount) {
        this.atmcantCount = atmcantCount;
    }

    public float getAtmcostCount() {
        return atmcostCount;
    }

    public void setAtmcostCount(float atmcostCount) {
        this.atmcostCount = atmcostCount;
    }

    public float getAtmsaleCount() {
        return atmsaleCount;
    }

    public void setAtmsaleCount(float atmsaleCount) {
        this.atmsaleCount = atmsaleCount;
    }

}
