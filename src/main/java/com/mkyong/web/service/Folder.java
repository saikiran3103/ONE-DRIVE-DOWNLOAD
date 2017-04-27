package com.mkyong.web.service;

import java.io.Serializable;

public class Folder implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5882302993194027971L;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Folder [childCount=" + childCount + "]";
	}

	/**
	 * @return the childCount
	 */
	public String getChildCount() {
		return childCount;
	}

	/**
	 * @param childCount the childCount to set
	 */
	public void setChildCount(String childCount) {
		this.childCount = childCount;
	}

	private String childCount;
}
