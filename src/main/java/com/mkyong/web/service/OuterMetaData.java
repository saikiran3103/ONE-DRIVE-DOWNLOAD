package com.mkyong.web.service;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class OuterMetaData implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1048498531485024402L;
	
	
	@SerializedName("@odata.context")
    private String odata_context;
	
	private List<MetaDataForFolder> value;

	/**
	 * @return the odata_context
	 */
	public String getOdata_context() {
		return odata_context;
	}

	/**
	 * @param odata_context the odata_context to set
	 */
	public void setOdata_context(String odata_context) {
		this.odata_context = odata_context;
	}

	/**
	 * @return the value
	 */
	public List<MetaDataForFolder> getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(List<MetaDataForFolder> value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OuterMetaData [odata_context=" + odata_context + ", value=" + value + "]";
	}
}
