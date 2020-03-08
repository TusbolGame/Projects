package com.ticketadvantage.services.dao.sites.espn;

/**
 * 
 * @author jmiller
 *
 */
public class EspnFootballTeamEfficiencies {
	public Integer rank;
	public String team;
	public Float offense;
	public Float defense;
	public Float specialteams;
	public Float overall;

	/**
	 * 
	 */
	public EspnFootballTeamEfficiencies() {
		super();
	}

	/**
	 * @return the rank
	 */
	public Integer getRank() {
		return rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(Integer rank) {
		this.rank = rank;
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
		this.team = team.toUpperCase();
	}

	/**
	 * @return the offense
	 */
	public Float getOffense() {
		return offense;
	}

	/**
	 * @param offense the offense to set
	 */
	public void setOffense(Float offense) {
		this.offense = offense;
	}

	/**
	 * @return the defense
	 */
	public Float getDefense() {
		return defense;
	}

	/**
	 * @param defense the defense to set
	 */
	public void setDefense(Float defense) {
		this.defense = defense;
	}

	/**
	 * @return the specialteams
	 */
	public Float getSpecialteams() {
		return specialteams;
	}

	/**
	 * @param specialteams the specialteams to set
	 */
	public void setSpecialteams(Float specialteams) {
		this.specialteams = specialteams;
	}

	/**
	 * @return the overall
	 */
	public Float getOverall() {
		return overall;
	}

	/**
	 * @param overall the overall to set
	 */
	public void setOverall(Float overall) {
		this.overall = overall;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EspnFootballTeamEfficiencies [rank=" + rank + ", team=" + team + ", offense=" + offense + ", defense="
				+ defense + ", specialteams=" + specialteams + ", overall=" + overall + "]";
	}
}