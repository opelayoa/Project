package com.tiendas3b.almacen.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dfa on 09/08/2016.
 */
public class RoadmapVO implements Serializable {

    private String date;
    private long truckId;
    private int travel;
//    private long driverId;
    private List<RoadmapDetailVO> activities;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTruckId() {
        return truckId;
    }

    public void setTruckId(long truckId) {
        this.truckId = truckId;
    }

    public int getTravel() {
        return travel;
    }

    public void setTravel(int travel) {
        this.travel = travel;
    }

//    public long getDriverId() {
//        return driverId;
//    }
//
//    public void setDriverId(long driverId) {
//        this.driverId = driverId;
//    }

    public List<RoadmapDetailVO> getActivities() {
        return activities;
    }

    public void setActivities(List<RoadmapDetailVO> activities) {
        this.activities = activities;
    }
}
