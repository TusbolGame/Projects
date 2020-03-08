/**
 * 
 */
package com.wootechnologies.services.dao.sites.vegasinsider;

import java.util.Date;

/**
 * @author jmiller
 *
 */
public class VegasInsiderGameData {
	private Integer gameid = Integer.valueOf("0");
	private Integer week = Integer.valueOf("0");
	private Integer month = Integer.valueOf("0");
	private Integer day = Integer.valueOf("0");
	private Integer year = Integer.valueOf("0");
	private Integer hour = Integer.valueOf("0");
	private Integer minute = Integer.valueOf("0");
	private Integer zipcode = Integer.valueOf("0");
	private Date date = new Date();
	private String ampm = "";
	private String timezone = "";
	private Boolean isconferencegame = new Boolean(false);
	private Boolean neutralsite = new Boolean(false);
	private String linefavorite = "";
	private String lineindicator = "";
	private Float linevalue = Float.valueOf("0");
	private Float line = Float.valueOf("0");
	private Float total = Float.valueOf("0");
	private Float coverspreadamount = Float.valueOf("0");
	private Float covertotalamount = Float.valueOf("0");
	private Boolean win = new Boolean(false);
	private Integer rotationid = Integer.valueOf("0");
	private String teamname = "";
	private String shortname = "";
	private Integer firstquarterscore = Integer.valueOf("0");
	private Integer secondquarterscore = Integer.valueOf("0");
	private Integer firsthalfscore = Integer.valueOf("0");
	private Integer thirdquarterscore = Integer.valueOf("0");
	private Integer fourthquarterscore = Integer.valueOf("0");
	private Integer secondhalfscore = Integer.valueOf("0");
	private Integer finalscore = Integer.valueOf("0");
	private Integer otscore = Integer.valueOf("0");
	private Boolean isfbs = new Boolean(false);

	/**
	 * @return the gameid
	 */
	public Integer getGameid() {
		return gameid;
	}
	/**
	 * @param gameid the gameid to set
	 */
	public void setGameid(Integer gameid) {
		this.gameid = gameid;
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
	 * @return the hour
	 */
	public Integer getHour() {
		return hour;
	}
	/**
	 * @param hour the hour to set
	 */
	public void setHour(Integer hour) {
		this.hour = hour;
	}
	/**
	 * @return the minute
	 */
	public Integer getMinute() {
		return minute;
	}
	/**
	 * @param minute the minute to set
	 */
	public void setMinute(Integer minute) {
		this.minute = minute;
	}
	/**
	 * @return the ampm
	 */
	public String getAmpm() {
		return ampm;
	}
	/**
	 * @param ampm the ampm to set
	 */
	public void setAmpm(String ampm) {
		this.ampm = ampm;
	}
	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}
	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
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
	 * @return the zipcode
	 */
	public Integer getZipcode() {
		return zipcode;
	}
	/**
	 * @param zipcode the zipcode to set
	 */
	public void setZipcode(Integer zipcode) {
		this.zipcode = zipcode;
	}
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
	 * @return the lineindicator
	 */
	public String getLineindicator() {
		return lineindicator;
	}
	/**
	 * @param lineindicator the lineindicator to set
	 */
	public void setLineindicator(String lineindicator) {
		this.lineindicator = lineindicator;
	}
	/**
	 * @return the linevalue
	 */
	public Float getLinevalue() {
		return linevalue;
	}
	/**
	 * @param linevalue the linevalue to set
	 */
	public void setLinevalue(Float linevalue) {
		this.linevalue = linevalue;
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
	 * @return the coverspreadamount
	 */
	public Float getCoverspreadamount() {
		return coverspreadamount;
	}
	/**
	 * @param coverspreadamount the coverspreadamount to set
	 */
	public void setCoverspreadamount(Float coverspreadamount) {
		this.coverspreadamount = coverspreadamount;
	}
	/**
	 * @return the covertotalamount
	 */
	public Float getCovertotalamount() {
		return covertotalamount;
	}
	/**
	 * @param covertotalamount the covertotalamount to set
	 */
	public void setCovertotalamount(Float covertotalamount) {
		this.covertotalamount = covertotalamount;
	}
	/**
	 * @return the win
	 */
	public Boolean getWin() {
		return win;
	}
	/**
	 * @param win the win to set
	 */
	public void setWin(Boolean win) {
		this.win = win;
	}
	/**
	 * @return the rotationid
	 */
	public Integer getRotationid() {
		return rotationid;
	}
	/**
	 * @param rotationid the rotationid to set
	 */
	public void setRotationid(Integer rotationid) {
		this.rotationid = rotationid;
	}
	/**
	 * @return the teamname
	 */
	public String getTeamname() {
		return teamname;
	}
	/**
	 * @param teamname the teamname to set
	 */
	public void setTeamname(String teamname) {
		this.teamname = teamname.toUpperCase();
	}
	/**
	 * @return the shortname
	 */
	public String getShortname() {
		return shortname;
	}
	/**
	 * @param shortname the shortname to set
	 */
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	/**
	 * @return the firstquarterscore
	 */
	public Integer getFirstquarterscore() {
		return firstquarterscore;
	}
	/**
	 * @param firstquarterscore the firstquarterscore to set
	 */
	public void setFirstquarterscore(Integer firstquarterscore) {
		this.firstquarterscore = firstquarterscore;
	}
	/**
	 * @return the secondquarterscore
	 */
	public Integer getSecondquarterscore() {
		return secondquarterscore;
	}
	/**
	 * @param secondquarterscore the secondquarterscore to set
	 */
	public void setSecondquarterscore(Integer secondquarterscore) {
		this.secondquarterscore = secondquarterscore;
	}
	/**
	 * @return the firsthalfscore
	 */
	public Integer getFirsthalfscore() {
		return firsthalfscore;
	}
	/**
	 * @param firsthalfscore the firsthalfscore to set
	 */
	public void setFirsthalfscore(Integer firsthalfscore) {
		this.firsthalfscore = firsthalfscore;
	}
	/**
	 * @return the thirdquarterscore
	 */
	public Integer getThirdquarterscore() {
		return thirdquarterscore;
	}
	/**
	 * @param thirdquarterscore the thirdquarterscore to set
	 */
	public void setThirdquarterscore(Integer thirdquarterscore) {
		this.thirdquarterscore = thirdquarterscore;
	}
	/**
	 * @return the fourthquarterscore
	 */
	public Integer getFourthquarterscore() {
		return fourthquarterscore;
	}
	/**
	 * @param fourthquarterscore the fourthquarterscore to set
	 */
	public void setFourthquarterscore(Integer fourthquarterscore) {
		this.fourthquarterscore = fourthquarterscore;
	}
	/**
	 * @return the secondhalfscore
	 */
	public Integer getSecondhalfscore() {
		return secondhalfscore;
	}
	/**
	 * @param secondhalfscore the secondhalfscore to set
	 */
	public void setSecondhalfscore(Integer secondhalfscore) {
		this.secondhalfscore = secondhalfscore;
	}
	/**
	 * @return the finalscore
	 */
	public Integer getFinalscore() {
		return finalscore;
	}
	/**
	 * @param finalscore the finalscore to set
	 */
	public void setFinalscore(Integer finalscore) {
		this.finalscore = finalscore;
	}
	/**
	 * @return the otscore
	 */
	public Integer getOtscore() {
		return otscore;
	}
	/**
	 * @param otscore the otscore to set
	 */
	public void setOtscore(Integer otscore) {
		this.otscore = otscore;
	}
	/**
	 * @return the isfbs
	 */
	public Boolean getIsfbs() {
		return isfbs;
	}
	/**
	 * @param isfbs the isfbs to set
	 */
	public void setIsfbs(Boolean isfbs) {
		this.isfbs = isfbs;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VegasInsiderGameData [gameid=" + gameid + ", week=" + week + ", hour=" + hour + ", minute=" + minute
				+ ", ampm=" + ampm + ", timezone=" + timezone + ", month=" + month + ", day=" + day + ", year=" + year
				+ ", date=" + date + ", zipcode=" + zipcode + ", isconferencegame=" + isconferencegame
				+ ", neutralsite=" + neutralsite + ", linefavorite=" + linefavorite + ", lineindicator=" + lineindicator
				+ ", linevalue=" + linevalue + ", line=" + line + ", total=" + total + ", coverspreadamount="
				+ coverspreadamount + ", covertotalamount=" + covertotalamount + ", win=" + win + ", rotationid="
				+ rotationid + ", teamname=" + teamname + ", shortname=" + shortname + ", firstquarterscore="
				+ firstquarterscore + ", secondquarterscore=" + secondquarterscore + ", firsthalfscore="
				+ firsthalfscore + ", thirdquarterscore=" + thirdquarterscore + ", fourthquarterscore="
				+ fourthquarterscore + ", secondhalfscore=" + secondhalfscore + ", finalscore=" + finalscore
				+ ", otscore=" + otscore + ", isfbs=" + isfbs + "]";
	}
}