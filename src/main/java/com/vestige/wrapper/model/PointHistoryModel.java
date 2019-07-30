package com.vestige.wrapper.model;


//Model For Showing PointDetail History

public class PointHistoryModel {

	private Long distributorId;
	private String bussinessMonth;
	private String levelId;
	private String CurrentLevel;
	private String selfPv;
	private String groupPv;
	private String totalPv;
	private String totalCumPv;

	public Long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}

	public String getBussinessMonth() {
		return bussinessMonth;
	}

	public void setBussinessMonth(String bussinessMonth) {
		this.bussinessMonth = bussinessMonth;
	}

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public String getCurrentLevel() {
		return CurrentLevel;
	}

	public void setCurrentLevel(String currentLevel) {
		CurrentLevel = currentLevel;
	}

	public String getSelfPv() {
		return selfPv;
	}

	public void setSelfPv(String selfPv) {
		this.selfPv = selfPv;
	}

	public String getGroupPv() {
		return groupPv;
	}

	public void setGroupPv(String groupPv) {
		this.groupPv = groupPv;
	}

	public String getTotalPv() {
		return totalPv;
	}

	public void setTotalPv(String totalPv) {
		this.totalPv = totalPv;
	}

	public String getTotalCumPv() {
		return totalCumPv;
	}

	public void setTotalCumPv(String totalCumPv) {
		this.totalCumPv = totalCumPv;
	}

	@Override
	public String toString() {
		return "PointDetailModel [distributorId=" + distributorId + ", bussinessMonth=" + bussinessMonth + ", levelId="
				+ levelId + ", CurrentLevel=" + CurrentLevel + ", selfPv=" + selfPv + ", groupPv=" + groupPv
				+ ", totalPv=" + totalPv + ", totalCumPv=" + totalCumPv + "]";
	}

}
