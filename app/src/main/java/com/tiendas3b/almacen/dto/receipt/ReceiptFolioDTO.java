package com.tiendas3b.almacen.dto.receipt;

public class ReceiptFolioDTO {

    private Integer folioEM;

    public ReceiptFolioDTO(Integer folioEM) {
        this.folioEM = folioEM;
    }

    public Integer getFolioEM() {
        return folioEM;
    }

    public void setFolioEM(Integer folioEM) {
        this.folioEM = folioEM;
    }

}
