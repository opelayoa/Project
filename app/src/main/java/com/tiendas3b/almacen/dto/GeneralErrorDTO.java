package com.tiendas3b.almacen.dto;

import java.io.Serializable;

/**
 * Created by dfa on 22/07/2016.
 */
public class GeneralErrorDTO implements Serializable {

    private Integer regionId;
    private String date;
    private Integer storesPick;
    private Integer storesRegion;
    private Integer scaffoldPick;
    private Integer teamMembers;
    private Integer errorsPick;
    private Float pctgErrScaffold;
    private Float pctgScaffoldMe;
    private Float pctgScaffoldHour;
    private Float pctgScaffoldMeHour;
    private Float errorsStoreDay;
    private Float errorsStoreRegionDay;
    private Float scaffoldStoresDay;
    private Float scaffoldStoresRegionDay;

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getStoresPick() {
        return storesPick;
    }

    public void setStoresPick(Integer storesPick) {
        this.storesPick = storesPick;
    }

    public Integer getStoresRegion() {
        return storesRegion;
    }

    public void setStoresRegion(Integer storesRegion) {
        this.storesRegion = storesRegion;
    }

    public Integer getScaffoldPick() {
        return scaffoldPick;
    }

    public void setScaffoldPick(Integer scaffoldPick) {
        this.scaffoldPick = scaffoldPick;
    }

    public Integer getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(Integer teamMembers) {
        this.teamMembers = teamMembers;
    }

    public Integer getErrorsPick() {
        return errorsPick;
    }

    public void setErrorsPick(Integer errorsPick) {
        this.errorsPick = errorsPick;
    }

    public Float getPctgErrScaffold() {
        return pctgErrScaffold;
    }

    public void setPctgErrScaffold(Float pctgErrScaffold) {
        this.pctgErrScaffold = pctgErrScaffold;
    }

    public Float getPctgScaffoldMe() {
        return pctgScaffoldMe;
    }

    public void setPctgScaffoldMe(Float pctgScaffoldMe) {
        this.pctgScaffoldMe = pctgScaffoldMe;
    }

    public Float getPctgScaffoldHour() {
        return pctgScaffoldHour;
    }

    public void setPctgScaffoldHour(Float pctgScaffoldHour) {
        this.pctgScaffoldHour = pctgScaffoldHour;
    }

    public Float getPctgScaffoldMeHour() {
        return pctgScaffoldMeHour;
    }

    public void setPctgScaffoldMeHour(Float pctgScaffoldMeHour) {
        this.pctgScaffoldMeHour = pctgScaffoldMeHour;
    }

    public Float getErrorsStoreDay() {
        return errorsStoreDay;
    }

    public void setErrorsStoreDay(Float errorsStoreDay) {
        this.errorsStoreDay = errorsStoreDay;
    }

    public Float getErrorsStoreRegionDay() {
        return errorsStoreRegionDay;
    }

    public void setErrorsStoreRegionDay(Float errorsStoreRegionDay) {
        this.errorsStoreRegionDay = errorsStoreRegionDay;
    }

    public Float getScaffoldStoresDay() {
        return scaffoldStoresDay;
    }

    public void setScaffoldStoresDay(Float scaffoldStoresDay) {
        this.scaffoldStoresDay = scaffoldStoresDay;
    }

    public Float getScaffoldStoresRegionDay() {
        return scaffoldStoresRegionDay;
    }

    public void setScaffoldStoresRegionDay(Float scaffoldStoresRegionDay) {
        this.scaffoldStoresRegionDay = scaffoldStoresRegionDay;
    }
}
