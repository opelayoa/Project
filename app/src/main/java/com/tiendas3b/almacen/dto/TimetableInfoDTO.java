package com.tiendas3b.almacen.dto;

import com.tiendas3b.almacen.db.dao.Utilization;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dfa on 25/05/2016.
 */
public class TimetableInfoDTO implements Serializable{

    private List<Utilization> utilization;
//    private List<Utilization> pallets;

    public List<Utilization> getUtilization() {
        return utilization;
    }

    public void setUtilization(List<Utilization> utilization) {
        this.utilization = utilization;
    }
}
