package com.wootechnologies.services.dao.sites.espn;

/**
 * 
 * @author jmiller
 *
 */
public class EspnFootballPowerIndex {
	public Integer rank;
	public String team;
	public Float wins;
	public Float losses;
	public Float winout;
	public Float confwin;
	public Integer remainsos;
	public Float fpi;

	/**
	 * 
	 */
	public EspnFootballPowerIndex() {
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
	 * @return the wins
	 */
	public Float getWins() {
		return wins;
	}

	/**
	 * @param wins the wins to set
	 */
	public void setWins(Float wins) {
		this.wins = wins;
	}

	/**
	 * @return the losses
	 */
	public Float getLosses() {
		return losses;
	}

	/**
	 * @param losses the losses to set
	 */
	public void setLosses(Float losses) {
		this.losses = losses;
	}

	/**
	 * @return the winout
	 */
	public Float getWinout() {
		return winout;
	}

	/**
	 * @param winout the winout to set
	 */
	public void setWinout(Float winout) {
		this.winout = winout;
	}

	/**
	 * @return the confwin
	 */
	public Float getConfwin() {
		return confwin;
	}

	/**
	 * @param confwin the confwin to set
	 */
	public void setConfwin(Float confwin) {
		this.confwin = confwin;
	}

	/**
	 * @return the remainsos
	 */
	public Integer getRemainsos() {
		return remainsos;
	}

	/**
	 * @param remainsos the remainsos to set
	 */
	public void setRemainsos(Integer remainsos) {
		this.remainsos = remainsos;
	}

	/**
	 * @return the fpi
	 */
	public Float getFpi() {
		return fpi;
	}

	/**
	 * @param fpi the fpi to set
	 */
	public void setFpi(Float fpi) {
		this.fpi = fpi;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EspnFootballPowerIndex [rank=" + rank + ", team=" + team + ", wins=" + wins + ", losses=" + losses
				+ ", winout=" + winout + ", confwin=" + confwin + ", remainsos=" + remainsos + ", fpi=" + fpi + "]";
	}
}
