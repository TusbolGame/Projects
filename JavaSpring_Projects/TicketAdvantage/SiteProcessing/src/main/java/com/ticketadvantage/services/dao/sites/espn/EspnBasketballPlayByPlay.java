/**
 * 
 */
package com.ticketadvantage.services.dao.sites.espn;

/**
 * @author jmiller
 *
 */
public class EspnBasketballPlayByPlay {
	public static Integer BLOCK = 1;
	public static Integer LAYUP = 2;
	public static Integer THREE_POINT = 3;
	public static Integer DEFENSIVE_REBOUND = 4;
	public static Integer TEAM_DEFENSIVE_REBOUND = 5;
	public static Integer TWO_POINT = 6;
	public static Integer TURNOVER = 7;
	public static Integer STEAL = 8;
	public static Integer LOOSE_BALL = 9;
	public static Integer FOUL = 10;
	public static Integer ASSIST = 11;
	public static Integer OFFENSIVE_REBOUND = 12;
	public static Integer SHOOTING_FOUL = 13;
	public static Integer FREE_THROW = 14;
	public static Integer OFFENSIVE_FOUL = 15;
	public static Integer TEAM_OFFENSIVE_REBOUND = 16;
	public static Integer GAINS_POSSESION = 17;

	// 0 - gains possesion
	// 1 - bad pass
	// 2 - steal
	// 3 - misses driving layup
	// 4 - defensive rebound
	// 5 - offensive rebound
	// 6 - misses two point shot
	// 7 - make two point shot
	// 8 - assist
	// 9 - block
	// 10 - foul
	// 11 - enters game
	// 12 - free throw
	// 13 - lost ball

	private Integer gameid = new Integer(0);
	private String gamedate = "";
	private String awayteam = "";
	private String hometeam = "";
	private String firstname = "";
	private String lastname = "";
	private Integer quarter = new Integer(0);
	private Integer actiontype;
	private Boolean scoringplay = false;
	private Boolean didscore = false;
	private Integer shotdistance = new Integer(0);
	private Integer awayscore = new Integer(0);
	private Integer homescore = new Integer(0);
	private Integer minute = new Integer(0);
	private Integer second = new Integer(0);

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
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
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
	 * @return the scoringplay
	 */
	public Boolean getScoringplay() {
		return scoringplay;
	}

	/**
	 * @param scoringplay the scoringplay to set
	 */
	public void setScoringplay(Boolean scoringplay) {
		this.scoringplay = scoringplay;
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
	 * @return the shotdistance
	 */
	public Integer getShotdistance() {
		return shotdistance;
	}

	/**
	 * @param shotdistance the shotdistance to set
	 */
	public void setShotdistance(Integer shotdistance) {
		this.shotdistance = shotdistance;
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
	 * @return the second
	 */
	public Integer getSecond() {
		return second;
	}

	/**
	 * @param second the second to set
	 */
	public void setSecond(Integer second) {
		this.second = second;
	}

	/**
	 * 
	 * @return
	 */
	public String toJSON() {
		return "pbp:{"
				+ "\"gid\":" + gameid 
				+ ", \"gdate\":\"" + gamedate 
				+ "\", \"fname\":\"" + firstname
				+ "\", \"lname\":\"" + lastname 
				+ "\", \"qrter\":" + quarter 
				+ ", \"action\":" + actiontype 
				+ ", \"splay\":" + scoringplay 
				+ ", \"dscore\":" + didscore 
				+ ", \"shot\":" + shotdistance 
				+ ", \"ascore\":" + awayscore
				+ ", \"hscore\":" + homescore 
				+ ", \"min\":" + minute 
				+ ", \"sec\":" + second + "}";		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EspnBasketballPlayByPlay [gameid=" + gameid + ", gamedate=" + gamedate + ", awayteam=" + awayteam + ", hometeam="
				+ hometeam + ", firstname=" + firstname + ", lastname=" + lastname + ", quarter=" + quarter
				+ ", actiontype=" + actiontype + ", scoringplay=" + scoringplay + ", didscore=" + didscore
				+ ", shotdistance=" + shotdistance + ", awayscore=" + awayscore + ", homescore=" + homescore
				+ ", minute=" + minute + ", second=" + second + "]";
	}
}