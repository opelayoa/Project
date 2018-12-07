package com.tiendas3b.almacen.shipment.vrp.dto;

/**
 * Created by danflo on 03/01/2018 madafaka.
 */

public class ViajeDTO {

    private long idviaje;
    private long idalmacen;
    private long idtiporuta;
    private long camion;
    private String fecha;
    private int ordenviaje;
    private boolean terminado;

    public long getIdviaje() {
        return idviaje;
    }

    public void setIdviaje(long idviaje) {
        this.idviaje = idviaje;
    }

    public long getIdalmacen() {
        return idalmacen;
    }

    public void setIdalmacen(long idalmacen) {
        this.idalmacen = idalmacen;
    }

    public long getIdtiporuta() {
        return idtiporuta;
    }

    public void setIdtiporuta(long idtiporuta) {
        this.idtiporuta = idtiporuta;
    }

    public long getCamion() {
        return camion;
    }

    public void setCamion(long camion) {
        this.camion = camion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getOrdenviaje() {
        return ordenviaje;
    }

    public void setOrdenviaje(int ordenviaje) {
        this.ordenviaje = ordenviaje;
    }

    public boolean isTerminado() {
        return terminado;
    }

    public void setTerminado(boolean terminado) {
        this.terminado = terminado;
    }
}
