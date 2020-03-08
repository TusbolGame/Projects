/**
 * 
 */
package com.wootechnologies.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author jmiller
 *
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class PreviewResponse implements Serializable {
	private static final long serialVersionUID = 3156058159245563904L;

	@XmlElement
	private Integer rotationid;

	@XmlElement
	private String linetype;

	@XmlElement
	private String sporttype;

	@XmlElement
	private Long accountid;

	@XmlElement
	private String accountname;

	@XmlElement
	private String amount;

	@XmlElement
	private String lineindicator = "";

	@XmlElement
	private String line = "";

	@XmlElement
	private String juiceindicator = "";

	@XmlElement
	private String juice = "";

	/**
	 * 
	 */
	public PreviewResponse() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
	 * @return the sporttype
	 */
	public String getSporttype() {
		return sporttype;
	}

	/**
	 * @param sporttype the sporttype to set
	 */
	public void setSporttype(String sporttype) {
		this.sporttype = sporttype;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PreviewResponse [rotationid=" + rotationid + ", linetype=" + linetype + ", sporttype=" + sporttype
				+ ", accountid=" + accountid + ", accountname=" + accountname + ", amount=" + amount
				+ ", lineindicator=" + lineindicator + ", line=" + line + ", juiceindicator=" + juiceindicator
				+ ", juice=" + juice + "]";
	}
}