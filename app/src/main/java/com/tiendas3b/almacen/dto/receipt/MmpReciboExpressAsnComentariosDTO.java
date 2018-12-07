package com.tiendas3b.almacen.dto.receipt;

public class MmpReciboExpressAsnComentariosDTO {

    private String tclave;
    private Long pclave;
    private Integer folioodc;
    private Integer folioasn;
    private Integer idUsuario;
    private String comentarios;

    public MmpReciboExpressAsnComentariosDTO(String tclave, Long pclave, Integer folioodc, Integer folioasn,
                                             Integer idUsuario, String comentarios) {
        super();
        this.tclave = tclave;
        this.pclave = pclave;
        this.folioodc = folioodc;
        this.folioasn = folioasn;
        this.idUsuario = idUsuario;
        this.comentarios = comentarios;
    }

    public String getTclave() {
        return tclave;
    }

    public void setTclave(String tclave) {
        this.tclave = tclave;
    }

    public Long getPclave() {
        return pclave;
    }

    public void setPclave(Long pclave) {
        this.pclave = pclave;
    }

    public Integer getFolioodc() {
        return folioodc;
    }

    public void setFolioodc(Integer folioodc) {
        this.folioodc = folioodc;
    }

    public Integer getFolioasn() {
        return folioasn;
    }

    public void setFolioasn(Integer folioasn) {
        this.folioasn = folioasn;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

}
