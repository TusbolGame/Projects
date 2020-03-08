/**
 * 
 */
package com.ticketadvantage.services.dao.sites.elitesports;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteProcessor;
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
public class EliteSportsProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(EliteSportsProcessSite.class);
	private static final EliteSportsParser ESP = new EliteSportsParser();
	private static final String PROCESS_BET_VALIDATION = "ProcessWager.asp";
	private static final String PENDING_BETS = "players_bets.asp";

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @param isMobile
	 * @param showRequestResponse
	 */
	public EliteSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("EliteSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering EliteSportsProcessSite()");

		// Setup the parser
		this.siteParser = ESP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "Football" };
		NFL_LINES_NAME = new String[] { "NFL" };
		NFL_FIRST_SPORT = new String[] { "Football" };
		NFL_FIRST_NAME = new String[] { "NFL" };
		NFL_SECOND_SPORT = new String[] { "Football" };
		NFL_SECOND_NAME = new String[] { "NFL" };
		NCAAF_LINES_SPORT = new String[] { "Football" };
		NCAAF_LINES_NAME = new String[] { "NCAA" };
		NCAAF_FIRST_SPORT = new String[] { "Football" };
		NCAAF_FIRST_NAME = new String[] { "NCAA" };
		NCAAF_SECOND_SPORT = new String[] { "Football" };
		NCAAF_SECOND_NAME = new String[] { "NCAA" };
		NBA_LINES_SPORT = new String[] { "Basketball" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "Basketball" };
		NBA_FIRST_NAME = new String[] { "NBA" };
		NBA_SECOND_SPORT = new String[] { "Basketball" };
		NBA_SECOND_NAME = new String[] { "NBA" };
		NCAAB_LINES_SPORT = new String[] { "Basketball" };
		NCAAB_LINES_NAME = new String[] { "NCAA" };
		NCAAB_FIRST_SPORT = new String[] { "Basketball" };
		NCAAB_FIRST_NAME = new String[] { "NCAA" };
		NCAAB_SECOND_SPORT = new String[] { "Basketball" };
		NCAAB_SECOND_NAME = new String[] { "NCAA" };
		NHL_LINES_SPORT = new String[] { "Hockey" };
		NHL_LINES_NAME = new String[] { "NHL" };
		NHL_FIRST_SPORT = new String[] { "Hockey" };
		NHL_FIRST_NAME = new String[] { "NHL" };
		NHL_SECOND_SPORT = new String[] { "Hockey" };
		NHL_SECOND_NAME = new String[] { "NHL" };
		WNBA_LINES_SPORT = new String[] { "Basketball" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_FIRST_SPORT = new String[] { "Basketball" };
		WNBA_FIRST_NAME = new String[] { "WNBA" };
		WNBA_SECOND_SPORT = new String[] { "Basketball" };
		WNBA_SECOND_NAME = new String[] { "WNBA" };
		MLB_LINES_SPORT = new String[] { "Baseball" };
		MLB_LINES_NAME = new String[] { "MLB", "Game" };
		MLB_FIRST_SPORT = new String[] { "Baseball" };
		MLB_FIRST_NAME = new String[] { "MLB", "1st 5 Innings" };
		MLB_SECOND_SPORT = new String[] { "Baseball" };
		MLB_SECOND_NAME = new String[] { "MLB", "Last 4 Innings" };

		LOGGER.info("Exiting EliteSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] ESSITES = new String [][] {
//				{ "http://2betesi.com", "SL1066", "mike", "1000", "1000", "1000", "Los Angeles", "ET" }
				{ "http://zzgamblezz.com", "BLUE1072", "bowl", "1000", "1000", "1000", "New York", "ET" }				
			};

			// Loop through the sites
			for (int i = 0; i < ESSITES.length; i++) {
				final EliteSportsProcessSite processSite = new EliteSportsProcessSite(ESSITES[i][0], 
						ESSITES[i][1],
						ESSITES[i][2], 
						false, 
						true);

/*
			    processSite.httpClientWrapper.setupHttpClient(ESSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = ESSITES[0][7];

				processSite.loginToSite(processSite.httpClientWrapper.getUsername(), processSite.httpClientWrapper.getPassword());
				Set<PendingEvent> pendingEvents = processSite.getPendingBets(ESSITES[i][0], ESSITES[i][1], null);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					Iterator<PendingEvent> itr = pendingEvents.iterator();
					while (itr.hasNext()) {
						System.out.println("PendingEvent: " + itr.next());
					}
				}
*/

			    processSite.httpClientWrapper.setupHttpClient(ESSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = ESSITES[0][7];
			    processSite.siteParser = processSite.ESP;
			    processSite.siteParser.setTimezone(ESSITES[0][7]);
			    processSite.ESP.setTimezone(ESSITES[0][7]);
				String xhtml = processSite.loginToSite(ESSITES[i][1], ESSITES[i][2]);

				Map<String, String> map = processSite.getMenuFromSite("mlbfirst", xhtml);
				LOGGER.debug("map: " + map);

				xhtml = processSite.selectSport("mlbfirst");
				LOGGER.debug("XHTML: " + xhtml);

				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("mlbfirst", xhtml);

				MlRecordEvent mre = new MlRecordEvent();
				mre.setMlplusminusfirstone("-");
				mre.setMlinputfirstone("110");
				mre.setId(new Long(951));
				mre.setEventname("1H WAS NATIONALS");
				mre.setEventtype("ml");
				mre.setSport("mlbfirst");
				mre.setUserid(new Long(6));
				mre.setEventid(new Integer(951));
				mre.setEventid1(new Integer(951));
				mre.setEventid2(new Integer(952));
				mre.setRotationid(new Integer(951));

				// Step 9 - Find event
				EventPackage eventPackage = processSite.findEvent(ep, mre);
				if (eventPackage == null) {
					throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE,
							"Game cannot be found on site for " + mre.getEventname(), xhtml);
				}
				LOGGER.error("eventPackage: " + eventPackage);

				// Now call the test suite
			//	EliteSportsProcessSite.testSite(processSite, i, ESSITES);
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
		ESP.setTimezone(timezone);

		// Get the home page
		String xhtml = getSite(httpClientWrapper.getHost());
		LOGGER.debug("XHTML: " + xhtml);

		// Get home page data
		MAP_DATA = ESP.parseIndex(xhtml);
		httpClientWrapper.setWebappname(determineWebappName(MAP_DATA.get("action")));

		// setup the webapp name
		if (httpClientWrapper.getWebappname() == null) {
			httpClientWrapper.setWebappname("");
		}

		// Setup the customer ID
		if (MAP_DATA.containsKey("customerID")) {
			MAP_DATA.put("customerID", username);	
		} else if (MAP_DATA.containsKey("CustomerID")) {
			MAP_DATA.put("CustomerID", username);
		} else if (MAP_DATA.containsKey("customerid")) {
			MAP_DATA.put("customerid", username);
		}
		// Setup the password
		if (MAP_DATA.containsKey("password")) {
			MAP_DATA.put("password", password);	
		} else if (MAP_DATA.containsKey("Password")) {
			MAP_DATA.put("Password", password);
		}
		LOGGER.debug("Map: " + MAP_DATA);

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		String actionName = setupNameValuesEmpty(postValuePairs, MAP_DATA, null);
		LOGGER.debug("ActionName: " + actionName);

		xhtml = authenticate(actionName, postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);
		String host = super.httpClientWrapper.getHost();
		host = host.replace("..", "");
		super.httpClientWrapper.setHost(host);

		// Parse the login
		MAP_DATA = ESP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATEZZZ: " + MAP_DATA);
		
		if (MAP_DATA.containsKey("action")) {
			postValuePairs = new ArrayList<NameValuePair>(1);
			if (MAP_DATA.containsKey("SportType")) {
				MAP_DATA.put("SportType", "Straight");
			}

			actionName = setupNameValuesEmpty(postValuePairs, MAP_DATA, null);
			actionName = actionName.replace("..", "");
			LOGGER.debug("ActionName123: " + actionName);
			host = super.httpClientWrapper.getHost();
			host = host.replace("..", "");
			super.httpClientWrapper.setHost(host);

			// Call the message or wager
			if (actionName.contains("WagerMenu.asp")) {
				xhtml = getSite(actionName);
			} else {
				final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>();
				headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
				headerValuePairs.add(new BasicNameValuePair("Referer", super.httpClientWrapper.getPreviousUrl()));
				headerValuePairs.add(new BasicNameValuePair("Host", super.httpClientWrapper.getHost()));
				final List<NameValuePair> retValue = super.httpClientWrapper.postSitePageNoRedirect(actionName, null, postValuePairs, headerValuePairs);

				String location = "";
				// Get all new cookies and the XHTML for website
				if (retValue != null && !retValue.isEmpty()) {
					final Iterator<NameValuePair> itr = retValue.iterator();
					while (itr != null && itr.hasNext()) {
						final NameValuePair nvp = (NameValuePair) itr.next();
						LOGGER.info("Header Name: " + nvp.getName());
						if ("Location".equals(nvp.getName())) {
							location = nvp.getValue();
						} else if ("xhtml".equals(nvp.getName())) {
							xhtml = nvp.getValue();
						}
					}
				}

				if (location != null && location.length() > 0) {
					location = location.replace("..", "");
					actionName = actionName.replace("..", "");
					xhtml = getSite(actionName);
					// xhtml = getSite(location);
				}
				LOGGER.error("xhtml: " + xhtml);
			}

			MAP_DATA = ESP.parseLogin(xhtml);
			LOGGER.debug("MAP_DATEXXX: " + MAP_DATA);
/*
			// Call the WagerMenu
			if (MAP_DATA.containsKey("action") && !"SbGameSelection.asp".equals(MAP_DATA.containsKey("action"))) {
				postValuePairs = new ArrayList<NameValuePair>(1);
				if (MAP_DATA.containsKey("SportType")) {
					MAP_DATA.put("SportType", "Straight");
				}

				if (MAP_DATA.containsKey("action")) {
					String aName = MAP_DATA.get("action");
					aName = aName.replace("..", "");
					LOGGER.debug("aName: " + aName);
					MAP_DATA.put("action", aName);
				}
				actionName = setupNameValuesEmpty(postValuePairs, MAP_DATA, null);
				LOGGER.debug("ActionName: " + actionName);

				// Call the WagerMenu
				xhtml = postSite(actionName, postValuePairs);
				MAP_DATA = ESP.parseLogin(xhtml);
			}
*/
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
		LOGGER.info("Entering selectSport()");
		LOGGER.debug("type: " + type);
		
		// Process the setup page
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);

		String actionName = setupNameValuesEmpty(postValuePairs, MAP_DATA, null);
		LOGGER.debug("ActionName: " + actionName);
		actionName = actionName.replace("..", "");

		// Get the menu
		final String xhtml = postSite(actionName, postValuePairs);

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

		final EliteSportsTransaction agSoftwareTransaction = new EliteSportsTransaction();

		LOGGER.info("Exiting createSiteTransaction()");
		return agSoftwareTransaction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#selectEvent(com.ticketadvantage.services.dao.sites.SiteTransaction, com.ticketadvantage.services.model.EventPackage, com.ticketadvantage.services.model.BaseRecordEvent, com.ticketadvantage.services.model.AccountEvent)
	 */
	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering selectEvent()");
		EliteSportsTeamPackage teamPackage = null;

		// Only get the first one because that's all there is for TDSports at this point
		final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);

		// Get the appropriate record event
		if (event instanceof SpreadRecordEvent) {			
			teamPackage = (EliteSportsTeamPackage)SiteWagers.setupTeam(SiteWagers.determineSpreadData((SpreadRecordEvent)event), eventPackage);
			postNameValuePairs.add(new BasicNameValuePair(teamPackage.getGameSpreadInputName(), teamPackage.getGameSpreadInputValue()));
		} else if (event instanceof TotalRecordEvent) {
			teamPackage = (EliteSportsTeamPackage)SiteWagers.setupTeam(SiteWagers.determineTotalData((TotalRecordEvent)event), eventPackage);
			postNameValuePairs.add(new BasicNameValuePair(teamPackage.getGameTotalInputName(), teamPackage.getGameTotalInputValue()));
		} else if (event instanceof MlRecordEvent) {
			teamPackage = (EliteSportsTeamPackage)SiteWagers.setupTeam(SiteWagers.determineMoneyLineData((MlRecordEvent)event), eventPackage);
			postNameValuePairs.add(new BasicNameValuePair(teamPackage.getGameMLInputName(), teamPackage.getGameMLInputValue().get("0")));
		}

		String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionName: " + actionName);
		actionName = actionName.replace("..", "");

		// Setup the wager
		final String xhtml = postSite(actionName, postNameValuePairs);

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
		EliteSportsTeamPackage teamPackage = null;

		MAP_DATA.clear();
		if (event instanceof SpreadRecordEvent) {			
			teamPackage = (EliteSportsTeamPackage)SiteWagers.setupTeam(SiteWagers.determineSpreadData((SpreadRecordEvent)event), eventPackage);
			MAP_DATA = ESP.parseEventSelection(xhtml, teamPackage, "spread");
		} else if (event instanceof TotalRecordEvent) {
			teamPackage = (EliteSportsTeamPackage)SiteWagers.setupTeam(SiteWagers.determineTotalData((TotalRecordEvent)event), eventPackage);
			MAP_DATA = ESP.parseEventSelection(xhtml, teamPackage, "total");
		} else if (event instanceof MlRecordEvent) {
			teamPackage = (EliteSportsTeamPackage)SiteWagers.setupTeam(SiteWagers.determineMoneyLineData((MlRecordEvent)event), eventPackage);
			MAP_DATA = ESP.parseEventSelection(xhtml, teamPackage, "ml");
		}
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Check for a wager limit and rerun
		if (MAP_DATA.containsKey("wageramount")) {
			throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The maximum wager amount has been reached $" + MAP_DATA.get("wageramount"), xhtml);
		}
		// Check for a wager limit and rerun
		if (MAP_DATA.containsKey("wagerminamount")) {
			throw new BatchException(BatchErrorCodes.MIN_WAGER_AMOUNT_NOT_REACHED, BatchErrorMessage.MIN_WAGER_AMOUNT_NOT_REACHED, "The minimum wager amount has NOT been reached $" + MAP_DATA.get("wagerminamount"), xhtml);
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
			// Process the wager transaction
			xhtml = processWager(eventPackage, event, ae, event.getWtype());

			if (processTransaction) {
				// Confirm the wager
				xhtml = confirmWager(xhtml);
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
		
		final String ticketNumber = ESP.parseTicketNumber(xhtml);
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
	private String processWager(EventPackage eventPackage, BaseRecordEvent event, AccountEvent ae, String wagerType) throws BatchException {
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
				final EliteSportsEventPackage eliteSportsEventPackage = (EliteSportsEventPackage)eventPackage;
				LOGGER.debug("EliteSportsEventPackage: " + eliteSportsEventPackage);
				if (siteTransaction.getRiskorwin().intValue() == 1 && eliteSportsEventPackage.getSpreadMax() != null) {
					if (sAmount.doubleValue() > eliteSportsEventPackage.getSpreadMax().intValue()) {
						siteAmount = eliteSportsEventPackage.getSpreadMax().toString();
					}
				} else if (siteTransaction.getRiskorwin().intValue() == 2) {
					Double wagerAmount = Double.valueOf(siteAmount);
					float spreadJuice = ae.getSpreadjuice();
					Double risk = wagerAmount * (100 / spreadJuice);
					risk = round(risk.doubleValue(), 2);
					LOGGER.error("Risk: " + risk);
					if (eliteSportsEventPackage.getSpreadMax() != null && risk.doubleValue() > eliteSportsEventPackage.getSpreadMax().intValue()) {
						siteAmount = eliteSportsEventPackage.getSpreadMax().toString();
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
				final EliteSportsEventPackage eliteSportsEventPackage = (EliteSportsEventPackage)eventPackage;
				LOGGER.debug("EliteSportsEventPackage: " + eliteSportsEventPackage);
				if (siteTransaction.getRiskorwin().intValue() == 1 && eliteSportsEventPackage.getTotalMax() != null) {
					if (tAmount.doubleValue() > eliteSportsEventPackage.getTotalMax().intValue()) {
						siteAmount = eliteSportsEventPackage.getTotalMax().toString();
					}
				} else if (siteTransaction.getRiskorwin().intValue() == 2) {
					Double wagerAmount = Double.valueOf(siteAmount);
					float totalJuice = ae.getTotaljuice();
					Double risk = wagerAmount * (100 / totalJuice);
					risk = round(risk.doubleValue(), 2);
					LOGGER.error("Risk: " + risk);
					if (eliteSportsEventPackage.getTotalMax() != null && risk.doubleValue() > eliteSportsEventPackage.getTotalMax().intValue()) {
						siteAmount = eliteSportsEventPackage.getTotalMax().toString();
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
				final EliteSportsEventPackage eliteSportsEventPackage = (EliteSportsEventPackage)eventPackage;
				LOGGER.debug("EliteSportsEventPackage: " + eliteSportsEventPackage);
				if (siteTransaction.getRiskorwin().intValue() == 1 && eliteSportsEventPackage.getMlMax() != null) {
					if (mAmount.doubleValue() > eliteSportsEventPackage.getMlMax().intValue()) {
						siteAmount = eliteSportsEventPackage.getMlMax().toString();
					}
				} else if (siteTransaction.getRiskorwin().intValue() == 2) {
					Double wagerAmount = Double.valueOf(siteAmount);
					float mlJuice = ae.getMljuice();
					Double risk = wagerAmount * (100 / mlJuice);
					risk = round(risk.doubleValue(), 2);
					LOGGER.error("Risk: " + risk);
					if (eliteSportsEventPackage.getMlMax() != null && risk.doubleValue() > eliteSportsEventPackage.getMlMax().intValue()) {
						siteAmount = eliteSportsEventPackage.getMlMax().toString();
					} else {
						siteAmount = risk.toString();
					}
				}
			}
		}

		LOGGER.debug("siteAmount: " + siteAmount);
		final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		MAP_DATA.put("wagerAmt", siteAmount);
		ae.setActualamount(siteAmount);

		// Now check if selection needs to be sent as well
		if (siteTransaction.getSelectName() != null && siteTransaction.getOptionValue() != null) {
			MAP_DATA.put(siteTransaction.getSelectName(), siteTransaction.getOptionValue());
		}

		// Check for risk or win
		if ("1".equals(wagerType)) {
			MAP_DATA.put("radiox", "riskType");
		} else {
			MAP_DATA.put("radiox", "toWinType");
		}

		String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionName: " + actionName);
		actionName = actionName.replace("..", "");

		// Setup the wager
		final String xhtml = postSite(actionName, postNameValuePairs);

		LOGGER.info("Exiting processWager()");
		return xhtml;
	}

	/**
	 * 
	 * @param htmlData
	 * @return
	 * @throws BatchException
	 */
	private String confirmWager(String xhtml) throws BatchException {
		LOGGER.info("Entering confirmWager()");

		// Parse the Confirm Wager page
		MAP_DATA = ESP.parseConfirmWager(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Check for a wager limit and rerun
		if (MAP_DATA.containsKey("wageramount")) {
			throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The maximum wager amount has been reached " + MAP_DATA.get("wageramount"), xhtml);
		}
		// Check for a wager limit and rerun
		if (MAP_DATA.containsKey("wagerminamount")) {
			throw new BatchException(BatchErrorCodes.MIN_WAGER_AMOUNT_NOT_REACHED, BatchErrorMessage.MIN_WAGER_AMOUNT_NOT_REACHED, "The minimum wager amount has NOT been reached $" + MAP_DATA.get("wagerminamount"), xhtml);
		}

		// Check for a valid password and upper case or not
		if (MAP_DATA.containsKey("password")) {
			if (MAP_DATA.containsKey("toUpperCase") && MAP_DATA.get("toUpperCase").equals("true")) {
				MAP_DATA.put("password", httpClientWrapper.getPassword().toUpperCase());
				MAP_DATA.remove("toUpperCase");
			} else {
				MAP_DATA.put("password", httpClientWrapper.getPassword());
			}
		} else if (MAP_DATA.containsKey("Password")) {
			if (MAP_DATA.containsKey("toUpperCase") && MAP_DATA.get("toUpperCase").equals("true")) {
				MAP_DATA.put("Password", httpClientWrapper.getPassword().toUpperCase());
				MAP_DATA.remove("toUpperCase");
			} else {
				MAP_DATA.put("Password", httpClientWrapper.getPassword());
			}
		}

		// Get the action
		String host = super.httpClientWrapper.getHost();
		host = host.replace("..", "");
		super.httpClientWrapper.setHost(host);

		final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		if (actionName != null) {
			actionName = actionName.replace("..", "");
			LOGGER.debug("ActionName: " + actionName);
		}

		// Process the wager
		final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>();
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", super.httpClientWrapper.getPreviousUrl()));
		headerValuePairs.add(new BasicNameValuePair("Host", super.httpClientWrapper.getHost()));
		final List<NameValuePair> retValue = super.httpClientWrapper.postSitePageNoRedirect(actionName, null, postNameValuePairs, headerValuePairs);

		String location = "";
		// Get all new cookies and the XHTML for website
		if (retValue != null && !retValue.isEmpty()) {
			final Iterator<NameValuePair> itr = retValue.iterator();
			while (itr != null && itr.hasNext()) {
				final NameValuePair nvp = (NameValuePair) itr.next();
				LOGGER.info("Header Name: " + nvp.getName());
				if ("Location".equals(nvp.getName())) {
					location = nvp.getValue();
				} else if ("xhtml".equals(nvp.getName())) {
					xhtml = nvp.getValue();
				}
			}
		}
		LOGGER.debug("location: " + location);

		if (location != null && location.length() > 0) {
			location = super.populateUrl(location);
			location = location.replace("..", "");
			xhtml = getSite(location);
		}
		LOGGER.error("xhtml: " + xhtml);

		if (!xhtml.contains("Wager has been accepted")) {
			// Sleep for 5 seconds
			sleepAsUser(5000);

			// Call it again
			String url = super.populateUrl(PROCESS_BET_VALIDATION);
			url = url.replace("..", "");
			xhtml = getSite(url);
			LOGGER.debug("xhtml: " + xhtml);

			if (!xhtml.contains("Wager has been accepted")) {
				// Sleep for 5 seconds
				sleepAsUser(5000);

				// Call it again
				url = super.populateUrl(PROCESS_BET_VALIDATION);
				url = url.replace("..", "");
				xhtml = getSite(url);

				LOGGER.debug("xhtml: " + xhtml);
			}
		}

		LOGGER.info("Exiting confirmWager()");
		return xhtml;
	}
}