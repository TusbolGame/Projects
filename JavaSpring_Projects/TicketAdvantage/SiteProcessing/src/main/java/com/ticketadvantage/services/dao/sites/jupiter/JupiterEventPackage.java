/**
 * 
 */
package com.ticketadvantage.services.dao.sites.jupiter;

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
public class JupiterEventPackage extends SiteEventPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	private String inetWagerNumber;
	private Integer spreadMax;
	private Integer totalMax;
	private Integer mlMax;
	
	//newly added field
	private Integer teamTotalMax;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 */
	public JupiterEventPackage() {
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
	
	/**
	 * @return the tamTotalMax
	 */
	public Integer getTeamTotalMax() {
		return teamTotalMax;
	}

	/**
	 * @param teamTotalMax the teamTotalMax to set
	 */
	public void setTeamTotalMax(Integer teamTotalMax) {
		this.teamTotalMax = teamTotalMax;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FiveDimeseEventPackage [inetWagerNumber=" + inetWagerNumber + ", spreadMax=" + spreadMax + ", totalMax="
				+ totalMax + ", mlMax=" + mlMax + ", teamTotalMax=" + teamTotalMax + "] " + super.toString();
	}
}