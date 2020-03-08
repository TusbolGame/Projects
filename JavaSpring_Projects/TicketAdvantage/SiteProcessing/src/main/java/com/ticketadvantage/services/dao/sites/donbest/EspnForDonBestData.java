/**
 * 
 */
package com.ticketadvantage.services.dao.sites.donbest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author jmiller
 *
 */
public class EspnForDonBestData {
	private LocalDate date;
	private LocalTime time;
	private LocalDateTime dateofgame;
	private Integer week = new Integer(0);
	private Integer year = new Integer(0);
	private String visitorteam = "";
	private String hometeam = "";
	private Integer visitorgamescore = new Integer(0);
	private Integer homegamescore = new Integer(0);
	private Integer visitor1hscore = new Integer(0);
	private Integer home1hscore = new Integer(0);
	private Integer visitor2hscore = new Integer(0);
	private Integer home2hscore = new Integer(0);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * @return the time
	 */
	public LocalTime getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(LocalTime time) {
		this.time = time;
	}

	/**
	 * @return the dateofgame
	 */
	public LocalDateTime getDateofgame() {
		return dateofgame;
	}

	/**
	 * @param dateofgame the dateofgame to set
	 */
	public void setDateofgame(LocalDateTime dateofgame) {
		this.dateofgame = dateofgame;
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
	 * @return the visitorteam
	 */
	public String getVisitorteam() {
		return visitorteam;
	}

	/**
	 * @param visitorteam the visitorteam to set
	 */
	public void setVisitorteam(String visitorteam) {
		this.visitorteam = visitorteam;
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
	 * @return the visitorgamescore
	 */
	public Integer getVisitorgamescore() {
		return visitorgamescore;
	}

	/**
	 * @param visitorgamescore the visitorgamescore to set
	 */
	public void setVisitorgamescore(Integer visitorgamescore) {
		this.visitorgamescore = visitorgamescore;
	}

	/**
	 * @return the homegamescore
	 */
	public Integer getHomegamescore() {
		return homegamescore;
	}

	/**
	 * @param homegamescore the homegamescore to set
	 */
	public void setHomegamescore(Integer homegamescore) {
		this.homegamescore = homegamescore;
	}

	/**
	 * @return the visitor1hscore
	 */
	public Integer getVisitor1hscore() {
		return visitor1hscore;
	}

	/**
	 * @param visitor1hscore the visitor1hscore to set
	 */
	public void setVisitor1hscore(Integer visitor1hscore) {
		this.visitor1hscore = visitor1hscore;
	}

	/**
	 * @return the home1hscore
	 */
	public Integer getHome1hscore() {
		return home1hscore;
	}

	/**
	 * @param home1hscore the home1hscore to set
	 */
	public void setHome1hscore(Integer home1hscore) {
		this.home1hscore = home1hscore;
	}

	/**
	 * @return the visitor2hscore
	 */
	public Integer getVisitor2hscore() {
		return visitor2hscore;
	}

	/**
	 * @param visitor2hscore the visitor2hscore to set
	 */
	public void setVisitor2hscore(Integer visitor2hscore) {
		this.visitor2hscore = visitor2hscore;
	}

	/**
	 * @return the home2hscore
	 */
	public Integer getHome2hscore() {
		return home2hscore;
	}

	/**
	 * @param home2hscore the home2hscore to set
	 */
	public void setHome2hscore(Integer home2hscore) {
		this.home2hscore = home2hscore;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EspnForDonBestData [date=" + date + ", time=" + time + ", dateofgame=" + dateofgame + ", week=" + week
				+ ", year=" + year + ", visitorteam=" + visitorteam + ", hometeam=" + hometeam + ", visitorgamescore="
				+ visitorgamescore + ", homegamescore=" + homegamescore + ", visitor1hscore=" + visitor1hscore
				+ ", home1hscore=" + home1hscore + ", visitor2hscore=" + visitor2hscore + ", home2hscore=" + home2hscore
				+ "]";
	}
}