/**
 * 
 */
package com.wootechnologies.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
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
@Table (name="recordevents")
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class RecordEvent implements Serializable {
	private static final long serialVersionUID = 1L;

//	@GeneratedValue(strategy = GenerationType.TABLE, generator = "accountstable")
//	@TableGenerator(name = "EVENTGEN", table = "SEQUENCES", pkColumnName = "SEQNAME", valueColumnName = "SEQ_NUMBER", pkColumnValue = "SEQUENCE", allocationSize=1)

	@Id
	@TableGenerator(name="recordeventstable",table="recordevents",pkColumnName="recordeventsprimarykey",pkColumnValue="sequence", valueColumnName="value",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	@XmlElement
	private Long id = null;

	@Column(name = "name", unique = false, nullable = false, length = 100)
	@XmlElement
	private String name;
	
	@Column(name = "type", nullable = false, length = 100)
	@XmlElement
	private String type;

	@Column(name = "sport", nullable = false, length = 100)
	@XmlElement
	private String sport;

	@Column(name = "userid", nullable = false)
	@XmlElement
	private Long userid;

	@Column(name = "groupid", nullable = false)
	@XmlElement
	private Long groupid;

	@Column(name = "eventid", nullable = true)
	@XmlElement
	private String eventid;
	
	@Column(name = "value", nullable = true)
	@XmlElement
	private String value;	

	@Column(name = "amount", nullable = true)
	@XmlElement
	private String amount;

	@Column(name = "juice", nullable = true)
	@XmlElement
	private String juice;

	@Column(name = "wtype", nullable = true)
	@XmlElement
	private String wtype;

	@Column(name = "siteselector", nullable = true)
	@XmlElement
	private String siteselector;

	@Column(name = "iscompleted", nullable = true)
	@XmlElement
	private Boolean iscompleted;

	@Temporal(TemporalType.DATE)
	@Column(name = "datecreated", nullable = false)
	@XmlElement
	private Date datecreated;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "datemodified", nullable = false)
	@XmlElement
	private Date datemodified;

	/**
	 * 
	 */
	public RecordEvent() {
		super();
//		log.debug("Entering User()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
    @PrePersist
    protected void onCreate() {
    	datecreated = datemodified = new Date();
    	iscompleted = false;
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the sport
	 */
	public String getSport() {
		return sport;
	}

	/**
	 * @param sport the sport to set
	 */
	public void setSport(String sport) {
		this.sport = sport;
	}

	/**
	 * @return the userid
	 */
	public Long getUserid() {
		return userid;
	}

	/**
	 * @param userid the userid to set
	 */
	public void setUserid(Long userid) {
		this.userid = userid;
	}

	/**
	 * @return the groupid
	 */
	public Long getGroupid() {
		return groupid;
	}

	/**
	 * @param groupid the groupid to set
	 */
	public void setGroupid(Long groupid) {
		this.groupid = groupid;
	}

	/**
	 * @return the eventid
	 */
	public String getEventid() {
		return eventid;
	}

	/**
	 * @param eventid the eventid to set
	 */
	public void setEventid(String eventid) {
		this.eventid = eventid;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * @return the juice
	 */
	public String getJuice() {
		return juice;
	}

	/**
	 * @param juice the juice to set
	 */
	public void setJuice(String juice) {
		this.juice = juice;
	}

	/**
	 * @return the wtype
	 */
	public String getWtype() {
		return wtype;
	}

	/**
	 * @param wtype the wtype to set
	 */
	public void setWtype(String wtype) {
		this.wtype = wtype;
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

	/**
	 * @return the iscompleted
	 */
	public Boolean getIscompleted() {
		return iscompleted;
	}

	/**
	 * @param iscompleted the iscompleted to set
	 */
	public void setIscompleted(Boolean iscompleted) {
		this.iscompleted = iscompleted;
	}

	/**
	 * @return the siteselector
	 */
	public String getSiteselector() {
		return siteselector;
	}

	/**
	 * @param siteselector the siteselector to set
	 */
	public void setSiteselector(String siteselector) {
		this.siteselector = siteselector;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RecordEvent [id=" + id + ",\n name=" + name + ",\n type=" + type + ",\n sport=" + sport + ",\n userid="
				+ userid + ",\n groupid=" + groupid + ",\n eventid=" + eventid + ",\n value=" + value + ",\n amount="
				+ amount + ",\n juice=" + juice + ",\n wtype=" + wtype + ",\n siteselector=" + siteselector
				+ ",\n iscompleted=" + iscompleted + ",\n datecreated=" + datecreated + ",\n datemodified=" + datemodified
				+ "]";
	}
}