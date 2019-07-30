package com.vestige.wrapper.model;

public class OrderCancelModel {
	private String logNumber;
	private String orderId;
	private Long distributorId;
	private Double logOrderAmount;

	public String getLogNumber() {
		return logNumber;
	}

	public void setLogNumber(String logNumber) {
		this.logNumber = logNumber;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}

	public Double getLogOrderAmount() {
		return logOrderAmount;
	}

	public void setLogOrderAmount(Double logOrderAmount) {
		this.logOrderAmount = logOrderAmount;
	}

}
