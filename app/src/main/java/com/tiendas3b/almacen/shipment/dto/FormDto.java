package com.tiendas3b.almacen.shipment.dto;

import com.tiendas3b.almacen.db.dao.Form;
import com.tiendas3b.almacen.shipment.activities.ListTripDetailActivity;

import java.util.List;

public class FormDto {
    private Long id;
    private String description;
    private List<RelationshipDto> relationships;

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

    public List<RelationshipDto> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<RelationshipDto> relationships) {
        this.relationships = relationships;
    }

    @Override
    public String toString() {
        return "FormDto{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", relationships=" + relationships +
                '}';
    }
}
