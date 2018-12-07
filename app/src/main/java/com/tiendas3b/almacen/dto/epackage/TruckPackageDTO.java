package com.tiendas3b.almacen.dto.epackage;

/**
 * Created by danflo on 01/08/2017 madafaka.
 */

import java.io.Serializable;

public class TruckPackageDTO implements Serializable {

    private int status;
    private long packageId;
    private long camClave;
    private long almacen;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(final long packageId) {
        this.packageId = packageId;
    }

    public long getCamClave() {
        return camClave;
    }

    public void setCamClave(long camClave) {
        this.camClave = camClave;
    }

    public long getAlmacen() {
        return almacen;
    }

    public void setAlmacen(long almacen) {
        this.almacen = almacen;
    }
}
