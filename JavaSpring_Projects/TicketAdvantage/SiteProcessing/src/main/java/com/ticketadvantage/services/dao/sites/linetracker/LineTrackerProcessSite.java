/**
 * 
 */
package com.ticketadvantage.services.dao.sites.linetracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
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
public class LineTrackerProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(LineTrackerProcessSite.class);
	private static final String USERID = "txtAccessOfCode";
	private static final String PASSID = "txtAccessOfPassword";
	private static final String CONFIRM_PASSWORD = "ctl00$cphWorkArea$txtConfirmPassword";
	private static final String PENDING_BETS = "PendingsWagers.aspx";
	protected final LineTrackerParser LTP = new LineTrackerParser();
	private int rerunCount = 0;
	private boolean resetConnection;
	private int count;
	private int bigcounter;

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 */
	public LineTrackerProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("LineTracker", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering LineTrackerProcessSite()");

		// Setup the parser
		this.siteParser = LTP;

		// Menu items
		NFL_LINES_NAME = new String[] { "NFL" };
		NFL_LINES_SPORT = new String[] { "Football" };
		NFL_FIRST_NAME = new String[] { "NFL 1st Half" };
		NFL_FIRST_SPORT = new String[] { "Football" };
		NFL_SECOND_NAME = new String[] { "NFL 2nd Half" };
		NFL_SECOND_SPORT = new String[] { "Football" };
		NCAAF_LINES_NAME = new String[] { "NCAA", "NCAA - Extra" };
		NCAAF_LINES_SPORT = new String[] { "Football" };
		NCAAF_FIRST_NAME = new String[] { "NCAA 1st Half", "NCAA - Extra 1st Half" };
		NCAAF_FIRST_SPORT = new String[] { "Football" };
		NCAAF_SECOND_NAME = new String[] { "NCAA 2nd Half" };
		NCAAF_SECOND_SPORT = new String[] { "Football" };
		// NBA_LINES_NAME = new String[] { "NBA", "NBA - Preseason" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_LINES_SPORT = new String[] { "Basketball" };
		NBA_FIRST_NAME = new String[] { "NBA 1st Half" };
		NBA_FIRST_SPORT = new String[] { "Basketball" };
		NBA_SECOND_NAME = new String[] { "NBA 2nd Half" };
		NBA_SECOND_SPORT = new String[] { "Basketball" };
		NCAAB_LINES_NAME = new String[] { "NCAA", "NCAA - Added / Extra" };
		NCAAB_LINES_SPORT = new String[] { "Basketball" };
		NCAAB_FIRST_NAME = new String[] { "NCAA 1st Half", "NCAA - Added / Extra 1st Half" };
		NCAAB_FIRST_SPORT = new String[] { "Basketball" };
		NCAAB_SECOND_NAME = new String[] { "NCAA 2nd Half", "NCAA - Added / Extra 2nd Half" };
		NCAAB_SECOND_SPORT = new String[] { "Basketball" };
		NHL_LINES_NAME = new String[] { "NHL", "NHL - Added / Extra" };
		NHL_LINES_SPORT = new String[] { "Hockey" };
		NHL_FIRST_NAME = new String[] { "NHL 1st Half", "NHL - Added / Extra 1st Half" };
		NHL_FIRST_SPORT = new String[] { "Hockey" };
		NHL_SECOND_NAME = new String[] { "NHL 2nd Half", "NHL - Added / Extra 2nd Half" };
		NHL_SECOND_SPORT = new String[] { "Hockey" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "Basketball" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_FIRST_SPORT = new String[] { "Basketball" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		WNBA_SECOND_SPORT = new String[] { "Basketball" };
		MLB_LINES_NAME = new String[] { "MLB", "MLB Exhibition" };
		MLB_LINES_SPORT = new String[] { "Baseball" };
		MLB_FIRST_NAME = new String[] { "MLB First 5 Innings" };
		MLB_FIRST_SPORT = new String[] { "Baseball" };
		MLB_SECOND_NAME = new String[] { "MLB 2nd Half" };
		MLB_SECOND_SPORT = new String[] { "Baseball" };
		MLB_SECOND_NAME = new String[] { "MLB 3rd Half" };
		MLB_SECOND_SPORT = new String[] { "Baseball" };

		LOGGER.info("Exiting LineTrackerProcessSite()");
	}

	/**
	 * 
	 * @param siteType
	 * @param host
	 * @param username
	 * @param password
	 * @param isMobile
	 * @param showRequestResponse
	 */
	public LineTrackerProcessSite(String siteType, String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super(siteType, host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering LineTrackerProcessSite()");

		// Setup the parser
		this.siteParser = LTP;

		// Menu items
		NFL_LINES_NAME = new String[] { "NFL" };
		NFL_LINES_SPORT = new String[] { "Football" };
		NFL_FIRST_NAME = new String[] { "NFL 1st Half" };
		NFL_FIRST_SPORT = new String[] { "Football" };
		NFL_SECOND_NAME = new String[] { "NFL 2nd Half" };
		NFL_SECOND_SPORT = new String[] { "Football" };
		NCAAF_LINES_NAME = new String[] { "NCAA" };
		NCAAF_LINES_SPORT = new String[] { "Football" };
		NCAAF_FIRST_NAME = new String[] { "NCAA 1st Half" };
		NCAAF_FIRST_SPORT = new String[] { "Football" };
		NCAAF_SECOND_NAME = new String[] { "NCAA 2nd Half" };
		NCAAF_SECOND_SPORT = new String[] { "Football" };
		// NBA_LINES_NAME = new String[] { "NBA", "NBA - Preseason" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_LINES_SPORT = new String[] { "Basketball" };
		NBA_FIRST_NAME = new String[] { "NBA 1st Half" };
		NBA_FIRST_SPORT = new String[] { "Basketball" };
		NBA_SECOND_NAME = new String[] { "NBA 2nd Half" };
		NBA_SECOND_SPORT = new String[] { "Basketball" };
		NCAAB_LINES_NAME = new String[] { "NCAA", "NCAA - Added / Extra" };
		NCAAB_LINES_SPORT = new String[] { "Basketball" };
		NCAAB_FIRST_NAME = new String[] { "NCAA 1st Half", "NCAA - Added / Extra 1st Half" };
		NCAAB_FIRST_SPORT = new String[] { "Basketball" };
		NCAAB_SECOND_NAME = new String[] { "NCAA 2nd Half", "NCAA - Added / Extra 2nd Half" };
		NCAAB_SECOND_SPORT = new String[] { "Basketball" };
		NHL_LINES_NAME = new String[] { "NHL", "NHL - Added / Extra" };
		NHL_LINES_SPORT = new String[] { "Hockey" };
		NHL_FIRST_NAME = new String[] { "NHL 1st Half", "NHL - Added / Extra 1st Half" };
		NHL_FIRST_SPORT = new String[] { "Hockey" };
		NHL_SECOND_NAME = new String[] { "NHL 2nd Half", "NHL - Added / Extra 2nd Half" };
		NHL_SECOND_SPORT = new String[] { "Hockey" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "Basketball" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_FIRST_SPORT = new String[] { "Basketball" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		WNBA_SECOND_SPORT = new String[] { "Basketball" };
		MLB_LINES_NAME = new String[] { "MLB" };
		MLB_LINES_SPORT = new String[] { "Baseball" };
		MLB_FIRST_NAME = new String[] { "MLB First 5 Innings" };
		MLB_FIRST_SPORT = new String[] { "Baseball" };
		MLB_SECOND_NAME = new String[] { "MLB 2nd Half" };
		MLB_SECOND_SPORT = new String[] { "Baseball" };
		MLB_SECOND_NAME = new String[] { "MLB 3rd Half" };
		MLB_SECOND_SPORT = new String[] { "Baseball" };

		LOGGER.info("Exiting LineTrackerProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] LSITES = new String [][] { 
//				{ "http://rusticlodge.club", "Kb408", "Denver", "500", "500", "250", "Phoenix", "PT" }
//				{ "http://www.msb247.com", "lb916", "123", "500", "500", "500", "Phoenix", "ET" }
//				{ "http://www.msb247.com", "lb908", "billy", "500", "500", "500", "Phoenix", "ET" }
//				{ "http://www.tpc247.com", "ca29", "123", "5000", "5000", "5000", "Los Angeles", "PT" }
//				{ "http://www.bwc.ag", "WC1108", "kc", "500", "500", "500", "Chicago", "CT" }
//				{ "http://www.ebet2.com", "SA1512", "puck", "500", "500", "500", "New York City", "CT" }
//				{ "http://www.msb247.com", "jg1007", "JAKEB", "500", "500", "500", "Chicago", "ET" }
//				{ "http://skullbet.com", "SK702", "v9746", "0", "0", "0", "Canada", "ET" }
//				{ "http://wos365.com", "lb901", "dak23", "0", "0", "0", "None", "ET" }
//				{ "http://betos365.com", "Q5832", "jake4577", "0", "0", "0", "None", "ET" }
//				{ "http://www.msb247.com", "Z98356", "jua789", "0", "0", "0", "None", "ET" }
				{ "http://www.windycity.ws", "jts115", "jh", "0", "0", "0", "None", "ET" }		
			};

			// Loop through the sites
			for (int i = 0; i < LSITES.length; i++) {
				final LineTrackerProcessSite processSite = new LineTrackerProcessSite(LSITES[i][0], LSITES[i][1], LSITES[i][2], false, true);

				// Now call the test suite
				// LineTrackerProcessSite.testSite(processSite, i, LSITES);
			    processSite.httpClientWrapper.setupHttpClient(LSITES[i][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = LSITES[i][7];

				String xhtml = processSite.loginToSite(LSITES[i][1], LSITES[i][2]);
				
				final Set<PendingEvent> pendingEvents = processSite.getPendingBets(LSITES[0][0], LSITES[0][1], new Object());
				if (pendingEvents != null && pendingEvents.size() > 0) {
					final Iterator<PendingEvent> itr = pendingEvents.iterator();
					if (itr != null) {
						while (itr.hasNext()) {
							final PendingEvent pe = itr.next();
							LOGGER.debug("PendingEvent: " + pe);
						}
					}
				}

/*
			    String xhtml = processSite.loginToSite(LSITES[0][1], LSITES[0][2]);
	
			    final Map<String, String> map = processSite.getMenuFromSite("mlblines", xhtml);
			    LOGGER.debug("map: " + map);
	
				xhtml = processSite.selectSport("mlblines");
				LOGGER.debug("XHTML: " + xhtml);
	
				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("mlblines", xhtml); 
	
				final MlRecordEvent mre = new MlRecordEvent();
				mre.setMlinputfirstone("");
				mre.setMlplusminusfirstone("");
				mre.setId(new Long(927));
				mre.setEventname("#928 Atlanta Braves -153");
				mre.setEventtype("ml");
				mre.setSport("mlblines"); 
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(927));
				mre.setEventid1(new Integer(927));
				mre.setEventid2(new Integer(928));
				mre.setRotationid(new Integer(928));
	
				// Step 9 - Find event
				EventPackage eventPackage = processSite.findEvent(ep, mre);
				if (eventPackage == null) {
					throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE, "Game cannot be found on site for " + mre.getEventname(), xhtml);
				}
*/
			}
		} catch (Throwable t) {
			LOGGER.error("Throwable: ", t);
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

		try {
			// Get time
			final Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
			int hour = date.get(Calendar.HOUR_OF_DAY);

			// Check if we are between 6 am CT and 9 pm CT
			if (hour < 22 && hour > 5) {
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

			// Get the pending bets data
			String xhtml = getSite(super.populateUrl(PENDING_BETS));
			LOGGER.debug("xhtml: " + xhtml);

			if (xhtml != null && xhtml.contains("TablePendingWagers")) {
				if (count++ == 6) {
					// LOGGER.error("Found wager for " + accountName + " " + accountId);
					count = 0;
				}
				if (bigcounter++ == 100) {
					// LOGGER.error("Found wager for " + accountName + " " + accountId + " xhtml: " + xhtml);
					bigcounter = 0;					
				}
				if (!xhtml.contains("<iframe src=\"/Logins\"")) {
					pendingWagers = LTP.parsePendingBets(xhtml, accountName, accountId);
				}
			} else if (xhtml != null && !xhtml.contains("TablePendingWagers")) {
				// Do nothing
				LOGGER.debug("No Open Bets");
				if (!xhtml.contains("<iframe src=\"/Logins\"")) {
					// LOGGER.error("xhtml: " + xhtml);
					// LOGGER.error("previousXhtml: " + previousXhtml);
					HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
					this.httpClientWrapper.setupHttpClient(proxyName);
					this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
					xhtml = getSite(super.populateUrl(PENDING_BETS));
					if (!xhtml.contains("<iframe src=\"/Logins\"")) {
						pendingWagers = LTP.parsePendingBets(xhtml, accountName, accountId);
					}
				}
			} else {
				// LOGGER.error("No pending wagers found for " + accountName + " " + accountId);
				HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
				this.httpClientWrapper.setupHttpClient(proxyName);
				this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
				xhtml = getSite(super.populateUrl(PENDING_BETS));
				if (!xhtml.contains("<iframe src=\"/Logins\"")) {
					pendingWagers = LTP.parsePendingBets(xhtml, accountName, accountId);
				}
			}
		} catch (BatchException be) {
			// Site returned no data after request
			if (be.getErrormessage().contains("Site returned no data after request")) {
				LOGGER.error("LineTracker BatchException: " + be.getBexception());
				HttpClientUtils.closeQuietly(this.httpClientWrapper.getClient());
				this.httpClientWrapper.setupHttpClient(proxyName);
				this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
				String xhtml = getSite(super.populateUrl(PENDING_BETS));
				if (!xhtml.contains("<iframe src=\"/Logins\"")) {
					pendingWagers = LTP.parsePendingBets(xhtml, accountName, accountId);
				}
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
		LTP.setTimezone(timezone);

		// Call the second page
		String xhtml = getSite(this.httpClientWrapper.getHost());
		LOGGER.debug("XHTML: " + xhtml);

		// Parse the index page
		MAP_DATA = LTP.parseIndex(xhtml);	

		if (!MAP_DATA.containsKey(USERID)) {
			// Now call the iframe src
			xhtml = getSite(this.httpClientWrapper.getHost() + MAP_DATA.get("iframe"));
			LOGGER.debug("XHTML: " + xhtml);

			// Parse the index page
			MAP_DATA = LTP.parseIndex(xhtml);			
		}

		final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		MAP_DATA.put(USERID, username);
		MAP_DATA.put(PASSID, password);
		final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, "");

		// Call the login
		xhtml = authenticate(actionName, postNameValuePairs);
		final String thewebappname = determineWebappName(httpClientWrapper.getPreviousUrl());
		LOGGER.debug("thewebappname: " + thewebappname);
		httpClientWrapper.setWebappname(thewebappname);

		if (xhtml.contains("tblComments")) {
			LOGGER.debug("XHTML: " + xhtml);
			xhtml = getSite(populateUrl("StraightBetMenuEx2.aspx"));
		}

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectSport(java.lang.String)
	 */
	@Override
	protected String selectSport(String type) throws BatchException {
		LOGGER.error("Entering selectSport()");
		LOGGER.debug("type: " + type);

		// Process the setup page
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		final String actionName = setupNameValuesEmpty(postValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.error("ActionName: " + actionName);

		// Post to site page
		final String xhtml = postSite(actionName, postValuePairs);

		LOGGER.error("Exiting selectSport()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#findEvent(java.util.Iterator, com.ticketadvantage.services.model.BaseRecordEvent)
	 */
	@Override
	protected EventPackage findEvent(Iterator<EventPackage> ep, BaseRecordEvent event) throws BatchException {
		LOGGER.error("Entering findEvent()");
		final EventPackage eventPackage = super.findEvent(ep, event);
		if (eventPackage != null) {
			LineTrackerTeamPackage team1 = (LineTrackerTeamPackage)eventPackage.getTeamone();
			LineTrackerTeamPackage team2 = (LineTrackerTeamPackage)eventPackage.getTeamtwo();

			String forValue1 = null;
			String forValue2 = null;
			String type = null;
			if (event instanceof SpreadRecordEvent) {
				type = "spread";
				forValue1 = team1.getGameSpreadSelectFor();
				forValue2 = team2.getGameSpreadSelectFor();
			} else if (event instanceof TotalRecordEvent) {
				type = "total";
				forValue1 = team1.getGameTotalSelectFor();
				forValue2 = team2.getGameTotalSelectFor();
			}
			LOGGER.error("forValue1: " + forValue1);
			LOGGER.error("forValue2: " + forValue2);

			// HTTP Header Options
			final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(1);
			headerValuePairs.add(new BasicNameValuePair("Host", this.httpClientWrapper.getHost()));
			headerValuePairs.add(new BasicNameValuePair("Origin", this.httpClientWrapper.getHost()));
			headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getHost()));

			final String data1 = "{\"data\":\"" + forValue1 + "\"}";
			final String data2 = "{\"data\":\"" + forValue2 + "\"}";
			final String url = this.httpClientWrapper.getHost() + "/AjaxWS.aspx/GetMultipleOffersOrBuySellPoints";
			List<NameValuePair> retValue = this.httpClientWrapper.postJSONSite(url, null, data1, headerValuePairs);
			String json = this.httpClientWrapper.getCookiesAndJSON(retValue);
			team1 = LTP.parseJSON(json, type, team1);

			retValue = this.httpClientWrapper.postJSONSite(url, null, data2, headerValuePairs);
			json = this.httpClientWrapper.getCookiesAndJSON(retValue);
			team2 = LTP.parseJSON(json, type, team2);
		}

		LOGGER.error("Exiting findEvent()");
		return eventPackage;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#createSiteTransaction()
	 */
	@Override
	protected SiteTransaction createSiteTransaction() {
		LOGGER.error("Entering createSiteTransaction()");

		final SiteTransaction siteTransaction = new SiteTransaction();

		LOGGER.error("Exiting createSiteTransaction()");
		return siteTransaction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectEvent(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.error("Entering selectEvent()");

		final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		postNameValuePairs.add(new BasicNameValuePair(siteTransaction.getInputName(), siteTransaction.getAmount()));

		// Now check if selection needs to be sent as well
		if (siteTransaction.getOptionValue() != null) {
			postNameValuePairs.add(new BasicNameValuePair(siteTransaction.getSelectName(), siteTransaction.getOptionValue()));
		}

		// Check wager type
		if ("1".equals(event.getWtype())) {
			// Risk
			postNameValuePairs.add(new BasicNameValuePair("ctl00$cphWorkArea$amountType", "Lay"));
		} else {
			// Win
			postNameValuePairs.add(new BasicNameValuePair("ctl00$cphWorkArea$amountType", "Take"));
		}

		// Setup post parameters and get action URL
		final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("actionName: " + actionName);
		
		// Call to select the game
		final String xhtml = postSite(actionName, postNameValuePairs);

		// Check for line changes
		if (xhtml.contains("One or more lines have changed please click ACCEPT or DELETE for each wager to continue.")) {
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed error", xhtml);
		}

		LOGGER.error("Exiting selectEvent()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.error("Entering parseEventSelection()");
		
		// Parse the event selection
		Map<String, String> tempMap = null;
		try {
			tempMap = LTP.parseEventSelection(xhtml, null, null);
			LOGGER.error("tempMap: " + tempMap);
		} catch (BatchException be) {
			if (be.getErrorcode() == BatchErrorCodes.LINE_CHANGED_ERROR) {
				xhtml = processLineChange(xhtml, event, ae);
				tempMap = LTP.parseEventSelection(xhtml, null, null);
			} else {
				throw be;
			}
		}
		
		// Check for a wager limit and rerun
		if (tempMap.containsKey("wageramount")) {
			// Only call the select event once
			if (rerunCount++ == 0) {
				String amt = tempMap.get("wageramount");
				LOGGER.debug("amt: " + amt);
				if (amt != null && !amt.equals("0")) {
					siteTransaction.setAmount(tempMap.get("wageramount"));
					ae.setAmount(tempMap.get("wageramount"));
					ae.setActualamount(tempMap.get("wageramount"));
					xhtml = selectEvent(siteTransaction, eventPackage, event, ae);
	
					try {
						tempMap = LTP.parseEventSelection(xhtml, null, null);
						LOGGER.error("tempMap: " + tempMap);
					} catch (BatchException be) {
						if (be.getErrorcode() == BatchErrorCodes.LINE_CHANGED_ERROR) {
							xhtml = processLineChange(xhtml, event, ae);
							tempMap = LTP.parseEventSelection(xhtml, null, null);
						} else {
							throw be;
						}
					}

					// Check for a wager limit and rerun
					if (tempMap.containsKey("wageramount")) {
						throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The maximum wager amount has been reached $" + tempMap.get("wageramount"), xhtml);
					} else if (tempMap.containsKey("wageraccountlimit")) {
						throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "The maximum account balance has been reached $" + tempMap.get("wageraccountlimit"), xhtml);
					} else {
						MAP_DATA = tempMap;
					}
				} else {
					throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The maximum wager amount has been reached $" + amt, xhtml);
				}
			} else {
				throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The maximum wager amount has been reached $" + siteTransaction.getAmount(), xhtml);
			}
		} else if (tempMap.containsKey("wageraccountlimit")) {
			throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "The maximum account balance has been reached " + tempMap.get("wageraccountlimit"), xhtml);
		} else {
			MAP_DATA = tempMap;
		}

		// Check wager type
		if ("1".equals(event.getWtype())) {
			// Risk
			double sAmount = Double.parseDouble(siteTransaction.getAmount());
			double mAmount = Double.parseDouble(MAP_DATA.get("risk"));
			if (sAmount != mAmount) {
				throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, "Risk amount is " + siteTransaction.getAmount() + " but site amount is " + MAP_DATA.get("risk"), xhtml);
			}
		} else {
			// Win
			double sAmount = Double.parseDouble(siteTransaction.getAmount());
			double mAmount = Double.parseDouble(MAP_DATA.get("win"));
			if (sAmount != mAmount) {
				throw new BatchException(BatchErrorCodes.ERROR_PARSING_WAGER_INFO, BatchErrorMessage.ERROR_PARSING_WAGER_INFO, "Win amount is " + siteTransaction.getAmount() + " but site amount is " + MAP_DATA.get("win"), xhtml);
			}
		}

		LOGGER.error("Exiting parseEventSelection()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#completeTransaction(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.error("Entering completeTransaction()");
		String xhtml = null;

		// Check to process the transaction or not
		if (processTransaction) {
			// Call the commit transaction
			final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
			MAP_DATA.put(CONFIRM_PASSWORD, httpClientWrapper.getPassword());

			final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
			LOGGER.debug("actionName: " + actionName);

			// Call the wager action
			xhtml = postSite(actionName, postNameValuePairs);
			
			// Check for line change
			if (xhtml.contains("At least one line (or price or pitcher) has changed")) {
				xhtml = processLineChange(xhtml, event, ae);
			}
			
			if (xhtml.contains("risk amount for your selected wagers of")) {
				throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "The maximum account balance has been reached for wager " + siteTransaction.getAmount(), xhtml);	
			}
		}

		LOGGER.error("Exiting completeTransaction()");
		return xhtml;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#parseTicketTransaction(java.lang.String)
	 */
	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		LOGGER.error("Entering parseTicketNumber()");

		final String ticketNumber = LTP.parseTicketNumber(xhtml);
		LOGGER.debug("ticketNumber: " + ticketNumber);

		LOGGER.error("Exiting parseTicketNumber()");
		return ticketNumber;		
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

		final Map<String, String> lineChanges = LTP.processLineChange(xhtml);
		LOGGER.error("lineChanges: " + lineChanges);

		if (lineChanges != null && !lineChanges.isEmpty()) {
			if (event instanceof SpreadRecordEvent) {
				final SpreadRecordEvent sre = (SpreadRecordEvent)event;
				if (determineSpreadLineChange(sre, ae, lineChanges)) {
					// setup the data
					List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
					lineChanges.remove("juiceindicator");
					lineChanges.remove("juice");
					lineChanges.remove("value");
					lineChanges.remove("valueindicator");
					lineChanges.put("password", this.httpClientWrapper.getPassword());
					String actionLogin = setupNameValuesEmpty(postValuePairs, lineChanges, null);
					LOGGER.debug("ActionLogin: " + actionLogin);

					// Post to site page
					xhtml = postSite(actionLogin, postValuePairs);							
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			} else if (event instanceof TotalRecordEvent) {
				final TotalRecordEvent tre = (TotalRecordEvent)event;
				if (determineTotalLineChange(tre, ae, lineChanges)) {
					// setup the data
					List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
					lineChanges.remove("juiceindicator");
					lineChanges.remove("juice");
					lineChanges.remove("value");
					lineChanges.remove("valueindicator");
					MAP_DATA.put("password", this.httpClientWrapper.getPassword());
					String actionLogin = setupNameValuesEmpty(postValuePairs, lineChanges, null);
					LOGGER.debug("ActionLogin: " + actionLogin);

					// Post to site page
					xhtml = postSite(actionLogin, postValuePairs);
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			} else if (event instanceof MlRecordEvent) {
				final MlRecordEvent mre = (MlRecordEvent)event;
				
				if (determineMlLineChange(mre, ae, lineChanges)) {
					// setup the data
					List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
					lineChanges.remove("juiceindicator");
					lineChanges.remove("juice");
					lineChanges.remove("value");
					lineChanges.remove("valueindicator");
					MAP_DATA.put("password", this.httpClientWrapper.getPassword());
					String actionLogin = setupNameValuesEmpty(postValuePairs, lineChanges, null);
					LOGGER.debug("ActionLogin: " + actionLogin);

					// Post to site page
					xhtml = postSite(actionLogin, postValuePairs);
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			}
		}

		LOGGER.info("Exiting processLineChange()");
		return xhtml;
	}
}