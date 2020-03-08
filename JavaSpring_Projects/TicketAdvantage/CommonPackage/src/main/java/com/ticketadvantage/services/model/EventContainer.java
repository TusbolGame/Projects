/**
 * 
 */
package com.ticketadvantage.services.model;

import java.util.Date;

/**
 * @author jmiller
 *
 */
public class EventContainer {
	private String eventtype;
	private String linetype;
	private String eventdate;
	private Date gamedate;
	private String gamesport;
	private String gametype;
	private String team;
	private String team1;
	private String team2;
	private String pitcher;
	private String pitcher1;
	private String pitcher2;
	private String rotationid;
	private String rotationid1;
	private String rotationid2;
	private String line;
	private String lineplusminus;
	private String juice;
	private String juiceplusminus;

	/**
	 * 
	 */
	public EventContainer() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the eventtype
	 */
	public String getEventtype() {
		return eventtype;
	}

	/**
	 * @param eventtype the eventtype to set
	 */
	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}

	/**
	 * @return the linetype
	 */
	public String getLinetype() {
		return linetype;
	}

	/**
	 * @param linetype the linetype to set
	 */
	public void setLinetype(String linetype) {
		this.linetype = linetype;
	}

	/**
	 * @return the eventdate
	 */
	public String getEventdate() {
		return eventdate;
	}

	/**
	 * @param eventdate the eventdate to set
	 */
	public void setEventdate(String eventdate) {
		this.eventdate = eventdate;
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
	 * @return the gamesport
	 */
	public String getGamesport() {
		return gamesport;
	}

	/**
	 * @param gamesport the gamesport to set
	 */
	public void setGamesport(String gamesport) {
		this.gamesport = gamesport;
	}

	/**
	 * @return the gametype
	 */
	public String getGametype() {
		return gametype;
	}

	/**
	 * @param gametype the gametype to set
	 */
	public void setGametype(String gametype) {
		this.gametype = gametype;
	}

	/**
	 * @return the team
	 */
	public String getTeam() {
		return team;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(String team) {
		this.team = team;
	}

	/**
	 * @return the team1
	 */
	public String getTeam1() {
		return team1;
	}

	/**
	 * @param team1 the team1 to set
	 */
	public void setTeam1(String team1) {
		this.team1 = team1;
	}

	/**
	 * @return the team2
	 */
	public String getTeam2() {
		return team2;
	}

	/**
	 * @param team2 the team2 to set
	 */
	public void setTeam2(String team2) {
		this.team2 = team2;
	}

	/**
	 * @return the pitcher
	 */
	public String getPitcher() {
		return pitcher;
	}

	/**
	 * @param pitcher the pitcher to set
	 */
	public void setPitcher(String pitcher) {
		this.pitcher = pitcher;
	}

	/**
	 * @return the pitcher1
	 */
	public String getPitcher1() {
		return pitcher1;
	}

	/**
	 * @param pitcher1 the pitcher1 to set
	 */
	public void setPitcher1(String pitcher1) {
		this.pitcher1 = pitcher1;
	}

	/**
	 * @return the pitcher2
	 */
	public String getPitcher2() {
		return pitcher2;
	}

	/**
	 * @param pitcher2 the pitcher2 to set
	 */
	public void setPitcher2(String pitcher2) {
		this.pitcher2 = pitcher2;
	}

	/**
	 * @return the rotationid
	 */
	public String getRotationid() {
		return rotationid;
	}

	/**
	 * @param rotationid the rotationid to set
	 */
	public void setRotationid(String rotationid) {
		this.rotationid = rotationid;
	}

	/**
	 * @return the rotationid1
	 */
	public String getRotationid1() {
		return rotationid1;
	}

	/**
	 * @param rotationid1 the rotationid1 to set
	 */
	public void setRotationid1(String rotationid1) {
		this.rotationid1 = rotationid1;
	}

	/**
	 * @return the rotationid2
	 */
	public String getRotationid2() {
		return rotationid2;
	}

	/**
	 * @param rotationid2 the rotationid2 to set
	 */
	public void setRotationid2(String rotationid2) {
		this.rotationid2 = rotationid2;
	}

	/**
	 * @return the line
	 */
	public String getLine() {
		return line;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(String line) {
		this.line = line;
	}

	/**
	 * @return the lineplusminus
	 */
	public String getLineplusminus() {
		return lineplusminus;
	}

	/**
	 * @param lineplusminus the lineplusminus to set
	 */
	public void setLineplusminus(String lineplusminus) {
		this.lineplusminus = lineplusminus;
	}

	/**
	 * @return the juice
	 */
	public String getJuice() {
		return juice;
	}

	/**
	 * @param juice the juice to set
	 */
	public void setJuice(String juice) {
		this.juice = juice;
	}

	/**
	 * @return the juiceplusminus
	 */
	public String getJuiceplusminus() {
		return juiceplusminus;
	}

	/**
	 * @param juiceplusminus the juiceplusminus to set
	 */
	public void setJuiceplusminus(String juiceplusminus) {
		this.juiceplusminus = juiceplusminus;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventContainer [eventtype=" + eventtype + ", linetype=" + linetype + ", eventdate=" + eventdate
				+ ", gamedate=" + gamedate + ", gamesport=" + gamesport + ", gametype=" + gametype + ", team=" + team
				+ ", team1=" + team1 + ", team2=" + team2 + ", pitcher=" + pitcher + ", pitcher1=" + pitcher1
				+ ", pitcher2=" + pitcher2 + ", rotationid=" + rotationid + ", rotationid1=" + rotationid1
				+ ", rotationid2=" + rotationid2 + ", line=" + line + ", lineplusminus=" + lineplusminus + ", juice="
				+ juice + ", juiceplusminus=" + juiceplusminus + "]";
	}
}