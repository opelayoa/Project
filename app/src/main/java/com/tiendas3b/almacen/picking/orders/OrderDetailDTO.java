package com.tiendas3b.almacen.picking.orders;

import java.io.Serializable;

public class OrderDetailDTO implements Serializable {

	private static final long serialVersionUID = 1134325832243641964L;
	private long iclave;
	private int existence;
	private int quantity;
	private String type;

	public long getIclave() {
		return iclave;
	}

	public void setIclave(final long iclave) {
		this.iclave = iclave;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(final int quantity) {
		this.quantity = quantity;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public int getExistence() {
		return existence;
	}

	public void setExistence(final int existence) {
		this.existence = existence;
	}

}
