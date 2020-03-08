/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsportsnew;

import java.io.Serializable;
import java.util.Map;

import com.ticketadvantage.services.dao.sites.SiteTeamPackage;

/**
 * @author jmiller
 *
 */
public class TDSportsNewTeamPackage extends SiteTeamPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	private String tDate;
	private String tTime;
	private Map<String, String> hiddenData;

	/**
	 * 
	 */
	public TDSportsNewTeamPackage() {
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
		return "TDSportsNewTeamPackage [tDate=" + tDate + ",\n tTime=" + tTime + ",\n hiddenData=" + hiddenData + "]\n" + super.toString();
	}
}