/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.util.ArrayList;
import java.util.HashMap;
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
public class SbPlayTDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(SbPlayTDSportsProcessSite.class);
	protected YoPigTDSportsParser TDP = new YoPigTDSportsParser();

	/**
	 * 
	 */
	public SbPlayTDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("SbPlayTDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering SbPlayTDSportsProcessSite()");

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
		NBA_LINES_SPORT = new String[] { "NBA BASKETBALL" };
		NBA_LINES_NAME = new String[] { "NBA - GAME LINES" };
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
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_LINES_SPORT = new String[] { "Basketball" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_FIRST_SPORT = new String[] { "Basketball" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		WNBA_SECOND_SPORT = new String[] { "Basketball" };

		LOGGER.info("Exiting SbPlayTDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
				{ "http://sbplay.com", "JJE141", "degen", "2000", "2000", "2000", "New York City", "ET"}
			};
			
			final SbPlayTDSportsProcessSite processSite = new SbPlayTDSportsProcessSite("http://sbplay.com", "JJE141", "degen", false, true);
		    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = TDSITES[0][7];

			processSite.loginToSite(processSite.httpClientWrapper.getUsername(), processSite.httpClientWrapper.getPassword());
			final Set<PendingEvent> pendingEvents = processSite.getPendingBets("JJE141", "degen", null);
			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				Iterator<PendingEvent> itr = pendingEvents.iterator();
				
				while (itr.hasNext()) {
					System.out.println("PendingEvent: " + itr.next());
				}
			} else {
				System.out.println("PendingEvents is null or empty");
			}

/*
			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final SbPlayTDSportsProcessSite processSite = new SbPlayTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
				final SbPlayTDSportsProcessSite processSite2 = new SbPlayTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];

			    processSite2.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite2.processTransaction = false;
			    processSite2.timezone = TDSITES[0][7];

				// Now call the test suite
				SbPlayTDSportsProcessSite.testSite2(processSite, processSite2, i, TDSITES);

			}
*/
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

		this.httpClientWrapper.setHost("http://sbplay.com");
		MAP_DATA = new HashMap<String, String>();
		MAP_DATA.put("Account", username);
		MAP_DATA.put("password", password);
		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", "http://sbplay.com/"));
		List<NameValuePair> retValue = httpClientWrapper.postSitePage("http://sbplay.com/core/engine/", null, postValuePairs, headerValuePairs);
		String xhtml = getCookiesAndXhtml(retValue);
		LOGGER.debug("HTML: " + xhtml);

		// Setup the webapp name
		httpClientWrapper.setWebappname("core/engine/wager");

		// Parse login
		MAP_DATA = TDP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		headerValuePairs = new ArrayList<NameValuePair>(1);
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", "http://sbplay.com/core/engine/wager/Message.aspx"));
		retValue = httpClientWrapper.getSitePage(populateUrl("Welcome.aspx"), null, headerValuePairs);
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		LOGGER.debug("HTML: " + xhtml);

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}