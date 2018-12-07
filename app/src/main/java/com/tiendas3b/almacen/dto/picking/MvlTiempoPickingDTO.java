package com.tiendas3b.almacen.dto.picking;

/**
 * Created by ruben.patino on 13/03/2018.
 */

public class MvlTiempoPickingDTO {
    public int tclave;
    public String fecha;
    public String clientesmn;
    public String tiempoinicial;
    public String tiempofinal;
    public String tiempopick;
    public String prodped;
    public String prodpick;
    public String prodfalta;
    public String prodincom;
    public String circuito;
    public String tiempoinicialconf;
    public String tiempofinalconf;
    public String tiempconfirma;

    public MvlTiempoPickingDTO(int tclave, String fecha, String clientesmn, String tiempoinicial, String tiempofinal, String tiempopick, String prodped, String prodpick, String prodfalta, String prodincom, String circuito, String tiempoinicialconf, String tiempofinalconf, String tiempoconfirma) {
        this.tclave = tclave;
        this.fecha = fecha;
        this.clientesmn = clientesmn;
        this.tiempoinicial = tiempoinicial;
        this.tiempofinal = tiempofinal;
        this.tiempopick = tiempopick;
        this.prodped = prodped;
        this.prodpick = prodpick;
        this.prodfalta = prodfalta;
        this.prodincom = prodincom;
        this.circuito = circuito;
        this.tiempoinicialconf = tiempoinicialconf;
        this.tiempofinalconf = tiempofinalconf;
        this.tiempconfirma = tiempoconfirma;
    }

    public int getTclave() {
        return tclave;
    }

    public void setTclave(int tclave) {
        this.tclave = tclave;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getClientesmn() {
        return clientesmn;
    }

    public void setClientesmn(String clientesmn) {
        this.clientesmn = clientesmn;
    }

    public String getTiempoinicial() {
        return tiempoinicial;
    }

    public void setTiempoinicial(String tiempoinicial) {
        this.tiempoinicial = tiempoinicial;
    }

    public String getTiempofinal() {
        return tiempofinal;
    }

    public void setTiempofinal(String tiempofinal) {
        this.tiempofinal = tiempofinal;
    }

    public String getTiempopick() {
        return tiempopick;
    }

    public void setTiempopick(String tiempopick) {
        this.tiempopick = tiempopick;
    }

    public String getProdped() {
        return prodped;
    }

    public void setProdped(String prodped) {
        this.prodped = prodped;
    }

    public String getProdpick() {
        return prodpick;
    }

    public void setProdpick(String prodpick) {
        this.prodpick = prodpick;
    }

    public String getProdfalta() {
        return prodfalta;
    }

    public void setProdfalta(String prodfalta) {
        this.prodfalta = prodfalta;
    }

    public String getProdincom() {
        return prodincom;
    }

    public void setProdincom(String prodincom) {
        this.prodincom = prodincom;
    }

    public String getCircuito() {
        return circuito;
    }

    public void setCircuito(String circuito) {
        this.circuito = circuito;
    }

    public String getTiempoinicialconf() {
        return tiempoinicialconf;
    }

    public void setTiempoinicialconf(String tiempoinicialconf) {
        this.tiempoinicialconf = tiempoinicialconf;
    }

    public String getTiempofinalconf() {
        return tiempofinalconf;
    }

    public void setTiempofinalconf(String tiempofinalconf) {
        this.tiempofinalconf = tiempofinalconf;
    }

    public String getTiempconfirma() {
        return tiempconfirma;
    }

    public void setTiempconfirma(String tiempconfirma) {
        this.tiempconfirma = tiempconfirma;
    }
}


