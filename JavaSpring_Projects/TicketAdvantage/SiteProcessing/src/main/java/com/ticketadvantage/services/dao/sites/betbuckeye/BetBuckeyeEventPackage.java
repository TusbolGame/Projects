/**
 * 
 */
package com.ticketadvantage.services.dao.sites.betbuckeye;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.ticketadvantage.services.dao.sites.SiteEventPackage;

/**
 * @author jmiller
 *
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class BetBuckeyeEventPackage extends SiteEventPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	private String inetWagerNumber;
	private Integer spreadMax;
	private Integer totalMax;
	private Integer mlMax;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 */
	public BetBuckeyeEventPackage() {
		super();
	}
	
	/**
	 * @return the inetWagerNumber
	 */
	public String getInetWagerNumber() {
		return inetWagerNumber;
	}

	/**
	 * @param inetWagerNumber the inetWagerNumber to set
	 */
	public void setInetWagerNumber(String inetWagerNumber) {
		this.inetWagerNumber = inetWagerNumber;
	}

	/**
	 * @return the spreadMax
	 */
	public Integer getSpreadMax() {
		return spreadMax;
	}

	/**
	 * @param spreadMax the spreadMax to set
	 */
	public void setSpreadMax(Integer spreadMax) {
		this.spreadMax = spreadMax;
	}

	/**
	 * @return the totalMax
	 */
	public Integer getTotalMax() {
		return totalMax;
	}

	/**
	 * @param totalMax the totalMax to set
	 */
	public void setTotalMax(Integer totalMax) {
		this.totalMax = totalMax;
	}

	/**
	 * @return the mlMax
	 */
	public Integer getMlMax() {
		return mlMax;
	}

	/**
	 * @param mlMax the mlMax to set
	 */
	public void setMlMax(Integer mlMax) {
		this.mlMax = mlMax;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BetBuckeyeEventPackage [inetWagerNumber=" + inetWagerNumber + ", spreadMax=" + spreadMax + ", totalMax="
				+ totalMax + ", mlMax=" + mlMax + "] " + super.toString();
	}
}