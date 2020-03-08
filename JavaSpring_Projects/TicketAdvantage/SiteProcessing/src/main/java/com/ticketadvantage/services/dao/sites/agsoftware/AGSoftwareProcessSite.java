/**
 * 
 */
package com.ticketadvantage.services.dao.sites.agsoftware;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteEventPackage;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.dao.sites.metallica.captcha.CaptchaReader;
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
public class AGSoftwareProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(AGSoftwareProcessSite.class);
	private static final String PENDING_BETS = "AlPendingWagers.asp";
	private final AGSoftwareParser ASP = new AGSoftwareParser();
	private final SportsInsightsSite processSite = new SportsInsightsSite("https://sportsinsights.actionnetwork.com/",
			"mojaxsventures@gmail.com", 
			"action1");
	private String captchaStr="";

	/**
	 * 
	 */
	public AGSoftwareProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("AGSoftware", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering AgSoftwareProcessSite()");

		// Setup the parser
		this.siteParser = ASP;

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
//		NBA_LINES_SPORT = new String[] { "PreseasonNBA" };
		NBA_LINES_SPORT = new String[] { "NBA", "PreseasonNBA" };
		NBA_LINES_NAME = new String[] { "Game" };
		NBA_FIRST_SPORT = new String[] { "NBA", "PreseasonNBA" };
		NBA_FIRST_NAME = new String[] { "1st Half" };
		NBA_SECOND_SPORT = new String[] { "NBA", "PreseasonNBA" };
		NBA_SECOND_NAME = new String[] { "2nd Half" };
		NCAAB_LINES_SPORT = new String[] { "NCAA", "NCAA EXTRA" };
		NCAAB_LINES_NAME = new String[] { "Game", "Basketball_NCAA" };
		NCAAB_FIRST_SPORT = new String[] { "NCAA", "NCAA EXTRA" };
		NCAAB_FIRST_NAME = new String[] { "1st Half" };
		NCAAB_SECOND_SPORT = new String[] { "NCAA", "NCAA EXTRA" };
		NCAAB_SECOND_NAME = new String[] { "2nd Half" };
		NHL_LINES_SPORT = new String[] { "NHL" };
		NHL_LINES_NAME = new String[] { "Game" };
		NHL_FIRST_SPORT = new String[] { "NHL" };
		NHL_FIRST_NAME = new String[] { "1st Period" };
		NHL_SECOND_SPORT = new String[] { "NHL" };
		NHL_SECOND_NAME = new String[] { "2nd Period" };
		NHL_SECOND_SPORT = new String[] { "NHL" };
		NHL_SECOND_NAME = new String[] { "3rd Period" };
		WNBA_LINES_SPORT = new String[] { "WNBA" };
		WNBA_LINES_NAME = new String[] { "Game" };
		WNBA_FIRST_SPORT = new String[] { "WNBA" };
		WNBA_FIRST_NAME = new String[] { "1st Half" };
		WNBA_SECOND_SPORT = new String[] { "WNBA" };
		WNBA_SECOND_NAME = new String[] { "2nd Half" };
		MLB_LINES_SPORT = new String[] { "MLB", "Preseason" };
		MLB_LINES_NAME = new String[] { "Game" };
		MLB_FIRST_SPORT = new String[] { "MLB", "Preseason" };
		MLB_FIRST_NAME = new String[] { "1st 5 Innings" };
		MLB_SECOND_SPORT = new String[] { "MLB", "Preseason" };
		MLB_SECOND_NAME = new String[] { "MLB 2nd half lines" };
		LOGGER.info("Exiting AgSoftwareProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] AGSITES = new String [][] {
//				{ "http://www.betting4entertainment.com", "51336", "ap6", "100", "100", "100", "Baltimore", "ET" },
//				{ "http://www.slidinghome.com", "Pa1547", "flyers", "1000", "1000", "1000", "Baltimore", "ET" },
//				{ "http://www.smoothbets.com", "M1039", "Red", "500", "500", "500", "Dallas", "ET" }
//				{ "http://www.vegasbettingworld.com", "YL824", "red", "500", "500", "500", "Baltimore", "ET" }
//				{ "http://www.smoothbets.com", "M1005", "ball", "500", "500", "500", "Dallas", "ET" }
//				{ "http://www.vegasbettingworld.com", "YL802", "golf", "500", "500", "500", "New York", "ET" }
//				{ "http://www.vegasbettingworld.com", "Yl825", "gold", "500", "500", "500", "New York", "ET" }
//				{ "http://www.nysportscasino.com", "9180", "nike", "500", "500", "500", "None", "ET" }
//				{ "http://www.westcoastwager.com", "35114", "jj10", "500", "500", "500", "None", "ET" }
//				{ "http://www.spicylines.com", "64425", "jump", "500", "500", "500", "None", "ET" }
				{ "http://www.vegasbettingworld.com", "qxcl755", "G", "100", "100", "100", "None", "ET" }
			};

			final AGSoftwareProcessSite processSite = new AGSoftwareProcessSite(AGSITES[0][0], AGSITES[0][1],
					AGSITES[0][2], false, false);
		    processSite.httpClientWrapper.setupHttpClient(AGSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = AGSITES[0][7];
		    processSite.testSpread(processSite, AGSITES);

/*
		    Set<PendingEvent> pendingEvents = processSite.getPendingBets(AGSITES[0][0], AGSITES[0][1], null);
			final Iterator<PendingEvent> itr = pendingEvents.iterator();
			while (itr.hasNext()) {
				final PendingEvent pe = itr.next();
				LOGGER.error("PendingEventXXX: " + pe);
				if (pe.getDoposturl()) {
					processSite.doProcessPendingEvent(pe);
				}
			}
*/
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
			pendingWagers = ASP.parsePendingBets(xhtml, accountName, accountId);
			if (pendingWagers != null && pendingWagers.size() > 0) {
				final Iterator<PendingEvent> itr = pendingWagers.iterator();
				EventPackage ep = null;

				while (itr.hasNext()) {
					final PendingEvent pendingEvent = itr.next();
					LOGGER.debug("PendingEventABC: " + pendingEvent);

					final Set<EventPackage> events = processSite.getAllSportsGame().getEvents();
					Iterator<EventPackage> eItr = events.iterator();
					boolean found = false;
					while (eItr.hasNext() && !found) {
						ep = eItr.next();
						final Integer rotation1 = ep.getTeamone().getId();
						final Integer rotation2 = ep.getTeamtwo().getId();
						final String rotationId = pendingEvent.getRotationid();
						LOGGER.debug("rotation1: " + rotation1);
						LOGGER.debug("rotation2: " + rotation2);
						LOGGER.debug("rotationId: " + rotationId);

						if (rotationId != null && rotationId.length() > 0 &&
							rotation1 != null && rotation2 != null &&
							(rotationId.equals(rotation1.toString()) || 
							 rotationId.equals(rotation2.toString()))) {

							// Set the dates
							pendingEvent.setGametype(ep.getSporttype());
							pendingEvent.setGamedate(ep.getEventdatetime());
							pendingEvent.setEventdate(ep.getDateofevent() + " " + ep.getTimeofevent());

							found = true;
							LOGGER.debug("PendingEvent: " + pendingEvent);
						}				
					}
				}
			}
		} else {
			LOGGER.error("xhtml: " + xhtml);
			LOGGER.error("Not logged in");
			if (xhtml != null && xhtml.contains("loginform")) {
				this.loginToSite(this.httpClientWrapper.getUsername(), this.httpClientWrapper.getPassword());
			}
			xhtml = getSite(super.populateUrl(PENDING_BETS));
			LOGGER.error("xhtml: " + xhtml);
			if (xhtml != null && xhtml.contains("view all wagers graded within the last 14 days.")) {
				pendingWagers = ASP.parsePendingBets(xhtml, accountName, accountId);
				
				if (pendingWagers != null && pendingWagers.size() > 0) {
					final Iterator<PendingEvent> itr = pendingWagers.iterator();
					EventPackage ep = null;

					while (itr.hasNext()) {
						final PendingEvent pendingEvent = itr.next();
						LOGGER.debug("PendingEventABC: " + pendingEvent);
	
						final Set<EventPackage> events = processSite.getAllSportsGame().getEvents();
						Iterator<EventPackage> eItr = events.iterator();
						boolean found = false;
						while (eItr.hasNext() && !found) {
							ep = eItr.next();
							final Integer rotation1 = ep.getTeamone().getId();
							final Integer rotation2 = ep.getTeamtwo().getId();
							final String rotationId = pendingEvent.getRotationid();
							LOGGER.debug("rotation1: " + rotation1);
							LOGGER.debug("rotation2: " + rotation2);
							LOGGER.debug("rotationId: " + rotationId);

							if (rotationId != null && rotationId.length() > 0 &&
								rotation1 != null && rotation2 != null &&
								(rotationId.equals(rotation1.toString()) || 
								 rotationId.equals(rotation2.toString()))) {

								// Set the dates
								pendingEvent.setGamedate(ep.getEventdatetime());
								pendingEvent.setEventdate(ep.getDateofevent() + " " + ep.getTimeofevent());

								found = true;
								LOGGER.debug("PendingEvent: " + pendingEvent);
							}				
						}
					}
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
		ASP.setTimezone(timezone);

		// Get the home page
		String xhtml = getSite(httpClientWrapper.getHost());
		LOGGER.debug("XHTML: " + xhtml);

		// Get home page data
		MAP_DATA = ASP.parseIndex(xhtml);
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

		// Authenticate
		xhtml = authenticate(actionName, postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		// Parse the login
		MAP_DATA = ASP.parseLogin(xhtml);
		if (MAP_DATA != null && MAP_DATA.containsKey("src")) {
			final String javascriptString = getSite(this.httpClientWrapper.getHost() + MAP_DATA.get("src"));
			LOGGER.debug("javascriptString: " + javascriptString);
			
			// Parse the cookie information
			String cookies = this.httpClientWrapper.getCookies();
			cookies = ASP.parseCookies(javascriptString, cookies);
			this.httpClientWrapper.setCookies(cookies);
		}
		
		if (MAP_DATA.containsKey("action")) {
			postValuePairs = new ArrayList<NameValuePair>(1);
			actionName = setupNameValuesEmpty(postValuePairs, MAP_DATA, null);
			LOGGER.debug("ActionName: " + actionName);

			// Call the WagerMenu
			xhtml = postSite(actionName, postValuePairs);
			if (xhtml != null && xhtml.contains("not a robot by entering the text from captcha")) {
				throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION, BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Problem getting captcha text", xhtml);
			}

			// Now get the Risk/Win page
			postValuePairs = new ArrayList<NameValuePair>(1);
//			postValuePairs.add(new BasicNameValuePair("target", "single"));
			postValuePairs.add(new BasicNameValuePair("target", ""));
			postValuePairs.add(new BasicNameValuePair("wt", "Straight Bet"));
			xhtml = postSite(actionName, postValuePairs);
			if (xhtml != null && xhtml.contains("not a robot by entering the text from captcha")) {
				throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION, BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Problem getting captcha text", xhtml);
			}
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
		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		final String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, null);
		LOGGER.debug("ActionLogin: " + actionLogin);

		// Post to site page
		String xhtml = postSite(actionLogin, postValuePairs);
		if (xhtml != null && xhtml.contains("not a robot by entering the text from captcha")) {
			MAP_DATA = ASP.parseCaptcha(xhtml);

			// value set by new code
			String captchadata = processCaptcha(MAP_DATA.get("CaptchaImage"));

			LOGGER.error("captchadata: " + captchadata);
			if (captchadata == null || captchadata.length() == 0) {
				// Throw an exception
				throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION,
						BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Problem getting captcha text", xhtml);
			} else {
				MAP_DATA.put("CaptchaMessage", captchadata);
			}

			postValuePairs = new ArrayList<NameValuePair>(1);
			String actionName = setupNameValuesEmpty(postValuePairs, MAP_DATA, null);
			LOGGER.debug("ActionName: " + actionName);

			// Call the WagerMenu
			xhtml = postSite(actionName, postValuePairs);

			if (xhtml != null && xhtml.contains("not a robot by entering the text from captcha")) {
				throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION,
						BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Problem getting captcha text", xhtml);
			}
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

		for (SiteEventPackage sep : backupEventsPackage) {
			if (sep.getSiteteamone().getGameSpreadInputName() != null) {
				MAP_DATA.put(sep.getSiteteamone().getGameSpreadInputName(), "");
			}
			if (sep.getSiteteamone().getGameSpreadSelectName() != null) {
				MAP_DATA.put(sep.getSiteteamone().getGameSpreadSelectName(), "0");
			}
			if (sep.getSiteteamone().getGameTotalInputName() != null) {
				MAP_DATA.put(sep.getSiteteamone().getGameTotalInputName(), "");
			}
			if (sep.getSiteteamone().getGameTotalSelectName() != null) {
				MAP_DATA.put(sep.getSiteteamone().getGameTotalSelectName(), "0");
			}
			if (sep.getSiteteamone().getGameMLInputName() != null) {
				MAP_DATA.put(sep.getSiteteamone().getGameMLInputName(), "");
			}
		}
		
		String siteAmount = determineRiskWinAmounts(siteTransaction, eventPackage, event, ae); 
		LOGGER.error("siteAmount: " + siteAmount);
		siteTransaction.setAmount(siteAmount);
		ae.setActualamount(siteAmount);
		MAP_DATA.put(siteTransaction.getInputName(), siteAmount);

/*
		// Get the spread transaction
		if (event instanceof SpreadRecordEvent) {
			siteTransaction = getSpreadTransaction((SpreadRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.error("siteAmount: " + siteAmount);
			Double sAmount = Double.parseDouble(siteAmount);
			if (sAmount != null) {
				final SiteEventPackage siteEventPackage = (SiteEventPackage)eventPackage;
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
				
				MAP_DATA.put(siteTransaction.getInputName(), siteAmount);
			}
		} else if (event instanceof TotalRecordEvent) {
			siteTransaction = getTotalTransaction((TotalRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.error("siteAmount: " + siteAmount);
			Double tAmount = Double.parseDouble(siteAmount);
			if (tAmount != null) {
				final SiteEventPackage siteEventPackage = (SiteEventPackage)eventPackage;
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

				MAP_DATA.put(siteTransaction.getInputName(), siteAmount);
			}
		} else if (event instanceof MlRecordEvent) {
			siteTransaction = getMlTransaction((MlRecordEvent)event, eventPackage, ae);
			siteAmount = siteTransaction.getAmount();
			LOGGER.error("siteAmount: " + siteAmount);
			Double mAmount = Double.parseDouble(siteAmount);
			if (mAmount != null) {
				final SiteEventPackage siteEventPackage = (SiteEventPackage)eventPackage;
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

				MAP_DATA.put(siteTransaction.getInputName(), siteAmount);
			}
		}

		LOGGER.debug("siteAmount: " + siteAmount);
		ae.setActualamount(siteAmount);
*/
		String xhtml = null;

		// Setup the wager
		httpClientWrapper.setCookies("slideStyle=long; __z_a=4230204650");

		// Process the setup page
		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, null);
		LOGGER.debug("ActionLogin: " + actionLogin);

		// Post to site page
		xhtml = postSite(actionLogin, postValuePairs);
		if (xhtml != null && xhtml.contains("not a robot by entering the text from captcha")) {
			throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION,
					BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Problem getting captcha text", xhtml);
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
		Map<String, String> wagers = ASP.parseEventSelection(xhtml, null, null);
		LOGGER.debug("wagers: " + wagers);
		MAP_DATA = wagers;

		String captchadata = null;
		LOGGER.error("CaptchaImage: " + wagers.containsKey("CaptchaImage"));

		if (wagers.containsKey("CaptchaImage") && wagers.get("CaptchaImage") != null && wagers.get("CaptchaImage").length() > 0) {
			//value set by new code
			captchadata = processCaptcha(wagers.get("CaptchaImage"));
			
			LOGGER.error("captchadata: " + captchadata);
			if (captchadata == null || captchadata.length() == 0) {
				// Throw an exception
				throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION, BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Problem getting captcha text", xhtml);
			} else {
				wagers.put("CaptchaMessage", captchadata);
			}

			List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
			String actionName = setupNameValuesEmpty(postValuePairs, wagers, null);
			LOGGER.debug("ActionName: " + actionName);

			// Call the WagerMenu
			xhtml = postSite(actionName, postValuePairs);

			if (xhtml != null && xhtml.contains("not a robot by entering the text from captcha")) {
				throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION, BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Problem getting captcha text", xhtml);
			}

			MAP_DATA = ASP.parseEventSelection(xhtml, null, null);
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
				xhtml = processWager(event, ae);
				LOGGER.debug("XHTML: " + xhtml);
				if (xhtml != null && xhtml.contains("not a robot by entering the text from captcha")) {
					throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION, BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Problem getting captcha text", xhtml);
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

		final String ticketNumber = ASP.parseTicketNumber(xhtml);
		LOGGER.debug("ticketNumber: " + ticketNumber);

		LOGGER.info("Exiting parseTicketTransaction()");
		return ticketNumber;		
	}

	/**
	 * 
	 * @param event
	 * @param ae
	 * @return
	 * @throws BatchException
	 */
	private String processWager(BaseRecordEvent event, AccountEvent ae) throws BatchException {
		LOGGER.info("Entering processWager()");

		// Set password
		MAP_DATA.put("password", this.httpClientWrapper.getPassword());

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		String actionUrl = setupNameValuesEmpty(postValuePairs, MAP_DATA, null);

		// Call check acceptance
		String xhtml = postSite(actionUrl, postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		// Check for captcha
		if (xhtml.contains("text from captcha below")) {
			// Throw an exception
			throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION, BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Unexpected Captcha Page", xhtml);
		} 

		// Check for line change
		if (xhtml.contains("THE LINE (OR PRICE) HAS CHANGED")) {
			xhtml = processLineChange(xhtml, event, ae);
		}

		// Check for captcha
		if (xhtml.contains("text from captcha below")) {
			// Throw an exception
			throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION, BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Unexpected Captcha Page", xhtml);
		} 

		// "ProcessWagerRelay.asp");return;}setTimeout('OnTimer()', 

		if (!xhtml.contains("Wager has been accepted!")) {
			int sleepAsUser = 45000;
			int index = xhtml.indexOf("\"ProcessWagerRelay.asp\");return;}setTimeout('OnTimer()', ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "\"ProcessWagerRelay.asp\");return;}setTimeout('OnTimer()', ".length());
				index = xhtml.indexOf(");");
				if (index != -1) {
					try {
						String sleepTime = xhtml.substring(0, index);
						sleepAsUser = Integer.parseInt(sleepTime);
					} catch (Throwable t) {
						LOGGER.error(t.getMessage(), t);
					}
				}
			}

			// Sleep for defined seconds
			sleepAsUser(sleepAsUser);

			// Call it again
			xhtml = getSite(super.populateUrl("ProcessWagerRelay.asp"));

			// Check for captcha
			if (xhtml.contains("text from captcha below")) {
				// Throw an exception
				throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION, BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Unexpected Captcha Page", xhtml);
			} 

			// Check for line change
			if (xhtml.contains("THE LINE (OR PRICE) HAS CHANGED")) {
				xhtml = processLineChange(xhtml, event, ae);
			}

			if (!xhtml.contains("Wager has been accepted!")) {
				sleepAsUser = 45000;
				index = xhtml.indexOf("\"ProcessWager.asp\");return;}setTimeout('OnTimer()', ");
				if (index != -1) {
					xhtml = xhtml.substring(index + "\"ProcessWager.asp\");return;}setTimeout('OnTimer()', ".length());
					index = xhtml.indexOf(");");
					try {
						String sleepTime = xhtml.substring(0, index);
						sleepAsUser = Integer.parseInt(sleepTime);
					} catch (Throwable t) {
						LOGGER.error(t.getMessage(), t);
					}
				}
				
				// Sleep for defined seconds
				sleepAsUser(sleepAsUser);

				// Call it again
				xhtml = getSite(super.populateUrl("ProcessWager.asp"));

				// Check for captcha
				if (xhtml.contains("text from captcha below")) {
					// Throw an exception
					throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION, BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Unexpected Captcha Page", xhtml);
				} 

				// Check for line change
				if (xhtml.contains("THE LINE (OR PRICE) HAS CHANGED")) {
					xhtml = processLineChange(xhtml, event, ae);
				}
			}
		}

		LOGGER.info("Exiting processWager()");
		return xhtml;
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

		final Map<String, String> lineChanges = ASP.processLineChange(xhtml);
		if (lineChanges != null && !lineChanges.isEmpty()) {
			if (event instanceof SpreadRecordEvent) {
				final SpreadRecordEvent sre = (SpreadRecordEvent)event;
				if (determineSpreadLineChange(sre, ae, lineChanges)) {
					// setup the data
					List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
					MAP_DATA.put("password", this.httpClientWrapper.getPassword());
					String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, null);
					LOGGER.debug("ActionLogin: " + actionLogin);

					// Post to site page
					xhtml = postSite(actionLogin, postValuePairs);
					if (xhtml != null && xhtml.contains("not a robot by entering the text from captcha")) {
						throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION, BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Problem getting captcha text", xhtml);
					}
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			} else if (event instanceof TotalRecordEvent) {
				final TotalRecordEvent tre = (TotalRecordEvent)event;
				if (determineTotalLineChange(tre, ae, lineChanges)) {
					// setup the data
					List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
					MAP_DATA.put("password", this.httpClientWrapper.getPassword());
					String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, null);
					LOGGER.debug("ActionLogin: " + actionLogin);

					// Post to site page
					xhtml = postSite(actionLogin, postValuePairs);
					if (xhtml != null && xhtml.contains("not a robot by entering the text from captcha")) {
						throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION, BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Problem getting captcha text", xhtml);
					}
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			} else if (event instanceof MlRecordEvent) {
				final MlRecordEvent mre = (MlRecordEvent)event;
				
				if (determineMlLineChange(mre, ae, lineChanges)) {
					// setup the data
					List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
					MAP_DATA.put("password", this.httpClientWrapper.getPassword());
					String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, null);
					LOGGER.debug("ActionLogin: " + actionLogin);

					// Post to site page
					xhtml = postSite(actionLogin, postValuePairs);
					if (xhtml != null && xhtml.contains("not a robot by entering the text from captcha")) {
						throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION, BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "Problem getting captcha text", xhtml);
					}
				} else {
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "The line has changed", xhtml);
				}
			}
		}

		LOGGER.info("Exiting processLineChange()");
		return xhtml;
	}

	/**
	 * 
	 * @param imagedata
	 * @return
	 */
	protected String processCaptcha(String imagedata) {
		String textdata = null;

		try {
			//Process captcha String
			final CaptchaReader obj = new CaptchaReader();
			textdata = captchaStr = obj.parseCaptcha(imagedata);
			LOGGER.error("Captcha Text: " + captchaStr);

			// Clean up text if necessary
			if (!Pattern.matches("[a-zA-Z0-9]{6}",captchaStr)) {
				// TODO
				LOGGER.error("Captcha has non alphanumeric characters " + textdata);
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return textdata;
	}
}