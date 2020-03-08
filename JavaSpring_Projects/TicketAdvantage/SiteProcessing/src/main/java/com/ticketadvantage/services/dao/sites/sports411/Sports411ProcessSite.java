/**
 * 
 */
package com.ticketadvantage.services.dao.sites.sports411;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.ProxyContainer;
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
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class Sports411ProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(Sports411ProcessSite.class);
	protected final Sports411Parser S411P = new Sports411Parser();
	private int rerunCount = 0;

	/**
	 * 
	 */
	public Sports411ProcessSite(String host, String username, String password) {
		super("Sports411", host, username, password);
		LOGGER.info("Entering Sports411ProcessSite()");

		// Setup the parser
		this.siteParser = S411P;

		// Menu items
		NFL_LINES_SPORT = new String[] { "FOOTBALL" };
		NFL_LINES_NAME = new String[] { "NFL" };
		NFL_FIRST_SPORT = new String[] { "FOOTBALL" };
		NFL_FIRST_NAME = new String[] { "NFL - 1ST HALVES" };
		NFL_SECOND_SPORT = new String[] { "FOOTBALL" };
		NFL_SECOND_NAME = new String[] { "NBA - 2ND HALVES" };
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
		NCAAB_LINES_NAME = new String[] { "COLLEGE BASKETBALL" };
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

		LOGGER.info("Exiting Sports411ProcessSite()");
	}

	/**
	 * 
	 * @param accountSoftware
	 * @param host
	 * @param username
	 * @param password
	 */
	public Sports411ProcessSite(String accountSoftware, String host, String username, String password) {
		super(accountSoftware, host, username, password);
		LOGGER.info("Entering Sports411ProcessSite()");
		LOGGER.info("Exiting Sports411ProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] {
				{ "http://www.sports411.ag", "8660", "rq7", "100", "100", "100", "Los Angeles", "PT"}
			};

			/*
			final TDSportsProcessSite processSite = new TDSportsProcessSite(TDSITES[0][0], TDSITES[0][1],
					TDSITES[0][2]);
		    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = TDSITES[0][7];

			processSite.loginToSite(processSite.httpClientWrapper.getUsername(), processSite.httpClientWrapper.getPassword());
			*/

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final Sports411ProcessSite processSite = new Sports411ProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2]);
				final Sports411ProcessSite processSite2 = new Sports411ProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2]);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];

			    processSite2.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite2.processTransaction = false;
			    processSite2.timezone = TDSITES[0][7];

				// Now call the test suite
				Sports411ProcessSite.testSite2(processSite, processSite2, i, TDSITES);
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
		S411P.setTimezone(timezone);

		// Call the second page
		String xhtml = super.getSite(httpClientWrapper.getHost());
		LOGGER.debug("XHTML: " + xhtml);

		// Get home page data
		MAP_DATA = S411P.parseIndex(xhtml);
		MAP_DATA.put("Submit1.x", "71");
		MAP_DATA.put("Submit1.y", "13");

		// Setup the username/password data
		setUsernamePassword(username, password);		
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, "");
		LOGGER.debug("ActionLogin: " + actionLogin);

		// Call the login
		xhtml = authenticate(actionLogin, postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		// WebappName
		httpClientWrapper.setWebappname(determineWebappName(httpClientWrapper.getPreviousUrl()));

		// Parse login
		MAP_DATA = S411P.parseLogin2(xhtml, "/login.aspx");
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		postValuePairs = new ArrayList<NameValuePair>(1);
		actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, "");
		LOGGER.debug("ActionLogin: " + actionLogin);

		if (actionLogin != null) {
//			sleepAsUser(300);
			// Call post login form
			xhtml = postSite(actionLogin, postValuePairs);
			LOGGER.debug("XHTML: " + xhtml);

			// Parse login
			MAP_DATA = S411P.parseLogin2(xhtml, "login.aspx");
			LOGGER.debug("MAP_DATA: " + MAP_DATA);

			postValuePairs = new ArrayList<NameValuePair>(1);
			actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, "");
			LOGGER.debug("ActionLogin: " + actionLogin);

//			sleepAsUser(300);
			// Call post login form
			xhtml = postSite(actionLogin, postValuePairs);
			LOGGER.debug("XHTML: " + xhtml);

			// Parse login
			MAP_DATA = S411P.parseLogin2(xhtml, "login.aspx");
			LOGGER.debug("MAP_DATA: " + MAP_DATA);

			postValuePairs = new ArrayList<NameValuePair>(1);
			actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, "");
			LOGGER.debug("ActionLogin: " + actionLogin);

			// Call post login form
			xhtml = postSite(actionLogin, postValuePairs);
			LOGGER.debug("XHTML: " + xhtml);
		}

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

		final EventPackage eventPackage = setupTransaction(event,  ae); 
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
			// Step 11 - Sleep for 1 second
//			sleepAsUser(1000);

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

			// Step 14 - Sleep for 1 second
//			sleepAsUser(1000);

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
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		final String actionName = setupNameValuesEmpty(postValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionName: " + actionName);

		// Post to site page
		String xhtml = postSite(actionName, postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

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

		final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionName: " + actionName);

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
			if (processTransaction) {
				// Check if we have exceeded
				if (MAP_DATA.containsKey("wagerbalanceexceeded")) {
					throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, "The maximum account balance has been reached for wager " + siteTransaction.getAmount(), xhtml);
				}

				// Process the wager transaction
				xhtml = processWager(eventPackage, event, ae, event.getWtype());

				// Confirm the wager
				xhtml = confirmWager(xhtml, siteTransaction, eventPackage, event, ae, event.getWtype());
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
			LOGGER.debug("siteAmount: " + siteAmount);
			Double sAmount = Double.parseDouble(siteAmount);
			if (sAmount != null) {
				Sports411EventPackage sports411EventPackage = (Sports411EventPackage)eventPackage;
				LOGGER.debug("Sports411EventPackage: " + sports411EventPackage);
				if (sAmount.doubleValue() > sports411EventPackage.getSpreadMax().intValue()) {
					siteAmount = sports411EventPackage.getSpreadMax().toString();
				}
			}
		} else if (event instanceof TotalRecordEvent) {
			siteTransaction = getTotalTransaction((TotalRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.debug("siteAmount: " + siteAmount);
			Double tAmount = Double.parseDouble(siteAmount);
			if (tAmount != null) {
				Sports411EventPackage sports411EventPackage = (Sports411EventPackage)eventPackage;
				LOGGER.debug("Sports411EventPackage: " + sports411EventPackage);
				if (tAmount.doubleValue() > sports411EventPackage.getTotalMax().intValue()) {
					siteAmount = sports411EventPackage.getTotalMax().toString();
				}
			}
		} else if (event instanceof MlRecordEvent) {
			siteTransaction = getMlTransaction((MlRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.debug("siteAmount: " + siteAmount);
			Double mAmount = Double.parseDouble(siteAmount);
			if (mAmount != null) {
				Sports411EventPackage sports411EventPackage = (Sports411EventPackage)eventPackage;
				LOGGER.debug("Sports411EventPackage: " + sports411EventPackage);
				if (mAmount.doubleValue() > sports411EventPackage.getTotalMax().intValue()) {
					siteAmount = sports411EventPackage.getMlMax().toString();
				}
			}
		}

		LOGGER.debug("siteAmount: " + siteAmount);
		final List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		String key = checkMapKey("amt_");
		MAP_DATA.put(key, siteAmount);
		ae.setActualamount(siteAmount);

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

		final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionName: " + actionName);

		// Setup the wager
		final String xhtml = postSite(actionName, postNameValuePairs);

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
				xhtml = postSite(actionName, postNameValuePairs);
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
		final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionName: " + actionName);

		// Process the wager
		xhtml = postSite(actionName, postNameValuePairs);

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
				if (key.contains("UserName") || key.contains("account") || key.contains("Account")) {
					MAP_DATA.put(key, username);
				} else if (key.contains("password") || key.contains("Password")) {
					if (MAP_DATA.containsKey("toUpperCase") && MAP_DATA.get("toUpperCase").equals("true")) {
						MAP_DATA.put(key, password.toUpperCase());
					} else {
						MAP_DATA.put(key, password);
					}
				}
			}
		}

		// Remove the toUpperCase if there is one
		if (MAP_DATA.containsKey("toUpperCase") && MAP_DATA.get("toUpperCase").equals("true")) {
			MAP_DATA.remove("toUpperCase");
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