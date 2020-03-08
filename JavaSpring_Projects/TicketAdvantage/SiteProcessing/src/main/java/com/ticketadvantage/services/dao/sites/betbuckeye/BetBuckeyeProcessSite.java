/**
 * 
 */
package com.ticketadvantage.services.dao.sites.betbuckeye;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.captcha.CaptchaWorker;
import com.ticketadvantage.services.captcha.GetCaptchaText;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.dao.sites.sportsinsights.SportsInsightsSite;
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
public class BetBuckeyeProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(BetBuckeyeProcessSite.class);
	private static final String PENDING_BETS = "SpecificPlayerPendingWagers.php";
	private final BetBuckeyeParser BBP = new BetBuckeyeParser();
	private int rerunCount = 0;
	private boolean resetConnection = false;

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 */
	public BetBuckeyeProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("BetBuckeye", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering BetBuckeyeProcessSite()");

		// Setup the parser
		this.siteParser = BBP;

		// Setup the menu items
		NFL_LINES_NAME = new String[] { "FOOTBALL_NFL_Game_", "FOOTBALL_NFL@20;PRESEAS_Game_" };
		NFL_LINES_SPORT = new String[] { "FOOTBALL" };
		NFL_FIRST_NAME = new String[] { "FOOTBALL_NFL_1st@20;Half_", "FOOTBALL_NFL@20;PRESEAS_1st@20;Half_" };
		NFL_FIRST_SPORT = new String[] { "FOOTBALL" };
		NFL_SECOND_NAME = new String[] { "FOOTBALL_NFL_2nd@20;Half_", "FOOTBALL_NFL@20;PRESEAS_2nd@20;Half_" };
		NFL_SECOND_SPORT = new String[] { "FOOTBALL" };
		NCAAF_LINES_NAME = new String[] { "FOOTBALL_COLLEGE_Game_", "FOOTBALL_COLLEXT_Game_" };
		NCAAF_LINES_SPORT = new String[] { "FOOTBALL" };
		NCAAF_FIRST_NAME = new String[] { "FOOTBALL_COLLEGE_1st@20;Half_", "FOOTBALL_COLLEXT_1st@20;Half_" };
		NCAAF_FIRST_SPORT = new String[] { "FOOTBALL" };
		NCAAF_SECOND_NAME = new String[] { "FOOTBALL_COLLEGE_2nd@20;Half_", "FOOTBALL_COLLEXT_2nd@20;Half_" };
		NCAAF_SECOND_SPORT = new String[] { "FOOTBALL" };
		NBA_LINES_NAME = new String[] { "BASKETBALL_NBA_Game_", "BASKETBALL_NBA@20;PRESEASO_Game_" };
		NBA_LINES_SPORT = new String[] { "BASKETBALL" };
		NBA_FIRST_NAME = new String[] { "BASKETBALL_NBA_1st@20;Half_" };
		NBA_FIRST_SPORT = new String[] { "BASKETBALL" };
		NBA_SECOND_NAME = new String[] { "BASKETBALL_NBA_2nd@20;Half_" };
		NBA_SECOND_SPORT = new String[] { "BASKETBALL" };
		NCAAB_LINES_SPORT = new String[] { "BASKETBALL" };
		NCAAB_LINES_NAME = new String[] { "BASKETBALL_NCAA_Game_", "BASKETBALL_NCAA@20;ADDED_Game_", "BASKETBALL_NCAA@20;EXTRA_Game_" }; 
		NCAAB_FIRST_SPORT = new String[] { "BASKETBALL" };
		NCAAB_FIRST_NAME = new String[] { "BASKETBALL_NCAA_1st@20;Half_", "BASKETBALL_NCAA@20;ADDED_1st@20;Half_", "BASKETBALL_NCAA@20;EXTRA_1st@20;Half_" };
		NCAAB_SECOND_SPORT = new String[] { "BASKETBALL" };
		NCAAB_SECOND_NAME = new String[] { "BASKETBALL_NCAA_2nd@20;Half_", "BASKETBALL_NCAA@20;ADDED_2nd@20;Half_", "BASKETBALL_NCAA@20;EXTRA_2nd@20;Half_" };
		NHL_LINES_NAME = new String[] { "HOCKEY_NHL_Game_", "HOCKEY_NHL@20;ADDED_Game_", "HOCKEY_NHL@20;EXTRA_Game_" }; 
		NHL_LINES_SPORT = new String[] { "HOCKEY" };		
		NHL_FIRST_NAME = new String[] { "HOCKEY_NHL_1st@20;Period_", "HOCKEY_NHL_1st@20;Half_", "HOCKEY_NHL@20;ADDED_1st@20;Half_", "HOCKEY_NHL@20;EXTRA_1st@20;Half_" };
		NHL_FIRST_SPORT = new String[] { "HOCKEY" };
		NHL_SECOND_NAME = new String[] { "HOCKEY_NHL_2nd@20;Period_", "HOCKEY_NHL_2nd@20;Half_", "HOCKEY_NHL@20;ADDED_2nd@20;Half_", "HOCKEY_NHL@20;EXTRA_2nd@20;Half_" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY" };
		WNBA_LINES_NAME = new String[] { "BASKETBALL_WNBA_Game_" };
		WNBA_LINES_SPORT = new String[] { "BASKETBALL" };
		WNBA_FIRST_NAME = new String[] { "BASKETBALL_WNBA_1st@20;Half_" };
		WNBA_FIRST_SPORT = new String[] { "BASKETBALL" };
		WNBA_SECOND_NAME = new String[] { "BASKETBALL_WNBA_2nd@20;Half_" };
		WNBA_SECOND_SPORT = new String[] { "BASKETBALL" };
		MLB_LINES_NAME = new String[] { "BASEBALL_MLB_Game_", "BASEBALL_MLB@20;PRESEASO_Game_" };
		MLB_LINES_SPORT = new String[] { "BASEBALL" };
		MLB_FIRST_NAME = new String[] { "BASEBALL_MLB_1st@20;5@20;Innings_" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL" };
		MLB_SECOND_NAME = new String[] { "BASEBALL_MLB_2nd@20;Half_" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL" };

		LOGGER.info("Exiting BetBuckeyeProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String [][] BSITES = new String [][] { 
//				{ "http://betbuckeyesports.com", "zt8219", "red3", "2000", "1000", "1000", "Chicago", "ET"}
//			 	{ "http://www.betbck.com", "A5313", "utah", "500", "500", "500", "Phoenix", "ET"}
//		        { "http://www.Tobysports.net", "Orc101", "pgf", "500", "500", "500", "Asheville", "ET"}
//			 	{ "http://allweeksports.com", "ja005", "Water5", "500", "500", "500", "Phoenix", "ET"}
//				{ "http://www.betbuckeyesports.com", "spl301", "cramsey", "0", "0", "0", "Phoenix", "ET"}
//				{ "http://789sports.com", "SPL312", "OWEN", "0", "0", "0", "New York City", "ET"}
//				{ "http://aceshigh.ag", "CLD5001", "c", "0", "0", "0", "New York City", "ET"}
//				{ "http://solidgoldwagering.com", "BM008", "Nephew", "0", "0", "0", "Chicago", "ET"}
//				{ "http://dimebet.ag", "JNN211", "rhino44", "0", "0", "0", "Chicago", "ET"}
//				{ "http://aceshigh.ag", "JNN219", "tunes2", "0", "0", "0", "Asheville", "ET"}
//				{ "https://ttdsportsbook.net", "309b", "3387", "0", "0", "0", "Los Angeles", "ET"}
//				{ "https://beatpat.com", "Z5059", "4444", "0", "0", "0", "New York", "ET"}
//				{ "https://betvegas365.com", "GA9002", "gold", "0", "0", "0", "New York", "ET"}
//				{ "https://ttdsportsbook.net", "Ttd10001", "l338", "0", "0", "0", "None", "ET"}
//				{ "https://dimebet.ag", "jnn286", "jake73", "0", "0", "0", "None", "ET"}
//				{ "https://luckysp.com", "SSS1421", "zee", "0", "0", "0", "None", "ET"}
//				{ "https://bet702sports.com/", "sf010", "j1010", "0", "0", "0", "None", "ET"}
//				{ "https://bookiebarn.com", "NCL138", "abc", "0", "0", "0", "None", "ET"}
//				{ "https://dimebet.ag", "JNN416", "stone", "0", "0", "0", "None", "ET"}
//				{ "https://bet702sports.com", "KKW103", "blue", "0", "0", "0", "None", "ET"}
//				{ "https://dimebet.ag", "JNN364", "1025", "0", "0", "0", "None", "ET"}
//				{ "https://solidgoldwagering.com", "BM008", "Nephew", "0", "0", "0", "None", "ET"}
//				{ "https://figs770.com", "TR53", "joey", "0", "0", "0", "None", "ET"}
//				{ "https://betemnow.com", "rms221", "larry", "0", "0", "0", "None", "ET"}
//				{ "https://betbuckeyesports.com", "JNN219", "tunes2", "0", "0", "0", "None", "ET"}
//				{ "https://76red.com", "u2b107", "jazz", "0", "0", "0", "None", "ET"}
//				{ "https://4quarterz.me", "bgt008", "mz1", "0", "0", "0", "None", "ET"}
				{ "https://betlucky168.ag", "TL8057", "l57", "0", "0", "0", "None", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < BSITES.length; i++) {
			    final BetBuckeyeProcessSite processSite = new BetBuckeyeProcessSite(BSITES[i][0], BSITES[i][1], BSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(BSITES[i][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = BSITES[i][7];
//			    processSite.testTotal(processSite, BSITES);
				
/*
				final PreviewInput previewInput = new PreviewInput();
				previewInput.setLinetype("total");
				previewInput.setLineindicator("o");
				previewInput.setRotationid(new Integer(969));
				previewInput.setSporttype("mlblines");
				previewInput.setProxyname("New York");
				previewInput.setTimezone("ET");

				final PreviewOutput previewData = processSite.previewEvent(previewInput);
				LOGGER.error("PreviewData: " + previewData);

			    processSite.httpClientWrapper.setupHttpClient(BSITES[i][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = BSITES[i][7];
*/

			    String xhtml = processSite.loginToSite(BSITES[i][1], BSITES[i][2]);
				final Set<PendingEvent> pendingEvents = processSite.getPendingBets(BSITES[i][0], BSITES[i][1], null);
				System.out.println("pendingEvents.size(): " + pendingEvents.size());

				Iterator<PendingEvent> itr = pendingEvents.iterator();
				while (itr.hasNext()) {
					final PendingEvent pe = itr.next();
					if (pe.getDoposturl()) {
						processSite.doProcessPendingEvent(pe);
					}
				}

				itr = pendingEvents.iterator();
				while (itr.hasNext()) {
					final PendingEvent pe = itr.next();
					LOGGER.error("PendingEventXXX: " + pe);
				}
			}
		} catch (Throwable t) {
			LOGGER.error("Throwable Exception", t);
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
			String xhtml = getSiteNoBr(super.populateUrl(PENDING_BETS));
			LOGGER.debug("xhtml: " + xhtml);

			// Check for a captcha
			if (BBP.checkCaptcha(xhtml, "captcha", "modalcaptcha")) {
				processCaptcha(xhtml, true);

				xhtml = getSiteNoBr(super.populateUrl(PENDING_BETS));
				LOGGER.debug("xhtml: " + xhtml);
			}

			if (xhtml != null && xhtml.contains("Pending Wagers")) {
				pendingWagers = BBP.parsePendingBets(xhtml, accountName, accountId);
			} else if (xhtml != null && xhtml.contains("No pending wagers found")) {
				// Do nothing
				LOGGER.debug("No Open Bets");
				pendingWagers = new HashSet<PendingEvent>();
			} else {
				LOGGER.debug("No pending wagers found for " + accountName + " " + accountId);
				if (xhtml != null && xhtml.contains("loginform")) {
					this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
					xhtml = getSiteNoBr(super.populateUrl(PENDING_BETS));
					pendingWagers = BBP.parsePendingBets(xhtml, accountName, accountId);
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
				String xhtml = getSiteNoBr(super.populateUrl(PENDING_BETS));
				pendingWagers = BBP.parsePendingBets(xhtml, accountName, accountId);
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
	 * @see com.ticketadvantage.services.dao.sites.SiteProcessor#doProcessPendingEvent(com.ticketadvantage.services.model.PendingEvent)
	 */
	@Override
	public void doProcessPendingEvent(PendingEvent pe) throws BatchException {
		LOGGER.info("Entering doProcessPendingEvent()");

		try {
			if (pe.getPosturl().equals("Something here")) {
				final SportsInsightsSite processSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com",
						"mojaxsventures@gmail.com", "action1");
				LOGGER.debug("RotationID: " + pe.getRotationid());
				LOGGER.debug("GameSport: " + pe.getGamesport());
				final EventPackage ep = processSite.getEventByIdNoSport(pe.getRotationid());
				LOGGER.debug("EventPackage: " + ep);

				if (ep != null) {
					// Get game type
					String gameType = ep.getSporttype();
					gameType = gameType.replace("NBA Preseaso Basketball", "NBA");
					gameType = gameType.replace("NBA Preseason Basketball", "NBA");
					pe.setGametype(gameType);
					pe.setGamedate(ep.getEventdatetime());
					pe.setEventdate(ep.getDateofevent() + " " + ep.getTimeofevent());
				}				
			} else {
				String xhtml = getSiteNoBr(populateUrl(pe.getPosturl()));
				LOGGER.debug("XHTML: " + xhtml);
	
				// Get game type
				String gameType = BBP.processTicketInfo(xhtml);
				if (gameType != null && gameType.length() > 0) {
					gameType = gameType.trim();
					gameType = gameType.replace("NHL Hockey", "NHL");
					gameType = gameType.replace("NBA Preseaso Basketball", "NBA");
					gameType = gameType.replace("NBA Preseason Basketball", "NBA");
					pe.setGametype(gameType);
				}
			}
		} catch (Throwable t) {
			LOGGER.debug("PendingEvent: " + pe);
			LOGGER.debug(t.getMessage(), t);
		}

		LOGGER.info("Exiting doProcessPendingEvent()");
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
		BBP.setTimezone(super.timezone);

		// Get index page
		String xhtml = getSiteNoBr(httpClientWrapper.getHost());
		LOGGER.debug("XHTML: " + xhtml);

		// Parse home page
		MAP_DATA = BBP.parseIndex(xhtml);
		this.httpClientWrapper.setWebappname(determineWebappName(MAP_DATA.get("action")));

		// Customer ID
		if (MAP_DATA.containsKey("customerID")) {
			MAP_DATA.put("customerID", username);	
		} else if (MAP_DATA.containsKey("CustomerID")) {
			MAP_DATA.put("CustomerID", username);
		} else if (MAP_DATA.containsKey("customerid")) {
			MAP_DATA.put("customerid", username);
		}

		// Password
		if (MAP_DATA.containsKey("password")) {
			MAP_DATA.put("password", password);	
		} else if (MAP_DATA.containsKey("Password")) {
			MAP_DATA.put("Password", password);
		}
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("ActionLogin: " + actionLogin);

		// Call the login
		xhtml = authenticateNoBr(actionLogin, postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		// Check for a captcha
		if (BBP.checkCaptcha(xhtml, "captcha", "modalcaptcha")) {
			processCaptcha(xhtml, true);
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
		List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
		String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("actionName: " + actionName);

		// Post to select sport type
		String xhtml = postSiteNoBr(actionName, postNameValuePairs);
		LOGGER.debug("XHTML: " + xhtml);
		
		// Check for a captcha
		if (BBP.checkCaptcha(xhtml, "captcha", "modalcaptcha")) {
			processCaptcha(xhtml, true);
		}

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
				BetBuckeyeEventPackage betBuckeyeEventPackage = (BetBuckeyeEventPackage)eventPackage;
				LOGGER.debug("BetBuckeyeEventPackage: " + betBuckeyeEventPackage);
				
				if (siteTransaction.getRiskorwin() != null && siteTransaction.getRiskorwin().intValue() == 1) {
					if (betBuckeyeEventPackage != null && betBuckeyeEventPackage.getSpreadMax() != null) {
						if (sAmount.doubleValue() > betBuckeyeEventPackage.getSpreadMax().intValue()) {
							siteAmount = betBuckeyeEventPackage.getSpreadMax().toString();
						}
					}
				} else {
					Double wagerAmount = Double.valueOf(siteAmount);
					float spreadJuice = ae.getSpreadjuice();
					Double risk = wagerAmount * (100 / spreadJuice);
					risk = round(risk.doubleValue(), 2);
					if (betBuckeyeEventPackage != null && betBuckeyeEventPackage.getSpreadMax() != null
							&& (risk.doubleValue() > betBuckeyeEventPackage.getSpreadMax().intValue())) {
						LOGGER.error("Risk: " + risk);
						LOGGER.error("betBuckeyeEventPackage.getSpreadMax().intValue(): "
								+ betBuckeyeEventPackage.getSpreadMax().intValue());
						siteAmount = betBuckeyeEventPackage.getSpreadMax().toString();
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
				BetBuckeyeEventPackage betBuckeyeEventPackage = (BetBuckeyeEventPackage)eventPackage;
				LOGGER.debug("BetBuckeyeEventPackage: " + betBuckeyeEventPackage);
				
				if (siteTransaction.getRiskorwin() != null && siteTransaction.getRiskorwin().intValue() == 1) {
					if (betBuckeyeEventPackage != null && betBuckeyeEventPackage.getTotalMax() != null) {
						if (tAmount.doubleValue() > betBuckeyeEventPackage.getTotalMax().intValue()) {
							siteAmount = betBuckeyeEventPackage.getTotalMax().toString();
						}
					}
				} else {
					Double wagerAmount = Double.valueOf(siteAmount);
					float totalJuice = ae.getTotaljuice();
					Double risk = wagerAmount * (100 / totalJuice);
					risk = round(risk.doubleValue(), 2);
					if (betBuckeyeEventPackage != null && betBuckeyeEventPackage.getTotalMax() != null
							&& (risk.doubleValue() > betBuckeyeEventPackage.getTotalMax().intValue())) {
						LOGGER.error("Risk: " + risk);
						LOGGER.error("betBuckeyeEventPackage.getTotalMax().intValue(): "
								+ betBuckeyeEventPackage.getTotalMax().intValue());
						siteAmount = betBuckeyeEventPackage.getTotalMax().toString();
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
				BetBuckeyeEventPackage betBuckeyeEventPackage = (BetBuckeyeEventPackage)eventPackage;
				LOGGER.debug("BetBuckeyeEventPackage: " + betBuckeyeEventPackage);
				
				if (siteTransaction.getRiskorwin() != null && siteTransaction.getRiskorwin().intValue() == 1) {
					if (betBuckeyeEventPackage != null && betBuckeyeEventPackage.getMlMax() != null) {
						if (mAmount.doubleValue() > betBuckeyeEventPackage.getMlMax().intValue()) {
							siteAmount = betBuckeyeEventPackage.getMlMax().toString();
						}
					}
				} else {
					Double wagerAmount = Double.valueOf(siteAmount);
					float mlJuice = ae.getMljuice();
					Double risk = wagerAmount * (100 / mlJuice);
					risk = round(risk.doubleValue(), 2);
					if (betBuckeyeEventPackage != null && betBuckeyeEventPackage.getMlMax() != null
							&& (risk.doubleValue() > betBuckeyeEventPackage.getMlMax().intValue())) {
						LOGGER.error("Risk: " + risk);
						LOGGER.error("betBuckeyeEventPackage.getMlMax().intValue(): "
								+ betBuckeyeEventPackage.getMlMax().intValue());
						siteAmount = betBuckeyeEventPackage.getMlMax().toString();
					} else {
						if (betBuckeyeEventPackage != null) {
							LOGGER.error("betBuckeyeEventPackage.getMlMax(): "
									+ betBuckeyeEventPackage.getMlMax());							
						}
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

		// Setup post parameters and get action URL
		final String actionName = setupNameValuesEmpty(postNameValuePairs, MAP_DATA, httpClientWrapper.getWebappname());
		LOGGER.debug("actionName: " + actionName);

		// Setup the wager
		String xhtml = postSiteNoBr(actionName, postNameValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		// Check for a captcha
		if (BBP.checkCaptcha(xhtml, "captcha", "modalcaptcha")) {
			processCaptcha(xhtml, true);
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

		// Parse the event selection
		Map<String, String> wagers = null;
		try {
			wagers = BBP.parseWagerTypes(xhtml);
			LOGGER.debug("wagers: " + wagers);
		} catch (BatchException be) {
			if (be.getErrorcode() == BatchErrorCodes.LINE_CHANGED_ERROR) {
				xhtml = processLineChange(xhtml, event, ae);
				wagers = BBP.parseWagerTypes(xhtml);
			} else {
				throw be;
			}
		}

		// Check for a wager limit and rerun
		if (wagers.containsKey("wageramount")) {
			// Only call the select event once
			if (rerunCount++ == 0) {
				siteTransaction.setAmount(wagers.get("wageramount"));
				ae.setAmount(wagers.get("wageramount"));
				ae.setActualamount(wagers.get("wageramount"));
				xhtml = selectEvent(siteTransaction, eventPackage, event, ae);

				try {
					wagers = BBP.parseWagerTypes(xhtml);
					LOGGER.debug("wagers: " + wagers);
				} catch (BatchException be) {
					if (be.getErrorcode() == BatchErrorCodes.LINE_CHANGED_ERROR) {
						xhtml = processLineChange(xhtml, event, ae);
						wagers = BBP.parseWagerTypes(xhtml);
					} else {
						throw be;
					}
				}
				
				// Check for a wager limit and rerun
				if (wagers.containsKey("wageramount")) {
					throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The maximum wager amount has been reached " + siteTransaction.getAmount());
				}
			} else {
				throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, "The maximum wager amount has been reached " + siteTransaction.getAmount());
			}
		}
		MAP_DATA = wagers;

		if (wagers != null) {
			LOGGER.debug("wagerType: " + wagers);
			final String wagerType = wagers.get("wagerType");

			// Check for risk or to win
			if ("riskType".equals(wagerType)) {
				if (!"1".equals(event.getWtype())) {
					// Send win url
					xhtml = getSiteNoBr(populateUrl(wagers.get("winurl")));
					LOGGER.debug("XHTML: " + xhtml);
				}
			} else if ("toWinType".equals(wagerType)) {
				if ("1".equals(event.getWtype())) {
					// Send risk url
					xhtml = getSiteNoBr(populateUrl(wagers.get("riskurl")));
					LOGGER.debug("XHTML: " + xhtml);
				}				
			}
		}

		// Parse the wager information
		try {
			MAP_DATA = BBP.parseEventSelection(xhtml, null, null);
		} catch (BatchException be) {
			if (be.getErrorcode() == BatchErrorCodes.LINE_CHANGED_ERROR) {
				xhtml = processLineChange(xhtml, event, ae);
				MAP_DATA = BBP.parseEventSelection(xhtml, null, null);
			} else {
				throw be;
			}
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
			// Check the acceptance
			if (processTransaction) {
				// Setup the post parameters
				List<NameValuePair> postNameValuePairs = new ArrayList<NameValuePair>(1);
				MAP_DATA.put("password", httpClientWrapper.getPassword());
				final String actionName = setupNameValuesWithName(postNameValuePairs, MAP_DATA, false, httpClientWrapper.getWebappname());
				LOGGER.debug("actionName: " + actionName);
				httpClientWrapper.setPreviousUrl(populateUrl("StraightVerifyWager.php"));

				// Send the wager confirmation
				xhtml = postSiteWithCheckNoBr(actionName, postNameValuePairs, "PlayerSbVerifyFixedWager");
				LOGGER.debug("XHTML: " + xhtml);

				// Check for line change
				if (xhtml.contains("One or more lines have changed")) {
					xhtml = processLineChange(xhtml, event, ae);
				}

				// Check for delayed timer scenario
				String wagerUrl = BBP.processCheckAcceptance(xhtml);
				if (wagerUrl != null) {
					// Sleep for 10 seconds
					sleepAsUser(10000);
					xhtml = getSiteWithCheckNoBr(populateUrl(wagerUrl), "PlayerSbVerifyFixedWager");
					LOGGER.debug("XHTML: " + xhtml);

					// Check for line change
					if (xhtml.contains("One or more lines have changed")) {
						xhtml = processLineChange(xhtml, event, ae);
					}
				}
			}
		} catch (BatchException be) {
			LOGGER.error("Exception completing transaction for account event " + ae + " event " + event, be);
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
		LOGGER.info("Entering parseTicketNumber()");
		
		final String ticketNumber = BBP.parseTicketNumber(xhtml);
		LOGGER.debug("ticketNumber: " + ticketNumber);

		LOGGER.info("Exiting parseTicketNumber()");
		return ticketNumber;		
	}

	/**
	 * 
	 * @param xhtmlData
	 * @throws BatchException
	 */
	private void processCaptcha(String xhtmlData, boolean anotherAttempt) throws BatchException {
		String purl = BBP.processCaptcha(xhtmlData);

		// Get the base captcha
		httpClientWrapper.getSitePageNoBr(populateUrl(purl), null, setupHeader(false));

		try {
			final CaptchaWorker captchaWorker = new CaptchaWorker(super.getHttpClientWrapper());
			final ExecutorService executor = Executors.newFixedThreadPool(5);
			Set<Future<Map<String, String>>> set = new HashSet<Future<Map<String, String>>>();
			
			// Call the base captcha
			String getCaptchaString = populateUrl("getcaptcha.asp?" + Math.random());
			getSiteNoBr(getCaptchaString);
			
			for (int i = 1; i <= 5; i++) {
				GetCaptchaText gct = new GetCaptchaText(getCaptchaString, populateUrl("captcha.asp?captchaID=" + String.valueOf(i)), getCaptchaString, String.valueOf(i), captchaWorker);
				Callable<Map<String, String>> callable = gct;
				Future<Map<String, String>> future = executor.submit(callable);
				set.add(future);
			}
		
			String imgText1 = "";
			String imgText2 = "";
			String imgText3 = "";
			String imgText4 = "";
			String imgText5 = "";
			boolean error = false;

			for (Future<Map<String, String>> future : set) {
				Map<String, String>  map = future.get();
				Set<Entry<String, String>> entry = map.entrySet();
				Iterator <Entry<String, String>> itr = entry.iterator();
				while (itr.hasNext()) {
					Entry<String, String> maps = itr.next();
					String key = maps.getKey();
					if ("1".equals(key)) {
						imgText1 = maps.getValue();
						if (imgText1 != null && imgText1.length() != 1) {
							error = true;
						} else if (imgText1 != null && imgText1.length() == 1){
							try {
								Integer.parseInt(imgText1);
							} catch (NumberFormatException nfe) {
								error = true;
							}
						}
					} else if ("2".equals(key)) {
						imgText2 = maps.getValue();
						if (imgText2 != null && imgText2.length() != 1) {
							error = true;
						} else if (imgText2 != null && imgText2.length() == 1){
							try {
								Integer.parseInt(imgText2);
							} catch (NumberFormatException nfe) {
								error = true;
							}
						}
					} else if ("3".equals(key)) {
						imgText3 = maps.getValue();
						if (imgText3 != null && imgText3.length() != 1) {
							error = true;
						} else if (imgText3 != null && imgText3.length() == 1){
							try {
								Integer.parseInt(imgText3);
							} catch (NumberFormatException nfe) {
								error = true;
							}
						}
					} else if ("4".equals(key)) {
						imgText4 = maps.getValue();
						if (imgText4 != null && imgText4.length() != 1) {
							error = true;
						} else if (imgText4 != null && imgText4.length() == 1){
							try {
								Integer.parseInt(imgText4);
							} catch (NumberFormatException nfe) {
								error = true;
							}
						}
					} else if ("5".equals(key)) {
						imgText5 = maps.getValue();
						if (imgText5 != null && imgText5.length() != 1) {
							error = true;
						} else if (imgText5 != null && imgText5.length() == 1){
							try {
								Integer.parseInt(imgText5);
							} catch (NumberFormatException nfe) {
								error = true;
							}
						}
					}
				}
			}
			
			executor.shutdown();

			// Wait until all threads are finished
			while (!executor.isTerminated()) {
			}

			if (error) {
				if (anotherAttempt) {
					processCaptcha(xhtmlData, false);
				}
			} else {
				final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
				postValuePairs.add(new BasicNameValuePair("captcha_text", imgText1+imgText2+imgText3+imgText4+imgText5));

				// Process the captcha image
				httpClientWrapper.postSitePageNoBr(populateUrl("process_captcha.php"), null, postValuePairs, setupHeader(true));
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
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

		final Map<String, String> lineChanges = BBP.processLineChange(xhtml);
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
					String actionLogin = setupNameValuesEmpty(postValuePairs, lineChanges, httpClientWrapper.getWebappname());
					LOGGER.debug("ActionLogin: " + actionLogin);

					// Post to site page
					xhtml = postSiteNoBr(actionLogin, postValuePairs);							
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
					lineChanges.put("password", this.httpClientWrapper.getPassword());
					String actionLogin = setupNameValuesEmpty(postValuePairs, lineChanges, httpClientWrapper.getWebappname());
					LOGGER.debug("ActionLogin: " + actionLogin);

					// Post to site page
					xhtml = postSiteNoBr(actionLogin, postValuePairs);
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
					lineChanges.put("password", this.httpClientWrapper.getPassword());
					String actionLogin = setupNameValuesEmpty(postValuePairs, lineChanges, httpClientWrapper.getWebappname());
					LOGGER.debug("ActionLogin: " + actionLogin);

					// Post to site page
					xhtml = postSiteNoBr(actionLogin, postValuePairs);
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			}
		}

		LOGGER.info("Exiting processLineChange()");
		return xhtml;
	}
}