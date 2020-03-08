/**
 * 
 */
package com.ticketadvantage.services.dao.sites.pinnacle;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.ProxyContainer;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.dao.sites.SiteWagers;
import com.ticketadvantage.services.dao.sites.sports411.Sports411EventPackage;
import com.ticketadvantage.services.dao.sites.sports411.Sports411TeamPackage;
import com.ticketadvantage.services.dao.sites.sports411.Sports411Transaction;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.CookieData;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class PinnacleProcessSite extends SiteProcessor {
	private static final Logger LOGGER = Logger.getLogger(PinnacleProcessSite.class);
	protected PinnacleParser PP = new PinnacleParser();
	private int rerunCount = 0;

	public PinnacleProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("Pinnacle", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering PinnacleProcessSite()");

		// Setup the parser
		this.siteParser = PP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL", "NFL - Preseason" };
		NFL_LINES_NAME = new String[] { "Game" };
		NFL_FIRST_SPORT = new String[] { "NFL", "NFL - Preseason" };
		NFL_FIRST_NAME = new String[] { "1st Half" };
		NFL_SECOND_SPORT = new String[] { "NFL", "NFL - Preseason" };
		NFL_SECOND_NAME = new String[] { "2nd Half" };
		NCAAF_LINES_SPORT = new String[] { "NCAA" };
		NCAAF_LINES_NAME = new String[] { "Game" };
		NCAAF_FIRST_SPORT = new String[] { "NCAA" };
		NCAAF_FIRST_NAME = new String[] { "1st Half" };
		NCAAF_SECOND_SPORT = new String[] { "NCAA" };
		NCAAF_SECOND_NAME = new String[] { "2nd Half" };
		NBA_LINES_SPORT = new String[] { "Basketball" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "Basketball" };
		NBA_FIRST_NAME = new String[] { "NBA 1st Halfs" };
		NBA_SECOND_SPORT = new String[] { "Basketball" };
		NBA_SECOND_NAME = new String[] { "NBA 2nd Halfs" };
		NCAAB_LINES_SPORT = new String[] { "Basketball" };
		NCAAB_LINES_NAME = new String[] { "NCAA" };
		NCAAB_FIRST_SPORT = new String[] { "Basketball" };
		NCAAB_FIRST_NAME = new String[] { "NCAA (College) 1st Halfs" };
		NCAAB_SECOND_SPORT = new String[] { "Basketball" };
		NCAAB_SECOND_NAME = new String[] { "NCAA (College) 2nd Halfs" };
		NHL_LINES_SPORT = new String[] { "Hockey" };
		NHL_LINES_NAME = new String[] { "NHL OT Included", "NHL OT Included Alternates", "NHL Regulation Time (see rules)" };
		NHL_FIRST_SPORT = new String[] { "Hockey" };
		NHL_FIRST_NAME = new String[] { "NHL 1st Period" };
		NHL_SECOND_SPORT = new String[] { "Hockey" };
		NHL_SECOND_NAME = new String[] { "NHL - 2ND HALVES", "NHL - 2ND PERIOD" };
		WNBA_LINES_SPORT = new String[] { "WNBA" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_FIRST_SPORT = new String[] { "WNBA" };
		WNBA_FIRST_NAME = new String[] { "1st Half" };
		WNBA_SECOND_SPORT = new String[] { "WNBA" };
		WNBA_SECOND_NAME = new String[] { "2nd Half" };
		MLB_LINES_SPORT = new String[] { "Baseball" };
		MLB_LINES_NAME = new String[] { "MLB" };
		MLB_FIRST_SPORT = new String[] { "Baseball" };
		MLB_FIRST_NAME = new String[] { "MLB" };
		MLB_SECOND_SPORT = new String[] { "Baseball" };
		MLB_SECOND_NAME = new String[] { "MLB" };

		LOGGER.info("Exiting PinnacleProcessSite()");
	}

	/**
	 * 
	 * @param accountSoftware
	 * @param host
	 * @param username
	 * @param password
	 */
	public PinnacleProcessSite(String accountSoftware, String host, String username, String password) {
		super(accountSoftware, host, username, password);
		LOGGER.info("Entering PinnacleProcessSite()");
		LOGGER.info("Exiting PinnacleProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] PINNACLESITES = new String [][] {
//				{ "https://www1.pinnacle.com", "52022", "Jake86", "100", "100", "100", "Costa Rica", "PT"}
				{ "https://www.pinnacle.com", "58026", "Thor50", "100", "100", "100", "Costa Rica", "PT"}
			};

			final PinnacleProcessSite processSite = new PinnacleProcessSite(PINNACLESITES[0][0], PINNACLESITES[0][1],
					PINNACLESITES[0][2], false, true);
		    processSite.httpClientWrapper.setupHttpClient(PINNACLESITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = PINNACLESITES[0][7];

		    String xhtml = processSite.loginToSite(processSite.httpClientWrapper.getUsername(), processSite.httpClientWrapper.getPassword());
		    LOGGER.error("xhtml: " + xhtml);

			// Step 5 - Get menu
//			Map<String, String> maps = processSite.getMenuFromSite("ncaablines", xhtml);
//			LOGGER.debug("maps: " + maps);

//		    xhtml = processSite.selectSport("ncaablines");
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
		PP.setTimezone(timezone);

		// Get the home page
//		super.httpClientWrapper.getSitePage(super.httpClientWrapper.getHost() + "/en/", null, setupHeader(false));

		MAP_DATA = new HashMap<String, String>();
		MAP_DATA.put("AppId", "Classic");
		MAP_DATA.put("CustomerId", username);
		MAP_DATA.put("Password", password);
		MAP_DATA.put("fakepasswordremembered", password);
		MAP_DATA.put("fakeusernameremembered", username);
		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		super.httpClientWrapper.setHost("https://www.pinnacle.com");

		List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", "https://www.pinnacle.com/en/rtn"));

/*
		final BasicCookieStore cookieStore = new BasicCookieStore();

		// Call the login
		Date now = new Date();
		final String webMetrics ="s-" + now.getTime() + "&r=https%3A%2F%2Fwww.pinnacle.com%2Fen%2Frtn";
		final BasicClientCookie webmetricsRUM = new BasicClientCookie("Webmetrics-RUM", webMetrics);
		webmetricsRUM.setDomain(".pinnacle.com");
		webmetricsRUM.setAttribute(ClientCookie.DOMAIN_ATTR, "true");
		webmetricsRUM.setPath("/");
		cookieStore.addCookie(webmetricsRUM); 

		now = new Date();
		final String adrum ="s-" + now.getTime() + "&r=https%3A%2F%2Fwww.pinnacle.com%2Fen%2Frtn%3F0";
		final BasicClientCookie adRUM = new BasicClientCookie("ADRUM", adrum);
		adRUM.setDomain(".pinnacle.com");
		adRUM.setAttribute(ClientCookie.DOMAIN_ATTR, "true");
		adRUM.setPath("/");
		cookieStore.addCookie(adRUM); 
		super.httpClientWrapper.setCookieStore(cookieStore);
*/

		Date now = new Date();
		final String webMetrics ="s-" + now.getTime() + "&r=https%3A%2F%2Fwww.pinnacle.com%2Fen%2Frtn";

		now = new Date();
		final String adrum ="s-" + now.getTime() + "&r=https%3A%2F%2Fwww.pinnacle.com%2Fen%2Frtn%3F0";

		// Login first
		List<NameValuePair> retValue = super.httpClientWrapper.authenticateSiteNoRedirect(super.httpClientWrapper.getHost() + "/login/authenticate/Classic/en-GB", "Webmetrics-RUM=" + webMetrics + ";" + "ADRUM=" + adrum, postValuePairs, headerValuePairs);

		String xhtml = "";
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

		// Get the menu page
		headerValuePairs = new ArrayList<NameValuePair>();
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", "https://www.pinnacle.com/en/"));

		if (location.length() > 0) {
			retValue = super.httpClientWrapper.getSitePageNoRedirect(super.httpClientWrapper.getHost() + location, null, headerValuePairs);
		}

		location = "";

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
		LOGGER.error("xhtml: " + xhtml);
		
		// Get the menu page
		headerValuePairs = new ArrayList<NameValuePair>();
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", "https://www.pinnacle.com/en/"));
		headerValuePairs.add(new BasicNameValuePair("Host", "www1.pinnacle.com"));
		super.httpClientWrapper.setHost("https://www1.pinnacle.com");

		if (location.length() > 0) {
			super.httpClientWrapper.setHost("https://www1.pinnacle.com");
			retValue = super.httpClientWrapper.getSitePageNoRedirect(location, null, headerValuePairs);
		}

		location = "";

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
		LOGGER.error("xhtml: " + xhtml);

		headerValuePairs = new ArrayList<NameValuePair>();
		headerValuePairs.add(new BasicNameValuePair("Cache-Control", "no-cache"));
		headerValuePairs.add(new BasicNameValuePair("X-Requested-With", "XMLHttpRequest"));
		headerValuePairs.add(new BasicNameValuePair("Referer", "https://www1.pinnacle.com/members/canvas.asp"));
		headerValuePairs.add(new BasicNameValuePair("Host", "www1.pinnacle.com"));

		// https://www1.pinnacle.com/members/BETPAGE.ASP?maincontent&LanguageId=egl
		// retValue = super.httpClientWrapper.postSitePageNoRedirect("https://www1.pinnacle.com/members/BETPAGE.ASP?maincontent&LanguageId=egl", cookiesToStore + cookiesToStore2 + "___utmvmKEucVEz=fBevaALGdUO; ___utmvbKEucVEz=DZv    XTTOaalG: StL;", null, headerValuePairs);
		retValue = super.httpClientWrapper.postSitePageNoRedirect("https://www1.pinnacle.com/members/BETPAGE.ASP?maincontent&LanguageId=egl", null, null, headerValuePairs);

		location = "";
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
		LOGGER.error("xhtml: " + xhtml);

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
		final String url = MAP_DATA.get("url");
		final Map<String, String> map = new HashMap<String, String>();
		map.put("hdnHashTag", "");
		map.put("languageEntry", "English|egl");
		map.put("linesTypeView", "c");
		map.put("oddsEntry", "american");

		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, map, "");

		// Post to site page
		final String xhtml = postSite(super.httpClientWrapper.getHost() + url, postValuePairs);
		LOGGER.error("XHTML: " + xhtml);

		LOGGER.info("Exiting selectSport()");
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
		
		final String ticketNumber = PP.parseTicketNumber(xhtml);
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
		Map<String, String> wagers = PP.parseConfirmWager(xhtml);

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
				MAP_DATA = PP.parseConfirmWager(xhtml);

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