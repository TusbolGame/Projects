package com.linemovement.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.linemovement.dto.MovementData;

/**
*  @author SureshKoumar @Pinesucceed
*  May 3, 2019
*/
@JsonAutoDetect
public class LineMovement {

	@JsonProperty
	private String id;

	/**
	 * game date and time
	 */
	@JsonProperty
	private LocalDateTime eventDateTime;
	
	@JsonProperty
	private LocalDateTime eventDate;
	
	/**
	 * {1=nfl, 2=nba, 3=mlb, 4=nhl, 8=wnba, 11=ncaaf, 12=ncaab}
	 */
	@JsonProperty
	private int sportNumber; 
	
	/**
	 * {1=nfl, 2=nba, 3=mlb, 4=nhl, 8=wnba, 11=ncaaf, 12=ncaab}
	 */
	@JsonProperty
	private String sportType; 
	
	/**
	 * // Game - 0 // First Half - 1  // Second Half - 2
	 */
	@JsonProperty
	private int gameNumber;
	
	/**
	 * // Game - 0 // First Half - 1  // Second Half - 2
	 */
	@JsonProperty
	private String gameType;

	/**
	 * //{1=Spread, 2=Moneyline, 3=OverUnder}
	 */
	@JsonProperty
	private int lineNumber; 
	
	/**
	 * //{1=Spread, 2=Moneyline, 3=OverUnder}
	 */
	@JsonProperty
	private String lineType; 
	
	/**
	 * year value
	 */
	@JsonProperty
	private Integer year;
	
	/**
	 * year value
	 */
	@JsonProperty
	private Integer week;

	/**
	 * name of the sportbook e.g Consensus, ATLANTIS LINE MOVEMENTS etc..
	 */
	@JsonProperty
	private String sportsbook;
	
	/**
	 * Team one name
	 */
	@JsonProperty
	private String visitorteam;  
	
	/**
	 * Team two name
	 */
	@JsonProperty
    private String hometeam;

	/**
	 * name of the site data associated with
	 */
	@JsonProperty
	private String siteName;

	@JsonProperty
	private String visitorScore;

	@JsonProperty
	private String homeScore;
	
	@JsonProperty
	private String visitorRotationId;

	@JsonProperty
	private String homeRotationId;

	/**
	 * Line movement history
	 */
	@JsonProperty
	private List<MovementData> linemovements = new ArrayList<>();

//	@JsonProperty
	//private Map<String,List<MovementData>> linemovements = new HashMap<String, List<MovementData>>();

	@Override
	public boolean equals(Object obj) {
		LineMovement lm = (LineMovement) obj;
		if(lm.eventDate.equals(this.getEventDate()))
			return true;
		else 
			return false;
	}

	@Override
	public int hashCode() {
		int hash = (this.hometeam+this.getEventDate()).hashCode();
		return hash;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * getters and Setters
	 */
	
	public int getSportNumber() {
		return sportNumber;
	}

	public LocalDateTime getEventDateTime() {
		return eventDateTime;
	}

	public void setEventDateTime(LocalDateTime eventDateTime) {
		this.eventDateTime = eventDateTime;
	}

	public void setSportNumber(int sportNumber) {
		this.sportNumber = sportNumber;
	}

	public String getSportType() {
		return sportType;
	}

	public void setSportType(String sportType) {
		this.sportType = sportType;
	}

	public int getGameNumber() {
		return gameNumber;
	}

	public void setGameNumber(int gameNumber) {
		this.gameNumber = gameNumber;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
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

	public String getSportsbook() {
		return sportsbook;
	}

	public void setSportsbook(String sportsbook) {
		this.sportsbook = sportsbook;
	}

	public String getVisitorteam() {
		return visitorteam;
	}

	public void setVisitorteam(String visitorteam) {
		this.visitorteam = visitorteam;
	}

	public String getHometeam() {
		return hometeam;
	}

	public void setHometeam(String hometeam) {
		this.hometeam = hometeam;
	}

	public List<MovementData> getLinemovements() {
		return linemovements;
	}

	public void setLinemovements(List<MovementData> linemovements) {
		this.linemovements = linemovements;
	}

	public void addLinemovement(MovementData movementData) {
		this.linemovements.add(movementData);
	}

	public LocalDateTime getEventDate() {
		return eventDate;
	}

	public void setEventDate(LocalDateTime eventDate) {
		this.eventDate = eventDate;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(String homeScore) {
		this.homeScore = homeScore;
	}

	public String getVisitorScore() {
		return visitorScore;
	}

	public void setVisitorScore(String visitorScore) {
		this.visitorScore = visitorScore;
	}

	/**
	 * @return the visitorRotationId
	 */
	public String getVisitorRotationId() {
		return visitorRotationId;
	}

	/**
	 * @param visitorRotationId the visitorRotationId to set
	 */
	public void setVisitorRotationId(String visitorRotationId) {
		this.visitorRotationId = visitorRotationId;
	}

	/**
	 * @return the homeRotationId
	 */
	public String getHomeRotationId() {
		return homeRotationId;
	}

	/**
	 * @param homeRotationId the homeRotationId to set
	 */
	public void setHomeRotationId(String homeRotationId) {
		this.homeRotationId = homeRotationId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LineMovement [id=" + id + ", eventDateTime=" + eventDateTime + ", eventDate=" + eventDate
				+ ", sportNumber=" + sportNumber + ", sportType=" + sportType + ", gameNumber=" + gameNumber
				+ ", gameType=" + gameType + ", lineNumber=" + lineNumber + ", lineType=" + lineType + ", year=" + year
				+ ", week=" + week + ", sportsbook=" + sportsbook + ", visitorteam=" + visitorteam + ", hometeam="
				+ hometeam + ", siteName=" + siteName + ", visitorScore=" + visitorScore + ", homeScore=" + homeScore
				+ ", visitorRotationId=" + visitorRotationId + ", homeRotationId=" + homeRotationId + ", linemovements="
				+ linemovements + "]";
	}
}