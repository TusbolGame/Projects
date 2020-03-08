/**
 * 
 */
package com.ticketadvantage.services.dao.sites.fiveDimes;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.ticketadvantage.services.dao.sites.SiteTeamPackage;

/**
 * @author jmiller
 *
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class FiveDimesTeamPackage extends SiteTeamPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	private String tDate;
	private String tTime;
	private Integer spreadMax;
	private Integer totalMax;
	private Integer mlMax;
	
	//newly added fields
	private Integer teamTotalMax;

	/**
	 * 
	 */
	public FiveDimesTeamPackage() {
		super();
	}

	/**
	 * @return the tDate
	 */
	public String gettDate() {
		return tDate;
	}

	/**
	 * @param tDate the tDate to set
	 */
	public void settDate(String tDate) {
		this.tDate = tDate;
	}

	/**
	 * @return the tTime
	 */
	public String gettTime() {
		return tTime;
	}

	/**
	 * @param tTime the tTime to set
	 */
	public void settTime(String tTime) {
		this.tTime = tTime;
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

	public Integer getTeamTotalMax() {
		return teamTotalMax;
	}

	public void setTeamTotalMax(Integer teamTotalMax) {
		this.teamTotalMax = teamTotalMax;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FiveDimesTeamPackage [tDate=" + tDate + ", tTime=" + tTime + ", spreadMax=" + spreadMax + ", totalMax="
				+ totalMax + ", mlMax=" + mlMax + ", teamTotalMax=" + teamTotalMax + "] " + super.toString();
	}
}