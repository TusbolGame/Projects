/**
 * 
 */
package com.wootechnologies.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "accountevent")
@XmlRootElement(name = "accountevent")
@XmlAccessorType(XmlAccessType.NONE)
public class AccountEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountevent_generator")
	@SequenceGenerator(name = "accountevent_generator", sequenceName = "accountevent_id_seq", allocationSize = 1)
	@Column(name = "id", updatable = false, nullable = false)
	@XmlElement(nillable = true)
	private Long id;

	@Column(name = "name", unique = false, nullable = false, length = 100)
	@XmlElement
	private String name;

	@Column(name = "spreadid", nullable = true)
	@XmlElement
	private Long spreadid;

	@Column(name = "totalid", nullable = true)
	@XmlElement
	private Long totalid;

	@Column(name = "mlid", nullable = true)
	@XmlElement
	private Long mlid;

	@Column(name = "eventid", unique = false, nullable = true)
	@XmlElement
	private Integer eventid;

	@Column(name = "eventname", unique = false, nullable = false, length = 100)
	@XmlElement
	private String eventname;

	@Column(name = "userid", nullable = false)
	@XmlElement
	private Long userid;

	@Column(name = "accountid", nullable = false)
	@XmlElement
	private Long accountid;

	@Column(name = "groupid", nullable = false)
	@XmlElement
	private Long groupid;

	@Column(name = "type", nullable = false, length = 100)
	@XmlElement
	private String type;

	@Column(name = "sport", nullable = false, length = 100)
	@XmlElement
	private String sport;

	@Column(name = "wagertype", nullable = true)
	@XmlElement
	private String wagertype;

	@Column(name = "amount", nullable = true)
	@XmlElement
	private String amount;

	@Column(name = "actualamount", nullable = true)
	@XmlElement
	private String actualamount;

	@Column(name = "maxspreadamount", nullable = true)
	@XmlElement
	private Integer maxspreadamount;

	@Column(name = "maxmlamount", nullable = true)
	@XmlElement
	private Integer maxmlamount;

	@Column(name = "maxtotalamount", nullable = true)
	@XmlElement
	private Integer maxtotalamount;

	// newly added field
	@Column(name = "maxteamtotalamount", nullable = true)
	@XmlElement
	private Integer maxteamtotalamount;
	
	@Column(name = "teamtotaljuice", nullable = true)
	@XmlElement
	private Float teamtotaljuice;
	
	@Column(name = "teamtotalindicator", nullable = true)
	@XmlElement
	private String teamtotalindicator;
	
	@Column(name = "teamtotal", nullable = true)
	@XmlElement
	private Float teamtotal;
	
	@Column(name = "timezone", nullable = true)
	@XmlElement
	private String timezone = "ET";

	@Column(name = "ownerpercentage", nullable = true)
	@XmlElement(nillable = true)
	private Integer ownerpercentage;

	@Column(name = "partnerpercentage", nullable = true)
	@XmlElement(nillable = true)
	private Integer partnerpercentage;

	@Column(name = "spreadindicator", nullable = true)
	@XmlElement
	private String spreadindicator;

	@Column(name = "spread", nullable = true)
	@XmlElement
	private Float spread;

	@Column(name = "spreadjuice", nullable = true)
	@XmlElement
	private Float spreadjuice;

	@Column(name = "totalindicator", nullable = true)
	@XmlElement
	private String totalindicator;

	@Column(name = "total", nullable = true)
	@XmlElement
	private Float total;

	@Column(name = "totaljuice", nullable = true)
	@XmlElement
	private Float totaljuice;

	@Column(name = "mlindicator", nullable = true)
	@XmlElement
	private String mlindicator;

	@Column(name = "ml", nullable = true)
	@XmlElement
	private Float ml;

	@Column(name = "mljuice", nullable = true)
	@XmlElement
	private Float mljuice;

	@Column(name = "accountconfirmation", nullable = true)
	@XmlElement
	private String accountconfirmation;

	@Column(name = "accounthtml", nullable = true, columnDefinition = "text")
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

	@Column(name = "riskamount", nullable = true)
	@XmlElement
	private String riskamount;

	@Column(name = "towinamount", nullable = true)
	@XmlElement
	private String towinamount;

	@Column(name = "eventresult", nullable = true)
	@XmlElement
	private String eventresult;

	@Column(name = "eventresultamount", nullable = true)
	@XmlElement
	private Float eventresultamount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountid", referencedColumnName = "id", insertable = false, updatable = false)
	private Accounts account;

	@Column(name = "accesstoken", nullable = true)
	@XmlElement
	private String accesstoken;

	@Column(name = "iscomplexcaptcha", nullable = true)
	@XmlElement
	private Boolean iscomplexcaptcha = new Boolean(false);

	@Column(name = "humanspeed", nullable = true)
	@XmlElement
	private Boolean humanspeed = new Boolean(false);

//	@OneToOne(fetch=FetchType.LAZY, mappedBy = "accountEvent", cascade = CascadeType.ALL)
//	private AccountEventFinal accountEventFinal;

	/**
	 * 
	 */
	public AccountEvent() {
		super();
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
	 * @return the spreadid
	 */
	public Long getSpreadid() {
		return spreadid;
	}

	/**
	 * @param spreadid the spreadid to set
	 */
	public void setSpreadid(Long spreadid) {
		this.spreadid = spreadid;
	}

	/**
	 * @return the totalid
	 */
	public Long getTotalid() {
		return totalid;
	}

	/**
	 * @param totalid the totalid to set
	 */
	public void setTotalid(Long totalid) {
		this.totalid = totalid;
	}

	/**
	 * @return the mlid
	 */
	public Long getMlid() {
		return mlid;
	}

	/**
	 * @param mlid the mlid to set
	 */
	public void setMlid(Long mlid) {
		this.mlid = mlid;
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
	 * @return the actualamount
	 */
	public String getActualamount() {
		return actualamount;
	}

	/**
	 * @param actualamount the actualamount to set
	 */
	public void setActualamount(String actualamount) {
		this.actualamount = actualamount;
	}

	/**
	 * @return the maxspreadamount
	 */
	public Integer getMaxspreadamount() {
		return maxspreadamount;
	}

	/**
	 * @param maxspreadamount the maxspreadamount to set
	 */
	public void setMaxspreadamount(Integer maxspreadamount) {
		this.maxspreadamount = maxspreadamount;
	}

	/**
	 * @return the maxmlamount
	 */
	public Integer getMaxmlamount() {
		return maxmlamount;
	}

	/**
	 * @param maxmlamount the maxmlamount to set
	 */
	public void setMaxmlamount(Integer maxmlamount) {
		this.maxmlamount = maxmlamount;
	}

	/**
	 * @return the maxtotalamount
	 */
	public Integer getMaxtotalamount() {
		return maxtotalamount;
	}

	/**
	 * @param maxtotalamount the maxtotalamount to set
	 */
	public void setMaxtotalamount(Integer maxtotalamount) {
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
	 * @return the mljuice
	 */
	public Float getMljuice() {
		return mljuice;
	}

	/**
	 * @param mljuice the mljuice to set
	 */
	public void setMljuice(Float mljuice) {
		this.mljuice = mljuice;
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
	 * @return the riskamount
	 */
	public String getRiskamount() {
		return riskamount;
	}

	/**
	 * @param riskamount the riskamount to set
	 */
	public void setRiskamount(String riskamount) {
		this.riskamount = riskamount;
	}

	/**
	 * @return the towinamount
	 */
	public String getTowinamount() {
		return towinamount;
	}

	/**
	 * @param towinamount the towinamount to set
	 */
	public void setTowinamount(String towinamount) {
		this.towinamount = towinamount;
	}

	/**
	 * 
	 * @return
	 */
	public String getEventresult() {
		return eventresult;
	}

	/**
	 * 
	 * @param eventresult
	 */
	public void setEventresult(String eventresult) {
		this.eventresult = eventresult;
	}

	/**
	 * 
	 * @return
	 */
	public Float getEventresultamount() {
		return eventresultamount;
	}

	/**
	 * 
	 * @param eventresultamount
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
	 * @return the account
	 */
	public Accounts getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Accounts account) {
		this.account = account;
	}

	/**
	 * @return the iscomplexcaptcha
	 */
	public Boolean getIscomplexcaptcha() {
		return iscomplexcaptcha;
	}

	/**
	 * @param iscomplexcaptcha the iscomplexcaptcha to set
	 */
	public void setIscomplexcaptcha(Boolean iscomplexcaptcha) {
		this.iscomplexcaptcha = iscomplexcaptcha;
	}
	
	/**
	 * @return the maxteamtotalamount
	 */
	public Integer getMaxteamtotalamount() {
		return maxteamtotalamount;
	}

	/**
	 * @param maxteamtotalamount the maxteamtotalamount to set
	 */
	public void setMaxteamtotalamount(Integer maxteamtotalamount) {
		this.maxteamtotalamount = maxteamtotalamount;
	}
	
	public Float getTeamtotaljuice() {
		return teamtotaljuice;
	}

	public void setTeamtotaljuice(Float teamtotaljuice) {
		this.teamtotaljuice = teamtotaljuice;
	}

	public String getTeamtotalindicator() {
		return teamtotalindicator;
	}

	public void setTeamtotalindicator(String teamtotalindicator) {
		this.teamtotalindicator = teamtotalindicator;
	}
	
	public Float getTeamtotal() {
		return teamtotal;
	}

	public void setTeamtotal(Float teamtotal) {
		this.teamtotal = teamtotal;
	}

	/**
	 * @return the humanspeed
	 */
	public Boolean getHumanspeed() {
		return humanspeed;
	}

	/**
	 * @param humanspeed the humanspeed to set
	 */
	public void setHumanspeed(Boolean humanspeed) {
		this.humanspeed = humanspeed;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AccountEvent [id=" + id + ", name=" + name + ", spreadid=" + spreadid + ", totalid=" + totalid
				+ ", mlid=" + mlid + ", eventid=" + eventid + ", eventname=" + eventname + ", userid=" + userid
				+ ", accountid=" + accountid + ", groupid=" + groupid + ", type=" + type + ", sport=" + sport
				+ ", wagertype=" + wagertype + ", amount=" + amount + ", actualamount=" + actualamount
				+ ", maxspreadamount=" + maxspreadamount + ", maxmlamount=" + maxmlamount + ", maxtotalamount="
				+ maxtotalamount + ", maxteamtotalamount=" + maxteamtotalamount + ", teamtotaljuice=" + teamtotaljuice
				+ ", teamtotalindicator=" + teamtotalindicator + ", teamtotal=" + teamtotal + ", timezone=" + timezone
				+ ", ownerpercentage=" + ownerpercentage + ", partnerpercentage=" + partnerpercentage
				+ ", spreadindicator=" + spreadindicator + ", spread=" + spread + ", spreadjuice=" + spreadjuice
				+ ", totalindicator=" + totalindicator + ", total=" + total + ", totaljuice=" + totaljuice
				+ ", mlindicator=" + mlindicator + ", ml=" + ml + ", mljuice=" + mljuice + ", accountconfirmation="
				+ accountconfirmation + ", accounthtml=" + accounthtml + ", iscompleted=" + iscompleted + ", proxy="
				+ proxy + ", attempts=" + attempts + ", currentattempts=" + currentattempts + ", attempttime="
				+ attempttime + ", errormessage=" + errormessage + ", errorexception=" + errorexception + ", errorcode="
				+ errorcode + ", status=" + status + ", eventdatetime=" + eventdatetime + ", datecreated=" + datecreated
				+ ", datemodified=" + datemodified + ", riskamount=" + riskamount + ", towinamount=" + towinamount
				+ ", eventresult=" + eventresult + ", eventresultamount=" + eventresultamount + ", account=" + account
				+ ", accesstoken=" + accesstoken + ", iscomplexcaptcha=" + iscomplexcaptcha + ", humanspeed="
				+ humanspeed + "]";
	}
}