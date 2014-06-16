package com.ecommerce.app.models;


import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Retailer implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("splashImage")
	String splashImage;

	@SerializedName("poweredBy")
	String poweredBy;

	@SerializedName("headerColor")
	String headerColor;
	@SerializedName("enablePassword")
	String enablePassword;

	

	public String getEnablePassword() {
		return enablePassword;
	}

	public void setEnablePassword(String enablePassword) {
		this.enablePassword = enablePassword;
	}

	@SerializedName("retailerTextColor")
	String retailerTextColor;

	@SerializedName("retailerName")
	String retailerName;

	@SerializedName("retailerFileType")
	String retailerFileType;

	@SerializedName("retailerFile")
	String retailerFile;

	@SerializedName("companyLogo")
	String companyLogo;

	@SerializedName("backdropColor1")
	String backdropColor1;

	@SerializedName("backdropColor2")
	String backdropColor2;

	@SerializedName("backdropType")
	String backdropType;

	@SerializedName("backdropFile")
	String backdropFile;

	@SerializedName("retailerStores")
	List<RetailerStores> retailerStores;

	public Retailer() {
		super();
	}

	public String getBackdropFile() {
		return backdropFile;
	}
	
	public void setBackdropFile(String backdropFile) {
		this.backdropFile = backdropFile;
	}
	
	public String getBackdropType() {
		return backdropType;
	}

	public void setBackdropType(String backdropType) {
		this.backdropType = backdropType;
	}

	public String getBackdropColor1() {
		return backdropColor1;
	}

	public void setBackdropColor1(String backdropColor1) {
		this.backdropColor1 = backdropColor1;
	}

	public String getBackdropColor2() {
		return backdropColor2;
	}

	public void setBackdropColor2(String backdropColor2) {
		this.backdropColor2 = backdropColor2;
	}

	public String getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}

	public String getSplashImage() {
		return splashImage;
	}

	public void setSplashImage(String splashImage) {
		this.splashImage = splashImage;
	}

	public String getPoweredBy() {
		return poweredBy;
	}

	public void setPoweredBy(String poweredBy) {
		this.poweredBy = poweredBy;
	}

	public String getHeaderColor() {
		return headerColor;
	}

	public void setHeaderColor(String headerColor) {
		this.headerColor = headerColor;
	}

	public String getRetailerTextColor() {
		return retailerTextColor;
	}

	public void setRetailerTextColor(String retailerTextColor) {
		this.retailerTextColor = retailerTextColor;
	}

	public String getRetailerName() {
		return retailerName;
	}

	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}

	public String getRetailerFileType() {
		return retailerFileType;
	}

	public void setRetailerFileType(String retailerFileType) {
		this.retailerFileType = retailerFileType;
	}

	public String getRetailerFile() {
		return retailerFile;
	}

	public void setRetailerFile(String retailerFile) {
		this.retailerFile = retailerFile;
	}

	public List<RetailerStores> getRetailerStores() {
		return retailerStores;
	}

	public void setRetailerStores(List<RetailerStores> retailerStores) {
		this.retailerStores = retailerStores;
	}

}
