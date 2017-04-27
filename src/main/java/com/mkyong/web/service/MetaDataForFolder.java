package com.mkyong.web.service;


import java.io.Serializable;
import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

public class MetaDataForFolder implements Serializable{
	
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 5568546212959636336L;

	private String id = "";

	    /**
	     * The Name.
	     */
	    private String name = "";

	    /**
	     * The created by reference. Possible keys are 'user', 'application' and 'device'.
	     */
	    private HashMap<String, User> createdBy = new HashMap<>();

	    /**
	     * The creation timestamp of this item.
	     */
	    private String createdDateTime;

	    /**
	     * The modified by reference. Possible keys are 'user', 'application' and 'device'.
	     */
	    private HashMap<String, User> lastModifiedBy = new HashMap<>();

	    /**
	     * The last modified timestamp of this item.
	     */
	    private String lastModifiedDateTime = "";

	    /**
	     * The cTag.
	     */
	    private String cTag = "";

	    /**
	     * The eTag.
	     */
	    private String eTag = "";

	    /**
	     * The size of an item in bytes.
	     */
	    private long size = 0;

	    /**
	     * URL that displays the resource in the browser.
	     */
	    private String webUrl = "";

	    /**
	     * The parent folder reference.
	     */
	    private ParentReference parentReference;

	    /**
	     * The raw JSON which is received from the OneDrive API.
	     */
	    private String rawJson = "";
	    
	    
	    private Folder folder;

	    /**
	     * A Url that can be used to download this file's content.
	     */
	    @SerializedName("@microsoft.graph.downloadUrl")
	    private String microsoft_graph_downloadUrl;

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
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the createdBy
		 */
		public HashMap<String, User> getCreatedBy() {
			return createdBy;
		}

		/**
		 * @param createdBy the createdBy to set
		 */
		public void setCreatedBy(HashMap<String, User> createdBy) {
			this.createdBy = createdBy;
		}

		/**
		 * @return the createdDateTime
		 */
		public String getCreatedDateTime() {
			return createdDateTime;
		}

		/**
		 * @param createdDateTime the createdDateTime to set
		 */
		public void setCreatedDateTime(String createdDateTime) {
			this.createdDateTime = createdDateTime;
		}

		/**
		 * @return the lastModifiedBy
		 */
		public HashMap<String, User> getLastModifiedBy() {
			return lastModifiedBy;
		}

		/**
		 * @param lastModifiedBy the lastModifiedBy to set
		 */
		public void setLastModifiedBy(HashMap<String, User> lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
		}

		/**
		 * @return the lastModifiedDateTime
		 */
		public String getLastModifiedDateTime() {
			return lastModifiedDateTime;
		}

		/**
		 * @param lastModifiedDateTime the lastModifiedDateTime to set
		 */
		public void setLastModifiedDateTime(String lastModifiedDateTime) {
			this.lastModifiedDateTime = lastModifiedDateTime;
		}

		/**
		 * @return the cTag
		 */
		public String getcTag() {
			return cTag;
		}

		/**
		 * @param cTag the cTag to set
		 */
		public void setcTag(String cTag) {
			this.cTag = cTag;
		}

		/**
		 * @return the eTag
		 */
		public String geteTag() {
			return eTag;
		}

		/**
		 * @param eTag the eTag to set
		 */
		public void seteTag(String eTag) {
			this.eTag = eTag;
		}

		/**
		 * @return the size
		 */
		public long getSize() {
			return size;
		}

		/**
		 * @param size the size to set
		 */
		public void setSize(long size) {
			this.size = size;
		}

		/**
		 * @return the webUrl
		 */
		public String getWebUrl() {
			return webUrl;
		}

		/**
		 * @param webUrl the webUrl to set
		 */
		public void setWebUrl(String webUrl) {
			this.webUrl = webUrl;
		}

		/**
		 * @return the parentReference
		 */
		public ParentReference getParentReference() {
			return parentReference;
		}

		/**
		 * @param parentReference the parentReference to set
		 */
		public void setParentReference(ParentReference parentReference) {
			this.parentReference = parentReference;
		}

		/**
		 * @return the rawJson
		 */
		public String getRawJson() {
			return rawJson;
		}

		/**
		 * @param rawJson the rawJson to set
		 */
		public void setRawJson(String rawJson) {
			this.rawJson = rawJson;
		}

		/**
		 * @return the microsoft_graph_downloadUrl
		 */
		public String getMicrosoft_graph_downloadUrl() {
			return microsoft_graph_downloadUrl;
		}

		public Folder getFolder() {
			return folder;
		}

		public void setFolder(Folder folder) {
			this.folder = folder;
		}

		/**
		 * @param microsoft_graph_downloadUrl the microsoft_graph_downloadUrl to set
		 */
		public void setMicrosoft_graph_downloadUrl(String microsoft_graph_downloadUrl) {
			this.microsoft_graph_downloadUrl = microsoft_graph_downloadUrl;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "MetaDataForFolder [id=" + id + ", name=" + name + ", createdBy=" + createdBy + ", createdDateTime="
					+ createdDateTime + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedDateTime="
					+ lastModifiedDateTime + ", cTag=" + cTag + ", eTag=" + eTag + ", size=" + size + ", webUrl="
					+ webUrl + ", parentReference=" + parentReference + ", rawJson=" + rawJson + ", folder=" + folder
					+ ", microsoft_graph_downloadUrl=" + microsoft_graph_downloadUrl + "]";
		}

	   


}
