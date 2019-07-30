package com.vestige.wrapper.model;

/***
 * 
 * @author shashank.madeshiya
 *
 */
public class DistributorRegistrationRespModel {
	private String distributorSeqNo;
	private String distributorId;

	public String getDistributorSeqNo() {
		return distributorSeqNo;
	}

	public void setDistributorSeqNo(String distributorSeqNo) {
		this.distributorSeqNo = distributorSeqNo;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	@Override
	public String toString() {
		return "DistributorRegistrationRespModel [distributorSeqNo=" + distributorSeqNo + ", distributorId="
				+ distributorId + "]";
	}

}
