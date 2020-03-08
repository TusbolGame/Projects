package com.wooanalytics.dao.sites.masseyratings;

/**
 * 
 * @author jmiller
 *
 */
public class MasseyRatingsRankingsData {
	private Integer week = Integer.valueOf("0");
	private Integer year = Integer.valueOf("0");
	private Integer rank = Integer.valueOf("0");
	private String team = "";
	private String conf = "";
	private Integer wins = Integer.valueOf("0");
	private Integer losses = Integer.valueOf("0");
	private Float mean = Float.valueOf("0");
	private Float median = Float.valueOf("0");
	private Float stDev = Float.valueOf("0");

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
	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
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
		this.team = team;
	}
	/**
	 * @return the conf
	 */
	public String getConf() {
		return conf;
	}
	/**
	 * @param conf the conf to set
	 */
	public void setConf(String conf) {
		this.conf = conf;
	}
	/**
	 * @return the wins
	 */
	public Integer getWins() {
		return wins;
	}
	/**
	 * @param wins the wins to set
	 */
	public void setWins(Integer wins) {
		this.wins = wins;
	}
	/**
	 * @return the losses
	 */
	public Integer getLosses() {
		return losses;
	}
	/**
	 * @param losses the losses to set
	 */
	public void setLosses(Integer losses) {
		this.losses = losses;
	}
	/**
	 * @return the mean
	 */
	public Float getMean() {
		return mean;
	}
	/**
	 * @param mean the mean to set
	 */
	public void setMean(Float mean) {
		this.mean = mean;
	}
	/**
	 * @return the median
	 */
	public Float getMedian() {
		return median;
	}
	/**
	 * @param median the median to set
	 */
	public void setMedian(Float median) {
		this.median = median;
	}
	/**
	 * @return the stDev
	 */
	public Float getStDev() {
		return stDev;
	}
	/**
	 * @param stDev the stDev to set
	 */
	public void setStDev(Float stDev) {
		this.stDev = stDev;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MasseyRatingsRankingsData [week=" + week + ", year=" + year + ", rank=" + rank + ", team=" + team
				+ ", conf=" + conf + ", wins=" + wins + ", losses=" + losses + ", mean=" + mean + ", median=" + median
				+ ", stDev=" + stDev + "]";
	}
}