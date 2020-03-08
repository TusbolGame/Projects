/**
 * 
 */
package com.ticketadvantage.services.dao.sites.wagershack;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.HttpClientUtils;
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
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class WagerShackMobileProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(WagerShackMobileProcessSite.class);
	private static final String PENDING_BETS = "OpenBets.aspx";
	private static final String SPORTS = "Sports.aspx";
	private static final String GET_SPORTS = "betting/getSports.asp";
	private static final String GET_LINES = "betting/getLinesbyLeague.asp";
	private static final String GET_STRAIGHT = "betting/getstraight.asp";
	private static final String DO_BET = "betting/doBet.asp";
	protected final WagerShackMobileParser WSMP = new WagerShackMobileParser();
	private boolean resetConnection;
	protected String gameId;
	protected Map<String, String> globalHiddenVariables;

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @param isMobile
	 * @param showRequestResponse
	 */
	public WagerShackMobileProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("WagerShackMobile", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering WagerShackMobileProcessSite()");

		// Setup the parser
		super.siteParser = WSMP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL", "**NFL**" };
		NFL_LINES_NAME = new String[] { "NFL", "PRESEASON NFL" };
		NFL_FIRST_SPORT = new String[] { "NFL", "**NFL**" };
		NFL_FIRST_NAME = new String[] { "NFL 1ST HALVES", "PRESEASON NFL 1ST HALVES" };
		NFL_SECOND_SPORT = new String[] { "NFL", "**NFL**" };
		NFL_SECOND_NAME = new String[] { "NFL 2ND HALVES" };
		NCAAF_LINES_SPORT = new String[] { "NCAA FB", "**CFB**" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL MEN", "NCAAF - EXTRA GAMES" };
		NCAAF_FIRST_SPORT = new String[] { "NCAA FB", "**CFB**" };
		NCAAF_FIRST_NAME = new String[] { "NCAA FB 1ST HALVES", "NCAAF - EXTRA GAMES 1ST HALVES" };
		NCAAF_SECOND_SPORT = new String[] { "NCAA FB", "**CFB**" };
		NCAAF_SECOND_NAME = new String[] { "NCAA FB 2ND HALVES", "NCAAF - EXTRA GAMES 2ND HALVES" };
		NBA_LINES_SPORT = new String[] { "BASKETBALL", "**BASKETBALL**" };
		NBA_LINES_NAME = new String[] { "NBA", "NBA PRESEASON", "NBA SUMMER LEAGUE" };
		NBA_FIRST_SPORT = new String[] { "BASKETBALL", "**BASKETBALL**" };
		NBA_FIRST_NAME = new String[] { "NBA 1ST HALVES" };
		NBA_SECOND_SPORT = new String[] { "BASKETBALL", "**BASKETBALL**" };
		NBA_SECOND_NAME = new String[] { "NBA 2ND HALVES" };
		NCAAB_LINES_SPORT = new String[] { "NCAA BK", "NCAAB" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL MEN", "NCAA BK MENS ADDED GAMES", "NCAA BK MENS EXTRA GAMES" };
		NCAAB_FIRST_SPORT = new String[] { "NCAA BK", "NCAAB" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BK MENS 1ST HALVES", "NCAA BK MENS ADDED GAMES 1ST HALVES", "NCAA BK MENS EXTRA GAMES 1ST HALVES" };
		NCAAB_SECOND_SPORT = new String[] { "NCAA BK", "NCAAB" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BK MENS 2ND HALVES", "NCAA BK MENS ADDED GAMES 2ND HALVES", "NCAA BK MENS EXTRA GAMES 2ND HALVES" }; 
		NHL_LINES_SPORT = new String[] { "HOCKEY", "**HOCKEY**" };
		NHL_LINES_NAME = new String[] { "NATIONAL HOCKEY LEAGUE" };
		NHL_FIRST_SPORT = new String[] { "HOCKEY", "**HOCKEY**" };
		NHL_FIRST_NAME = new String[] { "NHL PERIODS" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY", "**HOCKEY**" };
		NHL_SECOND_NAME = new String[] { "NHL PERIODS" };
		NHL_THIRD_SPORT = new String[] { "HOCKEY", "**HOCKEY**" };
		NHL_THIRD_NAME = new String[] { "NHL PERIODS" };
		WNBA_LINES_SPORT = new String[] { "BASKETBALL" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_FIRST_SPORT = new String[] { "BASKETBALL" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1ST HALVES" };
		WNBA_SECOND_SPORT = new String[] { "BASKETBALL" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2ND HALVES" };
		MLB_LINES_SPORT = new String[] { "BASEBALL" };
		MLB_LINES_NAME = new String[] { "MAJOR LEAGUE BASEBALL", "MLB GAME LINES", "EXHIBITION BASEBALL" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL" };
		MLB_FIRST_NAME = new String[] { "MLB 1ST FIVE INNINGS" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL" };
		MLB_SECOND_NAME = new String[] { "MLB 2ND HALF LINES" };
		MLB_THIRD_SPORT = new String[] { "BASEBALL" };
		MLB_THIRD_NAME = new String[] { "MLB 3RD HALF LINES" };

		LOGGER.info("Exiting WagerShackMobileProcessSite()");
	}

	/**
	 * 
	 * @param accountSoftware
	 * @param host
	 * @param username
	 * @param password
	 * @param isMobile
	 * @param showRequestResponse
	 */
	public WagerShackMobileProcessSite(String accountSoftware, String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super(accountSoftware, host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering WagerShackMobileProcessSite()");
		LOGGER.info("Exiting WagerShackMobileProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
//				{ "http://mobile.wagershack.com", "RK230", "PS", "500", "500", "500", "Los Angeles", "PT"}
//				{ "http://engine.betgotham.com/", "aj403", "903", "500", "500", "500", "Chicago", "ET"}
//				{ "http://playitez.com", "dg613", "white", "500", "500", "500", "None", "PT"}
//				{ "http://mobile.gowagerhub.com", "CLUB05", "YELLOW", "500", "500", "500", "None", "ET"}
//				{ "http://pty2030.com", "CLUB05", "YELLOW", "500", "500", "500", "None", "ET"}
//				{ "http://mobile.2playla.com/", "h6321", "cubs$", "500", "500", "500", "None", "CT"}
//				{"http://old.gomobilewager.com", "h6321", "cubs$", "500", "500", "500", "None", "ET"}
//				{ "http://mobile.2playla.com", "h6321", "CUBS$", "100", "100", "100", "None", "ET"}
//				{ "http://mobile.wagerroom.com", "cr1001", "BLUE", "100", "100", "100", "None", "ET"}
//				{ "http://mobile.wagerroom.com", "CR1007", "PINK", "0", "0", "0", "None", "ET"}
//				{ "http://mobile.wagerroom.com", "CR1004", "PURPLE", "0", "0", "0", "None", "ET"}
//				{ "https://mobile.pigwagers.ag", "QXAV228", "jake", "0", "0", "0", "None", "ET"}
//				{ "http://mobile.wagerroom.com", "CR1003", "GREEN", "0", "0", "0", "None", "ET"}
//				{ "http://mobile.wagerroom.com", "xCR1002", "CUCUMBER", "0", "0", "0", "None", "ET"}
//				{ "http://mobile.wagerroom.com", "xCR1004", "PURPLE", "0", "0", "0", "None", "ET"}
//				{ "http://engine.betgotham.com", "fw436", "WHITE", "0", "0", "0", "None", "ET"}
//				{ "http://mobile.wagerroom.com", "xjym08", "TURTLE", "0", "0", "0", "None", "ET"}
//				{ "http://mobile.wagerroom.com", "xCR1002", "CUCUMBER", "0", "0", "0", "None", "ET"}
//				{ "http://mobile.wagerroom.com", "xCR1003", "BALL", "0", "0", "0", "None", "ET"}
//				{ "http://mobile.wagerroom.com", "xCR1005", "WALK", "0", "0", "0", "None", "ET"}
//				{ "http://mobile.wagerroom.com", "xCR1009", "BANANA", "0", "0", "0", "None", "ET"}
//				{ "https://engine.action247.ag", "cal334", "NNLAX", "0", "0", "0", "None", "ET"}
  				{ "http://mobile.wagerroom.com", "xCR1001", "PIZZA", "0", "0", "0", "None", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final WagerShackMobileProcessSite processSite = new WagerShackMobileProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], true, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = true;
			    processSite.timezone = TDSITES[0][7];
			    processSite.testTotal(processSite, TDSITES);
			    processSite.loginToSite(TDSITES[i][1], TDSITES[i][2]);
				Set<PendingEvent> pendingEvents = processSite.getPendingBets(TDSITES[i][0], TDSITES[i][1], new Object());
				Iterator<PendingEvent> itr = pendingEvents.iterator();
				while (itr.hasNext()) {
					PendingEvent pe = itr.next();
					LOGGER.error("PendingEvent: " + pe);
				}
/*
				  final Map<String, String> map = processSite.getMenuFromSite("nbalines",xhtml); 
				  LOGGER.debug("map: " + map);
				  
				  xhtml = processSite.selectSport("nbalines");
				  LOGGER.debug("XHTML: " + xhtml);
				  
				  final Iterator<EventPackage> ep = processSite.parseGamesOnSite("nbalines", xhtml);
				  
				  TotalRecordEvent tre = new TotalRecordEvent();
				  tre.setTotalinputfirstone("171.5"); 
				  tre.setTotalinputjuicefirstone("140");
				  tre.setTotaljuiceplusminusfirstone("-");
				  tre.setEventname("NBA #519 HOU ROCKETS vrs TOR RAPTORS o229.5 -120.0 for Game");
				  tre.setEventtype("total"); 
				  tre.setSport("ncaablines"); 
				  tre.setUserid(new Long(6)); 
				  tre.setEventid(new Integer(519)); 
				  tre.setEventid1(new Integer(519)); 
				  tre.setEventid2(new Integer(520)); 
				  tre.setRotationid(519);
*/			 
/*
				final MlRecordEvent mre = new MlRecordEvent();
				mre.setMlinputfirstone("+");
				mre.setMlplusminusfirstone("+");
				mre.setId(new Long(541));
				mre.setEventname("#541 PORTLAND TRAIL BLAZERS +175");
				mre.setEventtype("ml");
				mre.setSport("nbalines");
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(541));
				mre.setEventid1(new Integer(541));
				mre.setEventid2(new Integer(542));
				mre.setRotationid(new Integer(541));
*/
/*			  
				// Step 9 - Find event
				final EventPackage eventPackage = processSite.findEvent(ep, tre); 
				if (eventPackage == null) { 
					throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE,
							BatchErrorMessage.GAME_NOT_AVAILABLE, 
							"Game cannot be found on site for " + tre.getEventname(), xhtml); 
				} 
				LOGGER.error("eventPackage: " + eventPackage);
*/
			}
		} catch (Throwable t) {
			LOGGER.info("Throwable: ", t);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#getPendingBets(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public Set<PendingEvent> getPendingBets(String accountName, String accountId, Object anythingObject) throws BatchException {
		LOGGER.info("Entering getPendingBets()");
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingWagers = null;
		
		// Get time
		final Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
		int hour = date.get(Calendar.HOUR_OF_DAY);

		// Check if we are between 6 am CT and 11 pm CT
		if (hour < 23 && hour > 5) {
			if (this.resetConnection) {
				LOGGER.error("Resetting the connection for " + accountName + " " + accountId);
				HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
				this.httpClientWrapper.setupHttpClient(proxyName);
				this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
				this.resetConnection = false;
			}
		} else {
			this.resetConnection = true;
		}

		try {
			// Get the pending bets data
			String xhtml = getSite(super.populateUrl(PENDING_BETS));
			LOGGER.debug("xhtml: " + xhtml);

			if (xhtml != null && xhtml.contains("Open Bets")) {
				pendingWagers = WSMP.parsePendingBets(xhtml, accountName, accountId);
				LOGGER.debug("PendingWagers: " + pendingWagers);
			} else if (xhtml != null && xhtml.contains("No pending wagers found")) {
				// Do nothing
				LOGGER.debug("No Open Bets");
				pendingWagers = new HashSet<PendingEvent>();
			} else {
				LOGGER.debug("No pending wagers found for " + accountName + " " + accountId);
				if (xhtml != null && xhtml.contains("loginform")) {
					this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
					xhtml = getSite(super.populateUrl(PENDING_BETS));
					pendingWagers = WSMP.parsePendingBets(xhtml, accountName, accountId);
				}

				if (pendingWagers != null && pendingWagers.isEmpty()) {
					pendingWagers = null;
				}
			}
		} catch (BatchException be) {
			if (be.getErrormessage().contains("BAD GATEWAY")) {
				LOGGER.error("Resetting the connection for " + accountName + " " + accountId);
				HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
				this.httpClientWrapper.setupHttpClient(proxyName);
				this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
				String xhtml = getSite(super.populateUrl(PENDING_BETS));
				pendingWagers = WSMP.parsePendingBets(xhtml, accountName, accountId);
			}

			if (pendingWagers != null && pendingWagers.isEmpty()) {
				pendingWagers = null;
			}
		}

		LOGGER.info("Exiting getPendingBets()");
		return pendingWagers;
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
		WSMP.setTimezone(timezone);
		httpClientWrapper.setWebappname("");

		System.out.println();
		// Get the home page
		String xhtml = getMobileSite(this.httpClientWrapper.getHost());
		LOGGER.debug("XHTML: " + xhtml);

		// Get the index info
		MAP_DATA = WSMP.parseIndex(xhtml);

		// Setup the Username
		if (MAP_DATA.containsKey("ctl00$MainContent$ctlLogin$_UserName")) {
			MAP_DATA.put("ctl00$MainContent$ctlLogin$_UserName", username);	
		} else if (MAP_DATA.containsKey("account")) {
			MAP_DATA.put("account", username);
		}

		// Setup the Password
		if (MAP_DATA.containsKey("ctl00$MainContent$ctlLogin$_Password")) {
			MAP_DATA.put("ctl00$MainContent$ctlLogin$_Password", password);	
		} else if (MAP_DATA.containsKey("password")) {
			MAP_DATA.put("password", password.toUpperCase());
		}

		LOGGER.debug("Map: " + MAP_DATA);

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, httpClientWrapper.getWebappname());

		// Call the login
		xhtml = authenticateMobile(actionLogin, postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		// Parse login
		MAP_DATA = WSMP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Setup MAP_DATA
		MAP_DATA.clear();
		LOGGER.debug("MAP_DATA.clear(): " + MAP_DATA);
		MAP_DATA.put("Account", username);
		MAP_DATA.put("password", password.toUpperCase());
		LOGGER.debug("AFTER MAP_DATA.clear(): " + MAP_DATA);
		postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, httpClientWrapper.getWebappname());

		// Call the login
		xhtml = authenticateMobile(super.populateUrl("/default.aspx"), postValuePairs);
	
		// Setup the webapp name
		httpClientWrapper.setWebappname("wager");

		// Get the Sports.aspx
		xhtml = getMobileSite(populateUrl(SPORTS));
		LOGGER.debug("XHTML: " + xhtml);

		// Get the hidden input fields
		MAP_DATA = WSMP.parseSports(xhtml);
		copyGlobalVariables();

		xhtml = getMobileSite(populateUrl(GET_SPORTS + "?pid=" + MAP_DATA.get("pid") + "&idp=" + MAP_DATA.get("idp") + "&idlt=" + MAP_DATA.get("idlt") + "&idlan=" + MAP_DATA.get("idlan") + "&bid=" + MAP_DATA.get("idbook") + "&wt=0"));
		LOGGER.debug("XHTML: " + xhtml);

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
			// Call the Game selecti on
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
				throw be;
			} else if (be.getErrorcode() == BatchErrorCodes.WAGER_HAS_EXPIRED) {
				throw be;
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

		final String chk = MAP_DATA.get("chk");
		
		// Process the setup page
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		postValuePairs.add(new BasicNameValuePair("aid", globalHiddenVariables.get("aid")));
		postValuePairs.add(new BasicNameValuePair("bid", globalHiddenVariables.get("idbook")));
		postValuePairs.add(new BasicNameValuePair("bpo", "0"));
		postValuePairs.add(new BasicNameValuePair("idc", globalHiddenVariables.get("idc")));
		postValuePairs.add(new BasicNameValuePair("idl", chk));
		postValuePairs.add(new BasicNameValuePair("idlan", globalHiddenVariables.get("idlan")));
		postValuePairs.add(new BasicNameValuePair("idls", globalHiddenVariables.get("idls")));
		postValuePairs.add(new BasicNameValuePair("idlt", globalHiddenVariables.get("idlt")));
		postValuePairs.add(new BasicNameValuePair("idp", globalHiddenVariables.get("idp")));
		postValuePairs.add(new BasicNameValuePair("idpl", globalHiddenVariables.get("idpl")));
		postValuePairs.add(new BasicNameValuePair("idwt", "0"));
		postValuePairs.add(new BasicNameValuePair("mlbl", globalHiddenVariables.get("mlbl")));
		postValuePairs.add(new BasicNameValuePair("nhll", globalHiddenVariables.get("nhll")));
		postValuePairs.add(new BasicNameValuePair("pid", globalHiddenVariables.get("pid")));
		postValuePairs.add(new BasicNameValuePair("utc", globalHiddenVariables.get("utc")));
		postValuePairs.add(new BasicNameValuePair("wt", "0"));

		// Get site page
		// http://mobile.gowagerhub.com/wager/Sports.aspx?WT=0&lid=7
		String xhtml = getMobileSite(populateUrl(SPORTS) + "?WT=0&lid=" + chk);
		LOGGER.debug("HTML: " + xhtml);

		xhtml = postMobileSite(populateUrl(GET_LINES), postValuePairs);
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

		final SiteTransaction siteTransaction = new SiteTransaction();

		LOGGER.info("Exiting createSiteTransaction()");
		return siteTransaction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectEvent(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event,
			AccountEvent ae) throws BatchException {
		LOGGER.info("Entering selectEvent()");
		SiteTeamPackage teamPackage = null;
		String idValue = null;

		// Get the appropriate record event
		if (event instanceof SpreadRecordEvent) {			
			teamPackage = SiteWagers.setupTeam(SiteWagers.determineSpreadData((SpreadRecordEvent)event), eventPackage);
			LOGGER.debug("teamPackage: " + teamPackage);
			if (teamPackage != null && 
				teamPackage.getGameSpreadInputId() != null &&
				teamPackage.getGameSpreadInputId().length() > 0) {
				idValue = teamPackage.getGameSpreadInputId();
			} else {
				throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION, BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No spread available for this game");
			}
		} else if (event instanceof TotalRecordEvent) {
			teamPackage = SiteWagers.setupTeam(SiteWagers.determineTotalData((TotalRecordEvent)event), eventPackage);
			LOGGER.debug("teamPackage: " + teamPackage);
			if (teamPackage != null && 
				teamPackage.getGameTotalInputId() != null && 
				teamPackage.getGameTotalInputId().length() > 0) {
				idValue = teamPackage.getGameTotalInputId();
			} else {
				throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION, BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No total available for this game");
			}
		} else if (event instanceof MlRecordEvent) {
			teamPackage = SiteWagers.setupTeam(SiteWagers.determineMoneyLineData((MlRecordEvent)event), eventPackage);
			LOGGER.debug("teamPackage: " + teamPackage);
			if (teamPackage != null && 
				teamPackage.getGameMLInputId() != null &&
				teamPackage.getGameMLInputId().length() > 0) {
				idValue = teamPackage.getGameMLInputId();
			} else {
				throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION, BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No moneyline available for this game");
			}
		}

		//
		// https://playitez.com/wager/betslip/getstraight.asp?pid=6801&idp=2775&idc=209198423&b=4_1961452_0_175%2C&c=1&amt=0
		//
		globalHiddenVariables.put("b", idValue);

		final StringBuffer sb = new StringBuffer(200);
		sb.append(GET_STRAIGHT);
		sb.append("?pid=").append(globalHiddenVariables.get("pid"));
		sb.append("&idp=").append(globalHiddenVariables.get("idp"));
		sb.append("&idc=").append(globalHiddenVariables.get("idc"));
		sb.append("&b=").append(idValue + "_0_0");
		sb.append("&c=").append("1");
		sb.append("&amt=0");
		sb.append("&wt=0");

		// Copy b parameter
		globalHiddenVariables.put("b", idValue);

		// Setup the wager
		String xhtml = getMobileSite(populateUrl(sb.toString()));
		LOGGER.debug("XHTML: " + xhtml);

		// Check for line changes
		if (xhtml.contains("The line changed for one")) {
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed error", xhtml);
		}

		LOGGER.info("Exiting selectEvent()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		SiteTeamPackage teamPackage = null;

		MAP_DATA.clear();
		gamesXhtml = xhtml;
		if (event instanceof SpreadRecordEvent) {
			teamPackage = (SiteTeamPackage)SiteWagers.setupTeam(SiteWagers.determineSpreadData((SpreadRecordEvent)event), eventPackage);
			MAP_DATA = WSMP.parseEventSelection(xhtml, teamPackage, "spread");
		} else if (event instanceof TotalRecordEvent) {
			teamPackage = (SiteTeamPackage)SiteWagers.setupTeam(SiteWagers.determineTotalData((TotalRecordEvent)event), eventPackage);
			MAP_DATA = WSMP.parseEventSelection(xhtml, teamPackage, "total");
		} else if (event instanceof MlRecordEvent) {
			teamPackage = (SiteTeamPackage)SiteWagers.setupTeam(SiteWagers.determineMoneyLineData((MlRecordEvent)event), eventPackage);
			MAP_DATA = WSMP.parseEventSelection(xhtml, teamPackage, "ml");
		}
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		LOGGER.info("Exiting parseEventSelection()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#completeTransaction(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering completeTransaction()");
		String xhtml = null;

		try {
			if (processTransaction) {
				// Check if we have exceeded
				if (MAP_DATA.containsKey("wagerbalanceexceeded")) {
					throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "The maximum account balance has been reached for wager " + siteTransaction.getAmount(), xhtml);
				}

				// Process the wager transaction
				xhtml = processWager(eventPackage, event, ae, event.getWtype());

				// Parse any errors
				WSMP.parseWagerTypes(xhtml);

				// Confirm the wager
				xhtml = confirmWager(xhtml, siteTransaction, eventPackage, event, ae, event.getWtype());
				
				if (xhtml.contains("The line changed for one")) {
					xhtml = processLineChange(xhtml, event, ae);
				}
			}
		} catch (BatchException be) {
			LOGGER.error("Exception completing transaction account event " + ae + " event " + event, be);
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
		String ticketNumber = null;

		try {
			ticketNumber = WSMP.parseTicketNumber(xhtml);
			LOGGER.debug("ticketNumber: " + ticketNumber);
		} catch (BatchException be) {
			throw be;
		}

		LOGGER.info("Exiting parseTicketTransaction()");
		return ticketNumber;		
	}
	
	/**
	 * 
	 * @param eventPackage
	 * @param event
	 * @param ae
	 * @param wagerType
	 * @return
	 * @throws BatchException
	 */
	protected String processWager(EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae, String wagerType) throws BatchException {
		LOGGER.info("Entering processWager()");
		SiteTransaction siteTransaction = null;
		String siteAmount = null;

		// Get the spread transaction
		if (event instanceof SpreadRecordEvent) {
			siteTransaction = getSpreadTransaction((SpreadRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.error("siteAmount: " + siteAmount);
			Double sAmount = Double.parseDouble(siteAmount);
			if (sAmount != null) {
				SiteEventPackage siteEventPackage = (SiteEventPackage)eventPackage;
				LOGGER.debug("SiteEventPackage: " + siteEventPackage);
				if (siteTransaction.getRiskorwin().intValue() == 1 && siteEventPackage.getSpreadMax() != null) {
					if (sAmount.doubleValue() > siteEventPackage.getSpreadMax().intValue()) {
						siteAmount = siteEventPackage.getSpreadMax().toString();
					}
				} else if (siteTransaction.getRiskorwin().intValue() == 2) {
					Double wagerAmount = Double.valueOf(siteAmount);
					float spreadJuice = ae.getSpreadjuice();
					Double risk = wagerAmount * (100 / spreadJuice);
					risk = round(risk.doubleValue(), 2);
					LOGGER.error("Risk: " + risk);
					if (siteEventPackage.getSpreadMax() != null && risk.doubleValue() > siteEventPackage.getSpreadMax().intValue()) {
						siteAmount = siteEventPackage.getSpreadMax().toString();
					} else {
						siteAmount = risk.toString();
					}
				}
			}
		} else if (event instanceof TotalRecordEvent) {
			siteTransaction = getTotalTransaction((TotalRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.error("siteAmount: " + siteAmount);
			Double tAmount = Double.parseDouble(siteAmount);
			if (tAmount != null) {
				SiteEventPackage siteEventPackage = (SiteEventPackage)eventPackage;
				LOGGER.debug("SiteEventPackage: " + siteEventPackage);
				if (siteTransaction.getRiskorwin().intValue() == 1 && siteEventPackage.getTotalMax() != null) {
					if (tAmount.doubleValue() > siteEventPackage.getTotalMax().intValue()) {
						siteAmount = siteEventPackage.getTotalMax().toString();
					}
				} else if (siteTransaction.getRiskorwin().intValue() == 2) {
					Double wagerAmount = Double.valueOf(siteAmount);
					float totalJuice = ae.getTotaljuice();
					Double risk = wagerAmount * (100 / totalJuice);
					risk = round(risk.doubleValue(), 2);
					LOGGER.error("Risk: " + risk);
					if (siteEventPackage.getTotalMax() != null && risk.doubleValue() > siteEventPackage.getTotalMax().intValue()) {
						siteAmount = siteEventPackage.getTotalMax().toString();
					} else {
						siteAmount = risk.toString();
					}
				}
			}
		} else if (event instanceof MlRecordEvent) {
			siteTransaction = getMlTransaction((MlRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.error("siteAmount: " + siteAmount);
			Double mAmount = Double.parseDouble(siteAmount);
			if (mAmount != null) {
				SiteEventPackage siteEventPackage = (SiteEventPackage)eventPackage;
				LOGGER.debug("SiteEventPackage: " + siteEventPackage);
				if (siteTransaction.getRiskorwin().intValue() == 1 && siteEventPackage.getMlMax() != null) {
					if (mAmount.doubleValue() > siteEventPackage.getMlMax().intValue()) {
						siteAmount = siteEventPackage.getMlMax().toString();
					}
				} else if (siteTransaction.getRiskorwin().intValue() == 2) {
					Double wagerAmount = Double.valueOf(siteAmount);
					float mlJuice = ae.getMljuice();
					Double risk = wagerAmount * (100 / mlJuice);
					risk = round(risk.doubleValue(), 2);
					LOGGER.error("Risk: " + risk);
					if (siteEventPackage.getMlMax() != null && risk.doubleValue() > siteEventPackage.getMlMax().intValue()) {
						siteAmount = siteEventPackage.getMlMax().toString();
					} else {
						siteAmount = risk.toString();
					}
				}
			}
		}

		siteAmount = siteAmount.replace(".00", "").replace(".0", "");
		LOGGER.debug("siteAmount: " + siteAmount);
		ae.setActualamount(siteAmount);

		// Setup the wager
		String xhtml = null;
		if (processTransaction) {
			// Now check if selection needs to be sent as well
			gameId = globalHiddenVariables.get("b");
			if (gameId != null) {
				final String sport = event.getSport();
				final String eventtype = event.getEventtype();
				if (sport != null && sport.contains("mlb") && eventtype.equals("ml")) {
					gameId += ("_" + siteAmount + "_3_0");
				} else {
					gameId += ("_" + siteAmount + "_0_0");
				}
			}

			//
			// https://playitez.com/wager/betslip/getstraight.asp?pid=6801&idp=2775&idc=209198423&b=4_1961452_0_175_0_3%2C&c=1&amt=1&fp=0
			//
			final StringBuffer sb = new StringBuffer(200);
			sb.append(GET_STRAIGHT);
			sb.append("?pid=").append(globalHiddenVariables.get("pid"));
			sb.append("&idp=").append(globalHiddenVariables.get("idp"));
			sb.append("&idc=").append(globalHiddenVariables.get("idc"));
			sb.append("&b=").append(gameId + "%2C");
			sb.append("&c=").append("1");
			sb.append("&amt=1");
			sb.append("&fp=0");

			// Copy b
			globalHiddenVariables.put("b", gameId);

			// Setup the wager
			xhtml = getMobileSite(populateUrl(sb.toString()));
			LOGGER.debug("XHTML: " + xhtml);
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
	protected String confirmWager(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae, String wagerType) throws BatchException {
		LOGGER.info("Entering confirmWager()");

		// Process the wager
		if (processTransaction) {
			// https://playitez.com/wager/betslip/doBet.asp?p=BLUE&idlt=4
			// http://mobile.gowagerhub.com/wager/betting/doBet.asp?idlt=237&hash=8A568E5F41B7E4DA88FE5C4A00AAD34E&pid=371897&wt=0
			final StringBuffer sb = new StringBuffer(200);
			sb.append(DO_BET);
			sb.append("?pid=").append(globalHiddenVariables.get("pid"));
			sb.append("&hash=").append(globalHiddenVariables.get("hash"));
			sb.append("&idlt=").append(globalHiddenVariables.get("idlt"));
			sb.append("&wt=0");
				
			// Setup the wager
			xhtml = getMobileSite(populateUrl(sb.toString()));
			LOGGER.debug("XHTML: " + xhtml);
		}

		LOGGER.info("Exiting confirmWager()");
		return xhtml;
	}

	/**
	 * 
	 */
	private void copyGlobalVariables() {
		LOGGER.info("Entering copyGlobalVariables()");

		globalHiddenVariables = new HashMap<String, String>();
		Iterator<String> keys = MAP_DATA.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			String value = MAP_DATA.get(key);
			globalHiddenVariables.put(key, value);
		}

		LOGGER.info("Exiting copyGlobalVariables()");
	}

	/**
	 * 
	 * @param xhtml
	 * @param event
	 * @param ae
	 * @return
	 * @throws BatchException
	 */
	private String processLineChange(String xhtml, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processLineChange()");

		final Map<String, String> lineChanges = WSMP.processLineChange(xhtml);
		if (lineChanges != null && !lineChanges.isEmpty()) {
			if (event instanceof SpreadRecordEvent) {
				final SpreadRecordEvent sre = (SpreadRecordEvent)event;
				if (determineSpreadLineChange(sre, ae, lineChanges)) {
					//
					// https://playitez.com/wager/betslip/getstraight.asp?pid=6801&idc=209198423&b=4_1961452_0_175_0_3%2C&c=1&amt=1&fp=0
					//
					final StringBuffer sb = new StringBuffer(200);
					sb.append(GET_STRAIGHT);
					sb.append("?pid=").append(globalHiddenVariables.get("pid"));
					sb.append("&idc=").append(globalHiddenVariables.get("idc"));
					sb.append("&bets=").append(lineChanges.get("newurl"));
					sb.append("&p=").append(lineChanges.get("p"));
					sb.append("&wt=").append(lineChanges.get("wt"));
					sb.append("&idwt=").append(lineChanges.get("idwt"));
					sb.append("&idlt=").append(globalHiddenVariables.get("idlt"));

					// https://playitez.com/wager/betslip/doBet.asp?p=BLUE&idlt=4
//					final StringBuffer sb = new StringBuffer(200);
//					sb.append(DO_BET);
//					sb.append("?p=").append(this.httpClientWrapper.getPassword().toUpperCase());
//					sb.append("&idlt=").append(globalHiddenVariables.get("idlt"));
//					sb.append("&b=").append(globalHiddenVariables.get("newurl"));

					// Setup the wager
					xhtml = getMobileSite(populateUrl(sb.toString()));
					LOGGER.debug("XHTML: " + xhtml);
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			} else if (event instanceof TotalRecordEvent) {
				final TotalRecordEvent tre = (TotalRecordEvent)event;
				if (tre.getTotalinputfirstone() != null && tre.getTotalinputfirstone().length() > 0) {
					lineChanges.put("valueindicator", "o");					
				} else {
					lineChanges.put("valueindicator", "u");
				}
				if (determineTotalLineChange(tre, ae, lineChanges)) {
					final StringBuffer sb = new StringBuffer(200);
					sb.append(GET_STRAIGHT);
					sb.append("?pid=").append(globalHiddenVariables.get("pid"));
					sb.append("&idc=").append(globalHiddenVariables.get("idc"));
					sb.append("&bets=").append(lineChanges.get("newurl"));
					sb.append("&p=").append(lineChanges.get("p"));
					sb.append("&wt=").append(lineChanges.get("wt"));
					sb.append("&idwt=").append(lineChanges.get("idwt"));
					sb.append("&idlt=").append(globalHiddenVariables.get("idlt"));

					// https://playitez.com/wager/betslip/doBet.asp?p=BLUE&idlt=4
//					final StringBuffer sb = new StringBuffer(200);
//					sb.append(DO_BET);
//					sb.append("?p=").append(this.httpClientWrapper.getPassword().toUpperCase());
//					sb.append("&idlt=").append(globalHiddenVariables.get("idlt"));
//					sb.append("&b=").append(globalHiddenVariables.get("newurl"));

					// Setup the wager
					xhtml = getMobileSite(populateUrl(sb.toString()));
					LOGGER.debug("XHTML: " + xhtml);
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			} else if (event instanceof MlRecordEvent) {
				final MlRecordEvent mre = (MlRecordEvent)event;
				if (determineMlLineChange(mre, ae, lineChanges)) {
					// https://playitez.com/wager/betslip/doBet.asp?p=BLUE&idlt=4
					final StringBuffer sb = new StringBuffer(200);
					sb.append(GET_STRAIGHT);
					sb.append("?pid=").append(globalHiddenVariables.get("pid"));
					sb.append("&idc=").append(globalHiddenVariables.get("idc"));
					sb.append("&bets=").append(lineChanges.get("newurl"));
					sb.append("&p=").append(lineChanges.get("p"));
					sb.append("&wt=").append(lineChanges.get("wt"));
					sb.append("&idwt=").append(lineChanges.get("idwt"));
					sb.append("&idlt=").append(globalHiddenVariables.get("idlt"));

					// https://playitez.com/wager/betslip/doBet.asp?p=BLUE&idlt=4
//					final StringBuffer sb = new StringBuffer(200);
//					sb.append(DO_BET);
//					sb.append("?p=").append(this.httpClientWrapper.getPassword().toUpperCase());
//					sb.append("&idlt=").append(globalHiddenVariables.get("idlt"));
//					sb.append("&b=").append(globalHiddenVariables.get("newurl"));

					// Setup the wager
					xhtml = getMobileSite(populateUrl(sb.toString()));
					LOGGER.debug("XHTML: " + xhtml);
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			}
		}

		LOGGER.info("Exiting processLineChange()");
		return xhtml;
	}
}