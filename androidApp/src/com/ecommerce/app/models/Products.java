package com.ecommerce.app.models;

import java.io.Serializable;

public class Products implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String productId;
	
	private String quantity;
	private Product product;

	public Products(String productid, String quan, Product product2) {
		// TODO Auto-generated constructor stub
		this.productId=productid;
		this.quantity=quan;
		this.product=product2;
	}

	public String getProductId() {
		return productId;
	}

	public void setProduct(String id) {
		this.productId = id;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
