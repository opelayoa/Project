package com.tiendas3b.almacen.dto;

import java.io.Serializable;

/**
 * Created by dfa on 27/07/2016.
 */
public class GeneralTravelDTO implements Serializable {

    private int regionId;
    private int truckId;
    private String truck;
    private int status;
    private int storesNum;
    private int travelsNum;
    private int scaffoldNum;
    private int downloadsNum;
    private int scaffoldDownloadNum;
    private float travelKm;
    private float totalKm;
    private int travelTime;
    private int totalTime;
    private float toll;
    private float diesel;
    private float trafficTicket;
    private float otherCost;

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public int getTruckId() {
        return truckId;
    }

    public void setTruckId(int truckId) {
        this.truckId = truckId;
    }

    public String getTruck() {
        return truck;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }

    public int getStoresNum() {
        return storesNum;
    }

    public void setStoresNum(int storesNum) {
        this.storesNum = storesNum;
    }

    public int getTravelsNum() {
        return travelsNum;
    }

    public void setTravelsNum(int travelsNum) {
        this.travelsNum = travelsNum;
    }

    public int getScaffoldNum() {
        return scaffoldNum;
    }

    public void setScaffoldNum(int scaffoldNum) {
        this.scaffoldNum = scaffoldNum;
    }

    public int getDownloadsNum() {
        return downloadsNum;
    }

    public void setDownloadsNum(int downloadsNum) {
        this.downloadsNum = downloadsNum;
    }

    public int getScaffoldDownloadNum() {
        return scaffoldDownloadNum;
    }

    public void setScaffoldDownloadNum(int scaffoldDownloadNum) {
        this.scaffoldDownloadNum = scaffoldDownloadNum;
    }

    public float getTravelKm() {
        return travelKm;
    }

    public void setTravelKm(float travelKm) {
        this.travelKm = travelKm;
    }

    public float getTotalKm() {
        return totalKm;
    }

    public void setTotalKm(float totalKm) {
        this.totalKm = totalKm;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public float getToll() {
        return toll;
    }

    public void setToll(float toll) {
        this.toll = toll;
    }

    public float getDiesel() {
        return diesel;
    }

    public void setDiesel(float diesel) {
        this.diesel = diesel;
    }

    public float getTrafficTicket() {
        return trafficTicket;
    }

    public void setTrafficTicket(float trafficTicket) {
        this.trafficTicket = trafficTicket;
    }

    public float getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(float otherCost) {
        this.otherCost = otherCost;
    }

}
