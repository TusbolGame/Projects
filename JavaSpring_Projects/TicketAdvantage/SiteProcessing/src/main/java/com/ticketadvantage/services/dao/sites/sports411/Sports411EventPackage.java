/**
 * 
 */
package com.ticketadvantage.services.dao.sites.sports411;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONArray;

import com.ticketadvantage.services.dao.sites.SiteEventPackage;

/**
 * @author jmiller
 *
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class Sports411EventPackage extends SiteEventPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer spreadMax;
	private Integer totalMax;
	private Integer mlMax;
	private Map<String, String> hiddenData;
	private String betId = "";
	private String gameTypeID = "";
	private String idGame = "";
	private String idlg = "";
	private String idspt = "";
	private String parentIdg = "";
	private String idCombinationGame = "";
	private String odds = "";
	private String pitcher = "";
	private String play = "";
	private String points = "";
	private String pointsTeaser = "";
	private String sport = "";
	private String teamName = "";
	private String wagerType = "";
	private String displayLine = "";
	private String gameType = "";
	private String details = "";
	private String lineType = "";
	private String lineStyle = "";
	private String categoryDesc = "";
	private String leagueDesc = "";
	private String gpd = "";
	private JSONArray derivatives;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 */
	public Sports411EventPackage() {
		super();
	}

	/**
	 * @return the spreadMax
	 */
	public Integer getSpreadMax() {
		return spreadMax;
	}

	/**
	 * @param spreadMax the spreadMax to set
	 */
	public void setSpreadMax(Integer spreadMax) {
		this.spreadMax = spreadMax;
	}

	/**
	 * @return the totalMax
	 */
	public Integer getTotalMax() {
		return totalMax;
	}

	/**
	 * @param totalMax the totalMax to set
	 */
	public void setTotalMax(Integer totalMax) {
		this.totalMax = totalMax;
	}

	/**
	 * @return the mlMax
	 */
	public Integer getMlMax() {
		return mlMax;
	}

	/**
	 * @param mlMax the mlMax to set
	 */
	public void setMlMax(Integer mlMax) {
		this.mlMax = mlMax;
	}

	/**
	 * @return the hiddenData
	 */
	public Map<String, String> getHiddenData() {
		return hiddenData;
	}

	/**
	 * @param hiddenData the hiddenData to set
	 */
	public void setHiddenData(Map<String, String> hiddenData) {
		this.hiddenData = hiddenData;
	}

	/**
	 * @return the betId
	 */
	public String getBetId() {
		return betId;
	}

	/**
	 * @param betId the betId to set
	 */
	public void setBetId(String betId) {
		this.betId = betId;
	}

	/**
	 * @return the gameTypeID
	 */
	public String getGameTypeID() {
		return gameTypeID;
	}

	/**
	 * @param gameTypeID the gameTypeID to set
	 */
	public void setGameTypeID(String gameTypeID) {
		this.gameTypeID = gameTypeID;
	}

	/**
	 * @return the idGame
	 */
	public String getIdGame() {
		return idGame;
	}

	/**
	 * @param idGame the idGame to set
	 */
	public void setIdGame(String idGame) {
		this.idGame = idGame;
	}

	/**
	 * @return the idlg
	 */
	public String getIdlg() {
		return idlg;
	}

	/**
	 * @param idlg the idlg to set
	 */
	public void setIdlg(String idlg) {
		this.idlg = idlg;
	}

	/**
	 * @return the parentIdg
	 */
	public String getParentIdg() {
		return parentIdg;
	}

	/**
	 * @param parentIdg the parentIdg to set
	 */
	public void setParentIdg(String parentIdg) {
		this.parentIdg = parentIdg;
	}

	/**
	 * @return the idCombinationGame
	 */
	public String getIdCombinationGame() {
		return idCombinationGame;
	}

	/**
	 * @param idCombinationGame the idCombinationGame to set
	 */
	public void setIdCombinationGame(String idCombinationGame) {
		this.idCombinationGame = idCombinationGame;
	}

	/**
	 * @return the odds
	 */
	public String getOdds() {
		return odds;
	}

	/**
	 * @param odds the odds to set
	 */
	public void setOdds(String odds) {
		this.odds = odds;
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
	 * @return the play
	 */
	public String getPlay() {
		return play;
	}

	/**
	 * @param play the play to set
	 */
	public void setPlay(String play) {
		this.play = play;
	}

	/**
	 * @return the points
	 */
	public String getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(String points) {
		this.points = points;
	}

	/**
	 * @return the pointsTeaser
	 */
	public String getPointsTeaser() {
		return pointsTeaser;
	}

	/**
	 * @param pointsTeaser the pointsTeaser to set
	 */
	public void setPointsTeaser(String pointsTeaser) {
		this.pointsTeaser = pointsTeaser;
	}

	/**
	 * @return the sport
	 */
	public String getSport() {
		return sport;
	}

	/**
	 * @param sport the sport to set
	 */
	public void setSport(String sport) {
		this.sport = sport;
	}

	/**
	 * @return the teamName
	 */
	public String getTeamName() {
		return teamName;
	}

	/**
	 * @param teamName the teamName to set
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	/**
	 * @return the wagerType
	 */
	public String getWagerType() {
		return wagerType;
	}

	/**
	 * @param wagerType the wagerType to set
	 */
	public void setWagerType(String wagerType) {
		this.wagerType = wagerType;
	}

	/**
	 * @return the displayLine
	 */
	public String getDisplayLine() {
		return displayLine;
	}

	/**
	 * @param displayLine the displayLine to set
	 */
	public void setDisplayLine(String displayLine) {
		this.displayLine = displayLine;
	}

	/**
	 * @return the gameType
	 */
	public String getGameType() {
		return gameType;
	}

	/**
	 * @param gameType the gameType to set
	 */
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * @return the lineType
	 */
	public String getLineType() {
		return lineType;
	}

	/**
	 * @param lineType the lineType to set
	 */
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	/**
	 * @return the lineStyle
	 */
	public String getLineStyle() {
		return lineStyle;
	}

	/**
	 * @param lineStyle the lineStyle to set
	 */
	public void setLineStyle(String lineStyle) {
		this.lineStyle = lineStyle;
	}

	/**
	 * @return the categoryDesc
	 */
	public String getCategoryDesc() {
		return categoryDesc;
	}

	/**
	 * @param categoryDesc the categoryDesc to set
	 */
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	/**
	 * @return the leagueDesc
	 */
	public String getLeagueDesc() {
		return leagueDesc;
	}

	/**
	 * @param leagueDesc the leagueDesc to set
	 */
	public void setLeagueDesc(String leagueDesc) {
		this.leagueDesc = leagueDesc;
	}

	/**
	 * @return the idspt
	 */
	public String getIdspt() {
		return idspt;
	}

	/**
	 * @param idspt the idspt to set
	 */
	public void setIdspt(String idspt) {
		this.idspt = idspt;
	}

	/**
	 * @return the gpd
	 */
	public String getGpd() {
		return gpd;
	}

	/**
	 * @param gpd the gpd to set
	 */
	public void setGpd(String gpd) {
		this.gpd = gpd;
	}

	/**
	 * @return the derivatives
	 */
	public JSONArray getDerivatives() {
		return derivatives;
	}

	/**
	 * @param derivatives the derivatives to set
	 */
	public void setDerivatives(JSONArray derivatives) {
		this.derivatives = derivatives;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Sports411EventPackage [spreadMax=" + spreadMax + ", totalMax=" + totalMax + ", mlMax=" + mlMax
				+ ", hiddenData=" + hiddenData + ", betId=" + betId + ", gameTypeID=" + gameTypeID + ", idGame="
				+ idGame + ", idlg=" + idlg + ", idspt=" + idspt + ", parentIdg=" + parentIdg + ", idCombinationGame="
				+ idCombinationGame + ", odds=" + odds + ", pitcher=" + pitcher + ", play=" + play + ", points="
				+ points + ", pointsTeaser=" + pointsTeaser + ", sport=" + sport + ", teamName=" + teamName
				+ ", wagerType=" + wagerType + ", displayLine=" + displayLine + ", gameType=" + gameType + ", details="
				+ details + ", lineType=" + lineType + ", lineStyle=" + lineStyle + ", categoryDesc=" + categoryDesc
				+ ", leagueDesc=" + leagueDesc + ", gpd=" + gpd + ", derivatives=" + derivatives + "]";
	}
}