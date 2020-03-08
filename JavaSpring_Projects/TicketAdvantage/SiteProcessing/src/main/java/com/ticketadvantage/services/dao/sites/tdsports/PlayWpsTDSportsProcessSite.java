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
public class PlayWpsTDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(PlayWpsTDSportsProcessSite.class);
	protected TDSportsParser TDP = new TDSportsParser();

	/**
	 * 
	 */
	public PlayWpsTDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("PlayWpsTDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering PlayWpsTDSportsProcessSite()");

		// Setup the parser
		this.siteParser = TDP;

		// Menu items
		NFL_LINES_NAME = new String[] { "NFL - GAME LINES" };
		NFL_LINES_SPORT = new String[] { "NFL" };
		NFL_FIRST_NAME = new String[] { "NFL 1ST HALVES" };
		NFL_FIRST_SPORT = new String[] { "NFL" };
		NFL_SECOND_NAME = new String[] { "NFL 2ND HALVES" };
		NFL_SECOND_SPORT = new String[] { "NFL" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL - GAME LINES", "NCAA FOOTBALL - MEN", "NCAA FOOTBALL  MEN", "NCAA FOOTBALL MEN" };
		NCAAF_LINES_SPORT = new String[] { "COLLEGE FOOTBALL", "NCAA FOOTBALL", "CFB" };
		NCAAF_FIRST_NAME = new String[] { "1ST HALVES - CFB", "NCAA FOOTBALL (1H)", "NCAA FOOTBALL - (1H)", "NCAA FB 1ST HALVES", "NCAA FB (1H)" };
		NCAAF_FIRST_SPORT = new String[] { "COLLEGE FOOTBALL", "NCAA FOOTBALL", "CFB" };
		NCAAF_SECOND_NAME = new String[] { "2ND HALVES - CFB", "NCAA FOOTBALL (2H)", "NCAA FOOTBALL - (2H)", "NCAA FB HALFTIMES", "NCAA FB (2H)" };
		NCAAF_SECOND_SPORT = new String[] { "COLLEGE FOOTBALL", "NCAA FOOTBALL", "CFB" };
		NBA_LINES_NAME = new String[] { "NBA - GAME LINES" };
		NBA_LINES_SPORT = new String[] { "NBA" };
		NBA_FIRST_NAME = new String[] { "NBA - 1ST HALF LINES" };
		NBA_FIRST_SPORT = new String[] { "NBA" };
		NBA_SECOND_NAME = new String[] { "NBA - 2ND HALF LINES" };
		NBA_SECOND_SPORT = new String[] { "NBA" };
		NCAAB_LINES_NAME = new String[] { "COLLEGE BASKETBALL - GAME LINES" };
		NCAAB_LINES_SPORT = new String[] { "CBB" };
		NCAAB_FIRST_NAME = new String[] { "1H COLLEGE BASKETBALL" };
		NCAAB_FIRST_SPORT = new String[] { "CBB" };
		NCAAB_SECOND_NAME = new String[] { "2H COLLEGE BASKETBALL" };
		NCAAB_SECOND_SPORT = new String[] { "CBB" };
		NHL_LINES_NAME = new String[] { "NHL - GAME LINES" };
		NHL_LINES_SPORT = new String[] { "NHL" };
		NHL_FIRST_NAME = new String[] { "NHL - 1ST HALF LINES", "NHL - 1ST PERIOD LINES" };
		NHL_FIRST_SPORT = new String[] { "NHL" };
		NHL_SECOND_NAME = new String[] { "NHL - 2ND HALF LINES", "NHL - 2ND PERIOD LINES" };
		NHL_SECOND_SPORT = new String[] { "NHL" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "Basketball" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_FIRST_SPORT = new String[] { "Basketball" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		WNBA_SECOND_SPORT = new String[] { "Basketball" };

		LOGGER.info("Exiting PlayWpsTDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
				{ "http://www.playwps.com/", "SH1054", "ab", "1000", "1000", "1000", "New York City", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final PlayWpsTDSportsProcessSite processSite = new PlayWpsTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
				final PlayWpsTDSportsProcessSite processSite2 = new PlayWpsTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];
			    processSite.loginToSite("SH1054", "ab");

			    Set<PendingEvent> pendingEvents = processSite.getPendingBets("SH1054", "ab", null);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					Iterator<PendingEvent> itr = pendingEvents.iterator();
					while (itr.hasNext()) {
						System.out.println("PendingEvent: " + itr.next());
					}
				} else {
					System.out.println("PendingEvents is null or empty");
				}

/*			    processSite2.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite2.processTransaction = false;
			    processSite2.timezone = TDSITES[0][7];

				// Now call the test suite
				PlaySports365TDSportsProcessSite.testSite2(processSite, processSite2, i, TDSITES);
*/
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

		// Get home page data
		MAP_DATA = TDP.parseIndex(xhtml);

		// Setup the username/password data
		MAP_DATA.put("ctl00$MainContent$ctlLogin$_UserName", username);
		MAP_DATA.put("ctl00$MainContent$ctlLogin$_Password", password);
		MAP_DATA.put("ctl00$MainContent$ctlLogin$BtnSubmit.x", "29");
		MAP_DATA.put("ctl00$MainContent$ctlLogin$BtnSubmit.y", "14");

		MAP_DATA.remove("password");
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

			if (pUrl != null && !pUrl.contains("CreateSports.aspx?WT=0")) {
				xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));
			}
		}

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}