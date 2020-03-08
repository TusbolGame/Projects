/**
 * 
 */
package com.ticketadvantage.services.model;

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
public class PreviewOutput implements Serializable {
	private static final long serialVersionUID = 3156058159245563904L;

	@XmlElement
	private Long accountid;

	@XmlElement
	private String accountname;

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
	public PreviewOutput() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
		return "PreviewOutput [accountid=" + accountid + ", accountname=" + accountname + ", lineindicator="
				+ lineindicator + ", line=" + line + ", juiceindicator=" + juiceindicator + ", juice=" + juice + "]";
	}
}