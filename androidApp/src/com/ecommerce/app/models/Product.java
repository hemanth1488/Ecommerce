package com.ecommerce.app.models;


import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	int categoryId;

	@SerializedName("id")
	String id;

	@SerializedName("name")
	String name;

	@SerializedName("short_desc")
	String shortDescription;

	@SerializedName("desc")
	String description;

	@SerializedName("how_it_works")
	String howItWorks;

	@SerializedName("old_price")
	String oldPrice;

	@SerializedName("new_price")
	String newPrice;

	@SerializedName("product_img")
	String image;

	String qty;

	public Product() {
		super();
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHowItWorks() {
		return howItWorks;
	}

	public void setHowItWorks(String howItWorks) {
		this.howItWorks = howItWorks;
	}

	public String getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(String oldPrice) {
		this.oldPrice = oldPrice;
	}

	public String getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(String newPrice) {
		this.newPrice = newPrice;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
