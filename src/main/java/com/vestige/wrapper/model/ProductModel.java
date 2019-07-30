package com.vestige.wrapper.model;

public class ProductModel {

	private Integer id;

	private String itemCode;

	private Integer parentCategoryId;

	private String categoryName;

	private String displayName;

	private Double bv;

	private Double pv;

	private Double itemPrice;

	private Integer promotionParticipation;

	private Integer status;

	private String shortName;

	private Integer taxCategoryId;

	private String itemImageUrl;

	private String webDescription;

	private Integer locationId;;

	private String mposCat;

	private String mposSubcat;

	private String mposProductShortName;

	private Double currentInventoryPosition;

	private Integer newDisplayCategoryId;

	private String newDisplayCategoryName;

	private Integer newDisplaySubCategoryId;

	private String newDisplaySubCategoryName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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

	public Integer getPromotionParticipation() {
		return promotionParticipation;
	}

	public void setPromotionParticipation(Integer promotionParticipation) {
		this.promotionParticipation = promotionParticipation;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Integer getTaxCategoryId() {
		return taxCategoryId;
	}

	public void setTaxCategoryId(Integer taxCategoryId) {
		this.taxCategoryId = taxCategoryId;
	}

	public String getItemImageUrl() {
		return itemImageUrl;
	}

	public void setItemImageUrl(String itemImageUrl) {
		this.itemImageUrl = itemImageUrl;
	}

	public String getWebDescription() {
		return webDescription;
	}

	public void setWebDescription(String webDescription) {
		this.webDescription = webDescription;
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
		return " id=" + id + ", itemCode=" + itemCode + ", parentCategoryId=" + parentCategoryId + ", categoryName="
				+ categoryName + ", displayName=" + displayName + ", bv=" + bv + ", pv=" + pv + ", itemPrice="
				+ itemPrice + ", promotionParticipation=" + promotionParticipation + ", status=" + status
				+ ", shortName=" + shortName + ", taxCategoryId=" + taxCategoryId + ", itemImageUrl=" + itemImageUrl
				+ ", webDescription=" + webDescription + ", locationId=" + locationId + ", mposCat=" + mposCat
				+ ", mposSubcat=" + mposSubcat + ", mposProductShortName=" + mposProductShortName
				+ ", currentInventoryPosition=" + currentInventoryPosition + ", newDisplayCategoryId="
				+ newDisplayCategoryId + ", newDisplayCategoryName=" + newDisplayCategoryName
				+ ", newDisplaySubCategoryId=" + newDisplaySubCategoryId + ", newDisplaySubCategoryName="
				+ newDisplaySubCategoryName + "]";
	}

}
