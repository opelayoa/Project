package com.tiendas3b.almacen.dto.epackage;

/**
 * Created by danflo on 01/08/2017 madafaka.
 */

import java.io.Serializable;

public class PositionPackageDTO implements Serializable {

    private boolean status;
    private long packageId;
    private long positionId;
    private long levelId;
    private long rackId;
    private long areaId;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(final boolean status) {
        this.status = status;
    }

    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(final long packageId) {
        this.packageId = packageId;
    }

    public long getPositionId() {
        return positionId;
    }

    public void setPositionId(final long positionId) {
        this.positionId = positionId;
    }

    public long getLevelId() {
        return levelId;
    }

    public void setLevelId(final long levelId) {
        this.levelId = levelId;
    }

    public long getRackId() {
        return rackId;
    }

    public void setRackId(final long rackId) {
        this.rackId = rackId;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(final long areaId) {
        this.areaId = areaId;
    }

}
