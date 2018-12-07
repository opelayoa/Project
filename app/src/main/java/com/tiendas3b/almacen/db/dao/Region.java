package com.tiendas3b.almacen.db.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "REGION".
 */
public class Region extends EqualsBase  {

    private Long id;
    private String name;
    private String address;
    private Integer platformNum;
    private String tels;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Region() {
    }

    public Region(Long id) {
        this.id = id;
    }

    public Region(Long id, String name, String address, Integer platformNum, String tels) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.platformNum = platformNum;
        this.tels = tels;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPlatformNum() {
        return platformNum;
    }

    public void setPlatformNum(Integer platformNum) {
        this.platformNum = platformNum;
    }

    public String getTels() {
        return tels;
    }

    public void setTels(String tels) {
        this.tels = tels;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}