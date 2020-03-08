/**
 * 
 */
package com.ticketadvantage.services.dao.sites.linetracker;

import java.io.Serializable;
import java.util.Map;

import com.ticketadvantage.services.dao.sites.SiteTeamPackage;

/**
 * @author jmiller
 *
 */
public class LineTrackerTeamPackage extends SiteTeamPackage implements Serializable {
	private static final long serialVersionUID = 1L;

	private String tDate;
	private String tTime;
	private String gameSpreadSelectFor;
	private String gameTotalSelectFor;
	private Map<String, String> hiddenData;

	/**
	 * 
	 */
	public LineTrackerTeamPackage() {
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

	/**
	 * @return the gameSpreadSelectFor
	 */
	public String getGameSpreadSelectFor() {
		return gameSpreadSelectFor;
	}

	/**
	 * @param gameSpreadSelectFor the gameSpreadSelectFor to set
	 */
	public void setGameSpreadSelectFor(String gameSpreadSelectFor) {
		this.gameSpreadSelectFor = gameSpreadSelectFor;
	}

	/**
	 * @return the gameTotalSelectFor
	 */
	public String getGameTotalSelectFor() {
		return gameTotalSelectFor;
	}

	/**
	 * @param gameTotalSelectFor the gameTotalSelectFor to set
	 */
	public void setGameTotalSelectFor(String gameTotalSelectFor) {
		this.gameTotalSelectFor = gameTotalSelectFor;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LineTrackerTeamPackage [tDate=" + tDate + ", tTime=" + tTime + ", gameSpreadSelectFor="
				+ gameSpreadSelectFor + ", gameTotalSelectFor=" + gameTotalSelectFor + ", hiddenData=" + hiddenData
				+ "]\n" + super.toString();
	}
}