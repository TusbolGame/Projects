package com.ticketadvantage.services.dao.sites.sportsline;

import java.util.Date;

public class SportslineOdds {
	public Date gamedate;
	public String awayteam;
	public String hometeam;
	public Float spread;
	public Float total;
	public Float moneyline;
	public String location;

	/**
	 * Constructor
	 */
	public SportslineOdds() {
		super();
	}

	/**
	 * @return the gamedate
	 */
	public Date getGamedate() {
		return gamedate;
	}

	/**
	 * @param gamedate the gamedate to set
	 */
	public void setGamedate(Date gamedate) {
		this.gamedate = gamedate;
	}

	/**
	 * @return the awayteam
	 */
	public String getAwayteam() {
		return awayteam;
	}

	/**
	 * @param awayteam the awayteam to set
	 */
	public void setAwayteam(String awayteam) {
		this.awayteam = awayteam;
	}

	/**
	 * @return the hometeam
	 */
	public String getHometeam() {
		return hometeam;
	}

	/**
	 * @param hometeam the hometeam to set
	 */
	public void setHometeam(String hometeam) {
		this.hometeam = hometeam;
	}

	/**
	 * @return the spread
	 */
	public Float getSpread() {
		return spread;
	}

	/**
	 * @param spread the spread to set
	 */
	public void setSpread(Float spread) {
		this.spread = spread;
	}

	/**
	 * @return the total
	 */
	public Float getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(Float total) {
		this.total = total;
	}

	/**
	 * @return the moneyline
	 */
	public Float getMoneyline() {
		return moneyline;
	}

	/**
	 * @param moneyline the moneyline to set
	 */
	public void setMoneyline(Float moneyline) {
		this.moneyline = moneyline;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SportslineOdds [gamedate=" + gamedate + ", awayteam=" + awayteam + ", hometeam=" + hometeam
				+ ", spread=" + spread + ", total=" + total + ", moneyline=" + moneyline + ", location=" + location
				+ "]";
	}
}