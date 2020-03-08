/**
 * 
 */
package com.wootechnologies.dataminer.ncaab;

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

import com.wooanalytics.dao.sites.masseyratings.MasseyRatingsNcaabData;
import com.wooanalytics.dao.sites.masseyratings.MasseyRatingsSite;
import com.wootechnologies.dataminer.WoOSpreads;
import com.wootechnologies.dataminer.db.ncaab.NcaabDataMinerDB;
import com.wootechnologies.dataminer.model.XandYObject;
import com.wootechnologies.errorhandling.BatchException;
import com.wootechnologies.services.dao.sites.espn.EspnCollegeBasketballGameData;
import com.wootechnologies.services.dao.sites.espn.EspnGameData;
import com.wootechnologies.services.dao.sites.vegasinsider.VegasInsiderGame;
import com.wootechnologies.services.dao.sites.vegasinsider.VegasInsiderParser;
import com.wootechnologies.services.dao.sites.vegasinsider.VegasInsiderProcessSite;
import com.wootechnologies.services.site.util.WeekByDate;

/**
 * @author jmiller
 *
 */
public class WoONcaabSpreads extends WoOSpreads {
	private static final Logger LOGGER = Logger.getLogger(WoONcaabSpreads.class);
	private static final MasseyRatingsSite mrs = new MasseyRatingsSite();
	private static final VegasInsiderProcessSite vips = new VegasInsiderProcessSite();
	private static NcaabDataMinerDB DATAMINERDB = new NcaabDataMinerDB();
	private final Map<Integer, List<MasseyRatingsNcaabData>> MasseyLatestRatings = new HashMap<Integer, List<MasseyRatingsNcaabData>>();
	protected List<Double> pointsPerGamelist = new ArrayList<Double>();
	protected List<Double> twoFgMadePerGamelist = new ArrayList<Double>();
	protected List<Double> twoFgAttemptedPerGamelist = new ArrayList<Double>();
	protected List<Double> threeFgMadePerGamelist = new ArrayList<Double>();
	protected List<Double> threeFgAttemptedPerGamelist = new ArrayList<Double>();
	protected List<Double> ftMadePerGamelist = new ArrayList<Double>();
	protected List<Double> ftAttemptedPerGamelist = new ArrayList<Double>();
	protected List<Double> offensiveReboundsPerGamelist = new ArrayList<Double>();
	protected List<Double> defensiveReboundsPerGamelist = new ArrayList<Double>();
	protected List<Double> assistsPerGamelist = new ArrayList<Double>();
	protected List<Double> stealsPerGamelist = new ArrayList<Double>();
	protected List<Double> blocksPerGamelist = new ArrayList<Double>();
	protected List<Double> turnoversPerGamelist = new ArrayList<Double>();
	protected List<Double> personalFoulsPerGamelist = new ArrayList<Double>();
	protected List<Double> technicalFoulsPerGamelist = new ArrayList<Double>();
	private static final Calendar seasonstart2015 = Calendar.getInstance();
	private static final Calendar seasonstart2016 = Calendar.getInstance();
	private static final Calendar seasonstart2017 = Calendar.getInstance();
	private static final Calendar seasonstart2018 = Calendar.getInstance();
	private static int[] LASTX = {
			1, 
			2, 
			3
	};

	static {
		seasonstart2015.set(Calendar.MONTH, 10);
		seasonstart2015.set(Calendar.DAY_OF_MONTH, 12);
		seasonstart2015.set(Calendar.YEAR, 2015);

		seasonstart2016.set(Calendar.MONTH, 10);
		seasonstart2016.set(Calendar.DAY_OF_MONTH, 11);
		seasonstart2016.set(Calendar.YEAR, 2016);

		seasonstart2017.set(Calendar.MONTH, 10);
		seasonstart2017.set(Calendar.DAY_OF_MONTH, 10);
		seasonstart2017.set(Calendar.YEAR, 2017);

		seasonstart2018.set(Calendar.MONTH, 10);
		seasonstart2018.set(Calendar.DAY_OF_MONTH, 6);
		seasonstart2018.set(Calendar.YEAR, 2018);
	}

	/**
	 * 
	 */
	public WoONcaabSpreads() {
		super();
		SHOW_INFO = false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final WoONcaabSpreads woODataMiner = new WoONcaabSpreads();
		final Integer seasonyear = 2018;

	    final Calendar startcal = Calendar.getInstance();
	    startcal.set(Calendar.MONTH, 0);
	    startcal.set(Calendar.DAY_OF_MONTH, 23);
	    startcal.set(Calendar.YEAR, 2019);
	    final Calendar endcal = Calendar.getInstance();
	    endcal.set(Calendar.MONTH, 0);
	    endcal.set(Calendar.DAY_OF_MONTH, 23);
	    endcal.set(Calendar.YEAR, 2019);
	    String filename = Integer.toString(startcal.get(Calendar.MONTH) + 1) +  Integer.toString(startcal.get(Calendar.DAY_OF_MONTH)) + Integer.toString(startcal.get(Calendar.YEAR)) +
	    		Integer.toString(endcal.get(Calendar.MONTH) + 1) +  Integer.toString(endcal.get(Calendar.DAY_OF_MONTH)) + Integer.toString(endcal.get(Calendar.YEAR)) + seasonyear;

	    int lastx = 3;
	    woODataMiner.masseyweek = 12;
//	    woODataMiner.determineSpreadsAvgManual(seasonyear, startcal.getTime(), "NORTHWESTERN", "MICHIGAN");
//	    woODataMiner.determineLastXManual(seasonyear, 6, startcal.getTime(), "MARYLAND", "MICHIGAN STATE");
	    woODataMiner.determineSpreadsAverage(seasonyear, startcal.getTime(), endcal.getTime(), "/Users/jmiller/Documents/WoOTechnology/basketballpicksavg" + filename + ".csv");
//	    woODataMiner.determineSpreads(seasonyear, startcal.getTime(), endcal.getTime(), "/Users/jmiller/Documents/WoOTechnology/basketballpicks" + filename + ".csv");
//		woODataMiner.determineLastXSpreads(lastx, seasonyear, startcal.getTime(), endcal.getTime(), "/Users/jmiller/Documents/WoOTechnology/basketballpickslast" + lastx + filename + ".csv");
//		woODataMiner.determineLastXSpreadsHomeAway(lastx, seasonyear, startcal.getTime(), endcal.getTime(), "/Users/jmiller/Documents/WoOTechnology/basketballpicksawayhomelast" + lastx + filename + ".csv");

	    
/*
		Date fromDate = null;
		Date toDate = null;

		String[] types = {
			"fgmade",
			"ftattempt",
			"fgpercentage",
			"3ptfgmade",
			"3ptfgattempt",
			"3ptfgpercentage",
			"ftmade",
			"ftattempt",
			"ftpercentage",
			"totalrebounds",
			"offrebounds",
			"defrebounds",
			"assists",
			"steals",
			"blocks",
			"totalturnovers",
			"personalfouls",
			"technicalfouls"
		};

//		String[] types = {
//			"offrebounds"
//		};

		try {
			final SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			isoFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
			fromDate = isoFormat.parse("2018-02-01 00:00:00");
			toDate = isoFormat.parse("2018-02-08 00:00:00");
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		for (String type : types) {
			final List<XandYObject> xys = woODataMiner.plotXandYDelta("finalscore", type, fromDate, toDate);
			PrintWriter printWriter = null;
			try {
				final FileWriter fileWriter = new FileWriter("/Users/jmiller/Documents/WoOTechnology/" + type + ".csv");
				printWriter = new PrintWriter(fileWriter);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
	
			if (xys != null && xys.size() > 0) {
				printWriter.print("Favorite Spread, " + type + ", Line\n");
				for (XandYObject xy : xys) {
					LOGGER.error("XY: " + xy);
					printWriter.print(xy.getX() + ", " + xy.getY() + ", " + xy.getLine() + "\n");
				}
	
				printWriter.close();
			}
		}
*/

	}

	/**
	 * 
	 * @param seasonyear
	 * @param gamedate
	 * @param awayname
	 * @param homename
	 */
	public void determineSpreadsManual(Integer seasonyear, Date gamedate, String awayname, String homename) {
		LOGGER.info("Entering determineSpreadsManual()");
		LOGGER.debug("seasonyear: " + seasonyear);

		// Open the connection
		DATAMINERDB.start();
	
		try {
			if (awayname != null || homename != null) {
				clearList();

				final Double spread = runAlgorithm(seasonyear, gamedate, gamedate, awayname, homename,
						new Date());

				LOGGER.error("spread: " + spread);
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
	 * @param seasonyear
	 * @param gamedate
	 * @param awayname
	 * @param homename
	 */
	public void determineLastXManual(Integer seasonyear, Integer X, Date gamedate, String awayname, String homename) {
		LOGGER.info("Entering determineLastXManual()");
		LOGGER.debug("seasonyear: " + seasonyear);

		// Open the connection
		DATAMINERDB.start();
		setupMasseyRatings(seasonyear + 1, gamedate, gamedate);

		try {
			if (awayname != null || homename != null) {
				clearList();

				//final Double spread = runGameAlgorithmLastX(X, seasonyear, gamedate, gamedate, awayname, homename, gamedate);
				final Double spread = this.runGameAlgorithmLastXHomeAway(X, seasonyear, gamedate, gamedate, awayname, homename, gamedate);

				LOGGER.error("spread: " + spread);
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		// Close the connection
		DATAMINERDB.complete();

		LOGGER.info("Exiting determineLastXManual()");
	}

	/**
	 * 
	 * @param seasonyear
	 * @param gamedate
	 * @param awayname
	 * @param homename
	 */
	public void determineSpreadsAvgManual(Integer seasonyear, Date gamedate, String awayname, String homename) {
		LOGGER.info("Entering determineSpreadsAvgManual()");
		LOGGER.debug("seasonyear: " + seasonyear);

		// Open the connection
		DATAMINERDB.start();
		
		setupMasseyRatings(seasonyear + 1, gamedate, gamedate);
		SHOW_INFO = true;
	
		try {
			if (awayname != null || homename != null) {
				clearList();

				Double spread = 0.0;
			    USE_WEEK_RANKINGS = true;
			    USE_MIX_RANKINGS = false;
				final Double regularspread = runAlgorithm(seasonyear, gamedate, gamedate, awayname, homename, gamedate);
				clearList();
				final Double spread1 = runGameAlgorithmLastX(1, seasonyear, gamedate, gamedate, awayname, homename, gamedate);
				clearList();
				final Double spread2 = runGameAlgorithmLastX(2, seasonyear, gamedate, gamedate, awayname, homename, gamedate);
				clearList();
				final Double spread3 = runGameAlgorithmLastX(3, seasonyear, gamedate, gamedate, awayname, homename, gamedate);
				clearList();
				final Double spread4 = runGameAlgorithmLastX(4, seasonyear, gamedate, gamedate, awayname, homename, gamedate);
				clearList();
				final Double spread5 = runGameAlgorithmLastX(5, seasonyear, gamedate, gamedate, awayname, homename, gamedate);
				clearList();
				final Double spread6 = runGameAlgorithmLastX(6, seasonyear, gamedate, gamedate, awayname, homename, gamedate);
				clearList();
				final Double spreadha1 = runGameAlgorithmLastXHomeAway(1, seasonyear, gamedate, gamedate, awayname, homename, gamedate);
				clearList();
				final Double spreadha2 = runGameAlgorithmLastXHomeAway(2, seasonyear, gamedate, gamedate, awayname, homename, gamedate);
				clearList();
				final Double spreadha3 = runGameAlgorithmLastXHomeAway(3, seasonyear, gamedate, gamedate, awayname, homename, gamedate);
				clearList();
				final Double spreadha4 = runGameAlgorithmLastXHomeAway(4, seasonyear, gamedate, gamedate, awayname, homename, gamedate);
				clearList();
				final Double spreadha5 = runGameAlgorithmLastXHomeAway(5, seasonyear, gamedate, gamedate, awayname, homename, gamedate);
				clearList();
				final Double spreadha6 = runGameAlgorithmLastXHomeAway(6, seasonyear, gamedate, gamedate, awayname, homename, gamedate);
				clearList();
				
				spread = (regularspread + spread1 + spread2 + spread3 + spread4 + spreadha1 + spreadha2 + spreadha3 + spreadha4) / 9;
				if (SHOW_INFO) {
					LOGGER.error("spread: " + spread);
				}

				LOGGER.error("spread: " + spread);
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		// Close the connection
		DATAMINERDB.complete();

		LOGGER.info("Exiting determineSpreadsAvgManual()");
	}

	/**
	 * 
	 * @param seasonyear
	 * @param start
	 * @param end
	 * @param filename
	 */
	public void determineSpreads(Integer seasonyear, Date start, Date end, String filename) {
		LOGGER.info("Entering determineSpreads()");
		LOGGER.debug("seasonyear: " + seasonyear);
		LOGGER.debug("start: " + start);
		LOGGER.debug("end: " + end);

		// Setup Massey Ratings
		setupMasseyRatings(seasonyear, start, end);
		final PrintWriter printWriter = startAlgorithm(DATAMINERDB, filename);
		final List<VegasInsiderGame> vigs = vips.getNcaabGameData(seasonyear, start, end);

		for (VegasInsiderGame ncaabGame : vigs) {
			if (ncaabGame.getLine() != null && ncaabGame.getLinefavorite() != null && ncaabGame.getLinefavorite().length() > 0) {
				try {
					if (SHOW_INFO) {
						LOGGER.error("ncaabGame: " + ncaabGame);
					}
					final String awayname = ncaabGame.getAwayteamdata().getTeamname().toUpperCase(); 
					final String homename = ncaabGame.getHometeamdata().getTeamname().toUpperCase();
					final boolean awayfound = foundTeam(awayname);
					final boolean homefound = foundTeam(homename);
					masseyweek = ncaabGame.getWeek();

					if ((awayname != null || homename != null) && (awayfound || homefound)) {
						clearList();

						final Double spread = runAlgorithm(seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						if (SHOW_INFO) {
							LOGGER.error("spread: " + spread);
						}

						endTransaction(printWriter, spread, new Double(9.5), new Double(30), ncaabGame);
					} else {
						LOGGER.error("Could not find away: " + awayname + " home: " + homename); 
					}
				} catch (Throwable t) {
					LOGGER.debug("Cannot get spread for " + ncaabGame.getAwayteamdata().getTeamname() + " " + ncaabGame.getHometeamdata().getTeamname() + " " + ncaabGame.getAwayteamdata().getFinalscore() + " " + ncaabGame.getHometeamdata().getFinalscore()
					+ " Line: " + ncaabGame.getLinefavorite() + " " + ncaabGame.getLine() + " week: " + ncaabGame.getWeek());
					LOGGER.error(t.getMessage(), t);
				}
			}
		}

		// Close the connection
		closeConnections(DATAMINERDB, printWriter);

		LOGGER.info("Exiting determineSpreads()");
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
		final List<VegasInsiderGame> vigs = vips.getNcaabGameData(seasonyear, start, end);
		final Map<String, Integer> weeks = WeekByDate.DetermineWeeks(seasonyear, start, end);
		for (int x = weeks.get("start"); x <= weeks.get("end"); x++) {
			LOGGER.debug("x: " + x);
			final List<MasseyRatingsNcaabData> mrnd = mrs.getNcaabGameData(seasonyear, x, x, 11);
			LOGGER.debug("mrnd: " + mrnd);
			MasseyLatestRatings.put(x, mrnd);
		}

		for (VegasInsiderGame ncaabGame : vigs) {
			if (ncaabGame.getLine() != null && ncaabGame.getLinefavorite() != null && ncaabGame.getLinefavorite().length() > 0) {
				try {
					if (SHOW_INFO) {
						LOGGER.error("ncaabGame: " + ncaabGame);
					}
					final String awayname = ncaabGame.getAwayteamdata().getTeamname().toUpperCase(); 
					final String homename = ncaabGame.getHometeamdata().getTeamname().toUpperCase();
					final boolean awayfound = foundTeam(awayname);
					final boolean homefound = foundTeam(homename);
					masseyweek = ncaabGame.getWeek();

					if ((awayname != null || homename != null) && (awayfound || homefound)) {
						clearList();

						Double spread = 0.0;
						Double spreadweek = 0.0;
						Double spreadmix = 0.0;
						Double spreadnoweek = 0.0;
					    USE_WEEK_RANKINGS = true;
					    USE_MIX_RANKINGS = false;
						Double regularspread = runAlgorithm(seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						Double spread1 = runGameAlgorithmLastX(1, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						Double spread2 = runGameAlgorithmLastX(2, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						Double spread3 = runGameAlgorithmLastX(3, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						Double spread4 = runGameAlgorithmLastX(4, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						Double spread5 = runGameAlgorithmLastX(5, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						Double spread6 = runGameAlgorithmLastX(6, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						Double spreadha1 = runGameAlgorithmLastXHomeAway(1, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						Double spreadha2 = runGameAlgorithmLastXHomeAway(2, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						Double spreadha3 = runGameAlgorithmLastXHomeAway(3, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						Double spreadha4 = runGameAlgorithmLastXHomeAway(4, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						Double spreadha5 = runGameAlgorithmLastXHomeAway(5, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						Double spreadha6 = runGameAlgorithmLastXHomeAway(6, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						
						spreadweek = (regularspread + spread1 + spread2 + spread3 + spread4 + spread5 + spread6 + spreadha1 + spreadha2 + spreadha3 + spreadha4 + spreadha5 + spreadha6) / 13;
						if (SHOW_INFO) {
							LOGGER.error("spreadweek: " + spread);
						}

					    USE_WEEK_RANKINGS = false;
					    USE_MIX_RANKINGS = true;
						regularspread = runAlgorithm(seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spread1 = runGameAlgorithmLastX(1, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spread2 = runGameAlgorithmLastX(2, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spread3 = runGameAlgorithmLastX(3, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spread4 = runGameAlgorithmLastX(4, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spread5 = runGameAlgorithmLastX(5, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spread6 = runGameAlgorithmLastX(6, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spreadha1 = runGameAlgorithmLastXHomeAway(1, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spreadha2 = runGameAlgorithmLastXHomeAway(2, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spreadha3 = runGameAlgorithmLastXHomeAway(3, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spreadha4 = runGameAlgorithmLastXHomeAway(4, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spreadha5 = runGameAlgorithmLastXHomeAway(5, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spreadha6 = runGameAlgorithmLastXHomeAway(6, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						
						spreadmix = (regularspread + spread1 + spread2 + spread3 + spread4 + spread5 + spread6 + spreadha1 + spreadha2 + spreadha3 + spreadha4 + spreadha5 + spreadha6) / 13;
						if (SHOW_INFO) {
							LOGGER.error("spreadmix: " + spreadmix);
						}
						
					    USE_WEEK_RANKINGS = false;
					    USE_MIX_RANKINGS = false;
						regularspread = runAlgorithm(seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spread1 = runGameAlgorithmLastX(1, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spread2 = runGameAlgorithmLastX(2, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spread3 = runGameAlgorithmLastX(3, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spread4 = runGameAlgorithmLastX(4, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spread5 = runGameAlgorithmLastX(5, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spread6 = runGameAlgorithmLastX(6, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spreadha1 = runGameAlgorithmLastXHomeAway(1, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spreadha2 = runGameAlgorithmLastXHomeAway(2, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spreadha3 = runGameAlgorithmLastXHomeAway(3, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spreadha4 = runGameAlgorithmLastXHomeAway(4, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spreadha5 = runGameAlgorithmLastXHomeAway(5, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						spreadha6 = runGameAlgorithmLastXHomeAway(6, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						clearList();
						
						spreadnoweek = (regularspread + spread1 + spread2 + spread3 + spread4 + spread5 + spread6 + spreadha1 + spreadha2 + spreadha3 + spreadha4 + spreadha5 + spreadha6) / 13;
						if (SHOW_INFO) {
							LOGGER.error("spreadnoweek: " + spreadnoweek);
						}

						spread = (spreadweek + spreadmix + spreadnoweek) / 3;
						if (SHOW_INFO) {
							LOGGER.error("spread: " + spread);
						}

						Integer awaymassey = null;
						Integer homemassey = null;

						final List<MasseyRatingsNcaabData> mrnd = MasseyLatestRatings.get(this.masseyweek.intValue() - 1);
						for (MasseyRatingsNcaabData mr : mrnd) {
							if (mr.getTeam().toUpperCase().equals(ncaabGame.getAwayteamdata().getTeamname().toUpperCase())) {
								awaymassey = mr.getMean().intValue();
							}
							if (mr.getTeam().toUpperCase().equals(ncaabGame.getHometeamdata().getTeamname().toUpperCase())) {
								homemassey = mr.getMean().intValue();
							}
						}

						// Setup the end part
						endAvgTransaction(printWriter, 
								ncaabGame.getAwayteamdata().getRotationid(),
								ncaabGame.getHometeamdata().getRotationid(),
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
								ncaabGame);
					} else {
						LOGGER.error("Could not find away: " + awayname + " home: " + homename); 
					}
				} catch (Throwable t) {
					LOGGER.debug("Cannot get spread for " + ncaabGame.getAwayteamdata().getTeamname() + " " + ncaabGame.getHometeamdata().getTeamname() + " " + ncaabGame.getAwayteamdata().getFinalscore() + " " + ncaabGame.getHometeamdata().getFinalscore()
					+ " Line: " + ncaabGame.getLinefavorite() + " " + ncaabGame.getLine() + " week: " + ncaabGame.getWeek());
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
	 * @param year
	 * @param start
	 * @param end
	 * @param filename
	 */
	public void determineLastXSpreads(Integer X, Integer seasonyear, Date start, Date end, String filename) {
		LOGGER.info("Entering determineLastTwoSpreads()");
		LOGGER.debug("seasonyear: " + seasonyear);
		LOGGER.debug("start: " + start);
		LOGGER.debug("end: " + end);

		// Setup Massey Ratings
		setupMasseyRatings(seasonyear, start, end);
		final PrintWriter printWriter = startAlgorithm(DATAMINERDB, filename);
		final List<VegasInsiderGame> vigs = vips.getNcaabGameData(seasonyear, start, end);

		for (VegasInsiderGame ncaabGame : vigs) {
			if (ncaabGame.getLine() != null && ncaabGame.getLinefavorite() != null && ncaabGame.getLinefavorite().length() > 0) {
				try {
					if (SHOW_INFO) {
						LOGGER.error("ncaabGame: " + ncaabGame);
					}
					final String awayname = ncaabGame.getAwayteamdata().getTeamname().toUpperCase(); 
					final String homename = ncaabGame.getHometeamdata().getTeamname().toUpperCase();
					final boolean awayfound = foundTeam(awayname);
					final boolean homefound = foundTeam(homename);
					masseyweek = ncaabGame.getWeek();

					if ((awayname != null || homename != null) && (awayfound || homefound)) {
						clearList();

						final Double spread = runGameAlgorithmLastX(X, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						if (SHOW_INFO) {
							LOGGER.error("spread: " + spread);
						}

						endTransaction(printWriter, spread, new Double(9.5), new Double(30), ncaabGame);
					} else {
						LOGGER.error("Could not find away: " + awayname + " home: " + homename); 
					}
				} catch (Throwable t) {
					LOGGER.debug("Cannot get spread for " + ncaabGame.getAwayteamdata().getTeamname() + " " + ncaabGame.getHometeamdata().getTeamname() + " " + ncaabGame.getAwayteamdata().getFinalscore() + " " + ncaabGame.getHometeamdata().getFinalscore()
					+ " Line: " + ncaabGame.getLinefavorite() + " " + ncaabGame.getLine() + " week: " + ncaabGame.getWeek());
					LOGGER.error(t.getMessage(), t);
				}
			}
		}

		// Close the connection
		closeConnections(DATAMINERDB, printWriter);

		LOGGER.info("Exiting determineLastTwoSpreads()");
	}

	/**
	 * 
	 * @param X
	 * @param seasonyear
	 * @param start
	 * @param end
	 * @param filename
	 */
	public void determineLastXSpreadsHomeAway(Integer X, Integer seasonyear, Date start, Date end, String filename) {
		LOGGER.info("Entering determineLastTwoSpreadsHomeAway()");
		LOGGER.debug("seasonyear: " + seasonyear);
		LOGGER.debug("start: " + start);
		LOGGER.debug("end: " + end);

		// Setup Massey Ratings
		setupMasseyRatings(seasonyear, start, end);
		final PrintWriter printWriter = startAlgorithm(DATAMINERDB, filename);
		final List<VegasInsiderGame> vigs = vips.getNcaabGameData(seasonyear, start, end);

		for (VegasInsiderGame ncaabGame : vigs) {
			if (ncaabGame.getLine() != null && ncaabGame.getLinefavorite() != null && ncaabGame.getLinefavorite().length() > 0) {
				try {
					if (SHOW_INFO) {
						LOGGER.error("ncaabGame: " + ncaabGame);
					}
					final String awayname = ncaabGame.getAwayteamdata().getTeamname().toUpperCase(); 
					final String homename = ncaabGame.getHometeamdata().getTeamname().toUpperCase();
					final boolean awayfound = foundTeam(awayname);
					final boolean homefound = foundTeam(homename);
					masseyweek = ncaabGame.getWeek();

					if ((awayname != null || homename != null) && (awayfound || homefound)) {
						clearList();

						final Double spread = runGameAlgorithmLastXHomeAway(X, seasonyear, start, end, ncaabGame.getAwayteamdata().getTeamname(), ncaabGame.getHometeamdata().getTeamname(), ncaabGame.getDate());
						if (SHOW_INFO) {
							LOGGER.error("spread: " + spread);
						}

						endTransaction(printWriter, spread, new Double(9.5), new Double(30), ncaabGame);
					} else {
						LOGGER.error("Could not find away: " + awayname + " home: " + homename); 
					}
				} catch (Throwable t) {
					LOGGER.debug("Cannot get spread for " + ncaabGame.getAwayteamdata().getTeamname() + " " + ncaabGame.getHometeamdata().getTeamname() + " " + ncaabGame.getAwayteamdata().getFinalscore() + " " + ncaabGame.getHometeamdata().getFinalscore()
					+ " Line: " + ncaabGame.getLinefavorite() + " " + ncaabGame.getLine() + " week: " + ncaabGame.getWeek());
					LOGGER.error(t.getMessage(), t);
				}
			}
		}

		// Close the connection
		closeConnections(DATAMINERDB, printWriter);

		LOGGER.info("Exiting determineLastTwoSpreadsHomeAway()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoOSpreads#runAlgorithm(java.lang.Integer, java.util.Date, java.util.Date, java.lang.String, java.lang.String, java.util.Date)
	 */
	@Override
	protected Double runAlgorithm(Integer seasonyear, Date startdate, Date thedate, String awayteam, String hometeam, Date gamedate) throws BatchException, SQLException {
		LOGGER.info("Entering runAlgorithm()");
		Double spread = null;
		if (SHOW_INFO) {
			LOGGER.error("awayteam: " + awayteam);
			LOGGER.error("hometeam: " + hometeam);
		}

		final List<EspnCollegeBasketballGameData> awaygames = new ArrayList<EspnCollegeBasketballGameData>();
		final List<EspnCollegeBasketballGameData> homegames = new ArrayList<EspnCollegeBasketballGameData>();

		Date seasonstart = null;
		if (seasonyear.intValue() == 2016) {
			seasonstart = seasonstart2016.getTime();
		} else if (seasonyear.intValue() == 2017) {
			seasonstart = seasonstart2017.getTime();
		} else {
			seasonstart = seasonstart2018.getTime();
		}

		List<EspnCollegeBasketballGameData> games = DATAMINERDB.getEspnNcaabGameDataByDateTeam(seasonyear, awayteam, seasonstart, thedate);
		if (SHOW_INFO) {
			LOGGER.error("awayteam games.size(): " + games.size());
		}

		if (games == null || games.size() == 0) {
			games = DATAMINERDB.getEspnNcaabGameDataBySeasonYear(seasonyear.intValue() - 1, awayteam);

			for (EspnCollegeBasketballGameData game : games) {
				if (game.getAwayteam().equals(awayteam)) {
					awaygames.add(game);
				} else if (game.getHometeam().equals(awayteam)) {
					awaygames.add(game);
				}
			}
		} else {
			for (EspnCollegeBasketballGameData game : games) {
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
				games = DATAMINERDB.getEspnNcaabGameDataBySeasonYear(seasonyear.intValue() - 1, awayteam);

				for (EspnCollegeBasketballGameData game : games) {
					if (game.getAwayteam().equals(awayteam)) {
						awaygames.add(game);
					} else if (game.getHometeam().equals(awayteam)) {
						awaygames.add(game);
					}
				}
			}
		}

		games = DATAMINERDB.getEspnNcaabGameDataByDateTeam(seasonyear, hometeam, seasonstart, thedate);
		if (SHOW_INFO) {
			LOGGER.error("Hometeam games.size(): " + games.size());
		}

		if (games == null || games.size() == 0) {
			games = DATAMINERDB.getEspnNcaabGameDataBySeasonYear(seasonyear.intValue() - 1, hometeam);

			for (EspnCollegeBasketballGameData game : games) {
				if (game.getAwayteam().equals(hometeam)) {
					homegames.add(game);
				} else if (game.getHometeam().equals(hometeam)) {
					homegames.add(game);
				}
			}
		} else {	
			for (EspnCollegeBasketballGameData game : games) {
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
				games = DATAMINERDB.getEspnNcaabGameDataBySeasonYear(seasonyear.intValue() - 1, hometeam);

				for (EspnCollegeBasketballGameData game : games) {
					if (game.getAwayteam().equals(hometeam)) {
						homegames.add(game);
					} else if (game.getHometeam().equals(hometeam)) {
						homegames.add(game);
					}
				}
			}
		}

		LOGGER.error(awayteam + " awaygames.size(): " + awaygames.size());
		LOGGER.error(hometeam + " homegames.size(): " + homegames.size());

		// Compare all of the games
		for (EspnCollegeBasketballGameData homegame : homegames) {
			for (EspnCollegeBasketballGameData awaygame : awaygames) {
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
					
					final Double commonFactor = (Double)(1.0 / 353.0);
					Float awayopp = null;
					Float homeopp = null;

					if (awaymassey.floatValue() == 0) {
						awayopp = new Float(354);
						aoppfactor = 1 + (Math.abs(awayopp) * commonFactor.floatValue());						
					} else if (awaymassey.floatValue() == awaygameawaymasseyrating.floatValue()) {
						awayopp = awaygamehomemasseyrating.floatValue();
						aoppfactor = 1 + (Math.abs(awaygamehomemasseyrating) * commonFactor.floatValue());	
					} else {
						awayopp = awaygameawaymasseyrating.floatValue();
						aoppfactor = 1 + (Math.abs(awaygameawaymasseyrating) * commonFactor.floatValue());
					}

					if (homemassey.floatValue() == 0) {
						homeopp = new Float(354);
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
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoOSpreads#runGameAlgorithmLastX(java.lang.Integer, java.lang.Integer, java.util.Date, java.util.Date, java.lang.String, java.lang.String, java.util.Date)
	 */
	@Override
	protected Double runGameAlgorithmLastX(Integer X, Integer seasonyear, Date startdate, Date thedate, String awayteam, String hometeam, Date gamedate) throws BatchException, SQLException {
		LOGGER.info("Entering runGameAlgorithmLastX()");
		LOGGER.error("startdate: " + startdate);
		LOGGER.error("thedate: " + thedate);

		if (SHOW_INFO) {
			LOGGER.error("awayteam: " + awayteam);
			LOGGER.error("hometeam: " + hometeam);
		}
		Double spread = null;
		final List<EspnCollegeBasketballGameData> awaygames = new ArrayList<EspnCollegeBasketballGameData>();
		final List<EspnCollegeBasketballGameData> homegames = new ArrayList<EspnCollegeBasketballGameData>();
		
		Date seasonstart = null;
		if (seasonyear.intValue() == 2016) {
			seasonstart = seasonstart2016.getTime();
		} else if (seasonyear.intValue() == 2017) {
			seasonstart = seasonstart2017.getTime();
		} else {
			seasonstart = seasonstart2018.getTime();
		}

		List<EspnCollegeBasketballGameData> games = DATAMINERDB.getEspnNcaabGameDataByDateTeam(seasonyear, awayteam, seasonstart, thedate);
		LOGGER.error("Awayteam games.size(): " + games.size());
		if (SHOW_INFO) {
			LOGGER.error("Awayteam games.size(): " + games.size());
		}

		if (games == null || games.size() == 0) {
			games = DATAMINERDB.getEspnNcaabGameDataBySeasonYear(seasonyear.intValue() - 1, awayteam);

			for (EspnCollegeBasketballGameData game : games) {
				if (game.getAwayteam().equals(awayteam)) {
					awaygames.add(game);
				} else if (game.getHometeam().equals(awayteam)) {
					awaygames.add(game);
				}
			}
		} else {
			for (EspnCollegeBasketballGameData game : games) {
				final Date espnDate = game.getGamedate();
				if (espnDate.before(gamedate)) {
					if (game.getAwayteam().equals(awayteam)) {
						if (awaygames.size() < X.intValue()) {
							awaygames.add(game);
						}
					} else if (game.getHometeam().equals(awayteam)) {
						if (awaygames.size() < X.intValue()) {
							awaygames.add(game);
						}
					}
				}
			}
			
			if (awaygames.size() == 0) {
				games = DATAMINERDB.getEspnNcaabGameDataBySeasonYear(seasonyear.intValue() - 1, awayteam);

				for (EspnCollegeBasketballGameData game : games) {
					if (game.getAwayteam().equals(awayteam)) {
						awaygames.add(game);
					} else if (game.getHometeam().equals(awayteam)) {
						awaygames.add(game);
					}
				}
			}
		}

		games = DATAMINERDB.getEspnNcaabGameDataByDateTeam(seasonyear, hometeam, seasonstart, thedate);
		LOGGER.error("Hometeam games.size(): " + games.size());
		if (SHOW_INFO) {
			LOGGER.error("Hometeam games.size(): " + games.size());
		}

		if (games == null || games.size() == 0) {
			games = DATAMINERDB.getEspnNcaabGameDataBySeasonYear(seasonyear.intValue() - 1, hometeam);

			for (EspnCollegeBasketballGameData game : games) {
				if (game.getAwayteam().equals(hometeam)) {
					homegames.add(game);
				} else if (game.getHometeam().equals(hometeam)) {
					homegames.add(game);
				}
			}
		} else {	
			for (EspnCollegeBasketballGameData game : games) {
				final Date espnDate = game.getGamedate();
				if (espnDate.before(gamedate)) {
					if (game.getAwayteam().equals(hometeam)) {
						if (homegames.size() < X.intValue()) {
							homegames.add(game);
						}
					} else if (game.getHometeam().equals(hometeam)) {
						if (homegames.size() < X.intValue()) {
							homegames.add(game);
						}
					}
				}
			}

			if (homegames.size() == 0) {
				games = DATAMINERDB.getEspnNcaabGameDataBySeasonYear(seasonyear.intValue() - 1, hometeam);

				for (EspnCollegeBasketballGameData game : games) {
					if (game.getAwayteam().equals(hometeam)) {
						homegames.add(game);
					} else if (game.getHometeam().equals(hometeam)) {
						homegames.add(game);
					}
				}
			}
		}

		LOGGER.error(awayteam + " awaygames.size(): " + awaygames.size());
		LOGGER.error(hometeam + " homegames.size(): " + homegames.size());

		// Compare all of the games
		for (EspnCollegeBasketballGameData homegame : homegames) {
			for (EspnCollegeBasketballGameData awaygame : awaygames) {
				if (SHOW_INFO) {
					LOGGER.error("awaygame: " + awaygame);
					LOGGER.error("homegame: " + homegame);
				}

				final Map<String, Integer> ratings = getTeamRatingsForTeams(awayteam, hometeam, awaygame, homegame);
				final Integer awaymassey = ratings.get("away");
				final Integer homemassey = ratings.get("home");

				if (SHOW_INFO) {
					LOGGER.error(awayteam + " awaymassey: " + awaymassey);
					LOGGER.error(hometeam + " homemassey: " + homemassey);
				}

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

					final Double commonFactor = (Double)(1.0 / 353.0);
					Float awayopp = null;
					Float homeopp = null;

					if (awaymassey.floatValue() == 0) {
						awayopp = new Float(354);
						aoppfactor = 1 + (Math.abs(awayopp) * commonFactor.floatValue());						
					} else if (awaymassey.floatValue() == awaygameawaymasseyrating.floatValue()) {
						awayopp = awaygamehomemasseyrating.floatValue();
						aoppfactor = 1 + (Math.abs(awaygamehomemasseyrating) * commonFactor.floatValue());	
					} else {
						awayopp = awaygameawaymasseyrating.floatValue();
						aoppfactor = 1 + (Math.abs(awaygameawaymasseyrating) * commonFactor.floatValue());
					}

					if (awaymassey.floatValue() == 0) {
						homeopp = new Float(354);
						hoppfactor = 1 + (Math.abs(homeopp) * commonFactor.floatValue());
					} else if (homemassey.floatValue() == homegameawaymasseyrating.floatValue()) {
						homeopp = homegamehomemasseyrating.floatValue();
						hoppfactor = 1 + (Math.abs(homegamehomemasseyrating) * commonFactor.floatValue());	
					} else {
						homeopp = homegameawaymasseyrating.floatValue();
						hoppfactor = 1 + (Math.abs(homegameawaymasseyrating) * commonFactor.floatValue());
					}

					awayfactor = 1 - ((float)(awaymassey * commonFactor));
					aoppfactor = 1 + ((float)(awayopp * commonFactor));
					homefactor = 1 - ((float)(homemassey * commonFactor));
					hoppfactor = 1 + ((float)(homeopp * commonFactor));

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

		LOGGER.info("Exiting runGameAlgorithmLastX()");
		return spread;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoOSpreads#runGameAlgorithmLastXHomeAway(java.lang.Integer, java.lang.Integer, java.util.Date, java.util.Date, java.lang.String, java.lang.String, java.util.Date)
	 */
	@Override
	protected Double runGameAlgorithmLastXHomeAway(Integer X, Integer seasonyear, Date startdate, Date thedate, String awayteam, String hometeam, Date gamedate) throws BatchException, SQLException {
		LOGGER.info("Entering runGameAlgorithmLastXHomeAway()");
		Double spread = null;

		if (SHOW_INFO) {
			LOGGER.error("awayteam: " + awayteam);
			LOGGER.error("hometeam: " + hometeam);
		}

		final List<EspnCollegeBasketballGameData> awaygames = new ArrayList<EspnCollegeBasketballGameData>();
		final List<EspnCollegeBasketballGameData> homegames = new ArrayList<EspnCollegeBasketballGameData>();

		Date seasonstart = null;
		if (seasonyear.intValue() == 2016) {
			seasonstart = seasonstart2016.getTime();
		} else if (seasonyear.intValue() == 2017) {
			seasonstart = seasonstart2017.getTime();
		} else {
			seasonstart = seasonstart2018.getTime();
		}

		List<EspnCollegeBasketballGameData> games = DATAMINERDB.getEspnNcaabGameDataByDateTeam(seasonyear, awayteam, seasonstart, thedate);
		if (SHOW_INFO) {
			LOGGER.error("Awayteam games.size(): " + games.size());
		}

		if (games == null || games.size() == 0) {
			games = DATAMINERDB.getEspnNcaabGameDataBySeasonYear(seasonyear.intValue() - 1, awayteam);

			for (EspnCollegeBasketballGameData game : games) {
				if (game.getAwayteam().equals(awayteam)) {
					awaygames.add(game);
				}
			}
		} else {
			for (EspnCollegeBasketballGameData game : games) {
				final Date espnDate = game.getGamedate();
				if (espnDate.before(gamedate)) {
					if (game.getAwayteam().equals(awayteam)) {
						if (awaygames.size() < X.intValue()) {
							awaygames.add(game);
						}
					}
				}
			}
			
			if (awaygames.size() == 0) {
				games = DATAMINERDB.getEspnNcaabGameDataBySeasonYear(seasonyear.intValue() - 1, awayteam);

				for (EspnCollegeBasketballGameData game : games) {
					if (game.getAwayteam().equals(awayteam)) {
						awaygames.add(game);
					}
				}
			}
		}

		games = DATAMINERDB.getEspnNcaabGameDataByDateTeam(seasonyear, hometeam, seasonstart, thedate);
		if (SHOW_INFO) {
			LOGGER.error("Hometeam games.size(): " + games.size());
		}

		if (games == null || games.size() == 0) {
			games = DATAMINERDB.getEspnNcaabGameDataBySeasonYear(seasonyear.intValue() - 1, hometeam);

			for (EspnCollegeBasketballGameData game : games) {
				if (game.getHometeam().equals(hometeam)) {
					homegames.add(game);
				}
			}
		} else {	
			for (EspnCollegeBasketballGameData game : games) {
				final Date espnDate = game.getGamedate();
				if (espnDate.before(gamedate)) {
					if (game.getHometeam().equals(hometeam)) {
						if (homegames.size() < X.intValue()) {
							homegames.add(game);
						}
					}
				}
			}

			if (homegames.size() == 0) {
				games = DATAMINERDB.getEspnNcaabGameDataBySeasonYear(seasonyear.intValue() - 1, hometeam);

				for (EspnCollegeBasketballGameData game : games) {
					if (game.getHometeam().equals(hometeam)) {
						homegames.add(game);
					}
				}
			}
		}

		// Compare all of the games
		for (EspnCollegeBasketballGameData homegame : homegames) {
			for (EspnCollegeBasketballGameData awaygame : awaygames) {
				if (SHOW_INFO) {
					LOGGER.error("awaygame: " + awaygame);
					LOGGER.error("homegame: " + homegame);
				}

				final Map<String, Integer> ratings = getTeamRatingsForTeams(awayteam, hometeam, awaygame, homegame);
				final Integer awaymassey = ratings.get("away");
				final Integer homemassey = ratings.get("home");
				if (SHOW_INFO) {
					LOGGER.error(awayteam + " awaymassey: " + awaymassey);
					LOGGER.error(hometeam + " homemassey: " + homemassey);
				}

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

					final Double commonFactor = (Double)(1.0 / 353.0);
					Float awayopp = null;
					Float homeopp = null;

					if (awaymassey.floatValue() == 0) {
						awayopp = new Float(354);
						aoppfactor = 1 + (Math.abs(awayopp) * commonFactor.floatValue());						
					} else if (awaymassey.floatValue() == awaygameawaymasseyrating.floatValue()) {
						awayopp = awaygamehomemasseyrating.floatValue();
						aoppfactor = 1 + (Math.abs(awaygamehomemasseyrating) * commonFactor.floatValue());	
					} else {
						awayopp = awaygameawaymasseyrating.floatValue();
						aoppfactor = 1 + (Math.abs(awaygameawaymasseyrating) * commonFactor.floatValue());
					}

					if (awaymassey.floatValue() == 0) {
						homeopp = new Float(354);
						hoppfactor = 1 + (Math.abs(homeopp) * commonFactor.floatValue());
					} else if (homemassey.floatValue() == homegameawaymasseyrating.floatValue()) {
						homeopp = homegamehomemasseyrating.floatValue();
						hoppfactor = 1 + (Math.abs(homegamehomemasseyrating) * commonFactor.floatValue());	
					} else {
						homeopp = homegameawaymasseyrating.floatValue();
						hoppfactor = 1 + (Math.abs(homegameawaymasseyrating) * commonFactor.floatValue());
					}

					awayfactor = 1 - ((float)(awaymassey * commonFactor));
					aoppfactor = 1 + ((float)(awayopp * commonFactor));
					homefactor = 1 - ((float)(homemassey * commonFactor));
					hoppfactor = 1 + ((float)(homeopp * commonFactor));

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

		LOGGER.info("Exiting runGameAlgorithmLastXHomeAway()");
		return spread;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoODataMiner#processGameData(java.lang.String, java.lang.String, com.ticketadvantage.services.dao.sites.espn.EspnGameData, com.ticketadvantage.services.dao.sites.espn.EspnGameData, java.lang.Float, java.lang.Float, java.lang.Float, java.lang.Float)
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
		final EspnCollegeBasketballGameData agame = (EspnCollegeBasketballGameData)awaygame;
		final EspnCollegeBasketballGameData hgame = (EspnCollegeBasketballGameData)homegame;

		// pointsPerGame
		Float appp = null;
		Float aoppp = null;
		Float hppp = null;
		Float hoppp = null;

		// fgMadePerGame
		Float afgmpg = null;
		Float aofgmpg = null;
		Float hfgmpg = null;
		Float hofgmpg = null;

		// fgAttemptedPerGame
		Float afgapg = null;
		Float aofgapg = null;
		Float hfgapg = null;
		Float hofgapg = null;

		// fgPct
		Float afgp = null;
		Float aofgp = null;
		Float hfgp = null;
		Float hofgp = null;

		// twoFgMadePerGame
		Float a2fgmpg = null;
		Float ao2fgmpg = null;
		Float h2fgmpg = null;
		Float ho2fgmpg = null;

		// twoFgAttemptedPerGame
		Float a2fgapg = null;
		Float ao2fgapg = null;
		Float h2fgapg = null;
		Float ho2fgapg = null;

		// twoPointPct
		Float a2pp = null;
		Float ao2pp = null;
		Float h2pp = null;
		Float ho2pp = null;

		// threeFgMadePerGame
		Float a3fgmpg = null;
		Float ao3fgmpg = null;
		Float h3fgmpg = null;
		Float ho3fgmpg = null;

		// threeFgAttemptedPerGame
		Float a3fgapg = null;
		Float ao3fgapg = null;
		Float h3fgapg = null;
		Float ho3fgapg = null;

		// threePointPct
		Float a3pp = null;
		Float ao3pp = null;
		Float h3pp = null;
		Float ho3pp = null;

		// ftMadePerGame
		Float aftmpg = null;
		Float aoftmpg = null;
		Float hftmpg = null;
		Float hoftmpg = null;

		// ftAttemptedPerGame
		Float aftapg = null;
		Float aoftapg = null;
		Float hftapg = null;
		Float hoftapg = null;

		// freeThrowPct
		Float aftp = null;
		Float aoftp = null;
		Float hftp = null;
		Float hoftp = null;

		// totalReboundsPerGame
		Float atrpg = null;
		Float aotrpg = null;
		Float htrpg = null;
		Float hotrpg = null;

		// offensiveReboundsPerGame
		Float aorpg = null;
		Float aoorpg = null;
		Float horpg = null;
		Float hoorpg = null;

		// defensiveReboundsPerGame
		Float adrpg = null;
		Float aodrpg = null;
		Float hdrpg = null;
		Float hodrpg = null;

		// assistsPerGame
		Float aapg = null;
		Float aoapg = null;
		Float hapg = null;
		Float hoapg = null;

		// stealsPerGame
		Float aspg = null;
		Float aospg = null;
		Float hspg = null;
		Float hospg = null;

		// blocksPerGame
		Float abpg = null;
		Float aobpg = null;
		Float hbpg = null;
		Float hobpg = null;

		// turnoversPerGame
		Float atpg = null;
		Float aotpg = null;
		Float htpg = null;
		Float hotpg = null;

		// personalFoulsPerGame
		Float apfpg = null;
		Float aopfpg = null;
		Float hpfpg = null;
		Float hopfpg = null;

		// technicalFoulsPerGame
		Float atfpg = null;
		Float aotfpg = null;
		Float htfpg = null;
		Float hotfpg = null;

		// effectiveFGPct
		Float aefp = null;
		Float aoefp = null;
		Float hefp = null;
		Float hoefp = null;

		// possessionsPerGame
		Float appg = null;
		Float aoppg = null;
		Float hppg = null;
		Float hoppg = null;

		// offensiveEfficiency
		Float aoe = null;
		Float aooe = null;
		Float hoe = null;
		Float hooe = null;


/*
		Float extraChancesPerGame = setupStat(extraChancesPerGamelist);
		Float scheduleStrengthByOther = setupStat(scheduleStrengthByOtherlist);
*/

		// Is this the away team?
		if (agame.getAwayteam().equals(awayteam)) {
			// pointsPerGame
			appp = agame.getAwayfinalscore().floatValue();
			aoppp = agame.getHomefinalscore().floatValue();

			// fgMadePerGame
			afgmpg = agame.getAwayfgmade().floatValue();
			aofgmpg = agame.getHomefgmade().floatValue();
			// fgAttemptedPerGame
			afgapg = agame.getAwayfgattempt().floatValue();
			aofgapg = agame.getHomefgattempt().floatValue();
			// fgPct
			afgp = agame.getAwayfgpercentage().floatValue();
			aofgp = agame.getHomefgpercentage().floatValue();

			// twoFgMadePerGame
			a2fgmpg = agame.getAwayfgmade().floatValue() - agame.getAway3ptfgmade().floatValue();
			ao2fgmpg = agame.getHomefgmade().floatValue() - agame.getHome3ptfgmade().floatValue();
			// twoFgAttemptedPerGame
			a2fgapg = agame.getAwayfgattempt().floatValue() - agame.getAway3ptfgattempt().floatValue();
			ao2fgapg = agame.getHomefgattempt().floatValue() - agame.getHome3ptfgattempt().floatValue();
			// twoPointPct
			a2pp = divideNumbers(a2fgmpg, a2fgapg) * 100;
			ao2pp = divideNumbers(ao2fgmpg, ao2fgapg) * 100;

			// threeFgMadePerGame
			a3fgmpg = agame.getAway3ptfgmade().floatValue();
			ao3fgmpg = agame.getHome3ptfgmade().floatValue();
			// threeFgAttemptedPerGame
			a3fgapg = agame.getAway3ptfgattempt().floatValue();
			ao3fgapg = agame.getHome3ptfgattempt().floatValue();
			// threePointPct
			a3pp = agame.getAway3ptfgpercentage().floatValue();
			ao3pp = agame.getHome3ptfgpercentage().floatValue();

			// ftMadePerGame
			aftmpg = agame.getAwayftmade().floatValue();
			aoftmpg = agame.getHomeftmade().floatValue();
			// ftAttemptedPerGame
			aftapg = agame.getAwayftattempt().floatValue();
			aoftapg = agame.getHomeftattempt().floatValue();
			// freeThrowPct
			aftp = agame.getAwayftpercentage().floatValue();
			aoftp = agame.getHomeftpercentage().floatValue();

			// totalReboundsPerGame
			atrpg = agame.getAwaytotalrebounds().floatValue();
			aotrpg = agame.getHometotalrebounds().floatValue();
			// offensiveReboundsPerGame
			aorpg = agame.getAwayoffrebounds().floatValue();
			aoorpg = agame.getHomeoffrebounds().floatValue();
			// defensiveReboundsPerGame
			adrpg = agame.getAwaydefrebounds().floatValue();
			aodrpg = agame.getHomedefrebounds().floatValue();

			// assistsPerGame
			aapg = agame.getAwayassists().floatValue();
			aoapg = agame.getHomeassists().floatValue();
			// stealsPerGame
			aspg = agame.getAwaysteals().floatValue();
			aospg = agame.getHomesteals().floatValue();
			// blocksPerGame
			abpg = agame.getAwayblocks().floatValue();
			aobpg = agame.getHomeblocks().floatValue();

			// turnoversPerGame
			atpg = agame.getAwaytotalturnovers().floatValue();
			aotpg = agame.getHometotalturnovers().floatValue();
			// personalFoulsPerGame
			apfpg = agame.getAwaypersonalfouls().floatValue();
			aopfpg = agame.getHomepersonalfouls().floatValue();
			// technicalFoulsPerGame
			atfpg = agame.getAwaytechnicalfouls().floatValue();
			aotfpg = agame.getHometechnicalfouls().floatValue();
		} else {
			// pointsPerGame
			appp = agame.getHomefinalscore().floatValue();
			aoppp = agame.getAwayfinalscore().floatValue();

			// fgMadePerGame
			afgmpg = agame.getHomefgmade().floatValue();
			aofgmpg = agame.getAwayfgmade().floatValue();
			// fgAttemptedPerGame
			afgapg = agame.getHomefgattempt().floatValue();
			aofgapg = agame.getAwayfgattempt().floatValue();
			// fgPct
			afgp = agame.getHomefgpercentage().floatValue();
			aofgp = agame.getAwayfgpercentage().floatValue();

			// twoFgMadePerGame
			a2fgmpg = agame.getHomefgmade().floatValue() - agame.getHome3ptfgmade().floatValue();
			ao2fgmpg = agame.getAwayfgmade().floatValue() - agame.getAway3ptfgmade().floatValue();
			// twoFgAttemptedPerGame
			a2fgapg = agame.getHomefgattempt().floatValue() - agame.getHome3ptfgattempt().floatValue();
			ao2fgapg = agame.getAwayfgattempt().floatValue() - agame.getAway3ptfgattempt().floatValue();
			// twoPointPct
			a2pp = divideNumbers(a2fgmpg, a2fgapg) * 100;
			ao2pp = divideNumbers(ao2fgmpg, ao2fgapg) * 100;

			// threeFgMadePerGame
			a3fgmpg = agame.getHome3ptfgmade().floatValue();
			ao3fgmpg = agame.getAway3ptfgmade().floatValue();
			// threeFgAttemptedPerGame
			a3fgapg = agame.getHome3ptfgattempt().floatValue();
			ao3fgapg = agame.getAway3ptfgattempt().floatValue();
			// threePointPct
			a3pp = agame.getHome3ptfgpercentage().floatValue();
			ao3pp = agame.getAway3ptfgpercentage().floatValue();

			// ftMadePerGame
			aftmpg = agame.getHomeftmade().floatValue();
			aoftmpg = agame.getAwayftmade().floatValue();
			// ftAttemptedPerGame
			aftapg = agame.getHomeftattempt().floatValue();
			aoftapg = agame.getAwayftattempt().floatValue();
			// freeThrowPct
			aftp = agame.getHomeftpercentage().floatValue();
			aoftp = agame.getAwayftpercentage().floatValue();

			// totalReboundsPerGame
			atrpg = agame.getHometotalrebounds().floatValue();
			aotrpg = agame.getAwaytotalrebounds().floatValue();
			// offensiveReboundsPerGame
			aorpg = agame.getHomeoffrebounds().floatValue();
			aoorpg = agame.getAwayoffrebounds().floatValue();
			// defensiveReboundsPerGame
			adrpg = agame.getHomedefrebounds().floatValue();
			aodrpg = agame.getAwaydefrebounds().floatValue();

			// assistsPerGame
			aapg = agame.getHomeassists().floatValue();
			aoapg = agame.getAwayassists().floatValue();
			// stealsPerGame
			aspg = agame.getHomesteals().floatValue();
			aospg = agame.getAwaysteals().floatValue();
			// blocksPerGame
			abpg = agame.getHomeblocks().floatValue();
			aobpg = agame.getAwayblocks().floatValue();

			// turnoversPerGame
			atpg = agame.getHometotalturnovers().floatValue();
			aotpg = agame.getAwaytotalturnovers().floatValue();
			// personalFoulsPerGame
			apfpg = agame.getHomepersonalfouls().floatValue();
			aopfpg = agame.getAwaypersonalfouls().floatValue();
			// technicalFoulsPerGame
			atfpg = agame.getHometechnicalfouls().floatValue();
			aotfpg = agame.getAwaytechnicalfouls().floatValue();
		}

		if (hgame.getAwayteam().equals(hometeam)) {
			// pointsPerGame
			hppp = hgame.getAwayfinalscore().floatValue();
			hoppp = hgame.getHomefinalscore().floatValue();

			// fgMadePerGame
			hfgmpg = hgame.getAwayfgmade().floatValue();
			hofgmpg = hgame.getHomefgmade().floatValue();
			// fgAttemptedPerGame
			hfgapg = hgame.getAwayfgattempt().floatValue();
			hofgapg = hgame.getHomefgattempt().floatValue();
			// fgPct
			hfgp = hgame.getAwayfgpercentage().floatValue();
			hofgp = hgame.getHomefgpercentage().floatValue();

			// twoFgMadePerGame
			h2fgmpg = hgame.getAwayfgmade().floatValue() - hgame.getAway3ptfgmade().floatValue();
			ho2fgmpg = hgame.getHomefgmade().floatValue() - hgame.getHome3ptfgmade().floatValue();
			// twoFgAttemptedPerGame
			h2fgapg = hgame.getAwayfgattempt().floatValue() - hgame.getAway3ptfgattempt().floatValue();
			ho2fgapg = hgame.getHomefgattempt().floatValue() - hgame.getHome3ptfgattempt().floatValue();
			// twoPointPct
			h2pp = divideNumbers(a2fgmpg, a2fgapg) * 100;
			ho2pp = divideNumbers(ao2fgmpg, ao2fgapg) * 100;

			// threeFgMadePerGame
			h3fgmpg = hgame.getAway3ptfgmade().floatValue();
			ho3fgmpg = hgame.getHome3ptfgmade().floatValue();
			// threeFgAttemptedPerGame
			h3fgapg = hgame.getAway3ptfgattempt().floatValue();
			ho3fgapg = hgame.getHome3ptfgattempt().floatValue();
			// threePointPct
			h3pp = hgame.getAway3ptfgpercentage().floatValue();
			ho3pp = hgame.getHome3ptfgpercentage().floatValue();

			// ftMadePerGame
			hftmpg = hgame.getAwayftmade().floatValue();
			hoftmpg = hgame.getHomeftmade().floatValue();
			// ftAttemptedPerGame
			hftapg = hgame.getAwayftattempt().floatValue();
			hoftapg = hgame.getHomeftattempt().floatValue();
			// freeThrowPct
			hftp = hgame.getAwayftpercentage().floatValue();
			hoftp = hgame.getHomeftpercentage().floatValue();

			// totalReboundsPerGame
			htrpg = hgame.getAwaytotalrebounds().floatValue();
			hotrpg = hgame.getHometotalrebounds().floatValue();
			// offensiveReboundsPerGame
			horpg = hgame.getAwayoffrebounds().floatValue();
			hoorpg = hgame.getHomeoffrebounds().floatValue();
			// defensiveReboundsPerGame
			hdrpg = hgame.getAwaydefrebounds().floatValue();
			hodrpg = hgame.getHomedefrebounds().floatValue();

			// assistsPerGame
			hapg = hgame.getAwayassists().floatValue();
			hoapg = hgame.getHomeassists().floatValue();
			// stealsPerGame
			hspg = hgame.getAwaysteals().floatValue();
			hospg = hgame.getHomesteals().floatValue();
			// blocksPerGame
			hbpg = hgame.getAwayblocks().floatValue();
			hobpg = hgame.getHomeblocks().floatValue();

			// turnoversPerGame
			htpg = hgame.getAwaytotalturnovers().floatValue();
			hotpg = hgame.getHometotalturnovers().floatValue();
			// personalFoulsPerGame
			hpfpg = hgame.getAwaypersonalfouls().floatValue();
			hopfpg = hgame.getHomepersonalfouls().floatValue();
			// technicalFoulsPerGame
			htfpg = hgame.getAwaytechnicalfouls().floatValue();
			hotfpg = hgame.getHometechnicalfouls().floatValue();
		} else {
			// pointsPerGame
			hppp = hgame.getHomefinalscore().floatValue();
			hoppp = hgame.getAwayfinalscore().floatValue();

			// fgMadePerGame
			hfgmpg = hgame.getHomefgmade().floatValue();
			hofgmpg = hgame.getAwayfgmade().floatValue();
			// fgAttemptedPerGame
			hfgapg = hgame.getHomefgattempt().floatValue();
			hofgapg = hgame.getAwayfgattempt().floatValue();
			// fgPct
			hfgp = hgame.getHomefgpercentage().floatValue();
			hofgp = hgame.getAwayfgpercentage().floatValue();

			// twoFgMadePerGame
			h2fgmpg = hgame.getHomefgmade().floatValue() - hgame.getHome3ptfgmade().floatValue();
			ho2fgmpg = hgame.getAwayfgmade().floatValue() - hgame.getAway3ptfgmade().floatValue();
			// twoFgAttemptedPerGame
			h2fgapg = hgame.getHomefgattempt().floatValue() - hgame.getHome3ptfgattempt().floatValue();
			ho2fgapg = hgame.getAwayfgattempt().floatValue() - hgame.getAway3ptfgattempt().floatValue();
			// twoPointPct
			h2pp = divideNumbers(a2fgmpg, a2fgapg) * 100;
			ho2pp = divideNumbers(ao2fgmpg, ao2fgapg) * 100;

			// threeFgMadePerGame
			h3fgmpg = hgame.getHome3ptfgmade().floatValue();
			ho3fgmpg = hgame.getAway3ptfgmade().floatValue();
			// threeFgAttemptedPerGame
			h3fgapg = hgame.getHome3ptfgattempt().floatValue();
			ho3fgapg = hgame.getAway3ptfgattempt().floatValue();
			// threePointPct
			h3pp = hgame.getHome3ptfgpercentage().floatValue();
			ho3pp = hgame.getAway3ptfgpercentage().floatValue();

			// ftMadePerGame
			hftmpg = hgame.getHomeftmade().floatValue();
			hoftmpg = hgame.getAwayftmade().floatValue();
			// ftAttemptedPerGame
			hftapg = hgame.getHomeftattempt().floatValue();
			hoftapg = hgame.getAwayftattempt().floatValue();
			// freeThrowPct
			hftp = hgame.getHomeftpercentage().floatValue();
			hoftp = hgame.getAwayftpercentage().floatValue();

			// totalReboundsPerGame
			htrpg = hgame.getHometotalrebounds().floatValue();
			hotrpg = hgame.getAwaytotalrebounds().floatValue();
			// offensiveReboundsPerGame
			horpg = hgame.getHomeoffrebounds().floatValue();
			hoorpg = hgame.getAwayoffrebounds().floatValue();
			// defensiveReboundsPerGame
			hdrpg = hgame.getHomedefrebounds().floatValue();
			hodrpg = hgame.getAwaydefrebounds().floatValue();

			// assistsPerGame
			hapg = hgame.getHomeassists().floatValue();
			hoapg = hgame.getAwayassists().floatValue();
			// stealsPerGame
			hspg = hgame.getHomesteals().floatValue();
			hospg = hgame.getAwaysteals().floatValue();
			// blocksPerGame
			hbpg = hgame.getHomeblocks().floatValue();
			hobpg = hgame.getAwayblocks().floatValue();
			// turnoversPerGame
			htpg = hgame.getHometotalturnovers().floatValue();
			hotpg = hgame.getAwaytotalturnovers().floatValue();

			// personalFoulsPerGame
			hpfpg = hgame.getHomepersonalfouls().floatValue();
			hopfpg = hgame.getAwaypersonalfouls().floatValue();
			// technicalFoulsPerGame
			htfpg = hgame.getHometechnicalfouls().floatValue();
			hotfpg = hgame.getAwaytechnicalfouls().floatValue();
		}

		final Double points = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, appp, aoppp, hppp, hoppp, 0.08);
//		final Double fgMade = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, afgmpg, aofgmpg, hfgmpg, hofgmpg, 0.06);
//		final Double fgAttempted = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, afgapg, aofgapg, hfgapg, hofgapg, 0.06);
		final Double twoFgMade = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, a2fgmpg, ao2fgmpg, h2fgmpg, ho2fgmpg, 0.08);
//		final Double twoFgMade = 0.0;
//		final Double twoFgAttempted = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, a2fgapg, ao2fgapg, h2fgapg, ho2fgapg, 0.3);
		final Double twoFgAttempted = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, a2pp, ao2pp, h2pp, ho2pp, 0.05);
//		final Double twoFgAttempted = 0.0;
		final Double threeFgMade = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, a3fgmpg, ao3fgmpg, h3fgmpg, ho3fgmpg, 0.2);
//		final Double threeFgMade = 0.0;
//		final Double threeFgAttempted = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, a3fgapg, ao3fgapg, h3fgapg, ho3fgapg, 0.5);
		final Double threeFgAttempted = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, a3pp, ao3pp, h3pp, ho3pp, 0.12);
//		final Double threeFgAttempted = 0.0;
		final Double ftFgMade = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, aftmpg, aoftmpg, hftmpg, hoftmpg, 0.08);
//		final Double ftFgMade = 0.0;
//		final Double ftFgAttempted = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, aftapg, aoftapg, hftapg, hoftapg, 0.2);
		final Double ftFgAttempted = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, aftp, aoftp, hftp, hoftp, 0.03);
//		final Double ftFgAttempted = 0.0;
		final Double offRebounds = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, aorpg, aoorpg, horpg, hoorpg, 0.125);
		final Double defRebounds = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, adrpg, aodrpg, hdrpg, hodrpg, 0.08);
		final Double assists = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, aapg, aoapg, hapg, hoapg, 0.05);
		final Double steals = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, aspg, aospg, hspg, hospg, 0.05);
		final Double blocks = calculateValueBoth(awayfactor, homefactor, aoppfactor, hoppfactor, abpg, aobpg, hbpg, hobpg, 0.05);
		final Double turnovers = calculateValueBothNegative(awayfactor, homefactor, aoppfactor, hoppfactor, atpg, aotpg, htpg, hotpg, 0.125);
		final Double personalfouls = calculateValueBothNegative(awayfactor, homefactor, aoppfactor, hoppfactor, apfpg, aopfpg, hpfpg, hopfpg, 0.01);
		final Double technicalfouls = calculateValueBothNegative(awayfactor, homefactor, aoppfactor, hoppfactor, atfpg, aotfpg, htfpg, hotfpg, 0.01);

		pointsPerGamelist.add(points);
		twoFgMadePerGamelist.add(twoFgMade);
		twoFgAttemptedPerGamelist.add(twoFgAttempted);
		threeFgMadePerGamelist.add(threeFgMade);
		threeFgAttemptedPerGamelist.add(threeFgAttempted);
		ftMadePerGamelist.add(ftFgMade);
		ftAttemptedPerGamelist.add(ftFgAttempted);
		offensiveReboundsPerGamelist.add(offRebounds);
		defensiveReboundsPerGamelist.add(defRebounds);
		assistsPerGamelist.add(assists);
		stealsPerGamelist.add(steals);
		blocksPerGamelist.add(blocks);
		turnoversPerGamelist.add(turnovers);
		personalFoulsPerGamelist.add(personalfouls);
		technicalFoulsPerGamelist.add(technicalfouls);

		if (SHOW_INFO) {
			LOGGER.error("awayteam: " + awayteam + " hometeam: " + hometeam);
			LOGGER.error("points: " + points);
			LOGGER.error("twoFgMade: " + twoFgMade);
			LOGGER.error("twoFgAttempted: " + twoFgAttempted);
			LOGGER.error("threeFgMade: " + threeFgMade);
			LOGGER.error("threeFgAttempted: " + threeFgAttempted);
			LOGGER.error("ftFgMade: " + ftFgMade);
			LOGGER.error("ftFgAttempted: " + ftFgAttempted);
			LOGGER.error("offRebounds: " + offRebounds);
			LOGGER.error("defRebounds: " + defRebounds);
			LOGGER.error("assists: " + assists);
			LOGGER.error("steals: " + steals);
			LOGGER.error("blocks: " + blocks);
			LOGGER.error("turnovers: " + turnovers);
			LOGGER.error("personalfouls: " + personalfouls);
			LOGGER.error("technicalfouls: " + technicalfouls);
			LOGGER.error("home court: " + "-3");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoOSpreads#lastGameRatings(boolean, com.wootechnologies.services.dao.sites.espn.EspnGameData, com.wootechnologies.services.dao.sites.espn.EspnGameData)
	 */
	@Override
	protected Map<String, Integer> lastGameRatings(boolean userweekratings, EspnGameData awayGame, EspnGameData homeGame) {
		final Map<String, Integer> lastGamesRatings = new HashMap<String, Integer>();
		Integer awaygameawaymasseyrating = null;
		Integer awaygamehomemasseyrating = null;
		Integer homegameawaymasseyrating = null;
		Integer homegamehomemasseyrating = null;

		if (userweekratings) {
			awaygameawaymasseyrating = awayGame.getAwaymasseyrating().intValue();
			awaygamehomemasseyrating = awayGame.getHomemasseyrating().intValue();
			homegameawaymasseyrating = homeGame.getAwaymasseyrating().intValue();
			homegamehomemasseyrating = homeGame.getHomemasseyrating().intValue();
		} else {
			final List<MasseyRatingsNcaabData> mrnd = MasseyLatestRatings.get(this.masseyweek.intValue() - 1);
			for (MasseyRatingsNcaabData mr : mrnd) {
				if (mr.getTeam().toUpperCase().equals(awayGame.getAwayteam().toUpperCase())) {
					awaygameawaymasseyrating = mr.getRank();
				}
			}
			for (MasseyRatingsNcaabData mr : mrnd) {
				if (mr.getTeam().toUpperCase().equals(awayGame.getHometeam().toUpperCase())) {
					awaygamehomemasseyrating = mr.getRank();
				}
			}
			for (MasseyRatingsNcaabData mr : mrnd) {
				if (mr.getTeam().toUpperCase().equals(homeGame.getAwayteam().toUpperCase())) {
					homegameawaymasseyrating = mr.getRank();
				}
			}
			for (MasseyRatingsNcaabData mr : mrnd) {
				if (mr.getTeam().toUpperCase().equals(homeGame.getHometeam().toUpperCase())) {
					homegamehomemasseyrating = mr.getRank();
				}
			}
		}

		if (awaygameawaymasseyrating.floatValue() == 0) {
			awaygameawaymasseyrating = new Integer(MAX_MASSEY_FACTOR);
			LOGGER.warn("awaygameawaymasseyrating is 0");
		}
		if (awaygamehomemasseyrating.floatValue() == 0) {
			awaygamehomemasseyrating = new Integer(MAX_MASSEY_FACTOR);
			LOGGER.warn("awaygamehomemasseyrating is 0");
		}
		if (homegameawaymasseyrating.floatValue() == 0) {
			homegameawaymasseyrating = new Integer(MAX_MASSEY_FACTOR);
			LOGGER.warn("homegameawaymasseyrating is 0");
		}
		if (homegamehomemasseyrating.floatValue() == 0) {
			homegamehomemasseyrating = new Integer(MAX_MASSEY_FACTOR);
			LOGGER.warn("homegamehomemasseyrating is 0");
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
	 * @see com.wootechnologies.dataminer.WoOSpreads#getTeamRatingsForTeams(java.lang.String, java.lang.String, com.wootechnologies.services.dao.sites.espn.EspnGameData, com.wootechnologies.services.dao.sites.espn.EspnGameData)
	 */
	@Override
	protected Map<String, Integer> getTeamRatingsForTeams(String awayteam, String hometeam, EspnGameData awaygame, EspnGameData homegame) {
		final Map<String, Integer> ratings = new HashMap<String, Integer>();
		Float awaymassey = null;
		Float homemassey = null;
		LOGGER.debug("masseyweek: " + this.masseyweek.intValue());

		if (awaygame.getAwayteam().toUpperCase().equals(awayteam.toUpperCase())) {
			if (USE_WEEK_RANKINGS) {
				awaymassey = awaygame.getAwaymasseyranking().floatValue();
			} else if (USE_MIX_RANKINGS) {
				final Float weekawaymassey = awaygame.getAwaymasseyranking().floatValue();
				final List<MasseyRatingsNcaabData> mrnd = MasseyLatestRatings.get(this.masseyweek.intValue() - 1);

				for (MasseyRatingsNcaabData mr : mrnd) {
					if (mr.getTeam().toUpperCase().equals(awayteam.toUpperCase())) {
						awaymassey = (mr.getMean() + weekawaymassey) / 2;
					}
				}
			} else {
				final List<MasseyRatingsNcaabData> mrnd = MasseyLatestRatings.get(this.masseyweek.intValue() - 1);

				for (MasseyRatingsNcaabData mr : mrnd) {
					if (mr.getTeam().toUpperCase().equals(awayteam.toUpperCase())) {
						awaymassey = mr.getMean();
					}
				}
			}
		} else if (awaygame.getHometeam().toUpperCase().equals(awayteam.toUpperCase())) {
			if (USE_WEEK_RANKINGS) {
				awaymassey = awaygame.getHomemasseyranking().floatValue();
			} else if (USE_MIX_RANKINGS) {
				final Float weekawaymassey = awaygame.getHomemasseyranking().floatValue();
				final List<MasseyRatingsNcaabData> mrnd = MasseyLatestRatings.get(this.masseyweek.intValue() - 1);

				for (MasseyRatingsNcaabData mr : mrnd) {
					if (mr.getTeam().toUpperCase().equals(awayteam.toUpperCase())) {
						awaymassey = (mr.getMean() + weekawaymassey) / 2;
					}
				}
			} else {
				final List<MasseyRatingsNcaabData> mrnd = MasseyLatestRatings.get(this.masseyweek.intValue() - 1);

				for (MasseyRatingsNcaabData mr : mrnd) {
					if (mr.getTeam().toUpperCase().equals(awayteam.toUpperCase())) {
						awaymassey = mr.getMean();
					}
				}
			}
		}

		if (homegame.getHometeam().toUpperCase().equals(hometeam.toUpperCase())) {
			if (USE_WEEK_RANKINGS) {
				homemassey = homegame.getHomemasseyranking().floatValue();
			} else if (USE_MIX_RANKINGS) {
				final Float weekhomemassey = homegame.getHomemasseyranking().floatValue();
				final List<MasseyRatingsNcaabData> mrnd = MasseyLatestRatings.get(this.masseyweek.intValue() - 1);

				for (MasseyRatingsNcaabData mr : mrnd) {
					if (mr.getTeam().toUpperCase().equals(hometeam.toUpperCase())) {
						homemassey = (mr.getMean() + weekhomemassey) / 2;
					}
				}
			} else {
				final List<MasseyRatingsNcaabData> mrnd = MasseyLatestRatings.get(this.masseyweek.intValue() - 1);

				for (MasseyRatingsNcaabData mr : mrnd) {
					if (mr.getTeam().toUpperCase().equals(hometeam.toUpperCase())) {
						homemassey = mr.getMean();
					}
				}
			}
		} else if (homegame.getAwayteam().toUpperCase().equals(hometeam.toUpperCase())) {
			if (USE_WEEK_RANKINGS) {
				homemassey = homegame.getAwaymasseyranking().floatValue();
			} else if (USE_MIX_RANKINGS) {
				final Float weekhomemassey = homegame.getAwaymasseyranking().floatValue();
				final List<MasseyRatingsNcaabData> mrnd = MasseyLatestRatings.get(this.masseyweek.intValue() - 1);

				for (MasseyRatingsNcaabData mr : mrnd) {
					if (mr.getTeam().toUpperCase().equals(hometeam.toUpperCase())) {
						homemassey = (mr.getMean() + weekhomemassey) / 2;
					}
				}
			} else {
				final List<MasseyRatingsNcaabData> mrnd = MasseyLatestRatings.get(this.masseyweek.intValue() - 1);

				for (MasseyRatingsNcaabData mr : mrnd) {
					if (mr.getTeam().toUpperCase().equals(hometeam.toUpperCase())) {
						homemassey = mr.getMean();
					}
				}
			}
		}

		LOGGER.debug("awayteam: " + awayteam);
		LOGGER.debug("hometeam: " + hometeam);
		LOGGER.debug("awaymassey: " + awaymassey);
		LOGGER.debug("homemassey: " + homemassey);

		try {
			// Setup the ratings
			ratings.put("away", awaymassey.intValue());
			ratings.put("home", homemassey.intValue());
		} catch (Throwable t) {
			final List<MasseyRatingsNcaabData> mrnd = MasseyLatestRatings.get(this.masseyweek.intValue() - 1);
			for (MasseyRatingsNcaabData mr : mrnd) {
				LOGGER.debug("MasseyRatingsNcaabData: " + mr);
			}
		}

		return ratings;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoODataMiner#determineSpread(java.lang.String, java.lang.String)
	 */
	@Override
	protected Double determineSpread(String awayteam, String hometeam) {
		Double spread = null;

		final Double points = setupStat(pointsPerGamelist);
		final Double twoFgMade = setupStat(twoFgMadePerGamelist);
		final Double twoFgAttempted = setupStat(twoFgAttemptedPerGamelist);
		final Double threeFgMade = setupStat(threeFgMadePerGamelist);
		final Double threeFgAttempted = setupStat(threeFgAttemptedPerGamelist);
		final Double ftFgMade = setupStat(ftMadePerGamelist);
		final Double ftFgAttempted = setupStat(ftAttemptedPerGamelist);
		final Double offRebounds = setupStat(offensiveReboundsPerGamelist);
		final Double defRebounds = setupStat(defensiveReboundsPerGamelist);
		final Double assists = setupStat(assistsPerGamelist);
		final Double steals = setupStat(stealsPerGamelist);
		final Double blocks = setupStat(blocksPerGamelist);
		final Double turnovers = setupStat(turnoversPerGamelist);
		final Double personalfouls = setupStat(personalFoulsPerGamelist);
		final Double technicalfouls = setupStat(technicalFoulsPerGamelist);

		if (SHOW_INFO) {
			LOGGER.error("awayteam: " + awayteam + " hometeam: " + hometeam);
			LOGGER.error("points: " + points);
			LOGGER.error("twoFgMade: " + twoFgMade);
			LOGGER.error("twoFgAttempted: " + twoFgAttempted);
			LOGGER.error("threeFgMade: " + threeFgMade);
			LOGGER.error("threeFgAttempted: " + threeFgAttempted);
			LOGGER.error("ftFgMade: " + ftFgMade);
			LOGGER.error("ftFgAttempted: " + ftFgAttempted);
			LOGGER.error("offRebounds: " + offRebounds);
			LOGGER.error("defRebounds: " + defRebounds);
			LOGGER.error("assists: " + assists);
			LOGGER.error("steals: " + steals);
			LOGGER.error("blocks: " + blocks);
			LOGGER.error("turnovers: " + turnovers);
			LOGGER.error("personalfouls: " + personalfouls);
			LOGGER.error("technicalfouls: " + technicalfouls);
			LOGGER.error("home court: " + "-3");
		}

		spread = points + 
			twoFgMade +
			twoFgAttempted +
			threeFgMade +
			threeFgAttempted +
			ftFgMade +
			ftFgAttempted +
			offRebounds +
			defRebounds +
			assists +
			steals +
			blocks +
			turnovers +
			personalfouls +
			technicalfouls - 3;

		return spread;
	}

	/**
	 * 
	 * @param awayfinalscore
	 * @param homefinalscore
	 * @param spread
	 * @param ncaabGame
	 * @return
	 */
	private String didDogAndLargeSpreadWin(Integer awayfinalscore, Integer homefinalscore, Double woospread, VegasInsiderGame ncaabGame) {
		String outcome = "";

		// First get the overall outcome
		String theoutcome = didWinSpread(awayfinalscore, homefinalscore, woospread, ncaabGame);

		if (ncaabGame.getHometeamdata().getTeamname().equals(ncaabGame.getLinefavorite())) {
			// Regardless of favorite or dog, if we have more than 10 pt difference mark it
			if (Math.abs(Math.abs(ncaabGame.getLine().floatValue()) - Math.abs(woospread)) > 10.5) {
				outcome = theoutcome;
			} else if (woospread >= ncaabGame.getLine().floatValue()) {
				// This means we have a dog
				outcome = theoutcome;
			}
		} else {
			// Regardless of favorite or dog, if we have more than 10 pt difference mark it
			if (Math.abs(Math.abs(ncaabGame.getLine().floatValue()) - Math.abs(woospread)) > 10.5) {
				outcome = theoutcome;
			} else if (woospread <= ncaabGame.getLine().floatValue()) {
				// This means we have a dog
				outcome = theoutcome;
			}			
		}

		return outcome;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoODataMiner#clearList()
	 */
	@Override
	protected void clearList() {
		pointsPerGamelist.clear();
		twoFgMadePerGamelist.clear();
		twoFgAttemptedPerGamelist.clear();
		threeFgMadePerGamelist.clear();
		threeFgAttemptedPerGamelist.clear();
		ftMadePerGamelist.clear();
		ftAttemptedPerGamelist.clear();
		offensiveReboundsPerGamelist.clear();
		defensiveReboundsPerGamelist.clear();
		assistsPerGamelist.clear();
		stealsPerGamelist.clear();
		blocksPerGamelist.clear();
		turnoversPerGamelist.clear();
		personalFoulsPerGamelist.clear();
		technicalFoulsPerGamelist.clear();
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoOSpreads#foundTeam(java.lang.String)
	 */
	@Override
	protected boolean foundTeam(String name) {
		boolean found = false;

		final Iterator<String> itr = VegasInsiderParser.NCAABMapping.keySet().iterator();
		while (itr.hasNext()) {
			final String key = itr.next();
			if (VegasInsiderParser.NCAABMapping.get(key).toUpperCase().equals(name)) {
				found = true;
			}
		}

		return found;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoOSpreads#getMasseyRatings(com.wootechnologies.services.dao.sites.vegasinsider.VegasInsiderGame)
	 */
	@Override
	protected Map<String, Integer> getMasseyRatings(VegasInsiderGame vigGame) {
		final Map<String, Integer> masseys = new HashMap<String, Integer>();
		Integer awaymassey = null;
		Integer homemassey = null;

		final List<MasseyRatingsNcaabData> mrnd = MasseyLatestRatings.get(this.masseyweek.intValue() - 1);
		for (MasseyRatingsNcaabData mr : mrnd) {
			if (mr.getTeam().toUpperCase().equals(vigGame.getAwayteamdata().getTeamname().toUpperCase())) {
				awaymassey = mr.getMean().intValue();
				masseys.put("awaymassey", awaymassey);
			}

			if (mr.getTeam().toUpperCase().equals(vigGame.getHometeamdata().getTeamname().toUpperCase())) {
				homemassey = mr.getMean().intValue();
				masseys.put("homemassey", homemassey);
			}
		}

		return masseys;
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

		return turnovers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoODataMiner#getHomeTurnovers(com.ticketadvantage.services.dao.sites.espn.EspnGameData)
	 */
	@Override
	protected int getHomeTurnovers(EspnGameData game) {
		int turnovers = 0;

		return turnovers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wootechnologies.dataminer.WoOSpreads#setupMasseyRatings(java.lang.Integer, java.util.Date, java.util.Date)
	 */
	@Override
	protected void setupMasseyRatings(Integer year, Date startDate, Date endDate) {
		if (!this.USE_WEEK_RANKINGS) {
			int endweekformassey = 14;

			if (year.intValue() == 2019) {
				endweekformassey = 11;
			}

			final Map<String, Integer> weeks = WeekByDate.DetermineWeeks(year, startDate, endDate);
			for (int x = weeks.get("start"); x <= weeks.get("end"); x++) {
				LOGGER.debug("x: " + x);
				final List<MasseyRatingsNcaabData> mrnd = mrs.getNcaabGameData(year, x, x, endweekformassey);
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
		final List<VegasInsiderGame> vigs = vips.getNcaabGameData(year, startDate, endDate);
		return vigs;
	}
}