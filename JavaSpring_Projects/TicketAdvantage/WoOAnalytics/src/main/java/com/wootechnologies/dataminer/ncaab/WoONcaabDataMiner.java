/**
 * 
 */
package com.wootechnologies.dataminer.ncaab;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.wooanalytics.dao.sites.masseyratings.MasseyRatingsNcaabData;
import com.wooanalytics.dao.sites.masseyratings.MasseyRatingsSite;
import com.wootechnologies.dataminer.WoODataMiner;
import com.wootechnologies.dataminer.db.ncaab.NcaabDataMinerDB;
import com.wootechnologies.dataminer.model.XandYObject;
import com.wootechnologies.services.dao.sites.espn.EspnCollegeBasketballGameData;
import com.wootechnologies.services.dao.sites.espn.EspnSite;
import com.wootechnologies.services.dao.sites.vegasinsider.VegasInsiderGame;
import com.wootechnologies.services.dao.sites.vegasinsider.VegasInsiderProcessSite;
import com.wootechnologies.services.site.util.WeekByDate;

/**
 * @author jmiller
 *
 */
public class WoONcaabDataMiner extends WoODataMiner {
	private static final Logger LOGGER = Logger.getLogger(WoONcaabDataMiner.class);
	private static final MasseyRatingsSite mrs = new MasseyRatingsSite();
	private static final VegasInsiderProcessSite vips = new VegasInsiderProcessSite();
	private static NcaabDataMinerDB DATAMINERDB = new NcaabDataMinerDB();
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
	private static final Calendar seasonstart2016 = Calendar.getInstance();
	private static final Calendar seasonstart2017 = Calendar.getInstance();
	private static final Calendar seasonstart2018 = Calendar.getInstance();
	
	static {
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
	 * @param args
	 */
	public static void main(String[] args) {
		final WoONcaabDataMiner woODataMiner = new WoONcaabDataMiner();
		final Integer seasonyear = 2018;

		try {
			woODataMiner.getNcaabGameData(seasonyear, 1, 1, 2019, 1, 21, 2019);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

//		woODataMiner.getNcaabGameData(seasonyear, 11, 10, 2018, 11, 11, 2018);
//		woODataMiner.getNcaabGameData(seasonyear, 2, 1, 2018, 3, 11, 2018);

	    
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
	 * @param startmonth
	 * @param startday
	 * @param startyear
	 * @param endmonth
	 * @param endday
	 * @param endyear
	 */
	public void getNcaabGameData(Integer seasonyear, Integer startmonth, Integer startday, Integer startyear, Integer endmonth, Integer endday, Integer endyear) {
		LOGGER.info("Entering getNcaabGameData()");

		final EspnSite espnSite = new EspnSite("http://www.espn.com", null, null);
		final Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.MONTH, startmonth - 1);
		startDate.set(Calendar.DAY_OF_MONTH, startday);
		startDate.set(Calendar.YEAR, startyear);

		final Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.MONTH, endmonth - 1);
		endDate.set(Calendar.DAY_OF_MONTH, endday);
		endDate.set(Calendar.YEAR, endyear);

		// Open the connection
		DATAMINERDB.start();
		final Calendar vcal1 = Calendar.getInstance();
		vcal1.setTime(endDate.getTime());
		vcal1.add(Calendar.DATE, 1);

		final List<MasseyRatingsNcaabData> masseyRatings = mrs.getNcaabGameData(seasonyear, startDate.getTime(), endDate.getTime());
		final List<VegasInsiderGame> vgames = vips.getNcaabGameData(seasonyear, startDate.getTime(), vcal1.getTime());
		final List<EspnCollegeBasketballGameData> ncaabGames = espnSite.getNcaabTeamData(startDate.getTime(), endDate.getTime());
		LOGGER.error("ncaabGames.size(): " + ncaabGames.size());

		for (EspnCollegeBasketballGameData ncaabGame : ncaabGames) {
			LOGGER.debug("ncaabGame: " + ncaabGame);
			ncaabGame.setSeasonyear(seasonyear);
			final Calendar ecal = Calendar.getInstance();
			ecal.setTime(ncaabGame.getGamedate());

			try {
				boolean vfound = false;
				for (VegasInsiderGame vgame : vgames) {
					final Calendar vcal = Calendar.getInstance();
					final Calendar vcalm = Calendar.getInstance();
					Date vdate = vgame.getDate();

					if (vdate != null) {
						vcal.setTime(vgame.getDate());
						vcalm.setTime(vgame.getDate());
						vcalm.add(Calendar.DATE, -1);

						LOGGER.debug("vgame.getAwayteamdata().getTeamname(): " + vgame.getAwayteamdata().getTeamname());
						LOGGER.debug("vgame.getHometeamdata().getTeamname(): " + vgame.getHometeamdata().getTeamname());
						LOGGER.debug("ncaabGame.getAwayteam(): " + ncaabGame.getAwayteam());
						LOGGER.debug("ncaabGame.getHometeam(): " + ncaabGame.getHometeam());	 
						LOGGER.debug("vcal.get(Calendar.MONTH): " + vcal.get(Calendar.MONTH));
						LOGGER.debug("ecal.get(Calendar.MONTH): " + ecal.get(Calendar.MONTH));
						LOGGER.debug("vcal.get(Calendar.DAY_OF_MONTH): " + vcal.get(Calendar.DAY_OF_MONTH));
						LOGGER.debug("ecal.get(Calendar.DAY_OF_MONTH): " + ecal.get(Calendar.DAY_OF_MONTH));
						LOGGER.debug("vcal.get(Calendar.YEAR): " + vcal.get(Calendar.YEAR));
						LOGGER.debug("ecal.get(Calendar.YEAR): " + ecal.get(Calendar.YEAR));

						if ((vgame.getAwayteamdata().getTeamname().toUpperCase().equals(ncaabGame.getAwayteam().toUpperCase()) || 
							vgame.getHometeamdata().getTeamname().toUpperCase().equals(ncaabGame.getHometeam().toUpperCase())) && 
							((vcal.get(Calendar.MONTH) == ecal.get(Calendar.MONTH) &&
							vcal.get(Calendar.DAY_OF_MONTH) == ecal.get(Calendar.DAY_OF_MONTH) &&
							vcal.get(Calendar.YEAR) == ecal.get(Calendar.YEAR)) || 
							(vcalm.get(Calendar.MONTH) == ecal.get(Calendar.MONTH) &&
							 vcalm.get(Calendar.DAY_OF_MONTH) == ecal.get(Calendar.DAY_OF_MONTH) &&
							 vcalm.get(Calendar.YEAR) == ecal.get(Calendar.YEAR)))) {
							LOGGER.debug("vgame: " + vgame);
							ncaabGame.setLine(vgame.getLine());

							try {
								final String line = String.valueOf(vgame.getLine());
								if (line != null && line.length() > 0 && line.startsWith("-")) {
									ncaabGame.setLineindicator("-");	
								} else if (line != null && line.length() > 0 && line.startsWith("0")) {
									ncaabGame.setLineindicator("+");
								} else if (line != null && line.length() > 0 && line.startsWith("+")) {
									ncaabGame.setLineindicator("+");
								}
								ncaabGame.setLinevalue(Math.abs(vgame.getLine()));
								ncaabGame.setLinefavorite(vgame.getLinefavorite());
								vfound = true;
							} catch (Throwable t) {
								LOGGER.warn(t.getMessage(), t);
							}
						}
					}
				}

				if (!vfound) {
					for (VegasInsiderGame vgame : vgames) {
						final Calendar vcal = Calendar.getInstance();
						final Calendar vcalm = Calendar.getInstance();
						Date vdate = vgame.getDate();

						if (vdate != null) {
							vcal.setTime(vgame.getDate());
							vcalm.setTime(vgame.getDate());
							vcalm.add(Calendar.DATE, -1);

							LOGGER.debug("vgame.getAwayteamdata().getTeamname(): " + vgame.getAwayteamdata().getTeamname());
							LOGGER.debug("vgame.getHometeamdata().getTeamname(): " + vgame.getHometeamdata().getTeamname());
							LOGGER.debug("ncaabGame.getAwayteam(): " + ncaabGame.getAwayteam());
							LOGGER.debug("ncaabGame.getHometeam(): " + ncaabGame.getHometeam());	 
							LOGGER.debug("vcal.get(Calendar.MONTH): " + vcal.get(Calendar.MONTH));
							LOGGER.debug("ecal.get(Calendar.MONTH): " + ecal.get(Calendar.MONTH));
							LOGGER.debug("vcal.get(Calendar.DAY_OF_MONTH): " + vcal.get(Calendar.DAY_OF_MONTH));
							LOGGER.debug("ecal.get(Calendar.DAY_OF_MONTH): " + ecal.get(Calendar.DAY_OF_MONTH));
							LOGGER.debug("vcal.get(Calendar.YEAR): " + vcal.get(Calendar.YEAR));
							LOGGER.debug("ecal.get(Calendar.YEAR): " + ecal.get(Calendar.YEAR));

							if ((vgame.getAwayteamdata().getTeamname().toUpperCase().equals(ncaabGame.getHometeam().toUpperCase()) || 
								vgame.getHometeamdata().getTeamname().toUpperCase().equals(ncaabGame.getAwayteam().toUpperCase())) && 
									((vcal.get(Calendar.MONTH) == ecal.get(Calendar.MONTH) &&
									vcal.get(Calendar.DAY_OF_MONTH) == ecal.get(Calendar.DAY_OF_MONTH) &&
									vcal.get(Calendar.YEAR) == ecal.get(Calendar.YEAR)) || 
									(vcalm.get(Calendar.MONTH) == ecal.get(Calendar.MONTH) &&
									 vcalm.get(Calendar.DAY_OF_MONTH) == ecal.get(Calendar.DAY_OF_MONTH) &&
									 vcalm.get(Calendar.YEAR) == ecal.get(Calendar.YEAR)))) {
								LOGGER.debug("vgame: " + vgame);
								ncaabGame.setLine(vgame.getLine());

								try {
									final String line = String.valueOf(vgame.getLine());
									if (line != null && line.length() > 0 && line.startsWith("-")) {
										ncaabGame.setLineindicator("-");	
									} else if (line != null && line.length() > 0 && line.startsWith("0")) {
										ncaabGame.setLineindicator("+");
									} else if (line != null && line.length() > 0 && line.startsWith("+")) {
										ncaabGame.setLineindicator("+");
									}
									ncaabGame.setLinevalue(Math.abs(vgame.getLine()));
									ncaabGame.setLinefavorite(vgame.getLinefavorite());
									vfound = true;
								} catch (Throwable t) {
									LOGGER.warn(t.getMessage(), t);
								}
							}
						}
					}					
				}

				// Match Massey Ratings
				final Integer week = WeekByDate.DetermineWeek(seasonyear, ncaabGame.getGamedate());
				ncaabGame.setWeek(week);
				LOGGER.debug("week: " + week);

				boolean awaymasseyfound = false;
				boolean homemasseyfound = false;
				for (MasseyRatingsNcaabData mr : masseyRatings) {
					if (mr.getTeam().toUpperCase().equals(ncaabGame.getAwayteam().toUpperCase()) && 
						mr.getWeek().intValue() == week.intValue()) {
						ncaabGame.setAwaymasseyrating(mr.getMean());
						awaymasseyfound = true;
					} else if (mr.getTeam().toUpperCase().equals(ncaabGame.getHometeam().toUpperCase()) && 
						mr.getWeek().intValue() == week.intValue()) {
						ncaabGame.setHomemasseyrating(mr.getMean());
						homemasseyfound = true;
					}
				}

				if (!awaymasseyfound) {
					LOGGER.error("week: " + week);
					LOGGER.error("ncaabGame.getDate(): " + ncaabGame.getGamedate());
					LOGGER.error("awaymassey not found: " + ncaabGame.getAwayteam());
				}

				if (!homemasseyfound) {
					LOGGER.error("week: " + week);
					LOGGER.error("ncaabGame.getDate(): " + ncaabGame.getGamedate());
					LOGGER.error("homemasseyfound not found: " + ncaabGame.getHometeam());
				}

				LOGGER.debug("ncaabGame: " + ncaabGame);
				DATAMINERDB.persistEspnNcaabGameData(ncaabGame);
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}

		// Close the connection
		DATAMINERDB.complete();

		LOGGER.info("Exiting getNcaabGameData()");
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
}