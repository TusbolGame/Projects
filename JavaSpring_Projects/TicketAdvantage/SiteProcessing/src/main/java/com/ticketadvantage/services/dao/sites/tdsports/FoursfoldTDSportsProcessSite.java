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
public class FoursfoldTDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(FoursfoldTDSportsProcessSite.class);
	protected FoursfoldTDSportsParser FTDSP = new FoursfoldTDSportsParser();

	/**
	 * 
	 */
	public FoursfoldTDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("FoursfoldTDSportsProcessSite", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering FoursfoldTDSportsProcessSite()");

		// Setup the parser
		this.siteParser = FTDSP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "FOOTBALL" };
		NFL_LINES_NAME = new String[] { "NFL", "NFL PRESEASON" };
		NFL_FIRST_SPORT = new String[] { "FOOTBALL" };
		NFL_FIRST_NAME = new String[] { "NFL (1H)", "NFL PRESEASON (1H)", "NFL PRESEASON - 1H LINES" };
		NFL_SECOND_SPORT = new String[] { "FOOTBALL" };
		NFL_SECOND_NAME = new String[] { "NFL (2H)", "NFL PRESEASON (2H)", "NFL PRESEASON - 2H LINES" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL - MEN" };
		NCAAF_LINES_SPORT = new String[] { "FOOTBALL" };
		NCAAF_FIRST_NAME = new String[] { "NCAA FOOTBALL - MEN (1H)", "NCAA FOOTBALL - 1H LINES" };
		NCAAF_FIRST_SPORT = new String[] { "FOOTBALL" };
		NCAAF_SECOND_NAME = new String[] { "NCAA FOOTBALL - MEN (2H)", "NCAA FOOTBALL - 2H LINES" };
		NCAAF_SECOND_SPORT = new String[] { "FOOTBALL" };
		NBA_LINES_SPORT = new String[] { "BASKETBALL" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "BASKETBALL" };
		NBA_FIRST_NAME = new String[] { "NBA (1H)" };
		NBA_SECOND_SPORT = new String[] { "NBA" };
		NBA_SECOND_NAME = new String[] { "NBA (2H)" };
		NCAAB_LINES_SPORT = new String[] { "BASKETBALL" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL - MEN" };
		NCAAB_FIRST_SPORT = new String[] { "BASKETBALL" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BASKETBALL (1H)" };
		NCAAB_SECOND_SPORT = new String[] { "BASKETBALL" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BASKETBALL (2H)" };
		NHL_LINES_SPORT = new String[] { "ICE HOCKEY" };
		NHL_LINES_NAME = new String[] { "NATIONAL HOCKEY LEAGUE" };
		NHL_FIRST_SPORT = new String[] { "ICE HOCKEY" };
		NHL_FIRST_NAME = new String[] { "NHL (1P)" };
		NHL_SECOND_SPORT = new String[] { "ICE HOCKEY" };
		NHL_SECOND_NAME = new String[] { "NHL (2P)" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "BASKETBALL" };
		WNBA_FIRST_NAME = new String[] { "WNBA (1H)" };
		WNBA_FIRST_SPORT = new String[] { "BASKETBALL" };
		WNBA_SECOND_NAME = new String[] { "WNBA (2H)" };
		WNBA_SECOND_SPORT = new String[] { "BASKETBALL" };
		MLB_LINES_NAME = new String[] { "MAJOR LEAGUE BASEBALL" };
		MLB_LINES_SPORT = new String[] { "BASEBALL" };
		MLB_FIRST_NAME = new String[] { "MLB - 1ST 5 FULL INNINGS" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL" };
		MLB_SECOND_NAME = new String[] { "MLB - LAST 4 INNINGS" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL" };

		LOGGER.info("Exiting FoursfoldTDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
				{ "http://www.4sfold.com", "Mt1050", "brett", "3000", "3000", "3000", "Los Angeles", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final FoursfoldTDSportsProcessSite processSite = new FoursfoldTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
				final FoursfoldTDSportsProcessSite processSite2 = new FoursfoldTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];

			    processSite2.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite2.processTransaction = false;
			    processSite2.timezone = TDSITES[0][7];

				// Now call the test suite
				FoursfoldTDSportsProcessSite.testSite2(processSite, processSite2, i, TDSITES);
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
		FTDSP.setTimezone(timezone);

		String host = httpClientWrapper.getHost();
		String xhtml = getSite(host);
		MAP_DATA = FTDSP.parseIndex(xhtml);
		MAP_DATA.put("ctl00$ctlLogin$_UserName", username);
		MAP_DATA.put("ctl00$ctlLogin$_Password", password);

		// Setup the username/password data
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		// Call the login
		xhtml = authenticate(actionLogin, postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		// Setup the webapp name
		LOGGER.error("HTTP GET: " + populateUrl("CreateSports.aspx?WT=0"));
		httpClientWrapper.setWebappname("/wager");
		xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));
		LOGGER.error("HTML: " + xhtml);

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}