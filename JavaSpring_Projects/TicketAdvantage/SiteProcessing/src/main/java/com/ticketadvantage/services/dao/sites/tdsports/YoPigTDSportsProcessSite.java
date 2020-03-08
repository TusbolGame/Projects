/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class YoPigTDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(YoPigTDSportsProcessSite.class);
	protected final YoPigTDSportsParser TDP = new YoPigTDSportsParser();

	/**
	 * 
	 */
	public YoPigTDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("YoPigTDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering YoPigTDSportsProcessSite()");

		// Setup the parser
		this.siteParser = TDP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_LINES_NAME = new String[] { "NFL - GAME LINES", "NFL PRESEASON GAME LINES" };
		NFL_FIRST_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_FIRST_NAME = new String[] { "NFL (1H)", "NFL PRESEASON (1H)" };
		NFL_SECOND_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_SECOND_NAME = new String[] { "NFL (2H)" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL - GAME LINES", "NCAA FOOTBALL - MEN", "NCAA FOOTBALL  MEN", "NCAA FOOTBALL MEN" };
		NCAAF_LINES_SPORT = new String[] { "COLLEGE FOOTBALL", "NCAA FOOTBALL", "CFB" };
		NCAAF_FIRST_NAME = new String[] { "1ST HALVES - CFB", "NCAA FOOTBALL (1H)", "NCAA FOOTBALL - (1H)", "NCAA FB 1ST HALVES", "NCAA FB (1H)" };
		NCAAF_FIRST_SPORT = new String[] { "COLLEGE FOOTBALL", "NCAA FOOTBALL", "CFB" };
		NCAAF_SECOND_NAME = new String[] { "2ND HALVES - CFB", "NCAA FOOTBALL (2H)", "NCAA FOOTBALL - (2H)", "NCAA FB HALFTIMES", "NCAA FB (2H)" };
		NCAAF_SECOND_SPORT = new String[] { "COLLEGE FOOTBALL", "NCAA FOOTBALL", "CFB" };
		NBA_LINES_SPORT = new String[] { "NBA BASKETBALL" };
		NBA_LINES_NAME = new String[] { "NBA - GAME LINES", "NBA SUMMER LEAGUE" };
		NBA_FIRST_SPORT = new String[] { "NBA BASKETBALL" };
		NBA_FIRST_NAME = new String[] { "NBA (1H)" };
		NBA_SECOND_SPORT = new String[] { "NBA BASKETBALL" };
		NBA_SECOND_NAME = new String[] { "NBA (2H)" };
		NCAAB_LINES_SPORT = new String[] { "NCAA BASKETBALL" };
		NCAAB_LINES_NAME = new String[] { "NCAA BK - GAME LINES" };
		NCAAB_FIRST_SPORT = new String[] { "NCAA BASKETBALL" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BK (1H)" };
		NCAAB_SECOND_SPORT = new String[] { "NCAA BASKETBALL" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BK (2H)" };
		NHL_LINES_SPORT = new String[] { "HOCKEY" };
		NHL_LINES_NAME = new String[] { "NHL - GAME LINES" };
		NHL_FIRST_SPORT = new String[] { "HOCKEY" };
		NHL_FIRST_NAME = new String[] { "NHL (1P)" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY" };
		NHL_SECOND_NAME = new String[] { "NHL (2P)" };
		NHL_THIRD_SPORT = new String[] { "HOCKEY" };
		NHL_THIRD_NAME = new String[] { "NHL (3P)" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "Basketball" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_FIRST_SPORT = new String[] { "Basketball" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		WNBA_SECOND_SPORT = new String[] { "Basketball" };
		MLB_LINES_SPORT = new String[] { "Baseball" };
		MLB_LINES_NAME = new String[] { "MLB - GAME LINES" };
		MLB_FIRST_SPORT = new String[] { "Baseball" };
		MLB_FIRST_NAME = new String[] { "MLB (1H)" };
		MLB_SECOND_SPORT = new String[] { "Baseball" };
		MLB_SECOND_NAME = new String[] { "MLB (2H)" };

		LOGGER.info("Exiting YoPigTDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
				//{ "http://www.yopig.ag", "az7030", "black55", "2000", "2000", "2000", "Los Angeles", "ET"}
				//{ "http://www.yopig.ag", "GS250", "green", "2000", "2000", "2000", "Phoenix", "ET"}
				//{ "http://www.yopig.ag", "1754", "cigar", "2000", "2000", "2000", "Los Angeles", "ET"}
				//{ "http://80stone.net", "GG9103", "south", "2000", "2000", "2000", "Los Angeles", "ET"}
				//{ "http://offshorewagers.com/", "Qxhy441", "red", "0", "0", "0", "None", "ET"}
				//{ "http://80stone.net", "GG5624", "spain", "500", "500", "500", "None", "ET"}
//				{ "http://bettordays.ag", "qxad108", "yankees", "0", "0", "0", "None", "ET"}
				{ "http://www.sporthub.ag", "Dal714", "weezy", "0", "0", "0", "None", "ET"}
			};

			final YoPigTDSportsProcessSite processSite = new YoPigTDSportsProcessSite(TDSITES[0][0], TDSITES[0][1],
					TDSITES[0][2], false, true);
		    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = TDSITES[0][7];
		    processSite.testSpread(processSite, TDSITES);
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

		// Call the second page
		List<NameValuePair> retValue = httpClientWrapper.getSitePage(httpClientWrapper.getHost(), null, setupHeader(false));
		String xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		retValue = httpClientWrapper.checkForRedirect(retValue, httpClientWrapper.getHost(), setupHeader(false), null, httpClientWrapper.getWebappname() + "/");
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
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
		httpClientWrapper.setWebappname("core/engine/wager");

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

			if (pUrl != null && !pUrl.contains("CreateSports.aspx?WT=0")) {
				xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));
			}
		}

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}