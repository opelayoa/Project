package com.tiendas3b.almacen.dto;

import java.io.Serializable;

/**
 * Created by dfa on 13/05/2016.
 */
public class InputTransferenceVO implements Serializable {

    private Integer folio;
    private String date;
    private long regionId;
    private long storeId;

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }
}
