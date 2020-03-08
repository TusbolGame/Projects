/**
 * 
 */
package com.wootechnologies.services.dao.sites.espn;

/**
 * @author jmiller
 *
 */
public class EspnBasketBallTeamData {
	private String name;
	private String rosterUrl;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the rosterUrl
	 */
	public String getRosterUrl() {
		return rosterUrl;
	}

	/**
	 * @param rosterUrl the rosterUrl to set
	 */
	public void setRosterUrl(String rosterUrl) {
		this.rosterUrl = rosterUrl;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EspnBballTeamData [name=" + name + ", rosterUrl=" + rosterUrl + "]";
	}
}
