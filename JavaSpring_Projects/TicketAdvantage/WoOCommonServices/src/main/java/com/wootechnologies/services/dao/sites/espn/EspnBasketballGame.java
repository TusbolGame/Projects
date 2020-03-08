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
public class EspnBasketballGame {
	private Integer gameid;
	private String gamedate;
	private String awayteam;
	private String hometeam;
	private List<EspnBasketballPlayByPlay> plays = new ArrayList<EspnBasketballPlayByPlay>();

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
	 * @return the plays
	 */
	public List<EspnBasketballPlayByPlay> getPlays() {
		return plays;
	}

	/**
	 * @param plays the plays to set
	 */
	public void setPlays(List<EspnBasketballPlayByPlay> plays) {
		this.plays = plays;
	}

	/**
	 * 
	 * @param play
	 */
	public void addPlay(EspnBasketballPlayByPlay play) {
		this.plays.add(play);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EspnBasketballGame [gameid=" + gameid + ", gamedate=" + gamedate + ", awayteam=" + awayteam + ", hometeam="
				+ hometeam + ", plays=" + plays + "]";
	}
}
