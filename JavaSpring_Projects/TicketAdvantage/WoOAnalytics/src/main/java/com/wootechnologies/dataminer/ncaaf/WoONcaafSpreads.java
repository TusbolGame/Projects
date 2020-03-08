/**
 * 
 */
package com.wootechnologies.dataminer.ncaaf;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wooanalytics.dao.sites.masseyratings.MasseyRatingsNcaafData;
import com.wooanalytics.dao.sites.masseyratings.MasseyRatingsSite;
import com.wootechnologies.dataminer.WoOSpreads;
import com.wootechnologies.dataminer.db.ncaaf.NcaafDataMinerDB;
import com.wootechnologies.dataminer.model.XandYObject;
import com.wootechnologies.errorhandling.BatchException;
import com.wootechnologies.services.dao.sites.espn.EspnCollegeFootballGameData;
import com.wootechnologies.services.dao.sites.espn.EspnGameData;
import com.wootechnologies.services.dao.sites.vegasinsider.VegasInsiderGame;
import com.wootechnologies.services.dao.sites.vegasinsider.VegasInsiderParser;
import com.wootechnologies.services.dao.sites.vegasinsider.VegasInsiderProcessSite;
import com.wootechnologies.services.site.util.WeekByDate;

/**
 * @author jmiller
 *
 */
public class WoONcaafSpreads extends WoOSpreads {
	private static final Logger LOGGER = Logger.getLogger(WoONcaafSpreads.class);
	private static final MasseyRatingsSite mrs = new MasseyRatingsSite();
	private static final VegasInsiderProcessSite vips = new VegasInsiderProcessSite();
	private static final NcaafDataMinerDB DATAMINERDB = new NcaafDataMinerDB();
	private static final Integer NCAAF_TEAMS = 260;
	private static final Integer MASSEY_MAX = 130;
	private static final Integer MASSEY_END_WEEK = 11;
	private final Map<Integer, List<MasseyRatingsNcaafData>> MasseyLatestRatings = new HashMap<Integer, List<MasseyRatingsNcaafData>>();
	private final List<Double> thirddownefflist = new ArrayList<Double>();
	private final List<Double> oppthirddownefflist = new ArrayList<Double>();
	private final List<Double> fourthdownefflist = new ArrayList<Double>();
	private final List<Double> oppfourthdownefflist = new ArrayList<Double>();
	private final List<Double> pointslist = new ArrayList<Double>();
	private final List<Double> firstdownslist = new ArrayList<Double>();
	private final List<Double> totalyardslist = new ArrayList<Double>();
	private final List<Double> rushingyardslist = new ArrayList<Double>();
	private final List<Double> rushingattemptslist = new ArrayList<Double>();
	private final List<Double> yardsperrushlist = new ArrayList<Double>();
	private final List<Double> passingyardslist = new ArrayList<Double>();
	private final List<Double> passingcompletionslist = new ArrayList<Double>();
	private final List<Double> passingattemptslist = new ArrayList<Double>();
	private final List<Double> yardsperpasslist = new ArrayList<Double>();
	private final List<Double> turnoverslist = new ArrayList<Double>();
	private final List<Double> possessionlist = new ArrayList<Double>();
	private static final Calendar seasonstart2014 = Calendar.getInstance();
	private static final Calendar seasonstart2015 = Calendar.getInstance();
	private static final Calendar seasonstart2016 = Calendar.getInstance();
	private static final Calendar seasonstart2017 = Calendar.getInstance();
	private static final Calendar seasonstart2018 = Calendar.getInstance();
	private static final Calendar seasonstart2019 = Calendar.getInstance();
	private boolean showspreadonly = false;
	private Double thirddownefffactor = 1.0;
	private Double oppthirddownefffactor = 1.0;
	private Double fourthdownefffactor = 0.06;
	private Double oppfourthdownefffactor = 1.0;
	private Double pointsfactor = 0.2;
	private Double firstdownsfactor = 0.15;
	private Double rushingyardsfactor = 0.2;
	private Double totalyardsfactor = 0.01;
	private Double rushingattemptsfactor = 0.02;
	private Double yardsperrushfactor = 0.05;
	private Double passingyardsfactor = 0.15;
	private Double passingcompletionsfactor = 0.025;
	private Double passingattemptsfactor = 0.01;
	private Double yardsperpassfactor = 0.03;
	private Double turnoversfactor = 0.05;
	private Double possessionfactor = 0.05;
	private Double homefieldfactor = 5.0;

/*
	private Double thirddownefffactor = 1.0;
	private Double oppthirddownefffactor = 1.0;
	private Double fourthdownefffactor = 0.06;
	private Double oppfourthdownefffactor = 1.0;
	private Double pointsfactor = 0.1;
	private Double firstdownsfactor = 0.04;
	private Double totalyardsfactor = 0.01;
	private Double rushingyardsfactor = 0.07;
	private Double rushingattemptsfactor = 0.02;
	private Double yardsperrushfactor = 0.05;
	private Double passingyardsfactor = 0.03;
	private Double passingcompletionsfactor = 0.025;
	private Double passingattemptsfactor = 0.01;
	private Double yardsperpassfactor = 0.03;
	private Double turnoversfactor = 0.01;
	private Double possessionfactor = 0.02;
	private Double homefieldfactor = 4.5;

	private Double commonteamfactor = 260.0;
	private Double thirddownefffactor = 1.0;
	private Double oppthirddownefffactor = 1.0;
	private Double fourthdownefffactor = 1.0;
	private Double oppfourthdownefffactor = 1.0;
	private Double pointsfactor = 0.15;
	private Double firstdownsfactor = 0.06;
	private Double totalyardsfactor = 0.03;
	private Double rushingyardsfactor = 0.04;
	private Double rushingattemptsfactor = 0.02;
	private Double yardsperrushfactor = 0.1;
	private Double passingyardsfactor = 0.03;
	private Double passingcompletionsfactor = 0.025;
	private Double passingattemptsfactor = 0.01;
	private Double yardsperpassfactor = 0.025;
	private Double turnoversfactor = 1.0;
	private Double possessionfactor = 0.1;
	private Double homefieldfactor = -3.0;
*/

	static {
		seasonstart2014.set(Calendar.MONTH, 7);
		seasonstart2014.set(Calendar.DAY_OF_MONTH, 28);
		seasonstart2014.set(Calendar.YEAR, 2014);

		seasonstart2015.set(Calendar.MONTH, 8);
		seasonstart2015.set(Calendar.DAY_OF_MONTH, 3);
		seasonstart2015.set(Calendar.YEAR, 2015);

		seasonstart2016.set(Calendar.MONTH, 7);
		seasonstart2016.set(Calendar.DAY_OF_MONTH, 26);
		seasonstart2016.set(Calendar.YEAR, 2016);

		seasonstart2017.set(Calendar.MONTH, 7);
		seasonstart2017.set(Calendar.DAY_OF_MONTH, 26);
		seasonstart2017.set(Calendar.YEAR, 2017);

		seasonstart2018.set(Calendar.MONTH, 7);
		seasonstart2018.set(Calendar.DAY_OF_MONTH, 25);
		seasonstart2018.set(Calendar.YEAR, 2018);

		seasonstart2019.set(Calendar.MONTH, 7);
		seasonstart2019.set(Calendar.DAY_OF_MONTH, 24);
		seasonstart2019.set(Calendar.YEAR, 2019);
	}

	/**
	 * 
	 */
	public WoONcaafSpreads() {
		super();
		COMMON_TEAM_FACTOR = new Double(NCAAF_TEAMS);
		MAX_MASSEY_FACTOR = new Integer(MASSEY_MAX);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final WoONcaafSpreads woODataMiner = new WoONcaafSpreads();

//		woODataMiner.testFactors(1, 14, 2017, "/home/ubuntu/testfactors.csv", true);
//		woODataMiner.testFactors(1, 14, 2017, "/Users/jmiller/Documents/WoOTechnology/testfactors.csv", true);

//		woODataMiner.determineSpreads(1, 17, 2018, true, "/Users/jmiller/Documents/WoOTechnology/footballpicksweekallweekallfootballWeek1-172018true.csv", true);
		woODataMiner.determineLastXSpreads(3, 11, 11, 2019, true, "/Users/jmiller/Documents/WoOTechnology/footballpicks112019last3true.csv", true);
//		woODataMiner.combineAllSpread(3, 1, 17, 2018, true, "/Users/jmiller/Documents/WoOTechnology/footballpicks1-172018Combined.csv");
//		woODataMiner.determineLastXSpreads(3, 1, 17, 2018, true, "/Users/jmiller/Documents/WoOTechnology/footballpicks1-172018last3true.csv", true);
//		woODataMiner.determineLastXSpreads(3, 1, 14, 2016, "/Users/jmiller/Documents/WoOTechnology/footballpicks1-14-2016last3.csv", true);
//		woODataMiner.determineLastXSpreads(3, 1, 14, 2017, "/Users/jmiller/Documents/WoOTechnology/footballpicks1-14-2017last3.csv", true);
//		woODataMiner.determineLastXSpreads(3, 1, 11, 2018, "/Users/jmiller/Documents/WoOTechnology/footballpicks1-11-2018last3.csv", true);

// 		woODataMiner.determineLastXSpreads(3, 13, 13, 2018, true, "/Users/jmiller/Documents/WoOTechnology/footballpicks13-13-2018lastthree.csv", true);
//		woODataMiner.compareAllSpread(1, 11, 2018, "/Users/jmiller/Documents/WoOTechnology/footballpicksweekallweekallfootball1-14-2017lasttwo.csv");
//		woODataMiner.determineLastXSpreads(3, 1, 14, 2017, "/Users/jmiller/Documents/WoOTechnology/footballpicksweekallweekallfootball1-14-2017lasttwo.csv", true);
//		woODataMiner.determineSpreadsLastXManual(3, 2, 2018, "LIBERTY", "ARMY", false, false, true, true);
//		woODataMiner.determineSpreadsLastXManual(3, 3, 2018, "ARKANSAS STATE", "TULSA", false, false, true, true);
//		woODataMiner.determineSpreadsLastXManual(3, 4, 2018, "NEBRASKA", "MICHIGAN", false, false, true, true);
//		woODataMiner.determineSpreadsLastXManual(3, 5, 2018, "NEVADA", "AIR FORCE", false, false, true, true);
//		woODataMiner.determineSpreadsLastXManual(3, 2, 2018, "ARKANSAS STATE", "ALABAMA", false, false, true, true);
//		woODataMiner.determineSpreadsLastXManual(3, 5, 2018, "TEMPLE", "BOSTON COLLEGE", false, false, true, true);

/*
//		woODataMiner.gameRankings(9, 9, 2018, "/Users/johnmiller/Documents/WoOTechnology/footballpicksrankingsweek9.csv");
//		woODataMiner.graphGames(3, 3, 2018, "~/Documents/WoOTechnology/footballpicksweekallv3.csv");
//		woODataMiner.getAllFCSTeams();
*/
	}

	/**
	 * 
	 * @param X
	 * @param weekstart
	 * @param weekend
	 * @param year
	 * @param useweekratings
	 * @param filename
	 * @param compareall
	 */
	public void testFactors(Integer X, Integer weekstart, Integer weekend, Integer year, Boolean useweekratings, String filename, boolean compareall) {
		LOGGER.info("Entering testFactors()");
		LOGGER.debug("weekstart: " + weekstart);
		LOGGER.debug("weekend: " + weekend);
		LOGGER.debug("year: " + year);
		this.USE_WEEK_RANKINGS = useweekratings;

		// Open the connection
		DATAMINERDB.start();
		PrintWriter printWriter = null;
		try {
			final FileWriter fileWriter = new FileWriter(filename);
			printWriter = new PrintWriter(fileWriter);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		printWriter.print("thirddownefffactor," + 
				"oppthirddownefffactor," + 
				"fourthdownefffactor," + 
				"pointsfactor," + 
				"firstdownsfactor," + 
				"totalyardsfactor," + 
				"rushingyardsfactor," + 
				"rushingattemptsfactor," + 
				"yardsperrushfactor," +
				"rushingattemptsfactor," + 
				"yardsperrushfactor," + 
				"passingyardsfactor," + 
				"yardsperpassfactor," + 
				"turnoversfactor," + 
				"possessionfactor," + 
				"homefieldfactor," + 
				"All Wins," + "All Losses," + "All Pushes," + 
				"WoO Wins," + "WoO Losses," + "WoO Pushes" + "\n");

		// Loop through all the games
		final List<VegasInsiderGame> vigs = vips.getNcaafGameData(year, weekstart, weekend, false);
		final List<String> didwin = new ArrayList<String>();
		final List<String> deltadidwin = new ArrayList<String>();

		Double origthirddownefffactor = thirddownefffactor;
		Double origoppthirddownefffactor = oppthirddownefffactor;
		Double origfourthdownefffactor = fourthdownefffactor;
		Double origoppfourthdownefffactor = oppfourthdownefffactor;
		Double origpointsfactor = pointsfactor;
		Double origfirstdownsfactor = firstdownsfactor;
		Double origtotalyardsfactor = totalyardsfactor;
		Double origrushingyardsfactor = rushingyardsfactor;
		Double origrushingattemptsfactor = rushingattemptsfactor;
		Double origyardsperrushfactor = yardsperrushfactor;
		Double origpassingyardsfactor = passingyardsfactor;
		Double origpassingcompletionsfactor = passingcompletionsfactor;
		Double origpassingattemptsfactor = passingattemptsfactor;
		Double origyardsperpassfactor = yardsperpassfactor;
		Double origturnoversfactor = turnoversfactor;
		Double origpossessionfactor = possessionfactor;
		Double orighomefieldfactor = homefieldfactor;

		thirddownefffactor = 0.01;
		oppthirddownefffactor = 0.01;
		fourthdownefffactor = 0.01;
		oppfourthdownefffactor = 0.01;
		pointsfactor = 0.01;
		firstdownsfactor = 0.01;
		totalyardsfactor = 0.01;
		rushingyardsfactor = 0.01;
		yardsperrushfactor = 0.01;
		passingyardsfactor = 0.01;
		yardsperpassfactor = 0.01;
		turnoversfactor = 0.01;
		possessionfactor = 0.01;
		homefieldfactor = -1.0;

		while (thirddownefffactor <= 1.0) {
			while (oppthirddownefffactor <= 1.0) {
				while (fourthdownefffactor <= 1.0) {
					while (oppfourthdownefffactor <= 1.0) {
						while (pointsfactor <= 1.0) {
							while (firstdownsfactor <= 1.0) {
								while (totalyardsfactor <= 1.0) {
									while (rushingyardsfactor <= 1.0) {
										while (yardsperrushfactor <= 1.0) {
											while (passingyardsfactor <= 1.0) {
												while (yardsperpassfactor <= 1.0) {
													while (turnoversfactor <= 1.0) {
														while (possessionfactor <= 1.0) {
															while (homefieldfactor >= -10.0) {
																didwin.clear();
																deltadidwin.clear();
																for (VegasInsiderGame ncaafGame : vigs) {
																	try {
																		final String awayname = ncaafGame.getAwayteamdata().getTeamname().toUpperCase(); 
																		final String homename = ncaafGame.getHometeamdata().getTeamname().toUpperCase();
																		final boolean awayfound = foundTeam(awayname);
																		final boolean homefound = foundTeam(homename);
																		final boolean awayisfbs = ncaafGame.getAwayteamdata().getIsfbs();
																		final boolean homeisfbs = ncaafGame.getHometeamdata().getIsfbs();
														
																		if ((awayname != null || homename != null) && (awayfound || homefound)) {
																			clearList();
														
																			// Get score delta
																			final Integer homefinalscore = ncaafGame.getHometeamdata().getFinalscore();
																			final Integer awayfinalscore = ncaafGame.getAwayteamdata().getFinalscore();
														
																			final Double spread = this.runGameAlgorithmLastX(X, ncaafGame.getWeek(), ncaafGame.getYear(), ncaafGame.getAwayteamdata().getTeamname(), ncaafGame.getHometeamdata().getTeamname(), awayisfbs, homeisfbs, compareall);
																			final Map<String, String> results = processResults(awayfinalscore, homefinalscore, spread, ncaafGame);
																			didwin.add(results.get("outcome"));
																			deltadidwin.add(results.get("deltaresults"));
																		}
																	} catch (Throwable t) {
																		LOGGER.debug("Cannot get spread for " + ncaafGame.getAwayteamdata().getTeamname() + " " + ncaafGame.getHometeamdata().getTeamname() + " " + ncaafGame.getAwayteamdata().getFinalscore() + " " + ncaafGame.getHometeamdata().getFinalscore()
																				+ " Line: " + ncaafGame.getLinefavorite() + " " + ncaafGame.getLine() + " week: " + ncaafGame.getWeek());
																		LOGGER.error(t.getMessage(), t);
																	}
																}
												
																int rwins = 0;
																int rlosses = 0;
																int rpushes = 0;
																for (String wlt : didwin) {
																	if (wlt != null && wlt.length() > 0) {
																		if (wlt.equals("W")) {
																			rwins++;
																		} else if (wlt.equals("L")) {
																			rlosses++;
																		} else if (wlt.equals("P")) {
																			rpushes++;
																		}
																	}
																}
																int dwins = 0;
																int dlosses = 0;
																int dpushes = 0;
																for (String wlt : deltadidwin) {
																	if (wlt != null && wlt.length() > 0) {
																		if (wlt.equals("W")) {
																			dwins++;
																		} else if (wlt.equals("L")) {
																			dlosses++;
																		} else if (wlt.equals("P")) {
																			dpushes++;
																		}
																	}
																}
												
																printWriter.print(thirddownefffactor + "," + 
																		oppthirddownefffactor + "," + 
																		fourthdownefffactor + "," + 
																		pointsfactor + "," + 
																		firstdownsfactor + "," + 
																		totalyardsfactor + "," + 
																		rushingyardsfactor + "," + 
																		rushingattemptsfactor + "," + 
																		yardsperrushfactor + "," +
																		rushingattemptsfactor + "," + 
																		yardsperrushfactor + "," + 
																		passingyardsfactor + "," + 
																		yardsperpassfactor + "," + 
																		turnoversfactor + "," + 
																		possessionfactor + "," + 
																		homefieldfactor + "," + 
																		rwins + "," + rlosses + "," + rpushes + "," + 
																		dwins + "," + dlosses + "," + dpushes + "\n");

																homefieldfactor -= 0.50;
															}
															homefieldfactor = -1.0;
															possessionfactor += 0.01;
														}
														possessionfactor = 0.01;
														turnoversfactor += 0.01;
													}
													turnoversfactor = 0.01;
													yardsperpassfactor += 0.01;
												}
												yardsperpassfactor = 0.01;
												passingyardsfactor += 0.01;
											}
											passingyardsfactor = 0.01;
											yardsperrushfactor += 0.01;
										}
										yardsperrushfactor = 0.01;
										rushingyardsfactor += 0.01;
									}
									rushingyardsfactor = 0.01;
									totalyardsfactor += 0.01;
								}
								totalyardsfactor = 0.01;
								firstdownsfactor += 0.01;
							}
							firstdownsfactor = 0.01;
							pointsfactor += 0.01;
						}
						pointsfactor = 0.01;
						oppfourthdownefffactor += 0.01;
					}
					oppfourthdownefffactor = 0.01;
					fourthdownefffactor += 0.01;
				}
				fourthdownefffactor = 0.01;
				oppthirddownefffactor += 0.01;
			}
			thirddownefffactor = 0.01;
			thirddownefffactor += 0.01;
		}

		// Close the connection
		closeConnections(DATAMINERDB, printWriter);

		LOGGER.info("Exiting testFactors()");
	}

	/**
	 * 
	 * @param X
	 * @param weekstart
	 * @param weekend
	 * @param year
	 * @param useweekratings
	 * @param filename
	 */
	public void compareAllSpread(Integer X, Integer weekstart, Integer weekend, Integer year, Boolean useweekratings, String filename) {
		LOGGER.info("Entering compareAllSpread()");
		LOGGER.debug("weekstart: " + weekstart);
		LOGGER.debug("weekend: " + weekend);
		LOGGER.debug("year: " + year);
		this.USE_WEEK_RANKINGS = useweekratings;

		// Open the connection
		DATAMINERDB.start();
		PrintWriter printWriter = null;

		try {
			final FileWriter fileWriter = new FileWriter(filename);
			printWriter = new PrintWriter(fileWriter);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		// Header
		printWriter.print("Game, Away Score, Home Score, Game Delta, Line Favorite, Line, Week, Win/Loss, Line Win/Loss, Game Date\n");
		
		// Setup Massey Ratings
		setupMasseyRatings(year, weekstart - 1, weekend);

		// Get get the football games
		vegasInsiderGames = vips.getNcaafGameData(year, 1, weekend, false);
	
		final List<VegasInsiderGame> vigs = vips.getNcaafGameData(year, weekstart, weekend, false);

		for (VegasInsiderGame ncaafGame : vigs) {
			try {
				if (SHOW_INFO) {
					LOGGER.error("ncaafGame: " + ncaafGame);
				}

				final String awayname = ncaafGame.getAwayteamdata().getTeamname().toUpperCase(); 
				final String homename = ncaafGame.getHometeamdata().getTeamname().toUpperCase();
				final boolean awayfound = foundTeam(awayname);
				final boolean homefound = foundTeam(homename);
				final boolean awayisfbs = ncaafGame.getAwayteamdata().getIsfbs();
				final boolean homeisfbs = ncaafGame.getHometeamdata().getIsfbs();

				if ((awayname != null || homename != null) && (awayfound || homefound)) {
					final List<String> didwin = new ArrayList<String>();
					final List<String> deltadidwin = new ArrayList<String>();

					clearList();
					
					masseyweek = ncaafGame.getWeek();

					// Get score delta
					final Integer homefinalscore = ncaafGame.getHometeamdata().getFinalscore();
					final Integer awayfinalscore = ncaafGame.getAwayteamdata().getFinalscore();
					final Integer gamedelta = determineScoreDelta(homefinalscore, awayfinalscore, ncaafGame);

					clearList();
					Double spread = runGameAlgorithm(ncaafGame.getWeek(), ncaafGame.getYear(), ncaafGame.getAwayteamdata().getTeamname(), ncaafGame.getHometeamdata().getTeamname(), awayisfbs, homeisfbs, true);
					Map<String, String> results = processResults(awayfinalscore, homefinalscore, spread, ncaafGame);
					didwin.add(results.get("outcome"));
					deltadidwin.add(results.get("deltaresults"));
					
					clearList();
					spread = runGameAlgorithm(ncaafGame.getWeek(), ncaafGame.getYear(), ncaafGame.getAwayteamdata().getTeamname(), ncaafGame.getHometeamdata().getTeamname(), awayisfbs, homeisfbs, false);
					results = processResults(awayfinalscore, homefinalscore, spread, ncaafGame);
					didwin.add(results.get("outcome"));
					deltadidwin.add(results.get("deltaresults"));
					
					clearList();
					spread = runGameAlgorithmLastX(3, ncaafGame.getWeek(), ncaafGame.getYear(), ncaafGame.getAwayteamdata().getTeamname(), ncaafGame.getHometeamdata().getTeamname(), awayisfbs, homeisfbs, true);
					results = processResults(awayfinalscore, homefinalscore, spread, ncaafGame);
					didwin.add(results.get("outcome"));
					deltadidwin.add(results.get("deltaresults"));
					
					clearList();
					spread = runGameAlgorithmLastX(3, ncaafGame.getWeek(), ncaafGame.getYear(), ncaafGame.getAwayteamdata().getTeamname(), ncaafGame.getHometeamdata().getTeamname(), awayisfbs, homeisfbs, false);
					results = processResults(awayfinalscore, homefinalscore, spread, ncaafGame);
					didwin.add(results.get("outcome"));
					deltadidwin.add(results.get("deltaresults"));
					
					clearList();
					spread = runGameAlgorithmLastXHomeAway(3, ncaafGame.getWeek(), ncaafGame.getYear(), ncaafGame.getAwayteamdata().getTeamname(), ncaafGame.getHometeamdata().getTeamname(), awayisfbs, homeisfbs);
					results = processResults(awayfinalscore, homefinalscore, spread, ncaafGame);
					didwin.add(results.get("outcome"));
					deltadidwin.add(results.get("deltaresults"));

					String finaloutcome = "";
					boolean issame = true;
					for (String result : didwin) {
						if (finaloutcome.length() > 0) {
							if (!result.equals(finaloutcome)) {
								issame = false;
							}
						}
						finaloutcome = result;
					}
					if (!issame) {
						finaloutcome = "";
					}

					String deltafinaloutcome = "";
					issame = true;
					for (String result : deltadidwin) {
						if (deltafinaloutcome.length() > 0) {
							if (!result.equals(deltafinaloutcome)) {
								issame = false;
							}
						}
						deltafinaloutcome = result;
					}
					if (!issame) {
						deltafinaloutcome = "";
					}

					printWriter.print(ncaafGame.getAwayteamdata().getTeamname() + " vs " + ncaafGame.getHometeamdata().getTeamname() + "," + awayfinalscore + "," + homefinalscore
							+ "," + gamedelta + "," + ncaafGame.getLinefavorite() + "," + ncaafGame.getLine() + "," + ncaafGame.getWeek() + "," + finaloutcome + "," + deltafinaloutcome + "," + ncaafGame.getDate() + "\n");
				} else {
					LOGGER.error("Could not find away: " + awayname + " home: " + homename); 
				}
			} catch (Throwable t) {
				LOGGER.debug("Cannot get spread for " + ncaafGame.getAwayteamdata().getTeamname() + " " + ncaafGame.getHometeamdata().getTeamname() + " " + ncaafGame.getAwayteamdata().getFinalscore() + " " + ncaafGame.getHometeamdata().getFinalscore()
						+ " Line: " + ncaafGame.getLinefavorite() + " " + ncaafGame.getLine() + " week: " + ncaafGame.getWeek());
				LOGGER.error(t.getMessage(), t);
			}
		}

		// Close the connection
		closeConnections(DATAMINERDB, printWriter);

		LOGGER.info("Exiting compareAllSpread()");
	}

	/**
	 * 
	 * @param X
	 * @param weekstart
	 * @param weekend
	 * @param year
	 * @param useweekratings
	 * @param filename
	 */
	public void combineAllSpread(Integer X, Integer weekstart, Integer weekend, Integer year, Boolean useweekratings, String filename) {
		LOGGER.info("Entering combineAllSpread()");
		LOGGER.debug("weekstart: " + weekstart);
		LOGGER.debug("weekend: " + weekend);
		LOGGER.debug("year: " + year);
		this.USE_WEEK_RANKINGS = useweekratings;

		// Setup Excel and DB
		final PrintWriter printWriter = startAlgorithm(DATAMINERDB, filename);
		
		// Setup Massey Ratings
		setupMasseyRatings(year, weekstart - 1, weekend);

		// Get get the football games
		vegasInsiderGames = vips.getNcaafGameData(year, 1, weekend, false);
		final List<VegasInsiderGame> vigs = vips.getNcaafGameData(year, weekstart, weekend, false);

		for (VegasInsiderGame ncaafGame : vigs) {
			try {
				if (SHOW_INFO) {
					LOGGER.error("ncaafGame: " + ncaafGame);
				}

				final String awayname = ncaafGame.getAwayteamdata().getTeamname().toUpperCase(); 
				final String homename = ncaafGame.getHometeamdata().getTeamname().toUpperCase();
				final boolean awayfound = foundTeam(awayname);
				final boolean homefound = foundTeam(homename);
				final boolean awayisfbs = ncaafGame.getAwayteamdata().getIsfbs();
				final boolean homeisfbs = ncaafGame.getHometeamdata().getIsfbs();

				if ((awayname != null || homename != null) && (awayfound || homefound)) {
					clearList();

					masseyweek = ncaafGame.getWeek();

					runGameAlgorithm(ncaafGame.getWeek(), ncaafGame.getYear(), ncaafGame.getAwayteamdata().getTeamname(), ncaafGame.getHometeamdata().getTeamname(), awayisfbs, homeisfbs, true);
					runGameAlgorithm(ncaafGame.getWeek(), ncaafGame.getYear(), ncaafGame.getAwayteamdata().getTeamname(), ncaafGame.getHometeamdata().getTeamname(), awayisfbs, homeisfbs, false);
					runGameAlgorithmLastX(X, ncaafGame.getWeek(), ncaafGame.getYear(), ncaafGame.getAwayteamdata().getTeamname(), ncaafGame.getHometeamdata().getTeamname(), awayisfbs, homeisfbs, true);
					runGameAlgorithmLastX(X, ncaafGame.getWeek(), ncaafGame.getYear(), ncaafGame.getAwayteamdata().getTeamname(), ncaafGame.getHometeamdata().getTeamname(), awayisfbs, homeisfbs, false);
					runGameAlgorithmLastXHomeAway(X, ncaafGame.getWeek(), ncaafGame.getYear(), ncaafGame.getAwayteamdata().getTeamname(), ncaafGame.getHometeamdata().getTeamname(), awayisfbs, homeisfbs);
					Double spread = this.determineSpread(ncaafGame.getAwayteamdata().getTeamname(), ncaafGame.getHometeamdata().getTeamname());

					if (SHOW_INFO) {
						LOGGER.error("spread: " + spread);
					}

					endTransaction(printWriter, spread, new Double(9.5), new Double(30), ncaafGame);
				} else {
					LOGGER.error("Could not find away: " + awayname + " home: " + homename); 
				}
			} catch (Throwable t) {
				LOGGER.debug("Cannot get spread for " + ncaafGame.getAwayteamdata().getTeamname() + " " + ncaafGame.getHometeamdata().getTeamname() + " " + ncaafGame.getAwayteamdata().getFinalscore() + " " + ncaafGame.getHometeamdata().getFinalscore()
						+ " Line: " + ncaafGame.getLinefavorite() + " " + ncaafGame.getLine() + " week: " + ncaafGame.getWeek());
				LOGGER.error(t.getMessage(), t);
			}
		}

		// Close the connection
		closeConnections(DATAMINERDB, printWriter);

		LOGGER.info("Exiting combineAllSpread()");
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param useweekratings
	 * @param awayteam
	 * @param hometeam
	 * @param awayisfbs
	 * @param homeisfbs
	 * @param compareall
	 * @param showdebug
	 */
	public void determineSpreadsManual(Integer week, Integer year, Boolean useweekratings, String awayteam, String hometeam, boolean awayisfbs, boolean homeisfbs, boolean compareall, boolean showdebug) {
		LOGGER.info("Entering determineSpreadsManual()");
		this.USE_WEEK_RANKINGS = useweekratings;

		// Open the connection
		DATAMINERDB.start();

		// Setup Massey Ratings
		setupMasseyRatings(year, week, week);

		try {
			if (awayteam != null || hometeam != null) {
				this.showspreadonly = showdebug;
				this.clearList();

				Double spread = this.runGameAlgorithm(week, year, awayteam, hometeam, awayisfbs, homeisfbs, compareall);
				LOGGER.error("spread: " + spread);

				this.showspreadonly = false;
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		// Close the connection
		DATAMINERDB.complete();

		LOGGER.info("Exiting determineSpreadsManual()");
	}

	/**
	 * 
	 * @param X
	 * @param week
	 * @param year
	 * @param useweekratings
	 * @param awayteam
	 * @param hometeam
	 * @param awayisfbs
	 * @param homeisfbs
	 * @param compareall
	 * @param showdebug
	 */
	public void determineSpreadsLastXManual(Integer X, Integer week, Integer year, Boolean useweekratings, String awayteam, String hometeam, boolean awayisfbs, boolean homeisfbs, boolean compareall, boolean showdebug) {
		LOGGER.info("Entering determineSpreadsLastXManual()");
		this.USE_WEEK_RANKINGS = useweekratings;

		// Open the connection
		DATAMINERDB.start();

		// Setup Massey Ratings
		setupMasseyRatings(year, week - 1, week);

		// Get get the football games
		vegasInsiderGames = vips.getNcaafGameData(year, 1, week, false);

		try {
			if (awayteam != null || hometeam != null) {
				this.clearList();
				this.SHOW_INFO = true;
				this.showspreadonly = showdebug;
				masseyweek = week;

				Double spread = this.runGameAlgorithmLastX(X, week, year, awayteam, hometeam, awayisfbs, homeisfbs, compareall);
				LOGGER.error("spread1: " + spread);

				this.SHOW_INFO = false;
				this.showspreadonly = false;
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		// Close the connection
		DATAMINERDB.complete();

		LOGGER.info("Exiting determineSpreadsLastXManual()");
	}

	/**
	 * 
	 * @param weekstart
	 * @param weekend
	 * @param year
	 * @param useweekratings
	 * @param filename
	 * @param compareall
	 */
	public void determineSpreads(Integer weekstart, Integer weekend, Integer year, Boolean useweekratings, String filename, boolean compareall) {
		LOGGER.info("Entering determineSpreads()");
		LOGGER.debug("weekstart: " + weekstart);
		LOGGER.debug("weekend: " + weekend);
		LOGGER.debug("year: " + year);
		this.USE_WEEK_RANKINGS = useweekratings;

		// Setup Massey Ratings
		setupMasseyRatings(year, weekstart - 1, weekend);

		// Get get the football games
		vegasInsiderGames = vips.getNcaafGameData(year, 1, weekend, false);
		final PrintWriter printWriter = startAlgorithm(DATAMINERDB, filename);
		final List<VegasInsiderGame> vigs = vips.getNcaafGameData(year, weekstart, weekend, false);

		for (VegasInsiderGame ncaafGame : vigs) {
			try {
				if (SHOW_INFO) {
					LOGGER.error("ncaafGame: " + ncaafGame);
				}

				final String awayname = ncaafGame.getAwayteamdata().getTeamname().toUpperCase(); 
				final String homename = ncaafGame.getHometeamdata().getTeamname().toUpperCase();
				final boolean awayfound = foundTeam(awayname);
				final boolean homefound = foundTeam(homename);
				final boolean awayisfbs = ncaafGame.getAwayteamdata().getIsfbs();
				final boolean homeisfbs = ncaafGame.getHometeamdata().getIsfbs();

				if ((awayname != null || homename != null) && (awayfound || homefound)) {
					clearList();

					LOGGER.error("masseyweek: " + ncaafGame.getWeek());
					masseyweek = ncaafGame.getWeek();

					final Double spread = runGameAlgorithm(ncaafGame.getWeek(), ncaafGame.getYear(), ncaafGame.getAwayteamdata().getTeamname(), ncaafGame.getHometeamdata().getTeamname(), awayisfbs, homeisfbs, compareall);
					if (SHOW_INFO) {
						LOGGER.error("spread: " + spread);
					}

					endTransaction(printWriter, spread, new Double(9.5), new Double(30), ncaafGame);
				} else {
					LOGGER.error("Could not find away: " + awayname + " home: " + homename); 
				}
			} catch (Throwable t) {
				LOGGER.debug("Cannot get spread for " + ncaafGame.getAwayteamdata().getTeamname() + " " + ncaafGame.getHometeamdata().getTeamname() + " " + ncaafGame.getAwayteamdata().getFinalscore() + " " + ncaafGame.getHometeamdata().getFinalscore()
						+ " Line: " + ncaafGame.getLinefavorite() + " " + ncaafGame.getLine() + " week: " + ncaafGame.getWeek());
				LOGGER.error(t.getMessage(), t);
			}
		}

		// Close the connection
		closeConnections(DATAMINERDB, printWriter);

		LOGGER.info("Exiting determineSpreads()");
	}

	/**
	 * 
	 * @param X
	 * @param weekstart
	 * @param weekend
	 * @param year
	 * @param useweekratings
	 * @param filename
	 * @param compareall
	 */
	public void determineLastXSpreads(Integer X, Integer weekstart, Integer weekend, Integer year, Boolean useweekratings, String filename, boolean compareall) {
		LOGGER.info("Entering determineLastTwoSpreads()");
		LOGGER.debug("weekstart: " + weekstart);
		LOGGER.debug("weekend: " + weekend);
		LOGGER.debug("year: " + year);
		this.USE_WEEK_RANKINGS = useweekratings;

		// Setup Massey Ratings
		setupMasseyRatings(year, weekstart - 1, weekend);

		// Get get the football games
		vegasInsiderGames = vips.getNcaafGameData(year, 1, weekend, false);
		final PrintWriter printWriter = startAlgorithm(DATAMINERDB, filename);
		final List<VegasInsiderGame> vigs = vips.getNcaafGameData(year, weekstart, weekend, false);

		for (VegasInsiderGame ncaafGame : vigs) {
			try {
				if (SHOW_INFO) {
					LOGGER.error("ncaafGame: " + ncaafGame);
				}

				final String awayname = ncaafGame.getAwayteamdata().getTeamname().toUpperCase(); 
				final String homename = ncaafGame.getHometeamdata().getTeamname().toUpperCase();
				final boolean awayfound = foundTeam(awayname);
				final boolean homefound = foundTeam(homename);
				final boolean awayisfbs = ncaafGame.getAwayteamdata().getIsfbs();
				final boolean homeisfbs = ncaafGame.getAwayteamdata().getIsfbs();

				if ((awayname != null || homename != null) && (awayfound || homefound)) {
					clearList();

					LOGGER.error("masseyweek: " + ncaafGame.getWeek());
					masseyweek = ncaafGame.getWeek();
					final Double spread = runGameAlgorithmLastX(X, ncaafGame.getWeek(), ncaafGame.getYear(), ncaafGame.getAwayteamdata().getTeamname(), ncaafGame.getHometeamdata().getTeamname(), awayisfbs, homeisfbs, compareall);
					if (SHOW_INFO) {
						LOGGER.error("spread: " + spread);
					}

					endTransaction(printWriter, spread, new Double(9.5), new Double(38), ncaafGame);
				} else {
					LOGGER.error("Could not find away: " + awayname + " home: " + homename); 
				}
			} catch (Throwable t) {
				LOGGER.debug("Cannot get spread for " + ncaafGame.getAwayteamdata().getTeamname() + " " + ncaafGame.getHometeamdata().getTeamname() + " " + ncaafGame.getAwayteamdata().getFinalscore() + " " + ncaafGame.getHometeamdata().getFinalscore()
				+ " Line: " + ncaafGame.getLinefavorite() + " " + ncaafGame.getLine() + " week: " + ncaafGame.getWeek());
				LOGGER.error(t.getMessage(), t);
			}
		}

		// Close the connection
		closeConnections(DATAMINERDB, printWriter);

		LOGGER.info("Exiting determineLastTwoSpreads()");
	}

	/**
	 * 
	 * @param X
	 * @param weekstart
	 * @param weekend
	 * @param year
	 * @param useweekratings
	 * @param filename
	 */
	public void determineLastXSpreadsHomeAway(Integer X, Integer weekstart, Integer weekend, Integer year, Boolean useweekratings, String filename) {
		LOGGER.info("Entering determineLastXSpreadsHomeAway()");
		LOGGER.debug("weekstart: " + weekstart);
		LOGGER.debug("weekend: " + weekend);
		LOGGER.debug("year: " + year);
		this.USE_WEEK_RANKINGS = useweekratings;

		// Setup Massey Ratings
		setupMasseyRatings(year, weekend, weekend);
		final PrintWriter printWriter = startAlgorithm(DATAMINERDB, filename);
		final List<VegasInsiderGame> vigs = vips.getNcaafGameData(year, weekstart, weekend, false);

		for (VegasInsiderGame ncaafGame : vigs) {
			try {
				if (SHOW_INFO) {
					LOGGER.error("ncaafGame: " + ncaafGame);
				}
				final String awayname = ncaafGame.getAwayteamdata().getTeamname().toUpperCase(); 
				final String homename = ncaafGame.getHometeamdata().getTeamname().toUpperCase();
				final boolean awayfound = foundTeam(awayname);
				final boolean homefound = foundTeam(homename);
				final boolean awayisfbs = ncaafGame.getAwayteamdata().getIsfbs();
				final boolean homeisfbs = ncaafGame.getAwayteamdata().getIsfbs();

				if ((awayname != null || homename != null) && (awayfound || homefound)) {
					clearList();

					final Double spread = runGameAlgorithmLastXHomeAway(X, ncaafGame.getWeek(), ncaafGame.getYear(), ncaafGame.getAwayteamdata().getTeamname(), ncaafGame.getHometeamdata().getTeamname(), awayisfbs, homeisfbs);
					if (SHOW_INFO) {
						LOGGER.error("spread: " + spread);
					}

					endTransaction(printWriter, spread, new Double(9.5), new Double(30), ncaafGame);
				} else {
					LOGGER.error("Could not find away: " + awayname + " home: " + homename); 
				}
			} catch (Throwable t) {
				LOGGER.debug("Cannot get spread for " + ncaafGame.getAwayteamdata().getTeamname() + " " + ncaafGame.getHometeamdata().getTeamname() + " " + ncaafGame.getAwayteamdata().getFinalscore() + " " + ncaafGame.getHometeamdata().getFinalscore()
						+ " Line: " + ncaafGame.getLinefavorite() + " " + ncaafGame.getLine() + " week: " + ncaafGame.getWeek());
				LOGGER.error(t.getMessage(), t);
			}
		}

		// Close the connection
		closeConnections(DATAMINERDB, printWriter);

		LOGGER.info("Exiting determineLastXSpreadsHomeAway()");
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param fromdate
	 * @param todate
	 * @return
	 */
	public List<XandYObject> plotXandYDelta(String x, String y, Date fromdate, Date todate) {
		LOGGER.info("Entering plotXandYDelta()");
		List<XandYObject> xyObjects = null;

		try {
			DATAMINERDB.start();
			xyObjects = DATAMINERDB.plotXandYDelta(x, y, fromdate, todate);
			DATAMINERDB.complete();
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting plotXandYDelta()");
		return xyObjects;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param fromdate
	 * @param todate
	 * @return
	 */
	public List<XandYObject> plotXandY(String x, String y, Date fromdate, Date todate) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoODataMiner#getAwayTurnovers(com.ticketadvantage.services.dao.sites.espn.EspnGameData)
	 */
	@Override
	protected int getAwayTurnovers(EspnGameData game) {
		int turnovers = 0;

		final EspnCollegeFootballGameData espnGame = (EspnCollegeFootballGameData)game;
		turnovers = espnGame.getAwayturnovers().intValue();

		return turnovers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoODataMiner#getHomeTurnovers(com.ticketadvantage.services.dao.sites.espn.EspnGameData)
	 */
	@Override
	protected int getHomeTurnovers(EspnGameData game) {
		int turnovers = 0;

		final EspnCollegeFootballGameData espnGame = (EspnCollegeFootballGameData)game;
		turnovers = espnGame.getHometurnovers().intValue();

		return turnovers;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param awayteam
	 * @param hometeam
	 * @param awayisfbs
	 * @param homeisfbs
	 * @param compareall
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	protected Double runGameAlgorithm(Integer week, Integer year, String awayteam, String hometeam, boolean awayisfbs, boolean homeisfbs, boolean compareall) throws BatchException, SQLException {
		LOGGER.info("Entering runGameAlgorithm()");
		Double spread = null;

		if (SHOW_INFO) {
			LOGGER.error("awayteam: " + awayteam);
			LOGGER.error("hometeam: " + hometeam);
			LOGGER.error("week: " + week);
			LOGGER.error("year: " + year);
		}

		final List<EspnCollegeFootballGameData> awaygames = new ArrayList<EspnCollegeFootballGameData>();
		final List<EspnCollegeFootballGameData> homegames = new ArrayList<EspnCollegeFootballGameData>();

		List<EspnCollegeFootballGameData> games = DATAMINERDB.getEspnNcaafGameDataByWeekByTeam(awayteam, week.intValue(), year.intValue());
		if (SHOW_INFO) {
			LOGGER.error("awayteam games.size(): " + games.size());
		}

		if (games == null || games.size() == 0) {
			games = DATAMINERDB.getEspnNcaafGameDataByYearByTeam(awayteam, year.intValue() - 1);

			for (EspnCollegeFootballGameData game : games) {
				if (game.getAwaycollegename().equals(awayteam)) {
					awaygames.add(game);
				} else if (game.getHomecollegename().equals(awayteam)) {
					awaygames.add(game);
				}
			}
		} else {
			for (EspnCollegeFootballGameData game : games) {
				if (game.getWeek().intValue() < week.intValue() &&
					game.getYear().intValue() <= year.intValue()) {
					if (game.getAwaycollegename().equals(awayteam)) {
						awaygames.add(game);
					} else if (game.getHomecollegename().equals(awayteam)) {
						awaygames.add(game);
					}
				}
			}
			
			if (awaygames.size() == 0) {
				games = DATAMINERDB.getEspnNcaafGameDataByYearByTeam(awayteam, year.intValue() - 1);

				for (EspnCollegeFootballGameData game : games) {
					if (game.getAwaycollegename().equals(awayteam)) {
						awaygames.add(game);
					} else if (game.getHomecollegename().equals(awayteam)) {
						awaygames.add(game);
					}
				}
			}
		}

		games = DATAMINERDB.getEspnNcaafGameDataByWeekByTeam(hometeam, week.intValue(), year.intValue());
		if (SHOW_INFO) {
			LOGGER.error("hometeam games.size(): " + games.size());
		}

		if (games == null || games.size() == 0) {
			games = DATAMINERDB.getEspnNcaafGameDataByYearByTeam(hometeam, year.intValue() - 1);

			for (EspnCollegeFootballGameData game : games) {
				if (game.getAwaycollegename().equals(hometeam)) {
					homegames.add(game);
				} else if (game.getHomecollegename().equals(hometeam)) {
					homegames.add(game);
				}
			}
		} else {
			for (EspnCollegeFootballGameData game : games) {
				if (game.getWeek().intValue() < week.intValue() &&
					game.getYear().intValue() <= year.intValue()) {
					if (game.getAwaycollegename().equals(hometeam)) {
						homegames.add(game);
					} else if (game.getHomecollegename().equals(hometeam)) {
						homegames.add(game);
					}
				}
			}
			
			if (homegames.size() == 0) {
				games = DATAMINERDB.getEspnNcaafGameDataByYearByTeam(hometeam, year.intValue() - 1);

				for (EspnCollegeFootballGameData game : games) {
					if (game.getAwaycollegename().equals(hometeam)) {
						homegames.add(game);
					} else if (game.getHomecollegename().equals(hometeam)) {
						homegames.add(game);
					}
				}
			}
		}

		// Compare all of the games and determine spread
		spread = processSpread(awayteam, hometeam, compareall, awaygames, homegames);

		LOGGER.info("Exiting runGameAlgorithm()");
		return spread;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param awayteam
	 * @param hometeam
	 * @param awayisfbs
	 * @param homeisfbs
	 * @param compareall
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	protected Double runGameAlgorithmLastX(Integer X, Integer week, Integer year, String awayteam, String hometeam, boolean awayisfbs, boolean homeisfbs, boolean compareall) throws BatchException, SQLException {
		LOGGER.info("Entering runGameAlgorithmLastX()");
		if (SHOW_INFO) {
			LOGGER.error("awayteam: " + awayteam);
			LOGGER.error("hometeam: " + hometeam);
			LOGGER.error("week: " + week);
			LOGGER.error("year: " + year);
		}
		Double spread = null;

		final List<EspnCollegeFootballGameData> awaygames = new ArrayList<EspnCollegeFootballGameData>();
		final List<EspnCollegeFootballGameData> homegames = new ArrayList<EspnCollegeFootballGameData>();
		List<EspnCollegeFootballGameData> games = DATAMINERDB.getEspnNcaafGameDataByWeekByTeam(awayteam, week.intValue(), year.intValue());
		if (SHOW_INFO) {
			LOGGER.error("awayteam games.size(): " + games.size());
		}

		if (games == null || games.size() == 0) {
			games = DATAMINERDB.getEspnNcaafGameDataByYearByTeam(awayteam, year.intValue() - 1);

			for (EspnCollegeFootballGameData game : games) {
				if (this.vegasInsiderGames != null && vegasInsiderGames.size() > 0) {
					for (VegasInsiderGame vig : vegasInsiderGames) {
						if (vig.getAwayteamdata().getTeamname().equals(game.getAwayteam()) && 
							vig.getHometeamdata().getTeamname().equals(game.getHometeam()) && 
							vig.getWeek().intValue() == game.getWeek().intValue() && 
							vig.getYear().intValue() == game.getYear().intValue()) {
							game.setLine(vig.getLine());
							game.setLinefavorite(vig.getLinefavorite());
						}
					}
				}

				if (game.getAwaycollegename().equals(awayteam)) {
					awaygames.add(game);
				} else if (game.getHomecollegename().equals(awayteam)) {
					awaygames.add(game);
				}
			}
		} else {
			for (EspnCollegeFootballGameData game : games) {
				if (this.vegasInsiderGames != null && vegasInsiderGames.size() > 0) {
					for (VegasInsiderGame vig : vegasInsiderGames) {
						if (vig.getAwayteamdata().getTeamname().equals(game.getAwayteam()) && 
							vig.getHometeamdata().getTeamname().equals(game.getHometeam()) && 
							vig.getWeek().intValue() == game.getWeek().intValue() && 
							vig.getYear().intValue() == game.getYear().intValue()) {
							game.setLine(vig.getLine());
							game.setLinefavorite(vig.getLinefavorite());
						}
					}
				}

				if (game.getWeek().intValue() < week.intValue() &&
					game.getYear().intValue() <= year.intValue()) {
					if (game.getAwaycollegename().equals(awayteam)) {
						if (awaygames.size() < X.intValue()) {
							awaygames.add(game);
						}
					} else if (game.getHomecollegename().equals(awayteam)) {
						if (awaygames.size() < X.intValue()) {
							awaygames.add(game);
						}
					}
				}
			}
			
			if (awaygames.size() == 0) {
				games = DATAMINERDB.getEspnNcaafGameDataByYearByTeam(awayteam, year.intValue() - 1);

				for (EspnCollegeFootballGameData game : games) {
					if (this.vegasInsiderGames != null && vegasInsiderGames.size() > 0) {
						for (VegasInsiderGame vig : vegasInsiderGames) {
							if (vig.getAwayteamdata().getTeamname().equals(game.getAwayteam()) && 
								vig.getHometeamdata().getTeamname().equals(game.getHometeam()) && 
								vig.getWeek().intValue() == game.getWeek().intValue() && 
								vig.getYear().intValue() == game.getYear().intValue()) {
								game.setLine(vig.getLine());
								game.setLinefavorite(vig.getLinefavorite());
							}
						}
					}

					if (game.getAwaycollegename().equals(awayteam)) {
						awaygames.add(game);
					} else if (game.getHomecollegename().equals(awayteam)) {
						awaygames.add(game);
					}
				}
			}
		}

		games = DATAMINERDB.getEspnNcaafGameDataByWeekByTeam(hometeam, week.intValue(), year.intValue());
		if (SHOW_INFO) {
			LOGGER.error("hometeam games.size(): " + games.size());
		}

		if (games == null || games.size() == 0) {
			games = DATAMINERDB.getEspnNcaafGameDataByYearByTeam(hometeam, year.intValue() - 1);

			for (EspnCollegeFootballGameData game : games) {
				if (this.vegasInsiderGames != null && vegasInsiderGames.size() > 0) {
					for (VegasInsiderGame vig : vegasInsiderGames) {
						if (vig.getAwayteamdata().getTeamname().equals(game.getAwayteam()) && 
							vig.getHometeamdata().getTeamname().equals(game.getHometeam()) && 
							vig.getWeek().intValue() == game.getWeek().intValue() && 
							vig.getYear().intValue() == game.getYear().intValue()) {
							game.setLine(vig.getLine());
							game.setLinefavorite(vig.getLinefavorite());
						}
					}
				}

				if (game.getAwaycollegename().equals(hometeam)) {
					homegames.add(game);
				} else if (game.getHomecollegename().equals(hometeam)) {
					homegames.add(game);
				}
			}
		} else {
			for (EspnCollegeFootballGameData game : games) {
				if (this.vegasInsiderGames != null && vegasInsiderGames.size() > 0) {
					for (VegasInsiderGame vig : vegasInsiderGames) {
						if (vig.getAwayteamdata().getTeamname().equals(game.getAwayteam()) && 
							vig.getHometeamdata().getTeamname().equals(game.getHometeam()) && 
							vig.getWeek().intValue() == game.getWeek().intValue() && 
							vig.getYear().intValue() == game.getYear().intValue()) {
							game.setLine(vig.getLine());
							game.setLinefavorite(vig.getLinefavorite());
						}
					}
				}

				if (game.getWeek().intValue() < week.intValue() &&
					game.getYear().intValue() <= year.intValue()) {
					if (game.getAwaycollegename().equals(hometeam)) {
						if (homegames.size() < X.intValue()) {
							homegames.add(game);
						}
					} else if (game.getHomecollegename().equals(hometeam)) {
						if (homegames.size() < X.intValue()) {
							homegames.add(game);
						}
					}
				}
			}

			if (homegames.size() == 0) {
				games = DATAMINERDB.getEspnNcaafGameDataByYearByTeam(hometeam, year.intValue() - 1);

				for (EspnCollegeFootballGameData game : games) {
					if (this.vegasInsiderGames != null && vegasInsiderGames.size() > 0) {
						for (VegasInsiderGame vig : vegasInsiderGames) {
							if (vig.getAwayteamdata().getTeamname().equals(game.getAwayteam()) && 
								vig.getHometeamdata().getTeamname().equals(game.getHometeam()) && 
								vig.getWeek().intValue() == game.getWeek().intValue() && 
								vig.getYear().intValue() == game.getYear().intValue()) {
								game.setLine(vig.getLine());
								game.setLinefavorite(vig.getLinefavorite());
							}
						}
					}

					if (game.getAwaycollegename().equals(hometeam)) {
						homegames.add(game);
					} else if (game.getHomecollegename().equals(hometeam)) {
						homegames.add(game);
					}
				}
			}
		}

		// Compare all of the games and determine spread
		spread = processSpread(awayteam, hometeam, compareall, awaygames, homegames);

		LOGGER.info("Exiting runGameAlgorithmLastX()");
		return spread;
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @param awayteam
	 * @param hometeam
	 * @param awayisfbs
	 * @param homeisfbs
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	protected Double runGameAlgorithmLastXHomeAway(Integer X, Integer week, Integer year, String awayteam, String hometeam, boolean awayisfbs, boolean homeisfbs) throws BatchException, SQLException {
		LOGGER.info("Entering runGameAlgorithmLastXHomeAway()");
		Double spread = null;

		if (SHOW_INFO) {
			LOGGER.error("awayteam: " + awayteam);
			LOGGER.error("hometeam: " + hometeam);
			LOGGER.error("week: " + week);
			LOGGER.error("year: " + year);
		}

		final List<EspnCollegeFootballGameData> awaygames = new ArrayList<EspnCollegeFootballGameData>();
		final List<EspnCollegeFootballGameData> homegames = new ArrayList<EspnCollegeFootballGameData>();
		List<EspnCollegeFootballGameData> games = DATAMINERDB.getEspnNcaafGameDataByWeekByTeam(awayteam, week.intValue(), year.intValue());
		if (SHOW_INFO) {
			LOGGER.error("awayteam games.size(): " + games.size());
		}

		if (games == null || games.size() == 0) {
			games = DATAMINERDB.getEspnNcaafGameDataByYearByTeam(awayteam, year.intValue() - 1);

			for (EspnCollegeFootballGameData game : games) {
				if (game.getAwaycollegename().equals(awayteam)) {
					awaygames.add(game);
				} else if (game.getHomecollegename().equals(awayteam)) {
					awaygames.add(game);
				}
			}
		} else {
			for (EspnCollegeFootballGameData game : games) {
				if (game.getWeek().intValue() < week.intValue() &&
					game.getYear().intValue() <= year.intValue()) {
					if (game.getAwaycollegename().equals(awayteam)) {
						if (awaygames.size() < X.intValue()) {
							awaygames.add(game);
						}
					}
				}
			}
			
			if (awaygames.size() == 0) {
				games = DATAMINERDB.getEspnNcaafGameDataByYearByTeam(awayteam, year.intValue() - 1);

				for (EspnCollegeFootballGameData game : games) {
					if (game.getAwaycollegename().equals(awayteam)) {
						awaygames.add(game);
					} else if (game.getHomecollegename().equals(awayteam)) {
						awaygames.add(game);
					}
				}
			}
		}

		games = DATAMINERDB.getEspnNcaafGameDataByWeekByTeam(hometeam, week.intValue(), year.intValue());
		if (SHOW_INFO) {
			LOGGER.error("hometeam games.size(): " + games.size());
		}

		if (games == null || games.size() == 0) {
			games = DATAMINERDB.getEspnNcaafGameDataByYearByTeam(hometeam, year.intValue() - 1);

			for (EspnCollegeFootballGameData game : games) {
				if (game.getAwaycollegename().equals(hometeam)) {
					homegames.add(game);
				} else if (game.getHomecollegename().equals(hometeam)) {
					homegames.add(game);
				}
			}
		} else {
			for (EspnCollegeFootballGameData game : games) {
				if (game.getWeek().intValue() < week.intValue() &&
					game.getYear().intValue() <= year.intValue()) {
					if (game.getHomecollegename().equals(hometeam)) {
						if (homegames.size() < X.intValue()) {
							homegames.add(game);
						}
					}
				}
			}
			
			if (homegames.size() == 0) {
				games = DATAMINERDB.getEspnNcaafGameDataByYearByTeam(hometeam, year.intValue() - 1);

				for (EspnCollegeFootballGameData game : games) {
					if (game.getAwaycollegename().equals(hometeam)) {
						homegames.add(game);
					} else if (game.getHomecollegename().equals(hometeam)) {
						homegames.add(game);
					}
				}
			}
		}

		// Compare all of the games
		for (EspnCollegeFootballGameData homegame : homegames) {
			for (EspnCollegeFootballGameData awaygame : awaygames) {
				processTeams(awayteam, hometeam, awaygame, homegame);
			}
		}

		// Determine the spread
		spread = determineSpread(awayteam, hometeam);

		LOGGER.info("Exiting runGameAlgorithmLastXHomeAway()");
		return spread;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoODataMiner#clearList()
	 */
	@Override
	protected void clearList() {
		thirddownefflist.clear();
		oppthirddownefflist.clear();
		fourthdownefflist.clear();
		oppfourthdownefflist.clear();
		pointslist.clear();
		firstdownslist.clear();
		totalyardslist.clear();
		rushingyardslist.clear();
		rushingattemptslist.clear();
		yardsperrushlist.clear();
		passingyardslist.clear();
		passingcompletionslist.clear();
		passingattemptslist.clear();
		yardsperpasslist.clear();
		turnoverslist.clear();
		possessionlist.clear();		
	}

	/**
	 * 
	 * @param awayteam
	 * @param hometeam
	 * @param compareall
	 * @param awaygames
	 * @param homegames
	 * @return
	 * @throws BatchException
	 * @throws SQLException
	 */
	protected Double processSpread(String awayteam, String hometeam, boolean compareall, List<EspnCollegeFootballGameData> awaygames, List<EspnCollegeFootballGameData> homegames) throws BatchException, SQLException {
		// Compare all of the games
		for (EspnCollegeFootballGameData homegame : homegames) {
			for (EspnCollegeFootballGameData awaygame : awaygames) {
				if (compareall) {
					processTeams(awayteam, hometeam, awaygame, homegame);
				} else {
					if (homegame.getWeek().intValue() == awaygame.getWeek().intValue() && 
						homegame.getYear().intValue() == awaygame.getYear().intValue()) {
						processTeams(awayteam, hometeam, awaygame, homegame);
					}
				}
			}
		}

		// Determine the spread
		return determineSpread(awayteam, hometeam);		
	}

	/**
	 * 
	 * @param awayfinalscore
	 * @param homefinalscore
	 * @param spread
	 * @param ncaafGame
	 * @return
	 */
	protected Map<String, String> processResults(Integer awayfinalscore, Integer homefinalscore, Double spread, VegasInsiderGame ncaafGame) {
		final Map<String, String> results = new HashMap<String, String>();

		// Determine if spread was covered
		final String outcome = didWinSpread(awayfinalscore, homefinalscore, spread, ncaafGame);
		results.put("outcome", outcome);

		if (outcome != null && outcome.equals("W")) {
			results.put("didwin", "true");
		}

		// Get the spread > 9.75
		final String vvalue = determineDeltaLine(spread, new Double(9.75), outcome, ncaafGame);
		results.put("deltaresults", vvalue);
		
		return results;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	protected boolean foundTeam(String name) {
		boolean found = false;

		final Iterator<String> itr = VegasInsiderParser.NCAAFMapping.keySet().iterator();
		while (itr.hasNext()) {
			final String key = itr.next();
			if (VegasInsiderParser.NCAAFMapping.get(key).toUpperCase().equals(name)) {
				found = true;
			}
		}

		return found;
	}

	/**
	 * 
	 * @param isfbs
	 * @param masseyRank
	 * @return
	 */
	private int modifyMasseyRating(Boolean isfbs, Integer masseyRank) {
		double fbsRank = 0.0;

		if (!isfbs) {
			if (masseyRank < 10) {
				fbsRank = 40.0 + (masseyRank * 10.0);
			} else {
				fbsRank = 130.0 + masseyRank;
			}
		} else {
			fbsRank = masseyRank.doubleValue();
		}

		return (int)fbsRank;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoODataMiner#getTeamRatingsForTeams(boolean, java.lang.String, java.lang.String, com.ticketadvantage.services.dao.sites.espn.EspnGameData, com.ticketadvantage.services.dao.sites.espn.EspnGameData)
	 */
	@Override
	protected Map<String, Integer> getTeamRatingsForTeams(String awayteam, String hometeam, EspnGameData awaygame, EspnGameData homegame) {
		final Map<String, Integer> ratings = new HashMap<String, Integer>();
		int awaymassey = 0;
		int homemassey = 0;
		final EspnCollegeFootballGameData agame = (EspnCollegeFootballGameData)awaygame;
		final EspnCollegeFootballGameData hgame = (EspnCollegeFootballGameData)homegame;

		if (agame.getAwayteam().equals(awayteam)) {
			if (USE_WEEK_RANKINGS) {
				awaymassey = agame.getAwaymasseyranking();
			} else {
				final List<MasseyRatingsNcaafData> mrnd = MasseyLatestRatings.get(this.masseyweek.intValue() - 1);
				for (MasseyRatingsNcaafData mr : mrnd) {
					if (mr.getTeam().equals(awayteam)) {
						awaymassey = mr.getRank().intValue();
					}
				}
			}
			awaymassey = modifyMasseyRating(agame.getAwayisfbs(), awaymassey);
		} else if (agame.getHometeam().equals(awayteam)) {
			if (USE_WEEK_RANKINGS) {
				awaymassey = agame.getHomemasseyranking();
			} else {
				final List<MasseyRatingsNcaafData> mrnd = MasseyLatestRatings.get(this.masseyweek.intValue() - 1);
				for (MasseyRatingsNcaafData mr : mrnd) {
					if (mr.getTeam().equals(awayteam)) {
						awaymassey = mr.getRank().intValue();
					}
				}
			}

			awaymassey = modifyMasseyRating(agame.getHomeisfbs(), awaymassey);
		}

		if (hgame.getHometeam().equals(hometeam)) {
			if (USE_WEEK_RANKINGS) {
				homemassey = hgame.getHomemasseyranking();
			} else {
				final List<MasseyRatingsNcaafData> mrnd = MasseyLatestRatings.get(this.masseyweek.intValue() - 1);
				for (MasseyRatingsNcaafData mr : mrnd) {
					if (mr.getTeam().equals(hometeam)) {
						homemassey = mr.getRank().intValue();
					}
				}
			}
			homemassey = modifyMasseyRating(hgame.getHomeisfbs(), homemassey);
		} else if (hgame.getAwayteam().equals(hometeam)) {
			if (USE_WEEK_RANKINGS) {
				homemassey = hgame.getAwaymasseyranking();
			} else {
				final List<MasseyRatingsNcaafData> mrnd = MasseyLatestRatings.get(this.masseyweek.intValue() - 1);
				for (MasseyRatingsNcaafData mr : mrnd) {
					if (mr.getTeam().equals(hometeam)) {
						homemassey = mr.getRank().intValue();
					}
				}
			}
			homemassey = modifyMasseyRating(hgame.getAwayisfbs(), homemassey);
		}

		// Setup the ratings
		ratings.put("away", awaymassey);
		ratings.put("home", homemassey);

		return ratings;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoODataMiner#lastGameRatings(boolean, com.ticketadvantage.services.dao.sites.espn.EspnGameData, com.ticketadvantage.services.dao.sites.espn.EspnGameData)
	 */
	@Override
	protected Map<String, Integer> lastGameRatings(boolean userweekratings, EspnGameData awayGame, EspnGameData homeGame) {
		final Map<String, Integer> lastGamesRatings = new HashMap<String, Integer>();
		final EspnCollegeFootballGameData agame = (EspnCollegeFootballGameData)awayGame;
		final EspnCollegeFootballGameData hgame = (EspnCollegeFootballGameData)homeGame;

		Integer awaygameawaymasseyrating = null;
		Integer awaygamehomemasseyrating = null;
		Integer homegameawaymasseyrating = null;
		Integer homegamehomemasseyrating = null;

		if (userweekratings) {
//			awaygameawaymasseyrating = agame.getAwaymasseyrating().intValue();
//			awaygamehomemasseyrating = agame.getHomemasseyrating().intValue();
//			homegameawaymasseyrating = hgame.getAwaymasseyrating().intValue();
//			homegamehomemasseyrating = hgame.getHomemasseyrating().intValue();
			awaygameawaymasseyrating = agame.getAwaymasseyranking().intValue();
			awaygamehomemasseyrating = agame.getHomemasseyranking().intValue();
			homegameawaymasseyrating = hgame.getAwaymasseyranking().intValue();
			homegamehomemasseyrating = hgame.getHomemasseyranking().intValue();
		} else {
			final List<MasseyRatingsNcaafData> mrnd = MasseyLatestRatings.get(this.masseyweek.intValue() - 1);
			for (MasseyRatingsNcaafData mr : mrnd) {
				if (mr.getTeam().equals(agame.getAwayteam())) {
					awaygameawaymasseyrating = mr.getRank();
				}
			}
			for (MasseyRatingsNcaafData mr : mrnd) {
				if (mr.getTeam().equals(agame.getHometeam())) {
					awaygamehomemasseyrating = mr.getRank();
				}
			}
			for (MasseyRatingsNcaafData mr : mrnd) {
				if (mr.getTeam().equals(hgame.getAwayteam())) {
					homegameawaymasseyrating = mr.getRank();
				}
			}
			for (MasseyRatingsNcaafData mr : mrnd) {
				if (mr.getTeam().equals(hgame.getHometeam())) {
					homegamehomemasseyrating = mr.getRank();
				}
			}
		}

		awaygameawaymasseyrating = modifyMasseyRating(agame.getAwayisfbs(), awaygameawaymasseyrating);
		awaygamehomemasseyrating = modifyMasseyRating(agame.getHomeisfbs(), awaygamehomemasseyrating);
		homegameawaymasseyrating = modifyMasseyRating(hgame.getAwayisfbs(), homegameawaymasseyrating);
		homegamehomemasseyrating = modifyMasseyRating(hgame.getHomeisfbs(), homegamehomemasseyrating);

		if (awaygameawaymasseyrating.floatValue() == 0) {
			awaygameawaymasseyrating = new Integer(MAX_MASSEY_FACTOR);
		}
		if (awaygamehomemasseyrating.floatValue() == 0) {
			awaygamehomemasseyrating = new Integer(MAX_MASSEY_FACTOR);
		}
		if (homegameawaymasseyrating.floatValue() == 0) {
			homegameawaymasseyrating = new Integer(MAX_MASSEY_FACTOR);
		}
		if (homegamehomemasseyrating.floatValue() == 0) {
			homegamehomemasseyrating = new Integer(MAX_MASSEY_FACTOR);
		}

		// Setup the ratings
		lastGamesRatings.put("awayaway", awaygameawaymasseyrating);
		lastGamesRatings.put("awayhome", awaygamehomemasseyrating);
		lastGamesRatings.put("homeaway", homegameawaymasseyrating);
		lastGamesRatings.put("homehome", homegamehomemasseyrating);

		return lastGamesRatings;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoODataMiner#processGameData(java.lang.String, java.lang.String, com.ticketadvantage.services.dao.sites.espn.EspnGameData, com.ticketadvantage.services.dao.sites.espn.EspnGameData, java.lang.Float, java.lang.Float, java.lang.Float, java.lang.Float, java.lang.Float, java.lang.Float)
	 */
	@Override
	protected void processGameData(String awayteam, 
			String hometeam, 
			EspnGameData awaygame, 
			EspnGameData homegame,
			Float awaymassey,
			Float homemassey,
			Float awayfactor, 
			Float aoppfactor, 
			Float homefactor, 
			Float hoppfactor) throws BatchException, SQLException {
		final EspnCollegeFootballGameData agame = (EspnCollegeFootballGameData)awaygame;
		final EspnCollegeFootballGameData hgame = (EspnCollegeFootballGameData)homegame;

		Float atde = null;
		Float aotde = null;
		Float htde = null;
		Float hotde = null;
		Float afde = null;
		Float aofde = null;
		Float hfde = null;
		Float hofde = null;
		Float appg = null;
		Float aoppg = null;
		Float hppg = null;
		Float hoppg = null;
		Float afd = null;
		Float aofd = null;
		Float hfd = null;
		Float hofd = null;
		Float aty = null;
		Float aoty = null;
		Float hty = null;
		Float hoty = null;
		Float ary = null;
		Float aory = null;
		Float hry = null;
		Float hory = null;
		Float ara = null;
		Float aora = null;
		Float hra = null;
		Float hora = null;
		Float aypr = null;
		Float aoypr = null;
		Float hypr = null;
		Float hoypr = null;
		Float apy = null;
		Float aopy = null;
		Float hpy = null;
		Float hopy = null;
		Float apc = null;
		Float aopc = null;
		Float hpc = null;
		Float hopc = null;
		Float apa = null;
		Float aopa = null;
		Float hpa = null;
		Float hopa = null;
		Float aypp = null;
		Float aoypp = null;
		Float hypp = null;
		Float hoypp = null;
		Float at = null;
		Float aot = null;
		Float ht = null;
		Float hot = null;
		Float apt = null;
		Float aopt = null;
		Float hpt = null;
		Float hopt = null;
		boolean isawayaway = false;

		if (agame.getAwaycollegename().equals(awayteam)) {
			atde = divideNumbers(agame.getAwaythirdefficiencymade().floatValue(), agame.getAwaythirdefficiencyattempts().floatValue());
			aotde = divideNumbers(agame.getHomethirdefficiencymade().floatValue(),  agame.getHomethirdefficiencyattempts().floatValue());
			afde = divideNumbers(agame.getAwayfourthefficiencymade().floatValue(), agame.getAwayfourthefficiencyattempts().floatValue());
			aofde = divideNumbers(agame.getHomefourthefficiencymade().floatValue(), agame.getHomefourthefficiencyattempts().floatValue());
			appg = agame.getAwayfinalscore().floatValue();
			aoppg = agame.getHomefinalscore().floatValue();
			afd = agame.getAwayfirstdowns().floatValue();
			aofd = agame.getHomefirstdowns().floatValue();
			aty = agame.getAwaytotalyards().floatValue();
			aoty = agame.getHometotalyards().floatValue();
			ary = agame.getAwayrushingyards().floatValue();
			aory = agame.getHomerushingyards().floatValue();
			ara = agame.getAwayrushingattempts().floatValue();
			aora = agame.getHomerushingattempts().floatValue();
			aypr = agame.getAwayyardsperrush();
			aoypr = agame.getHomeyardsperrush();
			apy = agame.getAwaypassingyards().floatValue();
			aopy = agame.getHomepassingyards().floatValue();
			apc = agame.getAwaypasscomp().floatValue();
			aopc = agame.getHomepasscomp().floatValue();
			apa = agame.getAwaypassattempts().floatValue();
			aopa = agame.getHomepassattempts().floatValue();
			aypp = agame.getAwayyardsperpass();
			aoypp = agame.getHomeyardsperpass();
			at = agame.getAwayturnovers().floatValue();
			aot = agame.getHometurnovers().floatValue();
			apt = agame.getAwaypossessionminutes().floatValue();
			aopt = agame.getHomepossessionminutes().floatValue();
		} else {
			atde = divideNumbers(agame.getHomethirdefficiencymade().floatValue(), agame.getHomethirdefficiencyattempts().floatValue());
			aotde = divideNumbers(agame.getAwaythirdefficiencymade().floatValue(), agame.getAwaythirdefficiencyattempts().floatValue());
			afde = divideNumbers(agame.getHomefourthefficiencymade().floatValue(), agame.getHomefourthefficiencyattempts().floatValue());
			aofde = divideNumbers(agame.getAwayfourthefficiencymade().floatValue(), agame.getAwayfourthefficiencyattempts().floatValue());
			appg = agame.getHomefinalscore().floatValue();
			aoppg = agame.getAwayfinalscore().floatValue();
			afd = agame.getHomefirstdowns().floatValue();
			aofd = agame.getAwayfirstdowns().floatValue();
			aty = agame.getHometotalyards().floatValue();
			aoty = agame.getAwaytotalyards().floatValue();
			ary = agame.getHomerushingyards().floatValue();
			aory = agame.getAwayrushingyards().floatValue();
			ara = agame.getHomerushingattempts().floatValue();
			aora = agame.getAwayrushingattempts().floatValue();
			aypr = agame.getHomeyardsperrush();
			aoypr = agame.getAwayyardsperrush();
			apy = agame.getHomepassingyards().floatValue();
			aopy = agame.getAwaypassingyards().floatValue();
			apc = agame.getHomepasscomp().floatValue();
			aopc = agame.getAwaypasscomp().floatValue();
			apa = agame.getHomepassattempts().floatValue();
			aopa = agame.getAwaypassattempts().floatValue();
			aypp = agame.getHomeyardsperpass();
			aoypp = agame.getAwayyardsperpass();
			at = agame.getHometurnovers().floatValue();
			aot = agame.getAwayturnovers().floatValue();
			apt = agame.getHomepossessionminutes().floatValue();
			aopt = agame.getAwaypossessionminutes().floatValue();
		}

		if (hgame.getAwaycollegename().equals(hometeam)) {
			htde = divideNumbers(hgame.getAwaythirdefficiencymade().floatValue(), hgame.getAwaythirdefficiencyattempts().floatValue());
			hotde = divideNumbers(hgame.getHomethirdefficiencymade().floatValue(), hgame.getHomethirdefficiencyattempts().floatValue());
			hfde = divideNumbers(hgame.getAwayfourthefficiencymade().floatValue(), hgame.getAwayfourthefficiencyattempts().floatValue());
			hofde = divideNumbers(hgame.getHomefourthefficiencymade().floatValue(), hgame.getHomefourthefficiencyattempts().floatValue());
			hppg = hgame.getAwayfinalscore().floatValue();
			hoppg = hgame.getHomefinalscore().floatValue();
			hfd = hgame.getAwayfirstdowns().floatValue();
			hofd = hgame.getHomefirstdowns().floatValue();
			hty = hgame.getAwaytotalyards().floatValue();
			hoty = hgame.getHometotalyards().floatValue();
			hry = hgame.getAwayrushingyards().floatValue();
			hory = hgame.getHomerushingyards().floatValue();
			hra = hgame.getAwayrushingattempts().floatValue();
			hora = hgame.getHomerushingattempts().floatValue();
			hypr = hgame.getAwayyardsperrush();
			hoypr = hgame.getHomeyardsperrush();
			hpy = hgame.getAwaypassingyards().floatValue();
			hopy = hgame.getHomepassingyards().floatValue();
			hpc = hgame.getAwaypasscomp().floatValue();
			hopc = hgame.getHomepasscomp().floatValue();
			hpa = hgame.getAwaypassattempts().floatValue();
			hopa = hgame.getHomepassattempts().floatValue();
			hypp = hgame.getAwayyardsperpass();
			hoypp = hgame.getHomeyardsperpass();
			ht = hgame.getAwayturnovers().floatValue();
			hot = hgame.getHometurnovers().floatValue();
			hpt = hgame.getAwaypossessionminutes().floatValue();
			hopt = hgame.getHomepossessionminutes().floatValue();
		} else {
			htde = divideNumbers(hgame.getHomethirdefficiencymade().floatValue(), hgame.getHomethirdefficiencyattempts().floatValue());
			hotde = divideNumbers(hgame.getAwaythirdefficiencymade().floatValue(), hgame.getAwaythirdefficiencyattempts().floatValue());
			hfde = divideNumbers(hgame.getHomefourthefficiencymade().floatValue(), hgame.getHomefourthefficiencyattempts().floatValue());
			hofde = divideNumbers(hgame.getAwayfourthefficiencymade().floatValue(), hgame.getAwayfourthefficiencyattempts().floatValue());
			hppg = hgame.getHomefinalscore().floatValue();
			hoppg = hgame.getAwayfinalscore().floatValue();
			hfd = hgame.getHomefirstdowns().floatValue();
			hofd = hgame.getAwayfirstdowns().floatValue();
			hty = hgame.getHometotalyards().floatValue();
			hoty = hgame.getAwaytotalyards().floatValue();
			hry = hgame.getHomerushingyards().floatValue();
			hory = hgame.getAwayrushingyards().floatValue();
			hra = hgame.getHomerushingattempts().floatValue();
			hora = hgame.getAwayrushingattempts().floatValue();
			hypr = hgame.getHomeyardsperrush();
			hoypr = hgame.getAwayyardsperrush();
			hpy = hgame.getHomepassingyards().floatValue();
			hopy = hgame.getAwaypassingyards().floatValue();
			hpc = hgame.getHomepasscomp().floatValue();
			hopc = hgame.getAwaypasscomp().floatValue();
			hpa = hgame.getHomepassattempts().floatValue();
			hopa = hgame.getAwaypassattempts().floatValue();
			hypp = hgame.getHomeyardsperpass();
			hoypp = hgame.getAwayyardsperpass();
			ht = hgame.getHometurnovers().floatValue();
			hot = hgame.getAwayturnovers().floatValue();
			hpt = hgame.getHomepossessionminutes().floatValue();
			hopt = hgame.getAwaypossessionminutes().floatValue();
		}

		final Double thirddowneff = calculateValue(awayfactor, homefactor, atde, htde, 1.0);
		final Double oppthirddowneff = -(calculateValue(aoppfactor, hoppfactor, aotde, hotde, 1.0));
		final Double fourthdowneff = calculateValue(awayfactor, homefactor, afde, hfde, 1.0);
		final Double oppfourthdowneff = -(calculateValue(aoppfactor, hoppfactor, aofde, hofde, 1.0));
		final Double points = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, appg, aoppg, hppg, hoppg, 0.15);
		final Double firstdowns = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, afd, aofd, hfd, hofd, 0.06);
		final Double totalyards = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, aty, aoty, hty, hoty, 0.03);
		final Double rushingyards = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, ary, aory, hry, hory, 0.04);
//		final Double rushingattempts = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, ara, aora, hra, hora, 0.02);
		final Double rushingattempts = new Double(0);
		final Double yardsperrush = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, aypr, aoypr, hypr, hoypr, 0.1);
		final Double passingyards = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, apy, aopy, hpy, hopy, 0.03);
//		final Double passingcompletions = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, apc, aopc, hpc, hopc, 0.025);
		final Double passingcompletions = new Double(0);
//		final Double passingattempts = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, apa, aopa, hpa, hopa, 0.01);
		final Double passingattempts = new Double(0);
		final Double yardsperpass = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, aypp, aoypp, hypp, hoypp, 0.025);
		final Double turnovers = calculateValueBothNegative(awayfactor, homefactor, aoppfactor, hoppfactor, at, aot, ht, hot, 1.0);
		final Double possession = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, apt, aopt, hpt, hopt, 0.1);

/*
		final Double thirddowneff = calculateValue(awayfactor, homefactor, atde, htde, thirddownefffactor);
		final Double oppthirddowneff = -(calculateValue(aoppfactor, hoppfactor, aotde, hotde, oppthirddownefffactor));
		final Double fourthdowneff = calculateValue(awayfactor, homefactor, afde, hfde, fourthdownefffactor);
		final Double oppfourthdowneff = -(calculateValue(aoppfactor, hoppfactor, aofde, hofde, oppfourthdownefffactor));

//		final Double points = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, appg, aoppg, hppg, hoppg, pointsfactor);
		final Double points = determineValue(aoppfactor, awayfactor, hoppfactor, homefactor, aoppg, appg, hoppg, hppg, 0.2);
//		Double points = 0.0;

//		final Double firstdowns = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, afd, aofd, hfd, hofd, firstdownsfactor);
		final Double firstdowns = determineValue(aoppfactor, awayfactor, hoppfactor, homefactor, aofd, afd, hofd, hfd, 0.22);

//		final Double totalyards = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, aty, aoty, hty, hoty, totalyardsfactor);
//		final Double totalyards = determineValue(aoppfactor, awayfactor, hoppfactor, homefactor, aoty, aty, hoty, hty, 0.1);
		final Double totalyards = 0.0;

//		Double rushingyards = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, ary, aory, hry, hory, rushingyardsfactor);
		Double rushingyards = determineValue(aoppfactor, awayfactor, hoppfactor, homefactor, aory, ary, hory, hry, 0.22);

//		final Double rushingattempts = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, ara, aora, hra, hora, rushingattemptsfactor);
//		Double rushingattempts = determineValue(aoppfactor, awayfactor, hoppfactor, homefactor, aora, ara, hora, hra, rushingattemptsfactor);
		final Double rushingattempts = new Double(0);

//		final Double yardsperrush = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, aypr, aoypr, hypr, hoypr, yardsperrushfactor);
//		final Double yardsperrush = determineValue(aoppfactor, awayfactor, hoppfactor, homefactor, aoypr, aypr, hoypr, hypr, yardsperrushfactor);
		final Double yardsperrush = 0.0;
		
//		final Double passingyards = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, apy, aopy, hpy, hopy, passingyardsfactor);
		final Double passingyards = determineValue(aoppfactor, awayfactor, hoppfactor, homefactor, aopy, apy, hopy, hpy, 0.15);

//		final Double passingcompletions = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, apc, aopc, hpc, hopc, passingcompletionsfactor);
		final Double passingcompletions = new Double(0);
//		final Double passingattempts = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, apa, aopa, hpa, hopa, passingattemptsfactor);
		final Double passingattempts = new Double(0);
//		final Double yardsperpass = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, aypp, aoypp, hypp, hoypp, yardsperpassfactor);
//		final Double yardsperpass = determineValue(aoppfactor, awayfactor, hoppfactor, homefactor, aoypp, aypp, hoypp, hypp, yardsperpassfactor);
		final Double yardsperpass = 0.0;

		final Double turnovers = calculateValueBothNegative(awayfactor, homefactor, aoppfactor, hoppfactor, at, aot, ht, hot, 0.1);
		final Double possession = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, apt, aopt, hpt, hopt, 0.05);
*/
		thirddownefflist.add(thirddowneff);
		oppthirddownefflist.add(oppthirddowneff);
		fourthdownefflist.add(fourthdowneff);
		oppfourthdownefflist.add(oppfourthdowneff);
		pointslist.add(points);
		firstdownslist.add(firstdowns);
		totalyardslist.add(totalyards);
		rushingyardslist.add(rushingyards);
		rushingattemptslist.add(rushingattempts);
		yardsperrushlist.add(yardsperrush);
		passingyardslist.add(passingyards);
		passingcompletionslist.add(passingcompletions);
		passingattemptslist.add(passingattempts);
		yardsperpasslist.add(yardsperpass);
		turnoverslist.add(turnovers);
		possessionlist.add(possession);

		if (SHOW_INFO) {
			LOGGER.error("awayteam: " + awayteam + " hometeam: " + hometeam);
			LOGGER.error("thirddowneff: " + thirddowneff);
			LOGGER.error("oppthirddowneff: " + oppthirddowneff);
			LOGGER.error("fourthdowneff: " + fourthdowneff);
			LOGGER.error("oppfourthdowneff: " + oppfourthdowneff);
			LOGGER.error("points: " + points);
			LOGGER.error("firstdowns: " + firstdowns);
			LOGGER.error("totalyards: " + totalyards);
			LOGGER.error("rushingyards: " + rushingyards);
			LOGGER.error("rushingattempts: " + rushingattempts);
			LOGGER.error("yardsperrush: " + yardsperrush);
			LOGGER.error("passingyards: " + passingyards);
			LOGGER.error("passingcompletions: " + passingcompletions);
			LOGGER.error("passingattempts: " + passingattempts);
			LOGGER.error("yardsperpass: " + yardsperpass);
			LOGGER.error("turnovers: " + turnovers);
			LOGGER.error("possession: " + possession);
			LOGGER.error("home field: -" + homefieldfactor);

			final Double spread = thirddowneff + oppthirddowneff + fourthdowneff + oppfourthdowneff + points + firstdowns + totalyards + rushingyards + 
					rushingattempts + yardsperrush + passingyards + passingcompletions + passingattempts + yardsperpass + turnovers + possession - homefieldfactor;
			
			if (showspreadonly) {
				LOGGER.error("Spread: " + spread);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoODataMiner#determineSpread(java.lang.String, java.lang.String)
	 */
	@Override
	protected Double determineSpread(String awayteam, String hometeam) {
		Double spread = null;
		final Double thirddowneff = setupStat(thirddownefflist);
		final Double oppthirddowneff = setupStat(oppthirddownefflist);
		final Double fourthdowneff = setupStat(fourthdownefflist);
		final Double oppfourthdowneff = setupStat(oppfourthdownefflist);
		final Double points = setupStat(pointslist);
		final Double firstdowns = setupStat(firstdownslist);
		final Double totalyards = setupStat(totalyardslist);
		final Double rushingyards = setupStat(rushingyardslist);
		final Double rushingattempts = setupStat(rushingattemptslist);
		final Double yardsperrush = setupStat(yardsperrushlist);
		final Double passingyards = setupStat(passingyardslist);
		final Double passingcompletions = setupStat(passingcompletionslist);
		final Double passingattempts = setupStat(passingattemptslist);
		final Double yardsperpass = setupStat(yardsperpasslist);
		final Double turnovers = setupStat(turnoverslist);
		final Double possession = setupStat(possessionlist);

		if (SHOW_INFO) {
			LOGGER.error("awayteam: " + awayteam + " hometeam: " + hometeam);
			LOGGER.error("thirddowneff: " + thirddowneff);
			LOGGER.error("oppthirddowneff: " + oppthirddowneff);
			LOGGER.error("fourthdowneff: " + fourthdowneff);
			LOGGER.error("oppfourthdowneff: " + oppfourthdowneff);
			LOGGER.error("points: " + points);
			LOGGER.error("firstdowns: " + firstdowns);
			LOGGER.error("totalyards: " + totalyards);
			LOGGER.error("rushingyards: " + rushingyards);
			LOGGER.error("rushingattempts: " + rushingattempts);
			LOGGER.error("yardsperrush: " + yardsperrush);
			LOGGER.error("passingyards: " + passingyards);
			LOGGER.error("passingcompletions: " + passingcompletions);
			LOGGER.error("passingattempts: " + passingattempts);
			LOGGER.error("yardsperpass: " + yardsperpass);
			LOGGER.error("turnovers: " + turnovers);
			LOGGER.error("possession: " + possession);
			LOGGER.error("home field: -" + homefieldfactor);
		}

		spread = thirddowneff + oppthirddowneff + fourthdowneff + oppfourthdowneff + points + firstdowns + totalyards + rushingyards + 
				rushingattempts + yardsperrush + passingyards + passingcompletions + passingattempts + yardsperpass + turnovers + possession - homefieldfactor;

		return spread;
	}

	/**
	 * 
	 * @param awaymasseyrating
	 * @param homemasseyrating
	 * @return
	 */
/**
	private Map<String, Float> determineFactor(Float awaymasseyrating, Float homemasseyrating) {
		LOGGER.debug("awaymasseyrating: " + awaymasseyrating);
		LOGGER.debug("homemasseyrating: " + homemasseyrating);

		final Map<String, Float> factors = new HashMap<String, Float>();
		Float awayfinal = null;
		Float homefinal = null;

		if (awaymasseyrating.floatValue() < homemasseyrating.floatValue()) {
			for (int x = 1; x < 24; x++) {
				// UAB 89.25
				// Coastal Carolina 119.66
				if (awaymasseyrating.floatValue() < (10 * x)) {
					if (homemasseyrating.floatValue() < 10) {
						LOGGER.error("awaymasseyrating1: " + awaymasseyrating);
						LOGGER.error("homemasseyrating1: " + homemasseyrating);
						awayfinal = new Float(1);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 20) {
						awayfinal = new Float(1 / 1.05);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 30) {
						awayfinal = new Float(1 / 1.11);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 40) {
						awayfinal = new Float(1 / 1.18);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 50) {
						awayfinal = new Float(1 / 1.26);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1.1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 60) {
						awayfinal = new Float(1 / 1.35);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 70) {
						awayfinal = new Float(1 / 1.45);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 80) {
						awayfinal = new Float(1 / 1.56);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 90) {
						awayfinal = new Float(1 / 1.68);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 100) {
						awayfinal = new Float(1 / 1.81);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 110) {
						awayfinal = new Float(1 / 1.95);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 120) {
						awayfinal = new Float(1 / 2.10);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 130) {
						awayfinal = new Float(1 / 2.26);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 140) {
						awayfinal = new Float(1 / 2.43);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 150) {
						awayfinal = new Float(1 / 2.61);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 160) {
						awayfinal = new Float(1 / 2.80);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 170) {
						awayfinal = new Float(1 / 3.00);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 180) {
						awayfinal = new Float(1 / 3.21);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 190) {
						awayfinal = new Float(1 / 3.43);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 200) {
						awayfinal = new Float(1 / 3.66);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 210) {
						awayfinal = new Float(1 / 3.90);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 220) {
						awayfinal = new Float(1 / 4.15);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (homemasseyrating.floatValue() < 240) {
						awayfinal = new Float(1 / 4.41);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else {
						awayfinal = new Float(1 / 4.68);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					}
				}
			}
		} else {
			for (int x = 1; x < 24; x++) {
				// UAB 89.25
				// Coastal Carolina 119.66
				if (homemasseyrating.floatValue() < (10 * x)) {
					if (awaymasseyrating.floatValue() < 10) {
						LOGGER.error("awaymasseyrating2: " + awaymasseyrating);
						LOGGER.error("homemasseyrating2: " + homemasseyrating);
						awayfinal = new Float(1);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1.1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 20) {
						awayfinal = new Float(1 / 1.05);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1.05);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 30) {
						awayfinal = new Float(1 / 1.11);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1.025);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 40) {
						awayfinal = new Float(1 / 1.18);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1.020);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 50) {
						awayfinal = new Float(1 / 1.26);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 2);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 60) {
						awayfinal = new Float(1 / 1.35);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 70) {
						awayfinal = new Float(1 / 1.45);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 80) {
						awayfinal = new Float(1 / 1.56);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 90) {
						awayfinal = new Float(1 / 1.68);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 100) {
						awayfinal = new Float(1 / 1.81);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 110) {
						awayfinal = new Float(1 / 1.95);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 120) {
						awayfinal = new Float(1 / 2.10);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 130) {
						awayfinal = new Float(1 / 2.26);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 140) {
						awayfinal = new Float(1 / 2.43);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 150) {
						awayfinal = new Float(1 / 2.61);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 160) {
						awayfinal = new Float(1 / 2.80);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 170) {
						awayfinal = new Float(1 / 3.00);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 180) {
						awayfinal = new Float(1 / 3.21);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 190) {
						awayfinal = new Float(1 / 3.43);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 200) {
						awayfinal = new Float(1 / 3.66);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 210) {
						awayfinal = new Float(1 / 3.90);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 220) {
						awayfinal = new Float(1 / 4.15);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else if (awaymasseyrating.floatValue() < 240) {
						awayfinal = new Float(1 / 4.41);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					} else {
						awayfinal = new Float(1 / 4.68);
						awayfinal = ((1 - awayfinal)/x) + awayfinal;
						homefinal = new Float((1 - awayfinal) + 1);
						factors.put("awayfactor", awayfinal);
						factors.put("homefactor", homefinal);
						break;
					}
				}
			}
		}

		return factors;
	}
**/

	/**
	 * @return the showspreadonly
	 */
	public boolean isShowspreadonly() {
		return showspreadonly;
	}

	/**
	 * @param showspreadonly the showspreadonly to set
	 */
	public void setShowspreadonly(boolean showspreadonly) {
		this.showspreadonly = showspreadonly;
	}

	/**
	 * @return the thirddownefffactor
	 */
	public Double getThirddownefffactor() {
		return thirddownefffactor;
	}

	/**
	 * @param thirddownefffactor the thirddownefffactor to set
	 */
	public void setThirddownefffactor(Double thirddownefffactor) {
		this.thirddownefffactor = thirddownefffactor;
	}

	/**
	 * @return the oppthirddownefffactor
	 */
	public Double getOppthirddownefffactor() {
		return oppthirddownefffactor;
	}

	/**
	 * @param oppthirddownefffactor the oppthirddownefffactor to set
	 */
	public void setOppthirddownefffactor(Double oppthirddownefffactor) {
		this.oppthirddownefffactor = oppthirddownefffactor;
	}

	/**
	 * @return the fourthdownefffactor
	 */
	public Double getFourthdownefffactor() {
		return fourthdownefffactor;
	}

	/**
	 * @param fourthdownefffactor the fourthdownefffactor to set
	 */
	public void setFourthdownefffactor(Double fourthdownefffactor) {
		this.fourthdownefffactor = fourthdownefffactor;
	}

	/**
	 * @return the oppfourthdownefffactor
	 */
	public Double getOppfourthdownefffactor() {
		return oppfourthdownefffactor;
	}

	/**
	 * @param oppfourthdownefffactor the oppfourthdownefffactor to set
	 */
	public void setOppfourthdownefffactor(Double oppfourthdownefffactor) {
		this.oppfourthdownefffactor = oppfourthdownefffactor;
	}

	/**
	 * @return the pointsfactor
	 */
	public Double getPointsfactor() {
		return pointsfactor;
	}

	/**
	 * @param pointsfactor the pointsfactor to set
	 */
	public void setPointsfactor(Double pointsfactor) {
		this.pointsfactor = pointsfactor;
	}

	/**
	 * @return the firstdownsfactor
	 */
	public Double getFirstdownsfactor() {
		return firstdownsfactor;
	}

	/**
	 * @param firstdownsfactor the firstdownsfactor to set
	 */
	public void setFirstdownsfactor(Double firstdownsfactor) {
		this.firstdownsfactor = firstdownsfactor;
	}

	/**
	 * @return the rushingyardsfactor
	 */
	public Double getRushingyardsfactor() {
		return rushingyardsfactor;
	}

	/**
	 * @param rushingyardsfactor the rushingyardsfactor to set
	 */
	public void setRushingyardsfactor(Double rushingyardsfactor) {
		this.rushingyardsfactor = rushingyardsfactor;
	}

	/**
	 * @return the totalyardsfactor
	 */
	public Double getTotalyardsfactor() {
		return totalyardsfactor;
	}

	/**
	 * @param totalyardsfactor the totalyardsfactor to set
	 */
	public void setTotalyardsfactor(Double totalyardsfactor) {
		this.totalyardsfactor = totalyardsfactor;
	}

	/**
	 * @return the rushingattemptsfactor
	 */
	public Double getRushingattemptsfactor() {
		return rushingattemptsfactor;
	}

	/**
	 * @param rushingattemptsfactor the rushingattemptsfactor to set
	 */
	public void setRushingattemptsfactor(Double rushingattemptsfactor) {
		this.rushingattemptsfactor = rushingattemptsfactor;
	}

	/**
	 * @return the yardsperrushfactor
	 */
	public Double getYardsperrushfactor() {
		return yardsperrushfactor;
	}

	/**
	 * @param yardsperrushfactor the yardsperrushfactor to set
	 */
	public void setYardsperrushfactor(Double yardsperrushfactor) {
		this.yardsperrushfactor = yardsperrushfactor;
	}

	/**
	 * @return the passingyardsfactor
	 */
	public Double getPassingyardsfactor() {
		return passingyardsfactor;
	}

	/**
	 * @param passingyardsfactor the passingyardsfactor to set
	 */
	public void setPassingyardsfactor(Double passingyardsfactor) {
		this.passingyardsfactor = passingyardsfactor;
	}

	/**
	 * @return the passingcompletionsfactor
	 */
	public Double getPassingcompletionsfactor() {
		return passingcompletionsfactor;
	}

	/**
	 * @param passingcompletionsfactor the passingcompletionsfactor to set
	 */
	public void setPassingcompletionsfactor(Double passingcompletionsfactor) {
		this.passingcompletionsfactor = passingcompletionsfactor;
	}

	/**
	 * @return the passingattemptsfactor
	 */
	public Double getPassingattemptsfactor() {
		return passingattemptsfactor;
	}

	/**
	 * @param passingattemptsfactor the passingattemptsfactor to set
	 */
	public void setPassingattemptsfactor(Double passingattemptsfactor) {
		this.passingattemptsfactor = passingattemptsfactor;
	}

	/**
	 * @return the yardsperpassfactor
	 */
	public Double getYardsperpassfactor() {
		return yardsperpassfactor;
	}

	/**
	 * @param yardsperpassfactor the yardsperpassfactor to set
	 */
	public void setYardsperpassfactor(Double yardsperpassfactor) {
		this.yardsperpassfactor = yardsperpassfactor;
	}

	/**
	 * @return the turnoversfactor
	 */
	public Double getTurnoversfactor() {
		return turnoversfactor;
	}

	/**
	 * @param turnoversfactor the turnoversfactor to set
	 */
	public void setTurnoversfactor(Double turnoversfactor) {
		this.turnoversfactor = turnoversfactor;
	}

	/**
	 * @return the possessionfactor
	 */
	public Double getPossessionfactor() {
		return possessionfactor;
	}

	/**
	 * @param possessionfactor the possessionfactor to set
	 */
	public void setPossessionfactor(Double possessionfactor) {
		this.possessionfactor = possessionfactor;
	}

	/**
	 * @return the homefieldfactor
	 */
	public Double getHomefieldfactor() {
		return homefieldfactor;
	}

	/**
	 * @param homefieldfactor the homefieldfactor to set
	 */
	public void setHomefieldfactor(Double homefieldfactor) {
		this.homefieldfactor = homefieldfactor;
	}

	/**
	 * @return the thirddownefflist
	 */
	public List<Double> getThirddownefflist() {
		return thirddownefflist;
	}

	/**
	 * @return the oppthirddownefflist
	 */
	public List<Double> getOppthirddownefflist() {
		return oppthirddownefflist;
	}

	/**
	 * @return the fourthdownefflist
	 */
	public List<Double> getFourthdownefflist() {
		return fourthdownefflist;
	}

	/**
	 * @return the oppfourthdownefflist
	 */
	public List<Double> getOppfourthdownefflist() {
		return oppfourthdownefflist;
	}

	/**
	 * @return the pointslist
	 */
	public List<Double> getPointslist() {
		return pointslist;
	}

	/**
	 * @return the firstdownslist
	 */
	public List<Double> getFirstdownslist() {
		return firstdownslist;
	}

	/**
	 * @return the totalyardslist
	 */
	public List<Double> getTotalyardslist() {
		return totalyardslist;
	}

	/**
	 * @return the rushingyardslist
	 */
	public List<Double> getRushingyardslist() {
		return rushingyardslist;
	}

	/**
	 * @return the rushingattemptslist
	 */
	public List<Double> getRushingattemptslist() {
		return rushingattemptslist;
	}

	/**
	 * @return the yardsperrushlist
	 */
	public List<Double> getYardsperrushlist() {
		return yardsperrushlist;
	}

	/**
	 * @return the passingyardslist
	 */
	public List<Double> getPassingyardslist() {
		return passingyardslist;
	}

	/**
	 * @return the passingcompletionslist
	 */
	public List<Double> getPassingcompletionslist() {
		return passingcompletionslist;
	}

	/**
	 * @return the passingattemptslist
	 */
	public List<Double> getPassingattemptslist() {
		return passingattemptslist;
	}

	/**
	 * @return the yardsperpasslist
	 */
	public List<Double> getYardsperpasslist() {
		return yardsperpasslist;
	}

	/**
	 * @return the turnoverslist
	 */
	public List<Double> getTurnoverslist() {
		return turnoverslist;
	}

	/**
	 * @return the possessionlist
	 */
	public List<Double> getPossessionlist() {
		return possessionlist;
	}

	/**
	 * 
	 * @param year
	 * @param startweek
	 * @param endweek
	 */
	private void setupMasseyRatings(Integer year, Integer startweek, Integer endweek) {
		if (!this.USE_WEEK_RANKINGS) {
			int endweekformassey = 17;

			if (year.intValue() == 2018) {
				endweekformassey = MASSEY_END_WEEK;
			}

			for (int x = startweek.intValue(); x <= endweek.intValue(); x++) {
				final List<MasseyRatingsNcaafData> mrnd = mrs.getNcaafGameData(year, x, x, endweekformassey, false);
				MasseyLatestRatings.put(x, mrnd);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoOSpreads#setupMasseyRatings(java.lang.Integer, java.util.Date, java.util.Date)
	 */
	@Override
	protected void setupMasseyRatings(Integer year, Date startDate, Date endDate) {
		if (!this.USE_WEEK_RANKINGS) {
			int endweekformassey = 17;

			if (year.intValue() == 2019) {
				endweekformassey = 11;
			}

			final Map<String, Integer> weeks = WeekByDate.DetermineWeeks(year, startDate, endDate);
			for (int x = weeks.get("start"); x <= weeks.get("end"); x++) {
				LOGGER.debug("x: " + x);
				final List<MasseyRatingsNcaafData> mrnd = mrs.getNcaafGameData(year, x, x, endweekformassey, false);
				LOGGER.debug("mrnd: " + mrnd);
				MasseyLatestRatings.put(x, mrnd);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoOSpreads#getVegasInsiderGames(java.lang.Integer, java.util.Date, java.util.Date)
	 */
	@Override
	protected List<VegasInsiderGame> getVegasInsiderGames(Integer year, Date startDate, Date endDate) {
		final Map<String, Integer> weeks = WeekByDate.DetermineWeeks(year, startDate, endDate);
		final List<VegasInsiderGame> vigs = vips.getNcaafGameData(year, weeks.get("start"), weeks.get("end"), false);

		return vigs;
	}

	@Override
	protected Double runAlgorithm(Integer seasonyear, 
			Date startdate, 
			Date thedate, 
			String awayteam, 
			String hometeam,
			Date gamedate) throws BatchException, SQLException {
		LOGGER.info("Entering runAlgorithm()");
		Double spread = null;
		if (SHOW_INFO) {
			LOGGER.error("awayteam: " + awayteam);
			LOGGER.error("hometeam: " + hometeam);
		}

		final List<EspnCollegeFootballGameData> awaygames = new ArrayList<EspnCollegeFootballGameData>();
		final List<EspnCollegeFootballGameData> homegames = new ArrayList<EspnCollegeFootballGameData>();

		final Map<String, Integer> weeks = WeekByDate.FootballDetermineWeeks(seasonyear, startdate, thedate);
		for (int x = weeks.get("start"); x <= weeks.get("end"); x++) {
			LOGGER.debug("x: " + x);
			List<EspnCollegeFootballGameData> games = DATAMINERDB.getEspnNcaafGameDataByWeekByTeam(awayteam, x, seasonyear);
	
			if (SHOW_INFO) {
				LOGGER.error("awayteam games.size(): " + games.size());
			}

			if (games == null || games.size() == 0) {
				games = DATAMINERDB.getEspnNcaafGameDataByYearByTeam(awayteam, seasonyear.intValue() - 1);

				for (EspnCollegeFootballGameData game : games) {
					if (game.getAwayteam().equals(awayteam)) {
						awaygames.add(game);
					} else if (game.getHometeam().equals(awayteam)) {
						awaygames.add(game);
					}
				}
			} else {
				for (EspnCollegeFootballGameData game : games) {
					final Date espnDate = game.getGamedate();
					if (espnDate.before(gamedate)) {
						if (game.getAwayteam().equals(awayteam)) {
							awaygames.add(game);
						} else if (game.getHometeam().equals(awayteam)) {
							awaygames.add(game);
						}
					}
				}
				
				if (awaygames.size() == 0) {
					games = DATAMINERDB.getEspnNcaafGameDataByYearByTeam(awayteam, seasonyear.intValue() - 1);

					for (EspnCollegeFootballGameData game : games) {
						if (game.getAwayteam().equals(awayteam)) {
							awaygames.add(game);
						} else if (game.getHometeam().equals(awayteam)) {
							awaygames.add(game);
						}
					}
				}
			}

			games = DATAMINERDB.getEspnNcaafGameDataByWeekByTeam(hometeam, x, seasonyear);
			
			if (SHOW_INFO) {
				LOGGER.error("awayteam games.size(): " + games.size());
			}

			if (games == null || games.size() == 0) {
				games = DATAMINERDB.getEspnNcaafGameDataByYearByTeam(hometeam, seasonyear.intValue() - 1);

				for (EspnCollegeFootballGameData game : games) {
					if (game.getAwayteam().equals(hometeam)) {
						homegames.add(game);
					} else if (game.getHometeam().equals(hometeam)) {
						homegames.add(game);
					}
				}
			} else {
				for (EspnCollegeFootballGameData game : games) {
					final Date espnDate = game.getGamedate();
					if (espnDate.before(gamedate)) {
						if (game.getAwayteam().equals(hometeam)) {
							homegames.add(game);
						} else if (game.getHometeam().equals(hometeam)) {
							homegames.add(game);
						}
					}
				}
				
				if (homegames.size() == 0) {
					games = DATAMINERDB.getEspnNcaafGameDataByYearByTeam(awayteam, seasonyear.intValue() - 1);

					for (EspnCollegeFootballGameData game : games) {
						if (game.getAwayteam().equals(hometeam)) {
							homegames.add(game);
						} else if (game.getHometeam().equals(hometeam)) {
							homegames.add(game);
						}
					}
				}
			}
		}

		LOGGER.error(awayteam + " awaygames.size(): " + awaygames.size());
		LOGGER.error(hometeam + " homegames.size(): " + homegames.size());

		// Compare all of the games
		for (EspnCollegeFootballGameData homegame : homegames) {
			for (EspnCollegeFootballGameData awaygame : awaygames) {
				if (SHOW_INFO) {
					LOGGER.error("awaygame: " + awaygame);
					LOGGER.error("homegame: " + homegame);
				}

				final Map<String, Integer> ratings = getTeamRatingsForTeams(awayteam, hometeam, awaygame, homegame);
				final Integer awaymassey = ratings.get("away");
				final Integer homemassey = ratings.get("home");

				final Map<String, Integer> lastGameRatings = lastGameRatings(true, awaygame, homegame);
				final Integer awaygameawaymasseyrating = lastGameRatings.get("awayaway");
				final Integer awaygamehomemasseyrating = lastGameRatings.get("awayhome");
				final Integer homegameawaymasseyrating = lastGameRatings.get("homeaway");
				final Integer homegamehomemasseyrating = lastGameRatings.get("homehome");

				Float awayfactor = Float.valueOf("1");
				Float aoppfactor = Float.valueOf("1");
				Float homefactor = Float.valueOf("1");
				Float hoppfactor = Float.valueOf("1");

				if (awaymassey != null && homemassey != null) {
					if (SHOW_INFO) {
						LOGGER.error(awayteam + " awaymassey: " + awaymassey);
						LOGGER.error(hometeam + " homemassey: " + homemassey);
						LOGGER.error(awayteam + " awaygameawaymasseyrating: " + awaygameawaymasseyrating);
						LOGGER.error(awayteam + " awaygamehomemasseyrating: " + awaygamehomemasseyrating);
						LOGGER.error(hometeam + " homegameawaymasseyrating: " + homegameawaymasseyrating);
						LOGGER.error(hometeam + " homegamehomemasseyrating: " + homegamehomemasseyrating);
					}

					// UC SANTA BARBARA awaymassey: 167.42 
					// NORTH DAKOTA STATE homemassey: 201.15 
					// UC SANTA BARBARA awaygameawaymasseyrating: 167.42 
					// UC SANTA BARBARA awaygamehomemasseyrating: 143.69 
					// NORTH DAKOTA STATE homegameawaymasseyrating: 201.15 
					// NORTH DAKOTA STATE homegamehomemasseyrating: 95.9 
					// UC SANTA BARBARA awayfactor: 0.5257224 
					// UC SANTA BARBARA aoppfactor: 1.4070538 
					// NORTH DAKOTA STATE homefactor: 0.43017 
					// NORTH DAKOTA STATE hoppfactor: 1.2716714 
					// 
					// 47.79
					
					final Double commonFactor = (Double)(1.0 / 254.0);
					Float awayopp = null;
					Float homeopp = null;

					if (awaymassey.floatValue() == 0) {
						awayopp = new Float(254);
						aoppfactor = 1 + (Math.abs(awayopp) * commonFactor.floatValue());						
					} else if (awaymassey.floatValue() == awaygameawaymasseyrating.floatValue()) {
						awayopp = awaygamehomemasseyrating.floatValue();
						aoppfactor = 1 + (Math.abs(awaygamehomemasseyrating) * commonFactor.floatValue());	
					} else {
						awayopp = awaygameawaymasseyrating.floatValue();
						aoppfactor = 1 + (Math.abs(awaygameawaymasseyrating) * commonFactor.floatValue());
					}

					if (homemassey.floatValue() == 0) {
						homeopp = new Float(254);
						hoppfactor = 1 + (Math.abs(homeopp) * commonFactor.floatValue());
					} else if (homemassey.floatValue() == homegameawaymasseyrating.floatValue()) {
						homeopp = homegamehomemasseyrating.floatValue();
						hoppfactor = 1 + (Math.abs(homegamehomemasseyrating) * commonFactor.floatValue());	
					} else {
						homeopp = homegameawaymasseyrating.floatValue();
						hoppfactor = 1 + (Math.abs(homegameawaymasseyrating) * commonFactor.floatValue());
					}

					if (homeopp.floatValue() < awayopp.floatValue()) {
						float delta = awayopp.floatValue() - homeopp.floatValue();
						awayopp = awaygamehomemasseyrating + delta;
						aoppfactor = 1 + (Math.abs(awaygamehomemasseyrating + delta) * commonFactor.floatValue());
						awayfactor = 1 - ((float)((awaymassey) * commonFactor));
						homefactor = 1 - ((float)((homemassey - delta) * commonFactor));
					} else {
						float delta = homeopp.floatValue() - awayopp.floatValue();
						homeopp = homegameawaymasseyrating + delta;
						hoppfactor = 1 + (Math.abs(homegameawaymasseyrating + delta) * commonFactor.floatValue());
						homefactor = 1 - ((float)((homemassey) * commonFactor));
						awayfactor = 1 - ((float)((awaymassey - delta) * commonFactor));
					}

					awayfactor = 1 - ((float)(awaymassey * commonFactor));
					aoppfactor = 1 + ((float)(awayopp * commonFactor));
					homefactor = 1 - ((float)(homemassey * commonFactor));
					hoppfactor = 1 + ((float)(homeopp * commonFactor));


/*					awayfactor = new Float(1.0);
					aoppfactor = new Float(1.0);
					homefactor = new Float(1.0);
					hoppfactor = new Float(1.0);
*/
					if (SHOW_INFO) {
						LOGGER.error(awayteam + " awayfactor: " + awayfactor);
						LOGGER.error(awayteam + " aoppfactor: " + aoppfactor);
						LOGGER.error(hometeam + " homefactor: " + homefactor);	
						LOGGER.error(hometeam + " hoppfactor: " + hoppfactor);
					}

					// Process the game data
					processGameData(awayteam,
							hometeam,
							awaygame,
							homegame,
							null,
							null,
							awayfactor, 
							aoppfactor, 
							homefactor, 
							hoppfactor);
				}
			}
		}

		// Determine the spread
		spread = determineSpread(awayteam, hometeam);

		LOGGER.info("Exiting runAlgorithm()");
		return spread;
	}

	/*
	 * 
	 */
	@Override
	protected Double runGameAlgorithmLastX(Integer X, Integer seasonyear, Date startdate, Date thedate, String awayteam,
			String hometeam, Date gamedate) throws BatchException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Double runGameAlgorithmLastXHomeAway(Integer X, Integer seasonyear, Date startdate, Date thedate,
			String awayteam, String hometeam, Date gamedate) throws BatchException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, Integer> getMasseyRatings(VegasInsiderGame vigGame) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param isfbs
	 * @param masseyRating
	 * @return
	 */
/*
	private Float modifyMasseyRating(Boolean isfbs, Float masseyRating) {
		if (!isfbs) {
			if (masseyRating < 16) {
				if (masseyRating < 1.50) {
					masseyRating = new Float(40);
				} else if (masseyRating < 3.80) {
					masseyRating = new Float(60);
				} else if (masseyRating < 4.50) {
					masseyRating = new Float(68);
				} else if (masseyRating < 11) {
					masseyRating = new Float(78);
				} else if (masseyRating < 12) {
					masseyRating = new Float(82);
				} else if (masseyRating < 13) {
					masseyRating = new Float(86);
				} else if (masseyRating < 13.10) {
					masseyRating = new Float(90);
				} else if (masseyRating < 13.50) {
					masseyRating = new Float(102);
				} else if (masseyRating < 14.20) {
					masseyRating = new Float(105);
				} else if (masseyRating < 15.50) {
					masseyRating = new Float(110);
				}
			} else {
				masseyRating = masseyRating.floatValue() + 130;
			}
		}

		return masseyRating;
	}
*/
}