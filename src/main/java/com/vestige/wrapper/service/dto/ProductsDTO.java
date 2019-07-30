package com.vestige.wrapper.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the Products entity.
 */
@SuppressWarnings("serial")
public class ProductsDTO implements Serializable {

	private String id;
	
    @NotNull
    private Long productId;

    @NotNull
    private String skuCode;
    
    private Integer parentCategoryId;
    private String categoryName;

    @NotNull
    private String productName;
    
    private Double bv;

    private Double pv;
    
    private Double itemPrice;
    private Boolean isActive;
    private Integer promotionParticipation;
    private Integer taxCategory;
    private String descs;
    private String detailDescs;
    private String itemImageUrl;
    private Integer locationId;
    private String mposCat;

    private String mposSubcat;

    private String mposProductShortName;

    private Double currentInventoryPosition;
    
    private Integer newDisplayCategoryId;
	
	private String newDisplayCategoryName;
	
	private Integer newDisplaySubCategoryId;
	
	private String newDisplaySubCategoryName;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	

	public Integer getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(Integer parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getBv() {
		return bv;
	}

	public void setBv(Double bv) {
		this.bv = bv;
	}

	public Double getPv() {
		return pv;
	}

	public void setPv(Double pv) {
		this.pv = pv;
	}

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getPromotionParticipation() {
		return promotionParticipation;
	}

	public void setPromotionParticipation(Integer promotionParticipation) {
		this.promotionParticipation = promotionParticipation;
	}

	public Integer getTaxCategory() {
		return taxCategory;
	}

	public void setTaxCategory(Integer taxCategory) {
		this.taxCategory = taxCategory;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public String getDetailDescs() {
		return detailDescs;
	}

	public void setDetailDescs(String detailDescs) {
		this.detailDescs = detailDescs;
	}

	public String getItemImageUrl() {
		return itemImageUrl;
	}

	public void setItemImageUrl(String itemImageUrl) {
		this.itemImageUrl = itemImageUrl;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getMposCat() {
		return mposCat;
	}

	public void setMposCat(String mposCat) {
		this.mposCat = mposCat;
	}

	public String getMposSubcat() {
		return mposSubcat;
	}

	public void setMposSubcat(String mposSubcat) {
		this.mposSubcat = mposSubcat;
	}

	public String getMposProductShortName() {
		return mposProductShortName;
	}

	public void setMposProductShortName(String mposProductShortName) {
		this.mposProductShortName = mposProductShortName;
	}

	public Double getCurrentInventoryPosition() {
		return currentInventoryPosition;
	}

	public void setCurrentInventoryPosition(Double currentInventoryPosition) {
		this.currentInventoryPosition = currentInventoryPosition;
	}

	public Integer getNewDisplayCategoryId() {
		return newDisplayCategoryId;
	}

	public void setNewDisplayCategoryId(Integer newDisplayCategoryId) {
		this.newDisplayCategoryId = newDisplayCategoryId;
	}

	public String getNewDisplayCategoryName() {
		return newDisplayCategoryName;
	}

	public void setNewDisplayCategoryName(String newDisplayCategoryName) {
		this.newDisplayCategoryName = newDisplayCategoryName;
	}

	public Integer getNewDisplaySubCategoryId() {
		return newDisplaySubCategoryId;
	}

	public void setNewDisplaySubCategoryId(Integer newDisplaySubCategoryId) {
		this.newDisplaySubCategoryId = newDisplaySubCategoryId;
	}

	public String getNewDisplaySubCategoryName() {
		return newDisplaySubCategoryName;
	}

	public void setNewDisplaySubCategoryName(String newDisplaySubCategoryName) {
		this.newDisplaySubCategoryName = newDisplaySubCategoryName;
	}

	@Override
	public String toString() {
		return "ProductsDTO [productId=" + productId + ", skuCode=" + skuCode + ", parentCategoryId=" + parentCategoryId
				+ ", categoryName=" + categoryName + ", productName=" + productName + ", bv=" + bv + ", pv=" + pv
				+ ", itemPrice=" + itemPrice + ", isActive=" + isActive + ", promotionParticipation="
				+ promotionParticipation + ", taxCategory=" + taxCategory + ", descs=" + descs + ", detailDescs="
				+ detailDescs + ", itemImageUrl=" + itemImageUrl + ", locationId=" + locationId + ", mposCat=" + mposCat
				+ ", mposSubcat=" + mposSubcat + ", mposProductShortName=" + mposProductShortName
				+ ", currentInventoryPosition=" + currentInventoryPosition + ", newDisplayCategoryId="
				+ newDisplayCategoryId + ", newDisplayCategoryName=" + newDisplayCategoryName
				+ ", newDisplaySubCategoryId=" + newDisplaySubCategoryId + ", newDisplaySubCategoryName="
				+ newDisplaySubCategoryName + "]";
	}
}