/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class BetitTDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(BetitTDSportsProcessSite.class);
	protected BetitTDSportsParser BSP = new BetitTDSportsParser();

	/**
	 * 
	 */
	public BetitTDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("BetitTDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering BetitTDSportsProcessSite()");

		// Setup the parser
		this.siteParser = BSP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NFL_LINES_NAME = new String[] { "NFL" };
		NFL_FIRST_SPORT = new String[] { "FOOTBALL HALFTIMES" };
		NFL_FIRST_NAME = new String[] { "NFL -  FIRST HALF LINES" };
		NFL_SECOND_SPORT = new String[] { "FOOTBALL HALFTIMES" };
		NFL_SECOND_NAME = new String[] { "NFL -  SECOND HALF LINES" };
		NCAAF_LINES_SPORT = new String[] { "FOOTBALL GAME LINES" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL" };
		NCAAF_FIRST_SPORT = new String[] { "FOOTBALL HALFTIMES" };
		NCAAF_FIRST_NAME = new String[] { "NCAA FOOTBALL - (1H)" };
		NCAAF_SECOND_SPORT = new String[] { "FOOTBALL HALFTIMES" };
		NCAAF_SECOND_NAME = new String[] { "NCAA FOOTBALL - (2H)" };
		NBA_LINES_SPORT = new String[] { "NBA BASKETBALL LINES" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "NBA BASKETBALL LINES" };
		NBA_FIRST_NAME = new String[] { "NBA - 1ST HALVES" };
		NBA_SECOND_SPORT = new String[] { "NBA BASKETBALL LINES" };
		NBA_SECOND_NAME = new String[] { "NBA - 2ND HALVES" };
		NCAAB_LINES_SPORT = new String[] { "NCAA BASKETBALL LINES" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL - MEN" };
		NCAAB_FIRST_SPORT = new String[] { "NCAA BASKETBALL HALFTIMES" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BASKETBALL - (1H)" };
		NCAAB_SECOND_SPORT = new String[] { "NCAA BASKETBALL HALFTIMES" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BASKETBALL - (2H)" };
		NHL_LINES_SPORT = new String[] { "HOCKEY GAME LINES AND PROPS" };
		NHL_LINES_NAME = new String[] { "NATIONAL HOCKEY LEAGUE" };
		NHL_FIRST_SPORT = new String[] { "HOCKEY GAME LINES AND PROPS" };
		NHL_FIRST_NAME = new String[] { "NHL - PERIOD LINES" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY GAME LINES AND PROPS" };
		NHL_SECOND_NAME = new String[] { "NHL - PERIOD LINES" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "Basketball" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_FIRST_SPORT = new String[] { "Basketball" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		WNBA_SECOND_SPORT = new String[] { "Basketball" };

		LOGGER.info("Exiting BetitTDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
				{ "http://www.betit.info/", "SF408", "GG987", "250", "250", "250", "Chicago", "ET"}
			};
/*
			final BetitTDSportsProcessSite processSite = new BetitTDSportsProcessSite(TDSITES[0][0], TDSITES[0][1], TDSITES[0][2], false, true);
		    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = TDSITES[0][7];

			processSite.loginToSite(processSite.httpClientWrapper.getUsername(), processSite.httpClientWrapper.getPassword());
			final Set<PendingEvent> pendingEvents = processSite.getPendingBets(TDSITES[0][1], TDSITES[0][2]);
			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				Iterator<PendingEvent> itr = pendingEvents.iterator();
				
				while (itr.hasNext()) {
					System.out.println("PendingEvent: " + itr.next());
				}
			} else {
				System.out.println("PendingEvents is null or empty");
			}
*/

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final BetitTDSportsProcessSite processSite = new BetitTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
				final BetitTDSportsProcessSite processSite2 = new BetitTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];

			    processSite2.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite2.processTransaction = false;
			    processSite2.timezone = TDSITES[0][7];

				// Now call the test suite
			    BetitTDSportsProcessSite.testSite2(processSite, processSite2, i, TDSITES);

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
		this.siteParser = TDP = BSP;
		this.siteParser.setTimezone(timezone);
		BSP.setTimezone(timezone);

		this.httpClientWrapper.setHost("http://www.betit.info");
		MAP_DATA = new HashMap<String, String>();
		MAP_DATA.put("account", username);
		MAP_DATA.put("password", password);
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", "http://www.betit.info"));
		List<NameValuePair> retValue = httpClientWrapper.postSitePage("http://www.betit.info/core/login.aspx", null, postValuePairs, headerValuePairs);
		String xhtml = getCookiesAndXhtml(retValue);
		LOGGER.debug("HTML: " + xhtml);

		// Parse login
		MAP_DATA = TDP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Setup the webapp name
		httpClientWrapper.setWebappname("core/wager");

		headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", "http://www.betit.info/core/wager/Message.aspx"));
		retValue = httpClientWrapper.getSitePage(populateUrl("Welcome.aspx#!"), null, headerValuePairs);
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("HTML: " + xhtml);

		headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", "http://www.betit.info/core/wager/Welcome.aspx"));
		retValue = httpClientWrapper.getSitePage(populateUrl("CreateSports.aspx?WT=0#!"), null, headerValuePairs);
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("HTML: " + xhtml);

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}