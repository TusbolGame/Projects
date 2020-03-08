/**
 * 
 */
package com.ticketadvantage.services.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author jmiller
 *
 */
@Entity
@Table (name="twitteraccountsta")
@XmlRootElement(name = "twitteraccount")
@XmlAccessorType(XmlAccessType.NONE)
public class TwitterAccounts implements Serializable, Comparable<TwitterAccounts> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "twitteraccountsta_generator")
	@SequenceGenerator(name="twitteraccountsta_generator", sequenceName = "twitteraccountsta_id_seq", allocationSize=1)
	@Column(name = "id", updatable = false, nullable = false)
	@XmlElement(nillable=true)
	private Long id;

	@Column(name = "name", nullable = false)
	@XmlElement
	private String name;

	@Column(name = "accountid", unique = true, nullable = false)
	@XmlElement
	private String accountid;

	@Column(name = "inet", unique = true, nullable = false)
	@XmlElement
	private String inet;

	@Column(name = "screenname", unique = false, nullable = false)
	@XmlElement(nillable=false)
	private String screenname;

	@Column(name = "handleid", unique = false, nullable = false)
	@XmlElement(nillable=false)
	private String handleid;

	@Column(name = "sitetype", nullable = true)
	@XmlElement(nillable=true)
	private String sitetype;

	@Column(name = "isactive", nullable = false)
	@XmlElement(nillable=true)
	private Boolean isactive;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datecreated", nullable = false)
	@XmlElement(nillable=true)
	private Date datecreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datemodified", nullable = false)
	@XmlElement(nillable=true)
	private Date datemodified;

	/**
	 * 
	 */
	public TwitterAccounts() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 */
    @PrePersist
    protected void onCreate() {
    	datecreated = datemodified = new Date();
    	if (this.isactive == null) {
    		this.isactive = new Boolean(true);
    	}
    }

    /**
     * 
     */
    @PreUpdate
    protected void onUpdate() {
    	datemodified = new Date();
    }

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
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
	 * @return the accountid
	 */
	public String getAccountid() {
		return accountid;
	}

	/**
	 * @param accountid the accountid to set
	 */
	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}

	/**
	 * @return the inet
	 */
	public String getInet() {
		return inet;
	}

	/**
	 * @param inet the inet to set
	 */
	public void setInet(String inet) {
		this.inet = inet;
	}

	/**
	 * @return the screenname
	 */
	public String getScreenname() {
		return screenname;
	}

	/**
	 * @param screenname the screenname to set
	 */
	public void setScreenname(String screenname) {
		this.screenname = screenname;
	}

	/**
	 * @return the handleid
	 */
	public String getHandleid() {
		return handleid;
	}

	/**
	 * @param handleid the handleid to set
	 */
	public void setHandleid(String handleid) {
		this.handleid = handleid;
	}

	/**
	 * @return the sitetype
	 */
	public String getSitetype() {
		return sitetype;
	}

	/**
	 * @param sitetype the sitetype to set
	 */
	public void setSitetype(String sitetype) {
		this.sitetype = sitetype;
	}

	/**
	 * @return the isactive
	 */
	public Boolean getIsactive() {
		return isactive;
	}

	/**
	 * @param isactive the isactive to set
	 */
	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

	/**
	 * @return the datecreated
	 */
	public Date getDatecreated() {
		return datecreated;
	}

	/**
	 * @param datecreated the datecreated to set
	 */
	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}

	/**
	 * @return the datemodified
	 */
	public Date getDatemodified() {
		return datemodified;
	}

	/**
	 * @param datemodified the datemodified to set
	 */
	public void setDatemodified(Date datemodified) {
		this.datemodified = datemodified;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TwitterAccounts o) {
	    if (getName() == null || o.getName() == null)
	        return 0;
	    return getName().compareTo(o.getName());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TwitterAccounts [id=" + id + ", name=" + name + ", accountid=" + accountid + ", inet=" + inet
				+ ", screenname=" + screenname + ", handleid=" + handleid + ", sitetype=" + sitetype + ", isactive="
				+ isactive + ", datecreated=" + datecreated + ", datemodified=" + datemodified + "]";
	}
}