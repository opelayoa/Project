package com.tiendas3b.almacen.dto.epackage;

/**
 * Created by danflo on 01/08/2017 madafaka.
 */

import java.io.Serializable;

public class EPackageLeaveDTO implements Serializable {

    private boolean status;
    private long packageId;
    private long camClave;
    private long almacen;
    private long statusLeave;
    private long tarjetStatus;
    private String fecha;

    public EPackageLeaveDTO(boolean status, long packageId, long camClave, long almacen, long statusLeave, long tarjetStatus, String fecha) {
        this.status = status;
        this.packageId = packageId;
        this.camClave = camClave;
        this.almacen = almacen;
        this.statusLeave = statusLeave;
        this.tarjetStatus = tarjetStatus;
        this.fecha = fecha;
    }

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

    public long getStatusLeave() {
        return statusLeave;
    }

    public void setStatusLeave(long statusLeave) {
        this.statusLeave = statusLeave;
    }

    public long getTarjetStatus() {
        return tarjetStatus;
    }

    public void setTarjetStatus(long tarjetStatus) {
        this.tarjetStatus = tarjetStatus;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
