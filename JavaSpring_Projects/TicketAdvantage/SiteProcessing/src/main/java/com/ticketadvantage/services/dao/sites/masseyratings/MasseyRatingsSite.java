/**
 * 
 */
package com.ticketadvantage.services.dao.sites.masseyratings;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibm.icu.util.Calendar;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.EventsPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TeamPackage;
import com.ticketadvantage.services.model.TotalRecordEvent;
import com.ticketadvantage.services.site.util.WeekByDate;

/**
 * @author jmiller
 *
 */
public class MasseyRatingsSite extends Updatable<EventPackage> {
	private final static Logger LOGGER = Logger.getLogger(MasseyRatingsSite.class);
	private final MasseyRatingsParser mrp = new MasseyRatingsParser();
	private int wins;
	private int losses;
	private int pushes;
	private final static String startdate2017 = "11-05-2017";
	private final static String startdate2018 = "11-04-2018";

	/**
	 * 
	 */
	public MasseyRatingsSite() {
		super("MasseyRatingsSite", "https://www.masseyratings.com/", null, null, false, false);
		LOGGER.info("Entering MasseyRatingsSite()");

		try {
			setTimezone("ET");
			getHttpClientWrapper().setupHttpClient("None");
			setProcessTransaction(false);
		} catch (Throwable t) {
			LOGGER.warn(t.getMessage(), t);
		}

		LOGGER.info("Exiting MasseyRatingsSite()");
	}

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 */
	public MasseyRatingsSite(String host, String username, String password) {
		super("MasseyRatingsSite", host, username, password, false, false);
		LOGGER.info("Entering MasseyRatingsSite()");

		try {
			setTimezone("ET");
			getHttpClientWrapper().setupHttpClient("None");
			setProcessTransaction(false);
		} catch (Throwable t) {
			LOGGER.warn(t.getMessage(), t);
		}

		LOGGER.info("Exiting MasseyRatingsSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final MasseyRatingsSite mrs = new MasseyRatingsSite("https://www.masseyratings.com/",
					"", 
					"");
			mrs.httpClientWrapper.setupHttpClient("None");

		    final Calendar startcal = Calendar.getInstance();
		    startcal.set(Calendar.MONTH, 10);
		    startcal.set(Calendar.DAY_OF_MONTH, 6);
		    startcal.set(Calendar.YEAR, 2018);
		    final Calendar endcal = Calendar.getInstance();
		    endcal.set(Calendar.MONTH, 10);
		    endcal.set(Calendar.DAY_OF_MONTH, 9);
		    endcal.set(Calendar.YEAR, 2018);

/*
		    final List<MasseyRatingsNcaafData> mrnd = mrs.getNcaafGameData(2014, 0, 1, 15, false);
		    for (MasseyRatingsNcaafData mr : mrnd) {
	    			LOGGER.error("MasseyRatingsNcaafData: " + mr);
		    }
*/
/*
		    List<MasseyRatingsNcaabData> mrnd = mrs.getNcaabGameData(2018, startcal.getTime(), endcal.getTime());
		    	// processSite.getNcaabGameData(2017, Date startDate, Date endDate);
		    for (MasseyRatingsNcaabData mr : mrnd) {
		    		LOGGER.error("MasseyRatingsNcaabData: " + mr);
//		    		System.out.println(mr.getTeam());
		    }
*/
/*
		    final List<MasseyRatingsNcaafData> mrnd = processSite.getNcaafGameData(2017, 1, 14, 14, false);
		    for (MasseyRatingsNcaafData mr : mrnd) {
		    		LOGGER.error("MasseyRatingsNcaafData: " + mr);
		    }
*/
/*		    LOGGER.error("Calling getCompositeRatings()");
		    Map<String, String> retValue = processSite.getCompositeRatings();

		    final Iterator<String> itr = retValue.keySet().iterator();
		    while (itr.hasNext()) {
		    		String key = itr.next();
		    		String value = retValue.get(key);
		    		LOGGER.debug("Key: " + key + " Value: " + value);
		    }
*/

		    List<TeamData> teams = new ArrayList<TeamData>();
		    String json = mrs.getSite("https://www.masseyratings.com/ratejson.php?s=298892&sub=11590");
		    LOGGER.debug("json: " + json);
		    Map<String, String> map = mrs.mrp.parseIndex(json);

		    Iterator<String> itr = map.keySet().iterator();
		    LOGGER.error("teams size: " + map.size());
		    while (itr.hasNext()) {
		    		String key = itr.next();
		    		String value = map.get(key);
		    		value = value.replaceAll("team.php", "teamjson.php");
  		    		json = mrs.getSite("https://www.masseyratings.com/" + value);
		    		final TeamData teamData = mrs.mrp.parseGameInfo(key, json);
		    		LOGGER.debug("TeamXXX: " + teamData);
		    		
		    		final List<MasseyRatingsData> resultData = teamData.getResults();
		    		LOGGER.debug("ResultData size: " + resultData.size());
		    		for (MasseyRatingsData rd : resultData) {
		    			if (rd.getUrlData() != null) {
		    				json = mrs.getSite("https://www.masseyratings.com/" + rd.getUrlData());
		    				mrs.mrp.parseGameResults(rd, json);
		    				mrs.checkForSpreadWin(rd);
		    			}
		    		}
		    		
		    		teams.add(teamData);
		    }

		    LOGGER.error("wins: " + mrs.wins);
		    LOGGER.error("losses: " + mrs.losses);
		    LOGGER.error("pushes: " + mrs.pushes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param proxy
	 */
	public void setProxy(String proxy) {
		LOGGER.info("Entering setProxy()");

		try {
			this.httpClientWrapper.setupHttpClient(proxy);
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}

		LOGGER.info("Exiting setProxy()");
	}

	/**
	 * 
	 * @param year
	 * @param startWeek
	 * @param endWeek
	 * @param currentWeek
	 * @param includebowls
	 * @return
	 */
	public List<MasseyRatingsNcaafData> getNcaafGameData(Integer year, Integer startWeek, Integer endWeek, Integer currentWeek, Boolean includebowls) {
		final List<MasseyRatingsNcaafData> ratings = new ArrayList<MasseyRatingsNcaafData>();

		try {
			for (int week = startWeek; week <= endWeek; week++) {
				LOGGER.debug("Week: " + week);
				String url = null;
				if (currentWeek == week) {
					url = "https://www.masseyratings.com/cf/compare.htm";
				} else {
					url = "https://www.masseyratings.com/cf/arch/compare" + year.toString() + "-" + week + ".htm";
				}
				String xhtml = super.getSite(url);
	
				List<MasseyRatingsNcaafData> tempratings = mrp.parseNcaafRatings(xhtml, week, year);
				for (MasseyRatingsNcaafData tr : tempratings) {
					LOGGER.debug("XXXX: " + tr);
					ratings.add(tr);
				}

				// Now get the 1AA teams
				if (currentWeek == week) {
					url = "https://www.masseyratings.com/cf/compare1aa.htm";
				} else {
					url = "https://www.masseyratings.com/cf/arch/compare1aa" + year.toString() + "-" + week + ".htm";
				}
				xhtml = super.getSite(url);
	
				tempratings = mrp.parseNcaafRatings(xhtml, week, year);
				for (MasseyRatingsNcaafData tr : tempratings) {
					LOGGER.debug("XXXX: " + tr);
					ratings.add(tr);
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return ratings;
	}

	/**
	 * 
	 * @param year
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<MasseyRatingsNcaabData> getNcaabGameData(Integer year, Date startDate, Date endDate) {
		LOGGER.debug("startDate: " + startDate);
		LOGGER.debug("endDate: " + endDate);
		final List<MasseyRatingsNcaabData> ratings = new ArrayList<MasseyRatingsNcaabData>();

		try {
			final Map<String, Integer> weeks = WeekByDate.DetermineWeeks(year, startDate, endDate);
			int sdiffs = weeks.get("start");
			int ediffs = weeks.get("end");
			LOGGER.error("start: " + sdiffs);
			LOGGER.error("end: " + ediffs);
		    String url = null;

			for (int week = sdiffs; week <= ediffs; week++) {
//				url = "https://www.masseyratings.com/cb/compare.htm";
				url = "https://www.masseyratings.com/cb/arch/compare" + (year.intValue() + 1) + "-" + week + ".htm";
				LOGGER.error("url: " + url);
				String xhtml = super.getSite(url);

				final List<MasseyRatingsNcaabData> tempratings = mrp.parseNcaabRatings(xhtml, week, year);
				for (MasseyRatingsNcaabData tr : tempratings) {
					tr.setWeek(week+1);
					ratings.add(tr);
				}
			}
		} catch (Throwable t) {
			LOGGER.debug(t.getMessage(), t);
		}

		return ratings;
	}

	/**
	 * 
	 * @param year
	 * @param startWeek
	 * @param endWeek
	 * @param currentWeek
	 * @param includebowls
	 * @return
	 */
	public List<MasseyRatingsNcaafData> getNcaafFCSGameData(Integer year, Integer startWeek, Integer endWeek, Integer currentWeek, Boolean includebowls) {
		final List<MasseyRatingsNcaafData> ratings = new ArrayList<MasseyRatingsNcaafData>();

		try {
			for (int week = startWeek; week <= endWeek; week++) {
				LOGGER.debug("Week: " + week);
				String url = null;

				// Now get the 1AA teams
				if (currentWeek == week) {
					url = "https://www.masseyratings.com/cf/compare1aa.htm";
				} else {
					url = "https://www.masseyratings.com/cf/arch/compare1aa" + year.toString() + "-" + week + ".htm";
				}
				String xhtml = super.getSite(url);
	
				List<MasseyRatingsNcaafData> tempratings = mrp.parseNcaafFCSRatings(xhtml, week, year);
				for (MasseyRatingsNcaafData tr : tempratings) {
					LOGGER.debug("MasseyRatingsNcaafData: " + tr);
					ratings.add(tr);
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return ratings;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, String> getCompositeRatings() {
		Map<String, String> retValue = null;

		try {
			final MasseyRatingsSite processSite = new MasseyRatingsSite("https://www.masseyratings.com/",
					"", 
					"");
		    processSite.httpClientWrapper.setupHttpClient("None");	
			String xhtml = processSite.getSite("https://www.masseyratings.com/cb/compare.htm");
			retValue = mrp.parseComposite(xhtml);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e );
		}

		return retValue;
	}

	/**
	 * 
	 * @param rd
	 */
	public void checkForSpreadWin(MasseyRatingsData rd) {
		if (rd.isGameComplete()) {
			int pfor = rd.getPointsfor();
			int pagainst = rd.getPointsagainst();
			int sfor = rd.getScore1();
			int sagainst = rd.getScore2();

			// -2
			int spread = pfor - pagainst;
			int sspread = sfor - sagainst;

			if (spread > sspread) {
				LOGGER.error("Spread L - " + rd.getTeam() + (rd.isAwayGame() ? " at " : " vs. ") + rd.getOpponentTeam()
						+ " " + rd.getScore1() + "-" + rd.getScore2() + " projected " + rd.getPointsfor() + "-"
						+ rd.getPointsagainst());
				losses++;
			} else if (spread < sspread) {
				LOGGER.error("Spread W - " + rd.getTeam() + (rd.isAwayGame() ? " at " : " vs. ") + rd.getOpponentTeam()
						+ " " + rd.getScore1() + "-" + rd.getScore2() + " projected " + rd.getPointsfor() + "-"
						+ rd.getPointsagainst());
				wins++;
			} else {
				LOGGER.error("Spread P - " + rd.getTeam() + (rd.isAwayGame() ? " at " : " vs. ") + rd.getOpponentTeam()
						+ " " + rd.getScore1() + "-" + rd.getScore2() + " projected " + rd.getPointsfor() + "-"
						+ rd.getPointsagainst());
				pushes++;
			}
		}
	}

	/**
	 * 
	 * @param spread
	 * @param juice
	 * @param aspTeam
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determineNegativeSpread(SpreadRecordEvent spreadRecordEvent, float spread, float juice, TeamPackage teamPackage) throws BatchException {
		return null;
	}
	
	/**
	 * 
	 * @param spread
	 * @param juice
	 * @param aspTeam
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determinePositiveSpread(SpreadRecordEvent spreadRecordEvent, float spread, float juice, TeamPackage aspTeam) throws BatchException {
		return null;
	}

	/**
	 * 
	 * @param spread
	 * @param juice
	 * @param aspTeam
	 * @return
	 * @throws BatchException
	 */
	protected SiteTransaction determineEqualSpread(SpreadRecordEvent spreadRecordEvent, float spread, float juice, TeamPackage aspTeam) throws BatchException {
		return null;
	}

	/**
	 * 
	 * @param type
	 * @return
	 * @throws BatchException
	 */
	protected EventsPackage retrievePackage(String type) throws BatchException {
		return null;
	}
	
	/**
	 * 
	 * @param spreadType
	 * @param eventPackage
	 * @return
	 * @throws BatchException
	 */
	protected SiteTeamPackage setupTeam(int spreadType, EventPackage eventPackage) throws BatchException {
		return null;
	}

	@Override
	public SiteTransaction createSpreadTransaction(SiteTeamPackage siteTeamPackage,
			SpreadRecordEvent spreadRecordEvent, int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SiteTransaction createTotalTransaction(SiteTeamPackage siteTeamPackage, TotalRecordEvent totalRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SiteTransaction createMoneyLineTransaction(SiteTeamPackage siteTeamPackage, MlRecordEvent mlRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected SiteTransaction createSiteTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String selectSport(String type) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event,
			AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<EventPackage> loadFromSite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void updateData(List<EventPackage> data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String loginToSite(String username, String password) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}
}