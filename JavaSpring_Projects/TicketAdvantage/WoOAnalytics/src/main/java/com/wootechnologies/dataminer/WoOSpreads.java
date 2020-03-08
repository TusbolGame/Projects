/**
 * 
 */
package com.wootechnologies.dataminer;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wootechnologies.dataminer.db.DataMinerDB;
import com.wootechnologies.errorhandling.BatchException;
import com.wootechnologies.services.dao.sites.espn.EspnCollegeFootballGameData;
import com.wootechnologies.services.dao.sites.espn.EspnGameData;
import com.wootechnologies.services.dao.sites.vegasinsider.VegasInsiderGame;

/**
 * @author jmiller
 *
 */
public abstract class WoOSpreads  {
	private static final Logger LOGGER = Logger.getLogger(WoOSpreads.class);
	protected DataMinerDB DATAMINERDB;
	protected Double COMMON_TEAM_FACTOR = new Double(1.0);
	protected Integer MAX_MASSEY_FACTOR = new Integer(1);
	protected boolean USE_WEEK_RANKINGS = false;
	protected boolean USE_MIX_RANKINGS = false;
	protected boolean SHOW_INFO = false;
	protected Integer masseyweek;
	protected List<VegasInsiderGame> vegasInsiderGames;
	protected static final String[] ALL_SPREADS_HEADER = new String[] {
			"Game Date", 
			"Week", 
			"Away Rot#", 
			"Home Rot#", 
			"Away Team", 
			"Home Team", 
			"Away Score", 
			"Home Score", 
			"Game Delta", 
			"Line Favorite", 
			"Line", 
			"Win/Loss", 
			"Line Win/Loss", 
			"Large Win/Loss", 
			"WoO Avg Spread", 
			"WoO Avg Spread Week", 
			"WoO Avg Spread Blended", 
			"WoO Avg Spread No Week", 
			"WoO Year Spread", 
			"WoO Last1", 
			"WoO Last2", 
			"WoO Last3", 
			"WoO Last4", 
			"WoO Last5", 
			"WoO Last6", 
			"WoO AH Last1", 
			"WoO AH Last2", 
			"WoO AH Last3", 
			"WoO AH Last4", 
			"WoO AH Last5", 
			"WoO AH Last6"
	};

	/**
	 * 
	 * @param lastx
	 * @param useyear
	 * @param uselastx
	 * @param uselasthomeawayx
	 * @param seasonyear
	 * @param start
	 * @param end
	 * @param filename
	 */
	public void determineSpreadsAverageAll(int[] lastx, 
			boolean useyear, 
			boolean uselastx, 
			boolean uselasthomeawayx, 
			Integer seasonyear, 
			Date start, 
			Date end, 
			String filename) {
		LOGGER.info("Entering determineSpreadsAverageAll()");
		LOGGER.debug("seasonyear: " + seasonyear);
		LOGGER.debug("start: " + start);
		LOGGER.debug("end: " + end);

		// Setup Massey Ratings
		setupMasseyRatings(seasonyear + 1, start, end);
		final PrintWriter printWriter = startAvgAlgorithm(DATAMINERDB, filename);
		final List<VegasInsiderGame> vigs = getVegasInsiderGames(seasonyear, start, end);

		for (VegasInsiderGame vigGame : vigs) {
			if (vigGame.getLine() != null && vigGame.getLinefavorite() != null && vigGame.getLinefavorite().length() > 0) {
				try {
					if (SHOW_INFO) {
						LOGGER.error("vigGame: " + vigGame);
					}
					final String awayname = vigGame.getAwayteamdata().getTeamname().toUpperCase(); 
					final String homename = vigGame.getHometeamdata().getTeamname().toUpperCase();
					final boolean awayfound = foundTeam(awayname);
					final boolean homefound = foundTeam(homename);
					masseyweek = vigGame.getWeek();

					if ((awayname != null || homename != null) && (awayfound || homefound)) {
						Double regularspread = 0.0;
						Double spreadweek = 0.0;
						Double spreadmix = 0.0;
						Double spreadnoweek = 0.0;
						Double spreadtotal = 0.0;
						final List<Double> spreads = new ArrayList<Double>();

						for (int i = 0; i < 3; i++) {
							spreads.clear();

							if (i == 0) {
								USE_WEEK_RANKINGS = true;
								USE_MIX_RANKINGS = false;
							} else if (i == 1) {
								USE_WEEK_RANKINGS = false;
								USE_MIX_RANKINGS = true;
							} else {
								USE_WEEK_RANKINGS = false;
								USE_MIX_RANKINGS = false;
							}

							if (useyear) {
								regularspread = runAlgorithm(seasonyear, 
										start, 
										end,
										vigGame.getAwayteamdata().getTeamname(),
										vigGame.getHometeamdata().getTeamname(), 
										vigGame.getDate());
								spreads.add(regularspread);
								clearList();
							}

							if (uselastx) {
								for (int x = 0; x < lastx.length; x++) {
									final Double spreadx = runGameAlgorithmLastX(lastx[x], 
											seasonyear, 
											start, 
											end,
											vigGame.getAwayteamdata().getTeamname(),
											vigGame.getHometeamdata().getTeamname(), 
											vigGame.getDate());
									spreads.add(spreadx);
									clearList();
								}
							}

							if (uselasthomeawayx) {
								for (int x = 0; x < lastx.length; x++) {
									final Double spreadhax = runGameAlgorithmLastXHomeAway(lastx[x], 
											seasonyear, 
											start,
											end, 
											vigGame.getAwayteamdata().getTeamname(),
											vigGame.getHometeamdata().getTeamname(), 
											vigGame.getDate());
									spreads.add(spreadhax);
									clearList();
								}
							}

							double totalspreads = 0.0;
							for (Double spread : spreads) {
								totalspreads += spread.doubleValue();
							}

							if (i == 0) {
								spreadweek = totalspreads / spreads.size();
								if (SHOW_INFO) {
									LOGGER.error("spreadweek: " + spreadweek);
								}
							} else if (i == 1) {
								spreadmix = totalspreads / spreads.size();
								if (SHOW_INFO) {
									LOGGER.error("spreadmix: " + spreadmix);
								}
							} else if (i == 2) {
								spreadnoweek = totalspreads / spreads.size();
								if (SHOW_INFO) {
									LOGGER.error("spreadnoweek: " + spreadnoweek);
								}
							}
						}

						spreadtotal = (spreadweek + spreadmix + spreadnoweek) / 3;
						if (SHOW_INFO) {
							LOGGER.error("spreadtotal: " + spreadtotal);
						}

						final Map<String, Integer> masseys = getMasseyRatings(vigGame);
						final Integer awaymassey = masseys.get("awaymassey");
						final Integer homemassey = masseys.get("homemassey");

						// Setup the end part
						endAvgTransactionAll(printWriter, 
								vigGame.getAwayteamdata().getRotationid(),
								vigGame.getHometeamdata().getRotationid(), 
								awaymassey, 
								homemassey, 
								spreadtotal,
								spreadweek, 
								spreadmix, 
								spreadnoweek, 
								regularspread, 
								spreads, 
								new Double(9.5),
								new Double(30), 
								vigGame);
					}
				} catch (Throwable t) {
					LOGGER.error(t.getMessage(), t);
				}
			}
		}

		LOGGER.info("Exiting determineSpreadsAverageAll()");
	}

	/**
	 * 
	 * @param headers
	 * @param lastx
	 * @param useyear
	 * @param uselastx
	 * @param uselasthomeawayx
	 * @param useweekrankings
	 * @param usemixrankings
	 * @param seasonyear
	 * @param start
	 * @param end
	 * @param filename
	 */
	public void determineSpreadsAverageDynamic(String[] headers, 
			int[] lastx, 
			boolean useyear, 
			boolean uselastx, 
			boolean uselasthomeawayx,  
			boolean useweekrankings, 
			boolean usemixrankings, 
			Integer seasonyear, 
			Date start, 
			Date end, 
			String filename) {
		LOGGER.info("Entering determineSpreadsAverageDynamic()");
		LOGGER.debug("seasonyear: " + seasonyear);
		LOGGER.debug("start: " + start);
		LOGGER.debug("end: " + end);

		// Setup Massey Ratings
		setupMasseyRatings(seasonyear + 1, start, end);
		final PrintWriter printWriter = startAlgorithmAvgDynamic(DATAMINERDB, headers, filename);
		final List<VegasInsiderGame> vigs = getVegasInsiderGames(seasonyear, start, end);

		for (VegasInsiderGame vigGame : vigs) {
			if (vigGame.getLine() != null && 
				vigGame.getLinefavorite() != null && 
				vigGame.getLinefavorite().length() > 0) {
				try {
					if (SHOW_INFO) {
						LOGGER.error("vigGame: " + vigGame);
					}
					final String awayname = vigGame.getAwayteamdata().getTeamname().toUpperCase(); 
					final String homename = vigGame.getHometeamdata().getTeamname().toUpperCase();
					final boolean awayfound = foundTeam(awayname);
					final boolean homefound = foundTeam(homename);
					masseyweek = vigGame.getWeek();

					if ((awayname != null || homename != null) && (awayfound || homefound)) {
						final List<Double> spreads = new ArrayList<Double>();
						Double spreadtotal = 0.0;
						USE_WEEK_RANKINGS = useweekrankings;
						USE_MIX_RANKINGS = usemixrankings;

						if (useyear) {
							final Double regularspread = runAlgorithm(seasonyear, 
									start, 
									end,
									vigGame.getAwayteamdata().getTeamname(), 
									vigGame.getHometeamdata().getTeamname(),
									vigGame.getDate());
							spreads.add(regularspread);
							clearList();
						}

						if (uselastx) {
							for (int x = 0; x < lastx.length; x++) {
								final Double spreadx = runGameAlgorithmLastX(lastx[x], 
										seasonyear, 
										start, 
										end,
										vigGame.getAwayteamdata().getTeamname(),
										vigGame.getHometeamdata().getTeamname(), 
										vigGame.getDate());
								spreads.add(spreadx);
								clearList();
							}
						}

						if (uselasthomeawayx) {
							for (int x = 0; x < lastx.length; x++) {
								final Double spreadhax = runGameAlgorithmLastXHomeAway(lastx[x], 
										seasonyear, 
										start, 
										end,
										vigGame.getAwayteamdata().getTeamname(),
										vigGame.getHometeamdata().getTeamname(), 
										vigGame.getDate());
								spreads.add(spreadhax);
								clearList();
							}
						}

						double totalspreads = 0.0;
						for (Double spread : spreads) {
							totalspreads += spread.doubleValue();
						}

						spreadtotal = totalspreads / spreads.size();
						if (SHOW_INFO) {
							LOGGER.error("spreadtotal: " + spreadtotal);
						}

						final Map<String, Integer> masseys = getMasseyRatings(vigGame);
						final Integer awaymassey = masseys.get("awaymassey");
						final Integer homemassey = masseys.get("homemassey");

						endAvgTransactionDynamic(printWriter, 
								vigGame.getAwayteamdata().getRotationid(),
								vigGame.getHometeamdata().getRotationid(), 
								awaymassey, 
								homemassey, 
								spreadtotal, 
								spreads,
								new Double(9.5), 
								new Double(30), 
								vigGame);
					}
				} catch (Throwable t) {
					LOGGER.error(t.getMessage(), t);
				}
			}
		}

		LOGGER.info("Exiting determineSpreadsAverageDynamic()");
	}

	/**
	 * 
	 * @param seasonyear
	 * @param start
	 * @param end
	 * @param filename
	 */
	public void determineSpreadsAverage(Integer seasonyear, Date start, Date end, String filename) {
		LOGGER.info("Entering determineSpreadsAverage()");
		LOGGER.debug("seasonyear: " + seasonyear);
		LOGGER.debug("start: " + start);
		LOGGER.debug("end: " + end);

		// Setup Massey Ratings
		setupMasseyRatings(seasonyear + 1, start, end);
		final PrintWriter printWriter = startAvgAlgorithm(DATAMINERDB, filename);
		final List<VegasInsiderGame> vigs = getVegasInsiderGames(seasonyear, start, end);

		for (VegasInsiderGame vigGame : vigs) {
			if (vigGame.getLine() != null && vigGame.getLinefavorite() != null && vigGame.getLinefavorite().length() > 0) {
				try {
					if (SHOW_INFO) {
						LOGGER.error("vigGame: " + vigGame);
					}
					final String awayname = vigGame.getAwayteamdata().getTeamname().toUpperCase(); 
					final String homename = vigGame.getHometeamdata().getTeamname().toUpperCase();
					final boolean awayfound = foundTeam(awayname);
					final boolean homefound = foundTeam(homename);
					masseyweek = vigGame.getWeek();

					if ((awayname != null || homename != null) && (awayfound || homefound)) {
						clearList();

						Double spread = 0.0;
						Double spreadweek = 0.0;
						Double spreadmix = 0.0;
						Double spreadnoweek = 0.0;
					    USE_WEEK_RANKINGS = true;
					    USE_MIX_RANKINGS = false;

						Double regularspread = runAlgorithm(seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						Double spread1 = runGameAlgorithmLastX(1, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						Double spread2 = runGameAlgorithmLastX(2, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						Double spread3 = runGameAlgorithmLastX(3, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						Double spread4 = runGameAlgorithmLastX(4, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						Double spread5 = runGameAlgorithmLastX(5, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						Double spread6 = runGameAlgorithmLastX(6, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						Double spreadha1 = runGameAlgorithmLastXHomeAway(1, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						Double spreadha2 = runGameAlgorithmLastXHomeAway(2, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						Double spreadha3 = runGameAlgorithmLastXHomeAway(3, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						Double spreadha4 = runGameAlgorithmLastXHomeAway(4, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						Double spreadha5 = runGameAlgorithmLastXHomeAway(5, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						Double spreadha6 = runGameAlgorithmLastXHomeAway(6, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						
						spreadweek = (regularspread + spread1 + spread2 + spread3 + spread4 + spread5 + spread6 + spreadha1 + spreadha2 + spreadha3 + spreadha4 + spreadha5 + spreadha6) / 13;
						if (SHOW_INFO) {
							LOGGER.error("spreadweek: " + spread);
						}

					    USE_WEEK_RANKINGS = false;
					    USE_MIX_RANKINGS = true;
						regularspread = runAlgorithm(seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spread1 = runGameAlgorithmLastX(1, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spread2 = runGameAlgorithmLastX(2, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spread3 = runGameAlgorithmLastX(3, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spread4 = runGameAlgorithmLastX(4, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spread5 = runGameAlgorithmLastX(5, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spread6 = runGameAlgorithmLastX(6, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spreadha1 = runGameAlgorithmLastXHomeAway(1, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spreadha2 = runGameAlgorithmLastXHomeAway(2, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spreadha3 = runGameAlgorithmLastXHomeAway(3, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spreadha4 = runGameAlgorithmLastXHomeAway(4, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spreadha5 = runGameAlgorithmLastXHomeAway(5, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spreadha6 = runGameAlgorithmLastXHomeAway(6, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						
						spreadmix = (regularspread + spread1 + spread2 + spread3 + spread4 + spread5 + spread6 + spreadha1 + spreadha2 + spreadha3 + spreadha4 + spreadha5 + spreadha6) / 13;
						if (SHOW_INFO) {
							LOGGER.error("spreadmix: " + spreadmix);
						}
						
					    USE_WEEK_RANKINGS = false;
					    USE_MIX_RANKINGS = false;
						regularspread = runAlgorithm(seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spread1 = runGameAlgorithmLastX(1, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spread2 = runGameAlgorithmLastX(2, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spread3 = runGameAlgorithmLastX(3, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spread4 = runGameAlgorithmLastX(4, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spread5 = runGameAlgorithmLastX(5, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spread6 = runGameAlgorithmLastX(6, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spreadha1 = runGameAlgorithmLastXHomeAway(1, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spreadha2 = runGameAlgorithmLastXHomeAway(2, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spreadha3 = runGameAlgorithmLastXHomeAway(3, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spreadha4 = runGameAlgorithmLastXHomeAway(4, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spreadha5 = runGameAlgorithmLastXHomeAway(5, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						spreadha6 = runGameAlgorithmLastXHomeAway(6, seasonyear, start, end, vigGame.getAwayteamdata().getTeamname(), vigGame.getHometeamdata().getTeamname(), vigGame.getDate());
						clearList();
						
						spreadnoweek = (regularspread + spread1 + spread2 + spread3 + spread4 + spread5 + spread6 + spreadha1 + spreadha2 + spreadha3 + spreadha4 + spreadha5 + spreadha6) / 13;
						if (SHOW_INFO) {
							LOGGER.error("spreadnoweek: " + spreadnoweek);
						}

						spread = (spreadweek + spreadmix + spreadnoweek) / 3;
						if (SHOW_INFO) {
							LOGGER.error("spread: " + spread);
						}

						final Map<String, Integer> masseys = getMasseyRatings(vigGame);
						final Integer awaymassey = masseys.get("awaymassey");
						final Integer homemassey = masseys.get("homemassey");

						// Setup the end part
						endAvgTransaction(printWriter, 
								vigGame.getAwayteamdata().getRotationid(),
								vigGame.getHometeamdata().getRotationid(),
								awaymassey,
								homemassey,
								spread, 
								spreadweek,
								spreadmix,
								spreadnoweek,
								regularspread, 
								spread1,
								spread2,
								spread3,
								spread4,
								spread5,
								spread6,
								spreadha1,
								spreadha2,
								spreadha3,
								spreadha4,
								spreadha5,
								spreadha6,
								new Double(9.5), 
								new Double(30), 
								vigGame);
					} else {
						LOGGER.error("Could not find away: " + awayname + " home: " + homename); 
					}
				} catch (Throwable t) {
					LOGGER.debug("Cannot get spread for " + vigGame.getAwayteamdata().getTeamname() + " " 
							+ vigGame.getHometeamdata().getTeamname() + " " + vigGame.getAwayteamdata().getFinalscore() + " " 
							+ vigGame.getHometeamdata().getFinalscore() + " Line: " + vigGame.getLinefavorite() + " " 
							+ vigGame.getLine() + " week: " + vigGame.getWeek());
					LOGGER.error(t.getMessage(), t);
				}
			}
		}

		// Close the connection
		closeConnections(DATAMINERDB, printWriter);

		LOGGER.info("Exiting determineSpreadsAverage()");
	}

	/**
	 * 
	 * @param DATAMINERDB
	 * @param filename
	 * @return
	 */
	protected PrintWriter startAlgorithm(DataMinerDB DATAMINERDB, String filename) {
		PrintWriter printWriter = null;

		// Open the connection
		DATAMINERDB.start();

		try {
			final FileWriter fileWriter = new FileWriter(filename);
			printWriter = new PrintWriter(fileWriter);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		// Header
		printWriter.print("Away Team, Home Team, Away Score, Home Score, Game Delta, Line Favorite, Line, WoO Spread, Week, Win/Loss, Line Win/Loss, Large Win/Loss, Game Date\n");

		return printWriter;
	}

	/**
	 * 
	 * @param DATAMINERDB
	 * @param filename
	 * @return
	 */
	protected PrintWriter startAlgorithmAvgDynamic(DataMinerDB DATAMINERDB, String[] headers, String filename) {
		PrintWriter printWriter = null;

		// Open the connection
		DATAMINERDB.start();

		try {
			final FileWriter fileWriter = new FileWriter(filename);
			printWriter = new PrintWriter(fileWriter);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		final StringBuffer sb = new StringBuffer(200);
		for (int x = 0; x < headers.length; x++) {
			sb.append(headers[x]).append(",");
		}
		sb.append("\n");

		// Header
		printWriter.print(sb.toString());

		return printWriter;
	}

	/**
	 * 
	 * @param DATAMINERDB
	 * @param filename
	 * @return
	 */
	protected PrintWriter startAvgAlgorithm(DataMinerDB DATAMINERDB, String filename) {
		PrintWriter printWriter = null;

		// Open the connection
		DATAMINERDB.start();

		try {
			final FileWriter fileWriter = new FileWriter(filename);
			printWriter = new PrintWriter(fileWriter);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		// Header
		printWriter.print("Game Date, Week, Away Rot#, Home Rot#, Away Team, Home Team, Away Score, Home Score, Game Delta, Line Favorite, Line, Win/Loss, Line Win/Loss, Large Win/Loss, WoO Avg Spread, WoO Avg Spread Week, WoO Avg Spread Blended, WoO Avg Spread No Week, WoO Year Spread, WoO Last1, WoO Last2, WoO Last3, WoO Last4, WoO Last5, WoO Last6, WoO AH Last1, WoO AH Last2, WoO AH Last3, WoO AH Last4, WoO AH Last5, WoO AH Last6\n");

		return printWriter;
	}

	/**
	 * 
	 * @param printWriter
	 * @param awayrot
	 * @param homerot
	 * @param awaymassey
	 * @param homemassey
	 * @param woospread
	 * @param spreadweek
	 * @param spreadmix
	 * @param spreadnoweek
	 * @param wooyearspread
	 * @param woospreads
	 * @param deltaValue
	 * @param largeValue
	 * @param vig
	 */
	protected void endAvgTransactionAll(PrintWriter printWriter, 
			Integer awayrot,
			Integer homerot,
			Integer awaymassey,
			Integer homemassey,
			Double woospread,
			Double spreadweek,
			Double spreadmix,
			Double spreadnoweek,
			Double wooyearspread,
			List<Double> woospreads, 
			Double deltaValue, 
			Double largeValue, 
			VegasInsiderGame vig) {
		if (woospread == null) {
			LOGGER.debug("Cannot get spread for " + vig.getAwayteamdata().getTeamname() + " " + vig.getHometeamdata().getTeamname() + " " + vig.getAwayteamdata().getFinalscore() + " " + vig.getHometeamdata().getFinalscore()
					+ " Line: " + vig.getLinefavorite() + " " + vig.getLine() + " week: " + vig.getWeek());
		} else {
			LOGGER.debug("vig.getHomeshortname(): " + vig.getHometeamdata().getTeamname());
			LOGGER.debug("vig.getAwayshortname(): " + vig.getAwayteamdata().getTeamname());
			LOGGER.debug("vig.getLinefavorite(): " + vig.getLinefavorite());
			final Integer homefinalscore = vig.getHometeamdata().getFinalscore();
			final Integer awayfinalscore = vig.getAwayteamdata().getFinalscore();

			// Determine if spread was covered
			final String outcome = didWinSpread(awayfinalscore, homefinalscore, woospread, vig);

			if (SHOW_INFO) {
				LOGGER.error(vig.getAwayteamdata().getTeamname() + " vs " + vig.getHometeamdata().getTeamname() + "," + 
				awayfinalscore + "," + 
				homefinalscore + "," + 
				vig.getLinefavorite() + "," + 
				vig.getLine() + "," + 
				woospread + "," +
				spreadweek + "," +
				spreadmix + "," +
				spreadnoweek + "," +
				wooyearspread + "," +
				vig.getWeek() + "," + 
				outcome);
			}

			// Get score delta
			final Integer gamedelta = determineScoreDelta(homefinalscore, awayfinalscore, vig);

			// Get the spread > deltaValue
			final String vvalue = determineDeltaLine(woospread, deltaValue, outcome, vig);

			// Now check the large values
			final String lvalue = determineLargeLine(woospread, deltaValue, largeValue, outcome, vig);

			final StringBuffer sb = new StringBuffer(500);
			sb.append(vig.getDate()).append(",");
			sb.append(vig.getWeek()).append(",");
			sb.append(awayrot).append(",");
			sb.append(homerot).append(",");
			sb.append("(" + awaymassey + ")" + vig.getAwayteamdata().getTeamname()).append(",");
			sb.append("(" + homemassey + ")" + vig.getHometeamdata().getTeamname()).append(",");
			sb.append(awayfinalscore).append(",");
			sb.append(homefinalscore).append(",");
			sb.append(gamedelta).append(",");
			sb.append(vig.getLinefavorite()).append(",");
			sb.append(vig.getLine()).append(",");
			sb.append(outcome).append(",");
			sb.append(vvalue).append(",");
			sb.append(lvalue).append(",");
			sb.append(woospread).append(",");
			sb.append(spreadweek).append(",");
			sb.append(spreadmix).append(",");
			sb.append(spreadnoweek).append(",");
			sb.append(wooyearspread).append(",");

			for (Double woospreadnum : woospreads) {
				sb.append(woospreadnum).append(",");
			}
			sb.append("\n");

			printWriter.print(sb.toString());
		}		
	}

	/**
	 * 
	 * @param printWriter
	 * @param awayrot
	 * @param homerot
	 * @param awaymassey
	 * @param homemassey
	 * @param woospread
	 * @param woospreads
	 * @param deltaValue
	 * @param largeValue
	 * @param vig
	 */
	protected void endAvgTransactionDynamic(PrintWriter printWriter, 
			Integer awayrot,
			Integer homerot,
			Integer awaymassey,
			Integer homemassey,
			Double woospread,
			List<Double> woospreads, 
			Double deltaValue, 
			Double largeValue, 
			VegasInsiderGame vig) {
		if (woospread == null) {
			LOGGER.debug("Cannot get spread for " + vig.getAwayteamdata().getTeamname() + 
					" " + vig.getHometeamdata().getTeamname() + " " + vig.getAwayteamdata().getFinalscore() + 
					" " + vig.getHometeamdata().getFinalscore() + " Line: " + vig.getLinefavorite() + 
					" " + vig.getLine() + " week: " + vig.getWeek());
		} else {
			LOGGER.debug("vig.getHomeshortname(): " + vig.getHometeamdata().getTeamname());
			LOGGER.debug("vig.getAwayshortname(): " + vig.getAwayteamdata().getTeamname());
			LOGGER.debug("vig.getLinefavorite(): " + vig.getLinefavorite());
			final Integer homefinalscore = vig.getHometeamdata().getFinalscore();
			final Integer awayfinalscore = vig.getAwayteamdata().getFinalscore();

			// Determine if spread was covered
			final String outcome = didWinSpread(awayfinalscore, homefinalscore, woospread, vig);

			// Get score delta
			final Integer gamedelta = determineScoreDelta(homefinalscore, awayfinalscore, vig);

			// Get the spread > deltaValue
			final String vvalue = determineDeltaLine(woospread, deltaValue, outcome, vig);

			// Now check the large values
			final String lvalue = determineLargeLine(woospread, deltaValue, largeValue, outcome, vig);

			final StringBuffer sb = new StringBuffer(500);
			sb.append(vig.getDate()).append(",");
			sb.append(vig.getWeek()).append(",");
			sb.append(awayrot).append(",");
			sb.append(homerot).append(",");
			sb.append("(" + awaymassey + ")" + vig.getAwayteamdata().getTeamname()).append(",");
			sb.append("(" + homemassey + ")" + vig.getHometeamdata().getTeamname()).append(",");
			sb.append(awayfinalscore).append(",");
			sb.append(homefinalscore).append(",");
			sb.append(gamedelta).append(",");
			sb.append(vig.getLinefavorite()).append(",");
			sb.append(vig.getLine()).append(",");
			sb.append(outcome).append(",");
			sb.append(vvalue).append(",");
			sb.append(lvalue).append(",");
			sb.append(woospread).append(",");

			for (Double woospreadnum : woospreads) {
				sb.append(woospreadnum).append(",");
			}
			sb.append("\n");

			if (SHOW_INFO) {
				LOGGER.error(sb.toString());
			}

			printWriter.print(sb.toString());
		}		
	}

	/**
	 * 
	 * @param printWriter
	 * @param awayrot
	 * @param homerot
	 * @param awaymassey
	 * @param homemassey
	 * @param woospread
	 * @param wooyearspread
	 * @param woospread1
	 * @param woospread2
	 * @param woospread3
	 * @param woospread4
	 * @param woospread5
	 * @param woospread6
	 * @param wooahspread1
	 * @param wooahspread2
	 * @param wooahspread3
	 * @param wooahspread4
	 * @param wooahspread5
	 * @param wooahspread6
	 * @param deltaValue
	 * @param largeValue
	 * @param vig
	 */
	protected void endAvgTransaction(PrintWriter printWriter, 
			Integer awayrot,
			Integer homerot,
			Integer awaymassey,
			Integer homemassey,
			Double woospread,
			Double spreadweek,
			Double spreadmix,
			Double spreadnoweek,
			Double wooyearspread,
			Double woospread1,
			Double woospread2,
			Double woospread3,
			Double woospread4,
			Double woospread5,
			Double woospread6,
			Double wooahspread1,
			Double wooahspread2,
			Double wooahspread3,
			Double wooahspread4,
			Double wooahspread5,
			Double wooahspread6,
			Double deltaValue, 
			Double largeValue, 
			VegasInsiderGame vig) {
		if (woospread == null) {
			LOGGER.debug("Cannot get spread for " + vig.getAwayteamdata().getTeamname() + " " + vig.getHometeamdata().getTeamname() + " " + vig.getAwayteamdata().getFinalscore() + " " + vig.getHometeamdata().getFinalscore()
					+ " Line: " + vig.getLinefavorite() + " " + vig.getLine() + " week: " + vig.getWeek());
		} else {
			LOGGER.debug("vig.getHomeshortname(): " + vig.getHometeamdata().getTeamname());
			LOGGER.debug("vig.getAwayshortname(): " + vig.getAwayteamdata().getTeamname());
			LOGGER.debug("vig.getLinefavorite(): " + vig.getLinefavorite());
			final Integer homefinalscore = vig.getHometeamdata().getFinalscore();
			final Integer awayfinalscore = vig.getAwayteamdata().getFinalscore();

			// Determine if spread was covered
			final String outcome = didWinSpread(awayfinalscore, homefinalscore, woospread, vig);

			if (SHOW_INFO) {
				LOGGER.error(vig.getAwayteamdata().getTeamname() + " vs " + vig.getHometeamdata().getTeamname() + "," + 
				awayfinalscore + "," + 
				homefinalscore + "," + 
				vig.getLinefavorite() + "," + 
				vig.getLine() + "," + 
				woospread + "," +
				spreadweek + "," +
				spreadmix + "," +
				spreadnoweek + "," +
				wooyearspread + "," +
				woospread1 + "," +
				woospread2 + "," +
				woospread3 + "," +
				woospread4 + "," +
				woospread5 + "," +
				woospread6 + "," +
				wooahspread1 + "," +
				wooahspread2 + "," +
				wooahspread3 + "," +
				wooahspread4 + "," +
				wooahspread5 + "," +
				wooahspread6 + "," +
				vig.getWeek() + "," + 
				outcome);
			}

			// Get score delta
			final Integer gamedelta = determineScoreDelta(homefinalscore, awayfinalscore, vig);

			// Get the spread > deltaValue
			final String vvalue = determineDeltaLine(woospread, deltaValue, outcome, vig);

			// Now check the large values
			final String lvalue = determineLargeLine(woospread, deltaValue, largeValue, outcome, vig);

			printWriter.print(
					vig.getDate() + "," + 
					vig.getWeek() + "," + 
					awayrot + "," + 
					homerot + "," +
					"(" + awaymassey + ")" + vig.getAwayteamdata().getTeamname() + "," + 
					"(" + homemassey + ")" + vig.getHometeamdata().getTeamname() + "," + 
					awayfinalscore + "," + 
					homefinalscore + "," + 
					gamedelta + "," + 
					vig.getLinefavorite() + "," + 
					vig.getLine() + "," + 
					outcome + "," + 
					vvalue + "," + 
					lvalue + "," + 
					woospread + "," +
					spreadweek + "," +
					spreadmix + "," +
					spreadnoweek + "," +		
					wooyearspread + "," +
					woospread1 + "," +
					woospread2 + "," +
					woospread3 + "," +
					woospread4 + "," +
					woospread5 + "," +
					woospread6 + "," +
					wooahspread1 + "," +
					wooahspread2 + "," +
					wooahspread3 + "," +
					wooahspread4 + "," +
					wooahspread5 + "," +
					wooahspread6 + "," + "\n");
		}		
	}

	/**
	 * 
	 * @param printWriter
	 * @param woospread
	 * @param deltaValue
	 * @param largeValue
	 * @param vig
	 */
	protected void endTransaction(PrintWriter printWriter, 
			Double woospread, 
			Double deltaValue, 
			Double largeValue, 
			VegasInsiderGame vig) {
		if (woospread == null) {
			LOGGER.debug("Cannot get spread for " + vig.getAwayteamdata().getTeamname() + " " + vig.getHometeamdata().getTeamname() + " " + vig.getAwayteamdata().getFinalscore() + " " + vig.getHometeamdata().getFinalscore()
					+ " Line: " + vig.getLinefavorite() + " " + vig.getLine() + " week: " + vig.getWeek());
		} else {
			LOGGER.debug("vig.getHomeshortname(): " + vig.getHometeamdata().getTeamname());
			LOGGER.debug("vig.getAwayshortname(): " + vig.getAwayteamdata().getTeamname());
			LOGGER.debug("vig.getLinefavorite(): " + vig.getLinefavorite());
			final Integer homefinalscore = vig.getHometeamdata().getFinalscore();
			final Integer awayfinalscore = vig.getAwayteamdata().getFinalscore();

			// Determine if spread was covered
			final String outcome = didWinSpread(awayfinalscore, homefinalscore, woospread, vig);

			if (SHOW_INFO) {
				LOGGER.error(vig.getAwayteamdata().getTeamname() + " vs " + vig.getHometeamdata().getTeamname() + "," + awayfinalscore + "," + homefinalscore
				+ "," + vig.getLinefavorite() + "," + vig.getLine() + "," + woospread + "," + vig.getWeek() + "," + outcome);
			}

			// Get score delta
			final Integer gamedelta = determineScoreDelta(homefinalscore, awayfinalscore, vig);

			// Get the spread > deltaValue
			final String vvalue = determineDeltaLine(woospread, deltaValue, outcome, vig);

			// Now check the large values
			final String lvalue = determineLargeLine(woospread, deltaValue, largeValue, outcome, vig);

			printWriter.print(vig.getAwayteamdata().getTeamname() + "," + vig.getHometeamdata().getTeamname() + "," + awayfinalscore + "," + homefinalscore
					+ "," + gamedelta + "," + vig.getLinefavorite() + "," + vig.getLine() + "," + woospread + "," + vig.getWeek() + "," + outcome + "," + vvalue + "," + lvalue + "," + vig.getDate() + "\n");
		}		
	}

	/**
	 * 
	 * @param awayteam
	 * @param hometeam
	 * @param awaygame
	 * @param homegame
	 * @throws BatchException
	 * @throws SQLException
	 */
	protected void processTeams(String awayteam, String hometeam, EspnGameData awaygame, EspnGameData homegame) throws BatchException, SQLException {
		if (SHOW_INFO) {
			LOGGER.error("awaygame: " + awaygame);
			LOGGER.error("homegame: " + homegame);
		}

		// First the teams rankings
		final Map<String, Integer> ratings = getTeamRatingsForTeams(awayteam, hometeam, awaygame, homegame);
		final Integer awaymassey = ratings.get("away");
		final Integer homemassey = ratings.get("home");

		// Then get the last game rankings
		final Map<String, Integer> lastGameRatings = lastGameRatings(USE_WEEK_RANKINGS, awaygame, homegame);
		final Integer awaygameawaymasseyrating = lastGameRatings.get("awayaway");
		final Integer awaygamehomemasseyrating = lastGameRatings.get("awayhome");
		final Integer homegameawaymasseyrating = lastGameRatings.get("homeaway");
		final Integer homegamehomemasseyrating = lastGameRatings.get("homehome");

		if (SHOW_INFO) {
			LOGGER.error(awayteam + " awaymassey: " + awaymassey);
			LOGGER.error(hometeam + " homemassey: " + homemassey);
			LOGGER.error(awayteam + " awaygameawaymasseyrating: " + awaygameawaymasseyrating);
			LOGGER.error(awayteam + " awaygamehomemasseyrating: " + awaygamehomemasseyrating);
			LOGGER.error(hometeam + " homegameawaymasseyrating: " + homegameawaymasseyrating);
			LOGGER.error(hometeam + " homegamehomemasseyrating: " + homegamehomemasseyrating);
		}

		Float awayfactor = Float.valueOf("1");
		Float aoppfactor = Float.valueOf("1");
		Float homefactor = Float.valueOf("1");
		Float hoppfactor = Float.valueOf("1");

		if (awaymassey != null && homemassey != null) {
			if (awaymassey.floatValue() == awaygameawaymasseyrating.floatValue()) {
				aoppfactor = new Float((1/Math.exp(awaygamehomemasseyrating.intValue() * 0.003846153846154)));
				awayfactor = new Float((1/Math.exp(awaymassey * 0.003846153846154)));
			} else {
				aoppfactor = new Float((1/Math.exp(awaygameawaymasseyrating.intValue() * 0.003846153846154)));
				awayfactor = new Float((1/Math.exp(awaymassey * 0.003846153846154)));
			}

			if (homemassey.floatValue() == homegameawaymasseyrating.floatValue()) {
				hoppfactor = new Float((1/Math.exp(homegamehomemasseyrating.intValue() * 0.003846153846154)));
				homefactor = new Float((1/Math.exp(homemassey * 0.003846153846154)));
			} else {
				hoppfactor = new Float((1/Math.exp(homegameawaymasseyrating.intValue() * 0.003846153846154)));
				homefactor = new Float((1/Math.exp(homemassey * 0.003846153846154)));
			}

			if (SHOW_INFO) {
				LOGGER.error(awayteam + " aoppfactor: " + aoppfactor);
				LOGGER.error(awayteam + " awayfactor: " + awayfactor);
				LOGGER.error(hometeam + " hoppfactor: " + hoppfactor);
				LOGGER.error(hometeam + " homefactor: " + homefactor);
			}

			// Process the game data
			processGameData(awayteam,
					hometeam,
					awaygame,
					homegame,
					awaymassey.floatValue(),
					homemassey.floatValue(),
					awayfactor, 
					aoppfactor, 
					homefactor, 
					hoppfactor);
		}		
	}

	/**
	 * 
	 * @param woospread
	 * @param deltaValue
	 * @param outcome
	 * @param vig
	 * @return
	 */
	protected String determineDeltaLine(Double woospread, Double deltaValue, String outcome, VegasInsiderGame vig) {
		String vvalue = "";
		final Float line = vig.getLine();
		
		if (vig.getAwayteamdata().getTeamname().equals(vig.getLinefavorite())) {
			if (woospread > 0) {
				float linediff = line.floatValue() + woospread.floatValue();
				if (Math.abs(linediff) > deltaValue) {
					if (outcome.equals("W")) {
						vvalue = "W";
					} else if (outcome.equals("L")) {
						vvalue = "L";
					} else if (outcome.equals("P")) {
						vvalue = "P";
					}
				}
			} else {
				float linediff = line.floatValue() - Math.abs(woospread.floatValue());
				if (Math.abs(linediff) > deltaValue) {
					if (outcome.equals("W")) {
						vvalue = "W";
					} else if (outcome.equals("L")) {
						vvalue = "L";
					} else if (outcome.equals("P")) {
						vvalue = "P";
					}
				}
			}
		} else {
			if (woospread > 0) {
				float linediff = line.floatValue() - woospread.floatValue();
				if (Math.abs(linediff) > deltaValue) {
					if (outcome.equals("W")) {
						vvalue = "W";
					} else if (outcome.equals("L")) {
						vvalue = "L";
					} else if (outcome.equals("P")) {
						vvalue = "P";
					}
				}
			} else {
				float linediff = Math.abs(woospread.floatValue()) + line.floatValue();
				if (Math.abs(linediff) > deltaValue) {
					if (outcome.equals("W")) {
						vvalue = "W";
					} else if (outcome.equals("L")) {
						vvalue = "L";
					} else if (outcome.equals("P")) {
						vvalue = "P";
					}
				}
			}
		}

		return vvalue;
	}

	/**
	 * 
	 * @param woospread
	 * @param deltaValue
	 * @param largeValue
	 * @param outcome
	 * @param vig
	 * @return
	 */
	protected String determineLargeLine(Double woospread, Double deltaValue, Double largeValue, String outcome, VegasInsiderGame vig) {
		String vvalue = "";
		final Float line = vig.getLine();
		
		if (vig.getAwayteamdata().getTeamname().equals(vig.getLinefavorite())) {
			if (woospread > 0) {
				float linediff = line.floatValue() + woospread.floatValue();
				if (Math.abs(linediff) > deltaValue && Math.abs(linediff) <= largeValue) {
					if (outcome.equals("W")) {
						vvalue = "W";
					} else if (outcome.equals("L")) {
						vvalue = "L";
					} else if (outcome.equals("P")) {
						vvalue = "P";
					}
				}
			} else {
				float linediff = line.floatValue() - Math.abs(woospread.floatValue());
				if (Math.abs(linediff) > deltaValue && Math.abs(linediff) <= largeValue) {
					if (outcome.equals("W")) {
						vvalue = "W";
					} else if (outcome.equals("L")) {
						vvalue = "L";
					} else if (outcome.equals("P")) {
						vvalue = "P";
					}
				}
			}
		} else {
			if (woospread > 0) {
				float linediff = line.floatValue() - woospread.floatValue();
				if (Math.abs(linediff) > deltaValue && Math.abs(linediff) <= largeValue) {
					if (outcome.equals("W")) {
						vvalue = "W";
					} else if (outcome.equals("L")) {
						vvalue = "L";
					} else if (outcome.equals("P")) {
						vvalue = "P";
					}
				}
			} else {
				float linediff = Math.abs(woospread.floatValue()) + line.floatValue();
				if (Math.abs(linediff) > deltaValue && Math.abs(linediff) <= largeValue) {
					if (outcome.equals("W")) {
						vvalue = "W";
					} else if (outcome.equals("L")) {
						vvalue = "L";
					} else if (outcome.equals("P")) {
						vvalue = "P";
					}
				}
			}
		}

		return vvalue;
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
	 * @param awayfinalscore
	 * @param homefinalscore
	 * @param woospread
	 * @param ncaabGame
	 * @return
	 */
	protected String didWinSpread(Integer awayfinalscore, Integer homefinalscore, Double woospread, VegasInsiderGame ncaabGame) {
		String outcome = "L";

		if (ncaabGame.getHometeamdata().getTeamname().equals(ncaabGame.getLinefavorite())) {
			int finalscore = homefinalscore - awayfinalscore;

			if (finalscore > 0 && finalscore == Math.abs(ncaabGame.getLine())) {
				outcome = "P";
			} else {
				// If we are negative this means the favorite for sure lost the spread
				if (finalscore < 0) {
					// Now check if the dog (i.e. Away team) was the WoO Spread choice
					if (woospread > ncaabGame.getLine().floatValue()) {
						outcome = "W";
					}
				} else {
					// The favorite won but did they cover
					if (finalscore > Math.abs(ncaabGame.getLine())) {
						// this means favorite covered, now was the WoO Spread on the favorite?
						if (woospread < ncaabGame.getLine().floatValue()) {
							outcome = "W";
						}
					} else {
						// Favorite didn't cover, now was the WoO Spread on the dog?
						if (woospread > ncaabGame.getLine().floatValue()) {
							outcome = "W";
						}
					}
				}
			}
		} else {
			int finalscore = awayfinalscore - homefinalscore;

			if (finalscore > 0 && finalscore == Math.abs(ncaabGame.getLine())) {
				outcome = "P";
			} else {
				// If we are negative this means the favorite for sure lost the spread
				if (finalscore < 0) {
					// Now check if the dog (i.e. Away team) was the WoO Spread choice
					if (woospread < Math.abs(ncaabGame.getLine().floatValue())) {
						outcome = "W";
					}
				} else {
					// The favorite won but did they cover
					if (finalscore > Math.abs(ncaabGame.getLine())) {
						// this means favorite covered, now was the WoO Spread on the favorite?
						if (woospread > Math.abs(ncaabGame.getLine().floatValue())) {
							outcome = "W";
						}
					} else {
						// Favorite didn't cover, now was the WoO Spread on the dog?
						if (woospread < Math.abs(ncaabGame.getLine().floatValue())) {
							outcome = "W";
						}
					}
				}
			}
		}

		return outcome;
	}

	/**
	 * 
	 * @param homefinalscore
	 * @param awayfinalscore
	 * @param ncaabGame
	 * @return
	 */
	protected Integer determineScoreDelta(Integer homefinalscore, Integer awayfinalscore, VegasInsiderGame ncaabGame) {
		Integer gamedelta = 0;

		// Check for home team vs. away team
		if (ncaabGame.getHometeamdata().getTeamname().equals(ncaabGame.getLinefavorite())) {
			gamedelta = homefinalscore - awayfinalscore;
		} else {
			gamedelta = awayfinalscore - homefinalscore;
		}

		return gamedelta;
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
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	protected Double setupStat(List<Double> list) {
		double objectlist = 0;

		for (Double object : list) {
			objectlist = objectlist + object.doubleValue();
		}

		return objectlist / list.size();
	}

	/**
	 * 
	 * @param top
	 * @param bottom
	 * @return
	 */
	protected float divideNumbers(float top, float bottom) {
		float thenumber = 0;
		
		if (bottom != 0) {
			thenumber = top/bottom;
		}

		return thenumber;
	}

	/**
	 * 
	 * @param aoppfactor
	 * @param awayfactor
	 * @param hoppfactor
	 * @param homefactor
	 * @param aoppvalue
	 * @param awayvalue
	 * @param hoppvalue
	 * @param homevalue
	 * @param factor
	 * @return
	 */
	protected Double determineValue(Float aoppfactor, 
			Float awayfactor, 
			Float hoppfactor, 
			Float homefactor,
			Float aoppvalue,
			Float awayvalue,
			Float hoppvalue,
			Float homevalue,
			Double factor) {
		Double value = null;
		Double avalue = null;
		Double hvalue = null;
		
		if (this.SHOW_INFO) {
			LOGGER.error("aoppfactor: " + aoppfactor);
			LOGGER.error("awayfactor: " + awayfactor);
			LOGGER.error("hoppfactor: " + hoppfactor);
			LOGGER.error("homefactor: " + homefactor);
		}

		avalue = calculateValue(awayfactor, awayfactor, awayvalue, aoppvalue, factor);
		hvalue = calculateValue(homefactor, hoppfactor, hoppvalue, homevalue, factor);

		if (SHOW_INFO) {
			LOGGER.error("avalue: " + avalue);
			LOGGER.error("hvalue: " + hvalue);
		}

		if (awayfactor > homefactor) {
			if (hvalue > 0 && avalue > 0) {
				value = hvalue + avalue;
			} else if (hvalue > 0 && avalue < 0) {
				value = avalue + hvalue;
			} else if (hvalue < 0 && avalue > 0) {
				value = avalue + hvalue;
			} else if (hvalue < 0 && avalue < 0) {
				value = hvalue - Math.abs(avalue);
			} else {
				value = avalue - hvalue;
			}
		} else {
			if (hvalue > 0 && avalue > 0) {
				value = hvalue + avalue;
			} else if (hvalue > 0 && avalue < 0) {
				value = avalue + hvalue;
			} else if (hvalue < 0 && avalue > 0) {
				value = avalue + hvalue;
			} else if (hvalue < 0 && avalue < 0) {
				value = hvalue - Math.abs(avalue);
			} else {
				value = hvalue - avalue;
			}
		}

		// Cap it
		if (value < -35) {
			value = -35.0;
		} else if (value > 35) {
			value = 35.0;
		}

		return value;
	}

	/**
	 * 
	 * @param awayfactor
	 * @param homefactor
	 * @param a
	 * @param aa
	 * @param h
	 * @param hh
	 * @param factor
	 * @return
	 */
	protected Double determineDataUsage(Float awayfactor, Float homefactor, Float aoppfactor, Float hoppfactor, Float a, Float aa, Float h, Float hh, Double factor) {
		Double retValue = null;

		if (aa == 0 && hh == 0) {
			retValue = ((a * awayfactor) - ((h * homefactor))) * factor;
		} else if (aa == 0) {
			retValue = ((a * awayfactor) - ((hh * homefactor))) * factor;
		} else if (hh == 0) {
			retValue = ((aa * awayfactor) - ((h * homefactor))) * factor;
		} else {
			retValue = ((aa * awayfactor) - ((hh * homefactor))) * factor;
		}

		return retValue;
	}

	/**
	 * 
	 * @param awayfactor
	 * @param homefactor
	 * @param a
	 * @param h
	 * @param factor
	 * @return
	 */
	protected Double calculateValue(Float awayfactor, Float homefactor, Float a, Float h, Double factor) {
		return ((a * awayfactor) - ((h * homefactor))) * factor;
	}

	/**
	 * 
	 * @param awayfactor
	 * @param homefactor
	 * @param a
	 * @param aa
	 * @param aOpp
	 * @param aaOpp
	 * @param h
	 * @param hh
	 * @param hOpp
	 * @param hhOpp
	 * @param factor
	 * @return
	 */
	protected Double determineDataUsageBoth(Float awayfactor, Float homefactor, Float aoppfactor, Float hoppfactor, Float a, Float aa, Float aOpp, Float aaOpp, Float h, Float hh, Float hOpp, Float hhOpp, Double factor) {
		Double retValue = null;

		if (aa == 0 && hh == 0) {
			retValue = (((a * awayfactor) - (h * homefactor)) + (-((aOpp * aoppfactor) - (hOpp * hoppfactor)))) * factor;
		} else if (aa == 0) {
			retValue = (((a * awayfactor) - (hh * homefactor)) + (-((aOpp * aoppfactor) - (hhOpp * hoppfactor)))) * factor;
		} else if (hh == 0) {
			retValue = (((aa * awayfactor) - (h * homefactor)) + (-((aaOpp * aoppfactor) - (hOpp * hoppfactor)))) * factor;
		} else {
			retValue = (((aa * awayfactor) - (hh * homefactor)) + (-((aaOpp * aoppfactor) - (hhOpp * hoppfactor)))) * factor;
		}

		return retValue;
	}

	/**
	 * 
	 * @param awayfactor
	 * @param homefactor
	 * @param aoppfactor
	 * @param hoppfactor
	 * @param a
	 * @param aOpp
	 * @param h
	 * @param hOpp
	 * @param factor
	 * @return
	 */
	protected Double calculateValueBoth(Float awayfactor, Float homefactor, Float aoppfactor, Float hoppfactor, Float a, Float aOpp, Float h, Float hOpp, Double factor) {
		LOGGER.debug("awayfactor: " + awayfactor);
		LOGGER.debug("homefactor: " + homefactor);
		LOGGER.debug("aoppfactor: " + aoppfactor);
		LOGGER.debug("hoppfactor: " + hoppfactor);
		LOGGER.debug("a: " + a);
		LOGGER.debug("aOpp: " + aOpp);
		LOGGER.debug("h: " + h);
		LOGGER.debug("hOpp: " + hOpp);
		LOGGER.debug("values: " + ((a * awayfactor) - (h * homefactor)));
		LOGGER.debug("Oppvalues: " + (-((aOpp * aoppfactor) - (hOpp * hoppfactor))));

		return (((a * awayfactor) - (h * homefactor)) + (-((aOpp * aoppfactor) - (hOpp * hoppfactor)))) * factor;
	}

	/**
	 * 
	 * @param awayfactor
	 * @param homefactor
	 * @param a
	 * @param aa
	 * @param aOpp
	 * @param aaOpp
	 * @param h
	 * @param hh
	 * @param hOpp
	 * @param hhOpp
	 * @param factor
	 * @return
	 */
	protected Double determineDataUsageBothNegative(Float awayfactor, Float homefactor, Float aoppfactor, Float hoppfactor, Float a, Float aa, Float aOpp, Float aaOpp, Float h, Float hh, Float hOpp, Float hhOpp, Double factor) {
		Double retValue = null;

		if (aa == 0 && hh == 0) {
			retValue = (-((a * awayfactor) - (h * homefactor)) + (((aOpp * aoppfactor) - (hOpp * hoppfactor)))) * factor;
		} else if (aa == 0) {
			retValue = (-((a * awayfactor) - (hh * homefactor)) + (((aOpp * aoppfactor) - (hhOpp * hoppfactor)))) * factor;
		} else if (hh == 0) {
			retValue = (-((aa * awayfactor) - (h * homefactor)) + (((aaOpp * aoppfactor) - (hOpp * hoppfactor)))) * factor;
		} else {
			retValue = (-((aa * awayfactor) - (hh * homefactor)) + (((aaOpp * aoppfactor) - (hhOpp * hoppfactor)))) * factor;
		}

		return retValue;
	}

	/**
	 * 
	 * @param awayfactor
	 * @param homefactor
	 * @param aoppfactor
	 * @param hoppfactor
	 * @param a
	 * @param aOpp
	 * @param h
	 * @param hOpp
	 * @param factor
	 * @return
	 */
	protected Double calculateValueBothNegative(Float awayfactor, Float homefactor, Float aoppfactor, Float hoppfactor, Float a, Float aOpp, Float h, Float hOpp, Double factor) {
		return (-((a * awayfactor) - (h * homefactor)) + (((aOpp * aoppfactor) - (hOpp * hoppfactor)))) * factor;
	}

	/**
	 * 
	 * @param ishometeam
	 * @param game
	 * @return
	 */
	protected Map<String, Boolean> didTeamWinCover(boolean ishometeam, EspnGameData game) {
		final Map<String, Boolean> didWinCover = new HashMap<String, Boolean>();

		// Get the favorite and line
		for (VegasInsiderGame vig : vegasInsiderGames) {
			if (vig.getWeek().intValue() == game.getWeek().intValue() && 
				vig.getYear().intValue() == game.getYear().intValue() && 
				vig.getAwayteamdata().getTeamname().equals(game.getAwayteam()) && 
				vig.getHometeamdata().getTeamname().equals(game.getHometeam())) {
				game.setLinefavorite(vig.getLinefavorite());
				game.setLine(vig.getLine());
			}
		}

		// Is this the home team?
		if (ishometeam) {
			// Did the home team win?
			if (game.getHomewin()) {
				didWinCover.put("didwin", new Boolean(true));

				if (game.getLinefavorite().equals(game.getHometeam())) {
					didWinCover.put("wasfavorite", new Boolean(true));
					float score = game.getHomefinalscore().floatValue() - game.getAwayfinalscore().floatValue();

					if (score >= Math.abs(game.getLine())) {
						didWinCover.put("didcover", new Boolean(true));
					} else {
						didWinCover.put("didcover", new Boolean(false));
					}
				} else {	
					didWinCover.put("wasfavorite", new Boolean(false));
					didWinCover.put("didcover", new Boolean(true));
				}
			} else {
				didWinCover.put("didwin", new Boolean(false));

				if (game.getLinefavorite().equals(game.getHometeam())) {
					didWinCover.put("wasfavorite", new Boolean(true));
					didWinCover.put("didcover", new Boolean(false));
				} else {	
					didWinCover.put("wasfavorite", new Boolean(false));
					float score = game.getAwayfinalscore().floatValue() - game.getHomefinalscore().floatValue();

					if (score >= Math.abs(game.getLine())) {
						didWinCover.put("didcover", new Boolean(false));
					} else {
						didWinCover.put("didcover", new Boolean(true));
					}
				}
			}
		} else {
			// Did the away team win?
			if (game.getAwaywin()) {
				didWinCover.put("didwin", new Boolean(true));

				if (game.getLinefavorite().equals(game.getAwayteam())) {
					didWinCover.put("wasfavorite", new Boolean(true));
					float score = game.getAwayfinalscore().floatValue() - game.getHomefinalscore().floatValue();

					if (score >= Math.abs(game.getLine())) {
						didWinCover.put("didcover", new Boolean(true));
					} else {
						didWinCover.put("didcover", new Boolean(false));
					}
				} else {
					didWinCover.put("wasfavorite", new Boolean(false));
					didWinCover.put("didcover", new Boolean(true));
				}
			} else {
				didWinCover.put("didwin", new Boolean(false));

				if (game.getLinefavorite().equals(game.getAwayteam())) {
					didWinCover.put("wasfavorite", new Boolean(true));
					didWinCover.put("didcover", new Boolean(false));
				} else {	
					didWinCover.put("wasfavorite", new Boolean(false));
					float score = game.getHomefinalscore().floatValue() - game.getAwayfinalscore().floatValue();

					if (score >= Math.abs(game.getLine())) {
						didWinCover.put("didcover", new Boolean(false));
					} else {
						didWinCover.put("didcover", new Boolean(true));
					}
				}
			}
		}

		return didWinCover;
	}

	/**
	 * 
	 * @param awaygameawaymasseyrating
	 * @param awaygamehomemasseyrating
	 * @param homegameawaymasseyrating
	 * @param homegamehomemasseyrating
	 * @return
	 */
	protected Float determineMasterMassey(Float awaygameawaymasseyrating, Float awaygamehomemasseyrating, Float homegameawaymasseyrating, Float homegamehomemasseyrating) {
		Float masterMassey = null;

		if (awaygameawaymasseyrating.floatValue() < awaygamehomemasseyrating.floatValue()) {
			if (awaygameawaymasseyrating.floatValue() < homegameawaymasseyrating.floatValue() && 
				awaygameawaymasseyrating.floatValue() < homegamehomemasseyrating.floatValue()) {
				masterMassey = awaygameawaymasseyrating;
			} else if (awaygameawaymasseyrating.floatValue() < homegameawaymasseyrating.floatValue()) {
				masterMassey = awaygameawaymasseyrating;
			} else if (awaygameawaymasseyrating.floatValue() < homegamehomemasseyrating.floatValue()) {
				masterMassey = awaygameawaymasseyrating;
			} else if (awaygameawaymasseyrating.floatValue() > homegameawaymasseyrating.floatValue()) {
				masterMassey = homegameawaymasseyrating;
			} else if (awaygameawaymasseyrating.floatValue() > homegamehomemasseyrating.floatValue()) {
				masterMassey = homegamehomemasseyrating;
			}
		} else if (awaygameawaymasseyrating.floatValue() > awaygamehomemasseyrating.floatValue()) {
			if (awaygamehomemasseyrating.floatValue() < homegameawaymasseyrating.floatValue() && 
				awaygamehomemasseyrating.floatValue() < homegamehomemasseyrating.floatValue()) {
				masterMassey = awaygamehomemasseyrating;
			} else if (awaygamehomemasseyrating.floatValue() < homegameawaymasseyrating.floatValue()) {
				if (awaygamehomemasseyrating.floatValue() > homegamehomemasseyrating.floatValue()) {
					masterMassey = homegamehomemasseyrating;
				} else if (awaygamehomemasseyrating.floatValue() > homegameawaymasseyrating.floatValue()) {
					masterMassey = homegameawaymasseyrating;
				} else {
					masterMassey = awaygamehomemasseyrating;
				}
			} else if (awaygamehomemasseyrating.floatValue() < homegamehomemasseyrating.floatValue() &&
					!(homegameawaymasseyrating.floatValue() < awaygamehomemasseyrating.floatValue())) {
				masterMassey = awaygamehomemasseyrating;
			} else if (awaygamehomemasseyrating.floatValue() > homegamehomemasseyrating.floatValue()) {
				masterMassey = homegamehomemasseyrating;
			} else if (awaygamehomemasseyrating.floatValue() > homegameawaymasseyrating.floatValue()) {
				masterMassey = homegameawaymasseyrating;
			}
		}

		return masterMassey;
	}

	/**
	 * 
	 * @param awayteam
	 * @param hometeam
	 * @param awayGame
	 * @param homeGame
	 * @param awaygameawaymasseyrating
	 * @param awaygamehomemasseyrating
	 * @param homegameawaymasseyrating
	 * @param homegamehomemasseyrating
	 * @param afactors
	 * @param aoppfactors
	 * @param hfactors
	 * @param hoppfactors
	 */
	protected Map<String, Float> determineFactors(String awayteam, 
			String hometeam, 
			EspnCollegeFootballGameData awayGame, 
			EspnCollegeFootballGameData homeGame,
			Float awaygameawaymasseyrating,
			Float awaygamehomemasseyrating,
			Float homegameawaymasseyrating,
			Float homegamehomemasseyrating,
			Map<String, Float> afactors,
			Map<String, Float> aoppfactors,
			Map<String, Float> hfactors,
			Map<String, Float> hoppfactors,
			Float awaymassey,
			Float homemassey,
			Float mastermassey) {
		final Map<String, Float> factors = new HashMap<String, Float>();

		if (awayGame.getAwaycollegename().equals(awayteam)) {
			if (awaygamehomemasseyrating.floatValue() < awaygameawaymasseyrating.floatValue()) {
				factors.put("awayfactor", afactors.get("homefactor"));
				factors.put("aoppfactor", aoppfactors.get("awayfactor"));					
			} else {
				factors.put("awayfactor", afactors.get("awayfactor"));
				factors.put("aoppfactor", aoppfactors.get("homefactor"));
			}
		} else {
			if (awaygamehomemasseyrating.floatValue() < awaygameawaymasseyrating.floatValue()) {
				if (awaygamehomemasseyrating.floatValue() == mastermassey.floatValue()) {
					if (awaygameawaymasseyrating.floatValue() > 200) {
						factors.put("awayfactor", afactors.get("homefactor"));
					} else {
						if (Math.abs(homemassey.floatValue() - awaymassey.floatValue()) < 35) {
							if (homemassey < 35 && awaymassey < 35) {
								factors.put("awayfactor", afactors.get("awayfactor") - new Float(0.1));
							} else if (homemassey < 75 && awaymassey < 75) {
								factors.put("awayfactor", afactors.get("homefactor"));
							} else {
								factors.put("awayfactor", afactors.get("awayfactor") - new Float(0.3));
							}
						} else if (Math.abs(homemassey.floatValue() - awaymassey.floatValue()) < 50) {
							if (homemassey < 35 && awaymassey < 35) {
								factors.put("awayfactor", afactors.get("awayfactor") + new Float(0.1));
							} else {
								factors.put("awayfactor", afactors.get("awayfactor") + new Float(0.3));
							}
						} else {
							factors.put("awayfactor", afactors.get("awayfactor"));
						}
					}

					if (awaygameawaymasseyrating.floatValue() > 150) {
						factors.put("aoppfactor", aoppfactors.get("awayfactor") + new Float(.20));
					} else {
						if (Math.abs(homemassey.floatValue() - awaymassey.floatValue()) < 35) {
							if (homemassey < 75 && awaymassey < 75) {
								factors.put("aoppfactor", aoppfactors.get("awayfactor"));
							} else {
								factors.put("aoppfactor", aoppfactors.get("homefactor") + new Float(0.3));
							}
						} else if (Math.abs(homemassey.floatValue() - awaymassey.floatValue()) < 50) {
							factors.put("aoppfactor", aoppfactors.get("homefactor") - new Float(0.3));
						} else {
							factors.put("aoppfactor", aoppfactors.get("homefactor"));
						}
					}
				} else {
					factors.put("awayfactor", afactors.get("awayfactor"));
					factors.put("aoppfactor", aoppfactors.get("homefactor"));
				}
			} else {
				if (Math.abs(homemassey - awaymassey) < 10) {
					factors.put("awayfactor", hfactors.get("homefactor") + new Float(0.2));
				} else if (Math.abs(homemassey.floatValue() - awaymassey.floatValue()) < 40 && 
						Math.abs(homemassey.floatValue() - awaymassey.floatValue()) < mastermassey.floatValue()) {
					factors.put("awayfactor", hfactors.get("homefactor") + new Float(0.17));
				} else {
					factors.put("awayfactor", hfactors.get("awayfactor"));
				}
				factors.put("aoppfactor", aoppfactors.get("awayfactor"));
			}
		}

		if (homeGame.getAwaycollegename().equals(hometeam)) {
			if (homegamehomemasseyrating.floatValue() < homegameawaymasseyrating.floatValue()) {
				if (homemassey.floatValue() < awaymassey.floatValue()) {
					factors.put("homefactor", hfactors.get("homefactor") + new Float(0.2));
					factors.put("hoppfactor", hoppfactors.get("awayfactor") - new Float(0.2));
				} else {
					factors.put("homefactor", hfactors.get("homefactor"));
					factors.put("hoppfactor", hoppfactors.get("awayfactor"));
				}
			} else {
				if (homemassey.floatValue() == homegameawaymasseyrating.floatValue()) {
					if (homegameawaymasseyrating.floatValue() < 40 && homegamehomemasseyrating.floatValue() < 70) {
						if (awaymassey.floatValue() < 40) {
							factors.put("homefactor", hfactors.get("awayfactor"));
							factors.put("hoppfactor", hoppfactors.get("homefactor"));								
						} else {
							factors.put("homefactor", hfactors.get("homefactor"));
							factors.put("hoppfactor", hoppfactors.get("awayfactor"));	
						}
					} else if (homegameawaymasseyrating.floatValue() < 50) {
						factors.put("homefactor", hfactors.get("awayfactor"));
						factors.put("hoppfactor", hoppfactors.get("homefactor"));
					} else if (Math.abs(homegamehomemasseyrating.floatValue() - homegameawaymasseyrating.floatValue()) < 15) {
						factors.put("homefactor", hfactors.get("homefactor"));
						factors.put("hoppfactor", hoppfactors.get("awayfactor"));
					} else {
						if (Math.abs(homemassey.floatValue() - awaymassey.floatValue()) < 20) {
							factors.put("homefactor", hfactors.get("awayfactor"));
							factors.put("hoppfactor", hoppfactors.get("homefactor"));
						} else {
							factors.put("homefactor", hfactors.get("awayfactor"));
							factors.put("hoppfactor", hoppfactors.get("awayfactor"));
						}
					}
				} else {
					factors.put("homefactor", hfactors.get("homefactor"));
					factors.put("hoppfactor", hoppfactors.get("homefactor"));
				}
			}
		} else {
			if (homegamehomemasseyrating.floatValue() < homegameawaymasseyrating.floatValue()) {
				if (Math.abs(homemassey - awaymassey) < 10) {
					if (homemassey < 50 && awaymassey < 50) {
						factors.put("homefactor", hfactors.get("homefactor") + new Float(0.20));
					} else {
						if (homegamehomemasseyrating.floatValue() > 90) {
							factors.put("homefactor", hfactors.get("homefactor") - new Float(0.10));
						} else {
							factors.put("homefactor", hfactors.get("awayfactor"));
						}
					}
				} else if (Math.abs(homemassey.floatValue() - awaymassey.floatValue()) < 20) {
					if (homegameawaymasseyrating.floatValue() > 180) {
						factors.put("homefactor", hfactors.get("awayfactor"));
					} else if (homegamehomemasseyrating.floatValue() > 90) {
						factors.put("homefactor", hfactors.get("homefactor") - new Float(0.10));
					} else {
						factors.put("homefactor", hfactors.get("homefactor"));
					}
				} else if (Math.abs(homegamehomemasseyrating.floatValue() - homegameawaymasseyrating.floatValue()) < 30) {
					factors.put("homefactor", hfactors.get("homefactor"));
				} else if (Math.abs(homemassey.floatValue() - awaymassey.floatValue()) < 50) {
					if (homegameawaymasseyrating.floatValue() > 170 && homegamehomemasseyrating.floatValue() < 100) {
						factors.put("homefactor", hfactors.get("awayfactor"));
					} else if (homegameawaymasseyrating.floatValue() > 170 && homegamehomemasseyrating.floatValue() < 100) {
						factors.put("homefactor", hfactors.get("homefactor") - new Float(0.20));
					} else if (homegameawaymasseyrating.floatValue() > 180) {
						factors.put("homefactor", hfactors.get("awayfactor"));
					} else if (homegameawaymasseyrating.floatValue() > 150) {
						factors.put("homefactor", hfactors.get("awayfactor") + new Float(0.15));
					} else if (homegameawaymasseyrating.floatValue() > 100) {
						factors.put("homefactor", hfactors.get("awayfactor"));
					} else {
//						factors.put("homefactor", hfactors.get("awayfactor") + new Float(0.15));
						factors.put("homefactor", hfactors.get("awayfactor"));
					}
				} else if (Math.abs(homemassey.floatValue() - awaymassey.floatValue()) < 40 && 
						Math.abs(homemassey.floatValue() - awaymassey.floatValue()) < mastermassey.floatValue()) {
					factors.put("homefactor", hfactors.get("awayfactor") + new Float(0.17));
				} else if (Math.abs(homemassey.floatValue() - awaymassey.floatValue()) < 80) {
					if (homemassey.floatValue() < 100) {
						factors.put("homefactor", hfactors.get("awayfactor") + new Float(0.18));
					} else {
						if (homegameawaymasseyrating.floatValue() > 170 && homegamehomemasseyrating.floatValue() < 100) {
							factors.put("homefactor", hfactors.get("homefactor"));
						} else {
							factors.put("homefactor", hfactors.get("awayfactor"));
						}
					}
				} else if (Math.abs(homemassey.floatValue() - awaymassey.floatValue()) < 120) {
					factors.put("homefactor", hfactors.get("awayfactor") + new Float(0.20));
				} else {
					factors.put("homefactor", hfactors.get("awayfactor"));
				}

				if (homegameawaymasseyrating.floatValue() > 175) {
					factors.put("hoppfactor", hoppfactors.get("homefactor"));
				} else {
					factors.put("hoppfactor", hoppfactors.get("awayfactor"));
				}
			} else {
				if (homemassey.floatValue() < awaymassey.floatValue()) {
					if (Math.abs(homemassey.floatValue() - awaymassey.floatValue()) < 40) {
						factors.put("homefactor", hfactors.get("awayfactor"));
					} else {
						factors.put("homefactor", hfactors.get("homefactor") + new Float(0.0));
					}
				} else {
					factors.put("homefactor", hfactors.get("homefactor"));
				}
				factors.put("hoppfactor", hoppfactors.get("homefactor"));
			}
		}

		return factors;
	}

	/**
	 * 
	 * @param awayteam
	 * @param hometeam
	 * @return
	 */
	protected abstract Double determineSpread(String awayteam, String hometeam);

	/**
	 * 
	 */
	protected abstract void clearList();

	/**
	 * 
	 * @param game
	 * @return
	 */
	protected abstract int getAwayTurnovers(EspnGameData game);

	/**
	 * 
	 * @param game
	 * @return
	 */
	protected abstract int getHomeTurnovers(EspnGameData game);

	/**
	 * 
	 * @param awayteam
	 * @param hometeam
	 * @param awaygame
	 * @param homegame
	 * @param awaymassey
	 * @param homemassey
	 * @param awayfactor
	 * @param aoppfactor
	 * @param homefactor
	 * @param hoppfactor
	 * @throws BatchException
	 * @throws SQLException
	 */
	protected abstract void processGameData(String awayteam, 
			String hometeam, 
			EspnGameData awaygame, 
			EspnGameData homegame,
			Float awaymassey,
			Float homemassey,
			Float awayfactor, 
			Float aoppfactor, 
			Float homefactor, 
			Float hoppfactor) throws BatchException, SQLException;

	/**
	 * 
	 * @param year
	 * @param startDate
	 * @param endDate
	 */
	protected abstract void setupMasseyRatings(Integer year, Date startDate, Date endDate);

	/**
	 * 
	 * @param year
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	protected abstract List<VegasInsiderGame> getVegasInsiderGames(Integer year, Date startDate, Date endDate);

	/**
	 * 
	 * @param seasonyear
	 * @param startdate
	 * @param thedate
	 * @param awayteam
	 * @param hometeam
	 * @param gamedate
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	protected abstract Double runAlgorithm(Integer seasonyear, Date startdate, Date thedate, String awayteam, String hometeam, Date gamedate) throws BatchException, SQLException;

	/**
	 * 
	 * @param X
	 * @param seasonyear
	 * @param startdate
	 * @param thedate
	 * @param awayteam
	 * @param hometeam
	 * @param gamedate
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	protected abstract Double runGameAlgorithmLastX(Integer X, Integer seasonyear, Date startdate, Date thedate, String awayteam, String hometeam, Date gamedate) throws BatchException, SQLException;

	/**
	 * 
	 * @param X
	 * @param seasonyear
	 * @param startdate
	 * @param thedate
	 * @param awayteam
	 * @param hometeam
	 * @param gamedate
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	protected abstract Double runGameAlgorithmLastXHomeAway(Integer X, Integer seasonyear, Date startdate, Date thedate, String awayteam, String hometeam, Date gamedate) throws BatchException, SQLException;

	/**
	 * 
	 * @param name
	 * @return
	 */
	protected abstract boolean foundTeam(String name);

	/**
	 * 
	 * @param vigGame
	 * @return
	 */
	protected abstract Map<String, Integer> getMasseyRatings(VegasInsiderGame vigGame);




	/**
	 * 
	 * @param userweekratings
	 * @param awayGame
	 * @param homeGame
	 * @return
	 */
	protected abstract Map<String, Integer> lastGameRatings(boolean userweekratings, EspnGameData awayGame, EspnGameData homeGame);

	/**
	 * 
	 * @param awayteam
	 * @param hometeam
	 * @param awaygame
	 * @param homegame
	 * @return
	 */
	protected abstract Map<String, Integer> getTeamRatingsForTeams(String awayteam, String hometeam, EspnGameData awaygame, EspnGameData homegame);
}