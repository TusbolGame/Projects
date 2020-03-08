/**
 * 
 */
package com.wootechnologies.services.dao.sites.espn;

/**
 * @author jmiller
 *
 */
public class EspnCollegeBasketballGameData extends EspnBasketballGameData {
	private Boolean isconferencegame = new Boolean(false);
	private Boolean neutralsite = new Boolean(false);
	private String awaycollegename = "";
	private String homecollegename = "";
	private String awaymascotname = "";
	private String homemascotname = "";
	private String awayshortname = "";
	private String homeshortname = "";
	private Integer awayranking = new Integer(0);
	private Integer homeranking = new Integer(0);

	/**
	 * @return the isconferencegame
	 */
	public Boolean getIsconferencegame() {
		return isconferencegame;
	}
	/**
	 * @param isconferencegame the isconferencegame to set
	 */
	public void setIsconferencegame(Boolean isconferencegame) {
		this.isconferencegame = isconferencegame;
	}
	/**
	 * @return the neutralsite
	 */
	public Boolean getNeutralsite() {
		return neutralsite;
	}
	/**
	 * @param neutralsite the neutralsite to set
	 */
	public void setNeutralsite(Boolean neutralsite) {
		this.neutralsite = neutralsite;
	}
	/**
	 * @return the awaycollegename
	 */
	public String getAwaycollegename() {
		return awaycollegename;
	}
	/**
	 * @param awaycollegename the awaycollegename to set
	 */
	public void setAwaycollegename(String awaycollegename) {
		this.awaycollegename = awaycollegename;
	}
	/**
	 * @return the homecollegename
	 */
	public String getHomecollegename() {
		return homecollegename;
	}
	/**
	 * @param homecollegename the homecollegename to set
	 */
	public void setHomecollegename(String homecollegename) {
		this.homecollegename = homecollegename;
	}
	/**
	 * @return the awaymascotname
	 */
	public String getAwaymascotname() {
		return awaymascotname;
	}
	/**
	 * @param awaymascotname the awaymascotname to set
	 */
	public void setAwaymascotname(String awaymascotname) {
		this.awaymascotname = awaymascotname;
	}
	/**
	 * @return the homemascotname
	 */
	public String getHomemascotname() {
		return homemascotname;
	}
	/**
	 * @param homemascotname the homemascotname to set
	 */
	public void setHomemascotname(String homemascotname) {
		this.homemascotname = homemascotname;
	}
	/**
	 * @return the awayshortname
	 */
	public String getAwayshortname() {
		return awayshortname;
	}
	/**
	 * @param awayshortname the awayshortname to set
	 */
	public void setAwayshortname(String awayshortname) {
		this.awayshortname = awayshortname;
	}
	/**
	 * @return the homeshortname
	 */
	public String getHomeshortname() {
		return homeshortname;
	}
	/**
	 * @param homeshortname the homeshortname to set
	 */
	public void setHomeshortname(String homeshortname) {
		this.homeshortname = homeshortname;
	}
	/**
	 * @return the awayranking
	 */
	public Integer getAwayranking() {
		return awayranking;
	}
	/**
	 * @param awayranking the awayranking to set
	 */
	public void setAwayranking(Integer awayranking) {
		this.awayranking = awayranking;
	}
	/**
	 * @return the homeranking
	 */
	public Integer getHomeranking() {
		return homeranking;
	}
	/**
	 * @param homeranking the homeranking to set
	 */
	public void setHomeranking(Integer homeranking) {
		this.homeranking = homeranking;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EspnCollegeBasketballGameData [isconferencegame=" + isconferencegame + ", neutralsite=" + neutralsite
				+ ", awaycollegename=" + awaycollegename + ", homecollegename=" + homecollegename + ", awaymascotname="
				+ awaymascotname + ", homemascotname=" + homemascotname + ", awayshortname=" + awayshortname
				+ ", homeshortname=" + homeshortname + ", awayranking=" + awayranking + ", homeranking=" + homeranking
				+ "] " + super.toString();
	}
}