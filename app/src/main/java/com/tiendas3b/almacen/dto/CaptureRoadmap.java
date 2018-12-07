package com.tiendas3b.almacen.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dfa on 15/04/2016.
 */
public class CaptureRoadmap implements Serializable{

    private List<RoadmapDTO> toSend;
    private long regionId;

    public List<RoadmapDTO> getToSend() {
        return toSend;
    }

    public void setToSend(List<RoadmapDTO> toSend) {
        this.toSend = toSend;
    }

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }
}
