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
@Table (name="parlayrecordevent")
@XmlRootElement(name = "parlayrecordevent")
@XmlAccessorType(XmlAccessType.NONE)
public class ParlayRecordEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parlayrecordevent_generator")
	@SequenceGenerator(name="parlayrecordevent_generator", sequenceName = "parlayrecordevent_id_seq", allocationSize=1)
	@Column(name = "id", updatable = false, nullable = false)
	@XmlElement(nillable=true)
	private Long id;

	@Column(name = "userid", nullable = false)
	@XmlElement
	private Long userid;

	@Column(name = "parlaytype", nullable = false, length = 20)
	@XmlElement
	private String parlaytype;

	@Column(name = "totalrisk", nullable = true)
	@XmlElement
	private Float totalrisk;

	@Column(name = "totalwin", nullable = true)
	@XmlElement
	private Float totalwin;

	@Column(name = "description", nullable = true, length = 100)
	@XmlElement
	private String description;

	@Column(name = "scrappername", nullable = true, length = 100)
	@XmlElement
	private String scrappername = "UI";

	@Column(name = "actiontype", nullable = true, length = 10)
	@XmlElement
	private String actiontype = "Standard";
	
	@Column(name = "textnumber", nullable = true, length = 40)
	@XmlElement
	private String textnumber;

	@Column(name = "wtype", nullable = true, length = 10)
	@XmlElement
	private String wtype;

	@Column(name = "iscompleted", nullable = true)
	@XmlElement
	private Boolean iscompleted;

	@Column(name = "attempts", nullable = false)
	@XmlElement
	private Integer attempts;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "attempttime", nullable = true)
	@XmlElement
	private Date attempttime;

	@Column(name = "currentattempts", nullable = true)
	@XmlElement
	private Integer currentattempts;

	@Column(name = "riskamount", nullable = true)
	@XmlElement
	private Float riskamount;

	@Column(name = "winamount", nullable = true)
	@XmlElement
	private Float winamount;

	@Column(name = "eventresult", nullable = true)
	@XmlElement
	private String eventresult;

	@Column(name = "eventresultamount", nullable = true)
	@XmlElement
	private Float eventresultamount;

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
	public ParlayRecordEvent() {
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
	 * @return the type
	 */
	public String getParlaytype() {
		return parlaytype;
	}

	/**
	 * @param type the type to set
	 */
	public void setParlaytype(String parlaytype) {
		this.parlaytype = parlaytype;
	}

	/**
	 * @return the totalrisk
	 */
	public Float getTotalrisk() {
		return totalrisk;
	}

	/**
	 * @param totalrisk the totalrisk to set
	 */
	public void setTotalrisk(Float totalrisk) {
		this.totalrisk = totalrisk;
	}

	/**
	 * @return the totalwin
	 */
	public Float getTotalwin() {
		return totalwin;
	}

	/**
	 * @param totalwin the totalwin to set
	 */
	public void setTotalwin(Float totalwin) {
		this.totalwin = totalwin;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return the riskamount
	 */
	public Float getRiskamount() {
		return riskamount;
	}

	/**
	 * @param riskamount the riskamount to set
	 */
	public void setRiskamount(Float riskamount) {
		this.riskamount = riskamount;
	}

	/**
	 * @return the towinamount
	 */
	public Float getWinamount() {
		return winamount;
	}

	/**
	 * @param towinamount the towinamount to set
	 */
	public void setWinamount(Float winamount) {
		this.winamount = winamount;
	}

	/**
	 * @return the eventresult
	 */
	public String getEventresult() {
		return eventresult;
	}

	/**
	 * @param eventresult the eventresult to set
	 */
	public void setEventresult(String eventresult) {
		this.eventresult = eventresult;
	}

	/**
	 * @return the eventresultamount
	 */
	public Float getEventresultamount() {
		return eventresultamount;
	}

	/**
	 * @param eventresultamount the eventresultamount to set
	 */
	public void setEventresultamount(Float eventresultamount) {
		this.eventresultamount = eventresultamount;
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
		return "ParlayRecordEvent [id=" + id + ", userid=" + userid + ", parlaytype=" + parlaytype + ", totalrisk=" + totalrisk
				+ ", totalwin=" + totalwin + ", description=" + description + ", scrappername=" + scrappername
				+ ", actiontype=" + actiontype + ", textnumber=" + textnumber + ", wtype=" + wtype + ", iscompleted="
				+ iscompleted + ", attempts=" + attempts + ", currentattempts=" + currentattempts + ", riskamount="
				+ riskamount + ", winamount=" + winamount + ", eventresult=" + eventresult + ", eventresultamount="
				+ eventresultamount + ", attempttime=" + attempttime + ", datecreated=" + datecreated
				+ ", datemodified=" + datemodified + "]";
	}
}