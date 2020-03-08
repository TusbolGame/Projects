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
public class IbetTDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(IbetTDSportsProcessSite.class);
	protected IbetTDSportsParser TDP = new IbetTDSportsParser();

	/**
	 * 
	 */
	public IbetTDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("IbetTDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering IbetTDSportsProcessSite()");

		// Setup the parser
		this.siteParser = new IbetTDSportsParser();

		// Menu items
		NFL_LINES_NAME = new String[] { "NFL" };
		NFL_LINES_SPORT = new String[] { "FOOTBALL" };
		NFL_FIRST_NAME = new String[] { "NFL - 1ST HALF" };
		NFL_FIRST_SPORT = new String[] { "FOOTBALL" };
		NFL_SECOND_NAME = new String[] { "NFL - 2ND HALF" };
		NFL_SECOND_SPORT = new String[] { "FOOTBALL" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL" };
		NCAAF_LINES_SPORT = new String[] { "COLLEGE FOOTBALL" };
		NCAAF_FIRST_NAME = new String[] { "NCAA FOOTBALL - 1ST HALF" };
		NCAAF_FIRST_SPORT = new String[] { "COLLEGE FOOTBALL" };
		NCAAF_SECOND_NAME = new String[] { "NCAA FOOTBALL - 2ND HALF" };
		NCAAF_SECOND_SPORT = new String[] { "COLLEGE FOOTBALL" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_LINES_SPORT = new String[] { "BASKETBALL" };
		NBA_FIRST_NAME = new String[] { "NBA - 1ST HALF" };
		NBA_FIRST_SPORT = new String[] { "BASKETBALL" };
		NBA_SECOND_NAME = new String[] { "NBA - 2ND HALF" };
		NBA_SECOND_SPORT = new String[] { "BASKETBALL" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL - MEN" };
		NCAAB_LINES_SPORT = new String[] { "COLLEGE BASKETBALL" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BASKETBALL - 1ST HALF" };
		NCAAB_FIRST_SPORT = new String[] { "COLLEGE BASKETBALL" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BASKETBALL - 2ND HALF" };
		NCAAB_SECOND_SPORT = new String[] { "COLLEGE BASKETBALL" };
		NHL_LINES_NAME = new String[] { "NHL" };
		NHL_LINES_SPORT = new String[] { "HOCKEY" };
		NHL_FIRST_NAME = new String[] { "NHL - 1ST HALF", "NHL - 1ST PERIOD" };
		NHL_FIRST_SPORT = new String[] { "HOCKEY" };
		NHL_SECOND_NAME = new String[] { "NHL - 2ND HALF", "NHL - 2ND PERIOD" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "Basketball" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_FIRST_SPORT = new String[] { "Basketball" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		WNBA_SECOND_SPORT = new String[] { "Basketball" };

		LOGGER.info("Exiting IbetTDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
				{ "http://www.ibet.ag", "akz", "123", "10000", "10000", "10000", "Los Angeles", "ET"}
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
				final IbetTDSportsProcessSite processSite = new IbetTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
				final IbetTDSportsProcessSite processSite2 = new IbetTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];

			    processSite2.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite2.processTransaction = false;
			    processSite2.timezone = TDSITES[0][7];

				// Now call the test suite
				IbetTDSportsProcessSite.testSite2(processSite, processSite2, i, TDSITES);
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

		// Call the index page
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

		// Set the webappname now
		if (httpClientWrapper.getPreviousUrl() != null) {
			httpClientWrapper.setWebappname(determineWebappName(httpClientWrapper.getPreviousUrl()));
		}

		// Hack for ibet.ag
		if (xhtml.contains("window.location=")) {
			int bindex = xhtml.indexOf("window.location='");
			if (bindex != -1) {
				String nXhtml = xhtml.substring(bindex + "window.location='".length());
				int eindex = nXhtml.indexOf("'");
				if (eindex != -1) {
					String nUrl = nXhtml.substring(0, eindex);
					xhtml = getSite(httpClientWrapper.getHost() + nUrl);
				}
			}
		}

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

			if (pUrl != null && !pUrl.contains("CreateSports.aspx?WT=0")) {
				xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));
			}
		}

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}