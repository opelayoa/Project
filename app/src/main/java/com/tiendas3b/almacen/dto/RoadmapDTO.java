package com.tiendas3b.almacen.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dfa on 09/08/2016.
 */
public class RoadmapDTO implements Serializable {

    private String date;
    private long truckId;
    private long travel;
    private long driverId;
    private List<RoadmapDetailDTO> activities;

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

    public long getTravel() {
        return travel;
    }

    public void setTravel(long travel) {
        this.travel = travel;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public List<RoadmapDetailDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<RoadmapDetailDTO> activities) {
        this.activities = activities;
    }
}
