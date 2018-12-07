package com.tiendas3b.almacen.db.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "ARTICLE_DECREASE".
 */
public class ArticleDecrease extends EqualsBase  {

    private Long id;
    private Long iclave;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public ArticleDecrease() {
    }

    public ArticleDecrease(Long id) {
        this.id = id;
    }

    public ArticleDecrease(Long id, Long iclave) {
        this.id = id;
        this.iclave = iclave;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIclave() {
        return iclave;
    }

    public void setIclave(Long iclave) {
        this.iclave = iclave;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
