package com.vestige.wrapper.model;

public class ProductDetailResponse {


	private String skuCode;
	private String productName;
	private Integer quantity;
	private Double productAmount;
	private String isPromo;
	private String giftVoucherCode;
	private String voucherSerialNumber;
	private String url;

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(Double productAmount) {
		this.productAmount = productAmount;
	}

	public String getIsPromo() {
		return isPromo;
	}

	public void setIsPromo(String isPromo) {
		this.isPromo = isPromo;
	}

	public String getGiftVoucherCode() {
		return giftVoucherCode;
	}

	public void setGiftVoucherCode(String giftVoucherCode) {
		this.giftVoucherCode = giftVoucherCode;
	}

	public String getVoucherSerialNumber() {
		return voucherSerialNumber;
	}

	public void setVoucherSerialNumber(String voucherSerialNumber) {
		this.voucherSerialNumber = voucherSerialNumber;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
