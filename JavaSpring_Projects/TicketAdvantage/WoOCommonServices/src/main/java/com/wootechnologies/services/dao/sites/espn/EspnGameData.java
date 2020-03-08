/**
 * 
 */
package com.wootechnologies.services.dao.sites.espn;

import java.util.Date;

/**
 * @author jmiller
 *
 */
public class EspnGameData {
	private Integer espngameid;
	private Integer week = new Integer(0);
	private Integer seasonyear = new Integer(0);
	private Integer month = new Integer(0);
	private Integer day = new Integer(0);
	private Integer year = new Integer(0);
	private Integer hour = new Integer(0);
	private Integer minute = new Integer(0);
	private Date gamedate = new Date();
	private String ampm = "";
	private String timezone = "";
	private String city = "";
	private String state = "";
	private Integer zipcode = new Integer(0);
	private String linefavorite = "";
	private String lineindicator = "";
	private Float linevalue = new Float(0);
	private Float line = new Float(0);
	private Float total = new Float(0);
	private Integer attendance = new Integer(0);
	private String tv = "";
	private String eventlocation = "";
	private String awayteam = "";
	private String hometeam = "";
	private String awayshortteam = "";
	private String homeshortteam = "";
	private String awayabbrevteam = "";
	private String homeabbrevteam = "";
	private String awayteamlogo = "";
	private String hometeamlogo = "";
	private Boolean awaywin = new Boolean(false);
	private Boolean homewin = new Boolean(false);
	private Integer awaywins = new Integer(0);
	private Integer homewins = new Integer(0);
	private Integer awaylosses = new Integer(0);
	private Integer homelosses = new Integer(0);
	private Integer awayfirstquarterscore = new Integer(0);
	private Integer homefirstquarterscore = new Integer(0);
	private Integer awaysecondquarterscore = new Integer(0);
	private Integer homesecondquarterscore = new Integer(0);
	private Integer awayfirsthalfscore = new Integer(0);
	private Integer homefirsthalfscore = new Integer(0);
	private Integer awaythirdquarterscore = new Integer(0);
	private Integer homethirdquarterscore = new Integer(0);
	private Integer awayfourthquarterscore = new Integer(0);
	private Integer homefourthquarterscore = new Integer(0);
	private Integer awaysecondhalfscore = new Integer(0);
	private Integer homesecondhalfscore = new Integer(0);
	private Integer awayotonescore = new Integer(0);
	private Integer homeotonescore = new Integer(0);
	private Integer awayottwoscore = new Integer(0);
	private Integer homeottwoscore = new Integer(0);
	private Integer awayotthreescore = new Integer(0);
	private Integer homeotthreescore = new Integer(0);
	private Integer awayotfourscore = new Integer(0);
	private Integer homeotfourscore = new Integer(0);
	private Integer awayotfivescore = new Integer(0);
	private Integer homeotfivescore = new Integer(0);
	private Integer awayotsixscore = new Integer(0);
	private Integer homeotsixscore = new Integer(0);
	private Integer awaysecondhalfotscore = new Integer(0);
	private Integer homesecondhalfotscore = new Integer(0);
	private Integer awayfinalscore = new Integer(0);
	private Integer homefinalscore = new Integer(0);
	private Float awaysagrinrating = new Float(0);
	private Float homesagrinrating = new Float(0);
	private Float awaymasseyrating = new Float(0);
	private Float homemasseyrating = new Float(0);
	private Integer awaymasseyranking = new Integer(0);
	private Integer homemasseyranking = new Integer(0);
	private Float awaysos = new Float(0);
	private Float homesos = new Float(0);

	/**
	 * @return the espngameid
	 */
	public Integer getEspngameid() {
		return espngameid;
	}
	/**
	 * @param espngameid the espngameid to set
	 */
	public void setEspngameid(Integer espngameid) {
		this.espngameid = espngameid;
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
	 * @return the seasonyear
	 */
	public Integer getSeasonyear() {
		return seasonyear;
	}
	/**
	 * @param seasonyear the seasonyear to set
	 */
	public void setSeasonyear(Integer seasonyear) {
		this.seasonyear = seasonyear;
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
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
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
	 * @return the attendance
	 */
	public Integer getAttendance() {
		return attendance;
	}
	/**
	 * @param attendance the attendance to set
	 */
	public void setAttendance(Integer attendance) {
		this.attendance = attendance;
	}
	/**
	 * @return the tv
	 */
	public String getTv() {
		return tv;
	}
	/**
	 * @param tv the tv to set
	 */
	public void setTv(String tv) {
		this.tv = tv;
	}
	/**
	 * @return the eventlocation
	 */
	public String getEventlocation() {
		return eventlocation;
	}
	/**
	 * @param eventlocation the eventlocation to set
	 */
	public void setEventlocation(String eventlocation) {
		this.eventlocation = eventlocation;
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
	 * @return the awaywin
	 */
	public Boolean getAwaywin() {
		return awaywin;
	}
	/**
	 * @param awaywin the awaywin to set
	 */
	public void setAwaywin(Boolean awaywin) {
		this.awaywin = awaywin;
	}
	/**
	 * @return the homewin
	 */
	public Boolean getHomewin() {
		return homewin;
	}
	/**
	 * @param homewin the homewin to set
	 */
	public void setHomewin(Boolean homewin) {
		this.homewin = homewin;
	}
	/**
	 * @return the awaywins
	 */
	public Integer getAwaywins() {
		return awaywins;
	}
	/**
	 * @param awaywins the awaywins to set
	 */
	public void setAwaywins(Integer awaywins) {
		this.awaywins = awaywins;
	}
	/**
	 * @return the homewins
	 */
	public Integer getHomewins() {
		return homewins;
	}
	/**
	 * @param homewins the homewins to set
	 */
	public void setHomewins(Integer homewins) {
		this.homewins = homewins;
	}
	/**
	 * @return the awaylosses
	 */
	public Integer getAwaylosses() {
		return awaylosses;
	}
	/**
	 * @param awaylosses the awaylosses to set
	 */
	public void setAwaylosses(Integer awaylosses) {
		this.awaylosses = awaylosses;
	}
	/**
	 * @return the homelosses
	 */
	public Integer getHomelosses() {
		return homelosses;
	}
	/**
	 * @param homelosses the homelosses to set
	 */
	public void setHomelosses(Integer homelosses) {
		this.homelosses = homelosses;
	}
	/**
	 * @return the awayfirstquarterscore
	 */
	public Integer getAwayfirstquarterscore() {
		return awayfirstquarterscore;
	}
	/**
	 * @param awayfirstquarterscore the awayfirstquarterscore to set
	 */
	public void setAwayfirstquarterscore(Integer awayfirstquarterscore) {
		this.awayfirstquarterscore = awayfirstquarterscore;
	}
	/**
	 * @return the homefirstquarterscore
	 */
	public Integer getHomefirstquarterscore() {
		return homefirstquarterscore;
	}
	/**
	 * @param homefirstquarterscore the homefirstquarterscore to set
	 */
	public void setHomefirstquarterscore(Integer homefirstquarterscore) {
		this.homefirstquarterscore = homefirstquarterscore;
	}
	/**
	 * @return the awaysecondquarterscore
	 */
	public Integer getAwaysecondquarterscore() {
		return awaysecondquarterscore;
	}
	/**
	 * @param awaysecondquarterscore the awaysecondquarterscore to set
	 */
	public void setAwaysecondquarterscore(Integer awaysecondquarterscore) {
		this.awaysecondquarterscore = awaysecondquarterscore;
	}
	/**
	 * @return the homesecondquarterscore
	 */
	public Integer getHomesecondquarterscore() {
		return homesecondquarterscore;
	}
	/**
	 * @param homesecondquarterscore the homesecondquarterscore to set
	 */
	public void setHomesecondquarterscore(Integer homesecondquarterscore) {
		this.homesecondquarterscore = homesecondquarterscore;
	}
	/**
	 * @return the awayfirsthalfscore
	 */
	public Integer getAwayfirsthalfscore() {
		return awayfirsthalfscore;
	}
	/**
	 * @param awayfirsthalfscore the awayfirsthalfscore to set
	 */
	public void setAwayfirsthalfscore(Integer awayfirsthalfscore) {
		this.awayfirsthalfscore = awayfirsthalfscore;
	}
	/**
	 * @return the homefirsthalfscore
	 */
	public Integer getHomefirsthalfscore() {
		return homefirsthalfscore;
	}
	/**
	 * @param homefirsthalfscore the homefirsthalfscore to set
	 */
	public void setHomefirsthalfscore(Integer homefirsthalfscore) {
		this.homefirsthalfscore = homefirsthalfscore;
	}
	/**
	 * @return the awaythirdquarterscore
	 */
	public Integer getAwaythirdquarterscore() {
		return awaythirdquarterscore;
	}
	/**
	 * @param awaythirdquarterscore the awaythirdquarterscore to set
	 */
	public void setAwaythirdquarterscore(Integer awaythirdquarterscore) {
		this.awaythirdquarterscore = awaythirdquarterscore;
	}
	/**
	 * @return the homethirdquarterscore
	 */
	public Integer getHomethirdquarterscore() {
		return homethirdquarterscore;
	}
	/**
	 * @param homethirdquarterscore the homethirdquarterscore to set
	 */
	public void setHomethirdquarterscore(Integer homethirdquarterscore) {
		this.homethirdquarterscore = homethirdquarterscore;
	}
	/**
	 * @return the awayfourthquarterscore
	 */
	public Integer getAwayfourthquarterscore() {
		return awayfourthquarterscore;
	}
	/**
	 * @param awayfourthquarterscore the awayfourthquarterscore to set
	 */
	public void setAwayfourthquarterscore(Integer awayfourthquarterscore) {
		this.awayfourthquarterscore = awayfourthquarterscore;
	}
	/**
	 * @return the homefourthquarterscore
	 */
	public Integer getHomefourthquarterscore() {
		return homefourthquarterscore;
	}
	/**
	 * @param homefourthquarterscore the homefourthquarterscore to set
	 */
	public void setHomefourthquarterscore(Integer homefourthquarterscore) {
		this.homefourthquarterscore = homefourthquarterscore;
	}
	/**
	 * @return the awaysecondhalfscore
	 */
	public Integer getAwaysecondhalfscore() {
		return awaysecondhalfscore;
	}
	/**
	 * @param awaysecondhalfscore the awaysecondhalfscore to set
	 */
	public void setAwaysecondhalfscore(Integer awaysecondhalfscore) {
		this.awaysecondhalfscore = awaysecondhalfscore;
	}
	/**
	 * @return the homesecondhalfscore
	 */
	public Integer getHomesecondhalfscore() {
		return homesecondhalfscore;
	}
	/**
	 * @param homesecondhalfscore the homesecondhalfscore to set
	 */
	public void setHomesecondhalfscore(Integer homesecondhalfscore) {
		this.homesecondhalfscore = homesecondhalfscore;
	}
	/**
	 * @return the awayotonescore
	 */
	public Integer getAwayotonescore() {
		return awayotonescore;
	}
	/**
	 * @param awayotonescore the awayotonescore to set
	 */
	public void setAwayotonescore(Integer awayotonescore) {
		this.awayotonescore = awayotonescore;
	}
	/**
	 * @return the homeotonescore
	 */
	public Integer getHomeotonescore() {
		return homeotonescore;
	}
	/**
	 * @param homeotonescore the homeotonescore to set
	 */
	public void setHomeotonescore(Integer homeotonescore) {
		this.homeotonescore = homeotonescore;
	}
	/**
	 * @return the awayottwoscore
	 */
	public Integer getAwayottwoscore() {
		return awayottwoscore;
	}
	/**
	 * @param awayottwoscore the awayottwoscore to set
	 */
	public void setAwayottwoscore(Integer awayottwoscore) {
		this.awayottwoscore = awayottwoscore;
	}
	/**
	 * @return the homeottwoscore
	 */
	public Integer getHomeottwoscore() {
		return homeottwoscore;
	}
	/**
	 * @param homeottwoscore the homeottwoscore to set
	 */
	public void setHomeottwoscore(Integer homeottwoscore) {
		this.homeottwoscore = homeottwoscore;
	}
	/**
	 * @return the awayotthreescore
	 */
	public Integer getAwayotthreescore() {
		return awayotthreescore;
	}
	/**
	 * @param awayotthreescore the awayotthreescore to set
	 */
	public void setAwayotthreescore(Integer awayotthreescore) {
		this.awayotthreescore = awayotthreescore;
	}
	/**
	 * @return the homeotthreescore
	 */
	public Integer getHomeotthreescore() {
		return homeotthreescore;
	}
	/**
	 * @param homeotthreescore the homeotthreescore to set
	 */
	public void setHomeotthreescore(Integer homeotthreescore) {
		this.homeotthreescore = homeotthreescore;
	}
	/**
	 * @return the awayotfourscore
	 */
	public Integer getAwayotfourscore() {
		return awayotfourscore;
	}
	/**
	 * @param awayotfourscore the awayotfourscore to set
	 */
	public void setAwayotfourscore(Integer awayotfourscore) {
		this.awayotfourscore = awayotfourscore;
	}
	/**
	 * @return the homeotfourscore
	 */
	public Integer getHomeotfourscore() {
		return homeotfourscore;
	}
	/**
	 * @param homeotfourscore the homeotfourscore to set
	 */
	public void setHomeotfourscore(Integer homeotfourscore) {
		this.homeotfourscore = homeotfourscore;
	}
	/**
	 * @return the awayotfivescore
	 */
	public Integer getAwayotfivescore() {
		return awayotfivescore;
	}
	/**
	 * @param awayotfivescore the awayotfivescore to set
	 */
	public void setAwayotfivescore(Integer awayotfivescore) {
		this.awayotfivescore = awayotfivescore;
	}
	/**
	 * @return the homeotfivescore
	 */
	public Integer getHomeotfivescore() {
		return homeotfivescore;
	}
	/**
	 * @param homeotfivescore the homeotfivescore to set
	 */
	public void setHomeotfivescore(Integer homeotfivescore) {
		this.homeotfivescore = homeotfivescore;
	}
	/**
	 * @return the awayotsixscore
	 */
	public Integer getAwayotsixscore() {
		return awayotsixscore;
	}
	/**
	 * @param awayotsixscore the awayotsixscore to set
	 */
	public void setAwayotsixscore(Integer awayotsixscore) {
		this.awayotsixscore = awayotsixscore;
	}
	/**
	 * @return the homeotsixscore
	 */
	public Integer getHomeotsixscore() {
		return homeotsixscore;
	}
	/**
	 * @param homeotsixscore the homeotsixscore to set
	 */
	public void setHomeotsixscore(Integer homeotsixscore) {
		this.homeotsixscore = homeotsixscore;
	}
	/**
	 * @return the awaysecondhalfotscore
	 */
	public Integer getAwaysecondhalfotscore() {
		return awaysecondhalfotscore;
	}
	/**
	 * @param awaysecondhalfotscore the awaysecondhalfotscore to set
	 */
	public void setAwaysecondhalfotscore(Integer awaysecondhalfotscore) {
		this.awaysecondhalfotscore = awaysecondhalfotscore;
	}
	/**
	 * @return the homesecondhalfotscore
	 */
	public Integer getHomesecondhalfotscore() {
		return homesecondhalfotscore;
	}
	/**
	 * @param homesecondhalfotscore the homesecondhalfotscore to set
	 */
	public void setHomesecondhalfotscore(Integer homesecondhalfotscore) {
		this.homesecondhalfotscore = homesecondhalfotscore;
	}
	/**
	 * @return the awayfinalscore
	 */
	public Integer getAwayfinalscore() {
		return awayfinalscore;
	}
	/**
	 * @param awayfinalscore the awayfinalscore to set
	 */
	public void setAwayfinalscore(Integer awayfinalscore) {
		this.awayfinalscore = awayfinalscore;
	}
	/**
	 * @return the homefinalscore
	 */
	public Integer getHomefinalscore() {
		return homefinalscore;
	}
	/**
	 * @param homefinalscore the homefinalscore to set
	 */
	public void setHomefinalscore(Integer homefinalscore) {
		this.homefinalscore = homefinalscore;
	}
	/**
	 * @return the awaysagrinrating
	 */
	public Float getAwaysagrinrating() {
		return awaysagrinrating;
	}
	/**
	 * @param awaysagrinrating the awaysagrinrating to set
	 */
	public void setAwaysagrinrating(Float awaysagrinrating) {
		this.awaysagrinrating = awaysagrinrating;
	}
	/**
	 * @return the homesagrinrating
	 */
	public Float getHomesagrinrating() {
		return homesagrinrating;
	}
	/**
	 * @param homesagrinrating the homesagrinrating to set
	 */
	public void setHomesagrinrating(Float homesagrinrating) {
		this.homesagrinrating = homesagrinrating;
	}
	/**
	 * @return the awaymasseyrating
	 */
	public Float getAwaymasseyrating() {
		return awaymasseyrating;
	}
	/**
	 * @param awaymasseyrating the awaymasseyrating to set
	 */
	public void setAwaymasseyrating(Float awaymasseyrating) {
		this.awaymasseyrating = awaymasseyrating;
	}
	/**
	 * @return the homemasseyrating
	 */
	public Float getHomemasseyrating() {
		return homemasseyrating;
	}
	/**
	 * @param homemasseyrating the homemasseyrating to set
	 */
	public void setHomemasseyrating(Float homemasseyrating) {
		this.homemasseyrating = homemasseyrating;
	}
	/**
	 * @return the awaymasseyranking
	 */
	public Integer getAwaymasseyranking() {
		return awaymasseyranking;
	}
	/**
	 * @param awaymasseyranking the awaymasseyranking to set
	 */
	public void setAwaymasseyranking(Integer awaymasseyranking) {
		this.awaymasseyranking = awaymasseyranking;
	}
	/**
	 * @return the homemasseyranking
	 */
	public Integer getHomemasseyranking() {
		return homemasseyranking;
	}
	/**
	 * @param homemasseyranking the homemasseyranking to set
	 */
	public void setHomemasseyranking(Integer homemasseyranking) {
		this.homemasseyranking = homemasseyranking;
	}
	/**
	 * @return the awaysos
	 */
	public Float getAwaysos() {
		return awaysos;
	}
	/**
	 * @param awaysos the awaysos to set
	 */
	public void setAwaysos(Float awaysos) {
		this.awaysos = awaysos;
	}
	/**
	 * @return the homesos
	 */
	public Float getHomesos() {
		return homesos;
	}
	/**
	 * @param homesos the homesos to set
	 */
	public void setHomesos(Float homesos) {
		this.homesos = homesos;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EspnGameData [espngameid=" + espngameid + ", week=" + week + ", seasonyear=" + seasonyear + ", month="
				+ month + ", day=" + day + ", year=" + year + ", hour=" + hour + ", minute=" + minute + ", gamedate="
				+ gamedate + ", ampm=" + ampm + ", timezone=" + timezone + ", city=" + city + ", state=" + state
				+ ", zipcode=" + zipcode + ", linefavorite=" + linefavorite + ", lineindicator=" + lineindicator
				+ ", linevalue=" + linevalue + ", line=" + line + ", total=" + total + ", attendance=" + attendance
				+ ", tv=" + tv + ", eventlocation=" + eventlocation + ", awayteam=" + awayteam + ", hometeam="
				+ hometeam + ", awaywin=" + awaywin + ", homewin=" + homewin + ", awaywins=" + awaywins + ", homewins="
				+ homewins + ", awaylosses=" + awaylosses + ", homelosses=" + homelosses + ", awayfirstquarterscore="
				+ awayfirstquarterscore + ", homefirstquarterscore=" + homefirstquarterscore
				+ ", awaysecondquarterscore=" + awaysecondquarterscore + ", homesecondquarterscore="
				+ homesecondquarterscore + ", awayfirsthalfscore=" + awayfirsthalfscore + ", homefirsthalfscore="
				+ homefirsthalfscore + ", awaythirdquarterscore=" + awaythirdquarterscore + ", homethirdquarterscore="
				+ homethirdquarterscore + ", awayfourthquarterscore=" + awayfourthquarterscore
				+ ", homefourthquarterscore=" + homefourthquarterscore + ", awaysecondhalfscore=" + awaysecondhalfscore
				+ ", homesecondhalfscore=" + homesecondhalfscore + ", awayotonescore=" + awayotonescore
				+ ", homeotonescore=" + homeotonescore + ", awayottwoscore=" + awayottwoscore + ", homeottwoscore="
				+ homeottwoscore + ", awayotthreescore=" + awayotthreescore + ", homeotthreescore=" + homeotthreescore
				+ ", awayotfourscore=" + awayotfourscore + ", homeotfourscore=" + homeotfourscore + ", awayotfivescore="
				+ awayotfivescore + ", homeotfivescore=" + homeotfivescore + ", awayotsixscore=" + awayotsixscore
				+ ", homeotsixscore=" + homeotsixscore + ", awaysecondhalfotscore=" + awaysecondhalfotscore
				+ ", homesecondhalfotscore=" + homesecondhalfotscore + ", awayfinalscore=" + awayfinalscore
				+ ", homefinalscore=" + homefinalscore + ", awaysagrinrating=" + awaysagrinrating
				+ ", homesagrinrating=" + homesagrinrating + ", awaymasseyrating=" + awaymasseyrating
				+ ", homemasseyrating=" + homemasseyrating + ", awaymasseyranking=" + awaymasseyranking
				+ ", homemasseyranking=" + homemasseyranking + ", awaysos=" + awaysos + ", homesos=" + homesos + "]";
	}
}