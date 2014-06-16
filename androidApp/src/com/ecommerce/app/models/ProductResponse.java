package com.ecommerce.app.models;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ProductResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("errorCode")
	String errorCode;

	@SerializedName("data")
	List<ProductCategory> data;

	private ProductResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public List<ProductCategory> getData() {
		return data;
	}

	public void setData(List<ProductCategory> data) {
		this.data = data;
	}

}
