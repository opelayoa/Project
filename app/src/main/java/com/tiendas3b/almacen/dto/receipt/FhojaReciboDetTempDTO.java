package com.tiendas3b.almacen.dto.receipt;

public class FhojaReciboDetTempDTO {

    private int num_pallets;
    private int cantidad_em;
    private int iclave;

    public FhojaReciboDetTempDTO(int num_pallets, int cantidad_em, int iclave) {
        this.num_pallets = num_pallets;
        this.cantidad_em = cantidad_em;
        this.iclave = iclave;
    }

    public int getNum_pallets() {
        return num_pallets;
    }

    public void setNum_pallets(int num_pallets) {
        this.num_pallets = num_pallets;
    }

    public int getCantidad_em() {
        return cantidad_em;
    }

    public void setCantidad_em(int cantidad_em) {
        this.cantidad_em = cantidad_em;
    }

    public int getIclave() {
        return iclave;
    }

    public void setIclave(int iclave) {
        this.iclave = iclave;
    }

}
