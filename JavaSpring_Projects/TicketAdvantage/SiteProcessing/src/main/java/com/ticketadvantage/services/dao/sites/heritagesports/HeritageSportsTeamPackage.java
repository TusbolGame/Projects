/**
 * 
 */
package com.ticketadvantage.services.dao.sites.heritagesports;

import java.io.Serializable;
import java.util.Map;

import com.ticketadvantage.services.dao.sites.SiteTeamPackage;

/**
 * @author jmiller
 *
 */
public class HeritageSportsTeamPackage extends SiteTeamPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	private String tDate;
	private String tTime;
	private Integer spreadMax;
	private Integer totalMax;
	private Integer mlMax;
	private Map<String, String> hiddenData;

	/**
	 * 
	 */
	public HeritageSportsTeamPackage() {
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
		return "HeritageSportsTeamPackage [tDate=" + tDate + ", tTime=" + tTime + ", spreadMax=" + spreadMax + ", totalMax="
				+ totalMax + ", mlMax=" + mlMax + ", hiddenData=" + hiddenData + "] " + super.toString();
	}
}