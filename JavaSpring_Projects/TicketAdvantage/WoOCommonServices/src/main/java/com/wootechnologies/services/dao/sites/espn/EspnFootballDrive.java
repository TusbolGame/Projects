/**
 * 
 */
package com.wootechnologies.services.dao.sites.espn;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jmiller
 *
 */
public class EspnFootballDrive {
	public static Integer KICK_OFF = 0;
	public static Integer TOUCHDOWN = 1;
	public static Integer FIELD_GOAL = 2;
	public static Integer INTERCEPTION_RETURN_TOUCHDOWN = 3;
	public static Integer FUMBLE_RETURN_TOUCHDOWN = 4;
	public static Integer PUNT = 5;
	public static Integer DOWNS = 6;
	public static Integer FUMBLE = 7;
	public static Integer INTERCEPTION = 8;
	public static Integer MISSED_FIELD_GOAL = 9;
	public static Integer END_OF_HALF = 10;
	public static Integer END_OF_GAME = 11;

	private Integer gameid = new Integer(0);
	private String gamedate = "";
	private String awayteam = "";
	private String hometeam = "";
	private Integer homescore = new Integer(0);
	private Integer awayscore = new Integer(0);
	private Integer quarter = new Integer(0);
	private Integer actiontype;
	private Boolean didscore = false;
	private Integer plays = new Integer(0);
	private Integer yards = new Integer(0);
	private Integer minutes = new Integer(0);
	private Integer seconds = new Integer(0);
	private List<EspnFootballPlayByPlay> playByPlays = new ArrayList<EspnFootballPlayByPlay>();

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
	 * @return the gamedate
	 */
	public String getGamedate() {
		return gamedate;
	}

	/**
	 * @param gamedate the gamedate to set
	 */
	public void setGamedate(String gamedate) {
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
	 * @return the homescore
	 */
	public Integer getHomescore() {
		return homescore;
	}

	/**
	 * @param homescore the homescore to set
	 */
	public void setHomescore(Integer homescore) {
		this.homescore = homescore;
	}

	/**
	 * @return the awayscore
	 */
	public Integer getAwayscore() {
		return awayscore;
	}

	/**
	 * @param awayscore the awayscore to set
	 */
	public void setAwayscore(Integer awayscore) {
		this.awayscore = awayscore;
	}

	/**
	 * @return the quarter
	 */
	public Integer getQuarter() {
		return quarter;
	}

	/**
	 * @param quarter the quarter to set
	 */
	public void setQuarter(Integer quarter) {
		this.quarter = quarter;
	}

	/**
	 * @return the actiontype
	 */
	public Integer getActiontype() {
		return actiontype;
	}

	/**
	 * @param actiontype the actiontype to set
	 */
	public void setActiontype(Integer actiontype) {
		this.actiontype = actiontype;
	}

	/**
	 * @return the didscore
	 */
	public Boolean getDidscore() {
		return didscore;
	}

	/**
	 * @param didscore the didscore to set
	 */
	public void setDidscore(Boolean didscore) {
		this.didscore = didscore;
	}

	/**
	 * @return the plays
	 */
	public Integer getPlays() {
		return plays;
	}

	/**
	 * @param plays the plays to set
	 */
	public void setPlays(Integer plays) {
		this.plays = plays;
	}

	/**
	 * @return the yards
	 */
	public Integer getYards() {
		return yards;
	}

	/**
	 * @param yards the yards to set
	 */
	public void setYards(Integer yards) {
		this.yards = yards;
	}

	/**
	 * @return the minutes
	 */
	public Integer getMinutes() {
		return minutes;
	}

	/**
	 * @param minutes the minutes to set
	 */
	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	/**
	 * @return the seconds
	 */
	public Integer getSeconds() {
		return seconds;
	}

	/**
	 * @param seconds the seconds to set
	 */
	public void setSeconds(Integer seconds) {
		this.seconds = seconds;
	}

	/**
	 * @return the playByPlays
	 */
	public List<EspnFootballPlayByPlay> getPlayByPlays() {
		return playByPlays;
	}

	/**
	 * @param playByPlays the playByPlays to set
	 */
	public void setPlayByPlays(List<EspnFootballPlayByPlay> playByPlays) {
		this.playByPlays = playByPlays;
	}

	/**
	 * 
	 * @param playByPlay
	 */
	public void addPlayByPlays(EspnFootballPlayByPlay playByPlay) {
		playByPlays.add(playByPlay);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EspnFootballDrive [gameid=" + gameid + ", gamedate=" + gamedate + ", awayteam=" + awayteam
				+ ", hometeam=" + hometeam + ", homescore=" + homescore + ", awayscore=" + awayscore + ", quarter="
				+ quarter + ", actiontype=" + actiontype + ", didscore=" + didscore + ", plays=" + plays + ", yards="
				+ yards + ", minutes=" + minutes + ", seconds=" + seconds + ", playByPlays=" + playByPlays + "]\n";
	}
}