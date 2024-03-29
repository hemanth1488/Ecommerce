package com.ecommerce.app.models;


import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class RetailerStores implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("storeAddress")
	String storeAddress;

	@SerializedName("storeContact")
	String storeContact;

	@SerializedName("latitude")
	String latitude;

	@SerializedName("longitude")
	String longitude;

	public RetailerStores() {
		super();
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getStoreContact() {
		return storeContact;
	}

	public void setStoreContact(String storeContact) {
		this.storeContact = storeContact;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
