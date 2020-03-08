/**
 * 
 */
package com.ticketadvantage.services.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * @author jmiller
 *
 */
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS) //slightly more normalized
@MappedSuperclass
@XmlAccessorType(XmlAccessType.NONE)
public abstract class BaseRecordEvent implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
    @XmlElement
	private Long id;

	@Column(name = "eventname", nullable = true, length = 100)
	@XmlElement
	private String eventname;
	
	@Column(name = "eventtype", nullable = false, length = 100)
	@XmlElement
	private String eventtype;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "eventdatetime", nullable = true)
	@XmlElement
	@XmlJavaTypeAdapter(EventDateAdapter.class)
	private Date eventdatetime;

	@Column(name = "sport", nullable = false, length = 100)
	@XmlElement
	private String sport;

	@Column(name = "userid", nullable = false)
	@XmlElement
	private Long userid;

	@Column(name = "accountid", nullable = true)
	@XmlElement
	private Long accountid = new Long(-99);

	@Column(name = "groupid", nullable = true)
	@XmlElement
	private Long groupid = new Long(-99);

	@Column(name = "eventid", nullable = false)
	@XmlElement
	private Integer eventid;
	
	@Column(name = "eventid1", nullable = true)
	@XmlElement
	private Integer eventid1;

	@Column(name = "eventid2", nullable = true)
	@XmlElement
	private Integer eventid2;

	@Column(name = "eventteam1", nullable = true, length = 100)
	@XmlElement
	private String eventteam1;

	@Column(name = "eventteam2", nullable = true, length = 100)
	@XmlElement
	private String eventteam2;

	@Column(name = "amount", nullable = true, length = 10)
	@XmlElement
	private String amount;

	@Column(name = "wtype", nullable = true, length = 10)
	@XmlElement
	private String wtype;

	@Column(name = "iscompleted", nullable = true)
	@XmlElement
	private Boolean iscompleted;

	@Column(name = "attempts", nullable = false)
	@XmlElement
	private Integer attempts;

	@Column(name = "currentattempts", nullable = true)
	@XmlElement
	private Integer currentattempts;

	@Column(name = "scrappername", nullable = true, length = 100)
	@XmlElement
	private String scrappername = "UI";

	@Column(name = "actiontype", nullable = true, length = 10)
	@XmlElement
	private String actiontype = "Standard";
	
	@Column(name = "textnumber", nullable = true, length = 40)
	@XmlElement
	private String textnumber;

	@Column(name = "rotationid", nullable = true)
	@XmlElement
	private Integer rotationid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "attempttime", nullable = true)
	@XmlElement
	private Date attempttime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datentime", nullable = true)
	@XmlElement
	@XmlJavaTypeAdapter(IncomingDateAdapter.class)
	private Date datentime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datecreated", nullable = true)
	@XmlElement
	private Date datecreated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datemodified", nullable = true)
	@XmlElement
	private Date datemodified;

	/**
	 * 
	 */
	public BaseRecordEvent() {
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
	 * @return the eventname
	 */
	public String getEventname() {
		return eventname;
	}

	/**
	 * @param eventname the eventname to set
	 */
	public void setEventname(String eventname) {
		this.eventname = eventname;
	}

	/**
	 * @return the gametype
	 */
	public String getEventtype() {
		return eventtype;
	}

	/**
	 * @param gametype the gametype to set
	 */
	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
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
	 * @return the accountid
	 */
	public Long getAccountid() {
		return accountid;
	}

	/**
	 * @param accountid the accountid to set
	 */
	public void setAccountid(Long accountid) {
		this.accountid = accountid;
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
	public Integer getEventid() {
		return eventid;
	}

	/**
	 * @param eventid the eventid to set
	 */
	public void setEventid(Integer eventid) {
		this.eventid = eventid;
	}

	/**
	 * @return the eventid1
	 */
	public Integer getEventid1() {
		return eventid1;
	}

	/**
	 * @param eventid1 the eventid1 to set
	 */
	public void setEventid1(Integer eventid1) {
		this.eventid1 = eventid1;
	}

	/**
	 * @return the eventid2
	 */
	public Integer getEventid2() {
		return eventid2;
	}

	/**
	 * @param eventid2 the eventid2 to set
	 */
	public void setEventid2(Integer eventid2) {
		this.eventid2 = eventid2;
	}

	/**
	 * @return the eventteam1
	 */
	public String getEventteam1() {
		return eventteam1;
	}

	/**
	 * @param eventteam1 the eventteam1 to set
	 */
	public void setEventteam1(String eventteam1) {
		this.eventteam1 = eventteam1;
	}

	/**
	 * @return the eventteam2
	 */
	public String getEventteam2() {
		return eventteam2;
	}

	/**
	 * @param eventteam2 the eventteam2 to set
	 */
	public void setEventteam2(String eventteam2) {
		this.eventteam2 = eventteam2;
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
	 * @return the attempts
	 */
	public Integer getAttempts() {
		return attempts;
	}

	/**
	 * @param attempts the attempts to set
	 */
	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}

	/**
	 * @return the currentattempts
	 */
	public Integer getCurrentattempts() {
		return currentattempts;
	}

	/**
	 * @param currentattempts the currentattempts to set
	 */
	public void setCurrentattempts(Integer currentattempts) {
		this.currentattempts = currentattempts;
	}

	/**
	 * @return the attempttime
	 */
	public Date getAttempttime() {
		return attempttime;
	}

	/**
	 * @param attempttime the attempttime to set
	 */
	public void setAttempttime(Date attempttime) {
		this.attempttime = attempttime;
	}

	/**
	 * @return the scrappername
	 */
	public String getScrappername() {
		return scrappername;
	}

	/**
	 * @param scrappername the scrappername to set
	 */
	public void setScrappername(String scrappername) {
		this.scrappername = scrappername;
	}

	/**
	 * @return the actiontype
	 */
	public String getActiontype() {
		return actiontype;
	}

	/**
	 * @param actiontype the actiontype to set
	 */
	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}

	/**
	 * @return the textnumber
	 */
	public String getTextnumber() {
		return textnumber;
	}

	/**
	 * @param textnumber the textnumber to set
	 */
	public void setTextnumber(String textnumber) {
		this.textnumber = textnumber;
	}

	/**
	 * @return the rotationid
	 */
	public Integer getRotationid() {
		return rotationid;
	}

	/**
	 * @param rotationid the rotationid to set
	 */
	public void setRotationid(Integer rotationid) {
		this.rotationid = rotationid;
	}

	/**
	 * @return the eventdatetime
	 */
	public Date getEventdatetime() {
		return eventdatetime;
	}

	/**
	 * @param eventdatetime the eventdatetime to set
	 */
	public void setEventdatetime(Date eventdatetime) {
		this.eventdatetime = eventdatetime;
	}

	/**
	 * @return the datentime
	 */
	public Date getDatentime() {
		return datentime;
	}

	/**
	 * @param datentime the datentime to set
	 */
	public void setDatentime(Date datentime) {
		this.datentime = datentime;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BaseRecordEvent [id=" + id + ", eventname=" + eventname + ", eventtype=" + eventtype
				+ ", eventdatetime=" + eventdatetime + ", sport=" + sport + ", userid=" + userid + ", accountid="
				+ accountid + ", groupid=" + groupid + ", eventid=" + eventid + ", eventid1=" + eventid1 + ", eventid2="
				+ eventid2 + ", eventteam1=" + eventteam1 + ", eventteam2=" + eventteam2 + ", amount=" + amount
				+ ", wtype=" + wtype + ", iscompleted=" + iscompleted + ", attempts=" + attempts + ", currentattempts="
				+ currentattempts + ", scrappername=" + scrappername + ", actiontype=" + actiontype + ", textnumber="
				+ textnumber + ", rotationid=" + rotationid + ", attempttime=" + attempttime + ", datentime="
				+ datentime + ", datecreated=" + datecreated + ", datemodified=" + datemodified + ", rotationid=" + rotationid + "]";
	}
}