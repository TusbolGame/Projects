/**
 * 
 */
package com.wootechnologies.services.dao.sites.espn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jmiller
 *
 */
public class EspnFootballGame {
	private Integer gameid;
	private Integer hour;
	private Integer minute;
	private String ampm;
	private String timezone;
	private Integer month;
	private Integer day;
	private Integer year;
	private Date date;
	private String tv;
	private String city;
	private String state;
	private Integer zipcode;
	private String linefavorite;
	private Float line;
	private Float total;
	private Integer attendance;
	private EspnFootballTeamData awayteamdata;
	private EspnFootballTeamData hometeamdata;
	private List<EspnFootballDrive> drives = new ArrayList<EspnFootballDrive>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

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
	 * @return the awayteamdata
	 */
	public EspnFootballTeamData getAwayteamdata() {
		return awayteamdata;
	}

	/**
	 * @param awayteamdata the awayteamdata to set
	 */
	public void setAwayteamdata(EspnFootballTeamData awayteamdata) {
		this.awayteamdata = awayteamdata;
	}

	/**
	 * @return the hometeamdata
	 */
	public EspnFootballTeamData getHometeamdata() {
		return hometeamdata;
	}

	/**
	 * @param hometeamdata the hometeamdata to set
	 */
	public void setHometeamdata(EspnFootballTeamData hometeamdata) {
		this.hometeamdata = hometeamdata;
	}

	/**
	 * @return the drives
	 */
	public List<EspnFootballDrive> getDrives() {
		return drives;
	}

	/**
	 * @param drives the drives to set
	 */
	public void setDrives(List<EspnFootballDrive> drives) {
		this.drives = drives;
	}

	/**
	 * 
	 * @param drive
	 */
	public void addDrive(EspnFootballDrive drive) {
		this.drives.add(drive);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EspnFootballGame [gameid=" + gameid + ", hour=" + hour + ", minute=" + minute + ", ampm=" + ampm
				+ ", timezone=" + timezone + ", month=" + month + ", day=" + day + ", year=" + year + ", date=" + date
				+ ", tv=" + tv + ", city=" + city + ", state=" + state + ", zipcode=" + zipcode + ", linefavorite="
				+ linefavorite + ", line=" + line + ", total=" + total + ", attendance=" + attendance
				+ ", awayteamdata=" + awayteamdata + ", hometeamdata=" + hometeamdata + ", drives=" + drives + "]";
	}
}