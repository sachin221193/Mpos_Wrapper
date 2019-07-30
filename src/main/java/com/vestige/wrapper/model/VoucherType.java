package com.vestige.wrapper.model;

/**
 * The OrderType enumeration.
 */
public enum VoucherType {
	GIFT("Gift"), 
    BONUS("Bonus"),
	PROMOTIONAL("Promotional");

	private final String value;

	private VoucherType(String value) {
	 this.value = value;
	}

	public String getValue() {
	  return value;
	}
}
