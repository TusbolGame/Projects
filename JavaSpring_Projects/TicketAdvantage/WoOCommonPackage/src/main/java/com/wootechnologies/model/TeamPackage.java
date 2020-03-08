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

import org.apache.log4j.Logger;

/**
 * @author jmiller
 *
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class TeamPackage implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(TeamPackage.class);
	private static final long serialVersionUID = 1L;

	@XmlElement
	private Integer id;

	@XmlElement
	private Date eventdatetime;

	@XmlElement
	private String dateofevent;

	@XmlElement
	private String timeofevent;

	@XmlElement
	private String eventid;
	
	@XmlElement
	private String team;

	@XmlElement
	private String spread;

	@XmlElement
	private String spreadindicatorone;
	
	@XmlElement
	private String spreadindicatortwo;

	@XmlElement
	private String spreadvalueone;

	@XmlElement
	private String spreadvaluetwo;

	@XmlElement
	private String spreadjuiceindicatorone;

	@XmlElement
	private String spreadjuiceindicatortwo;

	@XmlElement
	private String spreadjuicevalueone;

	@XmlElement
	private String spreadjuicevaluetwo;

	@XmlElement
	private String total;

	@XmlElement
	private String totalvalueone;

	@XmlElement
	private String totalvaluetwo;

	@XmlElement
	private String totaljuiceindicatorone;
	
	@XmlElement
	private String totaljuiceindicatortwo;

	@XmlElement
	private String totaljuicevalueone;

	@XmlElement
	private String totaljuicevaluetwo;

	@XmlElement
	private String mline;

	@XmlElement
	private String mlinejuiceindicatorone;

	@XmlElement
	private String mlinejuiceindicatortwo;

	@XmlElement
	private String mlinejuiceone;

	@XmlElement
	private String mlinejuicetwo;

	@XmlElement
	private Integer gameScore;
	
	@XmlElement
	private Integer firstScore;

	@XmlElement
	private Integer secondScore;

	@XmlElement
	private Integer thirdScore;

	@XmlElement
	private Integer liveScore;

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

	@XmlElement
	private String pitcher;

	@XmlElement
	private String teamshort;

	@Deprecated
	@XmlElement
	private Integer score;	

	/**
	 * 
	 */
	public TeamPackage() {
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
	 * @return the eventdatetime
	 */
	public Date getEventdatetime() {
		return eventdatetime;
	}

	/**
	 * @param eventdatetime the eventDate to set
	 */
	public void setEventdatetime(Date eventdatetime) {
		this.eventdatetime = eventdatetime;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	 * @return the eventid
	 */
	public String getEventid() {
		return eventid;
	}


	/**
	 * @param eventid the eventid to set
	 */
	public void setEventid(String eventid) {
		this.eventid = eventid;
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
	 * @return the spread
	 */
	public String getSpread() {
		return spread;
	}


	/**
	 * @param spread the spread to set
	 */
	public void setSpread(String spread) {
		this.spread = spread;
	}


	/**
	 * @return the spreadindicatorone
	 */
	public String getSpreadindicatorone() {
		return spreadindicatorone;
	}


	/**
	 * @param spreadindicatorone the spreadindicatorone to set
	 */
	public void setSpreadindicatorone(String spreadindicatorone) {
		this.spreadindicatorone = spreadindicatorone;
	}


	/**
	 * @return the spreadindicatortwo
	 */
	public String getSpreadindicatortwo() {
		return spreadindicatortwo;
	}


	/**
	 * @param spreadindicatortwo the spreadindicatortwo to set
	 */
	public void setSpreadindicatortwo(String spreadindicatortwo) {
		this.spreadindicatortwo = spreadindicatortwo;
	}


	/**
	 * @return the spreadvalueone
	 */
	public String getSpreadvalueone() {
		return spreadvalueone;
	}


	/**
	 * @param spreadvalueone the spreadvalueone to set
	 */
	public void setSpreadvalueone(String spreadvalueone) {
		this.spreadvalueone = spreadvalueone;
	}


	/**
	 * @return the spreadvaluetwo
	 */
	public String getSpreadvaluetwo() {
		return spreadvaluetwo;
	}


	/**
	 * @param spreadvaluetwo the spreadvaluetwo to set
	 */
	public void setSpreadvaluetwo(String spreadvaluetwo) {
		this.spreadvaluetwo = spreadvaluetwo;
	}


	/**
	 * @return the spreadjuiceindicatorone
	 */
	public String getSpreadjuiceindicatorone() {
		return spreadjuiceindicatorone;
	}


	/**
	 * @param spreadjuiceindicatorone the spreadjuiceindicatorone to set
	 */
	public void setSpreadjuiceindicatorone(String spreadjuiceindicatorone) {
		this.spreadjuiceindicatorone = spreadjuiceindicatorone;
	}


	/**
	 * @return the spreadjuiceindicatortwo
	 */
	public String getSpreadjuiceindicatortwo() {
		return spreadjuiceindicatortwo;
	}


	/**
	 * @param spreadjuiceindicatortwo the spreadjuiceindicatortwo to set
	 */
	public void setSpreadjuiceindicatortwo(String spreadjuiceindicatortwo) {
		this.spreadjuiceindicatortwo = spreadjuiceindicatortwo;
	}


	/**
	 * @return the spreadjuicevalueone
	 */
	public String getSpreadjuicevalueone() {
		return spreadjuicevalueone;
	}


	/**
	 * @param spreadjuicevalueone the spreadjuicevalueone to set
	 */
	public void setSpreadjuicevalueone(String spreadjuicevalueone) {
		this.spreadjuicevalueone = spreadjuicevalueone;
	}


	/**
	 * @return the spreadjuicevaluetwo
	 */
	public String getSpreadjuicevaluetwo() {
		return spreadjuicevaluetwo;
	}


	/**
	 * @param spreadjuicevaluetwo the spreadjuicevaluetwo to set
	 */
	public void setSpreadjuicevaluetwo(String spreadjuicevaluetwo) {
		this.spreadjuicevaluetwo = spreadjuicevaluetwo;
	}


	/**
	 * @return the total
	 */
	public String getTotal() {
		return total;
	}


	/**
	 * @param total the total to set
	 */
	public void setTotal(String total) {
		this.total = total;
	}


	/**
	 * @return the totalvalueone
	 */
	public String getTotalvalueone() {
		return totalvalueone;
	}


	/**
	 * @param totalvalueone the totalvalueone to set
	 */
	public void setTotalvalueone(String totalvalueone) {
		this.totalvalueone = totalvalueone;
	}


	/**
	 * @return the totalvaluetwo
	 */
	public String getTotalvaluetwo() {
		return totalvaluetwo;
	}


	/**
	 * @param totalvaluetwo the totalvaluetwo to set
	 */
	public void setTotalvaluetwo(String totalvaluetwo) {
		this.totalvaluetwo = totalvaluetwo;
	}


	/**
	 * @return the totaljuiceindicatorone
	 */
	public String getTotaljuiceindicatorone() {
		return totaljuiceindicatorone;
	}


	/**
	 * @param totaljuiceindicatorone the totaljuiceindicatorone to set
	 */
	public void setTotaljuiceindicatorone(String totaljuiceindicatorone) {
		this.totaljuiceindicatorone = totaljuiceindicatorone;
	}


	/**
	 * @return the totaljuiceindicatortwo
	 */
	public String getTotaljuiceindicatortwo() {
		return totaljuiceindicatortwo;
	}


	/**
	 * @param totaljuiceindicatortwo the totaljuiceindicatortwo to set
	 */
	public void setTotaljuiceindicatortwo(String totaljuiceindicatortwo) {
		this.totaljuiceindicatortwo = totaljuiceindicatortwo;
	}


	/**
	 * @return the totaljuicevalueone
	 */
	public String getTotaljuicevalueone() {
		return totaljuicevalueone;
	}


	/**
	 * @param totaljuicevalueone the totaljuicevalueone to set
	 */
	public void setTotaljuicevalueone(String totaljuicevalueone) {
		this.totaljuicevalueone = totaljuicevalueone;
	}


	/**
	 * @return the totaljuicevaluetwo
	 */
	public String getTotaljuicevaluetwo() {
		return totaljuicevaluetwo;
	}


	/**
	 * @param totaljuicevaluetwo the totaljuicevaluetwo to set
	 */
	public void setTotaljuicevaluetwo(String totaljuicevaluetwo) {
		this.totaljuicevaluetwo = totaljuicevaluetwo;
	}


	/**
	 * @return the mline
	 */
	public String getMline() {
		return mline;
	}


	/**
	 * @param mline the mline to set
	 */
	public void setMline(String mline) {
		this.mline = mline;
	}


	/**
	 * @return the mlinejuiceindicatorone
	 */
	public String getMlinejuiceindicatorone() {
		return mlinejuiceindicatorone;
	}


	/**
	 * @param mlinejuiceindicatorone the mlinejuiceindicatorone to set
	 */
	public void setMlinejuiceindicatorone(String mlinejuiceindicatorone) {
		this.mlinejuiceindicatorone = mlinejuiceindicatorone;
	}

	/**
	 * @return the mlinejuiceindicatortwo
	 */
	public String getMlinejuiceindicatortwo() {
		return mlinejuiceindicatortwo;
	}


	/**
	 * @param mlinejuiceindicatortwo the mlinejuiceindicatortwo to set
	 */
	public void setMlinejuiceindicatortwo(String mlinejuiceindicatortwo) {
		this.mlinejuiceindicatortwo = mlinejuiceindicatortwo;
	}


	/**
	 * @return the mlinejuiceone
	 */
	public String getMlinejuiceone() {
		return mlinejuiceone;
	}


	/**
	 * @param mlinejuiceone the mlinejuiceone to set
	 */
	public void setMlinejuiceone(String mlinejuiceone) {
		this.mlinejuiceone = mlinejuiceone;
	}


	/**
	 * @return the mlinejuicetwo
	 */
	public String getMlinejuicetwo() {
		return mlinejuicetwo;
	}


	/**
	 * @param mlinejuicetwo the mlinejuicetwo to set
	 */
	public void setMlinejuicetwo(String mlinejuicetwo) {
		this.mlinejuicetwo = mlinejuicetwo;
	}

	/**
	 * @return the gameScore
	 */
	public Integer getGameScore() {
		return gameScore;
	}

	/**
	 * @param gameScore the gameScore to set
	 */
	public void setGameScore(Integer gameScore) {
		this.gameScore = gameScore;
	}

	/**
	 * @return the firstScore
	 */
	public Integer getFirstScore() {
		return firstScore;
	}

	/**
	 * @param firstScore the firstScore to set
	 */
	public void setFirstScore(Integer firstScore) {
		this.firstScore = firstScore;
	}

	/**
	 * @return the secondScore
	 */
	public Integer getSecondScore() {
		return secondScore;
	}

	/**
	 * @param secondScore the secondScore to set
	 */
	public void setSecondScore(Integer secondScore) {
		this.secondScore = secondScore;
	}

	/**
	 * @return the thirdScore
	 */
	public Integer getThirdScore() {
		return thirdScore;
	}

	/**
	 * @param thirdScore the thirdScore to set
	 */
	public void setThirdScore(Integer thirdScore) {
		this.thirdScore = thirdScore;
	}

	/**
	 * @return the liveScore
	 */
	public Integer getLiveScore() {
		return liveScore;
	}

	/**
	 * @param liveScore the liveScore to set
	 */
	public void setLiveScore(Integer liveScore) {
		this.liveScore = liveScore;
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
		return eventid + " " + team;
	}

	/**
	 * @return the score
	 */
	@Deprecated
	public Integer getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	@Deprecated
	public void setScore(Integer score) {
		this.score = score;
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
	 * @return the teamshort
	 */
	public String getTeamshort() {
		return teamshort;
	}

	/**
	 * @param teamshort the teamshort to set
	 */
	public void setTeamshort(String teamshort) {
		this.teamshort = teamshort;
	}

	/**
	 * 
	 * @param pitcher
	 * @return
	 */
	public boolean findPitcher(String thePitcher) {
		LOGGER.info("Entering findPitcher()");
		LOGGER.debug("thePitcher: " + thePitcher);
		LOGGER.debug("this.pitcher: " + this.pitcher);

		boolean retValue = false;
		if (thePitcher.length() > 0 && thePitcher.length() == this.pitcher.length()) {
			if (thePitcher.equalsIgnoreCase(this.pitcher)) {
				LOGGER.debug("Found the pitcher");
				retValue = true;
			} else {
				// now check the first 3
				String firstThree = thePitcher.substring(0, 3);
				String firstThree2 = this.pitcher.substring(0, 3);
				LOGGER.debug("firstThree: " + firstThree);
				LOGGER.debug("firstThree2: " + firstThree2);
				if (firstThree.equalsIgnoreCase(firstThree2)) {
					LOGGER.debug("Found the pitcher");
					retValue = true;
				} else {
					// Now try to replace numbers with letters
					String tempPitcher = this.pitcher.replace("3", "E");
					LOGGER.debug("tempPitcher: " + tempPitcher);
					if (thePitcher.equalsIgnoreCase(tempPitcher)) {
						LOGGER.debug("Found the pitcher");
						retValue = true;
					} else {
						tempPitcher = tempPitcher.replace("0", "O");
						if (thePitcher.equalsIgnoreCase(tempPitcher)) {
							LOGGER.debug("Found the pitcher");
							retValue = true;
						} else {
							tempPitcher = tempPitcher.replace("+", "T");
							if (thePitcher.equalsIgnoreCase(tempPitcher)) {
								LOGGER.debug("Found the pitcher");
								retValue = true;
							} else {
								tempPitcher = tempPitcher.replace("1", "I");
								if (thePitcher.equalsIgnoreCase(tempPitcher)) {
									LOGGER.debug("Found the pitcher");
									retValue = true;
								}
							}
						}
					}
				}
			}
		}

		LOGGER.info("Exiting findPitcher()");
		return retValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TeamPackage [id=" + id + ", eventdatetime=" + eventdatetime + ", dateofevent=" + dateofevent
				+ ", timeofevent=" + timeofevent + ", eventid=" + eventid + ", team=" + team + ", spread=" + spread
				+ ", spreadindicatorone=" + spreadindicatorone + ", spreadindicatortwo=" + spreadindicatortwo
				+ ", spreadvalueone=" + spreadvalueone + ", spreadvaluetwo=" + spreadvaluetwo
				+ ", spreadjuiceindicatorone=" + spreadjuiceindicatorone + ", spreadjuiceindicatortwo="
				+ spreadjuiceindicatortwo + ", spreadjuicevalueone=" + spreadjuicevalueone + ", spreadjuicevaluetwo="
				+ spreadjuicevaluetwo + ", total=" + total + ", totalvalueone=" + totalvalueone + ", totalvaluetwo="
				+ totalvaluetwo + ", totaljuiceindicatorone=" + totaljuiceindicatorone + ", totaljuiceindicatortwo="
				+ totaljuiceindicatortwo + ", totaljuicevalueone=" + totaljuicevalueone + ", totaljuicevaluetwo="
				+ totaljuicevaluetwo + ", mline=" + mline + ", mlinejuiceindicatorone=" + mlinejuiceindicatorone
				+ ", mlinejuiceindicatortwo=" + mlinejuiceindicatortwo + ", mlinejuiceone=" + mlinejuiceone
				+ ", mlinejuicetwo=" + mlinejuicetwo + ", gameScore=" + gameScore + ", firstScore=" + firstScore
				+ ", secondScore=" + secondScore + ", thirdScore=" + thirdScore + ", liveScore=" + liveScore
				+ ", gameIsFinal=" + gameIsFinal + ", firstIsFinal=" + firstIsFinal + ", secondIsFinal=" + secondIsFinal
				+ ", thirdIsFinal=" + thirdIsFinal + ", liveIsFinal=" + liveIsFinal + ", pitcher=" + pitcher
				+ ", teamshort=" + teamshort + ", score=" + score + "]";
	}
}