package com.vestige.wrapper.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author ashutosh.sharma
 *
 */
public class DistributorModel {

	private String id;

	private Long distributorId;

	private Long uplineDistributorId;

	private String title;

	private String firstName;

	private String lastName;

	private Instant dob;

	private String coDistributorTitle;

	private String coDistributorFirstName;

	private String coDistributorLastName;

	private Instant coDistributorDob;

	private String telephoneNumber;

	private String mobileNumber;

	private String faxNumber;

	private String emailId;

	private String address1;

	private String address2;

	private String address3;

	private String address4;

	private String pincode;

	private Instant registrationDate;

	private Instant activationDate;

	private Double minFirstPurchaseAmount;

	private String status;

	private String panNumber;

	private String kitOrderNo;

	private String kitInvoiceNo;

	private String firstOrderTaken;

	private String remarks;

	private String userName;

	private String serialNo;

	private String password;

	private Double allInvoiceAmountSum;

	private String uploadedPanNumber;

	private String uploadedAddressProof;

	private String uploadedCancelledCheque;

	private Integer cityId;

	private Integer stateId;

	private Integer countryId;

	private Integer bankId;

	private Integer heirarchyLevelId;

	private Integer locationId;

	private Integer zoneId;

	private Double lastMonthSelfPv;

	private Double lastMonthTotalPv;

	private Double lastMonthTotalCumPv;

	@JsonIgnore
	private Instant createdOn;

	@JsonIgnore
	private Long createdBy;

	@JsonIgnore
	private Instant modifiedOn;

	@JsonIgnore
	private Long modifiedBy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}

	public Long getUplineDistributorId() {
		return uplineDistributorId;
	}

	public void setUplineDistributorId(Long uplineDistributorId) {
		this.uplineDistributorId = uplineDistributorId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Instant getDob() {
		return dob;
	}

	public void setDob(Instant dob) {
		this.dob = dob;
	}

	public String getCoDistributorTitle() {
		return coDistributorTitle;
	}

	public void setCoDistributorTitle(String coDistributorTitle) {
		this.coDistributorTitle = coDistributorTitle;
	}

	public String getCoDistributorFirstName() {
		return coDistributorFirstName;
	}

	public void setCoDistributorFirstName(String coDistributorFirstName) {
		this.coDistributorFirstName = coDistributorFirstName;
	}

	public String getCoDistributorLastName() {
		return coDistributorLastName;
	}

	public void setCoDistributorLastName(String coDistributorLastName) {
		this.coDistributorLastName = coDistributorLastName;
	}

	public Instant getCoDistributorDob() {
		return coDistributorDob;
	}

	public void setCoDistributorDob(Instant coDistributorDob) {
		this.coDistributorDob = coDistributorDob;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getAddress4() {
		return address4;
	}

	public void setAddress4(String address4) {
		this.address4 = address4;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public Instant getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Instant registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Instant getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Instant activationDate) {
		this.activationDate = activationDate;
	}

	public Double getMinFirstPurchaseAmount() {
		return minFirstPurchaseAmount;
	}

	public void setMinFirstPurchaseAmount(Double minFirstPurchaseAmount) {
		this.minFirstPurchaseAmount = minFirstPurchaseAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getKitOrderNo() {
		return kitOrderNo;
	}

	public void setKitOrderNo(String kitOrderNo) {
		this.kitOrderNo = kitOrderNo;
	}

	public String getKitInvoiceNo() {
		return kitInvoiceNo;
	}

	public void setKitInvoiceNo(String kitInvoiceNo) {
		this.kitInvoiceNo = kitInvoiceNo;
	}

	public String getFirstOrderTaken() {
		return firstOrderTaken;
	}

	public void setFirstOrderTaken(String firstOrderTaken) {
		this.firstOrderTaken = firstOrderTaken;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Double getAllInvoiceAmountSum() {
		return allInvoiceAmountSum;
	}

	public void setAllInvoiceAmountSum(Double allInvoiceAmountSum) {
		this.allInvoiceAmountSum = allInvoiceAmountSum;
	}

	public String getUploadedPanNumber() {
		return uploadedPanNumber;
	}

	public void setUploadedPanNumber(String uploadedPanNumber) {
		this.uploadedPanNumber = uploadedPanNumber;
	}

	public String getUploadedAddressProof() {
		return uploadedAddressProof;
	}

	public void setUploadedAddressProof(String uploadedAddressProof) {
		this.uploadedAddressProof = uploadedAddressProof;
	}

	public String getUploadedCancelledCheque() {
		return uploadedCancelledCheque;
	}

	public void setUploadedCancelledCheque(String uploadedCancelledCheque) {
		this.uploadedCancelledCheque = uploadedCancelledCheque;
	}

	public Double getLastMonthSelfPv() {
		return lastMonthSelfPv;
	}

	public void setLastMonthSelfPv(Double lastMonthSelfPv) {
		this.lastMonthSelfPv = lastMonthSelfPv;
	}

	public Double getLastMonthTotalPv() {
		return lastMonthTotalPv;
	}

	public void setLastMonthTotalPv(Double lastMonthTotalPv) {
		this.lastMonthTotalPv = lastMonthTotalPv;
	}

	public Double getLastMonthTotalCumPv() {
		return lastMonthTotalCumPv;
	}

	public void setLastMonthTotalCumPv(Double lastMonthTotalCumPv) {
		this.lastMonthTotalCumPv = lastMonthTotalCumPv;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Instant createdOn) {
		this.createdOn = createdOn;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Instant getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Instant modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public Integer getHeirarchyLevelId() {
		return heirarchyLevelId;
	}

	public void setHeirarchyLevelId(Integer heirarchyLevelId) {
		this.heirarchyLevelId = heirarchyLevelId;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

}
