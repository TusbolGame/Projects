/**
 * 
 */
package com.ticketadvantage.services.dao.sites.espn;

/**
 * @author jmiller
 *
 */
public class EspnFootballPlayByPlay {
	public static Integer KICK_OFF = 0;
	public static Integer MADE_FG = 0;
	public static Integer TD_PASS = 0;
	public static Integer TD_RUN = 0;
	public static Integer INTERCEPTION_TD = 0;
	public static Integer FUMBLE_TD = 0;
	public static Integer RUN = 0;
	public static Integer COMPLETED_PASS = 0;
	public static Integer INCOMPLETED_PASS = 0;
	public static Integer INTERCEPTION_PASS = 0;
	public static Integer PUNT = 0;
	public static Integer TIMEOUT = 0;
	public static Integer PENALTY = 0;

	private Integer gameid = new Integer(0);
	private Integer playtype = new Integer(0);
	private Integer down = new Integer(0);
	private Integer distance = new Integer(0);
	private String teamside;
	private Integer yardline = new Integer(0);
	private Integer quarter = new Integer(0);
	private Integer half = new Integer(0);
	private String playername = "";
	private Integer playeryards = new Integer(0);
	private Integer toyardline = new Integer(0);
	private String passername = "";
	private Integer passeryards = new Integer(0);
	private String returnname = "";
	private Integer returnyards = new Integer(0);
	private String gamedate = "";
	private String awayteam = "";
	private String hometeam = "";
	private Integer awayscore = new Integer(0);
	private Integer homescore = new Integer(0);
	private Integer minute = new Integer(0);
	private Integer second = new Integer(0);
	private String extrapointname;
	private Integer extrapointpts = new Integer(0);
	private Boolean isfirstdown = false;
	private Boolean didscore = false;
	private Boolean penalty = false;
	private Integer penaltyyards = new Integer(0);
	private Integer penaltytype = new Integer(0);
	private String penaltyteam;

	/**
	 * 
	 */
	public EspnFootballPlayByPlay() {
		super();
	}

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
	 * @return the playtype
	 */
	public Integer getPlaytype() {
		return playtype;
	}

	/**
	 * @param playtype the playtype to set
	 */
	public void setPlaytype(Integer playtype) {
		this.playtype = playtype;
	}

	/**
	 * @return the down
	 */
	public Integer getDown() {
		return down;
	}

	/**
	 * @param down the down to set
	 */
	public void setDown(Integer down) {
		this.down = down;
	}

	/**
	 * @return the distance
	 */
	public Integer getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	/**
	 * @return the teamside
	 */
	public String getTeamside() {
		return teamside;
	}

	/**
	 * @param teamside the teamside to set
	 */
	public void setTeamside(String teamside) {
		this.teamside = teamside;
	}

	/**
	 * @return the yardline
	 */
	public Integer getYardline() {
		return yardline;
	}

	/**
	 * @param yardline the yardline to set
	 */
	public void setYardline(Integer yardline) {
		this.yardline = yardline;
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
	 * @return the half
	 */
	public Integer getHalf() {
		return half;
	}

	/**
	 * @param half the half to set
	 */
	public void setHalf(Integer half) {
		this.half = half;
	}

	/**
	 * @return the playername
	 */
	public String getPlayername() {
		return playername;
	}

	/**
	 * @param playername the playername to set
	 */
	public void setPlayername(String playername) {
		this.playername = playername;
	}

	/**
	 * @return the playeryards
	 */
	public Integer getPlayeryards() {
		return playeryards;
	}

	/**
	 * @param playeryards the playeryards to set
	 */
	public void setPlayeryards(Integer playeryards) {
		this.playeryards = playeryards;
	}

	/**
	 * @return the toyardline
	 */
	public Integer getToyardline() {
		return toyardline;
	}

	/**
	 * @param toyardline the toyardline to set
	 */
	public void setToyardline(Integer toyardline) {
		this.toyardline = toyardline;
	}

	/**
	 * @return the passername
	 */
	public String getPassername() {
		return passername;
	}

	/**
	 * @param passername the passername to set
	 */
	public void setPassername(String passername) {
		this.passername = passername;
	}

	/**
	 * @return the passeryards
	 */
	public Integer getPasseryards() {
		return passeryards;
	}

	/**
	 * @param passeryards the passeryards to set
	 */
	public void setPasseryards(Integer passeryards) {
		this.passeryards = passeryards;
	}

	/**
	 * @return the returnname
	 */
	public String getReturnname() {
		return returnname;
	}

	/**
	 * @param returnname the returnname to set
	 */
	public void setReturnname(String returnname) {
		this.returnname = returnname;
	}

	/**
	 * @return the returnyards
	 */
	public Integer getReturnyards() {
		return returnyards;
	}

	/**
	 * @param returnyards the returnyards to set
	 */
	public void setReturnyards(Integer returnyards) {
		this.returnyards = returnyards;
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
	 * @return the extrapointname
	 */
	public String getExtrapointname() {
		return extrapointname;
	}

	/**
	 * @param extrapointname the extrapointname to set
	 */
	public void setExtrapointname(String extrapointname) {
		this.extrapointname = extrapointname;
	}

	/**
	 * @return the extrapointpts
	 */
	public Integer getExtrapointpts() {
		return extrapointpts;
	}

	/**
	 * @param extrapointpts the extrapointpts to set
	 */
	public void setExtrapointpts(Integer extrapointpts) {
		this.extrapointpts = extrapointpts;
	}

	/**
	 * @return the isfirstdown
	 */
	public Boolean getIsfirstdown() {
		return isfirstdown;
	}

	/**
	 * @param isfirstdown the isfirstdown to set
	 */
	public void setIsfirstdown(Boolean isfirstdown) {
		this.isfirstdown = isfirstdown;
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
	 * @return the penalty
	 */
	public Boolean getPenalty() {
		return penalty;
	}

	/**
	 * @param penalty the penalty to set
	 */
	public void setPenalty(Boolean penalty) {
		this.penalty = penalty;
	}

	/**
	 * @return the penaltyyards
	 */
	public Integer getPenaltyyards() {
		return penaltyyards;
	}

	/**
	 * @param penaltyyards the penaltyyards to set
	 */
	public void setPenaltyyards(Integer penaltyyards) {
		this.penaltyyards = penaltyyards;
	}

	/**
	 * @return the penaltytype
	 */
	public Integer getPenaltytype() {
		return penaltytype;
	}

	/**
	 * @param penaltytype the penaltytype to set
	 */
	public void setPenaltytype(Integer penaltytype) {
		this.penaltytype = penaltytype;
	}

	/**
	 * @return the penaltyteam
	 */
	public String getPenaltyteam() {
		return penaltyteam;
	}

	/**
	 * @param penaltyteam the penaltyteam to set
	 */
	public void setPenaltyteam(String penaltyteam) {
		this.penaltyteam = penaltyteam;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EspnFootballPlayByPlay [gameid=" + gameid + ", playtype=" + playtype + ", down=" + down + ", distance="
				+ distance + ", teamside=" + teamside + ", yardline=" + yardline + ", quarter=" + quarter + ", half="
				+ half + ", playername=" + playername + ", playeryards=" + playeryards + ", toyardline=" + toyardline
				+ ", passername=" + passername + ", passeryards=" + passeryards + ", returnname=" + returnname
				+ ", returnyards=" + returnyards + ", gamedate=" + gamedate + ", awayteam=" + awayteam + ", hometeam="
				+ hometeam + ", awayscore=" + awayscore + ", homescore=" + homescore + ", minute=" + minute
				+ ", second=" + second + ", extrapointname=" + extrapointname + ", extrapointpts=" + extrapointpts
				+ ", isfirstdown=" + isfirstdown + ", didscore=" + didscore + ", penalty=" + penalty + ", penaltyyards="
				+ penaltyyards + ", penaltytype=" + penaltytype + ", penaltyteam=" + penaltyteam + "]\n";
	}
}