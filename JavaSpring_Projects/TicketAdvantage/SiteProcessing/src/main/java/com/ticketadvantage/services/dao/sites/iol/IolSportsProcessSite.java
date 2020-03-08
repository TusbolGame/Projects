/**
 * 
 */
package com.ticketadvantage.services.dao.sites.iol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class IolSportsProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(IolSportsProcessSite.class);
	private static final String LOGIN = "/login-submit";
	private static final String PENDING_BETS = "/open-bets";
	private static final String SELECT_SPORT = "BbGameSelection.asp";
	private final IolSportsParser ISP = new IolSportsParser();
	private int rerunCount = 0;

	/**
	 * 
	 */
	public IolSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("IolSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering IolSportsProcessSite()");

		// Setup the parser
		this.siteParser = ISP;
		ISP.setTimezone(this.timezone);

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_LINES_NAME = new String[] { "NFL" };
		NFL_FIRST_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_FIRST_NAME = new String[] { "1st Half" };
		NFL_SECOND_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_SECOND_NAME = new String[] { "2nd Half" };
		NCAAF_LINES_SPORT = new String[] { "NCAA FOOTBALL" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL" };
		NCAAF_FIRST_SPORT = new String[] { "College" };
		NCAAF_FIRST_NAME = new String[] { "1st Half" };
		NCAAF_SECOND_SPORT = new String[] { "College" };
		NCAAF_SECOND_NAME = new String[] { "2nd Half" };
		NBA_LINES_SPORT = new String[] { "NBA BASKETBALL" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "NBA BASKETBALL" };
		NBA_FIRST_NAME = new String[] { "NBA 1ST Half" };
		NBA_SECOND_SPORT = new String[] { "NBA BASKETBALL" };
		NBA_SECOND_NAME = new String[] { "2nd Half" };
		NCAAB_LINES_SPORT = new String[] { "NCAA BASKETBALL" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL - MEN" };
		NCAAB_FIRST_SPORT = new String[] { "NCAA BASKETBALL" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BASKETBALL 1ST HALF" };
		NCAAB_SECOND_SPORT = new String[] { "NCAA BASKETBALL" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BASKETBALL 2ND HALF" };
		NHL_LINES_SPORT = new String[] { "NHL HOCKEY" };
		NHL_LINES_NAME = new String[] { "NATIONAL HOCKEY LEAGUE" };
		NHL_FIRST_SPORT = new String[] { "NHL HOCKEY" };
		NHL_FIRST_NAME = new String[] { "1st Half" };
		NHL_SECOND_SPORT = new String[] { "NHL HOCKEY" };
		NHL_SECOND_NAME = new String[] { "2nd Half" };
		WNBA_LINES_SPORT = new String[] { "WNBA" };
		WNBA_LINES_NAME = new String[] { "Game" };
		WNBA_FIRST_SPORT = new String[] { "WNBA" };
		WNBA_FIRST_NAME = new String[] { "1st Half" };
		WNBA_SECOND_SPORT = new String[] { "WNBA" };
		WNBA_SECOND_NAME = new String[] { "2nd Half" };
		MLB_LINES_SPORT = new String[] { "MLB BASEBALL" };
		MLB_LINES_NAME = new String[] { "MLB" };
		MLB_FIRST_SPORT = new String[] { "MLB BASEBALL" };
		MLB_FIRST_NAME = new String[] { "1st 5 Innings" };
		MLB_SECOND_SPORT = new String[] { "MLB BASEBALL" };
		MLB_SECOND_NAME = new String[] { "MLB 2nd half lines" };
		LOGGER.info("Exiting IolSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] HSSITES = new String [][] {
				{ "http://engine.betsmart123.com", "BF272", "NR", "500", "500", "500", "New York", "ET" }
			};

			// Loop through the sites
			for (int i = 0; i < HSSITES.length; i++) {
				final IolSportsProcessSite processSite = new IolSportsProcessSite(HSSITES[i][0], HSSITES[i][1],
						HSSITES[i][2], false, false);
				processSite.processTransaction = false;

			    processSite.httpClientWrapper.setupHttpClient(HSSITES[i][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = HSSITES[i][7];
				processSite.loginToSite(HSSITES[i][1], HSSITES[i][2]);
				final Set<PendingEvent> pendingEvents = processSite.getPendingBets(HSSITES[i][0], HSSITES[i][1], new Object());

				if (pendingEvents != null) {
					final Iterator<PendingEvent> itr = pendingEvents.iterator();
					while (itr.hasNext()) {
						final PendingEvent pe = itr.next();
						if (pe.getDoposturl()) {
							processSite.doProcessPendingEvent(pe);
						}
						System.out.println("PendingEventXXX: " + pe);
					}
				}
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

		if (xhtml != null && xhtml.contains("class=\"odd")) {
			pendingWagers = ISP.parsePendingBets(xhtml, accountName, accountId);
		} else if (xhtml != null && xhtml.contains("bp-login-submit")) {
			this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
			xhtml = getSite(super.populateUrl(PENDING_BETS));
			LOGGER.error("xhtml: " + xhtml);
			if (xhtml != null && xhtml.contains("<tr class=\\\"odd\\\">")) {
				pendingWagers = ISP.parsePendingBets(xhtml, accountName, accountId);
			}
		} else {
			LOGGER.debug("No pending wagers");
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
		ISP.setTimezone(timezone);

		// Get the home page
		String xhtml = getSite(httpClientWrapper.getHost());
		LOGGER.debug("XHTML: " + xhtml);

		// Get home page data
		MAP_DATA = ISP.parseIndex(xhtml);
		LOGGER.debug("Map: " + MAP_DATA);

		MAP_DATA = new HashMap<String, String>();
		MAP_DATA.put("username", username);
		MAP_DATA.put("password", password);
		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, null);

		xhtml = authenticate(httpClientWrapper.getHost() + LOGIN, postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		// Setup the webapp name
		httpClientWrapper.setWebappname("player");

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
				IolSportsEventPackage heritageSportsEventPackage = (IolSportsEventPackage)eventPackage;
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
						LOGGER.error("heritageSportsEventPackage.getSpreadMax().intValue(): "
								+ heritageSportsEventPackage.getSpreadMax().intValue());
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
				IolSportsEventPackage heritageSportsEventPackage = (IolSportsEventPackage)eventPackage;
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
						LOGGER.error("heritageSportsEventPackage.getTotalMax().intValue(): "
								+ heritageSportsEventPackage.getTotalMax().intValue());
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
				IolSportsEventPackage heritageSportsEventPackage = (IolSportsEventPackage)eventPackage;
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
						LOGGER.error("heritageSportsEventPackage.getMlMax().intValue(): "
								+ heritageSportsEventPackage.getMlMax().intValue());
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
		MAP_DATA = ISP.parseEventSelection(xhtml, null, null);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Parse the wager types
//		Map<String, String> wagers = ISP.parseWagerType(xhtml);
		Map<String, String> wagers = null;
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
//				MAP_DATA = ISP.parseWagerType(xhtml);

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

		final String ticketNumber = ISP.parseTicketNumber(xhtml);
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