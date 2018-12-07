package com.tiendas3b.almacen.dto;

import java.io.Serializable;

/**
 * Created by dfa on 09/08/2016.
 */
public class RoadmapDetailVO implements Serializable {

    private String date;
    private long activityId;
    private int origin;
    private int destination;

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

}
