/**
 * 
 */
package com.wooanalytics.dao.sites.kenpom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
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
import com.wootechnologies.services.dao.sites.SiteProcessor;
import com.wootechnologies.services.dao.sites.SiteTeamPackage;
import com.wootechnologies.services.dao.sites.SiteTransaction;

/**
 * @author jmiller
 *
 */
public class KenPomSite extends SiteProcessor {
	private final static Logger LOGGER = Logger.getLogger(KenPomSite.class);
	private final KenPomParser kpp = new KenPomParser();

	/**
	 * 
	 */
	public KenPomSite(String host, String username, String password) {
		super("KenPomSite", host, username, password, false, false);
		LOGGER.info("Entering KenPomSite()");
		LOGGER.info("Exiting KenPomSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final KenPomSite kps = new KenPomSite("https://kenpom.com", "William.Pottle1@gmail.com", "Montross");
			kps.httpClientWrapper.setupHttpClient("None");
			final List<KenPomData> lkpd = kps.getNcaabScheduleNextTwoDays();
	
			for (KenPomData kpd : lkpd) {
				LOGGER.error("kpd: " + kpd);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<KenPomData> getNcaabScheduleNextTwoDays() {
		final List<KenPomData> nd = new ArrayList<KenPomData>();

		try {
			this.loginToSite("William.Pottle1@gmail.com", "Montross");

			// Get time
			final Calendar yesterday = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			yesterday.add(Calendar.DATE, -1); // number of days to subtract
			int monthMinusOne = yesterday.get(Calendar.MONTH) + 1;			
			int dayMinusOne = yesterday.get(Calendar.DAY_OF_MONTH);
			int yearMinusOne = yesterday.get(Calendar.YEAR);
			String monthMinusOneString = String.valueOf(monthMinusOne);
			String dayMinusOneString = String.valueOf(dayMinusOne);

			if (monthMinusOne < 10) {
				monthMinusOneString = "0" + String.valueOf(monthMinusOne);
			}
			if (dayMinusOne < 10) {
				dayMinusOneString = "0" + String.valueOf(dayMinusOne);
			}
			String xhtml = super.getSite("https://kenpom.com/fanmatch.php?d=" + yearMinusOne + "-" + monthMinusOneString + "-" + dayMinusOneString);
			kpp.parseNcaabSchedule(yearMinusOne + "-" + monthMinusOneString + "-" + dayMinusOneString, nd, xhtml);

			// Get time
			final Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			int month = date.get(Calendar.MONTH) + 1;
			int day = date.get(Calendar.DAY_OF_MONTH);
			int year = date.get(Calendar.YEAR);
			String monthString = String.valueOf(month);
			String dayString = String.valueOf(day);

			if (month < 10) {
				monthString = "0" + String.valueOf(month);
			}
			if (day < 10) {
				dayString = "0" + String.valueOf(day);
			}
			xhtml = super.getSite("https://kenpom.com/fanmatch.php?d=" + year + "-" + monthString + "-" + dayString);
			kpp.parseNcaabSchedule(year + "-" + monthString + "-" + dayString, nd, xhtml);

			final Calendar datePlusOne = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			datePlusOne.add(Calendar.DATE, 1); // number of days to add
			int monthPlusOne = datePlusOne.get(Calendar.MONTH) + 1;
			int dayPlusOne = datePlusOne.get(Calendar.DAY_OF_MONTH);
			int yearPlusOne = datePlusOne.get(Calendar.YEAR);
			String monthPlusOneString = String.valueOf(monthPlusOne);
			String dayPlusOneString = String.valueOf(dayPlusOne);

			if (monthPlusOne < 10) {
				monthPlusOneString = "0" + String.valueOf(monthPlusOne);
			}
			if (dayPlusOne < 10) {
				dayPlusOneString = "0" + String.valueOf(dayPlusOne);
			}
			xhtml = super.getSite("https://kenpom.com/fanmatch.php?d=" + yearPlusOne + "-" + monthPlusOneString + "-" + dayPlusOneString);
			kpp.parseNcaabSchedule(yearPlusOne + "-" + monthPlusOneString + "-" + dayPlusOneString, nd, xhtml);

			for (KenPomData kpd : nd) {
				String roadUrl = kpd.getRoadUrlData();
//				xhtml = super.getSite("https://kenpom.com/" + roadUrl);
//				kpp.parseNcaabLocation(kpd, xhtml, 0, kpd.getRoadTeam());
				String homeUrl = kpd.getHomeUrlData();
				xhtml = super.getSite("https://kenpom.com/" + homeUrl);
				kpp.parseNcaabLocation(kpd, xhtml, 1, kpd.getHomeTeam());
				LOGGER.debug("KPDKPD: " + kpd);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return nd;
	}

	/**
	 * 
	 * @return
	 */
	public List<KenPomData> getNcaabScheduleNextDay() {
		final List<KenPomData> nd = new ArrayList<KenPomData>();

		try {
			this.loginToSite("William.Pottle1@gmail.com", "Montross");

			// Get tomorrow's date/time
			final Calendar datePlusOne = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			datePlusOne.add(Calendar.DATE, 1); // number of days to add
			int monthPlusOne = datePlusOne.get(Calendar.MONTH) + 1;
			int dayPlusOne = datePlusOne.get(Calendar.DAY_OF_MONTH);
			int yearPlusOne = datePlusOne.get(Calendar.YEAR);
			String monthPlusOneString = String.valueOf(monthPlusOne);
			String dayPlusOneString = String.valueOf(dayPlusOne);

			if (monthPlusOne < 10) {
				monthPlusOneString = "0" + String.valueOf(monthPlusOne);
			}
			if (dayPlusOne < 10) {
				dayPlusOneString = "0" + String.valueOf(dayPlusOne);
			}
			String xhtml = super.getSite("https://kenpom.com/fanmatch.php?d=" + yearPlusOne + "-" + monthPlusOneString + "-" + dayPlusOneString);
			kpp.parseNcaabSchedule(yearPlusOne + "-" + monthPlusOneString + "-" + dayPlusOneString, nd, xhtml);

			for (KenPomData kpd : nd) {
				final String homeUrl = kpd.getHomeUrlData();
				xhtml = super.getSite("https://kenpom.com/" + homeUrl);
				kpp.parseNcaabLocation(kpd, xhtml, 1, kpd.getHomeTeam());
				LOGGER.debug("KPDKPD: " + kpd);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return nd;
	}

	/**
	 * 
	 * @return
	 */
	public List<KenPomData> getNcaabScheduleNextThreeDays() {
		final List<KenPomData> nd = new ArrayList<KenPomData>();

		try {
			this.loginToSite("William.Pottle1@gmail.com", "Montross");

			// Get time
			final Calendar yesterday = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			yesterday.add(Calendar.DATE, -1); // number of days to subtract
			int monthMinusOne = yesterday.get(Calendar.MONTH) + 1;			
			int dayMinusOne = yesterday.get(Calendar.DAY_OF_MONTH);
			int yearMinusOne = yesterday.get(Calendar.YEAR);

			String xhtml = super.getSite("https://kenpom.com/fanmatch.php?d=" + yearMinusOne + "-" + monthMinusOne + "-" + dayMinusOne);
			kpp.parseNcaabSchedule(yearMinusOne + "-" + monthMinusOne + "-" + dayMinusOne, nd, xhtml);

			// Get time
			final Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			int month = date.get(Calendar.MONTH) + 1;
			int day = date.get(Calendar.DAY_OF_MONTH);
			int year = date.get(Calendar.YEAR);

			xhtml = super.getSite("https://kenpom.com/fanmatch.php?d=" + year + "-" + month + "-" + day);
			kpp.parseNcaabSchedule(year + "-" + month + "-" + day, nd, xhtml);

			final Calendar datePlusOne = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			datePlusOne.add(Calendar.DATE, 1); // number of days to add
			int monthPlusOne = datePlusOne.get(Calendar.MONTH) + 1;
			int dayPlusOne = datePlusOne.get(Calendar.DAY_OF_MONTH);
			int yearPlusOne = datePlusOne.get(Calendar.YEAR);

			xhtml = super.getSite("https://kenpom.com/fanmatch.php?d=" + yearPlusOne + "-" + monthPlusOne + "-" + dayPlusOne);
			kpp.parseNcaabSchedule(yearPlusOne + "-" + monthPlusOne + "-" + dayPlusOne, nd, xhtml);

			final Calendar datePlusTwo = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			datePlusTwo.add(Calendar.DATE, 2); // number of days to add
			int monthPlusTwo = datePlusTwo.get(Calendar.MONTH) + 1;
			int dayPlusTwo = datePlusTwo.get(Calendar.DAY_OF_MONTH);
			int yearPlusTwo = datePlusTwo.get(Calendar.YEAR);

			xhtml = super.getSite("https://kenpom.com/fanmatch.php?d=" + yearPlusTwo + "-" + monthPlusTwo + "-" + dayPlusTwo);
			kpp.parseNcaabSchedule(yearPlusTwo + "-" + monthPlusTwo + "-" + dayPlusTwo, nd, xhtml);

			for (KenPomData kpd : nd) {
				String roadUrl = kpd.getRoadUrlData();
//				xhtml = super.getSite("https://kenpom.com/" + roadUrl);
//				kpp.parseNcaabLocation(kpd, xhtml, 0, kpd.getRoadTeam());
				String homeUrl = kpd.getHomeUrlData();
				xhtml = super.getSite("https://kenpom.com/" + homeUrl);
				kpp.parseNcaabLocation(kpd, xhtml, 1, kpd.getHomeTeam());
				LOGGER.debug("KPDKPD: " + kpd);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return nd;
	}

	/**
	 * 
	 * @return
	 */
	public List<KenPomData> getNcaabScheduleLastThreeDays() {
		final List<KenPomData> nd = new ArrayList<KenPomData>();

		try {
			this.loginToSite("William.Pottle1@gmail.com", "Montross");

			// Get time
			final Calendar yesterday = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			yesterday.add(Calendar.DATE, -1); // number of days to subtract
			int monthMinusOne = yesterday.get(Calendar.MONTH) + 1;			
			int dayMinusOne = yesterday.get(Calendar.DAY_OF_MONTH);
			int yearMinusOne = yesterday.get(Calendar.YEAR);

			String xhtml = super.getSite("https://kenpom.com/fanmatch.php?d=" + yearMinusOne + "-" + monthMinusOne + "-" + dayMinusOne);
			kpp.parseNcaabSchedule(yearMinusOne + "-" + monthMinusOne + "-" + dayMinusOne, nd, xhtml);

			// Get time
			final Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			yesterday.add(Calendar.DATE, -2); // number of days to subtract
			int month = date.get(Calendar.MONTH) + 1;
			int day = date.get(Calendar.DAY_OF_MONTH);
			int year = date.get(Calendar.YEAR);

			xhtml = super.getSite("https://kenpom.com/fanmatch.php?d=" + year + "-" + month + "-" + day);
			kpp.parseNcaabSchedule(year + "-" + month + "-" + day, nd, xhtml);

			// Get time
			final Calendar datePlusOne = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
			datePlusOne.add(Calendar.DATE, -3); // number of days to substract
			int monthPlusOne = datePlusOne.get(Calendar.MONTH) + 1;
			int dayPlusOne = datePlusOne.get(Calendar.DAY_OF_MONTH);
			int yearPlusOne = datePlusOne.get(Calendar.YEAR);

			xhtml = super.getSite("https://kenpom.com/fanmatch.php?d=" + yearPlusOne + "-" + monthPlusOne + "-" + dayPlusOne);
			kpp.parseNcaabSchedule(yearPlusOne + "-" + monthPlusOne + "-" + dayPlusOne, nd, xhtml);

			LOGGER.debug("nd.size()" + nd.size());
			for (KenPomData kpd : nd) {
				String roadUrl = kpd.getRoadUrlData();
//				xhtml = super.getSite("https://kenpom.com/" + roadUrl);
//				kpp.parseNcaabLocation(kpd, xhtml, 0, kpd.getRoadTeam());
				String homeUrl = kpd.getHomeUrlData();
				xhtml = super.getSite("https://kenpom.com/" + homeUrl);
				kpp.parseNcaabLocation(kpd, xhtml, 1, kpd.getHomeTeam());
				LOGGER.debug("KPDKPD: " + kpd);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return nd;
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

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#loginToSite(java.lang.String, java.lang.String)
	 */
	@Override
	public String loginToSite(String username, String password) throws BatchException {
		LOGGER.info("Entering loginToSite()");
		LOGGER.debug("username: " + username);
		LOGGER.debug("password: " + password);

		// Get the home page
		String xhtml = getSite(httpClientWrapper.getHost());
		LOGGER.debug("XHTML: " + xhtml);

		// Setup the customer data
		MAP_DATA = new HashMap<String, String>();
		MAP_DATA.put("email", username);
		MAP_DATA.put("password", password);
		MAP_DATA.put("remember", "on");
		MAP_DATA.put("submit", "Login!");
		LOGGER.debug("Map: " + MAP_DATA);

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		// Call the login
		xhtml = authenticate("https://kenpom.com/handlers/login_handler.php", postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}