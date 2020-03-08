/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class Green444TDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(Green444TDSportsProcessSite.class);
	protected Green444TDSportsParser TDP = new Green444TDSportsParser();
	private static final String PENDING_BETS = "OpenBets.aspx";

	/**
	 * 
	 */
	public Green444TDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("Green444TDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering Green444TDSportsProcessSite()");

		// Setup the parser
		this.siteParser = TDP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_LINES_NAME = new String[] { "NFL - GAME LINES" };
		NFL_FIRST_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_FIRST_NAME = new String[] { "NFL (1H)" };
		NFL_SECOND_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_SECOND_NAME = new String[] { "NFL (2H)" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL - GAME LINES", "NCAA FOOTBALL - MEN", "NCAA FOOTBALL  MEN", "NCAA FOOTBALL MEN" };
		NCAAF_LINES_SPORT = new String[] { "COLLEGE FOOTBALL", "NCAA FOOTBALL", "CFB" };
		NCAAF_FIRST_NAME = new String[] { "1ST HALVES - CFB", "NCAA FOOTBALL (1H)", "NCAA FOOTBALL - (1H)", "NCAA FB 1ST HALVES", "NCAA FB (1H)" };
		NCAAF_FIRST_SPORT = new String[] { "COLLEGE FOOTBALL", "NCAA FOOTBALL", "CFB" };
		NCAAF_SECOND_NAME = new String[] { "2ND HALVES - CFB", "NCAA FOOTBALL (2H)", "NCAA FOOTBALL - (2H)", "NCAA FB HALFTIMES", "NCAA FB (2H)" };
		NCAAF_SECOND_SPORT = new String[] { "COLLEGE FOOTBALL", "NCAA FOOTBALL", "CFB" };
		NBA_LINES_SPORT = new String[] { "BASKETBALL" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "BASKETBALL" };
		NBA_FIRST_NAME = new String[] { "NBA 1ST HALFS" };
		NBA_SECOND_SPORT = new String[] { "NBA" };
		NBA_SECOND_NAME = new String[] { "NBA 2ND HALFS" };
		NCAAB_LINES_SPORT = new String[] { "BASKETBALL" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL - MEN" };
		NCAAB_FIRST_SPORT = new String[] { "BASKETBALL" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BASKETBALL - 1H" };
		NCAAB_SECOND_SPORT = new String[] { "BASKETBALL" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BASKETBALL - 2H" };
		NHL_LINES_SPORT = new String[] { "HOCKEY" };
		NHL_LINES_NAME = new String[] { "NATIONAL HOCKEY LEAGUE" };
		NHL_FIRST_SPORT = new String[] { "HOCKEY" };
		NHL_FIRST_NAME = new String[] { "NHL (1P)" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY" };
		NHL_SECOND_NAME = new String[] { "NHL (2P)" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "Basketball" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_FIRST_SPORT = new String[] { "Basketball" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		WNBA_SECOND_SPORT = new String[] { "Basketball" };

		LOGGER.info("Exiting Green444TDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
				{ "http://www.Green444.ag", "5106", "69", "2000", "2000", "2000", "Los Angeles", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final Green444TDSportsProcessSite processSite = new Green444TDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];
			    processSite.loginToSite(TDSITES[i][1], TDSITES[i][2]);

			    Set<PendingEvent> pendingEvents = processSite.getPendingBets(TDSITES[i][0], TDSITES[i][1], null);
			    Iterator<PendingEvent> pes = pendingEvents.iterator();
			    while (pes.hasNext()) {
				    	PendingEvent pe = pes.next();
				    	System.out.println("PendinEvent: " + pe);
			    }
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
		this.siteParser.setTimezone(timezone);
		TDP.setTimezone(timezone);

		// Call the main page
		List<NameValuePair> retValue = httpClientWrapper.getSitePage(httpClientWrapper.getHost(), null, setupHeader(false));
		String xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		// Get home page data
		MAP_DATA = TDP.parseIndex(xhtml);

		// Setup the username/password data
		setUsernamePassword(username, password);		
		LOGGER.debug("Map: " + MAP_DATA);

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, "");
		LOGGER.debug("ActionLogin: " + actionLogin);

		// Call the login
		xhtml = authenticate(actionLogin, postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		// Setup the webapp name
		httpClientWrapper.setWebappname(determineWebappName(httpClientWrapper.getPreviousUrl()));

		// Parse login
		MAP_DATA = TDP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		postValuePairs = new ArrayList<NameValuePair>(1);
		actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, "");
		LOGGER.debug("ActionLogin: " + actionLogin);

		if (actionLogin != null) {
			// Call post login form
			xhtml = postSite(actionLogin, postValuePairs);
			LOGGER.debug("XHTML: " + xhtml);

			String pUrl = httpClientWrapper.getPreviousUrl();
			LOGGER.debug("pUrl: " + pUrl);

			// Set the webappname
			httpClientWrapper.setWebappname(determineWebappName(pUrl));

			xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));
		}

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsProcessSite#getPendingBets(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public Set<PendingEvent> getPendingBets(String accountName, String accountId, Object anythingObject) throws BatchException {
		LOGGER.info("Entering getPendingBets()");
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingWagers = null;
		String xhtml = getSite(super.populateUrl(PENDING_BETS));
		LOGGER.debug("xhtml: " + xhtml);

		if (xhtml != null && xhtml.contains("Open Wagers For Player")) {
			pendingWagers = TDP.parsePendingBets(xhtml, accountName, accountId);
		} else {
			LOGGER.error("Pending Bets NOT Found");
		}

		LOGGER.info("Exiting getPendingBets()");
		return pendingWagers;
	}
}