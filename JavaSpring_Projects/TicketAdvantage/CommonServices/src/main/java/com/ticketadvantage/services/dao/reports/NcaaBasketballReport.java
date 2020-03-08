package com.ticketadvantage.services.dao.reports;

import java.util.List;
import java.util.Map;

import com.ticketadvantage.services.dao.cache.ReportCacheSingleton;
import com.ticketadvantage.services.dao.sites.kenpom.KenPomData;
import com.ticketadvantage.services.dao.sites.teamrankings.TeamRankingsData;
import com.ticketadvantage.services.dao.sites.usatoday.SagarinData;

public class NcaaBasketballReport {
	protected ReportCacheSingleton REPORT_CACHE = ReportCacheSingleton.getREPORT_CACHE();
	protected List<TeamRankingsData> effectiveFGPct;
	protected List<TeamRankingsData> opponentEffectiveFGPct;
	protected List<TeamRankingsData> twoPointPct;
	protected List<TeamRankingsData> opponentTwoPointPct =  null;
	protected List<TeamRankingsData> threePointPct;
	protected List<TeamRankingsData> opponentThreePointPct;
	protected List<TeamRankingsData> freeThrowPct;
	protected List<TeamRankingsData> possessionsPerGame;
	protected List<TeamRankingsData> offensiveEfficiency;
	protected List<TeamRankingsData> blocksPerGame;
	protected List<TeamRankingsData> opponentBlocksPerGame;
	protected List<TeamRankingsData> stealsPerGame;
	protected List<TeamRankingsData> opponentStealsPerGame;
	protected List<TeamRankingsData> offensiveReboundsPerGame;
	protected List<TeamRankingsData> oponentOffensiveReboundsPerGame;
	protected List<TeamRankingsData> assistsPerGame;
	protected List<TeamRankingsData> opponentAssistsPerGame;
	protected List<TeamRankingsData> turnoversPerGame;
	protected List<TeamRankingsData> opponentTurnoversPerGame;
	protected List<TeamRankingsData> personalFoulsPerGame;
	protected List<TeamRankingsData> oppoenentPersonalFoulsPerGame;
	protected List<TeamRankingsData> defensiveReboundsPerGame;
	protected List<TeamRankingsData> opponentDefensiveReboundsPerGame;
	protected List<TeamRankingsData> extraChancesPerGame;
	protected List<TeamRankingsData> scheduleStrengthByOther;
	protected List<TeamRankingsData> fgMadePerGame;
	protected List<TeamRankingsData> fgAttemptedPerGame;
	protected List<TeamRankingsData> threeFgMadePerGame;
	protected List<TeamRankingsData> threeFgAttemptedPerGame;
	protected List<TeamRankingsData> ftMadePerGame;
	protected List<TeamRankingsData> ftAttemptedPerGame;
	protected List<TeamRankingsData> opponentfgMadePerGame;
	protected List<TeamRankingsData> opponentfgAttemptedPerGame;
	protected List<TeamRankingsData> opponentthreeFgMadePerGame;
	protected List<TeamRankingsData> opponentthreeFgAttemptedPerGame;
	protected List<TeamRankingsData> opponentftMadePerGame;
	protected List<TeamRankingsData> opponentftAttemptedPerGame;
	protected List<SagarinData> sagarinRatings;
	protected Map<String, String> masseyComposite;
	protected List<KenPomData> lkpd;
	protected List<KenPomData> nextdaykpd;

	/**
	 * 
	 */
	public NcaaBasketballReport() {
		super();
	}

	/**
	 * @return the effectiveFGPct
	 */
	public List<TeamRankingsData> getEffectiveFGPct() {
		return effectiveFGPct;
	}

	/**
	 * @param effectiveFGPct the effectiveFGPct to set
	 */
	public void setEffectiveFGPct(List<TeamRankingsData> effectiveFGPct) {
		this.effectiveFGPct = effectiveFGPct;
	}

	/**
	 * @return the opponentEffectiveFGPct
	 */
	public List<TeamRankingsData> getOpponentEffectiveFGPct() {
		return opponentEffectiveFGPct;
	}

	/**
	 * @param opponentEffectiveFGPct the opponentEffectiveFGPct to set
	 */
	public void setOpponentEffectiveFGPct(List<TeamRankingsData> opponentEffectiveFGPct) {
		this.opponentEffectiveFGPct = opponentEffectiveFGPct;
	}

	/**
	 * @return the twoPointPct
	 */
	public List<TeamRankingsData> getTwoPointPct() {
		return twoPointPct;
	}

	/**
	 * @param twoPointPct the twoPointPct to set
	 */
	public void setTwoPointPct(List<TeamRankingsData> twoPointPct) {
		this.twoPointPct = twoPointPct;
	}

	/**
	 * @return the opponentTwoPointPct
	 */
	public List<TeamRankingsData> getOpponentTwoPointPct() {
		return opponentTwoPointPct;
	}

	/**
	 * @param opponentTwoPointPct the opponentTwoPointPct to set
	 */
	public void setOpponentTwoPointPct(List<TeamRankingsData> opponentTwoPointPct) {
		this.opponentTwoPointPct = opponentTwoPointPct;
	}

	/**
	 * @return the threePointPct
	 */
	public List<TeamRankingsData> getThreePointPct() {
		return threePointPct;
	}

	/**
	 * @param threePointPct the threePointPct to set
	 */
	public void setThreePointPct(List<TeamRankingsData> threePointPct) {
		this.threePointPct = threePointPct;
	}

	/**
	 * @return the opponentThreePointPct
	 */
	public List<TeamRankingsData> getOpponentThreePointPct() {
		return opponentThreePointPct;
	}

	/**
	 * @param opponentThreePointPct the opponentThreePointPct to set
	 */
	public void setOpponentThreePointPct(List<TeamRankingsData> opponentThreePointPct) {
		this.opponentThreePointPct = opponentThreePointPct;
	}

	/**
	 * @return the freeThrowPct
	 */
	public List<TeamRankingsData> getFreeThrowPct() {
		return freeThrowPct;
	}

	/**
	 * @param freeThrowPct the freeThrowPct to set
	 */
	public void setFreeThrowPct(List<TeamRankingsData> freeThrowPct) {
		this.freeThrowPct = freeThrowPct;
	}

	/**
	 * @return the possessionsPerGame
	 */
	public List<TeamRankingsData> getPossessionsPerGame() {
		return possessionsPerGame;
	}

	/**
	 * @param possessionsPerGame the possessionsPerGame to set
	 */
	public void setPossessionsPerGame(List<TeamRankingsData> possessionsPerGame) {
		this.possessionsPerGame = possessionsPerGame;
	}

	/**
	 * @return the offensiveEfficiency
	 */
	public List<TeamRankingsData> getOffensiveEfficiency() {
		return offensiveEfficiency;
	}

	/**
	 * @param offensiveEfficiency the offensiveEfficiency to set
	 */
	public void setOffensiveEfficiency(List<TeamRankingsData> offensiveEfficiency) {
		this.offensiveEfficiency = offensiveEfficiency;
	}

	/**
	 * @return the blocksPerGame
	 */
	public List<TeamRankingsData> getBlocksPerGame() {
		return blocksPerGame;
	}

	/**
	 * @param blocksPerGame the blocksPerGame to set
	 */
	public void setBlocksPerGame(List<TeamRankingsData> blocksPerGame) {
		this.blocksPerGame = blocksPerGame;
	}

	/**
	 * @return the opponentBlocksPerGame
	 */
	public List<TeamRankingsData> getOpponentBlocksPerGame() {
		return opponentBlocksPerGame;
	}

	/**
	 * @param opponentBlocksPerGame the opponentBlocksPerGame to set
	 */
	public void setOpponentBlocksPerGame(List<TeamRankingsData> opponentBlocksPerGame) {
		this.opponentBlocksPerGame = opponentBlocksPerGame;
	}

	/**
	 * @return the stealsPerGame
	 */
	public List<TeamRankingsData> getStealsPerGame() {
		return stealsPerGame;
	}

	/**
	 * @param stealsPerGame the stealsPerGame to set
	 */
	public void setStealsPerGame(List<TeamRankingsData> stealsPerGame) {
		this.stealsPerGame = stealsPerGame;
	}

	/**
	 * @return the opponentStealsPerGame
	 */
	public List<TeamRankingsData> getOpponentStealsPerGame() {
		return opponentStealsPerGame;
	}

	/**
	 * @param opponentStealsPerGame the opponentStealsPerGame to set
	 */
	public void setOpponentStealsPerGame(List<TeamRankingsData> opponentStealsPerGame) {
		this.opponentStealsPerGame = opponentStealsPerGame;
	}

	/**
	 * @return the offensiveReboundsPerGame
	 */
	public List<TeamRankingsData> getOffensiveReboundsPerGame() {
		return offensiveReboundsPerGame;
	}

	/**
	 * @param offensiveReboundsPerGame the offensiveReboundsPerGame to set
	 */
	public void setOffensiveReboundsPerGame(List<TeamRankingsData> offensiveReboundsPerGame) {
		this.offensiveReboundsPerGame = offensiveReboundsPerGame;
	}

	/**
	 * @return the oponentOffensiveReboundsPerGame
	 */
	public List<TeamRankingsData> getOponentOffensiveReboundsPerGame() {
		return oponentOffensiveReboundsPerGame;
	}

	/**
	 * @param oponentOffensiveReboundsPerGame the oponentOffensiveReboundsPerGame to set
	 */
	public void setOponentOffensiveReboundsPerGame(List<TeamRankingsData> oponentOffensiveReboundsPerGame) {
		this.oponentOffensiveReboundsPerGame = oponentOffensiveReboundsPerGame;
	}

	/**
	 * @return the assistsPerGame
	 */
	public List<TeamRankingsData> getAssistsPerGame() {
		return assistsPerGame;
	}

	/**
	 * @param assistsPerGame the assistsPerGame to set
	 */
	public void setAssistsPerGame(List<TeamRankingsData> assistsPerGame) {
		this.assistsPerGame = assistsPerGame;
	}

	/**
	 * @return the opponentAssistsPerGame
	 */
	public List<TeamRankingsData> getOpponentAssistsPerGame() {
		return opponentAssistsPerGame;
	}

	/**
	 * @param opponentAssistsPerGame the opponentAssistsPerGame to set
	 */
	public void setOpponentAssistsPerGame(List<TeamRankingsData> opponentAssistsPerGame) {
		this.opponentAssistsPerGame = opponentAssistsPerGame;
	}

	/**
	 * @return the turnoversPerGame
	 */
	public List<TeamRankingsData> getTurnoversPerGame() {
		return turnoversPerGame;
	}

	/**
	 * @param turnoversPerGame the turnoversPerGame to set
	 */
	public void setTurnoversPerGame(List<TeamRankingsData> turnoversPerGame) {
		this.turnoversPerGame = turnoversPerGame;
	}

	/**
	 * @return the opponentTurnoversPerGame
	 */
	public List<TeamRankingsData> getOpponentTurnoversPerGame() {
		return opponentTurnoversPerGame;
	}

	/**
	 * @param opponentTurnoversPerGame the opponentTurnoversPerGame to set
	 */
	public void setOpponentTurnoversPerGame(List<TeamRankingsData> opponentTurnoversPerGame) {
		this.opponentTurnoversPerGame = opponentTurnoversPerGame;
	}

	/**
	 * @return the personalFoulsPerGame
	 */
	public List<TeamRankingsData> getPersonalFoulsPerGame() {
		return personalFoulsPerGame;
	}

	/**
	 * @param personalFoulsPerGame the personalFoulsPerGame to set
	 */
	public void setPersonalFoulsPerGame(List<TeamRankingsData> personalFoulsPerGame) {
		this.personalFoulsPerGame = personalFoulsPerGame;
	}

	/**
	 * @return the oppoenentPersonalFoulsPerGame
	 */
	public List<TeamRankingsData> getOppoenentPersonalFoulsPerGame() {
		return oppoenentPersonalFoulsPerGame;
	}

	/**
	 * @param oppoenentPersonalFoulsPerGame the oppoenentPersonalFoulsPerGame to set
	 */
	public void setOppoenentPersonalFoulsPerGame(List<TeamRankingsData> oppoenentPersonalFoulsPerGame) {
		this.oppoenentPersonalFoulsPerGame = oppoenentPersonalFoulsPerGame;
	}

	/**
	 * @return the defensiveReboundsPerGame
	 */
	public List<TeamRankingsData> getDefensiveReboundsPerGame() {
		return defensiveReboundsPerGame;
	}

	/**
	 * @param defensiveReboundsPerGame the defensiveReboundsPerGame to set
	 */
	public void setDefensiveReboundsPerGame(List<TeamRankingsData> defensiveReboundsPerGame) {
		this.defensiveReboundsPerGame = defensiveReboundsPerGame;
	}

	/**
	 * @return the opponentDefensiveReboundsPerGame
	 */
	public List<TeamRankingsData> getOpponentDefensiveReboundsPerGame() {
		return opponentDefensiveReboundsPerGame;
	}

	/**
	 * @param opponentDefensiveReboundsPerGame the opponentDefensiveReboundsPerGame to set
	 */
	public void setOpponentDefensiveReboundsPerGame(List<TeamRankingsData> opponentDefensiveReboundsPerGame) {
		this.opponentDefensiveReboundsPerGame = opponentDefensiveReboundsPerGame;
	}

	/**
	 * @return the extraChancesPerGame
	 */
	public List<TeamRankingsData> getExtraChancesPerGame() {
		return extraChancesPerGame;
	}

	/**
	 * @param extraChancesPerGame the extraChancesPerGame to set
	 */
	public void setExtraChancesPerGame(List<TeamRankingsData> extraChancesPerGame) {
		this.extraChancesPerGame = extraChancesPerGame;
	}

	/**
	 * @return the scheduleStrengthByOther
	 */
	public List<TeamRankingsData> getScheduleStrengthByOther() {
		return scheduleStrengthByOther;
	}

	/**
	 * @param scheduleStrengthByOther the scheduleStrengthByOther to set
	 */
	public void setScheduleStrengthByOther(List<TeamRankingsData> scheduleStrengthByOther) {
		this.scheduleStrengthByOther = scheduleStrengthByOther;
	}

	/**
	 * @return the fgMadePerGame
	 */
	public List<TeamRankingsData> getFgMadePerGame() {
		return fgMadePerGame;
	}

	/**
	 * @param fgMadePerGame the fgMadePerGame to set
	 */
	public void setFgMadePerGame(List<TeamRankingsData> fgMadePerGame) {
		this.fgMadePerGame = fgMadePerGame;
	}

	/**
	 * @return the fgAttemptedPerGame
	 */
	public List<TeamRankingsData> getFgAttemptedPerGame() {
		return fgAttemptedPerGame;
	}

	/**
	 * @param fgAttemptedPerGame the fgAttemptedPerGame to set
	 */
	public void setFgAttemptedPerGame(List<TeamRankingsData> fgAttemptedPerGame) {
		this.fgAttemptedPerGame = fgAttemptedPerGame;
	}

	/**
	 * @return the threeFgMadePerGame
	 */
	public List<TeamRankingsData> getThreeFgMadePerGame() {
		return threeFgMadePerGame;
	}

	/**
	 * @param threeFgMadePerGame the threeFgMadePerGame to set
	 */
	public void setThreeFgMadePerGame(List<TeamRankingsData> threeFgMadePerGame) {
		this.threeFgMadePerGame = threeFgMadePerGame;
	}

	/**
	 * @return the threeFgAttemptedPerGame
	 */
	public List<TeamRankingsData> getThreeFgAttemptedPerGame() {
		return threeFgAttemptedPerGame;
	}

	/**
	 * @param threeFgAttemptedPerGame the threeFgAttemptedPerGame to set
	 */
	public void setThreeFgAttemptedPerGame(List<TeamRankingsData> threeFgAttemptedPerGame) {
		this.threeFgAttemptedPerGame = threeFgAttemptedPerGame;
	}

	/**
	 * @return the ftMadePerGame
	 */
	public List<TeamRankingsData> getFtMadePerGame() {
		return ftMadePerGame;
	}

	/**
	 * @param ftMadePerGame the ftMadePerGame to set
	 */
	public void setFtMadePerGame(List<TeamRankingsData> ftMadePerGame) {
		this.ftMadePerGame = ftMadePerGame;
	}

	/**
	 * @return the ftAttemptedPerGame
	 */
	public List<TeamRankingsData> getFtAttemptedPerGame() {
		return ftAttemptedPerGame;
	}

	/**
	 * @param ftAttemptedPerGame the ftAttemptedPerGame to set
	 */
	public void setFtAttemptedPerGame(List<TeamRankingsData> ftAttemptedPerGame) {
		this.ftAttemptedPerGame = ftAttemptedPerGame;
	}

	/**
	 * @return the opponentfgMadePerGame
	 */
	public List<TeamRankingsData> getOpponentfgMadePerGame() {
		return opponentfgMadePerGame;
	}

	/**
	 * @param opponentfgMadePerGame the opponentfgMadePerGame to set
	 */
	public void setOpponentfgMadePerGame(List<TeamRankingsData> opponentfgMadePerGame) {
		this.opponentfgMadePerGame = opponentfgMadePerGame;
	}

	/**
	 * @return the opponentfgAttemptedPerGame
	 */
	public List<TeamRankingsData> getOpponentfgAttemptedPerGame() {
		return opponentfgAttemptedPerGame;
	}

	/**
	 * @param opponentfgAttemptedPerGame the opponentfgAttemptedPerGame to set
	 */
	public void setOpponentfgAttemptedPerGame(List<TeamRankingsData> opponentfgAttemptedPerGame) {
		this.opponentfgAttemptedPerGame = opponentfgAttemptedPerGame;
	}

	/**
	 * @return the opponentthreeFgMadePerGame
	 */
	public List<TeamRankingsData> getOpponentthreeFgMadePerGame() {
		return opponentthreeFgMadePerGame;
	}

	/**
	 * @param opponentthreeFgMadePerGame the opponentthreeFgMadePerGame to set
	 */
	public void setOpponentthreeFgMadePerGame(List<TeamRankingsData> opponentthreeFgMadePerGame) {
		this.opponentthreeFgMadePerGame = opponentthreeFgMadePerGame;
	}

	/**
	 * @return the opponentthreeFgAttemptedPerGame
	 */
	public List<TeamRankingsData> getOpponentthreeFgAttemptedPerGame() {
		return opponentthreeFgAttemptedPerGame;
	}

	/**
	 * @param opponentthreeFgAttemptedPerGame the opponentthreeFgAttemptedPerGame to set
	 */
	public void setOpponentthreeFgAttemptedPerGame(List<TeamRankingsData> opponentthreeFgAttemptedPerGame) {
		this.opponentthreeFgAttemptedPerGame = opponentthreeFgAttemptedPerGame;
	}

	/**
	 * @return the opponentftMadePerGame
	 */
	public List<TeamRankingsData> getOpponentftMadePerGame() {
		return opponentftMadePerGame;
	}

	/**
	 * @param opponentftMadePerGame the opponentftMadePerGame to set
	 */
	public void setOpponentftMadePerGame(List<TeamRankingsData> opponentftMadePerGame) {
		this.opponentftMadePerGame = opponentftMadePerGame;
	}

	/**
	 * @return the opponentftAttemptedPerGame
	 */
	public List<TeamRankingsData> getOpponentftAttemptedPerGame() {
		return opponentftAttemptedPerGame;
	}

	/**
	 * @param opponentftAttemptedPerGame the opponentftAttemptedPerGame to set
	 */
	public void setOpponentftAttemptedPerGame(List<TeamRankingsData> opponentftAttemptedPerGame) {
		this.opponentftAttemptedPerGame = opponentftAttemptedPerGame;
	}

	/**
	 * @return the sagarinRatings
	 */
	public List<SagarinData> getSagarinRatings() {
		return sagarinRatings;
	}

	/**
	 * @param sagarinRatings the sagarinRatings to set
	 */
	public void setSagarinRatings(List<SagarinData> sagarinRatings) {
		this.sagarinRatings = sagarinRatings;
	}

	/**
	 * @return the lkpd
	 */
	public List<KenPomData> getLkpd() {
		return lkpd;
	}

	/**
	 * @param lkpd the lkpd to set
	 */
	public void setLkpd(List<KenPomData> lkpd) {
		this.lkpd = lkpd;
	}

	/**
	 * @return the nextdaykpd
	 */
	public List<KenPomData> getNextdaykpd() {
		return nextdaykpd;
	}

	/**
	 * @param nextdaykpd the nextdaykpd to set
	 */
	public void setNextdaykpd(List<KenPomData> nextdaykpd) {
		this.nextdaykpd = nextdaykpd;
	}

	/**
	 * @return the masseyComposite
	 */
	public Map<String, String> getMasseyComposite() {
		return masseyComposite;
	}

	/**
	 * @param masseyComposite the masseyComposite to set
	 */
	public void setMasseyComposite(Map<String, String> masseyComposite) {
		this.masseyComposite = masseyComposite;
	}

	/**
	 * @return the rEPORT_CACHE
	 */
	public ReportCacheSingleton getREPORT_CACHE() {
		return REPORT_CACHE;
	}
}