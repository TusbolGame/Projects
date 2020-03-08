/**
 * 
 */
package com.ticketadvantage.services.dao.sites.kenpom;

import java.util.Date;

/**
 * @author jmiller
 *
 */
public class KenPomData {
	private String dateString;
	private Date dateOfGame;
	private String roadTeam;
	private String homeTeam;
	private String roadUrlData;
	private String homeUrlData;
	private int roadPoints;
	private int homePoints;
	private int score1;
	private int score2;
	private int spread;
	private int total;
	private boolean spreadCovered;
	private boolean totalCovered;
	private boolean gameComplete;
	private int siteLocation;
	private int siteLocationType;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

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
	 * @return the roadTeam
	 */
	public String getRoadTeam() {
		return roadTeam;
	}

	/**
	 * @param roadTeam the roadTeam to set
	 */
	public void setRoadTeam(String roadTeam) {
		this.roadTeam = roadTeam;
	}

	/**
	 * @return the homeTeam
	 */
	public String getHomeTeam() {
		return homeTeam;
	}

	/**
	 * @param homeTeam the homeTeam to set
	 */
	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}

	/**
	 * @return the roadUrlData
	 */
	public String getRoadUrlData() {
		return roadUrlData;
	}

	/**
	 * @param roadUrlData the roadUrlData to set
	 */
	public void setRoadUrlData(String roadUrlData) {
		this.roadUrlData = roadUrlData;
	}

	/**
	 * @return the homeUrlData
	 */
	public String getHomeUrlData() {
		return homeUrlData;
	}

	/**
	 * @param homeUrlData the homeUrlData to set
	 */
	public void setHomeUrlData(String homeUrlData) {
		this.homeUrlData = homeUrlData;
	}

	/**
	 * @return the roadPoints
	 */
	public int getRoadPoints() {
		return roadPoints;
	}

	/**
	 * @param roadPoints the roadPoints to set
	 */
	public void setRoadPoints(int roadPoints) {
		this.roadPoints = roadPoints;
	}

	/**
	 * @return the homePoints
	 */
	public int getHomePoints() {
		return homePoints;
	}

	/**
	 * @param homePoints the homePoints to set
	 */
	public void setHomePoints(int homePoints) {
		this.homePoints = homePoints;
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
	 * @return the spread
	 */
	public int getSpread() {
		return spread;
	}

	/**
	 * @param spread the spread to set
	 */
	public void setSpread(int spread) {
		this.spread = spread;
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
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
	 * @return the siteLocation
	 */
	public int getSiteLocation() {
		return siteLocation;
	}

	/**
	 * @param siteLocation the siteLocation to set
	 */
	public void setSiteLocation(int siteLocation) {
		this.siteLocation = siteLocation;
	}

	/**
	 * @return the siteLocationType
	 */
	public int getSiteLocationType() {
		return siteLocationType;
	}

	/**
	 * @param siteLocationType the siteLocationType to set
	 */
	public void setSiteLocationType(int siteLocationType) {
		this.siteLocationType = siteLocationType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KenPomData [dateString=" + dateString + ", dateOfGame=" + dateOfGame + ", roadTeam=" + roadTeam
				+ ", homeTeam=" + homeTeam + ", roadUrlData=" + roadUrlData + ", homeUrlData=" + homeUrlData
				+ ", roadPoints=" + roadPoints + ", homePoints=" + homePoints + ", score1=" + score1 + ", score2="
				+ score2 + ", spread=" + spread + ", total=" + total + ", spreadCovered=" + spreadCovered
				+ ", totalCovered=" + totalCovered + ", gameComplete=" + gameComplete + ", siteLocation=" + siteLocation
				+ ", siteLocationType=" + siteLocationType + "]";
	}
}
