package com.noahedu.demo.mybatis.core;

import java.util.Date;

public abstract class Metadata extends Entity {
	
	private Date dateAdded;
	private Date dateModified;
	private String userAdded;
	private String userModified;
	private Date dateDeleted;
	private String userDeleted;
	
	public Metadata() {
		super(0);
	}
	
	public Metadata(int id) {
		super(id);
	}
	
	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	public Date getDateModified() {
		return dateModified;
	}
	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	public String getUserAdded() {
		return userAdded;
	}
	public void setUserAdded(String userAdded) {
		this.userAdded = userAdded;
	}
	public String getUserModified() {
		return userModified;
	}
	public void setUserModified(String userModified) {
		this.userModified = userModified;
	}

	public Date getDateDeleted() {
		return dateDeleted;
	}

	public void setDateDeleted(Date dateDeleted) {
		this.dateDeleted = dateDeleted;
	}

	public String getUserDeleted() {
		return userDeleted;
	}

	public void setUserDeleted(String userDeleted) {
		this.userDeleted = userDeleted;
	}
}
