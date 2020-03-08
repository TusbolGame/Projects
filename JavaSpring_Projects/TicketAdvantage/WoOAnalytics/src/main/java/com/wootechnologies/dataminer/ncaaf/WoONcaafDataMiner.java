/**
 * 
 */
package com.wootechnologies.dataminer.ncaaf;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.wooanalytics.dao.sites.masseyratings.MasseyRatingsNcaafData;
import com.wooanalytics.dao.sites.masseyratings.MasseyRatingsSite;
import com.wooanalytics.dao.sites.teamrankings.TeamRankingsSite;
import com.wooanalytics.dao.sites.teamrankings.TeamRankingsSos;
import com.wooanalytics.dao.sites.usatoday.SagarinNcaafData;
import com.wooanalytics.dao.sites.usatoday.UsaTodaySite;
import com.wootechnologies.dataminer.WoODataMiner;
import com.wootechnologies.dataminer.db.ncaaf.NcaafDataMinerDB;
import com.wootechnologies.dataminer.model.Efficiencies;
import com.wootechnologies.dataminer.model.FloatData;
import com.wootechnologies.services.dao.sites.espn.EspnCollegeFootballGameData;
import com.wootechnologies.services.dao.sites.espn.EspnSite;
import com.wootechnologies.services.dao.sites.vegasinsider.VegasInsiderGame;
import com.wootechnologies.services.dao.sites.vegasinsider.VegasInsiderProcessSite;

/**
 * @author jmiller
 *
 */
public class WoONcaafDataMiner extends WoODataMiner {
	private static final Logger LOGGER = Logger.getLogger(WoONcaafDataMiner.class);
	private static final EspnSite espnSite = new EspnSite();
	private static final MasseyRatingsSite mrs = new MasseyRatingsSite();
	private static final TeamRankingsSite trs = new TeamRankingsSite();
	private static final UsaTodaySite usa = new UsaTodaySite();
	private static final VegasInsiderProcessSite vips = new VegasInsiderProcessSite();
	private static final NcaafDataMinerDB DATAMINERDB = new NcaafDataMinerDB();
	private static final Integer MASSEY_END_WEEK = 11;

	/**
	 * 
	 */
	public WoONcaafDataMiner() {
		super();
		dataMiner = DATAMINERDB;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final WoONcaafDataMiner woODataMiner = new WoONcaafDataMiner();
//		woODataMiner.getNcaafTeamData(2014, 2014, 1, 14);
//		woODataMiner.getNcaafTeamData(2015, 2015, 1, 14);
//		woODataMiner.getNcaafTeamData(2016, 2016, 1, 14);
//		woODataMiner.getNcaafTeamData(2017, 2017, 1, 14);
//		woODataMiner.getNcaafTeamData(2018, 2018, 1, 14);
		woODataMiner.getNcaafTeamData(2019, 2019, 10, 10);
//		woODataMiner.setupNcaafWeeklyData(2019);
//		woODataMiner.gameRankings(9, 9, 2018, "/Users/johnmiller/Documents/WoOTechnology/footballpicksrankingsweek9.csv");
//		woODataMiner.graphGames(3, 3, 2018, "~/Documents/WoOTechnology/footballpicksweekallv3.csv");
//		woODataMiner.getAllFCSTeams();
	}

	/**
	 * 
	 */
	public void getAllFCSTeams() {
		final List<String> fcsTeams = vips.getNcaafFCSGameData();
		for (String ng : fcsTeams) {
			System.out.println(ng);
		}
	}

	/**
	 * 
	 * @param year
	 * @param startweek
	 * @param endweek
	 */
	public void getNcaafTeamData(Integer seasonyear, Integer year, Integer startweek, Integer endweek) {
		LOGGER.info("Entering getNcaafTeamData()");
		List<MasseyRatingsNcaafData> mrds = null;
		List<TeamRankingsSos> tsos = null;
		List<SagarinNcaafData> sags = null;

		try {
			try {
				Integer endweekformassey = 1;
				if (year == 2018) {
					endweekformassey = WoONcaafDataMiner.MASSEY_END_WEEK;
				}

				mrds = mrs.getNcaafGameData(year, startweek - 1, endweek, endweekformassey, false);
				LOGGER.error("mrds: " + mrds);
				sags = usa.getSagarinNcaafRatings();
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}

			// Open the connection
			DATAMINERDB.start();

			for (int x = startweek; x <= endweek; x++) {
				tsos = trs.getSos(x, year);
	
				// Get all the games
				final List<EspnCollegeFootballGameData> ncaafGames = espnSite.getNcaafTeamData(year, x, x, true);
				final List<VegasInsiderGame> vigs = vips.getNcaafGameData(year, x, x, false);

				// Loop through all the game to make sure they are setup correctly
				for (EspnCollegeFootballGameData ncaafGame : ncaafGames) {
					try {
						LOGGER.debug("ncaafGame: " + ncaafGame);
						if (ncaafGame.getYear() == null || ncaafGame.getYear().floatValue() == 0) {
							ncaafGame.setYear(year);
						}

						// Setup the lines
						setupLineOnGame(ncaafGame, vigs);

						// Setup the Sagarin ratings
						setupSagarinRatings(ncaafGame, sags);

						// Setup Massey Ratings/Rankings
						if (mrds != null && mrds.size() > 0) {
							for (MasseyRatingsNcaafData mr : mrds) {
								LOGGER.debug("MasseyRatingsNcaafData: " + mr);
								setupMasseyRatingsRankings(ncaafGame, mr);
							}
						}

						// Setup Strength of Schedules
						setupSos(ncaafGame, tsos);

						// If we don't have valid rankings, send out an error
						if (ncaafGame.getAwaymasseyranking() == null || ncaafGame.getAwaymasseyranking().floatValue() == 0) {
							LOGGER.error("MISSING: " + ncaafGame.getAwayteam().toUpperCase());
							LOGGER.error("ncaafGame: " + ncaafGame);
						}
						if (ncaafGame.getHomemasseyranking() == null || ncaafGame.getHomemasseyranking().floatValue() == 0) {
							LOGGER.error("MISSING: " + ncaafGame.getHometeam().toUpperCase());
							LOGGER.error("ncaafGame: " + ncaafGame);
						}
		
						// Setup away SOS if necessary
						if (ncaafGame.getAwaysos() == null || ncaafGame.getAwaysos().floatValue() == 0) {
							setupAwaySagarinSos(ncaafGame, sags);
						}
	
						// Setup home SOS if necessary
						if (ncaafGame.getHomesos() == null || ncaafGame.getHomesos().floatValue() == 0) {
							setupHomeSagarinSos(ncaafGame, sags);
						}
	
						// Check for a valid game
						if (ncaafGame.getAwayfinalscore() != 0 || ncaafGame.getHomefinalscore() != 0) {
							ncaafGame.setSeasonyear(seasonyear);
							LOGGER.debug("persisting: " + ncaafGame);
							DATAMINERDB.persistEspnNcaafGameData(ncaafGame);
						}
					} catch (Throwable t) {
						LOGGER.error(t.getMessage(), t);
					}
				}
			}
	
			// Close the connection
			DATAMINERDB.complete();
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting getNcaafTeamData()");
	}

	/**
	 * 
	 * @param year
	 */
	public void setupNcaafWeeklyData(Integer year) {
		LOGGER.info("Entering setupNcaafWeeklyData()");

		// Open the connection
		DATAMINERDB.start();

		try {
			final List<EspnCollegeFootballGameData> week0 = DATAMINERDB.getEspnNcaafGameDataByWeek(0, year);
			final List<EspnCollegeFootballGameData> week1 = DATAMINERDB.getEspnNcaafGameDataByWeek(1, year);
			final List<EspnCollegeFootballGameData> week2 = DATAMINERDB.getEspnNcaafGameDataByWeek(2, year);
			final List<EspnCollegeFootballGameData> week3 = DATAMINERDB.getEspnNcaafGameDataByWeek(3, year);
			final List<EspnCollegeFootballGameData> week4 = DATAMINERDB.getEspnNcaafGameDataByWeek(4, year);
			final List<EspnCollegeFootballGameData> week5 = DATAMINERDB.getEspnNcaafGameDataByWeek(5, year);
			final List<EspnCollegeFootballGameData> week6 = DATAMINERDB.getEspnNcaafGameDataByWeek(6, year);
			final List<EspnCollegeFootballGameData> week7 = DATAMINERDB.getEspnNcaafGameDataByWeek(7, year);
			final List<EspnCollegeFootballGameData> week8 = DATAMINERDB.getEspnNcaafGameDataByWeek(8, year);
			final List<EspnCollegeFootballGameData> week9 = DATAMINERDB.getEspnNcaafGameDataByWeek(9, year);
			final List<EspnCollegeFootballGameData> week10 = DATAMINERDB.getEspnNcaafGameDataByWeek(10, year);
			final List<EspnCollegeFootballGameData> week11 = DATAMINERDB.getEspnNcaafGameDataByWeek(11, year);
			final List<EspnCollegeFootballGameData> week12 = DATAMINERDB.getEspnNcaafGameDataByWeek(12, year);
			final List<EspnCollegeFootballGameData> week13 = DATAMINERDB.getEspnNcaafGameDataByWeek(13, year);
			final List<EspnCollegeFootballGameData> week14 = DATAMINERDB.getEspnNcaafGameDataByWeek(14, year);
//			List<EspnCollegeFootballGameData> week15 = DATAMINERDB.getEspnNcaafGameDataByWeek(15);

			for (EspnCollegeFootballGameData game : week0) {
				storeGameData(game, 1, year);
			}
			for (EspnCollegeFootballGameData game : week1) {
				storeGameData(game, 1, year);
			}
			for (EspnCollegeFootballGameData game : week2) {
				storeGameData(game, 1, year);
			}
			for (EspnCollegeFootballGameData game : week3) {
				storeGameData(game, 2, year);
			}
			for (EspnCollegeFootballGameData game : week4) {
				storeGameData(game, 3, year);
			}
			for (EspnCollegeFootballGameData game : week5) {
				storeGameData(game, 4, year);
			}
			for (EspnCollegeFootballGameData game : week6) {
				storeGameData(game, 5, year);
			}
			for (EspnCollegeFootballGameData game : week7) {
				storeGameData(game, 6, year);
			}
			for (EspnCollegeFootballGameData game : week8) {
				storeGameData(game, 7, year);
			}
			for (EspnCollegeFootballGameData game : week9) {
				storeGameData(game, 8, year);
			}
			for (EspnCollegeFootballGameData game : week10) {
				storeGameData(game, 9, year);
			}
			for (EspnCollegeFootballGameData game : week11) {
				storeGameData(game, 10, year);
			}
			for (EspnCollegeFootballGameData game : week12) {
				storeGameData(game, 11, year);
			}
			for (EspnCollegeFootballGameData game : week13) {
				storeGameData(game, 12, year);
			}
			for (EspnCollegeFootballGameData game : week14) {
				storeGameData(game, 13, year);
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		// Close the connection
		DATAMINERDB.complete();

		LOGGER.info("Exiting setupNcaafWeeklyData()");
	}

	/**
	 * 
	 * @param game
	 * @param week
	 * @param year
	 * @param sag
	 * @throws SQLException
	 */
	private void storeGameData(EspnCollegeFootballGameData game, Integer week, Integer year) throws SQLException {
		LOGGER.info("Entering storeGameData()");

		final String acollegename = game.getAwaycollegename();
		final String hcollegename = game.getHomecollegename();
		if (game.getYear() != null && game.getYear() == 0) {
			game.setYear(year);
		} else if (game.getYear() == null) {
			game.setYear(year);
		}

		// Number of games
		Integer anumgames = DATAMINERDB.numberOfGames(game.getYear(), acollegename);
		Integer hnumgames = DATAMINERDB.numberOfGames(game.getYear(), hcollegename);
		Integer aanumgames = DATAMINERDB.numberOfAwayGames(game.getYear(), acollegename);
		Integer hhnumgames = DATAMINERDB.numberOfHomeGames(game.getYear(), hcollegename);

		if (anumgames == null) {
			anumgames = new Integer(1);
		} else if (anumgames == 0) {
			anumgames = new Integer(1);
		} else {
			anumgames++;
		}
		if (hnumgames == null) {
			hnumgames = new Integer(1);
		} else if (hnumgames == 0) {
			hnumgames = new Integer(1);
		} else {
			hnumgames++;
		}
		if (aanumgames == null) {
			aanumgames = new Integer(1);
		} else if (aanumgames == 0) {
			aanumgames = new Integer(1);
		} else {
			aanumgames++;
		}
		if (hhnumgames == null) {
			hnumgames = new Integer(1);
		} else if (hnumgames == 0) {
			hhnumgames = new Integer(1);
		} else {
			hhnumgames++;
		}

		LOGGER.debug("Away team: " + game.getAwaycollegename() + " number of games: " + anumgames);
		LOGGER.debug("Home team: " + game.getHomecollegename() + " number of games: " + hnumgames);
		LOGGER.debug("Away team: " + game.getAwaycollegename() + " number of away games: " + aanumgames);
		LOGGER.debug("Home team: " + game.getHomecollegename() + " number of home games: " + hhnumgames);
		
		try {
			// Prepare the offsets
			Float awayfinal = new Float(1);
			Float homefinal = new Float(1);

			// First Downs	
			final Float afirstdowns = game.getAwayfirstdowns().floatValue() * awayfinal;
			final Float hfirstdowns = game.getHomefirstdowns().floatValue() * homefinal;
			final FloatData affdpg = DATAMINERDB.getFootballfirstdownspergame(year, "all", acollegename);
			final FloatData hffdpg = DATAMINERDB.getFootballfirstdownspergame(year, "all", hcollegename);
			final FloatData aaffdpg = DATAMINERDB.getFootballfirstdownspergame(year, "away", acollegename);
			final FloatData hhffdpg = DATAMINERDB.getFootballfirstdownspergame(year, "home", hcollegename);
			final Float afd = getFloatValue(afirstdowns, affdpg.getFloatdatatotal());
			final Float aafd = getFloatValue(afirstdowns, aaffdpg.getFloatdatatotal());
			final Float aoppfd = getFloatValue(hfirstdowns, affdpg.getOppfloatdatatotal());
			final Float aaoppfd = getFloatValue(hfirstdowns, aaffdpg.getOppfloatdatatotal());
			final Float hfd = getFloatValue(hfirstdowns, hffdpg.getFloatdatatotal());
			final Float hhfd = getFloatValue(hfirstdowns, hhffdpg.getFloatdatatotal());
			final Float hoppfd = getFloatValue(afirstdowns, hffdpg.getOppfloatdatatotal());
			final Float hhoppfd = getFloatValue(afirstdowns, hhffdpg.getOppfloatdatatotal());
			DATAMINERDB.persistFootballfirstdownspergame(game.getWeek(), game.getYear(), "all", acollegename, new Float(afd/anumgames), new Float(aoppfd/anumgames), afd, aoppfd, anumgames);
			DATAMINERDB.persistFootballfirstdownspergame(game.getWeek(), game.getYear(), "all", hcollegename, new Float(hfd/hnumgames), new Float(hoppfd/hnumgames), hfd, hoppfd, hnumgames);
			DATAMINERDB.persistFootballfirstdownspergame(game.getWeek(), game.getYear(), "away", acollegename, new Float(aafd/aanumgames), new Float(aaoppfd/aanumgames), aafd, aaoppfd, aanumgames);
			DATAMINERDB.persistFootballfirstdownspergame(game.getWeek(), game.getYear(), "home", hcollegename, new Float(hhfd/hhnumgames), new Float(hhoppfd/hhnumgames), hhfd, hhoppfd, hhnumgames);

			// Fourth Down Efficiency
			final Float afem = game.getAwayfourthefficiencymade().floatValue() * awayfinal;
			final Float afea = game.getAwayfourthefficiencyattempts().floatValue() * awayfinal;
			final Float hfem = game.getHomefourthefficiencymade().floatValue() * homefinal;
			final Float hfea = game.getHomefourthefficiencyattempts().floatValue() * homefinal;
			final Efficiencies afde = DATAMINERDB.getFootballfourthdowneffpergame(year, "all", acollegename);  
			final Efficiencies hfde = DATAMINERDB.getFootballfourthdowneffpergame(year, "all", hcollegename);
			final Efficiencies aafde = DATAMINERDB.getFootballfourthdowneffpergame(year, "away", acollegename);  
			final Efficiencies hhfde = DATAMINERDB.getFootballfourthdowneffpergame(year, "home", hcollegename);
			final Float afemtotal = getFloatValue(afem, afde.getMadetotal());
			final Float afeatotal = getFloatValue(afea, afde.getAttemptstotal());
			final Float aafemtotal = getFloatValue(afem, aafde.getMadetotal());
			final Float aafeatotal = getFloatValue(afea, aafde.getAttemptstotal());
			final Float hfemtotal = getFloatValue(hfem, afde.getOppmadetotal());
			final Float hfeatotal = getFloatValue(hfea, afde.getOppattemptstotal());
			final Float hhhhfemtotal = getFloatValue(hfem, aafde.getOppmadetotal());
			final Float hhhhfeatotal = getFloatValue(hfea, aafde.getOppattemptstotal());
			final Float hhfemtotal = getFloatValue(hfem, hfde.getMadetotal());
			final Float hhfeatotal = getFloatValue(hfea, hfde.getAttemptstotal());
			final Float hhhhhfemtotal = getFloatValue(hfem, hhfde.getMadetotal());
			final Float hhhhhfeatotal = getFloatValue(hfea, hhfde.getAttemptstotal());
			final Float hafemtotal = getFloatValue(afem, hfde.getOppmadetotal());
			final Float hafeatotal = getFloatValue(afea, hfde.getOppattemptstotal());
			final Float hhhhafemtotal = getFloatValue(afem, hhfde.getOppmadetotal());
			final Float hhhhafeatotal = getFloatValue(afea, hhfde.getOppattemptstotal());

			Float afed = null;
			if (afemtotal == 0 && afeatotal == 0) {
				afed = new Float(0); 
			} else {
				afed = new Float(afemtotal/afeatotal);
			}
			Float aafed = null;
			if (aafemtotal == 0 && aafeatotal == 0) {
				aafed = new Float(0); 
			} else {
				aafed = new Float(aafemtotal/aafeatotal);
			}
			Float aoppfed = null;
			if (hfemtotal == 0 && hfeatotal == 0) {
				aoppfed = new Float(0); 
			} else {
				aoppfed = new Float(hfemtotal/hfeatotal);
			}
			Float aaoppfed = null;
			if (hhfemtotal == 0 && hhfeatotal == 0) {
				aaoppfed = new Float(0); 
			} else {
				aaoppfed = new Float(hhfemtotal/hhfeatotal);
			}
			Float aoppfedtotal = null;
			if (afemtotal == 0 && afeatotal == 0) {
				aoppfedtotal = new Float(0); 
			} else {
				aoppfedtotal = new Float(afemtotal/afeatotal);
			}
			Float aaoppfedtotal = null;
			if (aafemtotal == 0 && aafeatotal == 0) {
				aaoppfedtotal = new Float(0); 
			} else {
				aaoppfedtotal = new Float(aafemtotal/aafeatotal);
			}
			Float hoppfedtotal = null;
			if (hfemtotal == 0 && hfeatotal == 0) {
				hoppfedtotal = new Float(0); 
			} else {
				hoppfedtotal = new Float(hfemtotal/hfeatotal);
			}
			Float hhhhoppfedtotal = null;
			if (hhfemtotal == 0 && hhfeatotal == 0) {
				hhhhoppfedtotal = new Float(0); 
			} else {
				hhhhoppfedtotal = new Float(hhhhhfemtotal/hhhhhfeatotal);
			}
			Float hfed = null;
			if (hhfemtotal == 0 && hhfeatotal == 0) {
				hfed = new Float(0); 
			} else {
				hfed = new Float(hhfemtotal/hhfeatotal);
			}
			Float hhhhfed = null;
			if (hhhhfemtotal == 0 && hhhhfeatotal == 0) {
				hhhhfed = new Float(0); 
			} else {
				hhhhfed = new Float(hhhhfemtotal/hhhhfeatotal);
			}
			Float hoppfed = null;
			if (hafemtotal == 0 && hafeatotal == 0) {
				hoppfed = new Float(0); 
			} else {
				hoppfed = new Float(hafemtotal/hafeatotal);
			}
			Float hhhhoppfed = null;
			if (hhhhafemtotal == 0 && hhhhafeatotal == 0) {
				hhhhoppfed = new Float(0); 
			} else {
				hhhhoppfed = new Float(hhhhafemtotal/hhhhafeatotal);
			}
			Float hhoppfedtotal = null;
			if (hhfemtotal == 0 && hhfeatotal == 0) {
				hhoppfedtotal = new Float(0); 
			} else {
				hhoppfedtotal = new Float(hhfemtotal/hhfeatotal);
			}
			Float hhhhhoppfedtotal = null;
			if (hhhhfemtotal == 0 && hhhhfeatotal == 0) {
				hhhhhoppfedtotal = new Float(0); 
			} else {
				hhhhhoppfedtotal = new Float(hhhhfemtotal/hhhhfeatotal);
			}
			Float hhhoppfedtotal = null;
			if (hafemtotal == 0 && hafeatotal == 0) {
				hhhoppfedtotal = new Float(0); 
			} else {
				hhhoppfedtotal = new Float(hafemtotal/hafeatotal);
			}
			Float hhhhhhoppfedtotal = null;
			if (hhhhafemtotal == 0 && hhhhafeatotal == 0) {
				hhhhhhoppfedtotal = new Float(0); 
			} else {
				hhhhhhoppfedtotal = new Float(hhhhafemtotal/hhhhafeatotal);
			}

			DATAMINERDB.persistFootballfourthdowneffpergame(game.getWeek(), 
					game.getYear(), 
					"all", 
					acollegename, 
					afed, 
					aoppfed, 
					aoppfedtotal, 
					hoppfedtotal, 
					anumgames,
					afem,
					afea,
					hfem,
					hfea,
					getFloatValue(afem, afde.getMadetotal()),
					getFloatValue(afea, afde.getAttemptstotal()),
					getFloatValue(hfem, afde.getOppmadetotal()),
					getFloatValue(hfea, afde.getOppattemptstotal()));
			DATAMINERDB.persistFootballfourthdowneffpergame(game.getWeek(), 
					game.getYear(), 
					"all", 
					hcollegename, 
					hfed,
					hoppfed, 
					hhoppfedtotal, 
					hhhoppfedtotal,
					hnumgames,
					hfem,
					hfea,
					afem,
					afea,
					getFloatValue(hfem, hfde.getMadetotal()),
					getFloatValue(hfea, hfde.getAttemptstotal()),
					getFloatValue(afem, hfde.getOppmadetotal()),
					getFloatValue(afea, hfde.getOppattemptstotal()));
			DATAMINERDB.persistFootballfourthdowneffpergame(game.getWeek(), 
					game.getYear(), 
					"away", 
					acollegename, 
					aafed, 
					aaoppfed, 
					aaoppfedtotal, 
					hhhhoppfedtotal, 
					aanumgames,
					afem,
					afea,
					hfem,
					hfea,
					getFloatValue(afem, aafde.getMadetotal()),
					getFloatValue(afea, aafde.getAttemptstotal()),
					getFloatValue(hfem, aafde.getOppmadetotal()),
					getFloatValue(hfea, aafde.getOppattemptstotal()));
			DATAMINERDB.persistFootballfourthdowneffpergame(game.getWeek(), 
					game.getYear(), 
					"home", 
					hcollegename, 
					hhhhfed,
					hhhhoppfed, 
					hhhhhoppfedtotal, 
					hhhhhhoppfedtotal,
					hhnumgames,
					hfem,
					hfea,
					afem,
					afea,
					getFloatValue(hfem, hhfde.getMadetotal()),
					getFloatValue(hfea, hhfde.getAttemptstotal()),
					getFloatValue(afem, hhfde.getOppmadetotal()),
					getFloatValue(afea, hhfde.getOppattemptstotal()));

			// Pass Attempts Per Game
			final Float apassatemptspergame = game.getAwaypassattempts().floatValue() * awayfinal;
			final Float hpassatemptspergame = game.getHomepassattempts().floatValue() * homefinal;
			final FloatData apafd = DATAMINERDB.getFootballpassattemptspergame(year, "all", acollegename);  
			final FloatData hpafd = DATAMINERDB.getFootballpassattemptspergame(year, "all", hcollegename);
			final FloatData aapafd = DATAMINERDB.getFootballpassattemptspergame(year, "away", acollegename);  
			final FloatData hhpafd = DATAMINERDB.getFootballpassattemptspergame(year, "home", hcollegename);
			final Float apa = getFloatValue(apassatemptspergame, apafd.getFloatdatatotal()); 
			final Float aopppa = getFloatValue(hpassatemptspergame, apafd.getOppfloatdatatotal()); 
			final Float aapa = getFloatValue(apassatemptspergame, aapafd.getFloatdatatotal()); 
			final Float aaopppa = getFloatValue(hpassatemptspergame, aapafd.getOppfloatdatatotal()); 
			final Float hpa = getFloatValue(hpassatemptspergame, hpafd.getFloatdatatotal());
			final Float hopppa = getFloatValue(apassatemptspergame, hpafd.getOppfloatdatatotal());
			final Float hhpa = getFloatValue(hpassatemptspergame, hhpafd.getFloatdatatotal());
			final Float hhopppa = getFloatValue(apassatemptspergame, hhpafd.getOppfloatdatatotal());
			DATAMINERDB.persistFootballpassattemptspergame(game.getWeek(), game.getYear(), "all", acollegename, new Float(apa/anumgames), new Float(aopppa/anumgames), apa, aopppa, anumgames);
			DATAMINERDB.persistFootballpassattemptspergame(game.getWeek(), game.getYear(), "all", hcollegename, new Float(hpa/hnumgames), new Float(hopppa/hnumgames), hpa, hopppa, hnumgames);
			DATAMINERDB.persistFootballpassattemptspergame(game.getWeek(), game.getYear(), "away", acollegename, new Float(aapa/aanumgames), new Float(aaopppa/aanumgames), aapa, aaopppa, aanumgames);
			DATAMINERDB.persistFootballpassattemptspergame(game.getWeek(), game.getYear(), "home", hcollegename, new Float(hhpa/hhnumgames), new Float(hhopppa/hhnumgames), hhpa, hhopppa, hhnumgames);

			// Pass Completions Per Game
			final Float apasscompletionspergame = game.getAwaypasscomp().floatValue() * awayfinal;
			final Float hpasscompletionspergame = game.getHomepasscomp().floatValue() * homefinal;
			final FloatData apcfd = DATAMINERDB.getFootballpasscompletionspergame(year, "all", acollegename);  
			final FloatData hpcfd = DATAMINERDB.getFootballpasscompletionspergame(year, "all", hcollegename);
			final FloatData aapcfd = DATAMINERDB.getFootballpasscompletionspergame(year, "away", acollegename);  
			final FloatData hhpcfd = DATAMINERDB.getFootballpasscompletionspergame(year, "home", hcollegename);
			final Float apc = getFloatValue(apasscompletionspergame, apcfd.getFloatdatatotal()); 
			final Float aopppc = getFloatValue(hpasscompletionspergame, apcfd.getOppfloatdatatotal());
			final Float aapc = getFloatValue(apasscompletionspergame, aapcfd.getFloatdatatotal()); 
			final Float aaopppc = getFloatValue(hpasscompletionspergame, aapcfd.getOppfloatdatatotal()); 
			final Float hpc = getFloatValue(hpasscompletionspergame, hpcfd.getFloatdatatotal()); 
			final Float hopppc = getFloatValue(apasscompletionspergame, hpcfd.getOppfloatdatatotal());
			final Float hhpc = getFloatValue(hpasscompletionspergame, hhpcfd.getFloatdatatotal()); 
			final Float hhopppc = getFloatValue(apasscompletionspergame, hhpcfd.getOppfloatdatatotal()); 
			DATAMINERDB.persistFootballpasscompletionspergame(game.getWeek(), game.getYear(), "all", acollegename, new Float(apc/anumgames), new Float(aopppc/anumgames), apc, aopppc, anumgames);
			DATAMINERDB.persistFootballpasscompletionspergame(game.getWeek(), game.getYear(), "all", hcollegename, new Float(hpc/hnumgames), new Float(hopppc/hnumgames), hpc, hopppc, hnumgames);
			DATAMINERDB.persistFootballpasscompletionspergame(game.getWeek(), game.getYear(), "away", acollegename, new Float(aapc/aanumgames), new Float(aaopppc/aanumgames), aapc, aaopppc, aanumgames);
			DATAMINERDB.persistFootballpasscompletionspergame(game.getWeek(), game.getYear(), "home", hcollegename, new Float(hhpc/hhnumgames), new Float(hhopppc/hhnumgames), hhpc, hhopppc, hhnumgames);
	
			// Pass Yards Per Game
			final Float apassyardspergame = game.getAwaypassingyards().floatValue() * awayfinal;
			final Float hpassyardspergame = game.getHomepassingyards().floatValue() * homefinal;
			final FloatData apyfd = DATAMINERDB.getFootballpassyardspergame(year, "all", acollegename);  
			final FloatData hpyfd = DATAMINERDB.getFootballpassyardspergame(year, "all", hcollegename);
			final FloatData aapyfd = DATAMINERDB.getFootballpassyardspergame(year, "away", acollegename);  
			final FloatData hhpyfd = DATAMINERDB.getFootballpassyardspergame(year, "home", hcollegename);
			final Float apy = getFloatValue(apassyardspergame, apyfd.getFloatdatatotal());
			final Float aopppy = getFloatValue(hpassyardspergame, apyfd.getOppfloatdatatotal()); 
			final Float aapy = getFloatValue(apassyardspergame, aapyfd.getFloatdatatotal());
			final Float aaopppy = getFloatValue(hpassyardspergame, aapyfd.getOppfloatdatatotal());
			final Float hpy = getFloatValue(hpassyardspergame, hpyfd.getFloatdatatotal());
			final Float hopppy = getFloatValue(apassyardspergame, hpyfd.getOppfloatdatatotal());
			final Float hhpy = getFloatValue(hpassyardspergame, hhpyfd.getFloatdatatotal());
			final Float hhopppy = getFloatValue(apassyardspergame, hhpyfd.getOppfloatdatatotal());
			DATAMINERDB.persistFootballpassyardspergame(game.getWeek(), game.getYear(), "all", acollegename, new Float(apy/anumgames), new Float(aopppy/anumgames), apy, aopppy, anumgames);
			DATAMINERDB.persistFootballpassyardspergame(game.getWeek(), game.getYear(), "all", hcollegename, new Float(hpy/hnumgames), new Float(hopppy/hnumgames), hpy, hopppy, hnumgames);
			DATAMINERDB.persistFootballpassyardspergame(game.getWeek(), game.getYear(), "away", acollegename, new Float(aapy/aanumgames), new Float(aaopppy/aanumgames), aapy, aaopppy, anumgames);
			DATAMINERDB.persistFootballpassyardspergame(game.getWeek(), game.getYear(), "home", hcollegename, new Float(hhpy/hhnumgames), new Float(hhopppy/hhnumgames), hhpy, hhopppy, hhnumgames);
	
			// Points Per Game 
			final Float apointspergame = game.getAwayfinalscore().floatValue() * awayfinal;
			final Float hpointspergame = game.getHomefinalscore().floatValue() * homefinal;
			final FloatData apfd = DATAMINERDB.getFootballpointspergame(year, "all", acollegename);  
			final FloatData hpfd = DATAMINERDB.getFootballpointspergame(year, "all", hcollegename);
			final FloatData aapfd = DATAMINERDB.getFootballpointspergame(year, "away", acollegename);  
			final FloatData hhpfd = DATAMINERDB.getFootballpointspergame(year, "home", hcollegename);
			final Float ap = getFloatValue(apointspergame, apfd.getFloatdatatotal()); 
			final Float aoppp = getFloatValue(hpointspergame, apfd.getOppfloatdatatotal()); 
			final Float aap = getFloatValue(apointspergame, aapfd.getFloatdatatotal()); 
			final Float aaoppp = getFloatValue(hpointspergame, aapfd.getOppfloatdatatotal()); 
			final Float hp = getFloatValue(hpointspergame, hpfd.getFloatdatatotal()); 
			final Float hoppp = getFloatValue(apointspergame, hpfd.getOppfloatdatatotal());
			final Float hhp = getFloatValue(hpointspergame, hhpfd.getFloatdatatotal()); 
			final Float hhoppp = getFloatValue(apointspergame, hhpfd.getOppfloatdatatotal()); 
			DATAMINERDB.persistFootballpointspergame(game.getWeek(), game.getYear(), "all", acollegename, new Float(ap/anumgames), new Float(aoppp/anumgames), ap, aoppp, anumgames);
			DATAMINERDB.persistFootballpointspergame(game.getWeek(), game.getYear(), "all", hcollegename, new Float(hp/hnumgames), new Float(hoppp/hnumgames), hp, hoppp, hnumgames);
			DATAMINERDB.persistFootballpointspergame(game.getWeek(), game.getYear(), "away", acollegename, new Float(aap/aanumgames), new Float(aaoppp/aanumgames), aap, aaoppp, aanumgames);
			DATAMINERDB.persistFootballpointspergame(game.getWeek(), game.getYear(), "home", hcollegename, new Float(hhp/hhnumgames), new Float(hhoppp/hhnumgames), hhp, hhoppp, hhnumgames);
	
			// Possession Time Per Game
			final Float apossessiontimepergame = game.getAwaypossessionminutes().floatValue() * awayfinal;
			final Float hpossessiontimepergame = game.getHomepossessionminutes().floatValue() * homefinal;
			final FloatData aptfd = DATAMINERDB.getFootballpossessiontimepergame(year, "all", acollegename);  
			final FloatData hptfd = DATAMINERDB.getFootballpossessiontimepergame(year, "all", hcollegename);
			final FloatData aaptfd = DATAMINERDB.getFootballpossessiontimepergame(year, "away", acollegename);  
			final FloatData hhptfd = DATAMINERDB.getFootballpossessiontimepergame(year, "home", hcollegename);
			final Float apt = getFloatValue(apossessiontimepergame, aptfd.getFloatdatatotal()); 
			final Float aopppt = getFloatValue(hpossessiontimepergame, aptfd.getOppfloatdatatotal()); 
			final Float aapt = getFloatValue(apossessiontimepergame, aaptfd.getFloatdatatotal()); 
			final Float aaopppt = getFloatValue(hpossessiontimepergame, aaptfd.getOppfloatdatatotal()); 
			final Float hpt = getFloatValue(hpossessiontimepergame, hptfd.getFloatdatatotal());
			final Float hopppt = getFloatValue(apossessiontimepergame, hptfd.getOppfloatdatatotal());
			final Float hhpt = getFloatValue(hpossessiontimepergame, hhptfd.getFloatdatatotal()); 
			final Float hhopppt = getFloatValue(apossessiontimepergame, hhptfd.getOppfloatdatatotal()); 
			DATAMINERDB.persistFootballpossessiontimepergame(game.getWeek(), game.getYear(), "all", acollegename, new Float(apt/anumgames), new Float(aopppt/anumgames), apt, aopppt, anumgames);
			DATAMINERDB.persistFootballpossessiontimepergame(game.getWeek(), game.getYear(), "all", hcollegename, new Float(hpt/hnumgames), new Float(hopppt/hnumgames), hpt, hopppt, hnumgames);
			DATAMINERDB.persistFootballpossessiontimepergame(game.getWeek(), game.getYear(), "away", acollegename, new Float(aapt/aanumgames), new Float(aaopppt/aanumgames), aapt, aaopppt, aanumgames);
			DATAMINERDB.persistFootballpossessiontimepergame(game.getWeek(), game.getYear(), "home", hcollegename, new Float(hhpt/hhnumgames), new Float(hhopppt/hhnumgames), hhpt, hhopppt, hhnumgames);

			// Rushing Attempts Per Game
			final Float arushingattemptspergame = game.getAwayrushingattempts().floatValue() * awayfinal;
			final Float hrushingattemptspergame = game.getHomerushingattempts().floatValue() * homefinal;
			final FloatData arafd = DATAMINERDB.getFootballrushingattemptspergame(week, year, "all", acollegename);  
			final FloatData hrafd = DATAMINERDB.getFootballrushingattemptspergame(week, year, "all", hcollegename);
			final FloatData aarafd = DATAMINERDB.getFootballrushingattemptspergame(week, year, "away", acollegename);  
			final FloatData hhrafd = DATAMINERDB.getFootballrushingattemptspergame(week, year, "home", hcollegename);
			final Float ara = getFloatValue(arushingattemptspergame, arafd.getFloatdatatotal()); 
			final Float aoppra = getFloatValue(hrushingattemptspergame, arafd.getOppfloatdatatotal());
			final Float aara = getFloatValue(arushingattemptspergame, aarafd.getFloatdatatotal()); 
			final Float aaoppra = getFloatValue(hrushingattemptspergame, aarafd.getOppfloatdatatotal());
			final Float hra = getFloatValue(hrushingattemptspergame, hrafd.getFloatdatatotal());
			final Float hoppra = getFloatValue(arushingattemptspergame, hrafd.getOppfloatdatatotal());
			final Float hhra = getFloatValue(hrushingattemptspergame, hhrafd.getFloatdatatotal());
			final Float hhoppra = getFloatValue(arushingattemptspergame, hhrafd.getOppfloatdatatotal());
			DATAMINERDB.persistFootballrushingattemptspergame(game.getWeek(), game.getYear(), "all", acollegename, new Float(ara/anumgames), new Float(aoppra/anumgames), ara, aoppra, anumgames);
			DATAMINERDB.persistFootballrushingattemptspergame(game.getWeek(), game.getYear(), "all", hcollegename, new Float(hra/hnumgames), new Float(hoppra/hnumgames), hra, hoppra, hnumgames);
			DATAMINERDB.persistFootballrushingattemptspergame(game.getWeek(), game.getYear(), "away", acollegename, new Float(aara/aanumgames), new Float(aaoppra/aanumgames), aara, aaoppra, aanumgames);
			DATAMINERDB.persistFootballrushingattemptspergame(game.getWeek(), game.getYear(), "home", hcollegename, new Float(hhra/hhnumgames), new Float(hhoppra/hhnumgames), hhra, hhoppra, hhnumgames);

			// Rushing Yards Per Game
			final Float arushingyardspergame = game.getAwayrushingyards().floatValue() * awayfinal;
			final Float hrushingyardspergame = game.getHomerushingyards().floatValue() * homefinal;
			final FloatData aryfd = DATAMINERDB.getFootballrushingyardspergame(week, year, "all", acollegename);
			final FloatData hryfd = DATAMINERDB.getFootballrushingyardspergame(week, year, "all", hcollegename);
			final FloatData aaryfd = DATAMINERDB.getFootballrushingyardspergame(week, year, "away", acollegename);
			final FloatData hhryfd = DATAMINERDB.getFootballrushingyardspergame(week, year, "home", hcollegename);
			final Float ary = getFloatValue(arushingyardspergame, aryfd.getFloatdatatotal());
			final Float aoppry = getFloatValue(hrushingyardspergame, aryfd.getOppfloatdatatotal());
			final Float aary = getFloatValue(arushingyardspergame, aaryfd.getFloatdatatotal());
			final Float aaoppry = getFloatValue(hrushingyardspergame, aaryfd.getOppfloatdatatotal());
			final Float hry = getFloatValue(hrushingyardspergame, hryfd.getFloatdatatotal());
			final Float hoppry = getFloatValue(arushingyardspergame, hryfd.getOppfloatdatatotal());
			final Float hhry = getFloatValue(hrushingyardspergame, hhryfd.getFloatdatatotal());
			final Float hhoppry = getFloatValue(arushingyardspergame, hhryfd.getOppfloatdatatotal());
			DATAMINERDB.persistFootballrushingyardspergame(game.getWeek(), game.getYear(), "all", acollegename, new Float(ary/anumgames), new Float(aoppry/anumgames), ary, aoppry, anumgames);
			DATAMINERDB.persistFootballrushingyardspergame(game.getWeek(), game.getYear(), "all", hcollegename, new Float(hry/hnumgames), new Float(hoppry/hnumgames), hry, hoppry, hnumgames);
			DATAMINERDB.persistFootballrushingyardspergame(game.getWeek(), game.getYear(), "away", acollegename, new Float(aary/aanumgames), new Float(aaoppry/aanumgames), aary, aaoppry, aanumgames);
			DATAMINERDB.persistFootballrushingyardspergame(game.getWeek(), game.getYear(), "home", hcollegename, new Float(hhry/hhnumgames), new Float(hhoppry/hhnumgames), hhry, hhoppry, hhnumgames);

			// 3rd Down Efficiency Per Game
			final Float atem = game.getAwaythirdefficiencymade().floatValue() * awayfinal;
			final Float atea = game.getAwaythirdefficiencyattempts().floatValue() * awayfinal;
			final Float htem = game.getHomethirdefficiencymade().floatValue() * homefinal;
			final Float htea = game.getHomethirdefficiencyattempts().floatValue() * homefinal;
			final Efficiencies atde = DATAMINERDB.getFootballthirddowneffpergame(week, year, "all", acollegename);  
			final Efficiencies htde = DATAMINERDB.getFootballthirddowneffpergame(week, year, "all", hcollegename);
			final Efficiencies aatde = DATAMINERDB.getFootballthirddowneffpergame(week, year, "away", acollegename);  
			final Efficiencies hhtde = DATAMINERDB.getFootballthirddowneffpergame(week, year, "home", hcollegename);
			final Float atemtotal = getFloatValue(atem, atde.getMadetotal());
			final Float ateatotal = getFloatValue(atea, atde.getAttemptstotal());
			final Float aatemtotal = getFloatValue(atem, aatde.getMadetotal());
			final Float aateatotal = getFloatValue(atea, aatde.getAttemptstotal());
			final Float htemtotal = getFloatValue(htem, atde.getOppmadetotal());
			final Float hteatotal = getFloatValue(htea, atde.getOppattemptstotal());
			final Float hhhhtemtotal = getFloatValue(htem, aatde.getOppmadetotal());
			final Float hhhhteatotal = getFloatValue(htea, aatde.getOppattemptstotal());
			final Float hhtemtotal = getFloatValue(htem, htde.getMadetotal());
			final Float hhteatotal = getFloatValue(htea, htde.getAttemptstotal());
			final Float hhhhhtemtotal = getFloatValue(htem, hhtde.getMadetotal());
			final Float hhhhhteatotal = getFloatValue(htea, hhtde.getAttemptstotal());
			final Float hatemtotal = getFloatValue(atem, htde.getOppmadetotal());
			final Float hateatotal = getFloatValue(atea, htde.getOppattemptstotal());
			final Float hhhhatemtotal = getFloatValue(atem, hhtde.getOppmadetotal());
			final Float hhhhateatotal = getFloatValue(atea, hhtde.getOppattemptstotal());

			Float ated = null;
			if (atemtotal == 0 && ateatotal == 0) {
				ated = new Float(0); 
			} else {
				ated = new Float(atemtotal/ateatotal);
			}
			Float aated = null;
			if (aatemtotal == 0 && aateatotal == 0) {
				aated = new Float(0); 
			} else {
				aated = new Float(aatemtotal/aateatotal);
			}
			Float aoppted = null;
			if (htemtotal == 0 && hteatotal == 0) {
				aoppted = new Float(0); 
			} else {
				aoppted = new Float(htemtotal/hteatotal);
			}
			Float aaoppted = null;
			if (hhhhtemtotal == 0 && hhhhteatotal == 0) {
				aaoppted = new Float(0); 
			} else {
				aaoppted = new Float(hhhhtemtotal/hhhhteatotal);
			}
			Float aopptedtotal = null;
			if (atemtotal == 0 && ateatotal == 0) {
				aopptedtotal = new Float(0); 
			} else {
				aopptedtotal = new Float(atemtotal/ateatotal);
			}
			Float aaopptedtotal = null;
			if (aatemtotal == 0 && aateatotal == 0) {
				aaopptedtotal = new Float(0); 
			} else {
				aaopptedtotal = new Float(aatemtotal/aateatotal);
			}
			Float hopptedtotal = null;
			if (htemtotal == 0 && hteatotal == 0) {
				hopptedtotal = new Float(0); 
			} else {
				hopptedtotal = new Float(htemtotal/hteatotal);
			}
			Float hhhhopptedtotal = null;
			if (hhhhtemtotal == 0 && hhhhteatotal == 0) {
				hhhhopptedtotal = new Float(0); 
			} else {
				hhhhopptedtotal = new Float(hhhhtemtotal/hhhhteatotal);
			}
			Float hted = null;
			if (hhtemtotal == 0 && hhteatotal == 0) {
				hted = new Float(0); 
			} else {
				hted = new Float(hhtemtotal/hhteatotal);
			}
			Float hhhhted = null;
			if (hhhhhtemtotal == 0 && hhhhhteatotal == 0) {
				hhhhted = new Float(0); 
			} else {
				hhhhted = new Float(hhhhhtemtotal/hhhhhteatotal);
			}
			Float hoppted = null;
			if (hatemtotal == 0 && hateatotal == 0) {
				hoppted = new Float(0); 
			} else {
				hoppted = new Float(hatemtotal/hateatotal);
			}
			Float hhhhoppted = null;
			if (hhhhatemtotal == 0 && hhhhateatotal == 0) {
				hhhhoppted = new Float(0); 
			} else {
				hhhhoppted = new Float(hhhhatemtotal/hhhhateatotal);
			}
			Float hhopptedtotal = null;
			if (hhtemtotal == 0 && hhteatotal == 0) {
				hhopptedtotal = new Float(0); 
			} else {
				hhopptedtotal = new Float(hhtemtotal/hhteatotal);
			}
			Float hhhhhopptedtotal = null;
			if (hhhhatemtotal == 0 && hhhhateatotal == 0) {
				hhhhhopptedtotal = new Float(0); 
			} else {
				hhhhhopptedtotal = new Float(hhhhatemtotal/hhhhateatotal);
			}
			Float hhhopptedtotal = null;
			if (hatemtotal == 0 && hateatotal == 0) {
				hhhopptedtotal = new Float(0); 
			} else {
				hhhopptedtotal = new Float(hatemtotal/hateatotal);
			}
			Float hhhhhhopptedtotal = null;
			if (hhhhatemtotal == 0 && hhhhateatotal == 0) {
				hhhhhhopptedtotal = new Float(0); 
			} else {
				hhhhhhopptedtotal = new Float(hhhhatemtotal/hhhhateatotal);
			}

			DATAMINERDB.persistFootballthirddowneffpergame(game.getWeek(), 
					game.getYear(), 
					"all", 
					acollegename, 
					ated, 
					aoppted, 
					aopptedtotal, 
					hopptedtotal, 
					anumgames,
					atem,
					atea,
					htem,
					htea,
					getFloatValue(atem, atde.getMadetotal()),
					getFloatValue(atea, atde.getAttemptstotal()),
					getFloatValue(htem, atde.getOppmadetotal()),
					getFloatValue(htea, atde.getOppattemptstotal()));
			DATAMINERDB.persistFootballthirddowneffpergame(game.getWeek(), 
					game.getYear(), 
					"all", 
					hcollegename, 
					hted,
					hoppted, 
					hhopptedtotal, 
					hhhopptedtotal,
					hnumgames,
					htem,
					htea,
					atem,
					atea,
					getFloatValue(htem, htde.getMadetotal()),
					getFloatValue(htea, htde.getAttemptstotal()),
					getFloatValue(atem, htde.getOppmadetotal()),
					getFloatValue(atea, htde.getOppattemptstotal()));
			DATAMINERDB.persistFootballthirddowneffpergame(game.getWeek(), 
					game.getYear(), 
					"away", 
					acollegename, 
					aated, 
					aaoppted, 
					aaopptedtotal, 
					hhhhopptedtotal, 
					aanumgames,
					atem,
					atea,
					htem,
					htea,
					getFloatValue(atem, aatde.getMadetotal()),
					getFloatValue(atea, aatde.getAttemptstotal()),
					getFloatValue(htem, aatde.getOppmadetotal()),
					getFloatValue(htea, aatde.getOppattemptstotal()));
			DATAMINERDB.persistFootballthirddowneffpergame(game.getWeek(), 
					game.getYear(), 
					"home", 
					hcollegename, 
					hhhhted,
					hhhhoppted, 
					hhhhhopptedtotal, 
					hhhhhhopptedtotal,
					hhnumgames,
					htem,
					htea,
					atem,
					atea,
					getFloatValue(htem, hhtde.getMadetotal()),
					getFloatValue(htea, hhtde.getAttemptstotal()),
					getFloatValue(atem, hhtde.getOppmadetotal()),
					getFloatValue(atea, hhtde.getOppattemptstotal()));

			// Total Yards Per Game
			final Float atotalyardspergame = game.getAwaytotalyards().floatValue() * awayfinal;
			final Float htotalyardspergame = game.getHometotalyards().floatValue() * homefinal;
			final FloatData atyfd = DATAMINERDB.getFootballtotalyardspergame(week, year, "all", acollegename);
			final FloatData htyfd = DATAMINERDB.getFootballtotalyardspergame(week, year, "all", hcollegename);
			final FloatData aatyfd = DATAMINERDB.getFootballtotalyardspergame(week, year, "away", acollegename);
			final FloatData hhtyfd = DATAMINERDB.getFootballtotalyardspergame(week, year, "home", hcollegename);
			final Float aty = getFloatValue(atotalyardspergame, atyfd.getFloatdatatotal());
			final Float aoppty = getFloatValue(htotalyardspergame, atyfd.getOppfloatdatatotal());
			final Float aaty = getFloatValue(atotalyardspergame, aatyfd.getFloatdatatotal());
			final Float aaoppty = getFloatValue(htotalyardspergame, aatyfd.getOppfloatdatatotal());
			final Float hty = getFloatValue(htotalyardspergame, htyfd.getFloatdatatotal());
			final Float hoppty = getFloatValue(atotalyardspergame, htyfd.getOppfloatdatatotal());
			final Float hhty = getFloatValue(htotalyardspergame, hhtyfd.getFloatdatatotal());
			final Float hhoppty = getFloatValue(atotalyardspergame, hhtyfd.getOppfloatdatatotal());
			DATAMINERDB.persistFootballtotalyardspergame(game.getWeek(), game.getYear(), "all", acollegename, new Float(aty/anumgames), new Float(aoppty/anumgames), aty, aoppty, anumgames);
			DATAMINERDB.persistFootballtotalyardspergame(game.getWeek(), game.getYear(), "all", hcollegename, new Float(hty/hnumgames), new Float(hoppty/hnumgames), hty, hoppty, hnumgames);				
			DATAMINERDB.persistFootballtotalyardspergame(game.getWeek(), game.getYear(), "away", acollegename, new Float(aaty/aanumgames), new Float(aaoppty/aanumgames), aaty, aaoppty, aanumgames);
			DATAMINERDB.persistFootballtotalyardspergame(game.getWeek(), game.getYear(), "home", hcollegename, new Float(hhty/hhnumgames), new Float(hhoppty/hhnumgames), hhty, hhoppty, hhnumgames);				

			// Turnovers Per Game
			final Float aturnoverspergame = game.getAwayturnovers().floatValue() * awayfinal;
			final Float hturnoverspergame = game.getHometurnovers().floatValue() * homefinal;
			final FloatData atfd = DATAMINERDB.getFootballturnoverspergame(year, "all", acollegename);  
			final FloatData htfd = DATAMINERDB.getFootballturnoverspergame(year, "all", hcollegename);
			final FloatData aatfd = DATAMINERDB.getFootballturnoverspergame(year, "away", acollegename);  
			final FloatData hhtfd = DATAMINERDB.getFootballturnoverspergame(year, "home", hcollegename);
			final Float at = getFloatValue(aturnoverspergame, atfd.getFloatdatatotal());
			final Float aoppt = getFloatValue(hturnoverspergame, atfd.getOppfloatdatatotal());
			final Float aat = getFloatValue(aturnoverspergame, aatfd.getFloatdatatotal());
			final Float aaoppt = getFloatValue(hturnoverspergame, aatfd.getOppfloatdatatotal());
			final Float ht = getFloatValue(hturnoverspergame, htfd.getFloatdatatotal());
			final Float hoppt = getFloatValue(aturnoverspergame, htfd.getOppfloatdatatotal());
			final Float hht = getFloatValue(hturnoverspergame, hhtfd.getFloatdatatotal());
			final Float hhoppt = getFloatValue(aturnoverspergame, hhtfd.getOppfloatdatatotal());
			DATAMINERDB.persistFootballturnoverspergame(game.getWeek(), game.getYear(), "all", acollegename, new Float(at/anumgames), new Float(aoppt/anumgames), at, aoppt, anumgames);
			DATAMINERDB.persistFootballturnoverspergame(game.getWeek(), game.getYear(), "all", hcollegename, new Float(ht/hnumgames), new Float(hoppt/hnumgames), ht, hoppt, hnumgames);
			DATAMINERDB.persistFootballturnoverspergame(game.getWeek(), game.getYear(), "away", acollegename, new Float(aat/aanumgames), new Float(aaoppt/aanumgames), aat, aaoppt, aanumgames);
			DATAMINERDB.persistFootballturnoverspergame(game.getWeek(), game.getYear(), "home", hcollegename, new Float(hht/hhnumgames), new Float(hhoppt/hhnumgames), hht, hhoppt, hhnumgames);

			// Yards Per Pass
			final Float ayardsperpass = game.getAwayyardsperpass().floatValue() * awayfinal;
			final Float hyardsperpass = game.getHomeyardsperpass().floatValue() * homefinal;
			final FloatData ayfd = DATAMINERDB.getFootballyardsperpass(year, "all", acollegename);  
			final FloatData hyfd = DATAMINERDB.getFootballyardsperpass(year, "all", hcollegename);
			final FloatData aayfd = DATAMINERDB.getFootballyardsperpass(year, "away", acollegename);  
			final FloatData hhyfd = DATAMINERDB.getFootballyardsperpass(year, "home", hcollegename);
			final Float ay = getFloatValue(ayardsperpass, ayfd.getFloatdatatotal()); 
			final Float aoppy = getFloatValue(hyardsperpass, ayfd.getOppfloatdatatotal());
			final Float aay = getFloatValue(ayardsperpass, aayfd.getFloatdatatotal()); 
			final Float aaoppy = getFloatValue(hyardsperpass, aayfd.getOppfloatdatatotal());
			final Float hy = getFloatValue(hyardsperpass, hyfd.getFloatdatatotal());
			final Float hoppy = getFloatValue(ayardsperpass, hyfd.getOppfloatdatatotal());
			final Float hhy = getFloatValue(hyardsperpass, hhyfd.getFloatdatatotal());
			final Float hhoppy = getFloatValue(ayardsperpass, hhyfd.getOppfloatdatatotal());
			DATAMINERDB.persistFootballyardsperpass(game.getWeek(), game.getYear(), "all", acollegename, new Float(ay/anumgames), new Float(aoppy/anumgames), ay, aoppy, anumgames);
			DATAMINERDB.persistFootballyardsperpass(game.getWeek(), game.getYear(), "all", hcollegename, new Float(hy/hnumgames), new Float(hoppy/hnumgames), hy, hoppy, hnumgames);
			DATAMINERDB.persistFootballyardsperpass(game.getWeek(), game.getYear(), "away", acollegename, new Float(aay/aanumgames), new Float(aaoppy/aanumgames), aay, aaoppy, aanumgames);
			DATAMINERDB.persistFootballyardsperpass(game.getWeek(), game.getYear(), "home", hcollegename, new Float(hhy/hhnumgames), new Float(hhoppy/hhnumgames), hhy, hhoppy, hhnumgames);

			// Yards Per Rush
			final Float ayardsperrush = game.getAwayyardsperrush().floatValue() * awayfinal;
			final Float hyardsperrush = game.getHomeyardsperrush().floatValue() * homefinal;
			final FloatData arfd = DATAMINERDB.getFootballyardsperrush(year, "all", acollegename);  
			final FloatData hrfd = DATAMINERDB.getFootballyardsperrush(year, "all", hcollegename);
			final FloatData aarfd = DATAMINERDB.getFootballyardsperrush(year, "away", acollegename);  
			final FloatData hhrfd = DATAMINERDB.getFootballyardsperrush(year, "home", hcollegename);
			final Float ar = getFloatValue(ayardsperrush, arfd.getFloatdatatotal());
			final Float aoppr = getFloatValue(hyardsperrush, arfd.getOppfloatdatatotal());
			final Float aar = getFloatValue(ayardsperrush, aarfd.getFloatdatatotal());
			final Float aaoppr = getFloatValue(hyardsperrush, aarfd.getOppfloatdatatotal());
			final Float hr = getFloatValue(hyardsperrush, hrfd.getFloatdatatotal());
			final Float hoppr = getFloatValue(ayardsperrush, hrfd.getOppfloatdatatotal());
			final Float hhr = getFloatValue(hyardsperrush, hhrfd.getFloatdatatotal());
			final Float hhoppr = getFloatValue(ayardsperrush, hhrfd.getOppfloatdatatotal());
			DATAMINERDB.persistFootballyardsperrush(game.getWeek(), game.getYear(), "all", acollegename, new Float(ar/anumgames), new Float(aoppr/anumgames), ar, aoppr, anumgames);
			DATAMINERDB.persistFootballyardsperrush(game.getWeek(), game.getYear(), "all", hcollegename, new Float(hr/hnumgames), new Float(hoppr/hnumgames), hr, hoppr, hnumgames);
			DATAMINERDB.persistFootballyardsperrush(game.getWeek(), game.getYear(), "away", acollegename, new Float(aar/aanumgames), new Float(aaoppr/aanumgames), aar, aaoppr, aanumgames);
			DATAMINERDB.persistFootballyardsperrush(game.getWeek(), game.getYear(), "home", hcollegename, new Float(hhr/hhnumgames), new Float(hhoppr/hhnumgames), hhr, hhoppr, hhnumgames);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting storeGameData()");
	}
}