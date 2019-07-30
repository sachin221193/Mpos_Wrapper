package com.vestige.wrapper.model;

public class TopSellingProductModel {
	private Integer itemId;
	private String itemName;
	private Double quantity;
	private String mposSubCategory;
	private String newDisplayCategoryName;

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getMposSubCategory() {
		return mposSubCategory;
	}

	public void setMposSubCategory(String mposSubCategory) {
		this.mposSubCategory = mposSubCategory;
	}

	public String getNewDisplayCategoryName() {
		return newDisplayCategoryName;
	}

	public void setNewDisplayCategoryName(String newDisplayCategoryName) {
		this.newDisplayCategoryName = newDisplayCategoryName;
	}
}