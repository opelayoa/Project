package com.tiendas3b.almacen.shipment.vrp.dto;

/**
 * Created by danflo on 03/01/2018 madafaka.
 */

public class EtapaDTO {

    private int ordenetapa;
    private long idviaje;
    private long origen;
    private long destino;
    private long tarimas;
    private double coorxori;
    private double cooryori;
    private double coorxdes;
    private double coorydes;
    private String actividad;
    private boolean terminado;

    public int getOrdenetapa() {
        return ordenetapa;
    }

    public void setOrdenetapa(int ordenetapa) {
        this.ordenetapa = ordenetapa;
    }

    public long getIdviaje() {
        return idviaje;
    }

    public void setIdviaje(long idviaje) {
        this.idviaje = idviaje;
    }

    public long getOrigen() {
        return origen;
    }

    public void setOrigen(long origen) {
        this.origen = origen;
    }

    public long getDestino() {
        return destino;
    }

    public void setDestino(long destino) {
        this.destino = destino;
    }

    public long getTarimas() {
        return tarimas;
    }

    public void setTarimas(long tarimas) {
        this.tarimas = tarimas;
    }

    public double getCoorxori() {
        return coorxori;
    }

    public void setCoorxori(double coorxori) {
        this.coorxori = coorxori;
    }

    public double getCooryori() {
        return cooryori;
    }

    public void setCooryori(double cooryori) {
        this.cooryori = cooryori;
    }

    public double getCoorxdes() {
        return coorxdes;
    }

    public void setCoorxdes(double coorxdes) {
        this.coorxdes = coorxdes;
    }

    public double getCoorydes() {
        return coorydes;
    }

    public void setCoorydes(double coorydes) {
        this.coorydes = coorydes;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public boolean isTerminado() {
        return terminado;
    }

    public void setTerminado(boolean terminado) {
        this.terminado = terminado;
    }
}
