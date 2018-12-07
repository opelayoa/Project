package com.tiendas3b.almacen.dto.receipt;

public class FMuestrasDTO {

    private String tclave;
    private Long iclave;
    private Long pclave;
    private String fecha;
    private Integer odc;
    private String factura;
    private String lote;
    private String fechaCaducidad;
    private Integer cantidad;
    private Integer dpfolio;
    private Integer userId;

    public FMuestrasDTO() {}

    public FMuestrasDTO(String tclave, Long iclave, Long pclave, String fecha, Integer odc, String factura,
                        String lote, String fechaCaducidad, Integer cantidad, Integer dpfolio, Integer userId) {
        super();
        this.tclave = tclave;
        this.iclave = iclave;
        this.pclave = pclave;
        this.fecha = fecha;
        this.odc = odc;
        this.factura = factura;
        this.lote = lote;
        this.fechaCaducidad = fechaCaducidad;
        this.cantidad = cantidad;
        this.dpfolio = dpfolio;
        this.userId = userId;
    }

    public String getTclave() {
        return tclave;
    }

    public void setTclave(String tclave) {
        this.tclave = tclave;
    }

    public Long getIclave() {
        return iclave;
    }

    public void setIclave(Long iclave) {
        this.iclave = iclave;
    }

    public Long getPclave() {
        return pclave;
    }

    public void setPclave(Long pclave) {
        this.pclave = pclave;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getOdc() {
        return odc;
    }

    public void setOdc(Integer odc) {
        this.odc = odc;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getDpfolio() { return dpfolio; }

    public void setDpfolio(Integer dpfolio) { this.dpfolio = dpfolio; }
}
