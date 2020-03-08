/**
 * 
 */
package com.ticketadvantage.services.dao.sites.espn;

/**
 * @author jmiller
 *
 */
public class EspnBasketballGameData extends EspnGameData {
	private Integer awayfgmade = new Integer(0);
	private Integer homefgmade = new Integer(0);
	private Integer awayfgattempt = new Integer(0);
	private Integer homefgattempt = new Integer(0);
	private Float awayfgpercentage = new Float(0);
	private Float homefgpercentage = new Float(0);
	private Integer away3ptfgmade = new Integer(0);
	private Integer home3ptfgmade = new Integer(0);
	private Integer away3ptfgattempt = new Integer(0);
	private Integer home3ptfgattempt = new Integer(0);
	private Float away3ptfgpercentage = new Float(0);
	private Float home3ptfgpercentage = new Float(0);
	private Integer awayftmade = new Integer(0);
	private Integer homeftmade = new Integer(0);
	private Integer awayftattempt = new Integer(0);
	private Integer homeftattempt = new Integer(0);
	private Float awayftpercentage = new Float(0);
	private Float homeftpercentage = new Float(0);
	private Integer awaytotalrebounds = new Integer(0);
	private Integer hometotalrebounds = new Integer(0);
	private Integer awayoffrebounds = new Integer(0);
	private Integer homeoffrebounds = new Integer(0);
	private Integer awaydefrebounds = new Integer(0);
	private Integer homedefrebounds = new Integer(0);
	private Integer awayassists = new Integer(0);
	private Integer homeassists = new Integer(0);
	private Integer awaysteals = new Integer(0);
	private Integer homesteals = new Integer(0);
	private Integer awayblocks = new Integer(0);
	private Integer homeblocks = new Integer(0);
	private Integer awaytotalturnovers = new Integer(0);
	private Integer hometotalturnovers = new Integer(0);
	private Integer awaypersonalfouls = new Integer(0);
	private Integer homepersonalfouls = new Integer(0);
	private Integer awaytechnicalfouls = new Integer(0);
	private Integer hometechnicalfouls = new Integer(0);
	private String ref1 = "";
	private String ref2 = "";
	private String ref3 = "";

	/**
	 * @return the awayfgmade
	 */
	public Integer getAwayfgmade() {
		return awayfgmade;
	}
	/**
	 * @param awayfgmade the awayfgmade to set
	 */
	public void setAwayfgmade(Integer awayfgmade) {
		this.awayfgmade = awayfgmade;
	}
	/**
	 * @return the homefgmade
	 */
	public Integer getHomefgmade() {
		return homefgmade;
	}
	/**
	 * @param homefgmade the homefgmade to set
	 */
	public void setHomefgmade(Integer homefgmade) {
		this.homefgmade = homefgmade;
	}
	/**
	 * @return the awayfgattempt
	 */
	public Integer getAwayfgattempt() {
		return awayfgattempt;
	}
	/**
	 * @param awayfgattempt the awayfgattempt to set
	 */
	public void setAwayfgattempt(Integer awayfgattempt) {
		this.awayfgattempt = awayfgattempt;
	}
	/**
	 * @return the homefgattempt
	 */
	public Integer getHomefgattempt() {
		return homefgattempt;
	}
	/**
	 * @param homefgattempt the homefgattempt to set
	 */
	public void setHomefgattempt(Integer homefgattempt) {
		this.homefgattempt = homefgattempt;
	}
	/**
	 * @return the awayfgpercentage
	 */
	public Float getAwayfgpercentage() {
		return awayfgpercentage;
	}
	/**
	 * @param awayfgpercentage the awayfgpercentage to set
	 */
	public void setAwayfgpercentage(Float awayfgpercentage) {
		this.awayfgpercentage = awayfgpercentage;
	}
	/**
	 * @return the homefgpercentage
	 */
	public Float getHomefgpercentage() {
		return homefgpercentage;
	}
	/**
	 * @param homefgpercentage the homefgpercentage to set
	 */
	public void setHomefgpercentage(Float homefgpercentage) {
		this.homefgpercentage = homefgpercentage;
	}
	/**
	 * @return the away3ptfgmade
	 */
	public Integer getAway3ptfgmade() {
		return away3ptfgmade;
	}
	/**
	 * @param away3ptfgmade the away3ptfgmade to set
	 */
	public void setAway3ptfgmade(Integer away3ptfgmade) {
		this.away3ptfgmade = away3ptfgmade;
	}
	/**
	 * @return the home3ptfgmade
	 */
	public Integer getHome3ptfgmade() {
		return home3ptfgmade;
	}
	/**
	 * @param home3ptfgmade the home3ptfgmade to set
	 */
	public void setHome3ptfgmade(Integer home3ptfgmade) {
		this.home3ptfgmade = home3ptfgmade;
	}
	/**
	 * @return the away3ptfgattempt
	 */
	public Integer getAway3ptfgattempt() {
		return away3ptfgattempt;
	}
	/**
	 * @param away3ptfgattempt the away3ptfgattempt to set
	 */
	public void setAway3ptfgattempt(Integer away3ptfgattempt) {
		this.away3ptfgattempt = away3ptfgattempt;
	}
	/**
	 * @return the home3ptfgattempt
	 */
	public Integer getHome3ptfgattempt() {
		return home3ptfgattempt;
	}
	/**
	 * @param home3ptfgattempt the home3ptfgattempt to set
	 */
	public void setHome3ptfgattempt(Integer home3ptfgattempt) {
		this.home3ptfgattempt = home3ptfgattempt;
	}
	/**
	 * @return the away3ptfgpercentage
	 */
	public Float getAway3ptfgpercentage() {
		return away3ptfgpercentage;
	}
	/**
	 * @param away3ptfgpercentage the away3ptfgpercentage to set
	 */
	public void setAway3ptfgpercentage(Float away3ptfgpercentage) {
		this.away3ptfgpercentage = away3ptfgpercentage;
	}
	/**
	 * @return the home3ptfgpercentage
	 */
	public Float getHome3ptfgpercentage() {
		return home3ptfgpercentage;
	}
	/**
	 * @param home3ptfgpercentage the home3ptfgpercentage to set
	 */
	public void setHome3ptfgpercentage(Float home3ptfgpercentage) {
		this.home3ptfgpercentage = home3ptfgpercentage;
	}
	/**
	 * @return the awayftmade
	 */
	public Integer getAwayftmade() {
		return awayftmade;
	}
	/**
	 * @param awayftmade the awayftmade to set
	 */
	public void setAwayftmade(Integer awayftmade) {
		this.awayftmade = awayftmade;
	}
	/**
	 * @return the homeftmade
	 */
	public Integer getHomeftmade() {
		return homeftmade;
	}
	/**
	 * @param homeftmade the homeftmade to set
	 */
	public void setHomeftmade(Integer homeftmade) {
		this.homeftmade = homeftmade;
	}
	/**
	 * @return the awayftattempt
	 */
	public Integer getAwayftattempt() {
		return awayftattempt;
	}
	/**
	 * @param awayftattempt the awayftattempt to set
	 */
	public void setAwayftattempt(Integer awayftattempt) {
		this.awayftattempt = awayftattempt;
	}
	/**
	 * @return the homeftattempt
	 */
	public Integer getHomeftattempt() {
		return homeftattempt;
	}
	/**
	 * @param homeftattempt the homeftattempt to set
	 */
	public void setHomeftattempt(Integer homeftattempt) {
		this.homeftattempt = homeftattempt;
	}
	/**
	 * @return the awayftpercentage
	 */
	public Float getAwayftpercentage() {
		return awayftpercentage;
	}
	/**
	 * @param awayftpercentage the awayftpercentage to set
	 */
	public void setAwayftpercentage(Float awayftpercentage) {
		this.awayftpercentage = awayftpercentage;
	}
	/**
	 * @return the homeftpercentage
	 */
	public Float getHomeftpercentage() {
		return homeftpercentage;
	}
	/**
	 * @param homeftpercentage the homeftpercentage to set
	 */
	public void setHomeftpercentage(Float homeftpercentage) {
		this.homeftpercentage = homeftpercentage;
	}
	/**
	 * @return the awaytotalrebounds
	 */
	public Integer getAwaytotalrebounds() {
		return awaytotalrebounds;
	}
	/**
	 * @param awaytotalrebounds the awaytotalrebounds to set
	 */
	public void setAwaytotalrebounds(Integer awaytotalrebounds) {
		this.awaytotalrebounds = awaytotalrebounds;
	}
	/**
	 * @return the hometotalrebounds
	 */
	public Integer getHometotalrebounds() {
		return hometotalrebounds;
	}
	/**
	 * @param hometotalrebounds the hometotalrebounds to set
	 */
	public void setHometotalrebounds(Integer hometotalrebounds) {
		this.hometotalrebounds = hometotalrebounds;
	}
	/**
	 * @return the awayoffrebounds
	 */
	public Integer getAwayoffrebounds() {
		return awayoffrebounds;
	}
	/**
	 * @param awayoffrebounds the awayoffrebounds to set
	 */
	public void setAwayoffrebounds(Integer awayoffrebounds) {
		this.awayoffrebounds = awayoffrebounds;
	}
	/**
	 * @return the homeoffrebounds
	 */
	public Integer getHomeoffrebounds() {
		return homeoffrebounds;
	}
	/**
	 * @param homeoffrebounds the homeoffrebounds to set
	 */
	public void setHomeoffrebounds(Integer homeoffrebounds) {
		this.homeoffrebounds = homeoffrebounds;
	}
	/**
	 * @return the awaydefrebounds
	 */
	public Integer getAwaydefrebounds() {
		return awaydefrebounds;
	}
	/**
	 * @param awaydefrebounds the awaydefrebounds to set
	 */
	public void setAwaydefrebounds(Integer awaydefrebounds) {
		this.awaydefrebounds = awaydefrebounds;
	}
	/**
	 * @return the homedefrebounds
	 */
	public Integer getHomedefrebounds() {
		return homedefrebounds;
	}
	/**
	 * @param homedefrebounds the homedefrebounds to set
	 */
	public void setHomedefrebounds(Integer homedefrebounds) {
		this.homedefrebounds = homedefrebounds;
	}
	/**
	 * @return the awayassists
	 */
	public Integer getAwayassists() {
		return awayassists;
	}
	/**
	 * @param awayassists the awayassists to set
	 */
	public void setAwayassists(Integer awayassists) {
		this.awayassists = awayassists;
	}
	/**
	 * @return the homeassists
	 */
	public Integer getHomeassists() {
		return homeassists;
	}
	/**
	 * @param homeassists the homeassists to set
	 */
	public void setHomeassists(Integer homeassists) {
		this.homeassists = homeassists;
	}
	/**
	 * @return the awaysteals
	 */
	public Integer getAwaysteals() {
		return awaysteals;
	}
	/**
	 * @param awaysteals the awaysteals to set
	 */
	public void setAwaysteals(Integer awaysteals) {
		this.awaysteals = awaysteals;
	}
	/**
	 * @return the homesteals
	 */
	public Integer getHomesteals() {
		return homesteals;
	}
	/**
	 * @param homesteals the homesteals to set
	 */
	public void setHomesteals(Integer homesteals) {
		this.homesteals = homesteals;
	}
	/**
	 * @return the awayblocks
	 */
	public Integer getAwayblocks() {
		return awayblocks;
	}
	/**
	 * @param awayblocks the awayblocks to set
	 */
	public void setAwayblocks(Integer awayblocks) {
		this.awayblocks = awayblocks;
	}
	/**
	 * @return the homeblocks
	 */
	public Integer getHomeblocks() {
		return homeblocks;
	}
	/**
	 * @param homeblocks the homeblocks to set
	 */
	public void setHomeblocks(Integer homeblocks) {
		this.homeblocks = homeblocks;
	}
	/**
	 * @return the awaytotalturnovers
	 */
	public Integer getAwaytotalturnovers() {
		return awaytotalturnovers;
	}
	/**
	 * @param awaytotalturnovers the awaytotalturnovers to set
	 */
	public void setAwaytotalturnovers(Integer awaytotalturnovers) {
		this.awaytotalturnovers = awaytotalturnovers;
	}
	/**
	 * @return the hometotalturnovers
	 */
	public Integer getHometotalturnovers() {
		return hometotalturnovers;
	}
	/**
	 * @param hometotalturnovers the hometotalturnovers to set
	 */
	public void setHometotalturnovers(Integer hometotalturnovers) {
		this.hometotalturnovers = hometotalturnovers;
	}
	/**
	 * @return the awaypersonalfouls
	 */
	public Integer getAwaypersonalfouls() {
		return awaypersonalfouls;
	}
	/**
	 * @param awaypersonalfouls the awaypersonalfouls to set
	 */
	public void setAwaypersonalfouls(Integer awaypersonalfouls) {
		this.awaypersonalfouls = awaypersonalfouls;
	}
	/**
	 * @return the homepersonalfouls
	 */
	public Integer getHomepersonalfouls() {
		return homepersonalfouls;
	}
	/**
	 * @param homepersonalfouls the homepersonalfouls to set
	 */
	public void setHomepersonalfouls(Integer homepersonalfouls) {
		this.homepersonalfouls = homepersonalfouls;
	}
	/**
	 * @return the awaytechnicalfouls
	 */
	public Integer getAwaytechnicalfouls() {
		return awaytechnicalfouls;
	}
	/**
	 * @param awaytechnicalfouls the awaytechnicalfouls to set
	 */
	public void setAwaytechnicalfouls(Integer awaytechnicalfouls) {
		this.awaytechnicalfouls = awaytechnicalfouls;
	}
	/**
	 * @return the hometechnicalfouls
	 */
	public Integer getHometechnicalfouls() {
		return hometechnicalfouls;
	}
	/**
	 * @param hometechnicalfouls the hometechnicalfouls to set
	 */
	public void setHometechnicalfouls(Integer hometechnicalfouls) {
		this.hometechnicalfouls = hometechnicalfouls;
	}
	/**
	 * @return the ref1
	 */
	public String getRef1() {
		return ref1;
	}
	/**
	 * @param ref1 the ref1 to set
	 */
	public void setRef1(String ref1) {
		this.ref1 = ref1;
	}
	/**
	 * @return the ref2
	 */
	public String getRef2() {
		return ref2;
	}
	/**
	 * @param ref2 the ref2 to set
	 */
	public void setRef2(String ref2) {
		this.ref2 = ref2;
	}
	/**
	 * @return the ref3
	 */
	public String getRef3() {
		return ref3;
	}
	/**
	 * @param ref3 the ref3 to set
	 */
	public void setRef3(String ref3) {
		this.ref3 = ref3;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EspnBasketballGameData [awayfgmade=" + awayfgmade + ", homefgmade=" + homefgmade + ", awayfgattempt="
				+ awayfgattempt + ", homefgattempt=" + homefgattempt + ", awayfgpercentage=" + awayfgpercentage
				+ ", homefgpercentage=" + homefgpercentage + ", away3ptfgmade=" + away3ptfgmade + ", home3ptfgmade="
				+ home3ptfgmade + ", away3ptfgattempt=" + away3ptfgattempt + ", home3ptfgattempt=" + home3ptfgattempt
				+ ", away3ptfgpercentage=" + away3ptfgpercentage + ", home3ptfgpercentage=" + home3ptfgpercentage
				+ ", awayftmade=" + awayftmade + ", homeftmade=" + homeftmade + ", awayftattempt=" + awayftattempt
				+ ", homeftattempt=" + homeftattempt + ", awayftpercentage=" + awayftpercentage + ", homeftpercentage="
				+ homeftpercentage + ", awaytotalrebounds=" + awaytotalrebounds + ", hometotalrebounds="
				+ hometotalrebounds + ", awayoffrebounds=" + awayoffrebounds + ", homeoffrebounds=" + homeoffrebounds
				+ ", awaydefrebounds=" + awaydefrebounds + ", homedefrebounds=" + homedefrebounds + ", awayassists="
				+ awayassists + ", homeassists=" + homeassists + ", awaysteals=" + awaysteals + ", homesteals="
				+ homesteals + ", awayblocks=" + awayblocks + ", homeblocks=" + homeblocks + ", awaytotalturnovers="
				+ awaytotalturnovers + ", hometotalturnovers=" + hometotalturnovers + ", awaypersonalfouls="
				+ awaypersonalfouls + ", homepersonalfouls=" + homepersonalfouls + ", awaytechnicalfouls="
				+ awaytechnicalfouls + ", hometechnicalfouls=" + hometechnicalfouls + ", ref1=" + ref1 + ", ref2="
				+ ref2 + ", ref3=" + ref3 + "] " + super.toString();
	}
}