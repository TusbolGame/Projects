/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class BetProPlusTDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(BetProPlusTDSportsProcessSite.class);
	protected BetProPlusTDSportsParser TDP = new BetProPlusTDSportsParser();

	/**
	 * 
	 */
	public BetProPlusTDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("BetProPlusTDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering BetProPlusTDSportsProcessSite()");

		// Setup the parser
		super.siteParser = TDP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "FOOTBALL" };
		NFL_LINES_NAME = new String[] { "NFL", "NFL PRESEASON",  };
		NFL_FIRST_SPORT = new String[] { "FOOTBALL" };
		NFL_FIRST_NAME = new String[] { "NFL PRESEASON - 1H LINES" };
		NFL_SECOND_SPORT = new String[] { "FOOTBALL" };
		NFL_SECOND_NAME = new String[] { "NFL PRESEASON - 1H LINES" };
		NCAAF_LINES_SPORT = new String[] { "FOOTBALL" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL - MEN" };
		NCAAF_FIRST_SPORT = new String[] { "FOOTBALL" };
		NCAAF_FIRST_NAME = new String[] { "NCAA FOOTBALL - 1H LINES" };
		NCAAF_SECOND_SPORT = new String[] { "FOOTBALL" };
		NCAAF_SECOND_NAME = new String[] { "NCAA FOOTBALL - 2H LINES" };
		NBA_LINES_SPORT = new String[] { "NBA" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "NBA" };
		NBA_FIRST_NAME = new String[] { "NBA 1ST HALVES" };
		NBA_SECOND_SPORT = new String[] { "NBA" };
		NBA_SECOND_NAME = new String[] { "NBA 2ND HALF LINES" };
		NCAAB_LINES_SPORT = new String[] { "CBB" };
		NCAAB_LINES_NAME = new String[] { "" };
		NCAAB_FIRST_SPORT = new String[] { "CBB" };
		NCAAB_FIRST_NAME = new String[] { "" };
		NCAAB_SECOND_SPORT = new String[] { "CBB" };
		NCAAB_SECOND_NAME = new String[] { "" };
		NHL_LINES_SPORT = new String[] { "NHL" };
		NHL_LINES_NAME = new String[] { "NATIONAL HOCKEY LEAGUE" };
		NHL_FIRST_SPORT = new String[] { "NHL" };
		NHL_FIRST_NAME = new String[] { "" };
		NHL_SECOND_SPORT = new String[] { "NHL" };
		NHL_SECOND_NAME = new String[] { "" };
		WNBA_LINES_SPORT = new String[] { "NBA" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_FIRST_SPORT = new String[] { "NBA" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1ST HALVES" };
		WNBA_SECOND_SPORT = new String[] { "NBA" };
		WNBA_SECOND_NAME = new String[] { "" };
		MLB_LINES_SPORT = new String[] { "BASEBALL" };
		MLB_LINES_NAME = new String[] { "MAJOR LEAGUE BASEBALL" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL" };
		MLB_FIRST_NAME = new String[] { "MLB 1ST FIVE INNINGS" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL" };
		MLB_SECOND_NAME = new String[] { "MLB 2ND HALF LINES" };

		LOGGER.info("Exiting BetProPlusTDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
//				{ "http://www.betproplus.com", "MPA701", "701", "500", "500", "500", "None", "ET"}
				{ "http://goeastway.com", "MPA701", "701", "500", "500", "500", "None", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final BetProPlusTDSportsProcessSite processSite = new BetProPlusTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
				final BetProPlusTDSportsProcessSite processSite2 = new BetProPlusTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];
			    processSite.loginToSite(TDSITES[0][1], TDSITES[0][2]);

				final Set<PendingEvent> pendingEvents = processSite.getPendingBets(TDSITES[0][0], TDSITES[0][1], new Object());
				if (pendingEvents != null && pendingEvents.size() > 0) {
					final Iterator<PendingEvent> itr = pendingEvents.iterator();
					if (itr != null) {
						while (itr.hasNext()) {
							final PendingEvent pe = itr.next();
							System.out.println("PendingEvent: " + pe);
						}
					}
				}

			    processSite2.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite2.processTransaction = false;
			    processSite2.timezone = TDSITES[0][7];

				// Now call the test suite
//				BetProPlusTDSportsProcessSite.testSite2(processSite, processSite2, i, TDSITES);
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
		super.siteParser.setTimezone(timezone);
		TDP.setTimezone(timezone);
		super.TDP = this.TDP;

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		postValuePairs.add(new BasicNameValuePair("Account", username.toUpperCase()));
		postValuePairs.add(new BasicNameValuePair("IdBook", ""));
		postValuePairs.add(new BasicNameValuePair("submit", ""));
		postValuePairs.add(new BasicNameValuePair("password", password.toUpperCase()));

		// Call the login
		String xhtml = authenticate(httpClientWrapper.getHost() + "/start.aspx", postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		// Parse login
		MAP_DATA = TDP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);
		
		postValuePairs = new ArrayList<NameValuePair>(1);
		String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, "");
		LOGGER.debug("ActionLogin: " + actionLogin);

		if (actionLogin != null) {
			// Call post login form
			xhtml = postSite(actionLogin, postValuePairs);

			// Parse login
			MAP_DATA = TDP.parseIndex(xhtml);
			LOGGER.debug("MAP_DATA: " + MAP_DATA);

			// Setup the webapp name
			httpClientWrapper.setWebappname(determineWebappName(httpClientWrapper.getPreviousUrl()));

			// Go directly into the menu page
			xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));
		}

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}