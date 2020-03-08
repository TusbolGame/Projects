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
import com.ticketadvantage.services.model.PreviewInput;
import com.ticketadvantage.services.model.PreviewOutput;

/**
 * @author jmiller
 *
 */
public class LvActionTDSportsProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(LvActionTDSportsProcessSite.class);
	protected LvActionTDSportsParser TDP = new LvActionTDSportsParser();

	/**
	 * 
	 */
	public LvActionTDSportsProcessSite(String host, String username, String password, boolean isMobile, boolean showRequestResponse) {
		super("LvActionTDSports", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering LvActionTDSportsProcessSite()");

		// Setup the parser
		this.siteParser = TDP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "NFL - WEEK", "NFL - PRESEASON", "NFL" };
		NFL_LINES_NAME = new String[] { "NFL GM: SPREADS - MONEY LN - TOTALS", "NFL (PRESEASON)" };
		NFL_FIRST_SPORT = new String[] { "NFL - WEEK", "NFL - PRESEASON", "NFL" };
		NFL_FIRST_NAME = new String[] { "NFL 1H: SPREADS - TOTALS", "NFL (PRESEASON 1H)" };
		NFL_SECOND_SPORT = new String[] { "NFL - WEEK", "NFL - PRESEASON", "NFL" };
		NFL_SECOND_NAME = new String[] { "NFL 2H: SPREADS - TOTALS" };
		NCAAF_LINES_SPORT = new String[] { "COLLEGE FOOTBALL" };
		NCAAF_LINES_NAME = new String[] { "NCAAF GM: SPREADS - MONEY LN - TOTALS" };
		NCAAF_FIRST_SPORT = new String[] { "COLLEGE FOOTBALL" };
		NCAAF_FIRST_NAME = new String[] { "NCAAF 1H: SPREADS - TOTALS" };
		NCAAF_SECOND_SPORT = new String[] { "COLLEGE FOOTBALL" };
		NCAAF_SECOND_NAME = new String[] { "NCAAF 2H: SPREADS - TOTALS" };
		NBA_LINES_SPORT = new String[] { "NBA" };
		NBA_LINES_NAME = new String[] { "NBA GM: SPREADS - MONEY LN - TOTALS" };
		NBA_FIRST_SPORT = new String[] { "NBA" };
		NBA_FIRST_NAME = new String[] { "NBA 1H: SPREADS - TOTALS" };
		NBA_SECOND_SPORT = new String[] { "NBA" };
		NBA_SECOND_NAME = new String[] { "NBA 2H: SPREADS - TOTALS" };
		NCAAB_LINES_SPORT = new String[] { "COLLEGE BASKETBALL" };
		NCAAB_LINES_NAME = new String[] { "NCAAB GM: SPREADS - MONEY LN - TOTALS" };
		NCAAB_FIRST_SPORT = new String[] { "COLLEGE BASKETBALL" };
		NCAAB_FIRST_NAME = new String[] { "NCAAB 1H: SPREADS - TOTALS" };
		NCAAB_SECOND_SPORT = new String[] { "COLLEGE BASKETBALL" };
		NCAAB_SECOND_NAME = new String[] { "NCAAB 2H: SPREADS - TOTALS" };
		NHL_LINES_SPORT = new String[] { "NHL" };
		NHL_LINES_NAME = new String[] { "NHL GM - MONEY LN - TOTALS - PUCK LN" };
		NHL_FIRST_SPORT = new String[] { "NHL" };
		NHL_FIRST_NAME = new String[] { " NHL PERIODS: MONEY LN - TOTALS" };
		NHL_SECOND_SPORT = new String[] { "NHL" };
		NHL_SECOND_NAME = new String[] { " NHL PERIODS: MONEY LN - TOTALS" };
		NHL_THIRD_SPORT = new String[] { "NHL" };
		NHL_THIRD_NAME = new String[] { " NHL PERIODS: MONEY LN - TOTALS" };
		WNBA_LINES_SPORT = new String[] { "Basketball" };
		WNBA_LINES_NAME = new String[] { "WNBA" };
		WNBA_FIRST_SPORT = new String[] { "Basketball" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half" };
		WNBA_SECOND_SPORT = new String[] { "Basketball" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		MLB_LINES_SPORT = new String[] { "MLB" };
		MLB_LINES_NAME = new String[] { "MLB GM: MONEY LN - TOTALS - RUN LN" };
		MLB_FIRST_SPORT = new String[] { "MLB" };
		MLB_FIRST_NAME = new String[] { "MLB 1ST 5 INN: MONEY LN - TOTALS - RUN LN" };
		MLB_SECOND_SPORT = new String[] { "MLB" };
		MLB_SECOND_NAME = new String[] { "MLB 2H: MONEY LN - TOTALS - RUN LN" };

		LOGGER.info("Exiting LvActionTDSportsProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
//				{ "http://classic.lvaction.com", "OB151", "189", "0", "0", "0", "Los Angeles", "ET"}
//				{ "http://classic.lvaction.com", "UG719", "tiger1", "0", "0", "0", "None", "ET"}
				{ "http://backend.lvaction.com", "ID255", "frank1", "0", "0", "0", "None", "ET"}
			};
			final LvActionTDSportsProcessSite processSite = new LvActionTDSportsProcessSite(TDSITES[0][0], TDSITES[0][1],
					TDSITES[0][2], false, true);
		    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
		    processSite.processTransaction = false;
		    processSite.timezone = TDSITES[0][7];

		    String xhtml = processSite.loginToSite(TDSITES[0][1], TDSITES[0][2]);

			final Set<PendingEvent> pendingEvents = processSite.getPendingBets(TDSITES[0][1], TDSITES[0][2], null);
			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				Iterator<PendingEvent> itr = pendingEvents.iterator();
				while (itr.hasNext()) {
					System.out.println("PendingEvent: " + itr.next());
				}
			}
/*
			final PreviewInput previewInput = new PreviewInput();
			previewInput.setLinetype("ml");
			previewInput.setLineindicator("-");
			previewInput.setRotationid(new Integer(970));
			previewInput.setSporttype("mlblines");
			previewInput.setProxyname("Los Angeles");
			previewInput.setTimezone("ET");

			final PreviewOutput previewData = processSite.previewEvent(previewInput);
			LOGGER.error("PreviewData: " + previewData);
*/
			// Now call the test suite
//			LvActionTDSportsProcessSite.testSite2(processSite, processSite, 0, TDSITES);
		    
/*
			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final LvActionTDSportsProcessSite processSite = new LvActionTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2]);
				final LvActionTDSportsProcessSite processSite2 = new LvActionTDSportsProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2]);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];

			    processSite2.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite2.processTransaction = false;
			    processSite2.timezone = TDSITES[0][7];

				// Now call the test suite
				LvActionTDSportsProcessSite.testSite2(processSite, processSite2, i, TDSITES);
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

		LOGGER.debug("HTTP GET REQUEST: " + httpClientWrapper.getHost());
		String xhtml = getSite(httpClientWrapper.getHost());
		LOGGER.debug("HTML: " + xhtml);
		MAP_DATA = TDP.parseIndex(xhtml);

		// Get home page data
		MAP_DATA.put("ctl00$MainContent$ctlLogin$_UserName", username);
		MAP_DATA.put("ctl00$MainContent$ctlLogin$_Password", password);

		List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		String actionLogin = setupNameValuesEmpty(postValuePairs, MAP_DATA, "");
		LOGGER.debug("ActionLogin: " + actionLogin);

		// Call the login
		LOGGER.debug("HTTP POST REQUEST: " + actionLogin);
		xhtml = authenticate(actionLogin, postValuePairs);
		LOGGER.debug("XHTML: " + xhtml);

		// Setup the webapp name
		httpClientWrapper.setWebappname("wager");

		// Get the main menu page
		LOGGER.debug("HTTP GET REQUEST: " + populateUrl("CreateSports.aspx?WT=0"));
		xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));
		LOGGER.debug("XHTML: " + xhtml);

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}