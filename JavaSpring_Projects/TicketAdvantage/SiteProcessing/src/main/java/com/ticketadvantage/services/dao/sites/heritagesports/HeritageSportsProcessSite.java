/**
 * 
 */
package com.ticketadvantage.services.dao.sites.heritagesports;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
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
import com.ticketadvantage.services.model.PreviewInput;
import com.ticketadvantage.services.model.PreviewOutput;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class HeritageSportsProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(HeritageSportsProcessSite.class);
	private static final String PENDING_BETS = "AlPendingWagers.asp";
	private static final String LEAGUES_SUMMARY = "leaguesSummary.asp";
	private static final String BB_SPORT_SELECTION = "BbSportSelection.asp";
	private static final String SELECT_SPORT = "BbGameSelection.asp";
	private final HeritageSportsParser HSP = new HeritageSportsParser();
	private int rerunCount = 0;

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @param isMobile
	 * @param showRequestResponse
	 */
	public HeritageSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("HeritageSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering HeritageSportsProcessSite()");

		// Setup the parser
		this.siteParser = HSP;
		HSP.setTimezone(this.timezone);

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL" };
		NFL_LINES_NAME = new String[] { "Game" };
		NFL_FIRST_SPORT = new String[] { "NFL" };
		NFL_FIRST_NAME = new String[] { "1st Half" };
		NFL_SECOND_SPORT = new String[] { "NFL" };
		NFL_SECOND_NAME = new String[] { "2nd Half" };
		NCAAF_LINES_SPORT = new String[] { "College", "Extra Games" };
		NCAAF_LINES_NAME = new String[] { "Game" };
		NCAAF_FIRST_SPORT = new String[] { "College", "Extra Games" };
		NCAAF_FIRST_NAME = new String[] { "1st Half" };
		NCAAF_SECOND_SPORT = new String[] { "College", "Extra Games" };
		NCAAF_SECOND_NAME = new String[] { "2nd Half" };
		NBA_LINES_SPORT = new String[] { "NBA", "NBA PRESEASON" };
		NBA_LINES_NAME = new String[] { "Game" };
		NBA_FIRST_SPORT = new String[] { "NBA", "NBA PRESEASON" };
		NBA_FIRST_NAME = new String[] { "1st Half" };
		NBA_SECOND_SPORT = new String[] { "NBA", "NBA PRESEASON" };
		NBA_SECOND_NAME = new String[] { "2nd Half" };
		NCAAB_LINES_SPORT = new String[] { "NCAA" };
		NCAAB_LINES_NAME = new String[] { "Game" };
		NCAAB_FIRST_SPORT = new String[] { "NCAA" };
		NCAAB_FIRST_NAME = new String[] { "1st Half" };
		NCAAB_SECOND_SPORT = new String[] { "NCAA" };
		NCAAB_SECOND_NAME = new String[] { "2nd Half" };
		NHL_LINES_SPORT = new String[] { "NHL" };
		NHL_LINES_NAME = new String[] { "Game" };
		NHL_FIRST_SPORT = new String[] { "NHL" };
		NHL_FIRST_NAME = new String[] { "1st Period" };
		NHL_SECOND_SPORT = new String[] { "NHL" };
		NHL_SECOND_NAME = new String[] { "2nd Period" };
		NHL_THIRD_SPORT = new String[] { "NHL" };
		NHL_THIRD_NAME = new String[] { "3rd Period" };
		WNBA_LINES_SPORT = new String[] { "WNBA" };
		WNBA_LINES_NAME = new String[] { "Game" };
		WNBA_FIRST_SPORT = new String[] { "WNBA" };
		WNBA_FIRST_NAME = new String[] { "1st Half" };
		WNBA_SECOND_SPORT = new String[] { "WNBA" };
		WNBA_SECOND_NAME = new String[] { "2nd Half" };
		MLB_LINES_SPORT = new String[] { "MLB", "MLB EXHIBITION" };
		MLB_LINES_NAME = new String[] { "Game" };
		MLB_FIRST_SPORT = new String[] { "MLB" };
		MLB_FIRST_NAME = new String[] { "1st 5 Innings" };
		MLB_SECOND_SPORT = new String[] { "MLB" };
		MLB_SECOND_NAME = new String[] { "MLB 2nd half lines" };
		LOGGER.info("Exiting HeritageSportsProcessSite()");
	}

	/**
	 * 
	 * @param siteName
	 * @param host
	 * @param username
	 * @param password
	 * @param isMobile
	 * @param showRequestResponse
	 */
	public HeritageSportsProcessSite(String siteName, String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super(siteName, host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering HeritageSportsProcessSite()");

		// Setup the parser
		this.siteParser = HSP;
		HSP.setTimezone(this.timezone);

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL" };
		NFL_LINES_NAME = new String[] { "Game" };
		NFL_FIRST_SPORT = new String[] { "NFL" };
		NFL_FIRST_NAME = new String[] { "1st Half" };
		NFL_SECOND_SPORT = new String[] { "NFL" };
		NFL_SECOND_NAME = new String[] { "2nd Half" };
		NCAAF_LINES_SPORT = new String[] { "College" };
		NCAAF_LINES_NAME = new String[] { "Game" };
		NCAAF_FIRST_SPORT = new String[] { "College" };
		NCAAF_FIRST_NAME = new String[] { "1st Half" };
		NCAAF_SECOND_SPORT = new String[] { "College" };
		NCAAF_SECOND_NAME = new String[] { "2nd Half" };
		NBA_LINES_SPORT = new String[] { "NBA" };
		NBA_LINES_NAME = new String[] { "Game" };
		NBA_FIRST_SPORT = new String[] { "NBA" };
		NBA_FIRST_NAME = new String[] { "1st Half" };
		NBA_SECOND_SPORT = new String[] { "NBA" };
		NBA_SECOND_NAME = new String[] { "2nd Half" };
		NCAAB_LINES_SPORT = new String[] { "NCAA" };
		NCAAB_LINES_NAME = new String[] { "Game" };
		NCAAB_FIRST_SPORT = new String[] { "NCAA" };
		NCAAB_FIRST_NAME = new String[] { "1st Half" };
		NCAAB_SECOND_SPORT = new String[] { "NCAA" };
		NCAAB_SECOND_NAME = new String[] { "2nd Half" };
		NHL_LINES_SPORT = new String[] { "NHL" };
		NHL_LINES_NAME = new String[] { "Game" };
		NHL_FIRST_SPORT = new String[] { "NHL" };
		NHL_FIRST_NAME = new String[] { "1st Period" };
		NHL_SECOND_SPORT = new String[] { "NHL" };
		NHL_SECOND_NAME = new String[] { "2nd Period" };
		NHL_THIRD_SPORT = new String[] { "NHL" };
		NHL_THIRD_NAME = new String[] { "3rd Period" };
		WNBA_LINES_SPORT = new String[] { "WNBA" };
		WNBA_LINES_NAME = new String[] { "Game" };
		WNBA_FIRST_SPORT = new String[] { "WNBA" };
		WNBA_FIRST_NAME = new String[] { "1st Half" };
		WNBA_SECOND_SPORT = new String[] { "WNBA" };
		WNBA_SECOND_NAME = new String[] { "2nd Half" };
		MLB_LINES_SPORT = new String[] { "MLB" };
		MLB_LINES_NAME = new String[] { "Game" };
		MLB_FIRST_SPORT = new String[] { "MLB" };
		MLB_FIRST_NAME = new String[] { "1st 5 Innings" };
		MLB_SECOND_SPORT = new String[] { "MLB" };
		MLB_SECOND_NAME = new String[] { "MLB 2nd half lines" };
		LOGGER.info("Exiting HeritageSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] HSSITES = new String [][] {
//				{ "http://engine.firstclasswager.com", "e402", "bounty", "500", "500", "500", "Los Angeles", "PT" }
//				{ "http://engine.bettommy.eu", "cj816", "jake", "1", "1", "1", "Chicago", "ET" }
				{ "http://engine.bettommy.eu", "dj116", "jake", "1", "1", "1", "Chicago", "ET" }
			};

			// Loop through the sites
			for (int i = 0; i < HSSITES.length; i++) {
				final HeritageSportsProcessSite processSite = new HeritageSportsProcessSite(HSSITES[i][0], HSSITES[i][1],
						HSSITES[i][2], false, false);
			    processSite.httpClientWrapper.setupHttpClient(HSSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = HSSITES[0][7];
			    processSite.siteParser = processSite.HSP;
			    processSite.siteParser.setTimezone(HSSITES[0][7]);
			    processSite.HSP.setTimezone(HSSITES[0][7]);

			    String xhtml = processSite.loginToSite(HSSITES[i][1], HSSITES[i][2]);

				final PreviewInput previewInput = new PreviewInput();
				previewInput.setLinetype("total");
				previewInput.setLineindicator("+");
				previewInput.setRotationid(new Integer(504));
				previewInput.setSporttype("nbalines");
				previewInput.setProxyname("Chicago");
				previewInput.setTimezone("ET");

				final PreviewOutput previewData = processSite.previewEvent(previewInput);
				LOGGER.error("PreviewData: " + previewData);

/*
			    
			    Map<String, String> map = processSite.getMenuFromSite("mlblines", xhtml);
			    LOGGER.debug("map: " + map);

				xhtml = processSite.selectSport("mlblines");
				LOGGER.debug("XHTML: " + xhtml);

				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("mlblines", xhtml);   
			
				MlRecordEvent mre = new MlRecordEvent();
				mre.setMlinputfirstone("");
				mre.setMlplusminusfirstone("");
				mre.setId(new Long(912));
				mre.setEventname("#912 San Diego Padres -112 for Game");
				mre.setEventtype("ml");
				mre.setSport("mlbfirst"); 
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(911));
				mre.setEventid1(new Integer(911));
				mre.setEventid2(new Integer(912));
				
				// Step 9 - Find event
				EventPackage eventPackage = processSite.findEvent(ep, mre);
				if (eventPackage == null) {
					throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE, "Game cannot be found on site for " + mre.getEventname(), xhtml);
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
		
		// Get the pending bets data
		String xhtml = getSite(super.populateUrl(PENDING_BETS));
		LOGGER.debug("xhtml: " + xhtml);

		if (xhtml != null && xhtml.contains("view all wagers graded within the last 14 days.")) {
			pendingWagers = HSP.parsePendingBets(xhtml, accountName, accountId);
		} else {
			LOGGER.error("xhtml: " + xhtml);
			LOGGER.error("Not logged in");
			if (xhtml != null && xhtml.contains("loginform")) {
				this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
			}
			xhtml = getSite(super.populateUrl(PENDING_BETS));
			LOGGER.error("xhtml: " + xhtml);
			if (xhtml != null && xhtml.contains("view all wagers graded within the last 14 days.")) {
				pendingWagers = HSP.parsePendingBets(xhtml, accountName, accountId);
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
		HSP.setTimezone(timezone);

		// Get the home page
		String xhtml = getSite(httpClientWrapper.getHost());
		LOGGER.debug("XHTML: " + xhtml);

		// Get home page data
		MAP_DATA = HSP.parseIndex(xhtml);
		httpClientWrapper.setWebappname(determineWebappName(MAP_DATA.get("action")));

		// setup the webapp name
		if (httpClientWrapper.getWebappname() == null) {
			httpClientWrapper.setWebappname("");
		}

		MAP_DATA.put("Login", "Login");		
		// Setup the customer ID
		MAP_DATA.put("customerID", username);	
		// Setup the password
		MAP_DATA.put("password", password);

		LOGGER.debug("Map: " + MAP_DATA);

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		String actionName = setupNameValuesEmpty(postValuePairs, MAP_DATA, null);
		LOGGER.debug("ActionName: " + actionName);

		xhtml = authenticate(actionName, postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);
		
		// Parse the login
		MAP_DATA = HSP.parseLogin(xhtml);
				
		xhtml = this.getSite(populateUrl(BB_SPORT_SELECTION));
		LOGGER.debug("XHTML: " + xhtml);

		// Parse the login
		MAP_DATA = HSP.parseLogin(xhtml);

		// http://engine.firstclasswager.com/leaguesSummary.asp?r=0.18122559978147768
		xhtml = this.getSite(populateUrl(LEAGUES_SUMMARY + "?r=" + Math.random()));

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
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
		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		MAP_DATA.put("action", SELECT_SPORT);
		String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, null);
		LOGGER.debug("ActionLogin: " + actionLogin);

		// Post to site page
		String xhtml = postSite(actionLogin, postValuePairs);

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
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering selectEvent()");
		String siteAmount = siteTransaction.getAmount();

		if (event instanceof SpreadRecordEvent) {
			siteTransaction = getSpreadTransaction((SpreadRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.debug("siteAmount: " + siteAmount);
			Double sAmount = Double.parseDouble(siteAmount);
			if (sAmount != null) {
				HeritageSportsEventPackage heritageSportsEventPackage = (HeritageSportsEventPackage)eventPackage;
				LOGGER.debug("HeritageSportsEventPackage: " + heritageSportsEventPackage);
				
				if (siteTransaction.getRiskorwin() != null && siteTransaction.getRiskorwin().intValue() == 1) {
					if (heritageSportsEventPackage != null && heritageSportsEventPackage.getSpreadMax() != null) {
						if (sAmount.doubleValue() > heritageSportsEventPackage.getSpreadMax().intValue()) {
							siteAmount = heritageSportsEventPackage.getSpreadMax().toString();
						}
					}
				} else {
					Double wagerAmount = Double.valueOf(siteAmount);
					float spreadJuice = ae.getSpreadjuice();
					Double risk = wagerAmount * (100 / spreadJuice);
					risk = round(risk.doubleValue(), 2);
					if (heritageSportsEventPackage != null && heritageSportsEventPackage.getSpreadMax() != null
							&& (risk.doubleValue() > heritageSportsEventPackage.getSpreadMax().intValue())) {
						LOGGER.error("Risk: " + risk);
						LOGGER.error("heritageSportsEventPackage.getSpreadMax().intValue(): "
								+ heritageSportsEventPackage.getSpreadMax().intValue());
						siteAmount = heritageSportsEventPackage.getSpreadMax().toString();
					} else {
						LOGGER.error("Risk: " + risk);
						siteAmount = risk.toString();
					}
				}
			}
		} else if (event instanceof TotalRecordEvent) {
			siteTransaction = getTotalTransaction((TotalRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.debug("siteAmount: " + siteAmount);
			Double tAmount = Double.parseDouble(siteAmount);
			if (tAmount != null) {
				HeritageSportsEventPackage heritageSportsEventPackage = (HeritageSportsEventPackage)eventPackage;
				LOGGER.debug("heritageSportsEventPackage: " + heritageSportsEventPackage);
				
				if (siteTransaction.getRiskorwin() != null && siteTransaction.getRiskorwin().intValue() == 1) {
					if (heritageSportsEventPackage != null && heritageSportsEventPackage.getTotalMax() != null) {
						if (tAmount.doubleValue() > heritageSportsEventPackage.getTotalMax().intValue()) {
							siteAmount = heritageSportsEventPackage.getTotalMax().toString();
						}
					}
				} else {
					Double wagerAmount = Double.valueOf(siteAmount);
					float totalJuice = ae.getTotaljuice();
					Double risk = wagerAmount * (100 / totalJuice);
					risk = round(risk.doubleValue(), 2);
					if (heritageSportsEventPackage != null && heritageSportsEventPackage.getTotalMax() != null
							&& (risk.doubleValue() > heritageSportsEventPackage.getTotalMax().intValue())) {
						LOGGER.error("Risk: " + risk);
						LOGGER.error("heritageSportsEventPackage.getTotalMax().intValue(): "
								+ heritageSportsEventPackage.getTotalMax().intValue());
						siteAmount = heritageSportsEventPackage.getTotalMax().toString();
					} else {
						LOGGER.error("Risk: " + risk);
						siteAmount = risk.toString();
					}
				}
			}
		} else if (event instanceof MlRecordEvent) {
			siteTransaction = getMlTransaction((MlRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.debug("siteAmount: " + siteAmount);
			Double mAmount = Double.parseDouble(siteAmount);
			if (mAmount != null) {
				HeritageSportsEventPackage heritageSportsEventPackage = (HeritageSportsEventPackage)eventPackage;
				LOGGER.debug("heritageSportsEventPackage: " + heritageSportsEventPackage);
				
				if (siteTransaction.getRiskorwin() != null && siteTransaction.getRiskorwin().intValue() == 1) {
					if (heritageSportsEventPackage != null && heritageSportsEventPackage.getMlMax() != null) {
						if (mAmount.doubleValue() > heritageSportsEventPackage.getMlMax().intValue()) {
							siteAmount = heritageSportsEventPackage.getMlMax().toString();
						}
					}
				} else {
					Double wagerAmount = Double.valueOf(siteAmount);
					float mlJuice = ae.getMljuice();
					Double risk = wagerAmount * (100 / mlJuice);
					risk = round(risk.doubleValue(), 2);
					if (heritageSportsEventPackage != null && heritageSportsEventPackage.getMlMax() != null
							&& (risk.doubleValue() > heritageSportsEventPackage.getMlMax().intValue())) {
						LOGGER.error("Risk: " + risk);
						LOGGER.error("heritageSportsEventPackage.getMlMax().intValue(): "
								+ heritageSportsEventPackage.getMlMax().intValue());
						siteAmount = heritageSportsEventPackage.getMlMax().toString();
					} else {
						LOGGER.error("Risk: " + risk);
						siteAmount = risk.toString();
					}
				}
			}
		}

		// Setup the post parameters
		List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		MAP_DATA.put(siteTransaction.getInputName(), siteAmount);
		ae.setActualamount(siteAmount);

		// Now check if selection needs to be sent as well
		if (siteTransaction.getSelectName() != null && siteTransaction.getOptionValue() != null) {
			MAP_DATA.put(siteTransaction.getSelectName(), siteTransaction.getOptionValue());
		}

		// Continue button
		MAP_DATA.put("submit1", "Continue");

		// Setup post parameters and get action URL
		final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("actionName: " + actionName);

		// Setup the wager
		String xhtml = postSite(actionName, postNameValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

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

		// Parse the event selection
		MAP_DATA = HSP.parseEventSelection(xhtml, null, null);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Parse the wager types
		Map<String, String> wagers = HSP.parseWagerType(xhtml);
		LOGGER.debug("wagers: " + wagers);

		// Check for a wager limit and rerun
		if (wagers.containsKey("wagerminamount")) {
			throw new BatchException(BatchErrorCodes.MIN_WAGER_AMOUNT_NOT_REACHED, BatchErrorMessage.MIN_WAGER_AMOUNT_NOT_REACHED, "The minimum wager amount has NOT been reached " + wagers.get("wagerminamount"));
		}

		// Check for a wager limit and rerun
		if (wagers.containsKey("wageramount")) {
			// Only call the select event once
			if (rerunCount++ == 0) {
				if (wagers.containsKey("goback")) {
					final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
					final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
					super.getSite(actionName);
				}
				String wagerAmount = wagers.get("wageramount");
				if (wagerAmount != null) {
					wagerAmount.replace(",", "");
				}
				siteTransaction.setAmount(wagerAmount);
				final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
				MAP_DATA.put("wagerAmt", wagerAmount);
				ae.setActualamount(wagerAmount);

				// Now check if selection needs to be sent as well
				if (siteTransaction.getSelectName() != null && siteTransaction.getOptionValue() != null) {
					MAP_DATA.put(siteTransaction.getSelectName(), siteTransaction.getOptionValue());
				}

				// Check for risk or win
				if ("1".equals(event.getWtype())) {
					postNameValuePairs.add(new BasicNameValuePair("radiox", "riskType"));
				} else {
					postNameValuePairs.add(new BasicNameValuePair("radiox", "toWinType"));
				}
				final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
				LOGGER.debug("ActionName: " + actionName);

				// Setup the wager
				xhtml = postSite(actionName, postNameValuePairs);

				// Parse the Confirm Wager page
				MAP_DATA = HSP.parseWagerType(xhtml);

				// Check for a wager limit and rerun
				if (MAP_DATA.containsKey("wageramount")) {
					throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The maximum wager amount has been reached $" + MAP_DATA.get("wageramount"), xhtml);
				} else if (MAP_DATA.containsKey("wagerminamount")) {
					throw new BatchException(BatchErrorCodes.MIN_WAGER_AMOUNT_NOT_REACHED, BatchErrorMessage.MIN_WAGER_AMOUNT_NOT_REACHED, "The minimum wager amount has NOT been reached $" + MAP_DATA.get("wagerminamount"), xhtml);
				} else if (MAP_DATA.containsKey("wagerbalanceexceeded")) {
					throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "Site account balance exceeded for transaction in the amount of $" + siteTransaction.getAmount(), xhtml);
				}
			}
		} else {
			MAP_DATA = wagers;
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
			if (processTransaction) {
				// Get the confirmation
				xhtml = processWager();
				LOGGER.debug("XHTML: " + xhtml);
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

		final String ticketNumber = HSP.parseTicketNumber(xhtml);
		LOGGER.debug("ticketNumber: " + ticketNumber);

		LOGGER.info("Exiting parseTicketTransaction()");
		return ticketNumber;		
	}

	/**
	 * 
	 * @return
	 * @throws BatchException
	 */
	private String processWager() throws BatchException {
		LOGGER.info("Entering processWager()");

		// Set password
		MAP_DATA.put("password", this.httpClientWrapper.getPassword());
		MAP_DATA.put("submit1", "Submit+Password");
		MAP_DATA.remove("risk");
		MAP_DATA.remove("win");

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		String actionUrl = setupNameValuesEmpty(postValuePairs, MAP_DATA, null);

		// Call check acceptance
		String xhtml = postSite(actionUrl, postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);
/*
		if (!xhtml.contains("Wager has been accepted!")) {
			// Sleep for 3 seconds
			sleepAsUser(30000);

			// Call it again
			xhtml = getSite(super.populateUrl("ProcessWagerRelay.asp"));
			if (!xhtml.contains("Wager has been accepted!")) {
				// Sleep for 5 seconds
				sleepAsUser(3000);
				// Call it again
				xhtml = getSite(super.populateUrl("ProcessWager.asp"));
			}
		}
*/

		LOGGER.info("Exiting processWager()");
		return xhtml;
	}
}