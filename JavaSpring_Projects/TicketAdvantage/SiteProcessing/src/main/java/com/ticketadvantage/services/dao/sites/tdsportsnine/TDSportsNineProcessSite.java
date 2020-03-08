/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsportsnine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsProcessSite;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class TDSportsNineProcessSite extends TDSportsProcessSite {
	private static final Logger LOGGER = Logger.getLogger(TDSportsNineProcessSite.class);
	protected TDSportsNineParser NSP = new TDSportsNineParser();

	/**
	 * 
	 */
	public TDSportsNineProcessSite(String host, String username, String password, boolean isMobile,
			boolean showRequestResponse) {
		super("TDSportsNine", host, username, password, isMobile, showRequestResponse);
		LOGGER.info("Entering TDSportsNineProcessSite()");

		// Setup the parser
		this.siteParser = NSP;

		// Menu items
		NFL_LINES_SPORT = new String[] { "FOOTBALL GAME LINES", "NFL FOOTBALL" };
		NFL_LINES_NAME = new String[] { "NFL", "NFL GAME LINES", "NFL PRESEASON" };
		NFL_FIRST_SPORT = new String[] { "FOOTBALL HALFTIMES", "NFL FOOTBALL" };
		NFL_FIRST_NAME = new String[] { "NFL 1ST HALF LINES", "NFL FIRST HALF LINES", "1H NFL PRESEASON" };
		NFL_SECOND_SPORT = new String[] { "FOOTBALL HALFTIMES", "NFL FOOTBALL" };
		NFL_SECOND_NAME = new String[] { "NFL 2ND HALF LINES", "NFL SECOND HALF LINES" };
		NCAAF_LINES_SPORT = new String[] { "FOOTBALL GAME LINES", "COLLEGE FOOTBALL" };
		NCAAF_LINES_NAME = new String[] { "NCAA FOOTBALL", "CFB GAME LINES" };
		NCAAF_FIRST_SPORT = new String[] { "FOOTBALL HALFTIMES", "COLLEGE FOOTBALL" };
		NCAAF_FIRST_NAME = new String[] { "CFB 1ST HALF LINES", "NCAA FOOTBALL - (1H)" };
		NCAAF_SECOND_SPORT = new String[] { "FOOTBALL HALFTIMES", "COLLEGE FOOTBALL" };
		NCAAF_SECOND_NAME = new String[] { "CFB 2ND HALF LINES", "NCAA FOOTBALL - (2H)" };
		NBA_LINES_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NBA_LINES_NAME = new String[] { "NBA" };
		NBA_FIRST_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NBA_FIRST_NAME = new String[] { "NBA 1H" };
		NBA_SECOND_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NBA_SECOND_NAME = new String[] { "NBA 2H" };
		NCAAB_LINES_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_LINES_NAME = new String[] { "NCAA BASKETBALL - MEN" };
		NCAAB_FIRST_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_FIRST_NAME = new String[] { "NCAA BASKETBALL - (1H)" };
		NCAAB_SECOND_SPORT = new String[] { "BASKETBALL GAME LINES" };
		NCAAB_SECOND_NAME = new String[] { "NCAA BASKETBALL - (2H)" };
		NHL_LINES_SPORT = new String[] { "HOCKEY" };
		NHL_LINES_NAME = new String[] { "NHL GAME LINES" };
		NHL_FIRST_SPORT = new String[] { "HOCKEY" };
		NHL_FIRST_NAME = new String[] { "NHL - PERIOD LINES" };
		NHL_SECOND_SPORT = new String[] { "HOCKEY" };
		NHL_SECOND_NAME = new String[] { "NHL - PERIOD LINES" };
		WNBA_LINES_SPORT = new String[] { "Basketball", "WNBA" };
		WNBA_LINES_NAME = new String[] { "WNBA", "WNBA GAME LINES" };
		WNBA_FIRST_SPORT = new String[] { "Basketball", "WNBA" };
		WNBA_FIRST_NAME = new String[] { "WNBA 1st Half", "WNBA 1ST HALFS" };
		WNBA_SECOND_SPORT = new String[] { "Basketball", "WNBA" };
		WNBA_SECOND_NAME = new String[] { "WNBA 2nd Half" };
		MLB_LINES_SPORT = new String[] { "BASEBALL GAME LINES", "MLB" };
		MLB_LINES_NAME = new String[] { "MLB - GAME LINES", "MAJOR LEAGUE BASEBALL", "MLB - EXHIBITION GAME LINES" };
		MLB_FIRST_SPORT = new String[] { "BASEBALL GAME LINES", "MLB" };
		MLB_FIRST_NAME = new String[] { "MLB 1H (5 FULL INNINGS)", "MLB 1ST 5 FULL INNINGS" };
		MLB_SECOND_SPORT = new String[] { "BASEBALL GAME LINES", "MLB" };
		MLB_SECOND_NAME = new String[] { "MLB 2H (4 FULL INNINGS+EXTRA INNS)", "MLB - 2ND HALVES" };

		LOGGER.info("Exiting TDSportsNineProcessSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String[][] TDSITES = new String [][] { 
//				{ "http://names777.com/", "Rc1525", "John", "100", "100", "100", "Chicago", "ET"},
//				{ "http://Betcatalinasports.com/", "catj776", "pink", "100", "100", "100", "Chicago", "ET"}
//				{ "http://anysport247.com", "BTB180", "der", "0", "0", "0", "New York", "ET"}
//				{ "http://1betvegas.com", "HHK199", "lamp", "0", "0", "0", "New York", "ET"}
//				{ "http://888east.com/", "JJ103", "JJ103", "100", "100", "100", "New York", "ET"}
//				{ "http://Betcatalinasports.com/", "catj776", "pink", "100", "100", "100", "Chicago", "ET"}
//				{ "http://everygame247.com", "MI231", "MUSKEGON231", "0", "0", "0", "None", "ET"}
				{ "http://anysport247.com", "ktm01", "nick", "0", "0", "0", "None", "ET"}
			};

			// Loop through the sites
			for (int i = 0; i < TDSITES.length; i++) {
				final TDSportsNineProcessSite processSite = new TDSportsNineProcessSite(TDSITES[i][0], TDSITES[i][1],
						TDSITES[i][2], false, true);
			    processSite.httpClientWrapper.setupHttpClient(TDSITES[0][6]);
			    processSite.processTransaction = false;
			    processSite.timezone = TDSITES[0][7];
			    processSite.siteParser = processSite.TDP = processSite.NSP;
			    processSite.siteParser.setTimezone(TDSITES[0][7]);
			    processSite.NSP.setTimezone(TDSITES[0][7]);
			    processSite.testSpread(processSite, TDSITES);

/*
				Set<PendingEvent> pendingEvents = processSite.getPendingBets(TDSITES[0][1], TDSITES[0][2], null);
				if (pendingEvents != null && !pendingEvents.isEmpty()) {
					Iterator<PendingEvent> itr = pendingEvents.iterator();
					while (itr.hasNext()) {
						System.out.println("PendingEvent: " + itr.next());
					}
				}
*/
			}
		} catch (Throwable t) {
			LOGGER.info("Throwable: ", t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ticketadvantage.services.dao.sites.SiteProcessor#loginToSite(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public String loginToSite(String username, String password) throws BatchException {
		LOGGER.info("Entering loginToSite()");
		LOGGER.debug("username: " + username);
		LOGGER.debug("password: " + password);

		// Setup the timezone
		this.siteParser = TDP = NSP;
		this.siteParser.setTimezone(timezone);
		NSP.setTimezone(timezone);

		this.httpClientWrapper.setHost(this.httpClientWrapper.getHost());
		String xhtml = getSite(this.httpClientWrapper.getHost());
		MAP_DATA = NSP.parseIndex(xhtml);
		MAP_DATA.remove("username");
		MAP_DATA.remove("password");
		MAP_DATA.put("ctl00$MainContent$ctlLogin$_UserName", username);
		MAP_DATA.put("ctl00$MainContent$ctlLogin$_Password", password.toLowerCase());
//		MAP_DATA.put("ctl00$MainContent$ctlLogin$_Password", password.toUpperCase());
		MAP_DATA.put("ctl00$MainContent$ctlLogin$BtnSubmit.x", "27");
		MAP_DATA.put("ctl00$MainContent$ctlLogin$BtnSubmit.y", "7");
		final List<NameValuePair> postValuePairs = new ArrayList<NameValuePair>(1);
		setupNameValuesEmpty(postValuePairs, MAP_DATA, "");

		xhtml = postSite(this.httpClientWrapper.getHost() + "/default.aspx", postValuePairs);
		LOGGER.debug("HTML: " + xhtml);

		// Parse login
		MAP_DATA = NSP.parseLogin(xhtml);
		LOGGER.debug("MAP_DATA: " + MAP_DATA);

		// Setup the webapp name
		httpClientWrapper.setWebappname("wager");

		// welcome.aspx
		xhtml = getSite(populateUrl("Welcome.aspx"));
		LOGGER.debug("HTML: " + xhtml);

		// CreateSports.aspx
		xhtml = getSite(populateUrl("CreateSports.aspx?WT=0"));
		LOGGER.debug("HTML: " + xhtml);

		LOGGER.info("Exiting loginToSite()");
		return xhtml;
	}
}