/**
 * 
 */
package com.wooanalytics.dao.sites.usatoday;

/**
 * @author jmiller
 *
 */
public class SagarinData {
	private int rank;
	private String team;
	private float value;

	/**
	 * 
	 */
	public SagarinData() {
		super();
	}

	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank) {
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
		this.team = team;
	}

	/**
	 * @return the value
	 */
	public float getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(float value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SagarinData [rank=" + rank + ", team=" + team + ", value=" + value + "]";
	}
}
