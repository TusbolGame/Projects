/**
 * 
 */
package com.wootechnologies.services.dao.sites.vegasinsider;

import java.io.Serializable;
import java.util.Date;


/**
 * @author jmiller
 *
 */
public class VegasInsiderGame implements Serializable, Comparable<VegasInsiderGame> {
	private static final long serialVersionUID = 1L;
	private String gameid;
	private Date date;
	private String linefavorite;
	private Float line;
	private Float total;
	private Integer week = Integer.valueOf("0");
	private Integer month = Integer.valueOf("0");
	private Integer day = Integer.valueOf("0");
	private Integer year = Integer.valueOf("0");
	private VegasInsiderGameData awayteamdata;
	private VegasInsiderGameData hometeamdata;
	private String lineurl;
	private VegasInsiderLineMovement vilm;

	/**
	 * @return the gameid
	 */
	public String getGameid() {
		return gameid;
	}
	/**
	 * @param gameid the gameid to set
	 */
	public void setGameid(String gameid) {
		this.gameid = gameid;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the linefavorite
	 */
	public String getLinefavorite() {
		return linefavorite;
	}
	/**
	 * @param linefavorite the linefavorite to set
	 */
	public void setLinefavorite(String linefavorite) {
		this.linefavorite = linefavorite;
	}
	/**
	 * @return the line
	 */
	public Float getLine() {
		return line;
	}
	/**
	 * @param line the line to set
	 */
	public void setLine(Float line) {
		this.line = line;
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
	 * @return the week
	 */
	public Integer getWeek() {
		return week;
	}
	/**
	 * @param week the week to set
	 */
	public void setWeek(Integer week) {
		this.week = week;
	}
	/**
	 * @return the month
	 */
	public Integer getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(Integer month) {
		this.month = month;
	}
	/**
	 * @return the day
	 */
	public Integer getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(Integer day) {
		this.day = day;
	}
	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}
	/**
	 * @return the awayteamdata
	 */
	public VegasInsiderGameData getAwayteamdata() {
		return awayteamdata;
	}
	/**
	 * @param awayteamdata the awayteamdata to set
	 */
	public void setAwayteamdata(VegasInsiderGameData awayteamdata) {
		this.awayteamdata = awayteamdata;
	}
	/**
	 * @return the hometeamdata
	 */
	public VegasInsiderGameData getHometeamdata() {
		return hometeamdata;
	}
	/**
	 * @param hometeamdata the hometeamdata to set
	 */
	public void setHometeamdata(VegasInsiderGameData hometeamdata) {
		this.hometeamdata = hometeamdata;
	}

	/**
	 * @return the lineurl
	 */
	public String getLineurl() {
		return lineurl;
	}

	/**
	 * @param lineurl the lineurl to set
	 */
	public void setLineurl(String lineurl) {
		this.lineurl = lineurl;
	}

	/**
	 * @return the vilm
	 */
	public VegasInsiderLineMovement getVilm() {
		return vilm;
	}

	/**
	 * @param vilm the vilm to set
	 */
	public void setVilm(VegasInsiderLineMovement vilm) {
		this.vilm = vilm;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(VegasInsiderGame o) {
	    if (getDate() == null || o.getDate() == null)
	        return 0;
	    return getDate().compareTo(o.getDate());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VegasInsiderGame [gameid=" + gameid + ", date=" + date + ", linefavorite=" + linefavorite + ", line="
				+ line + ", total=" + total + ", week=" + week + ", month=" + month + ", day=" + day + ", year=" + year
				+ ", awayteamdata=" + awayteamdata + ", hometeamdata=" + hometeamdata + ", lineurl=" + lineurl
				+ ", vilm=" + vilm + "]";
	}
}