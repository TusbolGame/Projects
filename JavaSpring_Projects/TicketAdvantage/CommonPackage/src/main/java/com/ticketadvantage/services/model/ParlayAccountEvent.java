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
import javax.persistence.Lob;
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
@Table (name="parlayaccountevent")
@XmlRootElement(name = "parlayaccountevent")
@XmlAccessorType(XmlAccessType.NONE)
public class ParlayAccountEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parlayaccountevent_generator")
	@SequenceGenerator(name="parlayaccountevent_generator", sequenceName = "parlayaccountevent_id_seq", allocationSize=1)
	@Column(name = "id", updatable = false, nullable = false)
	@XmlElement(nillable=true)
	private Long id;

	@Column(name = "userid", nullable = false)
	@XmlElement
	private Long userid;

	@Column(name = "parlayid", nullable = true)
	@XmlElement
	private Long parlayid;

	@Column(name = "eventid", unique = false, nullable = true)
	@XmlElement
	private Integer eventid;

	@Column(name = "eventname", unique = false, nullable = false, length = 100)
	@XmlElement
	private String eventname;

	@Column(name = "accountid", nullable = false)
	@XmlElement
	private Long accountid;

	@Column(name = "accountname", unique = false, nullable = false, length = 100)
	@XmlElement
	private String accountname;

	@Column(name = "groupid", nullable = false)
	@XmlElement
	private Long groupid;

	@Column(name = "parlaytype", nullable = false, length = 20)
	@XmlElement
	private String parlaytype;

	@Column(name = "sport", nullable = false, length = 20)
	@XmlElement
	private String sport;

	@Column(name = "wagertype", nullable = true)
	@XmlElement
	private String wagertype;

	@Column(name = "amount", nullable = true)
	@XmlElement
	private Float amount;

	@Column(name = "actualamount", nullable = true)
	@XmlElement
	private Float actualamount;

	@Column(name = "maxspreadamount", nullable = true)
	@XmlElement
	private Float maxspreadamount;

	@Column(name = "maxmlamount", nullable = true)
	@XmlElement
	private Float maxmlamount;

	@Column(name = "maxtotalamount", nullable = true)
	@XmlElement
	private Float maxtotalamount;

	@Column(name = "timezone", nullable = true)
	@XmlElement
	private String timezone = "ET";

	@Column(name = "ownerpercentage", nullable = true)
	@XmlElement(nillable=true)
	private Integer ownerpercentage;

	@Column(name = "partnerpercentage", nullable = true)
	@XmlElement(nillable=true)
	private Integer partnerpercentage;

	@Column(name = "spreadindicator", nullable = true)
	@XmlElement
	private String spreadindicator;

	@Column(name = "spreaddata", nullable = true)
	@XmlElement
	private String spreaddata;

	@Column(name = "spread", nullable = true)
	@XmlElement
	private Float spread;
	
	@Column(name = "spreadjuiceindicator", nullable = true)
	@XmlElement
	private String spreadjuiceindicator;

	@Column(name = "spreadjuicedata", nullable = true)
	@XmlElement
	private String spreadjuicedata;

	@Column(name = "spreadjuice", nullable = true)
	@XmlElement
	private Float spreadjuice;
	
	@Column(name = "totalindicator", nullable = true)
	@XmlElement
	private String totalindicator;

	@Column(name = "totaldata", nullable = true)
	@XmlElement
	private String totaldata;

	@Column(name = "total", nullable = true)
	@XmlElement
	private Float total;

	@Column(name = "totaljuiceindicator", nullable = true)
	@XmlElement
	private String totaljuiceindicator;

	@Column(name = "totaljuicedata", nullable = true)
	@XmlElement
	private String totaljuicedata;

	@Column(name = "totaljuice", nullable = true)
	@XmlElement
	private Float totaljuice;

	@Column(name = "mlindicator", nullable = true)
	@XmlElement
	private String mlindicator;

	@Column(name = "mldata", nullable = true)
	@XmlElement
	private String mldata;

	@Column(name = "ml", nullable = true)
	@XmlElement
	private Float ml;

	@Column(name = "accountconfirmation", nullable = true)
	@XmlElement
	private String accountconfirmation;
	
	@Column(name = "accounthtml", nullable = true, columnDefinition="text")
	@XmlElement
	@Lob
	private String accounthtml;

	@Column(name = "iscompleted", nullable = true)
	@XmlElement
	private Boolean iscompleted = new Boolean(false);

	@Column(name = "proxy", nullable = true)
	@XmlElement
	private String proxy;
	
	@Column(name = "attempts", nullable = false)
	@XmlElement
	private Integer attempts;
	
	@Column(name = "currentattempts", nullable = true)
	@XmlElement
	private Integer currentattempts;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "attempttime", nullable = true)
	@XmlElement
	private Date attempttime;

	@Column(name = "errormessage", nullable = true)
	@XmlElement
	private String errormessage;

	@Column(name = "errorexception", nullable = true)
	@XmlElement
	private String errorexception;

	@Column(name = "errorcode", nullable = true)
	@XmlElement
	private Integer errorcode;

	@Column(name = "status", nullable = true)
	@XmlElement
	private String status;

	@Column(name = "riskamount", nullable = true)
	@XmlElement
	private Float riskamount;

	@Column(name = "towinamount", nullable = true)
	@XmlElement
	private Float towinamount;

	@Column(name = "eventresult", nullable = true)
	@XmlElement
	private String eventresult;

	@Column(name = "eventresultamount", nullable = true)
	@XmlElement
	private Float eventresultamount;

	@Column(name = "accesstoken", nullable = true)
	@XmlElement
	private String accesstoken;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "eventdatetime", nullable = false)
	@XmlElement
	private Date eventdatetime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datecreated", nullable = false)
	@XmlElement
	private Date datecreated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datemodified", nullable = false)
	@XmlElement
	private Date datemodified;

	@Column(name = "type", nullable = false, length = 20)
	@XmlElement
	private String type;

	/**
	 * 
	 */
	public ParlayAccountEvent() {
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
	 * @return the parlayid
	 */
	public Long getParlayid() {
		return parlayid;
	}

	/**
	 * @param parlayid the parlayid to set
	 */
	public void setParlayid(Long parlayid) {
		this.parlayid = parlayid;
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
	 * @return the accountname
	 */
	public String getAccountname() {
		return accountname;
	}

	/**
	 * @param accountname the accountname to set
	 */
	public void setAccountname(String accountname) {
		this.accountname = accountname;
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
	 * @return the parlaytype
	 */
	public String getParlaytype() {
		return parlaytype;
	}

	/**
	 * @param parlaytype the parlaytype to set
	 */
	public void setParlaytype(String parlaytype) {
		this.parlaytype = parlaytype;
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
	 * @return the wagertype
	 */
	public String getWagertype() {
		return wagertype;
	}

	/**
	 * @param wagertype the wagertype to set
	 */
	public void setWagertype(String wagertype) {
		this.wagertype = wagertype;
	}

	/**
	 * @return the amount
	 */
	public Float getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Float amount) {
		this.amount = amount;
	}

	/**
	 * @return the actualamount
	 */
	public Float getActualamount() {
		return actualamount;
	}

	/**
	 * @param actualamount the actualamount to set
	 */
	public void setActualamount(Float actualamount) {
		this.actualamount = actualamount;
	}

	/**
	 * @return the maxspreadamount
	 */
	public Float getMaxspreadamount() {
		return maxspreadamount;
	}

	/**
	 * @param maxspreadamount the maxspreadamount to set
	 */
	public void setMaxspreadamount(Float maxspreadamount) {
		this.maxspreadamount = maxspreadamount;
	}

	/**
	 * @return the maxmlamount
	 */
	public Float getMaxmlamount() {
		return maxmlamount;
	}

	/**
	 * @param maxmlamount the maxmlamount to set
	 */
	public void setMaxmlamount(Float maxmlamount) {
		this.maxmlamount = maxmlamount;
	}

	/**
	 * @return the maxtotalamount
	 */
	public Float getMaxtotalamount() {
		return maxtotalamount;
	}

	/**
	 * @param maxtotalamount the maxtotalamount to set
	 */
	public void setMaxtotalamount(Float maxtotalamount) {
		this.maxtotalamount = maxtotalamount;
	}

	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the ownerpercentage
	 */
	public Integer getOwnerpercentage() {
		return ownerpercentage;
	}

	/**
	 * @param ownerpercentage the ownerpercentage to set
	 */
	public void setOwnerpercentage(Integer ownerpercentage) {
		this.ownerpercentage = ownerpercentage;
	}

	/**
	 * @return the partnerpercentage
	 */
	public Integer getPartnerpercentage() {
		return partnerpercentage;
	}

	/**
	 * @param partnerpercentage the partnerpercentage to set
	 */
	public void setPartnerpercentage(Integer partnerpercentage) {
		this.partnerpercentage = partnerpercentage;
	}

	/**
	 * @return the spreadindicator
	 */
	public String getSpreadindicator() {
		return spreadindicator;
	}

	/**
	 * @param spreadindicator the spreadindicator to set
	 */
	public void setSpreadindicator(String spreadindicator) {
		this.spreadindicator = spreadindicator;
	}

	/**
	 * @return the spreaddata
	 */
	public String getSpreaddata() {
		return spreaddata;
	}

	/**
	 * @param spreaddata the spreaddata to set
	 */
	public void setSpreaddata(String spreaddata) {
		this.spreaddata = spreaddata;
	}

	/**
	 * @return the spread
	 */
	public Float getSpread() {
		return spread;
	}

	/**
	 * @param spread the spread to set
	 */
	public void setSpread(Float spread) {
		this.spread = spread;
	}

	/**
	 * @return the spreadjuiceindicator
	 */
	public String getSpreadjuiceindicator() {
		return spreadjuiceindicator;
	}

	/**
	 * @param spreadjuiceindicator the spreadjuiceindicator to set
	 */
	public void setSpreadjuiceindicator(String spreadjuiceindicator) {
		this.spreadjuiceindicator = spreadjuiceindicator;
	}

	/**
	 * @return the spreadjuicedata
	 */
	public String getSpreadjuicedata() {
		return spreadjuicedata;
	}

	/**
	 * @param spreadjuicedata the spreadjuicedata to set
	 */
	public void setSpreadjuicedata(String spreadjuicedata) {
		this.spreadjuicedata = spreadjuicedata;
	}

	/**
	 * @return the spreadjuice
	 */
	public Float getSpreadjuice() {
		return spreadjuice;
	}

	/**
	 * @param spreadjuice the spreadjuice to set
	 */
	public void setSpreadjuice(Float spreadjuice) {
		this.spreadjuice = spreadjuice;
	}

	/**
	 * @return the totalindicator
	 */
	public String getTotalindicator() {
		return totalindicator;
	}

	/**
	 * @param totalindicator the totalindicator to set
	 */
	public void setTotalindicator(String totalindicator) {
		this.totalindicator = totalindicator;
	}

	/**
	 * @return the totaldata
	 */
	public String getTotaldata() {
		return totaldata;
	}

	/**
	 * @param totaldata the totaldata to set
	 */
	public void setTotaldata(String totaldata) {
		this.totaldata = totaldata;
	}

	/**
	 * @return the total
	 */
	public Float getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(Float total) {
		this.total = total;
	}

	/**
	 * @return the totaljuiceindicator
	 */
	public String getTotaljuiceindicator() {
		return totaljuiceindicator;
	}

	/**
	 * @param totaljuiceindicator the totaljuiceindicator to set
	 */
	public void setTotaljuiceindicator(String totaljuiceindicator) {
		this.totaljuiceindicator = totaljuiceindicator;
	}

	/**
	 * @return the totaljuicedata
	 */
	public String getTotaljuicedata() {
		return totaljuicedata;
	}

	/**
	 * @param totaljuicedata the totaljuicedata to set
	 */
	public void setTotaljuicedata(String totaljuicedata) {
		this.totaljuicedata = totaljuicedata;
	}

	/**
	 * @return the totaljuice
	 */
	public Float getTotaljuice() {
		return totaljuice;
	}

	/**
	 * @param totaljuice the totaljuice to set
	 */
	public void setTotaljuice(Float totaljuice) {
		this.totaljuice = totaljuice;
	}

	/**
	 * @return the mlindicator
	 */
	public String getMlindicator() {
		return mlindicator;
	}

	/**
	 * @param mlindicator the mlindicator to set
	 */
	public void setMlindicator(String mlindicator) {
		this.mlindicator = mlindicator;
	}

	/**
	 * @return the mldata
	 */
	public String getMldata() {
		return mldata;
	}

	/**
	 * @param mldata the mldata to set
	 */
	public void setMldata(String mldata) {
		this.mldata = mldata;
	}

	/**
	 * @return the ml
	 */
	public Float getMl() {
		return ml;
	}

	/**
	 * @param ml the ml to set
	 */
	public void setMl(Float ml) {
		this.ml = ml;
	}

	/**
	 * @return the accountconfirmation
	 */
	public String getAccountconfirmation() {
		return accountconfirmation;
	}

	/**
	 * @param accountconfirmation the accountconfirmation to set
	 */
	public void setAccountconfirmation(String accountconfirmation) {
		this.accountconfirmation = accountconfirmation;
	}

	/**
	 * @return the accounthtml
	 */
	public String getAccounthtml() {
		return accounthtml;
	}

	/**
	 * @param accounthtml the accounthtml to set
	 */
	public void setAccounthtml(String accounthtml) {
		this.accounthtml = accounthtml;
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
	 * @return the proxy
	 */
	public String getProxy() {
		return proxy;
	}

	/**
	 * @param proxy the proxy to set
	 */
	public void setProxy(String proxy) {
		this.proxy = proxy;
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
	 * @return the errormessage
	 */
	public String getErrormessage() {
		return errormessage;
	}

	/**
	 * @param errormessage the errormessage to set
	 */
	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}

	/**
	 * @return the errorexception
	 */
	public String getErrorexception() {
		return errorexception;
	}

	/**
	 * @param errorexception the errorexception to set
	 */
	public void setErrorexception(String errorexception) {
		this.errorexception = errorexception;
	}

	/**
	 * @return the errorcode
	 */
	public Integer getErrorcode() {
		return errorcode;
	}

	/**
	 * @param errorcode the errorcode to set
	 */
	public void setErrorcode(Integer errorcode) {
		this.errorcode = errorcode;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	public Float getTowinamount() {
		return towinamount;
	}

	/**
	 * @param towinamount the towinamount to set
	 */
	public void setTowinamount(Float towinamount) {
		this.towinamount = towinamount;
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
	 * @return the accesstoken
	 */
	public String getAccesstoken() {
		return accesstoken;
	}

	/**
	 * @param accesstoken the accesstoken to set
	 */
	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ParlayAccountEvent [id=" + id + ", userid=" + userid + ", parlayid=" + parlayid + ", eventid=" + eventid
				+ ", eventname=" + eventname + ", accountid=" + accountid + ", accountname=" + accountname
				+ ", groupid=" + groupid + ", parlaytype=" + parlaytype + ", sport=" + sport + ", wagertype="
				+ wagertype + ", amount=" + amount + ", actualamount=" + actualamount + ", maxspreadamount="
				+ maxspreadamount + ", maxmlamount=" + maxmlamount + ", maxtotalamount=" + maxtotalamount
				+ ", timezone=" + timezone + ", ownerpercentage=" + ownerpercentage + ", partnerpercentage="
				+ partnerpercentage + ", spreadindicator=" + spreadindicator + ", spreaddata=" + spreaddata
				+ ", spread=" + spread + ", spreadjuiceindicator=" + spreadjuiceindicator + ", spreadjuicedata="
				+ spreadjuicedata + ", spreadjuice=" + spreadjuice + ", totalindicator=" + totalindicator
				+ ", totaldata=" + totaldata + ", total=" + total + ", totaljuiceindicator=" + totaljuiceindicator
				+ ", totaljuicedata=" + totaljuicedata + ", totaljuice=" + totaljuice + ", mlindicator=" + mlindicator
				+ ", mldata=" + mldata + ", ml=" + ml + ", accountconfirmation=" + accountconfirmation
				+ ", accounthtml=" + accounthtml + ", iscompleted=" + iscompleted + ", proxy=" + proxy + ", attempts="
				+ attempts + ", currentattempts=" + currentattempts + ", attempttime=" + attempttime + ", errormessage="
				+ errormessage + ", errorexception=" + errorexception + ", errorcode=" + errorcode + ", status="
				+ status + ", riskamount=" + riskamount + ", towinamount=" + towinamount + ", eventresult="
				+ eventresult + ", eventresultamount=" + eventresultamount + ", accesstoken=" + accesstoken
				+ ", eventdatetime=" + eventdatetime + ", datecreated=" + datecreated + ", datemodified=" + datemodified
				+ ", type=" + type + "]";
	}
}