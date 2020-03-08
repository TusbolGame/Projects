/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.EventPackage;

/**
 * @author jmiller
 *
 */
public class BetgrandeTDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(BetgrandeTDSportsProcessSite.class);
	protected final BetgrandeTDSportsParser TDP = new BetgrandeTDSportsParser();

	/**
	 * 
	 */
	public BetgrandeTDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("BetgrandeTDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering BetgrandeTDSportsProcessSite()");

		// Setup the parser
		this.siteParser = TDP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_LINES_NAME = new String[] { "NFL" };
		NFL_FIRST_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_FIRST_NAME = new String[] { "NFL - FIRST HALFS" };
		NFL_SECOND_SPORT = new String[] { "NFL FOOTBALL" };
		NFL_SECOND_NAME = new String[] { "NFL - SECOND HALFS" };
		NCAAF_LINES_SPORT = new String[] { "NCAA FOOTBALL" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL - MEN" };
		NCAAF_FIRST_SPORT = new String[] { "NCAA FOOTBALL" };
		NCAAF_FIRST_NAME = new String[] { "1ST HALF NCAA FOOTBALL" };
		NCAAF_SECOND_SPORT = new String[] { "NCAA FOOTBALL" };
		NCAAF_SECOND_NAME = new String[] { "2ND HALF NCAA FOOTBALL" };
		NBA_LINES_SPORT = new String[] { "BASKETBALL" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "BASKETBALL" };
		NBA_FIRST_NAME = new String[] { "NBA 1ST HALFS" };
		NBA_SECOND_SPORT = new String[] { "BASKETBALL" };
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
		NHL_FIRST_NAME = new String[] { "NHL 1ST HALFS" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY" };
		NHL_SECOND_NAME = new String[] { "NHL 2ND HALFS" };
		NHL_THIRD_SPORT = new String[] { "HOCKEY" };
		NHL_THIRD_NAME = new String[] { "NHL 3RD HALFS" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "Basketball" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_FIRST_SPORT = new String[] { "Basketball" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		WNBA_SECOND_SPORT = new String[] { "Basketball" };
		MLB_LINES_SPORT = new String[] { "BASEBALL" };
		MLB_LINES_NAME = new String[] { "MAJOR LEAGUE BASEBALL" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL" };
		MLB_FIRST_NAME = new String[] { "MLB 1ST HALFS" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL" };
		MLB_SECOND_NAME = new String[] { "MLB 2ND HALFS" };

		LOGGER.info("Exiting BetgrandeTDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
				// { "http://www.betgrande.com", "7265", "jd", "2000", "2000", "2000", "Los Angeles", "ET"}
				{ "http://www.betgrande.com", "651", "tunes", "2000", "2000", "2000", "New York City", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final BetgrandeTDSportsProcessSite processSite = new BetgrandeTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];

				// Now call the test suite
			    String xhtml = processSite.loginToSite("651", "tunes");
			    LOGGER.debug("xhtml: " + xhtml);
			    
			    Map<String, String> map = processSite.getMenuFromSite("ncaaflines", xhtml);
			    LOGGER.debug("map: " + map);

				xhtml = processSite.selectSport("nbafirst");
				LOGGER.debug("XHTML: " + xhtml);

				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("ncaaflines", xhtml);
				LOGGER.debug("ep: " + ep);
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

		// Call the second page
		List<NameValuePair> retValue = httpClientWrapper.getSitePage(httpClientWrapper.getHost() + "/Login.aspx", null, setupHeader(false));
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
}