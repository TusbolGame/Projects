/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.util.ArrayList;
import java.util.HashMap;
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
public class SevenTwentyFourTDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(SevenTwentyFourTDSportsProcessSite.class);
	protected final SevenTwentyFourTDSportsParser TDP = new SevenTwentyFourTDSportsParser();

	/**
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @param isMobile
	 * @param showRequestResponse
	 */
	public SevenTwentyFourTDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("SevenTwentyFourTDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering SevenTwentyFourTDSportsProcessSite()");

		// Setup the parser
		this.siteParser = TDP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NFL_LINES_NAME = new String[] { "NFL - GAME LINES" };
		NFL_FIRST_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NFL_FIRST_NAME = new String[] { "NFL - FIRST HALFS" };
		NFL_SECOND_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NFL_SECOND_NAME = new String[] { "NFL - SECOND HALFS" };
		NCAAF_LINES_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NCAAF_LINES_NAME = new String[] { "NCAAF - GAME LINES" };
		NCAAF_FIRST_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NCAAF_FIRST_NAME = new String[] { "NCAA FOOTBALL - (1H)" };
		NCAAF_SECOND_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NCAAF_SECOND_NAME = new String[] { "NCAA FOOTBALL - (2H)" };
		NBA_LINES_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NBA_LINES_NAME = new String[] { "NBA - GAME LINES" };
		NBA_FIRST_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NBA_FIRST_NAME = new String[] { "NBA - 1ST HALVES" };
		NBA_SECOND_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NBA_SECOND_NAME = new String[] { "NBA 2ND HALFS" };
		NCAAB_LINES_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL - MEN" };
		NCAAB_FIRST_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BASKETBALL - (1H)" };
		NCAAB_SECOND_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BASKETBALL - (2H)" };
		NHL_LINES_SPORT = new String[] { "HOCKEY GAME LINES" };
		NHL_LINES_NAME = new String[] { "NHL - GAME LINES" };
		NHL_FIRST_SPORT = new String[] { "HOCKEY GAME LINES" };
		NHL_FIRST_NAME = new String[] { "NHL - PERIOD LINES" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY GAME LINES" };
		NHL_SECOND_NAME = new String[] { "NHL - PERIOD LINES" };
		NHL_THIRD_SPORT = new String[] { "HOCKEY GAME LINES" };
		NHL_THIRD_NAME = new String[] { "NHL - PERIOD LINES" };
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

		LOGGER.info("Exiting SevenTwentyFourTDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
				// { "http://www.betgrande.com", "7265", "jd", "2000", "2000", "2000", "Los Angeles", "ET"}
				{ "http://724sports.com/", "BA101", "yellow", "10000", "10000", "10000", "New York City", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final SevenTwentyFourTDSportsProcessSite processSite = new SevenTwentyFourTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];

				// Now call the test suite
			    String xhtml = processSite.loginToSite(TDSITES[0][1], TDSITES[0][2]);
			    LOGGER.debug("xhtml: " + xhtml);
			    
			    Map<String, String> map = processSite.getMenuFromSite("ncaablines", xhtml);
			    LOGGER.debug("map: " + map);

				xhtml = processSite.selectSport("ncaablines");
				LOGGER.debug("XHTML: " + xhtml);

				final Iterator<EventPackage> ep = processSite.parseGamesOnSite("ncaablines", xhtml);
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
		List<NameValuePair> retValue = httpClientWrapper.getSitePage(httpClientWrapper.getHost(), null, setupHeader(false));
		String xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		retValue = httpClientWrapper.checkForRedirect(retValue, httpClientWrapper.getHost(), setupHeader(false), null, httpClientWrapper.getWebappname() + "/");
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("XHTML: " + xhtml);

		// Get home page data
		Map<String, String> indexMap = TDP.parseIndex(xhtml);
		
		MAP_DATA = new HashMap<String, String>();
		MAP_DATA.put("__EVENTTARGET", "");
		MAP_DATA.put("__EVENTARGUMENT", "");
		MAP_DATA.put("__VIEWSTATE", indexMap.get("__VIEWSTATE"));
		MAP_DATA.put("__EVENTVALIDATION", indexMap.get("__EVENTVALIDATION"));
		MAP_DATA.put("ctl00$LoginForm1$_IdBook", "");
		MAP_DATA.put("ctl00$LoginForm1$Redir", "");
		MAP_DATA.put("ctl00$LoginForm1$_UserName", username);
		MAP_DATA.put("ctl00$LoginForm1$_Password", password);
		MAP_DATA.put("ctl00$LoginForm1$BtnSubmit.x", "19");
		MAP_DATA.put("ctl00$LoginForm1$BtnSubmit.y", "12");

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		// Call the login
		xhtml = authenticate(populateUrl("Default.aspx"), postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		// Setup the webapp name
		httpClientWrapper.setWebappname("wager");

		// Parse login
		MAP_DATA = TDP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// CreateSports page
		xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}