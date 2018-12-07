package com.tiendas3b.almacen.dto;

import java.io.Serializable;

/**
 * Created by danflo on 12/10/2016 madafaka.
 */
public class GlobalDecreaseDTO implements Serializable {

    private int month;
    private double cost;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

}
