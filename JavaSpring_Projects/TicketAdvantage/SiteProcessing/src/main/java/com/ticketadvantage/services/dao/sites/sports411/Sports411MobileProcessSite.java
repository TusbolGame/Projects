/**
 * 
 */
package com.ticketadvantage.services.dao.sites.sports411;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.ProxyContainer;
import com.ticketadvantage.services.dao.sites.SiteEventPackage;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.dao.sites.SiteWagers;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class Sports411MobileProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(Sports411MobileProcessSite.class);
	protected final Sports411MobileParser S411P = new Sports411MobileParser();
	private int rerunCount = 0;

	/**
	 * 
	 */
	public Sports411MobileProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("Sports411Mobile", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering Sports411MobileProcessSite()");

		// Setup the parser
		this.siteParser = S411P;

		// Menu items
		NFL_LINES_SPORT = new String[] { "FOOTBALL" };
		NFL_LINES_NAME = new String[] { "NFL" };
		NFL_FIRST_SPORT = new String[] { "FOOTBALL" };
		NFL_FIRST_NAME = new String[] { "NFL - 1ST HALVES" };
		NFL_SECOND_SPORT = new String[] { "FOOTBALL" };
		NFL_SECOND_NAME = new String[] { "NFL - 2ND HALVES" };
		NCAAF_LINES_SPORT = new String[] { "FOOTBALL" };
		NCAAF_LINES_NAME = new String[] { "COLLEGE FOOTBALL" };
		NCAAF_FIRST_SPORT = new String[] { "FOOTBALL" };
		NCAAF_FIRST_NAME = new String[] { "NCAA (F) - 1ST HALVES" };
		NCAAF_SECOND_SPORT = new String[] { "FOOTBALL" };
		NCAAF_SECOND_NAME = new String[] { "NCAA (F) - 2ND HALVES" };
		NBA_LINES_SPORT = new String[] { "BASKETBALL" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "BASKETBALL" };
		NBA_FIRST_NAME = new String[] { "NBA - 1ST HALVES" };
		NBA_SECOND_SPORT = new String[] { "BASKETBALL" };
		NBA_SECOND_NAME = new String[] { "NBA - 2ND HALVES" };
		NCAAB_LINES_SPORT = new String[] { "BASKETBALL" };
		NCAAB_LINES_NAME = new String[] { "COLLEGE BASKETBALL", "NCAA (B) EXTRA GAMES" };
		NCAAB_FIRST_SPORT = new String[] { "BASKETBALL" };
		NCAAB_FIRST_NAME = new String[] { "NCAA (B) - 1ST HALVES" };
		NCAAB_SECOND_SPORT = new String[] { "BASKETBALL" };
		NCAAB_SECOND_NAME = new String[] { "NCAA (B) - 2ND HALVES" };
		NHL_LINES_SPORT = new String[] { "HOCKEY" };
		NHL_LINES_NAME = new String[] { "NHL" };
		NHL_FIRST_SPORT = new String[] { "HOCKEY" };
		NHL_FIRST_NAME = new String[] { "NHL - 1ST HALVES", "NHL - 1ST PERIOD" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY" };
		NHL_SECOND_NAME = new String[] { "NHL - 2ND HALVES", "NHL - 2ND PERIOD" };
		WNBA_LINES_SPORT = new String[] { "BASKETBALL" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_FIRST_SPORT = new String[] { "BASKETBALL" };
		WNBA_FIRST_NAME = new String[] { "WNBA", "WNBA - 1ST HALVES" };
		WNBA_SECOND_SPORT = new String[] { "BASKETBALL" };
		WNBA_SECOND_NAME = new String[] { "WNBA", "WNBA - 2ND HALVES" };
		MLB_LINES_SPORT = new String[] { "BASEBALL" };
		MLB_LINES_NAME = new String[] { "MAJOR LEAGUE BASEBALL" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL" };
		MLB_FIRST_NAME = new String[] { "MLB-1ST 5 FULL INNINGS" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL" };
		MLB_SECOND_NAME = new String[] { "MLB - 2ND HALVES" };

		LOGGER.info("Exiting Sports411MobileProcessSite()");
	}

	/**
	 * 
	 * @param accountSoftware
	 * @param host
	 * @param username
	 * @param password
	 */
	public Sports411MobileProcessSite(String accountSoftware, String host, String username, String password) {
		super(accountSoftware, host, username, password);
		LOGGER.info("Entering Sports411MobileProcessSite()");
		LOGGER.info("Exiting Sports411MobileProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] {
//				{ "http://www.sports411.ag", "8660", "rq7", "0", "0", "0", "Los Angeles", "PT"}
//				{ "https://be.sports411.ag", "9461", "lenny", "0", "0", "0", "None", "PT"}
//				{ "https://be.bookmaker.eu", "bkr572689", "wootech123!", "100", "100", "100", "None", "CT" }
				{ "https://be.sports411.ag", "a3327", "espana2", "500", "500", "500", "None", "PT"}
			};

			final Sports411MobileProcessSite processSite = new Sports411MobileProcessSite(TDSITES[0][0], TDSITES[0][1],
					TDSITES[0][2], true, true);
		    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = TDSITES[0][7];

			String xhtml = processSite.loginToSite(processSite.httpClientWrapper.getUsername(),
					processSite.httpClientWrapper.getPassword());
			LOGGER.debug("xhtml: " + xhtml);

			final MlRecordEvent mre = new MlRecordEvent();
			mre.setMlplusminussecondone("+");
			mre.setMlinputsecondone("118");
			mre.setId(new Long(520));
			mre.setEventname("MLB #914 Baltimore Orioles +118 for Game");
			mre.setEventtype("ml");
			mre.setSport("mlblines"); 
			mre.setUserid(new Long(1));
			mre.setEventid(new Integer(913));
			mre.setEventid1(new Integer(913));
			mre.setEventid2(new Integer(914));
			mre.setRotationid(new Integer(914));
			mre.setWtype("2");
			mre.setAmount("500");
			final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
			cal.set(Calendar.HOUR_OF_DAY, 9);
			cal.set(Calendar.MINUTE, 35);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MONTH, 3);
			cal.set(Calendar.DAY_OF_MONTH, 11);
			cal.set(Calendar.YEAR, 2019);
			mre.setEventdatetime(cal.getTime());
			mre.setDatentime(cal.getTime());
			mre.setEventteam1("");
			mre.setEventteam2("Baltimore Orioles");
			mre.setGroupid(new Long(-99));
			mre.setIscompleted(false);
			mre.setScrappername("UI");
			mre.setActiontype("Standard");
			mre.setTextnumber("");
			mre.setDatecreated(new Date());
			mre.setDatemodified(new Date());
			mre.setCurrentattempts(0);

			final SpreadRecordEvent sre = new SpreadRecordEvent();
			sre.setSpreadplusminussecondone("-");
			sre.setSpreadinputsecondone("1.5");
			sre.setSpreadjuiceplusminussecondone("+");
			sre.setSpreadinputjuicesecondone("120");
			sre.setId(new Long(520));
			sre.setEventname("MLB #904 Houston Astros -1½ (+120) for Game");
			sre.setEventtype("spread");
			sre.setSport("mlblines"); 
			sre.setUserid(new Long(1));
			sre.setEventid(new Integer(903));
			sre.setEventid1(new Integer(903));
			sre.setEventid2(new Integer(904));
			sre.setRotationid(new Integer(904));
			sre.setWtype("1");
			sre.setAmount("500");
			final Calendar scal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
			scal.set(Calendar.HOUR_OF_DAY, 19);
			scal.set(Calendar.MINUTE, 10);
			scal.set(Calendar.SECOND, 0);
			scal.set(Calendar.MONTH, 3);
			scal.set(Calendar.DAY_OF_MONTH, 5);
			scal.set(Calendar.YEAR, 2019);
			sre.setEventdatetime(scal.getTime());
			sre.setDatentime(scal.getTime());
			sre.setEventteam1("");
			sre.setEventteam2("Houston Astros");
			sre.setGroupid(new Long(-99));
			sre.setIscompleted(false);
			sre.setScrappername("UI");
			sre.setActiontype("Standard");
			sre.setTextnumber("");
			sre.setDatecreated(new Date());
			sre.setDatemodified(new Date());
			sre.setCurrentattempts(0);

			final AccountEvent ae = new AccountEvent();
			ae.setAccountid(new Long(451));
			ae.setAmount("500");
			ae.setActualamount("");
			ae.setAttempts(0);
			ae.setCurrentattempts(0);
			ae.setDatecreated(new Date());
			ae.setDatemodified(new Date());
			ae.setEventdatetime(scal.getTime());
			ae.setEventid(914);
			ae.setEventname("MLB #914 Baltimore Orioles +118 for Game");
			ae.setGroupid(new Long(-99));
			ae.setIscompleted(false);
			ae.setMaxspreadamount(50);
			ae.setMaxtotalamount(50);
			ae.setMaxmlamount(500);
			ae.setMlindicator("-");
			ae.setTimezone("PT");
			ae.setSpread(new Float(-1.5));
			ae.setSpreadid(new Long(964));
			ae.setSpreadindicator("+");
			ae.setSpreadjuice(new Float(120));
			ae.setMl(new Float(118));
			ae.setMlid(new Long(964));
			ae.setMljuice(new Float(118));
			ae.setName("690Sports-XXX");
			ae.setOwnerpercentage(100);
			ae.setPartnerpercentage(0);
			ae.setProxy("None");
			ae.setSport("mlblines");
			ae.setSpreadindicator("");
			ae.setSpread(new Float(0));
			ae.setSpreadid(new Long(0));
			ae.setSpreadjuice(new Float(0));
			ae.setTotal(new Float(0));
			ae.setTotalid(new Long(0));
			ae.setTotalindicator("");
			ae.setTotaljuice(new Float(0));
			ae.setType("spread");
			ae.setUserid(new Long(1));
			ae.setWagertype("2");
			ae.setStatus("");
			ae.setIscomplexcaptcha(false);
			ae.setHumanspeed(false);
			
			processSite.processMlTransaction(mre, ae);
//			processSite.processSpreadTransaction(sre, ae);

/*
			Map<String, String> map = processSite.getMenuFromSite("ncaablines", xhtml);
			LOGGER.debug("map: " + map);

			xhtml = processSite.selectSport("ncaablines");
			LOGGER.debug("XHTML: " + xhtml);

			final Iterator<EventPackage> ep = processSite.parseGamesOnSite("ncaablines", xhtml);

			final SpreadRecordEvent mre = new SpreadRecordEvent();
			mre.setSpreadjuiceplusminussecondone("-");
			mre.setSpreadinputjuicesecondone("110");
			mre.setSpreadplusminussecondone("+");
			mre.setSpreadinputsecondone("7.5");
			mre.setId(new Long(110));
			mre.setEventname("#582 Sac Kings +7.5 -110");
			mre.setEventtype("spread");
			mre.setSport("ncaablines");
			mre.setUserid(new Long(6));
			mre.setEventid(new Integer(581));
			mre.setEventid1(new Integer(581));
			mre.setEventid2(new Integer(582));
			mre.setRotationid(new Integer(582));
			mre.setWtype("2");
			mre.setAmount("100");

			final TotalRecordEvent tre = new TotalRecordEvent();
			tre.setTotalinputsecondone("133");
			tre.setTotaljuiceplusminussecondone("-");
			tre.setTotalinputjuicesecondone("120");
			tre.setId(new Long(309));
			tre.setEventname("NCAAB #856 KANSAS STATE vrs WEST VIRGINIA u133.0 -120.0 for Game");
			tre.setEventtype("total");
			tre.setSport("ncaaflines");
			tre.setUserid(new Long(6));
			tre.setEventid(new Integer(805));
			tre.setEventid1(new Integer(805));
			tre.setEventid2(new Integer(806));
			tre.setRotationid(new Integer(806));
			tre.setWtype("2");

			// Step 9 - Find event
			EventPackage eventPackage = processSite.findEvent(ep, tre);
			if (eventPackage == null) {
				throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE,
						"Game cannot be found on site for " + mre.getEventname(), xhtml);
			}
*/
		} catch (Throwable t) {
			LOGGER.info("Throwable: ", t);
		}
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

		// Setup the timezone
		S411P.setTimezone(timezone);

		// Call the second page
		String xhtml = super.getMobileSite(httpClientWrapper.getHost());
		LOGGER.debug("HTML: " + xhtml);

		// Get home page data
		MAP_DATA = S411P.parseIndex(xhtml);
		MAP_DATA.put("account", username);
		MAP_DATA.put("password", password);
		MAP_DATA.remove("Submit1");

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, "");
		LOGGER.debug("ActionLogin: " + actionLogin);

		// Get the pending bets data
		final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(4);
		headerValuePairs.add(new BasicNameValuePair("Origin", this.httpClientWrapper.getHost()));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost() + "/en/login?lk=-1410755172/"));
		headerValuePairs.add(new BasicNameValuePair("authorization", "my-auth-token"));

		// https://be.sports411.ag/BetslipProxy.aspx/Login
		String jsonString = "{\"oDgs\":{\"BODgsRequestData\":{\"BODgsParameters\":{\"Player\":\"" + username + "\",\"Password\":\"" + password + "\",\"loginKey\":\"\"},\"BODgsPost\":{\"Bets\":{}},\"BOSignupRequest\":{}}}}";
		xhtml = super.postJSONSite(httpClientWrapper.getHost() + "/BetslipProxy.aspx/Login", jsonString, headerValuePairs);	
		
		// Call the login
//		xhtml = authenticateMobile(httpClientWrapper.getHost() + "/loginvalidate.aspx", postValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		// WebappName
		httpClientWrapper.setWebappname("");

		// Parse login
		MAP_DATA = S411P.parseLogin2(xhtml, "login.aspx");
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		setUsernamePassword(username, password);
		postValuePairs = new ArrayList<NameValuePair>(1);
		actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, "");
		LOGGER.debug("ActionLogin: " + actionLogin);

		// Use the classic view
		xhtml = super.getSite(httpClientWrapper.getHost() + "/SelectLeagues.aspx?wagertype=0");
		LOGGER.debug("HTML: " + xhtml);

/*
		if (actionLogin != null) {
			// Call post login form
			xhtml = postMobileSite(httpClientWrapper.getHost() + "/login.aspx", postValuePairs);
			LOGGER.debug("XHTML: " + xhtml);

			// Parse login
			MAP_DATA = S411P.parseLogin2(xhtml, "login.aspx");
			LOGGER.debug("MAP_DATA: " + MAP_DATA);

			postValuePairs = new ArrayList<NameValuePair>(1);
			MAP_DATA.put("ctl00$mainContent$bbox", "0400R9HVeoYv1gsNf94lis1ztu1p0Y9k4RDYnXnFeuAo3dP18km4Z/hfm7pq+hlM8xD7UMua3r2tn0CM/P8l/YckxNagR+wrhtnGDIKINVrQrNLcRehbfBGuVyDBS5H29TfUuZFchGeVJ9QNczN14q3lywh3sf2PehRXSTW5EtRLItPxwUJvdekrQgqspPEsMqd75IPP5C6+G8ADJHelPPSzJdx+o91KrXdXhtqtBLkVJp6Dew1QqPCWV6buKbPOUrKWEp48DVRl0oDWlCnbPCzZANRNEbllADzETzEBdNLiXsNS5LoCa7Ohp0Kif+bYVjngPSX/NxfriTvFzW/K2m1MJUzI+mPNi65Xi02TZ164MHhOfdJx+sF9hHsaLGMPfA5ky+QzFZsqaAyMJs72yUnJmKy2qvBVy7YLrtkxj5YTEHULpdNHfNCUY46H2W0dl9AiCA+hWOqdNHoULoDhcfK1nnng/jEWgBDf77A/AHthzIQI07p4tGGHBe8mR20/oWqfEfhuQX1cBnnAqvQx/6iBZtcbAlMg89W4woiYXkg7fgP1qOtVQPZTytT16TeM5diEHf7WUtiodkTK7BAgLp8bSQHTP4+P9b1VBmFJTID5+2SRw5h4t7eliiSOXYIK/I8iqlr028/3Pq8QEJ66vnPRw/rJz/fklocMF1TwjFw0afkd/g3ekijXJGXFRCJLFDgwnFJjUN1Wk+CCUpb51HWac3Y+4QKyjIZL0+hIaafgiM0qLP6Lq1GR5y7auKliuE2Cqyk8Swyp3vkg8/kLr4bwAMkd6U89LMl3H6j3Uqtd1eG2q0EuRUmnoN7DVCo8JZXpu4ps85SspYSnjwNVGXSgNaUKds8LNkA1E0RuWUAPMRPMQF00uJew1LkugJrs6GnQqJ/5thWOeA9Jf83F+uJO8XNb8rabUwlTMj6Y82LrleLTZNnXrgweAtuHmc6Pe7cNepEKIoJSuw3d6IKmSOf2anDigzlYY/qmssqTe7WzQg67PDmZdnwArgn0xH0DfFjiyuFFbVpl8yCfhzYVpCb849qpLt2ZL85XTb8qqlSvw0bxzJYo9QP/YMpuo0JeQGs7qLL3eIqe3Lwh/o9Lic5vlwP6gIHuvSKwk0oOtKqOrJSwejOX5dhKGYrsu13rc4RCbryu9G8AaRxi/UgQBHzxwUkaRoD62ZVyZvrFLFceL756en9ssKpnvzz3BSuSxojWfKwc8TfWXLcrUGJRxCdhaVbsuAtqFgyGhOqLtqZ6t0t34d5Kh0uGTSamHVG7rRHfqYAndlMe7bdZoUxykO48vWDDY6js4EVkiMmRbFqW+1cfzyluCQUdsRanzLWZ7oHL4vRfdwnJ5tx/1bXjwnIu7h/NNRQpAQpBDL+bbK9Z4M9l6B6vZgYctAeg7VQHF5ex9P1Zr3jTCio6wybqU4cLtnNZkMo2UfOhz0USjATLsXmx2vNwg2QegctWZHFg5nzRg+yXOr7brw8QY9umXM0gRtt2cmQtdAt06CcjYkspF8lcoqw04kgEYL5kL+6EqUedoA16l/knHjEtN2/kez/J5TjwBQlRZvH3wii4Zs6HyW7DuHxu8wgQg5MYDO1xERGUSwN1etPzcI=");
			actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, "");
			LOGGER.debug("ActionLogin: " + actionLogin);

			// Call post login form
			xhtml = postMobileSite(httpClientWrapper.getHost() + "/login.aspx", postValuePairs);
			LOGGER.debug("HTML: " + xhtml);

			// Parse login
			MAP_DATA = S411P.parseLogin2(xhtml, "login.aspx");
			LOGGER.debug("MAP_DATA: " + MAP_DATA);

			// Use the classic view
			xhtml = super.getSite(httpClientWrapper.getHost() + "/SelectLeagues.aspx?wagertype=0");
			LOGGER.debug("HTML: " + xhtml);
		}
*/

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#processSpreadTransaction(com.ticketadvantage.services.model.SpreadRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	public void processSpreadTransaction(SpreadRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processSpreadTransaction()");
		LOGGER.debug("SpreadRecordEvent: " + event);
		LOGGER.debug("AccountEvent: " + ae);

		final EventPackage eventPackage = setupTransaction(event, ae); 
		LOGGER.debug("EventPackage" + eventPackage);

		if (eventPackage != null) {
			// Finish the transaction
			finishTransaction(null, eventPackage, event, ae);
		}

		LOGGER.info("Exiting processSpreadTransaction()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#processTotalTransaction(com.ticketadvantage.services.model.TotalRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	public void processTotalTransaction(TotalRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processTotalTransaction()");
		LOGGER.debug("TotalRecordEvent: " + event);
		LOGGER.debug("AccountEvent: " + ae);
		
		final EventPackage eventPackage = setupTransaction(event,  ae); 
		LOGGER.debug("EventPackage" + eventPackage);

		if (eventPackage != null) {
			// Finish the transaction
			finishTransaction(null, eventPackage, event, ae);
		}

		LOGGER.info("Exiting processTotalTransaction()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#processMlTransaction(com.ticketadvantage.services.model.MlRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	public void processMlTransaction(MlRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processMlTransaction()");
		LOGGER.debug("MlRecordEvent: " + event);
		LOGGER.debug("AccountEvent: " + ae);

		final EventPackage eventPackage = setupTransaction(event,  ae); 
		LOGGER.debug("EventPackage" + eventPackage);

		if (eventPackage != null) {
			// Finish the transaction
			finishTransaction(null, eventPackage, event, ae);
		}

		LOGGER.info("Exiting processMlTransaction()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#finishTransaction(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	public void finishTransaction(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering finishTransaction()");
		String xhtml = "";

		try {
			//
			// Call the Game selection
			//
			// Step 12 - Select event and parse wager info
			xhtml = selectEvent(siteTransaction, eventPackage, event, ae);
			LOGGER.debug("xhtml: " + xhtml);
	
			// Step 13 - Parse the event selection
			parseEventSelection(xhtml, siteTransaction, eventPackage, event, ae);

			// Step 13 1/2 - Setup site transaction
			if (event instanceof SpreadRecordEvent) {
				siteTransaction = getSpreadTransaction((SpreadRecordEvent)event, eventPackage, ae);
			} else if (event instanceof TotalRecordEvent) {
				siteTransaction = getTotalTransaction((TotalRecordEvent)event, eventPackage, ae);
			} else if (event instanceof MlRecordEvent) {
				siteTransaction = getMlTransaction((MlRecordEvent)event, eventPackage, ae);	
			}

			//
			// Call the Complete Wager
			//
			// Step 15 - Complete transaction
			xhtml = completeTransaction(siteTransaction, eventPackage, event, ae);

			// Step 16 - Parse the ticket #
			String ticketNumber = parseTicketTransaction(xhtml);

			// Set the account data
			ae.setAccountconfirmation(ticketNumber);
			ae.setAccounthtml(xhtml);
		} catch (BatchException be) {
			if (be.getErrorcode() == BatchErrorCodes.LINE_CHANGED_ERROR) {
				final String type = event.getEventtype();
				if ("spread".equals(type)) {
					processSpreadTransaction((SpreadRecordEvent)event, ae);
				} else if ("total".equals(type)) {
					processTotalTransaction((TotalRecordEvent)event, ae);
				} else if ("ml".equals(type)) {
					processMlTransaction((MlRecordEvent)event, ae);
				}
			} else if (be.getErrorcode() == BatchErrorCodes.WAGER_HAS_EXPIRED) {
				final String type = event.getEventtype();
				if ("spread".equals(type)) {
					processSpreadTransaction((SpreadRecordEvent)event, ae);
				} else if ("total".equals(type)) {
					processTotalTransaction((TotalRecordEvent)event, ae);
				} else if ("ml".equals(type)) {
					processMlTransaction((MlRecordEvent)event, ae);
				}
			} else if (be.getErrorcode() == BatchErrorCodes.HTTP_BAD_GATEWAY_EXCEPTION) {
				final String[] proxyLocations = ProxyContainer.getProxyNames();
				for (int x = 0; (proxyLocations != null && x < proxyLocations.length); x++) {
					String proxyName = proxyLocations[x];
					if (proxyName != null && !proxyName.equals("None") && !proxyName.equals(ae.getProxy())) {
						ae.setProxy(proxyName);
						break;
					}
				}
				final String type = event.getEventtype();
				if ("spread".equals(type)) {
					processSpreadTransaction((SpreadRecordEvent)event, ae);
				} else if ("total".equals(type)) {
					processTotalTransaction((TotalRecordEvent)event, ae);
				} else if ("ml".equals(type)) {
					processMlTransaction((MlRecordEvent)event, ae);
				}
			} else {
				if (be.getHtml() == null || (be.getHtml() != null && be.getHtml().length() == 0)) {
					be.setHtml(xhtml);
					ae.setAccounthtml(xhtml);
				}
				throw be;
			}
		}

		LOGGER.info("Exitng finishTransaction()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectSport(java.lang.String)
	 */
	@Override
	protected String selectSport(String type) throws BatchException {
		LOGGER.info("Entering selectSport()");
		LOGGER.debug("type: " + type);

		// Process the setup page
		MAP_DATA.remove("ctl00$mainContent$btnContinueBottom");
		MAP_DATA.remove("ctl00$TimeZoneDesktop$ctl00$btnSetTimeZone");
		MAP_DATA.remove("ctl00$Feedback2$ctl00$feedbackBtnSubmit3");

		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		final String actionName = setupNameValuesEmpty(postValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionName: " + actionName);

		// Post to site page
		String xhtml = postMobileSite(actionName, postValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		LOGGER.info("Exiting selectSport()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#createSiteTransaction()
	 */
	@Override
	protected SiteTransaction createSiteTransaction() {
		LOGGER.info("Entering createSiteTransaction()");

		final Sports411Transaction tdSportsTransaction = new Sports411Transaction();

		LOGGER.info("Exiting createSiteTransaction()");
		return tdSportsTransaction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectEvent(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering selectEvent()");
		Sports411TeamPackage teamPackage = null;

		// Only get the first one because that's all there is for TDSports at this point
		final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);

		// Get the appropriate record event
		if (event instanceof SpreadRecordEvent) {			
			teamPackage = (Sports411TeamPackage)SiteWagers.setupTeam(SiteWagers.determineSpreadData((SpreadRecordEvent)event), eventPackage);
			if (teamPackage != null && 
				teamPackage.getGameSpreadInputName() != null &&
				teamPackage.getGameSpreadInputName().length() > 0 &&
				teamPackage.getGameSpreadInputValue() != null && 
				teamPackage.getGameSpreadInputValue().length() > 0) {
				postNameValuePairs.add(new BasicNameValuePair(teamPackage.getGameSpreadInputName(), teamPackage.getGameSpreadInputValue()));
			} else {
				throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION, BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No spread available for this game");
			}
		} else if (event instanceof TotalRecordEvent) {
			teamPackage = (Sports411TeamPackage)SiteWagers.setupTeam(SiteWagers.determineTotalData((TotalRecordEvent)event), eventPackage);
			if (teamPackage != null && 
				teamPackage.getGameTotalInputName() != null &&
				teamPackage.getGameTotalInputName().length() > 0 &&
				teamPackage.getGameTotalInputValue() != null && 
				teamPackage.getGameTotalInputValue().length() > 0) {
				postNameValuePairs.add(new BasicNameValuePair(teamPackage.getGameTotalInputName(), teamPackage.getGameTotalInputValue()));
			} else {
				throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION, BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No total available for this game");
			}
		} else if (event instanceof MlRecordEvent) {
			teamPackage = (Sports411TeamPackage)SiteWagers.setupTeam(SiteWagers.determineMoneyLineData((MlRecordEvent)event), eventPackage);
			if (teamPackage != null && 
				teamPackage.getGameMLInputName() != null &&
				teamPackage.getGameMLInputName().length() > 0 &&
				teamPackage.getGameMLInputValue() != null) {
				postNameValuePairs.add(new BasicNameValuePair(teamPackage.getGameMLInputName(), teamPackage.getGameMLInputValue().get("0")));
			} else {
				throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION, BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No moneyline available for this game");
			}
		}

		MAP_DATA.remove("ctl01$mainContent$ctl01");
		MAP_DATA.remove("ctl01$TimeZoneDesktop$ctl00$btnSetTimeZone");
		final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionName: " + actionName);

		// Setup the wager
		final String xhtml = postMobileSite(actionName, postNameValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		// Check for line change
		if (xhtml != null && xhtml.contains("The line changed for one (or more) of your selections.")) {
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
		}

		LOGGER.info("Exiting selectEvent()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		Sports411TeamPackage teamPackage = null;

		MAP_DATA.clear();
		gamesXhtml = xhtml;
		if (event instanceof SpreadRecordEvent) {
			teamPackage = (Sports411TeamPackage)SiteWagers.setupTeam(SiteWagers.determineSpreadData((SpreadRecordEvent)event), eventPackage);
			MAP_DATA = S411P.parseEventSelection(xhtml, teamPackage, "spread");
		} else if (event instanceof TotalRecordEvent) {
			teamPackage = (Sports411TeamPackage)SiteWagers.setupTeam(SiteWagers.determineTotalData((TotalRecordEvent)event), eventPackage);
			MAP_DATA = S411P.parseEventSelection(xhtml, teamPackage, "total");
		} else if (event instanceof MlRecordEvent) {
			teamPackage = (Sports411TeamPackage)SiteWagers.setupTeam(SiteWagers.determineMoneyLineData((MlRecordEvent)event), eventPackage);
			MAP_DATA = S411P.parseEventSelection(xhtml, teamPackage, "ml");
		}
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Check if we have exceeded
		if (MAP_DATA.containsKey("wagerbalanceexceeded")) {
			throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "The maximum account balance has been reached for wager " + siteTransaction.getAmount(), xhtml);
		}

		LOGGER.info("Exiting parseEventSelection()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#completeTransaction(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering completeTransaction()");
		String xhtml = null;

		try {
			// Check if we have exceeded
			if (MAP_DATA.containsKey("wagerbalanceexceeded")) {
				throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT,
						"The maximum account balance has been reached for wager " + siteTransaction.getAmount(), xhtml);
			}

			// Process the wager transaction
			xhtml = processWager(siteTransaction, eventPackage, event, ae, event.getWtype());

			// Check for line change
			if (xhtml != null && xhtml.contains("The line changed for one (or more) of your selections.")) {
				throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR,
						"The line has changed", xhtml);
			}

			if (processTransaction) {
				// Confirm the wager
				xhtml = confirmWager(xhtml, siteTransaction, eventPackage, event, ae, event.getWtype());

				// Check for line change
				if (xhtml != null && xhtml.contains("The line changed for one (or more) of your selections.")) {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			}
		} catch (BatchException be) {
			LOGGER.error("Exception getting ticket number for account event " + ae + " event " + event, be);
			throw be;
		}

		LOGGER.info("Exiting completeTransaction()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseTicketTransaction(java.lang.String)
	 */
	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketTransaction()");
		
		final String ticketNumber = S411P.parseTicketNumber(xhtml);
		LOGGER.debug("ticketNumber: " + ticketNumber);

		LOGGER.info("Exiting parseTicketTransaction()");
		return ticketNumber;		
	}

	/**
	 * 
	 * @param siteTransaction
	 * @param eventPackage
	 * @param event
	 * @param ae
	 * @param wagerType
	 * @return
	 * @throws BatchException
	 */
	private String processWager(SiteTransaction siteTransaction, 
			EventPackage eventPackage, 
			BaseRecordEvent event, 
			AccountEvent ae, 
			String wagerType) throws BatchException {
		LOGGER.info("Entering processWager()");
		SiteTransaction tempSiteTransaction = null;
		SiteTeamPackage theteam = null;
		final SiteEventPackage siteEventPackage = (SiteEventPackage)eventPackage;
		final SiteTeamPackage stp1 = siteEventPackage.getSiteteamone();
		final SiteTeamPackage stp2 = siteEventPackage.getSiteteamtwo();

		if (event.getRotationid().toString().contains(stp1.getId().toString())) {
			theteam = stp1;
		} else {
			theteam = stp2;
		}

		// Get the spread transaction
		if (event instanceof SpreadRecordEvent) {
			tempSiteTransaction = getSpreadTransaction((SpreadRecordEvent)event, eventPackage, ae);

			if (tempSiteTransaction.getRiskorwin() != null && 
				tempSiteTransaction.getRiskorwin().intValue() == 2) {
				float[] sitejuices = getSiteSpreadJuice(theteam);
				float spreadJuice = getJuice(ae.getSpreadjuice(), sitejuices);

				if (spreadJuice > 0) {
					wagerType = "1";
				}
			}
		} else if (event instanceof TotalRecordEvent) {
			tempSiteTransaction = getTotalTransaction((TotalRecordEvent)event, eventPackage, ae);

			if (tempSiteTransaction.getRiskorwin() != null && 
				tempSiteTransaction.getRiskorwin().intValue() == 2) {
				float[] sitejuices = getSiteTotalJuice(theteam);
				float totalJuice = getJuice(ae.getTotaljuice(), sitejuices);

				if (totalJuice > 0) {
					wagerType = "1";
				}
			}
		} else if (event instanceof MlRecordEvent) {
			tempSiteTransaction = getMlTransaction((MlRecordEvent)event, eventPackage, ae);

			if (tempSiteTransaction.getRiskorwin() != null && 
				tempSiteTransaction.getRiskorwin().intValue() == 2) {
				float[] sitejuices = getSiteMlJuice(theteam);
				float mlJuice = getJuice(ae.getMljuice(), sitejuices);

				if (mlJuice > 0) {
					wagerType = "1";
				}
			}
		}

		// Reset the $$ if we have to based on risk vs win
		String siteAmount = determineRiskWinAmounts(siteTransaction, eventPackage, event, ae); 
		LOGGER.error("siteAmount: " + siteAmount);
		siteTransaction.setAmount(siteAmount);
		ae.setActualamount(siteAmount);

		LOGGER.debug("siteAmount: " + siteAmount);
		final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		String key = checkMapKey("amt_");
		MAP_DATA.put(key, siteAmount);
		ae.setActualamount(siteAmount);

		// Now check if selection needs to be sent as well
		if (tempSiteTransaction.getSelectName() != null && tempSiteTransaction.getOptionValue() != null) {
			MAP_DATA.put(tempSiteTransaction.getSelectName(), tempSiteTransaction.getOptionValue());
		}

		// Check for risk or win
		if ("1".equals(wagerType)) {
			key = checkMapKey("AmtType_");
			MAP_DATA.put(key, "2");
		} else {
			key = checkMapKey("AmtType_");
			MAP_DATA.put(key, "1");
		}

		String actionName = null;
//		MAP_DATA.remove("__VIEWSTATE");
		MAP_DATA.remove("ctl00$TimeZoneDesktop$ctl00$btnSetTimeZone");
		MAP_DATA.remove("ctl00$mainContent$btnContinueBottom");
		MAP_DATA.remove("ctl00$Feedback2$ctl00$txtCaptcha");
		MAP_DATA.put("ctl00$mainContent$btnContinueTop", "Continue");
		MAP_DATA.put("ctl00$TimeZoneDesktop$ctl00$ddlTZone","24");
		MAP_DATA.put("ctl00$Feedback1$ctl00$txtComments","");

		key = checkMapKey("pitcher_");
		if (key != null && key.length() > 0) {
			MAP_DATA.put(key, "3");
		}

		actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionName: " + actionName);

		// Setup the wager
		String xhtml = null;
		if (processTransaction) {
			xhtml = postMobileSite(actionName, postNameValuePairs);
			LOGGER.debug("HTML: " + xhtml);
		}

		LOGGER.info("Exiting processWager()");
		return xhtml;
	}

	/**
	 * 
	 * @param xhtml
	 * @param eventPackage
	 * @param event
	 * @param ae
	 * @param wagerType
	 * @return
	 * @throws BatchException
	 */
	private String confirmWager(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae, String wagerType) throws BatchException {
		LOGGER.info("Entering confirmWager()");

		// Parse the Confirm Wager page
		Map<String, String> wagers = S411P.parseConfirmWager(xhtml);

		// Check for a wager limit and rerun
		if (wagers.containsKey("wagerminamount")) {
			throw new BatchException(BatchErrorCodes.MIN_WAGER_AMOUNT_NOT_REACHED, BatchErrorMessage.MIN_WAGER_AMOUNT_NOT_REACHED, "The minimum wager amount has NOT been reached $" + MAP_DATA.get("wagerminamount"), xhtml);
		}
		// Check for a wager limit and rerun
		if (wagers.containsKey("wagerbalanceexceeded")) {
			throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "The maximum account balance has been reached for wager " + siteTransaction.getAmount(), xhtml);
		}

		// Check for a wager limit and rerun
		if (wagers.containsKey("wageramount")) {
			// Only call the select event once
			if (rerunCount++ == 0) {
				siteTransaction.setAmount(wagers.get("wageramount"));
				ae.setAmount(wagers.get("wageramount"));
				ae.setActualamount(wagers.get("wageramount"));
				List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
				String key = checkMapKey("amt_");
				MAP_DATA.put(key, siteTransaction.getAmount());

				// Now check if selection needs to be sent as well
				if (siteTransaction.getSelectName() != null && siteTransaction.getOptionValue() != null) {
					MAP_DATA.put(siteTransaction.getSelectName(), siteTransaction.getOptionValue());
				}

				// Check for risk or win
				if ("1".equals(wagerType)) {
					key = checkMapKey("AmtType_");
					MAP_DATA.put(key, "2");
				} else {
					key = checkMapKey("AmtType_");
					MAP_DATA.put(key, "1");
				}

				String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
				LOGGER.debug("ActionName: " + actionName);

				// Setup the wager
				LOGGER.error("HTTP POST REQUEST: " + actionName);
				//xhtml = postSite(actionName, postNameValuePairs);
				LOGGER.error("HTML: " + xhtml);
				MAP_DATA = S411P.parseConfirmWager(xhtml);

				// Check for a wager limit and rerun
				if (MAP_DATA.containsKey("wageramount")) {
					throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The maximum wager amount has been reached " + siteTransaction.getAmount(), xhtml);
				}
				// Check for a wager limit and rerun
				if (MAP_DATA.containsKey("wagerminamount")) {
					throw new BatchException(BatchErrorCodes.MIN_WAGER_AMOUNT_NOT_REACHED, BatchErrorMessage.MIN_WAGER_AMOUNT_NOT_REACHED, "The minimum wager amount has NOT been reached $" + MAP_DATA.get("wagerminamount"), xhtml);
				}
				// Check for a wager limit and rerun
				if (MAP_DATA.containsKey("wagerbalanceexceeded")) {
					throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "The maximum account balance has been reached for wager " + siteTransaction.getAmount(), xhtml);
				}
			}
		} else {
			MAP_DATA = wagers;
		}

		// Get the action
		final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		String actionName = null;
//		MAP_DATA.remove("__VIEWSTATE");
		MAP_DATA.remove("ctl00$TimeZoneDesktop$ctl00$btnSetTimeZone");
		MAP_DATA.remove("ctl00$mainContent$btnContinueBottom");
		MAP_DATA.put("ctl00$mainContent$btnContinueTop", "Confirm your bet");
		MAP_DATA.put("ctl00$TimeZoneDesktop$ctl00$ddlTZone","24");
		MAP_DATA.put("ctl00$Feedback1$ctl00$txtComments","");

		actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());			
		LOGGER.debug("ActionName: " + actionName);

		// Process the wager
		if (processTransaction) {
			xhtml = postMobileSite(actionName, postNameValuePairs);
			LOGGER.debug("HTML: " + xhtml);
		}

		LOGGER.info("Exiting confirmWager()");
		return xhtml;
	}

	/**
	 * 
	 * @param username
	 * @param password
	 */
	protected void setUsernamePassword(String username, String password) {
		LOGGER.info("Entering setUsernamePassword()");

		// Setup the username/password data
		final Set<Entry<String, String>> indexs = MAP_DATA.entrySet();
		if (indexs != null && !indexs.isEmpty()) {
			final Iterator<Entry<String, String>> itr = indexs.iterator();
			while (itr != null && itr.hasNext()) {
				final Entry<String, String> values = itr.next();
				String key = values.getKey();
				String value = values.getValue();
				LOGGER.debug("KEY: " + key);
				LOGGER.debug("VALUE: " + value);
				if (key.contains("account") || key.contains("Account")) {
					MAP_DATA.put(key, username);
				} else if (key.contains("password") || key.contains("Password")) {
					MAP_DATA.put(key, password);
				}
			}
		}

		LOGGER.info("Exiting setUsernamePassword()");
	}

	/**
	 * 
	 * @param keySearch
	 * @return
	 */
	private String checkMapKey(String keySearch) {
		LOGGER.info("Entering checkMapKey()");
		String keyValue = null;

		// Setup the username/password data
		final Set<Entry<String, String>> indexs = MAP_DATA.entrySet();
		if (indexs != null && !indexs.isEmpty()) {
			final Iterator<Entry<String, String>> itr = indexs.iterator();
			while (itr != null && itr.hasNext()) {
				final Entry<String, String> values = itr.next();
				String key = values.getKey();
				String value = values.getValue();
				LOGGER.debug("KEY: " + key);
				LOGGER.debug("VALUE: " + value);

				if (key != null && key.contains(keySearch)) {
					keyValue = key;
				}
			}
		}

		LOGGER.info("Exiting checkMapKey()");
		return keyValue;
	}
}