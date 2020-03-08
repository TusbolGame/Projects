/**
 * 
 */
package com.wooanalytics.dao.sites.teamrankings;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.wootechnologies.errorhandling.BatchException;
import com.wootechnologies.model.AccountEvent;
import com.wootechnologies.model.BaseRecordEvent;
import com.wootechnologies.model.EventPackage;
import com.wootechnologies.model.EventsPackage;
import com.wootechnologies.model.MlRecordEvent;
import com.wootechnologies.model.SpreadRecordEvent;
import com.wootechnologies.model.TeamPackage;
import com.wootechnologies.model.TotalRecordEvent;
import com.wootechnologies.services.dao.sites.SiteTeamPackage;
import com.wootechnologies.services.dao.sites.SiteTransaction;

/**
 * @author jmiller
 *
 */
public class TeamRankingsSite extends Updatable<EventPackage> {
	private static final Logger LOGGER = Logger.getLogger(TeamRankingsSite.class);
	private static final TeamRankingsParser trp = new TeamRankingsParser();
	private static final Integer week12017month = 8;
	private static final Integer week12017day = 25;
	private static final Integer week22017month = 9;
	private static final Integer week22017day = 5;
	private static final Integer week12018month = 8;
	private static final Integer week12018day = 24;
	private static final Integer week22018month = 9;
	private static final Integer week22018day = 4;

	/**
	 * 
	 */
	public TeamRankingsSite() {
		super("TeamRankingsSite", "https://www.teamrankings.com", null, null, false, false);
		LOGGER.info("Entering TeamRankingsSite()");

		try {
			setTimezone("ET");
			getHttpClientWrapper().setupHttpClient("None");
			setProcessTransaction(false);
		} catch (Throwable t) {
			LOGGER.warn(t.getMessage(), t);
		}

		LOGGER.info("Exiting TeamRankingsSite()");
	}

	/**
	 * 
	 */
	public TeamRankingsSite(String host, String username, String password) {
		super("TeamRankingsSite", host, username, password, false, false);
		LOGGER.info("Entering TeamRankingsSite()");

		try {
			setTimezone("ET");
			getHttpClientWrapper().setupHttpClient("None");
			setProcessTransaction(false);
		} catch (Throwable t) {
			LOGGER.warn(t.getMessage(), t);
		}

		LOGGER.info("Exiting TeamRankingsSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final TeamRankingsSite trs = new TeamRankingsSite("https://www.teamrankings.com", "", "");
			trs.getHttpClientWrapper().setupHttpClient("None");
			final List<TeamRankingsSos> tsos = trs.getSos(1, 2018);
			for (TeamRankingsSos ts : tsos) {
				LOGGER.error("TeamRankingsSos: " + ts);
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}

	/**
	 * 
	 * @param week
	 * @param year
	 * @return
	 */
	public List<TeamRankingsSos> getSos(Integer week, Integer year) {
		List<TeamRankingsSos> trs = null;
		
		try {
			String xhtml = null;
			if (week == 1 && year == 2018) {
				xhtml = super.getSite("https://www.teamrankings.com/college-football/ranking/season-sos-by-other?date=" + year + "-" + week12017month + "-" + week12017day);
			} else if (week == 1 && year == 2019) {
				xhtml = super.getSite("https://www.teamrankings.com/college-football/ranking/season-sos-by-other?date=" + year + "-" + week12018month + "-" + week12018day);				
			} else if (week > 1 && year == 2018) {
				final Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, year);
				cal.set(Calendar.MONTH, week22017month);
				cal.set(Calendar.DAY_OF_MONTH, week22017day);
				
				int numweeks = week - 2;
				if (numweeks == 0) {
					xhtml = super.getSite("https://www.teamrankings.com/college-football/ranking/season-sos-by-other?date=" + year + "-" + week22017month + "-" + week22017day);	
				} else {
					for (int x = 0; x < numweeks; x++) {
						cal.add(Calendar.DAY_OF_MONTH, 7);
					}
					xhtml = super.getSite("https://www.teamrankings.com/college-football/ranking/season-sos-by-other?date=" + year + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH));
				}
			} else if (week > 1 && year == 2019) {
				final Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, year);
				cal.set(Calendar.MONTH, week22018month);
				cal.set(Calendar.DAY_OF_MONTH, week22018day);
				
				int numweeks = week - 2;
				if (numweeks == 0) {
					xhtml = super.getSite("https://www.teamrankings.com/college-football/ranking/season-sos-by-other?date=" + year + "-" + week22018month + "-" + week22018day);	
				} else {
					for (int x = 0; x < numweeks; x++) {
						cal.add(Calendar.DAY_OF_MONTH, 7);
					}
					xhtml = super.getSite("https://www.teamrankings.com/college-football/ranking/season-sos-by-other?date=" + year + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH));
				}
			}

			LOGGER.debug("xhtml: " + xhtml);
			trs = trp.parseSos(xhtml, week, year);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return trs;
	}

	/**
	 * 
	 * @return
	 */
	public List<TeamRankingsData> getData(String url) {
		List<TeamRankingsData> dc = null;

		try {
			String xhtml = super.getSite(url);
			LOGGER.debug("xhtml: " + xhtml);

			dc = trp.parseData(xhtml);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dc;
	}

	/**
	 * 
	 * @return
	 */
	public List<TeamRankingsData> getStrengthOfSchedule(String url) {
		List<TeamRankingsData> dc = null;

		try {
			String xhtml = super.getSite(url);
			LOGGER.debug("xhtml: " + xhtml);

			dc = trp.parseStrengthOfSchedule(xhtml);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dc;
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