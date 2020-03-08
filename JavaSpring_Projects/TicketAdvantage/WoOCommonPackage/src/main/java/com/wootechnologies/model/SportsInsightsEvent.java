/**
 * 
 */
package com.wootechnologies.model;

/**
 * @author jmiller
 *
 */
public class SportsInsightsEvent {
	public Integer EventId;
	public Integer SportId;
	public String HomeTeam;
	public String VisitorTeam;
	public String HomeTeamShort;
	public String VisitorTeamShort;
	public Integer TotalBets;
	public String EventDate;
	public String EventDateShort;
	public Integer HomeNSS;
	public Integer VisitorNSS;
	public String SportName;
	public String HomePitcher;
	public String VisitorPitcher;
	public Integer GameId;
	public Integer VisitorScore;
	public Integer HomeScore;
	public Integer PeriodState;
	public String PeriodTime;
	public String PeriodShort;
	public SportsInsightsValues VisitorValues;
	public SportsInsightsValues HomeValues;
	public Integer Value1;
	public Integer Value2;
	public String Group1;
	public String Group2;
	public Boolean MyGame;
	public Boolean NeutralSite;
	public String VisitorROTNumber;
	public String HomeROTNumber;
	public String GameCode;
	public Integer LeagueId;
	public String HalftimeModifiedDate;
	public Integer SystemEventId;
	public String Line;

	/**
	 * 
	 */
	public SportsInsightsEvent() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SportsInsightsEvent [EventId=" + EventId + ", SportId=" + SportId + ", HomeTeam=" + HomeTeam
				+ ", VisitorTeam=" + VisitorTeam + ", HomeTeamShort=" + HomeTeamShort + ", VisitorTeamShort="
				+ VisitorTeamShort + ", TotalBets=" + TotalBets + ", EventDate=" + EventDate + ", EventDateShort="
				+ EventDateShort + ", HomeNSS=" + HomeNSS + ", VisitorNSS=" + VisitorNSS + ", SportName=" + SportName
				+ ", HomePitcher=" + HomePitcher + ", VisitorPitcher=" + VisitorPitcher + ", GameId=" + GameId
				+ ", VisitorScore=" + VisitorScore + ", HomeScore=" + HomeScore + ", PeriodState=" + PeriodState
				+ ", PeriodTime=" + PeriodTime + ", PeriodShort=" + PeriodShort + ", VisitorValues=" + VisitorValues
				+ ", HomeValues=" + HomeValues + ", Value1=" + Value1 + ", Value2=" + Value2 + ", Group1=" + Group1
				+ ", Group2=" + Group2 + ", MyGame=" + MyGame + ", NeutralSite=" + NeutralSite + ", VisitorROTNumber="
				+ VisitorROTNumber + ", HomeROTNumber=" + HomeROTNumber + ", GameCode=" + GameCode + ", LeagueId="
				+ LeagueId + ", HalftimeModifiedDate=" + HalftimeModifiedDate + ", SystemEventId=" + SystemEventId
				+ "]";
	}
}