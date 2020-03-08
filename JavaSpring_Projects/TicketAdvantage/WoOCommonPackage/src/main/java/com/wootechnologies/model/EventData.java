package com.wootechnologies.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author jmiller
 *
 */
@XmlRootElement(name = "eventdata")
@XmlAccessorType(XmlAccessType.NONE)
public class EventData implements Serializable {
	private static final long serialVersionUID = 1L;

    @XmlElement
	private Long userid;

	@XmlElement
	private Integer rotationid;

	@XmlElement
	private String sport;

	@XmlElement
	private String linetype;

	@XmlElement
	private String lineindicator;

	@XmlElement
	private String line;

	@XmlElement
	private String juiceindicator;

	@XmlElement
	private String juice;

	@XmlElement
	private String amount;

	@XmlElement
	private String riskwin;

	@XmlElement
	private Long accountid;

	@XmlElement
	private Long groupid;

	/**
	 * 
	 */
	public EventData() {
		super();
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
	 * @return the linetype
	 */
	public String getLinetype() {
		return linetype;
	}

	/**
	 * @param linetype the linetype to set
	 */
	public void setLinetype(String linetype) {
		this.linetype = linetype;
	}

	/**
	 * @return the lineindicator
	 */
	public String getLineindicator() {
		return lineindicator;
	}

	/**
	 * @param lineindicator the lineindicator to set
	 */
	public void setLineindicator(String lineindicator) {
		this.lineindicator = lineindicator;
	}

	/**
	 * @return the line
	 */
	public String getLine() {
		return line;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(String line) {
		this.line = line;
	}

	/**
	 * @return the juiceindicator
	 */
	public String getJuiceindicator() {
		return juiceindicator;
	}

	/**
	 * @param juiceindicator the juiceindicator to set
	 */
	public void setJuiceindicator(String juiceindicator) {
		this.juiceindicator = juiceindicator;
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
	 * @return the riskwin
	 */
	public String getRiskwin() {
		return riskwin;
	}

	/**
	 * @param riskwin the riskwin to set
	 */
	public void setRiskwin(String riskwin) {
		this.riskwin = riskwin;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventData [userid=" + userid + ", rotationid=" + rotationid + ", sport=" + sport + ", linetype="
				+ linetype + ", lineindicator=" + lineindicator + ", line=" + line + ", juiceindicator="
				+ juiceindicator + ", juice=" + juice + ", amount=" + amount + ", riskwin=" + riskwin + ", accountid="
				+ accountid + ", groupid=" + groupid + "]";
	}
}
