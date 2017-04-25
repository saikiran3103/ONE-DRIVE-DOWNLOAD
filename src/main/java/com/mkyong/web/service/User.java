package com.mkyong.web.service;



import java.io.Serializable;

public class User implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5524561524531051674L;

	private String id;
	
	private String displayName;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", displayName=" + displayName + ", getId()=" + getId() + ", getDisplayName()="
				+ getDisplayName() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
