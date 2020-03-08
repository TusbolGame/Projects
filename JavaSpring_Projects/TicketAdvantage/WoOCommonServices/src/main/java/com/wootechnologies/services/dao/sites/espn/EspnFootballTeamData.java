/**
 * 
 */
package com.wootechnologies.services.dao.sites.espn;

/**
 * @author jmiller
 *
 */
public class EspnFootballTeamData {
	private String team;
	private Integer score;
	private Integer firsthalfscore;
	private Integer secondhalfscore;
	private Integer winrecord;
	private Integer lossrecord;
	private Integer confwinrecord;
	private Integer conflossrecord;
	private Integer ranking;
	private Integer firstdowns;
	private Integer thirddownsuccess;
	private Integer thirddowntotal;
	private Integer fourthdownsuccess;
	private Integer fourthdowntotal;
	private Integer totalyards;
	private Integer passingyards;
	private Integer passcompletions;
	private Integer passattempts;
	private Double yardsperpass;
	private Integer interceptions;
	private Integer rushingyards;
	private Integer rushingattempts;
	private Double yardsperrush;
	private Integer penalties;
	private Integer penaltyyards;
	private Integer turnovers;
	private Integer fumbleslost;
	private Integer interceptionsthrown;
	private Integer possessionminutes;
	private Integer possessionseconds;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
	 * @return the score
	 */
	public Integer getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(Integer score) {
		this.score = score;
	}

	/**
	 * @return the firsthalfscore
	 */
	public Integer getFirsthalfscore() {
		return firsthalfscore;
	}

	/**
	 * @param firsthalfscore the firsthalfscore to set
	 */
	public void setFirsthalfscore(Integer firsthalfscore) {
		this.firsthalfscore = firsthalfscore;
	}

	/**
	 * @return the secondhalfscore
	 */
	public Integer getSecondhalfscore() {
		return secondhalfscore;
	}

	/**
	 * @param secondhalfscore the secondhalfscore to set
	 */
	public void setSecondhalfscore(Integer secondhalfscore) {
		this.secondhalfscore = secondhalfscore;
	}

	/**
	 * @return the winrecord
	 */
	public Integer getWinrecord() {
		return winrecord;
	}

	/**
	 * @param winrecord the winrecord to set
	 */
	public void setWinrecord(Integer winrecord) {
		this.winrecord = winrecord;
	}

	/**
	 * @return the lossrecord
	 */
	public Integer getLossrecord() {
		return lossrecord;
	}

	/**
	 * @param lossrecord the lossrecord to set
	 */
	public void setLossrecord(Integer lossrecord) {
		this.lossrecord = lossrecord;
	}

	/**
	 * @return the confwinrecord
	 */
	public Integer getConfwinrecord() {
		return confwinrecord;
	}

	/**
	 * @param confwinrecord the confwinrecord to set
	 */
	public void setConfwinrecord(Integer confwinrecord) {
		this.confwinrecord = confwinrecord;
	}

	/**
	 * @return the conflossrecord
	 */
	public Integer getConflossrecord() {
		return conflossrecord;
	}

	/**
	 * @param conflossrecord the conflossrecord to set
	 */
	public void setConflossrecord(Integer conflossrecord) {
		this.conflossrecord = conflossrecord;
	}

	/**
	 * @return the ranking
	 */
	public Integer getRanking() {
		return ranking;
	}

	/**
	 * @param ranking the ranking to set
	 */
	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	/**
	 * @return the firstdowns
	 */
	public Integer getFirstdowns() {
		return firstdowns;
	}

	/**
	 * @param firstdowns the firstdowns to set
	 */
	public void setFirstdowns(Integer firstdowns) {
		this.firstdowns = firstdowns;
	}

	/**
	 * @return the thirddownsuccess
	 */
	public Integer getThirddownsuccess() {
		return thirddownsuccess;
	}

	/**
	 * @param thirddownsuccess the thirddownsuccess to set
	 */
	public void setThirddownsuccess(Integer thirddownsuccess) {
		this.thirddownsuccess = thirddownsuccess;
	}

	/**
	 * @return the thirddowntotal
	 */
	public Integer getThirddowntotal() {
		return thirddowntotal;
	}

	/**
	 * @param thirddowntotal the thirddowntotal to set
	 */
	public void setThirddowntotal(Integer thirddowntotal) {
		this.thirddowntotal = thirddowntotal;
	}

	/**
	 * @return the fourthdownsuccess
	 */
	public Integer getFourthdownsuccess() {
		return fourthdownsuccess;
	}

	/**
	 * @param fourthdownsuccess the fourthdownsuccess to set
	 */
	public void setFourthdownsuccess(Integer fourthdownsuccess) {
		this.fourthdownsuccess = fourthdownsuccess;
	}

	/**
	 * @return the fourthdowntotal
	 */
	public Integer getFourthdowntotal() {
		return fourthdowntotal;
	}

	/**
	 * @param fourthdowntotal the fourthdowntotal to set
	 */
	public void setFourthdowntotal(Integer fourthdowntotal) {
		this.fourthdowntotal = fourthdowntotal;
	}

	/**
	 * @return the totalyards
	 */
	public Integer getTotalyards() {
		return totalyards;
	}

	/**
	 * @param totalyards the totalyards to set
	 */
	public void setTotalyards(Integer totalyards) {
		this.totalyards = totalyards;
	}

	/**
	 * @return the passingyards
	 */
	public Integer getPassingyards() {
		return passingyards;
	}

	/**
	 * @param passingyards the passingyards to set
	 */
	public void setPassingyards(Integer passingyards) {
		this.passingyards = passingyards;
	}

	/**
	 * @return the passcompletions
	 */
	public Integer getPasscompletions() {
		return passcompletions;
	}

	/**
	 * @param passcompletions the passcompletions to set
	 */
	public void setPasscompletions(Integer passcompletions) {
		this.passcompletions = passcompletions;
	}

	/**
	 * @return the passattempts
	 */
	public Integer getPassattempts() {
		return passattempts;
	}

	/**
	 * @param passattempts the passattempts to set
	 */
	public void setPassattempts(Integer passattempts) {
		this.passattempts = passattempts;
	}

	/**
	 * @return the yardsperpass
	 */
	public Double getYardsperpass() {
		return yardsperpass;
	}

	/**
	 * @param yardsperpass the yardsperpass to set
	 */
	public void setYardsperpass(Double yardsperpass) {
		this.yardsperpass = yardsperpass;
	}

	/**
	 * @return the interceptions
	 */
	public Integer getInterceptions() {
		return interceptions;
	}

	/**
	 * @param interceptions the interceptions to set
	 */
	public void setInterceptions(Integer interceptions) {
		this.interceptions = interceptions;
	}

	/**
	 * @return the rushingyards
	 */
	public Integer getRushingyards() {
		return rushingyards;
	}

	/**
	 * @param rushingyards the rushingyards to set
	 */
	public void setRushingyards(Integer rushingyards) {
		this.rushingyards = rushingyards;
	}

	/**
	 * @return the rushingattempts
	 */
	public Integer getRushingattempts() {
		return rushingattempts;
	}

	/**
	 * @param rushingattempts the rushingattempts to set
	 */
	public void setRushingattempts(Integer rushingattempts) {
		this.rushingattempts = rushingattempts;
	}

	/**
	 * @return the yardsperrush
	 */
	public Double getYardsperrush() {
		return yardsperrush;
	}

	/**
	 * @param yardsperrush the yardsperrush to set
	 */
	public void setYardsperrush(Double yardsperrush) {
		this.yardsperrush = yardsperrush;
	}

	/**
	 * @return the penalties
	 */
	public Integer getPenalties() {
		return penalties;
	}

	/**
	 * @param penalties the penalties to set
	 */
	public void setPenalties(Integer penalties) {
		this.penalties = penalties;
	}

	/**
	 * @return the penaltyyards
	 */
	public Integer getPenaltyyards() {
		return penaltyyards;
	}

	/**
	 * @param penaltyyards the penaltyyards to set
	 */
	public void setPenaltyyards(Integer penaltyyards) {
		this.penaltyyards = penaltyyards;
	}

	/**
	 * @return the turnovers
	 */
	public Integer getTurnovers() {
		return turnovers;
	}

	/**
	 * @param turnovers the turnovers to set
	 */
	public void setTurnovers(Integer turnovers) {
		this.turnovers = turnovers;
	}

	/**
	 * @return the fumbleslost
	 */
	public Integer getFumbleslost() {
		return fumbleslost;
	}

	/**
	 * @param fumbleslost the fumbleslost to set
	 */
	public void setFumbleslost(Integer fumbleslost) {
		this.fumbleslost = fumbleslost;
	}

	/**
	 * @return the interceptionsthrown
	 */
	public Integer getInterceptionsthrown() {
		return interceptionsthrown;
	}

	/**
	 * @param interceptionsthrown the interceptionsthrown to set
	 */
	public void setInterceptionsthrown(Integer interceptionsthrown) {
		this.interceptionsthrown = interceptionsthrown;
	}

	/**
	 * @return the possessionminutes
	 */
	public Integer getPossessionminutes() {
		return possessionminutes;
	}

	/**
	 * @param possessionminutes the possessionminutes to set
	 */
	public void setPossessionminutes(Integer possessionminutes) {
		this.possessionminutes = possessionminutes;
	}

	/**
	 * @return the possessionseconds
	 */
	public Integer getPossessionseconds() {
		return possessionseconds;
	}

	/**
	 * @param possessionseconds the possessionseconds to set
	 */
	public void setPossessionseconds(Integer possessionseconds) {
		this.possessionseconds = possessionseconds;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EspnFootballTeamData [team=" + team + ", score=" + score + ", firsthalfscore=" + firsthalfscore
				+ ", secondhalfscore=" + secondhalfscore + ", winrecord=" + winrecord + ", lossrecord=" + lossrecord
				+ ", confwinrecord=" + confwinrecord + ", conflossrecord=" + conflossrecord + ", ranking=" + ranking
				+ ", firstdowns=" + firstdowns + ", thirddownsuccess=" + thirddownsuccess + ", thirddowntotal="
				+ thirddowntotal + ", fourthdownsuccess=" + fourthdownsuccess + ", fourthdowntotal=" + fourthdowntotal
				+ ", totalyards=" + totalyards + ", passingyards=" + passingyards + ", passcompletions="
				+ passcompletions + ", passattempts=" + passattempts + ", yardsperpass=" + yardsperpass
				+ ", interceptions=" + interceptions + ", rushingyards=" + rushingyards + ", rushingattempts="
				+ rushingattempts + ", yardsperrush=" + yardsperrush + ", penalties=" + penalties + ", penaltyyards="
				+ penaltyyards + ", turnovers=" + turnovers + ", fumbleslost=" + fumbleslost + ", interceptionsthrown="
				+ interceptionsthrown + ", possessionminutes=" + possessionminutes + ", possessionseconds="
				+ possessionseconds + "]\n";
	}
}