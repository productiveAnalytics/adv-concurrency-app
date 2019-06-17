package com.productiveAnalytics.concurrency.model;

import java.util.Date;

public class UserDetails {
	private int userId;
	private String userName;
	private boolean active;
	private Date lastLoginDatetime;
	
	/**
	 * C'stor for creating brand new user
	 * 
	 * @param userId
	 * @param userName
	 */
	public UserDetails(int userId, String userName) {
		this(userId, userName, true, null);
	}
	
	public UserDetails(int userId, String userName, boolean active, Date lastLoginDatetime) {
		this.userId = userId;
		this.userName = userName;
		this.active = active;
		this.lastLoginDatetime = lastLoginDatetime;
	}
	
	public final int getUserId() {
		return userId;
	}
	public final void setUserId(int userId) {
		this.userId = userId;
	}
	
	public final String getUserName() {
		return userName;
	}
	public final void setUserName(String userName) {
		this.userName = userName;
	}
	
	public final boolean isActive() {
		return active;
	}
	public final void setActive(boolean active) {
		this.active = active;
	}
	
	public final Date getLastLoginDatetime() {
		return lastLoginDatetime;
	}
	public final void setLastLoginDatetime(Date lastLoginDatetime) {
		this.lastLoginDatetime = lastLoginDatetime;
	}
}
