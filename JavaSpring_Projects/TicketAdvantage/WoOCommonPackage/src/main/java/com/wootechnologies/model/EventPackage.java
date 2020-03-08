/**
 * 
 */
package com.wootechnologies.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author jmiller
 *
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class EventPackage implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement
	private Integer id;
	
	@XmlElement
	private TeamPackage teamone;
	
	@XmlElement
	private TeamPackage teamtwo;

	@XmlElement
	private Date eventdatetime;

	@XmlElement	
	private String dateofevent;

	@XmlElement	
	private String timeofevent;

	@XmlElement	
	private String sporttype;

	@XmlElement	
	private String linetype;

	@XmlElement	
	private String eventtype;

	@XmlElement	
	private String wagertype;
		
	@XmlElement
	private String periodType;

	@XmlElement
	private String periodNumber;

	@XmlElement
	private Integer sportsInsightsEventId;

	@XmlElement
	private Boolean gameIsFinal = new Boolean(false);

	@XmlElement
	private Boolean firstIsFinal = new Boolean(false);

	@XmlElement
	private Boolean secondIsFinal = new Boolean(false);

	@XmlElement
	private Boolean thirdIsFinal = new Boolean(false);

	@XmlElement
	private Boolean liveIsFinal = new Boolean(false);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Do nothing
	}

	/**
	 * 
	 */
	public EventPackage() {
		super();
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the teamone
	 */
	public TeamPackage getTeamone() {
		return teamone;
	}

	/**
	 * @param teamone the teamone to set
	 */
	public void setTeamone(TeamPackage teamone) {
		this.teamone = teamone;
	}

	/**
	 * @return the teamtwo
	 */
	public TeamPackage getTeamtwo() {
		return teamtwo;
	}

	/**
	 * @param teamtwo the teamtwo to set
	 */
	public void setTeamtwo(TeamPackage teamtwo) {
		this.teamtwo = teamtwo;
	}

	/**
	 * @return the dateofevent
	 */
	public String getDateofevent() {
		return dateofevent;
	}

	/**
	 * @param dateofevent the dateofevent to set
	 */
	public void setDateofevent(String dateofevent) {
		this.dateofevent = dateofevent;
	}

	/**
	 * @return the timeofevent
	 */
	public String getTimeofevent() {
		return timeofevent;
	}

	/**
	 * @param timeofevent the timeofevent to set
	 */
	public void setTimeofevent(String timeofevent) {
		this.timeofevent = timeofevent;
	}

	/**
	 * @return the sporttype
	 */
	public String getSporttype() {
		return sporttype;
	}

	/**
	 * @param sporttype the sporttype to set
	 */
	public void setSporttype(String sporttype) {
		this.sporttype = sporttype;
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
	 * @return the wagertype
	 */
	public String getWagertype() {
		return wagertype;
	}

	/**
	 * @param wagertype the wagertype to set
	 */
	public void setWagertype(String wagertype) {
		this.wagertype = wagertype;
	}

	/**
	 * @return the eventdatetime
	 */
	public Date getEventdatetime() {
		return eventdatetime;
	}

	/**
	 * @param eventdatetime the eventdatetime to set
	 */
	public void setEventdatetime(Date eventdatetime) {
		this.eventdatetime = eventdatetime;
	}

	/**
	 * @return the periodType
	 */
	public String getPeriodType() {
		return periodType;
	}

	/**
	 * @param periodType the periodType to set
	 */
	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	/**
	 * @return the periodNumber
	 */
	public String getPeriodNumber() {
		return periodNumber;
	}

	/**
	 * @param periodNumber the periodNumber to set
	 */
	public void setPeriodNumber(String periodNumber) {
		this.periodNumber = periodNumber;
	}

	/**
	 * @return the sportsInsightsEventId
	 */
	public Integer getSportsInsightsEventId() {
		return sportsInsightsEventId;
	}

	/**
	 * @param sportsInsightsEventId the sportsInsightsEventId to set
	 */
	public void setSportsInsightsEventId(Integer sportsInsightsEventId) {
		this.sportsInsightsEventId = sportsInsightsEventId;
	}

	/**
	 * @return the gameIsFinal
	 */
	public Boolean getGameIsFinal() {
		return gameIsFinal;
	}

	/**
	 * @param gameIsFinal the gameIsFinal to set
	 */
	public void setGameIsFinal(Boolean gameIsFinal) {
		this.gameIsFinal = gameIsFinal;
	}

	/**
	 * @return the firstIsFinal
	 */
	public Boolean getFirstIsFinal() {
		return firstIsFinal;
	}

	/**
	 * @param firstIsFinal the firstIsFinal to set
	 */
	public void setFirstIsFinal(Boolean firstIsFinal) {
		this.firstIsFinal = firstIsFinal;
	}

	/**
	 * @return the secondIsFinal
	 */
	public Boolean getSecondIsFinal() {
		return secondIsFinal;
	}

	/**
	 * @param secondIsFinal the secondIsFinal to set
	 */
	public void setSecondIsFinal(Boolean secondIsFinal) {
		this.secondIsFinal = secondIsFinal;
	}

	/**
	 * @return the thirdIsFinal
	 */
	public Boolean getThirdIsFinal() {
		return thirdIsFinal;
	}

	/**
	 * @param thirdIsFinal the thirdIsFinal to set
	 */
	public void setThirdIsFinal(Boolean thirdIsFinal) {
		this.thirdIsFinal = thirdIsFinal;
	}

	/**
	 * @return the liveIsFinal
	 */
	public Boolean getLiveIsFinal() {
		return liveIsFinal;
	}

	/**
	 * @param liveIsFinal the liveIsFinal to set
	 */
	public void setLiveIsFinal(Boolean liveIsFinal) {
		this.liveIsFinal = liveIsFinal;
	}

	/**
	 * 
	 * @return
	 */
	public String search() {
		return teamone.search() + " " + teamtwo.search() + " " + dateofevent + " " + timeofevent;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventPackage [id=" + id + ", teamone=" + teamone + ", teamtwo=" + teamtwo + ", eventdatetime="
				+ eventdatetime + ", dateofevent=" + dateofevent + ", timeofevent=" + timeofevent + ", sporttype="
				+ sporttype + ", linetype=" + linetype + ", eventtype=" + eventtype + ", wagertype=" + wagertype
				+ ", periodType=" + periodType + ", periodNumber=" + periodNumber + ", sportsInsightsEventId="
				+ sportsInsightsEventId + ", gameIsFinal=" + gameIsFinal + ", firstIsFinal=" + firstIsFinal
				+ ", secondIsFinal=" + secondIsFinal + ", thirdIsFinal=" + thirdIsFinal + ", liveIsFinal=" + liveIsFinal
				+ "]";
	}
}