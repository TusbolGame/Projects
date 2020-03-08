/**
 * 
 */
package com.wooanalytics.dao.sites.usatoday;

/**
 * @author jmiller
 *
 */
public class SagarinNcaafData {
	private Float homeadvantage = new Float(0);
	private Integer rank = new Integer(0);
	private String team;
	private Boolean isfbs = new Boolean(true);
	private Float rating = new Float(0);
	private Integer wins = new Integer(0);
	private Integer losses = new Integer(0);
	private Integer ties = new Integer(0);
	private Float schedulestrength = new Float(0);
	private Integer schedulerank = new Integer(0);
	private Integer top10wins = new Integer(0);
	private Integer top10losses = new Integer(0);
	private Integer top10ties = new Integer(0);
	private Integer top30wins = new Integer(0);
	private Integer top30losses = new Integer(0);
	private Integer top30ties = new Integer(0);
	private Float predictor = new Float(0);
	private Integer predictorrank = new Integer(0);
	private Float mean = new Float(0);
	private Integer meanrank = new Integer(0);
	private Float recent = new Float(0);
	private Integer recentrank = new Integer(0);

	/**
	 * 
	 */
	public SagarinNcaafData() {
		super();
	}

	/**
	 * @return the homeadvantage
	 */
	public Float getHomeadvantage() {
		return homeadvantage;
	}

	/**
	 * @param homeadvantage the homeadvantage to set
	 */
	public void setHomeadvantage(Float homeadvantage) {
		this.homeadvantage = homeadvantage;
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
	 * @return the isfbs
	 */
	public Boolean getIsfbs() {
		return isfbs;
	}

	/**
	 * @param isfbs the isfbs to set
	 */
	public void setIsfbs(Boolean isfbs) {
		this.isfbs = isfbs;
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
	 * @return the ties
	 */
	public Integer getTies() {
		return ties;
	}

	/**
	 * @param ties the ties to set
	 */
	public void setTies(Integer ties) {
		this.ties = ties;
	}

	/**
	 * @return the schedulestrength
	 */
	public Float getSchedulestrength() {
		return schedulestrength;
	}

	/**
	 * @param schedulestrength the schedulestrength to set
	 */
	public void setSchedulestrength(Float schedulestrength) {
		this.schedulestrength = schedulestrength;
	}

	/**
	 * @return the schedulerank
	 */
	public Integer getSchedulerank() {
		return schedulerank;
	}

	/**
	 * @param schedulerank the schedulerank to set
	 */
	public void setSchedulerank(Integer schedulerank) {
		this.schedulerank = schedulerank;
	}

	/**
	 * @return the top10wins
	 */
	public Integer getTop10wins() {
		return top10wins;
	}

	/**
	 * @param top10wins the top10wins to set
	 */
	public void setTop10wins(Integer top10wins) {
		this.top10wins = top10wins;
	}

	/**
	 * @return the top10losses
	 */
	public Integer getTop10losses() {
		return top10losses;
	}

	/**
	 * @param top10losses the top10losses to set
	 */
	public void setTop10losses(Integer top10losses) {
		this.top10losses = top10losses;
	}

	/**
	 * @return the top10ties
	 */
	public Integer getTop10ties() {
		return top10ties;
	}

	/**
	 * @param top10ties the top10ties to set
	 */
	public void setTop10ties(Integer top10ties) {
		this.top10ties = top10ties;
	}

	/**
	 * @return the top30wins
	 */
	public Integer getTop30wins() {
		return top30wins;
	}

	/**
	 * @param top30wins the top30wins to set
	 */
	public void setTop30wins(Integer top30wins) {
		this.top30wins = top30wins;
	}

	/**
	 * @return the top30losses
	 */
	public Integer getTop30losses() {
		return top30losses;
	}

	/**
	 * @param top30losses the top30losses to set
	 */
	public void setTop30losses(Integer top30losses) {
		this.top30losses = top30losses;
	}

	/**
	 * @return the top30ties
	 */
	public Integer getTop30ties() {
		return top30ties;
	}

	/**
	 * @param top30ties the top30ties to set
	 */
	public void setTop30ties(Integer top30ties) {
		this.top30ties = top30ties;
	}

	/**
	 * @return the predictor
	 */
	public Float getPredictor() {
		return predictor;
	}

	/**
	 * @param predictor the predictor to set
	 */
	public void setPredictor(Float predictor) {
		this.predictor = predictor;
	}

	/**
	 * @return the predictorrank
	 */
	public Integer getPredictorrank() {
		return predictorrank;
	}

	/**
	 * @param predictorrank the predictorrank to set
	 */
	public void setPredictorrank(Integer predictorrank) {
		this.predictorrank = predictorrank;
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
	 * @return the meanrank
	 */
	public Integer getMeanrank() {
		return meanrank;
	}

	/**
	 * @param meanrank the meanrank to set
	 */
	public void setMeanrank(Integer meanrank) {
		this.meanrank = meanrank;
	}

	/**
	 * @return the recent
	 */
	public Float getRecent() {
		return recent;
	}

	/**
	 * @param recent the recent to set
	 */
	public void setRecent(Float recent) {
		this.recent = recent;
	}

	/**
	 * @return the recentrank
	 */
	public Integer getRecentrank() {
		return recentrank;
	}

	/**
	 * @param recentrank the recentrank to set
	 */
	public void setRecentrank(Integer recentrank) {
		this.recentrank = recentrank;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SagarinNcaafData [homeadvantage=" + homeadvantage + ", rank=" + rank + ", team=" + team + ", isfbs="
				+ isfbs + ", rating=" + rating + ", wins=" + wins + ", losses=" + losses + ", ties=" + ties
				+ ", schedulestrength=" + schedulestrength + ", schedulerank=" + schedulerank + ", top10wins="
				+ top10wins + ", top10losses=" + top10losses + ", top10ties=" + top10ties + ", top30wins=" + top30wins
				+ ", top30losses=" + top30losses + ", top30ties=" + top30ties + ", predictor=" + predictor
				+ ", predictorrank=" + predictorrank + ", mean=" + mean + ", meanrank=" + meanrank + ", recent="
				+ recent + ", recentrank=" + recentrank + "]";
	}
}