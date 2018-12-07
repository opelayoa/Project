package com.tiendas3b.almacen.db.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "ANSWER".
 */
public class Answer {

    private Long id;
    private String description;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Answer() {
    }

    public Answer(Long id) {
        this.id = id;
    }

    public Answer(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}