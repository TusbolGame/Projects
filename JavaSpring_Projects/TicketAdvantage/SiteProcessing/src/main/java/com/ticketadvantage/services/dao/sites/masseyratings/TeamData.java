package com.ticketadvantage.services.dao.sites.masseyratings;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author jmiller
 *
 */
public class TeamData {
	private String team;
	private List<MasseyRatingsData> results = new ArrayList<MasseyRatingsData>();

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
	 * @return the results
	 */
	public List<MasseyRatingsData> getResults() {
		return results;
	}

	/**
	 * 
	 * @param result
	 */
	public void addResult(MasseyRatingsData result) {
		this.results.add(result);
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List<MasseyRatingsData> results) {
		this.results = results;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TeamData [team=" + team + ", results=" + results + "]";
	}
}