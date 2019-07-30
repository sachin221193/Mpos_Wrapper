package com.vestige.wrapper.model;

/**
 * The OrderType enumeration.
 */
public enum PaymentType {
	CASH("Cash"), 
    BONUS_VOUCHER("Bonus"),
    ONLINE("Online");
    

	private final String value;

	private PaymentType(String value) {
	 this.value = value;
	}

	public String getValue() {
	  return value;
	}
}
