/**
 * 
 */
package com.ticketadvantage.services.dao.sites.elitesports;

import java.io.Serializable;
import java.util.Map;

import com.ticketadvantage.services.dao.sites.SiteTeamPackage;

/**
 * @author jmiller
 *
 */
public class EliteSportsTeamPackage extends SiteTeamPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	private String tDate;
	private String tTime;
	private Map<String, String> hiddenData;

	/**
	 * 
	 */
	public EliteSportsTeamPackage() {
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
	 * @return the hiddenData
	 */
	public Map<String, String> getHiddenData() {
		return hiddenData;
	}

	/**
	 * @param hiddenData the hiddenData to set
	 */
	public void setHiddenData(Map<String, String> hiddenData) {
		this.hiddenData = hiddenData;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EliteSportsTeamPackage [tDate=" + tDate + ",\n tTime=" + tTime + ",\n hiddenData=" + hiddenData + "]\n" + super.toString();
	}
}