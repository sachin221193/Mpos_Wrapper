package com.vestige.wrapper.model;

public class DistributorConsistencyModel {

	private Long distributorId;

	private String cnc1;

	private String cnc2;

	private String cnc3;

	private String cnc4;

	private String currentMonthInvoices;

	private Integer invoiceCount;

	private String businessMonth;

	private String businessDate;

	private String businessYear;

	public Long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}

	public String getCnc1() {
		return cnc1;
	}

	public void setCnc1(String cnc1) {
		this.cnc1 = cnc1;
	}

	public String getCnc2() {
		return cnc2;
	}

	public void setCnc2(String cnc2) {
		this.cnc2 = cnc2;
	}

	public String getCnc3() {
		return cnc3;
	}

	public void setCnc3(String cnc3) {
		this.cnc3 = cnc3;
	}

	public String getCnc4() {
		return cnc4;
	}

	public void setCnc4(String cnc4) {
		this.cnc4 = cnc4;
	}

	public String getCurrentMonthInvoices() {
		return currentMonthInvoices;
	}

	public void setCurrentMonthInvoices(String currentMonthInvoices) {
		this.currentMonthInvoices = currentMonthInvoices;
	}

	public Integer getInvoiceCount() {
		return invoiceCount;
	}

	public void setInvoiceCount(Integer invoiceCount) {
		this.invoiceCount = invoiceCount;
	}

	public String getBusinessMonth() {
		return businessMonth;
	}

	public void setBusinessMonth(String businessMonth) {
		this.businessMonth = businessMonth;
	}

	public String getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}

	public String getBusinessYear() {
		return businessYear;
	}

	public void setBusinessYear(String businessYear) {
		this.businessYear = businessYear;
	}

}
