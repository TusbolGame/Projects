package com.wooanalytics.dao.sites.masseyratings;

import java.util.Date;

/**
 * 
 * @author jmiller
 *
 */
public class MasseyRatingsData {
	private String dateString;
	private Date dateOfGame;
	private Integer rank = Integer.valueOf("0");
	private String team = "";
	private String conf = "";
	private Integer wins = Integer.valueOf("0");
	private Integer losses = Integer.valueOf("0");
	private String opponentTeam;
	private String urlData;
	private int pointsfor;
	private int pointsagainst;
	private int score1;
	private int score2;
	private boolean spreadCovered;
	private boolean totalCovered;
	private boolean gameComplete;
	private boolean awayGame;
	private Float spread;
	private String linefavorite;

	/**
	 * @return the dateString
	 */
	public String getDateString() {
		return dateString;
	}
	/**
	 * @param dateString the dateString to set
	 */
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	/**
	 * @return the dateOfGame
	 */
	public Date getDateOfGame() {
		return dateOfGame;
	}
	/**
	 * @param dateOfGame the dateOfGame to set
	 */
	public void setDateOfGame(Date dateOfGame) {
		this.dateOfGame = dateOfGame;
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
	 * @return the opponentTeam
	 */
	public String getOpponentTeam() {
		return opponentTeam;
	}
	/**
	 * @param opponentTeam the opponentTeam to set
	 */
	public void setOpponentTeam(String opponentTeam) {
		this.opponentTeam = opponentTeam;
	}
	/**
	 * @return the urlData
	 */
	public String getUrlData() {
		return urlData;
	}
	/**
	 * @param urlData the urlData to set
	 */
	public void setUrlData(String urlData) {
		this.urlData = urlData;
	}
	/**
	 * @return the pointsfor
	 */
	public int getPointsfor() {
		return pointsfor;
	}
	/**
	 * @param pointsfor the pointsfor to set
	 */
	public void setPointsfor(int pointsfor) {
		this.pointsfor = pointsfor;
	}
	/**
	 * @return the pointsagainst
	 */
	public int getPointsagainst() {
		return pointsagainst;
	}
	/**
	 * @param pointsagainst the pointsagainst to set
	 */
	public void setPointsagainst(int pointsagainst) {
		this.pointsagainst = pointsagainst;
	}
	/**
	 * @return the score1
	 */
	public int getScore1() {
		return score1;
	}
	/**
	 * @param score1 the score1 to set
	 */
	public void setScore1(int score1) {
		this.score1 = score1;
	}
	/**
	 * @return the score2
	 */
	public int getScore2() {
		return score2;
	}
	/**
	 * @param score2 the score2 to set
	 */
	public void setScore2(int score2) {
		this.score2 = score2;
	}
	/**
	 * @return the spreadCovered
	 */
	public boolean isSpreadCovered() {
		return spreadCovered;
	}
	/**
	 * @param spreadCovered the spreadCovered to set
	 */
	public void setSpreadCovered(boolean spreadCovered) {
		this.spreadCovered = spreadCovered;
	}
	/**
	 * @return the totalCovered
	 */
	public boolean isTotalCovered() {
		return totalCovered;
	}
	/**
	 * @param totalCovered the totalCovered to set
	 */
	public void setTotalCovered(boolean totalCovered) {
		this.totalCovered = totalCovered;
	}
	/**
	 * @return the gameComplete
	 */
	public boolean isGameComplete() {
		return gameComplete;
	}
	/**
	 * @param gameComplete the gameComplete to set
	 */
	public void setGameComplete(boolean gameComplete) {
		this.gameComplete = gameComplete;
	}
	
	/**
	 * @return the awayGame
	 */
	public boolean isAwayGame() {
		return awayGame;
	}
	/**
	 * @param awayGame the awayGame to set
	 */
	public void setAwayGame(boolean awayGame) {
		this.awayGame = awayGame;
	}

	/**
	 * @return the spread
	 */
	public Float getSpread() {
		return spread;
	}

	/**
	 * @param spread the spread to set
	 */
	public void setSpread(Float spread) {
		this.spread = spread;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MasseyRatingsData [dateString=" + dateString + ", dateOfGame=" + dateOfGame + ", rank=" + rank
				+ ", team=" + team + ", conf=" + conf + ", wins=" + wins + ", losses=" + losses + ", opponentTeam="
				+ opponentTeam + ", urlData=" + urlData + ", pointsfor=" + pointsfor + ", pointsagainst="
				+ pointsagainst + ", score1=" + score1 + ", score2=" + score2 + ", spreadCovered=" + spreadCovered
				+ ", totalCovered=" + totalCovered + ", gameComplete=" + gameComplete + ", awayGame=" + awayGame
				+ ", spread=" + spread + ", linefavorite=" + linefavorite + "]";
	}
}