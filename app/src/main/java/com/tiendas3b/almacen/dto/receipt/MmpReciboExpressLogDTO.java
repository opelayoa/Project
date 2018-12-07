package com.tiendas3b.almacen.dto.receipt;

public class MmpReciboExpressLogDTO {

    private String idAlmacen;
    private Long idProveedor;
    private Integer odc;
    private String fecha;
    private String hora;
    private Integer idOrden;
    private Integer idProceso;
    private String descripcion;
    private String usuario;

    public MmpReciboExpressLogDTO(String idAlmacen, Long idProveedor, Integer odc, String fecha, String hora, Integer idOrden, Integer idProceso, String descripcion) {
        this.idAlmacen = idAlmacen;
        this.idProveedor = idProveedor;
        this.odc = odc;
        this.fecha = fecha;
        this.hora = hora;
        this.idOrden = idOrden;
        this.idProceso = idProceso;
        this.descripcion = descripcion;
    }

    public String getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(String idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Integer getOdc() {
        return odc;
    }

    public void setOdc(Integer odc) {
        this.odc = odc;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Integer getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Integer idOrden) {
        this.idOrden = idOrden;
    }

    public Integer getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(Integer idProceso) {
        this.idProceso = idProceso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
