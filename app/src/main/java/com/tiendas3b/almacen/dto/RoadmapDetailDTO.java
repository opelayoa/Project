package com.tiendas3b.almacen.dto;

import java.io.Serializable;

/**
 * Created by dfa on 09/08/2016.
 */
public class RoadmapDetailDTO implements Serializable {

    private String date;
    private String endDate;
    private long activityId;
    private int scaffold;
    private int scaffoldDownload;
    private int origin;
    private int destination;
    private float initialKm;
    private float finalKm;
    private String initialTime;
    private String finalTime;
    private float toll;
    private float diesel;
    private float trafficTicket;
    private float otherCost;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public int getScaffold() {
        return scaffold;
    }

    public void setScaffold(int scaffold) {
        this.scaffold = scaffold;
    }

    public int getScaffoldDownload() {
        return scaffoldDownload;
    }

    public void setScaffoldDownload(int scaffoldDownload) {
        this.scaffoldDownload = scaffoldDownload;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
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

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
