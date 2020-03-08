/**
 * 
 */
package com.ticketadvantage.services.dao.sites.wagershack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.ProxyContainer;
import com.ticketadvantage.services.dao.sites.SiteEventPackage;
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
public class BetGothamProcessSite extends WagerShackMobileProcessSite {
	private static final Logger LOGGER = Logger.getLogger(BetGothamProcessSite.class);
	private static final String SPORTS = "Sports.aspx";
	private static final String GET_SPORTS = "betslip/getActiveLeagues.asp";
	private static final String GET_LINES = "betslip/getLinesbyLeague.asp";
	private static final String GET_STRAIGHT = "betslip/getstraight.asp";
	private static final String DO_BET = "betslip/doBet.asp";
	protected final BetGothamParser WSMP = new BetGothamParser();
	protected String gameId;
	protected Map<String, String> globalHiddenVariables;

	/**
	 * 
	 */
	public BetGothamProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("BetGotham", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering BetGothamProcessSite()");

		// Setup the parser
		super.siteParser = WSMP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL" };
		NFL_LINES_NAME = new String[] { "NFL" };
		NFL_FIRST_SPORT = new String[] { "NFL" };
		NFL_FIRST_NAME = new String[] { "NFL 1ST HALVES" };
		NFL_SECOND_SPORT = new String[] { "NFL" };
		NFL_SECOND_NAME = new String[] { "NFL 2ND HALVES" };
		NCAAF_LINES_SPORT = new String[] { "NCAA FB" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL MEN" };
		NCAAF_FIRST_SPORT = new String[] { "NCAA FB" };
		NCAAF_FIRST_NAME = new String[] { "NCAA FB 1ST HALVES" };
		NCAAF_SECOND_SPORT = new String[] { "NCAA FB" };
		NCAAF_SECOND_NAME = new String[] { "NCAA FB 2ND HALVES" };
		NBA_LINES_SPORT = new String[] { "NBA" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "NBA" };
		NBA_FIRST_NAME = new String[] { "NBA 1ST HALVES" };
		NBA_SECOND_SPORT = new String[] { "NBA" };
		NBA_SECOND_NAME = new String[] { "NBA 2ND HALVES" };
		NCAAB_LINES_SPORT = new String[] { "CBB", "NCAA BK" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL MEN", "NCAA BK MENS ADDED GAMES" };
		NCAAB_FIRST_SPORT = new String[] { "CBB", "NCAA BK" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BK 1ST HALVES", "NCAA BK MENS ADDED GAMES 1ST HALVES" };
		NCAAB_SECOND_SPORT = new String[] { "CBB", "NCAA BK" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BK MENS 2ND HALVES", "NCAA BK MENS ADDED GAMES 2ND HALVES" };
		NHL_LINES_SPORT = new String[] { "NHL", "HOCKEY" };
		NHL_LINES_NAME = new String[] { "NHL", "NATIONAL HOCKEY LEAGUE" };
		NHL_FIRST_SPORT = new String[] { "NHL", "HOCKEY" };
		NHL_FIRST_NAME = new String[] { "NHL 1ST HALVES", "NHL PERIODS" };
		NHL_SECOND_SPORT = new String[] { "NHL", "HOCKEY" };
		NHL_SECOND_NAME = new String[] { "NHL 2ND HALVES", "NHL PERIODS" };
		NHL_THIRD_SPORT = new String[] { "NHL", "HOCKEY" };
		NHL_THIRD_NAME = new String[] { "NHL 3RD HALVES", "NHL PERIODS" };
		WNBA_LINES_SPORT = new String[] { "WNBA" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_FIRST_SPORT = new String[] { "WNBA" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1ST HALVES" };
		WNBA_SECOND_SPORT = new String[] { "WNBA" };
		WNBA_SECOND_NAME = new String[] { "WNBA 1ST HALVES" };
		MLB_LINES_SPORT = new String[] { "BASEBALL" };
		MLB_LINES_NAME = new String[] { "MLB GAME LINES" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL" };
		MLB_FIRST_NAME = new String[] { "MLB 1ST 5  INNINGS" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL" };
		MLB_SECOND_NAME = new String[] { "MLB 2ND HALVES" };
		MLB_THIRD_SPORT = new String[] { "BASEBALL" };
		MLB_THIRD_NAME = new String[] { "MLB 3RD HALVES" };

		LOGGER.info("Exiting BetGothamProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
//				{ "http://mobile.wagershack.com", "RK230", "PS", "500", "500", "500", "Los Angeles", "PT"}
//				{ "http://betgotham.com/", "aj403", "903", "500", "500", "500", "Chicago", "ET"}
//				{ "http://mobile.gowagerhub.com", "CLUB05", "YELLOW", "500", "500", "500", "None", "ET"}
//				{ "http://engine.betgotham.com", "fw436", "WHITE", "0", "0", "0", "None", "ET"}
				{ "https://engine.action247.ag", "cal334", "NNLAX", "0", "0", "0", "None", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final BetGothamProcessSite processSite = new BetGothamProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], true, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];
			    processSite.testSpread(processSite, TDSITES);
			}
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
		WSMP.setTimezone(timezone);
		httpClientWrapper.setWebappname("");

		// Get the home page
		String xhtml = getMobileSite(this.httpClientWrapper.getHost());
		LOGGER.debug("XHTML: " + xhtml);

		// Get the index info
		MAP_DATA = WSMP.parseIndex(xhtml);

		// Setup the Username
		if (MAP_DATA.containsKey("account")) {
			MAP_DATA.put("account", username);
		}

		// Setup the Password
		if (MAP_DATA.containsKey("password")) {
			MAP_DATA.put("password", password);
		}
		LOGGER.debug("Map: " + MAP_DATA);

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, httpClientWrapper.getWebappname());

		// Call the login
		xhtml = authenticate(actionLogin.trim(), postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		// Parse login
		MAP_DATA = WSMP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Setup the webapp name
		httpClientWrapper.setWebappname("wager");

		// Get the Sports.aspx
		xhtml = getSite(populateUrl(SPORTS));
		LOGGER.debug("XHTML: " + xhtml);

		// Get the hidden input fields
		MAP_DATA = WSMP.parseSports(xhtml);
		copyGlobalVariables();

		postValuePairs = new ArrayList<NameValuePair>(1);
		postValuePairs.add(new BasicNameValuePair("pi", username + "|" + password));
		postValuePairs.add(new BasicNameValuePair("idp", MAP_DATA.get("idp")));
		postValuePairs.add(new BasicNameValuePair("idlt", MAP_DATA.get("idlt")));
		postValuePairs.add(new BasicNameValuePair("idlan", MAP_DATA.get("idlan")));
		postValuePairs.add(new BasicNameValuePair("bid", MAP_DATA.get("idbook")));
		postValuePairs.add(new BasicNameValuePair("wt", "0"));

		xhtml = postSite(populateUrl(GET_SPORTS), postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
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

		// http://mobile.wagershack.com/wager/Sports.aspx?WT=0&lid=38
		String chk = MAP_DATA.get("chk");
		final StringBuffer sb = new StringBuffer(200);
		sb.append(SPORTS);
		sb.append("?WT=0");
		sb.append("&lid=").append(chk);

		// Get the input hidden attributes
		String xhtml = getMobileSite(populateUrl(sb.toString()));
		LOGGER.debug("XHTML: " + xhtml);

		// Parse the hidden input attributes
		MAP_DATA = WSMP.parseSports(xhtml);
		copyGlobalVariables();

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
		postValuePairs.add(new BasicNameValuePair("idwt", globalHiddenVariables.get("currentWT")));
		postValuePairs.add(new BasicNameValuePair("mlbl", globalHiddenVariables.get("mlbl")));
		postValuePairs.add(new BasicNameValuePair("nhll", globalHiddenVariables.get("nhll")));
		postValuePairs.add(new BasicNameValuePair("pi", super.httpClientWrapper.getUsername() + "|" + super.httpClientWrapper.getPassword()));
		postValuePairs.add(new BasicNameValuePair("pid", globalHiddenVariables.get("pid")));
		postValuePairs.add(new BasicNameValuePair("utc", globalHiddenVariables.get("utc")));
		postValuePairs.add(new BasicNameValuePair("wt", globalHiddenVariables.get("currentWT")));
		postValuePairs.add(new BasicNameValuePair("wtid", "0"));

		// Post to site page
		xhtml = postMobileSite(populateUrl(GET_LINES), postValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		LOGGER.info("Exiting selectSport()");
		return xhtml;
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
				LOGGER.debug("idValue: " + idValue);
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
				LOGGER.debug("idValue: " + idValue);
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
				LOGGER.debug("idValue: " + idValue);
			} else {
				throw new BatchException(BatchErrorCodes.XHTML_GAMES_PARSING_EXCEPTION, BatchErrorMessage.XHTML_GAMES_PARSING_EXCEPTION, "No moneyline available for this game");
			}
		}

		// Now check if selection needs to be sent as well
		if (idValue != null) {
			int index = idValue.lastIndexOf("_");
			if (index != -1) {
				gameId = idValue = idValue.substring(0, index);
			}
		}
		LOGGER.debug("idValue: " + idValue);

		// http://mobile.wagershack.com/wager/betting/getstraight.asp?pid=343052&idp=10849&idc=193933591&b=1_1751814_-7_-115_0_0&c=1&amt=0&wt=0
		final StringBuffer sb = new StringBuffer(200);
		sb.append(GET_STRAIGHT);
		sb.append("?amt=0");
		sb.append("&b=").append(idValue + "%2C");
		sb.append("&c=").append("1");
		sb.append("&idc=").append(globalHiddenVariables.get("idc"));
		sb.append("&idp=").append(globalHiddenVariables.get("idp"));
		sb.append("&pi=").append(super.httpClientWrapper.getUsername() + "%7C" + super.httpClientWrapper.getPassword());
		sb.append("&pid=").append(globalHiddenVariables.get("pid"));

		// Setup the wager
		String xhtml = getMobileSite(populateUrl(sb.toString()));
		LOGGER.debug("XHTML: " + xhtml);

		// Check for line changes
		if (xhtml.contains("One or more lines have changed please click ACCEPT or DELETE for each wager to continue")) {
			throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed error", xhtml);
		}

		LOGGER.info("Exiting selectEvent()");
		return xhtml;
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

		final String ticketNumber = WSMP.parseTicketNumber(xhtml);
		LOGGER.debug("ticketNumber: " + ticketNumber);

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

		LOGGER.debug("siteAmount: " + siteAmount);
		ae.setActualamount(siteAmount);

		// Setup the wager
		String xhtml = null;
		if (processTransaction) {
			// http://mobile.wagershack.com/wager/betting/getstraight.asp?pid=343052&idp=10849&idc=193939990&b=3_1751821_45.5_-110_100_0.5_1&c=1&amt=1&fp=0&wt=0
			// http://engine.betgotham.com/wager/betslip/getstraight.asp?pid=10416&idp=336&idc=4765581&b=_50.0_0_1&c=1&amt=1&fp=0
			// http://engine.betgotham.com/wager/betslip/getstraight.asp?pi=AJ403%7C903&pid=10416&idp=336&idc=4765663&b=2_759246_-7.5_-115_50_0%2C&c=1&amt=1&fp=0
			final StringBuffer sb = new StringBuffer(200);
			sb.append(GET_STRAIGHT);
			sb.append("?pi=").append(super.httpClientWrapper.getUsername() + "%7C" + super.httpClientWrapper.getPassword());
			sb.append("&pid=").append(globalHiddenVariables.get("pid"));
			sb.append("&idp=").append(globalHiddenVariables.get("idp"));
			sb.append("&idc=").append(globalHiddenVariables.get("idc"));
			sb.append("&b=").append(gameId + "_" + siteAmount + "_0%2C");
			sb.append("&c=").append("1");
			sb.append("&amt=1");
			sb.append("&fp=0");

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
			// http://mobile.wagershack.com/wager/betting/doBet.asp?idlt=236&hash=D3D4C5DEB455AC79DD5FF47C88BD65D9&pid=343052&wt=0
			final StringBuffer sb = new StringBuffer(200);
			sb.append(DO_BET);
			sb.append("?pi=").append(super.httpClientWrapper.getUsername() + "%7C" + super.httpClientWrapper.getPassword());
			sb.append("&pid=").append(globalHiddenVariables.get("pid"));
			sb.append("&idlt=").append(globalHiddenVariables.get("idlt"));
			sb.append("&hash=").append(globalHiddenVariables.get("hash"));

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
}