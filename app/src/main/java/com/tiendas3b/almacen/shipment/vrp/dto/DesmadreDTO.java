package com.tiendas3b.almacen.shipment.vrp.dto;

import com.tiendas3b.almacen.db.dao.EqualsBase;

/**
 * Created by danflo on 04/01/2018 madafaka.
 */

public class DesmadreDTO extends EqualsBase {

    private long idviaje;
    private long idalmacen;
    private long idtiporuta;
    private long camion;
    private String fecha;
    private int ordenviaje;

    private int ordenetapa;
    private long origen;
    private long destino;
    private long tarimas;
    private double coorxori;
    private double cooryori;
    private double coorxdes;
    private double coorydes;
    private String actividad;

    private String tiporta;

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

    public int getOrdenetapa() {
        return ordenetapa;
    }

    public void setOrdenetapa(int ordenetapa) {
        this.ordenetapa = ordenetapa;
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

    public String getTiporta() {
        return tiporta;
    }

    public void setTiporta(String tiporta) {
        this.tiporta = tiporta;
    }

    @Override
    public Long getId() {
        return null;
    }
}
