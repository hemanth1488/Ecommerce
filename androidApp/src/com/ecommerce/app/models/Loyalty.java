package com.ecommerce.app.models;


import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Loyalty implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("errorCode")
	String errorCode;

	@SerializedName("loyaltyImage")
	String loyaltyImage;

	@SerializedName("termsCond")
	String termsCond;

	@SerializedName("fbUrl")
	String fbUrl;

	@SerializedName("fbIconDisplay")
	String fbIconDisplay;

	public Loyalty() {
		super();
	}

	public String getFbIconDisplay() {
		return fbIconDisplay;
	}

	public void setFbIconDisplay(String fbIconDisplay) {
		this.fbIconDisplay = fbIconDisplay;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getLoyaltyImage() {
		return loyaltyImage;
	}

	public void setLoyaltyImage(String loyaltyImage) {
		this.loyaltyImage = loyaltyImage;
	}

	public String getTermsCond() {
		return termsCond;
	}

	public void setTermsCond(String termsCond) {
		this.termsCond = termsCond;
	}

	public String getFbUrl() {
		return fbUrl;
	}

	public void setFbUrl(String fbUrl) {
		this.fbUrl = fbUrl;
	}

}
