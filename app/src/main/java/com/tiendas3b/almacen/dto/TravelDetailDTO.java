package com.tiendas3b.almacen.dto;

import java.io.Serializable;

/**
 * Created by dfa on 28/07/2016.
 */
public class TravelDetailDTO implements Serializable {

    private int regionId;
    private String date;
    private int truckId;
    private int actId;
    private String actDesc;
    private int scaffoldDownloadNum;
    private int destination;
    private float initialKm;
    private float finalKm;
    private float diffKm;
    private String initialTime;
    private String finalTime;
    private int diffTime;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTruckId() {
        return truckId;
    }

    public void setTruckId(int truckId) {
        this.truckId = truckId;
    }

    public int getActId() {
        return actId;
    }

    public void setActId(int actId) {
        this.actId = actId;
    }

    public String getActDesc() {
        return actDesc;
    }

    public void setActDesc(String actDesc) {
        this.actDesc = actDesc;
    }

    public int getScaffoldDownloadNum() {
        return scaffoldDownloadNum;
    }

    public void setScaffoldDownloadNum(int scaffoldDownloadNum) {
        this.scaffoldDownloadNum = scaffoldDownloadNum;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public float getInitialKm() {
        return initialKm;
    }

    public void setInitialKm(float initialKm) {
        this.initialKm = initialKm;
    }

    public float getFinalKm() {
        return finalKm;
    }

    public void setFinalKm(float finalKm) {
        this.finalKm = finalKm;
    }

    public float getDiffKm() {
        return diffKm;
    }

    public void setDiffKm(float diffKm) {
        this.diffKm = diffKm;
    }

    public String getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(String initialTime) {
        this.initialTime = initialTime;
    }

    public String getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(String finalTime) {
        this.finalTime = finalTime;
    }

    public int getDiffTime() {
        return diffTime;
    }

    public void setDiffTime(int diffTime) {
        this.diffTime = diffTime;
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

