package com.tiendas3b.almacen.picking.orders;

import java.io.Serializable;
import java.util.List;

public class OrderDTO implements Serializable {

	private static final long serialVersionUID = 4855628048611193346L;
	private long storeId;
	private long regionId;
	private String date;
	private int paybill;
	private int status;
	private List<OrderDetailDTO> details;

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public long getRegionId() {
		return regionId;
	}

	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getPaybill() {
		return paybill;
	}

	public void setPaybill(int paybill) {
		this.paybill = paybill;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<OrderDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetailDTO> details) {
		this.details = details;
	}

}
