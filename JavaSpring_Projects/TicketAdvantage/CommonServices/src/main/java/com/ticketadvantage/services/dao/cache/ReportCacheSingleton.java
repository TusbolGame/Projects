/**
 * 
 */
package com.ticketadvantage.services.dao.cache;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.kenpom.KenPomData;
import com.ticketadvantage.services.dao.sites.kenpom.KenPomSite;
import com.ticketadvantage.services.dao.sites.masseyratings.MasseyRatingsSite;
import com.ticketadvantage.services.dao.sites.teamrankings.TeamRankingsData;
import com.ticketadvantage.services.dao.sites.teamrankings.TeamRankingsSite;
import com.ticketadvantage.services.dao.sites.usatoday.SagarinData;
import com.ticketadvantage.services.dao.sites.usatoday.UsaTodaySite;

/**
 * @author jmiller
 *
 */
public class ReportCacheSingleton implements Runnable {
	private final static Logger LOGGER = Logger.getLogger(ReportCacheSingleton.class);
	private static ReportCacheSingleton REPORT_CACHE;
	private List<TeamRankingsData> effectiveFGPct = null;
	private List<TeamRankingsData> opponentEffectiveFGPct = null;
	private List<TeamRankingsData> twoPointPct = null;
	private List<TeamRankingsData> opponentTwoPointPct =  null;
	private List<TeamRankingsData> threePointPct = null;
	private List<TeamRankingsData> opponentThreePointPct = null;
	private List<TeamRankingsData> freeThrowPct = null;
	private List<TeamRankingsData> possessionsPerGame = null;
	private List<TeamRankingsData> offensiveEfficiency = null;
	private List<TeamRankingsData> blocksPerGame = null;
	private List<TeamRankingsData> opponentBlocksPerGame = null;
	private List<TeamRankingsData> stealsPerGame = null;
	private List<TeamRankingsData> opponentStealsPerGame = null;
	private List<TeamRankingsData> offensiveReboundsPerGame = null;
	private List<TeamRankingsData> oponentOffensiveReboundsPerGame = null;
	private List<TeamRankingsData> assistsPerGame = null;
	private List<TeamRankingsData> opponentAssistsPerGame = null;
	private List<TeamRankingsData> turnoversPerGame = null;
	private List<TeamRankingsData> opponentTurnoversPerGame = null;
	private List<TeamRankingsData> personalFoulsPerGame = null;
	private List<TeamRankingsData> oppoenentPersonalFoulsPerGame = null;
	private List<TeamRankingsData> defensiveReboundsPerGame = null;
	private 	List<TeamRankingsData> opponentDefensiveReboundsPerGame = null;
	private List<TeamRankingsData> extraChancesPerGame = null;
	private List<TeamRankingsData> scheduleStrengthByOther = null;
	private List<TeamRankingsData> fgMadePerGame = null;
	private List<TeamRankingsData> fgAttemptedPerGame = null;
	private List<TeamRankingsData> threeFgMadePerGame = null;
	private List<TeamRankingsData> threeFgAttemptedPerGame = null;
	private List<TeamRankingsData> ftMadePerGame = null;
	private List<TeamRankingsData> ftAttemptedPerGame = null;
	private List<TeamRankingsData> opponentfgMadePerGame = null;
	private List<TeamRankingsData> opponentfgAttemptedPerGame = null;
	private List<TeamRankingsData> opponentthreeFgMadePerGame = null;
	private List<TeamRankingsData> opponentthreeFgAttemptedPerGame = null;
	private List<TeamRankingsData> opponentftMadePerGame = null;
	private List<TeamRankingsData> opponentftAttemptedPerGame = null;
	private List<SagarinData> sagarinRatings = null;
	private List<KenPomData> lkpd = null;
	private List<KenPomData> nextdaykpd = null;
	private Map<String, String> masseyComposite = null;
	private final TeamRankingsSite processSite = new TeamRankingsSite("https://www.teamrankings.com/", "", "");
	private final UsaTodaySite usaTodaySite = new UsaTodaySite("https://www.usatoday.com/", "", "");
	private final KenPomSite kps = new KenPomSite("https://kenpom.com", "William.Pottle1@gmail.com", "Montross");
	private final MasseyRatingsSite masseyRatingsSite = new MasseyRatingsSite("https://www.masseyratings.com/", "", "");
	private boolean loaded;

	static {
		REPORT_CACHE = new ReportCacheSingleton();

		try {
			REPORT_CACHE.processSite.getHttpClientWrapper().setupHttpClient("None");
			REPORT_CACHE.usaTodaySite.getHttpClientWrapper().setupHttpClient("None");
			REPORT_CACHE.kps.getHttpClientWrapper().setupHttpClient("None");
			REPORT_CACHE.masseyRatingsSite.getHttpClientWrapper().setupHttpClient("None");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			System.exit(1);
		}
	}

	/**
	 * 
	 */
	private ReportCacheSingleton() {
		super();
		new Thread(this).start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the rEPORT_CACHE
	 */
	public static ReportCacheSingleton getREPORT_CACHE() {
		return REPORT_CACHE;
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
	 * @return the kps
	 */
	public KenPomSite getKps() {
		return kps;
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
	 * @return the loaded
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * @param loaded the loaded to set
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	/**
	 * 
	 */
	private void updateCache() {
/*
		while (true) {
			try {
				REPORT_CACHE.lkpd = kps.getNcaabScheduleNextTwoDays();
				REPORT_CACHE.nextdaykpd = kps.getNcaabScheduleNextDay();
				REPORT_CACHE.effectiveFGPct = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/effective-field-goal-pct");
				REPORT_CACHE.opponentEffectiveFGPct = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/opponent-effective-field-goal-pct");
				REPORT_CACHE.twoPointPct = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/two-point-pct");
				REPORT_CACHE.opponentTwoPointPct = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/opponent-two-point-pct");
				REPORT_CACHE.threePointPct = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/three-point-pct");
				REPORT_CACHE.opponentThreePointPct = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/opponent-three-point-pct");
				REPORT_CACHE.freeThrowPct = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/free-throw-pct");
				REPORT_CACHE.possessionsPerGame = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/possessions-per-game");
				REPORT_CACHE.offensiveEfficiency = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/offensive-efficiency");
				REPORT_CACHE.blocksPerGame = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/blocks-per-game");
				REPORT_CACHE.opponentBlocksPerGame = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/opponent-blocks-per-game");
				REPORT_CACHE.stealsPerGame = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/steals-per-game");
				REPORT_CACHE.opponentStealsPerGame = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/opponent-steals-per-game");
				REPORT_CACHE.offensiveReboundsPerGame = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/offensive-rebounds-per-game");
				REPORT_CACHE.oponentOffensiveReboundsPerGame = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/opponent-offensive-rebounds-per-game");
				REPORT_CACHE.assistsPerGame = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/assists-per-game");
				REPORT_CACHE.opponentAssistsPerGame = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/opponent-assists-per-game");
				REPORT_CACHE.turnoversPerGame = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/turnovers-per-game");
				REPORT_CACHE.opponentTurnoversPerGame = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/opponent-turnovers-per-game");
				REPORT_CACHE.personalFoulsPerGame = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/personal-fouls-per-game");
				REPORT_CACHE.oppoenentPersonalFoulsPerGame = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/opponent-personal-fouls-per-game");
				REPORT_CACHE.defensiveReboundsPerGame = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/defensive-rebounds-per-game");
				REPORT_CACHE.opponentDefensiveReboundsPerGame = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/opponent-defensive-rebounds-per-game");
				REPORT_CACHE.extraChancesPerGame = processSite
						.getData("https://www.teamrankings.com/ncaa-basketball/stat/extra-chances-per-game");
				REPORT_CACHE.scheduleStrengthByOther = processSite.getStrengthOfSchedule(
						"https://www.teamrankings.com/ncaa-basketball/ranking/schedule-strength-by-other");
				REPORT_CACHE.fgMadePerGame = processSite.getData(
						"https://www.teamrankings.com/ncaa-basketball/stat/field-goals-made-per-game");
				REPORT_CACHE.fgAttemptedPerGame = processSite.getData(
						"https://www.teamrankings.com/ncaa-basketball/stat/field-goals-attempted-per-game");
				REPORT_CACHE.threeFgMadePerGame = processSite.getData(
						"https://www.teamrankings.com/ncaa-basketball/stat/three-pointers-made-per-game");
				REPORT_CACHE.threeFgAttemptedPerGame = processSite.getData(
						"https://www.teamrankings.com/ncaa-basketball/stat/three-pointers-attempted-per-game");
				REPORT_CACHE.ftMadePerGame = processSite.getData(
						"https://www.teamrankings.com/ncaa-basketball/stat/free-throws-made-per-game");
				REPORT_CACHE.ftAttemptedPerGame = processSite.getData(
						"https://www.teamrankings.com/ncaa-basketball/stat/free-throws-attempted-per-game");
				REPORT_CACHE.opponentfgMadePerGame = processSite.getData(
						"https://www.teamrankings.com/ncaa-basketball/stat/opponent-field-goals-made-per-game");
				REPORT_CACHE.opponentfgAttemptedPerGame = processSite.getData(
						"https://www.teamrankings.com/ncaa-basketball/stat/opponent-field-goals-attempted-per-game");
				REPORT_CACHE.opponentthreeFgMadePerGame = processSite.getData(
						"https://www.teamrankings.com/ncaa-basketball/stat/opponent-three-pointers-made-per-game");
				REPORT_CACHE.opponentthreeFgAttemptedPerGame = processSite.getData(
						"https://www.teamrankings.com/ncaa-basketball/stat/opponent-three-pointers-attempted-per-game");
				REPORT_CACHE.opponentftMadePerGame = processSite.getData(
						"https://www.teamrankings.com/ncaa-basketball/stat/opponent-free-throws-made-per-game");
				REPORT_CACHE.opponentftAttemptedPerGame = processSite.getData(
						"https://www.teamrankings.com/ncaa-basketball/stat/opponent-free-throws-attempted-per-game");
				REPORT_CACHE.masseyComposite = masseyRatingsSite.getCompositeRatings();

				try {
					REPORT_CACHE.sagarinRatings = usaTodaySite.getSagarinRatings();
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}

				loaded = true;

				Thread.sleep(3600000);
			} catch (InterruptedException ie) {
				
			}
		}
*/
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		updateCache();
	}
}