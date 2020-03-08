/**
 * 
 */
package com.ticketadvantage.services.dao.sites.ncaa;

/**
 * @author jmiller
 *
 */
public class NcaaData {
	private String dateOfGameString;
	private String teamOneName;
	private String teamTwoName;
	private String teamOneScore;
	private String teamTwoScore;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the dateOfGameString
	 */
	public String getDateOfGameString() {
		return dateOfGameString;
	}

	/**
	 * @param dateOfGameString the dateOfGameString to set
	 */
	public void setDateOfGameString(String dateOfGameString) {
		this.dateOfGameString = dateOfGameString;
	}

	/**
	 * @return the teamOneName
	 */
	public String getTeamOneName() {
		return teamOneName;
	}

	/**
	 * @param teamOneName the teamOneName to set
	 */
	public void setTeamOneName(String teamOneName) {
		if (teamOneName == null) {
			this.teamOneName = "";
		} else {
			this.teamOneName = teamOneName;
		}
	}

	/**
	 * @return the teamTwoName
	 */
	public String getTeamTwoName() {
		return teamTwoName;
	}

	/**
	 * @param teamTwoName the teamTwoName to set
	 */
	public void setTeamTwoName(String teamTwoName) {
		if (teamTwoName == null) {
			this.teamTwoName = "";
		} else {
			this.teamTwoName = teamTwoName;
		}
	}

	/**
	 * @return the teamOneScore
	 */
	public String getTeamOneScore() {
		return teamOneScore;
	}

	/**
	 * @param teamOneScore the teamOneScore to set
	 */
	public void setTeamOneScore(String teamOneScore) {
		this.teamOneScore = teamOneScore;
	}

	/**
	 * @return the teamTwoScore
	 */
	public String getTeamTwoScore() {
		return teamTwoScore;
	}

	/**
	 * @param teamTwoScore the teamTwoScore to set
	 */
	public void setTeamTwoScore(String teamTwoScore) {
		this.teamTwoScore = teamTwoScore;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NcaaData [dateOfGameString=" + dateOfGameString + ", teamOneName=" + teamOneName + ", teamTwoName="
				+ teamTwoName + ", teamOneScore=" + teamOneScore + ", teamTwoScore=" + teamTwoScore + "]";
	}
}
