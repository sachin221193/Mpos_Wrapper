package com.vestige.wrapper.model;

/**
 * The OrderType enumeration.
 */
public enum OrderType {
	SHIPPING("Shipping"), 
    STORE_PICKUP("StorePickup");

	private final String value;

	private OrderType(String value) {
	 this.value = value;
	}

	public String getValue() {
	  return value;
	}
}
