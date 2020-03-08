/**
 * 
 */
package com.wootechnologies.dataminer;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wooanalytics.dao.sites.masseyratings.MasseyRatingsNcaafData;
import com.wooanalytics.dao.sites.masseyratings.MasseyRatingsRankingsData;
import com.wooanalytics.dao.sites.teamrankings.TeamRankingsSos;
import com.wooanalytics.dao.sites.usatoday.SagarinNcaafData;
import com.wootechnologies.dataminer.db.DataMinerDB;
import com.wootechnologies.services.dao.sites.espn.EspnGameData;
import com.wootechnologies.services.dao.sites.vegasinsider.VegasInsiderGame;

/**
 * @author jmiller
 *
 */
public abstract class WoODataMiner  {
	private static final Logger LOGGER = Logger.getLogger(WoODataMiner.class);
	protected Map<Integer, List<MasseyRatingsNcaafData>> MasseyLatestRatings = new HashMap<Integer, List<MasseyRatingsNcaafData>>();
	protected DataMinerDB dataMiner;

	/**
	 * 
	 */
	public WoODataMiner() {
		super();
	}

	/**
	 * 
	 * @param dataMiner
	 */
	public WoODataMiner(DataMinerDB dataMiner) {
		super();
		this.dataMiner = dataMiner;
	}

	/**
	 * 
	 * @param DATAMINERDB
	 */
	protected void startConnection(DataMinerDB DATAMINERDB) {
		// Close the connection
		DATAMINERDB.complete();		
	}

	/**
	 * 
	 * @param DATAMINERDB
	 */
	protected void closeConnection(DataMinerDB DATAMINERDB) {
		// Close the connection
		DATAMINERDB.complete();		
	}

	/**
	 * 
	 * @param DATAMINERDB
	 * @param printWriter
	 */
	protected void closeConnections(DataMinerDB DATAMINERDB, PrintWriter printWriter) {
		// Close the connection
		printWriter.close();
		DATAMINERDB.complete();		
	}

	/**
	 * 
	 * @param game
	 * @param vigs
	 */
	protected void setupLineOnGame(EspnGameData game, List<VegasInsiderGame> vigs) {
		if (vigs != null && vigs.size() > 0) {
			for (VegasInsiderGame vig : vigs) {
				if (vig.getAwayteamdata().getTeamname().equals(game.getAwayteam()) && 
					vig.getHometeamdata().getTeamname().equals(game.getHometeam()) && 
					vig.getWeek().intValue() == game.getWeek().intValue() && 
					vig.getYear().intValue() == game.getYear().intValue()) {
						game.setLine(vig.getLine());
						game.setLinefavorite(vig.getLinefavorite());
					}
			}
		}
	}

	/**
	 * 
	 * @param game
	 * @param sags
	 */
	protected void setupSagarinRatings(EspnGameData game, List<SagarinNcaafData> sags) {
		if (sags != null && sags.size() > 0) {
			for (SagarinNcaafData sg : sags) {
				LOGGER.debug("SagarinNcaafData: " + sg);
				if (sg.getTeam().toUpperCase().equals(game.getAwayteam().toUpperCase())) {
					LOGGER.debug("away found");
					game.setAwaysagrinrating(sg.getMean());
				} else if (sg.getTeam().toUpperCase().equals(game.getHometeam().toUpperCase())) {
					LOGGER.debug("home found");
					game.setHomesagrinrating(sg.getMean());
				}
			}
		}		
	}

	/**
	 * 
	 * @param game
	 * @param mrrd
	 * @return
	 */
	protected void setupMasseyRatingsRankings(EspnGameData game, MasseyRatingsRankingsData mrrd) {
		LOGGER.debug("EspnGameData: " + game);
		LOGGER.debug("MasseyRatingsRankingsData: " + mrrd);
		int week = game.getWeek().intValue();
		if (week > 0) {
			week--;
		}

		if (mrrd.getTeam().toUpperCase().equals(game.getAwayteam().toUpperCase())
				&& mrrd.getWeek().intValue() == week
				&& mrrd.getYear().intValue() == game.getYear().intValue()) {
			LOGGER.error("away EspnGameData: " + game);
			game.setAwaymasseyranking(mrrd.getRank());
			game.setAwaymasseyrating(mrrd.getMean());
		} else if (mrrd.getTeam().toUpperCase().equals(game.getHometeam().toUpperCase())
				&& mrrd.getWeek().intValue() == week
				&& mrrd.getYear().intValue() == game.getYear().intValue()) {
			LOGGER.error("home EspnGameData: " + game);
			game.setHomemasseyranking(mrrd.getRank());
			game.setHomemasseyrating(mrrd.getMean());
		}
	}

	/**
	 * 
	 * @param game
	 * @param tsos
	 */
	protected void setupSos(EspnGameData game, List<TeamRankingsSos> tsos) {
		if (tsos != null && tsos.size() > 0) {
			for (TeamRankingsSos ts : tsos) {
				if (ts.getTeam().toUpperCase().equals(game.getAwayteam().toUpperCase()) &&
					ts.getWeek().intValue() == game.getMonth().intValue() && 
					ts.getYear().intValue() == game.getYear().intValue()) {
					game.setAwaysos(ts.getRating());
				} else if (ts.getTeam().toUpperCase().equals(game.getHometeam().toUpperCase()) &&
						ts.getWeek().intValue() == game.getMonth().intValue() && 
						ts.getYear().intValue() == game.getYear().intValue()) {
					game.setHomesos(ts.getRating());
				}
			}
		}		
	}

	/**
	 * 
	 * @param game
	 * @param sags
	 */
	protected void setupAwaySagarinSos(EspnGameData game, List<SagarinNcaafData> sags) {
		if (sags != null && sags.size() > 0) {
			for (SagarinNcaafData sg : sags) {
				LOGGER.debug("SagarinNcaafData: " + sg);
				if (sg.getTeam().toUpperCase().equals(game.getAwayteam().toUpperCase())) {
					LOGGER.debug("away found");
					game.setAwaysos(sg.getSchedulestrength());
				}
			}
		}		
	}

	/**
	 * 
	 * @param game
	 * @param sags
	 */
	protected void setupHomeSagarinSos(EspnGameData game, List<SagarinNcaafData> sags) {
		if (sags != null && sags.size() > 0) {
			for (SagarinNcaafData sg : sags) {
				LOGGER.debug("SagarinNcaafData: " + sg);
				if (sg.getTeam().toUpperCase().equals(game.getHometeam().toUpperCase())) {
					LOGGER.debug("away found");
					game.setHomesos(sg.getSchedulestrength());
				}
			}
		}		
	}

	/**
	 * 
	 * @param set1
	 * @param set2
	 * @return
	 */
	protected Float getFloatValue(Float set1, Float set2) {
		LOGGER.info("Entering getFloatValue()");
		Float floatValue = null;

		if (set1 != null) {
			if (set2 != null) {
				floatValue = set1.floatValue() + set2.floatValue();
			} else {
				floatValue = set1.floatValue();
			}
		} else {
			floatValue = Float.valueOf("0");
		}

		LOGGER.info("Exiting getFloatValue()");
		return floatValue;
	}
}