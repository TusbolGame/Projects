/**
 * 
 */
package com.ticketadvantage.services.dao.sites.teamrankings;

/**
 * @author jmiller
 *
 */
public class TeamRankingsSos {
	private Integer week;
	private Integer year;
	private String team;
	private Integer rank;
	private Float rating;
	private Integer hi;
	private Integer low;
	private Integer last;

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
	 * @return the rating
	 */
	public Float getRating() {
		return rating;
	}
	/**
	 * @param rating the rating to set
	 */
	public void setRating(Float rating) {
		this.rating = rating;
	}
	/**
	 * @return the hi
	 */
	public Integer getHi() {
		return hi;
	}
	/**
	 * @param hi the hi to set
	 */
	public void setHi(Integer hi) {
		this.hi = hi;
	}
	/**
	 * @return the low
	 */
	public Integer getLow() {
		return low;
	}
	/**
	 * @param low the low to set
	 */
	public void setLow(Integer low) {
		this.low = low;
	}
	/**
	 * @return the last
	 */
	public Integer getLast() {
		return last;
	}
	/**
	 * @param last the last to set
	 */
	public void setLast(Integer last) {
		this.last = last;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TeamRankingsSos [week=" + week + ", year=" + year + ", team=" + team + ", rank=" + rank + ", rating="
				+ rating + ", hi=" + hi + ", low=" + low + ", last=" + last + "]";
	}
}