package com.vestige.wrapper.model;

import java.util.List;

/***
 * 
 * @author ashutosh.sharma
 * 
 *         modified by sohan on 10 Oct 2018
 *
 */
public class LoginModel {

	private String userId;
	private String firstName;
	private String lastName;
	private String userToken;
	private String emailId;
	private String mobileNo;
	private List<String> roles;
	private int userLevel;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	public int getUserLevelID() {
		return this.userLevel;
	}

	public void setUserLevelID(int userLevel) {
		this.userLevel = userLevel;
	}

	@Override
	public String toString() {
		return "LoginModel [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", userToken="
				+ userToken + ", emailId=" + emailId + ", mobileNo=" + mobileNo + ", roles=" + roles + ", userLevel="
				+ userLevel + "]";
	}

}
